package com.eduboss.domainVo;

public class CourseAttendaceStatusCountVo {

	private int newCount; // 老师未考勤数
	private int teacherAttendanceCount; // 已考勤待审数
	private int studyManagerAuditedCount; // 学管已确认待扣费数
	public int getNewCount() {
		return newCount;
	}
	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}
	public int getTeacherAttendanceCount() {
		return teacherAttendanceCount;
	}
	public void setTeacherAttendanceCount(int teacherAttendanceCount) {
		this.teacherAttendanceCount = teacherAttendanceCount;
	}
	public int getStudyManagerAuditedCount() {
		return studyManagerAuditedCount;
	}
	public void setStudyManagerAuditedCount(int studyManagerAuditedCount) {
		this.studyManagerAuditedCount = studyManagerAuditedCount;
	}
	
	
	
}
