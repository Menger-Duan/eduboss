package com.eduboss.dao.impl;

import com.eduboss.dao.TwoTeacherClassCourseDao;
import com.eduboss.domain.TwoTeacherClassCourse;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/3/30.
 */
@Repository("TwoTeacherClassCourseDao")
public class TwoTeacherClassCourseDaoImpl extends GenericDaoImpl<TwoTeacherClassCourse, Integer> implements TwoTeacherClassCourseDao {
}
