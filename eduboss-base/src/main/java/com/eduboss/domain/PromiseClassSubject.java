package com.eduboss.domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "promise_class_subject")
public class PromiseClassSubject implements java.io.Serializable{
	private int id ;
	private MiniClass miniClass;
	private DataDict subject;
	private BigDecimal courseHours;
	private String createTime;
	private String modifyTime;
	private String createUserId;
	private String modifyUserId;
	private DataDict quarterId;
	private PromiseStudent promiseStudent;
	private BigDecimal consumeCourseHours;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "MINI_CLASS_ID")
	public MiniClass getMiniClass() {
		return miniClass;
	}

	public void setMiniClass(MiniClass miniClass) {
		this.miniClass = miniClass;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID")
	public DataDict getSubject() {
		return subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@Column(name = "COURSE_HOURS")
	public BigDecimal getCourseHours() {
		return courseHours;
	}

	public void setCourseHours(BigDecimal courseHours) {
		this.courseHours = courseHours;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "QUARTER_ID")
	public DataDict getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(DataDict quarterId) {
		this.quarterId = quarterId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "promise_student_id")
	public PromiseStudent getPromiseStudent() {
		return promiseStudent;
	}

	public void setPromiseStudent(PromiseStudent promiseStudent) {
		this.promiseStudent = promiseStudent;
	}


	@Column(name = "consume_course_hours")
	public BigDecimal getConsumeCourseHours() {
		return consumeCourseHours;
	}

	public void setConsumeCourseHours(BigDecimal consumeCourseHours) {
		this.consumeCourseHours = consumeCourseHours;
	}
}
