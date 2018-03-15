package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author lixuejun
 * 员工培训经历表
 */
@Entity
@Table(name = "user_info_training_experience")
public class TrainingExperience {

	private String id;
	private String userInfoId; //员工信息ID
	private String trainingAgency; //培训机构
	private String site; ///地点
	private String startDate; //开始时间
	private String endDate; //结束时间
	private String trainingContent; //培训内容
	private String certificate; //获得证书
	private String certificateNumber; //证书编号
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime; 
	
	/** default constructor */
	public TrainingExperience() {
	}
	
	public TrainingExperience(String id) {
		this.id = id;
	}
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "USER_INFO_ID", length = 32)
	public String getUserInfoId() {
		return userInfoId;
	}
	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}
	
	@Column(name = "TRAINING_AGENCY", length = 50)
	public String getTrainingAgency() {
		return trainingAgency;
	}
	public void setTrainingAgency(String trainingAgency) {
		this.trainingAgency = trainingAgency;
	}
	
	@Column(name = "SITE", length = 50)
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	@Column(name = "START_DATE", length = 20)
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Column(name = "END_DATE", length = 20)
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "TRAINING_CONTENT", length = 50)
	public String getTrainingContent() {
		return trainingContent;
	}
	public void setTrainingContent(String trainingContent) {
		this.trainingContent = trainingContent;
	}
	
	@Column(name = "CERTIFICATE", length = 30)
	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	
	@Column(name = "CERTIFICATE_NUMBER", length = 30)
	public String getCertificateNumber() {
		return certificateNumber;
	}
	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	
	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}
