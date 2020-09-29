package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.PlatOnTypeEncoder;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.crypto.*;
import org.web3j.crypto.addressconvert.AddressManager;

import org.web3j.crypto.addressconvert.bech32.AddressBehavior;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.bean.Node;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.platon.bean.StakingParam;
import org.web3j.platon.bean.UpdateStakingParam;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.*;
import org.web3j.protocol.core.methods.response.*;
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


    private String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
    long chainId = 103;
    String blsPubKey = "5ccd6b8c32f2713faa6c9a46e5fb61ad7b7400e53fabcbc56bdc0c16fbfffe09ad6256982c7059e7383a9187ad93a002a7cda7a75d569f591730481a8b91b5fad52ac26ac495522a069686df1061fc184c31771008c1fedfafd50ae794778811";
    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.145:6789"));


    private Credentials superCredentials;
    private Credentials stakingCredentials;
    private Credentials benefitCredentials;
    private StakingContract stakingContract;

    @Before
    public void init() throws Exception {

        WalletApplication.init(WalletApplication.TESTNET, AddressManager.ADDRESS_TYPE_BECH32, AddressBehavior.CHANNLE_PLATON);

        superCredentials = Credentials.create("0xa689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
        //System.out.println("superCredentials balance="+ web3j.platonGetBalance(superCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

        stakingCredentials = Credentials.create("0x690a32ceb7eab4131f7be318c1672d3b9b2dadeacba20b99432a7847c1e926e0");
        //System.out.println("stakingCredentials address="+ stakingCredentials.getAddress());
        //System.out.println("stakingCredentials balance="+ web3j.platonGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

        benefitCredentials = Credentials.create("0x3581985348bffd03b286b37712165f7addf3a8d907b25efc44addf54117e9b91");
        //System.out.println("benefitCredentials balance="+ web3j.platonGetBalance(benefitCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());

        stakingContract = StakingContract.load(web3j,stakingCredentials, chainId);
    }

  /*  @Test
    public void transfer() throws Exception {
        Transfer.sendFunds(web3j, superCredentials, String.valueOf(chainId), stakingCredentials.getAddress(), new BigDecimal("10000000"), Convert.Unit.LAT).send();
        System.out.println("stakingCredentials balance="+ web3j.platonGetBalance(stakingCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance());
    }*/


    @Test
    public void staking() throws Exception {

        try {
            StakingAmountType stakingAmountType = StakingAmountType.FREE_AMOUNT_TYPE;
            String benifitAddress = benefitCredentials.getAddress();
            String externalId = "";
            String nodeName = "chendai-node3";
            String webSite = "www.baidu.com";
            String details = "chendai-node3-details";
            BigDecimal stakingAmount = Convert.toVon("5000000", Convert.Unit.LAT);
            BigInteger rewardPer = BigInteger.valueOf(1000L);

            Request<?, AdminProgramVersion> req = web3j.getProgramVersion();
            AdminProgramVersion adminProgramVersion = req.send();
            ProgramVersion programVersion = adminProgramVersion.getAdminProgramVersion();
            System.out.println("---programVersion:"  + programVersion.toString());

            PlatonSendTransaction platonSendTransaction = stakingContract.stakingReturnTransaction(new StakingParam.Builder()
                    .setNodeId(nodeId)
                    .setAmount(stakingAmount.toBigInteger())
                    .setStakingAmountType(stakingAmountType)
                    .setBenifitAddress(benifitAddress)
                    .setExternalId(externalId)
                    .setNodeName(nodeName)
                    .setWebSite(webSite)
                    .setDetails(details)
                    .setBlsPubKey(blsPubKey)
                   // .setProcessVersion(web3j.getProgramVersion().send().getAdminProgramVersion())
                    .setProcessVersion(programVersion)
                    .setBlsProof(web3j.getSchnorrNIZKProve().send().getAdminSchnorrNIZKProve())
                    .setRewardPer(rewardPer)
                    .build()).send();
            BaseResponse baseResponse = stakingContract.getStakingResult(platonSendTransaction).send();
            System.out.println(baseResponse.toString());  // 438552‬
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updateStakingInfo() {
        try {
            String benifitAddress = benefitCredentials.getAddress();
            String externalId = "";
            String nodeName = "chendai-node3-u";
            String webSite = "www.baidu.com-u";
            String details = "chendai-node3-details-u";
            BigInteger rewardPer = BigInteger.valueOf(2000L);

            BaseResponse baseResponse = stakingContract.updateStakingInfo(new UpdateStakingParam.Builder()
                    .setNodeId(nodeId)
                    .setBenifitAddress(benifitAddress)
                    .setExternalId(externalId)
                    .setNodeName(nodeName)
                    .setWebSite(webSite)
                    .setDetails(details)
                    .setRewardPer(rewardPer)
                    .build()).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addStaking() {
        try {
            BigDecimal addStakingAmount = Convert.toVon("4000000", Convert.Unit.LAT).add(new BigDecimal("999999999999999998"));
            BaseResponse baseResponse = stakingContract.addStaking(nodeId, StakingAmountType.FREE_AMOUNT_TYPE,addStakingAmount.toBigInteger()).send();
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
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public String sendTransaction(String privateKey, String toAddress, BigDecimal amount, long gasPrice, long gasLimit) {
//
//        BigInteger GAS_PRICE = BigInteger.valueOf(gasPrice);
//        BigInteger GAS_LIMIT = BigInteger.valueOf(gasLimit);
//
//        Credentials credentials = Credentials.create(privateKey);
//
//        try {
//
//            List<RlpType> result = new ArrayList<>();
//            result.add(RlpString.create(Numeric.hexStringToByteArray(PlatOnTypeEncoder.encode(new Int64(0)))));
//            String txType = Hex.toHexString(RlpEncoder.encode(new RlpList(result)));
//
//            RawTransaction rawTransaction = RawTransaction.createTransaction(getNonce(), GAS_PRICE, GAS_LIMIT, toAddress, amount.toBigInteger(),
//                    txType);
//
//            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 203, credentials);
//            String hexValue = Numeric.toHexString(signedMessage);
//
//            PlatonSendTransaction transaction = web3j.platonSendRawTransaction(hexValue).send();
//
//            return transaction.getTransactionHash();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    protected BigInteger getNonce() throws IOException {
//        PlatonGetTransactionCount ethGetTransactionCount = web3j.platonGetTransactionCount(
//                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();
//
//        if (ethGetTransactionCount.getTransactionCount().intValue() == 0) {
//            ethGetTransactionCount = web3j.platonGetTransactionCount(
//                    credentials.getAddress(), DefaultBlockParameterName.LATEST).send();
//        }
//
//        return ethGetTransactionCount.getTransactionCount();
//    }

    @Test
    public void getPackageReward() {
        try {
            BaseResponse<BigInteger> baseResponse = stakingContract.getPackageReward().send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getStakingReward() {
        try {
            BaseResponse<BigInteger> baseResponse = stakingContract.getStakingReward().send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAvgPackTime() {
        try {
            BaseResponse<BigInteger> baseResponse = stakingContract.getAvgPackTime().send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
