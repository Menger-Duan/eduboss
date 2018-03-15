package com.eduboss.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.Semester;

/**
 * 学生成绩查询vo
 * @author arvin
 *
 */
public class StudentAchievementSearchVo implements Serializable {

    private static final long serialVersionUID = 199738391807475169L;

    private String studentId;
    
    private String schoolYearId;
    
    private Semester semester;
    
    private String examinationTypeId;
    
    private String examinationDate;

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

    /**
     * 考试类型编号
     * @return
     */
    public String getExaminationTypeId() {
        return examinationTypeId;
    }

    public void setExaminationTypeId(String examinationTypeId) {
        this.examinationTypeId = examinationTypeId;
    }

    /**
     * 考试时间
     * @return
     */
    public String getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(String examinationDate) {
        this.examinationDate = examinationDate;
    }

    @Override
    public String toString() {
        return "StudentAchievementSearchVo [studentId=" + studentId
                + ", schoolYearId=" + schoolYearId + ", semester=" + semester
                + ", examinationTypeId=" + examinationTypeId
                + ", examinationDate=" + examinationDate + "]";
    }

}
