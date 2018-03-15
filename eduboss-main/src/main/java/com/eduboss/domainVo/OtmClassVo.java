package com.eduboss.domainVo;

import com.eduboss.common.OtmClassStatus;


public class OtmClassVo implements java.io.Serializable {

	private String otmClassId;
	private String name; // 名称
	// 一对多类型
	private Integer otmType;
	
	// 科目
	private String subjectId;
	private String subjectName;
	
	// 招生状态
	private String recruitStudentStatusId;
	private String recruitStudentStatusName;
	
	// 年级
	private String gradeId;
	private String gradeName;
	
	// 老师
	private String teacherId;
	private String teacherName;
	
	// 所属校区
	private String blCampusId;
	private String blCampusName;
	
	private String startDate; 
	private String endDate;
	
	private Double consume;
	
	private String remark; // 备注
	
	private Integer peopleQuantity; //一对多班级最大人数
	
	private String studentNames; // 报读学生名称
	private int otmClassPeopleNum; //报读学生数量
	
	private OtmClassStatus status;
	
	public String getOtmClassId() {
		return otmClassId;
	}
	public void setOtmClassId(String otmClassId) {
		this.otmClassId = otmClassId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getOtmType() {
		return otmType;
	}
	public void setOtmType(Integer otmType) {
		this.otmType = otmType;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getRecruitStudentStatusId() {
		return recruitStudentStatusId;
	}
	public void setRecruitStudentStatusId(String recruitStudentStatusId) {
		this.recruitStudentStatusId = recruitStudentStatusId;
	}
	public String getRecruitStudentStatusName() {
		return recruitStudentStatusName;
	}
	public void setRecruitStudentStatusName(String recruitStudentStatusName) {
		this.recruitStudentStatusName = recruitStudentStatusName;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
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
	public Double getConsume() {
		return consume;
	}
	public void setConsume(Double consume) {
		this.consume = consume;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getPeopleQuantity() {
		return peopleQuantity;
	}
	public void setPeopleQuantity(Integer peopleQuantity) {
		this.peopleQuantity = peopleQuantity;
	}
	public String getStudentNames() {
		return studentNames;
	}
	public void setStudentNames(String studentNames) {
		this.studentNames = studentNames;
	}
	public OtmClassStatus getStatus() {
		return status;
	}
	public void setStatus(OtmClassStatus status) {
		this.status = status;
	}
	public int getOtmClassPeopleNum() {
		return otmClassPeopleNum;
	}
	public void setOtmClassPeopleNum(int otmClassPeopleNum) {
		this.otmClassPeopleNum = otmClassPeopleNum;
	}
	
}
