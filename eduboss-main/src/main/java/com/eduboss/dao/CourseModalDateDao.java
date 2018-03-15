package com.eduboss.dao;

import com.eduboss.domain.CourseModalDate;

import java.util.List;

public interface CourseModalDateDao extends GenericDAO<CourseModalDate, String> {

    List<CourseModalDate> findModalDateByModalId(int modalId);
}
