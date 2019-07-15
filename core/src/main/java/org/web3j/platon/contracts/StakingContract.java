package org.web3j.platon.contracts;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.FunctionType;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.TransactionCallback;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.tx.PlatOnContract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class StakingContract extends PlatOnContract {

    public static StakingContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new StakingContract("", STAKING_CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }

    public static StakingContract load(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new StakingContract("", STAKING_CONTRACT_ADDRESS, web3j, transactionManager, contractGasProvider);
    }

    public static StakingContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String chainId) {
        return new StakingContract("", STAKING_CONTRACT_ADDRESS, chainId, web3j, credentials, contractGasProvider);
    }

    protected StakingContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public StakingContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public StakingContract(String contractBinary, String contractAddress, String chainId, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, chainId, web3j, credentials, gasProvider);
    }

    /**
     * @param nodeId            64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @param amount            质押的von
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param benifitAddress    20bytes 用于接受出块奖励和质押奖励的收益账户
     * @param externalId        外部Id(有长度限制，给第三方拉取节点描述的Id)
     * @param nodeName          被质押节点的名称(有长度限制，表示该节点的名称)
     * @param webSite           节点的第三方主页(有长度限制，表示该节点的主页)
     * @param details           节点的描述(有长度限制，表示该节点的描述)
     * @return
     */
    public RemoteCall<BaseResponse> staking(String nodeId, BigInteger amount, StakingAmountType stakingAmountType, String benifitAddress, String externalId, String nodeName, String webSite, String details) {
        final Function function = new Function(
                FunctionType.STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new Uint16(stakingAmountType.getValue())
                        , new Utf8String(benifitAddress)
                        , new Utf8String(nodeId)
                        , new Utf8String(externalId)
                        , new Utf8String(nodeName)
                        , new Utf8String(webSite)
                        , new Utf8String(details)
                        , new Int256(amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransactionWithFunctionType(function, amount);
    }


    /**
     * @param nodeId            64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @param amount            质押的von
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param benifitAddress    20bytes 用于接受出块奖励和质押奖励的收益账户
     * @param externalId        外部Id(有长度限制，给第三方拉取节点描述的Id)
     * @param nodeName          被质押节点的名称(有长度限制，表示该节点的名称)
     * @param webSite           节点的第三方主页(有长度限制，表示该节点的主页)
     * @param details           节点的描述(有长度限制，表示该节点的描述)
     * @return
     */
    public RemoteCall<PlatonSendTransaction> stakingReturnTransaction(String nodeId, BigInteger amount, StakingAmountType stakingAmountType, String benifitAddress, String externalId, String nodeName, String webSite, String details) {
        final Function function = new Function(
                FunctionType.STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new Uint16(stakingAmountType.getValue())
                        , new Utf8String(benifitAddress)
                        , new Utf8String(nodeId)
                        , new Utf8String(externalId)
                        , new Utf8String(nodeName)
                        , new Utf8String(webSite)
                        , new Utf8String(details)
                        , new Int256(amount)
                        , new Uint32(1000)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallPlatonTransaction(function, amount);
    }

    /**
     * 获取质押结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getStakingResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.STAKING_FUNC_TYPE);
    }

    /**
     * 异步获取质押结果
     *
     * @param nodeId              64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @param amount              质押的von
     * @param stakingAmountType   表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param benifitAddress      20bytes 用于接受出块奖励和质押奖励的收益账户
     * @param externalId          外部Id(有长度限制，给第三方拉取节点描述的Id)
     * @param nodeName            被质押节点的名称(有长度限制，表示该节点的名称)
     * @param webSite             节点的第三方主页(有长度限制，表示该节点的主页)
     * @param details             节点的描述(有长度限制，表示该节点的描述)
     * @param transactionCallback 回调
     */
    public void asyncStaking(String nodeId, BigInteger amount, StakingAmountType stakingAmountType, String benifitAddress, String externalId, String nodeName, String webSite, String details, TransactionCallback<BaseResponse> transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = stakingReturnTransaction(nodeId, amount, stakingAmountType, benifitAddress, externalId, nodeName, webSite, details);

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

    /**
     * 撤销质押
     *
     * @param nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
    public RemoteCall<BaseResponse> unStaking(String nodeId) {
        final Function function = new Function(FunctionType.STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 撤销质押
     *
     * @param nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
    public RemoteCall<PlatonSendTransaction> unStakingReturnTransaction(String nodeId) {
        final Function function = new Function(FunctionType.STAKING_FUNC_TYPE,
                Arrays.<Type>asList(new Utf8String(nodeId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取质押结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getUnStakingResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.WITHDREW_STAKING_FUNC_TYPE);
    }

    /**
     * 异步撤销质押
     *
     * @param nodeId 64bytes 被质押的节点Id(也叫候选人的节点Id)
     * @return
     */
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

    /**
     * 更新质押信息
     *
     * @param nodeId
     * @param stakingAmount
     * @param benifitAddress
     * @param externalId
     * @param nodeName
     * @param webSite
     * @param details
     * @return
     */
    public RemoteCall<BaseResponse> updateStakingInfo(String nodeId, BigInteger stakingAmount, String benifitAddress, String externalId, String nodeName, String webSite, String details) {
        Function function = new Function(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                Arrays.asList(new Utf8String(benifitAddress),
                        new Utf8String(nodeId),
                        new Utf8String(externalId),
                        new Utf8String(nodeName),
                        new Utf8String(webSite),
                        new Utf8String(details)),
                Collections.emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 更新质押信息
     *
     * @param nodeId         被质押的节点Id(也叫候选人的节点Id)
     * @param benifitAddress 用于接受出块奖励和质押奖励的收益账户
     * @param externalId     外部Id(有长度限制，给第三方拉取节点描述的Id)
     * @param nodeName       被质押节点的名称(有长度限制，表示该节点的名称)
     * @param webSite        节点的第三方主页(有长度限制，表示该节点的主页)
     * @param details        节点的第三方主页(有长度限制，表示该节点的主页)
     * @return
     */
    public RemoteCall<PlatonSendTransaction> updateStakingInfoReturnTransaction(String nodeId, String benifitAddress, String externalId, String nodeName, String webSite, String details) {

        Function function = new Function(FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE,
                Arrays.asList(new Utf8String(benifitAddress),
                        new Utf8String(nodeId),
                        new Utf8String(externalId),
                        new Utf8String(nodeName),
                        new Utf8String(webSite),
                        new Utf8String(details)),
                Collections.emptyList());
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取更新质押结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getUpdateStakingInfoResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.UPDATE_STAKING_INFO_FUNC_TYPE);
    }

    /**
     * 异步更新质押信息
     *
     * @param nodeId              被质押的节点Id(也叫候选人的节点Id)
     * @param benifitAddress      用于接受出块奖励和质押奖励的收益账户
     * @param externalId          外部Id(有长度限制，给第三方拉取节点描述的Id)
     * @param nodeName            被质押节点的名称(有长度限制，表示该节点的名称)
     * @param webSite             节点的第三方主页(有长度限制，表示该节点的主页)
     * @param details             节点的第三方主页(有长度限制，表示该节点的主页)
     * @param transactionCallback
     */
    public void asyncUpdateStakingInfo(String nodeId, String benifitAddress, String externalId, String nodeName, String webSite, String details, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<PlatonSendTransaction> ethSendTransactionRemoteCall = updateStakingInfoReturnTransaction(nodeId, benifitAddress, externalId, nodeName, webSite, details);

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

    /**
     * 增持质押
     *
     * @param nodeId            被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount            增持的von
     * @return
     */
    public RemoteCall<BaseResponse> addStaking(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = new Function(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new Utf8String(nodeId),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount))
                , Collections.emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 增持质押
     *
     * @param nodeId            被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType 表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount            增持的von
     * @return
     */
    public RemoteCall<PlatonSendTransaction> addStakingReturnTransaction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        Function function = new Function(FunctionType.ADD_STAKING_FUNC_TYPE,
                Arrays.asList(new Utf8String(nodeId),
                        new Uint16(stakingAmountType.getValue()),
                        new Uint256(amount))
                , Collections.emptyList());
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取增持质押的结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getAddStakingResult(PlatonSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.ADD_STAKING_FUNC_TYPE);
    }

    /**
     * @param nodeId              被质押的节点Id(也叫候选人的节点Id)
     * @param stakingAmountType   表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     * @param amount              增持的von
     * @param transactionCallback
     */
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

    /**
     * 获取质押信息
     *
     * @param nodeId
     * @return
     */
    public RemoteCall<BaseResponse> getStakingInfo(String nodeId) {
        Function function = new Function(FunctionType.GET_STAKINGINFO_FUNC_TYPE,
                Arrays.asList(new Utf8String(nodeId))
                , Collections.emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

}
