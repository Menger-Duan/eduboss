package com.eduboss.mail.pojo;

import org.apache.commons.lang3.StringUtils;

/**@author wmy
 *@date 2015年9月24日上午10:07:19
 *@version 1.0 
 *@description 部门
 */
public class MailUnit {
	/**
	 * 部门所在组织标识
	 */
	private String org_id;
	/**
	 * 部门标识
	 */
	private String org_unit_id;
	/**
	 * 部门名称
	 */
	private String org_unit_name;
	/**
	 * 部门所属上级部门标识
	 */
	private String parent_org_unit_id;
	/**
	 * 部门排序，排序以数字大的优先，即数字越大，排位越靠前。
	 */
	private Integer org_unit_list_rank;
	
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getOrg_unit_id() {
		return org_unit_id;
	}
	public void setOrg_unit_id(String org_unit_id) {
		this.org_unit_id = org_unit_id;
	}
	public String getOrg_unit_name() {
		return org_unit_name;
	}
	public void setOrg_unit_name(String org_unit_name) {
		this.org_unit_name = org_unit_name;
	}
	public String getParent_org_unit_id() {
		return parent_org_unit_id;
	}
	public void setParent_org_unit_id(String parent_org_unit_id) {
		this.parent_org_unit_id = parent_org_unit_id;
	}
	public Integer getOrg_unit_list_rank() {
		return org_unit_list_rank;
	}
	public void setOrg_unit_list_rank(Integer org_unit_list_rank) {
		this.org_unit_list_rank = org_unit_list_rank;
	}
	/**
	 * 
	 * @return
	 * @description 获取符合要求的属性字符串
	 * @author wmy
	 * @date 2015年9月24日上午10:13:39
	 * @return String
	 */
	public String getAttrs(){
		String ret = null;
    	StringBuffer attrs = new StringBuffer();
    	if (StringUtils.isNotBlank(this.org_unit_name)) {
    		attrs.append("&org_unit_name=" + this.org_unit_name);
    	}
    	if (StringUtils.isNotBlank(this.parent_org_unit_id)) {
    		attrs.append("&parent_org_unit_id=" + this.parent_org_unit_id);
    	}
    	if (this.org_unit_list_rank != null){
    		attrs.append("&org_unit_list_rank=" + this.org_unit_list_rank.toString());
    	}
    	if(StringUtils.isNotBlank(attrs)) ret = attrs.toString().substring(1);
    	return ret;
	}
}


