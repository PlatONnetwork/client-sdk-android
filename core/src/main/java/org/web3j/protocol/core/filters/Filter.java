package org.web3j.protocol.core.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.protocol.Web3j;

import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.PlatonFilter;
import org.web3j.protocol.core.methods.response.PlatonLog;
import org.web3j.protocol.core.methods.response.PlatonUninstallFilter;


/**
 * Class for creating managed filter requests with callbacks.
 */
public abstract class Filter<T> {

    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    final Web3j web3j;
    final Callback<T> callback;

    private volatile BigInteger filterId;

    private ScheduledFuture<?> schedule;

    public Filter(Web3j web3j, Callback<T> callback) {
        this.web3j = web3j;
        this.callback = callback;
    }

    public void run(ScheduledExecutorService scheduledExecutorService, long blockTime) {
        try {
            final PlatonFilter ethFilter = sendRequest();
            if (ethFilter.hasError()) {
                throwException(ethFilter.getError());
            }

            filterId = ethFilter.getFilterId();

            scheduledExecutorService.submit(new Runnable() {
                @Override
                public void run() {
                    Filter.this.getInitialFilterLogs();
                }
            });

            /*
            We want the filter to be resilient against client issues. On numerous occasions
            users have reported socket timeout exceptions when connected over HTTP to Geth and
            Parity clients. For examples, refer to
            https://github.com/web3j/web3j/issues/144 and
            https://github.com/ethereum/go-ethereum/issues/15243.

            Hence we consume errors and log them as errors, allowing our polling for changes to
            resume. The downside of this approach is that users will not be notified of
            downstream connection issues. But given the intermittent nature of the connection
            issues, this seems like a reasonable compromise.

            The alternative approach would be to have another thread that blocks waiting on
            schedule.get(), catching any Exceptions thrown, and passing them back up to the
            caller. However, the user would then be required to recreate subscriptions manually
            which isn't ideal given the aforementioned issues.
            */
            schedule = scheduledExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Filter.this.pollFilter(ethFilter);
                            } catch (Throwable e) {
                                // All exceptions must be caught, otherwise our job terminates without
                                // any notification
                                log.error("Error sending request", e);
                            }
                        }
                    },
                    0, blockTime, TimeUnit.MILLISECONDS);
        } catch (IOException e) {
            throwException(e);
        }
    }

    private void getInitialFilterLogs() {
        try {
            Request<?, PlatonLog> request = this.getFilterLogs(this.filterId);
            PlatonLog ethLog = null;
            if (request != null) {
                ethLog = request.send();
            } else {
                ethLog = new PlatonLog();
                ethLog.setResult(Collections.<PlatonLog.LogResult>emptyList());
            }
            process(ethLog.getLogs());

        } catch (IOException e) {
            throwException(e);
        }
    }

    private void pollFilter(PlatonFilter ethFilter) {
        PlatonLog ethLog = null;
        try {
            ethLog = web3j.platonGetFilterChanges(filterId).send();
        } catch (IOException e) {
            throwException(e);
        }
        if (ethLog.hasError()) {
            throwException(ethLog.getError());
        } else {
            process(ethLog.getLogs());
        }
    }

    abstract PlatonFilter sendRequest() throws IOException;

    abstract void process(List<PlatonLog.LogResult> logResults);

    public void cancel() {
        schedule.cancel(false);

        try {
            PlatonUninstallFilter ethUninstallFilter = web3j.platonUninstallFilter(filterId).send();
            if (ethUninstallFilter.hasError()) {
                throwException(ethUninstallFilter.getError());
            }

            if (!ethUninstallFilter.isUninstalled()) {
                throw new FilterException("Filter with id '" + filterId + "' failed to uninstall");
            }
        } catch (IOException e) {
            throwException(e);
        }
    }

    /**
     * Retrieves historic filters for the filter with the given id.
     * Getting historic logs is not supported by all filters.
     * If not the method should return an empty EthLog object
     *
     * @param filterId Id of the filter for which the historic log should be retrieved
     * @return Historic logs, or an empty optional if the filter cannot retrieve historic logs
     */
    protected abstract Request<?, PlatonLog> getFilterLogs(BigInteger filterId);

    void throwException(Response.Error error) {
        throw new FilterException("Invalid request: "
                + (error == null ? "Unknown Error" : error.getMessage()));
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}

