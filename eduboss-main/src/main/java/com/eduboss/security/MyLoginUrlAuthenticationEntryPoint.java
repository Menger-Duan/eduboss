package com.eduboss.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * 单点登录接口记录回调路径及请求参数
 * 
 * @author llh
 *
 */
public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
		String requestUrl = request.getServletPath().substring(1);
		if("loginsso.do".equals(requestUrl) || "logoutsso.do".equals(requestUrl))
		{
			String callbackurl = request.getParameter("callbackurl");
	        if(null != callbackurl)
	        {
	        	request.getSession().setAttribute("callbackurl", callbackurl);
	        }
		}
        super.commence(request, response, authException);
    }

   /*protected String buildHttpReturnUrlForRequest(HttpServletRequest request)
            throws IOException, ServletException {

        RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
        urlBuilder.setScheme("http");
        urlBuilder.setServerName(request.getServerName());
        urlBuilder.setPort(request.getServerPort());
        urlBuilder.setContextPath(request.getContextPath());
        urlBuilder.setServletPath(request.getServletPath());
        urlBuilder.setPathInfo(request.getPathInfo());
        urlBuilder.setQuery(request.getQueryString());

        return urlBuilder.getUrl();
    }*/
}
