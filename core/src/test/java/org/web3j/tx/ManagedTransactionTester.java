package org.web3j.tx;

import java.io.IOException;

import org.junit.Before;

import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionCount;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public abstract class ManagedTransactionTester {

    static final String ADDRESS = "0x3d6cb163f7c72d20b0fcd6baae5889329d138a4a";
    static final String TRANSACTION_HASH = "0xHASH";
    protected Web3j web3j;

    @Before
    public void setUp() throws Exception {
        web3j = mock(Web3j.class);
    }

    void prepareTransaction(TransactionReceipt transactionReceipt) throws IOException {
        prepareNonceRequest();
        prepareTransactionRequest();
        prepareTransactionReceipt(transactionReceipt);
    }

    @SuppressWarnings("unchecked")
    void prepareNonceRequest() throws IOException {
        PlatonGetTransactionCount ethGetTransactionCount = new PlatonGetTransactionCount();
        ethGetTransactionCount.setResult("0x1");

        Request<?, PlatonGetTransactionCount> transactionCountRequest = mock(Request.class);
        when(transactionCountRequest.send())
                .thenReturn(ethGetTransactionCount);
        when(web3j.platonGetTransactionCount(SampleKeys.ADDRESS, DefaultBlockParameterName.PENDING))
                .thenReturn((Request) transactionCountRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionRequest() throws IOException {
        PlatonSendTransaction ethSendTransaction = new PlatonSendTransaction();
        ethSendTransaction.setResult(TRANSACTION_HASH);

        Request<?, PlatonSendTransaction> rawTransactionRequest = mock(Request.class);
        when(rawTransactionRequest.send()).thenReturn(ethSendTransaction);
        when(web3j.platonSendRawTransaction(any(String.class)))
                .thenReturn((Request) rawTransactionRequest);
    }

    @SuppressWarnings("unchecked")
    void prepareTransactionReceipt(TransactionReceipt transactionReceipt) throws IOException {
        PlatonGetTransactionReceipt ethGetTransactionReceipt = new PlatonGetTransactionReceipt();
        ethGetTransactionReceipt.setResult(transactionReceipt);

        Request<?, PlatonGetTransactionReceipt> getTransactionReceiptRequest = mock(Request.class);
        when(getTransactionReceiptRequest.send())
                .thenReturn(ethGetTransactionReceipt);
        when(web3j.platonGetTransactionReceipt(TRANSACTION_HASH))
                .thenReturn((Request) getTransactionReceiptRequest);
    }
}
