/**
 * 
 */
package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.service.UserDeptJobService;

import liquibase.sql.Sql;

/**
 * @author yjg
 */

@Service("userDeptJobService")
public class UserDeptJobServiceImpl implements UserDeptJobService {

	@Autowired
	private UserDeptJobDao userDeptJobDao;
	@Autowired
	private OrganizationDao organizationDao;
	
	@Override
	public List<UserDeptJob> findDeptJobByUserIdWithDeptId(String userId,
			String deptId) {
		return userDeptJobDao.findDeptJobByUserIdWithDeptId(userId, deptId);
	}
	
	@Override
	public List<UserDeptJob> findDeptJobByUserId(String userId) {
		return userDeptJobDao.findDeptJobByUserId(userId);
	}

	@Override
	public List<UserDeptJob> getUserMainDeptJobByUserId(String userId) {
		Map<String, Object> params = new HashMap();
		params.put("userId", userId);
		String sql = "SELECT * from user_dept_job WHERE isMajorRole =0 and  USER_ID = :userId ";
		return userDeptJobDao.findBySql(sql, params);
	}

	@Override
	public List<Organization> getDeptByJobIds(String branchOrgLevel,Organization campus,String currentOrgLevel,Boolean isNetwork,String jobIds,String currentJobIds,String otherJobIds) {
		//String sql = "SELECT o.* from organization o,user_dept_job udj where o.id = udj.DEPT_ID and o.orgLevel like '"+orgLevel+"%' and udj.JOB_ID in ( "+jobIds+" )";
		String sql = null ;
		if(isNetwork){
			Map<String, Object> params = new HashMap<>();
			sql = "SELECT o.* from organization o,user_dept_job udj where o.id = udj.DEPT_ID and udj.JOB_ID in ("+jobIds+")";
			return organizationDao.findBySql(sql, params);
		}else{
			List<Organization> allOrgs = new ArrayList<Organization>();
			//分成两部分 本部门职位就当前组织架构的权限
			if(campus!=null){
				if(currentJobIds!=null){
					Map<String, Object> params1 = new HashMap<>();
					params1.put("currentOrgLevel", currentOrgLevel+"%");
					String currentSql = "SELECT o.* from organization o,user_dept_job udj where o.id = udj.DEPT_ID and o.orgLevel like :currentOrgLevel  and udj.JOB_ID in ("+currentJobIds+")";
				    allOrgs.addAll(organizationDao.findBySql(currentSql, params1));
				}
				if(otherJobIds!=null){
					Map<String, Object> params2 = new HashMap<>();
					params2.put("orgLevel", campus.getOrgLevel()+"%");
					String otherSql = "SELECT o.* from organization o,user_dept_job udj where o.id = udj.DEPT_ID and o.orgLevel like :orgLevel and udj.JOB_ID in ("+otherJobIds+")";
				    allOrgs.addAll(organizationDao.findBySql(otherSql, params2));
				}				
			}else{
				if(currentJobIds!=null){
					Map<String, Object> params3 = new HashMap<>();
					params3.put("currentOrgLevel", currentOrgLevel+"%");
					String currentSql = "SELECT o.* from organization o,user_dept_job udj where o.id = udj.DEPT_ID and o.orgLevel like :currentOrgLevel and udj.JOB_ID in ("+currentJobIds+")";
				    allOrgs.addAll(organizationDao.findBySql(currentSql, params3));
				}
				if(otherJobIds!=null){
					Map<String, Object> params4 = new HashMap<>();
					params4.put("branchOrgLevel", branchOrgLevel+"%");
					String otherSql = "SELECT o.* from organization o,user_dept_job udj where o.id = udj.DEPT_ID and o.orgLevel like :branchOrgLevel and udj.JOB_ID in ("+otherJobIds+")";
				    allOrgs.addAll(organizationDao.findBySql(otherSql, params4));
				}				
			}

			return allOrgs;
		}		
	}

	@Override
	public List<UserDeptJob> getDeptJobByKey(String key, String userId) {
		Map<String, Object> params = new HashMap<>();
		params.put("userId", userId);
		params.put("key", "%"+key+"%");
		String sql  ="SELECT udj.* from user_dept_job udj,user_job uj where udj.JOB_ID = uj.ID and udj.USER_ID = :userId and uj.JOB_NAME LIKE :key ";
		return userDeptJobDao.findBySql(sql, params);
	}

}
