package com.eduboss.domainVo;

public class MarketTypeAnalysisVo {

	/** 集团名*/
	String groupName;
	/** 分公司名*/
	String branchName;
	/** 校区名集团名*/
	String campusName;
	/** 资源类型*/
	String resourceTypeName;
	/** 校区合同数量*/
    String contractNumber;
    /** 校区合同占比*/
    String contractPercent;
    /** 校区合同总额*/
    String contractAmount;
    /** 校区合同总额占比*/
    String contractAPercent;
    
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}
	public String getContractNumber() {
		return contractNumber;
	}
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getContractPercent() {
		return contractPercent;
	}
	public void setContractPercent(String contractPercent) {
		this.contractPercent = contractPercent;
	}
	public String getContractAmount() {
		return contractAmount;
	}
	public void setContractAmount(String contractAmount) {
		this.contractAmount = contractAmount;
	}
	public String getContractAPercent() {
		return contractAPercent;
	}
	public void setContractAPercent(String contractAPercent) {
		this.contractAPercent = contractAPercent;
	}    
    
	
}
