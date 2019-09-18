package org.web3j.platon;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.alibaba.fastjson.annotation.JSONField;

public class BaseResponse<T> {

    @JSONField(name = "Code")
    public int code;

    @JSONField(name = "Data")
    public T data;

    @JSONField(name = "ErrMsg")
    public String errMsg;

    public TransactionReceipt transactionReceipt;

    public BaseResponse() {
    }

    public boolean isStatusOk() {
        return code == ErrorCode.SUCCESS;
    }

    public BaseResponse(Throwable throwable) {
        this.code = ErrorCode.SYSTEM_ERROR;
        this.errMsg = throwable.getMessage();
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", data=" + data +
                ", errMsg='" + errMsg + '\'' +
                ", transactionReceipt=" + transactionReceipt +
                '}';
    }
}
