package com.eduboss.openapi.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.Region;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentSchool;
import com.eduboss.exception.ApplicationException;
import com.eduboss.openapi.dao.DataDictDao;
import com.eduboss.openapi.dao.RegionDao;
import com.eduboss.openapi.dao.StudentDao;
import com.eduboss.openapi.dao.StudentSchoolDao;
import com.eduboss.openapi.service.StudentService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

/**
 * Created by Administrator on 2017/3/23.
 */
@Service("StudentServiceRpc")
public class StudentServiceImpl implements StudentService {


    @Autowired
    private StudentDao studentDao;

    @Autowired
    private RegionDao regionDao;

    @Autowired
    private StudentSchoolDao studentSchoolDao;

    @Autowired
    private DataDictDao dataDictDao;


    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public List<org.boss.rpc.eduboss.service.dto.StudentRpcVo> listStudentByParentPhoneNumber(String phoneNumber) {
        List<Student> students = studentDao.listStudentByParentPhoneNumber(phoneNumber);
        List<org.boss.rpc.eduboss.service.dto.StudentRpcVo> rpcStudentVo = HibernateUtils.voListMapping(students, org.boss.rpc.eduboss.service.dto.StudentRpcVo.class, "rpcStudentVo");
        for (org.boss.rpc.eduboss.service.dto.StudentRpcVo vo : rpcStudentVo) {
            if (StringUtil.isNotBlank(vo.getCityNo())) {
                vo.setCityName(regionDao.findById(vo.getCityNo()).getName());
            }
            if (StringUtil.isNotBlank(vo.getProvinceNo())) {
                vo.setProvinceName(regionDao.findById(vo.getProvinceNo()).getName());
            }
        }
        return rpcStudentVo;
    }
    
    @Override
    public void setBossMobileNo(String studentId, String bossMobileStudentNo, String phone) {
        Student student = studentDao.fiindStudentByIdAndParentPhoneNumber(studentId, phone);
        if (student != null) {
            student.setRelatedStudentNo(bossMobileStudentNo);
            studentDao.merge(student);
        }
    }

    /**
     * 保存学生
     *
     * @param student
     * @param parentPhoneNumber
     * @return
     */
    @Override
    public String saveStudent(org.boss.rpc.eduboss.service.dto.StudentRpcVo student, String parentPhoneNumber) {
        /*if (student.getStudentID() == null || StringUtil.isBlank(student.getStudentID())){
            student.setStudentID(null);
            if (parentPhoneNumber == null || StringUtil.isBlank(parentPhoneNumber)){
                throw new ApplicationException("新增学生必须有父母手机号");
            }
        }
        if (student.getStudentID()!=null){
            com.eduboss.domain.Student stuInDb = studentDao.findById(student.getStudentID());
            if (stuInDb!=null){
                stuInDb.setName(student.getName());
                stuInDb.setSex(student.getGender());
                if (student.getProvince()!=null){
                    String provinceId = student.getProvince().getId();
                    Region province = regionDao.findById(provinceId);
                    if (province!=null){
                        stuInDb.setProvice(province.getId());
                    }
                }else {
                    stuInDb.setProvice(null);
                }
                if (student.getCity()!=null){
                    String cityId = student.getCity().getId();
                    Region city = regionDao.findById(cityId);
                    if (city!=null){
                        stuInDb.setCity(city.getId());
                    }
                }else {
                    stuInDb.setCity(null);
                }
                if (student.getSchool()!=null){
                    String schoolId = student.getSchool().getId();
                    StudentSchool studentSchool = studentSchoolDao.findById(schoolId);
                    if (studentSchool!=null){
                        stuInDb.setSchool(studentSchool);
                        stuInDb.setSchoolOrTemp("school");
                    }
                }else {
                    stuInDb.setSchool(null);
                    stuInDb.setSchoolOrTemp("school");
                }
                if (student.getGrade()!=null){
                    String gradeId = student.getGrade().getId();
                    DataDict grade = dataDictDao.findById(gradeId);
                    if (grade!=null){
                        stuInDb.setGradeDict(grade);
                    }
                }else {
                    stuInDb.setGradeDict(null);
                }
                stuInDb.setModifyTime(DateTools.getCurrentDateTime());
                studentDao.save(stuInDb);
                return stuInDb.getId();
            }else {
                throw new ApplicationException("不存该studentID的学生");
            }
        }else {
            com.eduboss.domain.Student studentNew = new com.eduboss.domain.Student();
            studentNew.setName(student.getName());
            studentNew.setSex(student.getGender());
            if (student.getProvince()!=null){
                studentNew.setProvice(student.getProvince().getId());
            }
            if (student.getCity()!=null){
                studentNew.setCity(student.getCity().getId());
            }
            if (student.getSchool()!=null){
                StudentSchool studentSchool = studentSchoolDao.findById(student.getSchool().getId());
                studentNew.setSchool(studentSchool);
            }
            if (student.getGrade()!=null){
                DataDict grade = dataDictDao.findById(student.getGrade().getId());
                studentNew.setGradeDict(grade);
            }
            studentNew.setFatherPhone(parentPhoneNumber);
            studentNew.setSchoolOrTemp("school");
            studentNew.setCreateTime(DateTools.getCurrentDateTime());
            studentDao.save(studentNew);
            studentDao.commit();
            return studentNew.getId();
        }*/
        return null;
    }


    /**
     * 根据父母手机号，查询关联的学生列表
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public List<com.eduboss.rpc.dto.Student> listStudentByParentPhoneNumberInner(String phoneNumber) {
        List<Student> students = studentDao.listStudentByParentPhoneNumber(phoneNumber);
        List<com.eduboss.rpc.dto.Student> rpcStudentVo = HibernateUtils.voListMapping(students, com.eduboss.rpc.dto.Student.class, "rpcStudentInnerVo");
        return rpcStudentVo;
    }

    /**
     * 保存学生
     *
     * @param student
     * @param parentPhoneNumber
     * @return
     */
    @Override
    public String saveStudentInner(com.eduboss.rpc.dto.Student student, String parentPhoneNumber) {
        if (student.getStudentID() == null || StringUtil.isBlank(student.getStudentID())){
            student.setStudentID(null);
            if (parentPhoneNumber == null || StringUtil.isBlank(parentPhoneNumber)){
                throw new ApplicationException("新增学生必须有父母手机号");
            }
        }
        if (student.getStudentID()!=null){
            com.eduboss.domain.Student stuInDb = studentDao.findById(student.getStudentID());
            if (stuInDb!=null){
                stuInDb.setName(student.getName());
                stuInDb.setSex(student.getGender());
                if (student.getProvince()!=null){
                    String provinceId = student.getProvince().getId();
                    Region province = regionDao.findById(provinceId);
                    if (province!=null){
                        stuInDb.setProvice(province.getId());
                    }
                }else {
                    stuInDb.setProvice(null);
                }
                if (student.getCity()!=null){
                    String cityId = student.getCity().getId();
                    Region city = regionDao.findById(cityId);
                    if (city!=null){
                        stuInDb.setCity(city.getId());
                    }
                }else {
                    stuInDb.setCity(null);
                }
                if (student.getSchool()!=null){
                    String schoolId = student.getSchool().getId();
                    StudentSchool studentSchool = studentSchoolDao.findById(schoolId);
                    if (studentSchool!=null){
                        stuInDb.setSchool(studentSchool);
                        stuInDb.setSchoolOrTemp("school");
                    }
                }else {
                    stuInDb.setSchool(null);
                    stuInDb.setSchoolOrTemp("school");
                }
                if (student.getGrade()!=null){
                    String gradeId = student.getGrade().getId();
                    DataDict grade = dataDictDao.findById(gradeId);
                    if (grade!=null){
                        stuInDb.setGradeDict(grade);
                    }
                }else {
                    stuInDb.setGradeDict(null);
                }
                stuInDb.setModifyTime(DateTools.getCurrentDateTime());
                studentDao.save(stuInDb);
                return stuInDb.getId();
            }else {
                throw new ApplicationException("不存该studentID的学生");
            }
        }else {
            com.eduboss.domain.Student studentNew = new com.eduboss.domain.Student();
            studentNew.setName(student.getName());
            studentNew.setSex(student.getGender());
            if (student.getProvince()!=null){
                studentNew.setProvice(student.getProvince().getId());
            }
            if (student.getCity()!=null){
                studentNew.setCity(student.getCity().getId());
            }
            if (student.getSchool()!=null){
                StudentSchool studentSchool = studentSchoolDao.findById(student.getSchool().getId());
                studentNew.setSchool(studentSchool);
            }
            if (student.getGrade()!=null){
                DataDict grade = dataDictDao.findById(student.getGrade().getId());
                studentNew.setGradeDict(grade);
            }
            studentNew.setFatherPhone(parentPhoneNumber);
            studentNew.setSchoolOrTemp("school");
            studentNew.setCreateTime(DateTools.getCurrentDateTime());
            studentDao.save(studentNew);
            studentDao.commit();
            return studentNew.getId();
        }
    }
}
