package com.eduboss.domainVo.wechatVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 用于一对一 一对多接口
 * @author xiaojinwang
 *
 */
public class CourseForWechatVo {

	private String courseId;//课程id
	private String courseType;//课程类型 是一对一还是一对多
	private String productName;//报读产品
	private String courseDate;//日期
	private String courseTime;//上课时间
	private List<String> studentName = new ArrayList<>();//学生名字
	private String teacherName;//老师名字
	private String studyManagerName;//学管名字
	private String campusName;//校区名字
	private String courseStatusName;//课程状态	
	private BigDecimal planHours;//计划课时 
	private BigDecimal auditHours;//实际课时
	private String auditStatusName;//审批状态
	private String attendacePicName;//课时单 返回阿里云地址+图片名字 
	
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public List<String> getStudentName() {
		return studentName;
	}
	public void setStudentName(List<String> studentName) {
		this.studentName = studentName;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}
	public BigDecimal getPlanHours() {
		return planHours;
	}
	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
	}
	public BigDecimal getAuditHours() {
		return auditHours;
	}
	public void setAuditHours(BigDecimal auditHours) {
		this.auditHours = auditHours;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
	public String getAttendacePicName() {
		return attendacePicName;
	}
	public void setAttendacePicName(String attendacePicName) {
		this.attendacePicName = attendacePicName;
	}
	
	
	
}
