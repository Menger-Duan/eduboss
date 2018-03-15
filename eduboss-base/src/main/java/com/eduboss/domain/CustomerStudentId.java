package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * CustomerStudentId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class CustomerStudentId implements java.io.Serializable {

	// Fields

	private String customerId;
	private String studentId;

	// Constructors

	/** default constructor */
	public CustomerStudentId() {
	}

	/** full constructor */
	public CustomerStudentId(String customerId, String studentId) {
		this.customerId = customerId;
		this.studentId = studentId;
	}

	// Property accessors

	@Column(name = "CUSTOMER_ID", nullable = false, length = 32)
	public String getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Column(name = "STUDENT_ID", nullable = false, length = 32)
	public String getStudentId() {
		return this.studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CustomerStudentId))
			return false;
		CustomerStudentId castOther = (CustomerStudentId) other;

		return ((this.getCustomerId() == castOther.getCustomerId()) || (this
				.getCustomerId() != null && castOther.getCustomerId() != null && this
				.getCustomerId().equals(castOther.getCustomerId())))
				&& ((this.getStudentId() == castOther.getStudentId()) || (this
						.getStudentId() != null
						&& castOther.getStudentId() != null && this
						.getStudentId().equals(castOther.getStudentId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCustomerId() == null ? 0 : this.getCustomerId()
						.hashCode());
		result = 37 * result
				+ (getStudentId() == null ? 0 : this.getStudentId().hashCode());
		return result;
	}

}