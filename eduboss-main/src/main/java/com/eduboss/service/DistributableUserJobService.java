package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.DistributableUserJob;
import com.eduboss.domain.UserJob;

public interface DistributableUserJobService {

	// 保存
	public void save(DistributableUserJob distributableUserJob);
	
	// 删除 根据联合主键来删
	public void delete(DistributableUserJob distributableUserJob);
	
	//根据主职位id获取关联职位
	public List<UserJob> findRelateUserJobByMainJobId(String jobId);
	
	//根据主职位Id删除所有的关联职位
	public void deleteAllRelateUuerJobByMainJobId(String jobId);
	
	//根据主职位id 和所属部门orgLevel获取关联职位
	public List<UserJob> findRelateUserJobByJobIdAndOrgLevel(String jobId,String orgLevel);
	
}
