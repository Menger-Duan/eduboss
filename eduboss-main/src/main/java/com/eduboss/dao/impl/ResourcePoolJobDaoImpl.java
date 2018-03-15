package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.ResourcePoolJobDao;
import com.eduboss.domain.ResourcePoolJob;

@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class ResourcePoolJobDaoImpl extends GenericDaoImpl<ResourcePoolJob, String> implements ResourcePoolJobDao{

}
