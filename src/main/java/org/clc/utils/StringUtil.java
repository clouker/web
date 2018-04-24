package org.clc.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String camel2underline(String source) {
		return camel2underline(source, false);
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
}
