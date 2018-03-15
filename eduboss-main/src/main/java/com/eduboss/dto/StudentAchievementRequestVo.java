package com.eduboss.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.eduboss.common.AchievementAbstractType;
import com.eduboss.common.EvaluationType;
import com.eduboss.common.Semester;

/**
 * 学生成绩RequestVo
 * @author arvin
 *
 */
public class StudentAchievementRequestVo implements Serializable {

    private static final long serialVersionUID = 3301420709163846413L;

    private Integer id;
    
    private Integer achievementTemplateId;
    
    private String studentId;
    
    private String schoolYearId;
    
    private Semester semester;
    
    private String examinationTypeId;
    
    private String cityId;
    
    private String gradeId;
    
    private String examinationDate;
    
    private AchievementAbstractType achievementType;
    
    private String achievementEvidence;
    
    private List<StudentAchievementSubjectRequestVo> subjectSet;
    
    private BigDecimal totalScore;
    
    private Integer totalClassRanking;
    
    private Integer totalGradeRanking;
    
    private EvaluationType totalEvaluationType;
    
    private Integer version;

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
    @NotNull(message="成绩模板编号不能为空")
    public Integer getAchievementTemplateId() {
        return achievementTemplateId;
    }

    public void setAchievementTemplateId(Integer achievementTemplateId) {
        this.achievementTemplateId = achievementTemplateId;
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
     * 考试类型
     * @return
     */
    @NotBlank(message="考试类型不能为空")
    public String getExaminationTypeId() {
        return examinationTypeId;
    }

    public void setExaminationTypeId(String examinationTypeId) {
        this.examinationTypeId = examinationTypeId;
    }

    /**
     * 城市编号
     * @return
     */
    @NotBlank(message="城市编号不能为空")
    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    /**
     * 年级编号
     * @return
     */
    @NotBlank(message="年级编号不能为空")
    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    /**
     * 考试时间
     * @return
     */
    @NotBlank(message="考试时间不能为空")
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
    @NotNull(message="成绩类型不能为空")
    public AchievementAbstractType getAchievementType() {
        return achievementType;
    }

    public void setAchievementType(AchievementAbstractType achievementType) {
        this.achievementType = achievementType;
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
     * 学生成绩科目set
     * @return
     */
//    @NotNull(message="学生成绩科目别表不能为空")
    public List<StudentAchievementSubjectRequestVo> getSubjectSet() {
        return subjectSet;
    }

    public void setSubjectSet(List<StudentAchievementSubjectRequestVo> subjectSet) {
        this.subjectSet = subjectSet;
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
     * 总分评价
     * @return
     */
    public EvaluationType getTotalEvaluationType() {
        return totalEvaluationType;
    }

    public void setTotalEvaluationType(EvaluationType totalEvaluationType) {
        this.totalEvaluationType = totalEvaluationType;
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
        return "StudentAchievementRequestVo [id=" + id
                + ", achievementTemplateId=" + achievementTemplateId
                + ", studentId=" + studentId + ", schoolYearId=" + schoolYearId
                + ", semester=" + semester + ", examinationTypeId="
                + examinationTypeId + ", cityId=" + cityId + ", gradeId="
                + gradeId + ", examinationDate=" + examinationDate
                + ", achievementType=" + achievementType
                + ", achievementEvidence=" + achievementEvidence
                + ", subjectSet=" + subjectSet + ", totalScore=" + totalScore
                + ", totalClassRanking=" + totalClassRanking
                + ", totalGradeRanking=" + totalGradeRanking
                + ", totalEvaluationType=" + totalEvaluationType + ", version="
                + version + "]";
    }

}
