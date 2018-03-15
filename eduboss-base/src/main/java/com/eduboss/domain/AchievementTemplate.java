package com.eduboss.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * 成绩模板
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_template")
public class AchievementTemplate extends BaseVersionDomain {

    private static final long serialVersionUID = 61145055257453544L;

    private Integer id;
    
    private Region city;
    
    private String effectiveDate;
    
    private Integer achievementVersion;
    
    private Integer isCurrentVersion;
    
    private Integer isDeleted;
    
    private Set<AchievementTemplateGrade> grades;

    public AchievementTemplate() {
        super();
    }

    public AchievementTemplate(Integer id) {
        super();
        this.id = id;
    }

    public AchievementTemplate(Integer id, Region city, String effectiveDate,
            Integer achievementVersion, Integer isCurrentVersion,
            Integer isDeleted, Set<AchievementTemplateGrade> grades) {
        super();
        this.id = id;
        this.city = city;
        this.effectiveDate = effectiveDate;
        this.achievementVersion = achievementVersion;
        this.isCurrentVersion = isCurrentVersion;
        this.isDeleted = isDeleted;
        this.grades = grades;
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
     * 生效日期
     * @return
     */
    @Column(name = "effective_date", length = 10)
    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * 模板版本
     * @return
     */
    @Column(name = "achievement_version")
    public Integer getAchievementVersion() {
        return achievementVersion;
    }

    public void setAchievementVersion(Integer achievementVersion) {
        this.achievementVersion = achievementVersion;
    }

    /**
     * 是否当前版本1:否，0：是
     * @return
     */
    @Column(name = "is_current_version")
    public Integer getIsCurrentVersion() {
        return isCurrentVersion;
    }

    public void setIsCurrentVersion(Integer isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "achievementTemplate")
    @OrderBy("id ASC")
    public Set<AchievementTemplateGrade> getGrades() {
        return grades;
    }

    public void setGrades(Set<AchievementTemplateGrade> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "AchievementTemplate [id=" + id + ", city=" + city
                + ", effectiveDate=" + effectiveDate + ", achievementVersion="
                + achievementVersion + ", isCurrentVersion="
                + isCurrentVersion + ", isDeleted=" + isDeleted
                + ", grades=" + grades + super.toString() + "]";
    }
    
}
