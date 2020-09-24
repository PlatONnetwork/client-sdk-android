package com.alaya.protocol.parity.methods.response;

import com.alaya.crypto.WalletFile;
import com.alaya.protocol.core.Response;

/**
 * parity_ExportAccount.
 */
public class ParityExportAccount extends Response<WalletFile> {
    public WalletFile getWallet() {
        return getResult();
    }
}
