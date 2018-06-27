package org.clc.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.HashMap;
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
public class Web implements Callable {

    private String url;
    private Proxy proxy;
    private String method;
    private Map<String, String> param;
    private Map<String, String> header;
    private List<HttpCookie> cookie;

    public static void main(String[] args) {
//        String url = "http://www.androiddevtools.cn/";
//        String url = "https://10minutemail.net/cdn/js/ads.js";
//        String url = "https://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css";
//        String url = "https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=71b1394362061d95624631384bf50a5d/5ab5c9ea15ce36d30d89c34b36f33a87e950b138.jpg";
//        String url = "http://m10.music.126.net/20180415171127/5f0f794030364a229791516b1283e526/ymusic/9c9e/8bb8/3f20/3bf2006159a00871e2f30d099ac05702.mp3";
//        String url = "https://www.xiaozhizy.com";
//        String url = "http://v3.jiathis.com/code/images/counter.gif";
//		String url = "https://spring.io";
        String url = "http://sfz.ckd.cc/idcard.php";
//        String url = "http://localhost/user";
//        String url = "https://s7.addthis.com/l10n/client.zh.min.json";
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",8888));
        Map<String, Object> response = get(url, null, null, null, null);
        response.forEach((k, v) ->
                System.out.println(k/* + " ------------ " + v*/)
        );
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
        Map<String, Object> response = Web.send(this.url, this.method, this.param, this.header, this.cookie, this.proxy);
        sr.append("\n ==》Result: \n\t").append(response);
//        System.out.println(sr);
        return response;
    }

    public static Map<String, Object> get(String url) {
        return get(url, null, null, null, null);
    }

    public static Map<String, Object> get(String url, Map<String, String> param) {
        return get(url, param, null, null, null);
    }

    public static Map<String, Object> get(String url, Map<String, String> param, Map<String, String> header) {
        return get(url, param, header, null, null);
    }

    public static Map<String, Object> get(String url, Map<String, String> param, Map<String, String> header, Proxy proxy) {
        return get(url, param, header, null, proxy);
    }

    public static Map<String, Object> get(String url, Map<String, String> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        return send(url, "GET", param, header, cookie, proxy);
    }

    public static Map<String, Object> post(String url, Map<String, ?> param, Map<String, String> header, List<HttpCookie> cookie, Proxy proxy) {
        return send(url, "POST", param, header, cookie, proxy);
    }

    /**
     * 请求方法
     *
     * @param url
     * @param method
     * @param param
     * @param header
     * @param cookies
     * @param proxy
     * @return ResponseInfo ---> （code、msg、header、contentLength、contentType）
     */
    private static Map<String, Object> send(String url, String method, Map<String, ?> param, Map<String, String> header, List<HttpCookie> cookies, Proxy proxy) {
        Map<String, Object> response = new HashMap<>();
        if (url == null) {
            response.put("code", -1);
            response.put("msg", "url为空...");
            return response;
        }
        try {
            HttpURLConnection connection;
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
            int responseCode = connection.getResponseCode();
            response.putIfAbsent("code", responseCode);
            setResponseMsg(response, responseCode);
            // 当请求格式不对、服务器报错,直接返回
            if (400 <= responseCode)
                return response;
            response.putIfAbsent("header", connection.getHeaderFields());
            // connection.getContentLength() == -1  （服务端没设置content-length头）
            response.putIfAbsent("contentLength", connection.getContentLength());
            responseContent(response, connection.getContentType(), connection.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    // region post请求设置请求体
    private static void setRequestBody(Map<String, ?> param) {
    }
    // endregion

    //region 添加响应内容
    private static void responseContent(Map<String, Object> response, String contentType, InputStream inputStream) throws IOException {
        response.putIfAbsent("contentType", contentType);
        Object content = null;
        if (contentType != null) {
            if (contentType.contains("image"))
                content = getIMGStr(inputStream);
            else if (contentType.contains("text") || contentType.contains("json") || contentType.contains("javascript"))
                content = getContentWithHtml(inputStream);

        } else
            content = "无信息";
        response.putIfAbsent("content", content);
    }
    //endregion

    //region 文本内容解析
    private static String getContentWithHtml(InputStream inputStream) throws IOException {
        char[] buffer = new char[inputStream.available()];
        StringBuilder out = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
        while (true) {
            int rsz = isr.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        // ByteArrayOutputStream result = new ByteArrayOutputStream();
        // byte[] buffer = new byte[1024];
        // int length;
        // while ((length = is.read(buffer)) != -1)
        // result.write(buffer, 0, length);
        // System.out.println(result.toString("UTF-8"));
        return out.toString();
    }
    //endregion

    //region 图片解析(Base64字符串)
    private static String getIMGStr(InputStream input) throws IOException {
        byte[] data = new byte[input.available()];
        input.read(data);
        return new BASE64Encoder().encode(data);
    }
    //endregion

    //region base64字符串转化成图片
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
    //endregion

    //region 设置请求头
    private static void setHeader(URLConnection connection, Map<String, ?> header) {
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.61 Safari/537.36");
    }
    //endregion


    //region 设置响应消息
    private static void setResponseMsg(Map<String, Object> response, int responseCode) {
        String msg = "";
        switch (responseCode) {
            //-----------------------1xx(临时响应)-----------------------
            case 100:
                msg = "continue:请求者应当继续提出请求,服务器返回此代码表示已收到请求的第一部分,正在等待其余部分";
                break;
            case 101:
                msg = "switchProtocol:请求者已要求服务器切换协议,服务器已确认并准备切换";
                break;
            //-----------------------2xx(成功)-----------------------
            case 200:
                msg = "success:服务器已成功处理了请求";
                break;
            case 201:
                msg = "created:请求成功并且服务器创建了新的资源";
                break;
            case 202:
                msg = "已接受:服务器已接受请求,但尚未处理";
                break;
            case 203:
                msg = "非授权信息:服务器已成功处理了请求,但返回的信息可能来自另一来源";
                break;
            case 204:
                msg = "无内容:服务器成功处理了请求,但没有返回任何内容";
                break;
            case 205:
                msg = "重置内容:服务器成功处理了请求,但没有返回任何内容";
                break;
            case 206:
                msg = "部分内容:服务器成功处理了部分 GET 请求";
                break;
            //-----------------------3xx(重定向)-----------------------
            case 300:
                msg = "多种选择:针对请求,服务器可执行多种操作,服务器可根据请求者 (user agent) 选择一项操作,或提供操作列表供请求者选择";
                break;
            case 301:
                msg = "永久移动:请求的网页已永久移动到新位置,服务器返回此响应(对 GET 或 HEAD 请求的响应)时，会自动将请求者转到新位置";
                break;
            case 302:
                msg = "临时移动:服务器目前从不同位置的网页响应请求,但请求者应继续使用原有位置来进行以后的请求";
                break;
            case 303:
                msg = "查看其他位置:请求者应当对不同的位置使用单独的 GET 请求来检索响应时,服务器返回此代码";
                break;
            case 304:
                msg = "未修改:自从上次请求后,请求的网页未修改过,服务器返回此响应时,不会返回网页内容";
                break;
            case 305:
                msg = "must use proxy:请求者只能使用代理访问请求";
                break;
            case 307:
                msg = "临时重定向:服务器目前从不同位置的网页响应请求,但请求者应继续使用原有位置来进行以后的请求";
                break;
            //-----------------------4xx(请求错误)-----------------------
            case 400:
                msg = "错误请求:服务器不理解请求的语法";
                break;
            case 401:
                msg = "未授权:请求要求身份验证。 对于需要登录的网页，服务器可能返回此响应";
                break;
            case 403:
                msg = "禁止:服务器拒绝请求";
                break;
            case 404:
                msg = "未找到:服务器找不到请求的内容";
                break;
            case 405:
                msg = "方法禁用:禁用请求中指定的方法";
                break;
            case 406:
                msg = "不接受:无法使用请求的内容特性响应请求的网页";
                break;
            case 407:
                msg = "需要代理授权:此状态代码与 401 (未授权)类似,但指定请求者应当授权使用代理";
                break;
            case 408:
                msg = "request time out:服务器等候请求时发生超时";
                break;
            case 409:
                msg = "冲突:服务器在完成请求时发生冲突,服务器必须在响应中包含有关冲突的信息";
                break;
            case 410:
                msg = "已删除:如果请求的资源已永久删除,服务器就会返回此响应";
                break;
            case 411:
                msg = "需要有效长度:服务器不接受不含有效内容长度标头字段的请求";
                break;
            case 412:
                msg = "未满足前提条件:服务器未满足请求者在请求中设置的其中一个前提条件";
                break;
            case 413:
                msg = "请求实体过大:服务器无法处理请求,因为请求实体过大,超出服务器的处理能力";
                break;
            case 414:
                msg = "URL is too long:请求的 URI(通常为网址(过长,服务器无法处理";
                break;
            case 415:
                msg = "不支持的媒体类型:请求的格式不受请求页面的支持";
                break;
            case 416:
                msg = "请求范围不符合要求:如果页面无法提供请求的范围,则服务器会返回此状态代码";
                break;
            case 417:
                msg = "未满足期望值:服务器未满足'期望'请求标头字段的要求";
                break;
            //-----------------------5xx(服务器错误)-----------------------
            case 500:
                msg = "服务器内部错误:服务器遇到错误,无法完成请求";
                break;
            case 501:
                msg = "尚未实施:服务器不具备完成请求的功能.例如,服务器无法识别请求方法时可能会返回此代码";
                break;
            case 502:
                msg = "错误网关:服务器作为网关或代理,从上游服务器收到无效响应";
                break;
            case 503:
                msg = "服务不可用:服务器目前无法使用(由于超载或停机维护).通常,这只是暂时状态";
                break;
            case 504:
                msg = "网关超时:服务器作为网关或代理,但是没有及时从上游服务器收到请求";
                break;
            case 505:
                msg = "HTTP 版本不受支持:服务器不支持请求中所用的 HTTP 协议版本";
                break;
        }
        response.putIfAbsent("msg", msg);
    }
    //endregion

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (this.url != null)
            sb.append(" url: ").append(this.url);
        if (this.param != null)
            sb.append(" param: ").append(this.param);
        if (this.method != null)
            sb.append(" method: ").append(this.method);
        if (this.header != null)
            sb.append(" header: ").append(this.header);
        if (this.cookie != null)
            sb.append(" cookie: ").append(this.cookie);
        if (this.proxy != null)
            sb.append(" proxy: ").append(this.proxy);
        sb.append(" ]");
        return sb.toString();
    }
}
