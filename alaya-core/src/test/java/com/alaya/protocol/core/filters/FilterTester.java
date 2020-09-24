package com.alaya.protocol.core.filters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

import com.alaya.protocol.ObjectMapperFactory;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.Web3jFactory;
import com.alaya.protocol.Web3jService;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.methods.response.PlatonFilter;
import com.alaya.protocol.core.methods.response.PlatonLog;
import com.alaya.protocol.core.methods.response.PlatonUninstallFilter;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class FilterTester {

    private Web3jService web3jService;
    Web3j web3j;

    final ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
    final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    @Before
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3jFactory.build(web3jService, 1000, scheduledExecutorService);
    }

    @SuppressWarnings("unchecked")
    <T> void runTest(PlatonLog ethLog, Observable<T> observable) throws Exception {
        PlatonFilter ethFilter = objectMapper.readValue(
                "{\n"
                        + "  \"id\":1,\n"
                        + "  \"jsonrpc\": \"2.0\",\n"
                        + "  \"result\": \"0x1\"\n"
                        + "}", PlatonFilter.class);

        PlatonUninstallFilter ethUninstallFilter = objectMapper.readValue(
                "{\"jsonrpc\":\"2.0\",\"id\":1,\"result\":true}", PlatonUninstallFilter.class);

        final List<T> expected = createExpected(ethLog);
        final Set<T> results = Collections.synchronizedSet(new HashSet<T>());

        final CountDownLatch transactionLatch = new CountDownLatch(expected.size());

        final CountDownLatch completedLatch = new CountDownLatch(1);

        when(web3jService.send(any(Request.class), eq(PlatonFilter.class)))
                .thenReturn(ethFilter);
        when(web3jService.send(any(Request.class), eq(PlatonLog.class)))
                .thenReturn(ethLog);
        when(web3jService.send(any(Request.class), eq(PlatonUninstallFilter.class)))
                .thenReturn(ethUninstallFilter);

        Subscription subscription = observable.subscribe(
                new Action1<T>() {
                    @Override
                    public void call(T result) {
                        results.add(result);
                        transactionLatch.countDown();
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        fail(throwable.getMessage());
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        completedLatch.countDown();
                    }
                });

        transactionLatch.await(1, TimeUnit.SECONDS);
        assertThat(results, CoreMatchers.<Set<T>>equalTo(new HashSet<T>(expected)));

        subscription.unsubscribe();

        completedLatch.await(1, TimeUnit.SECONDS);
        assertTrue(subscription.isUnsubscribed());
    }

    @SuppressWarnings("unchecked")
    List createExpected(PlatonLog ethLog) {
        List<PlatonLog.LogResult> logResults = ethLog.getLogs();
        if (logResults.isEmpty()) {
            fail("Results cannot be empty");
        }

        List expected = new ArrayList();
        for (PlatonLog.LogResult logResult : ethLog.getLogs()) {
            expected.add(logResult.get());
        }
        return expected;
    }
}
