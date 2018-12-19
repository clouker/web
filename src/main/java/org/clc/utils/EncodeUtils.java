package org.clc.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * 加密工具类
 */
public class EncodeUtils {

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     */
    public static String md5(String text, String key) {
        if (text == null)
            return "";
        return DigestUtils.md5Hex(new StringBuffer(text).reverse().toString() + key);
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     */
    public static boolean verifyMd5(String text, String key, String md5) {
        String md5Text = md5(text, key);
        return md5Text.equalsIgnoreCase(md5);
    }
}
