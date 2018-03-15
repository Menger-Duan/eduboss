package com.eduboss.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eduboss.domain.Organization;
import com.eduboss.domain.UserDeptJob;

@Service
public interface UserDeptJobService {
	
	 public List<UserDeptJob> findDeptJobByUserIdWithDeptId(String userId,String deptId);
	 
	 public List<UserDeptJob> findDeptJobByUserId(String userId);
	 
	 public List<UserDeptJob> getUserMainDeptJobByUserId(String userId);
	 
	 //根据职位id来获取所属部门（组织架构）
	 public List<Organization> getDeptByJobIds(String branchOrgLevel,Organization campus,String currentOrgLevel,Boolean isNetwork,String jobIds,String currentJobIds,String otherJobIds);
	 
	 //判断userId是否具有特定职位
	 public List<UserDeptJob> getDeptJobByKey(String key,String userId);
	 
	 
}
