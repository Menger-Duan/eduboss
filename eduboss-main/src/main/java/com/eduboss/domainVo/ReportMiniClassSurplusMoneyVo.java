package com.eduboss.domainVo;

import com.eduboss.common.MiniClassSurplusMoneyType;
import com.eduboss.common.MiniClassStatus;

public class ReportMiniClassSurplusMoneyVo {

	private String countDate;
	private String blCampusId;
	private MiniClassSurplusMoneyType miniClassSurplusMoneyType;
	private MiniClassStatus courseStatus;
	private Boolean isAllCourseStatus;
	private String startDate;
	private String endDate;




	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public MiniClassSurplusMoneyType getMiniClassSurplusMoneyType() {
		return miniClassSurplusMoneyType;
	}
	public void setMiniClassSurplusMoneyType(
			MiniClassSurplusMoneyType miniClassSurplusMoneyType) {
		this.miniClassSurplusMoneyType = miniClassSurplusMoneyType;
	}

	public MiniClassStatus getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(MiniClassStatus courseStatus) {
		this.courseStatus = courseStatus;
	}

	public Boolean getIsAllCourseStatus() {
		return isAllCourseStatus;
	}

	public void setIsAllCourseStatus(Boolean isAllCourseStatus) {
		this.isAllCourseStatus = isAllCourseStatus;
	}
}
