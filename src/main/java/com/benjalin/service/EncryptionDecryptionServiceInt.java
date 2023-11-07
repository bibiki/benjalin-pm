package com.benjalin.service;

public interface EncryptionDecryptionServiceInt {

	String encrypt(String plainText, String key);
	
	String decrypt(String string, String key);
}
