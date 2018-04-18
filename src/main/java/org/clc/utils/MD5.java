package org.clc.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

public class MD5 {

	public static void main(String[] args) {
		String encoding = encoding("abcde");
		System.out.println(encoding + " --- " + encoding.length());
		String encoding1 = encoding("abcde");
		System.out.println(encoding1 + " --- " + encoding1.length());
	}

	public static String encoding(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(str.getBytes("utf-8"));
			return toHex(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String toHex(byte[] bytes) {
		final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++)
			ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]).append(HEX_DIGITS[bytes[i] & 0x0f]);
		return ret.toString();
	}

}
