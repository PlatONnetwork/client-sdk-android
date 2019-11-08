package org.web3j.tx.gas;

import java.math.BigInteger;

import org.web3j.tx.ManagedTransaction;

public class DefaultGasProvider extends ContractGasProvider {

    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000L);

    public static final BigInteger GAS_PRICE = BigInteger.valueOf(500000000000L);

    public DefaultGasProvider() {
        super(GAS_PRICE, GAS_LIMIT);
    }
}
