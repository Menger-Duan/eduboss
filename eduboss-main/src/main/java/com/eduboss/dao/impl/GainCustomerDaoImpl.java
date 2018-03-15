package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.GainCustomerDao;
import com.eduboss.domain.GainCustomer;

@Repository("GainCustomerDao")
public class GainCustomerDaoImpl extends GenericDaoImpl<GainCustomer, String> implements GainCustomerDao {

}

