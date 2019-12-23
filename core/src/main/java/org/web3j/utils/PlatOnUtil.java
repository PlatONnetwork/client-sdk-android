package org.web3j.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.PlatOnTypeDecoder;
import org.web3j.abi.PlatOnTypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.BytesType;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.IntType;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes3;
import org.web3j.abi.datatypes.generated.Int64;
import org.web3j.platon.BaseResponse;
import org.web3j.platon.CustomStaticArray;
import org.web3j.platon.CustomType;
import org.web3j.platon.ErrorCode;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.PlatonEstimateGas;
import org.web3j.rlp.*;
import org.web3j.tx.gas.DefaultGasProvider;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlatOnUtil {

    private static final Logger logger = LoggerFactory.getLogger("PlatOnUtil");

    private static final int MAX_DEPTH = 16;

    /**
     * Allow for content up to size of 2^64 bytes *
     */
    private static final double MAX_ITEM_LENGTH = Math.pow(256, 8);

    /**
     * Reason for threshold according to Vitalik Buterin:
     * - 56 bytes maximizes the benefit of both options
     * - if we went with 60 then we would have only had 4 slots for long strings
     * so RLP would not have been able to store objects above 4gb
     * - if we went with 48 then RLP would be fine for 2^128 space, but that's way too much
     * - so 56 and 2^64 space seems like the right place to put the cutoff
     * - also, that's where Bitcoin's varint does the cutof
     */
    private static final int SIZE_THRESHOLD = 56;

    /** RLP encoding rules are defined as follows: */

    /*
     * For a single byte whose value is in the [0x00, 0x7f] range, that byte is
     * its own RLP encoding.
     */

    /**
     * [0x80]
     * If a string is 0-55 bytes long, the RLP encoding consists of a single
     * byte with value 0x80 plus the length of the string followed by the
     * string. The range of the first byte is thus [0x80, 0xb7].
     */
    private static final int OFFSET_SHORT_ITEM = 0x80;

    /**
     * [0xb7]
     * If a string is more than 55 bytes long, the RLP encoding consists of a
     * single byte with value 0xb7 plus the length of the length of the string
     * in binary form, followed by the length of the string, followed by the
     * string. For example, a length-1024 string would be encoded as
     * \xb9\x04\x00 followed by the string. The range of the first byte is thus
     * [0xb8, 0xbf].
     */
    private static final int OFFSET_LONG_ITEM = 0xb7;

    /**
     * [0xc0]
     * If the total payload of a list (i.e. the combined length of all its
     * items) is 0-55 bytes long, the RLP encoding consists of a single byte
     * with value 0xc0 plus the length of the list followed by the concatenation
     * of the RLP encodings of the items. The range of the first byte is thus
     * [0xc0, 0xf7].
     */
    private static final int OFFSET_SHORT_LIST = 0xc0;

    /**
     * [0xf7]
     * If the total payload of a list is more than 55 bytes long, the RLP
     * encoding consists of a single byte with value 0xf7 plus the length of the
     * length of the list in binary form, followed by the length of the list,
     * followed by the concatenation of the RLP encodings of the items. The
     * range of the first byte is thus [0xf8, 0xff].
     */
    private static final int OFFSET_LONG_LIST = 0xf7;

    private PlatOnUtil() {

    }

    private static final String DEFAULT_ADDR = "0x0000000000000000000000000000000000000000";

    /**
     * 解析call方法返回
     *
     * @param result
     * @return
     */
    public static BaseResponse invokeDecode(String result) {

        if (result == null) {
            return new BaseResponse();
        }

        BaseResponse baseResponse = JSONUtil.parseObject(new String(Hex.decode(Numeric.cleanHexPrefix(result))), BaseResponse.class);

        if (baseResponse == null) {
            return new BaseResponse();
        }

        if (baseResponse.isStatusOk()) {
            baseResponse.errMsg = ErrorCode.getErrorMsg(ErrorCode.SUCCESS);
        } else {
            baseResponse.errMsg = (String) baseResponse.data;
            baseResponse.data = null;
        }
        return baseResponse;
    }

    /**
     * 合约方法调用编码
     *
     * @param function 合约函数
     * @return encoded data
     */
    public static String invokeEncode(Function function) {

        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(RlpEncoder.encode(RlpString.create(function.getType()))));

        List<Type> parameters = function.getInputParameters();
        for (Type parameter : parameters) {
            if (parameter instanceof IntType) {
                result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((IntType) parameter).getValue()))));
            } else if (parameter instanceof BytesType) {
                result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((BytesType) parameter).getValue()))));
            } else if (parameter instanceof Utf8String) {
                result.add(RlpString.create(RlpEncoder.encode(RlpString.create(((Utf8String) parameter).getValue()))));
            } else if (parameter instanceof CustomStaticArray) {
                result.add(((CustomStaticArray) parameter).getRlpEncodeData());
            } else if (parameter instanceof CustomType) {
                result.add(((CustomType) parameter).getRlpEncodeData());
            }
        }
        String data = Hex.toHexString(RlpEncoder.encode(new RlpList(result)));
        return data;
    }

    /**
     * 合约数据编码
     *
     * @param contractBinary 合约数据
     * @param abi            abi
     * @return encoded data
     */
    public static String deployEncode(String contractBinary, String abi) {
        // txType + bin + abi
        List<RlpType> result = new ArrayList<>();
        result.add(RlpString.create(Numeric.hexStringToByteArray(PlatOnTypeEncoder.encode(new Int64(1)))));
        result.add(RlpString.create(Numeric.hexStringToByteArray(contractBinary)));
        result.add(RlpString.create(Numeric.hexStringToByteArray(PlatOnTypeEncoder.encode(new Utf8String(abi)))));
        String data = Hex.toHexString(RlpEncoder.encode(new RlpList(result)));
        return data;
    }

    /**
     * 合约事件编码
     *
     * @param data             数据
     * @param outputParameters 出参
     * @return results
     */
    public static List<Type> eventDecode(String data, List<TypeReference<Type>> outputParameters) {
        RlpList rlpList = RlpDecoder.decode(Numeric.hexStringToByteArray(data));
        List<RlpType> rlpTypeList = rlpList.getValues();
        RlpList rlpList2 = (RlpList) rlpTypeList.get(0);
        List<Type> results = new ArrayList<>();

        for (int i = 0; i < outputParameters.size(); i++) {
            RlpString rlpString = (RlpString) rlpList2.getValues().get(i);
            byte[] rlpBytes = rlpString.getBytes();
            TypeReference<Type> typeReference = outputParameters.get(i);
            Class<Type> type;
            try {
                type = typeReference.getClassType();
            } catch (ClassNotFoundException e) {
                throw new UnsupportedOperationException("class not found:", e);
            }
            Type result = PlatOnTypeDecoder.decode(rlpBytes, type);
            results.add(result);
        }
        return results;
    }

    /**
     * 估算GasLimit
     *
     * @param web3j           web3j
     * @param estimateGasFrom 发送者
     * @param estimateGasTo   接收者
     * @param encodedData     编码后的数据
     * @return gasLimit
     * @throws IOException exception
     */
    public static BigInteger estimateGasLimit(Web3j web3j, String estimateGasFrom, String estimateGasTo, String encodedData) throws IOException {
        if (Strings.isEmpty(estimateGasTo)) {
            estimateGasTo = DEFAULT_ADDR;
        }
        Transaction transaction = Transaction.createEthCallTransaction(estimateGasFrom, estimateGasTo, encodedData);
        Request<?, PlatonEstimateGas> ethEstimateGasReq = web3j.platonEstimateGas(transaction);
        if (ethEstimateGasReq == null) {
            return DefaultGasProvider.GAS_LIMIT;
        }
        PlatonEstimateGas ethEstimateGasRes = ethEstimateGasReq.send();
        BigInteger ethEstimateGasLimit = ethEstimateGasRes.getAmountUsed();
        return ethEstimateGasLimit;
    }

    /**
     * @param msgData
     * @return
     */
    public static RLPList invokeDecode(byte[] msgData) {
        RLPList rlpList = new RLPList();
        fullTraverse(msgData, 0, 0, msgData.length, rlpList, Integer.MAX_VALUE);
        return rlpList;
    }

    /**
     * Get exactly one message payload
     */
    static void fullTraverse(byte[] msgData, int level, int startPos,
                             int endPos, RLPList rlpList, int depth) {
        if (level > MAX_DEPTH) {
            throw new RuntimeException(String.format("Error: Traversing over max RLP depth (%s)", MAX_DEPTH));
        }

        try {
            if (msgData == null || msgData.length == 0)
                return;
            int pos = startPos;

            while (pos < endPos) {

                logger.debug("fullTraverse: level: " + level + " startPos: " + pos + " endPos: " + endPos);


                // It's a list with a payload more than 55 bytes
                // data[0] - 0xF7 = how many next bytes allocated
                // for the length of the list
                if ((msgData[pos] & 0xFF) > OFFSET_LONG_LIST) {

                    byte lengthOfLength = (byte) (msgData[pos] - OFFSET_LONG_LIST);
                    int length = calcLength(lengthOfLength, msgData, pos);

                    if (length < SIZE_THRESHOLD) {
                        throw new RuntimeException("Short list has been encoded as long list");
                    }

                    // check that length is in payload bounds
                    verifyLength(length, msgData.length - pos - lengthOfLength);

                    byte[] rlpData = new byte[lengthOfLength + length + 1];
                    System.arraycopy(msgData, pos, rlpData, 0, lengthOfLength
                            + length + 1);

                    if (level + 1 < depth) {
                        RLPList newLevelList = new RLPList();
                        newLevelList.setRLPData(rlpData);

                        fullTraverse(msgData, level + 1, pos + lengthOfLength + 1,
                                pos + lengthOfLength + length + 1, newLevelList, depth);
                        rlpList.add(newLevelList);
                    } else {
                        rlpList.add(new RLPItem(rlpData));
                    }

                    pos += lengthOfLength + length + 1;
                    continue;
                }
                // It's a list with a payload less than 55 bytes
                if ((msgData[pos] & 0xFF) >= OFFSET_SHORT_LIST
                        && (msgData[pos] & 0xFF) <= OFFSET_LONG_LIST) {

                    byte length = (byte) ((msgData[pos] & 0xFF) - OFFSET_SHORT_LIST);

                    byte[] rlpData = new byte[length + 1];
                    System.arraycopy(msgData, pos, rlpData, 0, length + 1);

                    if (level + 1 < depth) {
                        RLPList newLevelList = new RLPList();
                        newLevelList.setRLPData(rlpData);

                        if (length > 0)
                            fullTraverse(msgData, level + 1, pos + 1, pos + length + 1, newLevelList, depth);
                        rlpList.add(newLevelList);
                    } else {
                        rlpList.add(new RLPItem(rlpData));
                    }

                    pos += 1 + length;
                    continue;
                }
                // It's an item with a payload more than 55 bytes
                // data[0] - 0xB7 = how much next bytes allocated for
                // the length of the string
                if ((msgData[pos] & 0xFF) > OFFSET_LONG_ITEM
                        && (msgData[pos] & 0xFF) < OFFSET_SHORT_LIST) {

                    byte lengthOfLength = (byte) (msgData[pos] - OFFSET_LONG_ITEM);
                    int length = calcLength(lengthOfLength, msgData, pos);

                    if (length < SIZE_THRESHOLD) {
                        throw new RuntimeException("Short item has been encoded as long item");
                    }

                    // check that length is in payload bounds
                    verifyLength(length, msgData.length - pos - lengthOfLength);

                    // now we can parse an item for data[1]..data[length]
                    byte[] item = new byte[length];
                    System.arraycopy(msgData, pos + lengthOfLength + 1, item,
                            0, length);

                    RLPItem rlpItem = new RLPItem(item);
                    rlpList.add(rlpItem);
                    pos += lengthOfLength + length + 1;

                    continue;
                }
                // It's an item less than 55 bytes long,
                // data[0] - 0x80 == length of the item
                if ((msgData[pos] & 0xFF) > OFFSET_SHORT_ITEM
                        && (msgData[pos] & 0xFF) <= OFFSET_LONG_ITEM) {

                    byte length = (byte) ((msgData[pos] & 0xFF) - OFFSET_SHORT_ITEM);

                    byte[] item = new byte[length];
                    System.arraycopy(msgData, pos + 1, item, 0, length);

                    if (length == 1 && (item[0] & 0xFF) < OFFSET_SHORT_ITEM) {
                        throw new RuntimeException("Single byte has been encoded as byte string");
                    }

                    RLPItem rlpItem = new RLPItem(item);
                    rlpList.add(rlpItem);
                    pos += 1 + length;

                    continue;
                }
                // null item
                if ((msgData[pos] & 0xFF) == OFFSET_SHORT_ITEM) {
                    byte[] item = ByteUtil.EMPTY_BYTE_ARRAY;
                    RLPItem rlpItem = new RLPItem(item);
                    rlpList.add(rlpItem);
                    pos += 1;
                    continue;
                }
                // single byte item
                if ((msgData[pos] & 0xFF) < OFFSET_SHORT_ITEM) {

                    byte[] item = {(byte) (msgData[pos] & 0xFF)};

                    RLPItem rlpItem = new RLPItem(item);
                    rlpList.add(rlpItem);
                    pos += 1;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("RLP wrong encoding (" + Hex.toHexString(msgData, startPos, endPos - startPos) + ")", e);
        } catch (OutOfMemoryError e) {
            throw new RuntimeException("Invalid RLP (excessive mem allocation while parsing) (" + Hex.toHexString(msgData, startPos, endPos - startPos) + ")", e);
        }
    }

    /**
     * Parse length of long item or list.
     * RLP supports lengths with up to 8 bytes long,
     * but due to java limitation it returns either encoded length
     * or {@link Integer#MAX_VALUE} in case if encoded length is greater
     *
     * @param lengthOfLength length of length in bytes
     * @param msgData        message
     * @param pos            position to parse from
     * @return calculated length
     */
    private static int calcLength(int lengthOfLength, byte[] msgData, int pos) {
        byte pow = (byte) (lengthOfLength - 1);
        int length = 0;
        for (int i = 1; i <= lengthOfLength; ++i) {

            int bt = msgData[pos + i] & 0xFF;
            int shift = 8 * pow;

            // no leading zeros are acceptable
            if (bt == 0 && length == 0) {
                throw new RuntimeException("RLP length contains leading zeros");
            }

            // return MAX_VALUE if index of highest bit is more than 31
            if (32 - Integer.numberOfLeadingZeros(bt) + shift > 31) {
                return Integer.MAX_VALUE;
            }

            length += bt << shift;
            pow--;
        }

        // check that length is in payload bounds
        verifyLength(length, msgData.length - pos - lengthOfLength);

        return length;
    }

    /**
     * Compares supplied length information with maximum possible
     *
     * @param suppliedLength  Length info from header
     * @param availableLength Length of remaining object
     * @throws RuntimeException if supplied length is bigger than available
     */
    private static void verifyLength(int suppliedLength, int availableLength) {
        if (suppliedLength > availableLength) {
            throw new RuntimeException(String.format("Length parsed from RLP (%s bytes) is greater " +
                    "than possible size of data (%s bytes)", suppliedLength, availableLength));
        }
    }
}
