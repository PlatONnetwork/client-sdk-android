package com.alaya.protocol.core.methods.response;

import com.alaya.protocol.core.Response;

import java.util.List;

/**
 * @author ziv
 */
public class PlatonPendingTransactions extends Response<List<Transaction>> {
    
    public List<Transaction> getTransactions() {
        return getResult();
    }
}