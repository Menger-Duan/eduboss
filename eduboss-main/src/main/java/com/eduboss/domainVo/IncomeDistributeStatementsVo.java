package com.eduboss.domainVo;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.BonusType;
import com.eduboss.common.IncomDistributeStatementsType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/12/29.
 */
public class IncomeDistributeStatementsVo {
    private String id;

    private String bonusType; //收费 or 退费

    private String baseBonusDistributeType; //校区业绩  or 提成人业绩

    private String subBonusDistributeType; //第二级分配类型

//    private User bonusStaff; //业绩归属
//
//    private Organization bonusOrg; //业绩归属

    private String bonusBelong; //业绩归属

    private String productType; //产品类型

    private BigDecimal amount;//金额

    private String operation;//操作类型 分配或者提取

    private BigDecimal currentAmount;//当前业绩

    private String createTime; //创建日期

    private String createUserName;

    private String fundsChangeTime;//收款日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBonusType() {
        return bonusType;
    }

    public void setBonusType(String bonusType) {
        this.bonusType = bonusType;
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

    public String getBonusBelong() {
        return bonusBelong;
    }

    public void setBonusBelong(String bonusBelong) {
        this.bonusBelong = bonusBelong;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFundsChangeTime() {
        return fundsChangeTime;
    }

    public void setFundsChangeTime(String fundsChangeTime) {
        this.fundsChangeTime = fundsChangeTime;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
}
