package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletApplication;

import org.web3j.crypto.addressconvert.AddressManager;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.StakingAmountType;
import org.web3j.platon.bean.Delegation;
import org.web3j.platon.bean.DelegationIdInfo;
import org.web3j.platon.contracts.DelegateContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.rlp.RlpDecoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.utils.Numeric;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

public class DelegateContractTest {

    private String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
    private String delegateAddress = "0xbfCAEc5286822434D59310E03B2F4F162A35CBDd";
    long chainId = 103;
    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.145:6789"));

    private Credentials credentials;

    private DelegateContract delegateContract;

    @Before
    public void init() {
        WalletApplication.init(AddressManager.ADDRESS_TYPE_BECH32, "lfp");
       //credentials = Credentials.create("0x9614c2b32f2d5d3421591ab3ffc03ac66c831fb6807b532f6e3a8e7aac31f1d9");
        credentials = Credentials.create("0x6fe419582271a4dcf01c51b89195b77b228377fde4bde6e04ef126a0b4373f79");
        delegateAddress = credentials.getAddress();
        delegateContract = DelegateContract.load(web3j, credentials, chainId);
    }

    @Test
    public void decode() throws UnsupportedEncodingException {

        String text = "f856838203ec8180b842b8401f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2840e4298b8ad3c21bcecceda1000000";

        RlpList rlpList = RlpDecoder.decode(Hex.decode(text));

        RlpList rl = (RlpList) rlpList.getValues().get(0);

        RlpString rlpType = (RlpString) rl.getValues().get(0);

        RlpString rlpTyp1 = (RlpString) rl.getValues().get(1);
        RlpString rlpTyp2 = (RlpString) rl.getValues().get(2);
        RlpString rlpTyp3 = (RlpString) rl.getValues().get(3);

        RlpList rlps = RlpDecoder.decode(rlpType.getBytes());
        BigInteger bigInteger = new BigInteger(((RlpString) rlps.getValues().get(0)).getBytes());

        RlpList rlps1 = RlpDecoder.decode(rlpTyp1.getBytes());
        BigInteger bigInteger1 = new BigInteger(1, ((RlpString) rlps1.getValues().get(0)).getBytes());

        RlpList rlps2 = RlpDecoder.decode(rlpTyp2.getBytes());
        String nodeId = Numeric.toHexString(((RlpString) rlps2.getValues().get(0)).getBytes());

        System.out.println(nodeId);

    }

    @Test
    public void delegateReturnTransaction() {
        try {
            PlatonSendTransaction platonSendTransaction = delegateContract.delegateReturnTransaction(nodeId, StakingAmountType.FREE_AMOUNT_TYPE, new BigInteger("10000000000000000000")).send();
            BaseResponse baseResponse = delegateContract.getDelegateResult(platonSendTransaction).send();
            System.out.println(baseResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delegate() {

        try {
            BaseResponse baseResponse = delegateContract.delegate(nodeId, StakingAmountType.FREE_AMOUNT_TYPE, new BigInteger("10000000000000000000")).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void unDelegate() {

        try {
            BaseResponse baseResponse = delegateContract.unDelegate(nodeId, BigInteger.valueOf(2360), new BigInteger("10000000000000000000")).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getDelegateInfo() {
       //有个疑问 BigInteger.valueOf(129518L)？？？
        try {
            BaseResponse<Delegation> baseResponse = delegateContract.getDelegateInfo(nodeId, delegateAddress, BigInteger.valueOf(129518L)).send();
            System.out.println(baseResponse.data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRelatedListByDelAddr() {
        try {
            BaseResponse<List<DelegationIdInfo>> baseResponse = delegateContract.getRelatedListByDelAddr(delegateAddress).send();
            System.out.println(baseResponse);
            DelegationIdInfo delegationIdInfo = baseResponse.data.get(0);
            System.out.println(delegationIdInfo.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
