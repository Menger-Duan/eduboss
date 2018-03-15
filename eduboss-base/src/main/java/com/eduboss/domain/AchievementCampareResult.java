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

import com.eduboss.common.StandardCategory;

/**
 * 成绩对比结果 废弃
 * @author arvin
 *
 */
/*@Entity
@Table(name = "achievement_campare_result")*/
public class AchievementCampareResult extends BaseVersionDomain {

    private static final long serialVersionUID = -8230333651048225225L;

    private Integer id;
    
    private AchievementBenchmark achievementBenchmark;
    
    private StandardCategory standardCategory;
    
    private String remark;

    public AchievementCampareResult() {
        super();
    }

    public AchievementCampareResult(Integer id,
            AchievementBenchmark achievementBenchmark,
            StandardCategory standardCategory, String remark) {
        super();
        this.id = id;
        this.achievementBenchmark = achievementBenchmark;
        this.standardCategory = standardCategory;
        this.remark = remark;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_benchmark_id")
    public AchievementBenchmark getAchievementBenchmark() {
        return achievementBenchmark;
    }

    public void setAchievementBenchmark(AchievementBenchmark achievementBenchmark) {
        this.achievementBenchmark = achievementBenchmark;
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

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AchievementCampareResult [id=" + id + ", achievementBenchmark="
                + achievementBenchmark + ", standardCategory="
                + standardCategory + ", remark=" + remark
                + super.toString() + "]";
    }
    
}
