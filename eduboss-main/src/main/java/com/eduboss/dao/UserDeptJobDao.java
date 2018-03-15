package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.UserDeptJob;


@Repository
public interface UserDeptJobDao extends GenericDAO<UserDeptJob, String> {
	
	public void marginUserDeptJobList(String userId, String deptIds,String jobIds);

	public void marginUserDeptJobList(List<UserDeptJob> userDeptJobs,String userId);

	public List<UserDeptJob> findDeptJobByUserId(String userId);
	
	public UserDeptJob findDeptJobByParam(String userId, int isMajorRole);
	
	public List<UserDeptJob> findDeptJobByUserIdWithDeptId(String userId,String deptId);
	
}
