package com.eduboss.domainVo;


/**@author wmy
 *@date 2015年11月10日下午3:53:17
 *@version 1.0 
 *@description
 */
public class CommonClassCourseVo implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -100226318148503718L;
	
    private String productType;
	private String courseId;  
	private String courseDate;  //日期
	private String courseBeginTime;  //开始时间
	private String courseEndTime;   //结束时间
	private String courseStatus;    //课程状态
	private String courseStatusName; //课程状态名称
	private String crashInd;   //冲突标记 
    private String campusId;   //校区
	private String campusName;  //校区名称
	private String teacherId;  //老师
	private String teacherName;
	private String teacherMobile;  //老师电话
	private String studyManagerId;   //一对一学管、小班班主任
	private String studyManagerName;  
	private String studyManagerMobile;  //学管/班主任电话
	private String subjectId;
	private String subject;  //科目
	private String gradeId;
	private String grade;   //年级
	private String classroomId;
	private String classroom;  //课室	
	private String classId;    //班级ID （一对一不使用）
	private String className;  //班级名称
	private String productId;
	private String productName;

	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getCourseBeginTime() {
		return courseBeginTime;
	}
	public void setCourseBeginTime(String courseBeginTime) {
		this.courseBeginTime = courseBeginTime;
	}
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getCourseStatus() {
		return courseStatus;
	}
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}
	public String getCrashInd() {
		return crashInd;
	}
	public void setCrashInd(String crashInd) {
		this.crashInd = crashInd;
	}
	public String getCampusId() {
		return campusId;
	}
	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
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
	public String getTeacherMobile() {
		return teacherMobile;
	}
	public void setTeacherMobile(String teacherMobile) {
		this.teacherMobile = teacherMobile;
	}
	public String getStudyManagerId() {
		return studyManagerId;
	}
	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}
	public String getStudyManagerMobile() {
		return studyManagerMobile;
	}
	public void setStudyManagerMobile(String studyManagerMobile) {
		this.studyManagerMobile = studyManagerMobile;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	public String getClassroom() {
		return classroom;
	}
	public void setClassroom(String classroom) {
		this.classroom = classroom;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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


