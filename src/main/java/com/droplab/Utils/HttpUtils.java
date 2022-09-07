package com.droplab.Utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * Http发送post请求工具，兼容http和https两种请求类型
 */
public class HttpUtils {

    /**
     * 请求超时时间
     */
    private static int TIME_OUT = 120000;
    public static void set_TIME_OUT(int time_out){
        TIME_OUT=time_out;
    }

    /**
     * Https请求
     */
    private static final String HTTPS = "https";

    /**
     * Content-Type
     */
    private static final String CONTENT_TYPE = "Content-Type";

    /**
     * 表单提交方式Content-Type
     */
    private static final String FORM_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * JSON提交方式Content-Type
     */
    private static final String JSON_TYPE = "application/json;charset=UTF-8";

    /**
     * 自定义Content-Type
     */
    private static String Content_Type="";
    public static void set_Content_Type(String content_Type){
        Content_Type=content_Type;
    }

    /**
     * 发送Get请求
     *
     * @param url 请求URL
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * 发送Get请求
     *
     * @param url 请求URL
     * @param headers 请求头参数
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response get(String url, Map<String, String> headers) throws IOException {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        // 如果是Https请求
        if (url.startsWith(HTTPS)) {
            getTrust();
        }
        Proxy proxy = getProxy();
        Connection connection=null;
        if(proxy !=null){
            if(!InfoUtils.ProxyUsername.equals("") && !InfoUtils.ProxyPassword.equals("")){
                Authenticator.setDefault(new MyAuthenticator(InfoUtils.ProxyUsername,InfoUtils.ProxyPassword));
            }
            connection = Jsoup.connect(url).proxy(proxy);
        }else {
            connection = Jsoup.connect(url);
        }
        connection.method(Method.GET);
        connection.timeout(TIME_OUT);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);
        connection.maxBodySize(0);

        if (null != headers) {
            connection.headers(headers);
        }

        Response response = connection.execute();
        return response;
    }

    /**
     * 发送JSON格式参数POST请求
     *
     * @param url 请求路径
     * @param params JSON格式请求参数
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response post(String url, String params) throws IOException {
        return doPostRequest(url, null, params);
    }

    /**
     * 发送JSON格式参数POST请求
     *
     * @param url 请求路径
     * @param headers 请求头中设置的参数
     * @param params JSON格式请求参数
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response post(String url, Map<String, String> headers, String params) throws IOException {
        return doPostRequest(url, headers, params);
    }

    /**
     * 字符串参数post请求
     *
     * @param url 请求URL地址
     * @param paramMap 请求字符串参数集合
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response post(String url, Map<String, String> paramMap) throws IOException {
        return doPostRequest(url, null, paramMap, null);
    }

    /**
     * 带请求头的普通表单提交方式post请求
     *
     * @param headers 请求头参数
     * @param url 请求URL地址
     * @param paramMap 请求字符串参数集合
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response post(Map<String, String> headers, String url, Map<String, String> paramMap) throws IOException {
        return doPostRequest(url, headers, paramMap, null);
    }

    /**
     * 带上传文件的post请求
     *
     * @param url 请求URL地址
     * @param paramMap 请求字符串参数集合
     * @param fileMap 请求文件参数集合
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response post(String url, Map<String, String> paramMap, Map<String, File> fileMap) throws IOException {
        return doPostRequest(url, null, paramMap, fileMap);
    }

    /**
     * 带请求头的上传文件post请求
     *
     * @param url 请求URL地址
     * @param headers 请求头参数
     * @param paramMap 请求字符串参数集合
     * @param fileMap 请求文件参数集合
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    public static Response post(String url, Map<String, String> headers, Map<String, String> paramMap, Map<String, File> fileMap) throws IOException {
        return doPostRequest(url, headers, paramMap, fileMap);
    }

    /**
     * 执行post请求
     *
     * @param url 请求URL地址
     * @param headers 请求头
     * @param jsonParams 请求JSON格式字符串参数
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    private static Response doPostRequest(String url, Map<String, String> headers, String jsonParams) throws IOException {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        // 如果是Https请求
        if (url.startsWith(HTTPS)) {
            getTrust();
        }
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
        Proxy proxy = getProxy();
        Connection connection=null;
        if(proxy !=null){
            if(!InfoUtils.ProxyUsername.equals("") && !InfoUtils.ProxyPassword.equals("")){
                Authenticator.setDefault(new MyAuthenticator(InfoUtils.ProxyUsername,InfoUtils.ProxyPassword));
            }
            connection = Jsoup.connect(url).proxy(proxy);
        }else {
            connection = Jsoup.connect(url);
        }
        connection.method(Method.POST);
        connection.timeout(TIME_OUT);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);
        connection.maxBodySize(0);

        if (null != headers) {
            connection.headers(headers);
        }

        if(Content_Type != null && !"".equals(Content_Type)){
            connection.header(CONTENT_TYPE, Content_Type);
        }else {
            connection.header(CONTENT_TYPE, JSON_TYPE);
        }
        connection.requestBody(jsonParams);

        Response response = connection.execute();
        return response;
    }

    /**
     * 普通表单方式发送POST请求
     *
     * @param url 请求URL地址
     * @param headers 请求头
     * @param paramMap 请求字符串参数集合
     * @param fileMap 请求文件参数集合
     * @return HTTP响应对象
     * @throws IOException 程序异常时抛出，由调用者处理
     */
    private static Response doPostRequest(String url, Map<String, String> headers, Map<String, String> paramMap, Map<String, File> fileMap) throws IOException {
        if (null == url || url.isEmpty()) {
            throw new RuntimeException("The request URL is blank.");
        }

        // 如果是Https请求
        if (url.startsWith(HTTPS)) {
            getTrust();
        }
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
        Proxy proxy = getProxy();
        Connection connection=null;
        if(proxy !=null){
            if(!InfoUtils.ProxyUsername.equals("") && !InfoUtils.ProxyPassword.equals("")){
                Authenticator.setDefault(new MyAuthenticator(InfoUtils.ProxyUsername,InfoUtils.ProxyPassword));
            }
            connection = Jsoup.connect(url).proxy(proxy);
        }else {
            connection = Jsoup.connect(url);
        }
        connection.method(Method.POST);
        connection.timeout(TIME_OUT);
        connection.ignoreHttpErrors(true);
        connection.ignoreContentType(true);
        connection.maxBodySize(0);

        if (null != headers) {
            connection.headers(headers);
        }

        // 收集上传文件输入流，最终全部关闭
        List<InputStream> inputStreamList = null;
        try {

            // 添加文件参数
            if (null != fileMap && !fileMap.isEmpty()) {
                inputStreamList = new ArrayList<InputStream>();
                InputStream in = null;
                File file = null;
                for (Entry<String, File> e : fileMap.entrySet()) {
                    file = e.getValue();
                    in = new FileInputStream(file);
                    inputStreamList.add(in);
                    connection.data(e.getKey(), file.getName(), in);
                }
            }

            // 普通表单提交方式
            else {
                if(Content_Type != null && !"".equals(Content_Type)){
                    connection.header(CONTENT_TYPE, Content_Type);
                }else {
                    connection.header(CONTENT_TYPE, FORM_TYPE);
                }
            }

            // 添加字符串类参数
            if (null != paramMap && !paramMap.isEmpty()) {
                connection.data(paramMap);
            }

            Response response = connection.execute();
            return response;
        }

        // 关闭上传文件的输入流
        finally {
            closeStream(inputStreamList);
        }
    }

    /**
     * 关流
     *
     * @param streamList 流集合
     */
    private static void closeStream(List<? extends Closeable> streamList) {
        if (null != streamList) {
            for (Closeable stream : streamList) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static Proxy getProxy(){
        if (InfoUtils.openProxy){
            InetSocketAddress inetSocketAddress = new InetSocketAddress(InfoUtils.ProxyHost, InfoUtils.ProxyPort);
            Proxy.Type type;
            switch (InfoUtils.ProxyType){
                case "HTTP":
                    type=Proxy.Type.HTTP;
                    break;
                case "SOCKS":
                    type=Proxy.Type.SOCKS;
                    break;
                default:
                    type=Proxy.Type.DIRECT;
                    break;
            }
            return new Proxy(type, inetSocketAddress);
        }else{
            return null;
        }
    }


    static class MyAuthenticator extends Authenticator {
        private String user = "";
        private String password = "";

        public MyAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password.toCharArray());
        }
    }
    /**
     * 获取服务器信任
     */
    private static void getTrust() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

