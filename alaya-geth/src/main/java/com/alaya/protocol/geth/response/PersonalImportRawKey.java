package com.alaya.protocol.geth.response;

import com.alaya.protocol.core.Response;

/**
 * personal_importRawKey.
 */
public class PersonalImportRawKey extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
