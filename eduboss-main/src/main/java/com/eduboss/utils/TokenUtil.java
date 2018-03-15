package com.eduboss.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.eduboss.common.Memcached;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {
	
	private static HashMap<String, User> tokenMap = new HashMap<String, User>();
	
	private static HashMap<String ,String > tokenString=new HashMap<String ,String >();
	
	private static HashMap<String, StudentVo> tokenStudentMap = new HashMap<String, StudentVo>();
	private static UserService userService;
	
	private static Logger logs = Logger.getLogger(TokenUtil.class);
	
	public static String genToken(User User) {
		UUID uuid = UUID.randomUUID();
		SpringMvcUtils.getSession().setAttribute("token", uuid.toString());
//		tokenString.put(uuid.toString(), User.getUserId());//存入用户的Id,减少hibernate session,以及减少tokenMap 占用内存
		//30天后的凌晨3点失效
		Memcached.set(uuid.toString(), User.getUserId(), DateTools.add(DateTools.add(DateTools.getTimesmorning(), Calendar.DATE, 30),Calendar.HOUR,3));
		return uuid.toString();
	}
	
	public static String putSessionAndUserId(String token,String userId) {
		UUID uuid = UUID.randomUUID();
		SpringMvcUtils.getSession().setAttribute("token",token);
		Memcached.set(token, userId, new Date(3600000));
		return uuid.toString();
	}
	
	public static boolean checkToken(String token) {
//        if(StringUtils.isBlank(token)){
//            Object sessionToken = SpringMvcUtils.getSession().getAttribute("token");
//            if(sessionToken != null){
//                token = sessionToken.toString();
//            }
//        }
		Object userId=Memcached.get(token);
		if (userId!=null) {
//			Memcached.set(token, userId,  DateTools.add(DateTools.getTimesmorning(), Calendar.HOUR, 24+3));
			SpringMvcUtils.getSession().setAttribute("token", token);
			return true;
		} else {
			return false;
		}
	}
	
	public static void checkTokenWithException(String token) {
		if (!checkToken(token)) {
			if(Memcached.get(token+"_undefined")!=null){//token失效计数，存活期是20S  ，如果同token失效10次，那么视这个为APP重连机制出问题，返回给前端折腾。
				int i=(int) Memcached.get(token+"_undefined");
				Memcached.set(token+"_undefined",i+1,  new Date(20000));
				if(i>=10){
					throw new ApplicationException(ErrorCode.USB_GO_DIE);
				}
			}else{
				Memcached.set(token+"_undefined",1,  new Date(20000));
			}
//			tokenString.remove(token);
			throw new ApplicationException(ErrorCode.TOKEN_ERROR);
		}
	}
	
//	public static boolean checkUserId(String userId) {
//		String token=getStringAttributeSession("token");
//		String suserId="";
//		if(StringUtils.isBlank(token))
//			return false;
//		else
//			suserId=tokenString.get(token)!=null?tokenString.get(token).toString():"";
//		return suserId.equals(userId);
//	}
	
	public static boolean checkTokenUserIdWithException(String token, String userId) {
		logs.info("传进来的token是"+token+" userId= "+userId);
		String gotUserId = Memcached.get(token)!=null?Memcached.get(token).toString():null;
		if(gotUserId != null && gotUserId.equals(userId)){
			Memcached.set(token, userId,  new Date(7200000));
			SpringMvcUtils.getSession().setAttribute("token", token);
			return true;
		}
		logs.info("结束的token是"+token+" userId= "+userId);
		return false;
	}
	
	public static User getTokenUser(String token) {
		String gotUserId = Memcached.get(token)!=null?Memcached.get(token).toString():null;
		if (StringUtil.isNotBlank(gotUserId)) {
			return getUserService().getUserById(gotUserId);
		} else {
			return null;
		}
	}
	
	
	/** 学生操作相关：登录时创建一个token，同时保存学生的学管师、学生，后续操作时，如果要用户，则返回学管师，如果要学生，则返回自己 */
	public static String genStudentToken(User User, StudentVo student){
		UUID uuid = UUID.randomUUID();
		tokenMap.put(uuid.toString(), User);
		tokenStudentMap.put(uuid.toString(), student);
		return uuid.toString();
	}
	
	public static boolean checkStudentToken(String token) {
		if (!StringUtils.isBlank(token) && tokenStudentMap.containsKey(token)) {
			SpringMvcUtils.getSession().setAttribute("token", token);
			return true;
		} else {
			return false;
		}
	}
	
	public static StudentVo getTokenStudent(String token) {
		if (checkStudentToken(token)) {
			return tokenStudentMap.get(token);
		} else {
			return null;
		}
	}
	
	/**
	 * 打卡机或者指纹机等硬件使用
	 * @param user
	 */
	public static void setToken(User user) {
        SpringMvcUtils.getSession().setAttribute("token", user.getToken());
//        tokenString.put(user.getToken(), user.getUserId());
        Memcached.set(user.getToken(), user.getUserId(),  new Date(3600000));
	}
	
	public static UserService getUserService(){
		if(userService==null){
			 WebApplicationContext webApplicationContext =ContextLoader.getCurrentWebApplicationContext();  
			 userService= (UserService) webApplicationContext.getBean(com.eduboss.service.UserService.class);
		}
		
		return userService;
	}


	/**
	 * 校验token是否正常，接口校验成功后，再对请求头的  employeeNo 对比
	 * @param token
	 * @param request
	 * @return
	 */
	public static Boolean checkInterfaceToken(String token, HttpServletRequest request){
		HttpClient client = HttpHeadersUtils.wrapHttpClient();
		String url =PropertiesUtils.getStringValue("PUBLIC_URL");
		HttpPost req = (HttpPost)HttpHeadersUtils.getRequestBase(RequestMethod.POST,url);
		req.setHeader("token",token);
		HttpResponse getResponse = null;
		logs.info("校验token:"+token+"url:"+url);
		try {
			getResponse = client.execute(req);
			if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String str = "";
					try {
						str = EntityUtils.toString(getResponse.getEntity());
						JSONObject result = new JSONObject(str);
						logs.info("返回信息："+result.toString()+"employeeNo:"+request.getHeader("X-XH-EmployeeNo"));
						if(result.getInt("resultCode")==0){
							JSONObject obj = result.getJSONObject("resultData");
							if(request.getHeader("X-XH-EmployeeNo")!=null
									&& obj.getString("EmployeeNo")!=null
									&& obj.getString("EmployeeNo").equals(request.getHeader("X-XH-EmployeeNo"))){
								return true;
							}
						}else{
							logs.error("校验Token异常,"+result.getString("resultMessage"));
							return false;
						}
					} catch (Exception e) {
						logs.error("校验Token异常,"+e.getMessage());
						return false;
					}
				}else{
					logs.error("校验Token异常,状态码为："+getResponse.getStatusLine().getStatusCode());
					return false;
				}
		} catch (IOException e) {
			logs.error("校验Token异常,状态码为"+e.getMessage());
			return false;
		}
		return false;
	}
	
}
