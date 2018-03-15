package com.eduboss.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eduboss.domain.User;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.UserService;
import com.eduboss.utils.SpringMvcUtils;
import com.eduboss.utils.TokenUtil;

@Controller
public class LoginController {

	private final static Logger log = Logger.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	/**
	 * 单点登录接口
	 * @param callbackurl
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/loginsso", method=RequestMethod.GET)
	public void loginsso(@RequestParam String callbackurl, HttpServletResponse response) throws IOException{
		String queryString = null;
		try {
			queryString = URLDecoder.decode(callbackurl, "utf-8");
		} catch (Exception e) {
			throw new ApplicationException("请求路径解码出错！");
		}
		
		User user = userService.getCurrentLoginUser();
		String userId = user.getUserId();
		String token = user.getToken();

		int index = queryString.indexOf("?");
        if(index != -1)
        {
        	callbackurl = queryString.substring(0,index+1);
            queryString = queryString.substring(index+1);
        }else
        {
        	callbackurl = queryString+"?";
        	queryString = null;
        }
        
        StringBuffer url = new StringBuffer();
		
		url.append(callbackurl);
		if( null != queryString)
		{
			String[] parameters = queryString.split("&");
			for(String param:parameters)
			{
				String[] keyValue = param.split("=");
				url.append(keyValue[0]);
				url.append("=").append(URLEncoder.encode(keyValue[1], "utf-8")).append("&");
			}
		}
		url.append("userId=").append(URLEncoder.encode(userId, "utf-8"));
		url.append("&").append("token=").append(URLEncoder.encode(token, "utf-8"));
		
		response.sendRedirect(url.toString());
		
	}
	
	/**
	 * 退出登录接口
	 * @param callbackurl
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/logoutsso", method=RequestMethod.GET)
	public void logoutsso(@RequestParam String callbackurl, HttpServletResponse response) throws IOException{
		String queryString = null;
		try {
			queryString = URLDecoder.decode(callbackurl, "utf-8");
		} catch (Exception e) {
			throw new ApplicationException("请求路径解码出错！");
		}
		SpringMvcUtils.getSession().invalidate();
		response.sendRedirect(queryString);
	}
}
