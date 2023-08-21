package br.com.sevencomm.web3j;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.web3j.crypto.CipherException;
//import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

public class CreateWallet {

	public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException, IOException {
		
		String fileName = WalletUtils.generateNewWalletFile(
		        "password@teste",
		        new File("C:/keys/wallet"));
		
		/*Credentials credentials = WalletUtils.loadCredentials(
		        "password@teste",
		        "C:/keys/wallet/walletfile");*/
	}
}
