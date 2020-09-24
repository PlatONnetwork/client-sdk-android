package com.alaya.ens;

import org.junit.Test;

import com.alaya.tx.ChainId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static com.alaya.ens.Contracts.MAINNET;
import static com.alaya.ens.Contracts.RINKEBY;
import static com.alaya.ens.Contracts.ROPSTEN;
import static com.alaya.ens.Contracts.resolveRegistryContract;

public class ContractsTest {

    @Test
    public void testResolveRegistryContract() {
        assertThat(resolveRegistryContract(ChainId.MAINNET + ""), is(MAINNET));
        assertThat(resolveRegistryContract(ChainId.ROPSTEN + ""), is(ROPSTEN));
        assertThat(resolveRegistryContract(ChainId.RINKEBY + ""), is(RINKEBY));
    }

    @Test(expected = EnsResolutionException.class)
    public void testResolveRegistryContractInvalid() {
        resolveRegistryContract(ChainId.NONE + "");
    }
}
