package com.eduboss.openapi.controller;

import com.eduboss.rpc.DataDictInnerRpc;
import com.eduboss.rpc.StudentInnerRpc;
import com.eduboss.rpc.dto.Label;
import com.eduboss.rpc.dto.StudentVo;
import com.eduboss.rpc.dto.Student;
import com.eduboss.utils.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by Administrator on 2017/3/22.
 */

@RequestMapping(value="/openapi")
@Controller
public class Openapi {


    @Autowired
    private StudentInnerRpc studentRpc;

    @Autowired
    private DataDictInnerRpc dataDictInnerRpc;

    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber 父母（客户）手机号
     * @return 学生列表
     */
    @RequestMapping(value="/listStudentByParentPhoneNumber")
    @ResponseBody
    public List listStudentByParentPhoneNumber(String phoneNumber){
        List<Student> list = studentRpc.listStudentByParentPhoneNumber(phoneNumber);
        return list;
    }

    /**
     * 获取全部省份列表
     *
     * @return 返回省份列表
     */
    @RequestMapping(value="/listAllProvince")
    @ResponseBody
    public List listAllProvince(){
        List<Label> allProvince = dataDictInnerRpc.listAllProvince();
        return allProvince;
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceID 省份ID
     * @return 返回省份下的城市列表
     */
    @RequestMapping(value="/listCityByProvinceID")
    @ResponseBody
    public List listCityByProvinceID(String provinceID){
        return dataDictInnerRpc.listCityByProvinceID(provinceID);
    }

    /**
     * 根据位置信息获取学校列表
     *
     * @param provinceID 省份ID
     * @param cityID     城市ID
     * @return 返回学校列表
     */
    @RequestMapping(value="/listSchoolByRegion")
    @ResponseBody
    public List listSchoolByRegion(String provinceID, String cityID){
        return dataDictInnerRpc.listSchoolByRegion(provinceID, cityID);
    }

    /**
     * 获取全部年级信息
     *
     * @return 返回年级列表
     */
    @RequestMapping(value="/listAllGrade")
    @ResponseBody
    public List listAllGrade(){
        return dataDictInnerRpc.listAllGrade();
    }





    /**
     * @param parentPhoneNumber  新增或者修改学生，新增学生必须有父母手机号
     * @param studentVo
     * @return studentId 返回学生ID
     */
    @RequestMapping(value="/saveStudent")
    @ResponseBody
    public String saveStudent(String parentPhoneNumber, StudentVo studentVo){

//        Student student = new Student();
//        student.setStudentID(studentID);
//        student.setName(name);
//        student.setGender(gender);
//        Label provcice = new Label();
//        provcice.setId(provinceID);
//        student.setProvince(provcice);
//        Label city = new Label();
//        city.setId(cityID);
//        student.setCity(city);
//        Label school = new Label();
//        school.setId(schoolID);
//        student.setSchool(school);
//        Label grade = new Label();
//        grade.setId(gradeID);
//        student.setGrade(grade);
        Student student = HibernateUtils.voObjectMapping(studentVo, Student.class, "saveStudentInner");
        String studentId = studentRpc.saveStudent(student, parentPhoneNumber);
        return studentId;
    }
}
