package com.alaya.protocol.core;

import com.alaya.protocol.core.methods.request.ShhFilter;
import com.alaya.protocol.core.methods.response.*;
import com.alaya.protocol.core.methods.response.AdminProgramVersion;
import com.alaya.protocol.core.methods.response.AdminSchnorrNIZKProve;
import com.alaya.protocol.core.methods.response.DbGetHex;
import com.alaya.protocol.core.methods.response.DbGetString;
import com.alaya.protocol.core.methods.response.DbPutHex;
import com.alaya.protocol.core.methods.response.DbPutString;
import com.alaya.protocol.core.methods.response.DebugEconomicConfig;
import com.alaya.protocol.core.methods.response.PlatonAccounts;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.alaya.protocol.core.methods.response.PlatonBlockNumber;
import com.alaya.protocol.core.methods.response.PlatonEstimateGas;
import com.alaya.protocol.core.methods.response.PlatonEvidences;
import com.alaya.protocol.core.methods.response.PlatonFilter;
import com.alaya.protocol.core.methods.response.PlatonCall;
import com.alaya.protocol.core.methods.response.PlatonGasPrice;
import com.alaya.protocol.core.methods.response.PlatonGetBalance;
import com.alaya.protocol.core.methods.response.PlatonGetBlockTransactionCountByHash;
import com.alaya.protocol.core.methods.response.PlatonGetBlockTransactionCountByNumber;
import com.alaya.protocol.core.methods.response.PlatonGetCode;
import com.alaya.protocol.core.methods.response.PlatonGetStorageAt;
import com.alaya.protocol.core.methods.response.PlatonGetTransactionCount;
import com.alaya.protocol.core.methods.response.PlatonGetTransactionReceipt;
import com.alaya.protocol.core.methods.response.PlatonLog;
import com.alaya.protocol.core.methods.response.PlatonPendingTransactions;
import com.alaya.protocol.core.methods.response.PlatonProtocolVersion;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.protocol.core.methods.response.PlatonSign;
import com.alaya.protocol.core.methods.response.PlatonSyncing;
import com.alaya.protocol.core.methods.response.PlatonTransaction;
import com.alaya.protocol.core.methods.response.PlatonUninstallFilter;
import com.alaya.protocol.core.methods.response.NetListening;
import com.alaya.protocol.core.methods.response.NetPeerCount;
import com.alaya.protocol.core.methods.response.NetVersion;
import com.alaya.protocol.core.methods.response.ShhAddToGroup;
import com.alaya.protocol.core.methods.response.ShhHasIdentity;
import com.alaya.protocol.core.methods.response.ShhMessages;
import com.alaya.protocol.core.methods.response.ShhNewFilter;
import com.alaya.protocol.core.methods.response.ShhNewGroup;
import com.alaya.protocol.core.methods.response.ShhNewIdentity;
import com.alaya.protocol.core.methods.response.ShhUninstallFilter;
import com.alaya.protocol.core.methods.response.ShhVersion;
import com.alaya.protocol.core.methods.response.Web3ClientVersion;
import com.alaya.protocol.core.methods.response.Web3Sha3;

import java.math.BigInteger;

/**
 * Core Ethereum JSON-RPC API.
 */
public interface Platon {
    Request<?, Web3ClientVersion> web3ClientVersion();

    Request<?, Web3Sha3> web3Sha3(String data);

    Request<?, NetVersion> netVersion();

    Request<?, NetListening> netListening();

    Request<?, NetPeerCount> netPeerCount();

    Request<?, PlatonProtocolVersion> platonProtocolVersion();

    Request<?, PlatonSyncing> platonSyncing();

    Request<?, PlatonGasPrice> platonGasPrice();

    Request<?, PlatonAccounts> platonAccounts();

    Request<?, PlatonBlockNumber> platonBlockNumber();

    Request<?, PlatonGetBalance> platonGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonGetStorageAt> platonGetStorageAt(
            String address, BigInteger position,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonGetTransactionCount> platonGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonGetBlockTransactionCountByHash> platonGetBlockTransactionCountByHash(
            String blockHash);

    Request<?, PlatonGetBlockTransactionCountByNumber> platonGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonGetCode> platonGetCode(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonSign> platonSign(String address, String sha3HashOfDataToSign);

    Request<?, PlatonSendTransaction> platonSendTransaction(
            com.alaya.protocol.core.methods.request.Transaction transaction);

    Request<?, PlatonSendTransaction> platonSendRawTransaction(
            String signedTransactionData);

    Request<?, PlatonCall> platonCall(
            com.alaya.protocol.core.methods.request.Transaction transaction,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonEstimateGas> platonEstimateGas(
            com.alaya.protocol.core.methods.request.Transaction transaction);

    Request<?, PlatonBlock> platonGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    Request<?, PlatonBlock> platonGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects);

    Request<?, PlatonTransaction> platonGetTransactionByHash(String transactionHash);

    Request<?, PlatonTransaction> platonGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex);

    Request<?, PlatonTransaction> platonGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, PlatonGetTransactionReceipt> platonGetTransactionReceipt(String transactionHash);

    Request<?, PlatonFilter> platonNewFilter(com.alaya.protocol.core.methods.request.PlatonFilter ethFilter);

    Request<?, PlatonFilter> platonNewBlockFilter();

    Request<?, PlatonFilter> platonNewPendingTransactionFilter();

    Request<?, PlatonUninstallFilter> platonUninstallFilter(BigInteger filterId);

    Request<?, PlatonLog> platonGetFilterChanges(BigInteger filterId);

    Request<?, PlatonLog> platonGetFilterLogs(BigInteger filterId);

    Request<?, PlatonLog> platonGetLogs(com.alaya.protocol.core.methods.request.PlatonFilter ethFilter);

    Request<?, PlatonPendingTransactions> platonPendingTx();

    Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore);

    Request<?, DbGetString> dbGetString(String databaseName, String keyName);

    Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore);

    Request<?, DbGetHex> dbGetHex(String databaseName, String keyName);

    Request<?, ShhPost> shhPost(
            com.alaya.protocol.core.methods.request.ShhPost shhPost);

    Request<?, ShhVersion> shhVersion();

    Request<?, ShhNewIdentity> shhNewIdentity();

    Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress);

    Request<?, ShhNewGroup> shhNewGroup();

    Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress);

    Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter);

    Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId);

    Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId);

    Request<?, ShhMessages> shhGetMessages(BigInteger filterId);

    Request<?, PlatonEvidences> platonEvidences();

    public Request<?, AdminProgramVersion> getProgramVersion();

    Request<?, AdminSchnorrNIZKProve> getSchnorrNIZKProve();

    Request<?, DebugEconomicConfig> getEconomicConfig();
}
