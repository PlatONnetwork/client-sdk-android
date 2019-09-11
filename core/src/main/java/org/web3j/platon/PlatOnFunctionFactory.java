package org.web3j.platon;

import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.generated.Uint16;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint64;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class PlatOnFunctionFactory {

    /**
     * 创建委托实例
     * @param nodeId
     * @param stakingAmountType
     * @param amount
     * @return
     */
    public static PlatOnFunction createDelegateFunction(String nodeId, StakingAmountType stakingAmountType, BigInteger amount) {
        return new PlatOnFunction(FunctionType.DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint16(stakingAmountType.getValue())
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount)));
    }

    /**
     * 创建解除委托实例
     * @param nodeId
     * @param stakingBlockNum
     * @param amount
     * @return
     */
    public static PlatOnFunction createUnDelegateFunction(String nodeId, BigInteger stakingBlockNum, BigInteger amount) {
        return new PlatOnFunction(FunctionType.WITHDREW_DELEGATE_FUNC_TYPE,
                Arrays.asList(new Uint64(stakingBlockNum)
                        , new BytesType(Numeric.hexStringToByteArray(nodeId))
                        , new Uint256(amount)));
    }

}
