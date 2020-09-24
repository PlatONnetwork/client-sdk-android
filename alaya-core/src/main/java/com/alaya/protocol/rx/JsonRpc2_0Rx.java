package com.alaya.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import com.alaya.protocol.core.DefaultBlockParameter;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.protocol.core.DefaultBlockParameterNumber;
import com.alaya.protocol.core.filters.*;
import com.alaya.protocol.core.methods.request.PlatonFilter;
import com.alaya.protocol.core.methods.response.Log;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.alaya.protocol.core.methods.response.PlatonTransaction;
import com.alaya.protocol.core.methods.response.Transaction;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.filters.BlockFilter;
import com.alaya.protocol.core.filters.Callback;
import com.alaya.protocol.core.filters.LogFilter;
import com.alaya.protocol.core.filters.PendingTransactionFilter;
import com.alaya.utils.Observables;

/**
 * web3j reactive API implementation.
 */
public class JsonRpc2_0Rx {

    private final Web3j web3j;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Web3j web3j, ScheduledExecutorService scheduledExecutorService) {
        this.web3j = web3j;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Observable<String> ethBlockHashObservable(final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                BlockFilter blockFilter = new BlockFilter(
                        web3j, new Callback<String>() {
                            @Override
                            public void onEvent(final String value) {
                                subscriber.onNext(value);
                            }
                        });
                JsonRpc2_0Rx.this.run(blockFilter, subscriber, pollingInterval);
            }
        });
    }

    public Observable<String> ethPendingTransactionHashObservable(final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                        web3j, new Callback<String>() {
                            @Override
                            public void onEvent(final String value) {
                                subscriber.onNext(value);
                            }
                        });
                JsonRpc2_0Rx.this.run(pendingTransactionFilter, subscriber, pollingInterval);
            }
        });
    }

    public Observable<Log> ethLogObservable(
            final PlatonFilter ethFilter,
            final long pollingInterval) {
        return Observable.create(new Observable.OnSubscribe<Log>() {
            @Override
            public void call(final Subscriber<? super Log> subscriber) {
                LogFilter logFilter = new LogFilter(
                        web3j, new Callback<Log>() {
                            @Override
                            public void onEvent(final Log t) {
                                subscriber.onNext(t);
                            }
                        }, ethFilter);

                JsonRpc2_0Rx.this.run(logFilter, subscriber, pollingInterval);
            }
        });
    }

    private <T> void run(
            final Filter<T> filter,
            Subscriber<? super T> subscriber,
            final long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                filter.cancel();
            }
        }));
    }

    public Observable<Transaction>  transactionObservable(final long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(new Func1<PlatonBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(final PlatonBlock ethBlock) {
                        return JsonRpc2_0Rx.this.toTransactions(ethBlock);
                    }
                });
    }

    public Observable<Transaction> pendingTransactionObservable(final long pollingInterval) {
        return ethPendingTransactionHashObservable(pollingInterval)
                .flatMap(new Func1<String, Observable<PlatonTransaction>>() {
                    @Override
                    public Observable<PlatonTransaction> call(final String transactionHash) {
                        return web3j.platonGetTransactionByHash(transactionHash).observable();
                    }
                })
                .map(new Func1<PlatonTransaction, Transaction>() {
                    @Override
                    public Transaction call(final PlatonTransaction ethTransaction) {
                        return ethTransaction.getTransaction();
                    }
                });
    }

    public Observable<PlatonBlock> blockObservable(
            final boolean fullTransactionObjects, final long pollingInterval) {
        return this.ethBlockHashObservable(pollingInterval)
                .flatMap(new Func1<String, Observable<? extends PlatonBlock>>() {
                    @Override
                    public Observable<? extends PlatonBlock> call(final String blockHash) {
                        return web3j.platonGetBlockByHash(blockHash,
                                fullTransactionObjects).observable();
                    }
                });
    }

    public Observable<PlatonBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<PlatonBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Observable<PlatonBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            final boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<PlatonBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            final boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber)
                    .flatMap(new Func1<BigInteger, Observable<? extends PlatonBlock>>() {
                        @Override
                        public Observable<? extends PlatonBlock> call(BigInteger i) {
                            return web3j.platonGetBlockByNumber(
                                    new DefaultBlockParameterNumber(i),
                                    fullTransactionObjects).observable();
                        }
                    });
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(new Func1<BigInteger, Observable<? extends PlatonBlock>>() {
                        @Override
                        public Observable<? extends PlatonBlock> call(BigInteger i) {
                            return web3j.platonGetBlockByNumber(
                                    new DefaultBlockParameterNumber(i),
                                    fullTransactionObjects).observable();
                        }
                    });
        }


    }

    public Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true)
                .flatMapIterable(new Func1<PlatonBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(PlatonBlock ethBlock) {
                        return toTransactions(ethBlock);
                    }
                });
    }

    public Observable<PlatonBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<PlatonBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(
                startBlock, fullTransactionObjects, onCompleteObservable)
                .subscribeOn(scheduler);
    }

    public Observable<PlatonBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, Observable.<PlatonBlock>empty());
    }

    private Observable<PlatonBlock> catchUpToLatestBlockObservableSync(
            DefaultBlockParameter startBlock, final boolean fullTransactionObjects,
            final Observable<PlatonBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        final BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(
                    replayBlocksObservableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Observable.defer(new Func0<Observable<PlatonBlock>>() {
                        @Override
                        public Observable<PlatonBlock> call() {
                            return JsonRpc2_0Rx.this.catchUpToLatestBlockObservableSync(
                                    new DefaultBlockParameterNumber(
                                            latestBlockNumber.add(BigInteger.ONE)),
                                    fullTransactionObjects,
                                    onCompleteObservable);
                        }
                    }));
        }
    }

    public Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(
                startBlock, true, Observable.<PlatonBlock>empty())
                .flatMapIterable(new Func1<PlatonBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(PlatonBlock ethBlock) {
                        return toTransactions(ethBlock);
                    }
                });
    }

    public Observable<PlatonBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, true, pollingInterval)
                .flatMapIterable(new Func1<PlatonBlock, Iterable<? extends Transaction>>() {
                    @Override
                    public Iterable<? extends Transaction> call(PlatonBlock ethBlock) {
                        return toTransactions(ethBlock);
                    }
                });
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            PlatonBlock latestEthBlock = web3j.platonGetBlockByNumber(
                    defaultBlockParameter, false).send();
            return latestEthBlock.getBlock().getNumber();
        }
    }

    private static List<Transaction> toTransactions(PlatonBlock ethBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Geth/Parity. You should resync to solve.
        List<PlatonBlock.TransactionResult> transactionResults = ethBlock.getBlock().getTransactions();
        List<Transaction> transactions = new ArrayList<Transaction>(transactionResults.size());

        for (PlatonBlock.TransactionResult transactionResult : transactionResults) {
            transactions.add((Transaction) transactionResult.get());
        }
        return transactions;
    }
}
