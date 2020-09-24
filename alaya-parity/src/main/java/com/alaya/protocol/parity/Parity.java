package com.alaya.protocol.parity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alaya.protocol.parity.methods.request.Derivation;
import com.alaya.crypto.WalletFile;
import com.alaya.protocol.admin.Admin;
import com.alaya.protocol.admin.methods.response.BooleanResponse;
import com.alaya.protocol.admin.methods.response.NewAccountIdentifier;
import com.alaya.protocol.admin.methods.response.PersonalSign;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.parity.methods.response.ParityAddressesResponse;
import com.alaya.protocol.parity.methods.response.ParityAllAccountsInfo;
import com.alaya.protocol.parity.methods.response.ParityDefaultAddressResponse;
import com.alaya.protocol.parity.methods.response.ParityDeriveAddress;
import com.alaya.protocol.parity.methods.response.ParityExportAccount;
import com.alaya.protocol.parity.methods.response.ParityListRecentDapps;

/**
 * JSON-RPC Request object building factory for Parity.
 */
public interface Parity extends Admin, Trace {
    Request<?, ParityAllAccountsInfo> parityAllAccountsInfo();
    
    Request<?, BooleanResponse> parityChangePassword(
            String accountId, String oldPassword, String newPassword);
    
    Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            String accountId, String password, Derivation hashType, boolean toSave);
    
    Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            String accountId, String password, List<Derivation> indicesType, boolean toSave);
    
    Request<?, ParityExportAccount> parityExportAccount(String accountId, String password);
    
    Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId);
    
    Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId);
    
    Request<?, ParityAddressesResponse> parityGetNewDappsAddresses();
    
    Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress();
    
    Request<?, ParityAddressesResponse> parityImportGethAccounts(ArrayList<String> gethAddresses);
    
    Request<?, BooleanResponse> parityKillAccount(String accountId, String password);
    
    Request<?, ParityAddressesResponse> parityListGethAccounts();
    
    Request<?, ParityListRecentDapps> parityListRecentDapps();
    
    Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(String phrase, String password);
    
    Request<?, NewAccountIdentifier> parityNewAccountFromSecret(String secret, String password);
    
    Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            WalletFile walletFile, String password);
    
    Request<?, BooleanResponse> parityRemoveAddress(String accountId);
    
    Request<?, BooleanResponse> paritySetAccountMeta(
            String accountId, Map<String, Object> metadata);
    
    Request<?, BooleanResponse> paritySetAccountName(String address, String name);
    
    Request<?, BooleanResponse> paritySetDappAddresses(
            String dAppId, ArrayList<String> availableAccountIds);
    
    Request<?, BooleanResponse> paritySetDappDefaultAddress(String dAppId, String defaultAddress);
    
    Request<?, BooleanResponse> paritySetNewDappsAddresses(ArrayList<String> availableAccountIds);
    
    Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress);
    
    Request<?, BooleanResponse> parityTestPassword(String accountId, String password);
    
    Request<?, PersonalSign> paritySignMessage(
            String accountId, String password, String hexMessage);
}
