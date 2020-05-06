package org.web3j.crypto.bech32;

public class AddressBech32 {

    private String mainnet;
    private String testnet;

    public AddressBech32() {
    }

    public AddressBech32(String mainnet, String testnet) {
        this.mainnet = mainnet;
        this.testnet = testnet;
    }

    public String getMainnet() {
        return mainnet;
    }

    public void setMainnet(String mainnet) {
        this.mainnet = mainnet;
    }

    public String getTestnet() {
        return testnet;
    }

    public void setTestnet(String testnet) {
        this.testnet = testnet;
    }
}
