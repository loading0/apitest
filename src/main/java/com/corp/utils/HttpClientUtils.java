/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.corp.utils;

import com.alibaba.fastjson.JSONObject;
import org.testng.Reporter;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * HTTP客户端工具类（支持HTTPS）
 *
 * @author
 * @version 2017-3-27
 */
public class HttpClientUtils {
    static String baseUrl = "http://www.kuaidi100.com/";
    public static String sendRequest(String bizData, String apiName, String method){
        String url = baseUrl + apiName;
        System.out.println("request url: "+url);
        Reporter.log("实际发送请求参数："+bizData);
        if ("post".equalsIgnoreCase(method)) {
            return HttpClientUtils.postJson(url,bizData);
        }else if ("get".equalsIgnoreCase(method)) {
            return HttpClientUtils.getJson(url,bizData);
        }
        return HttpClientUtils.postJson(url,bizData);
    }
    /**
     * http的post请求，增加异步请求头参数，传递json格式参数
     */
    public static String postJson(String url, String jsonString) {
        String charset = "UTF-8";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
        StringEntity stringEntity = new StringEntity(jsonString, charset);// 解决中文乱码问题
        stringEntity.setContentEncoding(charset);
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return executeRequest(httpPost, charset);
    }


    /**
     * http的get请求，增加异步请求头参数，传递json格式参数
     */
    public static String getJson(String url, String jsonString) {
        String charset = "UTF-8";
        Map map = JSONObject.parseObject(jsonString);
        //Map<String,String > map = (Map<String,String>)jsonObject;
        String param = CommonUtil.sortParams(map);
        String allUrl = url + "?" + param;
        HttpGet httpGet = new HttpGet(allUrl);
        httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
        httpGet.setHeader("Accept", "application/x-www-form-urlencoded");
        return executeRequest(httpGet, charset);
    }



    /**
     * 执行一个http请求，传递HttpGet或HttpPost参数
     */
    public static String executeRequest(HttpUriRequest httpRequest, String charset) {
        CloseableHttpClient httpclient;
        //Reporter.log("请求发送源数据："+httpRequest.toString());
        if ("https".equals(httpRequest.getURI().getScheme())) {
            httpclient = createSSLInsecureClient();
        } else {
            httpclient = HttpClients.createDefault();
        }
        String result = "";
        try {
            try {
                //设置代理，用于抓取请求数据
                //HttpHost proxy = new HttpHost("127.0.0.1", 8888, "HTTP");
                //CloseableHttpResponse response = httpclient.execute(proxy,httpRequest);
                CloseableHttpResponse response = httpclient.execute(httpRequest);
                HttpEntity entity = null;
                try {
                    entity = response.getEntity();
                    result = EntityUtils.toString(entity, charset);
                } finally {
                    EntityUtils.consume(entity);
                    response.close();
                }
            } finally {
                httpclient.close();
            }
        } catch (IOException ex) {

        }
        return result;
    }




    /**
     * 创建 SSL连接
     */
    public static CloseableHttpClient createSSLInsecureClient() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }
}
