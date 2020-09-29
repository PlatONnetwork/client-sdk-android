package org.web3j.platon.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class ParamValue {


    @JSONField(name = "StaleValue")
    private String staleValue;

    @JSONField(name = "Value")
    private String value;

    @JSONField(name = "ActiveBlock")
    private String activeBlock;

    public ParamValue() {
    }

    public String getStaleValue() {
        return staleValue;
    }

    public void setStaleValue(String staleValue) {
        this.staleValue = staleValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getActiveBlock() {
        return activeBlock;
    }

    public void setActiveBlock(String activeBlock) {
        this.activeBlock = activeBlock;
    }
}
