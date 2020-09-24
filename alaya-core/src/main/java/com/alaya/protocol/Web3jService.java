package com.alaya.protocol;

import java.io.IOException;
import java.util.concurrent.Future;

import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.Response;

/**
 * Services API.
 */
public interface Web3jService {
    <T extends Response> T send(
            Request request, Class<T> responseType) throws IOException;

    <T extends Response> Future<T> sendAsync(
            Request request, Class<T> responseType);
}
