package org.web3j.ticket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultWasmGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;

public class CandidateContractTest {

    private Logger logger = LoggerFactory.getLogger(CandidateContractTest.class);

    private static final Credentials CREDENTIALS = loadCredentials("/admin.json");

    private static final Credentials OWNER_CREDENTIALS = loadCredentials("/owner.json");

    private CandidateContract contract;

    private CandidateContract ownerContract;

    private static Credentials loadCredentials(String walletPath) {
        Credentials credentials = null;
        try {
            URL url = CandidateContractTest.class.getClass().getResource(walletPath);
            String path = url.getPath();
            credentials = WalletUtils.loadCredentials("88888888", path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        }
        return credentials;
    }

    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.9.76:7794"));

    @Before
    public void init() {
        contract = CandidateContract.load(
                web3j,
                CREDENTIALS,
                new DefaultWasmGasProvider()
        );

        ownerContract = CandidateContract.load(
                web3j,
                OWNER_CREDENTIALS,
                new DefaultWasmGasProvider()
        );
    }

    /**
     * Query verifiers list
     *
     * @throws Exception
     */
    @Test
    public void VerifiersList() throws Exception {

        String result = contract.VerifiersList().send();
        logger.debug("VerifiersList:{}", result);
    }


    /**
     * Query candidate details
     *
     * @throws Exception
     */
    @Test
    public void CandidateList() throws Exception {

        String nodeInfoList = contract.CandidateList().send();
        logger.debug("CandidateList:{}", nodeInfoList);
    }


    /**
     * GetBatchCandidateDetail
     *
     * @throws Exception
     */
    @Test
    public void GetBatchCandidateDetail() throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        //节点1
        stringBuilder.append("0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3");
        //分割
        stringBuilder.append(":");
        //节点2
        stringBuilder.append("0xc0e69057ec222ab257f68ca79d0e74fdb720261bcdbdfa83502d509a5ad032b29d57c6273f1c62f51d689644b4d446064a7c8279ff9abd01fa846a3555395535");

        //调用接口
        String result = contract.GetBatchCandidateDetail(stringBuilder.toString()).send();
        logger.debug("GetBatchCandidateDetail:{}", result);
    }

    /**
     * CandidateDetails
     *
     * @throws Exception
     */
    @Test
    public void CandidateDetails() throws Exception {

        //节点id
        String nodeId = "0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3";

        //调用接口
        String result = contract.CandidateDetails(nodeId).send();
        logger.debug("CandidateDetails:{}", result);
    }


    /**
     * CandidateWithdrawInfos
     *
     * @throws Exception
     */
    @Test
    public void CandidateWithdrawInfos() throws Exception {

        //节点id
        String nodeId = "0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3";

        //调用接口
        String result = contract.CandidateWithdrawInfos(nodeId).send();
        logger.debug("CandidateWithdrawInfos:{}", result);
    }


    /**
     * SetCandidateExtra
     *
     * @throws Exception
     */
    @Test
    public void SetCandidateExtra() throws Exception {

        //节点id
        String nodeId = "0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3";

        //附加数据
        JSONObject extra = new JSONObject();
        //节点名称
        extra.put("nodeName", "xxxx-noedeName");
        //节点logo
        extra.put("nodePortrait", "group2/M00/00/12/wKgJVlw0XSyAY78cAAH3BKJzz9Y83.jpeg");
        //机构简介
        extra.put("nodeDiscription", "xxxx-nodeDiscription1");
        //机构名称
        extra.put("nodeDepartment", "xxxx-nodeDepartment");
        //官网
        extra.put("officialWebsite", "xxxx-officialWebsite");


        //调用接口
        TransactionReceipt receipt = ownerContract.SetCandidateExtra(nodeId, extra.toJSONString()).send();
        logger.debug("TransactionReceipt:{}", JSON.toJSONString(receipt));

        //查看返回event
        List<SetCandidateExtraEventEventResponse> events = contract.getSetCandidateExtraEventEvents(receipt);
        for (SetCandidateExtraEventEventResponse event : events) {
            logger.debug("event:{}", JSON.toJSONString(event.param1));
        }
    }

    /**
     * CandidateWithdraw
     *
     * @throws Exception
     */
    @Test
    public void CandidateWithdraw() throws Exception {

        //节点id
        String nodeId = "0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3";

        //调用接口
        TransactionReceipt receipt = contract.CandidateWithdraw(nodeId).send();
        logger.debug("TransactionReceipt:{}", JSON.toJSONString(receipt));

        //查看返回event
        List<CandidateWithdrawEventEventResponse> events = contract.getCandidateWithdrawEventEvents(receipt);
        for (CandidateWithdrawEventEventResponse event : events) {
            logger.debug("event:{}", JSON.toJSONString(event.param1));
        }
    }


    /**
     * CandidateApplyWithdraw
     *
     * @throws Exception
     */
    @Test
    public void CandidateApplyWithdraw() throws Exception {

        //节点id
        String nodeId = "0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3";
        //退款金额, 单位 wei
        BigInteger value = BigInteger.valueOf(100);

        //调用接口
        TransactionReceipt receipt = ownerContract.CandidateApplyWithdraw(nodeId, value).send();
        logger.debug("TransactionReceipt:{}", JSON.toJSONString(receipt));

        //查看返回event
        List<CandidateApplyWithdrawEventEventResponse> events = ownerContract.getCandidateApplyWithdrawEventEvents(receipt);
        for (CandidateApplyWithdrawEventEventResponse event : events) {
            logger.debug("event:{}", JSON.toJSONString(event.param1));
        }
    }


    /**
     * CandidateDeposit
     *
     * @throws Exception
     */
    @Test
    public void CandidateDeposit() throws Exception {

        //节点id
        String nodeId = "0x6bad331aa2ec6096b2b6034570e1761d687575b38c3afc3a3b5f892dac4c86d0fc59ead0f0933ae041c0b6b43a7261f1529bad5189be4fba343875548dc9efd3";
        //质押金退款地址
        String owner = "0xf8f3978c14f585c920718c27853e2380d6f5db36";
        //出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
        BigInteger fee = BigInteger.valueOf(500L);
        //节点IP
        String host = "192.168.9.76";
        //节点P2P端口号
        String port = "26794";
        //附加数据
        JSONObject extra = new JSONObject();
        //节点名称
        extra.put("nodeName", "xxxx-noedeName");
        //节点logo
        extra.put("nodePortrait", "http://192.168.9.86:8082/group2/M00/00/00/wKgJVlr0KDyAGSddAAYKKe2rswE261.png");
        //机构简介
        extra.put("nodeDiscription", "xxxx-nodeDiscription中国人");
        //机构名称
        extra.put("nodeDepartment", "xxxx-nodeDepartment");
        //官网
        extra.put("officialWebsite", "https://www.platon.network/");

        //质押金额, 单位 wei
        BigInteger value = BigInteger.valueOf(100);

        //调用接口
        TransactionReceipt receipt = contract.CandidateDeposit(nodeId, owner, fee, host, port, extra.toJSONString(), value).send();
        logger.debug("CandidateDeposit TransactionReceipt:{}", JSON.toJSONString(receipt));

        //查看返回event
        List<CandidateDepositEventEventResponse> events = contract.getCandidateDepositEventEvents(receipt);
        for (CandidateDepositEventEventResponse event : events) {
            logger.debug("CandidateDeposit event:{}", JSON.toJSONString(event.param1));
        }
    }
}
