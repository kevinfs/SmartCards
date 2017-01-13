package com.client.smartcard;

import java.security.Key;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.smartcardio.CardChannel;

public class CardCommandHelper {

	private CardCommand cardCommand;

	public CardCommandHelper(CardChannel cardChannel) {
		super();
		this.cardCommand = new CardCommand(cardChannel);
	}

	/************************************************************
	 * Card Secret Codes
	 ************************************************************/

	/**
	 * Read CSCs counters
	 */

	public String readCSC0Counter() {
		try {
			return CardUtils.decodeToBytesForUser(cardCommand.read(0x07, 0x04));
		} catch (SecurityNotSatisfiedCardCommandException e) {
			return "";
		}
	}

	public String readCSC1Counter() {
		try {
			return CardUtils.decodeToBytesForUser(cardCommand.read(0x39, 0x04));
		} catch (SecurityNotSatisfiedCardCommandException e) {
			return "";
		}
	}

	public String readCSC2Counter() {
		try {
			return CardUtils.decodeToBytesForUser(cardCommand.read(0x3B, 0x04));
		} catch (SecurityNotSatisfiedCardCommandException e) {
			return "";
		}
	}

	/**
	 * Read CSCs
	 */

	public String readCSC0() throws SecurityNotSatisfiedCardCommandException {
		return CardUtils.decodeToBytesForUser(cardCommand.read(0x06, 0x04));
	}

	public String readCSC1() throws SecurityNotSatisfiedCardCommandException {
		return CardUtils.decodeToBytesForUser(cardCommand.read(0x38, 0x04));
	}

	public String readCSC2() throws SecurityNotSatisfiedCardCommandException {
		return CardUtils.decodeToBytesForUser(cardCommand.read(0x3A, 0x04));
	}

	/**
	 * Verify CSCs
	 */

	public boolean verifyCSC0() {
		return cardCommand.verify(0x07, CardUtils.CSC0);
	}

	public boolean verifyCSC1() {
		return cardCommand.verify(0x39, CardUtils.CSC1);
	}

	public boolean verifyCSC2() {
		return cardCommand.verify(0x3B, CardUtils.CSC2);
	}

	/**
	 * Update CSCs
	 */

	public boolean updateCSC1(byte[] CSC) {
		return cardCommand.update(0x38, 0x04, CSC);
	}

	public boolean updateCSC2(byte[] CSC) {
		return cardCommand.update(0x3A, 0x04, CSC);
	}

	/************************************************************
	 * User Areas
	 ************************************************************/

	/**
	 * Read User Areas
	 */

	public String readUserArea1(Integer sizeToRead) throws SecurityNotSatisfiedCardCommandException {
		return CardUtils.decodeToTextForUser(cardCommand.read(0x10, sizeToRead));
	}

	public String readUserArea2(Integer sizeToRead) throws SecurityNotSatisfiedCardCommandException {
		return CardUtils.decodeToTextForUser(cardCommand.read(0x28, sizeToRead));
	}

	/**
	 * Update User Areas
	 */

	public int updateUserArea1(String data) {

		// Convert data to bytes
		byte[] bytes = CardUtils.encodeTextForCard(data);

		if (cardCommand.update(0x10, bytes.length, bytes))
			return bytes.length;
		else
			return 0;

	}

	public int updateUserArea1(byte[] bytes) {

		byte[] bytesPadded = CardUtils.addPadding(bytes);

		if (cardCommand.update(0x10, bytesPadded.length, bytesPadded))
			return bytesPadded.length;
		else
			return 0;

	}

	public int updateUserArea2(String data) {

		// Convert data to bytes
		byte[] bytes = CardUtils.encodeTextForCard(data);

		if (cardCommand.update(0x28, bytes.length, bytes))
			return bytes.length;
		else
			return 0;

	}

	public int updateUserArea2(byte[] bytes) {

		byte[] bytesPadded = CardUtils.addPadding(bytes);

		System.out.println("lalalalalalalla " + bytesPadded.length);

		if (cardCommand.update(0x28, bytesPadded.length, bytesPadded))
			return bytesPadded.length;
		else
			return 0;

	}

	/**
	 * Reset User Areas
	 */

	public boolean resetUserArea1() {

		// Create empty buffer
		byte[] bytes = new byte[64];
		for (int i = 0; i < 64; i++)
			bytes[i] = (byte) 0x00;

		return cardCommand.update(0x10, 64, bytes);

	}

	public boolean resetUserArea2() {

		// Create empty buffer
		byte[] bytes = new byte[64];
		for (int i = 0; i < 64; i++)
			bytes[i] = (byte) 0x00;

		return cardCommand.update(0x28, 64, bytes);

	}

	/**
	 * Store Secret Key
	 */

	public boolean storeSecretKey(Key secretKey) {
		String keyEncoded = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		System.out.println(keyEncoded);

		String part1 = subString(keyEncoded, 0, 63);
		// System.out.println(part1.length());
		// System.out.println(part1);
		String part2 = subString(keyEncoded, 63, 64);
		// System.out.println(part2.length());
		// System.out.println(part2);

		if (updateUserArea1(part1) > 0 && updateUserArea2(part2) > 0)
			return true;
		else
			return false;

	}

	public Key retrieveSecretKey() {

		verifyCSC0();
		String part1 = "";
		String part2 = "";

		try {

			part1 = readUserArea1(64);
			part2 = readUserArea2(64);

			System.out.println("reconstructed :" + part1 + part2);
			System.out.println(part1);
			System.out.println(part2);

		} catch (SecurityNotSatisfiedCardCommandException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// decode the base64 encoded string

		byte[] decodedKey = Base64.getMimeDecoder().decode(part1 + part2);
		// rebuild key using SecretKeySpec
		SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "EC");

		// KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
		// KeySpec ks = new PKCS8EncodedKeySpec((keyPart1Read +
		// keyPart2Read).getBytes());
		// PublicKey privKey = (PublicKey) keyFactory.generatePrivate(ks);

		return secretKey;

	}

	private String subString(String myString, int start, int length) {
		return myString.substring(start, Math.min(start + length, myString.length()));
	}

}
