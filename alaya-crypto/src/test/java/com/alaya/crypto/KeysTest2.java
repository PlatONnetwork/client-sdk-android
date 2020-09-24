package com.alaya.crypto;

import com.alaya.crypto.addressconvert.AddressManager;
import com.alaya.crypto.addressconvert.bech32.AddressBech32;
import com.alaya.crypto.addressconvert.bech32.AddressBehavior;
import com.alaya.crypto.addressconvert.bech32.Bech32Util;
import com.alaya.utils.Numeric;
import com.alaya.utils.Strings;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class KeysTest2 {

    private static final byte[] ENCODED;

    static {
        byte[] privateKey = Numeric.hexStringToByteArray(SampleKeys.PRIVATE_KEY_STRING);
        byte[] publicKey = Numeric.hexStringToByteArray(SampleKeys.PUBLIC_KEY_STRING);
        ENCODED = Arrays.copyOf(privateKey, privateKey.length + publicKey.length);
        System.arraycopy(publicKey, 0, ENCODED, privateKey.length, publicKey.length);
    }

    @Before
    public void init(){
        //初始化
        WalletApplication.init(WalletApplication.TESTNET, AddressManager.ADDRESS_TYPE_BECH32, AddressBehavior.CHANNLE_ALAYA);
    }

 /*   @Test
    public void testCreateSecp256k1KeyPair() throws Exception {
        KeyPair keyPair = Keys.createSecp256k1KeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertNotNull(privateKey);
        assertNotNull(publicKey);

        assertThat(privateKey.getEncoded().length, is(144));
        assertThat(publicKey.getEncoded().length, is(88));
    }*/

 /*   @Test
    public void testCreateEcKeyPair() throws Exception {
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        assertThat(ecKeyPair.getPublicKey().signum(), is(1));
        assertThat(ecKeyPair.getPrivateKey().signum(), is(1));
    }*/

    @Test
    public void testGetAddressString() {
        assertThat(Keys.getAddress(SampleKeys.PUBLIC_KEY_STRING),
                is(SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testGetAddressZeroPaddedAddress() {
        String publicKey =
                "0xa1b31be4d58a7ddd24b135db0da56a90fb5382077ae26b250e1dc9cd6232ce22"
                        + "70f4c995428bc76aa78e522316e95d7834d725efc9ca754d043233af6ca90113";
        assertThat(Keys.getAddress(publicKey),
                is("01c52b08330e05d731e38c856c1043288f7d9744"));
    }

    /**
     * 私钥 deb2bd10eedef6d89cd8fac224dc8f1bdd26ed1c4b5c513995efb1b33404db17
     * 公钥 b7a5a9f1ccc77c3d06856b99088f042be046be1ccc21053fe8c01547211dcf45762253c6560ec6421fbf8cc89c6cc4a19d85a2d12aa211ca406123129a4674ef
     * 地址 lac18suc3vdkz62znc5h6rhq3gfk6cx96a0qer6stg
     */
    @Test
    public void test() {

     /*   Credentials credentials = Credentials.create("deb2bd10eedef6d89cd8fac224dc8f1bdd26ed1c4b5c513995efb1b33404db17");
        String addressStr = credentials.getAddress();
        System.out.println("addressStr:" + addressStr);*/

        String address = "fc21fcfe23faea9e3776882f372ed65a7f8e1b64";
        System.out.println(Bech32Util.addressEncode(Bech32Util.HRP_ATP, address));


        try{
            String v = "lat18suc3vdkz62znc5h6rhq3gfk6cx96a0q484uwy";
            String v1 = "lax1dkyctq9yk6k2jdez3w0wrwncnsn5uaqs2p02jq";
             System.out.println(Bech32Util.addressDecodeHex(v1));
           // Bech32.addressDecode(v);
        }catch (Exception e){
              System.out.println("捕获" + e.getMessage());
             e.printStackTrace();
        }
    }


    @Test
    public void test2() {



        Credentials credentials = Credentials.create("deb2bd10eedef6d89cd8fac224dc8f1bdd26ed1c4b5c513995efb1b33404db17");
        String addressStr = credentials.getAddress();
        System.out.println("addressStr:" + addressStr);


        String originalAddress = "0xb89964e28b8f1b7b21d3c794fccc3a01807d442a";
        AddressBech32 addressBech32 = AddressManager.getInstance().executeEncodeAddress(originalAddress);
        System.out.println("encode address Bech32 testNet:" + addressBech32.getTestnet());
        System.out.println("encode address Bech32 mainnet:" + addressBech32.getMainnet());


        String str = "lat18suc3vdkz62znc5h6rhq3gfk6cx96a0q484uwy";
        AddressBech32 decodeAddress = AddressManager.getInstance().executeDecodeAddress(str);
        System.out.println("decode Address:" + decodeAddress);


    }

    /**
     * Helper for re-arranging bits into groups.
     */
    private static byte[] convertBits(final byte[] in, final int fromBits,
                                      final int toBits, final boolean pad)  {
        int acc = 0;
        int bits = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream(64);
        final int maxv = (1 << toBits) - 1;
        final int max_acc = (1 << (fromBits + toBits - 1)) - 1;
        for (int i = 0; i < in.length; i++) {
            int value = in[i] & 0xff;
            if ((value >>> fromBits) != 0) {
                throw new RuntimeException(
                        String.format("Input value '%X' exceeds '%d' bit size", value, fromBits));
            }
            acc = ((acc << fromBits) | value) & max_acc;
            bits += fromBits;
            while (bits >= toBits) {
                bits -= toBits;
                out.write((acc >>> bits) & maxv);
            }
        }
        if (pad) {
            if (bits > 0)
                out.write((acc << (toBits - bits)) & maxv);
        } else if (bits >= fromBits || ((acc << (toBits - bits)) & maxv) != 0) {
            throw new RuntimeException("Could not convert bits, invalid padding");
        }
        return out.toByteArray();
    }


    @Test
    public void testGetAddressBigInteger() {
        assertThat(Keys.getAddress(SampleKeys.PUBLIC_KEY),
                is(SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testGetAddressSmallPublicKey() {
        byte[] address = Keys.getAddress(
                Numeric.toBytesPadded(BigInteger.valueOf(0x1234), Keys.PUBLIC_KEY_SIZE));
        String expected = Numeric.toHexStringNoPrefix(address);

        assertThat(Keys.getAddress("0x1234"), equalTo(expected));
    }

    @Test
    public void testGetAddressZeroPadded() {
        byte[] address = Keys.getAddress(
                Numeric.toBytesPadded(BigInteger.valueOf(0x1234), Keys.PUBLIC_KEY_SIZE));
        String expected = Numeric.toHexStringNoPrefix(address);

        String value = "1234";
        assertThat(Keys.getAddress("0x"
                        + Strings.zeros(Keys.PUBLIC_KEY_LENGTH_IN_HEX - value.length()) + value),
                equalTo(expected));
    }

    @Test
    public void testToChecksumAddress() {
        // Test cases as per https://github.com/ethereum/EIPs/blob/master/EIPS/eip-55.md#test-cases

        assertThat(Keys.toChecksumAddress("0xfb6916095ca1df60bb79ce92ce3ea74c37c5d359"),
                is("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359"));

        // All uppercase
        assertThat(Keys.toChecksumAddress("0x52908400098527886E0F7030069857D2E4169EE7"),
                is("0x52908400098527886E0F7030069857D2E4169EE7"));
        assertThat(Keys.toChecksumAddress("0x8617E340B3D01FA5F11F306F4090FD50E238070D"),
                is("0x8617E340B3D01FA5F11F306F4090FD50E238070D"));

        // All lowercase
        assertThat(Keys.toChecksumAddress("0xde709f2102306220921060314715629080e2fb77"),
                is("0xde709f2102306220921060314715629080e2fb77"));
        assertThat(Keys.toChecksumAddress("0x27b1fdb04752bbc536007a920d24acb045561c26"),
                is("0x27b1fdb04752bbc536007a920d24acb045561c26"));

        // Normal
        assertThat(Keys.toChecksumAddress("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed"),
                is("0x5aAeb6053F3E94C9b9A09f33669435E7Ef1BeAed"));
        assertThat(Keys.toChecksumAddress("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359"),
                is("0xfB6916095ca1df60bB79Ce92cE3Ea74c37c5d359"));
        assertThat(Keys.toChecksumAddress("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB"),
                is("0xdbF03B407c01E7cD3CBea99509d93f8DDDC8C6FB"));
        assertThat(Keys.toChecksumAddress("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb"),
                is("0xD1220A0cf47c7B9Be7A2E6BA89F429762e7b9aDb"));
    }

    @Test
    public void testSerializeECKey() {
        assertThat(Keys.serialize(SampleKeys.KEY_PAIR), is(ENCODED));
    }

    @Test
    public void testDeserializeECKey() {
        assertThat(Keys.deserialize(ENCODED), is(SampleKeys.KEY_PAIR));
    }

    @Test(expected = RuntimeException.class)
    public void testDeserializeInvalidKey() {
        Keys.deserialize(new byte[0]);
    }
}
