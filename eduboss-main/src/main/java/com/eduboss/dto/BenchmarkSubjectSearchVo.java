package com.eduboss.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.Semester;

/**
 * 基准成绩科目查询vo
 * @author arvin
 *
 */
public class BenchmarkSubjectSearchVo implements Serializable {

    private static final long serialVersionUID = 4427828210972787284L;

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
    @NotBlank(message="学年编号不能为空")
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
    @NotNull(message="学期不能为空")
    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "BenchmarkSubjectSearchVo [studentId=" + studentId
                + ", schoolYearId=" + schoolYearId + ", semester=" + semester
                + "]";
    }
    
}
