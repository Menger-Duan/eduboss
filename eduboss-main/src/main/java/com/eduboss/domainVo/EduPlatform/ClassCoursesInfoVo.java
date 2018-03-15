package com.eduboss.domainVo.EduPlatform;

/**
 * 
 * @author xiaojinwang 查询班级内的讲次（课程）列表
 */
public class ClassCoursesInfoVo {
	private String courseId;// 课程编号
	private String courseName;// 课程编号
	private String type;// 课程类型
	private String state;// 课程状态
	private String teacherId;// 教师编号
	private String teacherName;// 教师名称
	private String subjectId;// 学科编号
	private String subjectName;// 学科名称
	private String gradeId;// 年级编号
	private String gradeName;// 年级名称
	private String organizationId;// 校区编号
	private String organizationName;// 校区名称
	private String regionId;// 地区编号
	private String regionName;// 地区名称
	private String remainingTime;// 该课程剩余课时
	private String courseDate;//课程日期
	private String courseTime;// 上课时间
	private String courseEndTime;// 课程结束时间
	private String courseMinutes;// 课时时长
	private String dayOfWeek;// 周几 比如周五
	private String period;// 该课程课时（换算成分钟）	
	private Integer courseNum;//课程排序

	public ClassCoursesInfoVo() {

	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}

	public String getCourseTime() {
		return courseTime;
	}

	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}

	public String getCourseEndTime() {
		return courseEndTime;
	}

	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}

	public String getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(String courseMinutes) {
		this.courseMinutes = courseMinutes;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}

	public Integer getCourseNum() {
		return courseNum;
	}

	public void setCourseNum(Integer courseNum) {
		this.courseNum = courseNum;
	}
	

}
