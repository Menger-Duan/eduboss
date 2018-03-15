package com.eduboss.dto;


/**
 * @author ban
 *
 */
public class ImeiInfo extends Response {
	
	private String id;
	private String tmHeader;
	private String brend;
	private String model;
	private String productLocation;
	private String sellLocation;
	private String remark;
	
	public ImeiInfo(){}
	
	public ImeiInfo(String tmHeader, String brend, String model,
			String productLocation, String sellLocation) {
		super();
		this.tmHeader = tmHeader;
		this.brend = brend;
		this.model = model;
		this.productLocation = productLocation;
		this.sellLocation = sellLocation;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the tmHeader
	 */
	public String getTmHeader() {
		return tmHeader;
	}
	/**
	 * @param tmHeader the tmHeader to set
	 */
	public void setTmHeader(String tmHeader) {
		this.tmHeader = tmHeader;
	}
	/**
	 * @return the brend
	 */
	public String getBrend() {
		return brend;
	}
	/**
	 * @param brend the brend to set
	 */
	public void setBrend(String brend) {
		this.brend = brend;
	}
	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return the productLocation
	 */
	public String getProductLocation() {
		return productLocation;
	}
	/**
	 * @param productLocation the productLocation to set
	 */
	public void setProductLocation(String productLocation) {
		this.productLocation = productLocation;
	}
	/**
	 * @return the sellLocation
	 */
	public String getSellLocation() {
		return sellLocation;
	}
	/**
	 * @param sellLocation the sellLocation to set
	 */
	public void setSellLocation(String sellLocation) {
		this.sellLocation = sellLocation;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
