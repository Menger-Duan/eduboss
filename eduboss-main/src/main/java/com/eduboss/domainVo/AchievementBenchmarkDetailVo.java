package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 成绩基准科目详情
 * @author arvin
 *
 */
public class AchievementBenchmarkDetailVo implements Serializable {

    private static final long serialVersionUID = 4069863682549596541L;

    private Integer id;
    
    private Integer benchmarkAchievementId;
    
    private Integer benchmarkSubjectId;
    
    private String subjectId;
    
    private String subjectName;
    
    private String benchmarkSubjectName;
    
    private String benchmarkAchievementTypeName;
    
    private String benchmarkAchievementTypeValue;
    
    private String benchmarkCategoryName;
    
    private String benchmarkValue;
    
    private Integer benchmarkTotalScore; 
    
    private String benckmarkExaminationDate;
    
    private String compareSubjectName;
    
    private String compareAchievementTypeName;
    
    private String compareCategoryName;
    
    private String compareValue;
    
    private Integer compareTotalScore; 
    
    private String compareExaminationDate;
    
    private String standardCategoryValue;
    
    private String standardCategoryName;
    
    private String benchmarkTypeName;
    
    private String readOnlyDesc;
    
    private String modifyUserName;
    
    private Integer version;

    /**
     * 成绩基准科目编号
     * @return
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 基准学生成绩编号
     * @return
     */
    public Integer getBenchmarkAchievementId() {
        return benchmarkAchievementId;
    }

    public void setBenchmarkAchievementId(Integer benchmarkAchievementId) {
        this.benchmarkAchievementId = benchmarkAchievementId;
    }

    /**
     * 基准成绩科目编号
     * @return
     */
    public Integer getBenchmarkSubjectId() {
        return benchmarkSubjectId;
    }

    public void setBenchmarkSubjectId(Integer benchmarkSubjectId) {
        this.benchmarkSubjectId = benchmarkSubjectId;
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
     * 基准科目名称
     * @return
     */
    public String getBenchmarkSubjectName() {
        return benchmarkSubjectName;
    }

    public void setBenchmarkSubjectName(String benchmarkSubjectName) {
        this.benchmarkSubjectName = benchmarkSubjectName;
    }

    /**
     * 基准成绩类型值
     * @return
     */
    public String getBenchmarkAchievementTypeValue() {
        return benchmarkAchievementTypeValue;
    }

    public void setBenchmarkAchievementTypeValue(
            String benchmarkAchievementTypeValue) {
        this.benchmarkAchievementTypeValue = benchmarkAchievementTypeValue;
    }

    /**
     * 基准成绩类型名称
     * @return
     */
    public String getBenchmarkAchievementTypeName() {
        return benchmarkAchievementTypeName;
    }

    public void setBenchmarkAchievementTypeName(String benchmarkAchievementTypeName) {
        this.benchmarkAchievementTypeName = benchmarkAchievementTypeName;
    }

    /**
     * 基准成绩类别名称
     * @return
     */
    public String getBenchmarkCategoryName() {
        return benchmarkCategoryName;
    }

    public void setBenchmarkCategoryName(String benchmarkCategoryName) {
        this.benchmarkCategoryName = benchmarkCategoryName;
    }

    /**
     * 基准成绩值
     * @return
     */
    public String getBenchmarkValue() {
        return benchmarkValue;
    }

    public void setBenchmarkValue(String benchmarkValue) {
        this.benchmarkValue = benchmarkValue;
    }

    /**
     * 基准成绩总分
     * @return
     */
    public Integer getBenchmarkTotalScore() {
        return benchmarkTotalScore;
    }

    public void setBenchmarkTotalScore(Integer benchmarkTotalScore) {
        this.benchmarkTotalScore = benchmarkTotalScore;
    }

    /**
     * 基准考试时间
     * @return
     */
    public String getBenckmarkExaminationDate() {
        return benckmarkExaminationDate;
    }

    public void setBenckmarkExaminationDate(String benckmarkExaminationDate) {
        this.benckmarkExaminationDate = benckmarkExaminationDate;
    }

    /**
     * 对比科目名称
     * @return
     */
    public String getCompareSubjectName() {
        return compareSubjectName;
    }

    public void setCompareSubjectName(String compareSubjectName) {
        this.compareSubjectName = compareSubjectName;
    }

    /**
     * 对比成绩类型名称
     * @return
     */
    public String getCompareAchievementTypeName() {
        return compareAchievementTypeName;
    }

    public void setCompareAchievementTypeName(String compareAchievementTypeName) {
        this.compareAchievementTypeName = compareAchievementTypeName;
    }

    /**
     * 对比成绩类别名称
     * @return
     */
    public String getCompareCategoryName() {
        return compareCategoryName;
    }

    public void setCompareCategoryName(String compareCategoryName) {
        this.compareCategoryName = compareCategoryName;
    }

    /**
     * 对比科目值
     * @return
     */
    public String getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(String compareValue) {
        this.compareValue = compareValue;
    }

    /**
     * 对比科目总分
     * @return
     */
    public Integer getCompareTotalScore() {
        return compareTotalScore;
    }

    public void setCompareTotalScore(Integer compareTotalScore) {
        this.compareTotalScore = compareTotalScore;
    }

    /**
     * 对比考试时间
     * @return
     */
    public String getCompareExaminationDate() {
        return compareExaminationDate;
    }

    public void setCompareExaminationDate(String compareExaminationDate) {
        this.compareExaminationDate = compareExaminationDate;
    }

    /**
     * 进步类别值
     * @return
     */
    public String getStandardCategoryValue() {
        return standardCategoryValue;
    }

    public void setStandardCategoryValue(String standardCategoryValue) {
        this.standardCategoryValue = standardCategoryValue;
    }

    /**
     * 进步类别名称
     * @return
     */
    public String getStandardCategoryName() {
        return standardCategoryName;
    }

    public void setStandardCategoryName(String standardCategoryName) {
        this.standardCategoryName = standardCategoryName;
    }

    /**
     * 备注
     * @return
     */
    public String getBenchmarkTypeName() {
        return benchmarkTypeName;
    }

    public void setBenchmarkTypeName(String benchmarkTypeName) {
        this.benchmarkTypeName = benchmarkTypeName;
    }

    /**
     * 仅作记录描述
     * @return
     */
    public String getReadOnlyDesc() {
        return readOnlyDesc;
    }

    public void setReadOnlyDesc(String readOnlyDesc) {
        this.readOnlyDesc = readOnlyDesc;
    }

    /**
     * 修改用户名称
     * @return
     */
    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
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
        return "AchievementBenchmarkDetailVo [id=" + id
                + ", benchmarkAchievementId=" + benchmarkAchievementId
                + ", benchmarkSubjectId=" + benchmarkSubjectId + ", subjectId="
                + subjectId + ", subjectName=" + subjectName
                + ", benchmarkSubjectName=" + benchmarkSubjectName
                + ", benchmarkAchievementTypeName="
                + benchmarkAchievementTypeName
                + ", benchmarkAchievementTypeValue="
                + benchmarkAchievementTypeValue + ", benchmarkCategoryName="
                + benchmarkCategoryName + ", benchmarkValue=" + benchmarkValue
                + ", benchmarkTotalScore=" + benchmarkTotalScore
                + ", benckmarkExaminationDate=" + benckmarkExaminationDate
                + ", compareSubjectName=" + compareSubjectName
                + ", compareAchievementTypeName=" + compareAchievementTypeName
                + ", compareCategoryName=" + compareCategoryName
                + ", compareValue=" + compareValue + ", compareTotalScore="
                + compareTotalScore + ", compareExaminationDate="
                + compareExaminationDate + ", standardCategoryValue="
                + standardCategoryValue + ", standardCategoryName="
                + standardCategoryName + ", benchmarkTypeName="
                + benchmarkTypeName + ", readOnlyDesc=" + readOnlyDesc
                + ", modifyUserName=" + modifyUserName + ", version=" + version
                + "]";
    }

}
