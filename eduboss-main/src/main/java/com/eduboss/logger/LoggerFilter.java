package com.eduboss.logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;

import com.eduboss.domain.User;
import com.eduboss.domain.UserOperationLog;
import com.eduboss.service.UserOperationLogService;
import com.eduboss.service.UserService;


/**
 * 日志过滤器
 * @author qin.jingkai
 * @version v1.0
 * 2014-09-28
 *
 */
public class LoggerFilter implements Filter {

	private static final Logger log = LoggerFactory.getLogger(LoggerFilter.class);
	
	//记录日志的线程池
	private static ThreadPoolExecutor loggerThreadPool;
	
	private static UserOperationLogService logService;
	
	
	private static List<Pattern> noInterceptUrlRegxList=null;
	
	private UserService userService;
	
	
	
//	@Autowired
//	UserService userService;
	
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
		
		
		//不需要拦截，则不需要做任何处理
		if(!isNeedIntercept){
			//log.error("will====ret:"+uri);
			filterChain.doFilter(request, response);
		}else{
			//log.error("will====process:"+uri);
			WapperedResponse wapper=new WapperedResponse(response);
			String requestTime = LoggerHelper.getCurrentTime();
			long resuquestTimeM = new Date().getTime();
			filterChain.doFilter(request, wapper);//往下服务层请求，完成请求后返回这里
			long responseTimeM = new Date().getTime();
			String responseTime = LoggerHelper.getCurrentTime();
			
			 //方法名
			MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
			String accessMethodName=methodNameResolver.getHandlerMethodName(request); //ok
			log.debug(accessMethodName + ": " + (responseTimeM - resuquestTimeM) + ", requestTime:" + requestTime + ", responseTime:" + responseTime);
			//ip
			 String ipAddress=request.getRemoteAddr(); 
			 //入参
			String inputParameter="";
			StringBuffer inputParameterSb=new StringBuffer();
			 Enumeration  enumeration=request.getParameterNames();
			 while(enumeration.hasMoreElements()){
				 Object key=enumeration.nextElement();
				 String value=request.getParameter(key.toString());
				 inputParameterSb.append(key+"="+value);
				 inputParameterSb.append(";");
			 }
			 inputParameter=inputParameterSb.toString();
			 
			String userId="none";
			String userName="none";
			if(userService==null){
				ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext()); 
				userService=app.getBean(UserService.class);
			}
			
			StringBuffer accessInfo= new StringBuffer();
			byte[] responseData = wapper.getResponseData();
			User usr = userService.getCurrentLoginUser();
			if (usr != null) {
				userId = usr.getUserId();
				userName = usr.getName();
				 String userAgent = request.getHeader("USER-AGENT");
				 String browserInfo = "未知浏览器";
				 String operatSystemInfo= "未知操作系统";
				 if (StringUtils.isNotBlank(userAgent)) {
					 browserInfo=LoggerHelper.getBrowserInfo(userAgent);
					 operatSystemInfo=LoggerHelper.getOperatSystemInfo(userAgent);
				 }
				 String ouptParameter = ""; 
				accessInfo.append("userId="+userId+"`");
				accessInfo.append("userName="+userName+"`");
				accessInfo.append("operationTime="+requestTime+"`");
				accessInfo.append("accessMethodName="+accessMethodName+"`");
				accessInfo.append("inputParameter="+inputParameter+"`");
				accessInfo.append("ouptParameter="+ouptParameter+"`");
				accessInfo.append("browserInfo="+browserInfo+"`");
				accessInfo.append("operatSystemInfo="+operatSystemInfo+"`");
				accessInfo.append("ipAddress="+ipAddress+"`");
				accessInfo.append("responseTime="+responseTime+"`");
				accessInfo.append("processDuration="+String.valueOf(responseTimeM - resuquestTimeM)+"`");
				accessInfo.append("responseDataSize="+responseData.length+"`");
				int responseStatus = wapper.getStatus();
				accessInfo.append("responseStatus="+responseStatus+"`");
				accessInfo.append("sessionId="+request.getSession().getId()+"`");
				//非200 记录前200字节数据
				if(200 != responseStatus) {
				    int dataLength = responseData.length > 200 ? 200 : responseData.length;
				    accessInfo.append("responseData="+new String(responseData, 0, dataLength)+"`");
				}
				
			}
			if(StringUtils.isNotBlank(accessInfo)){
				log.info(accessInfo.toString());
			}
			// 输出处理后的数据(参考资料 http://www.java3z.com/cwbwebhome/article/article8/899.html?id=2380)
			ServletOutputStream output = response.getOutputStream();
			output.write(responseData);
			output.flush();
			output.close();
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
