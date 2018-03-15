package com.eduboss.domainVo;

import java.io.Serializable;

/**
 * 教材的信息
 * @author Administrator
 *
 */
public class TextBookVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//教材id
	private String id;
	//教材名称
	private String name;
	//状态 1已发布 0未发布
	private Integer state;
	//所属集团 1 boss 0 培优
	private Integer ownedGroup;
	//所属集团
	private String ownedGroupName;
	//产品类型
	private String category;
	//产品类型名称
	private String categoryName;
	//使用范围(所属分公司，单选，有一个"全部"的选项organizationId为"-1")
	private String organizationId;
	//使用范围 多个所属分公司
	private String organizationIds;
	//使用范围名字
	private String organizationNames;
	//年份编号                  
	private String productVersionId;
	//年份
	private String productVersionName;
	//季度编号
	private String productQuarterId;
	//季度
	private String productQuarterName;
	//产品系列编号
	private String courseSeriesId;
	//产品系列
	private String courseSeriesName;
	//年级编号
	private String gradeId;
	//年级 
	private String gradeName;
	//科目编号
	private String subjectId;
	//科目
	private String subjectName;
	//班级类型
	private String classTypeId;
	//班级类型
	private String classTypeName;
	//创建人id
	private String createBy;
	//创建人
	private String createByName;
	
	private String createDttm;
	
	private String updateDttm;
	
	//新增字段 教材类型
	//教材类型(传数字，1:A类教材 2:B类教材 3:C类教材 4:X类教材 5:Y类教材 6:Z类教材 7:S类教材 8:E类教材)
	private String wareType;
	//教材类型名字
	private String wareTypeName;
	

	public TextBookVo(){
		
	}

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

    

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getOwnedGroup() {
		return ownedGroup;
	}

	public void setOwnedGroup(Integer ownedGroup) {
		this.ownedGroup = ownedGroup;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(String organizationIds) {
		this.organizationIds = organizationIds;
	}


	public String getOrganizationNames() {
		return organizationNames;
	}

	public void setOrganizationNames(String organizationNames) {
		this.organizationNames = organizationNames;
	}

	public String getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(String productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getProductQuarterId() {
		return productQuarterId;
	}

	public void setProductQuarterId(String productQuarterId) {
		this.productQuarterId = productQuarterId;
	}

	public String getProductQuarterName() {
		return productQuarterName;
	}

	public void setProductQuarterName(String productQuarterName) {
		this.productQuarterName = productQuarterName;
	}

	public String getCourseSeriesId() {
		return courseSeriesId;
	}

	public void setCourseSeriesId(String courseSeriesId) {
		this.courseSeriesId = courseSeriesId;
	}

	public String getCourseSeriesName() {
		return courseSeriesName;
	}

	public void setCourseSeriesName(String courseSeriesName) {
		this.courseSeriesName = courseSeriesName;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getCreateDttm() {
		return createDttm;
	}

	public void setCreateDttm(String createDttm) {
		this.createDttm = createDttm;
	}

	public String getUpdateDttm() {
		return updateDttm;
	}

	public void setUpdateDttm(String updateDttm) {
		this.updateDttm = updateDttm;
	}

	public String getOwnedGroupName() {
		return ownedGroupName;
	}

	public void setOwnedGroupName(String ownedGroupName) {
		this.ownedGroupName = ownedGroupName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public String getWareType() {
		return wareType;
	}

	public void setWareType(String wareType) {
		this.wareType = wareType;
	}

	public String getWareTypeName() {
		return wareTypeName;
	}

	public void setWareTypeName(String wareTypeName) {
		this.wareTypeName = wareTypeName;
	}
	
	
	
	
}
