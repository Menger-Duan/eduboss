package com.eduboss.openapi.rpc.impl;

import com.eduboss.openapi.service.StudentService;
import com.eduboss.rpc.StudentInnerRpc;
import com.eduboss.rpc.dto.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("StudentInnerRpc")
public class StudentRpcImpl implements StudentInnerRpc {
    @Autowired
    private StudentService studentService;



    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber 父母（客户）手机号
     * @return 学生列表
     */
    @Override
    public List<Student> listStudentByParentPhoneNumber(String phoneNumber) {
        List<Student> students = studentService.listStudentByParentPhoneNumberInner(phoneNumber);
        return students;
    }

    /**
     * 新增或者修改学生，新增学生必须有父母手机号
     *
     * @param student           学生信息
     * @param parentPhoneNumber
     * @return 返回学生ID
     */
    @Override
    public String saveStudent(Student student, String parentPhoneNumber) {
        return studentService.saveStudentInner(student, parentPhoneNumber);
    }
}
