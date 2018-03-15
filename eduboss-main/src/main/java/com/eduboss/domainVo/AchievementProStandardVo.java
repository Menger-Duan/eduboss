package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 成绩进步标准vo
 * @author arvin
 *
 */
public class AchievementProStandardVo implements Serializable {

    private static final long serialVersionUID = 8742623742371802409L;

    private Integer id;
    
    private String name;
    
    private String standardCategoryName;
    
    private String standardCategoryValue;
    
    private String greaterTypeName;
    
    private String greaterTypeValue;
    
    private Integer scoreBetweenStart;
    
    private String lessTypeName;
    
    private String lessTypeValue;
    
    private Integer scoreBetweenEnd;
    
    private Integer classRankingBtwStart;
    
    private Integer classRankingBtwEnd;
    
    private Integer gradeRankingBtwStart;
    
    private Integer gradeRankingBtwEnd;
    
    private String evaluationList;
    
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
     * 大于类型名称
     * @return
     */
    public String getGreaterTypeName() {
        return greaterTypeName;
    }

    public void setGreaterTypeName(String greaterTypeName) {
        this.greaterTypeName = greaterTypeName;
    }

    /**
     * 大于类型值
     * @return
     */
    public String getGreaterTypeValue() {
        return greaterTypeValue;
    }

    public void setGreaterTypeValue(String greaterTypeValue) {
        this.greaterTypeValue = greaterTypeValue;
    }

    /**
     * 大于分数
     * @return
     */
    public Integer getScoreBetweenStart() {
        return scoreBetweenStart;
    }

    public void setScoreBetweenStart(Integer scoreBetweenStart) {
        this.scoreBetweenStart = scoreBetweenStart;
    }

    /**
     * 小于类型名称
     * @return
     */
    public String getLessTypeName() {
        return lessTypeName;
    }

    public void setLessTypeName(String lessTypeName) {
        this.lessTypeName = lessTypeName;
    }

    /**
     * 小于类型值
     * @return
     */
    public String getLessTypeValue() {
        return lessTypeValue;
    }

    public void setLessTypeValue(String lessTypeValue) {
        this.lessTypeValue = lessTypeValue;
    }

    /**
     * 小于分数
     * @return
     */
    public Integer getScoreBetweenEnd() {
        return scoreBetweenEnd;
    }

    public void setScoreBetweenEnd(Integer scoreBetweenEnd) {
        this.scoreBetweenEnd = scoreBetweenEnd;
    }

    /**
     * 大于等于班级排名
     * @return
     */
    public Integer getClassRankingBtwStart() {
        return classRankingBtwStart;
    }

    public void setClassRankingBtwStart(Integer classRankingBtwStart) {
        this.classRankingBtwStart = classRankingBtwStart;
    }

    /**
     * 小于等于班级排名
     * @return
     */
    public Integer getClassRankingBtwEnd() {
        return classRankingBtwEnd;
    }

    public void setClassRankingBtwEnd(Integer classRankingBtwEnd) {
        this.classRankingBtwEnd = classRankingBtwEnd;
    }

    /**
     * 大于等于年级排名
     * @return
     */
    public Integer getGradeRankingBtwStart() {
        return gradeRankingBtwStart;
    }

    public void setGradeRankingBtwStart(Integer gradeRankingBtwStart) {
        this.gradeRankingBtwStart = gradeRankingBtwStart;
    }

    /**
     * 小于等于年级排名
     * @return
     */
    public Integer getGradeRankingBtwEnd() {
        return gradeRankingBtwEnd;
    }

    public void setGradeRankingBtwEnd(Integer gradeRankingBtwEnd) {
        this.gradeRankingBtwEnd = gradeRankingBtwEnd;
    }

    /**
     * 评价值列表
     * @return
     */
    public String getEvaluationList() {
        return evaluationList;
    }

    public void setEvaluationList(String evaluationList) {
        this.evaluationList = evaluationList;
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

}
