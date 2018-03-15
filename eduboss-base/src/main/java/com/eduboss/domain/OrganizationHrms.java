package com.eduboss.domain;

import com.eduboss.common.OrganizationTypeHrms;

import javax.persistence.*;


/**
 * 人事组织架构表
 */
@Entity
@Table(name="organization_hrms")
public class OrganizationHrms implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String parentId;
	private OrganizationTypeHrms orgType;
	private String subOrgType;
	private String orgLevel;//组织架构编号
	private Integer status;//有效性：0--有效，1--无效
	private String type;
	private String description;//部门职能
	private String province;
	private String city;
	private String contact;
	private String address;
	private String lon; // 经度
	private String lat;  //纬度
	private Integer operateStatus;//操作状态：1--可用的，2--临时，99--物理删除
	private String createTime;
	private String createUserId;
	private String updateTime;
	private String updateUserId;
	private Integer orgOrder;
	private String pendingEffectCopy;//未生效组织架构副本
	private String tLevel;//所属T级
	private String orgSign;//机构标识（用于邮箱系统同步）

    public OrganizationHrms() {
    }
    public OrganizationHrms(String id) {
        this.id = id;
    }

    @Id
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
    
    @Column(name="parent_id", length=32)
    public String getParentId() {
        return this.parentId;
    }
    
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

	@Column(name="org_level", length=50)
	public String getOrgLevel() {
		return orgLevel;
	}

	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	@Column(name="org_order")
	public Integer getOrgOrder() {
		return orgOrder;
	}

	public void setOrgOrder(Integer orgOrder) {
		this.orgOrder = orgOrder;
	}


	@Column(name = "ORG_SIGN")
	public String getOrgSign() {
		return orgSign;
	}

	public void setOrgSign(String orgSign) {
		this.orgSign = orgSign;
	}

	@Column(name = "lon")
	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Column(name = "lat")
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "org_type")
	public OrganizationTypeHrms getOrgType() {
		return orgType;
	}

	public void setOrgType(OrganizationTypeHrms orgType) {
		this.orgType = orgType;
	}

	@Column(name = "sub_org_type")
	public String getSubOrgType() {
		return subOrgType;
	}

	public void setSubOrgType(String subOrgType) {
		this.subOrgType = subOrgType;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "contact")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "operate_status")
	public Integer getOperateStatus() {
		return operateStatus;
	}

	public void setOperateStatus(Integer operateStatus) {
		this.operateStatus = operateStatus;
	}

	@Column(name = "create_time")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_user_id")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "update_time")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name = "update_user_id")
	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "pending_Effect_Copy")
	public String getPendingEffectCopy() {
		return pendingEffectCopy;
	}

	public void setPendingEffectCopy(String pendingEffectCopy) {
		this.pendingEffectCopy = pendingEffectCopy;
	}

	@Column(name = "t_level")
	public String getTLevel() {
		return tLevel;
	}

	public void setTLevel(String tLevel) {
		this.tLevel = tLevel;
	}
}