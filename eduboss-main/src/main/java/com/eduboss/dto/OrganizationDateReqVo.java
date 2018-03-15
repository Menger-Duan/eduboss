package com.eduboss.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 机构日期请求Vo
 * @author arvin
 *
 */
public class OrganizationDateReqVo {

    private String branchId;
    
    private String campusId;
    
    private String startDate;
    
    private String endDate;
    
    private String startTime;
    
    private String endTime;

    /**
     * 分公司编号
     * @return
     */
    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    /**
     * 校区编号
     * @return
     */
    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    /**
     * 开始日期
     * @return
     */
    @NotBlank(message = "开始日期不能为空")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 结束日期
     * @return
     */
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 开始时间
     * @return
     */
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 结束时间
     * @return
     */
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "OrganizationDateRequestVo [branchId="
                + branchId + ", campusId=" + campusId + ", startDate="
                + startDate + ", endDate=" + endDate + ", startTime="
                + startTime + ", endTime=" + endTime + "]";
    }
    
}
