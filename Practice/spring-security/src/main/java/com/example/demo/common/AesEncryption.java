package com.example.demo.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.example.demo.exception.EncryptionException;

@Service
public class AesEncryption {
	private SecretKeySpec secretKey;
	private static final String aesKey = "70c349b29b184dc3b5f6";

	@PostConstruct
	public void setKey() {
		MessageDigest sha = null;
		try {
			byte[] key = aesKey.getBytes(StandardCharsets.UTF_8);
			sha = MessageDigest.getInstance("SHA-256");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException("NoSuchAlgorithmException for Encryption");
		}
	}

	public String encrypt(String strToEncrypt) {
		if (strToEncrypt == null || strToEncrypt.isEmpty()) {
			return "";
		}
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new EncryptionException("Failed to encrypt data [" + strToEncrypt + "]");
		}
	}

	public String decrypt(String strToDecrypt) {
		if (strToDecrypt == null || strToDecrypt.isEmpty()) {
			return "";
		}
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			throw new EncryptionException("Failed to decrypt data [" + strToDecrypt + "]");
		}
	}

	public String passwordHashing(String password) {

		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new EncryptionException("Failed to hash data" + e.getMessage());
		}
		messageDigest.update(password.getBytes());
		byte[] bytes = messageDigest.digest();
		StringBuffer stringBuffer = new StringBuffer();
		for (byte b1 : bytes) {
			stringBuffer.append(Integer.toHexString(b1 & 0xff).toString());
		}
		return stringBuffer.toString();
	}

}
