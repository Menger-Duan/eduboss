package com.eduboss.dto;

import com.eduboss.domain.Student;
import com.eduboss.domainVo.StudentVo;

/**
 * Created by Administrator on 2017/6/13.
 */
public class MobileStudentListTo {
    private int pageNo;
    private int pageSize;
    private String rcourseHour ;
    private String rcourseHourEnd ;
    private String brenchId;
    private String stuType;
    private Student student;
    private String stuNameGrade;
    private StudentVo studentVo;

    /****小班****/
    private String gradeId;
    private String blCampusId;
    private String productVersionId;
    private String miniClassGradeId;
    private String teacherId;
    private String studyManegerId;
    private String smallClassStatus;
    private String productQuarterId;
    /****小班****/
    private String yearId;
    private String head_teacherId;
    private String pStatus;
    private String stuNameGradeSchool;
    private String status;
    private String modelName1;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getRcourseHour() {
        return rcourseHour;
    }

    public void setRcourseHour(String rcourseHour) {
        this.rcourseHour = rcourseHour;
    }

    public String getRcourseHourEnd() {
        return rcourseHourEnd;
    }

    public void setRcourseHourEnd(String rcourseHourEnd) {
        this.rcourseHourEnd = rcourseHourEnd;
    }

    public String getBrenchId() {
        return brenchId;
    }

    public void setBrenchId(String brenchId) {
        this.brenchId = brenchId;
    }

    public String getStuType() {
        return stuType;
    }

    public void setStuType(String stuType) {
        this.stuType = stuType;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getStuNameGrade() {
        return stuNameGrade;
    }

    public void setStuNameGrade(String stuNameGrade) {
        this.stuNameGrade = stuNameGrade;
    }


    public StudentVo getStudentVo() {
        return studentVo;
    }

    public void setStudentVo(StudentVo studentVo) {
        this.studentVo = studentVo;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getBlCampusId() {
        return blCampusId;
    }

    public void setBlCampusId(String blCampusId) {
        this.blCampusId = blCampusId;
    }

    public String getProductVersionId() {
        return productVersionId;
    }

    public void setProductVersionId(String productVersionId) {
        this.productVersionId = productVersionId;
    }

    public String getMiniClassGradeId() {
        return miniClassGradeId;
    }

    public void setMiniClassGradeId(String miniClassGradeId) {
        this.miniClassGradeId = miniClassGradeId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getStudyManegerId() {
        return studyManegerId;
    }

    public void setStudyManegerId(String studyManegerId) {
        this.studyManegerId = studyManegerId;
    }

    public String getSmallClassStatus() {
        return smallClassStatus;
    }

    public void setSmallClassStatus(String smallClassStatus) {
        this.smallClassStatus = smallClassStatus;
    }

    public String getProductQuarterId() {
        return productQuarterId;
    }

    public void setProductQuarterId(String productQuarterId) {
        this.productQuarterId = productQuarterId;
    }

    public String getYearId() {
        return yearId;
    }

    public void setYearId(String yearId) {
        this.yearId = yearId;
    }

    public String getHead_teacherId() {
        return head_teacherId;
    }

    public void setHead_teacherId(String head_teacherId) {
        this.head_teacherId = head_teacherId;
    }

    public String getpStatus() {
        return pStatus;
    }

    public void setpStatus(String pStatus) {
        this.pStatus = pStatus;
    }

    public String getStuNameGradeSchool() {
        return stuNameGradeSchool;
    }

    public void setStuNameGradeSchool(String stuNameGradeSchool) {
        this.stuNameGradeSchool = stuNameGradeSchool;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModelName1() {
        return modelName1;
    }

    public void setModelName1(String modelName1) {
        this.modelName1 = modelName1;
    }
}
