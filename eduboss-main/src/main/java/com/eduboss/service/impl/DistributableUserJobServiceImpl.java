package com.eduboss.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.DistributableUserJobDao;
import com.eduboss.domain.DistributableUserJob;
import com.eduboss.domain.UserJob;
import com.eduboss.service.DistributableUserJobService;
import com.eduboss.service.UserJobService;
import com.google.common.collect.Maps;


@Service("distributableUserJobService")
public class DistributableUserJobServiceImpl implements DistributableUserJobService{

	@Autowired
	private DistributableUserJobDao distributableUserJobDao;
	
	@Autowired
	private UserJobService userJobService;

	@Override
	public void save(DistributableUserJob distributableUserJob) {
		distributableUserJobDao.save(distributableUserJob);		
	}

	@Override
	public void delete(DistributableUserJob distributableUserJob) {
		distributableUserJobDao.delete(distributableUserJob);
	}

	@Override
	public List<UserJob> findRelateUserJobByMainJobId(String jobId) {
		String sql = "select uj.* from user_job uj,distributable_user_job duj where uj.ID = duj.relate_job_id and duj.job_id = :jobId ";
		Map<String, Object> params = new HashMap();
		params.put("jobId", jobId);
		return userJobService.findAllUserJobBySql(sql, params);
	}

	@Override
	public void deleteAllRelateUuerJobByMainJobId(String jobId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("jobId", jobId);
		String sql ="delete from distributable_user_job where job_id = :jobId ";
		distributableUserJobDao.excuteSql(sql,params);
	}

	@Override
	public List<UserJob> findRelateUserJobByJobIdAndOrgLevel(String jobId, String orgLevel) {
		String sql = "select uj.* from distributable_user_job duj,user_job uj,user_dept_job udj,organization o where duj.relate_job_id = uj.ID and duj.relate_job_id = udj.JOB_ID "+
	   "and o.id = udj.DEPT_ID and duj.job_id = :jobId and o.orgLevel like  :orgLevel ";
		Map<String, Object> params = new HashMap();
		params.put("jobId", jobId);
		params.put("orgLevel", orgLevel+"%");
		return userJobService.findAllUserJobBySql(sql, params);
	}
	
	
	
	
	
}
