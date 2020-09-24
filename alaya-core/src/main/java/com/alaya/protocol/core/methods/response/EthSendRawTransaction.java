package com.alaya.protocol.core.methods.response;

import com.alaya.protocol.core.Response;

/**
 * eth_sendRawTransaction.
 */
public class EthSendRawTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
