package org.web3j.crypto;

public class WalletApplication {

    public static final String MAINNET = "MAINNET";
    public static final String TESTNET = "TESTNET";

    /**
     * 返回不同网络环境address
     * networkType : MAINNET（主网）、TESTNET（测试网）
     */
    public static String networkType = "";
    /**
     * 地址处理格式
     * addressFormatType ：ADDRESS_TYPE_BECH32（bech32）、ADDRESS_TYPE_XX（拓展预留）
     */
    public static int addressFormatType = 0;
    /**
     * 地址前缀Hrp
     */
    public static String addressHrp;


    public static void init(int addressFormat,String hrp){
        addressFormatType = addressFormat;
        addressHrp = hrp;
    }

}
