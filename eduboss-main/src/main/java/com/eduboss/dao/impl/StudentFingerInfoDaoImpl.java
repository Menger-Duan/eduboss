package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentFingerInfoDao;
import com.eduboss.domain.StudentFingerInfo;

@Repository
public class StudentFingerInfoDaoImpl  extends GenericDaoImpl<StudentFingerInfo, String> implements StudentFingerInfoDao {
	
}
