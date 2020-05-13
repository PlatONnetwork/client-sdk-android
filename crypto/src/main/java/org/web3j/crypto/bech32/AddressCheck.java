package org.web3j.crypto.bech32;

import org.web3j.crypto.WalletApplication;
import org.web3j.crypto.bech32.exception.AddressInitException;

public class AddressCheck {


    /**
     * wallet配置参数是否初始化
     * @return
     */
    public static boolean checkInitWalletParam(){
        boolean checkResult = false;
        try{
            if(WalletApplication.networkType.equals("") || WalletApplication.addressFormatType == 0){
                checkResult = false;
                throw new AddressInitException("please initialize first walletApplication...");
            }else{
                checkResult = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return checkResult;
    }


    /**
     * wallet address 有效性
     * @param address
     * @return
     */
    public static boolean checkAddressValidity(String address){
        if(address == null || "".equals(address))
            return true;

        String hrp = address.substring(0,3);
        if(hrp.equals(Bech32.HRP_LAT) || hrp.equals(Bech32.HRP_LAX) || hrp.equals(Bech32.HRP_PLA) || hrp.equals(Bech32.HRP_PLT)){
            if(address.length() == 42){
               return true;
            }
        }
        return false;
    }
}
