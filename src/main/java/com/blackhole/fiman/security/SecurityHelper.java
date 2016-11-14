package com.blackhole.fiman.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityHelper {
	
	private final static Logger log = LoggerFactory.getLogger(SecurityHelper.class);
	
	public final static String ID = "$96$";
	
	public final static int ITERATIONS = 64000;
	
	private final static int SIZE = 128;
	
	private final static Random random = new SecureRandom();
	
	private final static Pattern layout = Pattern.compile("\\$96\\$(\\d{5,7})\\$(.+)");

	private final static String HEXES = "0123456789ABCDEF";

	/**
	 * Hash a password for storage.
	 *
	 * @return a secure authentication token to be stored for later authentication
	 */
	public static String hash(char[] password) {
		byte[] salt = new byte[SIZE / 8];
		random.nextBytes(salt);
		byte[] dk = pbkdf2(password, salt, ITERATIONS);
		byte[] hash = new byte[salt.length + dk.length];
		System.arraycopy(salt, 0, hash, 0, salt.length);
		System.arraycopy(dk, 0, hash, salt.length, dk.length);
		return ID + ITERATIONS + '$' + SecurityHelper.toString(hash);
	}

	/**
	 * Authenticate with a password and a stored password token.
	 *
	 * @return true if the password and token match
	 */
	public static boolean authenticate(char[] enteredPassword, String storedToken) {
		Matcher m = layout.matcher(storedToken);
		if (!m.matches()) {
			throw new IllegalArgumentException("Invalid token format");
		}
		int iterations = Integer.parseInt(m.group(1));
		byte[] hash = SecurityHelper.toByteArray(m.group(2));
		byte[] salt = Arrays.copyOfRange(hash, 0, SIZE / 8);
		byte[] check = pbkdf2(enteredPassword, salt, iterations);
		int zero = 0;
		for (int idx = 0; idx < check.length; ++idx) {
			zero |= hash[salt.length + idx] ^ check[idx];
		}
		return zero == 0;
	}

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations) {
		KeySpec spec = new PBEKeySpec(password, salt, iterations, SIZE);
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return f.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException ex) {
			throw new IllegalStateException("Missing algorithm: PBKDF2WithHmacSHA1", ex);
		} catch (InvalidKeySpecException ex) {
			throw new IllegalStateException("Invalid SecretKeyFactory", ex);
		}
	}
	
	/**
	 * Convert hex string into a byte array
	 *
	 * @param hexString
	 * @return
	 */
	public static byte[] toByteArray(String hexString) {

		if (hexString == null) {
			return null;
		}

		byte[] b = new byte[hexString.length() / 2];
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(hexString.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		return b;
	}

	/**
	 * Convert a byte array into a hex string
	 *
	 * @param raw
	 * @return
	 */
	public static String toString(byte[] raw) {
		StringBuffer hex = new StringBuffer(2 * raw.length);
		for (int i = 0; i < raw.length; i++) {
			byte b = raw[i];
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
}
