package com.eduboss.utils;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.common.Constants;
import com.eduboss.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.xmlbeans.impl.util.Base64;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

public class HttpHeadersUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpHeadersUtils.class);

    /**
     * 设置统一登录平台的请求头
     */
    public static HttpRequestBase setLoginHeader(String contentType, RequestMethod methodType, String url,String oauthKey){
        HttpRequestBase getrequest=setLoginHeader(contentType,methodType,url,"oauth2",oauthKey);
        return getrequest;
    }

    /**
     * 设置OA　BaseAuth请求头
     */
    public static HttpRequestBase setOALoginHeader(String contentType, RequestMethod methodType,String method){
        String u = PropertiesUtils.getStringValue("HRMS_SECRET_ACCOUNT");
        String key = PropertiesUtils.getStringValue("HRMS_SECRET_KEY");
        String url =PropertiesUtils.getStringValue("HRMS_HOST")+method;
        if(StringUtils.isBlank(contentType)){
            contentType= Constants.CONTENT_TYPE_JSON;
        }
        return HttpHeadersUtils.setLoginHeader(contentType, methodType,url,u,key);
    }

    /**
     * 设置BaseAuth请求头
     */
    public static HttpRequestBase setLoginHeader(String contentType, RequestMethod methodType, String url,String authUser,String oauthKey){
        HttpRequestBase getrequest= getRequestBase(methodType,url);
        String auth = authUser+":" + oauthKey ;
        byte[] encodedAuth = Base64.encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        getrequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        getrequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
        return getrequest;
    }

    public static HttpRequestBase getRequestBase(RequestMethod methodType, String url){
        HttpRequestBase getrequest;
        if(methodType.equals(RequestMethod.GET)) {
            getrequest = new HttpGet(url);
        }else if(methodType.equals(RequestMethod.POST)){
            getrequest = new HttpPost(url);
        }else{
            logger.error("请求方式参数为："+methodType+"");
            throw new ApplicationException("传入参数错误");
        }
        return getrequest;
    }



    /**
     * 装饰HttpClient，允许访问https协议
     * @return
     */
    public static HttpClient wrapHttpClient()
    {
        HttpClient client = HttpClientUtils.getHttpClient();
        client = WebClientDevWrapper.wrapClient(client);
        return client;
    }
    
}
