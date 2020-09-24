package com.alaya.tx;

import java.io.IOException;
import java.math.BigInteger;

import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.JsonRpc2_0Web3j;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.protocol.exceptions.TransactionException;
import com.alaya.tx.response.PollingTransactionReceiptProcessor;
import com.alaya.tx.response.TransactionReceiptProcessor;

/**
 * Transaction manager abstraction for executing transactions with Ethereum client via
 * various mechanisms.
 */
public abstract class TransactionManager {

    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = JsonRpc2_0Web3j.DEFAULT_BLOCK_TIME;

    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;

    protected TransactionManager(TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        //最终地址入参
        if(!AddressCheck.checkAddressValidity(fromAddress)){
            this.fromAddress = AddressManager.getInstance().getAddress(fromAddress);
        }else{
            this.fromAddress = fromAddress;
        }
        this.transactionReceiptProcessor = transactionReceiptProcessor;
    }

    protected TransactionManager(Web3j web3j, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(
                        web3j, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }

    protected TransactionManager(
            Web3j web3j, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(web3j, sleepDuration, attempts), fromAddress);
    }

    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException, TransactionException {

        PlatonSendTransaction ethSendTransaction = sendTransaction(
                gasPrice, gasLimit, to, data, value);
        return processResponse(ethSendTransaction);
    }

    protected TransactionReceipt getTransactionReceipt(PlatonSendTransaction ethSendTransaction) throws IOException, TransactionException {
        return processResponse(ethSendTransaction);
    }

    public abstract PlatonSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException;

    public String getFromAddress() {
        return fromAddress;
    }

    private TransactionReceipt processResponse(PlatonSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException("Error processing transaction request: "
                    + transactionResponse.getError().getMessage());
        }

        String transactionHash = transactionResponse.getTransactionHash();

        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }


}
