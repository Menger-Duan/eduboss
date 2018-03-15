package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.DisabledCustomerDao;
import com.eduboss.domain.DisabledCustomer;

@Repository("disabledCustomerDao")
public class DisabledCustomerDaoImpl extends GenericDaoImpl<DisabledCustomer,Integer> implements DisabledCustomerDao {

}
