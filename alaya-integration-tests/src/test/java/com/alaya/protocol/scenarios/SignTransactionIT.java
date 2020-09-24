package com.alaya.protocol.scenarios;

import java.math.BigInteger;

import com.alaya.crypto.Hash;
import com.alaya.crypto.RawTransaction;
import com.alaya.crypto.TransactionEncoder;
import com.alaya.protocol.core.methods.response.PlatonSign;
import com.alaya.utils.Convert;
import com.alaya.utils.Numeric;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Sign transaction using Ethereum node.
 */
public class SignTransactionIT extends Scenario {

    @Test
    public void testSignTransaction() throws Exception {
        boolean accountUnlocked = unlockAccount();
        assertTrue(accountUnlocked);

        RawTransaction rawTransaction = createTransaction();

        byte[] encoded = TransactionEncoder.encode(rawTransaction);
        byte[] hashed = Hash.sha3(encoded);

        PlatonSign ethSign = web3j.platonSign(ALICE.getAddress(), Numeric.toHexString(hashed))
                .sendAsync().get();

        String signature = ethSign.getSignature();
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    private static RawTransaction createTransaction() {
        BigInteger value = Convert.toVon("1", Convert.Unit.LAT).toBigInteger();

        return RawTransaction.createEtherTransaction(
                BigInteger.valueOf(1048587), BigInteger.valueOf(500000), BigInteger.valueOf(500000),
                "0x9C98E381Edc5Fe1Ac514935F3Cc3eDAA764cf004",
                value);
    }
}
