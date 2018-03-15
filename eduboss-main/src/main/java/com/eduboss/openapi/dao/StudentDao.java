package com.eduboss.openapi.dao;

import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.Student;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */
public interface StudentDao extends GenericDAO<Student, String> {
    /**
     *根据父母手机号，查询关联的学生列表
     * @param phoneNumber
     * @return
     */
    List<Student> listStudentByParentPhoneNumber(String phoneNumber);

    /**
     * 根据编号和父母手机号，查询关联的学生
     * @param id
     * @param phoneNumber
     * @return
     */
    Student fiindStudentByIdAndParentPhoneNumber(String id, String phoneNumber);

}
