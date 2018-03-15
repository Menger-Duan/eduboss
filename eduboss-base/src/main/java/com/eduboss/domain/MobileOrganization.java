package com.eduboss.domain;
// default package

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.OrganizationType;
import com.eduboss.dto.UserMobileVo;
import com.eduboss.dto.SelectOptionResponse.NameValue;


/**
 * Organization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="mobile_organization")
public class MobileOrganization implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String id;
    private String name;
    private String parentId;
    private String orgLevel;
    private OrganizationType orgType;
    private int level;
    private Integer orgOrder;
    private List<MobileOrganization> subMobileOrganizations = new ArrayList<MobileOrganization>();
    private Integer userCount;
    private List<UserMobileVo> userList= new ArrayList<UserMobileVo>();

    public MobileOrganization() {
    }

    public MobileOrganization(String id) {
        this.id = id;
    }
   
    @Id
    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
    @Column(name="id", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="name", length=50)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="parentID", length=32)

    public String getParentId() {
        return this.parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

	@Column(name="orgLevel", length=50)
	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	@Column(name="orgOrder")
	public Integer getOrgOrder() {
		return orgOrder;
	}

	public void setOrgOrder(Integer orgOrder) {
		this.orgOrder = orgOrder;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="orgType")
	public OrganizationType getOrgType() {
		return orgType;
	}

	public void setOrgType(OrganizationType orgType) {
		this.orgType = orgType;
	}

	@Column(name="level")
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}


	@Transient
	public List<MobileOrganization> getSubMobileOrganizations() {
		return subMobileOrganizations;
	}

	public void setSubMobileOrganizations(
			List<MobileOrganization> subMobileOrganizations) {
		this.subMobileOrganizations = subMobileOrganizations;
	}

	@Transient
	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	@Transient
	public List<UserMobileVo> getUserList() {
		return userList;
	}

	public void setUserList(List<UserMobileVo> userList) {
		this.userList = userList;
	}

	public MobileOrganization(String id, String name, String parentId,
			String orgLevel, OrganizationType orgType, int level,
			Integer orgOrder, List<MobileOrganization> subMobileOrganizations,
			Integer userCount, List<UserMobileVo> userList) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.orgLevel = orgLevel;
		this.orgType = orgType;
		this.level = level;
		this.orgOrder = orgOrder;
		this.subMobileOrganizations = subMobileOrganizations;
		this.userCount = userCount;
		this.userList = userList;
	}
	
	public MobileOrganization(String id, String name, String parentId,
			String orgLevel) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.orgLevel = orgLevel;
	}

}