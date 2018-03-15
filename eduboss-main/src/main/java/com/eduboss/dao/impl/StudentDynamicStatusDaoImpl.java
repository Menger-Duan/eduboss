package com.eduboss.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerDynamicStatusDao;
import com.eduboss.dao.StudentDynamicStatusDao;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.StudentDynamicStatus;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.HibernateUtils;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("StudentDynamicStatusDao")
public class StudentDynamicStatusDaoImpl extends GenericDaoImpl<StudentDynamicStatus, String> implements StudentDynamicStatusDao {

	private static final Logger log = LoggerFactory.getLogger(StudentDynamicStatusDaoImpl.class);
	// property constants

	@Override
	public DataPackage findByStudentId(String studentId, DataPackage dp) {
		return super.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "occourTime", "desc"), Expression.eq("student.id", studentId));
	}
	
}
