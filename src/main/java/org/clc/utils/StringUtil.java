package org.clc.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String camel2underline(String source) {
		return camel2underline(source, false);
	}

	private static String camel2Underline(String propertyName) {
		return Pattern.compile("([A-Z])").matcher(propertyName).replaceAll("_$0").toUpperCase();
	}

	public static String camel2underline(String source, boolean toUpper) {
		char[] chars = source.toCharArray();
		StringBuilder targer = new StringBuilder(0);
		boolean flag = true;
		for (char c : chars) {
			if (flag) {
				targer.append(c);
				flag = false;
				continue;
			}
			if (64 < c && c < 91)
				targer.append("_" + (char) (c + 32));
		}
		return toUpper ? targer.toString().toUpperCase() : targer.toString();
	}

	public static String underline2camel(String source) {
		source = source.toLowerCase();
		Matcher matcher = Pattern.compile("_\\w").matcher(source);
		while (matcher.find())
			source = source.replaceAll(matcher.group(),matcher.group().replace("_","").toUpperCase());
		return source;
	}

	public static String getMD5(String fileName) {
		StringBuilder rtn = new StringBuilder(0);
		try {
			DigestInputStream inStream = new DigestInputStream(new BufferedInputStream(new FileInputStream(
					fileName)), MessageDigest.getInstance("MD5"));
			byte[] buf = new byte[1024];
			for (; ; )
				if (inStream.read(buf) <= 0)
					break;
			inStream.close();
			MessageDigest md5 = inStream.getMessageDigest();
			byte[] digest = md5.digest();
			for (int i = 0; i < digest.length; i++) {
				rtn.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
				rtn.append(Integer.toString(digest[i] & 0x0f, 16));
			}
		} catch (Exception e) {
		}
		return rtn.toString();
	}
}
