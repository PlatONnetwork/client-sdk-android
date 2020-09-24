package com.alaya.protocol.scenarios;

import java.math.BigInteger;

import com.alaya.generated.Fibonacci;
import com.alaya.protocol.Web3jFactory;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.protocol.http.HttpService;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test Fibonacci contract generated wrappers.
 *
 * <p>Generated via running org.web3j.codegen.SolidityFunctionWrapperGenerator with params:
 * <em>project-home</em>/src/test/resources/solidity/fibonacci.abi -o
 * <em>project-home</em>/src/integration-test/java -p org.web3j.generated
 */
public class FunctionWrappersIT extends Scenario {

    @Test
    public void testFibonacci() throws Exception {
        Fibonacci fibonacci = Fibonacci.load(
                "0x3c05b2564139fb55820b18b72e94b2178eaace7d",
                Web3jFactory.build(new HttpService()),
                ALICE, GAS_PRICE, GAS_LIMIT);

        BigInteger result = fibonacci.fibonacci(BigInteger.valueOf(10)).send();
        assertThat(result, equalTo(BigInteger.valueOf(55)));
    }

    @Test
    public void testFibonacciNotify() throws Exception {
        Fibonacci fibonacci = Fibonacci.load(
                "0x3c05b2564139fb55820b18b72e94b2178eaace7d",
                Web3jFactory.build(new HttpService()),
                ALICE, GAS_PRICE, GAS_LIMIT);

        TransactionReceipt transactionReceipt = fibonacci.fibonacciNotify(
                BigInteger.valueOf(15)).send();

        Fibonacci.NotifyEventResponse result = fibonacci.getNotifyEvents(transactionReceipt).get(0);

        assertThat(result.input,
                IsEqual.<BigInteger>equalTo(BigInteger.valueOf(15)));

        assertThat(result.result,
                IsEqual.<BigInteger>equalTo(BigInteger.valueOf(610)));
    }
}
