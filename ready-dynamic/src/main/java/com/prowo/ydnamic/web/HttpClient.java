package com.prowo.ydnamic.web;

import com.prowo.ydnamic.context.ComxContext;
import com.prowo.ydnamic.exception.CustomException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClient {

    public static final String CONTENT_TYPE_JSON = "application/json;charset=utf-8";
    public static final String CONTENT_TYPE_XML = "application/xml;charset=utf-8";
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded;charset=utf-8";

    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_GET = "GET";

    /**
     * 标准的HTTP请求
     *
     * @param url         请求url
     * @param data        请求数据
     * @param method      请求方式
     * @param contentType 接受数据方式
     * @return 返回数据
     * @throws IOException
     */
    public static String post(String url, String data, String method, String contentType) throws Exception {
        StringBuilder builder = new StringBuilder();
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        int timeout = getTimeOut();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        try {
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(data);
            out.flush();
            out.close();
            int rcode = connection.getResponseCode();
            if (rcode != 200) {
                throw new CustomException("m11");
            }
        } catch (Exception e) {
            throw new CustomException("m10");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    /**
     * 带时间戳的HTTP请求
     *
     * @param url         请求url
     * @param data        请求数据
     * @param method      请求方式
     * @param contentType 接受数据方式
     * @return 返回数据
     * @throws IOException
     */
    public static String call(String url, String data, String method, String contentType) throws Exception {
        StringBuilder builder = new StringBuilder();
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        int timeout = getTimeOut();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        try {
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.write(data);
            out.flush();
            out.close();
            int rcode = connection.getResponseCode();
            if (rcode != 200) {
                throw new CustomException("m11");
            }
        } catch (Exception e) {
            throw new CustomException("m10");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }

    private static int getTimeOut() {
        int timeout = 3000;
        ComxContext context = ComxContext.getContext();
        if (context.get("system.time.timeout") != null) {

            timeout = Integer.parseInt(context.get("system.time.timeout"));
        }
        return timeout;
    }

    /**
     * 带时间戳的HTTP请求
     *
     * @param url         请求url
     * @param method      请求方式
     * @param contentType 接受数据方式
     * @param sout        返回流
     * @throws IOException
     */
    public static void getResult(String url, String content, String method, String contentType, OutputStream sout)
            throws Exception {
        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod(method);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        int timeout = getTimeOut();
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        connection.connect();

        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
        out.write(content);
        // 把数据写入
        out.flush();
        out.close();

        int rcode = connection.getResponseCode();
        if (rcode != 200) {
            throw new RuntimeException("HTTP状态响应码:" + rcode);
        }
        InputStream stream = connection.getInputStream();
        IOUtils.copy(stream, sout);
        IOUtils.closeQuietly(stream);
    }

    public static String call(String url, String data) throws Exception {
        return call(url, data, REQUEST_METHOD_POST, CONTENT_TYPE_FORM);
    }

    public static String call(String url, Map<String, String> data) throws Exception {
        return call(url, toURLStr(data));
    }

    public static void call(String url, String data, OutputStream sout) throws Exception {
        call(url, data, REQUEST_METHOD_POST, CONTENT_TYPE_FORM);
    }

    public static void call(String url, Map<String, String> data, OutputStream sout) throws Exception {
        call(url, toURLStr(data), sout);
    }

    public static String post(String url, String data) throws Exception {
        return post(url, data, REQUEST_METHOD_POST, CONTENT_TYPE_FORM);
    }

    public static String post(String url, Map<String, String> data) throws Exception {
        return post(url, toURLStr(data));
    }

    public static void post(String url, String data, OutputStream sout) throws Exception {
        post(url, data, REQUEST_METHOD_POST, CONTENT_TYPE_FORM);
    }

    public static void post(String url, Map<String, String> data, OutputStream sout) throws Exception {
        post(url, toURLStr(data), sout);
    }

    /**
     * 将Map数据集合转换为URL地址的字符串
     *
     * @param data 要转换的数据
     * @return 字符串
     * @throws UnsupportedEncodingException 转码异常，这里不做处理
     */
    public static String toURLStr(Map<String, String> data) throws Exception {
        if (data == null || data.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (Entry<String, String> varParam : data.entrySet()) {
                sb.append("&").append(varParam.getKey()).append("=")
                        .append(URLEncoder.encode(varParam.getValue(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("HTTP请求数据内容编码错误，请确保编码为UTF-8", e);
        }
        return sb.substring(1);
    }

    public static String getHtmlContent(String htmlURL) throws IOException, CustomException {
        URL url = null;
        String rowContent = "";
        StringBuffer htmlContent = new StringBuffer();
        InputStreamReader reader = null;
        BufferedReader in = null;
        try {
            url = new URL(htmlURL);
            reader = new InputStreamReader(url.openStream(), "UTF-8");
            in = new BufferedReader(reader);
            while ((rowContent = in.readLine()) != null) {
                htmlContent.append(rowContent);
            }

        } catch (Exception e) {
            throw new CustomException("m11");
        } finally {
            try {
                reader.close();
                in.close();
            } catch (Exception e) {

            }
        }

        return htmlContent.toString();
    }

}
