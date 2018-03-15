package com.eduboss.domainVo.EduPlatform;

/**
 * Created by Administrator on 2017/4/26.
 */
public class StudentClassInfoVo {
    private String classId ;//班级编号
    private String classIdTwo ;//副班编号
    private String teacherId; //教师编号
    private String teacherIdTwo; //副班教师编号
    private String className;//班级名称
    private String gradeId;//年级编号
    private String gradeName;//年级名称
    private String subjectId;//学科编号
    private String subjectName;//学科名称
    private String type;//课程类型
    private String courseDate;//下次上课日期
    private String courseTime;//下次上课时间
    private String courseEndTime;//课程结束时间
    private String dayOfWeek;//周几
    private int allCourseCount;//总课程数
    private int finishCourseCount;//已经上了课的课程数
    private boolean finishFlag;//是否已结课
    private String enrollInClassTime;//学生报班时间
    private String classEndTime;//课程结束日期+时间
    private String teacherAccount;//主班老师账号
    private String teacherTwoAccount;//副班老师账号
    private String teacherName;//主班老师名字
    private String teacherTwoName;//副班老师名字
    private String remainCourseHoursCount;//剩余课时
    private String finishCourseHoursCount;//已上课时
    private String courseId;//课程id
    private String courseSummaryId;//课表id
    private String productName;//购买课程
    private String courseTeacherId;
    private String courseTeacherName;
    
    private boolean quitFlag;//是否退班(双师，小班) 是否退费（一对一，一对多）
    public StudentClassInfoVo() {
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassIdTwo() {
        return classIdTwo;
    }

    public void setClassIdTwo(String classIdTwo) {
        this.classIdTwo = classIdTwo;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherIdTwo() {
        return teacherIdTwo;
    }

    public void setTeacherIdTwo(String teacherIdTwo) {
        this.teacherIdTwo = teacherIdTwo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(String courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getAllCourseCount() {
        return allCourseCount;
    }

    public void setAllCourseCount(int allCourseCount) {
        this.allCourseCount = allCourseCount;
    }

    public int getFinishCourseCount() {
        return finishCourseCount;
    }

    public void setFinishCourseCount(int finishCourseCount) {
        this.finishCourseCount = finishCourseCount;
    }

    public boolean isFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(boolean finishFlag) {
        this.finishFlag = finishFlag;
    }

	public String getEnrollInClassTime() {
		return enrollInClassTime;
	}

	public void setEnrollInClassTime(String enrollInClassTime) {
		this.enrollInClassTime = enrollInClassTime;
	}

	public String getClassEndTime() {
		return classEndTime;
	}

	public void setClassEndTime(String classEndTime) {
		this.classEndTime = classEndTime;
	}

	public String getTeacherAccount() {
		return teacherAccount;
	}

	public void setTeacherAccount(String teacherAccount) {
		this.teacherAccount = teacherAccount;
	}

	public String getTeacherTwoAccount() {
		return teacherTwoAccount;
	}

	public void setTeacherTwoAccount(String teacherTwoAccount) {
		this.teacherTwoAccount = teacherTwoAccount;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherTwoName() {
		return teacherTwoName;
	}

	public void setTeacherTwoName(String teacherTwoName) {
		this.teacherTwoName = teacherTwoName;
	}

	public String getRemainCourseHoursCount() {
		return remainCourseHoursCount;
	}

	public void setRemainCourseHoursCount(String remainCourseHoursCount) {
		this.remainCourseHoursCount = remainCourseHoursCount;
	}

	public String getFinishCourseHoursCount() {
		return finishCourseHoursCount;
	}

	public void setFinishCourseHoursCount(String finishCourseHoursCount) {
		this.finishCourseHoursCount = finishCourseHoursCount;
	}

	public boolean isQuitFlag() {
		return quitFlag;
	}

	public void setQuitFlag(boolean quitFlag) {
		this.quitFlag = quitFlag;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseSummaryId() {
		return courseSummaryId;
	}

	public void setCourseSummaryId(String courseSummaryId) {
		this.courseSummaryId = courseSummaryId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCourseTeacherId() {
		return courseTeacherId;
	}

	public void setCourseTeacherId(String courseTeacherId) {
		this.courseTeacherId = courseTeacherId;
	}

	public String getCourseTeacherName() {
		return courseTeacherName;
	}

	public void setCourseTeacherName(String courseTeacherName) {
		this.courseTeacherName = courseTeacherName;
	}



}
