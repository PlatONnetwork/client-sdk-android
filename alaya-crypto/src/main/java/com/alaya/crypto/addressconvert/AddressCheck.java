package com.alaya.crypto.addressconvert;

import com.alaya.crypto.WalletApplication;
import com.alaya.crypto.addressconvert.bech32.Bech32Util;
import com.alaya.crypto.addressconvert.exception.AddressInitException;

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
        if(hrp.equals(Bech32Util.HRP_LAT) || hrp.equals(Bech32Util.HRP_LAX) ||
           hrp.equals(Bech32Util.HRP_PLA) || hrp.equals(Bech32Util.HRP_PLT) ||
           hrp.equals(Bech32Util.HRP_ATP) || hrp.equals(Bech32Util.HRP_ATX)){

            if(address.length() == 42){
               return true;
            }
        }
        return false;
    }
}
