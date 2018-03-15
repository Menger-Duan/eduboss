package com.eduboss.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.Semester;

/**
 * 成绩对比记录编辑Vo
 * @author arvin
 *
 */
public class AchievementComparisonRequestVo implements Serializable {

    private static final long serialVersionUID = 6909884480478614250L;

    private Integer id;
    
    private String studentId;
    
    private String schoolYearId;
    
    private Semester semester;
    
    private String subjectIds;
    
    private String subjectNames;
    
    private Integer comparativeAchievementId;
    
    private List<AchievementBenchmarkRequestVo> benchmarks;
    
    private Integer version;

    /**
     * 编号
     * @return
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    /**
     * 一对一在读科目编号s
     * @return
     */
    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    /**
     * 一对一在读科目名称s
     * @return
     */
    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    /**
     * 对比成绩编号
     * @return
     */
    @NotNull(message="对比成绩编号不能为空")
    public Integer getComparativeAchievementId() {
        return comparativeAchievementId;
    }

    public void setComparativeAchievementId(Integer comparativeAchievementId) {
        this.comparativeAchievementId = comparativeAchievementId;
    }

    /**
     * 成绩基准科目set
     * @return
     */
    @NotNull(message="成绩基准科目set不能为空")
    public List<AchievementBenchmarkRequestVo> getBenchmarks() {
        return benchmarks;
    }

    public void setBenchmarks(List<AchievementBenchmarkRequestVo> benchmarks) {
        this.benchmarks = benchmarks;
    }

    /**
     * 版本
     * @return
     */
    @NotNull(message="版本不能为空")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AchievementComparisonRequestVo [id=" + id + ", studentId="
                + studentId + ", schoolYearId=" + schoolYearId + ", semester="
                + semester + ", subjectIds=" + subjectIds + ", subjectNames="
                + subjectNames + ", comparativeAchievementId="
                + comparativeAchievementId + ", benchmarks=" + benchmarks
                + ", version=" + version + "]";
    }

}
