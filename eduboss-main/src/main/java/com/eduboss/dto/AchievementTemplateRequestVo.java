package com.eduboss.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;


/**
 * 新建、修改成绩模板Vo
 * @author arvin
 *
 */
public class AchievementTemplateRequestVo implements Serializable {

    private static final long serialVersionUID = 7011622427498006920L;

    private Integer id;
    
    private String cityId;
    
    private String effectiveDate;
    
    private Integer version;
    
    private Set<AchievementTemplateGradeRequestVo> grades;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
     * 生效日期
     * @return
     */
    @NotBlank(message="生效日期不能为空")
    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * 成绩模板年级set
     * @return
     */
    @NotNull(message="成绩模板年级列表不能为空")
    public Set<AchievementTemplateGradeRequestVo> getGrades() {
        return grades;
    }

    public void setGrades(Set<AchievementTemplateGradeRequestVo> grades) {
        this.grades = grades;
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
        return "AchievementTemplateRequestVo [id=" + id + ", cityId=" + cityId
                + ", effectiveDate=" + effectiveDate + ", version=" + version
                + ", grades=" + grades + "]";
    }

}
