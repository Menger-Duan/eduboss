package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * MiniClassStudentId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class MiniClassStudentId implements java.io.Serializable {

	// Fields

	private String miniClassId;
	private String studentId;

	// Constructors

	/** default constructor */
	public MiniClassStudentId() {
	}

	/** full constructor */
	public MiniClassStudentId(String miniClassId, String studentId) {
		this.miniClassId = miniClassId;
		this.studentId = studentId;
	}

	// Property accessors

	@Column(name = "MINI_CLASS_ID", nullable = false, length = 32)
	public String getMiniClassId() {
		return this.miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
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
		if (!(other instanceof MiniClassStudentId))
			return false;
		MiniClassStudentId castOther = (MiniClassStudentId) other;

		return ((this.getMiniClassId() == castOther.getMiniClassId()) || (this.getMiniClassId() != null
				&& castOther.getMiniClassId() != null && this.getMiniClassId().equals(castOther.getMiniClassId())))
				&& ((this.getStudentId() == castOther.getStudentId()) || (this.getStudentId() != null && castOther.getStudentId() != null && this
						.getStudentId().equals(castOther.getStudentId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getMiniClassId() == null ? 0 : this.getMiniClassId().hashCode());
		result = 37 * result + (getStudentId() == null ? 0 : this.getStudentId().hashCode());
		return result;
	}

}