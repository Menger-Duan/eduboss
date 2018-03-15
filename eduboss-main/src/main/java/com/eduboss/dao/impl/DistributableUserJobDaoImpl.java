package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.DistributableUserJobDao;
import com.eduboss.domain.DistributableUserJob;

@Repository("distributableUserJobDao")
public class DistributableUserJobDaoImpl extends GenericDaoImpl<DistributableUserJob, String> implements DistributableUserJobDao{

}
