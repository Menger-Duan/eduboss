package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 成绩基准科目vo
 * @author arvin
 *
 */
public class AchievementBenchmarkVo implements Serializable {

    private static final long serialVersionUID = -230763221503595833L;

    private Integer id;
    
    private Integer studentAchievementId;
    
    private String studentAchievementName;
    
    private String examinationDate;
    
    private String subjectId;
    
    private String subjectName;
    
    private String achievementTypeValue;
    
    private String achievementTypeName;
    
    private String value;
    
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
     * 基准成绩编号
     * @return
     */
    public Integer getStudentAchievementId() {
        return studentAchievementId;
    }

    public void setStudentAchievementId(Integer studentAchievementId) {
        this.studentAchievementId = studentAchievementId;
    }

    /**
     * 基准成绩名称
     * @return
     */
    public String getStudentAchievementName() {
        return studentAchievementName;
    }

    public void setStudentAchievementName(String studentAchievementName) {
        this.studentAchievementName = studentAchievementName;
    }

    /**
     * 基准成绩考试时间
     * @return
     */
    public String getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(String examinationDate) {
        this.examinationDate = examinationDate;
    }

    /**
     * 科目编号
     * @return
     */
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * 科目名称
     * @return
     */
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**
     * 成绩类型值
     * @return
     */
    public String getAchievementTypeValue() {
        return achievementTypeValue;
    }

    public void setAchievementTypeValue(String achievementTypeValue) {
        this.achievementTypeValue = achievementTypeValue;
    }

    /**
     * 成绩类型名称
     * @return
     */
    public String getAchievementTypeName() {
        return achievementTypeName;
    }

    public void setAchievementTypeName(String achievementTypeName) {
        this.achievementTypeName = achievementTypeName;
    }

    /**
     * 成绩
     * @return
     */
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "AchievementBenchmarkVo [id=" + id + ", studentAchievementId="
                + studentAchievementId + ", studentAchievementName="
                + studentAchievementName + ", subjectId=" + subjectId
                + ", subjectName=" + subjectName + ", achievementTypeValue="
                + achievementTypeValue + ", achievementTypeName="
                + achievementTypeName + ", value=" + value + "]";
    }

}
