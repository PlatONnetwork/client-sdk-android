package com.alaya.platon;

import com.alaya.abi.datatypes.BytesType;
import com.alaya.abi.datatypes.generated.Uint16;
import com.alaya.abi.datatypes.generated.Uint256;
import com.alaya.abi.datatypes.generated.Uint64;
import com.alaya.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

public class PlatOnFunctionFactory {

    private PlatOnFunctionFactory() {

    }

    /**
     * 创建
     * @return
     */
    public static PlatOnFunction createTransferFunction() {
        return new PlatOnFunction(FunctionType.TRANSFER);
    }

    /**
     * 创建委托实例
     *
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
     *
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
