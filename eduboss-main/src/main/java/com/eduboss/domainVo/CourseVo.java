package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseAttenceType;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.ProductType;
import com.eduboss.common.StudentStatus;


public class CourseVo {
	private String arrangerId;
	private String arrangerName;
	private BigDecimal auditHours;
	private String courseDate;
	private String courseId;
	private CourseStatus courseStatus;
	private String courseStatusName;
	private String courseTime;
	private int courseTimeLong;
	private BigDecimal courseMinutes;
	private String crashInd;
	private String createTime;
	private String createUserId;
	private String createUserName;
	private String currentRoleId;
	private String endDate;
	private String grade;
	private String gradeValue;
	private String modifyTime;
	private String modifyUserId;
	private String modifyUserName;
	private BigDecimal oneOnOneRemainingHour;
	private BigDecimal planHours;
	private String productId;
	private String productName;
	private ProductType productType;
	private BigDecimal realHours;
	private BigDecimal staduyManagerAuditHours;
	private String startDate;
	private String studentAttendTime;
	private String studentCrashInfo;
	private String studentId;
	private String studentName;
	private String studyManagerAuditId;
	private String studyManagerAuditName;
	private String studyManagerAuditTime;
	private String studyManagerId;
	private String studyManagerName;
	private String subject;
	private String subjectValue;
	private String teacherCrashInfo;
	private String teacherId;
	private String teacherName;
	private String teachingAttendTime;
    private BigDecimal teachingManagerAuditHours;
    private String teachingManagerAuditId;
    private String teachingManagerAuditName;
    private String teachingManagerAuditTime;
    private String auditStatusName;
    private String auditStatusId;
    private AuditStatus auditStatus;
    private String campusId;
    private String campusName;
    private String hasUnauditStatus;
	private String courseStartTime;//开始时间
	private String studentContact;//学生联系方式
	private String teacherContact;//老师联系方式
	private String studyManagerContact;//学管联系方式
	private String searchParam;//搜索条件
	
	private String attendacePicName;

	private String anshazhesuan; //按什么结算课时（小时）
	
	private List<String> loginUserOrganizationId;
	
	private String courseEndTime;//课程结束时间
	private String weekDay;//课程日期对应的星期
	
	private StudentStatus stuStatus;
	
	private String isWashed; // TRUE：发生过冲销，FALSE:未发生过冲销
	
	private String washRemark; // 冲销原因详情
	
	private String teacherType;//老师类型
	
	private Integer version;//乐观锁版本　加１后就是课程版本
	
	//考勤类型
	private CourseAttenceType courseAttenceType;
	private String courseAttenceTypeName;
    private String courseVersion;//课程版本　前端显示
    private int cVersion;//课程版本 数据库字段
	
		
	public String getSearchParam() {
		return searchParam;
	}
	public void setSearchParam(String searchParam) {
		this.searchParam = searchParam;
	}
	public String getStudentContact() {
		return studentContact;
	}
	public void setStudentContact(String studentContact) {
		this.studentContact = studentContact;
	}
	public String getTeacherContact() {
		return teacherContact;
	}
	public void setTeacherContact(String teacherContact) {
		this.teacherContact = teacherContact;
	}
	public String getStudyManagerContact() {
		return studyManagerContact;
	}
	public void setStudyManagerContact(String studyManagerContact) {
		this.studyManagerContact = studyManagerContact;
	}
	public String getCourseStartTime() {
		return courseStartTime;
	}
	public void setCourseStartTime(String courseStartTime) {
		this.courseStartTime = courseStartTime;
	}
	public String getArrangerId() {
		return arrangerId;
	}
	public String getArrangerName() {
		return arrangerName;
	}
	public BigDecimal getAuditHours() {
		return auditHours;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public String getCourseId() {
		return courseId;
	}
	public CourseStatus getCourseStatus() {
		return courseStatus;
	}
	public String getCourseStatusName() {
		return courseStatusName;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public int getCourseTimeLong() {
		return courseTimeLong;
	}
	public String getCrashInd() {
		return crashInd;
	}
	public String getCreateTime() {
		return createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public String getCurrentRoleId() {
		return currentRoleId;
	}
	public String getEndDate() {
		return endDate;
	}
	public String getGrade() {
		return grade;
	}
	public String getGradeValue() {
		return gradeValue;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public String getModifyUserName() {
		return modifyUserName;
	}
	public BigDecimal getOneOnOneRemainingHour() {
		return oneOnOneRemainingHour;
	}
	public BigDecimal getPlanHours() {
		return planHours;
	}
	public String getProductId() {
        return productId;
    }
	public String getProductName() {
        return productName;
    }
	public ProductType getProductType() {
		return productType;
	}
	public void setProductType(ProductType productType) {
		this.productType = productType;
	}
	public BigDecimal getRealHours() {
		return realHours;
	}
	public BigDecimal getStaduyManagerAuditHours() {
		return staduyManagerAuditHours;
	}
	public String getStartDate() {
		return startDate;
	}
	public String getStudentAttendTime() {
		return studentAttendTime;
	}
	public String getStudentCrashInfo() {
		return studentCrashInfo;
	}
	public String getStudentId() {
		return studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public String getStudyManagerAuditId() {
		return studyManagerAuditId;
	}
	public String getStudyManagerAuditName() {
		return studyManagerAuditName;
	}
	public String getStudyManagerAuditTime() {
		return studyManagerAuditTime;
	}
	public String getStudyManagerId() {
		return studyManagerId;
	}
	public String getStudyManagerName() {
		return studyManagerName;
	}
	public String getSubject() {
		return subject;
	}
	public String getSubjectValue() {
		return subjectValue;
	}
	public String getTeacherCrashInfo() {
		return teacherCrashInfo;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public String getTeachingAttendTime() {
		return teachingAttendTime;
	}
	public BigDecimal getTeachingManagerAuditHours() {
		return teachingManagerAuditHours;
	}
	public String getTeachingManagerAuditId() {
		return teachingManagerAuditId;
	}
	public String getTeachingManagerAuditName() {
		return teachingManagerAuditName;
	}
	public String getTeachingManagerAuditTime() {
		return teachingManagerAuditTime;
	}
	public void setArrangerId(String arrangerId) {
		this.arrangerId = arrangerId;
	}
	public void setArrangerName(String arrangerName) {
		this.arrangerName = arrangerName;
	}
	public void setAuditHours(BigDecimal auditHours) {
		this.auditHours = auditHours;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public void setCourseStatus(CourseStatus courseStatus) {
		this.courseStatus = courseStatus;
	}
	public void setCourseStatusName(String courseStatusName) {
		this.courseStatusName = courseStatusName;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public void setCourseTimeLong(int courseTimeLong) {
		this.courseTimeLong = courseTimeLong;
	}
	public void setCrashInd(String crashInd) {
		this.crashInd = crashInd;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public void setCurrentRoleId(String currentRoleId) {
		this.currentRoleId = currentRoleId;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public void setGradeValue(String gradeValue) {
		this.gradeValue = gradeValue;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public void setModifyUserName(String modifyUserName) {
		this.modifyUserName = modifyUserName;
	}
	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}
	public void setPlanHours(BigDecimal planHours) {
		this.planHours = planHours;
	}
	public void setProductId(String productId) {
        this.productId = productId;
    }
	public void setProductName(String productName) {
        this.productName = productName;
    }
	public void setRealHours(BigDecimal realHours) {
		this.realHours = realHours;
	}
	public void setStaduyManagerAuditHours(BigDecimal staduyManagerAuditHours) {
		this.staduyManagerAuditHours = staduyManagerAuditHours;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setStudentAttendTime(String studentAttendTime) {
		this.studentAttendTime = studentAttendTime;
	}
	public void setStudentCrashInfo(String studentCrashInfo) {
		this.studentCrashInfo = studentCrashInfo;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public void setStudyManagerAuditId(String studyManagerAuditId) {
		this.studyManagerAuditId = studyManagerAuditId;
	}
	public void setStudyManagerAuditName(String studyManagerAuditName) {
		this.studyManagerAuditName = studyManagerAuditName;
	}
	public void setStudyManagerAuditTime(String studyManagerAuditTime) {
		this.studyManagerAuditTime = studyManagerAuditTime;
	}
	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}
	public void setStudyManagerName(String studyManagerName) {
		this.studyManagerName = studyManagerName;
	}

    public void setSubject(String subject) {
		this.subject = subject;
	}

    public void setSubjectValue(String subjectValue) {
		this.subjectValue = subjectValue;
	}

    public void setTeacherCrashInfo(String teacherCrashInfo) {
		this.teacherCrashInfo = teacherCrashInfo;
	}

    public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public void setTeachingAttendTime(String teachingAttendTime) {
		this.teachingAttendTime = teachingAttendTime;
	}
	public void setTeachingManagerAuditHours(BigDecimal teachingManagerAuditHours) {
		this.teachingManagerAuditHours = teachingManagerAuditHours;
	}
	public void setTeachingManagerAuditId(String teachingManagerAuditId) {
		this.teachingManagerAuditId = teachingManagerAuditId;
	}
	public void setTeachingManagerAuditName(String teachingManagerAuditName) {
		this.teachingManagerAuditName = teachingManagerAuditName;
	}
	public void setTeachingManagerAuditTime(String teachingManagerAuditTime) {
		this.teachingManagerAuditTime = teachingManagerAuditTime;
	}
	public String getAuditStatusName() {
		return auditStatusName;
	}
	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}	
	public AuditStatus getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
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
	public String getHasUnauditStatus() {
		return hasUnauditStatus;
	}
	public void setHasUnauditStatus(String hasUnauditStatus) {
		this.hasUnauditStatus = hasUnauditStatus;
	}
	public String getAttendacePicName() {
		return attendacePicName;
	}
	public void setAttendacePicName(String attendacePicName) {
		this.attendacePicName = attendacePicName;
	}

	public String getAnshazhesuan() {
		return anshazhesuan;
	}

	public void setAnshazhesuan(String anshazhesuan) {
		this.anshazhesuan = anshazhesuan;
	}
	
	public List<String> getLoginUserOrganizationId() {
		return loginUserOrganizationId;
	}
	public void setLoginUserOrganizationId(List<String> loginUserOrganizationId) {
		this.loginUserOrganizationId = loginUserOrganizationId;
	}
	
	public String getCourseEndTime() {
		return courseEndTime;
	}
	public void setCourseEndTime(String courseEndTime) {
		this.courseEndTime = courseEndTime;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public StudentStatus getStuStatus() {
		return stuStatus;
	}
	public void setStuStatus(StudentStatus stuStatus) {
		this.stuStatus = stuStatus;
	}
	public String getAuditStatusId() {
		return auditStatusId;
	}
	public void setAuditStatusId(String auditStatusId) {
		this.auditStatusId = auditStatusId;
	}
	public String getIsWashed() {
		return isWashed;
	}
	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}
	public String getWashRemark() {
		return washRemark;
	}
	public void setWashRemark(String washRemark) {
		this.washRemark = washRemark;
	}
	public String getTeacherType() {
		return teacherType;
	}
	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}

	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public CourseAttenceType getCourseAttenceType() {
		return courseAttenceType;
	}
	public void setCourseAttenceType(CourseAttenceType courseAttenceType) {
		this.courseAttenceType = courseAttenceType;
	}
	public String getCourseAttenceTypeName() {
		return courseAttenceTypeName;
	}
	public void setCourseAttenceTypeName(String courseAttenceTypeName) {
		this.courseAttenceTypeName = courseAttenceTypeName;
	}
	public String getCourseVersion() {
		return courseVersion;
	}
	public void setCourseVersion(String courseVersion) {
		this.courseVersion = courseVersion;
	}
	public int getcVersion() {
		return cVersion;
	}
	public void setcVersion(int cVersion) {
		this.cVersion = cVersion;
	}
	
	
}
