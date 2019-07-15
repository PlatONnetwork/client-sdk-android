package org.web3j.ticket;

import com.alibaba.fastjson.JSON;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Files;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;

public class MultisigContractTest {

    private Logger logger = LoggerFactory.getLogger(MultisigContractTest.class);

    private static final String CONTRACT_BINARY = loadBinary();

    private static final Credentials CREDENTIALS = loadCredentials();

    private static String loadBinary() {
        URL url = MultisigContractTest.class.getClassLoader().getResource("sophia/contracts/build/multisig.wasm");
        String path = url.getPath();
        File file = new File(path);
        String binary = "";
        try {
            byte[] bytes = Files.readBytes(file);
            binary = Hex.toHexString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return binary;
    }

    private static Credentials loadCredentials() {
        Credentials credentials = null;
        try {
            URL url = MultisigContractTest.class.getClassLoader().getResource("sophia/contracts/build/admin.json");
            String path = url.getPath();
            credentials = WalletUtils.loadCredentials("88888888", path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    // 本地测试
    private Web3j web3j = Web3jFactory.build(new HttpService("http://10.10.8.21:6789"));
    // 飞翔
//    private Web3j web3j = Web3j.build(new HttpService("http://10.10.8.209:6789"));
    // 线上
//    private Web3j web3j = Web3j.build(new HttpService("http://13.67.44.50:6789"));


    public String deploy() throws Exception {
        BigInteger gasPrice = web3j.platonGasPrice().send().getGasPrice();
        Multisig contract = Multisig.deploy(web3j, CREDENTIALS, CONTRACT_BINARY, new StaticGasProvider(gasPrice, BigInteger.valueOf(250000000))).send();
        String contractAddress = contract.getContractAddress();
        logger.debug("Contract Address: {}", contractAddress);
        return contractAddress;
    }

    // 部署合约
    @Test
    public void deployContract() throws Exception {
        deploy();
    }

    /**
     * Get contract's owners
     *
     * @throws Exception
     */
    @Test
    public void getOwners() throws Exception {
        String contractAddress = deploy();
        BigInteger gasPrice = web3j.platonGasPrice().send().getGasPrice();
        Multisig contract = Multisig.load(CONTRACT_BINARY, contractAddress, web3j, CREDENTIALS, new StaticGasProvider(gasPrice, new BigInteger("2000000")));
        String owners = "0xfc6268a75664df3a1f50d77fc95bbfd8faeecaf0:0xfc6268a75664df3a1f50d77fc95bbfd8faeecaf1";
        contract.initWallet(owners, new BigInteger("2")).send();
        String result = contract.getOwners().send();
        Assert.assertEquals(owners, result);
    }

    /**
     * Get contract's owners
     *
     * @throws Exception
     */
    @Test
    public void getListSize() throws Exception {
        String contractAddress = deploy();
        BigInteger gasPrice = web3j.platonGasPrice().send().getGasPrice();
        Multisig contract = Multisig.load(CONTRACT_BINARY, contractAddress, web3j, CREDENTIALS, new StaticGasProvider(gasPrice, new BigInteger("2000000")));
        logger.debug("Multisig Before init: {}", JSON.toJSONString(contract, true));
        TransactionReceipt receipt = contract.initWallet("0xfc6268a75664df3a1f50d77fc95bbfd8faeecaf0:0xfc6268a75664df3a1f50d77fc95bbfd8faeecaf1", new BigInteger("2")).send();
        logger.debug("TransactionReceipt: {}", JSON.toJSONString(receipt, true));
        contract = Multisig.load(CONTRACT_BINARY, contractAddress, web3j, CREDENTIALS, new StaticGasProvider(gasPrice, new BigInteger("2000000")));
        logger.debug("Multisig After init: {}", JSON.toJSONString(contract, true));
        BigInteger listSize = contract.getListSize().send();
        logger.debug("ListSize:{}", listSize);
        Assert.assertTrue(BigInteger.ZERO.compareTo(listSize) <= 0);
    }
}
