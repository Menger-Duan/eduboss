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

import com.eduboss.common.OtmClassStatus;

/**
 * 
 * OtmClass Entity @author lixuejun
 *
 */
@Entity
@Table(name = "otm_class")
public class OtmClass implements java.io.Serializable {

	private String otmClassId;
	private String name; // 名称
	private Integer otmType; // 一对多类型
	private DataDict subject; // 科目
	private DataDict recruitStudentStatus; // 招生状态
	private DataDict grade; // 年级
	private User teacher;
	private Organization blCampus; // 所属校区
	private String startDate; // 开课日期
	private String remark; // 备注
	
	private Double consume; // 消耗的课时
	
	private OtmClassStatus status;
	
	private Integer peopleQuantity; // 一对多班级最大人数
	
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	/** default constructor */
	public OtmClass() {
		super();
	}
	
	/** full constructor */
	public OtmClass(String otmClassId, String name, Integer otmType,
			DataDict subject, DataDict recruitStudentStatus, DataDict grade,
			User teacher, Organization blCampus, String startDate,
			String remark, Double consume, OtmClassStatus status,
			Integer peopleQuantity, String createTime, String createUserId,
			String modifyTime, String modifyUserId) {
		super();
		this.otmClassId = otmClassId;
		this.name = name;
		this.otmType = otmType;
		this.subject = subject;
		this.recruitStudentStatus = recruitStudentStatus;
		this.grade = grade;
		this.teacher = teacher;
		this.blCampus = blCampus;
		this.startDate = startDate;
		this.remark = remark;
		this.consume = consume;
		this.status = status;
		this.peopleQuantity = peopleQuantity;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}
	
	@Id
	@GenericGenerator(name="generator", strategy="uuid.hex")
	@GeneratedValue(generator="generator")
	@Column(name = "OTM_CLASS_ID", unique = true, nullable = false, length = 32)
	public String getOtmClassId() {
		return otmClassId;
	}

	public void setOtmClassId(String otmClassId) {
		this.otmClassId = otmClassId;
	}
	
	@Column(name = "NAME", length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "OTM_TYPE", length = 2)
	public Integer getOtmType() {
		return otmType;
	}
	public void setOtmType(Integer otmType) {
		this.otmType = otmType;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return subject;
	}
	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "RECRUIT_STUDENT_STATUS")
	public DataDict getRecruitStudentStatus() {
		return recruitStudentStatus;
	}
	public void setRecruitStudentStatus(DataDict recruitStudentStatus) {
		this.recruitStudentStatus = recruitStudentStatus;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "GRADE")
	public DataDict getGrade() {
		return grade;
	}
	public void setGrade(DataDict grade) {
		this.grade = grade;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return teacher;
	}
	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}
	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}
	
	@Column(name = "START_DATE", length = 20)
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "REMARK", length = 512)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "CONSUME", precision = 9)
	public Double getConsume() {
		return this.consume;
	}
	public void setConsume(Double consume) {
		this.consume = consume;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS")
	public OtmClassStatus getStatus() {
		return status;
	}

	public void setStatus(OtmClassStatus status) {
		this.status = status;
	}
	
	@Column(name = "PEOPLE_QUANTITY", length = 2)
	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	
}
