package com.eduboss.domainVo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class TwoTeacherCourseSearchVo implements Serializable {

    private static final long serialVersionUID = -2505466961786059836L;

    private String studentId;
    
    private String teacherId;
    
    private String startDate;
    
    private String endDate;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @NotBlank
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @NotBlank
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "TwoTeacherCourseSearchVo [studentId=" + studentId
                + ", teacherId=" + teacherId + ", startDate=" + startDate
                + ", endDate=" + endDate + "]";
    }
    
}
