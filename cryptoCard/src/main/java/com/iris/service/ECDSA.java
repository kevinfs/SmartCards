package com.iris.service;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

public class ECDSA {

	private PublicKey pubKey;
	private PrivateKey privKey;
	private Signature ecdsa;
	private byte[] baSignature;
	private byte[] baText;

	public ECDSA(String text) throws GeneralSecurityException, Exception {

		KeyPairGenerator kpg;
		kpg = KeyPairGenerator.getInstance("EC", "SunEC");

		ECGenParameterSpec ecsp;
		ecsp = new ECGenParameterSpec("secp256r1");
		kpg.initialize(ecsp);

		KeyPair kp = kpg.genKeyPair();

		this.privKey = kp.getPrivate();
		this.pubKey = kp.getPublic();

		this.ecdsa = Signature.getInstance("SHA1withECDSA", "SunEC");
		this.ecdsa.initSign(this.privKey);

		this.baText = text.getBytes("UTF-8");

		this.ecdsa.update(this.baText);
		this.baSignature = this.ecdsa.sign();
	}

	public PublicKey getPubKey() {
		return pubKey;
	}

	public byte[] getBaText() {
		return baText;
	}

	public PrivateKey getPrivKey() {
		return privKey;
	}

	public Signature getEcdsa() {
		return ecdsa;
	}

	public byte[] getBaSignature() {
		return baSignature;
	}
}
