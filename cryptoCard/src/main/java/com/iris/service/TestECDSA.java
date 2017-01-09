package com.iris.service;

public class TestECDSA {
	
	public static void main(String [] args) throws Exception{

		ECDSA e = new ECDSA("SmartCards");
		ECDSA e2 = new ECDSA("CarteAPuce");
		
		Tools t = new Tools();
		
		boolean b = t.verifECDSA(e.getBaText(), e.getPubKey(), e.getBaSignature());
		System.out.println("Valid : " + b + "\n");
		
		boolean b2 = t.verifECDSA(e.getBaText(), e2.getPubKey(), e.getBaSignature());
		System.out.println("Valid : " + b2);
	}
}
