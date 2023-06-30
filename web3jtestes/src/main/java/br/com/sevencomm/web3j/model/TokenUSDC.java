package br.com.sevencomm.web3j.model;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.web3j.abi.EventEncoder;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.Response.Error;
import org.web3j.protocol.core.filters.FilterException;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

public class TokenUSDC extends Tokenerc20_sol_USDC {

    protected TokenUSDC(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(contractAddress, web3j, credentials, contractGasProvider);
    }

    protected TokenUSDC(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TokenUSDC load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new TokenUSDC(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static TokenUSDC load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new TokenUSDC(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TransferEventResponse getTransferEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(TRANSFER_EVENT, log);
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.log = log;
        typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
        typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public List<TransferEventResponse> transferEvent(EthFilter filter) {

    	filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
    	
    	List<TransferEventResponse> listTransferEventResponse = new ArrayList<TransferEventResponse>();
        EthLog ethLog = null;
        try {
        	ethLog = web3j.ethGetLogs(filter).send();
	    } catch (IOException e) {
	        throwException(e);
	    }
        
	    if (ethLog.hasError()) {
	        Error error = ethLog.getError();
	        throwException(error);
	    } else {
	    	for (EthLog.LogResult logResult : ethLog.getLogs()) {
	            if (logResult instanceof EthLog.LogObject) {
	                Log log = ((EthLog.LogObject) logResult).get();
	                listTransferEventResponse.add(getTransferEventFromLog(log));
	            } else {
	                throw new FilterException(
	                        "Unexpected result type: " + logResult.get() + " required LogObject");
	            }
	        }
	    }
                
        return listTransferEventResponse;
    }

    public List<TransferEventResponse> transferEvent(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        return transferEvent(filter);
    }
    
    void throwException(Response.Error error) {
        throw new FilterException(
                "Invalid request: " + (error == null ? "Unknown Error" : error.getMessage()));
    }

    void throwException(Throwable cause) {
        throw new FilterException("Error sending request", cause);
    }
}
