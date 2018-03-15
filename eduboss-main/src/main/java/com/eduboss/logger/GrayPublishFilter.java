package com.eduboss.logger;

import com.eduboss.domain.User;
import com.eduboss.domainVo.AppVersionVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.AppVersionService;
import com.eduboss.service.UserOperationLogService;
import com.eduboss.service.UserService;
import com.eduboss.utils.GrayMethodAnnotation;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 日志过滤器
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
public class GrayPublishFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(GrayPublishFilter.class);

	private static List<Pattern> noInterceptUrlRegxList=null;

	@Override
	public void destroy() {
		noInterceptUrlRegxList=null;

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
						 FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request=(HttpServletRequest) servletRequest;
		HttpServletResponse response=(HttpServletResponse) servletResponse;

			try {
				byte[] key = ObjectUtil.objectToBytes("grayInterfacePublish");
				byte[] value = JedisUtil.get(key);

				boolean flag=true;
				if (value != null) {
					Class c=filterChain.getClass();
					//方法名
					MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
					String requestMethod=methodNameResolver.getHandlerMethodName(request);

					Object object = ObjectUtil.bytesToObject(value);
					if (object instanceof Set) {
						Set<String> set = (Set<String>) object;
						for (String className : set) {
							String methodName="";
							if(className.indexOf("_")>0){
								methodName=className.split("_")[1];
							}
							if (requestMethod.equals(methodName)) {
								Response res= new Response();
								res.setResultMessage("系统升级中， 该功能暂停使用， 预计：20:00 后恢复！");
								res.setResultCode(999);
								flag=false;
								JSONObject json = JSONObject.fromObject(res);
								response.getOutputStream().write(json.toString().getBytes("UTF-8"));
								response.setStatus(500);
								response.flushBuffer();
							}
						}
					}
				}

				if(flag){
					filterChain.doFilter(request, response);
				}
			}catch (ClassNotFoundException e){
				e.printStackTrace();
			}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
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
