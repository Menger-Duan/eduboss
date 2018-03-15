package com.eduboss.domainVo;

import com.eduboss.common.BonusDistributeType;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/1/24.
 */
public class RefundIncomeDistributeVo {
    private String firstRefundDutyCampusId; // 退费责任校区1
    private String firstRefundDutyCampusName;
    private BigDecimal firstRefundDutyAmountCampus; // 校区退费责任金额1
    private BonusDistributeType firstSubBonusDistributeTypeCampus;//校区分配类型1

    private String secondRefundDutyCampusId; // 退费责任校区2
    private String secondRefundDutyCampusName;
    private BigDecimal secondRefundDutyAmountCampus; // 退费责任金额2
    private BonusDistributeType secondSubBonusDistributeTypeCampus;//校区分配类型2

    private String thirdRefundDutyCampusId; // 退费责任校区3
    private String thirdRefundDutyCampusName;
    private BigDecimal thirdRefundDutyAmountCampus; // 退费责任金额3
    private BonusDistributeType thirdSubBonusDistributeTypeCampus;//校区分配类型3

    private String fourthRefundDutyCampusId;
    private String fourthRefundDutyCampusName;
    private BigDecimal fourthRefundDutyAmountCampus;
    private BonusDistributeType fourthSubBonusDistributeTypeCampus;//校区分配类型4

    private String fifthRefundDutyCampusId;
    private String fifthRefundDutyCampusName;
    private BigDecimal fifthRefundDutyAmountCampus; // 退费责任校区金额5
    private BonusDistributeType fifthSubBonusDistributeTypeCampus;//校区分配类型5


    private String firstRefundDutyPersonId; // 退费责任人1
    private String firstRefundDutyPersonName;
    private BigDecimal firstRefundDutyAmountPerson; // 责任人退费责任金额1
    private BonusDistributeType firstSubBonusDistributeTypePerson; //责任人分配类型1

    private String secondRefundDutyPersonId; // 退费责任人2
    private String secondRefundDutyPersonName;
    private BigDecimal secondRefundDutyAmountPerson; // 退费责任人金额2
    private BonusDistributeType secondSubBonusDistributeTypePerson; //责任人分配类型2

    private String thirdRefundDutyPersonId; // 退费责任人3
    private String thirdRefundDutyPersonName;
    private BigDecimal thirdRefundDutyAmountPerson; // 退费责任人金额3
    private BonusDistributeType thirdSubBonusDistributeTypePerson; //责任人分配类型3

    private String fourthRefundDutyPersonId; // 退费责任人4
    private String fourthRefundDutyPersonName; // 退费责任人
    private BigDecimal fourthRefundDutyAmountPerson; // 退费责任人金额4
    private BonusDistributeType fourthSubBonusDistributeTypePerson; //责任人分配类型4

    private String fifthRefundDutyPersonId; // 退费责任人5
    private String fifthRefundDutyPersonName; // 退费责任人5
    private BigDecimal fifthRefundDutyAmountPerson; // 退费责任人金额5
    private BonusDistributeType fifthSubBonusDistributeTypePerson; //责任人分配类型5


    public String getFirstRefundDutyCampusId() {
        return firstRefundDutyCampusId;
    }

    public void setFirstRefundDutyCampusId(String firstRefundDutyCampusId) {
        this.firstRefundDutyCampusId = firstRefundDutyCampusId;
    }

    public String getFirstRefundDutyCampusName() {
        return firstRefundDutyCampusName;
    }

    public void setFirstRefundDutyCampusName(String firstRefundDutyCampusName) {
        this.firstRefundDutyCampusName = firstRefundDutyCampusName;
    }

    public BigDecimal getFirstRefundDutyAmountCampus() {
        return firstRefundDutyAmountCampus;
    }

    public void setFirstRefundDutyAmountCampus(BigDecimal firstRefundDutyAmountCampus) {
        this.firstRefundDutyAmountCampus = firstRefundDutyAmountCampus;
    }

    public BonusDistributeType getFirstSubBonusDistributeTypeCampus() {
        return firstSubBonusDistributeTypeCampus;
    }

    public void setFirstSubBonusDistributeTypeCampus(BonusDistributeType firstSubBonusDistributeTypeCampus) {
        this.firstSubBonusDistributeTypeCampus = firstSubBonusDistributeTypeCampus;
    }

    public String getSecondRefundDutyCampusId() {
        return secondRefundDutyCampusId;
    }

    public void setSecondRefundDutyCampusId(String secondRefundDutyCampusId) {
        this.secondRefundDutyCampusId = secondRefundDutyCampusId;
    }

    public String getSecondRefundDutyCampusName() {
        return secondRefundDutyCampusName;
    }

    public void setSecondRefundDutyCampusName(String secondRefundDutyCampusName) {
        this.secondRefundDutyCampusName = secondRefundDutyCampusName;
    }

    public BigDecimal getSecondRefundDutyAmountCampus() {
        return secondRefundDutyAmountCampus;
    }

    public void setSecondRefundDutyAmountCampus(BigDecimal secondRefundDutyAmountCampus) {
        this.secondRefundDutyAmountCampus = secondRefundDutyAmountCampus;
    }

    public BonusDistributeType getSecondSubBonusDistributeTypeCampus() {
        return secondSubBonusDistributeTypeCampus;
    }

    public void setSecondSubBonusDistributeTypeCampus(BonusDistributeType secondSubBonusDistributeTypeCampus) {
        this.secondSubBonusDistributeTypeCampus = secondSubBonusDistributeTypeCampus;
    }

    public String getThirdRefundDutyCampusId() {
        return thirdRefundDutyCampusId;
    }

    public void setThirdRefundDutyCampusId(String thirdRefundDutyCampusId) {
        this.thirdRefundDutyCampusId = thirdRefundDutyCampusId;
    }

    public String getThirdRefundDutyCampusName() {
        return thirdRefundDutyCampusName;
    }

    public void setThirdRefundDutyCampusName(String thirdRefundDutyCampusName) {
        this.thirdRefundDutyCampusName = thirdRefundDutyCampusName;
    }

    public BigDecimal getThirdRefundDutyAmountCampus() {
        return thirdRefundDutyAmountCampus;
    }

    public void setThirdRefundDutyAmountCampus(BigDecimal thirdRefundDutyAmountCampus) {
        this.thirdRefundDutyAmountCampus = thirdRefundDutyAmountCampus;
    }

    public BonusDistributeType getThirdSubBonusDistributeTypeCampus() {
        return thirdSubBonusDistributeTypeCampus;
    }

    public void setThirdSubBonusDistributeTypeCampus(BonusDistributeType thirdSubBonusDistributeTypeCampus) {
        this.thirdSubBonusDistributeTypeCampus = thirdSubBonusDistributeTypeCampus;
    }

    public String getFourthRefundDutyCampusId() {
        return fourthRefundDutyCampusId;
    }

    public void setFourthRefundDutyCampusId(String fourthRefundDutyCampusId) {
        this.fourthRefundDutyCampusId = fourthRefundDutyCampusId;
    }

    public String getFourthRefundDutyCampusName() {
        return fourthRefundDutyCampusName;
    }

    public void setFourthRefundDutyCampusName(String fourthRefundDutyCampusName) {
        this.fourthRefundDutyCampusName = fourthRefundDutyCampusName;
    }

    public BigDecimal getFourthRefundDutyAmountCampus() {
        return fourthRefundDutyAmountCampus;
    }

    public void setFourthRefundDutyAmountCampus(BigDecimal fourthRefundDutyAmountCampus) {
        this.fourthRefundDutyAmountCampus = fourthRefundDutyAmountCampus;
    }

    public BonusDistributeType getFourthSubBonusDistributeTypeCampus() {
        return fourthSubBonusDistributeTypeCampus;
    }

    public void setFourthSubBonusDistributeTypeCampus(BonusDistributeType fourthSubBonusDistributeTypeCampus) {
        this.fourthSubBonusDistributeTypeCampus = fourthSubBonusDistributeTypeCampus;
    }

    public String getFifthRefundDutyCampusId() {
        return fifthRefundDutyCampusId;
    }

    public void setFifthRefundDutyCampusId(String fifthRefundDutyCampusId) {
        this.fifthRefundDutyCampusId = fifthRefundDutyCampusId;
    }

    public String getFifthRefundDutyCampusName() {
        return fifthRefundDutyCampusName;
    }

    public void setFifthRefundDutyCampusName(String fifthRefundDutyCampusName) {
        this.fifthRefundDutyCampusName = fifthRefundDutyCampusName;
    }

    public BigDecimal getFifthRefundDutyAmountCampus() {
        return fifthRefundDutyAmountCampus;
    }

    public void setFifthRefundDutyAmountCampus(BigDecimal fifthRefundDutyAmountCampus) {
        this.fifthRefundDutyAmountCampus = fifthRefundDutyAmountCampus;
    }

    public BonusDistributeType getFifthSubBonusDistributeTypeCampus() {
        return fifthSubBonusDistributeTypeCampus;
    }

    public void setFifthSubBonusDistributeTypeCampus(BonusDistributeType fifthSubBonusDistributeTypeCampus) {
        this.fifthSubBonusDistributeTypeCampus = fifthSubBonusDistributeTypeCampus;
    }

    public String getFirstRefundDutyPersonId() {
        return firstRefundDutyPersonId;
    }

    public void setFirstRefundDutyPersonId(String firstRefundDutyPersonId) {
        this.firstRefundDutyPersonId = firstRefundDutyPersonId;
    }

    public String getFirstRefundDutyPersonName() {
        return firstRefundDutyPersonName;
    }

    public void setFirstRefundDutyPersonName(String firstRefundDutyPersonName) {
        this.firstRefundDutyPersonName = firstRefundDutyPersonName;
    }

    public BigDecimal getFirstRefundDutyAmountPerson() {
        return firstRefundDutyAmountPerson;
    }

    public void setFirstRefundDutyAmountPerson(BigDecimal firstRefundDutyAmountPerson) {
        this.firstRefundDutyAmountPerson = firstRefundDutyAmountPerson;
    }

    public BonusDistributeType getFirstSubBonusDistributeTypePerson() {
        return firstSubBonusDistributeTypePerson;
    }

    public void setFirstSubBonusDistributeTypePerson(BonusDistributeType firstSubBonusDistributeTypePerson) {
        this.firstSubBonusDistributeTypePerson = firstSubBonusDistributeTypePerson;
    }

    public String getSecondRefundDutyPersonId() {
        return secondRefundDutyPersonId;
    }

    public void setSecondRefundDutyPersonId(String secondRefundDutyPersonId) {
        this.secondRefundDutyPersonId = secondRefundDutyPersonId;
    }

    public String getSecondRefundDutyPersonName() {
        return secondRefundDutyPersonName;
    }

    public void setSecondRefundDutyPersonName(String secondRefundDutyPersonName) {
        this.secondRefundDutyPersonName = secondRefundDutyPersonName;
    }

    public BigDecimal getSecondRefundDutyAmountPerson() {
        return secondRefundDutyAmountPerson;
    }

    public void setSecondRefundDutyAmountPerson(BigDecimal secondRefundDutyAmountPerson) {
        this.secondRefundDutyAmountPerson = secondRefundDutyAmountPerson;
    }

    public BonusDistributeType getSecondSubBonusDistributeTypePerson() {
        return secondSubBonusDistributeTypePerson;
    }

    public void setSecondSubBonusDistributeTypePerson(BonusDistributeType secondSubBonusDistributeTypePerson) {
        this.secondSubBonusDistributeTypePerson = secondSubBonusDistributeTypePerson;
    }

    public String getThirdRefundDutyPersonId() {
        return thirdRefundDutyPersonId;
    }

    public void setThirdRefundDutyPersonId(String thirdRefundDutyPersonId) {
        this.thirdRefundDutyPersonId = thirdRefundDutyPersonId;
    }

    public String getThirdRefundDutyPersonName() {
        return thirdRefundDutyPersonName;
    }

    public void setThirdRefundDutyPersonName(String thirdRefundDutyPersonName) {
        this.thirdRefundDutyPersonName = thirdRefundDutyPersonName;
    }

    public BigDecimal getThirdRefundDutyAmountPerson() {
        return thirdRefundDutyAmountPerson;
    }

    public void setThirdRefundDutyAmountPerson(BigDecimal thirdRefundDutyAmountPerson) {
        this.thirdRefundDutyAmountPerson = thirdRefundDutyAmountPerson;
    }

    public BonusDistributeType getThirdSubBonusDistributeTypePerson() {
        return thirdSubBonusDistributeTypePerson;
    }

    public void setThirdSubBonusDistributeTypePerson(BonusDistributeType thirdSubBonusDistributeTypePerson) {
        this.thirdSubBonusDistributeTypePerson = thirdSubBonusDistributeTypePerson;
    }

    public String getFourthRefundDutyPersonId() {
        return fourthRefundDutyPersonId;
    }

    public void setFourthRefundDutyPersonId(String fourthRefundDutyPersonId) {
        this.fourthRefundDutyPersonId = fourthRefundDutyPersonId;
    }

    public String getFourthRefundDutyPersonName() {
        return fourthRefundDutyPersonName;
    }

    public void setFourthRefundDutyPersonName(String fourthRefundDutyPersonName) {
        this.fourthRefundDutyPersonName = fourthRefundDutyPersonName;
    }

    public BigDecimal getFourthRefundDutyAmountPerson() {
        return fourthRefundDutyAmountPerson;
    }

    public void setFourthRefundDutyAmountPerson(BigDecimal fourthRefundDutyAmountPerson) {
        this.fourthRefundDutyAmountPerson = fourthRefundDutyAmountPerson;
    }

    public BonusDistributeType getFourthSubBonusDistributeTypePerson() {
        return fourthSubBonusDistributeTypePerson;
    }

    public void setFourthSubBonusDistributeTypePerson(BonusDistributeType fourthSubBonusDistributeTypePerson) {
        this.fourthSubBonusDistributeTypePerson = fourthSubBonusDistributeTypePerson;
    }

    public String getFifthRefundDutyPersonId() {
        return fifthRefundDutyPersonId;
    }

    public void setFifthRefundDutyPersonId(String fifthRefundDutyPersonId) {
        this.fifthRefundDutyPersonId = fifthRefundDutyPersonId;
    }

    public String getFifthRefundDutyPersonName() {
        return fifthRefundDutyPersonName;
    }

    public void setFifthRefundDutyPersonName(String fifthRefundDutyPersonName) {
        this.fifthRefundDutyPersonName = fifthRefundDutyPersonName;
    }

    public BigDecimal getFifthRefundDutyAmountPerson() {
        return fifthRefundDutyAmountPerson;
    }

    public void setFifthRefundDutyAmountPerson(BigDecimal fifthRefundDutyAmountPerson) {
        this.fifthRefundDutyAmountPerson = fifthRefundDutyAmountPerson;
    }

    public BonusDistributeType getFifthSubBonusDistributeTypePerson() {
        return fifthSubBonusDistributeTypePerson;
    }

    public void setFifthSubBonusDistributeTypePerson(BonusDistributeType fifthSubBonusDistributeTypePerson) {
        this.fifthSubBonusDistributeTypePerson = fifthSubBonusDistributeTypePerson;
    }
}
