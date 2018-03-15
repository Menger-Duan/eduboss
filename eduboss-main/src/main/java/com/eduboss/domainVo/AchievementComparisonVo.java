package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 成绩对比记录vo
 * @author arvin
 *
 */
public class AchievementComparisonVo implements Serializable {

    private static final long serialVersionUID = 6373320929028771962L;

    private Integer id;
    
    private String studentId;
    
    private String schoolYearId;
    
    private String schoolYearName;
    
    private String semesterValue;
    
    private String semesterName;
    
    private String subjectIds;
    
    private String subjectNames;
    
    private Integer comparativeAchievementId;
    
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
     * 学年名称
     * @return
     */
    public String getSchoolYearName() {
        return schoolYearName;
    }

    public void setSchoolYearName(String schoolYearName) {
        this.schoolYearName = schoolYearName;
    }

    /**
     * 学期值
     * @return
     */
    public String getSemesterValue() {
        return semesterValue;
    }

    public void setSemesterValue(String semesterValue) {
        this.semesterValue = semesterValue;
    }

    /**
     * 学期名称
     * @return
     */
    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    /**
     * 在读一对一科目编号s
     * @return
     */
    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    /**
     * 在读一对一科目名称s
     * @return
     */
    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    /**
     * 对比学生成绩编号
     * @return
     */
    public Integer getComparativeAchievementId() {
        return comparativeAchievementId;
    }

    public void setComparativeAchievementId(Integer comparativeAchievementId) {
        this.comparativeAchievementId = comparativeAchievementId;
    }

    /**
     * 版本
     * @return
     */
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "AchievementComparisonVo [id=" + id + ", studentId=" + studentId
                + ", schoolYearId=" + schoolYearId + ", schoolYearName="
                + schoolYearName + ", semesterValue=" + semesterValue
                + ", semesterName=" + semesterName + ", subjectIds="
                + subjectIds + ", subjectNames=" + subjectNames
                + ", comparativeAchievementId=" + comparativeAchievementId
                + ", version=" + version + "]";
    }

}
