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
import org.web3j.platon.bean.UpdateStakingParam;
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
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
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
    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.9.190:1000/rpc"));

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
        credentials = Credentials.create("0x961bda67790709a870dc2cf03bcf7448b6803dff152443a62ad5065473432034");

        stakingContract = StakingContract.load(
                web3j,
                credentials, 101);

    }


    @Test
    public void staking() throws Exception {

//        String fromAddress = Keys.getAddress(ECKeyPair.create(Numeric.toBigIntNoPrefix("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b")));
//        String toAddress = "0xFAc238aD67f9ff52466dbaB7CaC44861cd2f8A70";
//
//        BigDecimal amount = Convert.toVon(BigDecimal.valueOf(100), Convert.Unit.LAT);
//////
//        String hash = sendTransaction("a689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b", toAddress, amount, 500000000000L, 210000L);
////
//        System.out.println(hash);
////
//        PlatonGetTransactionReceipt platonGetTransactionReceipt = web3j.platonGetTransactionReceipt(hash).send();
////
//        try {
//            PlatonGetBalance platonGetBalance = web3j.platonGetBalance(toAddress, DefaultBlockParameterName.LATEST).send();
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
                    .setProcessVersion(stakingContract.getProgramVersion())
                    .setBlsPubKey(stakingContract.getAdminSchnorrNIZKProve())
                    .build()).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateStakingInfo() {
        try {
            BaseResponse baseResponse = stakingContract.updateStakingInfo(new UpdateStakingParam.Builder()
                    .setNodeId(nodeId)
                    .setBenifitAddress(benifitAddress)
                    .setExternalId(externalId)
                    .setNodeName(nodeName)
                    .setWebSite("https://www.github.com/")
                    .setDetails(details)
                    .build()).send();
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

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 203, credentials);
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
