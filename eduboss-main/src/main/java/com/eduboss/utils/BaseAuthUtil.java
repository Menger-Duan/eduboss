package com.eduboss.utils;

import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.eduboss.common.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class BaseAuthUtil {

	/**
	 * OA BASE AUTH 鉴权
	 * @param request
	 * @param response
	 * @return
	 */
	public static Boolean checkAuthorizationOA(HttpServletRequest request,HttpServletResponse response){
		return checkAuthorization(request,response, PropertiesUtils.getStringValue(Constants.API_USER), PropertiesUtils.getStringValue(Constants.API_PWD));
	}


	/**
	 * 鉴权方法
	 * @param request
	 * @param response
	 * @return
	 */
	public static Boolean checkAuthorization(HttpServletRequest request,HttpServletResponse response,String userId,String psw){
		try{
			response.setCharacterEncoding("UTF-8");
			PrintWriter out=response.getWriter();
			String authorization=request.getHeader("authorization");
			if(authorization==null||authorization.equals("")){
				response.setStatus(401);
				response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
				out.print("对不起你没有权限！！");
				return false;
			}
			String userAndPass=new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
			if(userAndPass.split(":").length<2){
				response.setStatus(401);
				response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
				out.print("对不起你没有权限！！");
				return false;
			}
			String user=userAndPass.split(":")[0];
			String pass=userAndPass.split(":")[1];
			if(user.equals(userId)&&pass.equals(psw)){
				return true;
			}else{
				response.setStatus(401);
				response.setHeader("WWW-authenticate","Basic realm=\"帐号密码不匹配\"");
				out.print("对不起你没有权限！！");
				return false;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
}
