package com.eduboss.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.eduboss.domainVo.AppVersionVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.AppVersionService;


/**
 * APP 版本过滤
 *
 */
public class AppVersionFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(AppVersionFilter.class);
	
	private static List<Pattern> noInterceptUrlRegxList=null;
	
	AppVersionService appVersionService;
	
	@Override
	public void destroy() {
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
		
		//不需要拦截，则不需要做任何处理
		if(!isNeedIntercept){
			filterChain.doFilter(request, response);
		}else{
			String version=request.getHeader("CodeVersion");
//			String token=request.getParameter("token");
			if (StringUtils.isEmpty(version)) {
//				if(StringUtils.isNotEmpty(token)) {
//					log.error("版本需要更新");
//					response.getOutputStream().write("{\"resultCode\":110,\"resultMessage\":\"请登录晓OA系统首页，在工作链接中的【掌上星火APP】进行扫码下载！\"}".getBytes("utf-8"));
//					response.setStatus(500);
//					response.flushBuffer();
//				}else{
					filterChain.doFilter(request, response);
//				}
			} else{
				String mobileType=request.getHeader("MobileType");
				AppVersionVo vo=appVersionService.getInfoByMbUserType(mobileType, "eduBossManager", "1");
				if(vo==null || version.equals(vo.getVersion())){
					filterChain.doFilter(request, response);
				}else{
					log.info("版本需要更新");
					response.getOutputStream().write("{\"resultCode\":110,\"resultMessage\":\"请登录晓OA系统首页，在工作链接中的【掌上星火APP】进行扫码下载！\"}".getBytes("utf-8"));
					if(!mobileType.equals("iOS")){
						response.setStatus(500);
					}
					response.flushBuffer();
				}
			}
		}
		 
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		if(appVersionService==null){
			 WebApplicationContext webApplicationContext =ContextLoader.getCurrentWebApplicationContext();  
			 appVersionService= (AppVersionService) webApplicationContext.getBean(com.eduboss.service.AppVersionService.class);
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
