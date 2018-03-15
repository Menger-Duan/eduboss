package com.eduboss.dto;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/12/28.
 */
public class IncomeDistributeExceptThisfund {

    private BigDecimal userExceptOneOnOne = BigDecimal.ZERO;
    private BigDecimal campusExceptOneOnOne = BigDecimal.ZERO;

    private BigDecimal userExceptSmallClass = BigDecimal.ZERO;
    private BigDecimal campusExceptSmallClass = BigDecimal.ZERO;

    private BigDecimal userExceptOneOnMany = BigDecimal.ZERO;
    private BigDecimal campusExceptOneOnMany = BigDecimal.ZERO;

    private BigDecimal userExceptEcsClass = BigDecimal.ZERO;
    private BigDecimal campusExceptEcsClass = BigDecimal.ZERO;

    private BigDecimal userExceptOthers = BigDecimal.ZERO;
    private BigDecimal campusExceptOthers= BigDecimal.ZERO;

    private BigDecimal userExceptLecture = BigDecimal.ZERO;
    private BigDecimal campusExceptLecture = BigDecimal.ZERO;

    private BigDecimal userExceptTwoTeacher = BigDecimal.ZERO;
    private BigDecimal campusExceptTwoTeacher = BigDecimal.ZERO;

    public BigDecimal getUserExceptOneOnOne() {
        return userExceptOneOnOne;
    }

    public void setUserExceptOneOnOne(BigDecimal userExceptOneOnOne) {
        this.userExceptOneOnOne = userExceptOneOnOne;
    }

    public BigDecimal getCampusExceptOneOnOne() {
        return campusExceptOneOnOne;
    }

    public void setCampusExceptOneOnOne(BigDecimal campusExceptOneOnOne) {
        this.campusExceptOneOnOne = campusExceptOneOnOne;
    }

    public BigDecimal getUserExceptSmallClass() {
        return userExceptSmallClass;
    }

    public void setUserExceptSmallClass(BigDecimal userExceptSmallClass) {
        this.userExceptSmallClass = userExceptSmallClass;
    }

    public BigDecimal getCampusExceptSmallClass() {
        return campusExceptSmallClass;
    }

    public void setCampusExceptSmallClass(BigDecimal campusExceptSmallClass) {
        this.campusExceptSmallClass = campusExceptSmallClass;
    }

    public BigDecimal getUserExceptOneOnMany() {
        return userExceptOneOnMany;
    }

    public void setUserExceptOneOnMany(BigDecimal userExceptOneOnMany) {
        this.userExceptOneOnMany = userExceptOneOnMany;
    }

    public BigDecimal getCampusExceptOneOnMany() {
        return campusExceptOneOnMany;
    }

    public void setCampusExceptOneOnMany(BigDecimal campusExceptOneOnMany) {
        this.campusExceptOneOnMany = campusExceptOneOnMany;
    }

    public BigDecimal getUserExceptEcsClass() {
        return userExceptEcsClass;
    }

    public void setUserExceptEcsClass(BigDecimal userExceptEcsClass) {
        this.userExceptEcsClass = userExceptEcsClass;
    }

    public BigDecimal getCampusExceptEcsClass() {
        return campusExceptEcsClass;
    }

    public void setCampusExceptEcsClass(BigDecimal campusExceptEcsClass) {
        this.campusExceptEcsClass = campusExceptEcsClass;
    }

    public BigDecimal getUserExceptOthers() {
        return userExceptOthers;
    }

    public void setUserExceptOthers(BigDecimal userExceptOthers) {
        this.userExceptOthers = userExceptOthers;
    }

    public BigDecimal getCampusExceptOthers() {
        return campusExceptOthers;
    }

    public void setCampusExceptOthers(BigDecimal campusExceptOthers) {
        this.campusExceptOthers = campusExceptOthers;
    }

    public BigDecimal getUserExceptLecture() {
        return userExceptLecture;
    }

    public void setUserExceptLecture(BigDecimal userExceptLecture) {
        this.userExceptLecture = userExceptLecture;
    }

    public BigDecimal getCampusExceptLecture() {
        return campusExceptLecture;
    }

    public void setCampusExceptLecture(BigDecimal campusExceptLecture) {
        this.campusExceptLecture = campusExceptLecture;
    }

    public BigDecimal getUserExceptTwoTeacher() {
        return userExceptTwoTeacher;
    }

    public void setUserExceptTwoTeacher(BigDecimal userExceptTwoTeacher) {
        this.userExceptTwoTeacher = userExceptTwoTeacher;
    }

    public BigDecimal getCampusExceptTwoTeacher() {
        return campusExceptTwoTeacher;
    }

    public void setCampusExceptTwoTeacher(BigDecimal campusExceptTwoTeacher) {
        this.campusExceptTwoTeacher = campusExceptTwoTeacher;
    }
}
