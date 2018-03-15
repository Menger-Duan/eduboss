package com.eduboss.domainVo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 学生成绩vo
 * @author arvin
 *
 */
public class StudentAchievementVo implements Serializable {

    private static final long serialVersionUID = 8052651855018029489L;

    private Integer id;
    
    private String name;
    
    private String studentId;
    
    private Integer achievementTemplateId;
    
    private String schoolYearId;
    
    private String schoolYearName;
    
    private String semesterName;
    
    private String semesterValue;
    
    private String examinationTypeId;
    
    private String examinationTypeName;
    
    private String cityId;
    
    private String cityName;
    
    private String gradeId;
    
    private String gradeName;
    
    private String examinationDate;
    
    private String achievementTypeName;
    
    private String achievementTypeValue;
    
    private String achievementEvidence;
    
    private BigDecimal totalScore;
    
    private Integer totalClassRanking;
    
    private Integer totalGradeRanking;
    
    private String totalEvaluationTypeName;
    
    private String totalEvaluationTypeValue;
    
    private Integer subjectNum;
    
    private Integer templateSubjectNum;
    
    private String statisticalRate;
    
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 名称
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
     * 成绩模板编号
     * @return
     */
    public Integer getAchievementTemplateId() {
        return achievementTemplateId;
    }

    public void setAchievementTemplateId(Integer achievementTemplateId) {
        this.achievementTemplateId = achievementTemplateId;
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
     * 考试类型名称
     * @return
     */
    public String getExaminationTypeName() {
        return examinationTypeName;
    }

    public void setExaminationTypeName(String examinationTypeName) {
        this.examinationTypeName = examinationTypeName;
    }

    /**
     * 城市编号
     * @return
     */
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * 城市名称
     * @return
     */
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * 年级编号
     * @return
     */
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    /**
     * 年级名称
     * @return
     */
    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
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
     * 成绩凭证
     * @return
     */
    public String getAchievementEvidence() {
        return achievementEvidence;
    }

    public void setAchievementEvidence(String achievementEvidence) {
        this.achievementEvidence = achievementEvidence;
    }

    /**
     * 总分
     * @return
     */
    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 总分班级排名
     * @return
     */
    public Integer getTotalClassRanking() {
        return totalClassRanking;
    }

    public void setTotalClassRanking(Integer totalClassRanking) {
        this.totalClassRanking = totalClassRanking;
    }

    /**
     * 总分年级排名
     * @return
     */
    public Integer getTotalGradeRanking() {
        return totalGradeRanking;
    }

    public void setTotalGradeRanking(Integer totalGradeRanking) {
        this.totalGradeRanking = totalGradeRanking;
    }

    /**
     * 总分评级名称
     * @return
     */
    public String getTotalEvaluationTypeName() {
        return totalEvaluationTypeName;
    }

    public void setTotalEvaluationTypeName(String totalEvaluationTypeName) {
        this.totalEvaluationTypeName = totalEvaluationTypeName;
    }

    /**
     * 总分评级值
     * @return
     */
    public String getTotalEvaluationTypeValue() {
        return totalEvaluationTypeValue;
    }

    public void setTotalEvaluationTypeValue(String totalEvaluationTypeValue) {
        this.totalEvaluationTypeValue = totalEvaluationTypeValue;
    }
    
    /**
     * 科目数量
     * @return
     */
    public Integer getSubjectNum() {
        return subjectNum;
    }

    public void setSubjectNum(Integer subjectNum) {
        this.subjectNum = subjectNum;
    }

    /**
     * 模板科目数量
     * @return
     */
    public Integer getTemplateSubjectNum() {
        return templateSubjectNum;
    }

    public void setTemplateSubjectNum(Integer templateSubjectNum) {
        this.templateSubjectNum = templateSubjectNum;
    }

    /**
     * 科目统计率
     * @return
     */
    public String getStatisticalRate() {
        return statisticalRate;
    }

    public void setStatisticalRate(String statisticalRate) {
        this.statisticalRate = statisticalRate;
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
        return "StudentAchievementVo [id=" + id + ", name=" + name
                + ", studentId=" + studentId + ", achievementTemplateId="
                + achievementTemplateId + ", schoolYearId=" + schoolYearId
                + ", schoolYearName=" + schoolYearName + ", semesterName="
                + semesterName + ", semesterValue=" + semesterValue
                + ", examinationTypeId=" + examinationTypeId
                + ", examinationTypeName=" + examinationTypeName + ", cityId="
                + cityId + ", cityName=" + cityName + ", gradeId=" + gradeId
                + ", gradeName=" + gradeName + ", examinationDate="
                + examinationDate + ", achievementTypeName="
                + achievementTypeName + ", achievementTypeValue="
                + achievementTypeValue + ", achievementEvidence="
                + achievementEvidence + ", totalScore=" + totalScore
                + ", totalClassRanking=" + totalClassRanking
                + ", totalGradeRanking=" + totalGradeRanking
                + ", totalEvaluationTypeName=" + totalEvaluationTypeName
                + ", totalEvaluationTypeValue=" + totalEvaluationTypeValue
                + ", subjectNum=" + subjectNum + ", templateSubjectNum="
                + templateSubjectNum + ", statisticalRate=" + statisticalRate
                + ", version=" + version + "]";
    }

}
