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
 * 合同签订
 */
@Entity
@Table(name = "user_info_contract_signing")
public class ContractSigning {

	private String id;
	private String userInfoId; //员工信息ID
	private String emplContractId; //合同编号
	private String startDate; //开始时间
	private String endDate; //结束时间
	private String signingTime; //签订时间
	private String probationPeriod; //试用期
	private String remark; //备注
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime; 
	
	/** default constructor */
	public ContractSigning() {
	}
	
	public ContractSigning(String id) {
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
	
	@Column(name = "EMPL_CONTRACT_ID", length = 32)
	public String getEmplContractId() {
		return emplContractId;
	}
	public void setEmplContractId(String emplContractId) {
		this.emplContractId = emplContractId;
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
	
	@Column(name = "SIGNING_TIME", length = 32)
	public String getSigningTime() {
		return signingTime;
	}
	public void setSigningTime(String signingTime) {
		this.signingTime = signingTime;
	}
	
	@Column(name = "PROBATION_PERIOD", length = 20)
	public String getProbationPeriod() {
		return probationPeriod;
	}
	public void setProbationPeriod(String probationPeriod) {
		this.probationPeriod = probationPeriod;
	}
	
	@Column(name = "REMARK", length = 100)
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
