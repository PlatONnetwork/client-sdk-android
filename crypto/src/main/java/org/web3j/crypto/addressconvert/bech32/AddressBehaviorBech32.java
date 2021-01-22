package org.web3j.crypto.addressconvert.bech32;


import org.web3j.crypto.WalletApplication;

public class AddressBehaviorBech32 implements AddressBehavior {


    @Override
    public String getAddressHrp() {
        return WalletApplication.addressHrp;
    }

    @Override
    public String encodeAddress(String address) {

        if(getAddressHrp() != null && !getAddressHrp().equals("")){
            return Bech32Util.addressEncode(getAddressHrp(),address);
        }
        return "";
    }

    @Override
    public String decodeAddress(String address) {

          /*  Bech32.Bech32Data bech32Data  = Bech32.decode(address);
            return Numeric.toHexString(Bech32.convertBits(bech32Data.data, 5, 8, false));*/
          return "";
    }
}
