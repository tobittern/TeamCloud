package com.euler.common.core.utils;

import cn.hutool.core.util.URLUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;


/*
 * HttpClient工具类
 * */
public class HttpClientUtil {

    /**
     * HttpGet 方法
     *
     * @param url   url必填
     * @param param 参数，可空
     * @return
     */
    public static String doGet(String url, Map<String, String> param, Map<String, String> headMap) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            //超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                    .setSocketTimeout(15000).build();
            httpGet.setConfig(requestConfig);
            if (headMap != null) {
                for (Map.Entry<String, String> header :
                        headMap.entrySet()) {
                    httpGet.setHeader(header.getKey(), header.getValue());

                }
            }
            // 执行http请求
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            if (statusCode >= 200 && statusCode <= 300) {
                resultString = EntityUtils.toString(httpEntity, "utf-8");
            } else {
                httpGet.abort();
                String errorMsg = MessageFormat.format("httpget请求返回错误状态码,code:{0}，Url:{1},Params:{2}，ResultStr:{3}", statusCode, url, JsonHelper.toJson(param), EntityUtils.toString(httpEntity, "utf-8"));
                throw new RuntimeException(errorMsg);
            }

        } catch (Exception e) {

            String msg = MessageFormat.format("ErrorMessage:{0}，Url:{1},Params:{2}", e.getMessage(), url, JsonHelper.toJson(param));
            throw new RuntimeException(msg, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
                //关闭，不需要收集异常信息
                ex.printStackTrace();
            }
        }

        return resultString;
    }

    /**
     * HttpGet 方法
     *
     * @param url   url必填
     * @return
     */
    public static Map<String, String> doGet(String url, Map<String, String> headMap) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpContext httpContext = new HttpCoreContext();
        CloseableHttpResponse response = null;
        String resultString = null;
        Map<String, String> resultMap = new HashMap<>();
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            //超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                .setSocketTimeout(15000).build();
            httpGet.setConfig(requestConfig);
            if (headMap != null) {
                for (Map.Entry<String, String> header :
                    headMap.entrySet()) {
                    httpGet.setHeader(header.getKey(), header.getValue());

                }
            }
            // 执行http请求
            response = httpClient.execute(httpGet, httpContext);
            HttpUriRequest realRequest = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            String h5RequestToken = realRequest.getURI().getQuery().substring(17, 64);
            resultMap.put("h5_request_token", h5RequestToken);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            if (statusCode >= 200 && statusCode <= 300) {
                resultString = EntityUtils.toString(httpEntity, "utf-8");
            } else {
                httpGet.abort();
                String errorMsg = MessageFormat.format("httpget请求返回错误状态码,code:{0}，Url:{1},ResultStr:{2}", statusCode, url, EntityUtils.toString(httpEntity, "utf-8"));
                throw new RuntimeException(errorMsg);
            }

        } catch (Exception e) {

            String msg = MessageFormat.format("ErrorMessage:{0}，Url:{1}", e.getMessage(), url);
            throw new RuntimeException(msg, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
                //关闭，不需要收集异常信息
                ex.printStackTrace();
            }
        }
        resultMap.put("body", resultString);
        return resultMap;
    }

    /**
     * httpPost方法
     *
     * @param url   url必填
     * @param param 参数，可空
     * @return
     */
    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            //超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(5000).setConnectionRequestTimeout(5000)
                    .setSocketTimeout(15000).build();
            httpPost.setConfig(requestConfig);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            if (statusCode >= 200 && statusCode <= 300) {
                resultString = EntityUtils.toString(httpEntity, "utf-8");
            } else {
                httpPost.abort();
                String errorMsg = MessageFormat.format("httppost请求返回错误状态码,code:{0}，Url:{1},Params:{2}，ResultStr:{3}", statusCode, url, JsonHelper.toJson(param), EntityUtils.toString(httpEntity, "utf-8"));
                throw new RuntimeException(errorMsg);
            }

        } catch (IOException e) {
            String msg = MessageFormat.format("ErrorMessage:{0}，Url:{1},Params:{2}", e.getMessage(), url, JsonHelper.toJson(param));
            throw new RuntimeException(msg, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
                //关闭，不需要收集异常信息
                ex.printStackTrace();
            }
        }

        return resultString;
    }


    /**
     * @param url           url必填
     * @param data          post的data可为url参数或者json
     * @param contentType   可为null
     * @param authorization 易湃接口验证信息
     * @return
     */
    public static String post(String url, String data, ContentType contentType, String authorization) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        // 创建Http Post请求
        HttpPost httpPost = new HttpPost(url);
        //超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(15000)
                .setConnectionRequestTimeout(5000).build();
        httpPost.setConfig(requestConfig);


        if (contentType == null) {
            contentType = ContentType.APPLICATION_JSON;
        }
        StringEntity entity = new StringEntity(data, contentType);

        //认证信息
        if (authorization != null && !"".equals(authorization)) {
            httpPost.setHeader("Authorization", authorization);
        }
        // 创建请求内容

        httpPost.setEntity(entity);
        String resultString = null;

        try {
            // 执行http请求
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity httpEntity = response.getEntity();
            if (statusCode >= 200 && statusCode <= 300) {
                resultString = EntityUtils.toString(httpEntity, "utf-8");
            } else {
                httpPost.abort();
                String errorMsg = MessageFormat.format("httppost请求返回错误状态码,code:{0}，Url:{1},Params:{2}，ResultStr:{3}", statusCode, url, data, EntityUtils.toString(httpEntity, "UTF-8"));
                throw new RuntimeException(errorMsg);
            }

        } catch (Exception e) {
            String msg = MessageFormat.format("ErrorMessage:{0}，Url:{1},Params:{2}", e.getMessage(), url, data);
            throw new RuntimeException(msg, e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception ex) {
                //关闭，不需要收集异常信息
            }
        }

        return resultString;

    }

    /**
     * postwithJson
     *
     * @param url  请求url
     * @param data postdata
     * @return
     */
    public static String post(String url, String data) {

        String rtn = post(url, data, ContentType.APPLICATION_JSON, null);
        return rtn;

    }


    /**
     * 重试post数据，用于调用接口
     *
     * @param times    重试次数
     * @param apiUrl   请求url
     * @param postData postJson
     * @return
     */
    public static String postTryTimes(int times, String apiUrl, String postData, ContentType contentType) throws
            Exception {
        boolean flag = false;
        int i = 1;
        while (!flag && i <= times) {
            try {
                // log.info(MessageFormat.format("执行第{0}次",i));
                String rtn = post(apiUrl, postData, contentType, null);
                flag = true;//执行未抛出异常,标记成功
                return rtn;
            } catch (Exception ex) {
                if (i >= times)//超过重试限制
                {
                    throw ex;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }//异常休眠2秒再执行下一次

            } finally {
                i++;
            }
        }
        return "";
    }


    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
     *
     * @param parameterMap 需要转化的键值对集合
     * @return 字符串
     */
    public static String convertStringParamter(Map parameterMap) {
        StringBuffer parameterBuffer = new StringBuffer();
        if (parameterMap != null) {
            Set<Map.Entry<String, String>> set = parameterMap.entrySet();
            for (Map.Entry<String, String> entry : set) {
                String value = !StringUtils.isEmpty(entry.getValue()) ? entry.getValue() : "";
                try {
                    value = URLEncoder.encode(value, "utf-8");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                parameterBuffer.append(entry.getKey()).append("=").append(value).append("&");
            }
        }
        if (parameterBuffer.length() > 0) parameterBuffer.deleteCharAt(parameterBuffer.length() - 1);
        return parameterBuffer.toString();
    }

    public static Map<String, String> StringToMap(String mapText) {

        if (mapText == null || mapText.equals("")) {
            return null;
        }

        String[] text = mapText.split("&"); // 转换为数组
        if (text == null || text.length == 0)
            return null;
        Map<String, String> map = new HashMap<>();
        for (String str : text) {
            String[] keyText = str.split("="); // 转换key与value的数组
            if (keyText.length < 1) {
                continue;
            }
            map.put(keyText[0], URLUtil.decode(keyText[1]));

        }
        return map;
    }

}


