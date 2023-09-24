package com.euler.payment.utils;

import com.euler.payment.config.ApplePayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 苹果IAP内购验证工具类
 */
@Component
public class AppleVerifyUtil {

    @Autowired
    private ApplePayConfig applePayConfig;

    private final String url_sandbox = "https://sandbox.itunes.apple.com/verifyReceipt";
    private final String url_verify = "https://buy.itunes.apple.com/verifyReceipt";

    private class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * 苹果服务器验证
     *
     * @param receipt 账单
     * @param type 0：沙盒，1：线上
     * @return null 或返回结果 沙盒 https://sandbox.itunes.apple.com/verifyReceipt
     */
    public String buyAppVerify(String receipt, int type) {
        // 环境判断 线上/开发环境用不同的请求链接
        String url;
        if(type == 0){
            url = url_sandbox; //沙盒测试
        } else {
            url = url_verify; //线上测试
        }

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new AppleVerifyUtil.TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new AppleVerifyUtil.TrustAnyHostnameVerifier());
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Proxy-Connection", "Keep-Alive");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            BufferedOutputStream hurlBufOus = new BufferedOutputStream(conn.getOutputStream());
            // 拼成固定的格式传给平台
//            String requestBody = String.format(Locale.CHINA, "{\"receipt-data\":\"" + receipt + "\"}");
            String requestBody = String.format(Locale.CHINA, "{\"receipt-data\": \"%s\", \"password\": \"%s\"}", receipt, this.applePayConfig.getPassword());

            hurlBufOus.write(requestBody.getBytes());
            hurlBufOus.flush();

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception ex) {
            System.out.println("苹果服务器异常");
            ex.printStackTrace();
        }
        return null;
    }

}
