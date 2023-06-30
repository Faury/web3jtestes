package br.com.sevencomm.web3j;

import java.math.BigInteger;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import br.com.sevencomm.web3j.model.Tokenerc20_sol_USDC;
import br.com.sevencomm.web3j.model.Tokenerc20_sol_USDC.TransferEventResponse;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

public class ReadTransferEventsFlowable {

	public static void main(String[] args) throws InterruptedException {

		String contractAddress = "0x3F5Bee678C541437F789465FEE265478B11e5B9d";

		Web3j web3j = Web3j.build(new HttpService("https://polygon-mumbai.g.alchemy.com/v2/demo"));

		DefaultGasProvider contractGasProvider = new DefaultGasProvider();
		ReadonlyTransactionManager readonlyTransactionManager = new ReadonlyTransactionManager(web3j, contractAddress);
		Tokenerc20_sol_USDC usdc = Tokenerc20_sol_USDC.load(contractAddress, web3j, readonlyTransactionManager,
				contractGasProvider);

		Flowable<TransferEventResponse> flowable = usdc.transferEventFlowable(DefaultBlockParameter.valueOf(BigInteger.valueOf(37369832)),
				DefaultBlockParameterName.LATEST);

		Disposable d = flowable.subscribeWith(new DisposableSubscriber<TransferEventResponse>() {
			@Override
			public void onStart() {
				System.out.println("Start!");
				request(1);
			}

			@Override
			public void onNext(TransferEventResponse t) {
				System.out.println("From: " + t.from + ", To: " + t.to + " Value: " + t.value);

				request(1);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("Done!");
			}
		});

		Thread.sleep(500);

		d.dispose();

		// web3j usa um ScheduledThreadPoolExecutor, chamo exit para matar o processo e
		// todas as threads vinculadas
		System.exit(0);
	}
}
