package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.eduboss.common.AchievementType;
import com.eduboss.common.BenchmarkType;
import com.eduboss.common.StandardCategory;

/**
 * 成绩基准科目
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_benchmark")
public class AchievementBenchmark extends BaseVersionDomain {

    private static final long serialVersionUID = 7633634187881400468L;

    private Integer id;
    
    private AchievementComparison achievementComparison;
    
    private StudentAchievement studentAchievement;
    
    private StudentAchievementSubject benchmarkSubject;
    
    private AchievementType benchmarkAchievementType;
    
    private AchievementCategory benchmarkCategory;
    
    private StudentAchievementSubject compareSubject;
    
    private AchievementType compareAchievementType;
    
    private AchievementCategory compareCategory;
    
    private StandardCategory standardCategory;
    
    private BenchmarkType benchmarkType;
    
    private String readOnlyDesc;
    
    public AchievementBenchmark() {
        super();
    }

    public AchievementBenchmark(Integer id,
            AchievementComparison achievementComparison,
            StudentAchievement studentAchievement,
            StudentAchievementSubject benchmarkSubject,
            AchievementType benchmarkAchievementType,
            AchievementCategory benchmarkCategory,
            StudentAchievementSubject compareSubject,
            AchievementType compareAchievementType,
            AchievementCategory compareCategory,
            StandardCategory standardCategory, BenchmarkType benchmarkType,
            String readOnlyDesc) {
        super();
        this.id = id;
        this.achievementComparison = achievementComparison;
        this.studentAchievement = studentAchievement;
        this.benchmarkSubject = benchmarkSubject;
        this.benchmarkAchievementType = benchmarkAchievementType;
        this.benchmarkCategory = benchmarkCategory;
        this.compareSubject = compareSubject;
        this.compareAchievementType = compareAchievementType;
        this.compareCategory = compareCategory;
        this.standardCategory = standardCategory;
        this.benchmarkType = benchmarkType;
        this.readOnlyDesc = readOnlyDesc;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable=false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 成绩对比记录编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_comparison_id")
    public AchievementComparison getAchievementComparison() {
        return achievementComparison;
    }

    public void setAchievementComparison(AchievementComparison achievementComparison) {
        this.achievementComparison = achievementComparison;
    }

    /**
     * 学生成绩编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_achievement_id")
    public StudentAchievement getStudentAchievement() {
        return studentAchievement;
    }

    public void setStudentAchievement(StudentAchievement studentAchievement) {
        this.studentAchievement = studentAchievement;
    }

    /**
     * 基准学生成绩科目编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benchmark_subject_id")
    public StudentAchievementSubject getBenchmarkSubject() {
        return benchmarkSubject;
    }

    public void setBenchmarkSubject(StudentAchievementSubject benchmarkSubject) {
        this.benchmarkSubject = benchmarkSubject;
    }

    /**
     * 基准成绩类型
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "benchmark_achievement_type")
    public AchievementType getBenchmarkAchievementType() {
        return benchmarkAchievementType;
    }

    public void setBenchmarkAchievementType(AchievementType benchmarkAchievementType) {
        this.benchmarkAchievementType = benchmarkAchievementType;
    }

    /**
     * 基准成绩类别
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benchmark_category")
    public AchievementCategory getBenchmarkCategory() {
        return benchmarkCategory;
    }

    public void setBenchmarkCategory(AchievementCategory benchmarkCategory) {
        this.benchmarkCategory = benchmarkCategory;
    }

    /**
     * 对比学生成绩科目编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compare_subject_id")
    public StudentAchievementSubject getCompareSubject() {
        return compareSubject;
    }

    public void setCompareSubject(StudentAchievementSubject compareSubject) {
        this.compareSubject = compareSubject;
    }

    /**
     * 对比成绩类型
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "compare_achievement_type")
    public AchievementType getCompareAchievementType() {
        return compareAchievementType;
    }

    public void setCompareAchievementType(AchievementType compareAchievementType) {
        this.compareAchievementType = compareAchievementType;
    }

    /**
     * 对比成绩类别
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compare_category")
    public AchievementCategory getCompareCategory() {
        return compareCategory;
    }

    public void setCompareCategory(AchievementCategory compareCategory) {
        this.compareCategory = compareCategory;
    }
    
    /**
     * 进步类别
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "standard_category")
    public StandardCategory getStandardCategory() {
        return standardCategory;
    }

    public void setStandardCategory(StandardCategory standardCategory) {
        this.standardCategory = standardCategory;
    }

    /**
     * 基准判定类型
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "benchmark_type")
    public BenchmarkType getBenchmarkType() {
        return benchmarkType;
    }

    public void setBenchmarkType(BenchmarkType benchmarkType) {
        this.benchmarkType = benchmarkType;
    }

    /**
     * 仅作记录描述
     * @return
     */
    @Column(name = "read_only_desc")
    public String getReadOnlyDesc() {
        return readOnlyDesc;
    }

    public void setReadOnlyDesc(String readOnlyDesc) {
        this.readOnlyDesc = readOnlyDesc;
    }

    @Override
    public String toString() {
        return "AchievementBenchmark [id=" + id + ", achievementComparison="
                + achievementComparison + ", studentAchievement="
                + studentAchievement + ", benchmarkSubject=" + benchmarkSubject
                + ", benchmarkAchievementType=" + benchmarkAchievementType
                + ", benchmarkCategory=" + benchmarkCategory
                + ", compareSubject=" + compareSubject
                + ", compareAchievementType=" + compareAchievementType
                + ", compareCategory=" + compareCategory
                + ", standardCategory=" + standardCategory + ", benchmarkType="
                + benchmarkType + ", readOnlyDesc=" + readOnlyDesc + super.toString() + "]";
    }

}
