package org.web3j.protocol.platon;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.PlatOnTypeEncoder;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.SampleKeys;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.CustomStaticArray;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.bean.Node;
import org.web3j.platon.bean.RestrictingPlan;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.platon.contracts.StakingContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.tx.gas.DefaultWasmGasProvider;
import org.web3j.utils.Numeric;
import org.web3j.utils.PlatOnUtil;
import org.web3j.utils.RLPElement;
import org.web3j.utils.RLPList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class StakingContractTest {

    private static final int OFFSET_SHORT_ITEM = 0x80;
    private static final int OFFSET_LONG_ITEM = 0xb7;
    private static final int SIZE_THRESHOLD = 56;

    private Web3j web3j = Web3jFactory.build(new HttpService("http://10.10.8.200:6789"));

    private StakingContract stakingContract;

    private String nodeId = "1f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e429";
    private String benifitAddress = "12c171900f010b17e969702efa044d077e868082";
    private String externalId = "111111";
    private String nodeName = "platon";
    private String websites = "https://www.test.network";
    private String details = "supper node";

    private String address = "0x493301712671Ada506ba6Ca7891F436D29185821";
    private Credentials credentials;

    @Before
    public void init() {

        credentials = Credentials.create("0xa7f1d33a30c1e8b332443825f2209755c52086d0a88b084301a6727d9f84bf32");

        stakingContract = StakingContract.load(
                web3j,
                credentials,
                new DefaultWasmGasProvider(), "100");

//        try {
//            PlatonGetBalance platonGetBalance = web3j.platonGetBalance(address, DefaultBlockParameterName.LATEST).send();
//            System.out.println(platonGetBalance.getBalance());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    @Test
    public void staking() {

        String nodeId = "7dd1e2474a49aa19cd2ef7f806f28d97595674723fc73c38602df12c758126e9eee4e25d44d700ea06aad0eacf3990d7f8f7a4f41350eebbf644cc406305943c";
        String stakingAmount = "1000000000000000000000000000";
        StakingAmountType stakingAmountType = StakingAmountType.RESTRICTING_AMOUNT_TYPE;
        String benifitAddress = "0x5e57ae97e714abe990c882377aaf9c57f4ea363b";
        String externalId = "liyf-test-id";
        String nodeName = "liyf-test";
        String webSite = "www.baidu.com";
        String details = "details";
        String nodeVersion = "1792";

        try {
            BaseResponse baseResponse = stakingContract.staking(nodeId, new BigInteger(stakingAmount), stakingAmountType, benifitAddress, externalId, nodeName, webSite, details, new BigInteger(nodeVersion)).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String data = "f8a7838203e88180959412c171900f010b17e969702efa044d077e868082b842b8401f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e42987863131313131318786706c61746f6e999868747470733a2f2f7777772e746573742e6e6574776f726b8c8b737570706572206e6f64658b8ad3c21bcecceda100000083820700";
//
//        RLPList rlpList = PlatOnUtil.invokeDecode(Numeric.hexStringToByteArray(data));
//
//        List<RLPElement> rlpElementList = (List<RLPElement>) rlpList.get(0);
//
//        for (RLPElement rlpElement : rlpElementList) {
//            String text = null;
//            try {
//                text = new String(rlpElement.getRLPData(),"utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            System.out.println(text);
//        }


    }

    @Test
    public void updateStakingInfo() {
        try {
            BaseResponse baseResponse = stakingContract.updateStakingInfo(nodeId, benifitAddress, externalId, nodeName, "www.baidu.com", details).send();
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
}
