package com.eduboss.service.impl.rpc;

import java.util.ArrayList;
import java.util.List;

import org.boss.rpc.base.dto.Label;
import org.boss.rpc.base.dto.PageRpcVo;
import org.boss.rpc.eduboss.service.StudentRpc;
import org.boss.rpc.eduboss.service.dto.SchoolSearchRpcVo;
import org.boss.rpc.eduboss.service.dto.StudentRpcVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domain.StudentSchool;
import com.eduboss.dto.DataPackage;
import com.eduboss.openapi.service.StudentService;
import com.eduboss.service.StudentSchoolService;

/**
 * Created by Administrator on 2017/3/22.
 */

@Service("studentRpcImpl")
public class StudentRpcImpl implements StudentRpc {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentSchoolService studentSchoolService;

    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber 父母（客户）手机号
     * @return 学生列表
     */
    @Override
    public List<StudentRpcVo> listStudentByParentPhoneNumber(String phoneNumber) {
        List<StudentRpcVo> students = studentService.listStudentByParentPhoneNumber(phoneNumber);
        return students;
    }
    
    @Override
    public void setBossMobileNo(String studentId, String bossMobileStudentNo, String phone) {
        studentService.setBossMobileNo(studentId, bossMobileStudentNo, phone);
    }

    /**
     * 新增或者修改学生，新增学生必须有父母手机号
     *
     * @param student           学生信息
     * @param parentPhoneNumber
     * @return 返回学生ID
     */
    @Override
    public String saveStudent(StudentRpcVo student, String parentPhoneNumber) {
        return studentService.saveStudent(student, parentPhoneNumber);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageRpcVo<Label> listStudentSchools(SchoolSearchRpcVo schoolSearch) {
        DataPackage dp = new DataPackage(schoolSearch.getPageNo(), schoolSearch.getPageSize());
        dp = studentSchoolService.listPageSchoolByRegionAndName(schoolSearch.getProvinceNo(), 
                schoolSearch.getCityNo(), schoolSearch.getName(), dp);
        PageRpcVo<Label> resultVo = new PageRpcVo<Label>();
        resultVo.setCurrent(dp.getPageNo());
        resultVo.setSize(dp.getPageSize());
        resultVo.setTotal(dp.getRowCount());
        List<StudentSchool> list = (List<StudentSchool>) dp.getDatas();
        List<Label> voList = new ArrayList<Label>();
        for (StudentSchool school: list) {
            Label lable = new Label();
            lable.setId(school.getId());
            lable.setText(school.getName());
            voList.add(lable);
        }
        resultVo.setRecords(voList);
        return resultVo;
    }

    @Override
    public List<Label> listSchoolByIds(String[] schoolIds) {
        List<Label> voList = new ArrayList<Label>();
        List<StudentSchool> list = studentSchoolService.listSchoolByIds(schoolIds);
        for (StudentSchool school: list) {
            Label lable = new Label();
            lable.setId(school.getId());
            lable.setText(school.getName());
            voList.add(lable);
        }
        return voList;
    }

}
