package com.eduboss.dao.impl;

import com.eduboss.dao.CourseModalDateDao;
import com.eduboss.dao.CourseModalWeekDao;
import com.eduboss.domain.CourseModalDate;
import com.eduboss.domain.CourseModalWeek;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("CourseModalWeekDao")
public class CourseModalWeekDaoImpl extends GenericDaoImpl<CourseModalWeek, String> implements CourseModalWeekDao {

    @Override
    public List<CourseModalWeek> findModalWeekByModalId(int modalId) {
        Map<String,Object> map = new HashMap<>();
        String hql = "from CourseModalWeek where courseModal.id=:modalId";
        map.put("modalId",modalId);
        return this.findAllByHQL(hql,map);
    }
}
