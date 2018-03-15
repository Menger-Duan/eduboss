package com.eduboss.utils;

import com.eduboss.common.Constants;
import com.eduboss.dto.HrmsUserInfoDto;
import com.eduboss.exception.ApplicationException;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Map;

public class RequestOAUtils {

	private static final Logger logger = LoggerFactory.getLogger(RequestOAUtils.class);
    private static final String GET_USER_INFO = "/hrms/syncOuter/getUserInfoByEmNo?employeeNo=";


    public static HttpResponse getOaUserInfo(String employeeNo){
        HttpClient client = HttpHeadersUtils.wrapHttpClient();
        String method =GET_USER_INFO+employeeNo;
        HttpGet getrequest= (HttpGet) HttpHeadersUtils.setOALoginHeader(Constants.CONTENT_TYPE_JSON, RequestMethod.GET,method);
        HttpResponse getResponse = null;
        try {
            getResponse = client.execute(getrequest);
        } catch (IOException e) {
            logger.error("访问人事服务器有问题"+method);
            e.printStackTrace();
        }
        return getResponse;
    }

    public static HrmsUserInfoDto getOaUserInfoToObject(String employeeNo){
        HttpResponse getResponse = RequestOAUtils.getOaUserInfo(employeeNo);
        if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String str;
            try {
                str = EntityUtils.toString(getResponse.getEntity());
                Gson g = new Gson();
                Map userInfo = g.fromJson(str,Map.class);
                if(userInfo !=null && userInfo.get("msg")!=null && "success".equals(userInfo.get("msg")) && userInfo.get("result")!=null) {
                    return g.fromJson(g.toJson(userInfo.get("result")), HrmsUserInfoDto.class);
                }
            } catch (IOException e) {
                logger.error("获取用户信息有问题："+getResponse.getEntity());
                throw new ApplicationException("获取用户信息有问题！"+employeeNo);
            }
            logger.info("获取人事用户信息："+str);
        }else{
            throw new ApplicationException("获取用户信息有问题！"+employeeNo);
        }
        return null;
    }
}
