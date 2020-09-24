package com.alaya.platon.contracts;

import com.alaya.abi.datatypes.BytesType;
import com.alaya.abi.datatypes.Type;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.addressconvert.bech32.Bech32Util;
import com.alaya.platon.*;
import com.alaya.platon.bean.RestrictingItem;
import com.alaya.platon.bean.RestrictingPlan;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.tx.PlatOnContract;
import com.alaya.tx.gas.GasProvider;
import com.alaya.utils.JSONUtil;
import com.alaya.utils.Numeric;
import rx.Observable;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class RestrictingPlanContract extends PlatOnContract {

    /**
     * 查询操作
     *
     * @param web3j
     * @return
     */
    public static RestrictingPlanContract load(Web3j web3j) {
        return new RestrictingPlanContract(ContractAddress.RESTRICTING_PLAN_CONTRACT_ADDRESS, web3j);
    }

    /**
     * sendRawTransaction 使用用户自定义的gasProvider，必须传chainId
     *
     * @param web3j
     * @param credentials
     * @param chainId
     * @return
     */
    public static RestrictingPlanContract load(Web3j web3j, Credentials credentials, long chainId) {
        return new RestrictingPlanContract(ContractAddress.RESTRICTING_PLAN_CONTRACT_ADDRESS, chainId, web3j, credentials);
    }

    private RestrictingPlanContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    private RestrictingPlanContract(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    /**
     * 创建锁仓计划
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return
     */
    public RemoteCall<BaseResponse> createRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList) {
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Bech32Util.addressDecode(account)), new CustomStaticArray(restrictingPlanList)));
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 创建锁仓计划
     *
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider
     * @return
     */
    public RemoteCall<BaseResponse> createRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList, GasProvider gasProvider) {
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray(restrictingPlanList)), gasProvider);
        return executeRemoteCallTransactionWithFunctionType(function);
    }


    /**
     * 获取创建锁仓计划的gasProvider
     *
     * @param account
     * @param restrictingPlanList
     * @return
     */
    public Observable<GasProvider> getCreateRestrictingPlanGasProvider(String account, List<RestrictingPlan> restrictingPlanList) {
        return Observable.fromCallable(new Callable<GasProvider>() {
            @Override
            public GasProvider call() throws Exception {
                return new PlatOnFunction(
                        FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                        Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray(restrictingPlanList))).getGasProvider();
            }
        });
    }


    /**
     * 获取创建锁仓计划的gasProvider
     *
     * @param account
     * @param restrictingPlanList
     * @return
     */
    public Observable<BigInteger> getFeeAmount(BigInteger gasPrice, String account, List<RestrictingPlan> restrictingPlanList) {
        return Observable.fromCallable(new Callable<BigInteger>() {
            @Override
            public BigInteger call() throws Exception {
                PlatOnFunction platOnFunction = new PlatOnFunction(
                        FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                        Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray(restrictingPlanList)));
                return platOnFunction.getGasLimit().add(gasPrice == null || gasPrice.compareTo(BigInteger.ZERO) != 1 ? platOnFunction.getGasPrice() : gasPrice);
            }
        });
    }

    /**
     * 获取创建锁仓计划的gasProvider
     *
     * @param account
     * @param restrictingPlanList
     * @return
     */
    public Observable<GasProvider> getCreateRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList) {
        return Observable.fromCallable(new Callable<GasProvider>() {
            @Override
            public GasProvider call() throws Exception {
                return new PlatOnFunction(
                        FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                        Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray(restrictingPlanList))).getGasProvider();
            }
        });
    }

    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @return
     */
    public RemoteCall<PlatonSendTransaction> createRestrictingPlanReturnTransaction(String account, List<RestrictingPlan> restrictingPlanList) {
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray(restrictingPlanList)));
        return executeRemoteCallPlatonTransaction(function);
    }


    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider
     * @return
     */
    public RemoteCall<PlatonSendTransaction> createRestrictingPlanReturnTransaction(String account, List<RestrictingPlan> restrictingPlanList, GasProvider gasProvider) {
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(account)), new CustomStaticArray(restrictingPlanList)), gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getCreateRestrictingPlanResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.CREATE_RESTRICTINGPLAN_FUNC_TYPE);
    }

    /**
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param transactionCallback
     */
    public void asyncCreateRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = createRestrictingPlanReturnTransaction(account, restrictingPlanList);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getCreateRestrictingPlanResult(ethSendTransaction).sendAsync().get();
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
     * @param account             锁仓释放到账账户
     * @param restrictingPlanList 其中，Epoch：表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。
     *                            如果 account 是激励池地址，那么 period 值是 120（即，30*4） 的倍数。
     *                            另外，period * 每周期的区块数至少要大于最高不可逆区块高度。Amount：表示目标区块上待释放的金额。
     * @param gasProvider
     * @param transactionCallback
     */
    public void asyncCreateRestrictingPlan(String account, List<RestrictingPlan> restrictingPlanList,GasProvider gasProvider, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = createRestrictingPlanReturnTransaction(account, restrictingPlanList);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getCreateRestrictingPlanResult(ethSendTransaction).sendAsync().get();
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
     * 获取锁仓信息
     *
     * @param account 锁仓释放到账账户
     * @return
     */
    public RemoteCall<BaseResponse<RestrictingItem>> getRestrictingInfo(String account) {
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.GET_RESTRICTINGINFO_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Bech32Util.addressDecode(account))));
        return new RemoteCall<BaseResponse<RestrictingItem>>(new Callable<BaseResponse<RestrictingItem>>() {
            @Override
            public BaseResponse<RestrictingItem> call() throws Exception {
                BaseResponse response = executePatonCall(function);
                response.data = JSONUtil.parseObject(JSONUtil.toJSONString(response.data), RestrictingItem.class);
                return response;
            }
        });
    }

}
