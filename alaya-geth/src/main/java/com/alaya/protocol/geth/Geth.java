package com.alaya.protocol.geth;

import com.alaya.protocol.geth.response.PersonalEcRecover;
import com.alaya.protocol.geth.response.PersonalImportRawKey;
import com.alaya.protocol.admin.Admin;
import com.alaya.protocol.admin.methods.response.BooleanResponse;
import com.alaya.protocol.admin.methods.response.PersonalSign;
import com.alaya.protocol.core.Request;

/**
 * JSON-RPC Request object building factory for Geth. 
 */
public interface Geth extends Admin {
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);
    
    public Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    public Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    public Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);
    
}
