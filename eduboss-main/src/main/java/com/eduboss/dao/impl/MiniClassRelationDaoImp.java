package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MiniClassRelationDao;
import com.eduboss.domain.MiniClassRelation;

@Repository("MiniClassRelationDao")
public class MiniClassRelationDaoImp extends GenericDaoImpl<MiniClassRelation, String> implements
		MiniClassRelationDao {

}
