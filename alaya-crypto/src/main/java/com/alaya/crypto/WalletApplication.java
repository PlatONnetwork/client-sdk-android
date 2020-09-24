package com.alaya.crypto;

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
     * addressFormatType ：ADDRESS_TYPE_BECH32（bech32）、ADDRESS_TYPE_XX（拓展预留，比如可能P2PKH、P2SH）
     */
    public static int addressFormatType = 0;

    /**
     *  地址处理格式渠道：PLATON、ALAYA、LATTICEX
     */
    public static String channleType = "";


    /**
     * @param addressFormat ：确定钱包地址处理格式：BETCH32格式、其他XX格式
     * @param channle       ：确定链渠道环境：PLATON、ALAYA
     * @param network       ：确定指定链渠道环境下，主网、测试网
     *
     * 钱包地址概述：
     * 总体钱包地址格式分类：
     *  1、BETCH32格式
     *     1)、根据链渠道环境：PLATON ，衍生出主网lat，测试网lax地址
     *     2)、根据链渠道环境：ALAYA ，衍生出主网atp，测试网atx地址
     *  2、其他XX格式
     *     1)、根据链渠道环境区分，获取其他地址
     */
    public static void init(String network, int addressFormat,String channle){
        networkType = network;
        addressFormatType = addressFormat;
        channleType = channle;
    }

}
