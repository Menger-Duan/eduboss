package com.eduboss.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 成绩模板年级查询Vo
 * @author arvin
 *
 */
public class AchievementTemplateGradeSearVo implements Serializable {

    private static final long serialVersionUID = -2691124027180304676L;

    private String cityId;
    
    private String gradeId;
    
    private String examinationDate;

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

    @Override
    public String toString() {
        return "AchievementTemplateGradeSearVo [cityId=" + cityId
                + ", gradeId=" + gradeId + ", examinationDate="
                + examinationDate + "]";
    }
    
}
