package com.eduboss.domainVo;

import com.eduboss.common.ReportStudentSurplusFundingType;
import com.eduboss.common.StudentStatus;

/**
 * Created by yanliang on 8/3/15.
 */
public class ReportStudentSurplusFundingVo {
	private String countDate;
	private String blCampusId;
	private ReportStudentSurplusFundingType reportStudentSurplusFundingType;
	private StudentStatus studentStatus;
	private Boolean isAllStudentStatus;
	private String studentId;
	private String studyManagerId;
	private boolean showAll;
	

	public String getStudyManagerId() {
		return studyManagerId;
	}

	public void setStudyManagerId(String studyManagerId) {
		this.studyManagerId = studyManagerId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getCountDate() {
		return countDate;
	}

	public void setCountDate(String countDate) {
		this.countDate = countDate;
	}

	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	public ReportStudentSurplusFundingType getReportStudentSurplusFundingType() {
		return reportStudentSurplusFundingType;
	}

	public void setReportStudentSurplusFundingType(ReportStudentSurplusFundingType reportStudentSurplusFundingType) {
		this.reportStudentSurplusFundingType = reportStudentSurplusFundingType;
	}

	public StudentStatus getStudentStatus() {
		return studentStatus;
	}

	public void setStudentStatus(StudentStatus studentStatus) {
		this.studentStatus = studentStatus;
	}

	public Boolean getIsAllStudentStatus() {
		return isAllStudentStatus;
	}

	public void setIsAllStudentStatus(Boolean isAllStudentStatus) {
		this.isAllStudentStatus = isAllStudentStatus;
	}

	public boolean isShowAll() {
		return showAll;
	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}
	
}
