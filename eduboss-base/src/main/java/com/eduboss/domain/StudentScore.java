package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * Studentscoremanage entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDENTSCOREMANAGE")
public class StudentScore implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2073515146230568227L;
	private String id;
	private Student student;
	private DataDict grade;
	private DataDict subject;
	private BigDecimal score;
	private String time;
	private DataDict typeExam;
	private String classRange;
	private String gradeRange;
	private String createUserId;
	private String createTime;
	private String modifyTime;
	private String modifyUserId;
	private String remark;

	// Constructors

	/** default constructor */
	public StudentScore() {
	}

	/** minimal constructor */
	public StudentScore(String id) {
		this.id = id;
	}

	
	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id")
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return this.grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@Column(name = "SCORE" , precision = 10)
	public BigDecimal getScore() {
		return this.score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	@Column(name = "time", length = 128)
	public String getTime() {
		return this.time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TYPE_EXAM")
	public DataDict getTypeExam() {
		return this.typeExam;
	}

	public void setTypeExam(DataDict typeExam) {
		this.typeExam = typeExam;
	}

	@Column(name = "class_range", length = 64)
	public String getClassRange() {
		return this.classRange;
	}

	public void setClassRange(String classRange) {
		this.classRange = classRange;
	}

	@Column(name = "grade_range", length = 64)
	public String getGradeRange() {
		return this.gradeRange;
	}

	public void setGradeRange(String gradeRange) {
		this.gradeRange = gradeRange;
	}

	@Column(name = "CREATE_USER_ID", length = 20)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 20)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}