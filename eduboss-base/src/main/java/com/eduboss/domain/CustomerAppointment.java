package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.AppointmentType;
import com.eduboss.common.VisitType;

/**
 * CustomerAppointment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "customer_appointment")
public class CustomerAppointment implements java.io.Serializable {

	// Fields
	private String id;
	private Customer customer;
	private String meetingTime;
	private String appointmentTime;
	private User appointmentUser;
	private String meetingConfirmTime;
	private User meetingConfirmUser;
	private VisitType visitType;//  add by Yao 2015-04-20
	private AppointmentType appointmentType;//  add by Yao 2015-04-22   跟进 和预约的区分
	//for前端显示
//	private String customerName;
//	private String appointmentUserName;
//	private String meetingConfirmUserName;

	// Constructors

	/** default constructor */
	public CustomerAppointment() {
	}

	/** minimal constructor */
	public CustomerAppointment(String id) {
		this.id = id;
	}

	/** full constructor */
	public CustomerAppointment(String id, Customer customer,
			String meetingTime, String appointmentTime,
			User appointmentUser, String meetingConfirmTime,
			User meetingConfirmUser) {
		this.id = id;
		this.customer = customer;
		this.meetingTime = meetingTime;
		this.appointmentTime = appointmentTime;
		this.appointmentUser = appointmentUser;
		this.meetingConfirmTime = meetingConfirmTime;
		this.meetingConfirmUser = meetingConfirmUser;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Column(name = "MEETING_TIME", length = 20)
	public String getMeetingTime() {
		return this.meetingTime;
	}

	public void setMeetingTime(String meetingTime) {
		this.meetingTime = meetingTime;
	}

	@Column(name = "APPOINTMENT_TIME", length = 20)
	public String getAppointmentTime() {
		return this.appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APPOINTMENT_USER_ID")
	public User getAppointmentUser() {
		return this.appointmentUser;
	}

	public void setAppointmentUser(User appointmentUser) {
		this.appointmentUser = appointmentUser;
	}

	@Column(name = "MEETING_CONFIRM_TIME", length = 20)
	public String getMeetingConfirmTime() {
		return this.meetingConfirmTime;
	}

	public void setMeetingConfirmTime(String meetingConfirmTime) {
		this.meetingConfirmTime = meetingConfirmTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEETING_CONFIRM_USER_ID")
	public User getMeetingConfirmUser() {
		return this.meetingConfirmUser;
	}

	public void setMeetingConfirmUser(User meetingConfirmUser) {
		this.meetingConfirmUser = meetingConfirmUser;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "VISIT_TYPE", length = 32)
	public VisitType getVisitType() {
		return visitType;
	}

	public void setVisitType(VisitType visitType) {
		this.visitType = visitType;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "APPOINTMENT_TYPE", length = 32)
	public AppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(AppointmentType appointmentType) {
		this.appointmentType = appointmentType;
	}
	
	

//	/**
//	 * @return the customerName
//	 */
//	@Transient
//	public String getCustomerName() {
//		return customerName;
//	}
//
//	/**
//	 * @param customerName the customerName to set
//	 */
//	public void setCustomerName(String customerName) {
//		this.customerName = customerName;
//	}
//
//	/**
//	 * @return the appointmentUserName
//	 */
//	@Transient
//	public String getAppointmentUserName() {
//		return appointmentUserName;
//	}
//
//	/**
//	 * @param appointmentUserName the appointmentUserName to set
//	 */
//	public void setAppointmentUserName(String appointmentUserName) {
//		this.appointmentUserName = appointmentUserName;
//	}
//
//	/**
//	 * @return the meetingConfirmUserName
//	 */
//	@Transient
//	public String getMeetingConfirmUserName() {
//		return meetingConfirmUserName;
//	}
//
//	/**
//	 * @param meetingConfirmUserName the meetingConfirmUserName to set
//	 */
//	public void setMeetingConfirmUserName(String meetingConfirmUserName) {
//		this.meetingConfirmUserName = meetingConfirmUserName;
//	}

}