package com.client.smartcard;

import javax.smartcardio.CommandAPDU;

public class InvalidSecretCodeCardCommandException extends CardCommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1636441353743826473L;

	public InvalidSecretCodeCardCommandException(CommandAPDU command) {
		super(command, "Invalid Secret Code");
	}

}
