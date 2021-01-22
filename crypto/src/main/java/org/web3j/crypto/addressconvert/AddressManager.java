package org.web3j.crypto.addressconvert;


import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletApplication;
import org.web3j.crypto.addressconvert.bech32.AddressBech32;
import org.web3j.crypto.addressconvert.bech32.AddressBehavior;
import org.web3j.crypto.addressconvert.bech32.AddressBehaviorBech32;

public class AddressManager {


    public static final int ADDRESS_TYPE_BECH32 = 1;
    public static final int ADDRESS_TYPE_XX = 2;


    private AddressManager(){}

    private static class InstanceHolder {
        private static volatile AddressManager INSTANCE = new AddressManager();
    }

    public static AddressManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    //根据初始化init()地址格式
    //address 原始钱包地址
    //处理后Wallet地址
    public String getAddress(String address){
        if(AddressCheck.checkInitWalletParam()){
            return executeEncodeAddress(address);
        }
        return "";
    }

    //ecKeyPair转换成原始格式地址
    public String getAddress(ECKeyPair ecKeyPair){
        String originAddress = Keys.getAddress(ecKeyPair);
        return getAddress(originAddress);
    }


    private AddressBehavior addressBehavior;
    private void setAddressBehavior(AddressBehavior addressBehavior){
        this.addressBehavior = addressBehavior;
    }

    //ecKeyPair转换成lat/lax等地址格式
    public String executeEncodeAddress(ECKeyPair ecKeyPair){
        String originAddress = Keys.getAddress(ecKeyPair);
        return executeEncodeAddress(originAddress);
    }

    //最终入口
    //原始地址转换成lat/lax等地址格式

    public String executeEncodeAddress(String address){
        if(AddressCheck.checkInitWalletParam()){
            if(ADDRESS_TYPE_BECH32 == WalletApplication.addressFormatType){
                setAddressBehavior(new AddressBehaviorBech32());
            }else if(ADDRESS_TYPE_XX == WalletApplication.addressFormatType){
                //todo...
            }
            return addressBehavior.encodeAddress(address);
        }
        return null;
    }


   //lat/lax等地址格式转换成原始地址
    public String executeDecodeAddress(String address){

        if(ADDRESS_TYPE_BECH32 == WalletApplication.addressFormatType){
            setAddressBehavior(new AddressBehaviorBech32());
        }else if(ADDRESS_TYPE_XX == WalletApplication.addressFormatType){
            //todo...
        }
        return addressBehavior.decodeAddress(address);
    }






}
