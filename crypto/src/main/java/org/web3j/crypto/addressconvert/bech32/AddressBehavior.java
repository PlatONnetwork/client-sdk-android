package org.web3j.crypto.addressconvert.bech32;

public interface AddressBehavior {

    public static final String CHANNLE_PLATON = "PLATON";
    public static final String CHANNLE_ALAYA = "ALAYA";
    public static final String CHANNLE_LATT = "LATTICEX";


    String getAddressHrp();

    String encodeAddress(String address);

    String decodeAddress(String address);
}
