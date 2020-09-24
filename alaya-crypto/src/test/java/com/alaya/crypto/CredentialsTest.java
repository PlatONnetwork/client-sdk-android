package com.alaya.crypto;

import com.alaya.crypto.addressconvert.AddressManager;
import com.alaya.crypto.addressconvert.bech32.AddressBehavior;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CredentialsTest {

    @Before
    public void init(){
        WalletApplication.init(WalletApplication.TESTNET, AddressManager.ADDRESS_TYPE_BECH32,AddressBehavior.CHANNLE_ALAYA);
    }

    @Test
    public void testCredentialsFromString() {
        Credentials credentials = Credentials.create(SampleKeys.KEY_PAIR);
        System.out.println("------ address:" + credentials.getAddress());
        verify(credentials);
    }

    @Test
    public void testCredentialsFromECKeyPair() {
        Credentials credentials = Credentials.create(
                SampleKeys.PRIVATE_KEY_STRING, SampleKeys.PUBLIC_KEY_STRING);
        System.out.println("------ address:" + credentials.getAddress());
        verify(credentials);
    }

    private void verify(Credentials credentials) {
       // assertThat(credentials.getAddress(), is(SampleKeys.ADDRESS));
        assertThat(credentials.getEcKeyPair(), is(SampleKeys.KEY_PAIR));
    }
}
