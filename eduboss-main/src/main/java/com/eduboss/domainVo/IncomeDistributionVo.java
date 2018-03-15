package com.eduboss.domainVo;

import com.eduboss.domain.Organization;
import com.eduboss.domain.User;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/26.
 */
public class IncomeDistributionVo {
    private String id;
    private BigDecimal amount;//提成金额

    private String bonusType; //收款 退款

    private String baseBonusDistributeType;

    private String subBonusDistributeType;

    private String productType;

    private String bonusStaffId;

    private String bonusStaffName;

    private String bonusOrgId;

    private String bonusOrgName;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getBaseBonusDistributeType() {
        return baseBonusDistributeType;
    }

    public void setBaseBonusDistributeType(String baseBonusDistributeType) {
        this.baseBonusDistributeType = baseBonusDistributeType;
    }

    public String getSubBonusDistributeType() {
        return subBonusDistributeType;
    }

    public void setSubBonusDistributeType(String subBonusDistributeType) {
        this.subBonusDistributeType = subBonusDistributeType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBonusStaffId() {
        return bonusStaffId;
    }

    public void setBonusStaffId(String bonusStaffId) {
        this.bonusStaffId = bonusStaffId;
    }

    public String getBonusStaffName() {
        return bonusStaffName;
    }

    public void setBonusStaffName(String bonusStaffName) {
        this.bonusStaffName = bonusStaffName;
    }

    public String getBonusOrgId() {
        return bonusOrgId;
    }

    public void setBonusOrgId(String bonusOrgId) {
        this.bonusOrgId = bonusOrgId;
    }

    public String getBonusOrgName() {
        return bonusOrgName;
    }

    public void setBonusOrgName(String bonusOrgName) {
        this.bonusOrgName = bonusOrgName;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
    }


}
