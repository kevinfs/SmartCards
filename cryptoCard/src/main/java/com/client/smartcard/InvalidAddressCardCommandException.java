package com.client.smartcard;

import javax.smartcardio.CommandAPDU;

public class InvalidAddressCardCommandException extends CardCommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6952321128779391195L;

	public InvalidAddressCardCommandException(CommandAPDU command) {
		super(command, "Invalid P2 parameter / PB_PARAM");
	}

}
