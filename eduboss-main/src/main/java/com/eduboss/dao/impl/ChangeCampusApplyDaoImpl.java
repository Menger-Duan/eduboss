package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.ChangeCampusApplyDao;
import com.eduboss.domain.ChangeCampusApply;

@Repository("changeCampusApplyDao")
public class ChangeCampusApplyDaoImpl extends GenericDaoImpl<ChangeCampusApply,String> implements ChangeCampusApplyDao{

}
