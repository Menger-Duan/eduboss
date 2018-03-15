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

import com.eduboss.common.MonitorSubject;

/**
 * CountUserOperation entity. @author MyEclipse Persistence Tools
 */

@Entity
@Table(name = "COUNT_USER_OPERATION")
public class CountUserOperation implements java.io.Serializable {

	// Fields

	private String id;
	private User user;
	private MonitorSubject monitorSubject;
	private String countDate;
	private Double countQuantity;
	private String recordTime;

	// Constructors

	/** default constructor */
	public CountUserOperation() {
	}

	/** full constructor */
	public CountUserOperation(String id, User user, MonitorSubject monitorSubject,
			String countDate, Double countQuantity, String recordTime) {
		this.id = id;
		this.user = user;
		this.monitorSubject = monitorSubject;
		this.countDate = countDate;
		this.countQuantity = countQuantity;
		this.recordTime = recordTime;
	}

	// Property accessors

	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MONITOR_SUBJECT", length = 32)
	public MonitorSubject getMonitorSubject() {
		return this.monitorSubject;
	}

	public void setMonitorSubject(MonitorSubject monitorSubject) {
		this.monitorSubject = monitorSubject;
	}

	@Column(name = "COUNT_DATE", length = 10)
	public String getCountDate() {
		return this.countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	@Column(name = "COUNT_QUANTITY", precision = 9)
	public Double getCountQuantity() {
		return this.countQuantity;
	}

	public void setCountQuantity(Double countQuantity) {
		this.countQuantity = countQuantity;
	}

	@Column(name = "RECORD_TIME", length = 20)
	public String getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

}