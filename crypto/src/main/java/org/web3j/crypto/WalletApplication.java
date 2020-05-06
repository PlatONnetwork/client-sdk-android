package org.web3j.crypto;

public class WalletApplication {

    public static final String MAINNET = "MAINNET";
    public static final String TESTNET = "TESTNET";

    public static String networkType = "";
    public static int addressFormatType = 0;

    public static void init(String network, int addressFormat){
        networkType = network;
        addressFormatType = addressFormat;
    }

}
