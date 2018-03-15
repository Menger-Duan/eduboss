package com.eduboss.dao.impl;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseAttendanceBusinessDao;
import com.eduboss.domain.CourseAttendanceBusiness;

@Repository("CourseAttendanceBusinessDao")
public class CourseAttendanceBusinessDaoImpl extends GenericDaoImpl<CourseAttendanceBusiness, String> implements CourseAttendanceBusinessDao {


}
