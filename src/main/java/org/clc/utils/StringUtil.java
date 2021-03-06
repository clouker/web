package org.clc.utils;

import org.apache.commons.codec.digest.DigestUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 驼峰转下划线
     *
     * @param source  源字符串
     * @param toUpper 是否转大写
     * @return 目标
     */
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
                targer.append("_").append((char) (c + 32));
        }
        return toUpper ? targer.toString().toUpperCase() : targer.toString().toLowerCase();
    }

    /**
     * 驼峰转下划线&小写
     *
     * @param source 源字符串
     * @return 目标
     */
    public static String camel2underline$lower(String source) {
        return source.replaceAll("([A-Z])", "_$0").toLowerCase();
    }

    /**
     * 驼峰转下划线&大写
     *
     * @param source 源字符串
     * @return 目标
     */
    public static String camel2Underline$upper(String source) {
        return source.replaceAll("([A-Z])", "_$0").toUpperCase();
    }

    /**
     * 下线转驼峰
     *
     * @param source 源字符串
     * @return 目标
     */
    public static String underline2camel(String source) {
        Matcher matcher = Pattern.compile("_\\w").matcher(source);
        while (matcher.find())
            source = source.replaceAll(matcher.group(), matcher.group().replace("_", "").toUpperCase());
        return source;
    }

    /**
     * 在字符串（source）指定字符串（reg）前 添加新的字符串（addStr）
     */
    public static String insertStrBefore(String source, String addStr, char reg) {
        return insertStr(source, addStr, reg, 0);
    }

    /**
     * 在字符串（source）指定字符串（reg）后 添加新的字符串（addStr）
     */
    public static String insertStrAfter(String source, String addStr, char reg) {
        return insertStr(source, addStr, reg, 1);
    }

    /**
     * 在字符串（source）指定字符（reg）[前|后] 添加新的字符串（addStr）
     *
     * @param source        源字符串
     * @param addStr        待添加字符串
     * @param reg           指定字符位置--支持通配符
     * @param beforeOrafter 0前1后
     * @return 目标
     */
    private static String insertStr(String source, String addStr, char reg, int beforeOrafter) {
        if (beforeOrafter == 0)
            return source.replaceAll("[(" + reg + ")]", addStr + "$0");
        else if (beforeOrafter == 1)
            return source.replaceAll("[(" + reg + ")]", "$0" + addStr);
        return source;
    }

    public static void main(String[] args) {
        String s = "qweqwe中e.123";
        System.err.println(s + "===>\n\t" + insertStrBefore(s, "不", '中'));
    }

    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     */
    public static String md5(String text, String key) {
        if (text == null) return "";
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


    /**
     * 图片解析(Base64字符串)
     * @param input 图片流
     */
    public static String getIMGStr(InputStream input) {
        byte[] data = null;
        try {
            data = new byte[input.available()];
            int read = input.read(data);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(data != null ? data : new byte[0]);
    }

    /**
     * base64字符串转化成图片
     * @param imgStr base64Str
     */
    public static boolean GenerateImage(String imgStr) {
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0)// 调整异常数据
                    b[i] += 256;
            }
            // 生成jpeg图片
            String imgFilePath = "d:\\222.jpg";
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
