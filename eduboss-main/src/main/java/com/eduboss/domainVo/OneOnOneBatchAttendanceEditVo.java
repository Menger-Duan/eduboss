package com.eduboss.domainVo;

import java.util.List;

public class OneOnOneBatchAttendanceEditVo {

	private Double realHours;
	private Double staduyManagerAuditHours;
	private String courseId;
	private String operteType;
	private String ids;
	private String courseTime;
	private String targetParams;
	private List<OneOnOneBatchAttendanceEditVo> vos;
	private String courseAttenceType;
	private String courseVersion;
	private String attendanceDetail;
	
	public List<OneOnOneBatchAttendanceEditVo> getVos() {
		return vos;
	}
	public void setVos(List<OneOnOneBatchAttendanceEditVo> vos) {
		this.vos = vos;
	}
	public Double getRealHours() {
		return realHours;
	}
	public void setRealHours(Double realHours) {
		this.realHours = realHours;
	}
	public Double getStaduyManagerAuditHours() {
		return staduyManagerAuditHours;
	}
	public void setStaduyManagerAuditHours(Double staduyManagerAuditHours) {
		this.staduyManagerAuditHours = staduyManagerAuditHours;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getOperteType() {
		return operteType;
	}
	public void setOperteType(String operteType) {
		this.operteType = operteType;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getTargetParams() {
		return targetParams;
	}
	public void setTargetParams(String targetParams) {
		this.targetParams = targetParams;
	}
	public String getCourseAttenceType() {
		return courseAttenceType;
	}
	public void setCourseAttenceType(String courseAttenceType) {
		this.courseAttenceType = courseAttenceType;
	}
	public String getCourseVersion() {
		return courseVersion;
	}
	public void setCourseVersion(String courseVersion) {
		this.courseVersion = courseVersion;
	}
	public String getAttendanceDetail() {
		return attendanceDetail;
	}
	public void setAttendanceDetail(String attendanceDetail) {
		this.attendanceDetail = attendanceDetail;
	}
	
	
}
