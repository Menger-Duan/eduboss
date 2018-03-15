package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.eduboss.dto.DispNoGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.UserJobDao;
import com.eduboss.domain.UserJob;

@Repository("UserJobDao")
public class UserJobDaoImpl extends GenericDaoImpl<UserJob,String> implements UserJobDao {

	public int getUserJobCountByJobName(UserJob vo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql=" select count(1) from user_job where 1=1";
		String sqlWhere="";
		if (StringUtils.isNotEmpty(vo.getJobName())) {
			sqlWhere+=" and JOB_NAME = :jobName ";
			params.put("jobName", vo.getJobName());
		}
		if (StringUtils.isNotEmpty(vo.getId())) {
			sqlWhere+=" and id <> :id ";
			params.put("id", vo.getId());
		}
		sql += sqlWhere;
		return findCountSql(sql, params);
	}

	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public void save(UserJob userJob) {
		DispNoGenerator dispNoGenerator = new DispNoGenerator();
		if (userJob.getId() == null){
			String id = dispNoGenerator.generate("USER_JOB", hibernateTemplate.getSessionFactory().openSession());
			userJob.setId(id);
		}
		super.save(userJob);
	}
}
