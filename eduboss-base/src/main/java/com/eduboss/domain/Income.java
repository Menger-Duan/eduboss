package com.eduboss.domain;

import java.util.Date;

public class Income {
	private String id;
	private String contractId;
	private String stuName;
	private int chargeMoney;
	private String chargeType;
	private Date chargeDay;
	private Date signDay;
	private String chargeBy;
	
	public Income(String id, String contractId, String stuName,
			int chargeMoney, String chargeType, Date chargeDay, Date signDay,
			String chargeBy) {
		super();
		this.id = id;
		this.contractId = contractId;
		this.stuName = stuName;
		this.chargeMoney = chargeMoney;
		this.chargeType = chargeType;
		this.chargeDay = chargeDay;
		this.signDay = signDay;
		this.chargeBy = chargeBy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public int getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(int chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public Date getChargeDay() {
		return chargeDay;
	}
	public void setChargeDay(Date chargeDay) {
		this.chargeDay = chargeDay;
	}
	public Date getSignDay() {
		return signDay;
	}
	public void setSignDay(Date signDay) {
		this.signDay = signDay;
	}
	public String getChargeBy() {
		return chargeBy;
	}
	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	
	
	
}
