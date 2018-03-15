package com.eduboss.utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;

public class Base64Util {
    
    private static Logger LOGGER = Logger.getLogger(Base64Util.class);
    
    private static String OAUTH_SECRET_KEY = PropertiesUtils.getStringValue("OAUTH_SECRET_KEY");

    /**
     * 鉴权方法
     * @param request
     * @param response
     * @return
     */
    public static Boolean checkAuthorization(HttpServletRequest request,HttpServletResponse response){
        LOGGER.info("鉴权开始");
        try{
           response.setCharacterEncoding("UTF-8");
           PrintWriter out=response.getWriter();
           String authorization=request.getHeader("authorization");
           if(authorization==null||authorization.equals("")){
               response.setStatus(401);
               response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
               out.print("对不起你没有权限！！");
               LOGGER.info("鉴权失败");
               return false;
           }
           String userAndPass=new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
           LOGGER.info("userAndPass:" + userAndPass);
           if(userAndPass.split(":").length<2){
               response.setStatus(401);
               response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
               out.print("对不起你没有权限！！");
               LOGGER.info("鉴权失败");
               return false;
           }
          String user=userAndPass.split(":")[0];
          String pass=userAndPass.split(":")[1];
          if(user.equals("oauth2")&&pass.equals(OAUTH_SECRET_KEY)){
               LOGGER.info("鉴权成功");
               return true;
          }else{
               response.setStatus(401);
               response.setHeader("WWW-authenticate","Basic realm=\"帐号密码不匹配\"");
               out.print("对不起你没有权限！！");
               LOGGER.info("鉴权失败");
               return false;
           }
        }catch(Exception ex){
           ex.printStackTrace();
        }
        LOGGER.info("鉴权失败");
        return false;
    }
    
}
