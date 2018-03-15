package com.eduboss.dto;

import com.eduboss.common.ContractType;

/**
 * 专门用于接收合同 搜索参数的 VO
 * @author robinzhang
 *
 */

public class ContractVo {
	private String contractId;
	private ContractType contractType;
	private String signby;
	
	public String getSignby() {
		return signby;
	}
	public void setSignby(String signby) {
		this.signby = signby;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public ContractType getContractType() {
		return contractType;
	}
	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
}
