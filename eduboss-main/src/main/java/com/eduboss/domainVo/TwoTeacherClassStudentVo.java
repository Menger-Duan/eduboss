package com.eduboss.domainVo;

import java.math.BigDecimal;

public class TwoTeacherClassStudentVo {
	private int id;
	private String classTwoId;
	private String classTwoName;
	private String studentId;
	private String studentName;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String contractProductId;
	private String firstSchoolTime;//首次上课时间
	private String classId;
	private BigDecimal consumeAmount;
	private BigDecimal remaningtAmount;
	private BigDecimal remaningHours;


	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public BigDecimal getRemaningtAmount() {
		return remaningtAmount;
	}

	public void setRemaningtAmount(BigDecimal remaningtAmount) {
		this.remaningtAmount = remaningtAmount;
	}

	public BigDecimal getRemaningHours() {
		return remaningHours;
	}

	public void setRemaningHours(BigDecimal remaningHours) {
		this.remaningHours = remaningHours;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClassTwoId() {
		return classTwoId;
	}

	public void setClassTwoId(String classTwoId) {
		this.classTwoId = classTwoId;
	}

	public String getClassTwoName() {
		return classTwoName;
	}

	public void setClassTwoName(String classTwoName) {
		this.classTwoName = classTwoName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getContractProductId() {
		return contractProductId;
	}

	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}

	public String getFirstSchoolTime() {
		return firstSchoolTime;
	}

	public void setFirstSchoolTime(String firstSchoolTime) {
		this.firstSchoolTime = firstSchoolTime;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
}
