package com.eduboss.mail.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author lixuejun
 * @date 2015-12-10 16:34
 * @version 1.0 
 * @description 外部联系人
 */
public class MailExternalContract {

	private String objUid; // 对象标识
	private String obj_email; // 对象的 email 地址 
	private String org_id; // 对象所在组织标识
	private String obj_class; // 对象类型, 1 表示外部联系人, 其它值暂时保留
	private String org_unit_id; // 对象所在部门标识, null 表示组织直属
	private String privacy_level; // 对象是否公开(在组织通讯录中显示): 2 表示组织内公开, 4 表示全站公开
	
	private String true_name; // 用户姓名
	private String mobile_number; // 手机号码
	private String duty; //职位
	public String getObjUid() {
		return objUid;
	}
	public void setObjUid(String objUid) {
		this.objUid = objUid;
	}
	public String getObj_email() {
		return obj_email;
	}
	public void setObj_email(String obj_email) {
		this.obj_email = obj_email;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getObj_class() {
		return obj_class;
	}
	public void setObj_class(String obj_class) {
		this.obj_class = obj_class;
	}
	public String getOrg_unit_id() {
		return org_unit_id;
	}
	public void setOrg_unit_id(String org_unit_id) {
		this.org_unit_id = org_unit_id;
	}
	public String getPrivacy_level() {
		return privacy_level;
	}
	public void setPrivacy_level(String privacy_level) {
		this.privacy_level = privacy_level;
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
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}
	
	/**
	 * 
	 * @return
	 * @description 获取符合要求的属性字符串 
	 * @author lixuejun
	 * @date 2015-12-10 17:41
	 * @return String
	 */
	public String getAttrs(){
		String ret = null;
    	StringBuffer attrs = new StringBuffer();
    	if (StringUtils.isNotBlank(this.objUid )) {
    		attrs.append("&objUid =" + this.objUid );
    	}
    	if (StringUtils.isNotBlank(this.obj_email)) {
    		attrs.append("&obj_email=" + this.obj_email);
    	}
    	if (StringUtils.isNotBlank(this.org_id)) {
    		attrs.append("&org_id=" + this.org_id);
    	}
    	if (StringUtils.isNotBlank(this.obj_class)) {
    		attrs.append("&obj_class=" + this.obj_class);
    	}
    	if (StringUtils.isNotBlank(this.org_unit_id)) {
    		attrs.append("&org_unit_id=" + this.org_unit_id);
    	}
    	if (StringUtils.isNotBlank(this.privacy_level)) {
    		attrs.append("&privacy_level=" + this.privacy_level);
    	}
    	if (StringUtils.isNotBlank(this.true_name)) {
    		attrs.append("&true_name=" + this.true_name);
    	}
    	if (StringUtils.isNotBlank(this.mobile_number)) {
    		attrs.append("&mobile_number=" + this.mobile_number);
    	}  
    	if (StringUtils.isNotBlank(this.duty)) {
    		attrs.append("&duty=" + this.duty);
    	} 
    	if(StringUtils.isNotBlank(attrs)) ret = attrs.toString().substring(1);
    	return ret;
	}
}
