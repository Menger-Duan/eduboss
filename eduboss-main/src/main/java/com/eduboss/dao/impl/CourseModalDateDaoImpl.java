package com.eduboss.dao.impl;

import com.eduboss.dao.CourseModalDateDao;
import com.eduboss.domain.CourseModalDate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("CourseModalDateDao")
public class CourseModalDateDaoImpl extends GenericDaoImpl<CourseModalDate, String> implements CourseModalDateDao {

    @Override
    public List<CourseModalDate> findModalDateByModalId(int modalId) {
        Map<String,Object> map = new HashMap<>();
        String hql = "from CourseModalDate where modalId=:modalId";
        map.put("modalId",modalId);
        return this.findAllByHQL(hql,map);
    }
}
