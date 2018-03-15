package com.eduboss.domainVo;

import java.util.ArrayList;
import java.util.List;
import com.eduboss.dto.UserMobileVo;
// default package

public class MobileOrganizationVo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
    private String name;
    private String parentId;
    private String orgLevel;
    private String orgType;
    private int level;
    private Integer orgOrder;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getOrgLevel() {
		return orgLevel;
	}
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Integer getOrgOrder() {
		return orgOrder;
	}
	public void setOrgOrder(Integer orgOrder) {
		this.orgOrder = orgOrder;
	}
	public MobileOrganizationVo(String id, String name, String parentId,
			String orgLevel, String orgType, int level,Integer orgOrder) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.orgLevel = orgLevel;
		this.orgType = orgType;
		this.level = level;
		this.orgOrder=orgOrder;
	}
	public MobileOrganizationVo() {
	}
    

}