package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentFileDao;
import com.eduboss.domain.StudentFile;

@Repository("StudentFileDao")
public class StudentFileDaoImpl extends GenericDaoImpl<StudentFile, String> implements StudentFileDao{

}
