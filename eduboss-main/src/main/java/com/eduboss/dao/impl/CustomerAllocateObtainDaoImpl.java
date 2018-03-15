package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CustomerAllocateObtainDao;
import com.eduboss.domain.CustomerAllocateObtain;

@Repository("customerAllocateObtainDao")
public class CustomerAllocateObtainDaoImpl extends GenericDaoImpl<CustomerAllocateObtain,Integer> implements CustomerAllocateObtainDao {

}
