package com.eduboss.aop;

import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.RepeatSubmitAnnotation;
import net.sf.json.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Advice通知类
 * 测试after,before,around,throwing,returning Advice.
 * @author Admin
 *
 */
public class RepeatSubmitControllerAopHandler {
	
	/**
	 * @param joinPoint
	 */
	private void doBefore(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		String simpleName=joinPoint.getTarget().getClass().getSimpleName();
		RepeatSubmitAnnotation repeatSubmit = method.getAnnotation(RepeatSubmitAnnotation.class);
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		if(repeatSubmit!=null){
				Object[] argsa = joinPoint.getArgs();
				RequestAttributes ra = RequestContextHolder.getRequestAttributes();
				ServletRequestAttributes sra = (ServletRequestAttributes)ra;
				HttpServletRequest request = sra.getRequest();

				Object value=request.getSession().getAttribute("a");

				Cookie[] cookies = request.getCookies();
			    Object oldCookies=request.getSession().getAttribute("cookies");
				try {
					ObjectUtil.objectToBytes(argsa);
				} catch (IOException e){
					e.printStackTrace();
				}
				if(oldCookies==null)
					request.getSession().setAttribute("cookies",cookies);
				else{
					if(oldCookies instanceof Cookie[]){
						Cookie[]   c = (Cookie[]) oldCookies;
						if(cookies.equals(c)){
							System.out.print(c);
						}
					}
				}



				if(value==null)
					request.getSession().setAttribute("a", argsa);
				else{
					Object [] argsa2= (Object[]) value;

					for (Object o :argsa){
						System.out.print(o.hashCode());
					}

					for (Object o :argsa2){
						System.out.print(o.hashCode());
					}


					if(argsa.hashCode()==argsa2.hashCode()){
						System.out.println(argsa.hashCode());
					}
					if(argsa.equals(argsa2)){
						for (Object o :argsa){
							System.out.print(o);
						}
					}
				}

		}else if(requestMapping!=null) {
			try {
				byte[] key = ObjectUtil.objectToBytes("grayInterfacePublish");
				byte[] value = JedisUtil.get(key);
				if (value != null) {
					String ControllerMethod=simpleName+"_"+method.getName();
					Object object = ObjectUtil.bytesToObject(value);
					if (object instanceof Set) {
						Set<String> set = (Set<String>) object;
						for (String className : set) {
							if(className.equals(ControllerMethod)){
								throw new ApplicationException("系统升级中， 该功能暂停使用， 预计：20:00 后恢复");
							}
						}
					}
				}

			}catch (ClassNotFoundException e){
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				byte[] key=ObjectUtil.objectToBytes("grayTablePublish");
				byte[] value=JedisUtil.get(key);
				if(value!=null){
					Object object=ObjectUtil.bytesToObject(value);
					if (object instanceof Set) {
						Set<String> set= (Set<String>) object;
						for (String className:set){
							if(joinPoint.getArgs().length>0 && className.equals(joinPoint.getArgs()[0].getClass().getSimpleName())){
								throw new ApplicationException("系统升级中， 该功能暂停使用， 预计：20:00 后恢复");
							}
						}
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch (ClassNotFoundException e){
				e.printStackTrace();
			}
		}

	}

	/**
	 * @param joinPoint
	 */
	private void doAfter(JoinPoint joinPoint) {
		String endTime = DateTools.getCurrentDateTime();
		String methodName = joinPoint.getSignature().getName();
	}

	private  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		String methodName = joinPoint.getSignature().getName();
	}

}
