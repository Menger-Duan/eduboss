package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentCommentDao;
import com.eduboss.domain.StudentComment;

@Repository(value = "StudentCommentDao")
public class StudentCommentDaoImpl extends GenericDaoImpl<StudentComment, String> implements StudentCommentDao {
	
	

}
