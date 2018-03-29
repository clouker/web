package org.clc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebUtils {
    // 系统换行符：System.getProperty("line.separator")
    public static void main(String... args) {
        String url = "http://my.zp365.com/login/login.aspx";
        Map<String, String> params = new HashMap<>();
        params.put("ctl00$Con$PassPort", "15296284109");
        params.put("ctl00$Con$u_Pass", "zzm520888");
        params.put("ctl00$Con$nxt", "");
        Response response = WebUtils.post(url, params);
        ObjectMapper mapper = new ObjectMapper();
        String contentType = response.getContentType();
        if (contentType.equals("json")) {
            String content = response.getContent();
            content = content.substring(content.indexOf("(") + 1);
            try {
                System.out.println(mapper.readValue(content, Map.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(response.getContent());
        }
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static Response get(String url) {
        return get(url, null, null);
    }

    /**
     * get请求
     *
     * @param url
     * @return
     */
    public static Response get(String url, Map<String, String> params, List<Cookie> cookies) {
        return send(url, "get", cookies, params, null);
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @return
     */
    public static Response post(String url, Map<String, String> params) {
        return post(url, params, null, null);
    }

    /**
     * post请求
     *
     * @param url
     * @param params  请求数据
     * @param headers 自定义请求头
     * @return
     */
    public static Response post(String url, Map<String, String> params, Map<String, String> headers, List<Cookie> cookies) {
        return send(url, "post", cookies, params, headers);
    }

    /**
     * 公共请求
     *
     * @param url
     * @param method
     * @return
     */
    private static Response send(String url, String method, List<Cookie> cookies, Map<String, String> params, Map<String, String> headers) {
        Response rs = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        CookieStore cookieStore = new BasicCookieStore();
        if (cookies != null)
            cookies.forEach(cookie -> cookieStore.addCookie(cookie));
        try {
            httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            response = httpClient.execute(getHttpUriRequest(url, params, headers, method));
            StatusLine statusLine = response.getStatusLine();
            String contentType = "";
            StringBuilder stringBuilder = new StringBuilder();
            if (statusLine.getReasonPhrase().equalsIgnoreCase("OK")) {
                if (statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    // ------------------返回类型------------------ //
                    String type = entity.getContentType().getValue();
                    if (type != null && type.contains("/"))
                        contentType = type.substring(type.indexOf("/") + 1);
                    BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String line;
                    while ((line = br.readLine()) != null)
                        stringBuilder.append(line.replaceAll(">\\s*", ">" + System.getProperty("line.separator")));
                }
            }
/*            cookieStore.getCookies().forEach(item -> {
                System.out.println("Comment: " + item.getComment() + "\tDomain: " + item.getDomain()
                        + "\tCommentURL: " + item.getCommentURL() + "\tName: " + item.getName()
                        + "\tValue: " + item.getValue() + "\tPath(): " + item.getPath()
                        + "\tPorts: " + item.getPorts() + "\tVersion: " + item.getVersion()
                        + "\tExpiryDate(): " + item.getExpiryDate());
            });*/
            rs = new Response(statusLine.getStatusCode(), statusLine.getReasonPhrase(), stringBuilder.toString(), contentType, cookieStore.getCookies());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.close();
                if (httpClient != null)
                    httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    /**
     * 获得连接方法
     *
     * @param url     主机
     * @param headers 请求头
     * @param params  参数
     * @param method  请求类型
     * @return
     */
    private static HttpUriRequest getHttpUriRequest(String url, Map<String, String> params, Map<String, String> headers, String method) {
        HttpRequestBase httpRequest = null;
        try {
            if (method.equalsIgnoreCase("post")) {
                httpRequest = new HttpPost(url);
                // -----------------------设置请求数据:start--------------------------
                List<NameValuePair> pairs = new ArrayList<>();
                params = params != null ? params : new HashMap<>(0);
                params.forEach((k, v) -> {
                    System.out.println(k + " --------- " + v);
                    pairs.add(new BasicNameValuePair(k, v));
                });
                if (pairs.size() > 0)
                    ((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(pairs, "utf-8"));
                // -----------------------设置请求数据:end--------------------------
            } else {
                httpRequest = new HttpGet(url);
            }
            setHeader(httpRequest, headers);//设置请求头
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpRequest;
    }

    /**
     * 设置请求头
     *
     * @param request
     * @param headers
     */
    private static void setHeader(HttpUriRequest request, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach((k, v) -> {
                request.setHeader(k, v);
            });
        }
        request.setHeader("Connection", "keep-alive");
//        request.setHeader("Accept-Encoding","gzip, deflate");
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
    }
}
