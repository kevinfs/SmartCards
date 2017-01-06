package com.iris.smartcard;

import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

public class TestSmartCard {
	private static CardTerminal cardTerminal;
	private static Card card;
	private static CardChannel cardChannel;
	private static CardCommandHelper cardCommandHelper;

	static public List<CardTerminal> getTerminals() throws CardException {
		return TerminalFactory.getDefault().terminals().list();
	}

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
//				if (cardCommandHelper.resetUserArea1()) {
//					System.out.println("User Area 1 successfully reset");
//				}
				if (cardCommandHelper.resetUserArea2()) {
					System.out.println("User Area 2 successfully reset");
				}
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
				String testWord3 = "bonjour";
				int numberOfBytesWritten = cardCommandHelper.updateUserArea1(testWord3);

				// Read User Data 1 to confirm update

				try {
					System.out.println("User Area 1 : " + cardCommandHelper.readUserArea1(numberOfBytesWritten));
				} catch (SecurityNotSatisfiedCardCommandException e) {
					System.err.println(e.getMessage());
				}

				// cardCommandHelper.readUserArea2(testWord1.length() / 2);

				card.disconnect(true);

			}
			cardTerminal.waitForCardAbsent(60000);
		}

	}

}
