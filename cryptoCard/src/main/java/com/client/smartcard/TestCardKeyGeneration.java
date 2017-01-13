package com.client.smartcard;

import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import com.iris.service.ECDSA;

public class TestCardKeyGeneration {
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

		cardTerminal.waitForCardPresent(4000);
		if (cardTerminal.isCardPresent()) {

			connectToCard();

			System.out.println("Number of PIN attempts on CSC0 : " + cardCommandHelper.readCSC0Counter());
			System.out.println("Number of PIN attempts on CSC1 : " + cardCommandHelper.readCSC1Counter());
			System.out.println("Number of PIN attempts on CSC2 : " + cardCommandHelper.readCSC2Counter());

			cardCommandHelper.verifyCSC0();

			String login = "anselm";

			ECDSA ecdsa;
			try {
				ecdsa = new ECDSA(login);
				cardCommandHelper.storeSecretKey(ecdsa.getPubKey());
				cardCommandHelper.resetUserArea1();
				cardCommandHelper.resetUserArea2();
				
				cardCommandHelper.storeSecretKey(ecdsa.getPrivKey());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			card.disconnect(true);

		}
		cardTerminal.waitForCardAbsent(60000);

	}

}
