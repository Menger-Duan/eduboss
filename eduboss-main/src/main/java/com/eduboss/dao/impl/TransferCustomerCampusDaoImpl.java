package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.TransferCustomerCampusDao;
import com.eduboss.domain.TransferCustomerCampus;

@Repository("TransferCustomerCampusDao")
public class TransferCustomerCampusDaoImpl  extends GenericDaoImpl<TransferCustomerCampus, String> implements TransferCustomerCampusDao {


}

