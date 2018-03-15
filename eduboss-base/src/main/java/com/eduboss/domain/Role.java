package com.eduboss.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.RoleCode;
import com.eduboss.common.RoleType;
import com.eduboss.common.ValidStatus;
import com.eduboss.dto.SelectOptionResponse.NameValue;

/**
 * Role entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "role")
public class Role implements java.io.Serializable, NameValue {

	// Fields

	private String roleId;
	private String name;
	private String remark;
	private RoleCode roleCode;
	
	private Short roleLevel;//职位等级
	private String selectedResIds;
	
	private Short mailListStatus; //邮件列表状态
	
	private String roleSign;  //角色标识
	
	//用户人数
	private Integer userAmount;
	private Integer realCount;
	
	private List<User> users = new ArrayList<User>();
	
	private List<Resource> resources = new ArrayList<Resource>();
	
	private List<SystemNotice> systemNotices = new ArrayList<SystemNotice>();
	
	private List<String> relateRoleIds = new ArrayList<String>();
	
	private String createTime;
	
	private String modifyTime;
	
	private ValidStatus roleStatus; //状态
	
	private RoleType roleType; //角色类型
	
	private ValidStatus resourceStatus;//客户资源状态
	
	private Integer resourceNum;//资源容量
	
	private Integer returnCycle;//回收周期
	
	private Integer cycleType;//回收类型 0:不回收，1：可回收;	
	
	
	// Constructors

	/** default constructor */
	public Role() {
	}

	/** minimal constructor */
	public Role(String id) {
		this.roleId = id;
	}

	/** full constructor */
	public Role(String id, String name, String remark) {
		this.roleId = id;
		this.name = name;
		this.remark = remark;
	}

	// Property accessors
	@GenericGenerator(name = "roleIdGenerator", strategy = "com.eduboss.dto.DispNoGenerator")
	@Id
	@GeneratedValue(generator = "roleIdGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String id) {
		this.roleId = id;
	}

	@Column(name = "name", length = 20)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "remark", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "roleCode", length = 30)
	public RoleCode getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(RoleCode roleCode) {
		this.roleCode = roleCode;
	}
	
//	@ManyToMany(mappedBy = "role", fetch = FetchType.EAGER)
	@Transient
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
//	@ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
	@Transient
	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	@Transient
	public String getValue() {
		return this.getRoleId();
	}

	/**
	 * @return the selectedResIds
	 */
	@Transient
	public String getSelectedResIds() {
		return selectedResIds;
	}

	/**
	 * @param selectedResIds the selectedResIds to set
	 */
	public void setSelectedResIds(String selectedResIds) {
		this.selectedResIds = selectedResIds;
	}
	
	@Column(name = "ROLE_LEVEL")
	public Short getRoleLevel() {
		return this.roleLevel;
	}

	public void setRoleLevel(Short roleLevel) {
		this.roleLevel = roleLevel;
	}
	
	@Column(name = "MAILLIST_STATUS")
	public Short getMailListStatus() {
		return mailListStatus;
	}

	public void setMailListStatus(Short mailListStatus) {
		this.mailListStatus = mailListStatus;
	}
	
	@Column(name = "ROLE_SIGN")
	public String getRoleSign() {
		return roleSign;
	}

	public void setRoleSign(String roleSign) {
		this.roleSign = roleSign;
	}

	@Transient
	public Integer getUserAmount() {
		return userAmount;
	}

	public void setUserAmount(Integer userAmount) {
		this.userAmount = userAmount;
	}
	
	@Transient
	public Integer getRealCount() {
		return realCount;
	}

	public void setRealCount(Integer realCount) {
		this.realCount = realCount;
	}

	//@ManyToMany(mappedBy = "role",cascade = {CascadeType.MERGE },fetch = FetchType.EAGER)
	@Transient
	public List<SystemNotice> getSystemNotices() {
		return systemNotices;
	}

	public void setSystemNotices(List<SystemNotice> systemNotices) {
		this.systemNotices = systemNotices;
	}

	@Column(name = "CREATE_TIME", columnDefinition="timestamp", insertable = false, updatable = false)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME", columnDefinition="timestamp", insertable = false, updatable = false)
	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="ROLE_STATUS")
	public ValidStatus getRoleStatus() {
		return roleStatus;
	}
	
	public void setRoleStatus(ValidStatus roleStatus) {
		this.roleStatus = roleStatus;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="ROLE_TYPE")
	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name="RESOURCE_STATUS")
	public ValidStatus getResourceStatus() {
		return resourceStatus;
	}

	public void setResourceStatus(ValidStatus resourceStatus) {
		this.resourceStatus = resourceStatus;
	}
	
	@Column(name="RESOURCE_NUM")
	public Integer getResourceNum() {
		return resourceNum;
	}

	public void setResourceNum(Integer resourceNum) {
		this.resourceNum = resourceNum;
	}
	
	@Column(name="RETURN_CYCLE")
	public Integer getReturnCycle() {
		return returnCycle;
	}

	public void setReturnCycle(Integer returnCycle) {
		this.returnCycle = returnCycle;
	}
	
	@Column(name="CYCLE_TYPE")
	public Integer getCycleType() {
		return cycleType;
	}

	public void setCycleType(Integer cycleType) {
		this.cycleType = cycleType;
	}
	
	@Transient
	public List<String> getRelateRoleIds() {
		return relateRoleIds;
	}

	public void setRelateRoleIds(List<String> relateRoleIds) {
		this.relateRoleIds = relateRoleIds;
	}
	
	
	
}