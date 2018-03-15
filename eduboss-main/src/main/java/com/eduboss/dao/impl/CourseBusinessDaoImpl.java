package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseBusinessDao;
import com.eduboss.domain.CourseBusiness;

@Repository("CourseBusinessDao")
public class CourseBusinessDaoImpl extends GenericDaoImpl<CourseBusiness, String> implements CourseBusinessDao {


}
