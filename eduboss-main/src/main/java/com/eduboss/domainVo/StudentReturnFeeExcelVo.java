package com.eduboss.domainVo;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/5.
 */
public class StudentReturnFeeExcelVo {
    private String createTime;//
    private String createUser;//
    private String campus;//校区
    private String studentName;//学生
    private String gradeName;
    private String contractId;//合同
    private String contractProductName;//合同产品

    private String miniClassName;//小班名称
    private String miniClassSubject;//小班科目
    private String miniClassType;//班级类型
    private String teacher; // 班级老师
    private String studyManager; ////小班学管

    private BigDecimal returnNormalAmount; //实收退费
    private BigDecimal returnSpecialAmount; //超额退费
    private BigDecimal returnAmount;//退费金额
    private String returnReason;//退费原因

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getContractProductName() {
        return contractProductName;
    }

    public void setContractProductName(String contractProductName) {
        this.contractProductName = contractProductName;
    }

    public String getMiniClassName() {
        return miniClassName;
    }

    public void setMiniClassName(String miniClassName) {
        this.miniClassName = miniClassName;
    }

    public String getMiniClassSubject() {
        return miniClassSubject;
    }

    public void setMiniClassSubject(String miniClassSubject) {
        this.miniClassSubject = miniClassSubject;
    }

    public String getMiniClassType() {
        return miniClassType;
    }

    public void setMiniClassType(String miniClassType) {
        this.miniClassType = miniClassType;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getStudyManager() {
        return studyManager;
    }

    public void setStudyManager(String studyManager) {
        this.studyManager = studyManager;
    }

    public BigDecimal getReturnNormalAmount() {
        return returnNormalAmount;
    }

    public void setReturnNormalAmount(BigDecimal returnNormalAmount) {
        this.returnNormalAmount = returnNormalAmount;
    }

    public BigDecimal getReturnSpecialAmount() {
        return returnSpecialAmount;
    }

    public void setReturnSpecialAmount(BigDecimal returnSpecialAmount) {
        this.returnSpecialAmount = returnSpecialAmount;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
}
