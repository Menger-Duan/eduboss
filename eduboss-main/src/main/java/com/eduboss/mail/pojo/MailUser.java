package com.eduboss.mail.pojo;

import org.apache.commons.lang3.StringUtils;

/**@author wmy
 *@date 2015年9月24日上午11:37:28
 *@version 1.0 
 *@description 邮箱用户
 */
public class MailUser {
	
	/**
	 * 用户的provider id, 必须为存在的provider
	 */
	private String providerId = "1";
	/**
	 * 用户的组织id, 必须为存在的组织
	 */
	private String org_id;
	/**
	 * 用户id, 满足rfc规定的用户名
	 */
	private String user_id;
	
	//attr
	/**
	 * 用户邮箱密码
	 */
	private String password;
	/**
	 * 用户所在域名 (只读属性)
	 */
	private String domain_name;
	/**
	 * 用户的COS（服务等级）
	 */
	private Integer cos_id;	
	/**
	 * 用户状态，0为活动用户
	 */
	private Integer user_status;
	/**
	 * 用户部门id 
	 */
	private String org_unit_id;	
	/**
	 * 真实姓名
	 */
	private String true_name;
	/**
	 * 手机号码
	 */
	private String mobile_number;	
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 用户排序越大越前
	 */
	private Integer user_list_rank;
	/**
	 * 职位
	 */
	private String duty;
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id.toLowerCase();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDomain_name() {
		return domain_name;
	}
	public void setDomain_name(String domain_name) {
		this.domain_name = domain_name;
	}
	public Integer getCos_id() {
		return cos_id;
	}
	public void setCos_id(Integer cos_id) {
		this.cos_id = cos_id;
	}
	public Integer getUser_status() {
		return user_status;
	}
	public void setUser_status(Integer user_status) {
		this.user_status = user_status;
	}
	public String getOrg_unit_id() {
		return org_unit_id;
	}
	public void setOrg_unit_id(String org_unit_id) {
		this.org_unit_id = org_unit_id;
	}
	public String getTrue_name() {
		return true_name;
	}
	public void setTrue_name(String true_name) {
		this.true_name = true_name;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getUser_list_rank() {
		return user_list_rank;
	}
	public void setUser_list_rank(Integer user_list_rank) {
		this.user_list_rank = user_list_rank;
	}
	
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	/**
	 * 
	 * @return
	 * @description 获取符合要求的属性字符串 editType: 0新加时，1更新时
	 * @author wmy
	 * @date 2015年9月24日上午10:13:39
	 * @return String
	 */
	public String getAttrs(Integer editType){
		String ret = null;
    	StringBuffer attrs = new StringBuffer();
    	if (StringUtils.isNotBlank(this.password)) {
    		attrs.append("&password=" + this.password);
    	}
    	if (StringUtils.isNotBlank(this.domain_name) && editType == 0) {
    		attrs.append("&domain_name=" + this.domain_name);
    	}
    	if (this.cos_id != null) {
    		attrs.append("&cos_id=" + this.cos_id.toString());
    	}
    	if (this.user_status != null) {
    		attrs.append("&user_status=" + this.user_status.toString());
    	}
    	if (StringUtils.isNotBlank(this.org_unit_id)) {
    		attrs.append("&org_unit_id=" + this.org_unit_id);
    	}
    	if (StringUtils.isNotBlank(this.true_name)) {
    		attrs.append("&true_name=" + this.true_name);
    	}
    	if (StringUtils.isNotBlank(this.mobile_number)) {
    		attrs.append("&mobile_number=" + this.mobile_number);
    	}
    	if (StringUtils.isNotBlank(this.gender)) {
    		attrs.append("&gender=" + this.gender);
    	}   	
    	if (user_list_rank != null) {
    		attrs.append("&user_list_rank=" + this.user_list_rank.toString());
    	}
    	if (StringUtils.isNotBlank(this.duty)) {
    		attrs.append("&duty=" + this.duty);
    	}
    	if(StringUtils.isNotBlank(attrs)) ret = attrs.toString().substring(1);
    	return ret;
	}
	
}



