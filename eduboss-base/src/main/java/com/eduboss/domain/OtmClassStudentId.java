package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MiniClassStudentId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class OtmClassStudentId implements java.io.Serializable {

	// Fields

	private String otmClassId;
	private String studentId;

	// Constructors

	/** default constructor */
	public OtmClassStudentId() {
	}

	/** full constructor */
	public OtmClassStudentId(String otmClassId, String studentId) {
		this.otmClassId = otmClassId;
		this.studentId = studentId;
	}

	// Property accessors

	@Column(name = "OTM_CLASS_ID", nullable = false, length = 32)
	public String getOtmClassId() {
		return this.otmClassId;
	}

	public void setOtmClassId(String otmClassId) {
		this.otmClassId = otmClassId;
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
		if (!(other instanceof OtmClassStudentId))
			return false;
		OtmClassStudentId castOther = (OtmClassStudentId) other;

		return ((this.getOtmClassId() == castOther.getOtmClassId()) || (this.getOtmClassId() != null
				&& castOther.getOtmClassId() != null && this.getOtmClassId().equals(castOther.getOtmClassId())))
				&& ((this.getStudentId() == castOther.getStudentId()) || (this.getStudentId() != null && castOther.getStudentId() != null && this
						.getStudentId().equals(castOther.getStudentId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getOtmClassId() == null ? 0 : this.getOtmClassId ().hashCode());
		result = 37 * result + (getStudentId() == null ? 0 : this.getStudentId().hashCode());
		return result;
	}

}