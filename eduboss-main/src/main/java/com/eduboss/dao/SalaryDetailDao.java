package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.SalaryDetail;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SalaryDetailVo;
import com.eduboss.dto.DataPackage;

public interface SalaryDetailDao extends GenericDAO<SalaryDetail, String>{

	DataPackage getsalaryDetailList(DataPackage dataPackage,
			SalaryDetailVo salaryDetailVo, Map<String, Object> params);
	
	public List<User> getLimitUserAutoComplate(String term, String extraHql);

}
