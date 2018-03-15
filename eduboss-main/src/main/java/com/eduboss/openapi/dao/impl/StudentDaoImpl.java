package com.eduboss.openapi.dao.impl;

import com.eduboss.dao.impl.GenericDaoImpl;
import com.eduboss.domain.Student;
import com.eduboss.openapi.dao.StudentDao;
import com.eduboss.utils.StringUtil;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 */

@Repository(value = "StudentDaoRpc")
public class StudentDaoImpl extends GenericDaoImpl<Student, String> implements StudentDao {
    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public List<Student> listStudentByParentPhoneNumber(String phoneNumber) {
        List<Student> students = new ArrayList<>();
        if (StringUtil.isNotBlank(phoneNumber)){
            StringBuffer hql = new StringBuffer();
            Map<String, Object> params = new HashMap<>();
            hql.append("select s from Student s , CustomerStudent cs ,Customer c where   s.id = cs.student.id and cs.customer.id = c.id and  c.contact = :phoneNumber ");
            params.put("phoneNumber", phoneNumber);
            students = this.findAllByHQL(hql.toString(), params);
        }
        return students;
    }

    /**
     * 根据编号和父母手机号，查询关联的学生
     */
    @Override
    public Student fiindStudentByIdAndParentPhoneNumber(String id,
            String phoneNumber) {
        List<Student> students = null;
        if (StringUtil.isNotBlank(phoneNumber)){
            StringBuffer hql = new StringBuffer();
            Map<String, Object> params = new HashMap<>();
            hql.append("select s from Student s , CustomerStudent cs ,Customer c where   s.id = cs.student.id "
                    + " and cs.customer.id = c.id and  c.contact = :phoneNumber and s.id = :id ");
            params.put("id", id);
            params.put("phoneNumber", phoneNumber);
            students = this.findAllByHQL(hql.toString(), params);
        }
        if (students != null && !students.isEmpty()) {
            return students.get(0);
        }
        return null;
    }

    

}
