package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.eduboss.common.Semester;

/**
 * 成绩对比记录
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_comparison")
public class AchievementComparison extends BaseVersionDomain {

    private static final long serialVersionUID = 1067070282668015917L;

    private Integer id;
    
    private Student student;
    
    private DataDict schoolYear;
    
    private Semester semester;
    
    private String subjectIds;
    
    private String subjectNames;
    
    private StudentAchievement comparativeAchievement;
    
    private Integer isDeleted; 
    
    private Set<AchievementBenchmark> benchmarks;

    public AchievementComparison() {
        super();
    }

    public AchievementComparison(Integer id, Student student,
            DataDict schoolYear, Semester semester, String subjectIds,
            String subjectNames, StudentAchievement comparativeAchievement,
            Integer isDeleted, Set<AchievementBenchmark> benchmarks) {
        super();
        this.id = id;
        this.student = student;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.subjectIds = subjectIds;
        this.subjectNames = subjectNames;
        this.comparativeAchievement = comparativeAchievement;
        this.isDeleted = isDeleted;
        this.benchmarks = benchmarks;
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
     * 学生
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * 学年
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_year")
    public DataDict getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(DataDict schoolYear) {
        this.schoolYear = schoolYear;
    }

    /**
     * 学期
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "semester")
    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    /**
     * 在读一对一科目id
     * @return
     */
    @Column(name = "subject_ids")
    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

    /**
     * 在读一对一科目名称
     * @return
     */
    @Column(name = "subject_names")
    public String getSubjectNames() {
        return subjectNames;
    }

    public void setSubjectNames(String subjectNames) {
        this.subjectNames = subjectNames;
    }

    /**
     * 对比成绩
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comparative_achievement_id")
    public StudentAchievement getComparativeAchievement() {
        return comparativeAchievement;
    }

    public void setComparativeAchievement(StudentAchievement comparativeAchievement) {
        this.comparativeAchievement = comparativeAchievement;
    }

    /**
     * 是否删除1:否，0：是
     * @return
     */
    @Column(name = "is_deleted")
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 成绩基准科目set
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "achievementComparison")
    @OrderBy("id ASC")
    public Set<AchievementBenchmark> getBenchmarks() {
        return benchmarks;
    }

    public void setBenchmarks(Set<AchievementBenchmark> benchmarks) {
        this.benchmarks = benchmarks;
    }

    @Override
    public String toString() {
        return "AchievementComparison [id=" + id + ", student=" + student
                + ", schoolYear=" + schoolYear + ", semester=" + semester
                + ", subjectIds=" + subjectIds + ", subjectNames="
                + subjectNames + ", comparativeAchievement="
                + comparativeAchievement + ", isDeleted=" + isDeleted
                + ", benchmarks=" + benchmarks + "]";
    }

}
