package org.web3j.crypto.addressconvert;


import org.web3j.crypto.WalletApplication;
import org.web3j.crypto.addressconvert.bech32.Bech32Util;
import org.web3j.crypto.addressconvert.exception.AddressInitException;

public class AddressCheck {


    //wallet配置参数是否初始化
    public static boolean checkInitWalletParam(){
        boolean checkResult = false;
        try{
            if(WalletApplication.addressFormatType == 0 || WalletApplication.addressHrp == null || WalletApplication.addressHrp.equals("")){
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



    public static boolean checkAddressValidity(String address){
        if(address == null || "".equals(address)){
            return true;
        }
        String hrp = address.substring(0,3);
        if(WalletApplication.addressHrp == null){
            return false;
        }
        if(hrp.equals(WalletApplication.addressHrp)){
            if(address.length() == Bech32Util.ADDRESS_LENGTH){
                return true;
            }
        }
        return false;
    }
}
