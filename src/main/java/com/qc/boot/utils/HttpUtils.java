package com.qc.boot.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 功能描述: http utils
 *
 * @author lijinhua
 * @date 2022/8/25 16:18
 */
public class HttpUtils {

    //静态client
    public  static CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    /**
     * get请求获取String类型数据
     * @param url 请求链接
     * @return
     */
    public static  String get(String url){
        StringBuffer sb = new StringBuffer();
        HttpGet httpGet = new HttpGet(url);
        //todo
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        //开始请求过程
        try{
            HttpResponse response = httpClient.execute(httpGet);
            //读取结果
            HttpEntity entity = response.getEntity();
            InputStreamReader reader = new InputStreamReader(entity.getContent(),"utf-8");
            char[] charbufer;
            while (0<reader.read(charbufer=new char[10])){
                sb.append(charbufer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            httpGet.releaseConnection();
        }
        /** sb可以append，但最后需要toString，就会得到一个json字符串了*/
        return sb.toString().trim();
    }


    /**
     * post方式请求数据
     * @param url 请求链接
     * @param data post数据体
     * @return
     */
    public static  String post(String url, Map<String,String>data){

        StringBuffer sb = new StringBuffer();
        HttpPost httpPost = new HttpPost(url);
        //请求实体content
        List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
        if(null != data){
            for (String key : data.keySet()){
                valuePairs.add(new BasicNameValuePair(key,data.get(key)));
            }
        }
        try {
            //设置请求实体,转成key=value形式
            httpPost.setEntity(new UrlEncodedFormEntity(valuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            //这里有不同
            BufferedInputStream bis = new BufferedInputStream(httpEntity.getContent());
            byte [] buffer;
            while (0<bis.read(buffer=new byte[128])){
                sb.append(new String(buffer,"utf-8"));
            }

        }catch (UnsupportedEncodingException e){//数据格式有误
            e.printStackTrace();
        }catch (IOException e){//请求出错
            e.printStackTrace();
        }finally {
            httpPost.releaseConnection();
        }

        return  sb.toString().trim();

    }


}
