package com.eduboss.domainVo;

import com.eduboss.common.AppointmentType;

public class CustomerAppointmentVo {
	
	private String id;
	private String customerId;
	private String customerName;
	private String deliverTargetId;
	private String meetingTime;
	private String appointmentTime;
	private String appointmentUserId;
	private String appointmentUserName;
	private String meetingConfirmTime;
	private String meetingConfirmUserId;
	private String meetingConfirmUserName;
	private String blSchool;
	private String visitType;// 访问类型   add by Yao 2015-04-20
	private String visitTypeName;// 访问类型   add by Yao 2015-04-20
	private String cusMobile;// 客户电话   add by Yao 2015-04-21
	private String appointmentType;//  add by Yao 2015-04-22   跟进 和预约的区分
	private String appointmentTypeName;//  add by Yao 2015-04-22   跟进 和预约的区分
	
	private String appointmentUserJobId;  // add by tangyuping 2015-11-17
	private String appointmentUserJobName;
	private String meetingTimeEnd;
	private String meetingTimeStart;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getMeetingTime() {
		return meetingTime;
	}
	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public String getAppointmentUserId() {
		return appointmentUserId;
	}
	public void setAppointmentUserId(String appointmentUserId) {
		this.appointmentUserId = appointmentUserId;
	}
	public String getAppointmentUserName() {
		return appointmentUserName;
	}
	public void setAppointmentUserName(String appointmentUserName) {
		this.appointmentUserName = appointmentUserName;
	}
	public String getMeetingConfirmTime() {
		return meetingConfirmTime;
	}
	public void setMeetingConfirmTime(String meetingConfirmTime) {
		this.meetingConfirmTime = meetingConfirmTime;
	}
	public String getMeetingConfirmUserId() {
		return meetingConfirmUserId;
	}
	public void setMeetingConfirmUserId(String meetingConfirmUserId) {
		this.meetingConfirmUserId = meetingConfirmUserId;
	}
	public String getMeetingConfirmUserName() {
		return meetingConfirmUserName;
	}
	public void setMeetingConfirmUserName(String meetingConfirmUserName) {
		this.meetingConfirmUserName = meetingConfirmUserName;
	}
	public String getBlSchool() {
		return blSchool;
	}
	public void setBlSchool(String blSchool) {
		this.blSchool = blSchool;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	public String getCusMobile() {
		return cusMobile;
	}
	public void setCusMobile(String cusMobile) {
		this.cusMobile = cusMobile;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public String getAppointmentType() {
		return appointmentType;
	}
	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}
	public String getAppointmentTypeName() {
		return appointmentTypeName;
	}
	public void setAppointmentTypeName(String appointmentTypeName) {
		this.appointmentTypeName = appointmentTypeName;
	}
	public String getAppointmentUserJobId() {
		return appointmentUserJobId;
	}
	public void setAppointmentUserJobId(String appointmentUserJobId) {
		this.appointmentUserJobId = appointmentUserJobId;
	}
	public String getAppointmentUserJobName() {
		return appointmentUserJobName;
	}
	public void setAppointmentUserJobName(String appointmentUserJobName) {
		this.appointmentUserJobName = appointmentUserJobName;
	}
	public String getDeliverTargetId() {
		return deliverTargetId;
	}
	public void setDeliverTargetId(String deliverTargetId) {
		this.deliverTargetId = deliverTargetId;
	}
	public String getMeetingTimeEnd() {
		return meetingTimeEnd;
	}
	public void setMeetingTimeEnd(String meetingTimeEnd) {
		this.meetingTimeEnd = meetingTimeEnd;
	}
	public String getMeetingTimeStart() {
		return meetingTimeStart;
	}
	public void setMeetingTimeStart(String meetingTimeStart) {
		this.meetingTimeStart = meetingTimeStart;
	}

	
	
	
}
