package com.prowo.ydnamic.web;

import com.prowo.ydnamic.logger.LoggerUtil;
import com.prowo.ydnamic.logger.LoggerUtil.Level;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsClient {

    public static X509TrustManager default_trustManager = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] ax509certificate, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }

    };

    public static String get(String requestUrl) throws Exception {
        return call(requestUrl, null, HttpClient.REQUEST_METHOD_GET);
    }

    public static String post(String requestUrl, String data) throws Exception {
        return call(requestUrl, data, HttpClient.REQUEST_METHOD_POST);
    }

    public static String call(String requestUrl, String outputStr, String requestMethod) throws Exception {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {default_trustManager};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(null, requestUrl, new com.sun.net.ssl.internal.www.protocol.https.Handler());
            HttpURLConnection httpsUrlConn = (HttpURLConnection) url.openConnection();
            // httpsUrlConn.setSSLSocketFactory(ssf);
            Method method = httpsUrlConn.getClass().getMethod("setSSLSocketFactory", SSLSocketFactory.class);
            method.invoke(httpsUrlConn, ssf);

            httpsUrlConn.setDoOutput(true);
            httpsUrlConn.setDoInput(true);
            httpsUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpsUrlConn.setRequestMethod(requestMethod);

//            if ("GET".equalsIgnoreCase(requestMethod))
            httpsUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpsUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpsUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpsUrlConn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            LoggerUtil.log(Level.WRAN, "https server connection timed out.");
        } catch (Exception e) {
            LoggerUtil.log(Level.WRAN, "https request error:{0}", e.getMessage());
        }
        return buffer.toString();
    }
}
