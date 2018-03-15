package com.eduboss.domain;

import com.eduboss.common.MiniClassStatus;

import javax.persistence.*;

@Entity
@Table(name = "TWO_TEACHER_CLASS")
public class TwoTeacherClass implements java.io.Serializable {

	// Fields

	private int classId;
	private Product product;
	private String name;
	private Organization blCampus;
	private DataDict subject;
	private User teacher;
	private String startDate;
	private String classTime;

	private Double everyCourseClassNum;

	private String remark;
	private Double totalClassHours;

	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private MiniClassStatus status;
	private Integer peopleQuantity;
	private Double unitPrice;
	private Integer classTimeLength;

	private DataDict phase; //期


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLASS_ID", unique = true, nullable = false)
	public int getClassId() {
		return this.classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PRODUCE_ID")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "NAME", length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SUBJECT")
	public DataDict getSubject() {
		return this.subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "TEACHER_ID")
	public User getTeacher() {
		return this.teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

	@Column(name = "START_DATE", length = 20)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "CLASS_TIME")
	public String getClassTime() {
		return this.classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	@Column(name = "TOTAL_CLASS_HOURS", precision = 9)
	public Double getTotalClassHours() {
		return this.totalClassHours;
	}

	public void setTotalClassHours(Double totalClassHours) {
		this.totalClassHours = totalClassHours;
	}

	@Column(name = "UNIT_PRICE", precision = 9)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
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

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}

	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}

	@Column(name = "EVERY_COURSE_CLASS_NUM", precision = 9)
	public Double getEveryCourseClassNum() {
		return everyCourseClassNum;
	}

	public void setEveryCourseClassNum(Double everyCourseClassNum) {
		this.everyCourseClassNum = everyCourseClassNum;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	public MiniClassStatus getStatus() {
		return status;
	}

	public void setStatus(MiniClassStatus status) {
		this.status = status;
	}

	@Column(name = "PEOPLE_QUANTITY", length = 11)
	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}

	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}


	@Column(name="CLASS_TIME_LENGTH",precision = 10)
	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}


	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PHASE")
	public DataDict getPhase() {
		return phase;
	}

	public void setPhase(DataDict phase) {
		this.phase = phase;
	}
}