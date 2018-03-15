package com.eduboss.domainVo;

public class AuditAutoRecogVo {
	
	private String startDate;
	private String endDate;
	private String subject;
    private String campusId;
	private String currentRoleId;
	
	public AuditAutoRecogVo(){}

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public String getCurrentRoleId() {
		return currentRoleId;
	}

	public void setCurrentRoleId(String currentRoleId) {
		this.currentRoleId = currentRoleId;
	}
	
}
