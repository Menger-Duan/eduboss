package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * StudentSchool entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDENT_SCHOOL")
public class StudentSchool implements java.io.Serializable {

    private static final long serialVersionUID = 126807481464626495L;
    
	// Fields  studentSchool
    private String id;
	private String name;
	private String value;
	private Integer orderVal;
	private DataDict region;
	private DataDict schoolType;
	private String schoolStatus;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String address; // 学校地址
	private String log; //经度
	private String lat;//维度
	private String contact; // 联系方式
	private String remark;
	
	private String schoolLeader;//学校负责人
	private String schoolLeaderContact;//学校负责人联系方式

	private Region province;	//省份
	private Region city;	//城市
	private Region area;	//地区
	private String globalNumber; //全局编号

	// Constructors

	/** default constructor */
	public StudentSchool() {
	}

	public StudentSchool(String id) {
		super();
		this.id = id;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "VALUE", length = 128)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "ORDER_VAL")
	public Integer getOrderVal() {
		return this.orderVal;
	}

	public void setOrderVal(Integer orderVal) {
		this.orderVal = orderVal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	public DataDict getRegion() {
		return region;
	}

	public void setRegion(DataDict region) {
		this.region = region;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY")
	public DataDict getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(DataDict schoolType) {
		this.schoolType = schoolType;
	}

	@Column(name = "SCHOOL_STATUS", length = 32)
	public String getSchoolStatus() {
		return this.schoolStatus;
	}

	public void setSchoolStatus(String schoolStatus) {
		this.schoolStatus = schoolStatus;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "ADDRESS", length = 255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "CONTACT", length = 128)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "SCHOOL_LEADER", length = 20)
	public String getSchoolLeader() {
		return schoolLeader;
	}

	public void setSchoolLeader(String schoolLeader) {
		this.schoolLeader = schoolLeader;
	}

	@Column(name = "SCHOOL_LEADER_CONTACT", length = 32)
	public String getSchoolLeaderContact() {
		return schoolLeaderContact;
	}

	public void setSchoolLeaderContact(String schoolLeaderContact) {
		this.schoolLeaderContact = schoolLeaderContact;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "province_id")
	public Region getProvince() {
		return province;
	}

	public void setProvince(Region province) {
		this.province = province;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	public Region getCity() {
		return city;
	}

	public void setCity(Region city) {
		this.city = city;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id")
	public Region getArea() {
		return area;
	}

	public void setArea(Region area) {
		this.area = area;
	}

	@Column(name = "log", length = 255)
	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	@Column(name = "lat", length = 255)
	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	@Column(name = "global_number", length = 32)
    public String getGlobalNumber() {
        return globalNumber;
    }

    public void setGlobalNumber(String globalNumber) {
        this.globalNumber = globalNumber;
    }
	
}