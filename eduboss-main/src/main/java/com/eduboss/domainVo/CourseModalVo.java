package com.eduboss.domainVo;

import com.eduboss.common.WeekDay;

import java.util.TreeSet;

/**
 * Created by Administrator on 2017/7/6.
 */
public class CourseModalVo {
    private int id;
    private String modalName;  //名字
    private int techNum;    //讲数
    private String productYearId; //年份
    private String productSeasonId; //季节
    private TreeSet<WeekDay> courseWeek; //周数
    private String coursePhaseId; //期数
    private String branchId;  //分公司
    private String productYearName; //年份
    private String productSeasonName; //季节
    private String courseWeekName; //周数
    private String coursePhaseName; //期数
    private String branchName;  //分公司
    private int version;
    private TreeSet<String> courseDate;
    private String suffixName;//后缀
    private String classTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModalName() {
        return modalName;
    }

    public void setModalName(String modalName) {
        this.modalName = modalName;
    }

    public int getTechNum() {
        return techNum;
    }

    public void setTechNum(int techNum) {
        this.techNum = techNum;
    }

    public String getProductYearId() {
        return productYearId;
    }

    public void setProductYearId(String productYearId) {
        this.productYearId = productYearId;
    }

    public String getProductSeasonId() {
        return productSeasonId;
    }

    public void setProductSeasonId(String productSeasonId) {
        this.productSeasonId = productSeasonId;
    }

    public String getCoursePhaseId() {
        return coursePhaseId;
    }

    public void setCoursePhaseId(String coursePhaseId) {
        this.coursePhaseId = coursePhaseId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getProductYearName() {
        return productYearName;
    }

    public void setProductYearName(String productYearName) {
        this.productYearName = productYearName;
    }

    public String getProductSeasonName() {
        return productSeasonName;
    }

    public void setProductSeasonName(String productSeasonName) {
        this.productSeasonName = productSeasonName;
    }

    public String getCourseWeekName() {
        return courseWeekName;
    }

    public void setCourseWeekName(String courseWeekName) {
        this.courseWeekName = courseWeekName;
    }

    public String getCoursePhaseName() {
        return coursePhaseName;
    }

    public void setCoursePhaseName(String coursePhaseName) {
        this.coursePhaseName = coursePhaseName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public TreeSet<String> getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(TreeSet<String> courseDate) {
        this.courseDate = courseDate;
    }

    public TreeSet<WeekDay> getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(TreeSet<WeekDay> courseWeek) {
        this.courseWeek = courseWeek;
    }

    public String getSuffixName() {
        return suffixName;
    }

    public void setSuffixName(String suffixName) {
        this.suffixName = suffixName;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }
}
