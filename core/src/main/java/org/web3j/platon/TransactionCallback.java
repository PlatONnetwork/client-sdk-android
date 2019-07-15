package org.web3j.platon;

import org.web3j.protocol.core.methods.response.PlatonSendTransaction;

public interface TransactionCallback<T> {

    void onTransactionStart();

    void onTransaction(PlatonSendTransaction sendTransaction);

    void onTransactionSucceed(T t);

    void onTransactionFailed(BaseResponse baseResponse);
}
