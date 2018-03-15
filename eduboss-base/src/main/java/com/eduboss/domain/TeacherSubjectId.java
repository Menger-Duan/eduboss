package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * TeacherSubjectId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
public class TeacherSubjectId implements java.io.Serializable {

	// Fields

	private String id;
	private String subjet;
	private String grade;

	// Constructors

	/** default constructor */
	public TeacherSubjectId() {
	}

	/** full constructor */
	public TeacherSubjectId(String id, String subjet, String grade) {
		this.id = id;
		this.subjet = subjet;
		this.grade = grade;
	}

	// Property accessors

	@Column(name = "ID", nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "SUBJET", nullable = false, length = 32)
	public String getSubjet() {
		return this.subjet;
	}

	public void setSubjet(String subjet) {
		this.subjet = subjet;
	}

	@Column(name = "GRADE", nullable = false, length = 32)
	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@Transient
	public String getIdString () {
		return this.getId() + "-" + this.getGrade() + "-" + this.getSubjet();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof TeacherSubjectId))
			return false;
		TeacherSubjectId castOther = (TeacherSubjectId) other;

		return ((this.getId() == castOther.getId()) || (this.getId() != null && castOther.getId() != null && this.getId().equals(
				castOther.getId())))
				&& ((this.getSubjet() == castOther.getSubjet()) || (this.getSubjet() != null && castOther.getSubjet() != null && this
						.getSubjet().equals(castOther.getSubjet())))
				&& ((this.getGrade() == castOther.getGrade()) || (this.getGrade() != null && castOther.getGrade() != null && this
						.getGrade().equals(castOther.getGrade())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
		result = 37 * result + (getSubjet() == null ? 0 : this.getSubjet().hashCode());
		result = 37 * result + (getGrade() == null ? 0 : this.getGrade().hashCode());
		return result;
	}

}