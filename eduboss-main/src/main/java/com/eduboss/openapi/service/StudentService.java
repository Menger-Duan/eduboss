package com.eduboss.openapi.service;


import java.util.List;

import com.eduboss.rpc.dto.Student;

import org.boss.rpc.eduboss.service.dto.StudentRpcVo;

/**
 * Created by Administrator on 2017/3/23.
 */
public interface StudentService {
    /**
     *根据父母手机号，查询关联的学生列表
     * @param phoneNumber
     * @return
     */
    List<StudentRpcVo> listStudentByParentPhoneNumber(String phoneNumber);
    
    /**
     * 设置学生的在线报读关联的学生编号
     * @param studentId
     * @param bossMobileStudentNo
     * @param 客户手机号
     */
    void setBossMobileNo(String studentId, String bossMobileStudentNo, String phone);


    /**
     * 保存学生
     * @param student
     * @param parentPhoneNumber
     * @return
     */
    String saveStudent(StudentRpcVo student, String parentPhoneNumber);


    /**
     *根据父母手机号，查询关联的学生列表
     * @param phoneNumber
     * @return
     */
    List<Student> listStudentByParentPhoneNumberInner(String phoneNumber);

    /**
     * 保存学生
     * @param student
     * @param parentPhoneNumber
     * @return
     */
    String saveStudentInner(Student student, String parentPhoneNumber);
}
