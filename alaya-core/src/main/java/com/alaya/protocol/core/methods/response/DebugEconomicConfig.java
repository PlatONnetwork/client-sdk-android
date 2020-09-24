package com.alaya.protocol.core.methods.response;

import com.alaya.platon.bean.EconomicConfig;
import com.alaya.protocol.core.Response;
import com.alaya.utils.JSONUtil;

/**
 * platon_evidences.
 */
public class DebugEconomicConfig extends Response<String> {

    public EconomicConfig getEconomicConfig() {
        return JSONUtil.parseObject(getResult(), EconomicConfig.class);
    }
}
