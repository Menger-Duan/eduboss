package com.eduboss.domain;

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
 * @author laiyongchang
 * */

@Entity
@Table(name = "promise_class")
public class PromiseClass implements java.io.Serializable{
	/**
	 * 目标班序号
	 * */
	private String id;
	
	/**
	 * 目标班名称
	 * */
	private String pName;
	
	/**
	 * 校区
	 * */
	private Organization pSchool;
	
	/**
	 * 年级
	 * */
	private String grade;
	
	/**
	 * 学生人数
	 * */
	private Integer total_student;
	
	/**
	 * 班主任
	 * */
	private User head_teacher;
	
	/**
	 * 开班日期
	 * */
	private String startDate;
	
	/**
	 * 结课日期
	 * */
	private String endDate;
	
	/**
	 * 教室
	 * */
	private String classRoom;
	
	/**
	 * 是否已结课 
	 * */
	private String pStatus;
	
	/**
	 * 成功率
	 * */
	private Double success_rate;
	
	/**
	 * 创建者ID
	 * */
	private String createUserId;
	
	/**
	 * 创建时间
	 * */
	private String createDate;
	
	/**
	 * 修改者ID
	 * */
	private String modifyUserId;
	
	/**
	 * 修改时间
	 * */
	private String modifyDate;
	/**
	 * 目标班年份
	 */
	private DataDict year;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "PNAME")
	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PSCHOOL")
	public Organization getpSchool() {
		return pSchool;
	}

	public void setpSchool(Organization pSchool) {
		this.pSchool = pSchool;
	}
	
	@Column(name = "GRADE")
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Column(name = "TOTAL_STUDENT")
	public Integer getTotal_student() {
		return total_student;
	}

	public void setTotal_student(Integer totalStudent) {
		this.total_student = totalStudent;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn( name = "HEAD_TEACHER")
	public User getHead_teacher() {
		return head_teacher;
	}

	public void setHead_teacher(User headTeacher) {
		this.head_teacher = headTeacher;
	}
	
	@Column( name = "STARTDATE")
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "ENDDATE")
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "CLASSROOM")
	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}
	
	@Column(name = "PSTATUS")
	public String getpStatus() {
		return pStatus;
	}

	public void setpStatus(String pStatus) {
		this.pStatus = pStatus;
	}
	
	@Column(name = "SUCCESS_RATE")
	public Double getSuccess_rate() {
		return success_rate;
	}

	public void setSuccess_rate(Double successRate) {
		success_rate = successRate;
	}
	
	@Column(name = "CREATE_USER_ID")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_DATE")
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Column(name = "MODIFY_USER_ID")
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "MODIFY_DATE")
	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "YEAR_ID")
	public DataDict getYear() {
		return year;
	}

	public void setYear(DataDict year) {
		this.year = year;
	}
	
	
	

}
