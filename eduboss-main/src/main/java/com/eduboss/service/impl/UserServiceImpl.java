/**
 * 
 */
package com.eduboss.service.impl;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.eduboss.common.*;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.*;
import com.eduboss.dto.*;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.*;
import com.eduboss.task.SendUserPassWordMsgThread;
import com.eduboss.service.*;
import com.eduboss.utils.*;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import net.sf.json.JSONObject;

import org.apache.commons.lang.enums.Enum;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.aspectj.util.FileUtil;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SparkUserDetailVo;
import com.eduboss.dto.SparkUserVo;
import com.eduboss.dto.UserMobileVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.task.SendUserPassWordMsgThread;
import com.google.common.collect.Maps;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author lmj
 */

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private final static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDao userDao;

	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private RoleResourceDao roleResourceDao;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private UserOrganizationDao userOrganizationDao;

	@Autowired
	private UserOrganizationRoleDao userOrganizationRoleDao;

	@Autowired
	private UserModifyLogDao userModifyLogDao;
	
	@Autowired
	private CourseSummaryDao courseSummaryDao;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private MobileUserService mobileUserService;
	
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private MobileOrganizationDao mobileOrganizationDao;
	
	@Autowired
	private MyCollectionService myCollectionService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private MailUserViewDao mailUserViewDao;
	
	@Autowired
	private UserOrgDao userOrgDao;
	
	@Autowired
	private StudentSchoolDao studentSchoolDao;
	
	@Autowired
	private RefundWorkflowService refundWorkflowService;

	@Autowired
	private UserJobService userJobService;
	
	@Autowired
	private ChangeUserRoleRecordDao changeUserRoleRecordDao;

	@Autowired
	private UserTeacherAttributeDao userTeacherAttributeDao;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
    private SystemConfigService systemConfigService;


	@Autowired
	private OrganizationHrmsDao organizationHrmsDao;

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private DistributableRoleService distributableRoleService;

	/**
	 * 判断当前用户是否是指定的角色（职位）
	 * @param roleCode
	 * @return
	 */
	public boolean isCurrentUserRoleCode(RoleCode roleCode){
		if(roleCode!=null){
			List<UserOrganizationRole> uors =getUserOrganizationRoleByUserId(getCurrentLoginUserId());
				for(UserOrganizationRole uor : uors){
					if(uor.getRole().getRoleCode()!=null && roleCode.equals(uor.getRole().getRoleCode())){
						return true;
					}
				}
		}
		return false;
	}

    /**
     * 判断当前用户是否是指定的角色（职位）
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean isCurrentUserRoleId(String roleId) {
        if(StringUtils.isNotBlank(roleId)){
            User user =getCurrentLoginUser();
            if(user!=null && user.getRole()!=null){
                for(Role role : user.getRole()){
                    if(roleId.equals(role.getRoleId())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
	 * 判断用户是否是指定的角色（职位）
	 * @param userId
	 * @param roleCode
	 * @return
	 */
	public boolean isUserRoleCode(String userId,RoleCode roleCode){
		if(roleCode!=null && StringUtils.isNotEmpty(userId)){
			List<Role> roles = getRoleByUserId(userId);
			if(roles!=null && roles.size()>0){
				for(Role role : roles){
					if(roleCode.equals(role.getRoleCode())){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public List<User> getStaffByRoldIdAndOrgId(String roleId, String orgId, boolean isIncludeChildLevel, int limit) throws ApplicationException {
		String orgLevel = "";
		if (isIncludeChildLevel && StringUtils.isNotBlank(orgId)) {
			Organization org = organizationDao.findById(orgId);
			if (org != null) {
				orgLevel = org.getOrgLevel();
			}
		}
		if(CheckSystemUtils.checkNewOrg()) {
			return userDao.getStaffByRoleIdAndOrgIdNew(roleId, orgId, orgLevel, isIncludeChildLevel, limit);
		}else{
			return userDao.getStaffByRoleIdAndOrgId(roleId, orgId, orgLevel, isIncludeChildLevel, limit);
		}
	}
	
	@Override
	public List<User> getStaffByRoldCodeAndOrgId(String roleCode, String orgId, boolean isIncludeChildLevel, int limit)
			throws ApplicationException {
		Organization org=organizationDao.findById(orgId);
		if(org!=null)
			if(CheckSystemUtils.checkNewOrg()) {
				return userDao.getStaffByRoldCodeAndOrgIdNew(roleCode, orgId, org.getOrgLevel(), isIncludeChildLevel, limit);
			}else{
				return userDao.getStaffByRoldCodeAndOrgId(roleCode, orgId, org.getOrgLevel(), isIncludeChildLevel, limit);
			}
		else
			return null;
	}
	

	/**
	 * 根据类型和组织架构查询用户
	 *@param workType 用户类型
	 *@param orgId 组织架构id
	 *@param
	 *@param isIncludeChildLevel 是否查询 组织架构下的用户
	 *@return 用户列表
	 */
	public List<UserVo> getUserByWorkTypeAndOrgId(UserWorkType workType, String orgId, boolean isIncludeChildLevel)
			throws ApplicationException {
		Organization org=null;
		if(StringUtils.isNotBlank(orgId)){
			org=organizationDao.findById(orgId);
		}
		List<User> list=userDao.getUserByWorkTypeAndOrgId( workType,orgId,  org == null ? null : org.getOrgLevel(),  isIncludeChildLevel);
		return HibernateUtils.voListMapping(list, UserVo.class);
	}
	
	@Override
	public List<User> getStaffByNameAndRoleCodeAndOrgId(String name, String roleCode, String orgId, boolean isIncludeChildLevel, int limit) {
		Organization org=organizationDao.findById(orgId);
		if(org!=null) {
			if(CheckSystemUtils.checkNewOrg()){
				return userDao.getStaffByNameAndRoleCodeAndOrgIdNew(name, roleCode, orgId, org.getOrgLevel(),  isIncludeChildLevel, limit);
			}else{
				return userDao.getStaffByNameAndRoleCodeAndOrgId(name, roleCode, orgId, org.getOrgLevel(),  isIncludeChildLevel, limit);
			}
		} else {
			return null;
		}
	}


	@Override
	public User getUserById(String userId) throws ApplicationException {
		User user=new User();
		boolean inputUserId = true; //标记是否有传userId true代表有传 false代表没有传
		if(StringUtils.isEmpty(userId)){
			inputUserId = false;
			user = getCurrentLoginUser();
			userId=user.getUserId();
		}else{
			//user_organization FetchType.LAZY 
			 user = userDao.findById(userId);
        	 List<Organization> orgList = organizationDao.findOrganizationByUserIdOld(user.getUserId());//这里用于旧数据修改，暂时用旧的组织架构
        	 user.setOrganization(orgList);
        	 
		}			
		if (user != null) {
			List<Role> roleByUserId = userRoleDao.findRoleByUserId(user.getUserId());//暂时走旧的，新的获取用户信息有新接口
			if (!inputUserId){
				//没有传userId 当前user是currentLoginUser
				if (currentLoginUserIsReceptionistMode(roleByUserId)){
					List<Role> reception = roleDao.findRoleByCode(RoleCode.RECEPTIONIST);
					List<Role> base = roleDao.findRoleByName("系统基本权限");
					reception.addAll(base);
					user.setRole(reception);
				}else {
					user.setRole(roleByUserId);
				}
			}else {
				user.setRole(roleByUserId);
			}

//			user.setOrganization(organizationDao.findOrganizationByUserId(user.getUserId()));
//            userDao.getHibernateTemplate().initialize(user.getOrganization());
			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
			List<UserJob> jobs = new ArrayList<UserJob>();
			List<Organization> depts = new ArrayList<Organization>();
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
				
				UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
				Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
				
				if(userDeptJob2.getIsMajorRole()==0){
					if (dept!=null){
						user.setDeptId(dept.getId());
						user.setDeptName(dept.getName());
					}

					if(userJob != null){//sat和测试生产库的老师jobId不同，加个null判断
						user.setJobId(userJob.getId());
						user.setJobName(userJob.getJobName());
					}
					
				}else{
					jobs.add(userJob);
					depts.add(dept);
				}
			}
			user.setDept(depts);
			user.setJobs(jobs);
		}
		return user;
	}

	/**
	 * 判断当前用户是不是前台模式
	 * @return
	 */
	@Override
	public boolean currentLoginUserIsReceptionistMode(List<Role> roleByUserId) {
		boolean flagReceptionRole = false; //标记有没有前台角色
		for (Role role : roleByUserId){
			if (hasReceptionist(role)){
				flagReceptionRole = true;
				break;
			}
		}
		/**
		 * 有前台角色
		 */
		if (flagReceptionRole){
			if (SecurityContextHolder.getContext()!=null&&SecurityContextHolder.getContext().getAuthentication()!=null){
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				User user = ((UserDetailsImpl)principal).getUser();
				if (user.getRole().size()==2){
					//当前角色只有两个就是前台模式
					return true;
				}else {
					return false;//普通模式
				}
			}else {
				return false;
			}
		}else {
			return false;
		}

	}

	@Override
	public String findUserNameById(String userId) {
		User user=userDao.findById(userId);
		if (user!=null){
			return user.getName();
		}else {
			return "";
		}
	}

	@Override
	public User findUserById(String userId) throws ApplicationException {
		User user=userDao.findById(userId);
		if (user != null) {
			user.setRole(getRoleByUserId(user.getUserId()));
			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
			List<UserJob> jobs = new ArrayList<UserJob>();
			List<Organization> depts = new ArrayList<Organization>();
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
				
				UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
				Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
				
				if(userDeptJob2.getIsMajorRole()==0 && dept!=null && userJob!=null){
					user.setDeptId(dept.getId());
					user.setDeptName(dept.getName());
					user.setJobId(userJob.getId());
					user.setJobName(userJob.getJobName());
				}else{
					jobs.add(userJob);
					depts.add(dept);
				}
			}
			user.setDept(depts);
			user.setJobs(jobs);
		}
		return user;
	}
	
	
	@Override
	public User getUserInfoById(String userId) throws ApplicationException {
		User user=new User();
		//通过用户ID获取用户
		user = userDao.findById(userId);			
		//获取用户的所在的组织架构及职位
		if (user != null) {
			UserDeptJob userDeptJob = userDeptJobDao.findDeptJobByParam(user.getUserId(), 0);
			if(userDeptJob == null)
				return user;
			else
			{
				UserJob userJob=userJobDao.findById(userDeptJob.getJobId());
				Organization dept=organizationDao.findById(userDeptJob.getDeptId());
				
				user.setDeptId(dept.getId());
				user.setDeptName(dept.getName());
				user.setJobId(userJob.getId());
				user.setJobName(userJob.getJobName());
			}
			
		}
		return user;
	}


	@Override
	public User getUserByUserName(String userName) throws ApplicationException {
		Criterion userCriterion = Expression.eq("account",userName);
	    List<User> userList = userDao.findByCriteria(userCriterion);
	    User user = null;
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
		}
		
		return user;
	}

	@Override
	public User getUserByEmployeeNo(String employeeNo) throws ApplicationException {
		Criterion userCriterion = Expression.eq("employeeNo",employeeNo);
		List<User> userList = userDao.findByCriteria(userCriterion);
		User user = null;
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
		}

		return user;
	}

	public User getUserByName(String userName) throws ApplicationException {
		Criterion userCriterion = Expression.eq("name",userName);
	    List<User> userList = userDao.findByCriteria(userCriterion);
	    User user = null;
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
		}
		
		return user;
	}

	@Override
	public DataPackage getUserList(User user, DataPackage dp)
			throws ApplicationException {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User u where 1=1 ";
		if(StringUtils.isNotEmpty(user.getName())){
			hql+=" and u.name like :name ";
			params.put("name", "%" + user.getName() + "%");
		}
		if(StringUtils.isNotEmpty(user.getAccount())){
			hql+=" and u.account like :account ";
			params.put("account", "%" + user.getAccount() + "%");
		}
		if(user.getEnableFlg() != null){
			hql+=" and u.enableFlg = :enableFlg ";
			params.put("enableFlg", user.getEnableFlg());
		}
		if(user.getWorkType() != null){
			hql+=" and u.workType = :workType ";
			params.put("workType", user.getWorkType());
		}
		if(StringUtils.isNotEmpty(user.getSex())){
			hql+=" and u.sex = :sex ";
			params.put("sex", user.getSex());
		}
		if(StringUtils.isNotEmpty(user.getContact())){
			hql+=" and u.contact like :contact ";
			params.put("contact", user.getContact() + "%");
		}
		if(StringUtils.isNotEmpty(user.getRoleId())){
			hql+=" and u.userId in (select userId from UserRole where roleId = '"+user.getRoleId()+"') ";
		}
		
		if(StringUtils.isNotEmpty(user.getJobId())){
			hql+=" and u.userId in (select userId from UserDeptJob where jobId = '"+user.getJobId()+"') ";
		}
		
		if(StringUtils.isNotEmpty(user.getDeptId())){
			hql+=" and u.userId in (select userId from UserDeptJob where deptId = '"+user.getDeptId()+"') ";
		}
		
		if(StringUtils.isNotEmpty(user.getOrganizationId())){
			Organization org=organizationDao.findById(user.getOrganizationId());
			if(org != null)
				hql+=" and u.organizationId in (select id from Organization where  orgLevel like '"+org.getOrgLevel()+"%') ";
		}
		
        //如果不是超级管理员，员工管理只能查看本分公司的员工
        User usr =  getCurrentLoginUser();
		if(usr.getRoleCode().contains(RoleCode.SUPER_ADMIN.toString())){//.equals(usr.getRoleCode())
		}else {
			if(usr.getRoleCode().contains(RoleCode.CAMPUS_DIRECTOR.toString())){//校区主任
				hql+=" and  u.userId in(select userId from UserRole where roleId in" +
						" (select roleId from Role where roleCode ='"+ RoleCode.TEATCHER+"' ) " +
						") ";
			}
			Organization brench =getBelongBranch();//分公司
			hql+=" and organizationId in (select id from Organization where orgLevel like '"+brench.getOrgLevel()+"%' )";
		}
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by "+dp.getSidx()+" "+dp.getSord();
		} 
		
		dp=userDao.findPageByHQL(hql, dp, true, params);
        for (Object userObject : dp.getDatas()) {
        	User userDb = (User)userObject;
        	userDb.setRole(userRoleDao.findRoleByUserId(userDb.getUserId()));
        	//得到userorganization
        	List<Organization> orgList=getUserOrganizationList(userDb.getUserId());//旧数据用这个findOrganizationByUserId
        	userDb.setOrganization(orgList);
    		List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(userDb.getUserId());
        	String deptName="";
        	String jobName="";
        	for ( UserDeptJob userDeptJob2 : userDeptJob) {
				UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
				Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
				if(dept!=null)
				deptName+=dept.getName()+",";
				if(userJob!=null)
				jobName+=userJob.getJobName()+",";
			}
        	if(StringUtils.isNotBlank(jobName)){
        		userDb.setJobName(jobName.substring(0,jobName.length()-1));
        	}
        	if(StringUtils.isNotBlank(deptName)){
        		userDb.setDeptName(deptName.substring(0,deptName.length()-1));
        	}
        }
		return dp;
	}

	@Override
	public void saveOrUpdateUserForAdvance(User user) throws ApplicationException  {
    //如果表单提交过来的“USE_ALL_ORGANIZATION”为空字符串，不是存到表中。否则在本周课表时截取字符串会报字符串越界。
		if(StringUtils.isBlank(user.getUseAllOrganization())){
			user.setUseAllOrganization(null);
		}
		User userInDb = null;
		if(user.getUserId()!= null) {
			userInDb = userDao.findById(user.getUserId());
		}
		User u =  getCurrentLoginUser();
		user.setModifyTime(DateTools.getCurrentDateTime());
		user.setModifyUserId(u.getUserId());

		if(userInDb != null){
			userDao.save(userInDb);
			user.setPassword(userInDb.getPassword());
			user.setTeacherSubjectStatus(userInDb.getTeacherSubjectStatus());
			userDao.merge(user);

		}else{
			user.setUserId(null);
			user.setEnableFlg(0);
			user.setCreateTime(DateTools.getCurrentDateTime());
			user.setCreateUserId(u.getUserId());

			if(StringUtils.isEmpty(user.getPassword())){
				user.setPassword(getRandomString(6));//
			}

			/**
			 * 判断登陆账号存不存在
			 */
			if(existAccountInDB(user.getAccount())){
				throw  new  DataIntegrityViolationException("登录账号已存在！");
			}

			userDao.save(user);
			//  2016/8/8 短信通知
			String userContact = user.getContact();
			if (StringUtils.isBlank(userContact)){
				throw new ApplicationException("用户手机号码为空");
			}else {
				notifyUserNewPSW(user);
			}
		}

		if(StringUtils.isNotBlank(user.getRoleIds())) {
			userRoleDao.marginUserRoleList(user.getUserId(), user.getRoleIds());
			//把没有权限的页面收藏删除----begin
			List<Resource> resource = resourceDao.findResourceByRoleIds(user.getRoleIds());
			myCollectionService.deleteNotInNewResources(user.getUserId(), resource);
			//把没有权限的页面收藏删除----end
		}
		if (StringUtils.isNotBlank(user.getOrgIds())) {
			userOrganizationDao.marginUserOrgList(user.getUserId(), user.getOrgIds());
		}

		//BOSS保存用户不再维护部门职位，2017-01-04
		if (StringUtils.isNotBlank(user.getJobIds()) && StringUtils.isNotBlank(user.getDeptIds())) {
			try {
				userDeptJobDao.marginUserDeptJobList(user.getUserId(), user.getDeptIds(), user.getJobIds());
			} catch (DuplicateKeyException e) {
				throw new ApplicationException("角色部门重复，请去掉一组再保存");
			}
		}
	}

	@Override
	public void saveOrUpdateUser(User user) throws ApplicationException {
		//如果表单提交过来的“USE_ALL_ORGANIZATION”为空字符串，不是存到表中。否则在本周课表时截取字符串会报字符串越界。
		if(StringUtils.isBlank(user.getUseAllOrganization())){
			user.setUseAllOrganization(null);
		}
		User userInDb = null;
		if(user.getUserId()!= null) {
			userInDb = userDao.findById(user.getUserId());
		}
		User u =  getCurrentLoginUser();
		user.setModifyTime(DateTools.getCurrentDateTime());
		user.setModifyUserId(u.getUserId());
		
		if(userInDb != null){
			userDao.save(userInDb);
			user.setPassword(userInDb.getPassword());
			user.setTeacherSubjectStatus(userInDb.getTeacherSubjectStatus());
			userDao.merge(user);
			
		}else{
			user.setUserId(null);
			user.setEnableFlg(0);
			user.setCreateTime(DateTools.getCurrentDateTime());
			user.setCreateUserId(u.getUserId());

			if(StringUtils.isEmpty(user.getPassword())){
				user.setPassword(getRandomString(6));//
			}

			/**
			 * 判断登陆账号存不存在
			 */
			if(existAccountInDB(user.getAccount())){
				throw  new  DataIntegrityViolationException("登录账号已存在！");
			}

			userDao.save(user);
			//  2016/8/8 短信通知
			String userContact = user.getContact();
			if (StringUtils.isBlank(userContact)){
				throw new ApplicationException("用户手机号码为空");
			}else {
				notifyUserNewPSW(user);
			}
		}
		
		if(StringUtils.isNotBlank(user.getRoleIds())) {
			userRoleDao.marginUserRoleList(user.getUserId(), user.getRoleIds());
			//把没有权限的页面收藏删除----begin
			List<Resource> resource = resourceDao.findResourceByRoleIds(user.getRoleIds());
			myCollectionService.deleteNotInNewResources(user.getUserId(), resource);
			//把没有权限的页面收藏删除----end
		}
		if (StringUtils.isNotBlank(user.getOrgIds())) {
			userOrganizationDao.marginUserOrgList(user.getUserId(), user.getOrgIds());
		}
		
		//BOSS保存用户不再维护部门职位，2017-01-04
		if (StringUtils.isNotBlank(user.getJobIds()) && StringUtils.isNotBlank(user.getDeptIds())) {
			try {
				userDeptJobDao.marginUserDeptJobList(user.getUserId(), user.getDeptIds(), user.getJobIds());
			} catch (DuplicateKeyException e) {
				throw new ApplicationException("角色部门重复，请去掉一组再保存");
			}
		}
	}
	
	

	/**
	 *判断登陆账号存不存在
	 * @param account
	 * @return
	 */
	private boolean existAccountInDB(String account) {
		Map<String, Object> params = new HashMap<String, Object>();
		String countSql = "select count(*) from user where ACCOUNT = :account ";
		params.put("account", account);
		int count = userDao.findCountSql(countSql, params);
		return count>0;
	}

	/**
	 * 随机密码
	 * @param length
	 * @return
	 */
	private String getRandomString(int length){
		String str="abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for(int i = 0 ; i < length; ++i){
			int number = random.nextInt(54);//[0,54)上面字符串的长度

			sb.append(str.charAt(number));
		}
		return sb.toString();
	}




	@Override
	public void deleteUsers(String userIds) throws ApplicationException {
		String[] idArray = userIds.split(",");
		for (String id : idArray) {
			User target = getUserById(id);
			if(target != null){
				for(Role role : target.getRole()){
					role.getUsers().remove(target);
				}
				for(Organization org : target.getOrganization()){
					org.getUsers().remove(target);
				}
				userDao.delete(target);
			}
		}
	}
	
	@Override
	public void createUserMailAddr(String userId, String mailAddr) {
		if(StringUtils.isEmpty(userId)) {
			throw new ApplicationException(ErrorCode.USER_ID_EMPTY);
		}
		User user = userDao.findById(userId);
		if (user!=null) {
			user.setMailAddr(mailAddr);
			user.setMailStatus(0); //首次默认为开启
			user.setModifyTime(DateTools.getCurrentDateTime());
		} else {
			throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
		}		
		
	}
	
	/**
	 * 根据邮箱地址查找用户
	 */
	public User findUserByMailAddr(String mailAddr) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User u where mailAddr = :mailAddr ";
		params.put("mailAddr", mailAddr);
		List<User> list = userDao.findAllByHQL(hql, params);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public void setUserMailStatus(String userId, Integer status) {
		if(StringUtils.isEmpty(userId)) {
			throw new ApplicationException(ErrorCode.USER_ID_EMPTY);
		}
		User user = userDao.findById(userId);
		if (user!=null) {
			user.setMailStatus(status); 
			user.setModifyTime(DateTools.getCurrentDateTime());
		} else {
			throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
		}
	}

	@Override
	public DataPackage getRoleList(Role role, DataPackage dp)
			throws ApplicationException {
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(role);
		User usr =  getCurrentLoginUser();
		if(!RoleCode.SUPER_ADMIN.toString().equals(usr.getRoleCode())){
			criterionList.add(Expression.ge("roleLevel", usr.getRoleLevel()));
		}	
		List<Order> orderList =  HibernateUtils.prepareOrder(dp, "id", "asc");
		orderList.add(Order.asc("id"));
		dp = roleDao.findPageByCriteria(dp, orderList, criterionList);
		for (Object userObject : dp.getDatas()) {
			Role roleDb = (Role)userObject;

			if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
				roleDb.setUserAmount(userOrganizationRoleDao.findCountHql(" select count(*) from UserOrganizationRole where role.id='" + roleDb.getRoleId() + "'", new HashMap<String, Object>()));
				roleDb.setRealCount(userRoleDao.findCountSql("select count(*) from user u ,user_organization_role ur where u.USER_ID=ur.user_ID and ur.role_ID='" + roleDb.getRoleId() + "' and u.enable_flag='0'", new HashMap<String, Object>()));
			}else{
					roleDb.setUserAmount(userRoleDao.findCountHql(" select count(*) from UserRole where roleId='" + roleDb.getRoleId() + "'", new HashMap<String, Object>()));
					roleDb.setRealCount(userRoleDao.findCountSql("select count(*) from user u ,user_role ur where u.USER_ID=ur.userID and ur.roleID='" + roleDb.getRoleId() + "' and u.enable_flag='0'", new HashMap<String, Object>()));
				}
			}
		return dp;
	}
	
	@Override
	public DataPackage getRoleListForSelection(Role role, DataPackage dp) {
		StringBuffer sql=new StringBuffer();
		User usr =  getCurrentLoginUser();
		sql.append(" select * from role where id <>'ROL0000000146'    ");		
		if(!RoleCode.SUPER_ADMIN.toString().equals(usr.getRoleCode())){
			sql.append(" and ROLE_LEVEL>='"+usr.getRoleLevel()+"'  ");	
		}
		//将系统基本权限放在最后
		//sql.append(" union all select * from role where id='ROL0000000146' ");
		dp =roleDao.findPageBySql(sql.toString(), dp, true, new HashMap<String, Object>());
		Role role_base = roleDao.findById("'ROL0000000146'");
		dp.getDatas().add(role_base);
		dp.setRowCount(dp.getRowCount()+1);		
		return dp;
	   // return roleDao.findPageBySql(sql.toString(), dp, true, new HashMap<String, Object>());

	}


	@Override
	public void saveOrUpdateRole(Role role) throws ApplicationException {
		Role roleInDb = null;
		List<Role> list=roleDao.findRoleByName(role.getName());
		if(StringUtils.isNotBlank(role.getRoleId())) {
			roleInDb = roleDao.findById(role.getRoleId());
			for(Role r:list){
				if(!r.getRoleId().equals(role.getRoleId())){
					throw new ApplicationException("角色名有重复");
				}
			}
		}else if(list.size()>0){
			throw new ApplicationException("角色名有重复");
		}
			
		if(roleInDb != null){
			roleDao.save(roleInDb);
			roleDao.merge(role);
		}else{
			role.setRoleSign(getUniqueRoleSign(role.getRoleSign(), null));
			roleDao.save(role);
		}
		
		if (StringUtils.isNotBlank(role.getSelectedResIds())) {
			roleResourceDao.marginRoleResourceList(role.getRoleId(), role.getSelectedResIds());
		}
		distributableRoleService.updateDistributableRoleList(role.getRoleId(), role.getRelateRoleIds());
	}
	
	private String getUniqueRoleSign(String roleSign, String roleId) {
		String newRoleSign = roleSign;
		if(StringUtils.isNotBlank(roleSign)) {  
			Map<String, Object> params = new HashMap<String, Object>();
			//查找是否存在同样的roleSign
			String hql = " from Role r where 1=1 and r.roleSign like :roleSign ";
			params.put("roleSign", roleSign + "%");
			if(StringUtils.isNotBlank(roleId)) {
				hql += " and r.roleId <> :roleId ";
				params.put("roleId", roleId);
			}
			List<Role> roleList = roleDao.findAllByHQL(hql, params);			
			if(roleList != null && roleList.size() > 0) {  //避免重复，重设roleSign
				int max=100;
		        int min=10;
		        boolean flag = true;
		        while(flag == true) {
		        	//roleSign加上一个两位数的随机数
		        	newRoleSign = roleSign + Integer.valueOf((new Random().nextInt(max - min) + min)).toString();
			        boolean isNew = true;
			        for(Role otherRole : roleList) {			        	
			        	if(newRoleSign.equalsIgnoreCase(otherRole.getRoleSign())) {
			        		isNew = false;
			        		break;
			        	}
			        }
			        if(isNew == true) flag = false;
			        else flag = true;
		        }
			}
		} 	
		return newRoleSign;
	}


	@Override
	public void deleteRoles(String roleIds) throws ApplicationException {
		String[] idArray = roleIds.split(",");
		for (String id : idArray) {
			Role target = roleDao.findById(id);
			//不能直接删除用户，如果有联系用户或组织架构，要求先移除关联关系
			if (target == null) throw new ApplicationException(ErrorCode.ROLE_NOT_FOUND);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("roleId", id);
			int countUser=0;
			if(CheckSystemUtils.checkNewOrg()){
				countUser = userRoleDao.findCountHql(" select count(*) from UserOrganizationRole where role.id = :roleId ", params);//如果有用户在用该角色就不能删除
			}else {
				 countUser = userRoleDao.findCountHql(" select count(*) from UserRole where roleId = :roleId ", params);//如果有用户在用该角色就不能删除
			}
			if (countUser > 0) throw new ApplicationException(ErrorCode.HAS_USER_FOR_DELETE);
			
			//断开数据库关联并删除
			roleDao.clear();
			StringBuffer sql=new StringBuffer();
			sql.append(" delete from system_notice_role where role_id = :roleId ");
			roleDao.excuteSql(sql.toString(), params);
			roleDao.delete(new Role(id));
		}
	}


	@Override
	public DataPackage getOrganizationList(Organization org, DataPackage dp)
			throws ApplicationException {
		org.setBossUse(0);//只要勾选的。
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(org);
		dp= organizationDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "id", "asc"), criterionList);
		return dp;
	}

	public void checkSaveOrUpdateOrganization(Organization org){
		int count=organizationDao.getOrganizationCountByOrganizationType(org.getOrgType(), org.getId());
		if(OrganizationType.CAMPUS.equals(org.getOrgType()) && count>=CommonUtil.ALLOW_MAX_CAMPUS){
			throw new ApplicationException("对不起，最多只允许添加"+CommonUtil.ALLOW_MAX_CAMPUS+"个"+OrganizationType.CAMPUS.getName());
		}else if(OrganizationType.BRENCH.equals(org.getOrgType()) && count>=CommonUtil.ALLOW_MAX_BRENCH){
			throw new ApplicationException("对不起，最多只允许添加"+CommonUtil.ALLOW_MAX_BRENCH+"个"+OrganizationType.BRENCH.getName());
		}else if(OrganizationType.GROUNP.equals(org.getOrgType()) && count>=CommonUtil.ALLOW_MAX_GROUNP){
			throw new ApplicationException("对不起，最多只允许添加"+CommonUtil.ALLOW_MAX_GROUNP+"个"+OrganizationType.GROUNP.getName());
		}
	}
	
	@Override
	public void saveOrUpdateOrganization(Organization org)
			throws ApplicationException {
		checkSaveOrUpdateOrganization(org);
		Organization orgInDb = null;
		if(org.getId()!= null)
			orgInDb = organizationDao.findById(org.getId());
		if (org!=null&&org.getProvince()!=null&&StringUtil.isNotBlank(org.getProvince().getId())){
			org.setProvinceId(org.getProvince().getId());
		}

		if (org!=null&&org.getCity()!=null&&StringUtil.isNotBlank(org.getCity().getId())){
			org.setCityId(org.getCity().getId());
		}
		if(orgInDb != null){
			//设置组织架构层级
			org.setOrgLevel(getOrgLevelString(org));
			boolean isUpdateCampus = orgInDb.getOrgType() == OrganizationType.CAMPUS 
			        && (!org.getLat().equals(orgInDb.getLat()) || !org.getLon().equals(orgInDb.getLon()));
			organizationDao.save(orgInDb);
			organizationDao.merge(org);
			if (isUpdateCampus) {
			    organizationService.saveOrUpdateSchoolArea(org);
			}
		} else {
			//设置orgSign
			org.setOrgSign(getUniqueOrgSign(org.getOrgSign()));		
			//设置组织架构层级
			org.setOrgLevel(getOrgLevelString(org));
			//保存记录
			organizationDao.save(org);
		}
	}
	
	public String getUniqueOrgSign(String orgSign) {
		logger.info("uniqueOrgSign--orgSign:" + orgSign);
		String newOrgSign = orgSign;
		if(StringUtils.isNotBlank(orgSign)) {  
			//查找是否存在同样的orgSign
			Map<String, Object> params = new HashMap<String, Object>();
			String hql = " from Organization o where 1=1 and o.orgSign like :orgSign ";
			params.put("orgSign", orgSign + "%");
			List<Organization> orgList = organizationDao.findAllByHQL(hql, params);				
			if(orgList != null && orgList.size() > 0) {  //避免重复，重设orgSign
				int max=100;
		        int min=10;
		        boolean flag = true;
		        while(flag == true) {
		        	//orgSign加上一个两位数的随机数
		        	newOrgSign = orgSign + Integer.valueOf((new Random().nextInt(max - min) + min)).toString();
			        boolean isNew = true;
			        for(Organization otherOrg : orgList) {			        	
			        	if(newOrgSign.equalsIgnoreCase(otherOrg.getOrgSign())) {
			        		isNew = false;
			        		break;
			        	}
			        }
			        if(isNew == true) flag = false;
			        else flag = true;
		        }
			}
		} 	
		logger.info("uniqueOrgSign--newOrgSign:" + newOrgSign);
		return newOrgSign;
	}
	
	//如果新父节点下没有本id，则是新添加到该父节点的组织，要生成新的orgLevel，否则不用
	private String getOrgLevelString(Organization org) {
		String orgLevelString = "0001";
		if (StringUtils.isNotBlank(org.getParentId()) && !"-1".equalsIgnoreCase(org.getParentId())) {
			List<Organization> organizations = organizationDao.findByCriteria(Expression.eq("parentId", org.getParentId()), Expression.eq("id", org.getId()));
			if (organizations.size() > 0 && StringUtils.isNotBlank(organizations.get(0).getOrgLevel())) {//本来已存在于父节点中，不用修改层次关系
				orgLevelString = organizations.get(0).getOrgLevel();
			} else {//不存在于父节点中，要修改自身层次关系
				Organization parentOrganization = organizationDao.findById(org.getParentId());
				if (StringUtils.isNotBlank(parentOrganization.getOrgLevel())) {
					orgLevelString = organizationDao.getNextOrgLevel(parentOrganization.getId(), parentOrganization.getOrgLevel());
				}
			}
		}else{
			List<Organization> organizations = organizationDao.findByCriteria(Expression.eq("parentId", org.getParentId()), Expression.eq("id", org.getId()));
			if (organizations.size() > 0 && StringUtils.isNotBlank(organizations.get(0).getOrgLevel())) {//本来已存在于父节点中，不用修改层次关系
				orgLevelString = organizations.get(0).getOrgLevel();
			} else {
				Organization organization = organizationDao.getOrganizationFirstFloorMax();
				if (organization!=null && StringUtils.isNotBlank(organization.getOrgLevel())) {
					orgLevelString = String.valueOf((Integer.valueOf(organization.getOrgLevel()) + 1));
					orgLevelString = "0000".substring(0, 4 - orgLevelString.length()) + orgLevelString;//不够4位前面补0
				}
			}
		}
		return orgLevelString;
	}
	
	@Override
	public void deleteOrganizations(String organizationIds)
			throws ApplicationException {
		// TODO Auto-generated method stub
		String[] idArray = organizationIds.split(",");
		for (String id : idArray) {
			Organization target = organizationDao.findById(id);
//			if (target != null) {
//				for(User user : target.getUsers()){
//					user.getOrganization().remove(target);
//				}
//				organizationDao.delete(target);
//			}
			//不能直接删除用户，如果有联系用户或组织架构，要求先移除关联关系
			if (target == null) throw new ApplicationException(ErrorCode.ORG_NOT_FOUND);
			if (target.getUsers().size() > 0) throw new ApplicationException(ErrorCode.HAS_USER_FOR_DELETE);
			if (organizationDao.findByCriteria(Expression.eq("parentId", target.getId())).size() > 0) throw new ApplicationException(ErrorCode.HAS_ORG_FOR_DELETE);
			
			//断开数据库关联并删除
			organizationDao.clear();
			organizationDao.delete(new Organization(id));
		}
	}


	@Override
	public DataPackage getResourceList(Resource rsc, DataPackage dp) {
		return getResourceList(rsc, dp, false);
	}
	
	@Override
	public DataPackage getResourceList(Resource rsc, DataPackage dp, boolean includeNonRes)
			throws ApplicationException {
		// TODO Auto-generated method stub
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(rsc);
		if (!includeNonRes) {
			criterionList.add(Expression.ne("rtype", RoleResourceType.ANON_RES));
		}
		if(rsc.getRtype()!=null && StringUtils.isNotBlank(rsc.getRtype().getValue()) && rsc.getRtype().equals(RoleResourceType.APP)){//App的权限列表
			criterionList.add(Expression.eq("rtype", rsc.getRtype()));
		}else{//pc端的不找APP的权限
			criterionList.add(Expression.ne("rtype",RoleResourceType.APP));
		}
		List<Order> scoreOrderList = new ArrayList<Order>();//按菜单显示的排序方式来查询
		scoreOrderList.add(Order.asc("rorder"));
		scoreOrderList.add(Order.asc("id"));
		
		dp = resourceDao.findPageByCriteria(dp, scoreOrderList, criterionList);
		return dp;
	}

	/**
	 * 获取menu类型权限资源
	 * @return
	 */
	public List<Resource> getUserMenus() {
		return getUserResourcesByResType(RoleResourceType.MENU);
	}

	/**
	 * 获取button类型权限资源
	 * @return
	 */
	public List<Resource> getUserButtons() {
		return getUserResourcesByResType(RoleResourceType.BUTTON);
	}

	/**
	 * 获取某种类型的权限资源，根据资源类型
	 * @param resType
	 * @return
	 */
	@Override
	public List<Resource> getUserResourcesByResType(RoleResourceType resType) {
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return getUserResourcesByResTypeNew(resType);
		}else{
			return getUserResourcesByResTypeOld(resType);
		}
	}

	public  List<Resource> getUserResourcesByResTypeOld(RoleResourceType resType) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user =  getCurrentLoginUser();
		List<Role> roles = user.getRole();
		StringBuffer roleIds = new StringBuffer();
		StringBuffer specialRoleIds = new StringBuffer();
		for(Role role:roles){
			roleIds.append("'"+role.getRoleId()+"',");
//    		if (RoleCode.SPECIALROLE== role.getRoleCode()){
//				specialRoleIds.append("'"+role.getRoleId()+"',");
//			}

			if (role.getRoleCode() == RoleCode.STUDY_MANAGER){//学管师
				specialRoleIds.append("'specialRole',");//校区管理学管
			}
			if (role.getRoleCode() == RoleCode.CAMPUS_DIRECTOR) {//校区主任
				specialRoleIds.append("'specialRole1',");//校区管理主任
			}
			if (role.getRoleCode() == RoleCode.BREND_MENAGER) {//分公司总经理
				specialRoleIds.append("'specialRole2',");//分公司管理经理
			}
		}
		String sql = null;
		List<Organization> orgs = findOrganizationByUserId(user.getUserId());
		Boolean flag = isUserStateOfEmergency(orgs);
		if (resType == RoleResourceType.MENU) {

			if (flag){
				if (specialRoleIds.length()>1){
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID in " + "("
							+ specialRoleIds.substring(0, specialRoleIds.length() - 1) + ")) order by PARENT_ID,RORDER ";
				}else {
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID = 'ROL0000000146' ) order by PARENT_ID,RORDER ";
				}
			}else {
				if (roleIds.length() > 1) {
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID in " + "("
							+ roleIds.substring(0, roleIds.length() - 1) + ")) order by PARENT_ID,RORDER ";
				}
			}
		} else {
			if (flag) {
				if (specialRoleIds.length()>1){
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID in " + "("
							+ specialRoleIds.substring(0, specialRoleIds.length() - 1) + ")) order by PARENT_ID,RORDER ";
				}else {
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID = 'ROL0000000146' ) order by PARENT_ID,RORDER ";
				}
			}else{
				sql = "select * from resource where RTYPE = :resType "
						+ " and id in (select resourceID from role_resource where roleID in "
						+ "(select roleID from user_role where userID ='" + user.getUserId()
						+ "')) order by PARENT_ID,RORDER ";
			}
		}
		params.put("resType", resType.getValue());
		List<Resource> resources =new ArrayList<>();
		if (sql!=null){
			resources =resourceDao.findBySql(sql, params);
		}
		return resources;
	}

	@Override
	public List<Resource> getUserResourcesByResTypeNew(RoleResourceType resType) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user =  getCurrentLoginUser();
		List<Role> roles = user.getRole();
		StringBuffer roleIds = new StringBuffer();
		StringBuffer specialRoleIds = new StringBuffer();
		for(Role role:roles){
			roleIds.append("'"+role.getRoleId()+"',");
//    		if (RoleCode.SPECIALROLE== role.getRoleCode()){
//				specialRoleIds.append("'"+role.getRoleId()+"',");
//			}

			if (role.getRoleCode() == RoleCode.STUDY_MANAGER){//学管师
				specialRoleIds.append("'specialRole',");//校区管理学管
			}
			if (role.getRoleCode() == RoleCode.CAMPUS_DIRECTOR) {//校区主任
				specialRoleIds.append("'specialRole1',");//校区管理主任
			}
			if (role.getRoleCode() == RoleCode.BREND_MENAGER) {//分公司总经理
				specialRoleIds.append("'specialRole2',");//分公司管理经理
			}
		}
		String sql = null;
		List<Organization> orgs = findOrganizationByUserId(user.getUserId());
		Boolean flag = isUserStateOfEmergency(orgs);
		if (resType == RoleResourceType.MENU) {

			if (flag){
				if (specialRoleIds.length()>1){
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID in " + "("
							+ specialRoleIds.substring(0, specialRoleIds.length() - 1) + ")) order by PARENT_ID,RORDER ";
				}else {
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID = 'ROL0000000146' ) order by PARENT_ID,RORDER ";
				}
			}else {
				if (roleIds.length() > 1) {
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID in " + "("
							+ roleIds.substring(0, roleIds.length() - 1) + ")) order by PARENT_ID,RORDER ";
				}
			}
		} else {
			if (flag) {
				if (specialRoleIds.length()>1){
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID in " + "("
							+ specialRoleIds.substring(0, specialRoleIds.length() - 1) + ")) order by PARENT_ID,RORDER ";
				}else {
					sql = "select * from resource where RTYPE = :resType "
							+ " and id in (select resourceID from role_resource where roleID = 'ROL0000000146' ) order by PARENT_ID,RORDER ";
				}
			}else{
				sql = "select * from resource where RTYPE = :resType "
						+ " and id in (select resourceID from role_resource where roleID in "
						+ "(select role_ID from user_organization_role where user_ID ='" + user.getUserId()
						+ "')) order by PARENT_ID,RORDER ";
			}
		}
		params.put("resType", resType.getValue());
		List<Resource> resources =new ArrayList<>();
		if (sql!=null){
			resources =resourceDao.findBySql(sql, params);
		}
		return resources;
	}


	@Override
	public void saveOrUpdateResource(Resource rsc) throws ApplicationException {
		// TODO Auto-generated method stub
		Resource rscInDb = null;
		if(rsc.getId()!= null)
			rscInDb = resourceDao.findById(rsc.getId());
		if(rscInDb != null){
			for(Role role : rscInDb.getRoles()){
				role.getResources().remove(rscInDb);
			}
			for(Role role : rsc.getRoles()){
				role.getResources().add(rscInDb);
			}
			//自身创建时设置子节点标记为0，当添加子节点时再设置父的为1
			rsc.setHasChildren(0);
			Resource parentResource =null;
			if(StringUtils.isNotEmpty(rsc.getParentId())){
				parentResource = resourceDao.findById(rsc.getParentId());
			}
			if (parentResource != null) {
				parentResource.setHasChildren(1);
			} else {
				rsc.setParentId("-1");
			}
			
			if(rscInDb.getCreateTime()==null || StringUtils.isBlank(rscInDb.getCreateTime())){
				rsc.setCreateTime(DateTools.getCurrentDateTime());
				
			}else{
				rsc.setCreateTime(rscInDb.getCreateTime());
			}
			if(rscInDb.getCreateUser()==null){
				rsc.setCreateUser(this.getCurrentLoginUser().getUserId());
			}else{
				rsc.setCreateUser(rscInDb.getCreateUser());
			}
			resourceDao.save(rscInDb);
			resourceDao.merge(rsc);
		}else{
			for(Role role : rsc.getRoles()){
				role.getResources().add(rsc);
			}
			
			//自身创建时设置子节点标记为0，当添加子节点时再设置父的为1
			rsc.setHasChildren(0);
			if (StringUtils.isBlank(rsc.getParentId())) {
				rsc.setParentId("-1");
			} else {
				Resource parentResource = resourceDao.findById(rsc.getParentId());
				if (parentResource != null) {
					parentResource.setHasChildren(1);
				}
			}
			
			rsc.setCreateTime(DateTools.getCurrentDateTime());
			rsc.setCreateUser(this.getCurrentLoginUser().getUserId());
			resourceDao.save(rsc);
		}
	}


	@Override
	public void deleteResources(String resourceIds)
			throws ApplicationException {
		// TODO Auto-generated method stub
		String[] idArray = resourceIds.split(",");
		for (String id : idArray) {
			Resource target = resourceDao.findById(id);
			if(target != null){
				for(Role role : target.getRoles()){
					role.getResources().remove(target);
				}
				resourceDao.delete(target);
			}
		}
	}


	@Override
	public void saveRoleResource(List<RoleResource> roleResourceList)
			throws ApplicationException {
		// TODO Auto-generated method stub
		/*for(Iterator it = roleResourceList.iterator();it.hasNext();){
			RoleResource roleResource = (RoleResource)it.next();
			RoleResource roleReourceInDb = roleResourceDao.findById(roleResource.getId());
			if(roleReourceInDb != null){
				HibernateUtils.copyPropertysWithoutNull(roleReourceInDb, roleResource);
			}else{
				roleResource.setId(null);
				roleResourceDao.save(roleResource);
			}
		}*/
	}


	@Override
	public void deleteRoleResource(List<RoleResource> roleResourceList)
			throws ApplicationException {
		// TODO Auto-generated method stub
		for(Iterator it = roleResourceList.iterator();it.hasNext();){
			RoleResource roleResource = (RoleResource)it.next();
			roleResourceDao.delete(roleResource);
		}
	}


	@Override
	public void saveUserOrganization(List<UserOrganization> userOrganizationList)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteUserOrganization(
			List<UserOrganization> userOrganizationList)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void saveUserRole(List<UserRole> userRoleList)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteUserRole(List<UserRole> userRoleList)
			throws ApplicationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Organization getOrganizationById(String organizationId)
			throws ApplicationException {
		return organizationDao.findById(organizationId);
	}

	@Override
	public Organization getOrganizationAndRegionById(String organizationId)
			throws ApplicationException {
		Organization org= organizationDao.findById(organizationId);
		org = organizationDao.setProvinceAndCityToOrganization(org);
		if(org!=null && StringUtils.isNotEmpty(org.getRegionId())){
			DataDict dataDict=dataDictDao.findById(org.getRegionId());
			if(dataDict!=null && dataDict.getParentDataDict() != null){
				org.setProvinceId(dataDict.getParentDataDict().getId());
			}
		}
		return org;
	}
	/**
	 * 根据学校Id获取省市
	 */
	@Override
	public OrganizationVo findOrganizationBySchoolId(String schoolId) {		
		StudentSchool school=studentSchoolDao.findById(schoolId);
		if(school != null){
			OrganizationVo org=new OrganizationVo();
			Region province = school.getProvince();
			Region city = school.getCity();
			if (province!=null && city!=null){
				org.setProvinceId(province.getId());
				org.setRegionId(city.getId());
				org.setCityId(city.getId());
			}
			return org;
		}else{
			return null;
		}
		
	}

	/**
	 * 获取所有校区
	 *
	 * @return
	 */
	@Override
	public List<Organization> getAllCampusForMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from Organization where 1=1 ");
		hql.append(" and orgType = '").append(OrganizationType.CAMPUS).append("' ");
		hql.append(" and lon is not null and lat is not null");
		List<Organization> allCampus = organizationDao.findAllByHQL(hql.toString(), params);
		return allCampus;
	}

	@Override
	public void resetPassword(String userId) {
		if(StringUtils.isEmpty(userId)) {
			throw new ApplicationException(ErrorCode.USER_ID_EMPTY);
		}
		User user = userDao.findById(userId);
		if (user!=null) {
			user.setPassword(getRandomString(6));
			user.setModifyTime(DateTools.getCurrentDateTime());
            if(mailService.isMailSysInUse() == true) { //开启邮件系统
				if(StringUtils.isNotBlank(user.getMailAddr()) && user.getMailStatus() != null && user.getMailStatus() == 0) {
					mailService.chageMailUserPassword(user);
				}
			}
			String userContact = user.getContact();
			if (StringUtils.isBlank(userContact)){
				throw new ApplicationException("用户手机号码为空");
			}else {
				notifyUserResetPSW(user);
			}
		} else {
			throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
		}
	}

	/**
	 * 通知用户重置密码
	 * @param user
	 *
	 */
	private void notifyUserResetPSW(User user) {
		User currentLoginUser = getCurrentLoginUser();
		String currentUserId = currentLoginUser.getUserId();
		ThreadPoolTaskExecutor taskExecutor =(ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendUserPassWordMsgThread thread = new SendUserPassWordMsgThread(user, currentUserId, MsgNo.M6);
		taskExecutor.execute(thread);
	}

	/**
	 * 通知用户开通账户和初始密码
	 * @param user
	 */
	private void notifyUserNewPSW(User user) {
		User currentLoginUser = getCurrentLoginUser();
		String currentUserId = currentLoginUser.getUserId();
		String loginAddress = user.getLoginAddress();
		ThreadPoolTaskExecutor taskExecutor =(ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendUserPassWordMsgThread thread = new SendUserPassWordMsgThread(user, currentUserId, MsgNo.M7, loginAddress);
		taskExecutor.execute(thread);
	}


	@Override
	public void setUserEnable(String userId, boolean isEnable) {
		if(StringUtils.isEmpty(userId)) {
			throw new ApplicationException(ErrorCode.USER_ID_EMPTY);
		}
		if (!isEnable) {
			if (refundWorkflowService.checkHasAuditingRefundWorkflow(userId)) {
				throw new ApplicationException("学管存在未完成的退费或转账审核，请完成了审核工作流再禁用学管，或撤销审核才禁用学管。");
			}
		}
		User user = userDao.findById(userId);
		if(mailService.isMailSysInUse() == true) { //开启邮件系统
			if (null != user.getMailStatus() && 0 == user.getMailStatus()) {
				boolean flag = mailService.changeMailUserStatus(userId, 1);
				if(flag == true) this.setUserMailStatus(userId, 1);
			}
		}
		if (user!=null) {
			user.setEnableFlg(isEnable?0:1);
			user.setCcpStatus(isEnable?0:1);
			user.setModifyTime(DateTools.getCurrentDateTime());
		} else {
			throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
		}
	}
	
	
	
	@Override
	public List<UserDeptJob> getUserDeptJobList(String userId) {
		return userDeptJobDao.findDeptJobByUserId(userId);
	}	

	@Override
	public Role getRoleById(String roleId) throws ApplicationException {
		Role roleDb = roleDao.findById(roleId);
		if (roleDb != null) {
			roleDb.setResources(roleResourceDao.findResourceByRoleId(roleDb.getRoleId()));
			
			List<DistributableRole> distributableRoles = distributableRoleService.findDistributableRolesByRoleId(roleDb.getRoleId());
			List<String> relateRoleIds = new ArrayList<String>();
			for(DistributableRole dr : distributableRoles) {
				relateRoleIds.add(dr.getRelateRoleId());
			}
			roleDb.setRelateRoleIds(relateRoleIds);
		}
		return roleDb;
	}


	@Override
	public Resource getResourceById(String id) throws ApplicationException {
		return resourceDao.findById(id);
	}
	
	@Override
	public List<Resource> getResourcesByUserId(String userId,String Type) throws ApplicationException {
		return resourceDao.findAllResourcesByUserId(userId,Type);
	}
	
	public List<Resource> getAllAnonymouslyResourceList() {
		return resourceDao.findByCriteria(Expression.eq("rtype", RoleResourceType.ANON_RES));
	}

	@Override
	public List<Role> getRoleByUserId(String userId) {
		/**
		 * 获取判断配置参数，如果是0 取新的组织架构角色来获取
		 */
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			List<Role> returnList = new ArrayList<>();
			List<Role> orgRoleList = roleDao.getUserRoleList(userId);
			Map<String, Role> hasMap = new HashMap<>();
			for (Role orgRole : orgRoleList) {
				if (hasMap.get(orgRole.getRoleId()) == null) {
					hasMap.put(orgRole.getRoleId(), orgRole);
					returnList.add(orgRole);
				}
			}
			return returnList;
		}else {
			return userRoleDao.findRoleByUserId(userId);
		}
	}

	@Override
	public List<Role> getCurrentLoginUserRoles() {
		User user =  getCurrentLoginUser();
		return getRoleByUserId(user.getUserId());
	}

	@Override
	public List<Resource> getResourcesByRoleId(String roleId) {
		return roleResourceDao.findResourceByRoleId(roleId);
	}
	
	@Override
	public List<Resource> getResourcesByRoleIdArray(String[] roleIds) {
		return resourceDao.findResourceByRoleIdArray(roleIds);
	}


	@Override
	public List<Organization> findOrganizationByUserId(String userId) {
		return organizationDao.findOrganizationByUserId(userId);
	}


	@Override
	public User getCurrentLoginUser() {

		User user =new User();
		if( SecurityContextHolder.getContext()== null
			||SecurityContextHolder.getContext().getAuthentication()==null){
			Object token=SpringMvcUtils.getSession()!=null?SpringMvcUtils.getSession().getAttribute("token"):null;
			if(token!=null){	
				user = TokenUtil.getTokenUser(token.toString());
				return user;
			}
			return null;
		} 
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal == null) {
			throw new ApplicationException(ErrorCode.NO_LOGIN_USER_INFO);
		}
		user= ((UserDetailsImpl)principal).getUser();

		if(user.getOrganization().size()==0){
			List<Organization> list=getUserOrganizationList(user.getUserId());//旧数据用这个findOrganizationByUserId
			user.setOrganization(list);		
		}
		return user;
	}

	@Override
	public String getCurrentLoginUserId() {
		User user= getCurrentLoginUser();
		if(user!=null){
			return user.getUserId();
		}
		return null;
	}

	
	/**
	 * 获取当前登录用户归属主组织架构
	 */
	public Organization getCurrentLoginUserOrganization(){
		String organizationId = getCurrentLoginUser().getOrganizationId();
		if(StringUtils.isNotEmpty(organizationId)){
			return organizationDao.findById(organizationId);
		}
		return null;
	}

	@Override
	public String getCurrentLoginUserOrgId(String orgLevel) {
		User user = getCurrentLoginUser();
		String orgId = "";
		for (Organization organization : user.getOrganization()) {
			if (orgLevel.equalsIgnoreCase(organization.getOrgLevel())) {
				orgId = organization.getId();
			}
		}
		return orgId;
	}

	/*@Override
	public Organization gtBelongOrgazitionByOrgType(String orgLevel, OrganizationType orgType) {
		return organizationDao.getBelongOrgazitionByOrgType(orgLevel, orgType, true);
	}*/
	
	@Override
	public Organization getBelongOrgazitionByOrgType(String orgLevel,
			OrganizationType orgType) {
		return organizationDao.getBelongOrgazitionByOrgType(orgLevel, orgType, true);
	}
	
	@Override
	public Organization getOrgazitionByOrgType(String orgLevel, OrganizationType orgType,Boolean boolean1) {
		return organizationDao.getBelongOrgazitionByOrgType(orgLevel, orgType, boolean1);
	}

	@Override
	public Organization getBelongCampus() {
		return getBelongCampusByUserId(getCurrentLoginUser().getUserId()) ;
	}
	
	@Override
	public Organization getBelongCampusByUserId(String userId) {
		User user=userDao.findById(userId);
		Organization userOrganization=organizationDao.findById(user.getOrganizationId());
		if(userOrganization!=null && StringUtils.isNotBlank(userOrganization.getOrgLevel()))
			return getBelongOrgazitionByOrgType( userOrganization.getOrgLevel(), OrganizationType.CAMPUS);
		return null;
//		Organization returnOrg = null;
//		List<Organization> organizations = organizationDao.findOrganizationByUserId(userId);
//		if (organizations.size() > 0) {
//			returnOrg =  organizationDao.getBelongCampus(organizations.get(0).getOrgLevel());
//			if (returnOrg == null) {//如果没有校区（可能用户归属于集团的部门），找出用户所有组织架构中最高层级的返回
//				returnOrg = organizations.get(0);
//				for (Organization org : organizations) {
//					if (returnOrg.getOrgLevel().length() > org.getOrgLevel().length()) {
//						returnOrg = org;
//					}
//				}
//			}
//		}
//		return returnOrg;
	}

	@Override
	public Organization getTongjiguishuxiaoqu(String userId) {
		UserDeptJob deptJobByParam = userDeptJobDao.findDeptJobByParam(userId, 0);
		if (deptJobByParam != null && deptJobByParam.getId() >0) {
			Organization deptOrg = organizationDao.findById(deptJobByParam.getDeptId());//用户的主部门
			if (deptOrg!=null&&StringUtils.isNotBlank(deptOrg.getBelong())){
				Organization belongOrg = organizationDao.findById(deptOrg.getBelong());
				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel()))
					return getBelongOrgazitionByOrgType( belongOrg.getOrgLevel(), OrganizationType.CAMPUS);
			}
		}

		return null;
	}
	
	/**
	 * 根据组织架构id查询所属校区
	 * @param orgId
	 * @return
	 */
	public Organization getBelongCampusByOrgId(String orgId) {
		Organization org = organizationDao.findById(orgId);
		Organization returnOrg =org;
		String orgLevel = returnOrg.getOrgLevel();
		if (returnOrg != null) {
			returnOrg =  organizationDao.getBelongCampus(orgLevel);
		}
		if (returnOrg == null) {//如果没有校区,找分公司
			returnOrg =  organizationDao.getBelongBrench(orgLevel);
		}
		if (returnOrg == null) {//如果没有分公司，找集团
			returnOrg =  organizationDao.getBelongGrounp(orgLevel);
		}
		if (returnOrg == null) {//都没有查询到返回自己
			return org;
		}
		return returnOrg;
	}
	
	/**
	 * 根据组织架构id查询所属分公司
	 * @param orgId
	 * @return
	 */
	public Organization getBelongBranchByOrgId(String orgId) {
		Organization org = organizationDao.findById(orgId);
		Organization returnOrg =org;
		if (returnOrg != null) {//找分公司
			returnOrg =  organizationDao.getBelongBrench(returnOrg.getOrgLevel());
		}
		if (returnOrg == null && org != null) {//如果没有分公司，找集团
			returnOrg =  organizationDao.getBelongGrounp(org.getOrgLevel());
		}
		if (returnOrg == null) {//都没有查询到返回自己
			return org;
		}
		return returnOrg;
	}
	
	/**
	 * 根据组织架构id查询所属集团
	 * @param orgId
	 * @return
	 */
	public Organization getBelongGrounpByOrgId(String orgId) {
		Organization org = organizationDao.findById(orgId);
		Organization returnOrg =org;
		if (returnOrg != null) {//找集团
			returnOrg =  organizationDao.getBelongGrounp(returnOrg.getOrgLevel());
		}
		if (returnOrg == null) {//都没有查询到返回自己
			return org;
		}
		return returnOrg;
	}

	@Override
	public Organization getBelongBranch() {
		return getBelongBranchByUserId(getCurrentLoginUser().getUserId());
	}
	
	@Override
	public Organization getBelongBranchByUserId(String userId) {
		User user=userDao.findById(userId);
		Organization userOrganization=organizationDao.findById(user.getOrganizationId());
		if(userOrganization!=null && StringUtils.isNotBlank(userOrganization.getOrgLevel()))
			return getBelongOrgazitionByOrgType( userOrganization.getOrgLevel(), OrganizationType.BRENCH);
		return null;
//		Organization returnOrg = null;
//		List<Organization> organizations = organizationDao.findOrganizationByUserId(userId);
//		if (organizations.size() > 0) {
//			returnOrg = organizationDao.getBelongBrench(organizations.get(0).getOrgLevel());
//			if (returnOrg == null) {//如果没有分公司（可能用户归属于集团的部门），找出用户所有组织架构中最高层级的返回
//				returnOrg = organizations.get(0);
//				for (Organization org : organizations) {
//					if (returnOrg.getOrgLevel().length() > org.getOrgLevel().length()) {
//						returnOrg = org;
//					}
//				}
//			}
//		}
//		
//		return returnOrg;
	}
	
	
	@Override
	public Organization getBelongGroup() {
		return getBelongGroupUserId(getCurrentLoginUser().getUserId());
	}
	
	@Override
	public Organization getBelongGroupUserId(String userId) {
		User user=userDao.findById(userId);
		Organization userOrganization=organizationDao.findById(user.getOrganizationId());
		if(userOrganization!=null && StringUtils.isNotBlank(userOrganization.getOrgLevel()))
			return getBelongOrgazitionByOrgType( userOrganization.getOrgLevel(), OrganizationType.GROUNP);
		return null;
//		Organization returnOrg = null;
//		List<Organization> organizations = organizationDao.findOrganizationByUserId(userId);
//		if (organizations.size() > 0) {
//			returnOrg = organizationDao.getBelongGrounp(organizations.get(0).getOrgLevel());
//			if (returnOrg == null) {
//				returnOrg = organizations.get(0);
//				for (Organization org : organizations) {
//					if (returnOrg.getOrgLevel().length() > org.getOrgLevel().length()) {
//						returnOrg = org;
//					}
//				}
//			}
//		}
//		
//		return returnOrg;
	}
	
	@Override
	public Organization getBelongBranchByCampusId(String orgId) {
		Organization org = organizationDao.findById(orgId);
		return organizationDao.getBelongBrench(org.getOrgLevel());
	}


	@Override
	public Role getRoleByTag(RoleCode roleType) {
		List<Role>  roles = roleDao.findByCriteria(Expression.eq("roleCode", roleType));
		if (roles.size() > 0) {
			return roles.get(0);
		} else {
			return null;
		}
		
	}

	@Override
	public void saveUserPersonal(User user) {

		User userInDb = null;
		if(user.getUserId()!= null) {
			userInDb = userDao.findById(user.getUserId());
		}else{
			throw new ApplicationException(ErrorCode.PARAMETER_EMPTY);
		}
		userInDb.setContact(user.getContact());
		userInDb.setSex(user.getSex());
		userInDb.setEmengentPerson(user.getEmengentPerson());
		userInDb.setEmengentPersonContact(user.getEmengentPersonContact());
		userInDb.setAddress(user.getAddress());
		userInDb.setGraduateSchool(user.getGraduateSchool());
		userInDb.setEducation(user.getEducation());
		userInDb.setCertification(user.getCertification());
		User u =  getCurrentLoginUser();
		userInDb.setModifyTime(DateTools.getCurrentDateTime());
		userInDb.setModifyUserId(u.getName());
		userDao.save(userInDb);
	}

	@Override
	public List<Organization> getOrganizationBoy(Organization org) {
//		Organization brench=null;
		//如果不是超级管理员，只能查看本分公司
        User usr =  getCurrentLoginUser();
//		if(usr.getRoleCode().indexOf(RoleCode.SUPER_ADMIN.toString()) < 0) {
//			brench =getBelongBranch();//分公司
//		}
		return organizationDao.getOrganizationBoy(usr.getOrganization());
	}
	
	/**
	 * 如果checkAllPermit为true，则全部机构获取
	 * @param org
	 * @param checkAllPermit
	 * @return
	 */
	public List<Organization> getOrganizationBoy(Organization org,String checkAllPermit){
		if (checkAllPermit == null){
			return getOrganizationBoy(org);
		}else if(checkAllPermit.equals("true")){
			return organizationDao.getOrganizationBoy(org, null);
		}
		return getOrganizationBoy(org);
	}
	
	@Override
	public List<Organization> getBelongOrg() {
		// TODO Auto-generated method stub
		
		return organizationDao.getBelongOrg(organizationDao.findOrganizationByUserId(getCurrentLoginUser().getUserId()));
	}
	
	@Override
	public List<UserVo> findUserByRoleForAutoCompelete(String term, RoleCode roleCode) {
		List<User> users ;
		if(CheckSystemUtils.checkNewOrg()){
			users = userDao.findByNameOrIdAndRodeCodeNew(term, roleCode);
		}else{
			users = userDao.findByNameOrIdAndRodeCode(term, roleCode);
		}
		return HibernateUtils.voListMapping(users, UserVo.class);
	}
	
	@Override
	public void saveUser(User user) {
		User u =  getCurrentLoginUser();
		user.setModifyTime(DateTools.getCurrentDateTime());
		user.setModifyUserId(u.getUserId());
		user.setEnableFlg(0);
		user.setCreateTime(DateTools.getCurrentDateTime());
		user.setCreateUserId(u.getUserId());
		this.userDao.save(user);
	}
	
	@Override
	public DataPackage getUserListByJob(DataPackage dp, String jobId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User u where 1=1 ";
		if(StringUtils.isNotEmpty(jobId)){
			hql+=" and job.id like :jobId";
			params.put("jobId", "%" + jobId + "%");
		}
		dp=userDao.findPageByHQL(hql, dp, true, params);
		return dp;
	}
	
	/**
	 * 查找用户列表（自动搜索）
	 */
	@Override
	public List<User> getUserAutoComplate(String input, String roleCode, String parentOrgId) {
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return userDao.getLimitUserAutoComplateNew(input, roleCode, parentOrgId,null,0);
		}else {
			return userDao.getUserAutoComplate(input, roleCode, parentOrgId);
		}
	}

	@Override
	public boolean isUserByAccount(String userId,String account) {
		List<Criterion> critList=new ArrayList<Criterion>();
		critList.add(Expression.eq("account", account));
		if(StringUtils.isNotEmpty(userId)){
			critList.add(Expression.ne("userId", userId));
		}
		List<User> list=userDao.findAllByCriteria(critList);
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public Response updateUserPassword(String account, String oldPassWord,
			String newPassWord) {
		Response response=new Response();
		int i=userDao.updateUserPassword(account,oldPassWord,newPassWord);
		if(i==0){
			response.setResultCode(0);
			response.setResultMessage("当前密码错误，请重新输入");
		}else if(i==1){
            User u =   getCurrentLoginUser();
            u.setPassword(newPassWord);
            if(mailService.isMailSysInUse() == true) { //开启邮件系统
				if(u != null && StringUtils.isNotBlank(u.getMailAddr()) && u.getMailStatus() != null && u.getMailStatus() == 0) {
					mailService.chageMailUserPassword(u);
				}
			}
        }
		return response;
	}

	@Override
	public List<Role> getRoleByRoleCode(RoleCode roleCode) {
		List<Criterion> list=new ArrayList<Criterion>();
		list.add(Expression.eq("roleCode", roleCode));
		return roleDao.findAllByCriteria(list);
	}


	/**
	 * 查询拥有角色的用户
	 * @param
	 * @param
	 * @return
	 */
	public List<User> getUserByRoldCodes(String roleCodes){
		return userDao.getUserByRoldCodes(roleCodes, getBelongCampus().getOrgLevel());
	}
	public List<User> getUserByRoldCodesNoOrgLevel(String roleCodes){
		return userDao.getUserByRoldCodes(roleCodes, null);
	}

    /**
     * 根据roleCode和orgId获取对应角色用户
     *
     * @param roleCodes
     * @param organizationId
     * @return
     */
    @Override
    public List<User> getUserByRoldCodesAndOrgId(String roleCodes, String organizationId) {
		Organization organization = organizationDao.findById(organizationId);
		if (organization!=null){
			return userDao.getUserByRoldCodes(roleCodes, organization.getOrgLevel());
		}else {
			return userDao.getUserByRoldCodes(roleCodes, getBelongCampus().getOrgLevel());
		}
    }
    
    public List<User> getUserByRoldCodesAndOrgId2(String organizationId) {
		Organization organization = organizationDao.findById(organizationId);
		if (organization!=null){
			return userDao.getUserByRoldCodes2(organization.getOrgLevel());
		}else {
			return userDao.getUserByRoldCodes2(getBelongCampus().getOrgLevel());
		}
    }

    /**
     *
     * @param roleCodes
     * @param name
     * @return
     */
	@Override
	public List<User> getUserByRoldCodes(String roleCodes, String name){
		Organization org= getBelongCampus();
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return userDao.getUserByRoldCodesNew(roleCodes, org.getOrgLevel(), name);
		}else{
			return userDao.getUserByRoldCodes(roleCodes, org.getOrgLevel(),name);
		}
	}



    @Override
	public Response deleteDummyUser(User user) {
		Response response = new Response();
		if(courseSummaryDao.countCourseOfDummyTeacher(user.getUserId()) > 0){
			response.setResultCode(1);
			response.setResultMessage("已排课的虚拟老师不允许删除");
		}else{
			userDao.deleteById(user.getUserId());
			response.setResultMessage("删除虚拟老师成功");
		}
		return response;
	}

	@Override
	public List<Organization> getOrganizationTree(String organizationTypes) {
		Map<String, Object> params = new HashMap<String, Object>();
        String sql= "select * from organization ";
		if(StringUtils.isNotBlank(organizationTypes)){
			String[] types = StringUtil.replaceSpace(organizationTypes).split(",");
            sql+=" where orgType in (:types)";
			params.put("types", types);
		}
        sql+=" order by length(orgLevel), orgOrder";
        return organizationDao.findBySql(sql, params);

	}

	@Override
	public List<AutoCompleteOptionVo> getUserForPerformance(String term,
		String roleCode, String parentOrgId) {

		List<User> resList =new ArrayList<>();
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			resList = userDao.getLimitUserAutoComplateNew(term, roleCode, parentOrgId,null,CommonUtil.autoComplateLimit);
		}else{
			resList = userDao.getLimitUserAutoComplate(term, roleCode, parentOrgId);
		}

		List<AutoCompleteOptionVo> voList = new ArrayList<AutoCompleteOptionVo>();
		
		for(User user : resList){
			String label = "";
			AutoCompleteOptionVo userPer  = new AutoCompleteOptionVo();
			userPer.setValue(user.getUserId());
			label += user.getName();
			label += "("+getBelongCampusByUserId(user.getUserId()).getName()+",";
			if(StringUtils.isNotBlank(user.getRoleId())){
				Role role=roleDao.findById(user.getRoleId());
				if(role !=null )
					label += role.getName()+")";
			}
			userPer.setLabel(label);
			voList.add(userPer);
		}
		return voList;
	}
	
	@Override
	public List<AutoCompleteOptionVo> getUserByOrgForPerformance(String term,
		String roleCode, String parentOrgId) {
		String userId = this.getCurrentLoginUser().getUserId();
		List<User> resList =new ArrayList<>();
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			resList = userDao.getLimitUserAutoComplateNew(term, roleCode, parentOrgId,userId,CommonUtil.autoComplateLimit);
		}else {
			resList = userDao.getLimitUserByOrgAutoComplate(term, roleCode, parentOrgId, userId);
		}
		List<AutoCompleteOptionVo> voList = new ArrayList<AutoCompleteOptionVo>();
		
		for(User user : resList){
			String label = "";
			AutoCompleteOptionVo userPer  = new AutoCompleteOptionVo();
			userPer.setValue(user.getUserId());
			label += user.getName();
			label += "("+getBelongCampusByUserId(user.getUserId()).getName()+",";
			if(StringUtils.isNotBlank(user.getRoleId())){
				Role role=roleDao.findById(user.getRoleId());
				if(role !=null )
					label += role.getName()+")";
			}
			userPer.setLabel(label);
			voList.add(userPer);
		}
		return voList;
	}
	
	public List<User> getUserByBlcampusAndRole(String blcampus,String roleIds){
		return userDao.getUserByBlcampusAndRole(blcampus, roleIds);
	}
	
	/**
	 * 根据校区和职位来查询用户
	 */
	@Override
	public List<User> getUserByBlcampusAndUserJob(String blcampus, String userJobIds) {
		return userDao.getUserByBlcampusAndUserJob(blcampus, userJobIds);
	}

	/**
	 * 根据校区和职位来查询用户
	 */
	@Override
	public List<User> getUserByBlcampusAndUserJobValidate(String blcampus, String userJobIds) {
		return userDao.getUserByBlcampusAndUserJobValidate(blcampus, userJobIds);
	}
	
	@Override
	public void updateUserArchivesPath(User user) {
		User u = new User();
		if(StringUtils.isNotBlank(user.getUserId())){
			u=userDao.findById(user.getUserId());
			int b = 0;
			if(StringUtils.isNotBlank(u.getArchivesPath())){
				b = FileUtil.deleteContents(new File(u.getArchivesPath()));
			}
			if(b>0)
			u.setArchivesPath(null);
		}
		
	}

	@Override
	public List<UserForMobileVo> getMobileContactsForStudent(String studentId) {
		
		List<User> users = userDao.getContactsForStudent(studentId);
		List<UserForMobileVo> mUsers = HibernateUtils.voListMapping(users, UserForMobileVo.class); 
		
		for (Object userObject : mUsers) {
			UserForMobileVo userDb = (UserForMobileVo)userObject;
        	userDb.setRole(userRoleDao.findRoleByUserId(userDb.getUserId()));
        	userDb.setOrganization(organizationDao.findOrganizationByUserId(userDb.getUserId()));
        	// 判断有无mobileUser
    		MobileUser mobileUserForUser = mobileUserService.findMobileUserByStaffId(userDb.getUserId());
    		userDb.setMobileUserId(mobileUserForUser.getId());
        }
		return mUsers;
	}
	
	@Override
	public void updateUserToken(User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", user.getToken());
		params.put("userId", user.getUserId());
		userDao.excuteSql("update user set token = :token where USER_ID = :userId", params);//保存用户令牌
	}
	
	@Override
	public User getUserByToken(String token) {
		Criterion userCriterion = Expression.eq("token",token);
	    List<User> userList = userDao.findByCriteria(userCriterion);
	    User user = null;
		if (userList != null && userList.size() > 0) {
			user = userList.get(0);
		}
		
		return user;
	}
	
		@Override
	public List<User> getUserListForMobileByRoleAndOrg(List<Role> roles,
			List<Organization> organizations) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from User u where 1=1 ");
		if(roles!= null && roles.size()>0){
			hql.append(" and exists (select 1 from UserRole where u.userId = userId and roleId in ( :roles ");
//			for(Role role : roles) {
//				hql.append("'").append(role.getRoleId()).append("' ,");
//			}
			params.put("roles", roles);
//			hql.deleteCharAt(hql.length()-1);
			hql.append("))");
		}
		if(organizations!=null && organizations.size() > 0){
			hql.append(" and (");
//			for(Organization org : organizations) {
//				hql.append(" ( exists (select 1 from Organization where u.organizationId = id and orgLevel like '"+org.getOrgLevel()+"%' ))  or");
//			}
			for (int i = 0; i<organizations.size(); i++){
				hql.append(" ( exists (select 1 from Organization where u.organizationId = id and orgLevel like :orgLevel"+i+" ))  or");
				params.put("orgLevel"+i, organizations.get(i).getOrgLevel()+"%");
			}
			hql.delete(hql.length()-3, hql.length());
			hql.append(")");
		}
		return userDao.findAllByHQL(hql.toString(), params);
	}
		
	@Override
	public void regAppAccount(String userId) {
		// TODO Auto-generated method stub
		User user=userDao.findById(userId);
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
//		restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
//		restAPI.setAccount("8a48b551506925be01506e81d7171075", "74ce37445a1b43ce9699f0c062396bc1");// 初始化主帐号和主帐号TOKEN
//		restAPI.setAppId("8a48b551506925be01506e85801e1093");// 初始化应用ID
		
		
		restAPI.init(PropertiesUtils.getStringValue("ccpIP"), PropertiesUtils.getStringValue("ccpPort"));// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount(PropertiesUtils.getStringValue("ccpName"), PropertiesUtils.getStringValue("ccpPwd"));// 初始化主帐号和主帐号TOKEN
		restAPI.setAppId(PropertiesUtils.getStringValue("ccpAppId"));// 初始化应用ID
		result = restAPI.createSubAccount(user.getName());

		System.out.println("SDKTestCreateSubAccount result=" + result);
		
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
//			for(String key:keySet){
				Object object = data.get("SubAccount");
				List subAccount2 = (List) object;
				HashMap<String,Object> subAccount = (HashMap<String, Object>)subAccount2.get(0);
				System.out.println(subAccount.get("subToken")+"--"+subAccount.get("voipPwd")+"--"+subAccount.get("subAccountSid")+"--"+subAccount.get("voipAccount"));
				
				user.setCcpAccount(StringUtil.toString(subAccount.get("voipAccount")));
				user.setCcpPwd(StringUtil.toString(subAccount.get("voipPwd")));
				user.setCcpStatus(1);//刚开通未登录
				user.setModifyTime(DateTools.getCurrentDateTime());
				userDao.save(user);
//			}
		}else{
			//异常返回输出错误码和错误信息
//			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
			
			throw new ApplicationException(result.get("statusMsg").toString());
		}
	}	
	
	@Override
	public void activeUserAppStatus() {
		User user=this.getCurrentLoginUser();
		user.setCcpStatus(0);
		userDao.save(user);
	}
	
	
	
	@Override
	public List<MobileUserVo> getMobileContactsByStudent(String studentId) {		
		List<User> users = userDao.getContactsForStudent(studentId);
		List<MobileUserVo> voList =  new ArrayList<MobileUserVo>();		
		for (User user : users) {
			// 判断有无mobileUser
			MobileUser mobileUserForUser = mobileUserService.findMobileUserByStaffId(user.getUserId());
			MobileUserVo mobileUserVo = HibernateUtils.voObjectMapping(mobileUserForUser, MobileUserVo.class); 
			mobileUserVo.setName(user.getName());
			voList.add(mobileUserVo);
        }
		return voList;
	}

	@Override
	public void setRoleMailListStatus(String roleId, Short mailListStatus) {
		if(StringUtils.isEmpty(roleId)) {
			throw new ApplicationException(ErrorCode.ROLE_NOT_FOUND);
		}
		Role role = roleDao.findById(roleId);
		if (role!=null) {
			role.setMailListStatus(mailListStatus);
		} else {
			throw new ApplicationException(ErrorCode.ROLE_NOT_FOUND);
		}	
	}

	@Override
	public void onlyAddRoleSign(String roleId, String roleSign) {
		if(StringUtils.isEmpty(roleId)) {
			throw new ApplicationException(ErrorCode.ROLE_NOT_FOUND);
		}
		Role role = roleDao.findById(roleId);
		if (role!=null) {
			if(StringUtils.isBlank(role.getRoleSign())) {  //如果是空的
				role.setRoleSign(getUniqueRoleSign(roleSign, roleId));
			}				
		} else {
			throw new ApplicationException(ErrorCode.ROLE_NOT_FOUND);
		}
	}
	
	@Override
	public List<UserMobileVo> findUsersByUserName(String userName) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from User where name like :userName and ccpStatus=0 and enableFlg=0 ");
		params.put("userName", "%"+userName+"%");
		List<User> userlist = userDao.findAllByHQL(hql.toString(),params);
		List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);
		return userMobileVo;
	}
	
	@Override
	public List<UserMobileVo> findUsersByUserNameWithJiaXue(String userName) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from User where name like :userName and enableFlg=0 ");
		params.put("userName", "%"+userName+"%");
		List<User> userlist = userDao.findAllByHQL(hql.toString(), params);
		List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);
		return userMobileVo;
	}
	
	@Override
	public UserMobileVo findUsersByCcpAccount(String ccpAccount) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from User where ccpAccount = :ccpAccount and ccpStatus=0 ");
		params.put("ccpAccount", ccpAccount);
		List<User> userlist = userDao.findAllByHQL(hql.toString(), params);
		List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);
		if(userMobileVo.size()>0){
			UserMobileVo userMobile = userMobileVo.get(0);
			List<Map> listMap=new ArrayList<Map>();
			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(userMobile.getUserId());
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				Map map=new HashMap();
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
				UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
				Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
				if(userDeptJob2.getIsMajorRole()==0){
					userMobile.setDeptName(dept.getName());
					userMobile.setJobName(userJob.getJobName());
				}else{
					map.put("jobName", userJob.getName());
					map.put("deptName",dept.getName());
					map.put("jobLevel", userJob.getJobLevel());
					listMap.add(map);
				}
			}
			userMobile.setContact("");//设置为空，经常有用户打扰领导   2016-05-16 Yao
			userMobile.setJobDept(listMap);
			return userMobile;
		}
		return null;
	}
	
	@Override
	public Map getMainDeptAndJob(String userId) {
		StringBuffer sqls=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sqls.append(" select o.name deptName,uj.Job_name jobName from  ");
		sqls.append(" user_dept_job udj  ");
		sqls.append(" left join organization o on o.id=udj.DEPT_ID");
		sqls.append(" left join user_job uj on uj.id=udj.job_id");
		sqls.append(" where udj.user_id= :userId and udj.isMajorRole=0");
		params.put("userId", userId);
		
		List<Map<Object, Object>> userDeptJob = userDeptJobDao.findMapBySql(sqls.toString(), params);
		if(userDeptJob.size()>0){
			return userDeptJob.get(0);
		}
		return null;
	}
	
	@Override
	public List<UserMobileVo> findUsersByJobId(DataPackage dp, String jobId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select distinct u.* from User u,user_dept_job udj where 1=1  and u.ccp_Status=0 and u.ENABLE_FLAG=0 ");
		
		hql.append(" and udj.job_Id= :jobId ");
		params.put("jobId", jobId);
		hql.append(" and udj.user_id=u.user_Id");
		
//		List<User> userlist = userDao.findBySql(hql.toString());
		dp = userDao.findPageBySql(hql.toString(), dp, true, params);
		List<User> userlist = (List<User>) dp.getDatas();
		List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);
		
		for (UserMobileVo user : userMobileVo) {
			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
				if (userDeptJob2!=null){
					UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
					if (userJob!=null){
						user.setJobName(userJob.getJobName());
					}
					Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
					if(userDeptJob2.getIsMajorRole()==0){
						if (dept!=null){
							user.setDeptName(dept.getName());
						}
					}
				}


				user.setContact("");//设置为空，经常有用户打扰领导   2016-05-16 Yao
			}
		}
		
//	    dp.setDatas(userMobileVo);
		return userMobileVo;
	}
	
	
	public List<UserJob> getAllJobAndUserList() {
		String hql= " from UserJob where flag='0' ";
		Map<String, Object> params = Maps.newHashMap();
		Map<String, Object> params1 = Maps.newHashMap();
	    List<UserJob> UserJobs = userJobDao.findAllByHQL(hql, params);
		for (UserJob userJob : UserJobs) {
			userJob.setUserList(findUsersByJobId(new DataPackage(0,999),userJob.getId()));
			params1.put("userJobId", userJob.getId());
			userJob.setUserCount(userDeptJobDao.findCountHql(" select count( distinct udj.userId) from UserDeptJob  udj,User u where udj.userId=u.userId and u.ccpStatus=0 and u.enableFlg=0 and udj.jobId= :userJobId ", params1));
	    }
	    return UserJobs;
	};
	
	@Override
	public List<MobileOrganization> getAllMobileDeptList() {
		List<MobileOrganization> list= mobileOrganizationDao.getOrganizationBoy(null,1);
		Map<String, Object> params = Maps.newHashMap();
		for(MobileOrganization o :list){
			o.setUserCount(userDao.findCountHql(" select count(distinct udj.userId) from UserDeptJob udj,User u where udj.userId=u.userId and u.ccpStatus=0 and u.enableFlg=0 and udj.deptId in( select id from Organization where orgLevel = '"+o.getOrgLevel()+"' )", params));;
		}
		return list;
	}
	
	@Override
	public Map<String, Object> getAllMobileDeptAndUserByParentId(
			DataPackage dp, String parentId, String deptLevel, int level) {
		Map<String,Object> returnMap=new HashMap<String,Object>();
		List<MobileOrganization> mobileorgList = mobileOrganizationDao.getOrganizationBoy(parentId,level+1);
		List<UserMobileVo> list = findUsersByDept(dp, parentId, deptLevel, null, level);
		Map<String, Object> params = Maps.newHashMap();
		for(MobileOrganization o :mobileorgList){
			o.setUserCount(userDao.findCountHql(" select count(distinct udj.userId) from UserDeptJob udj,User u where udj.userId=u.userId and u.ccpStatus=0 and u.enableFlg=0 and udj.deptId in( select id from Organization where orgLevel = '"+o.getOrgLevel()+"' )", params));;
		}
		returnMap.put("userList", list);
		returnMap.put("orgList", mobileorgList);
		return returnMap;
	}
	
	@Override
	public List<MobileOrganizationVo> getAllMobileOrganizationList() {
		Map<String, Object> params = Maps.newHashMap();
		String hql=" from MobileOrganization where 1=1  order by orgOrder ";
		List<MobileOrganization> mobileorgList = mobileOrganizationDao.findAllByHQL(hql, params);
		List<MobileOrganizationVo> mobileOrgVoList=new ArrayList<MobileOrganizationVo>();
		for (MobileOrganization morg : mobileorgList) {
			MobileOrganizationVo mov=new MobileOrganizationVo();
			mov.setId(morg.getId());
			mov.setLevel(morg.getLevel());
			mov.setName(morg.getName());
			mov.setOrgLevel(morg.getOrgLevel());
			mov.setOrgOrder(morg.getOrgOrder());
			mov.setOrgType(morg.getOrgType().getValue());
			mov.setParentId(morg.getParentId());
			mobileOrgVoList.add(mov);
		}
		return mobileOrgVoList;
	}
	
//	@Override
//	public List<MobileOrganizationVo> getAllMobileOrganizationList() {
//		String hql=" from MobileOrganization where level=1  order by orgOrder ";
//		List<MobileOrganization> mobileorgList = mobileOrganizationDao.findAllByHQL(hql);
//		List<MobileOrganizationVo> mobileOrgVoList=new ArrayList<MobileOrganizationVo>();
//		for (MobileOrganization morg : mobileorgList) {
//			MobileOrganizationVo mov=new MobileOrganizationVo();
//			DataPackage dp=new DataPackage(0,999999);
//			mov.setUserList(this.findUsersByDeptPC(dp, morg.getId(), morg.getOrgLevel(), morg.getOrgType().getValue(), morg.getLevel()));
//			List<MobileOrganization> subOrg = mobileOrganizationDao.getOrganizationBoy(morg.getId(), 2);
//			List<MobileOrganizationVo> subOrgVo1=new ArrayList<MobileOrganizationVo>();
//			for (MobileOrganization sub : subOrg) {
//				MobileOrganizationVo subMov=new MobileOrganizationVo();
//				subMov.setUserList(this.findUsersByDeptPC(dp, sub.getId(), sub.getOrgLevel(), sub.getOrgType().getValue(), sub.getLevel()));
//				subMov.setId(sub.getId());
//				subMov.setId(sub.getId());
//				subMov.setLevel(sub.getLevel());
//				subMov.setName(sub.getName());
//				subMov.setOrgLevel(sub.getOrgLevel());
//				subMov.setOrgOrder(sub.getOrgOrder());
//				subMov.setOrgType(sub.getOrgType().getValue());
//				subMov.setParentId(sub.getParentId());
//				subOrgVo1.add(subMov);
//			}
//			mov.setId(morg.getId());
//			mov.setLevel(morg.getLevel());
//			mov.setName(morg.getName());
//			mov.setOrgLevel(morg.getOrgLevel());
//			mov.setOrgOrder(morg.getOrgOrder());
//			mov.setOrgType(morg.getOrgType().getValue());
//			mov.setParentId(morg.getParentId());
//			mov.setSubMobileOrganizationVos(subOrgVo1);
//			mobileOrgVoList.add(mov);
//		}
//		return mobileOrgVoList;
//	}
	
	@Override
	public List<MobileOrganization> getAllMobileDeptAndUserList() {
		List<MobileOrganization> mobileorgList=new ArrayList<MobileOrganization>();
			String hql=" from MobileOrganization where level=1 order by orgOrder ";
		Map<String, Object> params = Maps.newHashMap();
			 mobileorgList = mobileOrganizationDao.findAllByHQL(hql, params);
		Map<String, Object> params1 = Maps.newHashMap();
			for (MobileOrganization morg : mobileorgList) {
				morg.setUserCount(userDao.findCountHql(" select count(distinct udj.userId) from UserDeptJob udj,User u where udj.userId=u.userId and u.ccpStatus=0 and u.enableFlg=0 and udj.deptId in( select id from Organization where orgLevel = '"+morg.getOrgLevel()+"' )", params1));
				
				morg.setUserList(findUsersByDept(new DataPackage(0,999),morg.getId(),morg.getOrgLevel(),morg.getOrgType().getValue(),morg.getLevel()));
				
				List<MobileOrganization> subOrg = mobileOrganizationDao.getOrganizationBoy(morg.getId(), 2);
				for (MobileOrganization sub : subOrg) {
					sub.setUserList(findUsersByDept(new DataPackage(0,999),sub.getId(),sub.getOrgLevel(),sub.getOrgType().getValue(),sub.getLevel()));
					sub.setUserCount(userDao.findCountHql(" select count(distinct udj.userId) from UserDeptJob udj,User u where udj.userId=u.userId and u.ccpStatus=0 and u.enableFlg=0 and udj.deptId  in( select id from Organization where orgLevel like '"+sub.getOrgLevel()+"%' )", params1));
				}
				morg.setSubMobileOrganizations(subOrg);
			}
		return mobileorgList;
	}
	
	@Override
	public List<MobileOrganization> getAllMobileDeptAndUserList(String type,String servicePath) {
		servicePath=ObjectToFile.getTheFilePath(servicePath,"deptUsers.txt");
		List<MobileOrganization> mobileorgList=new ArrayList<MobileOrganization>();
		if(StringUtils.isNotBlank(type) && type.equals("1")){
			mobileorgList=getAllMobileDeptAndUserList();
			ObjectToFile.writeObject(mobileorgList,servicePath);
		}else {
			try {
				mobileorgList=(List<MobileOrganization>) ObjectToFile.readObject(servicePath);
			} catch (Exception e) {
				mobileorgList=getAllMobileDeptAndUserList();
				ObjectToFile.writeObject(mobileorgList,servicePath);
			}
		}
		return mobileorgList;
	}
	
	@Override
	public List<UserJob> getAllJobAndUserList(String type, String servicePath) {
		servicePath=ObjectToFile.getTheFilePath(servicePath,"jobUsers.txt");
	    List<UserJob> UserJobs = new ArrayList<UserJob>();
		if(StringUtils.isNotBlank(type) && type.equals("1")){
			UserJobs=getAllJobAndUserList();
			ObjectToFile.writeObject(UserJobs,servicePath);
		}else {
			try {
				UserJobs=(List<UserJob>) ObjectToFile.readObject(servicePath);
			} catch (Exception e) {
				UserJobs=getAllJobAndUserList();
				ObjectToFile.writeObject(UserJobs,servicePath);
			}
		}
		
		return UserJobs;
	}
	
	
	
	@Override
	public List<UserMobileVo> findUsersByDept(DataPackage dp, String deptId,
			String deptLevel, String deptType,int level) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("  select ss.* from (select  u.*,min(uj.JOB_LEVEL) as job_level");
		sql.append("  from user u left join user_dept_job udj on u.USER_ID=udj.USER_ID left join organization o on o.id=udj.DEPT_ID");
		sql.append(" left join user_job uj on uj.id=udj.job_id");
		
		sql.append(" where 1=1 and u.ccp_Status=0 and u.ENABLE_FLAG=0 ");
		if(level==2){
			sql.append(" AND  (o.id= :deptId or o.orgLevel like :deptLevel )");
			params.put("deptId", deptId);
			params.put("deptLevel", deptLevel+"%");
		}else{
			sql.append(" and o.id= :deptId ");
			params.put("deptId", deptId);
		}
		sql.append(" group by u.`USER_ID`,u.`NAME`,u.`ACCOUNT`,u.`PSW`,u.`ENABLE_FLAG`,u.`organizationID`,u.`accountExpired`,u.`accountLocked`,u.`credentialsExpired`,u.`role_id`,u.`SEX`,u.`CONTACT`,u.`IDCORD_NO`,u.`SALARY_ACCOUNT`,u.`SALARY_ACCOUNT_BREND`,u.`ENTRY_DATE`,");
		sql.append(" u.`BORTHDAY`,u.`AGE`,u.`SENIORITY`,u.`TEACH_YEARS`,u.`GRADUATE_SCHOOL`,u.`EDUCATION`,u.`CERTIFICATION`,u.`EMENGENT_PERSON`,u.`EMENGENT_PERSON_CONTACT`,u.`ATTANCE_NO`,u.`RESIGN_DATE`,u.`RESIGN_RESULT`,u.`WORK_TYPE`,u.`ADDRESS`,");
		sql.append(" u.`CREATE_USER_ID`,u.`CREATE_TIME`,u.`MODIFY_USER_ID`,u.`MODIFY_TIME`,u.`USE_ALL_ORGANIZATION`,u.`ARCHIVES_PATH`,u.`token`,u.`MAIL_STATUS`,u.`MAIL_ADDR`,u.`CCP_ACCOUNT`,u.`CCP_PWD`,u.`REAL_NAME`,u.`CCP_STATUS`");
		sql.append(" ) ss order by ss.job_level");
		
//		sql.append(" order by uj.job_level ");
		
		dp = userDao.findPageBySql(sql.toString(), dp, true, params);
		List<User> userlist = (List<User>) dp.getDatas();
		
		
	    List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);


		Map<String, Object> params1 = Maps.newHashMap();

	    for (UserMobileVo user : userMobileVo) {
//			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
			user.setDeptId(deptId);
			StringBuffer sqls=new StringBuffer();
			sqls.append(" select udj.* from  ");
			sqls.append(" user_dept_job udj  ");
			sqls.append(" left join organization o on o.id=udj.DEPT_ID");
			sqls.append(" left join user_job uj on uj.id=udj.job_id");
			sqls.append(" where 1=1  and udj.user_id='"+user.getUserId()+"'");
			if(level==2){
				sqls.append(" AND  (o.id='"+deptId+"' or o.orgLevel like '"+deptLevel+"%')");
			}else{
				sqls.append(" and o.id='"+deptId+"'");
			}
			sqls.append(" order by uj.job_level ");
			List<UserDeptJob> userDeptJob = (List<UserDeptJob>) userDeptJobDao.findBySql(sqls.toString(), params1);
			
			
			List<Map> listMap=new ArrayList<Map>();
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				Map deptJobMap=new HashMap();
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
//				if(userDeptJob2.getId().getDeptId().equals(deptId)){
					UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
					Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
					deptJobMap.put("jobName", userJob.getName());
					deptJobMap.put("deptName",dept.getName());
					deptJobMap.put("jobLevel", userJob.getJobLevel());
					listMap.add(deptJobMap);
//					user.setDeptName(dept.getName());
//					user.setJobName(userJob.getJobName());
//				}
			}
			user.setJobDept(listMap);
			user.setContact("");//设置为空，经常有用户打扰领导   2016-05-16   Yao
		}
//	    dp.setDatas(userMobileVo);
		return userMobileVo;
	}

	@Override
	public List<UserMobileVo> findUsersByDeptPC(DataPackage dp, String deptId,
			String deptLevel, String deptType,int level) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("  select ss.* from (select  u.`USER_ID`,u.`NAME`,u.`ACCOUNT`,u.`PSW`,u.`ENABLE_FLAG`,u.`organizationID`,u.`accountExpired`,u.`accountLocked`,u.`credentialsExpired`,u.`role_id`,u.`SEX`,u.`CONTACT`,u.`IDCORD_NO`,u.`SALARY_ACCOUNT`,u.`SALARY_ACCOUNT_BREND`,u.`ENTRY_DATE`, ");
		sql.append(" u.`BORTHDAY`,u.`AGE`,u.`SENIORITY`,u.`TEACH_YEARS`,u.`GRADUATE_SCHOOL`,u.`EDUCATION`,u.`CERTIFICATION`,u.`EMENGENT_PERSON`,u.`EMENGENT_PERSON_CONTACT`,u.`ATTANCE_NO`,u.`RESIGN_DATE`,u.`RESIGN_RESULT`,u.`WORK_TYPE`,u.`ADDRESS`, ");
		sql.append(" u.`CREATE_USER_ID`,u.`CREATE_TIME`,u.`MODIFY_USER_ID`,u.`MODIFY_TIME`,u.`USE_ALL_ORGANIZATION`,u.`ARCHIVES_PATH`,u.`token`,u.`MAIL_STATUS`,u.`MAIL_ADDR`,u.`CCP_ACCOUNT`,u.`CCP_PWD`,u.`REAL_NAME`,u.`CCP_STATUS`,min(uj.JOB_LEVEL) as job_level");
		sql.append("  from user u left join user_dept_job udj on u.USER_ID=udj.USER_ID left join organization o on o.id=udj.DEPT_ID");
		sql.append(" left join user_job uj on uj.id=udj.job_id");
		
		sql.append(" where 1=1 and u.ENABLE_FLAG=0 ");
		if(level==2){
			sql.append(" AND  (o.id= :deptId or o.orgLevel like :deptLevel )");
			params.put("deptId", deptId);
			params.put("deptLevel", deptLevel+"%");
		}else{
			sql.append(" and o.id= :deptId ");
			params.put("deptId", deptId);
		}
		sql.append(" group by u.`USER_ID`,u.`NAME`,u.`ACCOUNT`,u.`PSW`,u.`ENABLE_FLAG`,u.`organizationID`,u.`accountExpired`,u.`accountLocked`,u.`credentialsExpired`,u.`role_id`,u.`SEX`,u.`CONTACT`,u.`IDCORD_NO`,u.`SALARY_ACCOUNT`,u.`SALARY_ACCOUNT_BREND`,u.`ENTRY_DATE`,");
		sql.append(" u.`BORTHDAY`,u.`AGE`,u.`SENIORITY`,u.`TEACH_YEARS`,u.`GRADUATE_SCHOOL`,u.`EDUCATION`,u.`CERTIFICATION`,u.`EMENGENT_PERSON`,u.`EMENGENT_PERSON_CONTACT`,u.`ATTANCE_NO`,u.`RESIGN_DATE`,u.`RESIGN_RESULT`,u.`WORK_TYPE`,u.`ADDRESS`,");
		sql.append(" u.`CREATE_USER_ID`,u.`CREATE_TIME`,u.`MODIFY_USER_ID`,u.`MODIFY_TIME`,u.`USE_ALL_ORGANIZATION`,u.`ARCHIVES_PATH`,u.`token`,u.`MAIL_STATUS`,u.`MAIL_ADDR`,u.`CCP_ACCOUNT`,u.`CCP_PWD`,u.`REAL_NAME`,u.`CCP_STATUS`");
		sql.append(" ) ss order by ss.job_level");
		
//		sql.append(" order by uj.job_level ");
		
		dp = userDao.findPageBySql(sql.toString(), dp, true, params);
		List<User> userlist = (List<User>) dp.getDatas();
		
		
	    List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);
		Map<String, Object> params1 = Maps.newHashMap();
	    for (UserMobileVo user : userMobileVo) {
//			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
			user.setDeptId(deptId);
			StringBuffer sqls=new StringBuffer();
			sqls.append(" select udj.* from  ");
			sqls.append(" user_dept_job udj  ");
			sqls.append(" left join organization o on o.id=udj.DEPT_ID");
			sqls.append(" left join user_job uj on uj.id=udj.job_id");
			sqls.append(" where 1=1  and udj.user_id='"+user.getUserId()+"'");
			if(level==2){
				sqls.append(" AND  (o.id='"+deptId+"' or o.orgLevel like '"+deptLevel+"%')");
			}else{
				sqls.append(" and o.id='"+deptId+"'");
			}
			sqls.append(" order by uj.job_level ");
			List<UserDeptJob> userDeptJob = (List<UserDeptJob>) userDeptJobDao.findBySql(sqls.toString(), params1);
			
			
			List<Map> listMap=new ArrayList<Map>();
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				Map deptJobMap=new HashMap();
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
//				if(userDeptJob2.getId().getDeptId().equals(deptId)){
					UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
					Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
					deptJobMap.put("jobName", userJob.getName());
					deptJobMap.put("deptName",dept.getName());
					deptJobMap.put("jobLevel", userJob.getJobLevel());
					listMap.add(deptJobMap);
//					user.setDeptName(dept.getName());
//					user.setJobName(userJob.getJobName());
//				}
			}
			user.setJobDept(listMap);
		}
//	    dp.setDatas(userMobileVo);
		return userMobileVo;
	}
	
	@Override
	public List<UserMobileVo> findUsersInPC(DataPackage dp) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("  select ss.* from (select  u.`USER_ID`,u.`NAME`,u.`ACCOUNT`,u.`PSW`,u.`ENABLE_FLAG`,u.`organizationID`,u.`accountExpired`,u.`accountLocked`,u.`credentialsExpired`,u.`role_id`,u.`SEX`,u.`CONTACT`,u.`IDCORD_NO`,u.`SALARY_ACCOUNT`,u.`SALARY_ACCOUNT_BREND`,u.`ENTRY_DATE`, ");
		sql.append(" u.`BORTHDAY`,u.`AGE`,u.`SENIORITY`,u.`TEACH_YEARS`,u.`GRADUATE_SCHOOL`,u.`EDUCATION`,u.`CERTIFICATION`,u.`EMENGENT_PERSON`,u.`EMENGENT_PERSON_CONTACT`,u.`ATTANCE_NO`,u.`RESIGN_DATE`,u.`RESIGN_RESULT`,u.`WORK_TYPE`,u.`ADDRESS`, ");
		sql.append(" u.`CREATE_USER_ID`,u.`CREATE_TIME`,u.`MODIFY_USER_ID`,u.`MODIFY_TIME`,u.`USE_ALL_ORGANIZATION`,u.`ARCHIVES_PATH`,u.`token`,u.`MAIL_STATUS`,u.`MAIL_ADDR`,u.`CCP_ACCOUNT`,u.`CCP_PWD`,u.`REAL_NAME`,u.`CCP_STATUS`,min(uj.JOB_LEVEL) as job_level");
		sql.append("  from user u left join user_dept_job udj on u.USER_ID=udj.USER_ID left join organization o on o.id=udj.DEPT_ID");
		sql.append(" left join user_job uj on uj.id=udj.job_id");
		
		sql.append(" where 1=1 and u.ENABLE_FLAG=0 ");
		sql.append(" group by u.`USER_ID`,u.`NAME`,u.`ACCOUNT`,u.`PSW`,u.`ENABLE_FLAG`,u.`organizationID`,u.`accountExpired`,u.`accountLocked`,u.`credentialsExpired`,u.`role_id`,u.`SEX`,u.`CONTACT`,u.`IDCORD_NO`,u.`SALARY_ACCOUNT`,u.`SALARY_ACCOUNT_BREND`,u.`ENTRY_DATE`,");
		sql.append(" u.`BORTHDAY`,u.`AGE`,u.`SENIORITY`,u.`TEACH_YEARS`,u.`GRADUATE_SCHOOL`,u.`EDUCATION`,u.`CERTIFICATION`,u.`EMENGENT_PERSON`,u.`EMENGENT_PERSON_CONTACT`,u.`ATTANCE_NO`,u.`RESIGN_DATE`,u.`RESIGN_RESULT`,u.`WORK_TYPE`,u.`ADDRESS`,");
		sql.append(" u.`CREATE_USER_ID`,u.`CREATE_TIME`,u.`MODIFY_USER_ID`,u.`MODIFY_TIME`,u.`USE_ALL_ORGANIZATION`,u.`ARCHIVES_PATH`,u.`token`,u.`MAIL_STATUS`,u.`MAIL_ADDR`,u.`CCP_ACCOUNT`,u.`CCP_PWD`,u.`REAL_NAME`,u.`CCP_STATUS`");
		sql.append(" ) ss order by ss.job_level");
		
//		sql.append(" order by uj.job_level ");
		
		dp = userDao.findPageBySql(sql.toString(), dp, true, params);
		List<User> userlist = (List<User>) dp.getDatas();
//	    List<UserMobileVo> userMobileVo = HibernateUtils.voListMapping(userlist, UserMobileVo.class);
		List<UserMobileVo> userMobileVo = new ArrayList<UserMobileVo>();
		Map<String, Object> params1 = Maps.newHashMap();
	    for (User user1 : userlist) {
//			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
	    	UserMobileVo user=HibernateUtils.voObjectMapping(user1, UserMobileVo.class);
			user.setDeptId(user1.getOrganizationId());
			StringBuffer sqls=new StringBuffer();
			sqls.append(" select udj.* from  ");
			sqls.append(" user_dept_job udj  ");
			sqls.append(" left join organization o on o.id=udj.DEPT_ID");
			sqls.append(" left join user_job uj on uj.id=udj.job_id");
			sqls.append(" where 1=1  and udj.user_id='"+user.getUserId()+"'");
			sqls.append(" order by uj.job_level ");
			List<UserDeptJob> userDeptJob = (List<UserDeptJob>) userDeptJobDao.findBySql(sqls.toString(), params1);
			List<Map> listMap=new ArrayList<Map>();
			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
				Map deptJobMap=new HashMap();
				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
//				if(userDeptJob2.getId().getDeptId().equals(deptId)){
					UserJob userJob=userJobDao.findById(userDeptJob2.getJobId());
					Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
					deptJobMap.put("jobName", userJob.getName());
					deptJobMap.put("deptName",dept.getName());
					deptJobMap.put("jobLevel", userJob.getJobLevel());
					listMap.add(deptJobMap);
//				}
			}
			user.setJobDept(listMap);
			userMobileVo.add(user);
		}
		return userMobileVo;
	}
	
	@Override
	public List<UserMobileVo> findUsersInPC1(DataPackage dp) {
		
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select u.user_id,u.name,u.organizationID,u.account, ");
		sql.append(" (case (select orgType from organization where id=uj.dept_id) when 'DEPARTMENT' ");
		sql.append(" then (select name from organization where id=uj.dept_id) else '-' end) dept,");
		sql.append(" (select name from organization where id=r.CAMPUS_ID) campus,");
		sql.append(" (select name from organization where id=r.BRANCH_ID) brench,");
		sql.append(" (select job_name from user_job where id = uj.JOB_ID) job_name from user u left join ref_user_org r on u.USER_ID = r.USER_ID ");
		sql.append(" left join user_dept_job uj on u.USER_ID = uj.USER_ID where uj.isMajorRole=0 and u.ENABLE_FLAG=0"); 
		List<UserMobileVo> umList=new ArrayList<UserMobileVo>();
		List<Map<Object, Object>> useMap = userDeptJobDao.findMapBySql(sql.toString(), params);
		//{organizationID=000001, job_name=总裁, user_id=112233, campus=星火集团, name=xhadmin, dept=-, account=xhadmin, brench=星火集团}
		for(Map map:useMap){
			UserMobileVo umv=new UserMobileVo();
			umv.setUserId(map.get("user_id").toString());
			umv.setName(map.get("name").toString());
			umv.setAccount(map.get("account").toString());
			umv.setDeptId(map.get("organizationID").toString());
			List<Map> listMap=new ArrayList<Map>();
			Map deptJobMap=new HashMap();
			deptJobMap.put("jobName", map.get("job_name").toString());
			listMap.add(deptJobMap);
			umv.setJobDept(listMap);
			umList.add(umv);
		}
		return umList;
	}
	
	
	@Override
	public List<User> getUserListHaveMail() {
		// TODO Auto-generated method stub
		List<MailUserView> muvList= mailUserViewDao.getUserListHaveMail();
		List<User> userList = HibernateUtils.voListMapping(muvList, User.class);
//		List<User> userList = userDao.getUserListHaveMail();
//		for(User user : userList) {
//			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(user.getUserId());
//			for (Iterator iterator = userDeptJob.iterator(); iterator.hasNext();) {
//				UserDeptJob userDeptJob2 = (UserDeptJob) iterator.next();
//				
//				UserJob userJob=userJobDao.findById(userDeptJob2.getId().getJobId());
//				Organization dept=organizationDao.findById(userDeptJob2.getId().getDeptId());
//				//设置主部门和主职位
//				if(userDeptJob2.getIsMajorRole()==0){
//					user.setDeptId(dept.getId());
//					user.setDeptName(dept.getName());
//					user.setJobId(userJob.getId());
//					user.setJobName(userJob.getJobName());
//                    break;
//				}
//			}
//		}	
		return userList;
	}

	@Override
	public List<MobileOrganization> getAllMobileDeptListByUserIds(String userIds) {
		Map<String, Object> params = Maps.newHashMap();
		String hql=" from MobileOrganization where orgLevel in(select substring(orgLevel,1,8)"+
	               " from Organization where id in(select deptId from UserDeptJob "+
	               " where userId in (:userIds))) order by orgLevel ";
		String[] ids = StringUtil.replaceSpace(userIds).split(",");
		params.put("userIds", ids);
		List<MobileOrganization> mobileorgList = mobileOrganizationDao.findAllByHQL(hql, params);
		
		List<MobileOrganization> result = new ArrayList<MobileOrganization>();

		
		for (MobileOrganization morg : mobileorgList) {
			
			List<UserMobileVo> users = findUsersByDeptPC(new DataPackage(0,999),morg.getId(),morg.getOrgLevel(),morg.getOrgType().getValue(),morg.getLevel());
			List<UserMobileVo> subUsers = new ArrayList<UserMobileVo>();
			for(UserMobileVo user:users)
			{
				for(String id:ids)
				{
					if(user.getUserId().equals(id.substring(1, id.length()-1)))
					{
						subUsers.add(user);
					}
				}
			}
			morg.setUserList(subUsers);
			List<MobileOrganization> morgSub = new ArrayList<MobileOrganization>();
			
			List<MobileOrganization> secondOrgs = mobileOrganizationDao.getOrganizationBoy(morg.getId(), 2);
			for(MobileOrganization secondOrg:secondOrgs)
			{
				List<UserMobileVo> brenchUsers = findUsersByDeptPC(new DataPackage(0,999),secondOrg.getId(),secondOrg.getOrgLevel(),secondOrg.getOrgType().getValue(),secondOrg.getLevel());
				List<UserMobileVo> brenchUser = new ArrayList<UserMobileVo>();
				for(UserMobileVo user:brenchUsers)
				{
					for(String id:ids)
					{
						if(user.getUserId().equals(id.substring(1, id.length()-1)))
						{
							brenchUser.add(user);
						}
					}
				}
				if(brenchUser.size()>0)
				{
					secondOrg.setUserList(brenchUser);
					morgSub.add(secondOrg);
				}
			}
			morg.setSubMobileOrganizations(morgSub);
			result.add(morg);
		}
		return result;
	}

	
	@Override
	public List<MobileOrganization> getSubMobileDeptListByOrgId(String organizationId) {
		return mobileOrganizationDao.getOrganizationBoy(organizationId, null);
	}

	//获取登录人具体部门职位对应的组织架构
	@Override
	public String getOrgByUserJob(String deptJobId, String orgType) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		User user=this.getCurrentLoginUser();
		Organization organization=organizationDao.findById(user.getOrganizationId());
		sql.append("select * from user_dept_job where JOB_ID= :deptJobId and USER_ID= :userId ORDER BY isMajorRole ASC ");
		params.put("deptJobId", deptJobId);
		params.put("userId", user.getUserId());
		List<UserDeptJob> list=new ArrayList<UserDeptJob>();
		list=userDeptJobDao.findBySql(sql.toString(), params);
		String orgId="";
		
		if(list!=null && list.size()>0){
			UserDeptJob job=list.get(0);
			Organization org=organizationDao.findById(job.getDeptId());
			Organization orgt=new Organization();
			if(!orgType.equals(org.getOrgType().toString()) && org.getOrgType().equals(OrganizationType.BRENCH)){
				for(UserDeptJob dept:list){
					orgt=organizationDao.findById(dept.getDeptId());
					if(!orgt.getOrgType().equals(OrganizationType.BRENCH)){
						org=orgt;
						break;
					}
				}
			}
									
			String brenchId="";
			String campusId="";
			if(org.getOrgType().equals(OrganizationType.BRENCH)){
				brenchId=org.getId();
			}else if(org.getOrgType().equals(OrganizationType.CAMPUS)){
				brenchId=org.getParentId();
				campusId=org.getId();
			}else if(org.getOrgType().equals(OrganizationType.DEPARTMENT)){				
				Organization campus=organizationDao.findById(org.getParentId());
				//分公司下的部门
				if(campus.getOrgType().equals(OrganizationType.BRENCH)){
					campusId="BRENCH";
					brenchId=campus.getId();
				}else{
					brenchId=campus.getParentId();
					campusId=org.getParentId();
				}
				
			}			
			
			if(orgType.equals("BRENCH")){				
				//选择分公司资源池
				orgId=brenchId;				
			}else if(orgType.equals("CAMPUS")){
				//选择校区资源池				
				//咨询师职位对应到分公司但是选择了校区
				if(StringUtils.isNotBlank(campusId)){
					orgId=campusId;
				}else{
					orgId="CAMPUS";
				}
				
			}
			
		}		
		return orgId;
		
	}

	/**
	 * 根据传入的职位Id得到用户
	 */
	@Override
	public List<User> getUserAutoComplateByJobId(String jobId) {
		return userDao.getUserAutoComplateByJobId(jobId);
	}

	/**
	 * 获取需要同步的用户信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<UserOrg> getAllUserInfo() {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select u.user_id,u.name,r.campus_id organizationID,u.ENABLE_FLAG,");
		sql.append(" (case (select orgType from organization where id=uj.dept_id) when 'DEPARTMENT' ");
		sql.append(" then (select name from organization where id=uj.dept_id) else '-' end) dept,");
		sql.append(" (select name from organization where id=r.CAMPUS_ID) campus,");
		
		sql.append(" (select id from organization where id=r.CAMPUS_ID) campusId,");
		
		sql.append(" (select name from organization where id=r.BRANCH_ID) brench,");
		
		sql.append(" (select id from organization where id=r.BRANCH_ID) brenchId,");
		
		sql.append(" (select job_name from user_job where id = uj.JOB_ID) job_name from user u left join ref_user_org r on u.USER_ID = r.USER_ID ");
		sql.append(" left join user_dept_job uj on u.USER_ID = uj.USER_ID where uj.isMajorRole=0 "); 
		
		return userOrgDao.findBySql(sql.toString(), params);
	}
	
	
	public List<UserOrg> getBatchUserInfoByIds(String userIds)
	{
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
//		StringBuilder param = new StringBuilder();
		//分隔userId
		String[] userId = StringUtil.replaceSpace(userIds).split(",");
//		for(String id:userId)
//		{
//			param.append("'").append(id).append("',");
//		}
		
		sql.append(" select u.user_id,u.name,r.campus_id organizationID,u.ENABLE_FLAG,");
		sql.append(" (case (select orgType from organization where id=uj.dept_id) when 'DEPARTMENT' ");
		sql.append(" then (select name from organization where id=uj.dept_id) else '-' end) dept,");
		sql.append(" (select name from organization where id=r.CAMPUS_ID) campus,");
		sql.append(" (select name from organization where id=r.BRANCH_ID) brench,");
		sql.append(" (select job_name from user_job where id = uj.JOB_ID) job_name from user u left join ref_user_org r on u.USER_ID = r.USER_ID ");
		sql.append(" left join user_dept_job uj on u.USER_ID = uj.USER_ID where uj.isMajorRole=0 ");
		sql.append(" and u.user_id in(:userId) ");
		params.put("userId", userId);
//		if(param.length() > 0)
//		{
//			param = param.deleteCharAt(param.length()-1);
//
//		}
		
		return userOrgDao.findBySql(sql.toString(), params);
	}

	/**
	 * 获取某校区某职位的用户列表
	 */
	public List<User> getUserBycampusAndjobSign(String campusId, String userjobSign) {
		return userDao.getUserBycampusAndjobSign(campusId, userjobSign);
	}

	@Override
	public List<Map<String, String>> getUsersByJobCode(String jobCode,
			String orgType, String campusId) {
		String[] jobCodes=StringUtil.replaceSpace(jobCode).split(",");
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql =new StringBuffer();
		sql.append(" select distinct u.USER_ID as userId,u.`NAME` as name,concat('', o.id) as campusId from user u  ");
		sql.append(" left join user_dept_job udj on u.USER_ID=udj.USER_ID ");
		sql.append(" left join user_job uj on udj.JOB_ID=uj.ID ");
		sql.append(" left join organization o on o.id=udj.dept_id ");
		sql.append(" where 1=1 and u.ENABLE_FLAG='0' ");
		
		if(orgType.equals(OrganizationType.BRENCH.getValue())){
			sql.append(" and o.belong in (select parentId from organization where id= :campusId )");
			params.put("campusId", campusId);
		}else if(orgType.equals(OrganizationType.CAMPUS.getValue())){
			sql.append(" and o.belong= :campusId ");
			params.put("campusId", campusId);
		}
		
		if(jobCodes.length>0){
			sql.append(" and uj.JOB_SIGN in (:jobCodes)");
			params.put("jobCodes", jobCodes);
//			for (int i = 1; i < jobCodes.length; i++) {
//				sql.append(" ',"+jobCodes[i]+"'");
//			}

		}else{
			return null;
		}

		List<Map<Object, Object>> mapBySql = userDao.findMapBySql(sql.toString(), params);
		List<Map<String, String>> result = new ArrayList<>();

		for (Map<Object, Object> map : mapBySql){
			Map<String, String> a = new HashMap();
			for (Map.Entry<Object, Object> b :map.entrySet()){
				a.put(b.getKey().toString(), b.getValue().toString());
			}
			result.add(a);
			a = null;
		}
		return result;
	}
	
	

	@Override
	public List<OrganizationVo> getAllOrganizationVo() {
		return HibernateUtils.voListMapping(organizationDao.findAll(), OrganizationVo.class);
	}

	@Override
	public List<NeedSyncUserVo> getAllUserForOAVo() {
		List<NeedSyncUserVo> returnList=new ArrayList<NeedSyncUserVo>();
		Map<String, Object> params = Maps.newHashMap();
		String sql=" select USER_ID,NAME,ACCOUNT,PSW,ENABLE_FLAG,MAIL_ADDR,CONTACT,SEX,WORK_TYPE,REAL_NAME from  user";
		List<Map<Object,Object>> users = userDao.findMapBySql(sql,params);
		for(Map user:users){
			NeedSyncUserVo nuv=new NeedSyncUserVo();
			nuv.setId(user.get("USER_ID").toString());
			nuv.setName(user.get("NAME")==null?"":user.get("NAME").toString());
			nuv.setE_mail(user.get("MAIL_ADDR")==null?"":user.get("MAIL_ADDR").toString());
			nuv.setAccount(user.get("ACCOUNT")==null?"":user.get("ACCOUNT").toString());
			nuv.setPassword(user.get("PSW")==null?"":user.get("PSW").toString());
			nuv.setContact(user.get("CONTACT")==null?"":user.get("CONTACT").toString());
			nuv.setSex(user.get("SEX")==null?"":user.get("SEX").toString());
			nuv.setWorkType(user.get("WORK_TYPE")==null?"":user.get("WORK_TYPE").toString());
			nuv.setEnableFlg(user.get("ENABLE_FLAG")==null?0:Integer.parseInt(user.get("ENABLE_FLAG").toString()));
			nuv.setRealName(user.get("REAL_NAME")==null?"":user.get("REAL_NAME").toString());
			nuv.setDeptJobList(getUserDeptJobList(nuv.getId()));
			returnList.add(nuv);
		}
		return returnList;
	}
	
	@Override
	public void updateUserBySpark(SparkUserVo vo) {
		List<UserDeptJob> oldDeptJobList = new ArrayList<UserDeptJob>();
		List<UserDeptJob> newDeptJobList = new ArrayList<UserDeptJob>();
		Integer oldEnableFlg = 0;
		String oldUserName = "";
		 if(vo==null && StringUtils.isEmpty(vo.getAction())){
			throw new ApplicationException("参数异常");
		 }
		 logger.info("SparkUserVo:" + vo.toString());
		 User user = null;
//		 String depts="";
//		 String jobs="";
		 SparkUserDetailVo origin=vo.getOrigin();
		 SparkUserDetailVo info = new SparkUserDetailVo();
		Map<String, Object> params = Maps.newHashMap();
		params.put("originAccount", origin.getAccount());
		 user= userDao.findOneByHQL(" from User where account= :originAccount ", params);
		 if(user==null && !vo.getAction().equals("create")){
			 vo.setAction("create");
		 } 
		 user = user != null ? user : new User();
		 if(vo.getAction().equals("create")){
		     logger.info("crate user " + origin.getAccount());
			 String roleId ="ROL0000000146";
			 String orgId="000001";
			 putInfoToUser(origin, user);
			 user.setEnableFlg(0);
			 user.setCreateTime(DateTools.getCurrentDateTime());
			 user.setRoleId("ROL0000000146");//默认一个roleID ,手机端不会报错。
			 //主职位
			UserJob uj= null;
			String deptJob= origin.getJob();
			 String [] deptJobs=deptJob.split(",");
			 for (int i = 0; i < deptJobs.length; i++) {
				String jjj=deptJobs[i];
				String [] things=jjj.split("\\|");
				if(things[2].equals("0")){
					uj= userJobService.findUserJobById(things[1]);
					orgId=things[0];
				}
			}

			 if(uj!=null && uj.getName().equals("老师") || uj.getName().equals("咨询师") || uj.getName().equals("学管师")){
				List<Role> role= roleDao.findRoleByName(uj.getName());
				if(role.size()>0){
					roleId+=","+role.get(0).getRoleId();
				}
			 }
			SystemConfig findSc = new SystemConfig();
			findSc.setTag("coreMailDomain");
			SystemConfig domainSc = systemConfigService.getSystemPath(findSc);
			user.setMailAddr(user.getAccount() + "@" + domainSc.getValue());
			userDao.save(user);
			userRoleDao.marginUserRoleList(user.getUserId(), roleId);//系统基本权限

			 int i =0 ;
			 for(String roleID:roleId.split(",")) {//组织架构角色一一对应处理
				 UserOrganizationRole orgRole = new UserOrganizationRole();
				 orgRole.setRole(new Role(roleID));
				 orgRole.setOrganization(new Organization(orgId));
				 orgRole.setUser(user);
				 orgRole.setIsMain(i);
				 userOrganizationRoleDao.save(orgRole);
				 if(i==0){//只有一个主
				 	i++;
				 }
			 }
			info=origin;
		 }else{
		     logger.info("modify user " + origin.getAccount());
			 //通过用户名找到用户
			 oldDeptJobList = getUserDeptJobList(user.getUserId());
			 oldUserName = user.getRealName();
			 oldEnableFlg = user.getEnableFlg();
			 if(vo.getUpdated().getIs_disabled()!=null &&vo.getUpdated().getIs_disabled()){
				 //把userId放进队列里面
				 JedisUtil.lpush("invalidUserInfo".getBytes(), JedisUtil.ObjectToByte(user.getUserId())); 
				 // 无效用户，删除邮箱
//				 mailService.deleteFullMailUser(user);
			 }
			 putInfoToUser(vo.getUpdated(), user);
			 userDao.save(user);
			 info=vo.getUpdated();
		 }
		 userDao.commit();
		
		 String deptJob= info.getJob();
		 String [] deptJobs=deptJob.split(",");
		 /*String newMainDeptId = "";
		 String newMainJobId = "";*/
		 for (int i = 0; i < deptJobs.length; i++) {
			String jjj=deptJobs[i];
			String [] things=jjj.split("\\|");
			if(things[2].equals("0") && vo.getAction().equals("create")){
					user.setOrganizationId(things[0]);
				UserOrganization uo=new UserOrganization();
				uo.setOrganization(new Organization(things[0]));
				uo.setUser(new User(user.getUserId()));
				logger.info("crate UserOrganization " + things[0]);
				userOrganizationDao.save(uo);
			}
			/*if(things[2].equals("0")) {
				newMainDeptId = things[0];
				newMainJobId = things[1];
			}*/
			newDeptJobList.add(new UserDeptJob(user.getUserId(), things[0],things[1],Integer.valueOf(things[2])));
		}
		 
		if (newDeptJobList.size()>0) {
			 try {
				 userDeptJobDao.marginUserDeptJobList(newDeptJobList,user.getUserId());
			 } catch (DuplicateKeyException e) {
				 throw new ApplicationException("角色部门重复，请去掉一组再保存");
			 }
		 }
		
		/*if(StringUtils.isNotBlank(user.getMailAddr()) && user.getMailStatus() != null && user.getMailStatus() == 0) {
			// 修改邮箱密码
			if(StringUtils.isNotBlank(info.getPassword()) && mailService.isMailSysInUse() == true) { //开启邮件系统
				mailService.chageMailUserPassword(user);
			}
			
			// 更新用户姓名
			//更新用户姓名，或换了主部门, 主职位变了
			if((StringUtil.isNotBlank(info.getName()) && StringUtil.isNotBlank(oldUserName) && !oldUserName.equals(info.getName())) || 
				(oldDeptJobList != null && oldDeptJobList.size() > 0  && (!oldDeptJobList.get(0).getDeptId().equals(newMainDeptId) 
					|| !oldDeptJobList.get(0).getJobId().equals(newMainJobId)))){
				UserJob userJob = userJobService.findUserJobById(newMainJobId);
				if(userJob!=null){
					user.setDeptId(newMainDeptId);
					user.setJobName(userJob.getName());
					mailService.updateMailUser(user);					
				}
			}
			
			// 修改邮箱外部联系人
			if(StringUtils.isNotBlank(user.getMailAddr())) {
				//部门职位变了
				if(judgeUserDeptJobIsChange(oldDeptJobList, newDeptJobList) == true) {
					mailService.updateMailListUserOnDeptJobChange(user, oldDeptJobList, newDeptJobList);
				}
			}
		}
		
		 // 开通全职用户邮箱
		 if(vo.getAction().equals("create") // 新建用户
//				 && (info != null && info.getIs_disabled() !=null && !info.getIs_disabled()) // 有效用户(暂时去掉)
				 && user.getWorkType().equals(UserWorkType.FULL_TIME) // 全职
				 && mailService.isMailSysInUse()) {
			 this.createFullMailUser(user.getUserId());
		 }
		 
		 // 启用全职用户(邮箱状态为关闭)
		 if (!vo.getAction().equals("create") // 修改用户
				 && (info != null && info.getIs_disabled() !=null && !info.getIs_disabled()) // 有效用户
				 && user.getWorkType() == UserWorkType.FULL_TIME // 全职
				 && (oldEnableFlg == null || oldEnableFlg != 0) // 旧用户为无效用户
				 && (user.getMailStatus() == null || 0 != user.getMailStatus()) // 旧用户的邮箱为未开通
				 && mailService.isMailSysInUse()) { 
			this.updateFullMailUser(user);
		 }*/

	}
	
	
	
	private List<String> stringToList(String listString) {
		List<String> returnList = new ArrayList<String> ();
		for (String itemString : listString.split(",")) {
			returnList.add(itemString);
		}
		return returnList;
	}
	private boolean judgeUserDeptJobIsChange(List<UserDeptJob> oldDeptJobList, List<UserDeptJob> newDeptJobList) {
		boolean updateMailListFalg = false;
		if((oldDeptJobList == null || oldDeptJobList.size() == 0) && (newDeptJobList != null && newDeptJobList.size() > 0)) {
			updateMailListFalg = true;
		}else if((oldDeptJobList != null && oldDeptJobList.size() > 0) && (newDeptJobList != null && newDeptJobList.size() > 0)){
			 if(oldDeptJobList.size() != newDeptJobList.size()){
					updateMailListFalg = true;
			 }else {
				 for(UserDeptJob oldUdj : oldDeptJobList) {
					 boolean matchFalg = false;
					 String oldDeptId = oldUdj.getDeptId();
					 String oldJobId = oldUdj.getJobId();
					 int oldIsMajor = oldUdj.getIsMajorRole();
					 System.out.println(oldDeptId + "-" + oldJobId + "-" + oldIsMajor);
					 for(UserDeptJob newUdj : newDeptJobList){
						 String newDeptId = newUdj.getDeptId();
						 String newJobId = newUdj.getJobId();
						 int newIsMajor = newUdj.getIsMajorRole();
						 if(oldDeptId.equals(newDeptId) && oldJobId.equals(newJobId) && oldIsMajor == newIsMajor) {
							 matchFalg = true;
						 }
					 }
					 if(matchFalg == false) {  //不一样
						 updateMailListFalg = true;
						 break;
					 }
				 }
			 }
		}
		return updateMailListFalg;
	}
	
	public void putInfoToUser(SparkUserDetailVo info,User user){
		if(StringUtil.isBlank(info.getAccount())){
			throw new ApplicationException("帐号不能为空吧！");
		}
		user.setAccount(info.getAccount());
		user.setName(info.getName());
		user.setWorkType(UserWorkType.valueOf(info.getWork_type()));
		user.setEmployeeNo(info.getEmployee_no());
		user.setSex(info.getSex());
		user.setContact(info.getPhone());
		user.setRealName(info.getName());
		user.setModifyTime(DateTools.getCurrentDateTime());
//		user.setAccount(info.getId());
		if(info.getIs_disabled()!=null && info.getIs_disabled()){
			user.setEnableFlg(1);
			//设置用户老师属性为无效 rdc705970
			UserTeacherAttributeVo userTeacherAttribute = findUserTeacherAttribute(user.getUserId());
			if (userTeacherAttribute!=null){
				userTeacherAttribute.setTeacherSwitch(false);
				editUserTeacherAttribute(userTeacherAttribute);
			}
		}else{
			user.setEnableFlg(0);
		}
		if(StringUtils.isNotBlank(info.getPassword())){
			user.setPassword(info.getPassword());
		}
	}
	
	public void getDeptJobString(SparkUserDetailVo info,String depts,String jobs){
		String deptJob= info.getJob();
		String [] deptJobs=deptJob.split(",");
		for (int i = 0; i < deptJobs.length; i++) {
			String jjj=deptJobs[0];
			
			String [] things=jjj.split("\\|");
			depts+=things[0]+",";
			jobs+=things[1]+",";
			
		}
	}
	
	/**
	 * 查找待编制老师
	 */
	@Override
	public DataPackage getPagePreTeacher(PreTeacherVersionVo preTeacherVersionVo, DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		String hql = " from User u where 1=1 ";
		hql+=" and u.enableFlg = '0' and u.teacherSubjectStatus = 0";
		if(StringUtils.isNotEmpty(preTeacherVersionVo.getTeacherName())){
			hql+=" and u.name like  :teacherName ";
			params.put("teacherName", "%"+preTeacherVersionVo.getTeacherName()+"%");
		}
		if(StringUtils.isNotEmpty(preTeacherVersionVo.getSex())){
			hql+=" and u.sex = :sex ";
			params.put("sex", preTeacherVersionVo.getSex());
		}
		if(StringUtils.isNotEmpty(preTeacherVersionVo.getContact())){
			hql+=" and u.contact like :contact ";
			params.put("contact", preTeacherVersionVo.getContact()+"%");
		}
		
		if(StringUtils.isNotEmpty(preTeacherVersionVo.getBlCampusId())){
			Organization org = organizationDao.findById(preTeacherVersionVo.getBlCampusId());
			hql+=" and u.userId in (select userId from UserDeptJob where jobId = 'USE0000000004' "
					+ " and deptId in (select id from Organization where orgLevel like '" + org.getOrgLevel()+"%' )) "; // job为老师
			params.put("orgLevel", org.getOrgLevel()+"%");
		} else {
			hql+=" and u.userId in (select userId from UserDeptJob where jobId = 'USE0000000004') "; // job为老师
		}
		
		 User usr =  getCurrentLoginUser();
		if(RoleCode.CAMPUS_DIRECTOR.toString().equals(usr.getRoleCode())) {//校区主任
			if (CheckSystemUtils.checkNewOrg()) {
				hql += " and  u.userId in(select user.userId from UserOrganizationRole where role.roleCode ='" + RoleCode.TEATCHER + "' )  ";
			}else{
				hql += " and  u.userId in(select userId from UserRole where roleId in" +
						" (select roleId from Role where roleCode ='" + RoleCode.TEATCHER + "' ) " +
						") ";
			}
		}
		Organization brench =getBelongBranch();//分公司
//		Organization org = getCurrentLoginUserOrganization();
		hql+=" and organizationId in (select id from Organization where orgLevel like :orgLevel )";
		params.put("orgLevel", brench.getOrgLevel()+"%");
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by "+dp.getSidx()+" "+dp.getSord();
		} 
		
		dp=userDao.findPageByHQL(hql, dp, true, params);
		
		List<PreTeacherVersionVo> returnList = new ArrayList<PreTeacherVersionVo>();
        for (Object userObject : dp.getDatas()) {
        	User userDb = (User)userObject;
        	PreTeacherVersionVo vo = HibernateUtils.voObjectMapping(userDb, PreTeacherVersionVo.class);
    		List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(userDb.getUserId());
        	for (UserDeptJob userDeptJob2 : userDeptJob) {
        		if (userDeptJob2.getJobId().equals("USE0000000004")) {
        			Organization dept=organizationDao.findById(userDeptJob2.getDeptId());
        			if(dept!=null) {
        				vo.setBlCampusId(dept.getId());
        				vo.setBlCampusName(dept.getName());
        			}
        		}
			}
        	if (StringUtils.isNotBlank(userDb.getBorthday())) {
        		vo.setAge(DateTools.getAge(userDb.getBorthday()));
        	}
        	if (StringUtils.isNotBlank(userDb.getEntryDate())) {
        		vo.setWorkingYears(DateTools.getAge(userDb.getEntryDate()));
        	}
        	returnList.add(vo);
        }
        dp.setDatas(returnList);
		return dp;
	}
	

	/**
	 * 更新老师的编制状态
	 */
	@Override
	public void updateTeacherSubjectStatus(String userId, int teacherSubjectStatus) {
		userDao.updateTeacherSubjectStatus(userId, teacherSubjectStatus);
	}

	/**
	 * 根据用户ids查找老师名字
	 */
	@Override
	public String getUserNamesByUserIds(String[] userIds) {
//		String userIdsStr = "";
//		for (String userId : userIds) {
//			if (!userIdsStr.contains(userId)) {
//				userIdsStr += "'" + userId + "',";
//			}
//		}
//		String userNames = "";
//		if (StringUtil.isNotBlank(userIdsStr)) {
//
		String	userNames = userDao.getUserNamesByUserIds(userIds) ;
//		}
		return userNames;
	}

	@Override
	public Map<String, Object> getUserInfo(DataPackage dataPackage, String teacherId, String phone, String name,Integer type) {
		return getUserInfo(dataPackage, teacherId, phone, name, type,null);
	}

	@Override
	public Map<String, Object> getUserInfo(DataPackage dataPackage, String teacherId, String phone, String name,Integer type,String employeeNo) {
		Map<String, Object> map = new HashMap<String, Object>();

		List<OrgUserInfoVo> result = new ArrayList<OrgUserInfoVo>();
		StringBuilder query = new StringBuilder();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		Map<String, Object> params = Maps.newHashMap();

		Integer start = (dataPackage.getPageNo() - 1) * dataPackage.getPageSize();

		//修改需求 20170424
		if(type==1){
			query.append(" select u.ACCOUNT as account,u.USER_ID as id,u.`NAME` as `name`,u.organizationID as orgId,o.`name` as orgName,o.city_id as regionId,r.`name` as regionName,u.CREATE_TIME as createDate,u.MODIFY_TIME as updateDate ");
			query.append(" ,u.MAIL_ADDR as email,uj.ID as jobId,uj.JOB_NAME as jobName,u.employee_No as employeeNo ");
			query.append(" from `user` u ");
			query.append(" left join organization o on o.id = u.organizationID ");
			query.append(" left join region r on r.id = o.city_id ");
			query.append(" left join user_dept_job udj on u.USER_ID = udj.USER_ID ");
			query.append(" left join user_job uj on udj.JOB_ID = uj.ID ");
			query.append(" where udj.isMajorRole = 0 and u.ENABLE_FLAG = '0' ");
			if (StringUtils.isNotBlank(teacherId)) {
				query.append(" and u.USER_ID = :teacherId ");
				params.put("teacherId", teacherId);
			}
			if (StringUtils.isNotBlank(phone)) {
				query.append(" and u.CONTACT = :phone ");
				params.put("phone", phone);
			}
			if(StringUtils.isNotBlank(name)){
				query.append(" and u.`NAME` like :name ");
				params.put("name", "%"+name+"%");
			}
			if(StringUtils.isNotBlank(employeeNo)){
				query.append(" and u.`employee_No` = :employeeNo ");
				params.put("employeeNo", employeeNo);
			}

			String count = "select count(*) from ( " + query.toString() + " ) countall ";
			query.append(" limit " + start + "," + dataPackage.getPageSize());
			List<Map<Object, Object>> list = userDao.findMapBySql(query.toString(),params);
			query.delete(0, query.length());
			OrgUserInfoVo userInfoVo = null;
			for(Map<Object, Object> object:list){
				userInfoVo = new OrgUserInfoVo();
				userInfoVo.setAccount(object.get("account")!=null?object.get("account").toString():"");
				userInfoVo.setId(object.get("id")!=null?object.get("id").toString():"");
				userInfoVo.setName(object.get("name")!=null?object.get("name").toString():"");
				userInfoVo.setLevel("");// 教师等级 暂时为空没这个字段
				userInfoVo.setOrgId(object.get("orgId")!=null?object.get("orgId").toString():"");
				userInfoVo.setOrgName(object.get("orgName")!=null?object.get("orgName").toString():"");
				userInfoVo.setRegionId(object.get("regionId")!=null?object.get("regionId").toString():"");
				userInfoVo.setRegionName(object.get("regionName")!=null?object.get("regionName").toString():"");
				userInfoVo.setCreateDate(object.get("createDate")!=null?object.get("createDate").toString():"");
				userInfoVo.setUpdateDate(object.get("updateDate")!=null?object.get("updateDate").toString():"");
				userInfoVo.setEmail(object.get("email")!=null?object.get("email").toString():"");
				userInfoVo.setJobId(object.get("jobId")!=null?object.get("jobId").toString():"");
				userInfoVo.setJobName(object.get("jobName")!=null?object.get("jobName").toString():"");
				userInfoVo.setEmployeeNo(object.get("employeeNo")!=null?object.get("employeeNo").toString():"");
				if(object.get("orgId")!=null){
					Organization org_branch = this.getBelongBranchByOrgId(object.get("orgId").toString());
					Organization org_campus = this.getBelongCampusByOrgId(object.get("orgId").toString());
					if(org_branch!=null && org_branch.getOrgType() == OrganizationType.BRENCH){
						userInfoVo.setBranchId(org_branch.getId());
						userInfoVo.setBranchName(org_branch.getName());
					}
					if(org_campus!=null && org_campus.getOrgType() == OrganizationType.CAMPUS){
						userInfoVo.setCampusId(org_campus.getId());
						userInfoVo.setCampusName(org_campus.getName());
					}
				}
				result.add(userInfoVo);

			}
			Integer totalCount = userDao.findCountSql(count,params);
			Integer pageSize = dataPackage.getPageSize();
			Integer totalPage = totalCount / pageSize;
			if (totalCount % pageSize > 0) {
				totalPage++;
			}
			resultMap.put("totalPage", totalPage);
			resultMap.put("totalCount", totalCount);
			resultMap.put("item", result);
			map.put("resultStatus", 200);
			map.put("resultMessage", "用户列表");
			map.put("result", resultMap);
			return map;
		}else if(type==2){
			query.append(" select u.ACCOUNT as account,u.USER_ID as id,u.`NAME` as `name`,u.organizationID as orgId,o.`name` as orgName,uj.ID as jobId,uj.JOB_NAME as jobName ");
			query.append(" from `user` u,organization o,user_dept_job udj,user_job uj ");
			query.append(" where o.id = u.organizationID ");
			query.append(" and u.USER_ID = udj.USER_ID ");
			query.append(" and udj.JOB_ID = uj.ID ");
			query.append(" and u.ENABLE_FLAG = '0' and udj.isMajorRole = 0 ");
			if (StringUtils.isNotBlank(teacherId)) {
				query.append(" and u.USER_ID = :teacherId ");
				params.put("teacherId", teacherId);
			}
			if (StringUtils.isNotBlank(phone)) {
				query.append(" and u.CONTACT = :phone ");
				params.put("phone", phone);
			}
			if(StringUtils.isNotBlank(name)){
				query.append(" and u.`NAME` like :name ");
				params.put("name", "%"+name+"%");
			}
			if(StringUtils.isNotBlank(employeeNo)){
				query.append(" and u.`employee_No` = :employeeNo ");
				params.put("employeeNo", employeeNo);
			}
			String count = "select count(*) from ( " + query.toString() + " ) countall ";
			query.append(" limit " + start + "," + dataPackage.getPageSize());
			List<Map<Object, Object>> list = userDao.findMapBySql(query.toString(),params);
			query.delete(0, query.length());
			OrgUserInfoVo userInfoVo = null;
			for(Map<Object, Object> object:list){
				userInfoVo = new OrgUserInfoVo();
				userInfoVo.setAccount(object.get("account")!=null?object.get("account").toString():"");
				userInfoVo.setId(object.get("id")!=null?object.get("id").toString():"");
				userInfoVo.setName(object.get("name")!=null?object.get("name").toString():"");
				userInfoVo.setOrgId(object.get("orgId")!=null?object.get("orgId").toString():"");
				userInfoVo.setOrgName(object.get("orgName")!=null?object.get("orgName").toString():"");
				userInfoVo.setJobId(object.get("jobId")!=null?object.get("jobId").toString():"");
				userInfoVo.setJobName(object.get("jobName")!=null?object.get("jobName").toString():"");
				result.add(userInfoVo);
			}
			Integer totalCount = userDao.findCountSql(count,params);
			Integer pageSize = dataPackage.getPageSize();
			Integer totalPage = totalCount / pageSize;
			if (totalCount % pageSize > 0) {
				totalPage++;
			}
			resultMap.put("totalPage", totalPage);
			resultMap.put("totalCount", totalCount);
			resultMap.put("item", result);
			map.put("resultStatus", 200);
			map.put("resultMessage", "用户列表");
			map.put("result", resultMap);
			return map;
		}else{
			return map;
		}



	}


	/**
	 * 根据账户查找用户
	 */
	@Override
	public User findUserByAccount(String account) {
		return userDao.findUserByAccount(account);
	}

	
	@Override
	public User loadUserById(String userId) {
		return userDao.findById(userId);
	}
	@Override
	public String getUserRoleSign(String userId) {
		StringBuffer sqls=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sqls.append(" select uj.JOB_SIGN jobSign from  ");
		sqls.append(" user_dept_job udj  ");
		sqls.append(" left join organization o on o.id=udj.DEPT_ID");
		sqls.append(" left join user_job uj on uj.id=udj.job_id");
		sqls.append(" where udj.user_id= :userId and udj.isMajorRole=0");

		params.put("userId", userId);
		
		List<Map<Object, Object>> userDeptJob = userDeptJobDao.findMapBySql(sqls.toString(), params);
		if(userDeptJob.size()>0){
			if(userDeptJob.get(0).get("jobSign")!=null){
				return userDeptJob.get(0).get("jobSign").toString();
			}else{
				return null;
			}
			
		}
		return null;
	}
	
	

	@Override
	public List<Map<String, String>> getUserByOrganizationAndRoleCode(String orgLevel, List<String> roleCodes) {
		StringBuilder query = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
        if(roleCodes.size()==0){
        	roleCodes.add(RoleCode.RECEPTIONIST.getValue());
        	roleCodes.add(RoleCode.OPERATION_DIRECTOR.getValue());
        }
		//String[] roleCodesArray = (String[]) roleCodes.toArray();
//		StringBuilder roleCode = new StringBuilder();
//        for(String s:roleCodes){
//        	roleCode.append("'"+s+"',");
//        }
		query.append(
				"select u.USER_ID as userId from `user` u,organization o WHERE u.organizationID=o.id and u.USER_ID in (SELECT ur.userID from user_role ur WHERE ur.roleID in (SELECT r.id from role r WHERE r.roleCode in(:roleCodesArray) )) ");
		query.append(" and u.ENABLE_FLAG = '0' and o.orgLevel LIKE :orgLevel ");
		params.put("roleCodesArray", roleCodes);
		params.put("orgLevel", orgLevel+"%");
		List<Map<Object, Object>> list = userDao.findMapBySql(query.toString(), params);
		List<Map<String, String>> result = new ArrayList<>();
		for (Map<Object, Object> map : list){
			Map<String, String> a =  new HashMap<>();
			for (Map.Entry<Object, Object> b : map.entrySet()){
				a.put(b.getKey().toString(), b.getValue().toString());
			}
			result.add(a);
			a = null;
		}

		return result;
	}

	@Override
	public List<Map<String, String>> getUserByOrganizationAndRoleCodeNew(String orgLevel, List<String> roleCodes) {
		StringBuilder query = new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		if(roleCodes.size()==0){
			roleCodes.add(RoleCode.RECEPTIONIST.getValue());
			roleCodes.add(RoleCode.OPERATION_DIRECTOR.getValue());
		}
		query.append(
				"select u.USER_ID as userId from `user` u,organization o WHERE u.organizationID=o.id and u.USER_ID in (SELECT ur.user_ID from user_organization_role ur WHERE ur.role_ID in (SELECT r.id from role r WHERE r.roleCode in(:roleCodesArray) )) ");
		query.append(" and u.ENABLE_FLAG = '0' and o.orgLevel LIKE :orgLevel ");
		params.put("roleCodesArray", roleCodes);
		params.put("orgLevel", orgLevel+"%");
		List<Map<Object, Object>> list = userDao.findMapBySql(query.toString(), params);
		List<Map<String, String>> result = new ArrayList<>();
		for (Map<Object, Object> map : list){
			Map<String, String> a =  new HashMap<>();
			for (Map.Entry<Object, Object> b : map.entrySet()){
				a.put(b.getKey().toString(), b.getValue().toString());
			}
			result.add(a);
			a = null;
		}

		return result;
	}
	
	@Override
	public List<String> getUserRoleSignFromRole(String userId){
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return getUserRoleSignFromRoleNew(userId);
		}else{
			return getUserRoleSignFromRoleOld(userId);
		}
	}

	public List<String> getUserRoleSignFromRoleOld(String userId){
		StringBuffer sqls=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		sqls.append(" SELECT r.ROLE_SIGN as roleSign from user_role ur,role r WHERE ur.roleID = r.id and ur.userID = :userId ");
		List<Map<Object,Object>> list =userRoleDao.findMapBySql(sqls.toString(), params);
		List<String> result = new ArrayList<>();
		if(list.size()>0){
			for(Map<Object,Object>map:list){
				result.add(map.get("roleSign").toString());
			}
			return result;
		}
		return null;
	}

	@Override
	public List<String> getUserRoleSignFromRoleNew(String userId){
		StringBuffer sqls=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		sqls.append(" SELECT r.ROLE_SIGN as roleSign from user_organization_role ur,role r WHERE ur.role_ID = r.id and ur.user_ID = :userId ");
		List<Map<Object,Object>> list =userRoleDao.findMapBySql(sqls.toString(), params);
		List<String> result = new ArrayList<>();
		if(list.size()>0){
			for(Map<Object,Object>map:list){
				result.add(map.get("roleSign").toString());
			}
			return result;
		}
		return null;
	}
	
	
	@Override
	public List<Resource> getResourcesByUserIdRoleId(String userId, String type, List<Role> roles) {
		StringBuffer sql =new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select distinct r.* from user u ");
		sql.append(" left join user_organization_role ur on ur.user_ID=u.USER_ID");
		sql.append(" left join role_resource rr on ur.role_ID=rr.roleID");
		sql.append(" left join resource r on r.ID=rr.resourceID");
		
		sql.append(" where 1=1 AND r.ID IS NOT NULL ");
		if(StringUtils.isNotBlank(userId)){
			sql.append(" and u.user_id= :userId ");
			params.put("userId", userId);
		}
		
		if(StringUtils.isNotBlank(type)){
			sql.append(" and r.rtype= :type ");
			params.put("type", type);
		}

		if(roles!=null && roles.size()>0){
			List<String> r = new ArrayList<>();
//        	StringBuffer roleIds =  new StringBuffer();
        	for(Role role:roles){
//        		roleIds.append("'"+role.getRoleId()+"',");
				r.add(role.getRoleId());
        	}
        	r.add("'ROL0000000146'");
//        	roleIds.append("'ROL0000000146',");
        	if(r.size()>1){
				//String[] rArray = (String[])r.toArray();
				params.put("rArray", r);
				sql.append(" and rr.roleID in (:rArray)");
        	}
        }
		
		return resourceDao.findBySql(sql.toString(), params);


	}

	@Override
	public Response changeUserRole(String password,String type) {
		Response response  = new Response();
	    //当前登录者
		if(password==null){
			response.setResultCode(-1);
			response.setResultMessage("密码不能为空");		
		}
		if(type==null){
			response.setResultCode(-1);
			response.setResultMessage("切换失败");				
		}
		User user = this.getCurrentLoginUser();
		if(user==null) {
			response.setResultCode(-1);
			response.setResultMessage("用户已经退出登录");
		}
		if(response.getResultCode()==-1){
			return response;
		}
		
		List<Role> roles = this.getRoleByUserId(user.getUserId());		
		List<ResourceGrantedVo> resources = new ArrayList<ResourceGrantedVo> ();
		Short roleLevel=0;
		String roleCodes="";		
		Boolean flag = false;
		Boolean hasReceptionist = false;
		Role receptionistRole = null;
		List<Role> roleList = new ArrayList<>();
		
		for (Role role : roles) {
			//硬编码 判断是否有前台权限
			if(!flag){
				hasReceptionist = hasReceptionist(role);
				if(hasReceptionist){
					flag = true;
					receptionistRole = role;
					roleList.add(receptionistRole);
				}
			}
			if(role.getRoleId().equals("ROL0000000146")){
				roleList.add(role);
			}
		}
		if(type.equals("0")){

			//切换到前台模式 不需要密码校验			
			for(Role temp:roleList){
				if (temp.getRoleCode() != null) {
					if(roleCodes.equals("")){
						roleCodes+=temp.getRoleCode().getValue();
					}else{
						roleCodes+=","+temp.getRoleCode().getValue();
					}
				}
				if (roleLevel == 0) {
					roleLevel = temp.getRoleLevel();
				}else if(temp.getRoleLevel()<roleLevel){
					roleLevel=temp.getRoleLevel();
				}
			}
			if(hasReceptionist){
				resources.addAll(HibernateUtils.voListMapping(this.getResourcesByUserIdRoleId(user.getUserId(),"",roleList), ResourceGrantedVo.class));
				user.setRoleCode(roleCodes);
				user.setRoleLevel(roleLevel);
				user.setRole(roleList);
			}
			//保存切换到前台模式的记录到数据库
			ChangeUserRoleRecord record = new ChangeUserRoleRecord();
			record.setUserId(user.getUserId());
		    record.setUserName(user.getName());
		    
		    List<UserDeptJob> list =getUserDeptJobList(user.getUserId());
		    for(UserDeptJob deptJob:list){
		    	if(deptJob.getIsMajorRole()==0){
				    String deptId =deptJob.getDeptId();
				    record.setDeptId(deptId);
				    Organization dept = organizationDao.findById(deptId);
				    record.setDeptName(dept!=null?dept.getName():"");	
		    	}
		    }

		    Organization campus = getBelongCampusByOrgId(user.getOrganizationId());
		    Organization branch = getBelongBranchByOrgId(user.getOrganizationId());
		    record.setCampusId(campus!=null?campus.getId():"");
		    record.setCampusName(campus!=null?campus.getName():"");
		    record.setBranchId(branch!=null?branch.getId():"");
		    record.setBranchName(branch!=null?branch.getName():"");
		    record.setCreateTime(DateTools.getCurrentDateTime());	    
		    changeUserRoleRecordDao.save(record);
		
			
			
		}else if(type.equals("1")){
			//切换到普通模式
			//校验密码
			if(password.equals(user.getPassword())){
				//密码校验成功
				//切换到普通模式 不去掉前台功能 20170224---xiaojinwang
				//roles.remove(receptionistRole);
				for(Role temp:roles){
					if (temp.getRoleCode() != null) {
						if(roleCodes.equals("")){
							roleCodes+=temp.getRoleCode().getValue();
						}else{
							roleCodes+=","+temp.getRoleCode().getValue();
						}
					}
					if (roleLevel == 0) {
						roleLevel = temp.getRoleLevel();
					}else if(temp.getRoleLevel()<roleLevel){
						roleLevel=temp.getRoleLevel();
					}
				}
				resources.addAll(HibernateUtils.voListMapping(this.getResourcesByUserIdRoleId(user.getUserId(),"",roles), ResourceGrantedVo.class));
				user.setRoleCode(roleCodes);
				user.setRoleLevel(roleLevel);
				user.setRole(roles);				
				
			}else{
				response.setResultCode(-1);
				response.setResultMessage("密码错误");
				return response;
			}
			
		}		
		resources.addAll(HibernateUtils.voListMapping(this.getAllAnonymouslyResourceList(), ResourceGrantedVo.class));	
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.addAll(resources);
		user.setAuthorities(authorities);
		
		//更新springsecurity
//		UserDetailsService userDetailsService =   
//      	      (UserDetailsService)ApplicationContextUtil.getContext().getBean("userDetailsService");  
    	//根据用户名username加载userDetails  
    	UserDetails userDetails = new UserDetailsImpl(user);
    	PreAuthenticatedAuthenticationToken authentication =   
      	      new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());
    	SecurityContextHolder.getContext().setAuthentication(authentication);  	
		return response;
	}
	private Boolean hasReceptionist(Role role){
		if(role.getRoleCode()!=null && role.getRoleCode()==RoleCode.RECEPTIONIST){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<User> getAllInvalidUsers() {
		Map<String, Object> params = Maps.newHashMap();
		return userDao.findBySql("select * from user where ENABLE_FLAG = 1 ", params);
	}
	
	
    /**
     * 根据roleCode和orgLevel获取对应角色用户
     *
     * @param roleCodes  若有多个roleCode则使用英文状态下的逗号进行,分割
     * @param orgLevel
     * @return
     */
    @Override
    public List<User> getUserByRoldCodesAndOrgLevel(String roleCodes, String orgLevel) {
        return userDao.getUserByRoldCodes(roleCodes, orgLevel);
    }

	@Override
	public List<User> getUserByJobSignAndOrgLevel(String jobSign, String orgLevel) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select distinct u.* from user u,user_dept_job udj where u.USER_ID=udj.USER_ID  ");
	    sql.append(" and u.userId in (select uo.user_ID from user_organization_role uo where uo.organization_ID in (select id from organization where orgLevel like :orgLevel ))");
		sql.append(" and udj.JOB_ID = (select ID from user_job where JOB_SIGN = :jobSign )");

		params.put("orgLevel", orgLevel+"%");
		params.put("jobSign", jobSign);
		return userDao.findBySql(sql.toString(), params);
	}

	@Override
	public List<Role> loadRoleByUserId(String userId) {
		return userRoleDao.loadRoleByUserId(userId);
	}

	@Override
	public Organization getUserMainDeptByUserId(String userId) {
		StringBuffer sqls=new StringBuffer();

		Map<String, Object> params = new HashMap<>();

		sqls.append(" select o.* from  ");
		sqls.append(" user_dept_job udj  ");
		sqls.append(" left join organization o on o.id=udj.DEPT_ID");
		sqls.append(" where udj.user_id= :userId and udj.isMajorRole=0");
		params.put("userId", userId);

		List<Organization> userDepts = organizationDao.findBySql(sqls.toString(), params);
		if(userDepts.size()>0){
			return userDepts.get(0);
		}
		return null;
	}

	/**
	 * 创建邮箱用户包括外部联系人
	 * @param userId
	 */
	private void createFullMailUser(String userId) {
		User user = this.getUserById(userId);
		//判断用户所在部门是否为空
		if(user != null && StringUtil.isNotBlank(user.getDeptId())) {
			String newMailAddr = mailService.createMailUser(user);
			user.setMailAddr(newMailAddr);
			mailService.createMailExternalContract(user);
			if(StringUtil.isNotBlank(newMailAddr)) {
				this.createUserMailAddr(user.getUserId(), newMailAddr);
			}
		}
	}
	
	/**
	 * 修改用户，启用全职用户(邮箱状态为关闭)
	 * @param user
	 */
	private void updateFullMailUser(User user) {
		if(StringUtils.isNotBlank(user.getMailAddr())) {
			boolean flag = mailService.changeMailUserStatus(user.getUserId(), 0);
			if(flag == true) user.setMailStatus(0);
		} else {  //创建企业邮箱
			//判断用户所在部门是否为空
			user = this.getUserById(user.getUserId());
			if(user != null && StringUtil.isNotBlank(user.getDeptId())) {
				String newMailAddr = mailService.createMailUser(user);
				user.setMailAddr(newMailAddr);
				mailService.createMailExternalContract(user);
				if(StringUtil.isNotBlank(newMailAddr)) {
					this.createUserMailAddr(user.getUserId(), newMailAddr);
				}
			}
		}
	}

	@Override
	public Map<String, String> getUserMainDeptAndBelongByUserId(String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
        StringBuffer sqls=new StringBuffer();		
		sqls.append(" select o.belong,udj.JOB_ID jobId from ");
		sqls.append(" user_dept_job udj,organization o  ");
		sqls.append(" where o.id=udj.DEPT_ID and udj.user_id= :userId and udj.isMajorRole=0");

		List<Map<Object,Object>> list = organizationDao.findMapBySql(sqls.toString(),params);
		if(list!=null && list.size()>0){
			Map temp = list.get(0);
			return (Map<String, String>)temp;
		}
		return null;
	}

	@Override
	public List<String> getUserAllRoleSign(String userId) {
		StringBuffer sqls=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		List<String> result = new ArrayList<>();
		sqls.append(" select uj.JOB_SIGN jobSign from  ");
		sqls.append(" user_dept_job udj  ");
		sqls.append(" left join organization o on o.id=udj.DEPT_ID");
		sqls.append(" left join user_job uj on uj.id=udj.job_id");
		sqls.append(" where udj.user_id= :userId");

		params.put("userId", userId);
		
		List<Map<Object, Object>> userDeptJob = userDeptJobDao.findMapBySql(sqls.toString(), params);
		if(userDeptJob.size()>0){
			for(Map<Object, Object> map:userDeptJob){
				if(map.get("jobSign")!=null){
					result.add(map.get("jobSign").toString());
				}
			}			
		}
		return result;
	}

    @Override
    public void processOssCallbackWithRedis(String ossCallbackBody) {
		JSONObject jsonObject = JSONObject.fromObject(ossCallbackBody);
		if (jsonObject.get("object") != null){
			if(jsonObject.get("x:uploadid") != null){
				String uploadId = jsonObject.getString("x:uploadid");
				String result = JedisUtil.get(uploadId);
				if (StringUtil.isNotBlank(result)){

				}else {
					JedisUtil.set(uploadId.getBytes(), jsonObject.getString("object").getBytes(), 60 * 10);
				}
			}
		}
    }

    @Override
    public void editUserTeacherAttribute(UserTeacherAttributeVo attributeVo) {
		UserTeacherAttribute userTeacherAttribute = getUserTeacherAttributeByUserId(attributeVo.getUserId());
		if (userTeacherAttribute == null){
			userTeacherAttribute = new UserTeacherAttribute(attributeVo.getUserId());
			userTeacherAttribute.setRecommendStatus(RecommendStatus.NOT_RECOMMEND);
		}
		if (!attributeVo.getTeacherSwitch()) {
		    userTeacherAttribute.setRecommendStatus(RecommendStatus.NOT_RECOMMEND);
		}
		userTeacherAttribute.setTeacherSwitch(attributeVo.getTeacherSwitch());
		userTeacherAttribute.setPicUrl(attributeVo.getPicUrl());
		userTeacherAttribute.setVideoUrl(attributeVo.getVideoUrl());
		userTeacherAttribute.setUniversity(attributeVo.getUniversity());
		userTeacherAttribute.setDegree(attributeVo.getDegree());
		if (StringUtil.isNotBlank(attributeVo.getGradeId())){
			userTeacherAttribute.setGradeDict(dataDictDao.findById(attributeVo.getGradeId()));
		}
		if (StringUtil.isNotBlank(attributeVo.getSubjectId())){
			userTeacherAttribute.setSubject(dataDictDao.findById(attributeVo.getSubjectId()));
		}
		userTeacherAttribute.setRemark(attributeVo.getRemark());
		userTeacherAttribute.setTeachingStyle(attributeVo.getTeachingStyle());
		userTeacherAttributeDao.save(userTeacherAttribute);
	}

	private UserTeacherAttribute getUserTeacherAttributeByUserId(String userId) {
		String hql = " from UserTeacherAttribute where userId= :userId";
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		return userTeacherAttributeDao.findOneByHQL(hql, params);
	}

	@Override
	public UserTeacherAttributeVo findUserTeacherAttribute(String userId) {
		UserTeacherAttribute userTeacherAttribute = getUserTeacherAttributeByUserId(userId);
		if (userTeacherAttribute!=null){
			UserTeacherAttributeVo userTeacherAttributeVo = HibernateUtils.voObjectMapping(userTeacherAttribute, UserTeacherAttributeVo.class);
			return userTeacherAttributeVo;
		}

		return null;
	}

	/**
	 * 判断用户是否处于紧急状态
	 *
	 * @param orgs
	 * @return
	 */
	@Override
	public Boolean isUserStateOfEmergency(List<Organization> orgs) {
		Boolean stateOfEmergencyFlag = false;
		for (Organization o : orgs){
			if (o.getStateOfEmergency() == StateOfEmergency.EMERGENCY){
				stateOfEmergencyFlag = true;
				break;
			}
		}
		return stateOfEmergencyFlag;
	}

	/**
	 * 获取所有禁用的邮件用户
	 */
    @Override
    public List<User> listDisabledHadMailUser() {
        return userDao.listDisabledHadMailUser();
    }


	//    /**
//	 * 创建邮箱用户包括外部联系人
//	 * @param userId
//	 */
//	private void createFullMailUser(String userId) {
//		User user = this.getUserById(userId);
//		//判断用户所在部门是否为空
//		if(user != null && StringUtil.isNotBlank(user.getDeptId())) {
//			String newMailAddr = mailService.createMailUser(user);
//			user.setMailAddr(newMailAddr);
//			mailService.createMailExternalContract(user);
//			if(StringUtil.isNotBlank(newMailAddr)) {
//				this.createUserMailAddr(user.getUserId(), newMailAddr);
//			}
//		}
//	}
//	
//	/**
//	 * 修改用户，启用全职用户(邮箱状态为关闭)
//	 * @param user
//	 */
//	private void updateFullMailUser(User user) {
//		if(StringUtils.isNotBlank(user.getMailAddr())) {
//			boolean flag = mailService.changeMailUserStatus(user.getUserId(), 0);
//			if(flag == true) user.setMailStatus(0);
//		} else {  //创建企业邮箱
//			//判断用户所在部门是否为空
//			user = this.getUserById(user.getUserId());
//			if(user != null && StringUtil.isNotBlank(user.getDeptId())) {
//				String newMailAddr = mailService.createMailUser(user);
//				user.setMailAddr(newMailAddr);
//				mailService.createMailExternalContract(user);
//				if(StringUtil.isNotBlank(newMailAddr)) {
//					this.createUserMailAddr(user.getUserId(), newMailAddr);
//				}
//			}
//		}
//	}

	@Override
	public User findUserByAccountContact(String account, String contact) {
		// TODO Auto-generated method stub
		return userDao.findUserByAccountContact(account,contact);
	}

	@Override
	public User findUserByEmployeeNo(String employeeNo) {
		// TODO Auto-generated method stub
		return userDao.findUserByEmployeeNo(employeeNo);
	}

	@Override
	public Response findUserByUserForTransaferVo(UserForTransaferVo userForTransaferVo) {
		// TODO Auto-generated method stub
		User user = null;
		if(userForTransaferVo!=null) {
			//工号
			if(StringUtils.isNotBlank(userForTransaferVo.getEmployeeNo())) {
	    		user = this.findUserByEmployeeNo(userForTransaferVo.getEmployeeNo());
	    	//账号和电话
	    	//}else if(StringUtils.isNotBlank(userForTransaferVo.getAccount())&&StringUtils.isNotBlank(userForTransaferVo.getContact())){
	    	//	user = this.findUserByAccountContact(userForTransaferVo.getAccount(), userForTransaferVo.getContact());
	    	//姓名和电话
	    	}else if(StringUtils.isNotBlank(userForTransaferVo.getName())&&StringUtils.isNotBlank(userForTransaferVo.getContact())){
	    		user = this.findUserByNameContact(userForTransaferVo.getName(), userForTransaferVo.getContact());
	    	}else {
	    		return new Response(ErrorCode.LIVE_USER_PARAM_EMPTY.getErrorCode(),ErrorCode.LIVE_USER_PARAM_EMPTY.getErrorString());
	    	}
		}else {
			return new Response(ErrorCode.LIVE_USER_PARAM_EMPTY.getErrorCode(),ErrorCode.LIVE_USER_PARAM_EMPTY.getErrorString());
		}
		
    	if(user == null) {
    		return new Response(ErrorCode.LIVE_USER_EMPTY.getErrorCode(),ErrorCode.LIVE_USER_EMPTY.getErrorString());
    	}
		return new Response(200,"",user);
	}

	private User findUserByNameContact(String name, String contact) {
		// TODO Auto-generated method stub
		return userDao.findUserByNameContact(name,contact);
	}

	@Override
	public Response getUserDetail(UserForTransaferVo userForTransaferVo) {
		// TODO Auto-generated method stub
		Response response = this.findUserByUserForTransaferVo(userForTransaferVo);
		if(200 != response.getResultCode()) {
			return response;
		}
		User user =  (User) response.getData();
		userForTransaferVo.setAccount(user.getAccount());
		userForTransaferVo.setContact(user.getContact());
		userForTransaferVo.setEmployeeNo(user.getEmployeeNo());
		userForTransaferVo.setUserId(user.getUserId());
		userForTransaferVo.setName(user.getName());
		
		Organization userOrganization=organizationDao.findById(user.getOrganizationId());
		String orgLevel="";
		if(userOrganization!=null && StringUtils.isNotBlank(userOrganization.getOrgLevel())) {
			//return getBelongOrgazitionByOrgType( userOrganization.getOrgLevel(), OrganizationType.GROUNP);
			Map<String, Object> params = new HashMap<String, Object>();
			String sql="SELECT * from organization where :orgLevel LIKE concat(orgLevel,'%')"+
					"  ORDER BY LENGTH(orgLevel),orgOrder ";
			params.put("orgLevel", userOrganization.getOrgLevel());
			List<Organization> Organizations = organizationDao.findBySql(sql, params);
			if(Organizations!=null&&Organizations.size()>0) {
				for(Organization org :Organizations) {
					if (org.getOrgType() == OrganizationType.CAMPUS) {
						userForTransaferVo.setCampus(org.getName());
						userForTransaferVo.setCampusId(org.getId());
					}else if(org.getOrgType() == OrganizationType.GROUNP) {
						userForTransaferVo.setGroup(org.getName());
						userForTransaferVo.setGroupId(org.getId());
					}else if(org.getOrgType() == OrganizationType.BRENCH) {
						userForTransaferVo.setBrench(org.getName());
						userForTransaferVo.setBrenchId(org.getId());
						orgLevel = org.getOrgLevel();
					}
				}
			}
		}
		if(StringUtils.isNotBlank(orgLevel)) {
			//分公司下所有校区，是咨询师可操作校区
			StringBuffer sql2 = new StringBuffer();
			Map<String, Object> maps = new HashMap<String, Object>();
			sql2.append(" select * from organization where 1=1 and STATUS=0 and boss_use=0 ");
			sql2.append(" and orgType='"+OrganizationType.CAMPUS+"' and orgLevel like :orgLevel ");
			maps.put("orgLevel", orgLevel+"%");
			List<Organization> campusList = organizationDao.findBySql(sql2.toString(), maps);
			List<Map<String, Object>> result = new ArrayList<>();
			for (Organization o : campusList){
				Map<String, Object> item = new LinkedHashMap<>();
				item.put("organizationId", o.getId());
				item.put("organizationName", o.getName());
				result.add(item);
			}
			userForTransaferVo.setCampusAreas(result);
		}
		response.setData(userForTransaferVo);
		return response;
	}

	@Override
	public List<Organization> getDeptExistsDistributableZXS(String userId) {
		// TODO Auto-generated method stub
		Map<String, Object> maps = new HashMap<String, Object>();
		String[] job_signs = new String[] {RoleSign.ZXS.getValue().toLowerCase(),RoleSign.ZXZG.getValue().toLowerCase()};
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT o.* ");
		sql.append(" FROM user_dept_job udj JOIN distributable_user_job duj ON udj.JOB_ID = duj.job_id  ");
		sql.append(" JOIN user_job uj ON uj.ID = duj.relate_job_id  ");
		sql.append(" JOIN organization o ON o.id = udj.DEPT_ID ");
		sql.append(" WHERE udj.USER_ID = :userId  AND uj.JOB_SIGN IN (:job_signs)");
		sql.append(" ORDER BY CONVERT(o.orgLevel,SIGNED) ");
		maps.put("userId", userId);
		maps.put("job_signs", job_signs);
		List<Organization> organizations = organizationDao.findBySql(sql.toString(), maps);
		return organizations;
	}

	@Override
	public List<Map<Object, Object>> getUserOrganizationByJobs(String orgLevel, String[] jobs, String userName) {
		// TODO Auto-generated method stub
		Map<String, Object> maps = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT u.USER_ID userId,u.NAME name,u.ACCOUNT account,o.id organizationId,o.name organizationName,o.orgType orgType");
		sql.append(" FROM user_dept_job udj JOIN user u ON udj.USER_ID = u.USER_ID  ");
		sql.append(" JOIN user_job uj ON uj.ID = udj.JOB_ID  ");
		sql.append(" JOIN organization o ON o.id = u.organizationID ");
		sql.append(" WHERE o.orgLevel like :orgLevel AND u.ENABLE_FLAG='0' AND uj.JOB_SIGN IN (:jobs) ");
		if(StringUtils.isNotBlank(userName)) {
			maps.put("userName", "%"+userName+"%");
			sql.append(" AND u.NAME like :userName");
		}
		sql.append(" ORDER BY CONVERT(o.orgLevel,SIGNED) ");
		maps.put("orgLevel", orgLevel+"%");
		maps.put("jobs", jobs);
		List<Map<Object, Object>> users = userDao.findMapBySql(sql.toString(), maps);
		
		
		return users;
	}

	@Override
	public List<Map<Object, Object>> getUserByJobsOrgLevel(String orgLevel, String[] jobs) {
		// TODO Auto-generated method stub
		Map<String, Object> maps = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT u.USER_ID userId,u.NAME name,u.ACCOUNT account,o.id organizationId,o.name organizationName,o.orgType orgType");
		sql.append(" FROM user_dept_job udj JOIN user u ON udj.USER_ID = u.USER_ID  ");
		sql.append(" JOIN user_job uj ON uj.ID = udj.JOB_ID  ");
		sql.append(" JOIN organization o ON o.id = u.organizationID ");
		sql.append(" WHERE o.orgLevel = :orgLevel AND u.ENABLE_FLAG='0' AND uj.JOB_SIGN IN (:jobs) ");
		sql.append(" ORDER BY CONVERT(o.orgLevel,SIGNED) ");
		maps.put("orgLevel", orgLevel);
		maps.put("jobs", jobs);
		List<Map<Object, Object>> users = userDao.findMapBySql(sql.toString(), maps);
		
		
		return users;
	}

	@Override
	public List<Map<Object, Object>> getUserOrganizationByJobsAndName(String orgLevel, String[] jobs,
			String userName, String brenchName) {
		// TODO Auto-generated method stub
		Map<String, Object> maps = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT u.USER_ID userId,u.NAME name,:brenchName brenchName");
		sql.append(" FROM user_dept_job udj JOIN user u ON udj.USER_ID = u.USER_ID  ");
		sql.append(" JOIN user_job uj ON uj.ID = udj.JOB_ID  ");
		sql.append(" JOIN organization o ON o.id = u.organizationID ");
		sql.append(" WHERE o.orgLevel like :orgLevel AND u.ENABLE_FLAG='0' AND uj.JOB_SIGN IN (:jobs) ");
		if(StringUtils.isNotBlank(userName)) {
			maps.put("userName", "%"+userName+"%");
			sql.append(" AND u.NAME like :userName");
		}
		sql.append(" ORDER BY CONVERT(o.orgLevel,SIGNED) ");
		maps.put("orgLevel", orgLevel+"%");
		maps.put("jobs", jobs);
		maps.put("brenchName", brenchName);
		List<Map<Object, Object>> users = userDao.findMapBySql(sql.toString(), maps);
		
		
		return users;
	}

	@Override
	public Response modifyUserInfo(UserEditDto dto) {
    	Response res = new Response();
		if(!ParamCheckUtil.checkParamIsNull(dto.getUserId(),res)){
			return res;
		}

		User user=userDao.findById(dto.getUserId());
		user.setModifyTime(DateTools.getCurrentDateTime());
		user.setModifyUserId(getCurrentLoginUserId());

		List<UserOrganizationRole> orgList = userOrganizationRoleDao.findAllOrgRoleByUserId(dto.getUserId());

		Map<String , UserOrganizationRole> checkMap = new HashMap<>();

		for(UserOrganizationRole uor : dto.getUserOrgRoles()){//新组织架构
				if(uor.getIsMain()==0){//设置主组织架构到用户表
					user.setOrganizationId(uor.getOrganization().getId());
					user.setRoleId(uor.getRole().getRoleId());
				}
				checkMap.put(uor.getRole().getRoleId()+uor.getOrganization().getId(),uor);
		}

		StringBuilder modifyMsg= new StringBuilder();
		for(UserOrganizationRole uor : orgList){//旧组织架构
			if(checkMap.get(uor.getRole().getRoleId()+uor.getOrganization().getId())!=null){
				UserOrganizationRole newUor=checkMap.get(uor.getRole().getRoleId()+uor.getOrganization().getId());
				if(!uor.getIsMain().equals(newUor.getIsMain())){//修改了主次部门
					uor.setIsMain(newUor.getIsMain());
				}

				checkMap.remove(uor.getRole().getRoleId()+uor.getOrganization().getId());//清理Key
			}else{
				userOrganizationRoleDao.delete(uor);
				modifyMsg.append("删除"+uor.getOrganization().getName()+"-"+uor.getRole().getName()+";");
			}
		}


		for(String key :checkMap.keySet()){
			UserOrganizationRole addUor = checkMap.get(key);
			userOrganizationRoleDao.save(addUor);
			Organization org = organizationDao.findById(addUor.getOrganization().getId());
			Role role = roleDao.findById(addUor.getRole().getRoleId());
			modifyMsg.append("新增"+org.getName()+"-"+role.getName()+";");
		}

		if(modifyMsg.length()>0) {  //保存日志
			userModifyLogDao.save(new UserModifyLog(user.getUserId(), modifyMsg.toString().substring(0,modifyMsg.length()-1), getCurrentLoginUserId()));
		}

		if(dto.getType()!=null) {
			user.setType(dto.getType());
		}else{
			user.setType(UserType.EMPLOYEE_USER);
		}
		return res;
	}

	@Override
	public Response getUserInfoByUserId(String userId) {
    	Response res = new Response();
		UserDetailDto dto = new UserDetailDto();
    	List<UserModifyLog> logList =userModifyLogDao.findAllByUserId(userId);
    	for(UserModifyLog log : logList){
    		User user = userDao.findById(log.getCreateUser());
    		log.setCreateUserName(user.getName());
		}

		dto.setLogs(logList);
		List<UserOrganizationRole> organizationRoles = userOrganizationRoleDao.findAllOrgRoleByUserId(userId);
		dto.setUserOrgRoles(HibernateUtils.voListMapping(organizationRoles,UserOrganizationRoleVo.class));
		User user = userDao.findById(userId);
		if(user.getType()!=null) {
			dto.setType(user.getType().getValue());
		}else{
			dto.setType(UserType.EMPLOYEE_USER.getValue());
		}
		HttpClient client = HttpHeadersUtils.wrapHttpClient();
		String u = PropertiesUtils.getStringValue("HRMS_SECRET_ACCOUNT");
		String key = PropertiesUtils.getStringValue("HRMS_SECRET_KEY");
		String url =PropertiesUtils.getStringValue("HRMS_HOST")+"/hrms/syncOuter/getUserInfoByEmployeeNo?employeeNo="+user.getEmployeeNo();
		HttpGet getrequest= (HttpGet) HttpHeadersUtils.setLoginHeader("application/json", RequestMethod.GET,url,u,key);

		HttpResponse getResponse = null;
		try {
			getResponse = client.execute(getrequest);
		} catch (IOException e) {
			logger.error("访问人事服务器有问题"+url);
			e.printStackTrace();
		}
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = "";
			try {
				str = EntityUtils.toString(getResponse.getEntity());
				Gson g = new Gson();
				Map userInfo = g.fromJson(str,Map.class);
				if(userInfo !=null && userInfo.get("msg")!=null && "success".equals(userInfo.get("msg")) && userInfo.get("result")!=null) {
					dto.setUserInfo(userInfo.get("result"));
				}
			} catch (IOException e) {
				logger.error("获取用户信息有问题："+getResponse.getEntity());
				e.printStackTrace();
			}
			logger.info("获取人事用户信息："+str);

		}else{
			throw new ApplicationException("获取用户信息有问题！"+url);
		}


		res.setData(dto);
		return res;
	}

	public HrmsUserInfoDto getHrmsUserInfo(String employeeNo){
		return RequestOAUtils.getOaUserInfoToObject(employeeNo);
	}

    @Override
    public List<Organization> getUserOrganizationList(String userId) {
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			List<Organization> returnList = new ArrayList<>();
			List<Organization> orgRoleList = organizationDao.getUserOrganizationList(userId);
			Map<String, Organization> hasMap = new HashMap<>();
			for (Organization orgRole : orgRoleList) {
				if (hasMap.get(orgRole.getId()) == null) {
					hasMap.put(orgRole.getId(), orgRole);
					returnList.add(orgRole);
				}
			}
			return returnList;
		}else{
			return findOrganizationByUserId(userId);
		}
    }

	@Override
	public DataPackage getUserInfoList(UserSearchDto dto,DataPackage dp) {

		dp = userDao.getUserInfoList(dto,dp);


		List<Map<String,Object>> returnList = new ArrayList<>();
		for(Object o : dp.getDatas()){
			Map map = (Map)o;

			List<UserOrganizationRole> organizationRoles = userOrganizationRoleDao.findAllOrgRoleByUserId(map.get("userId").toString());
			List<UserOrganizationRoleVo> orgRolelist = new ArrayList<>();
			for(UserOrganizationRole orgRole :organizationRoles){//只显示主部门。
				if(orgRole.getIsMain()!=null && orgRole.getIsMain()==0) {
					UserOrganizationRoleVo rvo = HibernateUtils.voObjectMapping(orgRole, UserOrganizationRoleVo.class);
					orgRolelist.add(rvo);
				}
			}
			map.put("orgRoles",orgRolelist);

			if(map.get("employeeNo")!=null) {
				HrmsUserInfoDto userInfo = getHrmsUserInfo(map.get("employeeNo").toString());
				if(userInfo !=null) {
					map.put("userDeptJob", userInfo.getStationInfo());
				}
			}
			returnList.add(map);
		}

		dp.setDatas(returnList);
		return dp;
	}

	@Override
	public Response checkUserOrgCanModify(UserOrganizationRoleVo vo) {
		Response res = new Response();
		if(StringUtil.isEmpty(vo.getOrganizationId())){
			res.setResultCode(-1);
			res.setResultMessage("参数有误，组织架构ID未传！");
			return res;
		}

		Role role = roleDao.findById(vo.getRoleId());

		if(role!=null && role.getRoleCode()!=null  && !role.getRoleCode().equals(RoleCode.STUDY_MANAGER) && !role.getRoleCode().equals(RoleCode.STUDY_MANAGER_HEAD)){
			return res;
		}


		Organization org = organizationDao.findById(vo.getOrganizationId());

		String changeCampusId = vo.getOrganizationId();

		if(org.getOrgType()!=null && org.getOrgType().equals(OrganizationType.DEPARTMENT)){
			changeCampusId= org.getParentId();
		}

		List<Student> stuList=studentService.findStuByManegerIdAndCampusId(vo.getUserId(), changeCampusId);
		if(stuList!=null && stuList.size()>0){
			Organization blCampus=organizationDao.findById(org.getId());
			res.setResultCode(-1);
			res.setResultMessage("该用户在'"+blCampus.getName()+"'有带学生，需要批量更换学管师清除所带学生才可以修改相关组织架构");
		}
		return res;
	}

	@Override
	public List<UserOrganizationRole> getUserOrganizationRoleByUserId(String userId) {
		return  userOrganizationRoleDao.findAllOrgRoleByUserId(userId);
	}

    @Override
    public List getResourcesByEmployeeNo(String employeeNo, String type) {
		List<Resource> list =  resourceDao.getResourcesByEmployeeNo(employeeNo,type);
        return HibernateUtils.voListMapping(list,ResourceVo.class);
    }

    @Override
    public Response pushUserInfo(MessagePushVo vo) {
		Response res = new Response();
		if(StringUtils.isBlank(vo.getData())){
			logger.error("同步人事用户参数为空！");
			return res;
		}

		HrmsUserInfoDto info = RequestOAUtils.getOaUserInfoToObject(vo.getData());

		if(info==null){
			logger.error("查询人事用户信息为空！"+vo.getData());
			return res;
		}

		User user = userDao.findUserByEmployeeNo(info.getEmployeeNo(),null);
		if(user==null){
			user = new User();
			List<UserOrganizationRole> organizationRoles = initUserOrgRole(info);
			if(organizationRoles==null && organizationRoles.size()==0){
				logger.info("查询人事组织架构在系统上无对应！"+vo.getData());
				return res;
			}
			info.pushInfoToUser(user);
			user.setType(UserType.EMPLOYEE_USER);
			userDao.save(user);
			for(UserOrganizationRole organizationRole:organizationRoles){
				if(organizationRole.getIsMain()==0){//主组织架构
					user.setOrganizationId(organizationRole.getOrganization().getId());
					user.setRoleId(organizationRole.getRole().getRoleId());
				}
				organizationRole.setUser(user);
				userOrganizationRoleDao.save(organizationRole);
			}
		}else{
			if(!info.getEnableFlag().equals(user.getEnableFlg()) && info.getEnableFlag()==1){
				//客户资源回流。
				JedisUtil.lpush("invalidUserInfo".getBytes(), JedisUtil.ObjectToByte(user.getUserId()));
				updateTeacherStatus(user.getUserId());
			}
			info.pushInfoToUser(user);
			userDao.save(user);
		}

        return res;
    }

	/**
	 * 在线报读的老师状态设为无效
	 * @param userId
	 */
	private void updateTeacherStatus(String userId){
		//设置用户老师属性为无效 rdc705970
		UserTeacherAttributeVo userTeacherAttribute = findUserTeacherAttribute(userId);
		if (userTeacherAttribute!=null) {
			userTeacherAttribute.setTeacherSwitch(false);
			editUserTeacherAttribute(userTeacherAttribute);
		}
	}

	private List<UserOrganizationRole> initUserOrgRole(HrmsUserInfoDto info) {
		List<UserOrganizationRole> orgRole = new ArrayList<>();
		if(info.getStationInfo()==null){
			return orgRole;
		}
		for(HrmsUserInfoDto.StationInfo stationInfo:  info.getStationInfo()){
			if(!stationInfo.getOrganizationSign().equals(OrganizationSign.valueOf(PropertiesUtils.getStringValue("PROJECT")))){//不是该系统的不需要处理
				continue;
			}
			UserOrganizationRole role = new UserOrganizationRole();
			Organization org = findBossOrganizationUntilNull(stationInfo.getOrganizationId());
			if(org!=null){
				String roleId = getBossRoleByHrmsRoleId(stationInfo.getStationId());
				role.setOrganization(org);
				role.setRole(new Role(roleId));
				if(orgRole.size()==0){
					role.setIsMain(0);
				}else{
					role.setIsMain(1);
				}
				orgRole.add(role);
			}
		}
//		orgRole.setOrganization(new Organization("000001"));
//		orgRole.setRole(new Role(PropertiesUtils.getStringValue(Constants.DEFAULT_ROLE_ID)));
//		orgRole.setIsMain(0);
		return orgRole;
	}

	/**
	 * 获取BOSS对应的角色
	 * @param hrmsRoleId
	 * @return
	 */
	public String getBossRoleByHrmsRoleId(String hrmsRoleId){
		String teacherIds = PropertiesUtils.getStringValue(Constants.HRMS_TEACHER_IDS);
		String consultorIds = PropertiesUtils.getStringValue(Constants.HRMS_CONSULTOR_IDS);
		String manergerIds = PropertiesUtils.getStringValue(Constants.HRMS_MANERGER_IDS);
		List<Role> roles = new ArrayList<>();
		if(teacherIds.contains(hrmsRoleId)) {
			roles = roleDao.findRoleByName(PropertiesUtils.getStringValue(Constants.BOSS_TEACHER_NAME));

		}else if (consultorIds.contains(hrmsRoleId)){
			roles = roleDao.findRoleByName(PropertiesUtils.getStringValue(Constants.BOSS_CONSULTOR_NAME));

		}else if (manergerIds.contains(hrmsRoleId)){
			roles = roleDao.findRoleByName(PropertiesUtils.getStringValue(Constants.BOSS_MANERGER_NAME));
		}
		if(roles.size()>0){
			Role role = roles.get(0);
			return role.getRoleId();
		}else{
			return PropertiesUtils.getStringValue(Constants.DEFAULT_ROLE_ID);
		}
	}

	/**
	 * 根据人事的组织架构ID获取BOSS的组织架构ID，往上找，直到空
	 * @param hrmsId
	 * @return
	 */
	public Organization findBossOrganizationUntilNull(String hrmsId){
		Organization org =  organizationDao.findOrganizationByHrmsId(hrmsId);
		if(org!=null){
			return org;
		}

		OrganizationHrms hrms = organizationHrmsDao.findById(hrmsId);
		if(hrms==null || StringUtils.isBlank(hrms.getParentId())){
			return null;
		}
		return findBossOrganizationUntilNull(hrms.getParentId());

	}

	@Override
	public Response updateRoleStatusById(String roleId, ValidStatus roleStatus) {
		// TODO Auto-generated method stub
		Response res = new Response();
		if(StringUtils.isBlank(roleId)||roleStatus ==null) {
			res.setResultCode(-1);
			res.setResultMessage("角色id为空或角色状态为空！");
			return res;
		}
		Role roleDb = roleDao.findById(roleId);
		if(roleDb == null) {
			res.setResultCode(-1);
			res.setResultMessage("角色不存在！");
			return res;
		}
		
		if(roleDb.getRoleStatus() != roleStatus) {
			roleDb.setRoleStatus(roleStatus);
			roleDao.save(roleDb);
		}
		return res;
	}


}
