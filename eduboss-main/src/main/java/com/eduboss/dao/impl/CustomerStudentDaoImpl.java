package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerStudentDao;
import com.eduboss.domain.CustomerStudent;
import com.eduboss.domain.CustomerStudentId;
import com.eduboss.utils.StringUtil;

@Repository
public class CustomerStudentDaoImpl extends GenericDaoImpl<CustomerStudent, CustomerStudentId> implements
		CustomerStudentDao {

	/**
	 * 根据客户id，学生id获取客户学生关系
	 */
	@Override
	public CustomerStudent getOneCustomerStudent(String customerId,
			String studentId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from CustomerStudent where 1=1 ";
		if (StringUtil.isNotBlank(customerId)) {
			hql += " and customer.id = :customerId ";
			params.put("customerId", customerId);
		}
		if (StringUtil.isNotBlank(studentId)) {
			hql += " and student.id = :studentId ";
			params.put("studentId", studentId);
		}
		List<CustomerStudent> list = super.findAllByHQL(hql, params);
		return list.size() > 0 ? list.get(0) : null;
	}
	
}
