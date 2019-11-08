package org.web3j.protocol.core.methods.response;

import com.alibaba.fastjson.JSON;

import org.web3j.platon.bean.EconomicConfig;
import org.web3j.protocol.core.Response;

/**
 * platon_evidences.
 */
public class DebugEconomicConfig extends Response<String> {

    public String getEconomicConfigStr() {
        return getResult();
    }
    
    public EconomicConfig getEconomicConfig() {
        return JSON.parseObject(getResult(), EconomicConfig.class);
    }
}
