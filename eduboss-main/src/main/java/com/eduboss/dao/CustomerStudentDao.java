package com.eduboss.dao;

import com.eduboss.domain.CustomerStudent;
import com.eduboss.domain.CustomerStudentId;

public interface CustomerStudentDao extends GenericDAO<CustomerStudent, CustomerStudentId> {

	/**
	 * 根据客户id，学生id获取客户学生关系
	 * @param customerId
	 * @param studentId
	 * @return
	 */
	CustomerStudent getOneCustomerStudent(String customerId, String studentId);
	
}
