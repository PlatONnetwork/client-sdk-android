package com.alaya.platon.contracts;

import com.alaya.abi.datatypes.BytesType;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.utils.Numeric;
import com.alaya.platon.BaseResponse;
import com.alaya.platon.ContractAddress;
import com.alaya.platon.CustomStaticArray;
import com.alaya.platon.FunctionType;
import com.alaya.platon.PlatOnFunction;
import com.alaya.platon.bean.Reward;
import com.alaya.tx.PlatOnContract;
import com.alaya.tx.gas.GasProvider;
import com.alaya.utils.JSONUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class RewardContract extends PlatOnContract {

    /**
     * 查询操作
     *
     * @param web3j
     * @return
     */
    public static RewardContract load(Web3j web3j) {
        return new RewardContract(ContractAddress.REWARD_CONTRACT_ADDRESS, web3j);
    }

    /**
     * sendRawTransaction 使用默认的gasProvider
     *
     * @param web3j
     * @param credentials
     * @param chainId
     * @return
     */
    public static RewardContract load(Web3j web3j, Credentials credentials, long chainId) {
        return new RewardContract(ContractAddress.REWARD_CONTRACT_ADDRESS, chainId, web3j, credentials);
    }

    private RewardContract(String contractAddress, long chainId, Web3j web3j, Credentials credentials) {
        super(contractAddress, chainId, web3j, credentials);
    }

    private RewardContract(String contractAddress, Web3j web3j) {
        super(contractAddress, web3j);
    }

    /**
     * 提取账户当前所有的可提取的委托奖励
     *
     * @param gasProvider 用户指定的gasProvider
     * @return
     */
    public RemoteCall<PlatonSendTransaction> withdrawDelegateRewardReturnTransaction(GasProvider gasProvider) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.WITHDRAW_DELEGATE_REWARD_FUNC_TYPE, gasProvider);
        return executeRemoteCallPlatonTransaction(function);
    }

    /**
     * 查询账户在各节点未提取委托奖励
     *
     * @param nodeIDs
     * @return
     */
    public RemoteCall<BaseResponse<List<Reward>>> getDelegateReward(String address, List<String> nodeIDs) {
        PlatOnFunction function = new PlatOnFunction(FunctionType.GET_DELEGATE_REWARD_FUNC_TYPE,
                Arrays.asList(new BytesType(Numeric.hexStringToByteArray(address)), new CustomStaticArray(nodeIDs)));
        return new RemoteCall<BaseResponse<List<Reward>>>(new Callable<BaseResponse<List<Reward>>>() {
            @Override
            public BaseResponse<List<Reward>> call() throws Exception {
                BaseResponse response = executePatonCall(function);
                response.data = JSONUtil.parseArray(JSONUtil.toJSONString(response.data), Reward.class);
                return response;
            }
        });
    }
}
