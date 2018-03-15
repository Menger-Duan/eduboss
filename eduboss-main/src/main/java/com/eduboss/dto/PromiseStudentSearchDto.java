package com.eduboss.dto;

import com.eduboss.common.PromiseAuditStatus;
import com.eduboss.common.StudentPromiseStatus;

public class PromiseStudentSearchDto {

    /**
     * 上课状态
     * */
    private String[] courseStatus;  //0:已完结；1：进行中；

    /**
     * 完结状态
     * */
    private String resultStatus;  // 1:成功；0:失败；

    private String plSchoolId;

        private String pName;

    private String studentName;

    private String year;

    private String brenchId;

    private String campusId;

    private String gradeId;

    private PromiseAuditStatus auditStatus;

    private String abortClass;

    private String productName;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String[] getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String[] courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getPlSchoolId() {
        return plSchoolId;
    }

    public void setPlSchoolId(String plSchoolId) {
        this.plSchoolId = plSchoolId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBrenchId() {
        return brenchId;
    }

    public void setBrenchId(String brenchId) {
        this.brenchId = brenchId;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getAbortClass() {
        return abortClass;
    }

    public void setAbortClass(String abortClass) {
        this.abortClass = abortClass;
    }

    public PromiseAuditStatus getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(PromiseAuditStatus auditStatus) {
        this.auditStatus = auditStatus;
    }
}
