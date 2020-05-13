package org.web3j.crypto.bech32;

import org.web3j.crypto.WalletApplication;
import org.web3j.utils.Numeric;

public class AddressBehaviorBech32 implements AddressBehavior {

    @Override
    public String channleType() {
        return WalletApplication.channleType;
    }

    @Override
    public AddressBech32 encodeAddress(String address) {
        AddressBech32 addressBech32 = new AddressBech32();
        if(channleType() == AddressBehavior.CHANNLE_PLATON){
            String latAddress = Bech32.addressEncode(Bech32.HRP_LAT,address);
            String laxAddress = Bech32.addressEncode(Bech32.HRP_LAX, address);
            addressBech32.setMainnet(latAddress);
            addressBech32.setTestnet(laxAddress);
        }else{
            String plaAddress = Bech32.addressEncode(Bech32.HRP_PLA, address);
            String pltAddress = Bech32.addressEncode(Bech32.HRP_PLT, address);
            addressBech32.setMainnet(plaAddress);
            addressBech32.setTestnet(pltAddress);
        }
        return addressBech32;
    }

    @Override
    public AddressBech32 decodeAddress(String address) {

          /*  Bech32.Bech32Data bech32Data  = Bech32.decode(address);
            return Numeric.toHexString(Bech32.convertBits(bech32Data.data, 5, 8, false));*/
          return null;
    }
}
