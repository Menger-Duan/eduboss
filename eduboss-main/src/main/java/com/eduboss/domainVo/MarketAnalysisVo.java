package com.eduboss.domainVo;

public class MarketAnalysisVo {

	/** 组织架构 */
	String organizationName;
	/** 资源来源 */
	String resourceTypeName;
	/** 相应级别（如校区）合同数量 */
	String numberOfContract;
	/** 相应级别（如校区）合同占比 */
	String contractPercent;
	/** 相应级别（如校区）合同总额 */
	String contractAmount;
	/** 相应级别（如校区）合同总额占比 */
	String contractAmountPercent;

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public String getNumberOfContract() {
		return numberOfContract;
	}

	public void setNumberOfContract(String numberOfContract) {
		this.numberOfContract = numberOfContract;
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

	public String getContractAmountPercent() {
		return contractAmountPercent;
	}

	public void setContractAmountPercent(String contractAmountPercent) {
		this.contractAmountPercent = contractAmountPercent;
	}

}
