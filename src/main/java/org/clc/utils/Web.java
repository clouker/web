package org.clc.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

public class Web {

    //返回对象
    private Map<String, ?> response;

    enum Code {
        SUCCESS(200, "ok"),
        REDIRECT(302, "redirect");
        public int code;
        public String msg;

        Code(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

    public static void main(String[] args) {
//        String url = "http://www.baidu.com";
//        String url = "https://10minutemail.net/cdn/js/ads.js";
        String url = "http://v3.jiathis.com/code/images/counter.gif";
//        String url = "https://s7.addthis.com/l10n/client.zh.min.json";
        get(url, null);
    }

    public static Map<String, ?> get(String url, Map<String, String> params) {
        return send(url, "get", params, null, null);
    }

    private static Map<String, ?> send(String url, String method, Map<String, ?> param, Map<String, ?> header, Map<String, HttpCookie> cookies) {
        try {
            URLConnection connection = new URL(url).openConnection();
            setHeaders(connection, header);
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.connect();
            System.out.println(connection.getContentType());
//            System.out.println(connection.getContentEncoding());
//            System.out.println(connection.getHeaderFields());
            InputStream is = connection.getInputStream();
//            char[] buffer = new char[1024];
//            StringBuilder out = new StringBuilder();
//            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
//            while (true) {
//                int rsz = isr.read(buffer, 0, buffer.length);
//                if (rsz < 0)
//                    break;
//                out.append(buffer, 0, rsz);

//            }
//            System.out.println(out.toString());
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1)
                result.write(buffer, 0, length);
            System.out.println(result.toString("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    private static void setHeaders(URLConnection connection, Map<String, ?> header) {
        connection.setRequestProperty("", "");
        connection.setRequestProperty("connection", "Keep-Alive");
    }
}
