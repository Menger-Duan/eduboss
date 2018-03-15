package com.eduboss.dao;

import com.eduboss.domain.UserJob;


public interface UserJobDao extends GenericDAO<UserJob, String>{

	public int getUserJobCountByJobName(UserJob vo);
}
