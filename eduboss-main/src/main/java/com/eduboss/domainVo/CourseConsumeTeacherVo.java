package com.eduboss.domainVo;

import com.eduboss.common.OrganizationType;


public class CourseConsumeTeacherVo {

	private String startDate;
	private String endDate;
	private String blCampusId;
	private String blCampusName;
	private String branchId;
	private String branchName;
	private OrganizationType organizationType;

	private String miniClassTypeId; //班级类型

	private String productTypeIds;  //产品类型

	private String anshazhesuan;        //按课时或者按小时折算 用来算报表的课消
	
	private String subject;
	
	private String type;  //1 ：按课消  2：按小时
	
	private String productQuarterSearch;//产品季度

	/**
	 * 产品季度名称
	 */
	private String productQuarterSearchName;
	

    private String staticCampusId;
    
    private String teacherType;

	/**
	 * 教师类型名称
	 */
	private String teacherTypeName;

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public OrganizationType getOrganizationType() {
		return organizationType;
	}
	public void setOrganizationType(OrganizationType organizationType) {
		this.organizationType = organizationType;
	}

	public String getMiniClassTypeId() {
		return miniClassTypeId;
	}

	public void setMiniClassTypeId(String miniClassTypeId) {
		this.miniClassTypeId = miniClassTypeId;
	}



	public String getProductTypeIds() {
		return productTypeIds;
	}

	public void setProductTypeIds(String productTypeIds) {
		this.productTypeIds = productTypeIds;
	}


	public String getAnshazhesuan() {
		return anshazhesuan;
	}

	public void setAnshazhesuan(String anshazhesuan) {
		this.anshazhesuan = anshazhesuan;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProductQuarterSearch() {
		return productQuarterSearch;
	}
	public void setProductQuarterSearch(String productQuarterSearch) {
		this.productQuarterSearch = productQuarterSearch;
	}
	public String getStaticCampusId() {
		return staticCampusId;
	}
	public void setStaticCampusId(String staticCampusId) {
		this.staticCampusId = staticCampusId;
	}
	public String getTeacherType() {
		return teacherType;
	}
	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
	}

	public String getProductQuarterSearchName() {
		return productQuarterSearchName;
	}

	public void setProductQuarterSearchName(String productQuarterSearchName) {
		this.productQuarterSearchName = productQuarterSearchName;
	}

	public String getTeacherTypeName() {
		return teacherTypeName;
	}

	public void setTeacherTypeName(String teacherTypeName) {
		this.teacherTypeName = teacherTypeName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}
}
