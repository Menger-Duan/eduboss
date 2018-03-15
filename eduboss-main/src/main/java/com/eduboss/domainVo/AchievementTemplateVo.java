package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 成绩模板vo
 * @author arvin
 *
 */
public class AchievementTemplateVo implements Serializable {
    
    private static final long serialVersionUID = -6440986728771028003L;

    private Integer id;
    
    private String cityId;
    
    private String cityName;
    
    private String effectiveDate;
    
    private Integer achievementVersion;
    
    private Integer isCurrentVersion;
    
    private String createTime;
    
    private String createUserName;
    
    private Integer version;

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
     * 起始生效日期
     * @return
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * 版本
     * @return
     */
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
    public Integer getIsCurrentVersion() {
        return isCurrentVersion;
    }

    public void setIsCurrentVersion(Integer isCurrentVersion) {
        this.isCurrentVersion = isCurrentVersion;
    }

    /**
     * 创建时间
     * @return
     */
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 创建人名称
     * @return
     */
    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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
