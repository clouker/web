package org.clc.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

public class MD5 {

    public static void main(String[] args) {
        jdkMD5("12s3@!213456");
        ccMD5("12s3@!213456");
    }

    // 用jdk实现:MD5
    public static void jdkMD5(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(src.getBytes());
            System.out.println("JDK MD5:" + Hex.encodeHexString(md5Bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 用common codes实现实现:MD5
    public static void ccMD5(String src) {
        System.out.println("common codes MD5:" + DigestUtils.md5Hex(src.getBytes()));
    }

}
