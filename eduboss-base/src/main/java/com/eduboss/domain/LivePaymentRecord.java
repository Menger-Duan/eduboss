package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.LiveContactType;
import com.eduboss.common.LiveFinanceType;
import com.eduboss.dto.Response;

/**
 * 直播平台，咨询师新签现金流
 *
 */
@Entity
@Table(name = "live_payment_record")
public class LivePaymentRecord extends Response implements java.io.Serializable {

	// Fields

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
	private BigDecimal originalPrice = BigDecimal.ZERO;
	
	private String payType;
	
	private String reqsn;
	
	public LivePaymentRecord() {
		super();
	}
	


	public LivePaymentRecord(String id, String paymentDate, String studentName, String studentId, String studentContact,
			String userName, String userEmployeeNo, String userContact, String orderCampusId, BigDecimal totalAmount,
			BigDecimal paidAmount, BigDecimal campusAchievement, BigDecimal userAchievement, String orderNum,
			String courseName, LiveContactType contactType, LiveFinanceType financeType, BigDecimal originalPrice) {
		super();
		this.id = id;
		this.paymentDate = paymentDate;
		this.studentName = studentName;
		this.studentId = studentId;
		this.studentContact = studentContact;
		this.userName = userName;
		this.userEmployeeNo = userEmployeeNo;
		this.userContact = userContact;
		this.orderCampusId = orderCampusId;
		this.totalAmount = totalAmount;
		this.paidAmount = paidAmount;
		this.campusAchievement = campusAchievement;
		this.userAchievement = userAchievement;
		this.orderNum = orderNum;
		this.courseName = courseName;
		this.contactType = contactType;
		this.financeType = financeType;
		this.originalPrice = originalPrice;
	}



	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "payment_date", length = 20)
	public String getPaymentDate() {
		return paymentDate;
	}


	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	@Column(name = "student_name", length = 64)
	public String getStudentName() {
		return studentName;
	}


	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	@Column(name = "student_id", length = 32)
	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Column(name = "student_contact", length = 32)
	public String getStudentContact() {
		return studentContact;
	}


	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}

	@Column(name = "user_name", length = 64)
	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "user_employeeNo", length = 32)
	public String getUserEmployeeNo() {
		return userEmployeeNo;
	}


	public void setUserEmployeeNo(String userEmployeeNo) {
		this.userEmployeeNo = userEmployeeNo;
	}

	@Column(name = "user_contact", length = 15)
	public String getUserContact() {
		return userContact;
	}


	public void setUserContact(String userContact) {
		this.userContact = userContact;
	}
	@Column(name = "order_campusId", length = 32)
	public String getOrderCampusId() {
		return orderCampusId;
	}

	public void setOrderCampusId(String orderCampusId) {
		this.orderCampusId = orderCampusId;
	}

	@Column(name = "total_amount", precision = 10, scale = 2)
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}


	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "paid_amount", precision = 10, scale = 2)
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}


	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Column(name = "campus_achievement", precision = 10, scale = 2)
	public BigDecimal getCampusAchievement() {
		return campusAchievement;
	}


	public void setCampusAchievement(BigDecimal campusAchievement) {
		this.campusAchievement = campusAchievement;
	}

	@Column(name = "user_achievement", precision = 10, scale = 2)
	public BigDecimal getUserAchievement() {
		return userAchievement;
	}


	public void setUserAchievement(BigDecimal userAchievement) {
		this.userAchievement = userAchievement;
	}

	@Column(name = "order_num", length = 32)
	public String getOrderNum() {
		return orderNum;
	}


	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	@Column(name = "course_name", length = 150)
	public String getCourseName() {
		return courseName;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "contact_type")
	public LiveContactType getContactType() {
		return contactType;
	}

	public void setContactType(LiveContactType contactType) {
		this.contactType = contactType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "finance_type")
	public LiveFinanceType getFinanceType() {
		return financeType;
	}

	public void setFinanceType(LiveFinanceType financeType) {
		this.financeType = financeType;
	}

	@Column(name = "original_price", precision = 10, scale = 2)
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Column(name = "pay_type", length = 10)
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	@Column(name = "reqsn", length = 32)
	public String getReqsn() {
		return reqsn;
	}
	
	public void setReqsn(String reqsn) {
		this.reqsn = reqsn;
	}



	@Override
	public String toString() {
		return "LivePaymentRecord [id=" + id + ", paymentDate=" + paymentDate + ", studentName=" + studentName
				+ ", studentId=" + studentId + ", studentContact=" + studentContact + ", userName=" + userName
				+ ", userEmployeeNo=" + userEmployeeNo + ", userContact=" + userContact + ", orderCampusId="
				+ orderCampusId + ", totalAmount=" + totalAmount + ", paidAmount=" + paidAmount + ", campusAchievement="
				+ campusAchievement + ", userAchievement=" + userAchievement + ", orderNum=" + orderNum
				+ ", courseName=" + courseName + ", contactType=" + contactType + ", financeType=" + financeType
				+ ", originalPrice=" + originalPrice + ", payType=" + payType + ", reqsn=" + reqsn + "]";
	}


	
}