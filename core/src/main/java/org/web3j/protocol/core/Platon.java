package org.web3j.protocol.core;

import org.web3j.protocol.core.methods.request.ShhFilter;
import org.web3j.protocol.core.methods.response.AdminProgramVersion;
import org.web3j.protocol.core.methods.response.AdminSchnorrNIZKProve;
import org.web3j.protocol.core.methods.response.DbGetHex;
import org.web3j.protocol.core.methods.response.DbGetString;
import org.web3j.protocol.core.methods.response.DbPutHex;
import org.web3j.protocol.core.methods.response.DbPutString;
import org.web3j.protocol.core.methods.response.DebugEconomicConfig;
import org.web3j.protocol.core.methods.response.PlatonAccounts;
import org.web3j.protocol.core.methods.response.PlatonBlock;
import org.web3j.protocol.core.methods.response.PlatonBlockNumber;
import org.web3j.protocol.core.methods.response.PlatonEstimateGas;
import org.web3j.protocol.core.methods.response.PlatonEvidences;
import org.web3j.protocol.core.methods.response.PlatonFilter;
import org.web3j.protocol.core.methods.response.PlatonCall;
import org.web3j.protocol.core.methods.response.PlatonGasPrice;
import org.web3j.protocol.core.methods.response.PlatonGetBalance;
import org.web3j.protocol.core.methods.response.PlatonGetBlockTransactionCountByHash;
import org.web3j.protocol.core.methods.response.PlatonGetBlockTransactionCountByNumber;
import org.web3j.protocol.core.methods.response.PlatonGetCode;
import org.web3j.protocol.core.methods.response.PlatonGetStorageAt;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionCount;
import org.web3j.protocol.core.methods.response.PlatonGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.PlatonLog;
import org.web3j.protocol.core.methods.response.PlatonPendingTransactions;
import org.web3j.protocol.core.methods.response.PlatonProtocolVersion;
import org.web3j.protocol.core.methods.response.PlatonSendTransaction;
import org.web3j.protocol.core.methods.response.PlatonSign;
import org.web3j.protocol.core.methods.response.PlatonSyncing;
import org.web3j.protocol.core.methods.response.PlatonTransaction;
import org.web3j.protocol.core.methods.response.PlatonUninstallFilter;
import org.web3j.protocol.core.methods.response.NetListening;
import org.web3j.protocol.core.methods.response.NetPeerCount;
import org.web3j.protocol.core.methods.response.NetVersion;
import org.web3j.protocol.core.methods.response.ShhAddToGroup;
import org.web3j.protocol.core.methods.response.ShhHasIdentity;
import org.web3j.protocol.core.methods.response.ShhMessages;
import org.web3j.protocol.core.methods.response.ShhNewFilter;
import org.web3j.protocol.core.methods.response.ShhNewGroup;
import org.web3j.protocol.core.methods.response.ShhNewIdentity;
import org.web3j.protocol.core.methods.response.ShhUninstallFilter;
import org.web3j.protocol.core.methods.response.ShhVersion;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;

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
            org.web3j.protocol.core.methods.request.Transaction transaction);

    Request<?, PlatonSendTransaction> platonSendRawTransaction(
            String signedTransactionData);

    Request<?, PlatonCall> platonCall(
            org.web3j.protocol.core.methods.request.Transaction transaction,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, PlatonEstimateGas> platonEstimateGas(
            org.web3j.protocol.core.methods.request.Transaction transaction);

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

    Request<?, PlatonFilter> platonNewFilter(org.web3j.protocol.core.methods.request.PlatonFilter ethFilter);

    Request<?, PlatonFilter> platonNewBlockFilter();

    Request<?, PlatonFilter> platonNewPendingTransactionFilter();

    Request<?, PlatonUninstallFilter> platonUninstallFilter(BigInteger filterId);

    Request<?, PlatonLog> platonGetFilterChanges(BigInteger filterId);

    Request<?, PlatonLog> platonGetFilterLogs(BigInteger filterId);

    Request<?, PlatonLog> platonGetLogs(org.web3j.protocol.core.methods.request.PlatonFilter ethFilter);

    Request<?, PlatonPendingTransactions> platonPendingTx();

    Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore);

    Request<?, DbGetString> dbGetString(String databaseName, String keyName);

    Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore);

    Request<?, DbGetHex> dbGetHex(String databaseName, String keyName);

    Request<?, org.web3j.protocol.core.methods.response.ShhPost> shhPost(
            org.web3j.protocol.core.methods.request.ShhPost shhPost);

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
