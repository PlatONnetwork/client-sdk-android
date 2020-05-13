package org.web3j.crypto.bech32;


import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletApplication;


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

    public String getAddress(ECKeyPair ecKeyPair){
        String originAddress = Keys.getAddress(ecKeyPair);
        return getAddress(originAddress);
    }


    private AddressBehavior addressBehavior;
    private void setAddressBehavior(AddressBehavior addressBehavior){
        this.addressBehavior = addressBehavior;
    }

    public AddressBech32 executeEncodeAddress(ECKeyPair ecKeyPair){
        String originAddress = Keys.getAddress(ecKeyPair);
        return executeEncodeAddress(originAddress);
    }

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

    public AddressBech32 executeDecodeAddress(String address){

        if(ADDRESS_TYPE_BECH32 == WalletApplication.addressFormatType){
            setAddressBehavior(new AddressBehaviorBech32());
        }else if(ADDRESS_TYPE_XX == WalletApplication.addressFormatType){
            //todo...
        }
        return addressBehavior.decodeAddress(address);
    }






}
