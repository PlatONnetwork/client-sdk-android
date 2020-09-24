package com.alaya.protocol.parity.methods.response;

import java.util.ArrayList;

import com.alaya.protocol.core.Response;

/**
 * parity_ListRecentDapps.
 */
public class ParityListRecentDapps extends Response<ArrayList<String>> {
    public ArrayList<String> getDappsIds() {
        return getResult();
    }
}
