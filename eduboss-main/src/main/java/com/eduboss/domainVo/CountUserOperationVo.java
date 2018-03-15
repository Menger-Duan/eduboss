package com.eduboss.domainVo;

import com.eduboss.common.MonitorSubject;
import com.eduboss.common.MonitorUiSubject;
import com.eduboss.common.OrganizationType;
import com.eduboss.domain.User;

public class CountUserOperationVo {

	private String id;
	private User user;
	private String userId;
	private String teacherId;
	private String teacherName;
	private String userName;
	private MonitorUiSubject monitorUiSubject;
	private MonitorSubject monitorSubject;
	private String monitorSubjectName;
	private String countDate;
	private Double countQuantity;
	private String recordTime;
	private String startDate;
	private String endDate;
	private String blCampusId;
	private OrganizationType organizationType;
	private String organizationId;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public MonitorSubject getMonitorSubject() {
		return monitorSubject;
	}
	public void setMonitorSubject(MonitorSubject monitorSubject) {
		this.monitorSubject = monitorSubject;
	}
	public String getMonitorSubjectName() {
		return monitorSubjectName;
	}
	public void setMonitorSubjectName(String monitorSubjectName) {
		this.monitorSubjectName = monitorSubjectName;
	}
	public String getCountDate() {
		return countDate;
	}
	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}
	public Double getCountQuantity() {
		return countQuantity;
	}
	public void setCountQuantity(Double countQuantity) {
		this.countQuantity = countQuantity;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
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
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public MonitorUiSubject getMonitorUiSubject() {
		return monitorUiSubject;
	}
	public void setMonitorUiSubject(MonitorUiSubject monitorUiSubject) {
		this.monitorUiSubject = monitorUiSubject;
	}
	public OrganizationType getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	
}
