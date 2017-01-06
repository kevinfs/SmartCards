package com.client.smartcard;

import javax.smartcardio.CommandAPDU;

public class SecurityNotSatisfiedCardCommandException extends CardCommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -583895792320749856L;

	public SecurityNotSatisfiedCardCommandException(CommandAPDU command) {
		super(command, "Security not satisfied / PB_SECUR");
	}

}
