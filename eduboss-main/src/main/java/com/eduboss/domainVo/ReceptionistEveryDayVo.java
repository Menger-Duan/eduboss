package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;


public class ReceptionistEveryDayVo {
	private String id;
	private String loginDate;
	private BigDecimal telNum;
	private BigDecimal telNumw;
	private BigDecimal visitNumz;
	private BigDecimal visitNumy;
	private String writeTime;
	
	private String userId;
	private String userName;
	
	private String organizationId;
	private String orgName;
	
	private RoleCode role;
	private OrganizationType organizationType;
	private BasicOperationQueryLevelType basicOperationQueryLevelType;
	
	/** 新增资源数 */
	private BigDecimal customerNums;
	
	/** 合同数 */
	private BigDecimal contractNums;
	
	/** 未登记数*/
	private BigDecimal loginNumsw;
	
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginDate() {
		return loginDate;
	}
	
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
	}
	public BigDecimal getTelNum() {
		return telNum;
	}
	public void setTelNum(BigDecimal telNum) {
		this.telNum = telNum;
	}
	public BigDecimal getTelNumw() {
		return telNumw;
	}
	public void setTelNumw(BigDecimal telNumw) {
		this.telNumw = telNumw;
	}
	public BigDecimal getVisitNumz() {
		return visitNumz;
	}
	public void setVisitNumz(BigDecimal visitNumz) {
		this.visitNumz = visitNumz;
	}
	public BigDecimal getVisitNumy() {
		return visitNumy;
	}
	public void setVisitNumy(BigDecimal visitNumy) {
		this.visitNumy = visitNumy;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getWriteTime() {
		return writeTime;
	}

	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public BigDecimal getCustomerNums() {
		return customerNums;
	}

	public void setCustomerNums(BigDecimal customerNums) {
		this.customerNums = customerNums;
	}

	public BigDecimal getContractNums() {
		return contractNums;
	}

	public void setContractNums(BigDecimal contractNums) {
		this.contractNums = contractNums;
	}

	public BigDecimal getLoginNumsw() {
		return loginNumsw;
	}

	public void setLoginNumsw(BigDecimal loginNumsw) {
		this.loginNumsw = loginNumsw;
	}

	public RoleCode getRole() {
		return role;
	}

	public void setRole(RoleCode role) {
		this.role = role;
	}

	public OrganizationType getOrganizationType() {
		return organizationType;
	}

	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public BasicOperationQueryLevelType getBasicOperationQueryLevelType() {
		return basicOperationQueryLevelType;
	}

	public void setBasicOperationQueryLevelType(
			BasicOperationQueryLevelType basicOperationQueryLevelType) {
		this.basicOperationQueryLevelType = basicOperationQueryLevelType;
	}

		
	

}
