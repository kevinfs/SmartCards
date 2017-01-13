package com.client.smartcard;

import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import com.iris.service.ECDSA;
import com.iris.service.Tools;

public class TestSmartCard {
	private static CardTerminal cardTerminal;
	private static Card card;
	private static CardChannel cardChannel;
	private static CardCommandHelper cardCommandHelper;

	static public String toString(byte[] byteTab) {
		String texte = "";
		String hexNombre;
		for (int i = 0; i < byteTab.length; i++) {
			hexNombre = "";
			hexNombre = Integer.toHexString(byteTab[i]);
			if (hexNombre.length() == 1) {
				texte += " 0" + hexNombre;
			} else {
				texte += " " + hexNombre;
			}
		}
		return texte;
	}

	public static List<CardTerminal> getTerminals() throws CardException {
		return TerminalFactory.getDefault().terminals().list();
	}

	public static void connectToTerminal() {
		List<CardTerminal> terminauxDispos;
		try {
			terminauxDispos = TestSmartCard.getTerminals();
			if (terminauxDispos.size() > 0) {
				cardTerminal = terminauxDispos.get(0);
				System.out.println("Connected to : " + cardTerminal.toString());
			}
		} catch (CardException e) {
			System.err.println("Unable to find a terminal");
		}
	}

	public static void connectToCard() {
		try {
			// Card connection
			card = cardTerminal.connect("T=0");

			// ATR (answer To Reset)
			System.out.println("Connected to : " + toString(card.getATR().getBytes()));
			System.out.println("Connected to : " + CardUtils.decodeToBytesForUser(card.getATR().getBytes()));

			// Open channel
			cardChannel = card.getBasicChannel();
			cardCommandHelper = new CardCommandHelper(cardChannel);
		} catch (CardException e) {
			System.err.println("Unable to connect to a card");
		}
	}

	/**
	 * 
	 * @param args
	 * @throws CardException
	 */
	public static void main(String[] args) throws CardException {

		connectToTerminal();

		while (true) {

			cardTerminal.waitForCardPresent(4000);
			if (cardTerminal.isCardPresent()) {

				connectToCard();

				System.out.println("Number of PIN attempts on CSC0 : " + cardCommandHelper.readCSC0Counter());
				System.out.println("Number of PIN attempts on CSC1 : " + cardCommandHelper.readCSC1Counter());
				System.out.println("Number of PIN attempts on CSC2 : " + cardCommandHelper.readCSC2Counter());

				try {
					cardCommandHelper.readCSC0();
				} catch (SecurityNotSatisfiedCardCommandException e) {
					System.out.println("Access denied. You must fill in your PIN first.");
				}

				if (!cardCommandHelper.verifyCSC0()) {
					System.out.println("Wrong PIN, " + cardCommandHelper.readCSC0Counter() + " attemps remaining !");
				}

				try {
					System.out.println(cardCommandHelper.readCSC0());
				} catch (SecurityNotSatisfiedCardCommandException e) {
					System.out.println("You must fill in your PIN first. " + cardCommandHelper.readCSC0Counter()
							+ " attemps remaining !");
				}

				// updateCSC1();
				// updateCSC2();

				// cardCommandHelper.readCSC1Counter();
				// cardCommandHelper.verifyCSC1();
				// cardCommandHelper.readCSC1();
				//
				// cardCommandHelper.readCSC2Counter();
				// cardCommandHelper.verifyCSC2();
				// cardCommandHelper.readCSC2();
				//
				// if (cardCommandHelper.resetUserArea1()) {
				// System.out.println("User Area 1 successfully reset");
				// }
				// if (cardCommandHelper.resetUserArea2()) {
				// System.out.println("User Area 2 successfully reset");
				// }
				//
				// String testWord1 = "AABBCCDDEEFF001122334455";
				// String testWord2 = "tototata";
				// System.out.println("toto ? " + testWord2.getBytes());

				// Read User Data 1

				try {
					System.out.println("User Area 1 : " + cardCommandHelper.readUserArea1(8));
				} catch (SecurityNotSatisfiedCardCommandException e) {
					System.err.println(e.getMessage());
				}

				// Update User Data 1
				// String testWord3 = "bonjour";
				// int numberOfBytesWritten =
				// cardCommandHelper.updateUserArea1(testWord3);

				// Read User Data 1 to confirm update

				// try {
				// System.out.println("User Area 1 : " +
				// cardCommandHelper.readUserArea1(numberOfBytesWritten));
				// } catch (SecurityNotSatisfiedCardCommandException e) {
				// System.err.println(e.getMessage());
				// }

				// ECDSA Test
				// cardCommandHelper.resetUserArea1();
				try {
					String login = "kevinfs2";
					ECDSA ecdsa = new ECDSA("kevinfs2");

					// cardCommandHelper.storeSecretKey(ecdsa.getPrivKey());

					// Access
					// String keyPart1Read =
					// cardCommandHelper.readUserArea1(64);
					// System.out.println(keyPart1Read);
					// String keyPart2Read =
					// cardCommandHelper.readUserArea2(28);
					// System.out.println(keyPart2Read);

					Key privateKey = cardCommandHelper.retrieveSecretKey();

					// Sign
					Signature ecdsaSign = Signature.getInstance("SHA1withECDSA", "SunEC");
					ecdsaSign.initSign((PrivateKey) privateKey);
					ecdsaSign.update(login.getBytes());
					byte[] signature = ecdsaSign.sign();

					// KeySpec ks = new PKCS8EncodedKeySpec((keyPart1Read +
					// keyPart2Read).getBytes());
					// PublicKey privKey = (PublicKey)
					// keyFactory.generatePrivate(ks);

					// Verify
					// Sami
					String key = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEjOp8Zfh2e2SB+GvFNn6tk5LahS8FGIGDvjNySOL9u/+wqoXX1Q4f5mYPSc2ByfTi6WMTRgwU+/J9r7VXvTs+nw==";
					// Kevin
//					String key = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAETdK8yRG3RlJl4DWMlMr54I7eWKqD/ushvbevyulVq96YDNas/7L6galhe1t9590JHyHsQeXt7nbTRmO1vEbD7w==";
					
					byte[] decodedKey = Base64.getMimeDecoder().decode(key);
					KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
					X509EncodedKeySpec ecpks = new X509EncodedKeySpec(decodedKey);
					PublicKey publicKey = keyFactory.generatePublic(ecpks);

					// PublicKey secretKey = new SecretKeySpec(decodedKey, 0,
					// decodedKey.length, "EC");
					boolean b = Tools.verifECDSA(login.getBytes(), publicKey, signature);
					System.out.println("Valid : " + b + "\n");

				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// cardCommandHelper.readUserArea2(testWord1.length() / 2);

				card.disconnect(true);

			}
			cardTerminal.waitForCardAbsent(60000);
		}

	}

}
