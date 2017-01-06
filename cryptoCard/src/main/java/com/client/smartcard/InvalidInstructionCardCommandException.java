package com.client.smartcard;

import javax.smartcardio.CommandAPDU;

public class InvalidInstructionCardCommandException extends CardCommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7139311344473277055L;

	public InvalidInstructionCardCommandException(CommandAPDU command) {
		super(command, "Invalid instruction byte (INS) / PB_INS");
	}

}
