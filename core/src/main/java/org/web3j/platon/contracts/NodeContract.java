package org.web3j.platon.contracts;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.FunctionType;
import org.web3j.platon.bean.Node;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tx.PlatOnContract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.util.Arrays;
import java.util.List;

public class NodeContract extends PlatOnContract {

    public static NodeContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new NodeContract("", STAKING_CONTRACT_ADDRESS, web3j, credentials, contractGasProvider);
    }

    public static NodeContract load(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new NodeContract("", STAKING_CONTRACT_ADDRESS, web3j, transactionManager, contractGasProvider);
    }

    public static NodeContract load(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String chainId) {
        return new NodeContract("", NODE_CONTRACT_ADDRESS, chainId, web3j, credentials, contractGasProvider);
    }

    protected NodeContract(String contractBinary, String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, transactionManager, gasProvider);
    }

    public NodeContract(String contractBinary, String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, web3j, credentials, gasProvider);
    }

    public NodeContract(String contractBinary, String contractAddress, String chainId, Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        super(contractBinary, contractAddress, chainId, web3j, credentials, gasProvider);
    }

    /**
     * 查询当前结算周期的验证人队列
     *
     * @return
     */
    public RemoteCall<BaseResponse> getVerifierList() {
        final Function function = new Function(FunctionType.GET_VERIFIERLIST_FUNC_TYPE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executePlatonRemoteCallSingleValueReturn(function);
    }

    /**
     * 查询当前共识周期的验证人列表
     *
     * @return
     */
    public RemoteCall<BaseResponse> getValidatorList() {
        final Function function = new Function(FunctionType.GET_VALIDATORLIST_FUNC_TYPE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executePlatonRemoteCallSingleValueReturn(function);
    }

    /**
     * 查询所有实时的候选人列表
     *
     * @return
     */
    public RemoteCall<BaseResponse> getCandidateList() {
        final Function function = new Function(FunctionType.GET_CANDIDATELIST_FUNC_TYPE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {
                }));
        return executePlatonRemoteCallSingleValueReturn(function);
    }
}
