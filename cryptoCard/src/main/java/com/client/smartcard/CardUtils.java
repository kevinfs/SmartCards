package com.client.smartcard;

public class CardUtils {

	public static byte[] CSC0 = new byte[] { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA };
	public static byte[] CSC1 = new byte[] { (byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11 };
	public static byte[] CSC2 = new byte[] { (byte) 0x22, (byte) 0x22, (byte) 0x22, (byte) 0x22 };
	public static byte[] word1 = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD };
	public static byte[] word2 = new byte[] { (byte) 0xEE, (byte) 0xFF, (byte) 0x00, (byte) 0x11 };
	public static byte[] word3 = new byte[] { (byte) 0xAA, (byte) 0xBB, (byte) 0xCC, (byte) 0xDD, (byte) 0xEE,
			(byte) 0xFF, (byte) 0x00, (byte) 0x11 };

	/**
	 * String "AABBCCDD" becomes byte[] {0xDD, 0xCC, 0xBB, 0xAA}
	 * 
	 * @param s
	 *            String to encode containing byte representation of a word
	 * @return byte array usable by the card
	 */
	public static byte[] encodeBytesForCard(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = len - 1; i > 0; i -= 2) {
			data[(len - i)
					/ 2] = (byte) ((Character.digit(s.charAt(i - 1), 16) << 4) + Character.digit(s.charAt(i), 16));
		}

		return data;
	}

	/**
	 * String "AABBCCDD" becomes byte[] {0xDD, 0xCC, 0xBB, 0xAA}
	 * 
	 * @param s
	 *            String to encode containing human-readable text
	 * @return byte array usable by the card
	 */
	public static byte[] encodeTextForCard(String s) {
		int length = s.length();
		int finalLength = length;
		while (finalLength % 4 != 0) {
			finalLength++;
		}

		// Fix bug of missing last char when finalLength == length
//		int copySize = finalLength - 1;
//		if (finalLength == length) {
//			copySize = finalLength;
//		}

		// Now we know adequate length for transmission,
		// we can create the array of bytes
		byte[] bytes = new byte[finalLength];
		byte[] tmp = s.getBytes();
		// and fill it with the string
		for (int i = 0; i < tmp.length; i++) {
			System.arraycopy(tmp, i, bytes, finalLength - 1 - i, 1);
		}

		// Add some padding at the beginning
		if (finalLength > length) {
			byte[] padding = new byte[finalLength - length];

			for (int i = 0; i < finalLength - length; i++)
				padding[i] = (byte) 0x00;

			System.arraycopy(padding, 0, bytes, 0, finalLength - length);
		}

		return bytes;

	}

	/**
	 * byte[] {0xDD, 0xCC, 0xBB, 0xAA} becomes String "AABBCCDD"
	 * 
	 * @param bytes
	 *            bytes to decode
	 * @return String containing the complete word
	 */
	public static String decodeToBytesForUser(byte[] bytes) {
		StringBuilder finalString = new StringBuilder();
		StringBuilder tmpString = new StringBuilder();

		int i = 1;
		for (byte b : bytes) {
			// For each byte, prepend formatted value
			tmpString.insert(0, String.format("%02X", b));

			// Make groups of four bytes
			if ((i) % 4 == 0) {
				finalString.insert(0, tmpString);
				tmpString.delete(0, tmpString.length());
			}
			i++;
		}

		return finalString.toString();
	}

	/**
	 * byte[] {0xDD, 0xCC, 0xBB, 0xAA} becomes String "AABBCCDD"
	 * 
	 * @param bytes
	 *            bytes to decode
	 * @return String containing the complete word
	 */
	public static String decodeToTextForUser(byte[] bytes) {
		// Revert array
		byte[] revertedBytes = new byte[bytes.length];
		for (int i = bytes.length - 1; i > 0; i--) {
			revertedBytes[bytes.length - 1 - i] = bytes[i];
		}
		// Convert to string
		String finalString = new String(revertedBytes);
		return finalString;
	}

	public static String toHexTrueOrder(byte[] bytes) {
		StringBuilder buff = new StringBuilder();
		for (byte b : bytes) {
			buff.append(String.format("%02X", b));
		}

		return buff.toString();
	}

	public static String decodeCommand(byte[] data) {
		StringBuilder finalString = new StringBuilder();

		// For each byte, append formatted value
		for (byte b : data) {
			finalString.append(String.format("0x%02X ", b));
			// System.out.println("a");
		}

		return finalString.toString();
	}

}
