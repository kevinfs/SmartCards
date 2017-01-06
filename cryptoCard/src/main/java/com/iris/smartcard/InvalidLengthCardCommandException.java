package com.iris.smartcard;

import javax.smartcardio.CommandAPDU;

public class InvalidLengthCardCommandException extends CardCommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5020650042513859189L;

	public InvalidLengthCardCommandException(CommandAPDU command) {
		super(command, "Invalid length of expected data / PB_LONG");
	}
}
