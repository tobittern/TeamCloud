package com.euler.common.payment.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.euler.common.core.constant.Constants;
import com.euler.common.core.domain.dto.RequestHeaderDto;
import com.euler.common.core.utils.HttpClientUtil;
import com.euler.common.core.utils.HttpRequestHeaderUtils;
import com.euler.common.core.utils.StringUtils;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: WxPayHtmlUtils
 * @Description: 请求头工具类
 */
@Slf4j
public class WxPayHtmlUtils {

    /**
     * 获取请求头信息
     *
     * @return
     */
    public static String fetchRequest2Str(HttpServletRequest request) {
        String reqStr = null;
        BufferedReader streamReader = null;
        try {
            streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();
            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }
            reqStr = responseStrBuilder.toString();
            log.info("Request Received is  \n" + reqStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (streamReader != null) {
                    streamReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reqStr;
    }


    /**
     * 获取请求头签名
     *
     * @return
     */
    public static SignatureHeader fetchRequest2SignatureHeader(HttpServletRequest request) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(request.getHeader("Wechatpay-Signature"));
        signatureHeader.setNonce(request.getHeader("Wechatpay-Nonce"));
        signatureHeader.setSerial(request.getHeader("Wechatpay-Serial"));
        signatureHeader.setTimeStamp(request.getHeader("Wechatpay-TimeStamp"));
        return signatureHeader;
    }

    public static String readUrl(String url) {
        try {
            String result = url;
            String htmls = HttpRequest.get(url).header("Referer", "https://app.eulertu.cn").execute().body();
            String regExp = "weixin://wap/pay\\S*\\b";
            Pattern pattern = Pattern.compile(regExp);//匹配的模式
            Matcher matcher = pattern.matcher(htmls);
            if (matcher.find()) {
                // 将匹配当前正则表达式的字符串即文件名称进行赋值
                result = matcher.group();
            }

            return result;
        } catch (Exception e) {
            log.info("微信H5支付获取deeplink异常：{}", url, e);
            return url;
        }
    }


    @SneakyThrows
    public static String getAliH5Url(String htmlBody) {

        if (StringUtils.isEmpty(htmlBody))
            return  null;
        String h5Url="";

        h5Url= StringUtils.substring(htmlBody,htmlBody.indexOf("https://"),htmlBody.indexOf("json")+4);
        String biz_content= StringUtils.substring(htmlBody,htmlBody.indexOf("{&quot;"),htmlBody.indexOf("&quot;}")+7);

        h5Url+="&biz_content="+URLUtil.encode(HtmlUtil.unescape(biz_content));

        RequestHeaderDto headerDto = HttpRequestHeaderUtils.getFromHttpRequest();
        // 通过User-Agent来获取设备信息：1：安卓，2：ios，没有值的话，默认上安卓
        String userAgent = headerDto.getUserAgent();
        String device = Constants.SYSTEM_TYPE_1;
        if(userAgent != null && userAgent != "" && (userAgent.contains("iPhone") || userAgent.contains("iphone"))) {
            device = Constants.SYSTEM_TYPE_2;
        }
//        boolean isandriod=true;
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Mobile Safari/537.36");
//        String body = HttpClientUtil.doGet(h5Url, null, headerMap);
        Map<String, String> map = HttpClientUtil.doGet(h5Url, headerMap);
        String body = map.get("body");
        String h5RequestToken = map.get("h5_request_token");
        if (StringUtils.isEmpty(body))
        {
            log.info("获取支付宝deeplink失败，{}",htmlBody);
        }

        // 通过正则表达式提取需要的字符串；也可以直接提取session的值 `pattern = "'session':(.*)'";`
        String pattern = "inData =(.*)";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(body);
        if (matcher.find()) {
            String pullUrl = matcher.group();
            if (pullUrl.length() > 9) {
                pullUrl = pullUrl.substring(9, pullUrl.length() - 1);
                if(pullUrl != null) {
                    if (StringUtils.equals(Constants.SYSTEM_TYPE_1, device)) {
                        JSONObject params = JSONObject.parseObject(pullUrl);
                        if (params.getString("dataString") != null) {
                            pullUrl = params.getString("dataString");
                            // 安卓
                            return String.format("alipays://platformapi/startApp?appId=20000125&orderSuffix=%s#Intent;scheme=alipays;package=com.eg.android.AlipayGphone;end", URLEncoder.encode(pullUrl, "utf-8"));
                        }
                    } else if (StringUtils.equals(Constants.SYSTEM_TYPE_2, device)) {
                        // iso
                        return String.format("alipay://alipayclient/?%s", URLEncoder.encode(pullUrl, "utf-8"));
                    }
                } else {
                    return formartUrl(device, h5RequestToken);
                }
            }
        } else {
            return formartUrl(device, h5RequestToken);
        }
        log.info("获取支付宝deeplink失败，{}",htmlBody);
        return null;
    }

    @SneakyThrows
    private static String formartUrl(String device, String h5RequestToken) {
        if (StringUtils.equals(Constants.SYSTEM_TYPE_2, device)) {
            // iso
            String postUrl = String.format("{\"requestType\":\"SafePay\",\"fromAppUrlScheme\":\"alipays\",\"dataString\":\"h5_route_token=\\\"%s\\\"&is_h5_route=\\\"true\\\"&h5_route_senior=\\\"true\\\"\"}", h5RequestToken);
            return String.format("alipay://alipayclient/?%s", URLEncoder.encode(postUrl, "utf-8"));
        } else {
            // 安卓
            String postUrl = String.format("h5_route_token=\"%s\"&is_h5_route=\"true\"&h5_route_senior=\"true\"", h5RequestToken);
            return String.format("alipays://platformapi/startApp?appId=20000125&orderSuffix=%s", URLEncoder.encode(postUrl, "utf-8"));
        }
    }

    /**
     * 获取证书
     *
     * @param inputStream 证书文件
     * @return {@link X509Certificate} 获取证书
     */
    public static X509Certificate getCertificate(InputStream inputStream) {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);
            cert.checkValidity();
            return cert;
        } catch (CertificateExpiredException e) {
            throw new RuntimeException("证书已过期", e);
        } catch (CertificateNotYetValidException e) {
            throw new RuntimeException("证书尚未生效", e);
        } catch (CertificateException e) {
            throw new RuntimeException("无效的证书", e);
        }
    }

    /**
     * 批量转账到零钱
     *
     * @param requestUrl        请求链接
     * @param requestJson       组合参数
     * @param mchID4M           商户号
     * @param serialNo          商户的证书序列号
     * @param privateKeyPath    商户私钥证书路径
     * @return
     */
    public static String postTransRequest(String requestUrl, String requestJson, String mchID4M, String serialNo, String privateKeyPath) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        // 认证类型
        String schema = "WECHATPAY2-SHA256-RSA2048";
        try {
            // 商户私钥证书
            HttpPost httpPost = new HttpPost(requestUrl);
            // 建议指定charset=utf-8, 不设置正确的字符集，可能导致签名错误
            httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.JSON.toString());
            httpPost.addHeader(HttpHeaders.ACCEPT, ContentType.JSON.toString());
            log.info("====requestJson:=====" + requestJson);
            // 获取token
            String strToken = getToken("POST", "/v3/transfer/batches", requestJson, mchID4M, serialNo, privateKeyPath);
            // 添加认证信息
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, schema + " " + strToken);
            httpPost.setEntity(new StringEntity(requestJson, "UTF-8"));

            // 发起转账请求
            response = httpclient.execute(httpPost);
            log.info("-----getHeaders.Request-ID:"+response.getHeaders("Request-ID"));
            // 获取返回的数据
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param method       请求方法 post
     * @param canonicalUrl 请求地址
     * @param body         请求参数
     * @param merchantId   这里用的商户号
     * @param serialNo     商户的证书序列号
     * @param keyPath      商户证书地址
     * @return
     * @throws Exception
     */
    public static String getToken(String method, String canonicalUrl, String body, String merchantId, String serialNo, String keyPath) throws Exception {
        String signStr = "";
        // 获取32位随机字符串
        String nonceStr = RandomUtil.randomString(32);
        // 当前系统运行时间
        long timestamp = System.currentTimeMillis() / 1000;
        if (StringUtils.isEmpty(body)) {
            body = "";
        }
        // 消息
        String message = buildMessage(method, canonicalUrl, timestamp, nonceStr, body);
        // 签名操作
        String signature = sign(message.getBytes("utf-8"), keyPath);
        log.info("签名信息:{}", signature);
        // 这五项签名信息，无顺序要求
        signStr = "mchid=\"" + merchantId + "\",timestamp=\"" + timestamp + "\",nonce_str=\"" + nonceStr
            + "\",serial_no=\"" + serialNo + "\",signature=\"" + signature + "\"";
        log.info("=====signStr: ========="+ signStr);
        return signStr;
    }

    /**
     * 构造签名串
     *
     * @param method       GET,POST,PUT等
     * @param canonicalUrl 请求接口
     * @param timestamp    获取发起请求时的系统当前时间戳
     * @param nonceStr     随机字符串
     * @param body         待签名字符串
     * @return
     */
    public static String buildMessage(String method, String canonicalUrl, long timestamp, String nonceStr, String body) {
        return method + "\n" + canonicalUrl + "\n" + timestamp + "\n" + nonceStr + "\n" + body + "\n";
    }

    public static String sign(byte[] message, String keyPath) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(getPrivateKey(keyPath));
        sign.update(message);
        return Base64.encodeBase64String(sign.sign());
    }

    /**
     * 微信支付-前端唤起支付参数-获取商户私钥
     *
     * @param filePath 私钥文件路径  (required)
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey(String filePath) throws IOException {

        String content = FileUtil.readUtf8String(filePath);
        String beginPrivateKey = "-----BEGIN PRIVATE KEY-----";
        String endPrivateKey = "-----END PRIVATE KEY-----";
        try {
            String privateKey = content.replace(beginPrivateKey, "")
                .replace(endPrivateKey, "").replaceAll("\\s+", "");
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(
                new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("无效的密钥格式");
        }
    }

}
