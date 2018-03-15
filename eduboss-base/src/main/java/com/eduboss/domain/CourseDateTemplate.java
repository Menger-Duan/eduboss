package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.CourseTemplateType;

/**
 * CourseDateTemplate entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "course_date_template")
public class CourseDateTemplate implements java.io.Serializable {

	// Fields

	private String templateId;
	private String userId;
	private String name;
	private CourseTemplateType templateType;
	private String startDate;
	private String endDate;
	private String selectedDates;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;

	// Constructors

	/** default constructor */
	public CourseDateTemplate() {
	}

	/** minimal constructor */
	public CourseDateTemplate(String userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	/** full constructor */
	public CourseDateTemplate(String userId, String name, String startDate, String endDate, String selectedDates, String createTime,
			String createUserId, String modifyTime, String modifyUserId) {
		this.userId = userId;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.selectedDates = selectedDates;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "TEMPLATE_ID", unique = true, nullable = false, length = 32)
	public String getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	@Column(name = "USER_ID", nullable = false, length = 32)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "NAME", nullable = false, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "START_DATE", length = 10)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "END_DATE", length = 10)
	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Column(name = "SELECTED_DATES", length = 8192)
	public String getSelectedDates() {
		return this.selectedDates;
	}

	public void setSelectedDates(String selectedDates) {
		this.selectedDates = selectedDates;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 10)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	/**
	 * @return the courseTemplateType
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TEMPLATE_TYPE", length = 32)
	public CourseTemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * @param courseTemplateType the courseTemplateType to set
	 */
	public void setTemplateType(CourseTemplateType courseTemplateType) {
		this.templateType = courseTemplateType;
	}

}