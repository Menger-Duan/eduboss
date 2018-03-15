package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.eduboss.common.BusinessType;

/**
 * 业务对接关联表
 * @author lixuejun
 *
 */
@Entity
@Table(name = "business_assoc_mapping")
public class BusinessAssocMapping {

    private Long id;
    
    private String businessId;
    
    private BusinessType businessType;
    
    private String relateNo;
    
    private String createTime;
    
    private String modifyTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable=false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "business_id", length = 32)
    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "business_type", length = 20)
    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    @Column(name = "relate_no", length = 20)
    public String getRelateNo() {
        return relateNo;
    }

    public void setRelateNo(String relateNo) {
        this.relateNo = relateNo;
    }

    @Column(name = "create_time", length = 20, updatable=false, insertable=false)
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name = "modify_time", length = 20, updatable=false, insertable=false)
    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    
}
