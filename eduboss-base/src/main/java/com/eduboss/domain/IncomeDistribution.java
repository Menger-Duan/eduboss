package com.eduboss.domain;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.BonusType;
import com.eduboss.common.ProductType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 新版业绩分配
 * Created by Administrator on 2016/12/17.
 */


@Entity
@Table(name = "INCOME_DISTRIBUTION")
public class IncomeDistribution {

    private int id;

    private User bonusStaff;

    private Organization bonusOrg;

    private BonusType bonusType; //收费 or 退费

    private BonusDistributeType baseBonusDistributeType; //校区业绩  or 提成人业绩

    private BonusDistributeType subBonusDistributeType; //第二级分配类型

    private ProductType productType; //产品类型

    private BigDecimal amount;//提成金额


    private Timestamp createTime;

    private User createUser;


    private FundsChangeHistory fundsChangeHistory;

    private StudentReturnFee studentReturnFee;//退费记录




    private Organization bonusStaffCampus;//业绩提成人校区 ？看看还需不需要
    private Organization contractCampus;//业绩来源校区，合同校区 ？看看还需不需要

    public IncomeDistribution() {
    }

//    @GenericGenerator(name = "generator", strategy = "uuid.hex")
//    @Id
//    @GeneratedValue(generator = "generator")
//    @Column(name = "id", unique = true, nullable = false, length = 32)
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable=false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Column(name = "amount", precision = 10)
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "create_time", length = 20)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
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
    @JoinColumn(name = "student_return_id")
    public StudentReturnFee getStudentReturnFee() {
        return studentReturnFee;
    }

    public void setStudentReturnFee(StudentReturnFee studentReturnFee) {
        this.studentReturnFee = studentReturnFee;
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
    @Column(name = "product_type", length = 32)
    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }




    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bonus_staff_campus")
    public Organization getBonusStaffCampus() {
        return bonusStaffCampus;
    }

    public void setBonusStaffCampus(Organization bonusStaffCampus) {
        this.bonusStaffCampus = bonusStaffCampus;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_campus")
    public Organization getContractCampus() {
        return contractCampus;
    }

    public void setContractCampus(Organization contractCampus) {
        this.contractCampus = contractCampus;
    }
}
