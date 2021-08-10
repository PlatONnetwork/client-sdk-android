package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletApplication;
import org.web3j.crypto.addressconvert.AddressManager;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.VoteOption;
import org.web3j.platon.bean.ProgramVersion;
import org.web3j.platon.bean.Proposal;
import org.web3j.platon.bean.TallyResult;
import org.web3j.platon.contracts.ProposalContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.AdminProgramVersion;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.List;

public class ProposalContractTest {

    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.145:6789"));
    private String nodeId = "77fffc999d9f9403b65009f1eb27bae65774e2d8ea36f7b20a89f82642a5067557430e6edfe5320bb81c3666a19cf4a5172d6533117d7ebcd0f2c82055499050";
    long chainId = 103;
    private Credentials credentials;
    private ProposalContract proposalContract;
    //private String pIDID = "1234567890";
    private String pIDID = "1589266662335";
    private String proposalId = "0x2d4d674ac4b7c75f98b406e0136a473115ef85c4377542db231295783b56cc0f";


    @Before
    public void init() {
        WalletApplication.init(AddressManager.ADDRESS_TYPE_BECH32, "lfp");
        credentials = Credentials.create("0x690a32ceb7eab4131f7be318c1672d3b9b2dadeacba20b99432a7847c1e926e0");
        proposalContract = ProposalContract.load(web3j, credentials, chainId);
    }

    /**
     * 查询提案列表
     */
    @Test
    public void listProposal() {
        try {
            BaseResponse<List<Proposal>> baseResponse = proposalContract.getProposalList().send();
            List<Proposal> proposalList = baseResponse.data;
            System.out.println(proposalList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交文本提案
     */
    @Test
    public void submitTextProposal() {
        try {
 /*           PlatonBlock ethBlock =
                    web3j.platonGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            BigInteger blockNumber = ethBlock.getBlock().getNumber();
            System.out.println(blockNumber);*/

            Proposal proposal = Proposal.createSubmitTextProposalParam(nodeId, pIDID);
            BaseResponse baseResponse = proposalContract.submitProposal(proposal).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提交(版本)提案
     */
    @Test
    public void submitVersionProposal() {
        try {
            PlatonBlock ethBlock =
                    web3j.platonGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            BigInteger blockNumber = ethBlock.getBlock().getNumber();
            BigInteger endVoltingBlock = blockNumber.divide(BigInteger.valueOf(200)).multiply(BigInteger.valueOf(200)).add(BigInteger.valueOf(200).multiply(BigInteger.valueOf(10))).subtract(BigInteger.valueOf(10));
            BigInteger activeBlock = endVoltingBlock.add(BigInteger.valueOf(10)).add(BigInteger.valueOf(1000));

            BaseResponse baseResponse = proposalContract.submitProposal(Proposal.createSubmitVersionProposalParam(nodeId, pIDID, BigInteger.valueOf(5000), endVoltingBlock)).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 提交(取消)提案
     */
    @Test
    public void submitCancelProposal() {
        try {

            Proposal proposal = Proposal.createSubmitCancelProposalParam(nodeId, pIDID, BigInteger.valueOf(100000), proposalId);
            BaseResponse baseResponse = proposalContract.submitProposal(proposal).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询已生效的版本
     */
    @Test
    public void getActiveVersion() {
        try {
            BaseResponse baseResponse = proposalContract.getActiveVersion().send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 给提案投票
     */
    @Test
    public void vote() {
        try {

            Request<?, AdminProgramVersion> req = web3j.getProgramVersion();
            AdminProgramVersion adminProgramVersion = req.send();
            ProgramVersion programVersion = adminProgramVersion.getAdminProgramVersion();
            System.out.println("---programVersion:"  + programVersion.toString());

            BaseResponse baseResponse = proposalContract.vote(proposalContract.getProgramVersion(), VoteOption.YEAS, proposalId, nodeId).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 版本声明
     */
    @Test
    public void declareVersion() {
        try {

            Request<?, AdminProgramVersion> req = web3j.getProgramVersion();
            AdminProgramVersion adminProgramVersion = req.send();
            ProgramVersion programVersion = adminProgramVersion.getAdminProgramVersion();
            System.out.println("---programVersion:"  + programVersion.toString());

            BaseResponse baseResponse = proposalContract.declareVersion(programVersion, nodeId).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询提案
     */
    @Test
    public void getProposal() {
        try {
            BaseResponse<Proposal> baseResponse = proposalContract.getProposal(proposalId).send();
            System.out.println(baseResponse.data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询提案结果
     */
    @Test
    public void getTallyResult() {
        try {
            BaseResponse<TallyResult> baseResponse = proposalContract.getTallyResult(proposalId).send();
            System.out.println(baseResponse.data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  /*  @Test
    public void getProgramVersion() {
        try {
            ProgramVersion programVersion = proposalContract.getProgramVersion();
           // ProgramVersion programVersion = web3j.getProgramVersion();
           // System.out.println(programVersion.getVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
