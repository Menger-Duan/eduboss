package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.eduboss.common.StandardCategory;

/**
 * 成绩基准科目操作日志
 * @author arvin
 *
 */
@Entity
@Table(name = "achievement_benchmark_log")
public class AchievementBenchmarkLog extends BaseDomain {

    private static final long serialVersionUID = -3334745018403713909L;

    private Integer id;
    
    private Integer achievementBenchmarkId;
    
    private StandardCategory standardCategoryOri;
    
    private StandardCategory standardCategoryTar;

    public AchievementBenchmarkLog() {
        super();
    }

    public AchievementBenchmarkLog(Integer id, Integer achievementBenchmarkId,
            StandardCategory standardCategoryOri,
            StandardCategory standardCategoryTar) {
        super();
        this.id = id;
        this.achievementBenchmarkId = achievementBenchmarkId;
        this.standardCategoryOri = standardCategoryOri;
        this.standardCategoryTar = standardCategoryTar;
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
     * 成绩基准科目编号
     * @return
     */
    @Column(name = "achievement_benchmark_id", nullable=false)
    public Integer getAchievementBenchmarkId() {
        return achievementBenchmarkId;
    }

    public void setAchievementBenchmarkId(Integer achievementBenchmarkId) {
        this.achievementBenchmarkId = achievementBenchmarkId;
    }

    /**
     * 原进步类别
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "standard_category_ori")
    public StandardCategory getStandardCategoryOri() {
        return standardCategoryOri;
    }

    public void setStandardCategoryOri(StandardCategory standardCategoryOri) {
        this.standardCategoryOri = standardCategoryOri;
    }

    /**
     * 目标进步类别
     * @return
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "standard_category_tar")
    public StandardCategory getStandardCategoryTar() {
        return standardCategoryTar;
    }

    public void setStandardCategoryTar(StandardCategory standardCategoryTar) {
        this.standardCategoryTar = standardCategoryTar;
    }

    @Override
    public String toString() {
        return "AchievementBenchmarkLog [id=" + id
                + ", achievementBenchmarkId=" + achievementBenchmarkId
                + ", standardCategoryOri=" + standardCategoryOri
                + ", standardCategoryTar=" + standardCategoryTar + super.toString() + "]";
    }
    
}
