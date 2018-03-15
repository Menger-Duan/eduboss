package com.eduboss.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eduboss.common.ProductSingleSubject;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.MiniClassCourseType;
import com.eduboss.common.ProductType;

/**
 * Product entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "product")
public class Product implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private ProductType category;
	private ProductSingleSubject singleSubject;
	private BigDecimal price;
	private BigDecimal discount;
	private String createTime;
	private String createUserId;
	private String modifyTime;
	private String modifyUserId;
	private String shortname; 
	private DataDict gradeDict;
	private DataDict gradeSegmentDict;
	private BigDecimal miniClassTotalhours;
	private  Organization organization; //ORGANIZATION_ID
	private Integer classTimeLength;//CLASS_TIME_LENGTH
	private String subjectNames;//产品名称（用，号隔开，用于批量添加产品）
	private String proStatus;
	
	/**
	 * 目标班保底资金比例
	 * */
	private Double promiseClassDiscount;
	private String subjectIds;//关联科目id
	private ProductCategory  productCategoryId;//产品类别 PRODUCT_CATEGORY_ID
	private String validStartDate;  //VALID_START_DATE 有效开始时间
	private String validEndDate;  //VALID_END_DATE 有效结束时间
	private String promotionId;//PROMOTION_ID 优惠方案
	private String promotionName;
	private MiniClassCourseType miniClassCourseType;
    private DataDict productGroup;
    
    private DataDict courseSeries;
    private DataDict productVersion;
    private DataDict productQuarter;
    private DataDict subject;
    private DataDict classType;
    
    //一对多
    private Integer oneOnManyType;

	private DataDict other_type;  // 其他产品类型的细分

	private DataDict other_of_type; //其他产品类型的所属产品

	private Double maxPromotionDiscount;//最大优惠比例


	private DataDict phase; //小班的期

	//小班绑定教材
	//新增字段 教材id 教材名字   和产品一对一关系   add 2016-11-08
	private Integer textBookId;
	private String textBookName;

	//直播
	private String liveId;
	private BigDecimal totalAmount;
	private String startTime;
	private BigDecimal promotionAmount;
	private User teacher;
	//直播新增字段

	private List<ProductPackage> productPackageList = new ArrayList<>();


	// private Set<ContractProduct> contractProducts = new
	// HashSet<ContractProduct>(0);

	// Constructors

	/** default constructor */
	public Product() {
	}

	public Product(String productId) {
		this.id = productId;
	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CATEGORY", length = 32)
	public ProductType getCategory() {
		return this.category;
	}

	public void setCategory(ProductType category) {
		this.category = category;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "single_subject", length = 32)
	public ProductSingleSubject getSingleSubject() {
		return singleSubject;
	}

	public void setSingleSubject(ProductSingleSubject singleSubject) {
		this.singleSubject = singleSubject;
	}

	@Column(name = "PRICE", precision = 10)
	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	@Column(name = "DISCOUNT", precision = 10)
	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "CREATE_USER_ID", length = 32)
	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_USER_ID", length = 32)
	public String getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(String modifyUserId) {
		this.modifyUserId = modifyUserId;
	}
	
	@Column(name = "SHORTNAME", length = 32)
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID")
	public DataDict getGradeDict() {
		return gradeDict;
	}

	public void setGradeDict(DataDict gradeDict) {
		this.gradeDict = gradeDict;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_SEGMENT_ID")
	public DataDict getGradeSegmentDict() {
		return gradeSegmentDict;
	}

	public void setGradeSegmentDict(DataDict gradeSegmentDict) {
		this.gradeSegmentDict = gradeSegmentDict;
	}

	@Column(name = "MINI_CLASS_TOTAL_HOURS", precision = 10)
	public BigDecimal getMiniClassTotalhours() {
		return miniClassTotalhours;
	}

	public void setMiniClassTotalhours(BigDecimal miniClassTotalhours) {
		this.miniClassTotalhours = miniClassTotalhours;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATION_ID")
	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	@Transient
	public String getSubjectNames() {
		return subjectNames;
	}

	public void setSubjectNames(String subjectNames) {
		this.subjectNames = subjectNames;
	}

	@Column(name="CLASS_TIME_LENGTH",precision = 10)
	public Integer getClassTimeLength() {
		return classTimeLength;
	}

	public void setClassTimeLength(Integer classTimeLength) {
		this.classTimeLength = classTimeLength;
	}

	@Column(name = "PRO_STATUS", length = 32)
	public String getProStatus() {
		return proStatus;
	}

	public void setProStatus(String proStatus) {
		this.proStatus = proStatus;
	}

	@Column(name = "PROMISE_CLASS_DISCOUNT")
	public Double getPromiseClassDiscount() {
		return promiseClassDiscount;
	}

	public void setPromiseClassDiscount(Double promiseClassDiscount) {
		this.promiseClassDiscount = promiseClassDiscount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATEGORY_ID")
	public ProductCategory getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(ProductCategory productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	@Column(name="VALID_START_DATE",precision = 20)
	public String getValidStartDate() {
		return validStartDate;
	}

	public void setValidStartDate(String validStartDate) {
		this.validStartDate = validStartDate;
	}

	@Column(name="VALID_END_DATE",precision = 20)
	public String getValidEndDate() {
		return validEndDate;
	}

	public void setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
	}

	@Column(name="PROMOTION_ID",precision = 300)
	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	@Column(name="SUBJECT_IDS",precision = 600)
	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	@Column(name="PROMOTION_NAME",precision = 300)
	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "MINI_CLASS_COURSE_TYPE", length = 20)
	public MiniClassCourseType getMiniClassCourseType() {
		return miniClassCourseType;
	}

	public void setMiniClassCourseType(MiniClassCourseType miniClassCourseType) {
		this.miniClassCourseType = miniClassCourseType;
	}

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_GROUP_ID")
    public DataDict getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(DataDict productGroup) {
        this.productGroup = productGroup;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COURSE_SERIES_ID")
    public DataDict getCourseSeries() {
    	return courseSeries;
    }
    
    public void setCourseSeries(DataDict courseSeries) {
    	this.courseSeries = courseSeries;
    }
    
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_VERSION_ID")
	public DataDict getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(DataDict productVersion) {
		this.productVersion = productVersion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_QUARTER_ID")
	public DataDict getProductQuarter() {
		return productQuarter;
	}

	public void setProductQuarter(DataDict productQuarter) {
		this.productQuarter = productQuarter;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SMALL_CLASS_PHASE_ID")
	public DataDict getPhase() {
		return phase;
	}

	public void setPhase(DataDict phase) {
		this.phase = phase;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID")
	public DataDict getSubject() {
		return subject;
	}

	public void setSubject(DataDict subject) {
		this.subject = subject;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_TYPE_ID")
	public DataDict getClassType() {
		return classType;
	}

	public void setClassType(DataDict classType) {
		this.classType = classType;
	}

	
	@Column(name = "ONE_ON_MANY_TYPE",precision = 6)
	public Integer getOneOnManyType() {
		return oneOnManyType;
	}

	public void setOneOnManyType(Integer oneOnManyType) {
		this.oneOnManyType = oneOnManyType;
	}

	// @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY,
	// mappedBy = "product")
	// public Set<ContractProduct> getContractProducts() {
	// return this.contractProducts;
	// }
	//
	// public void setContractProducts(Set<ContractProduct> contractProducts) {
	// this.contractProducts = contractProducts;
	// }

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Other_Category")
	public DataDict getOther_type() {
		return other_type;
	}

	public void setOther_type(DataDict other_type) {
		this.other_type = other_type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Other_Of_Category")
	public DataDict getOther_of_type() {
		return other_of_type;
	}

	public void setOther_of_type(DataDict other_of_type) {
		this.other_of_type = other_of_type;
	}

	@Column(name = "MAX_PROMOTION_DISCOUNT")
	public Double getMaxPromotionDiscount() {
		return maxPromotionDiscount;
	}

	public void setMaxPromotionDiscount(Double maxPromotionDiscount) {
		this.maxPromotionDiscount = maxPromotionDiscount;
	}

	@Column(name = "live_id")
	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	@Column(name = "total_amount")
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "start_time")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Column(name = "promotion_amount")
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id")
	public User getTeacher() {
		return teacher;
	}

	public void setTeacher(User teacher) {
		this.teacher = teacher;
	}

    @Column(name = "TEXTBOOK_ID")
    public Integer getTextBookId() {
        return textBookId;
    }

    public void setTextBookId(Integer textBookId) {
        this.textBookId = textBookId;
    }

    @Column(name = "TEXTBOOK_NAME", length = 32)
    public String getTextBookName() {
        return textBookName;
    }

    public void setTextBookName(String textBookName) {
        this.textBookName = textBookName;
    }

	@Transient
	public List<ProductPackage> getProductPackageList() {
		return productPackageList;
	}

	public void setProductPackageList(List<ProductPackage> productPackageList) {
		this.productPackageList = productPackageList;
	}
}