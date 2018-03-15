package com.eduboss.dao;

import com.eduboss.domain.CourseModal;
import com.eduboss.domainVo.CourseModalVo;

import java.util.List;

public interface CourseModalDao extends GenericDAO<CourseModal, String> {

    List<CourseModal> getCourseModalList(CourseModalVo vo);
}
