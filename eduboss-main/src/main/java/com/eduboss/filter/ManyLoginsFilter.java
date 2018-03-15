package com.eduboss.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.UserOperationLogService;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;

public class ManyLoginsFilter implements Filter{
	
	//记录日志的线程池
	private static ThreadPoolExecutor loggerThreadPool;
	
	private static UserOperationLogService logService;
	
	
	private static List<Pattern> noInterceptUrlRegxList=null;
	
	private UserService userService;
	
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		//loggerThreadPool.shutdown();
		logService=null;
		noInterceptUrlRegxList=null;

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) servletRequest;
		HttpServletResponse response=(HttpServletResponse) servletResponse;
		
		String uri=request.getRequestURI();
		boolean isNeedIntercept=true;//是否需要拦截  true位需要拦截
		//url过滤处理begin
		//noInterceptUrlRegx
		for (Pattern pattern: noInterceptUrlRegxList) {
			 Matcher matcher=pattern.matcher(uri);
			if(matcher.find()){
				isNeedIntercept=false;
				break;
			}
		}
		//url过滤处理end
		String userId=request.getParameter("indexUserId");
		if (StringUtils.isBlank(userId)) {
			String referer=request.getHeader("Referer");
			if (StringUtils.isNotBlank(referer) && referer.indexOf("indexUserId") > 0 && referer.lastIndexOf("?") > 0) {
				String[] parameter = referer.substring(referer.lastIndexOf("?")).split("&");
				for (String parameterStr : parameter) {
					if (parameterStr.contains("indexUserId") && parameterStr.split("=").length > 0) {
						userId = parameterStr.split("=")[1];
					}
				}
			}
		}
		if(userService==null){
			ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
			userService=app.getBean(UserService.class);
		}
		//不需要拦截，则不需要做任何处理
		if(isNeedIntercept && StringUtil.isNotEmpty(userId) && userService.getCurrentLoginUser()!=null &&
				!userId.equals(userService.getCurrentLoginUser().getUserId())){//
			response.getOutputStream().write("8619".getBytes());
			response.flushBuffer();
		} else {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		int corePoolSize=3;   //最小线程数目
		int maximumPoolSize=10; //最大线程数
		//loggerThreadPool=new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000));
		if(logService==null){
			 WebApplicationContext webApplicationContext =ContextLoader.getCurrentWebApplicationContext();  
			 logService= (UserOperationLogService) webApplicationContext.getBean(com.eduboss.service.UserOperationLogService.class);
		}
		
		
		//初始化一些不用拦截的url
		if(noInterceptUrlRegxList==null){
			noInterceptUrlRegxList=new ArrayList<Pattern>();
			String noInterceptUrl=filterConfig.getInitParameter("noInterceptUrlRegx");
			String[] noInterceptUrlArr=noInterceptUrl.split(",");
			for (String patternStr: noInterceptUrlArr) {
				Pattern  pattern=Pattern.compile(patternStr);
				noInterceptUrlRegxList.add(pattern);
			}
		}

	}
}
