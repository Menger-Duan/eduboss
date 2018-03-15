package com.eduboss.domainVo;

public class MiniClassProductVo {

	// Fields
	private String id;
	private String miniClassId;
	private String miniClassName;
	private String productId;
	private String productName;
	private String isMainProduct; // 是否为主产品(1：是；0：否)
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	
	private String classType;
	
	private String productVersion;
	private String productQuarter;
	private String productGrade;

	private String flagOfChargeCourse; // 标记小班是否已经有： 已结算 已取消 的课程
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMiniClassId() {
		return miniClassId;
	}
	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}
	public String getMiniClassName() {
		return miniClassName;
	}
	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getIsMainProduct() {
		return isMainProduct;
	}
	public void setIsMainProduct(String isMainProduct) {
		this.isMainProduct = isMainProduct;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getModifyUserId() {
		return modifyUserId;
	}
	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	public String getProductQuarter() {
		return productQuarter;
	}
	public void setProductQuarter(String productQuarter) {
		this.productQuarter = productQuarter;
	}
	public String getProductGrade() {
		return productGrade;
	}
	public void setProductGrade(String productGrade) {
		this.productGrade = productGrade;
	}

	public String getFlagOfChargeCourse() {
		return flagOfChargeCourse;
	}

	public void setFlagOfChargeCourse(String flagOfChargeCourse) {
		this.flagOfChargeCourse = flagOfChargeCourse;
	}
}
