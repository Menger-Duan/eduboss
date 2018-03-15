package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.LiveContactType;
import com.eduboss.common.LiveFinanceType;
import com.eduboss.dto.Response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 直播平台，直播课程详情
 *
 */
@Entity
@Table(name = "live_course_detail")
@JsonIgnoreProperties(ignoreUnknown = true) 
public class LiveCourseDetail extends Response implements java.io.Serializable {

	// Fields

	private String id;
	private String courseId;
	private String courseName;
	private String payRecordId;
	
	private BigDecimal paidAmount = BigDecimal.ZERO;
	
	private BigDecimal originalPrice = BigDecimal.ZERO;
	
	private String courseTypeId;
	
	private String courseTypeName;
	
	public LiveCourseDetail() {
		super();
	}
	



	public LiveCourseDetail(String id, String courseId, String courseName, String payRecordId, BigDecimal paidAmount,
			BigDecimal originalPrice, String courseTypeId, String courseTypeName) {
		super();
		this.id = id;
		this.courseId = courseId;
		this.courseName = courseName;
		this.payRecordId = payRecordId;
		this.paidAmount = paidAmount;
		this.originalPrice = originalPrice;
		this.courseTypeId = courseTypeId;
		this.courseTypeName = courseTypeName;
	}




	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "paid_amount", precision = 10, scale = 2)
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}


	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Column(name = "course_id", length = 32)
	public String getCourseId() {
		return courseId;
	}


	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	@Column(name = "course_name", length = 150)
	public String getCourseName() {
		return courseName;
	}


	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Column(name = "pay_record_id", length = 32)
	public String getPayRecordId() {
		return payRecordId;
	}


	public void setPayRecordId(String payRecordId) {
		this.payRecordId = payRecordId;
	}

	@Column(name = "original_price", precision = 10, scale = 2)
	public BigDecimal getOriginalPrice() {
		return originalPrice;
	}


	public void setOriginalPrice(BigDecimal originalPrice) {
		this.originalPrice = originalPrice;
	}

	@Column(name = "course_type_id", length = 255)
	public String getCourseTypeId() {
		return courseTypeId;
	}


	public void setCourseTypeId(String courseTypeId) {
		this.courseTypeId = courseTypeId;
	}

	@Column(name = "course_type_name", length = 255)
	public String getCourseTypeName() {
		return courseTypeName;
	}


	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}


	@Override
	public String toString() {
		return "LiveCourseDetail [id=" + id + ", courseId=" + courseId + ", courseName=" + courseName + ", payRecordId="
				+ payRecordId + ", paidAmount=" + paidAmount + ", originalPrice=" + originalPrice + ", courseTypeId="
				+ courseTypeId + ", courseTypeName=" + courseTypeName + "]";
	}


}