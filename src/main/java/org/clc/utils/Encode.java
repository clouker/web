package org.clc.utils;

import java.security.MessageDigest;

public class Encode {

	public static void main(String[] args) {
		String encoding = MD5("2");
		System.out.println(encoding + " --- " + encoding.length());
		String encoding1 = MD5("1");
		System.out.println(encoding1 + " --- " + encoding1.length());
	}

	/**
	 * MD5加密
	 *
	 * @param str
	 * @return
	 */
	public static String MD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("Encode");
			byte[] bytes = md.digest(str.getBytes("utf-8"));
			return toHex(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String toHex(byte[] bytes) {
		final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		for (byte aByte : bytes) ret.append(HEX_DIGITS[(aByte >> 4) & 0x0f]).append(HEX_DIGITS[aByte & 0x0f]);
		return ret.toString();
	}

}
