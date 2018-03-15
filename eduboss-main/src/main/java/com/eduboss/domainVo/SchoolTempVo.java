package com.eduboss.domainVo;

import com.eduboss.domain.Region;

public class SchoolTempVo {

	private String id;
	private String name;
	private String value;
	private Integer orderVal;
	private String regionId;
	private String regionName;
	private String proviceName;
	private String schoolTypeId;
	private String schoolTypeName;
	private String schoolTempAuditStatusName;
	private String schoolStatus;
	private String createUserId;
	private String createTime;
	private String modifyUserId;
	private String modifyTime;
	private String provinceId;
	private String address; // 学校地址
	private String contact; // 联系方式
	private String remark;
	
	private String schoolLeader;//学校负责人
	private String schoolLeaderContact;//学校负责人联系方式


	private String signUserName; //签约人

	private String signUserContact; //签约人电话

	private String studentName;  //学生姓名

	private String contractId; //合同id

	private String studentId;//学生id

	private String studentGrade;

	private String auditUserName;

	private String schoolId;

	private String schoolName;
	private String matchingProvinceId;
	private String matchingRegionId;

	private Region province;	//省份
	private Region city;	//城市
	private Region area;	//地区

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getOrderVal() {
		return orderVal;
	}

	public void setOrderVal(Integer orderVal) {
		this.orderVal = orderVal;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getSchoolTypeId() {
		return schoolTypeId;
	}

	public void setSchoolTypeId(String schoolTypeId) {
		this.schoolTypeId = schoolTypeId;
	}

	public String getSchoolTypeName() {
		return schoolTypeName;
	}

	public void setSchoolTypeName(String schoolTypeName) {
		this.schoolTypeName = schoolTypeName;
	}

	public String getSchoolStatus() {
		return schoolStatus;
	}

	public void setSchoolStatus(String schoolStatus) {
		this.schoolStatus = schoolStatus;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSchoolLeader() {
		return schoolLeader;
	}

	public void setSchoolLeader(String schoolLeader) {
		this.schoolLeader = schoolLeader;
	}

	public String getSchoolLeaderContact() {
		return schoolLeaderContact;
	}

	public void setSchoolLeaderContact(String schoolLeaderContact) {
		this.schoolLeaderContact = schoolLeaderContact;
	}

	/**
	 * 学校名称在一个城市里面的一种学校级别是唯一的
	 * @param o
	 * @return
     */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SchoolTempVo that = (SchoolTempVo) o;

		if (!name.equals(that.name)) return false;
		if (!regionId.equals(that.regionId)) return false;
		return schoolTypeId.equals(that.schoolTypeId);

	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + regionId.hashCode();
		result = 31 * result + schoolTypeId.hashCode();
		return result;
	}

	public String getProviceName() {
		return proviceName;
	}

	public void setProviceName(String proviceName) {
		this.proviceName = proviceName;
	}

	public String getSignUserName() {
		return signUserName;
	}

	public void setSignUserName(String signUserName) {
		this.signUserName = signUserName;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getSchoolTempAuditStatusName() {
		return schoolTempAuditStatusName;
	}

	public void setSchoolTempAuditStatusName(String schoolTempAuditStatusName) {
		this.schoolTempAuditStatusName = schoolTempAuditStatusName;
	}

	public String getSignUserContact() {
		return signUserContact;
	}

	public void setSignUserContact(String signUserContact) {
		this.signUserContact = signUserContact;
	}

	public void setStudentGrade(String studentGrade) {
		this.studentGrade = studentGrade;
	}

	public String getStudentGrade() {
		return studentGrade;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public void setMatchingRegionId(String matchingRegionId) {
		this.matchingRegionId = matchingRegionId;
	}

	public String getMatchingRegionId() {
		return matchingRegionId;
	}

	public void setMatchingProvinceId(String matchingProvinceId) {
		this.matchingProvinceId = matchingProvinceId;
	}

	public String getMatchingProvinceId() {
		return matchingProvinceId;
	}


	public Region getProvince() {
		return province;
	}

	public void setProvince(Region province) {
		this.province = province;
	}

	public Region getCity() {
		return city;
	}

	public void setCity(Region city) {
		this.city = city;
	}

	public Region getArea() {
		return area;
	}

	public void setArea(Region area) {
		this.area = area;
	}
}
