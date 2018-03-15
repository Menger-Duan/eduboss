package com.eduboss.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.Semester;

/**
 * 成绩对比查询vo
 * @author arvin
 *
 */
public class AchievementComparisonSearchVo implements Serializable {

    private static final long serialVersionUID = -387420988133456425L;

    private String studentId;
    
    private String schoolYearId;
    
    private Semester semester;

    /**
     * 学生编号
     * @return
     */
    @NotBlank(message="学生编号不能为空")
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * 学年编号
     * @return
     */
    public String getSchoolYearId() {
        return schoolYearId;
    }

    public void setSchoolYearId(String schoolYearId) {
        this.schoolYearId = schoolYearId;
    }

    /**
     * 学期
     * @return
     */
    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "AchievementComparisonSearchVo [studentId=" + studentId
                + ", schoolYearId=" + schoolYearId + ", semester=" + semester
                + "]";
    }
    
}
