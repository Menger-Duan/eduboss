package com.eduboss.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerAppointmentDao;
import com.eduboss.dao.RefundContractWfDao;
import com.eduboss.domain.CustomerAppointment;
import com.eduboss.domain.RefundContractWf;

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
@Repository("RefundContractWfDao")
public class RefundContractWfDaoImpl extends GenericDaoImpl<RefundContractWf, String> implements RefundContractWfDao {

	private static final Logger log = LoggerFactory.getLogger(RefundContractWfDaoImpl.class);
	// property constants
	
}
