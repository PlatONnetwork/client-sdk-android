package org.web3j.protocol.core.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;

/**
 * @author ziv
 */
public class EthPendingTransactions extends Response<List<Transaction>> {
    
    public List<Transaction> getTransactions() {
        return getResult();
    }
}