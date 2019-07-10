package org.web3j.rlp;

import java.math.BigInteger;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class RlpDecoderTest {

    /**
     * Examples taken from https://github.com/ethereum/wiki/wiki/RLP#examples.
     * For further examples see https://github.com/ethereum/tests/tree/develop/RLPTests.
     */
    @Test
    public void testRLPDecode() {

        // empty array of binary
        assertTrue(RlpDecoder.decode(new byte[]{}).getValues().isEmpty());

        // The string "dog" = [ 0x83, 'd', 'o', 'g' ]
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x83, 'd', 'o', 'g'}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create("dog")));

        // The list [ "cat", "dog" ] = [ 0xc8, 0x83, 'c', 'a', 't', 0x83, 'd', 'o', 'g' ]
        RlpList rlpList = (RlpList) RlpDecoder.decode(
                new byte[]{
                        (byte) 0xc8, (byte) 0x83, 'c', 'a', 't', (byte) 0x83, 'd', 'o', 'g'
                }).getValues().get(0);

        assertThat(rlpList.getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create("cat")));

        assertThat(rlpList.getValues().get(1),
                CoreMatchers.<RlpType>is(RlpString.create("dog")));

        // The empty string ('null') = [ 0x80 ]
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x80}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create("")));

        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x80}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(new byte[]{})));

        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x80}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(BigInteger.ZERO)));

        // The empty list = [ 0xc0 ]
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0xc0}).getValues().get(0),
                instanceOf(RlpList.class));

        assertTrue(((RlpList) RlpDecoder.decode(new byte[]{(byte) 0xc0})
                .getValues().get(0)).getValues().isEmpty());

        // The encoded integer 0 ('\x00') = [ 0x00 ]
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x00}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(BigInteger.valueOf(0).byteValue())));

        // The encoded integer 15 ('\x0f') = [ 0x0f ]
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x0f}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(BigInteger.valueOf(15).byteValue())));

        // The encoded integer 1024 ('\x04\x00') = [ 0x82, 0x04, 0x00 ]
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x82, (byte) 0x04, (byte) 0x00})
                        .getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(BigInteger.valueOf(0x0400))));

        // The set theoretical representation of three,
        // [ [], [[]], [ [], [[]] ] ] = [ 0xc7, 0xc0, 0xc1, 0xc0, 0xc3, 0xc0, 0xc1, 0xc0 ]
        rlpList = RlpDecoder.decode(new byte[]{
                (byte) 0xc7,
                (byte) 0xc0,
                (byte) 0xc1, (byte) 0xc0,
                (byte) 0xc3, (byte) 0xc0, (byte) 0xc1, (byte) 0xc0});
        assertThat(rlpList, instanceOf(RlpList.class));

        assertThat(rlpList.getValues().size(), equalTo(1));

        assertThat(rlpList.getValues().get(0), instanceOf(RlpList.class));

        assertThat(((RlpList)
                        rlpList.getValues().get(0)).getValues().size(),
                equalTo(3));

        assertThat(((RlpList)
                        rlpList.getValues().get(0)).getValues().get(0),
                instanceOf(RlpList.class));

        assertThat(((RlpList)
                        ((RlpList) rlpList.getValues().get(0)).getValues().get(0)).getValues().size(),
                equalTo(0));

        assertThat(((RlpList)
                        ((RlpList) rlpList.getValues().get(0)).getValues().get(1)).getValues().size(),
                equalTo(1));

        assertThat(((RlpList)
                        ((RlpList) rlpList.getValues().get(0)).getValues().get(2)).getValues().size(),
                equalTo(2));

        assertThat(((RlpList)
                        ((RlpList) rlpList.getValues().get(0)).getValues().get(2)).getValues().get(0),
                instanceOf(RlpList.class));

        assertThat(((RlpList)
                        ((RlpList)
                                ((RlpList) rlpList.getValues().get(0)).getValues().get(2)).getValues().get(0))
                        .getValues().size(),
                equalTo(0));

        assertThat(((RlpList)
                        ((RlpList)
                                ((RlpList) rlpList.getValues().get(0)).getValues().get(2)).getValues().get(1))
                        .getValues().size(),
                equalTo(1));

        // The string "Lorem ipsum dolor sit amet,
        // consectetur adipisicing elit" =
        // [ 0xb8, 0x38, 'L', 'o', 'r', 'e', 'm', ' ', ... , 'e', 'l', 'i', 't' ]

        assertThat(RlpDecoder.decode(
                new byte[]{
                        (byte) 0xb8,
                        (byte) 0x38,
                        'L', 'o', 'r', 'e', 'm', ' ', 'i', 'p', 's', 'u', 'm', ' ',
                        'd', 'o', 'l', 'o', 'r', ' ', 's', 'i', 't', ' ',
                        'a', 'm', 'e', 't', ',', ' ',
                        'c', 'o', 'n', 's', 'e', 'c', 't', 'e', 't', 'u', 'r', ' ',
                        'a', 'd', 'i', 'p', 'i', 's', 'i', 'c', 'i', 'n', 'g', ' ',
                        'e', 'l', 'i', 't'
                }).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(
                        "Lorem ipsum dolor sit amet, consectetur adipisicing elit")));

        // https://github.com/paritytech/parity/blob/master/util/rlp/tests/tests.rs#L239
        assertThat(RlpDecoder.decode(new byte[]{(byte) 0x00}).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(new byte[]{0})));

        rlpList = RlpDecoder.decode(new byte[]{
                (byte) 0xc6, (byte) 0x82, (byte) 0x7a, (byte) 0x77, (byte) 0xc1,
                (byte) 0x04, (byte) 0x01});

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().size(),
                equalTo(3));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(0),
                instanceOf(RlpString.class));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(1),
                instanceOf(RlpList.class));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(2),
                instanceOf(RlpString.class));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create("zw")));

        assertThat(((RlpList)
                        ((RlpList) rlpList.getValues().get(0)).getValues().get(1)).getValues().get(0),
                CoreMatchers.<RlpType>is(RlpString.create(4)));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(2),
                CoreMatchers.<RlpType>is(RlpString.create(1)));

        // payload more than 55 bytes
        String data = "F86E12F86B80881BC16D674EC8000094CD2A3D9F938E13CD947EC05ABC7FE734D"
                + "F8DD8268609184E72A00064801BA0C52C114D4F5A3BA904A9B3036E5E118FE0DBB987"
                + "FE3955DA20F2CD8F6C21AB9CA06BA4C2874299A55AD947DBC98A25EE895AABF6B625C"
                + "26C435E84BFD70EDF2F69";

        byte[] payload = Numeric.hexStringToByteArray(data);
        rlpList = RlpDecoder.decode(payload);

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().size(),
                equalTo(2));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(0),
                instanceOf(RlpString.class));

        assertThat(((RlpList) rlpList.getValues().get(0)).getValues().get(1),
                instanceOf(RlpList.class));

        assertThat(((RlpList)
                        ((RlpList) rlpList.getValues().get(0)).getValues().get(1)).getValues().size(),
                equalTo(9));

    }

    @Test
    public void testDecoderObject() {
        String encodedData = "0xf902d101f902ccf8b1b8401f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2842233494000000740ce31b3fac20dac379db243021a51e809410000000000000000000000000000000000000028080020102820100022001820101f68e7878636363636464646464646464864920416d20308d7777772e62616964752e636f6d917468697320697320206261696475207e7ef8b1b8402f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2843546694740ce31b3fac20dac379db243021a51e804445559410000000000000000000000000000000000000020101020103820100022001820101f68e7878636363636464646464646464864920416d20318d7777772e62616964752e636f6d917468697320697320206261696475207e7ef8b1b8403f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2854487894000000740ce31b3fac20dac379db243021a51e809410000000000000000000000000000000000000020204020104820100022001820101f68e7878636363636464646464646464864920416d20328d7777772e62616964752e636f6d917468697320697320206261696475207e7ef8b1b8403f3a8672348ff6b789e416762ad53e69063138b8eb4d8780101658f24b2369f1a8e09499226b467d8bc0c4e03e1dc903df857eeb3c67733d21b6aaee2856464694000000740ce31b3fac20dac379db243021a51e809410000000000000000000000000000000000000020309020105820100022001820101f68e7878636363636464646464646464864920416d20338d7777772e62616964752e636f6d917468697320697320206261696475207e7e80";

        RlpList rlpList = RlpDecoder.decode(encodedData.getBytes());
    }
}
