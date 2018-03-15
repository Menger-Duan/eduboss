package com.eduboss.domainVo;

import com.eduboss.common.ProductSingleSubject;
import com.eduboss.common.ProductType;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.User;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product")
public class ProductVo implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private ProductType category;
	private String categoryName;
	private ProductSingleSubject singleSubject;
	private String singleSubjectName;
	private Double price;
	private Double discount;
	private String shortname; 
	private String gradeId;
	private String gradeName;
	private String gradeSegmentId;
	private String gradeSegmentName;
	private BigDecimal miniClassTotalhours;
	private String organizationId;
	private String organizationName;
	private Integer classTimeLength;
	private String proStatus;
	private String subjectIds;//关联科目id
	private String  productCategoryId;//产品类别ID
	private String  productCategoryName;//产品类别名称
	private String  productCategoryFullName;//产品类别名称
	private String validStartDate;  // 有效开始时间
	private String validEndDate;  // 有效结束时间
	private String promotionId;//优惠方案
	private String promotionName;
	private String miniClassCourseType;
    private String productGroupId;
    private String productGroupName;
    private String courseSeriesId;
    private String productVersionId;
    private String productQuarterId;
    private String subjectId;
    private String classTypeId;
    private String courseSeriesName;
    private String productVersionName;
    private String productQuarterName;
    private String subjectName;
    private String classTypeName;
    
    private String createTime;
    private String createUserId;
	

    //一对多
    private Integer oneOnManyType;

	private String other_type_id; // 其他产品类型的细分

	private String other_of_type_id; //其他产品类型的所属产品
	/**
	 * 目标班保底资金比例
	 * */
	private Double promiseClassDiscount;
	
	private Double maxPromotionDiscount; //最大优惠比例

	private DataDict phase; //小班的期

	private String phaseId;
	private String phaseName;

	private String liveId;
	private BigDecimal totalAmount;
	private String startTime;
	private BigDecimal promotionAmount;
	private String teacherId;
	private String teacherName;

	//新增字段 教材id 教材名字   和产品一对一关系   add 2016-11-08
	private Integer textBookId;
	private String textBookName;
	
	private Set<String> branchId=new HashSet<>();

	private List<ProductVo> sonProduct = new ArrayList<>();

	private List<ProductVoCount> subProducts =  new ArrayList<>();
	
	// Constructors

	/** default constructor */
	public ProductVo() {
	}


	public ProductSingleSubject getSingleSubject() {
		return singleSubject;
	}

	public void setSingleSubject(ProductSingleSubject singleSubject) {
		this.singleSubject = singleSubject;
	}

	public String getSingleSubjectName() {
		return singleSubjectName;
	}

	public void setSingleSubjectName(String singleSubjectName) {
		this.singleSubjectName = singleSubjectName;
	}

	// Property accessors
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getDiscount() {
		return this.discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ProductType getCategory() {
		return category;
	}

	public void setCategory(ProductType category) {
		this.category = category;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getGradeSegmentId() {
		return gradeSegmentId;
	}

	public void setGradeSegmentId(String gradeSegmentId) {
		this.gradeSegmentId = gradeSegmentId;
	}

	public String getGradeSegmentName() {
		return gradeSegmentName;
	}

	public void setGradeSegmentName(String gradeSegmentName) {
		this.gradeSegmentName = gradeSegmentName;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public BigDecimal getMiniClassTotalhours() {
		return miniClassTotalhours;
	}

	public void setMiniClassTotalhours(BigDecimal miniClassTotalhours) {
		this.miniClassTotalhours = miniClassTotalhours;
	}

	public String getSmallClassAutoCompleteValue() {
		
		return this.id + "," + this.price + "," + this.discount + "," + (this.miniClassTotalhours==null ? 0 :this.miniClassTotalhours.intValue() );
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}

	public String getProStatus() {
		return proStatus;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	public Double getPromiseClassDiscount() {
		return promiseClassDiscount;
	}

	public void setPromiseClassDiscount(Double promiseClassDiscount) {
		this.promiseClassDiscount = promiseClassDiscount;
	}
	


	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(String validStartDate) {
		this.validStartDate = validStartDate;
	}

	public String getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getProductCategoryFullName() {
		return productCategoryFullName;
	}

	public void setProductCategoryFullName(String productCategoryFullName) {
		this.productCategoryFullName = productCategoryFullName;
	}

	public String getMiniClassCourseType() {
		return miniClassCourseType;
	}

	public void setMiniClassCourseType(String miniClassCourseType) {
		this.miniClassCourseType = miniClassCourseType;
	}

    public String getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }
    
	public String getCourseSeriesId() {
		return courseSeriesId;
	}

	public void setCourseSeriesId(String courseSeriesId) {
		this.courseSeriesId = courseSeriesId;
	}

	public String getProductVersionId() {
		return productVersionId;
	}

	public void setProductVersionId(String productVersionId) {
		this.productVersionId = productVersionId;
	}

	public String getProductQuarterId() {
		return productQuarterId;
	}

	public void setProductQuarterId(String productQuarterId) {
		this.productQuarterId = productQuarterId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}
	
	public String getCourseSeriesName() {
		return courseSeriesName;
	}

	public void setCourseSeriesName(String courseSeriesName) {
		this.courseSeriesName = courseSeriesName;
	}

	public String getProductVersionName() {
		return productVersionName;
	}

	public void setProductVersionName(String productVersionName) {
		this.productVersionName = productVersionName;
	}

	public String getProductQuarterName() {
		return productQuarterName;
	}

	public void setProductQuarterName(String productQuarterName) {
		this.productQuarterName = productQuarterName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	
	public Integer getOneOnManyType() {
		return oneOnManyType;
	}

	public void setOneOnManyType(Integer oneOnManyType) {
		this.oneOnManyType = oneOnManyType;
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getOther_type_id() {
		return other_type_id;
	}

	public void setOther_type_id(String other_type_id) {
		this.other_type_id = other_type_id;
	}

	public String getOther_of_type_id() {
		return other_of_type_id;
	}

	public void setOther_of_type_id(String other_of_type_id) {
		this.other_of_type_id = other_of_type_id;
	}

	public Double getMaxPromotionDiscount() {
		return maxPromotionDiscount;
	}

	public void setMaxPromotionDiscount(Double maxPromotionDiscount) {
		this.maxPromotionDiscount = maxPromotionDiscount;
	}

	public DataDict getPhase() {
		return phase;
	}

	public void setPhase(DataDict phase) {
		this.phase = phase;
	}

	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}


	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Set<String> getBranchId() {
		return branchId;
	}

	public void setBranchId(Set<String> branchId) {
		this.branchId = branchId;
	}

	public List<ProductVo> getSonProduct() {
		return sonProduct;
	}

	public void setSonProduct(List<ProductVo> sonProduct) {
		this.sonProduct = sonProduct;
	}

	public List<ProductVoCount> getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(List<ProductVoCount> subProducts) {
		this.subProducts = subProducts;
	}
	
	

	public Integer getTextBookId() {
		return textBookId;
	}

	public void setTextBookId(Integer textBookId) {
		this.textBookId = textBookId;
	}

	public String getTextBookName() {
		return textBookName;
	}

	public void setTextBookName(String textBookName) {
		this.textBookName = textBookName;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductVo productVo = (ProductVo) o;

        if (category != productVo.category) return false;
        if (categoryName != null ? !categoryName.equals(productVo.categoryName) : productVo.categoryName != null)
            return false;
        if (classTimeLength != null ? !classTimeLength.equals(productVo.classTimeLength) : productVo.classTimeLength != null)
            return false;
        if (discount != null ? !discount.equals(productVo.discount) : productVo.discount != null) return false;
        if (gradeId != null ? !gradeId.equals(productVo.gradeId) : productVo.gradeId != null) return false;
        if (gradeName != null ? !gradeName.equals(productVo.gradeName) : productVo.gradeName != null) return false;
        if (id != null ? !id.equals(productVo.id) : productVo.id != null) return false;
        if (miniClassCourseType != null ? !miniClassCourseType.equals(productVo.miniClassCourseType) : productVo.miniClassCourseType != null)
            return false;
        if (miniClassTotalhours != null ? !miniClassTotalhours.equals(productVo.miniClassTotalhours) : productVo.miniClassTotalhours != null)
            return false;
        if (name != null ? !name.equals(productVo.name) : productVo.name != null) return false;
        if (organizationId != null ? !organizationId.equals(productVo.organizationId) : productVo.organizationId != null)
            return false;
        if (organizationName != null ? !organizationName.equals(productVo.organizationName) : productVo.organizationName != null)
            return false;
        if (price != null ? !price.equals(productVo.price) : productVo.price != null) return false;
        if (proStatus != null ? !proStatus.equals(productVo.proStatus) : productVo.proStatus != null) return false;
        if (productCategoryFullName != null ? !productCategoryFullName.equals(productVo.productCategoryFullName) : productVo.productCategoryFullName != null)
            return false;
        if (productCategoryId != null ? !productCategoryId.equals(productVo.productCategoryId) : productVo.productCategoryId != null)
            return false;
        if (productCategoryName != null ? !productCategoryName.equals(productVo.productCategoryName) : productVo.productCategoryName != null)
            return false;
        if (productGroupId != null ? !productGroupId.equals(productVo.productGroupId) : productVo.productGroupId != null)
            return false;
        if (productGroupName != null ? !productGroupName.equals(productVo.productGroupName) : productVo.productGroupName != null)
            return false;
        if (promiseClassDiscount != null ? !promiseClassDiscount.equals(productVo.promiseClassDiscount) : productVo.promiseClassDiscount != null)
            return false;
        if (promotionId != null ? !promotionId.equals(productVo.promotionId) : productVo.promotionId != null)
            return false;
        if (promotionName != null ? !promotionName.equals(productVo.promotionName) : productVo.promotionName != null)
            return false;
        if (shortname != null ? !shortname.equals(productVo.shortname) : productVo.shortname != null) return false;
        if (subjectIds != null ? !subjectIds.equals(productVo.subjectIds) : productVo.subjectIds != null) return false;
        if (validEndDate != null ? !validEndDate.equals(productVo.validEndDate) : productVo.validEndDate != null)
            return false;
        if (validStartDate != null ? !validStartDate.equals(productVo.validStartDate) : productVo.validStartDate != null)
            return false;
        if (courseSeriesId != null ? !courseSeriesId.equals(productVo.courseSeriesId) : productVo.courseSeriesId != null) return false;
        if (productVersionId != null ? !productVersionId.equals(productVo.productVersionId) : productVo.productVersionId != null) return false;
        if (productQuarterId != null ? !productQuarterId.equals(productVo.productQuarterId) : productVo.productQuarterId != null) return false;
        if (subjectId != null ? !subjectId.equals(productVo.subjectId) : productVo.subjectId != null) return false;
        if (classTypeId != null ? !classTypeId.equals(productVo.classTypeId) : productVo.classTypeId != null) return false;

		if (other_type_id != null ? !other_type_id.equals(productVo.other_type_id) : productVo.other_type_id != null) return false;

		if (other_of_type_id != null ? !other_of_type_id.equals(productVo.other_of_type_id) : productVo.other_of_type_id != null) return false;


        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (shortname != null ? shortname.hashCode() : 0);
        result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
        result = 31 * result + (gradeName != null ? gradeName.hashCode() : 0);
        result = 31 * result + (miniClassTotalhours != null ? miniClassTotalhours.hashCode() : 0);
        result = 31 * result + (organizationId != null ? organizationId.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (classTimeLength != null ? classTimeLength.hashCode() : 0);
        result = 31 * result + (proStatus != null ? proStatus.hashCode() : 0);
        result = 31 * result + (subjectIds != null ? subjectIds.hashCode() : 0);
        result = 31 * result + (productCategoryId != null ? productCategoryId.hashCode() : 0);
        result = 31 * result + (productCategoryName != null ? productCategoryName.hashCode() : 0);
        result = 31 * result + (productCategoryFullName != null ? productCategoryFullName.hashCode() : 0);
        result = 31 * result + (validStartDate != null ? validStartDate.hashCode() : 0);
        result = 31 * result + (validEndDate != null ? validEndDate.hashCode() : 0);
        result = 31 * result + (promotionId != null ? promotionId.hashCode() : 0);
        result = 31 * result + (promotionName != null ? promotionName.hashCode() : 0);
        result = 31 * result + (miniClassCourseType != null ? miniClassCourseType.hashCode() : 0);
        result = 31 * result + (productGroupId != null ? productGroupId.hashCode() : 0);
        result = 31 * result + (productGroupName != null ? productGroupName.hashCode() : 0);
        result = 31 * result + (promiseClassDiscount != null ? promiseClassDiscount.hashCode() : 0);
        result = 31 * result + (courseSeriesId != null ? courseSeriesId.hashCode() : 0);
        result = 31 * result + (productVersionId != null ? productVersionId.hashCode() : 0);
        result = 31 * result + (productQuarterId != null ? productQuarterId.hashCode() : 0);
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        result = 31 * result + (classTypeId != null ? classTypeId.hashCode() : 0);
        result = 31 * result + (other_type_id != null ? other_type_id.hashCode() : 0);
        result = 31 * result + (other_of_type_id != null ? other_of_type_id.hashCode() : 0);
        return result;
    }
}