package com.alaya.protocol.parity.methods.response;

import java.util.List;

import com.alaya.protocol.core.Response;

/**
 * trace_block
 * trace_filter
 * trace_transaction.
 */
public class ParityTracesResponse extends Response<List<Trace>> {
    public List<Trace> getTraces() {
        return getResult();
    }
}
