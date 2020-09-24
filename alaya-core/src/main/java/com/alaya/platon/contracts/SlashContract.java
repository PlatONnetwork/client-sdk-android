package com.alaya.platon.contracts;

import com.alaya.abi.datatypes.BytesType;
import com.alaya.abi.datatypes.Utf8String;
import com.alaya.abi.datatypes.generated.Uint32;
import com.alaya.abi.datatypes.generated.Uint64;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.utils.Numeric;
import com.alaya.platon.BaseResponse;
import com.alaya.platon.ContractAddress;
import com.alaya.platon.DuplicateSignType;
import com.alaya.platon.FunctionType;
import com.alaya.platon.PlatOnFunction;
import com.alaya.platon.TransactionCallback;
import com.alaya.tx.PlatOnContract;
import com.alaya.tx.gas.GasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import rx.Observable;

public class SlashContract extends PlatOnContract {

    /**
     * 查询操作
     *
     * @param web3j
     * @return
     */
    public static SlashContract load(Web3j web3j) {
        return new SlashContract(ContractAddress.SLASH_CONTRACT_ADDRESS, web3j);
    }

    /**
     * sendRawTransaction 使用默认的gasProvider
     *
     * @param web3j
     * @param credentials
     * @param chainId
     * @return
     */
    public static SlashContract load(Web3j web3j, Credentials credentials, long chainId) {
        return new SlashContract(ContractAddress.SLASH_CONTRACT_ADDRESS, chainId, web3j, credentials);
    }

    private SlashContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private SlashContract(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    /**
     * 举报双签
     *
     * @param data 证据的json值
     * @return
     */
    public RemoteCall<BaseResponse> reportDoubleSign(String data) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Utf8String(data)));
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 举报双签
     *
     * @param data 证据的json值
     * @param gasProvider
     * @return
     */
    public RemoteCall<BaseResponse> reportDoubleSign(String data,GasProvider gasProvider) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Utf8String(data)),gasProvider);
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 获取举报双签的gasProvider
     * @param data
     * @return
     */
    public Observable<GasProvider> getReportDoubleSignGasProvider(String data) {
        return Observable.fromCallable(new Callable<GasProvider>() {
            @Override
            public GasProvider call() throws Exception {
                return new PlatOnFunction(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                        Arrays.asList(new Utf8String(data))).getGasProvider();
            }
        });
    }

    /**
     * 获取手续费
     * @param gasPrice
     * @param data
     * @return
     */
    public Observable<BigInteger> getFeeAmount(BigInteger gasPrice, String data) {
        return Observable.fromCallable(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                PlatOnFunction platOnFunction = new PlatOnFunction(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                        Arrays.asList(new Utf8String(data)));
                return platOnFunction.getGasLimit().multiply(gasPrice == null || gasPrice.compareTo(BigInteger.ZERO) != 1 ? platOnFunction.getGasPrice() : gasPrice);
            }
        });
    }

    /**
     * 举报双签
     *
     * @param data 证据的json值
     * @return
     */
    public RemoteCall<PlatonSendTransaction> reportDoubleSignReturnTransaction(String data) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Utf8String(data)));
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 举报双签
     *
     * @param data 证据的json值
     * @param gasProvider
     * @return
     */
    public RemoteCall<PlatonSendTransaction> reportDoubleSignReturnTransaction(String data,GasProvider gasProvider) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Utf8String(data)),gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getReportDoubleSignResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.REPORT_DOUBLESIGN_FUNC_TYPE);
    }

    /**
     * 异步举报双签
     *
     * @param data                证据的json值
     * @param transactionCallback
     */
    public void asyncReportDoubleSignResult(String data, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = reportDoubleSignReturnTransaction(data);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getReportDoubleSignResult(ethSendTransaction).sendAsync().get();
            if (transactionCallback != null) {
                if (baseResponse.isStatusOk()) {
                    transactionCallback.onTransactionSucceed(baseResponse);
                } else {
                    transactionCallback.onTransactionFailed(baseResponse);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (transactionCallback != null) {
                transactionCallback.onTransactionFailed(new BaseResponse(e));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            if (transactionCallback != null) {
                transactionCallback.onTransactionFailed(new BaseResponse(e));
            }
        }
    }

    /**
     * 异步举报双签
     *
     * @param data                证据的json值
     * @param gasProvider
     * @param transactionCallback
     */
    public void asyncReportDoubleSignResult(String data, GasProvider gasProvider,TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = reportDoubleSignReturnTransaction(data,gasProvider);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getReportDoubleSignResult(ethSendTransaction).sendAsync().get();
            if (transactionCallback != null) {
                if (baseResponse.isStatusOk()) {
                    transactionCallback.onTransactionSucceed(baseResponse);
                } else {
                    transactionCallback.onTransactionFailed(baseResponse);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            if (transactionCallback != null) {
                transactionCallback.onTransactionFailed(new BaseResponse(e));
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            if (transactionCallback != null) {
                transactionCallback.onTransactionFailed(new BaseResponse(e));
            }
        }
    }

    /**
     * 查询节点是否已被举报过多签
     *
     * @param doubleSignType 代表双签类型，1：prepare，2：viewChange
     * @param nodeId        举报的节点id
     * @param blockNumber    多签的块高
     * @return
     */
    public RemoteCall<BaseResponse<String>> checkDoubleSign(DuplicateSignType doubleSignType, String nodeId, BigInteger blockNumber) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.CHECK_DOUBLESIGN_FUNC_TYPE,
                Arrays.asList(new Uint32(doubleSignType.getValue())
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint64(blockNumber)));
        return new RemoteCall<BaseResponse<String>>(new Callable<BaseResponse<String>>() {
            @Override
            public BaseResponse<String> call() throws Exception {
                return executePatonCall(function);
            }
        });
    }

}
