package com.alaya.platon;

import com.alaya.abi.datatypes.Type;
import com.alaya.rlp.RlpType;

public abstract class CustomType implements Type {

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getTypeAsString() {
        return null;
    }

    public abstract RlpType getRlpEncodeData();
}
