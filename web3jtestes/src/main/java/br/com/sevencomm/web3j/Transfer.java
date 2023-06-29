package br.com.sevencomm.web3j;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import br.com.sevencomm.web3j.model.Tokenerc20_sol_USDC;

public class Transfer {

	public static void main(String[] args) throws Exception {
		String contractAddress = "0x3F5Bee678C541437F789465FEE265478B11e5B9d";
		String to = "0x47bddffaB5057c2725dde8E22aBe63A9E4091E25";
		
		// Busco a chave da minha carteira
        String pk = Files.readString(Path.of("C:/keys/key.txt"));
        
		Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.g.alchemy.com/v2/demo"));

		Long chainId = Long.parseLong(web3j.netVersion().send().getNetVersion());
        Credentials credentials = Credentials.create(pk);
        
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        
		DefaultGasProvider contractGasProvider = new DefaultGasProvider();
		
		Tokenerc20_sol_USDC usdc = Tokenerc20_sol_USDC.load(contractAddress, web3j, transactionManager, contractGasProvider);
		
		TransactionReceipt transactionReceipt = usdc.transfer(to, BigInteger.valueOf(1000000)).send();

		if (transactionReceipt.isStatusOK()) {
            System.out.println("Transferência bem sucedida!");
        } else {
        	System.out.println("Erro ao fazer Transferência! " + transactionReceipt.getRevertReason());
        }
		
		// web3j usa um ScheduledThreadPoolExecutor, chamo exit para matar o processo e todas as threads vinculadas
		System.exit(0);
	}

}
