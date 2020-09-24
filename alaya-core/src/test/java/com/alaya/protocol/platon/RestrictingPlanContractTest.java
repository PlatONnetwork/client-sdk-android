package com.alaya.protocol.platon;

import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletApplication;
import com.alaya.crypto.addressconvert.AddressManager;
import com.alaya.crypto.addressconvert.bech32.AddressBehavior;
import com.alaya.platon.BaseResponse;
import com.alaya.platon.bean.RestrictingItem;
import com.alaya.platon.bean.RestrictingPlan;
import com.alaya.platon.contracts.RestrictingPlanContract;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.Web3jFactory;
import com.alaya.protocol.http.HttpService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RestrictingPlanContractTest {


    private Web3j web3j = Web3jFactory.build(new HttpService("http://192.168.120.142:6789"));
    long chainId = 103;

    private RestrictingPlanContract restrictingPlanContract;

    private Credentials credentials;
    private Credentials deleteCredentials;

    @Before
    public void init() {
        WalletApplication.init(WalletApplication.TESTNET, AddressManager.ADDRESS_TYPE_BECH32, AddressBehavior.CHANNLE_ALAYA);
        credentials = Credentials.create("0xa689f0879f53710e9e0c1025af410a530d6381eebb5916773195326e123b822b");
        deleteCredentials = Credentials.create("0x6fe419582271a4dcf01c51b89195b77b228377fde4bde6e04ef126a0b4373f79");
        restrictingPlanContract = RestrictingPlanContract.load(web3j, credentials, chainId);
    }

    @Test
    public void createRestrictingPlan() {

        List<RestrictingPlan> restrictingPlans = new ArrayList<>();
        restrictingPlans.add(new RestrictingPlan(BigInteger.valueOf(100), new BigInteger("1000000000000000000000")));
        try {
            BaseResponse baseResponse = restrictingPlanContract.createRestrictingPlan(deleteCredentials.getAddress(), restrictingPlans).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getRestrictingPlanInfo() {
        try {
            BaseResponse<RestrictingItem> baseResponse = restrictingPlanContract.getRestrictingInfo(deleteCredentials.getAddress()).send();
            System.out.println(baseResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
