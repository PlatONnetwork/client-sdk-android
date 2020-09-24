package com.alaya.protocol.parity;

import java.math.BigInteger;
import java.util.List;

import com.alaya.protocol.parity.methods.request.TraceFilter;
import com.alaya.protocol.parity.methods.response.ParityFullTraceResponse;
import com.alaya.protocol.parity.methods.response.ParityTraceGet;
import com.alaya.protocol.parity.methods.response.ParityTracesResponse;
import com.alaya.protocol.core.DefaultBlockParameter;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.methods.request.Transaction;

/**
 * * JSON-RPC Parity traces API request object building factory.
 */
public interface Trace {
    Request<?, ParityFullTraceResponse> traceCall(
            Transaction transaction,
            List<String> traceTypes,
            DefaultBlockParameter blockParameter);

    Request<?, ParityFullTraceResponse> traceRawTransaction(String data, List<String> traceTypes);

    Request<?, ParityFullTraceResponse> traceReplayTransaction(
            String hash, List<String> traceTypes);

    Request<?, ParityTracesResponse> traceBlock(DefaultBlockParameter blockParameter);

    Request<?, ParityTracesResponse> traceFilter(TraceFilter traceFilter);

    Request<?, ParityTraceGet> traceGet(String hash, List<BigInteger> indices);

    Request<?, ParityTracesResponse> traceTransaction(String hash);
}
