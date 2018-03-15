package com.eduboss.dto;

import java.io.Serializable;

import com.eduboss.common.StandardCategory;

/**
 * 学生成绩对比基准更新vo
 * @author arvin
 *
 */
public class AchievementBenchmarkUpdateVo implements Serializable {

    private static final long serialVersionUID = -2261641215041708242L;

    private Integer id;
    
    private StandardCategory standardCategory;
    
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 进步类别
     * @return
     */
    public StandardCategory getStandardCategory() {
        return standardCategory;
    }

    public void setStandardCategory(StandardCategory standardCategory) {
        this.standardCategory = standardCategory;
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
        return "AchievementBenchmarkUpdateVo [id=" + id + ", standardCategory="
                + standardCategory + ", version=" + version + "]";
    }

}
