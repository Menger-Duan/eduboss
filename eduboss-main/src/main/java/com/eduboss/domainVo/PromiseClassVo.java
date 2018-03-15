package com.eduboss.domainVo;

public class PromiseClassVo {
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
	private String pSchoolName;
	
	private String pSchoolId;
	
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
	private String head_teacherId;
	
	private String head_teacherName;
	
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
	 * 年份
	 */
	private String yearId;
    private String yearName;
    
    //目标班产品
    private String productId;
	private String productName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	
	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getTotal_student() {
		return total_student;
	}

	public void setTotal_student(Integer totalStudent) {
		total_student = totalStudent;
	}

	public String getHead_teacherId() {
		return head_teacherId;
	}

	public void setHead_teacherId(String headTeacherId) {
		head_teacherId = headTeacherId;
	}

	public String getHead_teacherName() {
		return head_teacherName;
	}

	public void setHead_teacherName(String headTeacherName) {
		head_teacherName = headTeacherName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getClassRoom() {
		return classRoom;
	}

	public void setClassRoom(String classRoom) {
		this.classRoom = classRoom;
	}

	public String getpStatus() {
		return pStatus;
	}

	public void setpStatus(String pStatus) {
		this.pStatus = pStatus;
	}

	public Double getSuccess_rate() {
		return success_rate;
	}

	public void setSuccess_rate(Double successRate) {
		success_rate = successRate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getpSchoolName() {
		return pSchoolName;
	}

	public void setpSchoolName(String pSchoolName) {
		this.pSchoolName = pSchoolName;
	}

	public String getpSchoolId() {
		return pSchoolId;
	}

	public void setpSchoolId(String pSchoolId) {
		this.pSchoolId = pSchoolId;
	}

	public String getYearId() {
		return yearId;
	}

	public void setYearId(String yearId) {
		this.yearId = yearId;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	
	
}
