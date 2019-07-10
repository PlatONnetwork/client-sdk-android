package org.web3j.platon.contracts;

import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.FunctionType;
import org.web3j.platon.TransactionCallback;
import org.web3j.platon.bean.Proposal;
import org.web3j.platon.VoteOption;
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

public class ProposalContract extends PlatOnContract {

    public static ProposalContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ProposalContract("", STAKING_CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }

    public static ProposalContract load(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ProposalContract("", STAKING_CONTRACT_ADDRESS, web3j, transactionManager, contractGasProvider);
    }

    public static ProposalContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String chainId) {
        return new ProposalContract("", NODE_CONTRACT_ADDRESS, chainId, web3j, credentials, contractGasProvider);
    }

    protected ProposalContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public ProposalContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public ProposalContract(String contractBinary, String contractAddress, String chainId, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, chainId, web3j, credentials, gasProvider);
    }

    /**
     * 查询提案
     *
     * @param proposalId
     * @return
     */
    public RemoteCall<BaseResponse> getProposal(String proposalId) {
        Function function = new Function(FunctionType.GET_PROPOSAL_FUNC_TYPE,
                Arrays.asList(new Utf8String(proposalId)), Collections.emptyList());
        return executeRemoteCallSingleValueReturn(function, BaseResponse.class);
    }

    /**
     * 查询提案结果
     *
     * @param proposalId
     * @return
     */
    public RemoteCall<BaseResponse> getTallyResult(String proposalId) {
        Function function = new Function(FunctionType.GET_TALLY_RESULT_FUNC_TYPE,
                Arrays.asList(new Utf8String(proposalId)), Collections.emptyList());
        return executeRemoteCallSingleValueReturn(function, BaseResponse.class);
    }

    /**
     * 获取提案列表
     *
     * @return
     */
    public RemoteCall<BaseResponse> getProposalList() {
        Function function = new Function(FunctionType.GET_PROPOSAL_LIST_FUNC_TYPE,
                Arrays.<Type>asList(), Collections.emptyList());
        return executeRemoteCallSingleValueReturn(function, BaseResponse.class);
    }

    /**
     * 给提案投票
     *
     * @param proposalID 提案ID
     * @param verifier   投票验证人
     * @param voteOption 投票选项
     * @return
     */
    public RemoteCall<BaseResponse> vote(String proposalID, String verifier, VoteOption voteOption) {
        Function function = new Function(FunctionType.VOTE_FUNC_TYPE,
                Arrays.<Type>asList(new Utf8String(verifier),
                        new Utf8String(proposalID), new Uint16(voteOption.getValue())),
                Collections.emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * @param proposalID 提案ID
     * @param verifier   投票验证人
     * @param voteOption 投票选项
     * @return
     */
    public RemoteCall<EthSendTransaction> voteReturnTransaction(String proposalID, String verifier, VoteOption voteOption) {
        Function function = new Function(FunctionType.VOTE_FUNC_TYPE,
                Arrays.<Type>asList(new Utf8String(verifier),
                        new Utf8String(proposalID), new Uint16(voteOption.getValue())),
                Collections.emptyList());
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取投票结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getVoteResult(EthSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.VOTE_FUNC_TYPE);
    }


    /**
     * @param proposalID          提案ID
     * @param verifier            投票验证人
     * @param voteOption          投票选项
     * @param transactionCallback
     */
    public void asyncVote(String proposalID, String verifier, VoteOption voteOption, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<EthSendTransaction> ethSendTransactionRemoteCall = voteReturnTransaction(proposalID, verifier, voteOption);

        try {
            EthSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getVoteResult(ethSendTransaction).sendAsync().get();
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
     * 版本声明
     *
     * @param activeNode 声明的节点，只能是验证人/候选人
     * @param version    声明的版本
     * @return
     */
    public RemoteCall<BaseResponse> declareVersion(String activeNode, BigInteger version) {
        Function function = new Function(FunctionType.DECLARE_VERSION_FUNC_TYPE,
                Arrays.<Type>asList(new Utf8String(activeNode),
                        new Uint16(version)),
                Collections.emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * @param activeNode 声明的节点，只能是验证人/候选人
     * @param version    声明的版本
     * @return
     */
    public RemoteCall<EthSendTransaction> declareVersionReturnTransaction(String activeNode, BigInteger version) {
        Function function = new Function(FunctionType.DECLARE_VERSION_FUNC_TYPE,
                Arrays.<Type>asList(new Utf8String(activeNode),
                        new Uint16(version)),
                Collections.emptyList());
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取版本声明的结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getDeclareVersionResult(EthSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.DELEGATE_FUNC_TYPE);
    }

    /**
     * 异步生命版本
     *
     * @param activeNode
     * @param version
     * @param transactionCallback
     */
    public void asyncDeclareVersion(String activeNode, BigInteger version, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<EthSendTransaction> ethSendTransactionRemoteCall = declareVersionReturnTransaction(activeNode, version);

        try {
            EthSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getVoteResult(ethSendTransaction).sendAsync().get();
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
     * 提交提案
     *
     * @param proposal 包括文本提案和版本提案
     * @return
     */
    public RemoteCall<BaseResponse> submitProposal(Proposal proposal) {
        if (proposal == null) {
            throw new NullPointerException("proposal must not be null");
        }
        Function function = new Function(proposal.getSubmitFunctionType(),
                proposal.getSubmitInputParameters(),
                Collections.emptyList());
        return executeRemoteCallTransactionWithFunctionType(function);
    }

    /**
     * 提交提案
     *
     * @param proposal
     * @return
     */
    public RemoteCall<EthSendTransaction> submitProposalReturnTransaction(Proposal proposal) {
        if (proposal == null) {
            throw new NullPointerException("proposal must not be null");
        }
        Function function = new Function(proposal.getSubmitFunctionType(),
                proposal.getSubmitInputParameters(),
                Collections.emptyList());
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 获取提交提案的结果
     *
     * @param ethSendTransaction
     * @return
     */
    public RemoteCall<BaseResponse> getSubmitProposalResult(EthSendTransaction ethSendTransaction) {
        return executeRemoteCallTransactionWithFunctionType(ethSendTransaction, FunctionType.SUBMIT_TEXT_FUNC_TYPE);
    }

    /**
     * 异步获取提案结果
     *
     * @param proposal
     * @param transactionCallback
     */
    public void asyncSubmitProposal(Proposal proposal, TransactionCallback transactionCallback) {

        if (transactionCallback != null) {
            transactionCallback.onTransactionStart();
        }

        RemoteCall<EthSendTransaction> ethSendTransactionRemoteCall = submitProposalReturnTransaction(proposal);

        try {
            EthSendTransaction ethSendTransaction = ethSendTransactionRemoteCall.sendAsync().get();
            if (transactionCallback != null) {
                transactionCallback.onTransaction(ethSendTransaction);
            }
            BaseResponse baseResponse = getSubmitProposalResult(ethSendTransaction).sendAsync().get();
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
