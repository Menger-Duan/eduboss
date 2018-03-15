package com.eduboss.domain;

import java.math.BigDecimal;
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

import com.eduboss.common.AchievementAbstractType;
import com.eduboss.common.EvaluationType;
import com.eduboss.common.Semester;

/**
 * 学生成绩
 * @author arvin
 *
 */
@Entity
@Table(name = "student_achievement")
public class StudentAchievement extends BaseVersionDomain {

    private static final long serialVersionUID = -8067382953192594150L;

    private Integer id;
    
    private AchievementTemplate achievementTemplate;
    
    private Student student;
    
    private DataDict schoolYear;
    
    private Semester semester;
    
    private DataDict examinationType;
    
    private Region city;
    
    private DataDict grade;
    
    private String examinationDate;
    
    private AchievementAbstractType achievementType;
    
    private String achievementEvidence;
    
    private BigDecimal totalScore;
    
    private Integer totalClassRanking;
    
    private Integer totalGradeRanking;
    
    private EvaluationType totalEvaluationType;
    
    private Integer isDeleted;
    
    private Integer subjectNum;
    
    private Integer templateSubjectNum;
    
    private Set<StudentAchievementSubject> subjectSet;

    public StudentAchievement() {
        super();
    }

    public StudentAchievement(Integer id) {
        super();
        this.id = id;
    }

    public StudentAchievement(Integer id,
            AchievementTemplate achievementTemplate, Student student,
            DataDict schoolYear, Semester semester, DataDict examinationType,
            Region city, DataDict grade, String examinationDate,
            AchievementAbstractType achievementType,
            String achievementEvidence, BigDecimal totalScore,
            Integer totalClassRanking, Integer totalGradeRanking,
            EvaluationType totalEvaluationType, Integer isDeleted,
            Integer subjectNum, Integer templateSubjectNum,
            Set<StudentAchievementSubject> subjectSet) {
        super();
        this.id = id;
        this.achievementTemplate = achievementTemplate;
        this.student = student;
        this.schoolYear = schoolYear;
        this.semester = semester;
        this.examinationType = examinationType;
        this.city = city;
        this.grade = grade;
        this.examinationDate = examinationDate;
        this.achievementType = achievementType;
        this.achievementEvidence = achievementEvidence;
        this.totalScore = totalScore;
        this.totalClassRanking = totalClassRanking;
        this.totalGradeRanking = totalGradeRanking;
        this.totalEvaluationType = totalEvaluationType;
        this.isDeleted = isDeleted;
        this.subjectNum = subjectNum;
        this.templateSubjectNum = templateSubjectNum;
        this.subjectSet = subjectSet;
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
     * 成绩模板编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_template_id")
    public AchievementTemplate getAchievementTemplate() {
        return achievementTemplate;
    }

    public void setAchievementTemplate(AchievementTemplate achievementTemplate) {
        this.achievementTemplate = achievementTemplate;
    }

    /**
     * 学生编号
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
     * 考试类型
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "examination_type")
    public DataDict getExaminationType() {
        return examinationType;
    }

    public void setExaminationType(DataDict examinationType) {
        this.examinationType = examinationType;
    }

    /**
     * 城市编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    public Region getCity() {
        return city;
    }

    public void setCity(Region city) {
        this.city = city;
    }

    /**
     * 年级编号
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    public DataDict getGrade() {
        return grade;
    }

    public void setGrade(DataDict grade) {
        this.grade = grade;
    }

    /**
     * 考试日期
     * @return
     */
    @Column(name = "examination_date", length = 10)
    public String getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(String examinationDate) {
        this.examinationDate = examinationDate;
    }

    /**
     * 成绩类型
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "achievement_type")
    public AchievementAbstractType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementAbstractType achievementType) {
        this.achievementType = achievementType;
    }

    /**
     * 考试凭证
     * @return
     */
    @Column(name = "achievement_evidence", length = 50)
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
    @Column(name = "total_score", precision = 5)
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
    @Column(name = "total_class_ranking")
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
    @Column(name = "total_grade_ranking")
    public Integer getTotalGradeRanking() {
        return totalGradeRanking;
    }

    public void setTotalGradeRanking(Integer totalGradeRanking) {
        this.totalGradeRanking = totalGradeRanking;
    }

    /**
     * 总的评级
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "total_evaluation_type")
    public EvaluationType getTotalEvaluationType() {
        return totalEvaluationType;
    }

    public void setTotalEvaluationType(EvaluationType totalEvaluationType) {
        this.totalEvaluationType = totalEvaluationType;
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
     * 科目数量
     * @return
     */
    @Column(name = "subject_num")
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
    @Column(name = "template_subject_num")
    public Integer getTemplateSubjectNum() {
        return templateSubjectNum;
    }

    public void setTemplateSubjectNum(Integer templateSubjectNum) {
        this.templateSubjectNum = templateSubjectNum;
    }

    /**
     * 学生成绩科目set
     * @return
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studentAchievement")
    @OrderBy("id ASC")
    public Set<StudentAchievementSubject> getSubjectSet() {
        return subjectSet;
    }

    public void setSubjectSet(Set<StudentAchievementSubject> subjectSet) {
        this.subjectSet = subjectSet;
    }

    @Override
    public String toString() {
        return "StudentAchievement [id=" + id + ", achievementTemplate="
                + achievementTemplate + ", student=" + student
                + ", schoolYear=" + schoolYear + ", semester=" + semester
                + ", examinationType=" + examinationType + ", city=" + city
                + ", grade=" + grade + ", examinationDate=" + examinationDate
                + ", achievementType=" + achievementType
                + ", achievementEvidence=" + achievementEvidence
                + ", totalScore=" + totalScore + ", totalClassRanking="
                + totalClassRanking + ", totalGradeRanking="
                + totalGradeRanking + ", totalEvaluationType="
                + totalEvaluationType + ", isDeleted=" + isDeleted
                + ", subjectNum=" + subjectNum + ", templateSubjectNum="
                + templateSubjectNum + ", subjectSet=" + subjectSet + super.toString() + "]";
    }

}
