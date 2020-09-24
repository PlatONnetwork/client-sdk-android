package com.alaya.protocol.geth;

import java.util.Arrays;

import com.alaya.protocol.geth.response.PersonalImportRawKey;
import com.alaya.protocol.Web3jService;
import com.alaya.protocol.admin.JsonRpc2_0Admin;
import com.alaya.protocol.admin.methods.response.BooleanResponse;
import com.alaya.protocol.admin.methods.response.PersonalSign;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.geth.response.PersonalEcRecover;

/**
 * JSON-RPC 2.0 factory implementation for Geth.
 */
class JsonRpc2_0Geth extends JsonRpc2_0Admin implements Geth {

    public JsonRpc2_0Geth(Web3jService web3jService) {
        super(web3jService);
    }
    
    @Override
    public Request<?, PersonalImportRawKey> personalImportRawKey(
            String keydata, String password) {
        return new Request<String, PersonalImportRawKey>(
                "personal_importRawKey",
                Arrays.asList(keydata, password),
                web3jService,
                PersonalImportRawKey.class);
    }

    @Override
    public Request<?, BooleanResponse> personalLockAccount(String accountId) {
        return new Request<String, BooleanResponse>(
                "personal_lockAccount",
                Arrays.asList(accountId),
                web3jService,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> personalSign(
            String message, String accountId, String password) {
        return new Request<String, PersonalSign>(
                "personal_sign",
                Arrays.asList(message,accountId,password),
                web3jService,
                PersonalSign.class);
    }

    @Override
    public Request<?, PersonalEcRecover> personalEcRecover(
            String hexMessage, String signedMessage) {
        return new Request<String, PersonalEcRecover>(
                "personal_ecRecover",
                Arrays.asList(hexMessage,signedMessage),
                web3jService,
                PersonalEcRecover.class);
    } 
    
}
