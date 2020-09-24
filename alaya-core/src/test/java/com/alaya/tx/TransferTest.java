package com.alaya.tx;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.methods.response.PlatonGasPrice;
import com.alaya.protocol.core.methods.response.TransactionReceipt;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferTest extends ManagedTransactionTester {

    private TransactionReceipt transactionReceipt;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        transactionReceipt = prepareTransfer();
    }

    @Test
    public void testSendFunds() throws Exception {
//        assertThat(Transfer.sendFunds(web3j, SampleKeys.CREDENTIALS, ADDRESS,
//                BigDecimal.TEN, Convert.Unit.LAT).send(),
//                is(transactionReceipt));
    }

    @Test
    public void testSendFundsAsync() throws  Exception {
//        assertThat(Transfer.sendFunds(web3j, SampleKeys.CREDENTIALS, ADDRESS,
//                BigDecimal.TEN, Convert.Unit.LAT).send(),
//                is(transactionReceipt));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTransferInvalidValue() throws Exception {
//        Transfer.sendFunds(web3j, SampleKeys.CREDENTIALS, ADDRESS,
//                new BigDecimal(0.1), Convert.Unit.VON).send();
    }

    @SuppressWarnings("unchecked")
    private TransactionReceipt prepareTransfer() throws IOException {
        TransactionReceipt transactionReceipt = new TransactionReceipt();
        transactionReceipt.setTransactionHash(TRANSACTION_HASH);
        prepareTransaction(transactionReceipt);

        final PlatonGasPrice ethGasPrice = new PlatonGasPrice();
        ethGasPrice.setResult("0x1");

        Request<?, PlatonGasPrice> gasPriceRequest = mock(Request.class);
        when(gasPriceRequest.send()).thenReturn(ethGasPrice);
        when(web3j.platonGasPrice()).thenReturn((Request) gasPriceRequest);

        return transactionReceipt;
    }
}
