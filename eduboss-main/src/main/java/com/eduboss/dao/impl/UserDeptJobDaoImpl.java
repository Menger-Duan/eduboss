package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.domain.UserDeptJob;

@Repository("UserDeptJobDaoImpl")
public class UserDeptJobDaoImpl extends GenericDaoImpl<UserDeptJob, String> implements UserDeptJobDao {
	
	@Override
	public void marginUserDeptJobList(String userId, String deptIds,
			String jobIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql =" DELETE  from user_dept_job where user_id = :userId ";
		params.put("userId", userId);
		this.excuteSql(sql, params);


		List<String> deptArray = stringToList(deptIds);
		List<String> jobArray = stringToList(jobIds);
		
		if (deptArray.size() > 0 && jobArray.size()>0) {
			for (int i = 0; i < deptArray.size(); i++) {
				String dept = deptArray.get(i);
				String job = jobArray.get(i);
				if(StringUtils.isNotBlank(dept)&& StringUtils.isNotBlank(job)){
					if(i==0)
					super.save(new UserDeptJob(userId, dept,job,0));
					else
					super.save(new UserDeptJob(userId, dept,job,1));
				}
			}
			
		}
	}

	@Override
	public void marginUserDeptJobList(List<UserDeptJob> userDeptJobs,String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql ="select * from user_dept_job where user_id = :userId";
		params.put("userId", userId);
		List<UserDeptJob> userDeptJobsDB =this.findBySql(sql, params);
		for (Iterator<UserDeptJob> itDb=userDeptJobsDB.iterator();itDb.hasNext();){
			UserDeptJob udjDB=(UserDeptJob)itDb.next();
			this.delete(udjDB);
			this.commit();
		}

		for(UserDeptJob userDeptJob:userDeptJobs){
			this.save(userDeptJob);
			this.commit();
		}
	}

	@Override
	public List<UserDeptJob> findDeptJobByUserId(String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserDeptJob where userId = :userId order by isMajorRole asc ";
		params.put("userId", userId);
		return super.findAllByHQL(hql, params);
	}
	
	@Override
	public UserDeptJob findDeptJobByParam(String userId, int isMajorRole) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from UserDeptJob where userId = :userId and isMajorRole = :isMajorRole ";
		params.put("userId", userId);
		params.put("isMajorRole", isMajorRole);
		super.findAllByHQL(hql, params);
		List<UserDeptJob> userRoles = super.findAllByHQL(hql, params);
		if(userRoles.size() == 0)
			return null;
		else
			return userRoles.get(0);
	}
	
	@Override
	public List<UserDeptJob> findDeptJobByUserIdWithDeptId(String userId,String deptId) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(deptId!=null){
			String hql = " from UserDeptJob where userId = :userId and deptId = :deptId order by isMajorRole asc ";
			params.put("userId", userId);
			params.put("deptId", deptId);
			List<UserDeptJob> userDeptJobList = super.findAllByHQL(hql, params);
			
			String hql2 = " from UserDeptJob where userId = :userId order by isMajorRole asc ";
			Map<String, Object> params2 = new HashMap<String, Object>();
			params2.put("userId", userId);
			List<UserDeptJob> userRoles = super.findAllByHQL(hql2, params2);
			for(UserDeptJob udj : userRoles){
				if(!userDeptJobList.contains(udj)) userDeptJobList.add(udj);
			}
			return userDeptJobList;
		}else{
			String hql = " from UserDeptJob where userId = :userId order by isMajorRole asc ";
			params.put("userId", userId);
			return super.findAllByHQL(hql, params);
		}
	}
	
	private List<String> stringToList(String listString) {
		List<String> returnList = new ArrayList<String> ();
		for (String itemString : listString.split(",")) {
			returnList.add(itemString);
		}
		return returnList;
	}
	
}
