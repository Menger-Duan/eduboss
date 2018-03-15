package com.eduboss.dao.impl;

import com.eduboss.dao.StudentAccInfoDao;
import com.eduboss.dao.TwoTeacherBrenchDao;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentAccInfo;
import com.eduboss.domain.TwoTeacherClassBrench;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;

@Repository("StudentAccInfoDao")
public class StudentAccInfoDaoImpl extends GenericDaoImpl<StudentAccInfo, String> implements StudentAccInfoDao {
    @Override
    public StudentAccInfo findInfoByStudentId(String id) {
        String hql = " from StudentAccInfo where student.id = '"+id+"'";
        StudentAccInfo info=this.findOneByHQL(hql,new HashMap<>());
        if(info==null){
            info = new StudentAccInfo();
            info.setAccountAmount(BigDecimal.ZERO);
            info.setStudent(new Student(id));
            this.save(info);
            this.commit();
        }
        return info;
    }
}
