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
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Platon;
import org.web3j.protocol.core.methods.response.PlatonBlock;
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

    //    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.88:6788"));
    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.112.120:8222"));

    private StakingContract stakingContract;

    String nodeId = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
    String stakingAmount = "10000000000000000000000000";
    StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
    String benifitAddress = "0x5e57ae97e714abe990c882377aaf9c57f4ea363b";
    String externalId = "liyf-test-id";
    String nodeName = "liyf-test";
    String webSite = "www.baidu.com";
    String details = "details";
    String blsPubKey = "a3709aba3deb9a49411b61d930735e4ff8828c5973d76f91554ffd835803b724c22c58656d1698b6395098234cf31fe22761a56bde3527d54b85f44fa6627d22";

    private Credentials credentials;

    @Before
    public void init() {

//        credentials = Credentials.create("0xa11859ce23effc663a9460e332ca09bd812acc390497f8dc7542b6938e13f8d7");
        credentials = Credentials.create("0xc783df0e98baf34f2ed791f6087be8e3f55fe9c4e4687e0ddc30a37abc15b287");

        stakingContract = StakingContract.load(
                web3j,
                credentials, "102");

    }


    @Test
    public void staking() throws Exception {

//        String fromAddress = Keys.getAddress(ECKeyPair.create(Numeric.toBigIntNoPrefix("a11859ce23effc663a9460e332ca09bd812acc390497f8dc7542b6938e13f8d7")));
//        String toAddress = Keys.getAddress(ECKeyPair.create(Numeric.toBigIntNoPrefix("9d8dc0c4895d3bc1df7e557dda91089b539fe681807dedcf458850b02d8e7790")));
//////
//        String hash = sendTransaction("a11859ce23effc663a9460e332ca09bd812acc390497f8dc7542b6938e13f8d7", toAddress, new BigDecimal("6000000000000000000000000"), 500000000000L, 210000L);
////
//        try {
//            PlatonGetBalance platonGetBalance = web3j.platonGetBalance("0x" + toAddress, DefaultBlockParameterName.LATEST).send();
//            PlatonGetBalance platonGetBalance2 = web3j.platonGetBalance("0x" + fromAddress, DefaultBlockParameterName.LATEST).send();
////
//            System.out.println(platonGetBalance.getBalance().longValue());
//            System.out.println(platonGetBalance2.getBalance().longValue());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            PlatonBlock platonBlock = web3j.platonGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
//            System.out.println(platonBlock.getBlock().getNumberRaw());
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
