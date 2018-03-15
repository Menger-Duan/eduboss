package com.eduboss.dto;

import java.math.BigDecimal;

/**
 * 获取合同中每种合同产品的可分配金额
 * Created by Administrator on 2016/5/5.
 */
public class ContractEachProductTypeMoney {
    private String contractId;
    private BigDecimal oneMoney;
    private BigDecimal miniMoney;
    private BigDecimal otmMoney;
    private BigDecimal ecsMoney;
    private BigDecimal otherMoney;
    private BigDecimal lectureMoney;
    private BigDecimal twoTeacherMoney;
    private BigDecimal liveMoney;


    private boolean exitOneOnOne = false;
    private boolean exitSmallClass = false;
    private boolean exitOneOnMany = false;
    private boolean exitEcsClass = false;
    private boolean exitOthers = false;
    private boolean exitLecture = false;
    private boolean exitTwoTeacher = false;
    private boolean exitLive=false;


    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public BigDecimal getOneMoney() {
        return oneMoney;
    }

    public void setOneMoney(BigDecimal oneMoney) {
        this.oneMoney = oneMoney;
    }

    public BigDecimal getMiniMoney() {
        return miniMoney;
    }

    public void setMiniMoney(BigDecimal miniMoney) {
        this.miniMoney = miniMoney;
    }

    public BigDecimal getOtmMoney() {
        return otmMoney;
    }

    public void setOtmMoney(BigDecimal otmMoney) {
        this.otmMoney = otmMoney;
    }

    public BigDecimal getEcsMoney() {
        return ecsMoney;
    }

    public void setEcsMoney(BigDecimal ecsMoney) {
        this.ecsMoney = ecsMoney;
    }

    public BigDecimal getOtherMoney() {
        return otherMoney;
    }

    public void setOtherMoney(BigDecimal otherMoney) {
        this.otherMoney = otherMoney;
    }

    public BigDecimal getLectureMoney() {
        return lectureMoney;
    }

    public void setLectureMoney(BigDecimal lectureMoney) {
        this.lectureMoney = lectureMoney;
    }


    public boolean isExitOneOnOne() {
        return exitOneOnOne;
    }

    public void setExitOneOnOne(boolean exitOneOnOne) {
        this.exitOneOnOne = exitOneOnOne;
    }

    public boolean isExitSmallClass() {
        return exitSmallClass;
    }

    public void setExitSmallClass(boolean exitSmallClass) {
        this.exitSmallClass = exitSmallClass;
    }

    public boolean isExitOneOnMany() {
        return exitOneOnMany;
    }

    public void setExitOneOnMany(boolean exitOneOnMany) {
        this.exitOneOnMany = exitOneOnMany;
    }

    public boolean isExitEcsClass() {
        return exitEcsClass;
    }

    public void setExitEcsClass(boolean exitEcsClass) {
        this.exitEcsClass = exitEcsClass;
    }

    public boolean isExitOthers() {
        return exitOthers;
    }

    public void setExitOthers(boolean exitOthers) {
        this.exitOthers = exitOthers;
    }

    public boolean isExitLecture() {
        return exitLecture;
    }

    public void setExitLecture(boolean exitLecture) {
        this.exitLecture = exitLecture;
    }

    public BigDecimal getTwoTeacherMoney() {
        return twoTeacherMoney;
    }

    public void setTwoTeacherMoney(BigDecimal twoTeacherMoney) {
        this.twoTeacherMoney = twoTeacherMoney;
    }

    public boolean isExitTwoTeacher() {
        return exitTwoTeacher;
    }

    public void setExitTwoTeacher(boolean exitTwoTeacher) {
        this.exitTwoTeacher = exitTwoTeacher;
    }

    public BigDecimal getLiveMoney() {
        return liveMoney;
    }

    public void setLiveMoney(BigDecimal liveMoney) {
        this.liveMoney = liveMoney;
    }

    public boolean isExitLive() {
        return exitLive;
    }

    public void setExitLive(boolean exitLive) {
        this.exitLive = exitLive;
    }
}
