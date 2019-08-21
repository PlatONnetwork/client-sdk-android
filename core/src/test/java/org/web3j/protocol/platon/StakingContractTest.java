package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.PlatOnTypeEncoder;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletFile;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.bean.Node;
import org.web3j.platon.bean.StakingParam;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonGetBalance;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionCount;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class StakingContractTest {

    private static final int OFFSET_SHORT_ITEM = 0x80;
    private static final int OFFSET_LONG_ITEM = 0xb7;
    private static final int SIZE_THRESHOLD = 56;

    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.76:6795"));

    private StakingContract stakingContract;

    String nodeId = "3aca21c898c892dae5081682119573c86b4dec3d50875cd95358cc28068231929827f3ee95471cb857986ad7f4b7d64f54be493dc3d476f603bdc2bc8a64e79b";
    String stakingAmount = "1000000000000000000000000000";
    StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
    String benifitAddress = "0x5e57ae97e714abe990c882377aaf9c57f4ea363b";
    String externalId = "liyf-test-id";
    String nodeName = "liyf-test";
    String webSite = "www.baidu.com";
    String details = "details";
    String blsPubKey = "cec189e90234b2c4d9e55402c1abf7cfbbc85dbf1b6b43820a2c6f953464c201bf6d1c3f51cf5e7cbc6e40815406f611b1aeca99acd782ed8b8e33c82f71ee08";

    private Credentials credentials;

    @Before
    public void init() {

        credentials = Credentials.create("0x69df96d55baae56664847ff3ef38a8725da1746392cffdf2ae59b2107144dd70");

        stakingContract = StakingContract.load(
                web3j,
                credentials, "100");


    }


    @Test
    public void staking() {

//        String fromAddress = Keys.getAddress(ECKeyPair.create(Numeric.toBigIntNoPrefix("a7f1d33a30c1e8b332443825f2209755c52086d0a88b084301a6727d9f84bf32")));
//        String toAddress = Keys.getAddress(ECKeyPair.create(Numeric.toBigIntNoPrefix("69df96d55baae56664847ff3ef38a8725da1746392cffdf2ae59b2107144dd70")));
//
//        sendTransaction("0xa7f1d33a30c1e8b332443825f2209755c52086d0a88b084301a6727d9f84bf32", "0x" + toAddress, new BigDecimal("200000000000000000000000000"), 500000000000L, 60000L);
//
//        try {
//            PlatonGetBalance platonGetBalance = web3j.platonGetBalance("0x" + toAddress, DefaultBlockParameterName.LATEST).send();
//            PlatonGetBalance platonGetBalance2 = web3j.platonGetBalance("0x" + fromAddress, DefaultBlockParameterName.LATEST).send();
//
//            System.out.println(platonGetBalance.getBalance().longValue());
//            System.out.println(platonGetBalance2.getBalance().longValue());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            BaseResponse baseResponse = stakingContract.staking(new StakingParam.Builder()
                    .setNodeId(nodeId)
                    .setAmount(new BigInteger(stakingAmount))
                    .setStakingAmountType(stakingAmountType)
                    .setBenifitAddress(benifitAddress)
                    .setExternalId(externalId)
                    .setNodeName(nodeName)
                    .setWebSite(webSite)
                    .setDetails(details)
                    .setBlsPubKey(blsPubKey)
                    .build()).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateStakingInfo() {
        try {
            BaseResponse baseResponse = stakingContract.updateStakingInfo(nodeId, benifitAddress, externalId, nodeName, "https://www.github.com/", details).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addStaking() {
        try {
            BaseResponse baseResponse = stakingContract.addStaking(nodeId, StakingAmountType.FREE_AMOUNT_TYPE, BigInteger.valueOf(10000)).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unStaking() {
        try {
            BaseResponse baseResponse = stakingContract.unStaking(nodeId).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getStakingInfo() {
        try {
            BaseResponse<Node> baseResponse = stakingContract.getStakingInfo(nodeId).send();
            Node node = baseResponse.data;
            System.out.println(node.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String sendTransaction(String privateKey, String toAddress, BigDecimal amount, long gasPrice, long gasLimit) {

        BigInteger GAS_PRICE = BigInteger.valueOf(gasPrice);
        BigInteger GAS_LIMIT = BigInteger.valueOf(gasLimit);

        Credentials credentials = Credentials.create(privateKey);

        try {

            List<RlpType> result = new ArrayList<>();
            result.add(RlpString.create(Numeric.hexStringToByteArray(PlatOnTypeEncoder.encode(new Int64(0)))));
            String txType = Hex.toHexString(RlpEncoder.encode(new RlpList(result)));

            RawTransaction rawTransaction = RawTransaction.createTransaction(getNonce(), GAS_PRICE, GAS_LIMIT, toAddress, amount.toBigInteger(),
                    txType);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, new Byte("100"), credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            PlatonSendTransaction transaction = web3j.platonSendRawTransaction(hexValue).send();

            return transaction.getTransactionHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected BigInteger getNonce() throws IOException {
        PlatonGetTransactionCount ethGetTransactionCount = web3j.platonGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        if (ethGetTransactionCount.getTransactionCount().intValue() == 0) {
            ethGetTransactionCount = web3j.platonGetTransactionCount(
                    credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
        }

        return ethGetTransactionCount.getTransactionCount();
    }
}
