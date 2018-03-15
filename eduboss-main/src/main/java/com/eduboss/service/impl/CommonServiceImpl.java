package com.eduboss.service.impl;

import com.eduboss.common.*;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.LoginResponse;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.CommonService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.UserService;
import com.eduboss.sms.AliyunSmsUtil;
import com.eduboss.utils.*;
import com.google.common.collect.Maps;

import com.pad.common.CmsUseType;
import com.pad.dao.UserIpadPwdDao;
import com.pad.dto.CmsMenuVo;
import com.pad.dto.IpadLoginDto;
import com.pad.entity.UserIpadPwd;
import com.pad.service.CmsMenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

@Service("com.eduboss.service.CommonService")
public class CommonServiceImpl implements CommonService {
    
    private final static Logger logger = Logger.getLogger(CommonServiceImpl.class);

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DataDictDao dataDictDao;

	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PrintRecordDao printRecordDao;
	
	@Autowired
	private MobileUserService mobileUserService;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserOrganizationDao userOrganizationDao;
	
	@Autowired
	private UserDeptJobDao  userDeptJobDao;
	
	@Autowired
	private StudentSchoolDao studentSchoolDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private JdbcTemplateDao jdbcTemplateDao;

	@Autowired
	private SystemVerifyCodeDao systemVerifyCodeDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CmsMenuService cmsMenuService;

	@Autowired
	private UserIpadPwdDao userIpadPwdDao;


	@Override
	public LoginResponse login(String userName, String password, MobileUserType mobileUserType) throws ApplicationException {
		LoginResponse response = new LoginResponse();
		String account=userName;
		String employeeNo;
//		if(PropertiesUtils.getStringValue("institution")!=null && "xinghuo".equals(PropertiesUtils.getStringValue("institution"))) {
			Map<String, String> resultmap = checkLoginInfoByLoginPlat(userName, password,"boss");
			if(resultmap.get("account")==null || resultmap.get("employeeNo")==null){
				response.setResultCode(998);
				response.setResultMessage(resultmap.get("msg"));
				return response;
			}
			account=resultmap.get("account");
			employeeNo=resultmap.get("employeeNo");
			response.setEmployeeNo(employeeNo);
//		}

		SimpleExpression userNameCr = Expression.eq("employeeNo", employeeNo);
		List<User> users = userDao.findByCriteria(userNameCr);
		if (users.size() == 0) {
			response.setResultCode(998);
			response.setResultMessage("找不到用户信息");
			return response;
		}
//		else{
//			if (!password.equals(users.get(0).getPassword()) && (PropertiesUtils.getStringValue("institution")==null || !"xinghuo".equals(PropertiesUtils.getStringValue("institution")))) {
//				response.setResultCode(998);
//				response.setResultMessage("用户名或密码错误");
//				return response;
//			}
//		}
		
		if(users.size()>0 && users.get(0).getEnableFlg()==1){
			response.setResultCode(998);
			response.setResultMessage("该用户已被禁用");
			return response;
		}
		
		User user=users.get(0);
		
		
		List<Role> roles = userService.getRoleByUserId(user.getUserId());
		user.setRole(roles);
		
		response.setUser(user);
		
		// 加入roleName 的存储
		Role role = roleDao.findById(user.getRoleId());
		user.setRoleName(role.getName()); 
		// 加入orgName 
		Organization organization = organizationDao.findById(user.getOrganizationId());
		user.setOrganizationName(organization.getName());
		
		Map map=userService.getMainDeptAndJob(user.getUserId());
		
		if(map!=null){
			user.setDeptName(map.get("deptName")==null?"":map.get("deptName").toString());
			user.setJobName(map.get("jobName")==null?"":map.get("jobName").toString());
		}
		
		MobileUser mobileUserForStaff=new MobileUser();
		// 判断有无mobileUserType   如果是空就是老师端
		if( mobileUserType!=null ){
			mobileUserForStaff = mobileUserService.findMobileUserByStaffId(user.getUserId(), mobileUserType);
		}else{
			//不是空    按类型加userId来找的
			mobileUserForStaff = mobileUserService.findMobileUserByStaffId(user.getUserId());
		}
		
		response.setMobileUser(mobileUserForStaff);
	
		List<ResourceVo> resourcelist = HibernateUtils.voListMapping(userService.getResourcesByUserId(user.getUserId(),RoleResourceType.APP.getValue()), ResourceVo.class);
		
		response.setMenuList(resourcelist);
		
		return response;
	}

	@Override
	public IpadLoginDto ipadLogin(String userName, String password) {
		IpadLoginDto response = new IpadLoginDto();

		Map<String, String> resultmap = checkLoginInfoByLoginPlat(userName, password,"boss");
		if(resultmap.get("account")==null){
			response.setResultCode(998);
			response.setResultMessage(resultmap.get("msg"));
			return response;
		}
		String account=resultmap.get("account");


		SimpleExpression userNameCr = Expression.eq("account", account);
		List<User> users = userDao.findByCriteria(userNameCr);
		if (users.size() == 0) {
			response.setResultCode(-1);
			response.setResultMessage("找不到用户信息");
			return response;
		}
		User user = users.get(0);
		if(user.getEnableFlg()==1){
			response.setResultCode(998);
			response.setResultMessage("该用户已被禁用");
			return response;
		}


		Map<String,String> infoMap = new HashMap();
		infoMap.put("name",user.getName());
		infoMap.put("id",user.getUserId());
		Organization org = userService.getBelongBranchByUserId(user.getUserId());
		infoMap.put("orgName",org.getName());
		infoMap.put("orgId",org.getId());
		infoMap.put("orgType",org.getOrgType().getValue());
		response.setUserInfo(infoMap);
		List<CmsMenuVo> menuList = cmsMenuService.getCmsMenuByOrgId(org.getId(),1);
		List<CmsMenuVo> visitorMenuList=new ArrayList<>();
		List<CmsMenuVo> workMenuList=new ArrayList<>();

		for(CmsMenuVo vo : menuList){
			if(StringUtils.isNotBlank(vo.getStatus()) && vo.getStatus().equals("0")) {
				List<CmsMenuVo> sonMenu = cmsMenuService.findMenuByParentId(vo.getId());
				vo.setSonMenu(sonMenu);
			}
			if(vo.getUseType().equals(CmsUseType.LOGIN_USER)){
				workMenuList.add(vo);
			}else{
				visitorMenuList.add(vo);
			}
		}

		response.setWorkMenuVoList(workMenuList);
		response.setVisitorMenuVoList(visitorMenuList);
		response.setToken(TokenUtil.genToken(user));

		UserIpadPwd pwd = userIpadPwdDao.findById(user.getUserId());
		if(pwd!=null){//已经设置了密码
			response.setSetPwd(true);
		}

		return response;
	}

	@Override
	public Map<String,String> checkLoginInfoByLoginPlat(String username,String password,String type){
		HttpClient client = HttpHeadersUtils.wrapHttpClient();
		HttpPost req = null;
		String url = null;
		if(type.equals("oa")){
			url = PropertiesUtils.getStringValue("OAUTH_HOST")+"/oauth2/oa/"+"check_password";
			req = (HttpPost) HttpHeadersUtils.setLoginHeader("application/json", RequestMethod.POST,url,PropertiesUtils.getStringValue("OAUTH_SECRET_KEY_OA"));
		}else{
			url = PropertiesUtils.getStringValue("OAUTH_HOST")+"/oauth2/"+PropertiesUtils.getStringValue("PROJECT_NAME")+"/"+"check_password";
			req = (HttpPost) HttpHeadersUtils.setLoginHeader("application/json", RequestMethod.POST,url,PropertiesUtils.getStringValue("OAUTH_SECRET_KEY"));
		}
		
		Map nvps = new HashMap();
		nvps.put("username", username);
		nvps.put("password", password);
		Map<String,String> map = new HashMap<>();
		try {
			String jsonstr = net.sf.json.JSONObject.fromObject(nvps).toString();
			logger.info("checkLoginInfoByLoginPlat post jsonstr:" + jsonstr);
			StringEntity se = new StringEntity(jsonstr);
			req.setEntity(se);
			HttpResponse getResponse = client.execute(req);
			if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					/**读取服务器返回过来的json字符串数据**/
					str = EntityUtils.toString(getResponse.getEntity());
					/**把json字符串转换成json对象**/
					JSONObject result = new JSONObject(str);

					if(result.getBoolean("success")){
						JSONObject obj = result.getJSONObject("user_info");
                        
						map.put("msg",result.getString("msg"));
						map.put("account",obj.getString("account"));
						map.put("phone",obj.getString("phone"));
						map.put("name",obj.getString("name"));
						map.put("employeeNo",obj.getString("employee_no"));

					}else{
						map.put("msg",result.getString("msg"));
					}
					return map;
				} catch (Exception e) {
					map.put("msg","登录异常："+e.getMessage());
					logger.info("checkLoginInfoByLoginPlat response msg:" + e.getMessage());
				}
			}else{
				map.put("msg","登录异常,状态码为："+getResponse.getStatusLine().getStatusCode());
				logger.info("checkLoginInfoByLoginPlat response msg,登录异常,状态码为："+getResponse.getStatusLine().getStatusCode());
			}
		} catch (IOException e) {
			map.put("msg","登录IO异常："+e.getMessage());
		}
		logger.info("checkLoginInfoByLoginPlat response map:" + map.toString());
		return map;
	}

	@Override
	public DataPackage getSelectOptions(DataDict option, DataPackage dp) throws ApplicationException {
		if (option.getCategory() == null) {
			throw new ApplicationException(ErrorCode.OPTION_CATEGORY_EMPTY);
		}
		List<DataDict> selectOptions = new ArrayList<>();

		if (option.getParentDataDict()!=null){
			selectOptions = dataDictDao.findByCriteria(HibernateUtils.prepareOrder(dp, "dictOrder", "asc"), Expression.eq("category", option.getCategory()), Expression.ne("state", "1"), Expression.eq("parentDataDict", option.getParentDataDict()));
		}else {
			selectOptions = dataDictDao.findByCriteria(HibernateUtils.prepareOrder(dp, "dictOrder", "asc"), Expression.eq("category", option.getCategory()), Expression.ne("state", "1"));
		}


//		List<DataDict> selectOptions = dataDictDao.findAll();
		dp.setDatas(selectOptions);
		
		return dp;
	}

	/**
	 * 根据用户名和组织架构父ID返回校区列表,如果orgId为空，返回所有校区
	 */
	@Override
	public List<Organization> getCapumsByOrgLevel(String userName, String orgLevel) {
		return organizationDao.findByCriteria(Expression.like("orgLevel", orgLevel,MatchMode.START), Expression.eq("orgType", OrganizationType.CAMPUS), Expression.eq("bossUse", 0));
	}
	

	/**
	 * 根据id获取组织架构下 指定类型下的所有所有数据
	 * @param orgType
	 * @param orgId
	 * @return
	 */
	public List<Organization> getOrganizatonByOrgIdAndType(OrganizationType orgType, String orgId) {
		String orgLevel = "";
		if (StringUtils.isEmpty(orgId)) {
			//权限查询所属组织架构下的所有校区信息
			Organization org=null;
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
			if(orgType.equals(OrganizationType.CAMPUS)){
			if(userOrganizations != null && userOrganizations.size() > 0){//如果不传组织Id，就默认查所有的所属校区
				Session session = organizationDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
				Criteria croteria = session.createCriteria(Organization.class)
						.add(Expression.eq("orgType", orgType));

				croteria.add(Expression.eq("bossUse", 0));
				if(userOrganizations.get(0).getOrgType().equals(OrganizationType.DEPARTMENT)){//部门的话找上一级对应的数据
					
					Criterion  restrictions= Restrictions.like("orgLevel",organizationDao.findById(userOrganizations.get(0).getParentId()).getOrgLevel(), MatchMode.START);
					
					for(int i = 1; i < userOrganizations.size(); i++){
						if(userOrganizations.get(i).getOrgType().equals(OrganizationType.DEPARTMENT)){
							restrictions= Restrictions.or(Restrictions.like("orgLevel",organizationDao.findById(userOrganizations.get(i).getParentId()).getOrgLevel(), MatchMode.START),restrictions);
						}else{
							restrictions= Restrictions.or(Restrictions.like("orgLevel",userOrganizations.get(i).getOrgLevel(), MatchMode.START),restrictions);
						}
					}
					croteria.add(restrictions);
				}else{
					Criterion  restrictions= Restrictions.like("orgLevel",userOrganizations.get(0).getOrgLevel(), MatchMode.START);
					
					for(int i = 1; i < userOrganizations.size(); i++){
						if(userOrganizations.get(i).getOrgType().equals(OrganizationType.DEPARTMENT)){
							restrictions= Restrictions.or(Restrictions.like("orgLevel",organizationDao.findById(userOrganizations.get(i).getParentId()).getOrgLevel(), MatchMode.START),restrictions);
						}else{
							restrictions= Restrictions.or(Restrictions.like("orgLevel",userOrganizations.get(i).getOrgLevel(), MatchMode.START),restrictions);
						}
					}
					croteria.add(restrictions);
				}
					
				return croteria.list();
			}else{
					 org = userService.getBelongCampus();
			}
			}else if(orgType.equals(OrganizationType.GROUNP)){
				Session session = organizationDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
				Criteria croteria = session.createCriteria(Organization.class)
						.add(Expression.eq("orgType", orgType));
				croteria.add(Expression.eq("bossUse", 0));
				if(userOrganizations.get(0).getOrgType().equals(OrganizationType.DEPARTMENT)){//部门的话找上一级对应的数据

					Criterion  restrictions= Restrictions.like("orgLevel",organizationDao.findById(userOrganizations.get(0).getParentId()).getOrgLevel(), MatchMode.START);

					for(int i = 1; i < userOrganizations.size(); i++){
						if(userOrganizations.get(i).getOrgType().equals(OrganizationType.DEPARTMENT)){
							restrictions= Restrictions.or(Restrictions.like("orgLevel",organizationDao.findById(userOrganizations.get(i).getParentId()).getOrgLevel(), MatchMode.START),restrictions);
						}else{
							restrictions= Restrictions.or(Restrictions.like("orgLevel",userOrganizations.get(i).getOrgLevel(), MatchMode.START),restrictions);
						}
					}
					croteria.add(restrictions);
				}else{
					Criterion  restrictions= Restrictions.like("orgLevel",userOrganizations.get(0).getOrgLevel(), MatchMode.START);

					for(int i = 1; i < userOrganizations.size(); i++){
						if(userOrganizations.get(i).getOrgType().equals(OrganizationType.DEPARTMENT)){
							restrictions= Restrictions.or(Restrictions.like("orgLevel",organizationDao.findById(userOrganizations.get(i).getParentId()).getOrgLevel(), MatchMode.START),restrictions);
						}else{
							restrictions= Restrictions.or(Restrictions.like("orgLevel",userOrganizations.get(i).getOrgLevel(), MatchMode.START),restrictions);
						}
					}
					croteria.add(restrictions);
				}
				return croteria.list();
			}else{
				if(userOrganizations != null && userOrganizations.size() > 0){//如果不传组织Id，就默认查所有的所属分公司
					Session session = organizationDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
					Criteria croteria = session.createCriteria(Organization.class)
							.add(Expression.eq("orgType", orgType));
					croteria.add(Expression.eq("bossUse", 0));
					Criterion  restrictions= Restrictions.like("orgLevel",userOrganizations.get(0).getOrgLevel(), MatchMode.START);
					for(int i = 1; i < userOrganizations.size(); i++){
						restrictions= Restrictions.or(Restrictions.like("orgLevel",userOrganizations.get(i).getOrgLevel(), MatchMode.START),restrictions);
					}					
					croteria.add(restrictions);
					List list = croteria.list();
					if(list.size()<1){
						org = userService.getBelongBranch();
					}else{
						return croteria.list();
					}
				}else if(orgType.equals(OrganizationType.BRENCH) && userOrganizations.get(0).getOrgType().equals("CAMPUS")){
					//登录人是校区，查询所属分公司
					org=organizationDao.findById(userOrganizations.get(0).getParentId());
				}
				else{
				 org = userService.getBelongBranch();
				}
			}
			if (org != null) {
				orgLevel = org.getOrgLevel();
			}
		}else if(StringUtil.isNotBlank(orgId) && orgType.equals(OrganizationType.CAMPUS)){
			//分公司联动校区
			StringBuffer sql=new StringBuffer();
			Organization org = userService.getOrganizationById(orgId);
			Map<String, Object> params = Maps.newHashMap();
			List<Organization> orgList = new ArrayList<>();
			if (org!=null){
				params.put("orgLevel", org.getOrgLevel()+"%");
				sql.append("select * from organization where orgLevel like :orgLevel and orgType='CAMPUS' and boss_use = 0 ");
				orgList=organizationDao.findBySql(sql.toString(),params);
			}
			return orgList;
		}
		else {
			Organization org = userService.getOrganizationById(orgId);
			if (org != null) {
				orgLevel = org.getOrgLevel();
			}
		}
		return organizationDao.findByCriteria(Expression.like("orgLevel", orgLevel,MatchMode.START), Expression.eq("orgType", orgType), Expression.eq("bossUse", 0));
	}
	
	/**
	 * 根据id获取组织架构下 指定类型下的所有所有数据
	 * @param orgTypes
	 * @param orgId
	 * @return
	 */
	public List<Organization> getOrganizatonByOrgIdAndType(List<OrganizationType> orgTypes, String orgId) {
		List<Organization>  organizations =new ArrayList<Organization>();
		for(OrganizationType orgType : orgTypes){
			organizations.addAll(getOrganizatonByOrgIdAndType(orgType, orgId));
		}
		return organizations;
	}
	
	/*@Override
	public List<Organization> getAllBrench(String organizationId) {
		if(StringUtils.isNotEmpty(organizationId)){
			Organization organization=organizationDao.findById(organizationId);
			if(organization != null)
				return organizationDao.findByCriteria(Expression.eq("orgType", OrganizationType.BRENCH)
						,Expression.like("orgLevel", organization.getOrgLevel(), MatchMode.START));
		}
		return organizationDao.findByCriteria(Expression.eq("orgType", OrganizationType.BRENCH));
	}*/
	
	@Override
	public List<Organization> getAllBrench(String organizationId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = " select * from organization where orgType = 'BRENCH' and boss_use = 0 ";
		if(StringUtils.isNotEmpty(organizationId)){
			Organization organization=organizationDao.findById(organizationId);
			if(organization != null) {
				sql += "  and orgLevel like :orgLevel ";
				params.put("orgLevel", organization.getOrgLevel()+"%");
			}
		}
		return organizationDao.findBySql(sql, params);
	}

	@Override
	public List<DataDict> getDataDictByParentId(String parentId) {
		return dataDictDao.findByCriteria(Expression.eq("parentDataDict.id", parentId));
	}

	@Override
	public List<DataDict> getDataDictAllCity() {
		return dataDictDao.findByCriteria(Expression.eq("category", DataDictCategory.CITY));
	}
	
	@Override
	public SelectOptionResponse getSelectOption(String selectOptionCategory, String parentId) {
		if (selectOptionCategory == null) {
			throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
		}
		
		List<NameValue> nvs = new ArrayList<NameValue>();

		if (EnumHelper.getEnum(selectOptionCategory) != null) {//是enum，返回该enum的所有选项
			nvs.addAll(EnumHelper.getEnumOptions(selectOptionCategory));
		} else {//否则从数据库读数据字典
			DataPackage dataPackage = new DataPackage(0, 999);
			if (StringUtil.isNotBlank(parentId)){
				dataPackage = getSelectOptions(new DataDict(DataDictCategory.valueOf(selectOptionCategory), new DataDict(parentId)), dataPackage);
			}else {
				dataPackage = getSelectOptions(new DataDict(null, DataDictCategory.valueOf(selectOptionCategory)), dataPackage);
			}

			List<DataDict> sos = (List<DataDict>)dataPackage.getDatas();
//			List<DataDict> sos = dataDictDao.getDataDictListByCategoryJdbc(DataDictCategory.valueOf(selectOptionCategory));
			for (DataDict so : sos) {
				nvs.add(so);
			}
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "请选择");
		return selectOptionResponse;
	}
	
	/**
	 * 记录打印信息
	 */
	public void recordPrintInfo(PrintType printType, String businessId) {
		
		PrintRecord printRecord = new PrintRecord();
		printRecord.setPrintType(printType);
		printRecord.setBusinessId(businessId);
		printRecord.setOperatorId(userService.getCurrentLoginUser().getUserId());
		printRecord.setPrintTime(DateTools.getCurrentDateTime());
		printRecordDao.save(printRecord);
	}

	/*@Override
	public List<List<Organization>> getBrenchCampusByLimit(String organizationId) {
//		Map<String, List<Organization>> maps = new HashMap<String, List<Organization>>();
		List<List<Organization>> list = new ArrayList<List<Organization>>();
		List<Organization> brenchs = this.getAllBrench(organizationId);
		Organization blOrganization = null;
		if(StringUtils.isNotEmpty(organizationId)){
			blOrganization=organizationDao.findById(organizationId);
		}
		if(brenchs!=null && brenchs.size()>0){
			for(Organization brench : brenchs){
				List<Organization> organization = new ArrayList<Organization>();
				organization.add(brench);
				StringBuilder hql = new StringBuilder();
				hql.append(" from Organization where 1=1");
				hql.append(" and orgLevel like '"+brench.getOrgLevel()+"%'").append(" and orgType = 'CAMPUS'");
				if(blOrganization != null)
					hql.append(" and orgLevel like '"+blOrganization.getOrgLevel()+"%'");
				List<Organization> org = organizationDao.findAllByHQL(hql.toString());
				organization.addAll(org);
				list.add(organization);
			}
		}else if(blOrganization != null){
			List<Organization> organization = new ArrayList<Organization>();
			StringBuilder hql = new StringBuilder();
			hql.append(" from Organization where 1=1");
			hql.append(" and orgType = 'CAMPUS'");
				hql.append(" and orgLevel like '"+blOrganization.getOrgLevel()+"%'");
			List<Organization> org = organizationDao.findAllByHQL(hql.toString());
			organization.addAll(org);
			list.add(organization);
		}
		
		return list;
	}*/
	
	@Override
	public List<List<Organization>> getBrenchCampusByLimit(String organizationId) {
		List<List<Organization>> list = new ArrayList<List<Organization>>();
		List<Organization> brenchs = this.getAllBrench(organizationId);
		Organization blOrganization = null;
		if(StringUtils.isNotEmpty(organizationId)){
			blOrganization=organizationDao.findById(organizationId);
		}
		/**
		 * 查询所有分公司的校区
		 */
		
		
		
		if (blOrganization == null){
			Map<String, Object> params = Maps.newHashMap();
			StringBuilder sql = new StringBuilder();
			Map<Organization, List<Organization>> branchMap = new HashMap<>();
			Map<String, Organization> map = new HashMap<>();
			for (Organization brench : brenchs){
				branchMap.put(brench, new ArrayList<Organization>());
				map.put(brench.getId(), brench);
			}
			sql.append(" select * from organization where orgType = 'CAMPUS' and boss_use = 0 ");
			List<Organization> org = organizationDao.findBySql(sql.toString(), params);
			for(Organization campus :org){
				Organization branch = map.get(campus.getParentId());
				if (branch!=null){
					List<Organization> organizations = branchMap.get(branch);
					organizations.add(campus);
				}
			}
			for (Map.Entry<Organization, List<Organization>> entry:branchMap.entrySet()){
				List<Organization> organization = new ArrayList<>();
				List<Organization> campus =new ArrayList<>();
				organization.add(entry.getKey());
				for (Organization o : entry.getValue()){
					campus.add(o);
				}
				organization.addAll(campus);
				list.add(organization);
			}

			return list;
		}
		if(blOrganization != null && brenchs!=null && brenchs.size()>0){
			Map<String, Object> params = Maps.newHashMap();
			for(Organization brench : brenchs){
				List<Organization> organization = new ArrayList<Organization>();
				organization.add(brench);
				StringBuilder sql = new StringBuilder();
				sql.append(" select * from organization where 1=1 and boss_use = 0 ");
				sql.append(" and orgLevel like '" + brench.getOrgLevel()+"%'").append(" and orgType = 'CAMPUS'");
				sql.append(" and orgLevel like '"+blOrganization.getOrgLevel()+"%'");
				List<Organization> org = organizationDao.findBySql(sql.toString(), params);
				organization.addAll(org);
				list.add(organization);
			}
		}else if(blOrganization != null){
			Map<String, Object> params = Maps.newHashMap();
			List<Organization> organization = new ArrayList<Organization>();
			StringBuilder sql = new StringBuilder();
			sql.append(" select * from organization where 1=1 and boss_use = 0 ");
			sql.append(" and orgType = 'CAMPUS'");
			sql.append(" and orgLevel like '"+blOrganization.getOrgLevel()+"%'");
			List<Organization> org = organizationDao.findBySql(sql.toString(), params);
			organization.addAll(org);
			list.add(organization);
		}
		
		return list;
	}
	
	//************************************根据客户查询学生   start ************************************************//
//	@Override
	public SelectOptionResponse getStudentByCustomer(CustomerVo cus){
		StringBuilder hql = new StringBuilder();
		List<Student> list = new ArrayList<Student>();
		if(cus.getId() != null && StringUtils.isNotBlank(cus.getId()) ){
			Map<String, Object> params = Maps.newHashMap();
			params.put("customerId", cus.getId());
			hql.append(" from Student where id in (select studentId from CustomerStudent where customerId = :customerId ) ");
			list = studentDao.findAllByHQL(hql.toString(),params);			
		}		
		List<NameValue> nvs = new ArrayList<NameValue>();
		for(Student stu : list){
			nvs.add(SelectOptionResponse.buildNameValue(stu.getName(), stu.getId()));
		}
		SelectOptionResponse selectOptionResponse = new SelectOptionResponse(nvs);
		selectOptionResponse.getValue().remove(null);
		selectOptionResponse.getValue().put("", "选择学生");

		return selectOptionResponse;
			
	}

	//************************************根据客户查询学生   end ************************************************//


	/**
	 * 如果一个用户分配了“分公司建班人”的角色，
	 * 那么这个用户的数据权限范围无论是在“分公司”还是在“校区”，
	 * 都可以在小班管理界面建立用户所在分公司所有校区的小班，
	 * 并且可以查看其所在分公司的所有校区的小班
	 *
	 * @return
	 */
	@Override
	public List<List<Organization>> getCampusForSmallClassCourse() {
		List<List<Organization>> list = new ArrayList<List<Organization>>();
		User user = userService.getCurrentLoginUser();
		//先判断是否具有分公司建班人角色
		List<Role> roles = user.getRole();
		Organization belongBranch = null;
		for (Role role : roles){
			if ("分公司建班人".equals(role.getName())){
				belongBranch = userService.getBelongBranch();
				System.out.println("具有分公司建班人角色的所属分公司"+belongBranch.getName());
			}
		}
		if (belongBranch!=null){
			list.addAll(this.getBrenchCampusByLimit(belongBranch.getId()));
			return list;
		}
		String organizationId = user.getOrganizationId();
		Organization org = organizationDao.findById(organizationId);

		OrganizationType orgType = org.getOrgType();

		List<Organization> organization = new ArrayList<Organization>();
		if (OrganizationType.GROUNP == orgType) {
			return this.getBrenchCampusByLimit(organizationId);
		} else if (OrganizationType.BRENCH == orgType) {
			List<Organization> uolist=user.getOrganization();
			for (Organization uo : uolist) {
				if (OrganizationType.BRENCH == uo.getOrgType()) {
					list.addAll(this.getBrenchCampusByLimit(uo.getId()));
				}
			}
			return list;
		} else if (OrganizationType.CAMPUS == orgType) {
			Organization parentOrg = organizationDao.findById(org.getParentId());
			organization.add(parentOrg);
			organization.add(org);
			for (Organization tmp_org: user.getOrganization()) {
				if (!org.getParentId().equals(tmp_org.getParentId())) {
					Organization tmp_parentOrg = organizationDao.findById(tmp_org.getParentId());
					List<Organization> tmp_organization = new ArrayList<Organization>();
					tmp_organization.add(tmp_parentOrg);
					tmp_organization.add(tmp_org);
					list.add(tmp_organization);
				} else {
					if (!org.getId().equals(tmp_org.getId())) {
						organization.add(tmp_org);
					}
				}
			}
//			organization.addAll(user.getOrganization());
			list.add(organization);
			return list;
		} else {
			Organization parentCampus = organizationDao.findById(org.getParentId());
			if (StringUtils.isNotEmpty(parentCampus.getParentId())){
				Organization parentBrench = organizationDao.findById(parentCampus.getParentId());
				if(parentBrench!=null){
					organization.add(parentBrench);
				}
			}
			if(parentCampus!=null) {
				organization.add(parentCampus);
			}
			list.add(organization);
			return list;
		}
	}

	@Override
	public List<List<Organization>> getCampusByLoginUser() {
		User user = userService.getCurrentLoginUser();
		String organizationId = user.getOrganizationId();
		Organization org = organizationDao.findById(organizationId);
		OrganizationType orgType = org.getOrgType();
		List<List<Organization>> list = new ArrayList<List<Organization>>();
		List<Organization> organization = new ArrayList<Organization>();
		if (OrganizationType.GROUNP == orgType) {
			return this.getBrenchCampusByLimit(organizationId);
		} else if (OrganizationType.BRENCH == orgType) {
			for(Organization o : user.getOrganization()){
				if (OrganizationType.BRENCH == o.getOrgType()) {
					list.addAll(this.getBrenchCampusByLimit(o.getId()));
				}
			}
			return list;
		} else if (OrganizationType.CAMPUS == orgType) {
			Organization parentOrg = organizationDao.findById(org.getParentId());
			organization.add(parentOrg);
			organization.add(org);
			for (Organization tmp_org: user.getOrganization()) {
				if (!org.getParentId().equals(tmp_org.getParentId())) {
					Organization tmp_parentOrg = organizationDao.findById(tmp_org.getParentId());
					List<Organization> tmp_organization = new ArrayList<Organization>();
					tmp_organization.add(tmp_parentOrg);
					tmp_organization.add(tmp_org);
					list.add(tmp_organization);
				} else {
					if (!org.getId().equals(tmp_org.getId())) {
						organization.add(tmp_org);
					}
				}
			}
//			organization.addAll(user.getOrganization());
			list.add(organization);
			return list;
		} else {
			Organization parentCampus = organizationDao.findById(org.getParentId());
			if (StringUtils.isNotEmpty(parentCampus.getParentId())){
				Organization parentBrench = organizationDao.findById(parentCampus.getParentId());
				if(parentBrench!=null){
					organization.add(parentBrench);
				}
			}
			if(parentCampus!=null) {
				organization.add(parentCampus);
			}
			list.add(organization);
			return list;
		}
	}
	

	/**
	 * 获取所有校区
	 *
	 * @return
	 */
	@Override
	public Map<String, Map> getAllCampus() {
		Map<String,Map> map=new HashMap<>();
		Map<String,Map> resultMap=new HashMap<>();
		StringBuilder hql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from Organization where orgType = 'CAMPUS' ");
		List<Organization> org = organizationDao.findAllByHQL(hql.toString(),params);
		for (Organization o : org) {
			map=pushToMap(map,o);
		}
		Set<Entry<String, Map>> set = map.entrySet();
		for (Entry<String, Map> entry : set) {
			Organization brench = organizationDao.findById(entry.getKey());
			resultMap.put(brench.getName(), entry.getValue());
		}

		return resultMap;
	}

	@Override
	public Map<String,Map> getCampusByLoginUserForMobile() {
		User user = userService.getCurrentLoginUser();
		Map<String,Map> map=new HashMap<>();
		Map<String,Map> resultMap=new HashMap<>();
		for (Organization tmp_org: user.getOrganization()) {
			if (OrganizationType.GROUNP == tmp_org.getOrgType()) {
				List<Organization> organization = new ArrayList<Organization>();
				StringBuilder hql = new StringBuilder();
				Map<String, Object> params = Maps.newHashMap();
				hql.append(" from Organization where 1=1");
				hql.append(" and orgType = 'CAMPUS'");
				List<Organization> org = organizationDao.findAllByHQL(hql.toString(),params);
				for (Organization org2 : org) {
					map=pushToMap(map,org2);
				}
			}else if (OrganizationType.BRENCH == tmp_org.getOrgType()){
				List<Organization> organization = new ArrayList<Organization>();
				StringBuilder hql = new StringBuilder();
				Map<String, Object> params = Maps.newHashMap();
				params.put("orgLevel", tmp_org.getOrgLevel()+"%");
				hql.append(" from Organization where 1=1");
				hql.append(" and orgType = 'CAMPUS'");
					hql.append(" and orgLevel like :orgLevel ");
				List<Organization> org = organizationDao.findAllByHQL(hql.toString(),params);
				for (Organization org2 : org) {
					map=pushToMap(map,org2);
				}
			}else{
				map=pushToMap(map,tmp_org);
			}
		}
		Set<Entry<String, Map>> set = map.entrySet();
		for (Entry<String, Map> entry : set) {
			Organization brench = organizationDao.findById(entry.getKey());
			resultMap.put(brench.getName(), entry.getValue());
		}
		
		return resultMap;
	}
	
	public Map<String,Map> pushToMap(Map<String,Map> map,Organization o){
		Map omap=new HashMap();
		if(map.get(o.getParentId())!=null){
			 omap= map.get(o.getParentId());
			 if(omap.get(o.getId())==null){
				 map.remove(o.getParentId());
				 omap.put(o.getId(), o.getName());
			 }
			 map.put(o.getParentId(), omap);
		}else{
			omap.put(o.getId(), o.getName());
			map.put(o.getParentId(), omap);
		}
		
		return map;
	}
	
	public SelectOptionResponse getReceptionistFollowUpTargetForSelection() {
		//查校区查询咨询师
		String roleId = "";
		String parentOrgId = "";
		User user = userService.getCurrentLoginUser();
		if(user!=null){
			if (user.getOrganization().size() > 0) {
				Organization organization = userService.getBelongCampus();
				if (organization != null) {
					parentOrgId = organization.getId();
				}
			}
			if (user.getRole().size() > 0) {
				roleId = userService.getRoleByTag(RoleCode.CONSULTOR).getRoleId();
			}
		}
		List<User> users = userService.getStaffByRoldIdAndOrgId(roleId, parentOrgId, true, 999);
		
		List<NameValue> nvs = new ArrayList<NameValue>();
		nvs.add(SelectOptionResponse.buildNameValue("请选择", ""));
		for (User so : users) {
			nvs.add(so);
		}
			
		return new SelectOptionResponse(nvs);
	}

	
	/* (non-Javadoc)
	 * showBelong :  只差所属的咨询师
	 */
	@Override
	public List getReceptionistGroupByCampus(String showBelong,String[] job ) {
		// 当showBelong =branch 即客户管理的录入 要筛选分配对象

		Organization organization = userService.getBelongBranch();
		String orgLevel = organization.getOrgLevel();
		if (StringUtil.isNotBlank(showBelong) && showBelong.equals("manage")) {
			List list = new ArrayList<>();
			StringBuilder query = new StringBuilder();
			
			// 查询当前登录者所在分公司的 分公司资源池 外呼资源池 外呼主管 咨询师  --这里作废
			//需求改动---xiaojinwang 2017-03-04
			
//			query.append(" select CONCAT(o.id,'') as userId,rp.`NAME` as userName,CONCAT(o.id,'') as orgId,o.`name` as orgName ");
//			query.append(" from resource_pool rp INNER  JOIN organization o on rp.ORGANIZATION_ID = o.id ");
//			query.append(" where o.orgLevel LIKE '" + organization.getOrgLevel()
//			+ "%' and (o.orgType ='"+OrganizationType.BRENCH.getValue()+"' or o.`name` LIKE '%外呼%' ) ");
//			List list_branch =organizationDao.findMapBySql(query.toString());
//			list.addAll(list_branch);
//			query.delete(0, query.length());
			
			
			Map<String, Object> params = Maps.newHashMap();
			
			List<Role> roles = userService.getCurrentLoginUserRoles();
			List<RoleSign> roleSigns = new ArrayList<>();
			List<String> roleSignsString = new LinkedList<>();
			for(Role role:roles){
				if(role.getRoleCode()!=null){
					//市场经理  市场总监 ---- 营运主任  外呼主管
					if(role.getRoleCode()==RoleCode.BREND_MERKETING_DIRECTOR||role.getRoleCode()==RoleCode.MARKETING_DIRECTOR){
						roleSigns.add(RoleSign.YYZR);
						roleSigns.add(RoleSign.XQYYFZR);
						roleSigns.add(RoleSign.WHZG);
					}
					//校区营运主任 营运主任 ---- 咨询主管 咨询师
					if(role.getRoleCode()==RoleCode.CAMPUS_OPERATION_DIRECTOR||role.getRoleCode()==RoleCode.OPERATION_DIRECTOR){
						roleSigns.add(RoleSign.ZXZG);
						roleSigns.add(RoleSign.ZXS);
					}
					//外呼主管 --外呼专员 校区前台
					if(role.getRoleCode()==RoleCode.OUTCALL_MANAGER||role.getRoleCode()==RoleCode.BRANCH_OUTCALL_MANAGER){
						roleSigns.add(RoleSign.WHZY);
						roleSigns.add(RoleSign.QT);
					}
					//网络主管  ---网络营销专员 网络专员 营运总监 营运经理 校区营运主任 营运主任
					if(role.getRoleCode()==RoleCode.NETWORK_MANAGER){
						roleSigns.add(RoleSign.WLYXZY);
						roleSigns.add(RoleSign.WLZY);
						roleSigns.add(RoleSign.YYZJ);
						roleSigns.add(RoleSign.YYJL);
						roleSigns.add(RoleSign.XQZR);//校区主任 
						roleSigns.add(RoleSign.XQYYFZR);//校区营运主任
					}
				}
			}
			//StringBuffer joBuffer = new StringBuffer();
			String jobSigns = null;
			if(roleSigns.size()>0){
				for(RoleSign roleSign : roleSigns){
				// joBuffer.append("'"+roleSign.getValue().toLowerCase()+"',");
					roleSignsString.add(roleSign.getValue().toLowerCase());
				}
//				if(joBuffer.length()>1){
//					jobSigns = joBuffer.substring(0, joBuffer.length()-1);
//				}
			}
					
			//查询 校区营运主任和外呼主管
			query.append(" select u.USER_ID userId,CONCAT(u.NAME,'（',uj.job_name,'）') userName,CONCAT(o.id,'') orgId,o.name orgName from user_dept_job udj");
			query.append(" left join user u on u.user_id=udj.USER_ID");
			query.append(" left join organization o on o.id=udj.DEPT_ID");
			query.append(" left join user_job uj on uj.ID=udj.JOB_ID");
			query.append(" where u.ENABLE_FLAG='0' ");
			if(roleSigns.size()>0){
				params.put("roleSignsString", roleSignsString);
				query.append(" and (  uj.JOB_SIGN in ( :roleSignsString ) )");
			}else{
				params.put("jobSign1", RoleSign.WHZG.getValue().toLowerCase());
				params.put("jobSign2", RoleSign.YYZR.getValue().toLowerCase());
				params.put("jobSign3", RoleSign.XQYYFZR.getValue().toLowerCase());
				query.append(" and (  uj.JOB_SIGN = :jobSign1 or uj.JOB_SIGN= :jobSign2 or uj.JOB_SIGN= :jobSign3 )");
			}
			
			if (organization != null) {
				params.put("orgLevel", orgLevel+"%");
				query.append(" and udj.dept_id in (select id from organization where orgLevel like :orgLevel )");
			}
			List list_person =organizationDao.findMapBySql(query.toString(),params);
			list.addAll(list_person);
			return list;
		}else if(StringUtil.isNotBlank(showBelong) && showBelong.equals("transfer_introduce")){
            //转介绍分配对象 调用方法EduBoss.serviceApi.getTransferTargetByCampus xiaojinwang 20170629 并不是这里			
			String roleCode = job[0];
			int flag = 0;
			RoleSign roleSign  = RoleSign.ZXS;
			if(roleCode.equals(RoleCode.CONSULTOR.getValue())){
				//本校咨询师
				flag =1;
				roleSign = RoleSign.ZXS;
			}else if(roleCode.equals(RoleCode.STUDY_MANAGER.getValue())){
				//
				flag =0;
			}else if(roleCode.equals(RoleCode.TEATCHER.getValue())){
				//学管师
				flag =2;
				roleSign = RoleSign.XGS;
			}
			if(flag!=0){
				StringBuffer sql = new StringBuffer();
				sql.append(
						"select u.USER_ID userId,CONCAT(u.NAME,'（',uj.job_name,'）') userName,CONCAT(o.id,'') orgId,o.name orgName from user_dept_job udj");
				sql.append(" left join user u on u.user_id=udj.USER_ID");
				sql.append(" left join organization o on o.id=udj.DEPT_ID");
				sql.append(" left join user_job uj on uj.ID=udj.JOB_ID");
				sql.append(" where u.ENABLE_FLAG='0' ");
				sql.append(" and uj.JOB_SIGN ='" + roleSign.getValue().toLowerCase() + "' ");
				organization = userService.getBelongCampus();
				if (organization != null) {
					sql.append(" and udj.dept_id in (select id from organization where orgLevel like '"
							+ organization.getOrgLevel() + "%')");
				}
				sql.append(" order by o.orgLevel,o.orgOrder;");
				Map<String, Object> params = Maps.newHashMap();
 				return organizationDao.findMapBySql(sql.toString(),params);
			}
			return null;
		}else {

			
			Map<String, Object> params = Maps.newHashMap();
			
			StringBuilder jobname = new StringBuilder(" ");
			String job_name = null;
			if (job != null && job.length > 0) {
				int length = job.length;
				for (int i = 0; i < length; i++) {
					jobname.append(" uj.JOB_SIGN ='" + RoleSign.valueOf(job[i]).getValue().toLowerCase() + "' or");
				}
				job_name = jobname.toString().substring(0, jobname.length() - 3);
			}
			StringBuffer sql = new StringBuffer();
			sql.append(
					"select u.USER_ID userId,CONCAT(u.NAME,'（',uj.job_name,'）') userName,CONCAT(o.id,'') orgId,o.name orgName from user_dept_job udj");
			sql.append(" left join user u on u.user_id=udj.USER_ID");
			sql.append(" left join organization o on o.id=udj.DEPT_ID");
			sql.append(" left join user_job uj on uj.ID=udj.JOB_ID");
			sql.append(" where u.ENABLE_FLAG='0' ");
			if (job_name != null) {
				//params.put("job_name", job_name);
				sql.append(" and ("+job_name+")");
			}
			if (StringUtils.isNotBlank(showBelong) && "1".equals(showBelong)) {
				organization = userService.getBelongCampus();
				if (organization != null) {
					params.put("orgLevel", organization.getOrgLevel()+"%");
					sql.append(" and udj.dept_id in (select id from organization where orgLevel like :orgLevel )");
				}
			} else if (StringUtils.isNotBlank(showBelong) && "2".equals(showBelong)) {// 外呼（TMK）的咨询师分配列表要查询所属分公司下的咨询师

				if (organization != null) {
					params.put("orgLevel", organization.getOrgLevel()+"%");
					sql.append(" and udj.dept_id in (select id from organization where orgLevel like :orgLevel )");
				}
			}
			sql.append(" order by o.orgLevel,o.orgOrder;");
			return organizationDao.findMapBySql(sql.toString(),params);

		}
	}
	
	public List<Organization> getBrenchIncludeOtherCampus(OrganizationType orgType){
		String orgLevel="";
		Organization org=null;
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){//如果不传组织Id，就默认查所有的所属分公司
			Session session = organizationDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
			Criteria croteria = session.createCriteria(Organization.class)
					.add(Expression.eq("orgType", orgType));
			croteria.add(Expression.eq("bossUse",0));
			Criterion  restrictions;
			if(userOrganizations.get(0).getOrgType().equals(OrganizationType.CAMPUS)){
				Organization brench=organizationDao.findById(userOrganizations.get(0).getParentId());
				restrictions=Restrictions.like("orgLevel",brench.getOrgLevel(), MatchMode.START);
			}else{
				restrictions=Restrictions.like("orgLevel",userOrganizations.get(0).getOrgLevel(), MatchMode.START);
			}
			for(int i = 1; i < userOrganizations.size(); i++){
				if(userOrganizations.get(i).getOrgType().equals(OrganizationType.CAMPUS)){
					Organization brench=organizationDao.findById(userOrganizations.get(i).getParentId());
					restrictions=Restrictions.or(Restrictions.like("orgLevel",brench.getOrgLevel(), MatchMode.START),restrictions);
				}else{
					restrictions=Restrictions.or(Restrictions.like("orgLevel",userOrganizations.get(i).getOrgLevel(), MatchMode.START),restrictions);
				}
			}
			croteria.add(restrictions);
			List list = croteria.list();
			if(list.size()<1){
				org = userService.getBelongBranch();
			}else{
				return croteria.list();
			}
		}else if(orgType.equals(OrganizationType.BRENCH) && userOrganizations != null && userOrganizations.size()>0 && userOrganizations.get(0).getOrgType().equals("CAMPUS")){
			//登录人是校区，查询所属分公司
			org=organizationDao.findById(userOrganizations.get(0).getParentId());
		}
		else{
		 org = userService.getBelongBranch();
		}
		if (org != null) {
			orgLevel = org.getOrgLevel();
		}
		return organizationDao.findByCriteria(Expression.like("orgLevel", orgLevel,MatchMode.START), Expression.eq("orgType", orgType), Expression.eq("bossUse", 0));
	}
	
	/**
	 * 根据城市获取学校
	 */
	public List<StudentSchoolVo> getStuSchools(String blCampus){
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from student_school ");
		if(blCampus != null && StringUtils.isNotBlank(blCampus)){
			sql.append(" where city_id= :blCampus ");
            params.put("blCampus", blCampus);
		}else{
			sql.append(" where 1=2 ");
		}
		List<StudentSchool> list=studentSchoolDao.findBySql(sql.toString(),params);
		List<StudentSchoolVo> volist=HibernateUtils.voListMapping(list, StudentSchoolVo.class);
		return volist;
	}


	@Override
	public List<StudentSchoolVo> getStudentSchool(String provinceId, String cityId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from student_school ");
		if (StringUtils.isNotBlank(provinceId)||StringUtils.isNotBlank(cityId)){
			sql.append(" where 1=1 ");
			if (StringUtils.isNotBlank(provinceId)){
				sql.append(" and province_id = :provinceId ");
				params.put("provinceId", provinceId);
			}
			if (StringUtils.isNotBlank(cityId)){
				sql.append(" and city_id = :cityId ");
				params.put("cityId", cityId);
			}
		}else {
			sql.append(" where 1=2 ");
		}
		sql.append(" and ADDRESS is not null and LOG is not null and LAT is not null");
		List<StudentSchool> list=studentSchoolDao.findBySql(sql.toString(),params);
		List<StudentSchoolVo> volist=HibernateUtils.voListMapping(list, StudentSchoolVo.class);
		return volist;
	}

//	@Override
	public String getTokenInLogin(String userName, String password)
			throws ApplicationException {
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
			return "";
		}
		SimpleExpression userNameCr = Expression.eq("account", userName);
		List<User> users = userDao.findByCriteria(userNameCr);
		if (users.size() == 0 || !password.equalsIgnoreCase(users.get(0).getPassword())) {
			return "";
		}
		if(users.size()>0 && users.get(0).getEnableFlg()==1){
			return "";
		}
		User user=users.get(0);
		String token=TokenUtil.genToken(user);
		TokenUtil.putSessionAndUserId(token, user.getUserId());
		return token;
	}

	/**
	 * 专门为找到所属分公司的所有校区
	 * @return
	 */
	@Override
	public List<Organization> selectOrgOptionOfBranch() {
		List<Organization>  organizations =new ArrayList<Organization>();
		Organization branch = userService.getBelongBranch();
		organizations.addAll(getOrganizatonByOrgIdAndType(OrganizationType.CAMPUS, branch.getId()));
		return organizations;
	}
	/**
	 * 根据学生查询所有关联校区
	*/
	@Override
	public List<List<Organization>> getAllStudentCampus(String studentId) {
		List<Organization> list = organizationDao.getAllStudentCampus(studentId);
		List<List<Organization>> returnList = new ArrayList<List<Organization>>();
		for (Organization org : list) {
			List<Organization> addList = new ArrayList<Organization>();
			addList.add(organizationDao.findById(org.getParentId()));
			addList.add(org);
			returnList.add(addList);
		}
		return returnList;
	}
	
	/**
     * 获取当前时间
     */
	@Override
	public String getCurrentDateTime() {
//		if (JedisUtil.exists("currentDateTime")) {
//			return JedisUtil.get("currentDateTime");
//		} else {
			return DateTools.getCurrentDateTime();
//		}
	}

	@Override
	public List<SelectVo> getBranchCampus() {
		//去掉当前校区
		Organization organization = userService.getBelongCampus();
		String belongBranchId = organization.getParentId();
		StringBuilder query = new StringBuilder(64);
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgId", organization.getId());
		query.append(" select concat(a.id,'') as id,a.`name` as `name`,concat(oo.id,'') as parentId, oo.`name` as parentName from (SELECT * from organization o where o.orgType ='CAMPUS' and o.id <> :orgId and o.boss_use = 0) a ");
		query.append(" LEFT JOIN organization oo on oo.id = a.parentID ");
		List<Map<Object, Object>> resultMap = organizationDao.findMapBySql(query.toString(),params);
		List<SelectTempVo> list = new ArrayList<>(); 
		SelectTempVo sVo = null;
		for(Map<Object, Object> map:resultMap){
			sVo = new SelectTempVo();
			sVo.setId(map.get("id")!=null?map.get("id").toString():"");
			sVo.setName(map.get("name")!=null?map.get("name").toString():"");
			sVo.setParentId(map.get("parentId")!=null?map.get("parentId").toString():"");
			sVo.setParentName(map.get("parentName")!=null?map.get("parentName").toString():"");
			list.add(sVo);
		}
		
		//根据parentId去重 
		ArrayList<SelectTempVo> parentList = new ArrayList<>();
		Set<String> parentId = new HashSet<String>();
		for(SelectTempVo tempVo:list){
			if(parentId.add(tempVo.getParentId())){
				parentList.add(tempVo);
			}
		}
		List<SelectVo> result = new LinkedList<>();
		List<SelectVo> childresult = null;
		SelectVo selectVo = null;
		SelectVo childSelectVo = null;
		int size = parentList.size();
		for(SelectTempVo vo:parentList){
			selectVo = new SelectVo();
			if(vo.getParentId().equals(belongBranchId)){
				selectVo.setOrder(parentList.size());
			}else{
				size--;
				selectVo.setOrder(size);
			}
			selectVo.setId(vo.getParentId());
			selectVo.setName(vo.getParentName());
			childresult = new ArrayList<SelectVo>();		
			Iterator<SelectTempVo> iterator = list.iterator();
			while (iterator.hasNext()) {
				SelectTempVo selectTempVo = (SelectTempVo) iterator.next();
				if(vo.getParentId().equals(selectTempVo.getParentId())){
					childSelectVo = new SelectVo();				
					childSelectVo.setId(selectTempVo.getId());
					childSelectVo.setName(selectTempVo.getName());	
					childresult.add(childSelectVo);
					iterator.remove();
				}	
			}
			selectVo.setList(childresult);
			result.add(selectVo);
		}	
		Collections.sort(result, new selectVoComparator());
				
		return result;
	}
	
	
	class selectVoComparator implements Comparator<SelectVo> {
		@Override
		public int compare(SelectVo o1, SelectVo o2) {
			return (o2.getOrder()-o1.getOrder());
		}
		
	}


	@Override
	public List getUsersByRoleSign(String showBelong, String[] job) {
		Organization organization = null;
		if (StringUtils.isNotBlank(showBelong) && showBelong.equals(OrganizationType.GROUNP.getValue())) {
			organization = userService.getBelongGroup();
		} else if (showBelong.equals(OrganizationType.BRENCH.getValue())) {
			organization = userService.getBelongBranch();
		} else if (showBelong.equals(OrganizationType.CAMPUS.getValue())) {
			organization = userService.getBelongCampus();
		}
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder jobname = new StringBuilder(" ");
		String job_name = null;
		if (job != null && job.length > 0) {
			int length = job.length;
			for (int i = 0; i < length; i++) {
				jobname.append(" uj.job_name ='" + RoleSign.valueOf(job[i]).getName() + "' or");
			}
			job_name = jobname.toString().substring(0, jobname.length() - 3);
			
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select u.USER_ID userId,CONCAT(u.NAME,'（',uj.job_name,'）') userName,CONCAT(o.id,'') orgId,o.name orgName from user_dept_job udj");
		sql.append(" left join user u on u.user_id=udj.USER_ID");
		sql.append(" left join organization o on o.id=udj.DEPT_ID");
		sql.append(" left join user_job uj on uj.ID=udj.JOB_ID");
		sql.append(" where u.ENABLE_FLAG='0' ");
		if (job_name != null) {
			//params.put("job_name", job_name);
			sql.append(" and ("+job_name+")");
		}
		if (organization != null) {
			params.put("orgLevel", organization.getOrgLevel()+"%");
			sql.append(" and udj.dept_id in (select id from organization where orgLevel like :orgLevel )");
		}

		sql.append(" order by o.orgLevel,o.orgOrder;");
		return organizationDao.findMapBySql(sql.toString(),params);

	}

	@Override
	public List getAllCampusForSelection(String orgType) {
		//查询所有的校区
		//修改接口 如果orgType为空则获取原来的全部分公司 如果orgType不为空 则获取当前登陆者所在的分公司
		Organization organization = null;
		if(StringUtil.isNotBlank(orgType)){
			String userId =userService.getCurrentLoginUser().getUserId();
			 organization  = userService.getBelongBranchByUserId(userId);
		}
		StringBuffer query = new StringBuffer();
		query.append(" select CONCAT(o.id,'') orgId,o.`name` orgName,CONCAT(oo.id,'') orgParentId, oo.`name` orgParentName from organization o ");
		query.append(" JOIN organization oo on oo.id = o.parentID WHERE o.orgType ='"+OrganizationType.CAMPUS+"' and o.boss_use=0 ");
		if(organization!=null){
			query.append(" and oo.id ='"+organization.getId()+"' ");
		}
		Map<String, Object> params = Maps.newHashMap();
		List list =organizationDao.findMapBySql(query.toString(),params);
		return list;
	}

	/**
	 * 转换组织架构
	 * @param campusByLoginUser
	 * @return
	 */
	@Override
	public List<List<OrganizationVo>> mappingOrganizationVo(List<List<Organization>> campusByLoginUser) {
		List<List<OrganizationVo>> result = new ArrayList<>();
		for (List<Organization> list : campusByLoginUser){
			List<OrganizationVo> organizationVos = new ArrayList<>();
			for (Organization o : list){
				OrganizationVo organizationVo = HibernateUtils.voObjectMapping(o, OrganizationVo.class);
				organizationVos.add(organizationVo);
			}
			result.add(organizationVos);
		}
		return result;

	}

	@Override
	public List<OrganizationVo> getOrganizationByRegionId(String provinceId, String cityId) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select * from organization ");

		if (StringUtils.isNotBlank(provinceId)||StringUtils.isNotBlank(cityId)){
			sql.append(" where 1=1 ");
			if (StringUtils.isNotBlank(provinceId)){
				sql.append(" and province_id= :provinceId ");
				params.put("provinceId", provinceId);
			}
			if (StringUtils.isNotBlank(cityId)){
				sql.append(" and city_id =:cityId ");
				params.put("cityId", cityId);
			}
		}else {
			sql.append(" where 1=2");
		}
		sql.append(" and orgType='CAMPUS' AND address IS NOT NULL AND lat IS NOT NULL AND lon IS NOT NULL ");
		List<Organization> list = organizationDao.findBySql(sql.toString(), params);
		List<OrganizationVo> organizationVos = HibernateUtils.voListMapping(list, OrganizationVo.class);
		return organizationVos;
	}

	@Override
	public List<List<Organization>> getCampusByLoginUserNew() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from organization where orgType='CAMPUS' and boss_use = 0 ");
		User user= userService.getCurrentLoginUser();
		sql.append(" and (");
		int i =0;
		for(Organization org:user.getOrganization()){
			if(i>0){
				sql.append(" or ");
			}
			sql.append(" orgLevel like '"+org.getOrgLevel()+"%'");
			i++;
		}
		sql.append("  )");
		Map map =new HashMap<String, Object>();
		return getParentOrganizationAppendOrg(organizationDao.findBySql(sql.toString(), map));
	}

    @Override
    public int sendVerifyCode(String contact, String phoneNum) {
		User user = userService.getCurrentLoginUser();
		SystemVerifyCode code = new SystemVerifyCode();
		code.setPhoneNum(phoneNum);
		code.setStatus(0);
		code.setVerifyCode(String.valueOf((int)((Math.random()*9+1)*100000)));
		if(user!=null){
			code.setCreateUser(user.getUserId());
			code.setModifyUser(user.getUserId());
		}
		systemVerifyCodeDao.save(code);
		if(!AliyunSmsUtil.sendSms(PropertiesUtils.getStringValue("SIGN_NAME"),"SMS_75815039",phoneNum,"{\"verification\":\""+code.getVerifyCode()+"\"}"))
		{
			throw new ApplicationException("发送验证码失败,请稍后再试");
		}
        return code.getId();
    }

	@Override
	public Response verifyUserCode(String customerId, String verifyCode, int id) {
		SystemVerifyCode code=systemVerifyCodeDao.findById(id);
		if(code.getStatus()!=0)
		{
			return new Response(-1,"验证码已经失效，请重新发送！");
		}

		if(StringUtils.isNotBlank(verifyCode) && verifyCode.equals(code.getVerifyCode())){
			code.setStatus(1);
			if(StringUtils.isNotBlank(customerId)) {
				Customer cus = customerDao.findById(customerId);
				if (!cus.getContact().equals(code.getPhoneNum())) {
					cus.setContact(code.getPhoneNum());
				}
			}
			return new Response();
		}

		return new Response(-1,"验证失败！");
	}
	
	@Override
    public String getVerification(HttpServletRequest request, String phone) {
        String verification = "" + (int)((Math.random()*9+1)*100000);
        if (JedisUtil.exists(Constants.REDIS_PRE_VERIFICATION_TIME_LIMIT_KEY  + request.getSession().getId() + phone)) {
            throw new ApplicationException(ErrorCode.BAD_REQUEST, "请不要重复请求");
        }
        if (AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_75815039", phone, 
                "{\"verification\":\""+  verification + "\"}")) {
            JedisUtil.setex(Constants.REDIS_PRE_VERIFICATION_TIME_LIMIT_KEY + request.getSession().getId() + phone, 60, verification);
            JedisUtil.setex(Constants.REDIS_PRE_VERIFICATION_INVALID_KEY + request.getSession().getId() + phone, 60 * 10, verification);
            return verification;
        } else {
            throw new ApplicationException(ErrorCode.SYSTEM_ERROR);
        }
    }

	public List<List<Organization>> getParentOrganizationAppendOrg(List<Organization> orgList){
		List<List<Organization>> returnList = new ArrayList<>();
		Map<String,List<Organization>> map = new HashMap<>();
		for(Iterator it =orgList.iterator(); it.hasNext();){
			List<Organization> singleOrg= new ArrayList<>();
			Organization org= (Organization) it.next();
			if(map.get(org.getParentId())==null){
				Organization parentOrg= organizationDao.findById(org.getParentId());
				singleOrg.add(parentOrg);
			}else{
				singleOrg=map.get(org.getParentId());
			}
			singleOrg.add(org);
			map.put(org.getParentId(),singleOrg);
		}

		for(String key:map.keySet()){
			returnList.add(map.get(key));
		}

		return returnList;
	}

	@Override
	public List<Organization> loadOrganizatonByOrgIdAndType(String orgType, String orgId) {		
		if(StringUtil.isBlank(orgId)){
			Organization organization = userService.getBelongBranch();
			if(organization!=null){
				String sql =" select * from organization WHERE orgType ='"+orgType+"' and orgLevel LIKE '"+organization.getOrgLevel()+"%' ";				
				return organizationDao.findBySql(sql, Maps.newHashMap());
			}
		}		
		return new ArrayList<Organization>();
	}

	@Override
	public Response judgeUserIdentity(String userId) {
		// TODO Auto-generated method stub
		//User currentUser = userService.getCurrentLoginUser();
		//String userId = currentUser.getUserId();
		Boolean isZXS = userService.isUserRoleCode(userId, RoleCode.CONSULTOR);//是否咨询师
		Boolean isCOD = userService.isUserRoleCode(userId, RoleCode.CAMPUS_OPERATION_DIRECTOR);//是否校区营运主任
		Boolean isCD = userService.isUserRoleCode(userId, RoleCode.CONSULTOR_DIRECTOR);//是否咨询主管
		Boolean isXQZR = userService.isUserRoleCode(userId, RoleCode.CAMPUS_DIRECTOR);//是否校区主任
		//#1935
		Boolean isSCJL = userService.isUserRoleCode(userId, RoleCode.BREND_MERKETING_DIRECTOR);//是否市场经理
		Boolean isWHZG = userService.isUserRoleCode(userId, RoleCode.OUTCALL_MANAGER);//是否外呼主管
		Boolean isWLZG = userService.isUserRoleCode(userId, RoleCode.NETWORK_MANAGER);//是否网络主管
		
		String roleSign = userService.getUserRoleSign(userId);
		Boolean judgeZXS = false;
		Boolean judgeXQZR = false;
		Response response = new Response();
		//判断是否是咨询师或者营主
		if(isZXS||isCD||RoleSign.ZXS.getValue().equalsIgnoreCase(roleSign)
						||RoleSign.ZXZG.getValue().equalsIgnoreCase(roleSign)
						) {
			judgeZXS = true;
		}
		
		if(isCOD||isXQZR||isSCJL||isWHZG||isWLZG||RoleSign.XQZR.getValue().equalsIgnoreCase(roleSign)
				||RoleSign.XQYYFZR.getValue().equalsIgnoreCase(roleSign)
				||RoleSign.YYZR.getValue().equalsIgnoreCase(roleSign)
				||RoleSign.WHZG.getValue().equalsIgnoreCase(roleSign)
				||RoleSign.WLZG.getValue().equalsIgnoreCase(roleSign)
				||RoleSign.SCJL.getValue().equalsIgnoreCase(roleSign)
				) {
			judgeXQZR = true;
		}
		Map<String,Object> map = new HashMap<>();
		if(judgeZXS&&judgeXQZR) {
			map.put("code", 0);
			map.put("message", "营主和咨询师");
		}else if(judgeZXS){
			map.put("code", 1);
			map.put("message", "咨询师");
		}else if(judgeXQZR){
			map.put("code", 2);
			map.put("message", "营主");
		}else {
			map.put("code", 3);
			map.put("message", "没有权限");
		}
		response.setData(map);
		return response;
	}
	
	
	
	
	
	

}
