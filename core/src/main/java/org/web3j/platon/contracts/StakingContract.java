package org.web3j.platon.contracts;

import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.exceptions.MessageDecodingException;
import org.web3j.platon.*;
import org.web3j.platon.bean.Node;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.platon.bean.StakingParam;
import org.web3j.platon.bean.UpdateStakingParam;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.tx.PlatOnContract;
import org.web3j.tx.gas.GasProvider;
import org.web3j.utils.JSONUtil;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.functions.Func1;

public class StakingContract extends PlatOnContract {

    public static StakingContract load(Web3j web3j) {
        return new StakingContract(ContractAddress.STAKING_CONTRACT_ADDRESS, web3j);
    }

    public static StakingContract load(Web3j web3j, Credentials credentials, long chainId) {
        return new StakingContract(ContractAddress.STAKING_CONTRACT_ADDRESS, chainId, web3j, credentials);
    }

   //查询操作
    private StakingContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    //sendRawTransaction，使用默认gasProvider
    private StakingContract(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    //查询当前结算周期的区块奖励
    @SuppressWarnings("unchecked")
    public RemoteCall<BaseResponse<BigInteger>> getPackageReward() {
        PlatOnFunction function = new PlatOnFunction(FunctionType.GET_PACKAGEREWARD_FUNC_TYPE);
        return new RemoteCall<BaseResponse<BigInteger>>(new Callable<BaseResponse<BigInteger>>() {
            @Override
            public BaseResponse<BigInteger> call() throws Exception {
                BaseResponse baseResponse = executePatonCall(function);
                baseResponse.data = Numeric.decodeQuantity((String) baseResponse.data);
                return baseResponse;
            }
        });
    }

    //查询当前结算周期的质押奖励
    @SuppressWarnings("unchecked")
    public RemoteCall<BaseResponse<BigInteger>> getStakingReward() {
        PlatOnFunction function = new PlatOnFunction(FunctionType.GET_STAKINGREWARD_FUNC_TYPE);
        return new RemoteCall<BaseResponse<BigInteger>>(new Callable<BaseResponse<BigInteger>>() {
            @Override
            public BaseResponse<BigInteger> call() throws Exception {
                BaseResponse baseResponse = executePatonCall(function);
                baseResponse.data = Numeric.decodeQuantity((String) baseResponse.data);
                return baseResponse;
            }
        });
    }

  //查询打包区块的平均时间
    @SuppressWarnings("unchecked")
    public RemoteCall<BaseResponse<BigInteger>> getAvgPackTime() {
        PlatOnFunction function = new PlatOnFunction(FunctionType.GET_AVGPACKTIME_FUNC_TYPE);
        return new RemoteCall<BaseResponse<BigInteger>>(new Callable<BaseResponse<BigInteger>>() {
            @Override
            public BaseResponse<BigInteger> call() throws Exception {
                return executePatonCall(function);
            }
        });
    }


   //发起质押
    public RemoteCall<BaseResponse> staking(StakingParam stakingParam) throws Exception {
        StakingParam tempStakingParam = stakingParam.clone();
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.STAKING_FUNC_TYPE,
                tempStakingParam.getSubmitInputParameters());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    //发起质押
    public RemoteCall<BaseResponse> staking(StakingParam stakingParam, GasProvider gasProvider) throws Exception {
        StakingParam tempStakingParam = stakingParam.clone();
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.STAKING_FUNC_TYPE,
                tempStakingParam.getSubmitInputParameters(), gasProvider);
        return executeRemoteCallTransactionWithFunctionType(function);
    }

   //获取质押gasProvider
    public GasProvider getStakingGasProvider(StakingParam stakingParam) {
        return new PlatOnFunction(
                FunctionType.STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters()).getGasProvider();
    }


  //获取质押gasProvider
    public Observable<BigInteger> getFeeAmount(BigInteger gasPrice, StakingParam stakingParam) {
        StakingParam tempStakingParam = stakingParam.clone();
        return Observable.fromCallable(new Callable<ProgramVersion>() {
            @Override
            public ProgramVersion call() throws Exception {
                return getProgramVersion();
            }
        }).map(new Func1<ProgramVersion, BigInteger>() {
            @Override
            public BigInteger call(ProgramVersion programVersion) {
                tempStakingParam.setProcessVersion(programVersion);
                PlatOnFunction platOnFunction = new PlatOnFunction(
                        FunctionType.STAKING_FUNC_TYPE,
                        tempStakingParam.getSubmitInputParameters());
                return platOnFunction.getGasLimit().add(gasPrice == null || gasPrice.compareTo(BigInteger.ZERO) != 1 ? platOnFunction.getGasPrice() : gasPrice);
            }
        });
    }


  //发起质押
    public RemoteCall<PlatonSendTransaction> stakingReturnTransaction(StakingParam stakingParam) throws Exception {
        StakingParam tempStakingParam = stakingParam.clone();
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters());
        return executeRemoteCallPlatonTransaction(function);
    }

    //发起质押
    public RemoteCall<PlatonSendTransaction> stakingReturnTransaction(StakingParam stakingParam, GasProvider gasProvider) throws Exception {
        StakingParam tempStakingParam = stakingParam.clone();
        final PlatOnFunction function = new PlatOnFunction(
                FunctionType.STAKING_FUNC_TYPE,
                stakingParam.getSubmitInputParameters(), gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

    public String getAdminSchnorrNIZKProve() throws Exception {
        return web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve();
    }

   //获取质押结果
    public RemoteCall<BaseResponse> getStakingResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.STAKING_FUNC_TYPE);
    }

   //异步获取质押结果
    public void asyncStaking(StakingParam stakingParam, TransactionCallback<BaseResponse> transactionCallback) throws Exception {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = stakingReturnTransaction(stakingParam);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getStakingResult(ethSendTransaction).sendAsync().get();
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


   //异步获取质押结果
    public void asyncStaking(StakingParam stakingParam, GasProvider gasProvider, TransactionCallback<BaseResponse> transactionCallback) throws Exception {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = stakingReturnTransaction(stakingParam, gasProvider);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getStakingResult(ethSendTransaction).sendAsync().get();
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

    //撤销质押
    //nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
    public RemoteCall<BaseResponse> unStaking(String nodeId) {
        final PlatOnFunction function = new PlatOnFunction(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    //撤销质押
    //nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
    public RemoteCall<BaseResponse> unStaking(String nodeId, GasProvider gasProvider) {
        final PlatOnFunction function = new PlatOnFunction(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(nodeId))), gasProvider);
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    //获取撤销质押的gasProvider
    public Observable<GasProvider> getUnStakingGasProvider(String nodeId) {
        return Observable.fromCallable(new Callable<GasProvider>() {
            @Override
            public GasProvider call() throws Exception {
                return new PlatOnFunction(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                        Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(nodeId)))).getGasProvider();
            }
        });
    }

    //撤销质押
    //nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
    public RemoteCall<PlatonSendTransaction> unStakingReturnTransaction(String nodeId) {
        final PlatOnFunction function = new PlatOnFunction(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return executeRemoteCallPlatonTransaction(function);
    }

    //撤销质押
    //nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
    public RemoteCall<PlatonSendTransaction> unStakingReturnTransaction(String nodeId, GasProvider gasProvider) {
        final PlatOnFunction function = new PlatOnFunction(FunctionType.WITHDREW_STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new BytesType(Numeric.hexStringToByteArray(nodeId))), gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

   //获取质押结果
    public RemoteCall<BaseResponse> getUnStakingResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.WITHDREW_STAKING_FUNC_TYPE);
    }

    //异步撤销质押
    //nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
    @SuppressWarnings("unchecked")
    public void asyncUnStaking(String nodeId, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = unStakingReturnTransaction(nodeId);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getUnStakingResult(ethSendTransaction).sendAsync().get();
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


    //异步撤销质押
    //nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
    @SuppressWarnings("unchecked")
    public void asyncUnStaking(String nodeId, GasProvider gasProvider, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = unStakingReturnTransaction(nodeId, gasProvider);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getUnStakingResult(ethSendTransaction).sendAsync().get();
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

   //更新质押信息
    public RemoteCall<BaseResponse> updateStakingInfo(UpdateStakingParam updateStakingParam) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                updateStakingParam.getSubmitInputParameters());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    //更新质押信息
    public RemoteCall<BaseResponse> updateStakingInfo(UpdateStakingParam updateStakingParam, GasProvider gasProvider) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                updateStakingParam.getSubmitInputParameters(), gasProvider);
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    //获取更新质押信息gasProvider
    public Observable<GasProvider> getUpdateStakingInfoGasProvider(UpdateStakingParam updateStakingParam) {
        return Observable.fromCallable(new Callable<GasProvider>() {
            @Override
            public GasProvider call() throws Exception {
                return new PlatOnFunction(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                        updateStakingParam.getSubmitInputParameters()).getGasProvider();
            }
        });
    }

   //更新质押信息
    public RemoteCall<PlatonSendTransaction> updateStakingInfoReturnTransaction(UpdateStakingParam updateStakingParam) {

        PlatOnFunction function = new PlatOnFunction(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                updateStakingParam.getSubmitInputParameters());
        return executeRemoteCallPlatonTransaction(function);
    }

    //更新质押信息
    public RemoteCall<PlatonSendTransaction> updateStakingInfoReturnTransaction(UpdateStakingParam updateStakingParam, GasProvider gasProvider) {

        PlatOnFunction function = new PlatOnFunction(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                updateStakingParam.getSubmitInputParameters(), gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

  //获取更新质押结果
    public RemoteCall<BaseResponse> getUpdateStakingInfoResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE);
    }

   //异步更新质押信息
    @SuppressWarnings("unchecked")
    public void asyncUpdateStakingInfo(UpdateStakingParam updateStakingParam, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = updateStakingInfoReturnTransaction(updateStakingParam);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getUpdateStakingInfoResult(ethSendTransaction).sendAsync().get();
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

   //异步更新质押信息
    @SuppressWarnings("unchecked")
    public void asyncUpdateStakingInfo(UpdateStakingParam updateStakingParam, GasProvider gasProvider, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = updateStakingInfoReturnTransaction(updateStakingParam, gasProvider);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getUpdateStakingInfoResult(ethSendTransaction).sendAsync().get();
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

    //增持质押
    //nodeId            被质押的节点Id(也叫候选人的节点Id)
    //stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
    //amount            增持的von
    public RemoteCall<BaseResponse> addStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount)));
        return executeRemoteCallTransactionWithFunctionType(function);
    }


    //增持质押
    //nodeId            被质押的节点Id(也叫候选人的节点Id)
    //stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
    //amount            增持的von
    public RemoteCall<BaseResponse> addStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount)));
        return executeRemoteCallTransactionWithFunctionType(function);
    }

  //获取增持质押gasProvider
    public Observable<GasProvider> getAddStakingGasProvider(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        return Observable.fromCallable(new Callable<GasProvider>() {
            @Override
            public GasProvider call() throws Exception {
                return new PlatOnFunction(FunctionType.ADD_STAKING_FUNC_TYPE,
                        Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                                new Uint16(stakingAmountType.getValue()),
                                new Uint256(amount))).getGasProvider();
            }
        });
    }

    //增持质押
    //nodeId            被质押的节点Id(也叫候选人的节点Id)
    //stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
    //amount            增持的von
    public RemoteCall<PlatonSendTransaction> addStakingReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount)));
        return executeRemoteCallPlatonTransaction(function);
    }

    //增持质押
    //nodeId            被质押的节点Id(也叫候选人的节点Id)
    //stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
    //amount            增持的von
    public RemoteCall<PlatonSendTransaction> addStakingReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId)),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount)), gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

    //获取增持质押的结果
    public RemoteCall<BaseResponse> getAddStakingResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.ADD_STAKING_FUNC_TYPE);
    }

    //增持质押
    //nodeId            被质押的节点Id(也叫候选人的节点Id)
    //stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
    //amount            增持的von
    @SuppressWarnings("unchecked")
    public void asyncAddStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, TransactionCallback transactionCallback) {
        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = addStakingReturnTransaction(nodeId, stakingAmountType, amount);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getAddStakingResult(ethSendTransaction).sendAsync().get();
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


    @SuppressWarnings("unchecked")
    public void asyncAddStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, GasProvider gasProvider, TransactionCallback transactionCallback) {
        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = addStakingReturnTransaction(nodeId, stakingAmountType, amount, gasProvider);

        try {
            PlatonSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getAddStakingResult(ethSendTransaction).sendAsync().get();
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

   //获取质押信息
    @SuppressWarnings("unchecked")
    public RemoteCall<BaseResponse<Node>> getStakingInfo(String nodeId) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.GET_STAKINGINFO_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(nodeId))));
        return new RemoteCall<BaseResponse<Node>>(new Callable<BaseResponse<Node>>() {
            @Override
            public BaseResponse<Node> call() throws Exception {
                BaseResponse response = executePatonCall(function);
                if(response.code == ErrorCode.SUCCESS){
                    String strJson = JSONUtil.toJSONString(response.data);
                    response.data = JSONUtil.parseObject(strJson, Node.class);
                    response.errMsg = ErrorCode.getErrorMsg(response.code);
                }
                return response;
            }
        });
    }

}
