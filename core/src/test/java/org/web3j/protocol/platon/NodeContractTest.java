package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletApplication;
import org.web3j.crypto.bech32.AddressBehavior;
import org.web3j.crypto.bech32.AddressManager;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.Node;
import org.web3j.platon.contracts.NodeContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultWasmGasProvider;

import java.util.List;

public class NodeContractTest {

    private String nodeId = "e0b6af6cc2e10b2b74540b87098083d48343805a3ff09c655eab0b20dba2b2851aea79ee75b6e150bde58ead0be03ee4a8619ea1dfaf529cbb8ff55ca23531ed";
    long chainId = 103;
    String blsPubKey = "3238f5217fb00faaab8a938423fa523906e5fdb047b31382d36cf3eaee4617ad7ce9564507a0e3741d924adc65ba3d10bca5b72cacec9beebd0ddb7d292e412659af8fc1faf72d278fe252451fc3d2b594533facdba2eb485fadeab9eb229100";
    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.142:6789"));

    private Credentials credentials;

    private NodeContract nodeContract;

    @Before
    public void init() {
        WalletApplication.init(WalletApplication.TESTNET, AddressManager.ADDRESS_TYPE_BECH32,AddressBehavior.CHANNLE_PLATON);
        credentials = Credentials.create("0x690a32ceb7eab4131f7be318c1672d3b9b2dadeacba20b99432a7847c1e926e0");
        nodeContract = NodeContract.load(web3j,credentials, chainId);
    }

    @Test
    public void getVerifierList() {
        try {
            BaseResponse<List<Node>> baseResponse = nodeContract.getVerifierList().send();
            List<Node> nodeList = baseResponse.data;
            System.out.println(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getValidatorList() {
        try {
            BaseResponse<List<Node>> baseResponse = nodeContract.getValidatorList().send();
            List<Node> nodeList = baseResponse.data;
            System.out.println(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getCandidateList() {
        try {
            BaseResponse<List<Node>> baseResponse = nodeContract.getCandidateList().send();
            List<Node> nodeList = baseResponse.data;
            System.out.println(nodeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
