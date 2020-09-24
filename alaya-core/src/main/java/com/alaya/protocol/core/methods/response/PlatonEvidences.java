package com.alaya.protocol.core.methods.response;

import com.alaya.protocol.core.Response;

/**
 * platon_evidences.
 */
public class PlatonEvidences extends Response<String> {

    public String getEvidences() {
        return getResult();
    }
}
