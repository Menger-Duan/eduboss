package com.eduboss.domain;

import javax.persistence.*;

@Entity
@Table(name = "TWO_TEACHER_BRENCH")
public class TwoTeacherClassBrench implements java.io.Serializable {

	private int id;
	private TwoTeacherClass classId;
	private Organization brenchId;



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "CLASS_ID")
	public TwoTeacherClass getClassId() {
		return classId;
	}

	public void setClassId(TwoTeacherClass classId) {
		this.classId = classId;
	}



	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BRENCH_ID")
	public Organization getBrenchId() {
		return brenchId;
	}

	public void setBrenchId(Organization brenchId) {
		this.brenchId = brenchId;
	}
}