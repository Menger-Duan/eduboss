package com.eduboss.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * SpringMvc HTTP处理类
 * @author ndd
 *
 */
public class SpringMvcUtils {
	
	public static HttpSession getSession(){
		if(RequestContextHolder.getRequestAttributes()==null)
			return null;
		return ((ServletRequestAttributes)RequestContextHolder
			      .getRequestAttributes()).getRequest().getSession();
	}
	
	public static HttpSession getSession(boolean isNew){
		return ((ServletRequestAttributes)RequestContextHolder
			      .getRequestAttributes()).getRequest().getSession(isNew);
	}
	
	public static Object getSessionAttribute(String name){
	    HttpSession session = getSession(false);
	    return session != null ? session.getAttribute(name) : null;
	}
	
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder
			      .getRequestAttributes()).getRequest();
	}
	
	public static String getParameter(String name){
		return getRequest().getParameter(name);
	}
	
	//可能会报异常转换异常  0.0
	public static HttpServletResponse getResponse(){	
		return ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getResponse();
	}
}
