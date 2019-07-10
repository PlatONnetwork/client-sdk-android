package org.web3j.platon.contracts;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.FunctionType;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.TransactionCallback;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.tx.PlatOnContract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class DelegateContract extends PlatOnContract {

    public static DelegateContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new DelegateContract("", STAKING_CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }

    public static DelegateContract load(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new DelegateContract("", STAKING_CONTRACT_ADDRESS, web3j, transactionManager, contractGasProvider);
    }

    public static DelegateContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String chainId) {
        return new DelegateContract("", NODE_CONTRACT_ADDRESS, chainId, web3j, credentials, contractGasProvider);
    }

    public DelegateContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public DelegateContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public DelegateContract(String contractBinary, String contractAddress, String chainId, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, chainId, web3j, credentials, gasProvider);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<BaseResponse> delegate(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = new Function(FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint16(stakingAmountType.getValue())
                        , new Utf8String(nodeId)
                        , new Uint256(amount)), Collections.EMPTY_LIST);
        return executeRemoteCallTransactionWithFunctionType(function, amount);
    }

    /**
     * 发起委托
     *
     * @param nodeId            被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount            委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<EthSendTransaction> delegateReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = new Function(FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint16(stakingAmountType.getValue())
                        , new Utf8String(nodeId)
                        , new Uint256(amount)), Collections.EMPTY_LIST);
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取委托结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getDelegateResult(EthSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.DELEGATE_FUNC_TYPE);
    }

    /**
     * 发起委托
     *
     * @param nodeId 被质押的节点的NodeId
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做委托，0: 自由金额； 1: 锁仓金额
     * @param amount 委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @param transactionCallback
     */
    public void asyncDelegate(String nodeId, StakingAmountType stakingAmountType, BigInteger amount, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<EthSendTransaction> ethSendTransactionRemoteCall = delegateReturnTransaction(nodeId, stakingAmountType, amount);

        try {
            EthSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getDelegateResult(ethSendTransaction).sendAsync().get();
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
     * 减持/撤销委托(全部减持就是撤销)
     *
     * @param nodeId          被质押的节点的NodeId
     * @param stakingBlockNum 代表着某个node的某次质押的唯一标示
     * @param amount          减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<BaseResponse> unDelegate(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
        Function function = new Function(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new Utf8String(nodeId)
                        , new Uint256(amount)), Collections.EMPTY_LIST);
        return executeRemoteCallTransactionWithFunctionType(function, amount);
    }

    /**
     * 减持/撤销委托(全部减持就是撤销)
     *
     * @param nodeId          被质押的节点的NodeId
     * @param stakingBlockNum 代表着某个node的某次质押的唯一标示
     * @param amount          减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * @return
     */
    public RemoteCall<EthSendTransaction> unDelegateReturnTransaction(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
        Function function = new Function(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new Utf8String(nodeId)
                        , new Uint256(amount)), Collections.EMPTY_LIST);
        return executeRemoteCallPlatonTransaction(function, amount);
    }

    /**
     * 获取减持/撤销委托(全部减持就是撤销)的结果
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getUnDelegateResult(EthSendTransaction ethSendTransaction){

        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.WITHDREW_DELEGATE_FUNC_TYPE);
    }

    /**
     * 异步获取撤销委托的结果
     * @param nodeId
     * @param stakingBlockNum
     * @param amount
     * @param transactionCallback
     */
    public void asyncUnDelegate(String nodeId, BigInteger stakingBlockNum, BigInteger amount,TransactionCallback transactionCallback){

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<EthSendTransaction> ethSendTransactionRemoteCall = unDelegateReturnTransaction(nodeId, stakingBlockNum, amount);

        try {
            EthSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getUnDelegateResult(ethSendTransaction).sendAsync().get();
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

}
