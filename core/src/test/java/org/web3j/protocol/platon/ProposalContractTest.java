package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.ProposalType;
import org.web3j.platon.VoteOption;
import org.web3j.platon.bean.Proposal;
import org.web3j.platon.bean.TallyResult;
import org.web3j.platon.contracts.ProposalContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;

public class ProposalContractTest {

    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.76:6794"));
    private String nodeId = "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c";
    private Credentials credentials;
    private ProposalContract proposalContract;
    private String pIDID = "1234567890";

    @Before
    public void init() {

        credentials = Credentials.create("0xa7f1d33a30c1e8b332443825f2209755c52086d0a88b084301a6727d9f84bf32");

        proposalContract = ProposalContract.load(web3j,
                credentials, "100");
    }

    @Test
    public void listProposal() {
        try {
            BaseResponse<List<Proposal>> baseResponse = proposalContract.getProposalList().send();
            List<Proposal> proposalList = baseResponse.data;
            System.out.println(proposalList.get(0).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void submitTextProposal() {
        try {
            PlatonBlock ethBlock =
                    web3j.platonGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            BigInteger blockNumber = ethBlock.getBlock().getNumber();
            System.out.println(blockNumber);
            BaseResponse baseResponse = proposalContract.submitProposal(Proposal.createSubmitTextProposalParam(nodeId, pIDID)).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Test
    public void submitCancelProposal() {
        try {
            BaseResponse baseResponse = proposalContract.submitProposal(Proposal.createSubmitCancelProposalParam(nodeId, pIDID, BigInteger.valueOf(100000), "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c")).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getActiveVersion() {
        try {
            BaseResponse baseResponse = proposalContract.getActiveVersion().send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void vote() {
        try {
            BaseResponse baseResponse = proposalContract.vote("0xdb4c13f35902089051810ab39224f4a2ad6da0ad0ea9d949c471acbcc09b288a", "411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c", VoteOption.YEAS).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void declareVersion() {
        try {
            BaseResponse baseResponse = proposalContract.declareVersion("411a6c3640b6cd13799e7d4ed286c95104e3a31fbb05d7ae0004463db648f26e93f7f5848ee9795fb4bbb5f83985afd63f750dc4cf48f53b0e84d26d6834c20c").send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getProposal() {
        try {
            BaseResponse<Proposal> baseResponse = proposalContract.getProposal("0x2ceea9176087f6fe64162b8efb2d71ffd0cc0c0326b24738bb644e71db0d5cc6").send();
            System.out.println(baseResponse.data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getTallyResult() {
        try {
            BaseResponse<TallyResult> baseResponse = proposalContract.getTallyResult("0x2ceea9176087f6fe64162b8efb2d71ffd0cc0c0326b24738bb644e71db0d5cc6").send();
            System.out.println(baseResponse.data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getProgramVersion() {
        try {
            BaseResponse baseResponse = proposalContract.getProgramVersion().send();
            System.out.println(baseResponse.data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
