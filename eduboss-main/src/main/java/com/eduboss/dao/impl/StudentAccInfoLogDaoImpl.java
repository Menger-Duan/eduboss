package com.eduboss.dao.impl;

import com.eduboss.dao.StudentAccInfoLogDao;
import com.eduboss.domain.StudentAccInfoLog;
import org.springframework.stereotype.Repository;

@Repository("StudentAccInfoLogDao")
public class StudentAccInfoLogDaoImpl extends GenericDaoImpl<StudentAccInfoLog, String> implements StudentAccInfoLogDao {
}
