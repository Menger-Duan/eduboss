package com.eduboss.dao.impl;

import com.eduboss.dao.TwoTeacherClassStudentDao;
import com.eduboss.domain.TwoTeacherClassStudent;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/30.
 */
@Repository("TwoTeacherClassStudentDao")
public class TwoTeacherClassStudentDaoImpl extends GenericDaoImpl<TwoTeacherClassStudent, String> implements TwoTeacherClassStudentDao {
    @Override
    public TwoTeacherClassStudent getTwoTeacherClassStudent(String studentId, int classTwoId) {
        String hql = " from TwoTeacherClassStudent where student.id = :studentId and classTwo.id = :classTwoId ";
        Map<String, Object> params = new HashMap<>();
        params.put("studentId", studentId);
        params.put("classTwoId", classTwoId);
        return super.findOneByHQL(hql, params);
    }
}
