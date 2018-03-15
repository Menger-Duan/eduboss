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

import com.eduboss.common.LectureClassStatus;

@Entity
@Table(name = "LECTURE_CLASS")
public class LectureClass implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String lectureId;
	private String lectureName;
	private Product product;
	private DataDict grade;
	private DataDict subject;
	private Organization blBranch;
	private DataDict recruitStudentStatus;
	private String teacher;
	private String startDate;
	private Integer members;//最低开班人数
	private Integer lectureTimeLong;
	private String remark;
	private String createTime;
	private String createUser;
	private String modifyTime;
	private String modifyUser;
	private LectureClassStatus lectureStatus;
	private String startTime;

	public LectureClass() {
	}

	public LectureClass(String lectureId) {
		this.lectureId=lectureId;
	}

	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
	@GeneratedValue(generator="generator")
	@Column(name = "LECTURE_ID", unique = true, nullable = false, length = 32)
	public String getLectureId() {
		return lectureId;
	}

	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCT")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return this.grade;
	}

	public void setGrade(DataDict grade) {
		this.grade = grade;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@Column(name = "TEACHERS", length = 100)
	public String getTeacher() {
		return this.teacher;
	}

	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}

	@Column(name = "START_DATE", length = 20)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	

	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER", length = 32)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER", length = 32)
	public String getModifyUser() {
		return this.modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BL_BRANCH_ID")
	public Organization getBlBranch() {
		return blBranch;
	}

	public void setBlBranch(Organization blBranch) {
		this.blBranch = blBranch;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "RECRUIT_STUDENT_STATUS")
	public DataDict getRecruitStudentStatus() {
		return recruitStudentStatus;
	}

	public void setRecruitStudentStatus(DataDict recruitStudentStatus) {
		this.recruitStudentStatus = recruitStudentStatus;
	}

	
	@Column(name = "LECTURE_NAME", length = 100)
	public String getLectureName() {
		return lectureName;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}

	@Column(name = "MEMBERS")
	public Integer getMembers() {
		return members;
	}

	public void setMembers(Integer members) {
		this.members = members;
	}

	@Column(name = "LECTURE_TIME_LONG")
	public Integer getLectureTimeLong() {
		return lectureTimeLong;
	}

	
	public void setLectureTimeLong(Integer lectureTimeLong) {
		this.lectureTimeLong = lectureTimeLong;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="LECTURE_STATUS")
	public LectureClassStatus getLectureStatus() {
		return lectureStatus;
	}

	public void setLectureStatus(LectureClassStatus lectureStatus) {
		this.lectureStatus = lectureStatus;
	}

	@Column(name = "START_TIME", length = 20)
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	

}