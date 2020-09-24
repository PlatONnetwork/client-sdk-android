package com.alaya.platon;

import com.alaya.protocol.core.methods.response.PlatonSendTransaction;

public interface TransactionCallback<T> {

    void onTransactionStart();

    void onTransaction(PlatonSendTransaction sendTransaction);

    void onTransactionSucceed(T t);

    void onTransactionFailed(BaseResponse baseResponse);
}
