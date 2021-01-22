package org.web3j.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.web3j.crypto.addressconvert.AddressManager;
import org.web3j.utils.Numeric;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class WalletTest {

    @Before
   public void init(){
        WalletApplication.init(AddressManager.ADDRESS_TYPE_BECH32, "lfp");
   }

    @Test
    public void testCreateStandard() throws Exception {

        WalletFile walletFile =  Wallet.createStandard(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR);
        System.out.println("------ address:" + walletFile.getAddress());
        //System.out.println("------ testnet:" + walletFile.getAddress().getTestnet());
        //testCreate(Wallet.createStandard(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    @Test
    public void testCreateLight() throws Exception {

        WalletFile walletFile =  Wallet.createLight(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR);
        System.out.println("------ address:" + walletFile.getAddress());

        //System.out.println("------ mainnet:" + walletFile.getAddress().getMainnet());
        //System.out.println("------ testnet:" + walletFile.getAddress().getTestnet());
        //testCreate(Wallet.createLight(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    private void testCreate(WalletFile walletFile) throws Exception {
        assertThat(walletFile.getAddress(), is(SampleKeys.ADDRESS_NO_PREFIX));
    }

    @Test
    public void testEncryptDecryptStandard() throws Exception {
        testEncryptDecrypt(Wallet.createStandard(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    @Test
    public void testEncryptDecryptLight() throws Exception {
        testEncryptDecrypt(Wallet.createLight(SampleKeys.PASSWORD, SampleKeys.KEY_PAIR));
    }

    private void testEncryptDecrypt(WalletFile walletFile) throws Exception {
        assertThat(Wallet.decrypt(SampleKeys.PASSWORD, walletFile), equalTo(SampleKeys.KEY_PAIR));
    }

    @Test
    public void testDecryptAes128Ctr() throws Exception {
        WalletFile walletFile = load(AES_128_CTR);
        ECKeyPair ecKeyPair = Wallet.decrypt(PASSWORD, walletFile);
        assertThat(Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey()), is(SECRET));
    }

    @Test
    public void testDecryptScrypt() throws Exception {
        WalletFile walletFile = load(SCRYPT);
        ECKeyPair ecKeyPair = Wallet.decrypt(PASSWORD, walletFile);
        assertThat(Numeric.toHexStringNoPrefix(ecKeyPair.getPrivateKey()), is(SECRET));
    }

    @Test
    public void testGenerateRandomBytes() {
        assertThat(Wallet.generateRandomBytes(0), is(new byte[]{}));
        assertThat(Wallet.generateRandomBytes(10).length, is(10));
    }

    private WalletFile load(String source) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(source, WalletFile.class);
    }

    private static final String PASSWORD = "Insecure Pa55w0rd";
    private static final String SECRET =
            "a392604efc2fad9c0b3da43b5f698a2e3f270f170d859912be0d54742275c5f6";

    //CHECKSTYLE:OFF
    private static final String AES_128_CTR = "{\n" +
            "    \"crypto\" : {\n" +
            "        \"cipher\" : \"aes-128-ctr\",\n" +
            "        \"cipherparams\" : {\n" +
            "            \"iv\" : \"02ebc768684e5576900376114625ee6f\"\n" +
            "        },\n" +
            "        \"ciphertext\" : \"7ad5c9dd2c95f34a92ebb86740b92103a5d1cc4c2eabf3b9a59e1f83f3181216\",\n" +
            "        \"kdf\" : \"pbkdf2\",\n" +
            "        \"kdfparams\" : {\n" +
            "            \"c\" : 262144,\n" +
            "            \"dklen\" : 32,\n" +
            "            \"prf\" : \"hmac-sha256\",\n" +
            "            \"salt\" : \"0e4cf3893b25bb81efaae565728b5b7cde6a84e224cbf9aed3d69a31c981b702\"\n" +
            "        },\n" +
            "        \"mac\" : \"2b29e4641ec17f4dc8b86fc8592090b50109b372529c30b001d4d96249edaf62\"\n" +
            "    },\n" +
            "    \"id\" : \"af0451b4-6020-4ef0-91ec-794a5a965b01\",\n" +
            "    \"version\" : 3\n" +
            "}";

    private static final String SCRYPT = "{\n" +
            "    \"crypto\" : {\n" +
            "        \"cipher\" : \"aes-128-ctr\",\n" +
            "        \"cipherparams\" : {\n" +
            "            \"iv\" : \"3021e1ef4774dfc5b08307f3a4c8df00\"\n" +
            "        },\n" +
            "        \"ciphertext\" : \"4dd29ba18478b98cf07a8a44167acdf7e04de59777c4b9c139e3d3fa5cb0b931\",\n" +
            "        \"kdf\" : \"scrypt\",\n" +
            "        \"kdfparams\" : {\n" +
            "            \"dklen\" : 32,\n" +
            "            \"n\" : 262144,\n" +
            "            \"r\" : 8,\n" +
            "            \"p\" : 1,\n" +
            "            \"salt\" : \"4f9f68c71989eb3887cd947c80b9555fce528f210199d35c35279beb8c2da5ca\"\n" +
            "        },\n" +
            "        \"mac\" : \"7e8f2192767af9be18e7a373c1986d9190fcaa43ad689bbb01a62dbde159338d\"\n" +
            "    },\n" +
            "    \"id\" : \"7654525c-17e0-4df5-94b5-c7fde752c9d2\",\n" +
            "    \"version\" : 3\n" +
            "}";
    //CHECKSTYLE:ON
}
