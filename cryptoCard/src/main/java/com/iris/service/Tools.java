package com.iris.service;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;

@Service
public class Tools {

	@SuppressWarnings("unused")
	public String md5(String password) {
		byte[] uniqueKey = password.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
		} catch (NoSuchAlgorithmException e) {
			throw new Error("No MD5 support in this VM.");
		}

		StringBuilder hashString = new StringBuilder();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				hashString.append('0');
				hashString.append(hex.charAt(hex.length() - 1));
			} else
				hashString.append(hex.substring(hex.length() - 2));
		}
		return hashString.toString();
	}

	public int alea(int n) {
		return (int) (Math.random() * n);
	}

	public String ECDSA(String s) throws Exception{
		/*
		 * Generate an ECDSA signature
		 */

		/*
		 * Generate a key pair
		 */

		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

		keyGen.initialize(256, random);

		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey priv = pair.getPrivate();
		PublicKey pub = pair.getPublic();

		/*
		 * Create a Signature object and initialize it with the private key
		 */

		Signature dsa = Signature.getInstance("SHA256withECDSA");

		dsa.initSign(priv);

		byte[] strByte = s.getBytes("UTF-8");
		dsa.update(strByte);

		/*
		 * Now that all the data to be signed has been read in, generate a
		 * signature for it
		 */

		byte[] realSig = dsa.sign();
		String signature =  new BigInteger(1, realSig).toString(16);
		return signature;

	}
}
