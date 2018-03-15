package com.eduboss.domainVo;

import java.io.Serializable;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ValidStatus;

public class ResourcePoolUserVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String parentId;
	private OrganizationType orgType;
	private String orgLevel;
    private String resourcePoolName;// 资源池名称
    private ValidStatus resourcePoolstatus; //状态 (有效，无效)
    private boolean isParent;
 
    //新增 20170304 用于配置跨部门节点状态标记
    private Integer nodeState; //0： 不可见1：可见节点
    private Integer childNodeNums;//子节点个数
    
    //新增
    private Integer current;//当前的资源数
    private Integer total;//最多可以拥有的资源数
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public OrganizationType getOrgType() {
		return orgType;
	}
	public void setOrgType(OrganizationType orgType) {
		this.orgType = orgType;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getResourcePoolName() {
		return resourcePoolName;
	}
	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}
	public ValidStatus getResourcePoolstatus() {
		return resourcePoolstatus;
	}
	public void setResourcePoolstatus(ValidStatus resourcePoolstatus) {
		this.resourcePoolstatus = resourcePoolstatus;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public Integer getCurrent() {
		return current;
	}
	public void setCurrent(Integer current) {
		this.current = current;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getNodeState() {
		return nodeState;
	}
	public void setNodeState(Integer nodeState) {
		this.nodeState = nodeState;
	}
	public Integer getChildNodeNums() {
		return childNodeNums;
	}
	public void setChildNodeNums(Integer childNodeNums) {
		this.childNodeNums = childNodeNums;
	}
		
    
}
