package org.clc.utils.http;

import org.apache.http.cookie.Cookie;
import org.clc.utils.StringUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 简单web请求
 *
 * --支持多线程调用
 * -----List<Callable<Object>> list = new ArrayList<>()
 * -----list.add(Web.run[Get|Post]))
 * -----ThreadPool.run(list)
 *
 * @Author clc
 */
public class Web implements Callable<Object> {

    private String url;
    private Proxy proxy;
    private String method;
    private Map<String, String> param;
    private Map<String, String> header;
    private List<HttpCookie> cookie;

    public static void main(String[] args) {
        String url = "https://www.baidu.com/img/baidu_jgylogo3.gif";
//        String url = "https://csdnimg.cn/public/common/toolbar/images/csdnqr@2x.png";
//        String url = "https://s7.addthis.com/l10n/client.zh.min.json";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",8888));
        Response response = get(url, null, null, null, null);
        System.out.println(response);
    }

    public static Web runGet(String url, Map<String, String> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        return run(url, "GET", param, header, cookie, proxy);
    }

    public static Web runPost(String url, Map<String, String> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        return run(url, "POST", param, header, cookie, proxy);
    }

    private static Web run(String url, String mothod, Map<String, String> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        Web web = new Web();
        web.method = mothod;
        web.url = url;
        web.param = param;
        web.proxy = proxy;
        web.header = header;
        web.cookie = cookie;
        return web;
    }

    @Override
    public Object call() {
        StringBuilder sr = new StringBuilder(LocalDateTime.now().toString());
        sr.append(" : ").append(Thread.currentThread().getName()).append(" --- ").append(this.toString());
        Response response = Web.send(this.url, this.method, this.param, this.header, this.cookie, this.proxy);
        sr.append("\n ==》Result: \n\t").append(response);
        return sr.toString();
    }

    public static Response get(String url) {
        return get(url, null, null, null, null);
    }

    public static Response get(String url, Map<String, String> param) {
        return get(url, param, null, null, null);
    }

    public static Response get(String url, Map<String, String> param, Map<String, String> header) {
        return get(url, param, header, null, null);
    }

    public static Response get(String url, Map<String, String> param, Map<String, String> header, Proxy proxy) {
        return get(url, param, header, null, proxy);
    }

    public static Response get(String url, Map<String, String> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        return send(url, "GET", param, header, cookie, proxy);
    }

    public static Response post(String url, Map<String, ?> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        return send(url, "POST", param, header, cookie, proxy);
    }

    /**
     * 请求方法
     */
    private static Response send(String url, String method,
                                 Map<String, ?> param,
                                 Map<String, String> header,
                                 List<HttpCookie> cookies,
                                 Proxy proxy) {
        HttpURLConnection connection = null;
        try {
            // 代理
            if (proxy != null)
                connection = (HttpURLConnection) new URL(url).openConnection(proxy);
            else
                connection = (HttpURLConnection) new URL(url).openConnection();
            // 设置请求头
            setHeader(connection, header);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);
            connection.setRequestMethod(method);
            if (method.equals("POST")) {// Post cannot use caches
                connection.setUseCaches(false);
                connection.setDoOutput(true);
            }
            connection.connect();
            if (method.equals("POST")) {
                setRequestBody(param);
                OutputStream outputStream = connection.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);// 获取URLConnection对象对应的输出流
                printWriter.print(param);// 发送请求参数
                printWriter.flush();// flush输出流的缓冲
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(connection);
    }

    // region post请求设置请求体
    private static void setRequestBody(Map<String, ?> param) {

    }
    // endregion

    //region 设置请求头
    private static void setHeader(URLConnection connection, Map<String, ?> header) {
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.61 Safari/537.36");
    }
    //endregion

    // 响应类
    public static class Response {

        private void _default() {
            this.statusCode = -1;
            this.msg = "error";
            this.content = "";
            this.contentLength = 0;
            this.contentType = "";
        }

        Response(HttpURLConnection connection) {
            if (connection != null) {
                try {
                    this.statusCode = connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                    this.statusCode = -1;
                    this.msg = "error";
                }
                this.msg = getResponseMsg();
                this.header = connection.getHeaderFields();
                // connection.getContentLength() == -1  （服务端没设置content-length头）
                this.contentLength = connection.getContentLength();
                this.contentType = connection.getContentType();
                try {
                    this.content = responseContent(connection.getInputStream());
                } catch (IOException e) {
                    this.content = "";
                    e.printStackTrace();
                }
            } else
                _default();
        }

        //region getResponseMsg(): 根据响应码获取响应消息
        private String getResponseMsg() {
            switch (this.statusCode) {
                //-----------------------1xx(临时响应)-----------------------
                case 100:
                    return "continue(请求者应当继续提出请求,服务器返回此代码表示已收到请求的第一部分,正在等待其余部分)";
                case 101:
                    return "switchProtocol(请求者已要求服务器切换协议,服务器已确认并准备切换)";
                //-----------------------2xx(成功)-----------------------
                case 200:
                    return "success(服务器已成功处理了请求)";
                case 201:
                    return "created(请求成功并且服务器创建了新的资源)";
                case 202:
                    return "已接受(服务器已接受请求,但尚未处理)";
                case 203:
                    return "非授权信息(服务器已成功处理了请求,但返回的信息可能来自另一来源)";
                case 204:
                    return "无内容(服务器成功处理了请求,但没有返回任何内容)";
                case 205:
                    return "重置内容(服务器成功处理了请求,但没有返回任何内容)";
                case 206:
                    return "部分内容(服务器成功处理了部分 GET 请求)";
                //-----------------------3xx(重定向)-----------------------
                case 300:
                    return "多种选择(针对请求,服务器可执行多种操作,服务器可根据请求者 (user agent) 选择一项操作,或提供操作列表供请求者选择)";
                case 301:
                    return "永久移动(请求的网页已永久移动到新位置,服务器返回此响应(对 GET 或 HEAD 请求的响应)时，会自动将请求者转到新位置)";
                case 302:
                    return "临时移动:服务器目前从不同位置的网页响应请求,但请求者应继续使用原有位置来进行以后的请求";
                case 303:
                    return "查看其他位置(请求者应当对不同的位置使用单独的 GET 请求来检索响应时,服务器返回此代码)";
                case 304:
                    return "未修改(自从上次请求后,请求的网页未修改过,服务器返回此响应时,不会返回网页内容)";
                case 305:
                    return "must use proxy(请求者只能使用代理访问请求)";
                case 307:
                    return "临时重定向(服务器目前从不同位置的网页响应请求,但请求者应继续使用原有位置来进行以后的请求)";
                //-----------------------4xx(请求错误)-----------------------
                case 400:
                    return "错误请求(服务器不理解请求的语法)";
                case 401:
                    return "未授权(请求要求身份验证.对于需要登录的网页,服务器可能返回此响应)";
                case 403:
                    return "禁止(服务器拒绝请求)";
                case 404:
                    return "未找到(服务器找不到请求的内容)";
                case 405:
                    return "方法禁用(禁用请求中指定的方法)";
                case 406:
                    return "不接受(无法使用请求的内容特性响应请求的网页)";
                case 407:
                    return "需要代理授权(此状态代码与 401 (未授权)类似,但指定请求者应当授权使用代理)";
                case 408:
                    return "request time out(服务器等候请求时发生超时)";
                case 409:
                    return "冲突(服务器在完成请求时发生冲突,服务器必须在响应中包含有关冲突的信息)";
                case 410:
                    return "已删除(如果请求的资源已永久删除,服务器就会返回此响应)";
                case 411:
                    return "需要有效长度(服务器不接受不含有效内容长度标头字段的请求)";
                case 412:
                    return "未满足前提条件(服务器未满足请求者在请求中设置的其中一个前提条件)";
                case 413:
                    return "请求实体过大(服务器无法处理请求,因为请求实体过大,超出服务器的处理能力)";
                case 414:
                    return "URL is too long(请求的 URI通常为网址过长,服务器无法处理)";
                case 415:
                    return "不支持的媒体类型(请求的格式不受请求页面的支持)";
                case 416:
                    return "请求范围不符合要求(如果页面无法提供请求的范围,则服务器会返回此状态代码)";
                case 417:
                    return "未满足期望值(服务器未满足'期望'请求标头字段的要求)";
                //-----------------------5xx(服务器错误)-----------------------
                case 500:
                    return "服务器内部错误(服务器遇到错误,无法完成请求)";
                case 501:
                    return "尚未实施(服务器不具备完成请求的功能.例如,服务器无法识别请求方法时可能会返回此代码)";
                case 502:
                    return "错误网关(服务器作为网关或代理,从上游服务器收到无效响应)";
                case 503:
                    return "服务不可用(服务器目前无法使用(由于超载或停机维护).通常,这只是暂时状态)";
                case 504:
                    return "网关超时(服务器作为网关或代理,但是没有及时从上游服务器收到请求)";
                case 505:
                    return "版本不受支持(服务器不支持请求中所用的 HTTP 协议版本)";
                default:
                    return "未知状态码: " + this.statusCode;
            }
        }
        //endregion

        //region responseContent(): 添加响应内容
        private Object responseContent(InputStream inputStream) throws IOException {
            if (this.contentType != null) {
                if (this.contentType.contains("image"))
                    return StringUtil.getIMGStr(inputStream);
                else if (this.contentType.contains("text") || this.contentType.contains("json") || this.contentType.contains("javascript"))
                    return getText(inputStream);
                else return "";
            } else
                return "";
        }
        //endregion

        //region 文本内容解析
        private static String getText(InputStream inputStream) throws IOException {
            char[] buffer = new char[inputStream.available()];
            StringBuilder out = new StringBuilder();
            InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            while (true) {
                int rsz = isr.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
//         ByteArrayOutputStream result = new ByteArrayOutputStream();
//         byte[] buffer = new byte[1024];
//         int length;
//         while ((length = is.read(buffer)) != -1)
//         result.write(buffer, 0, length);
//         System.out.println(result.toString("UTF-8"));
            return out.toString();
        }
        //endregion

        //region property
        private int statusCode;
        private String msg;
        private Object content;
        private List<Cookie> cookies;
        private String contentType;
        private int contentLength;
        private Map<String, List<String>> header;
        //endregion

        //region getter/setter
        public int getContentLength() {
            return contentLength;
        }
        public void setContentLength(int contentLength) {
            this.contentLength = contentLength;
        }
        public Map<String, List<String>> getHeader() {
            return header;
        }
        public void setHeader(Map<String, List<String>> header) {
            this.header = header;
        }
        public int getStatusCode() {
            return statusCode;
        }
        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public Object getContent() {
            return content;
        }
        public void setContent(Object content) {
            this.content = content;
        }
        public List<Cookie> getCookies() {
            return cookies;
        }
        public void setCookies(List<Cookie> cookies) {
            this.cookies = cookies;
        }
        public String getContentType() {
            return contentType;
        }
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }
        //endregion

        @Override
        public String toString() {
            return "response info ==>" +
                    "\n\tstatusCode: " + this.statusCode +
                    "\n\tmsg: " + this.msg +
                    "\n\theader: " + this.header +
                    "\n\tcookies: " + this.cookies +
                    "\n\tcontentType: " + this.contentType +
                    "\n\tcontentLength: " + this.contentLength +
                    "\n\tcontent: " + this.content;
        }
    }
}
