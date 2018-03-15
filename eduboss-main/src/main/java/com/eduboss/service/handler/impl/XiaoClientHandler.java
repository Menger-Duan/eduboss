package com.eduboss.service.handler.impl;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.eduboss.exception.ApplicationException;
import com.eduboss.service.handler.ClientHandler;
import com.eduboss.utils.PropertiesUtils;

@Component
public class XiaoClientHandler implements ClientHandler {
    
    private final static Logger logger = Logger.getLogger(XiaoClientHandler.class);
    
    private static final String UPDATE_STUDENT_CONTACT = "/internal/tequila/student/xhboss";

    @Override
    public boolean updateStudentContact(String studentId, String contact) {
        boolean result = false;
        logger.info("晓服务平台修改学生手机号,studentId:" + studentId + "contact:" + contact);
//        String url = "https://test-api.xiao100.com/internal/tequila/student/xhboss/STU170824000001/phone";
        String url = PropertiesUtils.getStringValue("XIAO_OAUTH_HOST") + UPDATE_STUDENT_CONTACT + "/" + studentId + "/phone";
        HttpPut httpPost = new HttpPut(url);
        logger.info("晓服务平台修改学生手机号URL:"+url);
        CloseableHttpClient client = HttpClients.createDefault();
//      json方式
        JSONObject jsonParam = new JSONObject();  
        jsonParam.put("phone", contact);
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
        logger.info("晓服务平台修改学生手机号jsonParam:" + jsonParam);
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");
        httpPost.addHeader("X-Cocktail-Secret",PropertiesUtils.getStringValue("XIAO_OAUTH_SECRET_KEY"));
        httpPost.setEntity(entity);
        try {
            HttpResponse resp = client.execute(httpPost);
            logger.info("晓服务平台修改学生手机号response:"+resp);
            if (resp != null && resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = true;
            } else if(resp != null && resp.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                String str = EntityUtils.toString(resp.getEntity(), "utf-8");
                JSONObject jo = JSONObject.fromObject(str);
                if(jo.get("errorCode")!=null && "11070003".equals(jo.get("errorCode").toString())){ // 学生不存在，BOSS系统可以忽略这个错误。
                    result = true;
                } else {
                    throw new ApplicationException("访问晓服务平台修改学生手机号接口出错" + resp.getStatusLine().getStatusCode());
                }
            } else if (resp != null && resp.getStatusLine().getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
                String str = EntityUtils.toString(resp.getEntity(), "utf-8");
                JSONObject jo = JSONObject.fromObject(str);
                logger.info("晓服务平台修改学生手机号JSONObject:" + jo);
                if(jo.get("errorCode")!=null && "11070001".equals(jo.get("errorCode").toString())){
                    throw new ApplicationException("无效手机号");
                } else  if(jo.get("errorCode")!=null && "11070008".equals(jo.get("errorCode").toString())){
                    throw new ApplicationException("手机号已经被其他用户注册");
                } else {
                    throw new ApplicationException("访问晓服务平台修改学生手机号接口出错" + resp.getStatusLine().getStatusCode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ApplicationException("访问晓服务平台修改学生手机号接口出错" + e.getMessage());
        }
        return result;
    }

}
