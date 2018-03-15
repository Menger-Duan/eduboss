package com.eduboss.domainVo;

import com.eduboss.common.AppointmentType;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.VisitType;

public class CustomerFolowupVo {
	
	private String id;
	private String customerId;
	private String customerName;
	private String remark;
	private String createTime;
	private String createUserId;
	private String createUserName;//记录的创建者  如果是预约记录 就是预约人 如果是跟进记录  就是跟进人
	private String satisficing;
	private String satisficingName;
	// 客户意向度分级
	private String intentionOfTheCustomerId; 
	private String intentionOfTheCustomerName;
	//意向科目
	private String intentionSubjectId;   
	private String intentionSubjectName;   
	//购买力分级
	private String purchasingPowerId;     
	private String purchasingPowerName;
	
	private String meetingTime;
	private String meetingConfirmTime;
	private String meetingConfirmUserId;
	private String meetingConfirmUserName;
	private String appointCampusId;   //预约校区Id
	private String appointCampusName;//预约校区
	private VisitType visitType;//  add by Yao 2015-04-20
	private AppointmentType appointmentType;//  add by Yao 2015-04-22   跟进 和预约的区分
	
	private String appointmentUserJobId;  // add by tangyuping 2015-11-17
	private String appointmentUserJobName; //预约人职位
	

	private String meetingTimeEnd;
	private String meetingTimeStart;
	
	private String recentlyTime;//客户最近跟进时间
	private String recentlyRemark; //客户最新跟进备注
	
	
	private String cusMobile;// 客户电话   add by Yao 2015-04-21
	
	private String followStuId;
	private String followStuName;  //跟进学生
	private String getOneAppointment; //标记，获取最新一次预约信息
	
	
	private String studentName; //学生姓名  

	private String studentNameList;	//学生姓名列表(用于多个学生显示)

	private String schoolId;	//就读学校id
	
	private String schoolName; //就读学校名称

	private String schoolNameList;	//学校名字列表(用于显示)
	
	private String gradeId;//学生年级Id

	private String gradeNameList;//学生年级名字
	
	private String isAppointCome;//是否上门  1上门 0 没上门
	
	//private String dealStatus;//用于网络校区的分配校区  
	
	private String agencyUserId;
	private String agencyUserName;
	
	private CustomerDealStatus dealStatus;
	private String dealStatusName;
	
	private String appointBranch;
	
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getSatisficing() {
		return satisficing;
	}
	public void setSatisficing(String satisficing) {
		this.satisficing = satisficing;
	}
	public String getSatisficingName() {
		return satisficingName;
	}
	public void setSatisficingName(String satisficingName) {
		this.satisficingName = satisficingName;
	}
	public String getIntentionOfTheCustomerId() {
		return intentionOfTheCustomerId;
	}

	public void setIntentionOfTheCustomerId(String intentionOfTheCustomerId) {
		this.intentionOfTheCustomerId = intentionOfTheCustomerId;
	}

	public String getIntentionOfTheCustomerName() {
		return intentionOfTheCustomerName;
	}

	public void setIntentionOfTheCustomerName(String intentionOfTheCustomerName) {
		this.intentionOfTheCustomerName = intentionOfTheCustomerName;
	}

	public String getIntentionSubjectId() {
		return intentionSubjectId;
	}

	public void setIntentionSubjectId(String intentionSubjectId) {
		this.intentionSubjectId = intentionSubjectId;
	}

	public String getIntentionSubjectName() {
		return intentionSubjectName;
	}

	public void setIntentionSubjectName(String intentionSubjectName) {
		this.intentionSubjectName = intentionSubjectName;
	}

	public String getPurchasingPowerId() {
		return purchasingPowerId;
	}

	public void setPurchasingPowerId(String purchasingPowerId) {
		this.purchasingPowerId = purchasingPowerId;
	}

	public String getPurchasingPowerName() {
		return purchasingPowerName;
	}

	public void setPurchasingPowerName(String purchasingPowerName) {
		this.purchasingPowerName = purchasingPowerName;
	}
	public String getMeetingTime() {
		return meetingTime;
	}
	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
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
	public String getAppointCampusId() {
		return appointCampusId;
	}
	public void setAppointCampusId(String appointCampusId) {
		this.appointCampusId = appointCampusId;
	}
	public String getAppointCampusName() {
		return appointCampusName;
	}
	public void setAppointCampusName(String appointCampusName) {
		this.appointCampusName = appointCampusName;
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
	public String getCusMobile() {
		return cusMobile;
	}
	public void setCusMobile(String cusMobile) {
		this.cusMobile = cusMobile;
	}
	public VisitType getVisitType() {
		return visitType;
	}
	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}
	public AppointmentType getAppointmentType() {
		return appointmentType;
	}
	public void setAppointmentType(AppointmentType appointmentType) {
		this.appointmentType = appointmentType;
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
	public String getRecentlyTime() {
		return recentlyTime;
	}
	public void setRecentlyTime(String recentlyTime) {
		this.recentlyTime = recentlyTime;
	}
	public String getRecentlyRemark() {
		return recentlyRemark;
	}
	public void setRecentlyRemark(String recentlyRemark) {
		this.recentlyRemark = recentlyRemark;
	}
	public String getFollowStuId() {
		return followStuId;
	}
	public void setFollowStuId(String followStuId) {
		this.followStuId = followStuId;
	}
	public String getFollowStuName() {
		return followStuName;
	}
	public void setFollowStuName(String followStuName) {
		this.followStuName = followStuName;
	}
	public String getGetOneAppointment() {
		return getOneAppointment;
	}
	public void setGetOneAppointment(String getOneAppointment) {
		this.getOneAppointment = getOneAppointment;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentNameList() {
		return studentNameList;
	}
	public void setStudentNameList(String studentNameList) {
		this.studentNameList = studentNameList;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getSchoolNameList() {
		return schoolNameList;
	}
	public void setSchoolNameList(String schoolNameList) {
		this.schoolNameList = schoolNameList;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeNameList() {
		return gradeNameList;
	}
	public void setGradeNameList(String gradeNameList) {
		this.gradeNameList = gradeNameList;
	}
	public String getIsAppointCome() {
		return isAppointCome;
	}
	public void setIsAppointCome(String isAppointCome) {
		this.isAppointCome = isAppointCome;
	}

	
	public String getAgencyUserId() {
		return agencyUserId;
	}
	public void setAgencyUserId(String agencyUserId) {
		this.agencyUserId = agencyUserId;
	}
	public String getAgencyUserName() {
		return agencyUserName;
	}
	public void setAgencyUserName(String agencyUserName) {
		this.agencyUserName = agencyUserName;
	}
	public CustomerDealStatus getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(CustomerDealStatus dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getDealStatusName() {
		return dealStatusName;
	}
	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}
	public String getAppointBranch() {
		return appointBranch;
	}
	public void setAppointBranch(String appointBranch) {
		this.appointBranch = appointBranch;
	}
	
	
	
	
	
}
