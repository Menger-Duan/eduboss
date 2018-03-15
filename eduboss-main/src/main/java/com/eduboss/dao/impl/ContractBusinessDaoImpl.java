package com.eduboss.dao.impl;

import com.eduboss.dao.ContractBusinessDao;
import com.eduboss.domain.ContractBusiness;
import org.springframework.stereotype.Repository;

@Repository("ContractBusinessDao")
public class ContractBusinessDaoImpl extends GenericDaoImpl<ContractBusiness, String> implements ContractBusinessDao {

}
