package br.com.sevencomm.web3j;

import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import br.com.sevencomm.web3j.model.TokenUSDC;
import br.com.sevencomm.web3j.model.Tokenerc20_sol_USDC.TransferEventResponse;

public class ReadTransferEvents {

	public static void main(String[] args) throws InterruptedException {

		String contractAddress = "0x3F5Bee678C541437F789465FEE265478B11e5B9d";

		Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.g.alchemy.com/v2/demo"));

		DefaultGasProvider contractGasProvider = new DefaultGasProvider();
		ReadonlyTransactionManager readonlyTransactionManager = new ReadonlyTransactionManager(web3j, contractAddress);
		TokenUSDC usdc = TokenUSDC.load(contractAddress, web3j, readonlyTransactionManager,
				contractGasProvider);

		List<TransferEventResponse> listTransferEvents = usdc.transferEvent(DefaultBlockParameterName.EARLIEST,
				DefaultBlockParameterName.LATEST);

		for(TransferEventResponse t: listTransferEvents) {
			System.out.println("From: " + t.from + ", To: " + t.to + " Value: " + t.value);
		}
		
		// web3j usa um ScheduledThreadPoolExecutor, chamo exit para matar o processo e
		// todas as threads vinculadas
		System.exit(0);
	}
}
