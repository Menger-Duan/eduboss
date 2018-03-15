package com.eduboss.domain;
// default package

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.eduboss.common.StateOfEmergency;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ValidStatus;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import tebie.applib.api.S;


/**
 * Organization entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="organization")
public class Organization implements java.io.Serializable,NameValue {

	
    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
     private String name;
     private String parentId;
     private String regionId;//归属地区
     private String remark;
     private String orgLevel;
     private Integer orgOrder;
     private OrganizationType orgType;
     
//     private String provinceId;
     private String address;//地址
	private String lon; // 经度
	private String lat;  //纬度
     private String contact;//电话
     private String belong;//统筹归属
     
     // 客户资源池配置 旧逻辑
     private String customerPoolName;//CUSTOMER_POOL_NAME
     private String isPublicPool;//IS_PUBLIC_POOL
     private String accessRoles;//ACCESS_ROLES
     
     // 资源池配置 新逻辑
     private String resourcePoolName;// 资源池名称
     private ValidStatus resourcePoolstatus; //状态 (有效，无效)
     
     private String orgSign;//ORG_SIGN 组织标识
     
	private List<User> users = new ArrayList<User>();
	
	private List<SystemNotice> systemNotices = new ArrayList<SystemNotice>();
	
	
	private String sybAppId;
	private String sybCusId;
	private String sybAppKey;

	private String provinceId;	//省份
	private String cityId;	//城市
	private String areaId;	//地区

	private Region province;	//省份
	private Region city;	//城市
	private Region area;	//地区

	private StateOfEmergency stateOfEmergency;//紧急状态

	private Integer status;		//状态
	private String hrmsId;	//人事ID
	private Integer bossUse;   //boss是否使用
	/**
	 * 交通信息
	 */
	private String trafficInfo;
	/**
	 * 是否有附加信息
	 */
	private Integer hasAppend;
	/**
	 * 联系人
	 */
	private String contactUser;

    // Constructors

    /** default constructor */
    public Organization() {
    }

	/** minimal constructor */
    public Organization(String id) {
        this.id = id;
    }
    
    /** full constructor */
    public Organization(String name, String parentId, String remark) {
        this.name = name;
        this.parentId = parentId;
        this.remark = remark;
    }

   
    // Property accessors
    @Id
//    @GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
//	@GeneratedValue(generator = "generator")
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
    
    @Column(name="remark", length=100)

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

	@Transient
	public String getValue() {
		return this.id;
	}

//	@ManyToMany(mappedBy = "organization",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
	@Transient
	public List<User> getUsers() {
		return users;
	}

//	@Transient
//	public String getProvinceId() {
//		return provinceId;
//	}
//
//	public void setProvinceId(String provinceId) {
//		this.provinceId = provinceId;
//	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the orgLevel
	 */
	@Column(name="orgLevel", length=50)
	public String getOrgLevel() {
		return orgLevel;
	}

	/**
	 * @param orgLevel the orgLevel to set
	 */
	public void setOrgLevel(String orgLevel) {
		this.orgLevel = orgLevel;
	}

	/**
	 * @return the orgOrder
	 */
	@Column(name="orgOrder")
	public Integer getOrgOrder() {
		return orgOrder;
	}

	/**
	 * @param orgOrder the orgOrder to set
	 */
	public void setOrgOrder(Integer orgOrder) {
		this.orgOrder = orgOrder;
	}

	/**
	 * @return the orgType
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="orgType")
	public OrganizationType getOrgType() {
		return orgType;
	}

	/**
	 * @param orgType the orgType to set
	 */
	public void setOrgType(OrganizationType orgType) {
		this.orgType = orgType;
	}

	@Column(name = "REGION_ID")
	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "BELONG")
	public String getBelong() {
		return belong;
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	@Column(name = "CONTACT")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "CUSTOMER_POOL_NAME")
	public String getCustomerPoolName() {
		return customerPoolName;
	}

	public void setCustomerPoolName(String customerPoolName) {
		this.customerPoolName = customerPoolName;
	}

	@Column(name = "IS_PUBLIC_POOL")
	public String getIsPublicPool() {
		return isPublicPool;
	}

	public void setIsPublicPool(String isPublicPool) {
		this.isPublicPool = isPublicPool;
	}

	@Column(name = "ACCESS_ROLES")
	public String getAccessRoles() {
		return accessRoles;
	}

	public void setAccessRoles(String accessRoles) {
		this.accessRoles = accessRoles;
	}
	
	@Column(name = "RESOURCE_POOL_NAME")
	public String getResourcePoolName() {
		return resourcePoolName;
	}

	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="RESOURCE_POOL_STATUS")
	public ValidStatus getResourcePoolstatus() {
		return resourcePoolstatus;
	}
	
	public void setResourcePoolstatus(ValidStatus resourcePoolstatus) {
		this.resourcePoolstatus = resourcePoolstatus;
	}

	@Column(name = "ORG_SIGN")
	public String getOrgSign() {
		return orgSign;
	}

	public void setOrgSign(String orgSign) {
		this.orgSign = orgSign;
	}

	//@ManyToMany(mappedBy = "organization",cascade = { CascadeType.MERGE },fetch = FetchType.EAGER)
	@Transient
	public List<SystemNotice> getSystemNotices() {
		return systemNotices;
	}

	public void setSystemNotices(List<SystemNotice> systemNotices) {
		this.systemNotices = systemNotices;
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

	@Column(name = "SYB_APPID")
	public String getSybAppId() {
		return sybAppId;
	}

	public void setSybAppId(String sybAppId) {
		this.sybAppId = sybAppId;
	}

	@Column(name = "SYB_CUSID")
	public String getSybCusId() {
		return sybCusId;
	}

	public void setSybCusId(String sybCusId) {
		this.sybCusId = sybCusId;
	}

	@Column(name = "SYB_APPKEY")
	public String getSybAppKey() {
		return sybAppKey;
	}

	public void setSybAppKey(String sybAppKey) {
		this.sybAppKey = sybAppKey;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "province_id")
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "city_id")
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

//	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "area_id")
	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	@Transient
	public Region getProvince() {
		return province;
	}

	public void setProvince(Region province) {
		this.province = province;
	}
	@Transient
	public Region getCity() {
		return city;
	}

	public void setCity(Region city) {
		this.city = city;
	}
	@Transient
	public Region getArea() {
		return area;
	}

	public void setArea(Region area) {
		this.area = area;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="state_of_emergency")
	public StateOfEmergency getStateOfEmergency() {
		return stateOfEmergency;
	}

	public void setStateOfEmergency(StateOfEmergency stateOfEmergency) {
		this.stateOfEmergency = stateOfEmergency;
	}

	@Column(name="status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name="hrms_id")
	public String getHrmsId() {
		return hrmsId;
	}

	public void setHrmsId(String hrmsId) {
		this.hrmsId = hrmsId;
	}

	@Column(name="boss_use")
	public Integer getBossUse() {
		return bossUse;
	}

	public void setBossUse(Integer bossUse) {
		this.bossUse = bossUse;
	}


	@Transient
	public void setHrmsOrganizationInfo(OrganizationHrms hrms){
		this.status=hrms.getStatus();
		this.setOrgSign(hrms.getOrgSign());
		this.setOrgOrder(hrms.getOrgOrder());
	}


	@Column(name = "traffic_info")
	public String getTrafficInfo() {
		return trafficInfo;
	}

	public void setTrafficInfo(String trafficInfo) {
		this.trafficInfo = trafficInfo;
	}

	@Column(name = "has_append")
	public Integer getHasAppend() {
		return hasAppend;
	}

	public void setHasAppend(Integer hasAppend) {
		this.hasAppend = hasAppend;
	}

	@Column(name = "contact_user")
	public String getContactUser() {
		return contactUser;
	}

	public void setContactUser(String contactUser) {
		this.contactUser = contactUser;
	}
}