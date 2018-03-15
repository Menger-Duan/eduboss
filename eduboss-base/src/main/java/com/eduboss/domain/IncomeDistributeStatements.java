package com.eduboss.domain;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.BonusType;
import com.eduboss.common.IncomDistributeStatementsType;
import com.eduboss.common.ProductType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/12/23.
 */

/**
 * 业绩分配流水
 */
@Entity
@Table(name = "INCOME_DISTRIBUTE_STATEMENTS")
public class IncomeDistributeStatements {

    private int id;

    private BonusType bonusType; //收费 or 退费

    private BonusDistributeType baseBonusDistributeType; //校区业绩  or 提成人业绩

    private BonusDistributeType subBonusDistributeType; //第二级分配类型

    private User bonusStaff; //业绩归属

    private Organization bonusOrg; //业绩归属

    private ProductType productType; //产品类型

    private BigDecimal amount;//金额

    private IncomDistributeStatementsType operation;//操作类型 分配或者提取

    private BigDecimal currentAmount;//当前业绩

    private Timestamp createTime; //创建日期

    private String fundsChangeTime;//收款日期

    private FundsChangeHistory fundsChangeHistory;

    private User createUser;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable=false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "bonus_type", length = 32)
    public BonusType getBonusType() {
        return bonusType;
    }

    public void setBonusType(BonusType bonusType) {
        this.bonusType = bonusType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "base_bonus_type")
    public BonusDistributeType getBaseBonusDistributeType() {
        return baseBonusDistributeType;
    }

    public void setBaseBonusDistributeType(BonusDistributeType baseBonusDistributeType) {
        this.baseBonusDistributeType = baseBonusDistributeType;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "sub_bonus_type")
    public BonusDistributeType getSubBonusDistributeType() {
        return subBonusDistributeType;
    }

    public void setSubBonusDistributeType(BonusDistributeType subBonusDistributeType) {
        this.subBonusDistributeType = subBonusDistributeType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_staff_id")
    public User getBonusStaff() {
        return bonusStaff;
    }

    public void setBonusStaff(User bonusStaff) {
        this.bonusStaff = bonusStaff;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizationId")
    public Organization getBonusOrg() {
        return bonusOrg;
    }

    public void setBonusOrg(Organization bonusOrg) {
        this.bonusOrg = bonusOrg;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", length = 32)
    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    @Column(name = "amount", precision = 10)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "currentAmount", precision = 10)
    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    public IncomDistributeStatementsType getOperation() {
        return operation;
    }

    public void setOperation(IncomDistributeStatementsType operation) {
        this.operation = operation;
    }

    @Column(name = "create_time" )
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    @Column(name = "funds_change_time")
    public String getFundsChangeTime() {
        return fundsChangeTime;
    }

    public void setFundsChangeTime(String fundsChangeTime) {
        this.fundsChangeTime = fundsChangeTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funds_change_id")
    public FundsChangeHistory getFundsChangeHistory() {
        return fundsChangeHistory;
    }

    public void setFundsChangeHistory(FundsChangeHistory fundsChangeHistory) {
        this.fundsChangeHistory = fundsChangeHistory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }
}
