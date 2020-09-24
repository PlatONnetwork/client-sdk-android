package com.alaya.protocol.admin;

import com.alaya.protocol.Web3jService;

public class AdminFactory {
    public static Admin build(Web3jService web3jService) {
        return new JsonRpc2_0Admin(web3jService);
    }
}
