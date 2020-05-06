package org.web3j.crypto.bech32;

import org.web3j.crypto.WalletApplication;
import org.web3j.crypto.bech32.exception.AddressInitException;

public class AddressCheck {


    /**
     * wallet配置参数是否初始化
     * @return
     */
    public static boolean checkInitWalletParam(){
        if(WalletApplication.networkType.equals("") || WalletApplication.addressFormatType == 0){
            new AddressInitException("please init walletApplication...");
            return false;
        }
        return true;
    }


    /**
     * wallet address 有效性
     * @param address
     * @return
     */
    public static boolean checkAddressValidity(String address){
        String hrp = address.substring(0,2);
        if(hrp.equals(Bech32.HRP_LAT) || hrp.equals(Bech32.HRP_LAX) || hrp.equals(Bech32.HRP_PLA) || hrp.equals(Bech32.HRP_PLT)){
            if(address.length() == 42){
               return true;
            }
        }
        return false;
    }
}
