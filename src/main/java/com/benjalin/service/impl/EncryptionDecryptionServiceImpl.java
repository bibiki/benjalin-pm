package com.benjalin.service.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.benjalin.service.EncryptionDecryptionServiceInt;

public class EncryptionDecryptionServiceImpl implements EncryptionDecryptionServiceInt {

	private static final String ENCRYPTION_ALGORITHM = "AES";
	private static Cipher cipher;
	private static final int ENCRYPTION_KEY_LENGTH = 32;
	
	public EncryptionDecryptionServiceImpl() {
		try {
			cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String encrypt(String plainText, String key) {
		try {
			SecretKey secretKey = this.getSecretKey(key);
			byte[] plainTextByte = plainText.getBytes();
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedByte = cipher.doFinal(plainTextByte);
			Base64.Encoder encoder = Base64.getEncoder();
			String encryptedText = encoder.encodeToString(encryptedByte);
			return encryptedText;
		}
		catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException exception) {
			exception.printStackTrace();
			throw new IllegalArgumentException("Failed to ecnrypt. Please make sure the given key is a valid one!");
		}
	}
	
	@Override
	public String decrypt(String encryptedText, String key) {
		try {
			key = this.normalizeStringLength(key, ENCRYPTION_KEY_LENGTH);
			SecretKey secretKey = this.getSecretKey(key);
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] encryptedTextByte = decoder.decode(encryptedText);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
			String decryptedText = new String(decryptedByte);
			return decryptedText;
		}
		catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException exception) {
			exception.printStackTrace();
			throw new IllegalArgumentException("Failed to decrypt. Please make sure the given key is a valid one!");
		}
	}
	
	public static String encrypt(String plainText, SecretKey secretKey)
			throws Exception {
		byte[] plainTextByte = plainText.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}

	public static String decrypt(String encryptedText, SecretKey secretKey)
			throws Exception {
		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		String decryptedText = new String(decryptedByte);
		return decryptedText;
	}

	private String normalizeStringLength(String string, int expectedLength) {
		while(string.length() < expectedLength) {
			string += string;
		}
		return string.substring(0, expectedLength);
	}
	
	private SecretKey getSecretKey(String key) {
		byte[] decodedKey = Base64.getDecoder().decode(normalizeStringLength(key, ENCRYPTION_KEY_LENGTH));
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ENCRYPTION_ALGORITHM);
		return originalKey;
	}
}