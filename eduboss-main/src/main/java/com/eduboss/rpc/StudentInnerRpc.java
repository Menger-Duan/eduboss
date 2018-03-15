package com.eduboss.rpc;

import com.eduboss.rpc.dto.Student;

import java.util.List;

/**
 * 学生接口
 *
 * @author sk
 *
 */
public interface StudentInnerRpc {
    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber 父母（客户）手机号
     * @return 学生列表
     */
    List<Student> listStudentByParentPhoneNumber(String phoneNumber);


    /**
     * 新增或者修改学生，新增学生必须有父母手机号
     *
     * @param student 学生信息
     * @return 返回学生ID
     */
    String saveStudent(Student student, String parentPhoneNumber);
}
