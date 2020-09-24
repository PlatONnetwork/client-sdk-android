package com.alaya.platon;

import java.util.HashMap;
import java.util.Map;

public enum StakingAmountType {

    FREE_AMOUNT_TYPE(0), RESTRICTING_AMOUNT_TYPE(1);

    int value;

    StakingAmountType(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }

    static Map<Integer, StakingAmountType> map = new HashMap<>();

    static {
        for (StakingAmountType stakingAmountType : values()) {
            map.put(stakingAmountType.value, stakingAmountType);
        }
    }

    public static StakingAmountType getStakingAmountType(int value) {
        StakingAmountType stakingAmountType = map.get(value);
        return stakingAmountType == null ? FREE_AMOUNT_TYPE : stakingAmountType;
    }
}
