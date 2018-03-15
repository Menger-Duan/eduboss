package com.eduboss.domainVo;

import java.math.BigDecimal;


/**
 * @description 签单客户列表 VO
 * @date 2016-08-24
 * @author xiaojinwang
 *
 */
public class SignupCustomerVo {

	//客户姓名
	private String cusName;
	//客户ID
	private String cusId;
	//联系方式
	private String contact;
	//登记人
	private String createUserName;
	private String createCustomerUserId;
	//登记人部门
	private String createUserDept;
	//资源入口Id
	private String resEntranceId;
    //资源入口名称
	private String resEntranceName;
	//资源来源
	private String cusType;
	private String cusTypeName;
	//来源细分
	private String cusOrg;
	private String cusOrgName;
	
	
	//分配类型
	private String deliverType;
	//分配目标 就是跟进人userId
	private String deliverTarget;
	//分配目标名字 就是跟人姓名
	private String deliverTargetName;
	//跟进记录 也是最新跟进记录
	private String followupRemark;
	//所属分公司
	private String branchName; 
	//分公司的Id
	private String branchId;
	//所属校区
	private String blCampusName;
	//所属校区id	
	private String blCampusId;
	
	//第一份合同金额
	private BigDecimal firstContractMoney;
	//第一份合同时间
	private String firstContractTime; 
	//学生的id
	private String studentId;
	//学生姓名
	private String studentName;
	//学生年级
	private String gradeName;
	//学生年级Id
	private String gradeId;;
	//学管师名字
	private String studyManegerName;
	
		
	//学生姓名列表(用于显示)
	private String studentNameList;
	//就读学校id
	private String schoolId;
	//就读学校名称
	private String schoolName;	
	//就读学校名称列表(用于显示)
	private String schoolNameList;
	//学生状态
	private String studentStatus;
	//学生状态名字
	private String studentStatusName;
	
	
	//客户跟进状态
	private String dealStatusName;
	
	//客户是否签合同
	private String isSignContract;
	// 最后一次跟进时间
	private String lastFollowUpTime; 
	//客户录入时间
	private String createTime;
	
	//客户学生最近签单日期和现在时间相隔天数
	private Integer days;
	
	//#1728 计算网络分配时间  duanmenrun 20171110
	private String allocateTime;
	private String obtainTime;
	private String outTime;
	
	private String firstCampusName;
	
	public String getCusName() {
		return cusName;
	}
	public void setCusName(String cusName) {
		this.cusName = cusName;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getCreateCustomerUserId() {
		return createCustomerUserId;
	}
	public void setCreateCustomerUserId(String createCustomerUserId) {
		this.createCustomerUserId = createCustomerUserId;
	}
	public String getCreateUserDept() {
		return createUserDept;
	}
	public void setCreateUserDept(String createUserDept) {
		this.createUserDept = createUserDept;
	}
	public String getResEntranceId() {
		return resEntranceId;
	}
	public void setResEntranceId(String resEntranceId) {
		this.resEntranceId = resEntranceId;
	}
	public String getResEntranceName() {
		return resEntranceName;
	}
	public void setResEntranceName(String resEntranceName) {
		this.resEntranceName = resEntranceName;
	}
	public String getCusType() {
		return cusType;
	}
	public void setCusType(String cusType) {
		this.cusType = cusType;
	}
	public String getCusOrg() {
		return cusOrg;
	}
	public void setCusOrg(String cusOrg) {
		this.cusOrg = cusOrg;
	}
	public String getDeliverType() {
		return deliverType;
	}
	public void setDeliverType(String deliverType) {
		this.deliverType = deliverType;
	}
	public String getDeliverTarget() {
		return deliverTarget;
	}
	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	public String getDeliverTargetName() {
		return deliverTargetName;
	}
	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	public String getFollowupRemark() {
		return followupRemark;
	}
	public void setFollowupRemark(String followupRemark) {
		this.followupRemark = followupRemark;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public BigDecimal getFirstContractMoney() {
		return firstContractMoney;
	}
	public void setFirstContractMoney(BigDecimal firstContractMoney) {
		this.firstContractMoney = firstContractMoney;
	}
	public String getFirstContractTime() {
		return firstContractTime;
	}
	public void setFirstContractTime(String firstContractTime) {
		this.firstContractTime = firstContractTime;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getStudyManegerName() {
		return studyManegerName;
	}
	public void setStudyManegerName(String studyManegerName) {
		this.studyManegerName = studyManegerName;
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
	public String getStudentStatus() {
		return studentStatus;
	}
	public void setStudentStatus(String studentStatus) {
		this.studentStatus = studentStatus;
	}
	public String getSchoolNameList() {
		return schoolNameList;
	}
	public void setSchoolNameList(String schoolNameList) {
		this.schoolNameList = schoolNameList;
	}
	public String getDealStatusName() {
		return dealStatusName;
	}
	public void setDealStatusName(String dealStatusName) {
		this.dealStatusName = dealStatusName;
	}
	public String getStudentStatusName() {
		return studentStatusName;
	}
	public void setStudentStatusName(String studentStatusName) {
		this.studentStatusName = studentStatusName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCusTypeName() {
		return cusTypeName;
	}
	public void setCusTypeName(String cusTypeName) {
		this.cusTypeName = cusTypeName;
	}
	public String getCusOrgName() {
		return cusOrgName;
	}
	public void setCusOrgName(String cusOrgName) {
		this.cusOrgName = cusOrgName;
	}
	public String getIsSignContract() {
		return isSignContract;
	}
	public void setIsSignContract(String isSignContract) {
		this.isSignContract = isSignContract;
	}
	public String getLastFollowUpTime() {
		return lastFollowUpTime;
	}
	public void setLastFollowUpTime(String lastFollowUpTime) {
		this.lastFollowUpTime = lastFollowUpTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getAllocateTime() {
		return allocateTime;
	}
	public void setAllocateTime(String allocateTime) {
		this.allocateTime = allocateTime;
	}
	public String getObtainTime() {
		return obtainTime;
	}
	public void setObtainTime(String obtainTime) {
		this.obtainTime = obtainTime;
	}
	public String getOutTime() {
		return outTime;
	}
	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}
	public String getFirstCampusName() {
		return firstCampusName;
	}
	public void setFirstCampusName(String firstCampusName) {
		this.firstCampusName = firstCampusName;
	}
	
	
}
