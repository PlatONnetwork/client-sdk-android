package org.web3j.platon.bean;

import com.alibaba.fastjson.annotation.JSONField;

import org.spongycastle.util.encoders.Hex;
import org.web3j.utils.ByteUtil;
import org.web3j.utils.RLPList;

import java.math.BigInteger;

public class Node {

    @JSONField(name = "NodeId")
    private String nodeId;
    @JSONField(name = "StakingAddress")
    private String stakingAddress;
    @JSONField(name = "BenifitAddress")
    private String benifitAddress;

    @JSONField(name = "StakingTxIndex")
    private BigInteger stakingTxIndex;
    @JSONField(name = "ProcessVersion")
    private BigInteger processVersion;
    @JSONField(name = "Status")
    private BigInteger status;
    @JSONField(name = "StakingEpoch")
    private BigInteger stakingEpoch;
    @JSONField(name = "StakingBlockNum")
    private BigInteger stakingBlockNum;
    @JSONField(name = "Shares")
    private BigInteger shares;
    @JSONField(name = "Released")
    private BigInteger released;
    @JSONField(name = "ReleasedHes")
    private BigInteger releasedHes;
    @JSONField(name = "RestrictingPlan")
    private BigInteger restrictingPlan;
    @JSONField(name = "RestrictingPlanHes")
    private BigInteger restrictingPlanHes;

    private Description description;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getStakingAddress() {
        return stakingAddress;
    }

    public void setStakingAddress(String stakingAddress) {
        this.stakingAddress = stakingAddress;
    }

    public String getBenifitAddress() {
        return benifitAddress;
    }

    public void setBenifitAddress(String benifitAddress) {
        this.benifitAddress = benifitAddress;
    }

    public BigInteger getStakingTxIndex() {
        return stakingTxIndex;
    }

    public void setStakingTxIndex(BigInteger stakingTxIndex) {
        this.stakingTxIndex = stakingTxIndex;
    }

    public BigInteger getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(BigInteger processVersion) {
        this.processVersion = processVersion;
    }

    public BigInteger getStatus() {
        return status;
    }

    public void setStatus(BigInteger status) {
        this.status = status;
    }

    public BigInteger getStakingEpoch() {
        return stakingEpoch;
    }

    public void setStakingEpoch(BigInteger stakingEpoch) {
        this.stakingEpoch = stakingEpoch;
    }

    public BigInteger getStakingBlockNum() {
        return stakingBlockNum;
    }

    public void setStakingBlockNum(BigInteger stakingBlockNum) {
        this.stakingBlockNum = stakingBlockNum;
    }

    public BigInteger getShares() {
        return shares;
    }

    public void setShares(BigInteger shares) {
        this.shares = shares;
    }

    public BigInteger getReleased() {
        return released;
    }

    public void setReleased(BigInteger released) {
        this.released = released;
    }

    public BigInteger getReleasedHes() {
        return releasedHes;
    }

    public void setReleasedHes(BigInteger releasedHes) {
        this.releasedHes = releasedHes;
    }

    public BigInteger getRestrictingPlan() {
        return restrictingPlan;
    }

    public void setRestrictingPlan(BigInteger restrictingPlan) {
        this.restrictingPlan = restrictingPlan;
    }

    public BigInteger getRestrictingPlanHes() {
        return restrictingPlanHes;
    }

    public void setRestrictingPlanHes(BigInteger restrictingPlanHes) {
        this.restrictingPlanHes = restrictingPlanHes;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public Node() {
    }

    public Node(RLPList rlpCandidate) {

        this.nodeId = Hex.toHexString(rlpCandidate.get(0).getRLPData());
        this.stakingAddress = Hex.toHexString(rlpCandidate.get(1).getRLPData());
        this.benifitAddress = Hex.toHexString(rlpCandidate.get(2).getRLPData());

        this.stakingTxIndex = ByteUtil.byteArrayToBigInteger(rlpCandidate.get(3).getRLPData());
        this.processVersion = ByteUtil.byteArrayToBigInteger(rlpCandidate.get(4).getRLPData());
        this.status = ByteUtil.byteArrayToBigInteger(rlpCandidate.get(5).getRLPData());
        this.stakingEpoch = ByteUtil.byteArrayToBigInteger(rlpCandidate.get(6).getRLPData());
        this.stakingBlockNum = ByteUtil.byteArrayToBigInteger(rlpCandidate.get(7).getRLPData());

        this.shares = ByteUtil.bytesToBigInteger(rlpCandidate.get(8).getRLPData());
        this.released = ByteUtil.bytesToBigInteger(rlpCandidate.get(9).getRLPData());
        this.releasedHes = ByteUtil.bytesToBigInteger(rlpCandidate.get(10).getRLPData());
        this.restrictingPlan = ByteUtil.bytesToBigInteger(rlpCandidate.get(11).getRLPData());
        this.restrictingPlanHes = ByteUtil.bytesToBigInteger(rlpCandidate.get(12).getRLPData());
        this.description = new Description((RLPList) rlpCandidate.get(13));
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "nodeId='" + nodeId + '\'' +
                ", stakingAddress='" + stakingAddress + '\'' +
                ", benifitAddress='" + benifitAddress + '\'' +
                ", stakingTxIndex=" + stakingTxIndex +
                ", processVersion=" + processVersion +
                ", status=" + status +
                ", stakingEpoch=" + stakingEpoch +
                ", stakingBlockNum=" + stakingBlockNum +
                ", shares=" + shares +
                ", released=" + released +
                ", releasedTmp=" + releasedHes +
                ", lockRepo=" + restrictingPlan +
                ", lockRepoTmp=" + restrictingPlanHes +
                ", description=" + description +
                '}';
    }

    class Description {
        private String externalId;
        private String nodeName;
        private String website;
        private String details;

        public Description(RLPList rlpList) {
            this.externalId = new String(rlpList.get(0).getRLPData());
            this.nodeName = new String(rlpList.get(1).getRLPData());
            this.website = new String(rlpList.get(2).getRLPData());
            this.details = new String(rlpList.get(3).getRLPData());
        }

        @Override
        public String toString() {
            return "Description{" +
                    "externalId='" + externalId + '\'' +
                    ", nodeName='" + nodeName + '\'' +
                    ", website='" + website + '\'' +
                    ", details='" + details + '\'' +
                    '}';
        }
    }
}
