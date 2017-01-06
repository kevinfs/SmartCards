package com.client.smartcard;

import javax.smartcardio.CommandAPDU;

public class CardCommandException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8776924948392384865L;

	public CardCommandException(CommandAPDU command, String message) {
		super(message + ", when executing command " + CardUtils.decodeCommand(command.getData()) + " ("
				+ command.getData() + ").");
	}

}
