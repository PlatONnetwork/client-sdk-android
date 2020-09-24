package com.alaya.protocol.parity.methods.response;

import com.alaya.protocol.core.Response;

/**
 * parity_deriveAddressHash
 * parity_deriveAddressIndex.
 */
public class ParityDeriveAddress extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
