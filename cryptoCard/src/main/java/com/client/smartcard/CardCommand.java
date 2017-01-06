package com.client.smartcard;

import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

public class CardCommand {

	private CardChannel cardChannel;

	public CardCommand(CardChannel cardChannel) {
		this.cardChannel = cardChannel;
	}

	/**
	 * Generic read method
	 * 
	 * @param position
	 * @param size
	 * @return
	 */

	public byte[] read(int position, int size) throws SecurityNotSatisfiedCardCommandException {

		byte[] output = "".getBytes();
		CommandAPDU command = new CommandAPDU(0x80, 0xBE, 0x00, position, size);

		try {
			output = cardQuery(command);
		} catch (SecurityNotSatisfiedCardCommandException e1) {
			throw new SecurityNotSatisfiedCardCommandException(command);
		} catch (CardCommandException e) {
			System.err.println(e.getMessage());
		}

		return output;

	}

	/**
	 * Generic update method
	 * 
	 * @param position
	 * @param size
	 * @param data
	 * @return
	 */

	public boolean update(int position, int size, byte[] data) {

		CommandAPDU command = new CommandAPDU(0x80, 0xDE, 0x00, position, data, size);

		try {
			cardQuery(command);
		} catch (CardCommandException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;

	}

	/**
	 * Generic verify method
	 * 
	 * @param position
	 * @param CSC
	 *            Card Secret Code to test
	 * @return true if verification succeeds, false if CSC is wrong, false and
	 *         error message if error occurs
	 */

	public boolean verify(int position, byte[] CSC) {

		CommandAPDU command = new CommandAPDU(0x00, 0x20, 0x00, position, CSC, 0x04);

		try {
			cardQuery(command);
		} catch (InvalidSecretCodeCardCommandException e1) {
			return false;
		} catch (CardCommandException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;

	}

	/**
	 * Generic method to query data from the card
	 * 
	 * @param command
	 * @return
	 * @throws CardCommandException
	 */

	private byte[] cardQuery(CommandAPDU command) throws CardCommandException {

		ResponseAPDU r;
		byte[] output = "".getBytes();

		try {
			r = cardChannel.transmit(command);
			if (r.getSW() == 0x9000) {

				// Everything went fine
				output = r.getData();

			} else if (r.getSW() == 0x6581)
				throw new UnknownModeCardCommandException(command);

			else if (r.getSW() == 0x6700)
				throw new InvalidLengthCardCommandException(command);

			else if (r.getSW() == 0x6982)
				throw new SecurityNotSatisfiedCardCommandException(command);

			else if (r.getSW() == 0x6B00)
				throw new InvalidAddressCardCommandException(command);

			else if (r.getSW() == 0x6D00)
				throw new InvalidInstructionCardCommandException(command);

			else if (r.getSW() == 0x6300)
				throw new InvalidSecretCodeCardCommandException(command);

		} catch (CardException e) {
			System.err.println("Unable to execute query : " + CardUtils.decodeCommand(command.getData()));
		}

		return output;

	}

}
