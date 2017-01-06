package com.iris.smartcard;

import javax.smartcardio.CommandAPDU;

public class UnknownModeCardCommandException extends CardCommandException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5992285558206211582L;

	public UnknownModeCardCommandException(CommandAPDU command) {
		super(command, "Unknown mode / PB_MEM");
	}

}
