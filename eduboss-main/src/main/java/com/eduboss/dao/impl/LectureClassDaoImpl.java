package com.eduboss.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.LectureClassDao;
import com.eduboss.domain.LectureClass;

@Repository("LectureClassDao")
public class LectureClassDaoImpl extends GenericDaoImpl<LectureClass, String> implements LectureClassDao {
	
	private static final Logger log = LoggerFactory.getLogger(LectureClassDaoImpl.class);
	

}
