package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.eduboss.common.LiveContactType;
import com.eduboss.common.LiveFinanceType;
import com.eduboss.domain.LiveCourseDetail;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true) 
public class LivePaymentRecordVo {

	private String id;
	private String paymentDate;
	private String studentName;
	private String studentId;
	private String studentContact;
	private String userName;
	private String userEmployeeNo;
	private String userContact;
	private String orderCampusId;
	
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private BigDecimal paidAmount = BigDecimal.ZERO;
	private BigDecimal campusAchievement = BigDecimal.ZERO;
	private BigDecimal userAchievement = BigDecimal.ZERO;
	
	private String orderNum;
	private String courseName;
	private LiveContactType contactType;
	private LiveFinanceType financeType;
	private String group;
	private List<LiveCourseResponseVo> courseList; 
	
	private String orderCampusName;
	private String contactTypeName;
	private String financeTypeName;
	
	private List<LiveCourseDetail> courseDetails; 
	
	private String uniqueId ;
	
	private BigDecimal originalPrice = BigDecimal.ZERO;
	
	private String transactionNum;
	
	private String payType;
	
	private String reqsn;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentContact() {
		return studentContact;
	}
	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmployeeNo() {
		return userEmployeeNo;
	}
	public void setUserEmployeeNo(String userEmployeeNo) {
		this.userEmployeeNo = userEmployeeNo;
	}
	public String getUserContact() {
		return userContact;
	}
	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	public String getOrderCampusId() {
		return orderCampusId;
	}
	public void setOrderCampusId(String orderCampusId) {
		this.orderCampusId = orderCampusId;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public BigDecimal getCampusAchievement() {
		return campusAchievement;
	}
	public void setCampusAchievement(BigDecimal campusAchievement) {
		this.campusAchievement = campusAchievement;
	}
	public BigDecimal getUserAchievement() {
		return userAchievement;
	}
	public void setUserAchievement(BigDecimal userAchievement) {
		this.userAchievement = userAchievement;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public LiveContactType getContactType() {
		return contactType;
	}
	public void setContactType(LiveContactType contactType) {
		this.contactType = contactType;
	}
	public LiveFinanceType getFinanceType() {
		return financeType;
	}
	public void setFinanceType(LiveFinanceType financeType) {
		this.financeType = financeType;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	public List<LiveCourseResponseVo> getCourseList() {
		return courseList;
	}
	public void setCourseList(List<LiveCourseResponseVo> courseList) {
		this.courseList = courseList;
	}
	public String getOrderCampusName() {
		return orderCampusName;
	}
	public void setOrderCampusName(String orderCampusName) {
		this.orderCampusName = orderCampusName;
	}
	public String getContactTypeName() {
		return contactTypeName;
	}
	public void setContactTypeName(String contactTypeName) {
		this.contactTypeName = contactTypeName;
	}
	public String getFinanceTypeName() {
		return financeTypeName;
	}
	public void setFinanceTypeName(String financeTypeName) {
		this.financeTypeName = financeTypeName;
	}
	public List<LiveCourseDetail> getCourseDetails() {
		return courseDetails;
	}
	public void setCourseDetails(List<LiveCourseDetail> courseDetails) {
		this.courseDetails = courseDetails;
	}

	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}
	
	public String getTransactionNum() {
		return transactionNum;
	}
	public void setTransactionNum(String transactionNum) {
		this.transactionNum = transactionNum;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getReqsn() {
		return reqsn;
	}
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}
	@Override
	public String toString() {
		return "LivePaymentRecordVo [id=" + id + ", paymentDate=" + paymentDate + ", studentName=" + studentName
				+ ", studentId=" + studentId + ", studentContact=" + studentContact + ", userName=" + userName
				+ ", userEmployeeNo=" + userEmployeeNo + ", userContact=" + userContact + ", orderCampusId="
				+ orderCampusId + ", totalAmount=" + totalAmount + ", paidAmount=" + paidAmount + ", campusAchievement="
				+ campusAchievement + ", userAchievement=" + userAchievement + ", orderNum=" + orderNum
				+ ", courseName=" + courseName + ", contactType=" + contactType + ", financeType=" + financeType
				+ ", group=" + group + ", courseList=" + courseList + ", orderCampusName=" + orderCampusName
				+ ", contactTypeName=" + contactTypeName + ", financeTypeName=" + financeTypeName + ", courseDetails="
				+ courseDetails + ", uniqueId=" + uniqueId + ", originalPrice=" + originalPrice + ", transactionNum="
				+ transactionNum + ", payType=" + payType + ", reqsn=" + reqsn + "]";
	}
	
	
	
}
