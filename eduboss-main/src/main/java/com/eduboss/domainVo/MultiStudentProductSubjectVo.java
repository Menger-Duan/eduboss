package com.eduboss.domainVo;

import java.util.Set;

public class MultiStudentProductSubjectVo {

	private String studentId;
	private String courseSummaryId;
	private Set<ProductSubjectCourseHoursVo> productSubjectCourseHoursSet;
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getCourseSummaryId() {
		return courseSummaryId;
	}
	public void setCourseSummaryId(String courseSummaryId) {
		this.courseSummaryId = courseSummaryId;
	}
	public Set<ProductSubjectCourseHoursVo> getProductSubjectCourseHoursSet() {
		return productSubjectCourseHoursSet;
	}
	public void setProductSubjectCourseHoursSet(
			Set<ProductSubjectCourseHoursVo> productSubjectCourseHoursSet) {
		this.productSubjectCourseHoursSet = productSubjectCourseHoursSet;
	}
	
}
