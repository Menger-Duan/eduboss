package com.eduboss.dao.impl;

import com.eduboss.common.Constants;
import com.eduboss.common.RoleCode;
import com.eduboss.common.UserWorkType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.UserSearchDto;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("UserDao")
public class UserDaoImpl extends GenericDaoImpl<User, String> implements UserDao {
	
	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Override
	public List<User> getStaffByRoleIdAndOrgId(String roleId, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from User u  where ";
		if(isIncludeChildLevel && StringUtils.isNotBlank(orgLevel)) {
			hql+=" u.organizationId in (select id from Organization where orgLevel like :orgLevel) ";
			params.put("orgLevel", orgLevel + "%");
		} else {
			hql+=" u.organizationId = :organizationId ";
			params.put("organizationId", orgId);
		}
		
		if (StringUtils.isNotBlank(roleId)) {
			hql +=" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleId = :roleId ) ) ";
			params.put("roleId", roleId);
		}
		return super.findLimitHql(hql, limit, params);
	}

	@Override
	public List<User> getStaffByRoleIdAndOrgIdNew(String roleId, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT u.* from user  u ");
		sql.append(" left join user_organization_role uor on u.user_id = uor.user_id ");
		sql.append(" left join organization o on o.id = uor.organization_id");
		sql.append(" left join role r on r.id = uor.role_id ");
		sql.append(" where and u.enable_flag = 0 ");
		if(isIncludeChildLevel) {
			sql.append(" and o.orgLevel like :orgLevel) ");
			params.put("orgLevel", orgLevel + "%");
		} else {
			sql.append(" and u.organizationId = :organizationId ");
			params.put("organizationId", orgId);
		}
		if (StringUtils.isNotBlank(roleId)) {
			sql.append(" and r.id = :roleId ");
			params.put("roleId", roleId);
		}
		return super.findLimitSql(sql.toString(), limit, params);
	}
	
	@Override
	public List<User> getStaffByRoldCodeAndOrgId(String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from User u  where ";
		if(isIncludeChildLevel) {
			hql+=" u.organizationId in (select id from Organization  where orgLevel like :orgLevel) ";
			params.put("orgLevel", orgLevel + "%");
		} else {
			hql+=" u.organizationId = :organizationId ";
			params.put("organizationId", orgId);
		}
		
		if (StringUtils.isNotBlank(roleCode)) {
			hql +=" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode ) ) ";
			params.put("roleCode", RoleCode.valueOf(roleCode));
		}
		hql += " and u.enableFlg = 0 ";
//		return super.findLimitHql(hql, limit, params);
		return super.findAllByHQL(hql, params);
	}

	@Override
	public List<User> getStaffByRoldCodeAndOrgIdNew(String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select DISTINCT u.* from user  u ");
		sql.append(" left join user_organization_role uor on u.user_id = uor.user_id ");
		sql.append(" left join organization o on o.id = uor.organization_id");
		sql.append(" left join role r on r.id = uor.role_id ");
		sql.append(" where and u.enable_flag = 0 ");
		if(isIncludeChildLevel) {
			sql.append(" and o.orgLevel like :orgLevel) ");
			params.put("orgLevel", orgLevel + "%");
		} else {
			sql.append(" and u.organizationId = :organizationId ");
			params.put("organizationId", orgId);
		}
		if (StringUtils.isNotBlank(roleCode)) {
			sql.append(" and r.roleCode = :roleCode ");
			params.put("roleCode", roleCode);
		}
		return super.findLimitSql(sql.toString(), limit, params);
	}
	
	@Override
	public List<User> getStaffByNameAndRoleCodeAndOrgId(String name, String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from User u  where u.name like :name ";
		params.put("name", "%" + name + "%");
		if(isIncludeChildLevel) {
			hql+=" and u.organizationId in (select id from Organization  where orgLevel like :orgLevel) ";
			params.put("orgLevel", orgLevel + "%");
		} else {
			hql+=" and u.organizationId = :organizationId ";
			params.put("organizationId", orgId);
		}
		if (StringUtils.isNotBlank(roleCode)) {
			hql +=" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode )) ";
			params.put("roleCode", RoleCode.valueOf(roleCode));
		}
		return super.findLimitHql(hql, limit, params);
	}

	@Override
	public List<User> getStaffByNameAndRoleCodeAndOrgIdNew(String name, String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(" select DISTINCT u.* from user  u ");
		sql.append(" left join user_organization_role uor on u.user_id = uor.user_id ");
		sql.append(" left join organization o on o.id = uor.organization_id");
		sql.append(" left join role r on r.id = uor.role_id ");
		sql.append(" where u.name like :name");

		params.put("name", "%" + name + "%");
		if(isIncludeChildLevel) {
			sql.append(" and o.orgLevel like :orgLevel) ");
			params.put("orgLevel", orgLevel + "%");
		} else {
			sql.append(" and u.organizationId = :organizationId ");
			params.put("organizationId", orgId);
		}
		if (StringUtils.isNotBlank(roleCode)) {
			sql.append(" and r.roleCode = :roleCode ");
			params.put("roleCode", roleCode);
		}
		return super.findLimitSql(sql.toString(), limit, params);
	}
	
	@Override
	public List<User> findByNameOrIdAndRodeCode(String term, RoleCode roleCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from User u  where (u.name like :name or u.userId like :userId) ";
		params.put("name", "%" + term + "%");
		params.put("userId", "%" + term + "%");
		hql	+= " and u.userId in (select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode )) ";
		params.put("roleCode", roleCode);
		return super.findLimitHql(hql, 30, params);
	}

	@Override
	public List<User> findByNameOrIdAndRodeCodeNew(String term, RoleCode roleCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct u.* from user  u ");
		sql.append(" left join user_organization_role uor on u.user_id = uor.user_id ");
		sql.append(" left join role r on r.id = uor.role_id ");
		sql.append(" where (u.name like :name or u.user_id like :userId) ");

		params.put("name", "%" + term + "%");
		params.put("userId", "%" + term + "%");
		sql.append(" and r.roleCode = :roleCode ");
		params.put("roleCode", roleCode);
		sql.append(" limit 30");

		return super.findBySql(sql.toString(), params);
	}
	
	/**
	 * 根据类型和组织架构查询用户
	 *@param workType 用户类型
	 *@param orgId 组织架构id
	 *@param orgLevel 组织架构code
	 *@param isIncludeChildLevel 是否查询 组织架构下的用户
	 *@return 用户列表
	 */
	@Override
	public List<User> getUserByWorkTypeAndOrgId(UserWorkType workType, String orgId, String orgLevel, boolean isIncludeChildLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from User u  where enableFlg <> 1 ";
		if(StringUtils.isNotBlank(orgId)){
			if(isIncludeChildLevel) {
				hql+="and u.organizationId in (select id from Organization  where orgLevel like :orgLevel) ";
				params.put("orgLevel", orgLevel + "%");
			} else {
				hql+="and u.organizationId = :organizationId ";
				params.put("organizationId", orgId);
			}
		}
		if (workType!=null) {
			hql +=" and u.workType = :workType  ";
			params.put("workType", workType);
		}
		return super.findAllByHQL(hql, params);
	}
	
	/**
	 * 查找用户列表（自动搜索）
	 */
	@Override
	public List<User> getUserAutoComplate(String input, String roleCode, String parentOrgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from User u where 1=1 ");
        hql.append(" and enableFlg <> 1");
        if (StringUtil.isNotBlank(input)) {
        	hql.append(" and  ( u.userId like :userId ");
        	hql.append(" or   name like :name) ");
        	params.put("userId", "%" + input + "%");
        	params.put("name", "%" + input + "%");
        }

		if(StringUtils.isNotBlank(roleCode)) {
			hql.append(" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode )) ");
			params.put("roleCode", RoleCode.valueOf(roleCode));
		}
		if(StringUtils.isNotBlank(parentOrgId)) {
			Organization org = organizationDao.findById(parentOrgId);
            hql.append(" and (");
			hql.append(" u.userId in (select user.userId  from UserOrganization where organizationId in ( select id from Organization  where orgLevel like :orgLevel)) ");
			params.put("orgLevel", "" + org.getOrgLevel() + "%");
            hql.append(" or u.workType = 'DUMMY' ");
            hql.append(" )");
		}
		return this.findAllByHQL(hql.toString(), params);
	}

	/**
	 * 查找用户列表（自动搜索）
	 */
	@Override
	public List<User> getLimitUserAutoComplate(String input, String roleCode, String parentOrgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		input=input.replaceAll("'", "");
		StringBuffer hql = new StringBuffer("from User u where 1=1 ");
        hql.append(" and enableFlg <> 1");
        hql.append(" and  ( u.userId like :userId ");
		hql.append(" or   name like :name) ");
		params.put("userId", "%" + input + "%");
		params.put("name", "%" + input + "%");
		if(StringUtils.isNotBlank(roleCode)) {
			hql.append(" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode )) ");
			params.put("roleCode", RoleCode.valueOf(roleCode));
		}
		if(StringUtils.isNotBlank(parentOrgId)) {
			Organization org = organizationDao.findById(parentOrgId);
            hql.append(" and (");
            hql.append(" u.userId in (select user.userId  from UserOrganization where organizationId in ( select id from Organization  where orgLevel like :orgLevel)) ");
            params.put("orgLevel", org.getOrgLevel() + "%");
            hql.append(" or u.workType = 'DUMMY' ");
            hql.append(" )");
		}
		return this.findLimitHql(hql.toString(),CommonUtil.autoComplateLimit, params);
	}

	/**
	 * 查找用户列表（自动搜索）新
	 */
	@Override
	public List<User> getLimitUserAutoComplateNew(String input, String roleCode, String parentOrgId,String userId,int limit) {
		Map<String, Object> params = new HashMap();
		input=input.replaceAll("'", "");
		StringBuffer sql = new StringBuffer("select distinct u.* from user u ");
		sql.append(" left join user_organization_role uor on uor.user_id=u.USER_ID");
		sql.append(" left join organization o on uor.organization_id=o.id");
		sql.append(" left join role r on r.id = uor.role_id where 1=1 ");

		sql.append(" and u.enable_Flag <> 1");
		if(StringUtils.isNotBlank(input)) {
			sql.append(" and  ( u.user_Id like :userId ");
			sql.append(" or   u.name like :name) ");
			params.put("userId", "%" + input + "%");
			params.put("name", "%" + input + "%");
		}
		if(StringUtils.isNotBlank(roleCode)) {
			sql.append(" and  r.roleCode = :roleCode  ");
			params.put("roleCode", roleCode);
		}
		if(StringUtils.isNotBlank(parentOrgId)) {
			Organization org = organizationDao.findById(parentOrgId);
			sql.append(" and (");
			sql.append(" o.orgLevel like :orgLevel ");
			params.put("orgLevel", ""+org.getOrgLevel() + "%");
			sql.append(" or u.work_Type = 'DUMMY' ");
			sql.append(" )");
		}

		if(StringUtils.isNotBlank(userId)){
			// 用户权限限制
			List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userId);
			if(userOrganizations != null && userOrganizations.size() > 0){
				Organization org = userOrganizations.get(0);
				sql.append(" and  (");
				sql.append(" u.organizationId in (select id from Organization where orgLevel like :orgLevel0 )");
				params.put("orgLevel0", "" + org.getOrgLevel() + "%");
				for(int i = 1; i < userOrganizations.size(); i++){
					sql.append(" or organizationId in (select id from Organization where orgLevel like  :orgLevel" + i + " )");
					params.put("orgLevel" + i, "" + userOrganizations.get(i).getOrgLevel() + "%");
				}
				sql.append(" )");
			}
		}
		if(limit>0) {
			sql.append(" limit " +limit);
		}
		return this.findBySql(sql.toString(), params);
	}
	
	/**
	 * 查找用户列表（自动搜索）按组织架构走
	 */
	@Override
	public List<User> getLimitUserByOrgAutoComplate(String input, String roleCode, String parentOrgId, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from User u where 1=1 ");
        hql.append(" and enableFlg <> 1");
        hql.append(" and  ( u.userId like :userId ");
  		hql.append(" or   name like :name) ");
  		params.put("userId", "%" + input + "%");
		params.put("name", "%" + input + "%");
		// 用户权限限制
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userId);

        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            hql.append(" and  (");
            hql.append(" organizationId in (select id from Organization where orgLevel like :orgLevel )");
            params.put("orgLevel", "" + org.getOrgLevel() + "%");
            for(int i = 1; i < userOrganizations.size(); i++){
            	hql.append(" or organizationId in (select id from Organization where orgLevel like  :orgLevel" + i + " )");
            	params.put("orgLevel" + i, "" + userOrganizations.get(i).getOrgLevel() + "%");
            }
            hql.append(" )");
        }

		if(StringUtils.isNotBlank(roleCode)) {
			hql.append(" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode ) ) ");
			params.put("roleCode", RoleCode.valueOf(roleCode));
		}
		if(StringUtils.isNotBlank(parentOrgId)) {
			Organization org = organizationDao.findById(parentOrgId);
            hql.append(" and (");
            hql.append(" u.userId in (select user.userId  from UserOrganization where organizationId in ( select id from Organization  where orgLevel like :parentOrgId )) ");
            params.put("parentOrgId", "" + org.getOrgLevel() + "%");
            hql.append(" or u.workType = 'DUMMY' ");
            hql.append(" )");
		}
		return this.findLimitHql(hql.toString(),CommonUtil.autoComplateLimit, params);
	}

	@Override
	public int updateUserPassword(String account, String oldPassWord,
			String newPassWord) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="update User set password = :newPassWord where account= :account " +
				"and password= :oldPassWord ";
		params.put("newPassWord", newPassWord);
		params.put("account", account);
		params.put("oldPassWord", oldPassWord);
		return this.excuteHql(hql, params);
	}
	
	/**
	 * 查询拥有角色的用户
	 * @param roleCodes 多个用，号隔开
	 * @param orgLevel 组织架构层级
	 * @return
	 */
	@Override
	public List<User> getUserByRoldCodes(String roleCodes,String orgLevel) {
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return this.getUserByRoldCodesNew(roleCodes, orgLevel, null);
		}else {
			return this.getUserByRoldCodes(roleCodes, orgLevel, null);
		}
	}
	
	@Override
	public List<User> getUserByRoldCodes2(String orgLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from User u  where 1=1 ";
        hql+=" and u.enableFlg = '0'";

		if(StringUtils.isNotEmpty(orgLevel)){
			if(orgLevel.length()>12)
				orgLevel = orgLevel.substring(0, 12);
			hql+= " and userId in(select user.userId from UserOrganizationRole where organizationId in "+
				  "(select id from Organization where orgLevel like :orgLevel ) )";
			params.put("orgLevel", orgLevel + "%");

		}
		//设置职位是校区主任的用户
		hql +=" and u.userId in(select ur.userId from UserDeptJob ur where ur.jobId in(select r.id from UserJob r where r.jobName ='校区主任' )) ";
		return super.findAllByHQL(hql, params);
	}
	
	/**
	 * 查询拥有角色的用户 -加name模糊查询
	 * @param roleCodes 多个用，号隔开
	 * @param orgLevel 组织架构层级
	 * @return
	 */
	@Override
	public List<User> getUserByRoldCodes(String roleCodes,String orgLevel,String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from User u  where 1=1 ";
		hql+=" and u.enableFlg = '0'";
		if(StringUtils.isNotEmpty(name)){
			hql+=" and u.name like :name ";
			params.put("name", name + "%");
		}
		if(StringUtils.isNotEmpty(orgLevel)){
			hql+= " and userId in(select user.userId from UserOrganization where organizationId in (select id from Organization where orgLevel like :orgLevel) )";
			params.put("orgLevel", orgLevel + "%");

		}
		if (StringUtils.isNotBlank(roleCodes)) {
			String roleCodeArray[] = roleCodes.split(",");
			if(roleCodeArray.length>1){
				RoleCode codes[] = new RoleCode[roleCodeArray.length];
				for(int i = 0; i< roleCodeArray.length; i++){
					codes[i] = RoleCode.valueOf(roleCodeArray[i]);
				}
				hql +=" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode in (:codes) ) ) ";
				params.put("codes", codes);
			}else{
				hql +=" and u.userId in(select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleCode = :roleCode ) ) ";
				params.put("roleCode", RoleCode.valueOf(roleCodes));
			}
		}
		return super.findAllByHQL(hql, params);
	}


	@Override
	public List<User> getUserByRoldCodesNew(String roleCodes,String orgLevel,String name) {
		Map<String, Object> params = new HashMap();
		StringBuffer sql = new StringBuffer("select distinct u.* from user u ");
		sql.append(" left join user_organization_role uor on uor.user_id=u.USER_ID");
		sql.append(" left join organization o on uor.organization_id=o.id");
		sql.append(" left join role r on r.id = uor.role_id where 1=1 ");
		sql.append(" and (u.enable_Flag =0 or u.enable_Flag is null)");

		if(StringUtils.isNotEmpty(name)){
			sql.append(" and u.name like :name ");
			params.put("name", name + "%");
		}
		if(StringUtils.isNotEmpty(orgLevel)){
			sql.append(" and o.orgLevel like :orgLevel ");
			params.put("orgLevel", orgLevel + "%");

		}
		if (StringUtils.isNotBlank(roleCodes)) {
			String roleCodeArray[] = roleCodes.split(",");
			if(roleCodeArray.length>1){
				sql.append(" and  r.roleCode in (:codes) ");
				params.put("codes", roleCodeArray);
			}else{
				sql.append(" and  r.roleCode = :roleCode ");
				params.put("roleCode", roleCodes);
			}
		}
		return super.findBySql(sql.toString(), params);
	}

	@Override
	public String getUserIdByName(String studyManegerName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User where name = :name ";
		params.put("name", studyManegerName);
		List<User> list= super.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0).getUserId();
		}
		return null;
	}

	@Override
	public void deleteById(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " delete from User where userId = :userId ";
		params.put("userId", userId);
		super.excuteHql(hql, params);
	}

	
	@Override
	public List<User> getStudentReferenceUser(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from User where userId in (select studyManegerId from Student stu where stu.id = :studentId) "
				 + " or userId in (select distinct teacher.userId from Course cou where cou.student.id = :courseStuId) ";
		params.put("studentId", studentId);
		params.put("courseStuId", studentId);
		return this.findAllByHQL(hql, params);
	}
	
	@Override
	public List<User> getUserByBlcampusAndRole(String blcampus,String roleIds){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" SELECT * from `user` where organizationID = :organizationID ";
		params.put("organizationID", blcampus);
		if (StringUtils.isNotBlank(roleIds)) {
			String roleIdArray[] = roleIds.split(",");
			if(roleIdArray.length>1){
				sql += " and USER_ID in(SELECT userID from user_role WHERE roleID in(:roleIds) )";
				params.put("roleIds", roleIdArray);
			}else{
				sql += " and USER_ID in(SELECT userID from user_role WHERE roleID = :roleId  )";
				params.put("roleId", roleIds);
			}
		}
		return findBySql(sql, params);
	}

	@Override
	public List<User> getUserByBlcampusAndRoleNew(String blcampus,String roleIds){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" SELECT * from `user` where organizationID = :organizationID ";
		params.put("organizationID", blcampus);
		if (StringUtils.isNotBlank(roleIds)) {
			String roleIdArray[] = roleIds.split(",");
			if(roleIdArray.length>1){
				sql += " and USER_ID in(SELECT user_ID from user_organization_role WHERE role_ID in(:roleIds) )";
				params.put("roleIds", roleIdArray);
			}else{
				sql += " and USER_ID in(SELECT user_ID from user_organization_role WHERE role_ID = :roleId  )";
				params.put("roleId", roleIds);
			}
		}
		return findBySql(sql, params);
	}
	
	/**
	 * 根据校区和职位来查询用户
	 */
	@Override
	public List<User> getUserByBlcampusAndUserJob(String blcampus, String userJobIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" SELECT * from `user` where organizationID = :organizationID ";
		params.put("organizationID", blcampus);
		if (StringUtils.isNotBlank(userJobIds)) {
			String jobIdArray[] = userJobIds.split(",");
			if(jobIdArray.length>1){
				sql += " and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID in(:jobIds) ";
				params.put("jobIds", jobIdArray);
			}else{
				sql += " and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID = :jobId ";
				params.put("jobId", userJobIds);
			}
			sql += " and DEPT_ID = :deptId ) ";
			params.put("deptId", blcampus);
		}
		return findBySql(sql, params);
	}
	
	@Override
	public List<User> getUserByBlcampusAndUserJobValidate(String blcampus, String userJobIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" SELECT * from `user` where organizationID = :organizationID ";
		params.put("organizationID", blcampus);
		if (StringUtils.isNotBlank(userJobIds)) {
			String jobIdArray[] = userJobIds.split(",");
			if(jobIdArray.length>1){
				sql += " and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID in(:jobIds) and DEPT_ID = :deptId ) and enable_flag=0";
				params.put("jobIds", jobIdArray);
			}else{
				sql += " and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID = :jobId and DEPT_ID = :deptId ) and enable_flag=0";
				params.put("jobId", userJobIds);
			}
			params.put("deptId", blcampus);
		}
		return findBySql(sql, params);
	}
	
	@Override
	public List<User> getContactsForStudent(String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		List<User> users = new ArrayList<User>();
		// 一对一 得到 老师
		StringBuffer hql_1on1_teacher = new StringBuffer();
		hql_1on1_teacher.append("select distinct  course.teacher from Course as course where course.student.id = :studentId");
		params.put("studentId", studentId);
		users.addAll(super.findAllByHQL(hql_1on1_teacher.toString(), params));

		// 一对一 学管
		StringBuffer hql_1on1_studyManager = new StringBuffer();
		hql_1on1_studyManager.append("select distinct  course.studyManager from Course as course where course.student.id = :studentId");
		users.addAll(super.findAllByHQL(hql_1on1_studyManager.toString(), params));

		// 小班 得到 老师
		StringBuffer hql_mini_teacher = new StringBuffer();
		hql_mini_teacher.append("select distinct course.teacher from MiniClassCourse as course ")
			.append("where exists ( from course.miniClass.miniClassStudents as miniStudent where miniStudent.student.id  = :studentId)");
		users.addAll(super.findAllByHQL(hql_mini_teacher.toString(), params));

		// 小班 班主任
		StringBuffer hql_mini_studyHead= new StringBuffer();
		hql_mini_studyHead.append("select distinct course.studyHead from MiniClassCourse as course ")
		.append("where exists ( from course.miniClass.miniClassStudents as miniStudent where miniStudent.student.id  = :studentId)");
		users.addAll(super.findAllByHQL(hql_mini_studyHead.toString(), params));
		return users;
	}

	@Override
	public List<User> getUserByRoleAndOrgFroMail(String roleId, String orgLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql_sb = new StringBuffer();
		hql_sb.append("from User u where u.mailAddr <> '' ");
		if(StringUtil.isNotBlank(orgLevel)) {
		    hql_sb.append(" and u.userId in (select uo.user.userId from UserOrganizationRole uo where uo.organization.id in (select id from Organization where orgLevel like :orgLevel))");
		    params.put("orgLevel", orgLevel + "%");
		}
		if(StringUtil.isNotBlank(roleId)) {
			hql_sb.append(" and u.userId in (select ur.userId from UserRole ur where ur.roleId in(select r.roleId from Role r where r.roleId= :roleId))");
			params.put("roleId", roleId);
		}
		return super.findAllByHQL(hql_sb.toString(), params);
	}

	@Override
	public List<User> getUserListHaveMail() {
		StringBuffer hql_sb = new StringBuffer();
		hql_sb.append("from User u where u.mailAddr <> '' ");
		return super.findAllByHQL(hql_sb.toString(), new HashMap<String, Object>());
	}
	
	/**
	 * 根据传入的职位Id得到用户
	 */
	@Override
	public List<User> getUserAutoComplateByJobId(String jobId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct u.* from User u,user_dept_job udj where u.USER_ID=udj.USER_ID  ");
		String[] idArray = jobId.split(",");
		if(idArray.length>0){
			sql.append(" and (udj.job_Id = :jobId0 ");
			params.put("jobId0", idArray[0]);
			for (int i = 1; i < idArray.length; i++) {
				sql.append(" or udj.job_Id = :jobId" + i + " ");
				params.put("jobId" + i, idArray[i]);
			}
			sql.append(" )");				
		}
		return findBySql(sql.toString(), params);
	}
	
	/**
	 * 获取某校区某职位的用户列表
	 */
	public List<User> getUserBycampusAndjobSign(String deptId, String userjobSign) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct u.* from user u,user_dept_job udj where u.USER_ID=udj.USER_ID  ");
		sql.append(" and udj.DEPT_ID = :deptId ");
		sql.append(" and udj.JOB_ID = (select ID from user_job where JOB_SIGN = :jobSign )");
		params.put("deptId", deptId);
		params.put("jobSign", userjobSign);
		return findBySql(sql.toString(), params);
	}

	/**
	 * 更新老师的编制状态
	 */
	@Override
	public void updateTeacherSubjectStatus(String userId,
			int teacherSubjectStatus) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " update user set TEACHER_SUBJECT_STATUS = :teacherSubjectStatus WHERE USER_ID = :userId ";
		params.put("teacherSubjectStatus", teacherSubjectStatus);
		params.put("userId", userId);
		super.excuteSql(sql, params);
	}

	/**
	 * 根据用户ids查找老师名字
	 */
	@Override
	public String getUserNamesByUserIds(String[] userIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String userNames = "";
		if (userIds != null && userIds.length > 0) {
			String sql = " select concat(group_concat(if(ENABLE_FLAG = 0, NAME, CONCAT(NAME, '(无效)'))), '') userNames from user where 1 = 1 ";
			if (userIds.length > 1) {
				sql += " and USER_ID in (:userIds) ";
				params.put("userIds", userIds);
			} else {
				sql += " and USER_ID = :userId ";
				params.put("userId", userIds[0]);
			}
			List<Map<Object, Object>> list = super.findMapBySql(sql, params);
			if (list != null && list.size() > 0) {
				userNames = (String) list.get(0).get("userNames");
			}
		}
		return userNames;
	}

	/**
	 * 根据账户查找用户
	 */
	@Override
	public User findUserByAccount(String account) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User where account = :account ";
		params.put("account", account);
		List<User> list= super.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * extraHql : role_ql_config表的权限控制
	 */
	@Override
	public List<User> getLimitUserAutoComplate(String term,
			String extraHql) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from User u where 1=1 ");
        hql.append(" and enableFlg <> 1");
		hql.append(" and  ( userId like :userId ");
		params.put("userId", "%" + term + "%");
		hql.append(" or   name like :name ) ");
		params.put("name", "%" + term + "%");
		if(StringUtil.isNotBlank(extraHql)){
			hql.append(extraHql);
		}
		return super.findLimitHql(hql.toString(), CommonUtil.autoComplateLimit, params);
	}
	
	/**
     * 获取所有禁用的邮件用户
     */
    @Override
    public List<User> listDisabledHadMailUser() {
        StringBuffer hql = new StringBuffer("from User u where 1=1 ");
        hql.append(" and (enableFlg = 1 or workType = 'PART_TIME' ) ");
        hql.append(" and  mailAddr is not null and mailAddr != '' ");
        return super.findAllByHQL(hql.toString(), null);
    }

	@Override
	public User findUserByAccountContact(String account, String contact) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User where account = :account and contact = :contact and ENABLE_FLAG='0' ";
		params.put("account", account);
		params.put("contact", contact);
		List<User> list= super.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public User findUserByEmployeeNo(String employeeNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User where employee_No = :employeeNo and ENABLE_FLAG='0' ";
		params.put("employeeNo", employeeNo);
		List<User> list= super.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return findUserByEmployeeNo(employeeNo,"0");
	}

	@Override
	public User findUserByEmployeeNo(String employeeNo, String enableFlag) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User where employee_No = :employeeNo ";
		params.put("employeeNo", employeeNo);
		if(StringUtils.isNotBlank(enableFlag)){
			hql+=" and ENABLE_FLAG=:enableFlag";
			params.put("enableFlag", enableFlag);
		}
		List<User> list= super.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public User findUserByNameContact(String name, String contact) {
		// TODO Auto-generated method stub
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from User where name = :name and contact = :contact and ENABLE_FLAG='0' ";
		params.put("name", name);
		params.put("contact", contact);
		List<User> list= super.findAllByHQL(hql, params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

    @Override
    public DataPackage getUserInfoList(UserSearchDto dto, DataPackage dp) {
    	StringBuilder sql = new StringBuilder();
    	Map param = new HashMap();
    	sql.append(" select user_id userId,name,account,ENABLE_FLAG enableFlag,employee_No employeeNo,sex,WORK_TYPE workType,user_type userType,contact  from user where 1=1");

    	if(StringUtil.isNotBlank(dto.getEmployeeNo())){
			sql.append(" and employee_No= :employeeNo");
			param.put("employeeNo",dto.getEmployeeNo());
		}

		if(StringUtil.isNotBlank(dto.getName())){
			sql.append(" and name like :name");
			param.put("name","%"+dto.getName()+"%");
		}

		if(StringUtil.isNotBlank(dto.getAccount())) {
			sql.append(" and account= :account");
			param.put("account", dto.getAccount());
		}
		if(StringUtil.isNotBlank(dto.getWorkType())) {
			sql.append(" and WORK_TYPE= :workType");
			param.put("workType", dto.getWorkType());
		}

		if(StringUtil.isNotBlank(dto.getUserType())) {
			sql.append(" and user_type= :userType");
			param.put("userType", dto.getUserType());
		}

		if(StringUtil.isNotBlank(dto.getContact())) {
			sql.append(" and CONTACT= :contact");
			param.put("contact", dto.getContact());
		}

		if(StringUtil.isNotBlank(dto.getEnableFlag())) {
			sql.append(" and Enable_flag= :enableFlag");
			param.put("enableFlag", dto.getEnableFlag());
		}

		sql.append(" and EXISTS (select 1 from user_organization_role uor where uor.user_id=`user`.USER_ID ");
		if(StringUtil.isNotBlank(dto.getOrgId()) || StringUtil.isNotBlank(dto.getRoleId()))	{
			if(StringUtil.isNotBlank(dto.getOrgId())){
				sql.append(" and uor.organization_id in (:orgId)");
				param.put("orgId",dto.getOrgId().split(","));
			}
			if(StringUtil.isNotBlank(dto.getRoleId())){
				sql.append(" and uor.role_id =:roleId");
				param.put("roleId",dto.getRoleId());
			}
		}
		sql.append(" and uor.organization_id in " +roleQLConfigService.getAllOrgAppendSql());
		sql.append(" )");



		if (org.apache.commons.lang.StringUtils.isNotBlank(dp.getSord())
				&& org.apache.commons.lang.StringUtils.isNotBlank(dp.getSidx())) {
			sql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		}else{
			sql.append(" order by modify_time desc");
		}

        return this.findMapPageBySQL(sql.toString(),dp,true,param);
    }

}
