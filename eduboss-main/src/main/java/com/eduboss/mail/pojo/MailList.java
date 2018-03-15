package com.eduboss.mail.pojo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;

/**@author wmy
 *@date 2015年9月24日上午11:38:15
 *@version 1.0 
 *@description 邮件列表
 */
public class MailList {
	/**
	 * 默认为1
	 */
	private String providerId = "1";
	/**
	 * 邮件列表所在组织
	 */
	private String orgId;
	/**
	 * 邮件列表标识
	 */
	private String mailListId;  //对应本系统的角色Id
	
	//attr
	/**
	 * 邮件列表服务等级 
	 * 邮件列表对象和普通用户最主要的区别是它们使用特定类型的 COS, 在系统默认安装的情况下, 
	 * 这个特定 COS (我们叫做邮件列表COS) 的 cos_id 是 3, 如果不是默认安装请咨询系统SA或者登录到siteadmin查询) 去查看
	 */
	private Integer cosId;
	/**
	 * (必须设置的属性之1)自动转发, 此属性必须置1, 打开转发功能
	 */
	private Integer forwardActive;
	/**
	 * (必须设置的属性之2)此属性必须置0, 表示转发后不保留本地副本 
	 */
	private Integer keepLocal;
	/**
	 * (必须设置的属性之3 )对垃圾邮件的处理方式, 此属性必须置为1, 拒收所有未被允许的用户发过来的邮件 
	 */
	private Integer rejectJunk;
	/**
	 * 邮件列表状态 0:活跃 1:关闭(禁止) 
	 */
	private Integer userStatus;
	/**
	 * 邮件列表的名字 
	 */
	private String trueName;
	/**
	 * 列表成员 (静态),逗号分隔的邮件地址 
	 */
	private String forwardMailList;  //当值为"empty",把邮件列表的用户置空
	/**
	 * 允许谁发往此列表 ,0:允许所有人 2:允许列表中及指定授权用户 3:只允许指定授权用户
	 */
	private Integer junkFilter;
	/**
	 * 授权使用者,逗号分隔的邮件地址 
	 */
	private String safeList;
	/**
	 * 禁止使用者,逗号分隔的邮件地址
	 */
	private String refuseList;
	/**
	 * 是否在组织地址本中显示 ,0：不显示 (一般置0) 2：显示 
	 */
	private Integer privacyLevel;
	/**
	 * 邮箱列表域名
	 */
	private String domainName;
	/**
	 * 部门
	 */
	private String orgUnitId;
	
	/**
	 * 用户排序越大越前
	 */
	private Integer mailListRank;
	
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getMailListId() {
		return mailListId;
	}
	public void setMailListId(String mailListId) {
		this.mailListId = mailListId.toLowerCase();
	}
	public Integer getCosId() {
		return cosId;
	}
	public void setCosId(Integer cosId) {
		this.cosId = cosId;
	}
	public Integer getForwardActive() {
		return forwardActive;
	}
	public void setForwardActive(Integer forwardActive) {
		this.forwardActive = forwardActive;
	}
	public Integer getKeepLocal() {
		return keepLocal;
	}
	public void setKeepLocal(Integer keepLocal) {
		this.keepLocal = keepLocal;
	}
	public Integer getRejectJunk() {
		return rejectJunk;
	}
	public void setRejectJunk(Integer rejectJunk) {
		this.rejectJunk = rejectJunk;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getForwardMailList() {
		return forwardMailList;
	}
	public void setForwardMailList(String forwardMailList) {
		this.forwardMailList = forwardMailList;
	}
	public Integer getJunkFilter() {
		return junkFilter;
	}
	public void setJunkFilter(Integer junkFilter) {
		this.junkFilter = junkFilter;
	}
	public String getSafeList() {
		return safeList;
	}
	public void setSafeList(String safeList) {
		this.safeList = safeList;
	}
	public String getRefuseList() {
		return refuseList;
	}
	public void setRefuseList(String refuseList) {
		this.refuseList = refuseList;
	}
	public Integer getPrivacyLevel() {
		return privacyLevel;
	}
	public void setPrivacyLevel(Integer privacyLevel) {
		this.privacyLevel = privacyLevel;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public String getOrgUnitId() {
		return orgUnitId;
	}
	public void setOrgUnitId(String orgUnitId) {
		this.orgUnitId = orgUnitId;
	}
	
	public Integer getMailListRank() {
		return mailListRank;
	}
	public void setMailListRank(Integer mailListRank) {
		this.mailListRank = mailListRank;
	}
	/**
	 * 
	 * @return
	 * @description 获取符合要求的属性字符串 editType: 0添加， 1更新(因为个别属性不可更新)
	 */
	public String getAttrs(Integer editType){
		String ret = null;
    	StringBuffer attrs = new StringBuffer();
    	if (this.cosId != null) {
    		attrs.append("&cos_id=" + this.cosId.toString());
    	}
    	if (this.forwardActive != null) {
    		attrs.append("&forwardactive=" + this.forwardActive.toString());
    	}
    	if (this.keepLocal != null) {
    		attrs.append("&keeplocal=" + this.keepLocal.toString());
    	}
    	if (this.rejectJunk != null) {
    		attrs.append("&rejectjunk=" + this.rejectJunk.toString());
    	}
    	if (this.userStatus != null) {
    		attrs.append("&user_status=" + this.userStatus.toString());
    	}
    	if (StringUtils.isNotBlank(this.trueName)) {
    		attrs.append("&true_name=" + this.trueName);
    	}
    	if (StringUtils.isNotBlank(this.forwardMailList)) {
    		if("empty".equalsIgnoreCase(this.forwardMailList)) {  //置为空，empty
    			attrs.append("&forwardmaillist=");
    		}else{
        		try {
    				attrs.append("&forwardmaillist=" + URLEncoder.encode(this.forwardMailList, "GBK"));
    			} catch (UnsupportedEncodingException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	if (this.junkFilter != null) {
    		attrs.append("&junkfilter=" + this.junkFilter.toString());
    	}
    	if (StringUtils.isNotBlank(this.safeList)) {
    		attrs.append("&safelist=" + this.safeList);
    	}
    	if (StringUtils.isNotBlank(this.refuseList)) {
    		attrs.append("&refuselist=" + this.refuseList);
    	}
    	if (this.privacyLevel != null) {
    		attrs.append("&privacy_level=" + this.privacyLevel.toString());
    	}  	
    	if (StringUtils.isNotBlank(this.domainName) && editType == 0) {
    		attrs.append("&domain_name=" + this.domainName);
    	}
    	if (mailListRank != null) {
    		attrs.append("&user_list_rank=" + this.mailListRank.toString());
    	}
    	if (StringUtils.isNotBlank(this.orgUnitId)) {
    		attrs.append("&org_unit_id=" + this.orgUnitId);
    	}
    	if(StringUtils.isNotBlank(attrs)) ret = attrs.toString().substring(1);
    	return ret;
	}
	
}


