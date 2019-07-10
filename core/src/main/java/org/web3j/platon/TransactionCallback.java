package org.web3j.platon;

import org.web3j.protocol.core.methods.response.EthSendTransaction;

public interface TransactionCallback<T> {

    void onTransactionStart();

    void onTransaction(EthSendTransaction sendTransaction);

    void onTransactionSucceed(T t);

    void onTransactionFailed(BaseResponse baseResponse);
}
