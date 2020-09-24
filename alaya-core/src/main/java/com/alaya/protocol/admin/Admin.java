package com.alaya.protocol.admin;

import java.math.BigInteger;

import com.alaya.protocol.admin.methods.response.NewAccountIdentifier;
import com.alaya.protocol.admin.methods.response.PersonalListAccounts;
import com.alaya.protocol.admin.methods.response.PersonalUnlockAccount;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.methods.request.Transaction;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;

/**
 * JSON-RPC Request object building factory for common Parity and Geth. 
 */
public interface Admin extends Web3j {
    public Request<?, PersonalListAccounts> personalListAccounts();
    
    public Request<?, NewAccountIdentifier> personalNewAccount(String password);
    
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String address, String passphrase, BigInteger duration);
    
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String address, String passphrase);
    
    public Request<?, PlatonSendTransaction> personalSendTransaction(
            Transaction transaction, String password);

}   
