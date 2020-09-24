package com.alaya.ens;

import java.io.IOException;
import java.math.BigInteger;

import com.alaya.utils.Numeric;
import org.junit.Before;
import org.junit.Test;

import com.alaya.abi.TypeEncoder;
import com.alaya.abi.datatypes.Utf8String;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.Web3jFactory;
import com.alaya.protocol.Web3jService;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.alaya.protocol.core.methods.response.PlatonCall;
import com.alaya.protocol.core.methods.response.PlatonSyncing;
import com.alaya.protocol.core.methods.response.NetVersion;
import com.alaya.tx.ChainId;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.alaya.ens.EnsResolver.DEFAULT_SYNC_THRESHOLD;
import static com.alaya.ens.EnsResolver.isValidEnsName;

public class EnsResolverTest {

    private Web3j web3j;
    private Web3jService web3jService;
    private EnsResolver ensResolver;

    @Before
    public void setUp() {
        web3jService = mock(Web3jService.class);
        web3j = Web3jFactory.build(web3jService);
        ensResolver = new EnsResolver(web3j);
    }

    @Test
    public void testResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Byte.toString(ChainId.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractAddress =
                "0x00000000000000000000000019e03255f667bdfd50a32722df860b1eeaf4d635";

        PlatonCall resolverAddressResponse = new PlatonCall();
        resolverAddressResponse.setResult(resolverAddress);

        PlatonCall contractAddressResponse = new PlatonCall();
        contractAddressResponse.setResult(contractAddress);

        when(web3jService.send(any(Request.class), eq(NetVersion.class)))
                .thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(PlatonCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(PlatonCall.class)))
                .thenReturn(contractAddressResponse);

        assertThat(ensResolver.resolve("web3j.eth"),
                is("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
    }

    @Test
    public void testReverseResolve() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        NetVersion netVersion = new NetVersion();
        netVersion.setResult(Byte.toString(ChainId.MAINNET));

        String resolverAddress =
                "0x0000000000000000000000004c641fb9bad9b60ef180c31f56051ce826d21a9a";
        String contractName =
                "0x0000000000000000000000000000000000000000000000000000000000000020"
                + TypeEncoder.encode(new Utf8String("web3j.eth"));
        System.err.println(contractName);

        PlatonCall resolverAddressResponse = new PlatonCall();
        resolverAddressResponse.setResult(resolverAddress);

        PlatonCall contractNameResponse = new PlatonCall();
        contractNameResponse.setResult(contractName);

        when(web3jService.send(any(Request.class), eq(NetVersion.class)))
                .thenReturn(netVersion);
        when(web3jService.send(any(Request.class), eq(PlatonCall.class)))
                .thenReturn(resolverAddressResponse);
        when(web3jService.send(any(Request.class), eq(PlatonCall.class)))
                .thenReturn(contractNameResponse);

        assertThat(ensResolver.reverseResolve("0x19e03255f667bdfd50a32722df860b1eeaf4d635"),
                is("web3j.eth"));
    }

    @Test
    public void testIsSyncedSyncing() throws Exception {
        configureSyncing(true);

        assertFalse(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedFullySynced() throws Exception {
        configureSyncing(false);
        configureLatestBlock(System.currentTimeMillis() / 1000);  // block timestamp is in seconds

        assertTrue(ensResolver.isSynced());
    }

    @Test
    public void testIsSyncedBelowThreshold() throws Exception {
        configureSyncing(false);
        configureLatestBlock((System.currentTimeMillis() / 1000) - DEFAULT_SYNC_THRESHOLD);

        assertFalse(ensResolver.isSynced());
    }

    private void configureSyncing(boolean isSyncing) throws IOException {
        PlatonSyncing ethSyncing = new PlatonSyncing();
        PlatonSyncing.Result result = new PlatonSyncing.Result();
        result.setSyncing(isSyncing);
        ethSyncing.setResult(result);

        when(web3jService.send(any(Request.class), eq(PlatonSyncing.class)))
                .thenReturn(ethSyncing);
    }

    private void configureLatestBlock(long timestamp) throws IOException {
        PlatonBlock.Block block = new PlatonBlock.Block();
        block.setTimestamp(Numeric.encodeQuantity(BigInteger.valueOf(timestamp)));
        PlatonBlock ethBlock = new PlatonBlock();
        ethBlock.setResult(block);

        when(web3jService.send(any(Request.class), eq(PlatonBlock.class)))
                .thenReturn(ethBlock);
    }

    @Test
    public void testIsEnsName() {
        assertTrue(isValidEnsName("eth"));
        assertTrue(isValidEnsName("web3.eth"));
        assertTrue(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635.eth"));

        assertFalse(isValidEnsName("0x19e03255f667bdfd50a32722df860b1eeaf4d635"));
        assertFalse(isValidEnsName("19e03255f667bdfd50a32722df860b1eeaf4d635"));

        assertTrue(isValidEnsName(""));
    }
}
