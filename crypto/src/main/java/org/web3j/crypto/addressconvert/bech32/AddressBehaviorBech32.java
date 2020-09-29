package org.web3j.crypto.addressconvert.bech32;


import org.web3j.crypto.WalletApplication;

public class AddressBehaviorBech32 implements AddressBehavior {


    @Override
    public String channleType() {
        return WalletApplication.channleType;
    }

    @Override
    public AddressBech32 encodeAddress(String address) {
        AddressBech32 addressBech32 = new AddressBech32();
        String mainnetAddress = "";
        String testnetAddress = "";

        if(channleType() == CHANNLE_PLATON){

            mainnetAddress = Bech32Util.addressEncode(Bech32Util.HRP_LAT,address);
            testnetAddress = Bech32Util.addressEncode(Bech32Util.HRP_LAX, address);
        } else if(channleType() == CHANNLE_ALAYA){

            mainnetAddress = Bech32Util.addressEncode(Bech32Util.HRP_ATP,address);
            testnetAddress = Bech32Util.addressEncode(Bech32Util.HRP_ATX, address);
        }else{

            mainnetAddress = Bech32Util.addressEncode(Bech32Util.HRP_PLA, address);
            testnetAddress = Bech32Util.addressEncode(Bech32Util.HRP_PLT, address);

        }
        addressBech32.setMainnet(mainnetAddress);
        addressBech32.setTestnet(testnetAddress);
        return addressBech32;
    }

    @Override
    public AddressBech32 decodeAddress(String address) {

          /*  Bech32.Bech32Data bech32Data  = Bech32.decode(address);
            return Numeric.toHexString(Bech32.convertBits(bech32Data.data, 5, 8, false));*/
          return null;
    }
}
