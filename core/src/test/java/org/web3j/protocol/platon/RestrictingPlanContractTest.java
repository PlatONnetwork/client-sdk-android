package org.web3j.protocol.platon;

import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.bean.RestrictingItem;
import org.web3j.platon.bean.RestrictingPlan;
import org.web3j.platon.contracts.RestrictingPlanContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.PlatonGetBalance;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RestrictingPlanContractTest {


    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.9.190:1000/rpc"));

    private String address = "0x493301712671Ada506ba6Ca7891F436D29185821";
    private String benifitAddress = "0xd79af06736eceda68d5cf85736ff09b18bfedbb8";

    private RestrictingPlanContract restrictingPlanContract;

    private Credentials credentials;

    @Before
    public void init() {

        credentials = Credentials.create("0xdbf0a2d285c9668dafc908d5096b98d30746cdae7c2fd978b65c1e3d15abe5f9");

        restrictingPlanContract = RestrictingPlanContract.load(web3j, credentials, 103);
    }

    @Test
    public void createRestrictingPlan() {

        List<RestrictingPlan> restrictingPlans = new ArrayList<>();
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(100), new BigInteger("15000000000000000000")));
        try {
            BaseResponse baseResponse = restrictingPlanContract.createRestrictingPlan(benifitAddress, restrictingPlans).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRestrictingPlanInfo() {
        try {
            BaseResponse<RestrictingItem> baseResponse = restrictingPlanContract.getRestrictingInfo(benifitAddress).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
