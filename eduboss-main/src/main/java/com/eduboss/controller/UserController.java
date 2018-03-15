package com.eduboss.controller;

import com.aliyun.oss.common.utils.HttpHeaders;
import com.cloopen.rest.sdk.utils.encoder.BASE64Decoder;
import com.eduboss.common.Constants;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDetailsImpl;
import com.eduboss.domainVo.*;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SparkUserVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.util.Base64;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping(value="/UserController")
public class UserController {

	public static final Logger log = Logger.getLogger(UserController.class);

	@Autowired
	UserService userService;
	
	/**
	 * 查找用户列表（自动搜索）
	 * @param term
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserAutoComplate", method =  RequestMethod.GET)
	@ResponseBody
	public String getUserAutoComplate(@RequestParam String term) throws Exception {
		String autoCompeleteData = "";
		
		List<User> resList = userService.getUserAutoComplate(term, null, null);
		
		for (User user : resList) {
			autoCompeleteData += "\""+user.getName()+"-"+user.getAccount()+","+user.getContact() +","+user.getUserId()+"\","; 
		}
		if (autoCompeleteData.length() > 0) {
			autoCompeleteData = autoCompeleteData.substring(0, autoCompeleteData.length() - 1);
		}
		autoCompeleteData = "["+autoCompeleteData+"]";
		
		return autoCompeleteData;
	}
	
	/**
	 * 获取用户token
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserToken", method =  RequestMethod.GET)
	@ResponseBody
	public String getUserToken() throws Exception {
		return SpringMvcUtils.getSession().getAttribute("token")==null ? null : SpringMvcUtils.getSession().getAttribute("token").toString();
	}
	
	/**
	 * 查找用户列表（自动搜索）放回value-label的list 可以用来用于autocomplete的set值
	 * @param term
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getUserAutoComplateEnhanced", method =  RequestMethod.GET)
	@ResponseBody
	public List<AutoCompleteOptionVo> getUserAutoCompleteEnhanced(@RequestParam String term, String roleCode,  String parentOrgId ) throws Exception {
		if(term.indexOf("'") < 0 ) {
			if (StringUtils.isBlank(parentOrgId)) {//如果不传，则默认用员工当前的组织架构
				//User user = userService.getCurrentLoginUser();
				parentOrgId =userService.getBelongCampus().getId();
			}
			List<User> resList = new ArrayList<User>();
			if (StringUtils.isBlank(roleCode)) {
				resList = userService.getUserAutoComplate(term, roleCode, parentOrgId);
			} else {
				String[] roleArray = roleCode.split(",");
				for (String role : roleArray) {
					resList.addAll(userService.getUserAutoComplate(term, role, parentOrgId));
				}
			}
			List<AutoCompleteOptionVo> optionVos =  HibernateUtils.voListMapping(resList, AutoCompleteOptionVo.class);
			return optionVos;
		} else {
			return new ArrayList<AutoCompleteOptionVo>(0);
		}
	}

	/************************************************新组织架构*************************************************************/

	@RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response modifyUserInfo(@RequestBody UserEditDto dto){
		return userService.modifyUserInfo(dto);
	}

	@RequestMapping(value = "/getUserInfoByUserId")
	@ResponseBody
	public Response getUserInfoByUserId(@RequestParam String userId){
		return userService.getUserInfoByUserId(userId);
	}


	@RequestMapping(value = "/getUserInfoList",method = RequestMethod.GET)
	@ResponseBody
	public DataPackage getUserInfoList(@ModelAttribute UserSearchDto dto,@ModelAttribute DataPackage dp){
		return userService.getUserInfoList(dto,dp);
	}


	@RequestMapping(value = "/checkUserOrgCanModify",method = RequestMethod.POST)
	@ResponseBody
	public Response checkUserOrgCanModify(@RequestBody UserOrganizationRoleVo vo){
		return userService.checkUserOrgCanModify(vo);
	}

	/************************************************新组织架构*************************************************************/

	/**
	 * 是否提醒用户修改初始密码
	 * @return
	 */
	@RequestMapping(value = "/remindUserModifyDefaultPwd")
	@ResponseBody
	public Boolean remindUserModifyDefaultPwd(){
		if (Constants.REMIND_USER_MODIFY_DEFAULT_PASSWORD) {
			boolean currentUserPwdIsDefault = currentUserPwdIsDefault();
			if (currentUserPwdIsDefault) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * 当前登录用户密码是否是初始密码
	 * @return
	 */
	@RequestMapping(value = "/currentUserPwdIsDefault")
	@ResponseBody
	public Boolean currentUserPwdIsDefault(){
		User user = userService.getCurrentLoginUser();
		if (Constants.DEFAULT_PASSWORD.equals(user.getPassword())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 查询用户组织架构、主职位（业绩分配）
	 * */
	@RequestMapping(value = "/getUserForPerformance")
	@ResponseBody
	public List<AutoCompleteOptionVo> getUserForPerformance(@RequestParam String term, String roleCode,  String parentOrgId){
		return userService.getUserForPerformance(term,roleCode,parentOrgId);
	}
	
	/**
	 * 查询用户组织架构、主职位,根据组织架构走
	 * */
	@RequestMapping(value = "/getUserByOrgForPerformance")
	@ResponseBody
	public List<AutoCompleteOptionVo> getUserByOrgForPerformance(@RequestParam String term, String roleCode,  String parentOrgId){
		return userService.getUserByOrgForPerformance(term,roleCode,parentOrgId);
	}
	
	@RequestMapping(value = "/updateUserArchivesPath")
	@ResponseBody
	public Response updateUserArchivesPath(User user){
		userService.updateUserArchivesPath(user);
		return new Response();
	}
	
	@RequestMapping(value = "/regAppAccount")
	@ResponseBody
	public Response regAppAccount(@RequestParam String userId){
		userService.regAppAccount(userId);
		return new Response();
	}
	
	//根据指定的职位id查找用户
	@RequestMapping(value = "/getUserAutoComplateByJobId", method =  RequestMethod.GET)
	@ResponseBody
	public List<AutoCompleteOptionVo> getUserAutoComplateByJobId(@RequestParam String jobId){
		List<User> resList=new ArrayList<User>();
        resList=userService.getUserAutoComplateByJobId(jobId);			
		List<AutoCompleteOptionVo> optionVos =  HibernateUtils.voListMapping(resList, AutoCompleteOptionVo.class);
		return optionVos;
	}
	
	//根据指定的角色rolecode查找用户
	@RequestMapping(value = "/getUserAutoComplateByRoleCode", method =  RequestMethod.GET)
	@ResponseBody
	public List<AutoCompleteOptionVo> getUserAutoComplateByRoleCode(@RequestParam String roleCode){
		List<User> resList=new ArrayList<User>();
        resList=userService.getUserByRoldCodesNoOrgLevel(roleCode);			
		List<AutoCompleteOptionVo> optionVos =  HibernateUtils.voListMapping(resList, AutoCompleteOptionVo.class);
		return optionVos;
	}
	
	/**
	 * 获取某校区某职位的用户列表
	 */
	@RequestMapping(value = "/getUserBycampusAndjobSign", method =  RequestMethod.POST)
	@ResponseBody
	public List<User> getUserBycampusAndjobSign(@RequestParam String campusId, @RequestParam String userjobSign) {
		return userService.getUserBycampusAndjobSign(campusId, userjobSign);
	}
	
	@RequestMapping(value = "/loginCallBack",method = RequestMethod.GET)
	public String loginCallBack(String code,HttpServletRequest request ,HttpServletResponse response) throws IOException {
		
		 Map<String,String> returnMap=new HashMap<String,String>();
			if(!StringUtils.isNotBlank(code)){
				return "没有授权";
			}
		
		String url = PropertiesUtils.getStringValue("OAUTH_HOST")+"/oauth2/"+PropertiesUtils.getStringValue("PROJECT_NAME")+"/user_info/"+code;
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		String auth = "oauth2:" +  PropertiesUtils.getStringValue("OAUTH_SECRET_KEY");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		getrequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		getrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		try {
			HttpResponse getResponse = client.execute(getrequest);
			if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(getResponse.getEntity());
                    /**把json字符串转换成json对象**/
                    JSONObject result = new JSONObject(str);
                    boolean flag = result.getBoolean("success");
                    if(flag)
                    {
                    	JSONObject obj = result.getJSONObject("user_info");
                    	
                    	//从spring容器中获取UserDetailsService(这个从数据库根据用户名查询用户信息,及加载权限的service)  
                    	UserDetailsService userDetailsService =   
                    	      (UserDetailsService)ApplicationContextUtil.getContext().getBean("userDetailsService");  
                    	  
                    	//根据用户名username加载userDetails  
                    	UserDetails userDetails = userDetailsService.loadUserByUsername(obj.getString("employee_no"));
						log.info("登录成功Code:"+code+"帐号："+obj.getString("account")+"员工号："+obj.getString("employee_no"));
                    	//根据userDetails构建新的Authentication,这里使用了  
                    	//PreAuthenticatedAuthenticationToken当然可以用其他token,如UsernamePasswordAuthenticationToken                 
                    	PreAuthenticatedAuthenticationToken authentication =   
                    	      new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());  
                    	  
                    	//设置authentication中details  
                    	authentication.setDetails(new WebAuthenticationDetails(request));  
                    	  
                    	//存放authentication到SecurityContextHolder  
                    	SecurityContextHolder.getContext().setAuthentication(authentication);  
//                    	HttpSession session = request.getSession(true);
                    	HttpSession session = migrateSession(request);
                    	//在session中存放security context,方便同一个session中控制用户的其他操作  
                    	session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                    	response.sendRedirect("/eduboss/index.html");
                    }
                } catch (Exception e) {
                	e.printStackTrace();
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}


	public static void main(String [] args){
		String auth = "oauth2:" +  PropertiesUtils.getStringValue("OAUTH_SECRET_KEY");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);

		System.out.print(Base64.decode(PropertiesUtils.getStringValue("OAUTH_SECRET_KEY").getBytes()));
	}


	@RequestMapping(value = "/loginCallBackNew",method = RequestMethod.GET)
	@ResponseBody
	public String loginCallBackNew(@RequestParam String code,HttpServletRequest request ,HttpServletResponse response) throws IOException {

		Map<String,String> returnMap=new HashMap<String,String>();
		if(!StringUtils.isNotBlank(code)){
			return "没有授权";
		}
		String url = PropertiesUtils.getStringValue("OAUTH_HOST")+"/oauth2/"+PropertiesUtils.getStringValue("PROJECT_NAME")+"/user_info/"+code;
		log.info("登录Code:"+code+";url:"+url);
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		String auth = "oauth2:" +  PropertiesUtils.getStringValue("OAUTH_SECRET_KEY");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		getrequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		getrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
		try {
			HttpResponse getResponse = client.execute(getrequest);
			if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					/**读取服务器返回过来的json字符串数据**/
					str = EntityUtils.toString(getResponse.getEntity());
					/**把json字符串转换成json对象**/
					JSONObject result = new JSONObject(str);
					boolean flag = result.getBoolean("success");
                    log.info("登录flag:"+flag);
					if(flag)
					{
						JSONObject obj = result.getJSONObject("user_info");

						/**
                         *从spring容器中获取UserDetailsService(这个从数据库根据用户名查询用户信息,及加载权限的service)
                         */
						UserDetailsService userDetailsService =
								(UserDetailsService)ApplicationContextUtil.getContext().getBean("userDetailsService");

                        /**
                         *根据用户名username加载userDetails
                         */
						UserDetails userDetails = userDetailsService.loadUserByUsername(obj.getString("employee_no"));

						log.info("登录成功Code:"+code+"帐号："+obj.getString("account")+"员工号："+obj.getString("employee_no"));
                        /**
                         *根据userDetails构建新的Authentication,这里使用了
                         *PreAuthenticatedAuthenticationToken当然可以用其他token,如UsernamePasswordAuthenticationToken
                         */
						PreAuthenticatedAuthenticationToken authentication =
								new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());

						//设置authentication中details
						authentication.setDetails(new WebAuthenticationDetails(request));

						//存放authentication到SecurityContextHolder
						SecurityContextHolder.getContext().setAuthentication(authentication);
//                    	HttpSession session = request.getSession(true);
						HttpSession session = migrateSession(request);
						//在session中存放security context,方便同一个session中控制用户的其他操作
						session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
						return "success";
					}else{
                        log.error("登录异常状态码："+flag);
                    }
				} catch (Exception e) {
                    log.error("登录异常状态码："+e.getMessage());
					e.printStackTrace();
				}
			}else{
				log.error("登录异常状态码："+getResponse.getStatusLine().getStatusCode());
				log.error("登录异常url："+url);
			}
		} catch (IOException e) {
			log.error("登录异常IO");
			e.printStackTrace();
		}

		return "false";
	}

	/**
	 * 创建新的Session，复制旧Session的属性
	 * 
	 * @see org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy
	 *       .onAuthentication(Authentication, HttpServletRequest, HttpServletResponse)
	 */
	private HttpSession migrateSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		HashMap<String, Object> attributesToMigrate = new HashMap<String, Object>();
		Enumeration<?> enumer = session.getAttributeNames();
        while (enumer.hasMoreElements()) {
            String key = (String) enumer.nextElement();
            attributesToMigrate.put(key, session.getAttribute(key));
        }
		
        session.invalidate();
        session = request.getSession(true);
        //Copy attributes to new session
        if (!attributesToMigrate.isEmpty()) {
            for (Map.Entry<String, Object> entry : attributesToMigrate.entrySet()) {
                session.setAttribute(entry.getKey(), entry.getValue());
            }
        }
        
        return session;
	}

	
	/**
	 * 装饰HttpClient，允许访问https协议
	 * @return
	 */
	private HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}
	
	/**
	 * 装饰request请求，设置请求头
	 * @param request
	 * @param secret
	 */
	private void wrapRequest(HttpRequestBase request, String secret)
	{
		String auth = "api:" + secret;
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	}

	

	
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		
		User user = userService.getUserByUserName(username);
		if (user == null){ return null;}
		
		List<Role> roles = userService.getRoleByUserId(user.getUserId());
		user.setRole(roles);
		
		
		List<ResourceGrantedVo> resources = new ArrayList<ResourceGrantedVo> ();
		Short roleLevel=0;
		String roleCodes="";
		for (Role role : roles) {
			if (role.getRoleCode() != null) {
				if(roleCodes.equals("")){
					roleCodes+=role.getRoleCode().getValue();
				}else{
					roleCodes+=","+role.getRoleCode().getValue();
				}
			}
			if (roleLevel == 0) {
				roleLevel = role.getRoleLevel();
			}else if(role.getRoleLevel()<roleLevel){
				roleLevel=role.getRoleLevel();
			}
		}
		
		resources.addAll(HibernateUtils.voListMapping(userService.getResourcesByUserId(user.getUserId(),""), ResourceGrantedVo.class));//一条语句返回查询所有权限信息
		
		user.setRoleCode(roleCodes);
		user.setRoleLevel(roleLevel);
		
		resources.addAll(HibernateUtils.voListMapping(userService.getAllAnonymouslyResourceList(), ResourceGrantedVo.class));
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.addAll(resources);
		user.setAuthorities(authorities);
		user.setToken(TokenUtil.genToken(user));
		userService.updateUserToken(user);
		return new UserDetailsImpl(user);
	}
	
	@RequestMapping(value = "/toSparkLogin",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> toSparkLogin(HttpServletRequest request ,HttpServletResponse response) throws IOException {
		Map<String,String> returnMap = new HashMap<String,String>();
		String strBackUrl = "http://" + request.getServerName() //服务器地址  
                + ":"   
                + request.getServerPort()           //端口号  
                + request.getContextPath() ;     //项目名称   
		String url=PropertiesUtils.getStringValue("OAUTH_HOST")+"/oauth2/boss?"+strBackUrl+"/UserController/loginCallBack.do";
		returnMap.put("url", url);
		return returnMap;
	}

	@RequestMapping(value = "/loginOut",method = RequestMethod.GET)
	@ResponseBody
	public Response loginOut(HttpServletRequest request ,HttpServletResponse response) throws IOException {
		SecurityContextHolder.getContext().setAuthentication(null);
		request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", null);
		return new Response();
	}


	
	@RequestMapping(value = "/updateUserBySpark",method = RequestMethod.POST,consumes = "application/json")
	@ResponseBody
	public void updateUserBySpark(HttpServletRequest request ,HttpServletResponse response) {
		if(checkAuthorization(request, response)){
		 ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		    try {
				SparkUserVo vo = mapper.readValue(request.getInputStream(), SparkUserVo.class);
				/**
				 * 停用OA同步用户
				 */
				//userService.updateUserBySpark(vo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 鉴权方法
	 * @param request
	 * @param response
	 * @return
	 */
	public Boolean checkAuthorization(HttpServletRequest request,HttpServletResponse response){
            try{
               response.setCharacterEncoding("UTF-8");
               PrintWriter out=response.getWriter();
               String authorization=request.getHeader("authorization");
               if(authorization==null||authorization.equals("")){
                   response.setStatus(401);
                   response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                   out.print("对不起你没有权限！！");
                   return false;
               }
               String userAndPass=new String(new BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
               if(userAndPass.split(":").length<2){
                   response.setStatus(401);
                   response.setHeader("WWW-authenticate","Basic realm=\"请输入管理员密码\"");
                   out.print("对不起你没有权限！！");
                   return false;
               }
              String user=userAndPass.split(":")[0];
              String pass=userAndPass.split(":")[1];
               if(user.equals(PropertiesUtils.getStringValue("API_USER"))&&pass.equals(PropertiesUtils.getStringValue("API_PWD"))){
            	   return true;
               }else{
            	   response.setStatus(401);
                   response.setHeader("WWW-authenticate","Basic realm=\"帐号密码不匹配\"");
                   out.print("对不起你没有权限！！");
                   return false;
               }
            }catch(Exception ex){
               ex.printStackTrace();
            }
			return false;
	}


}
