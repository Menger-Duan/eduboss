package com.eduboss.domainVo;

import java.math.BigDecimal;

public class ProductSubjectCourseHoursVo {

	private String productId;
	private String subjectId;
	private BigDecimal courseHours;
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public BigDecimal getCourseHours() {
		return courseHours;
	}
	public void setCourseHours(BigDecimal courseHours) {
		this.courseHours = courseHours;
	}
}
