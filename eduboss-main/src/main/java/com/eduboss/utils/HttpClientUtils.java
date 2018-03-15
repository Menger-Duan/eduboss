package com.eduboss.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient客户端基础类
 * HttpClientUtils.java
 * @author linlihua
 * 2016年6月7日
 */
public class HttpClientUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
	/**
     * 发送get请求，并返回结果
     * @param url    路径
     * @return JSONObject对象
     */
    public static JSONObject httpGet(String url){
        //get请求返回结果
        JSONObject jsonResult = null;
        try {
        	HttpResponse response = exeHttpGet(url);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                /**把json字符串转换成json对象**/
                try{
                	jsonResult = new JSONObject(strResult);
                }catch(Exception ex)
                {
                	//处理返回的字符串可能包含的【】
                	strResult = strResult.substring(1, strResult.length()-1);
                	jsonResult = new JSONObject(strResult);
                }
                
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * 获取get请求返回的结果
     * @param url
     * @return
     */
    public static String httpGetInfo(String url){
        try {
        	HttpResponse response = exeHttpGet(url);
 
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
            	return EntityUtils.toString(response.getEntity());
            } else {
                logger.error("get请求提交失败:" + url);
            }
        } catch (IOException e) {
            logger.error("get请求提交失败:" + url, e);
        }
        return null;
    }
    
    /**
     * 获取HttpClient客户端对象
     * @return
     */
    public static HttpClient getHttpClient(){
    	HttpParams mHttpParams=new BasicHttpParams();
    	//设置连接超时时间，默认为5分钟
    	HttpConnectionParams.setConnectionTimeout(mHttpParams, 5*60*1000);
    	//设置请求超时时间，默认为5分钟
    	HttpConnectionParams.setSoTimeout(mHttpParams, 5*60*1000);
    	//设置socket缓冲区大小，默认为128K
    	HttpConnectionParams.setSocketBufferSize(mHttpParams, 128*1024);
    	//设置请求允许重定向
    	HttpClientParams.setRedirecting(mHttpParams, true);
    	
    	HttpClient httpClient=new DefaultHttpClient(mHttpParams);
    	
    	return httpClient;
    }
    
    
    /**
     * httpPost
     * @param url  路径
     * @param jsonParam 参数
     * @return JSONObject对象
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam){
        return httpPost(url, jsonParam, false);
    }
 
    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){
        //post请求返回结果
    	HttpClient httpClient = getHttpClient();
    	
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
            	List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
                
                @SuppressWarnings("unchecked")
				Set<String> keySet = jsonParam.keySet();  
                for(String key : keySet) {  
                    nvps.add(new BasicNameValuePair(key, (String) jsonParam.get(key)));  
                }  
                method.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            }
            HttpResponse result = httpClient.execute(method);
            
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK || result.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    try{
                    	if(null != str)
                    	{
                    		jsonResult = new JSONObject(str);
                    	}else
                    	{
                    		return null;
                    	}
                    }catch(Exception ex)
                    {
                    	//处理返回的字符串可能包含的【】
                    	str = str.substring(1, str.length()-1);
                    	jsonResult = new JSONObject(str);
                    }
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    
    /**
     * httpPut
     * @param url  路径
     * @param jsonParam 参数
     * @return
     */
    public static JSONObject httpPut(String url,JSONObject jsonParam){
        return httpPut(url, jsonParam, false);
    }
    
    /**
     * put请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    public static JSONObject httpPut(String url,JSONObject jsonParam, boolean noNeedResponse){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPut method = new HttpPut(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    try{
                    	jsonResult = new JSONObject(str);
                    }catch(Exception ex)
                    {
                    	//处理返回的字符串可能包含的【】
                    	str = str.substring(1, str.length()-1);
                    	jsonResult = new JSONObject(str);
                    }
                } catch (Exception e) {
                    logger.error("post请求提交失败:" + url, e);
                }
            }
        } catch (IOException e) {
            logger.error("post请求提交失败:" + url, e);
        }
        return jsonResult;
    }
    
    /**
     * 执行http get请求
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private static HttpResponse exeHttpGet(String url) throws ClientProtocolException, IOException
    {
    	HttpClient client = getHttpClient();
        //发送get请求
        HttpGet request = new HttpGet(url);
        return client.execute(request);
    }
    
    
}
