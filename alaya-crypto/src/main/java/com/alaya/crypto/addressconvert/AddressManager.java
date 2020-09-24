package com.alaya.crypto.addressconvert;


import com.alaya.crypto.ECKeyPair;
import com.alaya.crypto.Keys;
import com.alaya.crypto.WalletApplication;
import com.alaya.crypto.addressconvert.bech32.AddressBech32;
import com.alaya.crypto.addressconvert.bech32.AddressBehavior;
import com.alaya.crypto.addressconvert.bech32.AddressBehaviorBech32;


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


    /**
     * 根据初始化init()网络环境、地址格式
     * @param address 原始钱包地址
     * @return  处理后Wallet地址
     */
    public String getAddress(String address){
        if(AddressCheck.checkInitWalletParam()){
            AddressBech32 addressBech32 = executeEncodeAddress(address);
            if(WalletApplication.networkType.equals(WalletApplication.MAINNET)){
                return addressBech32.getMainnet();
            }else{
                return addressBech32.getTestnet();
            }
        }
        return "";
    }

    /**
     *  ecKeyPair转换成原始格式地址
     * @param ecKeyPair
     * @return
     */
    public String getAddress(ECKeyPair ecKeyPair){
        String originAddress = Keys.getAddress(ecKeyPair);
        return getAddress(originAddress);
    }


    private AddressBehavior addressBehavior;
    private void setAddressBehavior(AddressBehavior addressBehavior){
        this.addressBehavior = addressBehavior;
    }

    /**
     *    ecKeyPair转换成lat/lax等地址格式
     * @param ecKeyPair
     * @return
     */
    public AddressBech32 executeEncodeAddress(ECKeyPair ecKeyPair){
        String originAddress = Keys.getAddress(ecKeyPair);
        return executeEncodeAddress(originAddress);
    }

    /**
     *  最终入口
     *  原始地址转换成lat/lax等地址格式
     * @param address
     * @return
     */
    public AddressBech32 executeEncodeAddress(String address){
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


    /**
     *  lat/lax等地址格式转换成原始地址
     * @param address
     * @return
     */
    public AddressBech32 executeDecodeAddress(String address){

        if(ADDRESS_TYPE_BECH32 == WalletApplication.addressFormatType){
            setAddressBehavior(new AddressBehaviorBech32());
        }else if(ADDRESS_TYPE_XX == WalletApplication.addressFormatType){
            //todo...
        }
        return addressBehavior.decodeAddress(address);
    }






}
