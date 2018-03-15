package com.eduboss.dto;


public class PromiseChargeSearchDto {

    private String startDate;

    private String endDate;

    private String season;

    private String miniClassName;

    private String attendanceStatus;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getMiniClassName() {
        return miniClassName;
    }

    public void setMiniClassName(String miniClassName) {
        this.miniClassName = miniClassName;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
}
