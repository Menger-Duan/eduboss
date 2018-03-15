package com.eduboss.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eduboss.domain.User;
import com.eduboss.domain.UserDetailsImpl;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.TokenUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySimpleUrlAuthenticationSuccessHandler implements
		AuthenticationSuccessHandler {

	private Logger logger = Logger.getLogger(MySimpleUrlAuthenticationSuccessHandler.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final String split = "?";
	private final String splitParam = "&";
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication arg2) throws IOException, ServletException {
        UserDetailsImpl userDetailImpl = (UserDetailsImpl) arg2.getPrincipal();
        User user = userDetailImpl.getUser();
        String token = TokenUtil.genToken(user);
        request.getSession().setAttribute("token", token);
        String callbackurl = (String)request.getSession().getAttribute("callbackurl");
        
        //String userId = user.getUserId();
        
        if(callbackurl != null)
        {
        	//将当前登录账号的userId和sessionId存到全局变量中
        	/*ServletContext context = request.getSession().getServletContext();
        	@SuppressWarnings("unchecked")
			HashMap<String, HttpSession> userMap = (HashMap<String, HttpSession>)context.getAttribute("userLoginList");
        	//如果当前账号已经登录，则踢下线
        	if(userMap != null)
        	{
        		HttpSession session = (HttpSession)userMap.get(user.getUserId());
            	if(session != null)
            	{
            		session.invalidate();
            	}
            	session = request.getSession();
            	userMap.put(userId, session);
        	}else
        	{
        		HttpSession session = request.getSession();
        		userMap = new HashMap<String, HttpSession>();
        		userMap.put(userId, session);
        	}
        	context.setAttribute("userLoginList", userMap);*/
        	
        	request.getSession().removeAttribute("callbackurl");
        	
        	String queryString = URLDecoder.decode(callbackurl, "utf-8");
            
            int index = queryString.indexOf(split);
            if(index != -1)
            {
            	callbackurl = queryString.substring(0,index);
                queryString = queryString.substring(index+1);
            }else
            {
            	callbackurl = queryString;
            	queryString = null;
            }
        	logger.info("当前时间："+DateTools.getCurrentDateTime()+",进行单点登录...");
        	Cookie tc=new Cookie("token",token);
        	 tc.setMaxAge(3600*24*7);
        	 tc.setPath("/");
        	Cookie uc=new Cookie("userId",user.getUserId());
        	uc.setMaxAge(3600*24*7);
        	uc.setPath("/");
        	response.addCookie(tc);
        	response.addCookie(uc);
        	response.sendRedirect(splitParamter(callbackurl, queryString, user.getUserId(), token));//页面重定向
        }else
        {
        	redirectStrategy.sendRedirect(request, response, "/index.html");//登录成功，固定跳转到首页
        }
	}
	
	/**
	 * 重新拼接参数
	 * @param callbackurl
	 * @param queryString
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private String splitParamter(String callbackurl, String queryString, String userId, String token) throws UnsupportedEncodingException
	{
		StringBuffer url = new StringBuffer();
		
		url.append(callbackurl);
		if(!"".equals(queryString) && null != queryString)
		{
			url.append(split);
			String[] parameters = queryString.split(splitParam);
			for(String param:parameters)
			{
				String[] keyValue = param.split("=");
				url.append(keyValue[0]);
				url.append("=").append(URLEncoder.encode(keyValue[1], "utf-8")).append(splitParam);
			}
		}else
		{
			url.append(split);
		}
		url.append("userId=").append(URLEncoder.encode(userId, "utf-8"));
		url.append(splitParam).append("token=").append(URLEncoder.encode(token, "utf-8"));
		
		return url.toString();
	}
	


}
