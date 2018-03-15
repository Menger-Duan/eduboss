package com.eduboss.dao;

import com.eduboss.domain.CourseModalWeek;

import java.util.List;

public interface CourseModalWeekDao extends GenericDAO<CourseModalWeek, String> {

    List<CourseModalWeek> findModalWeekByModalId(int modalId);
}
