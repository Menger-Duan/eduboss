package com.eduboss.domainVo;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ProductType;


/**
 * ContractProduct entity. 
 */
public class ContractProductVo implements java.io.Serializable {

	// Fields

	private String id;
	private String contractId;
	
	private String productId;
	private Double price;
	private String productionName;
	private ProductType productType;
	private String productTypeName;
	private ProductVo productVo; 
	
	private Double quantity;
	private Double quantityInProduct;
	private Double dealDiscount;
	private Double payment;
	private Double paidAmount;
	private Double consumeAmount;
	
	private BigDecimal realConsumeAmount;
	private BigDecimal promotionConsumeAmount;
	
	private Double consumeQuanity;
	private Double maxKeTiquMoney; //最大可提取金额  （是已分配业绩的最大值）
	private BigDecimal productTypeFenpeiMoney; // 每种产品分配的金额
	private ContractProductStatus status;
	private ContractProductPaidStatus paidStatus;
	private String statusName;
	private String paidStatusName;
	
	private String promotionIds ;
	private String promotionJson ;
	private BigDecimal promotionAmount;
	
	private Double changedAmount; // 课程转课实收金额
	
	private BigDecimal realAmount;
	private BigDecimal totalAmount;

    private String miniClassId;
    
    private String miniClassName;
    
    private String lectureId;
    
    private String lectureName;

	private int twoClassId;
	private String twoClassName;
    
    private String continueMiniClassId;
    
    private String extendMiniClassId;
    
    private String firstSchoolTime;
	// Constructors
    
    private BigDecimal remainingAmount;
	private BigDecimal remainingAmountOfBasicAmount;
	private BigDecimal remainingAmountOfPromotionAmount;
    
    
    private Set<ContractProductSubjectVo> contractProductSubVos =  new HashSet<ContractProductSubjectVo>();
    
    
    private String studentId;
	private String studentName;
	private String escClassId;
	private String escClassName;
	private String teacherName;
	
    private BigDecimal remainFinance;// 剩余资金
    private BigDecimal remainCourseHour;// 剩余课时

    private String subjectIds;
    private String isFrozen;
	private String isWashed; // TRUE：发生过冲销，FALSE：没发生过
	private String isUnvalid; // TRUE : 执行取消操作，FALSE：不需要执行取消
	private String classTime;
	private String classTeacherName;
	private BigDecimal realBuyHours;//小班报名课时数
	
	// 一对一课时管理增加
	private String gradeName; // 年级
	private BigDecimal totalHours; // 总课时
	private BigDecimal availableHours; // 可分配课时
	private String oooSubjectDistributedHoursDes; // 已分配课时描述
	private BigDecimal distributableHours; // 未分配课时
	private String signByWhoName; // 签单人
	private String blCampusName; // 所属校区
	private String signTime; // 签约日期
	private BigDecimal oooSubjectDistributedHours; // 一对一合同产品已分配到科目的课时
	private BigDecimal unDistributedMoney; // 一对一合同未分配金额

	private String liveId; //直播课程id
	private String hashFlag;//用于处理hash比较。
	
	public String getFirstSchoolTime() {
		return firstSchoolTime;
	}

	public void setFirstSchoolTime(String firstSchoolTime) {
		this.firstSchoolTime = firstSchoolTime;
	}

	/** default constructor */
	public ContractProductVo() {
		
	}

	public String getHashFlag() {
		return hashFlag;
	}

	public void setHashFlag(String hashFlag) {
		this.hashFlag = hashFlag;
	}

	public String getMiniClassId() {
        return miniClassId;
    }

    public void setMiniClassId(String miniClassId) {
        this.miniClassId = miniClassId;
    }

    public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getDealDiscount() {
		return this.dealDiscount;
	}

	public void setDealDiscount(Double dealDiscount) {
		this.dealDiscount = dealDiscount;
	}

	public Double getPayment() {
		return this.payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getProductionName() {
		return productionName;
	}

	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}

	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	public String getProductTypeName() {
		return productTypeName;
	}

	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(Double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public Double getConsumeQuanity() {
		return consumeQuanity;
	}

	public void setConsumeQuanity(Double consumeQuanity) {
		this.consumeQuanity = consumeQuanity;
	}

	public ContractProductStatus getStatus() {
		return status;
	}

	public void setStatus(ContractProductStatus status) {
		this.status = status;
	}

	public ContractProductPaidStatus getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(ContractProductPaidStatus paidStatus) {
		this.paidStatus = paidStatus;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getPaidStatusName() {
		return paidStatusName;
	}

	public void setPaidStatusName(String paidStatusName) {
		this.paidStatusName = paidStatusName;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public Set<ContractProductSubjectVo> getContractProductSubVos() {
		return contractProductSubVos;
	}

	public void setContractProductSubVos(
			Set<ContractProductSubjectVo> contractProductSubVos) {
		this.contractProductSubVos = contractProductSubVos;
	}

	

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public BigDecimal getRemainFinance() {
		return remainFinance;
	}

	public void setRemainFinance(BigDecimal remainFinance) {
		this.remainFinance = remainFinance;
	}

	public BigDecimal getRemainCourseHour() {
		return remainCourseHour;
	}

	public void setRemainCourseHour(BigDecimal remainCourseHour) {
		this.remainCourseHour = remainCourseHour;
	}

	public String getPromotionIds() {
		return promotionIds;
	}

	public void setPromotionIds(String promotionIds) {
		this.promotionIds = promotionIds;
	}

	public String getPromotionJson() {
		return promotionJson;
	}

	public void setPromotionJson(String promotionJson) {
		this.promotionJson = promotionJson;
	}

	

	public BigDecimal getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

    public Double getChangedAmount() {
        return changedAmount;
    }

    public void setChangedAmount(Double changedAmount) {
        this.changedAmount = changedAmount;
    }

    public String getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(String subjectIds) {
        this.subjectIds = subjectIds;
    }

	public ProductVo getProductVo() {
		return productVo;
	}

	public void setProductVo(ProductVo productVo) {
		this.productVo = productVo;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public BigDecimal getRemainingAmountOfBasicAmount() {
		return remainingAmountOfBasicAmount;
	}

	public void setRemainingAmountOfBasicAmount(
			BigDecimal remainingAmountOfBasicAmount) {
		this.remainingAmountOfBasicAmount = remainingAmountOfBasicAmount;
	}

	public BigDecimal getRemainingAmountOfPromotionAmount() {
		return remainingAmountOfPromotionAmount;
	}

	public void setRemainingAmountOfPromotionAmount(
			BigDecimal remainingAmountOfPromotionAmount) {
		this.remainingAmountOfPromotionAmount = remainingAmountOfPromotionAmount;
	}

	public Double getQuantityInProduct() {
		return quantityInProduct;
	}

	public void setQuantityInProduct(Double quantityInProduct) {
		this.quantityInProduct = quantityInProduct;
	}

	public String getMiniClassName() {
		return miniClassName;
	}

	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}

	public String getContinueMiniClassId() {
		return continueMiniClassId;
	}

	public void setContinueMiniClassId(String continueMiniClassId) {
		this.continueMiniClassId = continueMiniClassId;
	}

	public String getExtendMiniClassId() {
		return extendMiniClassId;
	}

	public void setExtendMiniClassId(String extendMiniClassId) {
		this.extendMiniClassId = extendMiniClassId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getEscClassId() {
		return escClassId;
	}

	public void setEscClassId(String escClassId) {
		this.escClassId = escClassId;
	}

	public String getEscClassName() {
		return escClassName;
	}

	public void setEscClassName(String escClassName) {
		this.escClassName = escClassName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Double getMaxKeTiquMoney() {
		return maxKeTiquMoney;
	}

	public void setMaxKeTiquMoney(Double maxKeTiquMoney) {
		this.maxKeTiquMoney = maxKeTiquMoney;
	}

	public BigDecimal getProductTypeFenpeiMoney() {
		return productTypeFenpeiMoney;
	}

	public void setProductTypeFenpeiMoney(BigDecimal productTypeFenpeiMoney) {
		this.productTypeFenpeiMoney = productTypeFenpeiMoney;
	}

	public String getLectureId() {
		return lectureId;
	}

	public void setLectureId(String lectureId) {
		this.lectureId = lectureId;
	}

	public String getLectureName() {
		return lectureName;
	}

	public void setLectureName(String lectureName) {
		this.lectureName = lectureName;
	}

	public String getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(String isFrozen) {
		this.isFrozen = isFrozen;
	}

	public String getClassTime() {
		return classTime;
	}

	public void setClassTime(String classTime) {
		this.classTime = classTime;
	}

	public String getClassTeacherName() {
		return classTeacherName;
	}

	public void setClassTeacherName(String classTeacherName) {
		this.classTeacherName = classTeacherName;
	}

	public BigDecimal getRealBuyHours() {
		return realBuyHours;
	}

	public void setRealBuyHours(BigDecimal realBuyHours) {
		this.realBuyHours = realBuyHours;
	}

	public String getIsWashed() {
		return isWashed;
	}

	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}

	public String getIsUnvalid() {
		return isUnvalid;
	}

	public void setIsUnvalid(String isUnvalid) {
		this.isUnvalid = isUnvalid;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public BigDecimal getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(BigDecimal totalHours) {
		this.totalHours = totalHours;
	}

	public BigDecimal getAvailableHours() {
		return availableHours;
	}

	public void setAvailableHours(BigDecimal availableHours) {
		this.availableHours = availableHours;
	}

	public String getOooSubjectDistributedHoursDes() {
		return oooSubjectDistributedHoursDes;
	}

	public void setOooSubjectDistributedHoursDes(String distributedHours) {
		this.oooSubjectDistributedHoursDes = distributedHours;
	}

	public BigDecimal getDistributableHours() {
		return distributableHours;
	}

	public void setDistributableHours(BigDecimal distributableHours) {
		this.distributableHours = distributableHours;
	}

	public String getSignByWhoName() {
		return signByWhoName;
	}

	public void setSignByWhoName(String signByWhoName) {
		this.signByWhoName = signByWhoName;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public String getSignTime() {
		return signTime;
	}

	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public BigDecimal getOooSubjectDistributedHours() {
		return oooSubjectDistributedHours;
	}

	public void setOooSubjectDistributedHours(BigDecimal oooSubjectDistributedHours) {
		this.oooSubjectDistributedHours = oooSubjectDistributedHours;
	}

	public BigDecimal getUnDistributedMoney() {
		return unDistributedMoney;
	}

	public void setUnDistributedMoney(BigDecimal unDistributedMoney) {
		this.unDistributedMoney = unDistributedMoney;
	}

	public BigDecimal getRealConsumeAmount() {
		return realConsumeAmount;
	}

	public void setRealConsumeAmount(BigDecimal realConsumeAmount) {
		this.realConsumeAmount = realConsumeAmount;
	}

	public BigDecimal getPromotionConsumeAmount() {
		return promotionConsumeAmount;
	}

	public void setPromotionConsumeAmount(BigDecimal promotionConsumeAmount) {
		this.promotionConsumeAmount = promotionConsumeAmount;
	}

	public int getTwoClassId() {
		return twoClassId;
	}

	public void setTwoClassId(int twoClassId) {
		this.twoClassId = twoClassId;
	}

	public String getTwoClassName() {
		return twoClassName;
	}

	public void setTwoClassName(String twoClassName) {
		this.twoClassName = twoClassName;
	}

	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((availableHours == null) ? 0 : availableHours.hashCode());
        result = prime * result
                + ((blCampusName == null) ? 0 : blCampusName.hashCode());
        result = prime * result
                + ((changedAmount == null) ? 0 : changedAmount.hashCode());
        result = prime
                * result
                + ((classTeacherName == null) ? 0 : classTeacherName.hashCode());
        result = prime * result
                + ((classTime == null) ? 0 : classTime.hashCode());
        result = prime * result
                + ((consumeAmount == null) ? 0 : consumeAmount.hashCode());
        result = prime * result
                + ((consumeQuanity == null) ? 0 : consumeQuanity.hashCode());
        result = prime
                * result
                + ((continueMiniClassId == null) ? 0 : continueMiniClassId
                        .hashCode());
        result = prime * result
                + ((contractId == null) ? 0 : contractId.hashCode());
        result = prime
                * result
                + ((contractProductSubVos == null) ? 0 : contractProductSubVos
                        .hashCode());
        result = prime * result
                + ((dealDiscount == null) ? 0 : dealDiscount.hashCode());
        result = prime
                * result
                + ((distributableHours == null) ? 0 : distributableHours
                        .hashCode());
        result = prime * result
                + ((escClassId == null) ? 0 : escClassId.hashCode());
        result = prime * result
                + ((escClassName == null) ? 0 : escClassName.hashCode());
        result = prime
                * result
                + ((extendMiniClassId == null) ? 0 : extendMiniClassId
                        .hashCode());
        result = prime * result
                + ((firstSchoolTime == null) ? 0 : firstSchoolTime.hashCode());
        result = prime * result
                + ((gradeName == null) ? 0 : gradeName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((isFrozen == null) ? 0 : isFrozen.hashCode());
        result = prime * result
                + ((isUnvalid == null) ? 0 : isUnvalid.hashCode());
        result = prime * result
                + ((isWashed == null) ? 0 : isWashed.hashCode());
        result = prime * result
                + ((lectureId == null) ? 0 : lectureId.hashCode());
        result = prime * result
                + ((lectureName == null) ? 0 : lectureName.hashCode());
        result = prime * result + ((liveId == null) ? 0 : liveId.hashCode());
        result = prime * result
                + ((maxKeTiquMoney == null) ? 0 : maxKeTiquMoney.hashCode());
        result = prime * result
                + ((miniClassId == null) ? 0 : miniClassId.hashCode());
        result = prime * result
                + ((miniClassName == null) ? 0 : miniClassName.hashCode());
        result = prime
                * result
                + ((oooSubjectDistributedHours == null) ? 0
                        : oooSubjectDistributedHours.hashCode());
        result = prime
                * result
                + ((oooSubjectDistributedHoursDes == null) ? 0
                        : oooSubjectDistributedHoursDes.hashCode());
        result = prime * result
                + ((paidAmount == null) ? 0 : paidAmount.hashCode());
        result = prime * result
                + ((paidStatus == null) ? 0 : paidStatus.hashCode());
        result = prime * result
                + ((paidStatusName == null) ? 0 : paidStatusName.hashCode());
        result = prime * result + ((payment == null) ? 0 : payment.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result
                + ((productId == null) ? 0 : productId.hashCode());
        result = prime * result
                + ((productType == null) ? 0 : productType.hashCode());
        result = prime
                * result
                + ((productTypeFenpeiMoney == null) ? 0
                        : productTypeFenpeiMoney.hashCode());
        result = prime * result
                + ((productTypeName == null) ? 0 : productTypeName.hashCode());
        result = prime * result
                + ((productVo == null) ? 0 : productVo.hashCode());
        result = prime * result
                + ((productionName == null) ? 0 : productionName.hashCode());
        result = prime * result
                + ((promotionAmount == null) ? 0 : promotionAmount.hashCode());
        result = prime
                * result
                + ((promotionConsumeAmount == null) ? 0
                        : promotionConsumeAmount.hashCode());
        result = prime * result
                + ((promotionIds == null) ? 0 : promotionIds.hashCode());
        result = prime * result
                + ((promotionJson == null) ? 0 : promotionJson.hashCode());
        result = prime * result
                + ((quantity == null) ? 0 : quantity.hashCode());
        result = prime
                * result
                + ((quantityInProduct == null) ? 0 : quantityInProduct
                        .hashCode());
        result = prime * result
                + ((realAmount == null) ? 0 : realAmount.hashCode());
        result = prime * result
                + ((realBuyHours == null) ? 0 : realBuyHours.hashCode());
        result = prime
                * result
                + ((realConsumeAmount == null) ? 0 : realConsumeAmount
                        .hashCode());
        result = prime
                * result
                + ((remainCourseHour == null) ? 0 : remainCourseHour.hashCode());
        result = prime * result
                + ((remainFinance == null) ? 0 : remainFinance.hashCode());
        result = prime * result
                + ((remainingAmount == null) ? 0 : remainingAmount.hashCode());
        result = prime
                * result
                + ((remainingAmountOfBasicAmount == null) ? 0
                        : remainingAmountOfBasicAmount.hashCode());
        result = prime
                * result
                + ((remainingAmountOfPromotionAmount == null) ? 0
                        : remainingAmountOfPromotionAmount.hashCode());
        result = prime * result
                + ((signByWhoName == null) ? 0 : signByWhoName.hashCode());
        result = prime * result
                + ((signTime == null) ? 0 : signTime.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result
                + ((statusName == null) ? 0 : statusName.hashCode());
        result = prime * result
                + ((studentId == null) ? 0 : studentId.hashCode());
        result = prime * result
                + ((studentName == null) ? 0 : studentName.hashCode());
        result = prime * result
                + ((subjectIds == null) ? 0 : subjectIds.hashCode());
        result = prime * result
                + ((teacherName == null) ? 0 : teacherName.hashCode());
        result = prime * result
                + ((totalAmount == null) ? 0 : totalAmount.hashCode());
        result = prime * result
                + ((totalHours == null) ? 0 : totalHours.hashCode());
        result = prime * result + twoClassId;
        result = prime * result
                + ((twoClassName == null) ? 0 : twoClassName.hashCode());
        result = prime
                * result
                + ((unDistributedMoney == null) ? 0 : unDistributedMoney
                        .hashCode());
		result = prime
				* result
				+ ((hashFlag == null) ? 0 : hashFlag
				.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContractProductVo other = (ContractProductVo) obj;
        if (availableHours == null) {
            if (other.availableHours != null)
                return false;
        } else if (!availableHours.equals(other.availableHours))
            return false;
        if (blCampusName == null) {
            if (other.blCampusName != null)
                return false;
        } else if (!blCampusName.equals(other.blCampusName))
            return false;
        if (changedAmount == null) {
            if (other.changedAmount != null)
                return false;
        } else if (!changedAmount.equals(other.changedAmount))
            return false;
        if (classTeacherName == null) {
            if (other.classTeacherName != null)
                return false;
        } else if (!classTeacherName.equals(other.classTeacherName))
            return false;
        if (classTime == null) {
            if (other.classTime != null)
                return false;
        } else if (!classTime.equals(other.classTime))
            return false;
        if (consumeAmount == null) {
            if (other.consumeAmount != null)
                return false;
        } else if (!consumeAmount.equals(other.consumeAmount))
            return false;
        if (consumeQuanity == null) {
            if (other.consumeQuanity != null)
                return false;
        } else if (!consumeQuanity.equals(other.consumeQuanity))
            return false;
        if (continueMiniClassId == null) {
            if (other.continueMiniClassId != null)
                return false;
        } else if (!continueMiniClassId.equals(other.continueMiniClassId))
            return false;
        if (contractId == null) {
            if (other.contractId != null)
                return false;
        } else if (!contractId.equals(other.contractId))
            return false;
        if (contractProductSubVos == null) {
            if (other.contractProductSubVos != null)
                return false;
        } else if (!contractProductSubVos.equals(other.contractProductSubVos))
            return false;
        if (dealDiscount == null) {
            if (other.dealDiscount != null)
                return false;
        } else if (!dealDiscount.equals(other.dealDiscount))
            return false;
        if (distributableHours == null) {
            if (other.distributableHours != null)
                return false;
        } else if (!distributableHours.equals(other.distributableHours))
            return false;
        if (escClassId == null) {
            if (other.escClassId != null)
                return false;
        } else if (!escClassId.equals(other.escClassId))
            return false;
        if (escClassName == null) {
            if (other.escClassName != null)
                return false;
        } else if (!escClassName.equals(other.escClassName))
            return false;
        if (extendMiniClassId == null) {
            if (other.extendMiniClassId != null)
                return false;
        } else if (!extendMiniClassId.equals(other.extendMiniClassId))
            return false;
        if (firstSchoolTime == null) {
            if (other.firstSchoolTime != null)
                return false;
        } else if (!firstSchoolTime.equals(other.firstSchoolTime))
            return false;
        if (gradeName == null) {
            if (other.gradeName != null)
                return false;
        } else if (!gradeName.equals(other.gradeName))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (isFrozen == null) {
            if (other.isFrozen != null)
                return false;
        } else if (!isFrozen.equals(other.isFrozen))
            return false;
        if (isUnvalid == null) {
            if (other.isUnvalid != null)
                return false;
        } else if (!isUnvalid.equals(other.isUnvalid))
            return false;
        if (isWashed == null) {
            if (other.isWashed != null)
                return false;
        } else if (!isWashed.equals(other.isWashed))
            return false;
        if (lectureId == null) {
            if (other.lectureId != null)
                return false;
        } else if (!lectureId.equals(other.lectureId))
            return false;
        if (lectureName == null) {
            if (other.lectureName != null)
                return false;
        } else if (!lectureName.equals(other.lectureName))
            return false;
        if (liveId == null) {
            if (other.liveId != null)
                return false;
        } else if (!liveId.equals(other.liveId))
            return false;
        if (maxKeTiquMoney == null) {
            if (other.maxKeTiquMoney != null)
                return false;
        } else if (!maxKeTiquMoney.equals(other.maxKeTiquMoney))
            return false;
        if (miniClassId == null) {
            if (other.miniClassId != null)
                return false;
        } else if (!miniClassId.equals(other.miniClassId))
            return false;
        if (miniClassName == null) {
            if (other.miniClassName != null)
                return false;
        } else if (!miniClassName.equals(other.miniClassName))
            return false;
        if (oooSubjectDistributedHours == null) {
            if (other.oooSubjectDistributedHours != null)
                return false;
        } else if (!oooSubjectDistributedHours
                .equals(other.oooSubjectDistributedHours))
            return false;
        if (oooSubjectDistributedHoursDes == null) {
            if (other.oooSubjectDistributedHoursDes != null)
                return false;
        } else if (!oooSubjectDistributedHoursDes
                .equals(other.oooSubjectDistributedHoursDes))
            return false;
        if (paidAmount == null) {
            if (other.paidAmount != null)
                return false;
        } else if (!paidAmount.equals(other.paidAmount))
            return false;
        if (paidStatus != other.paidStatus)
            return false;
        if (paidStatusName == null) {
            if (other.paidStatusName != null)
                return false;
        } else if (!paidStatusName.equals(other.paidStatusName))
            return false;
        if (payment == null) {
            if (other.payment != null)
                return false;
        } else if (!payment.equals(other.payment))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (productId == null) {
            if (other.productId != null)
                return false;
        } else if (!productId.equals(other.productId))
            return false;
        if (productType != other.productType)
            return false;
        if (productTypeFenpeiMoney == null) {
            if (other.productTypeFenpeiMoney != null)
                return false;
        } else if (!productTypeFenpeiMoney.equals(other.productTypeFenpeiMoney))
            return false;
        if (productTypeName == null) {
            if (other.productTypeName != null)
                return false;
        } else if (!productTypeName.equals(other.productTypeName))
            return false;
        if (productVo == null) {
            if (other.productVo != null)
                return false;
        } else if (!productVo.equals(other.productVo))
            return false;
        if (productionName == null) {
            if (other.productionName != null)
                return false;
        } else if (!productionName.equals(other.productionName))
            return false;
        if (promotionAmount == null) {
            if (other.promotionAmount != null)
                return false;
        } else if (!promotionAmount.equals(other.promotionAmount))
            return false;
        if (promotionConsumeAmount == null) {
            if (other.promotionConsumeAmount != null)
                return false;
        } else if (!promotionConsumeAmount.equals(other.promotionConsumeAmount))
            return false;
        if (promotionIds == null) {
            if (other.promotionIds != null)
                return false;
        } else if (!promotionIds.equals(other.promotionIds))
            return false;
        if (promotionJson == null) {
            if (other.promotionJson != null)
                return false;
        } else if (!promotionJson.equals(other.promotionJson))
            return false;
        if (quantity == null) {
            if (other.quantity != null)
                return false;
        } else if (!quantity.equals(other.quantity))
            return false;
        if (quantityInProduct == null) {
            if (other.quantityInProduct != null)
                return false;
        } else if (!quantityInProduct.equals(other.quantityInProduct))
            return false;
        if (realAmount == null) {
            if (other.realAmount != null)
                return false;
        } else if (!realAmount.equals(other.realAmount))
            return false;
        if (realBuyHours == null) {
            if (other.realBuyHours != null)
                return false;
        } else if (!realBuyHours.equals(other.realBuyHours))
            return false;
        if (realConsumeAmount == null) {
            if (other.realConsumeAmount != null)
                return false;
        } else if (!realConsumeAmount.equals(other.realConsumeAmount))
            return false;
        if (remainCourseHour == null) {
            if (other.remainCourseHour != null)
                return false;
        } else if (!remainCourseHour.equals(other.remainCourseHour))
            return false;
        if (remainFinance == null) {
            if (other.remainFinance != null)
                return false;
        } else if (!remainFinance.equals(other.remainFinance))
            return false;
        if (remainingAmount == null) {
            if (other.remainingAmount != null)
                return false;
        } else if (!remainingAmount.equals(other.remainingAmount))
            return false;
        if (remainingAmountOfBasicAmount == null) {
            if (other.remainingAmountOfBasicAmount != null)
                return false;
        } else if (!remainingAmountOfBasicAmount
                .equals(other.remainingAmountOfBasicAmount))
            return false;
        if (remainingAmountOfPromotionAmount == null) {
            if (other.remainingAmountOfPromotionAmount != null)
                return false;
        } else if (!remainingAmountOfPromotionAmount
                .equals(other.remainingAmountOfPromotionAmount))
            return false;
        if (signByWhoName == null) {
            if (other.signByWhoName != null)
                return false;
        } else if (!signByWhoName.equals(other.signByWhoName))
            return false;
        if (signTime == null) {
            if (other.signTime != null)
                return false;
        } else if (!signTime.equals(other.signTime))
            return false;
        if (status != other.status)
            return false;
        if (statusName == null) {
            if (other.statusName != null)
                return false;
        } else if (!statusName.equals(other.statusName))
            return false;
        if (studentId == null) {
            if (other.studentId != null)
                return false;
        } else if (!studentId.equals(other.studentId))
            return false;
        if (studentName == null) {
            if (other.studentName != null)
                return false;
        } else if (!studentName.equals(other.studentName))
            return false;
        if (subjectIds == null) {
            if (other.subjectIds != null)
                return false;
        } else if (!subjectIds.equals(other.subjectIds))
            return false;
        if (teacherName == null) {
            if (other.teacherName != null)
                return false;
        } else if (!teacherName.equals(other.teacherName))
            return false;
        if (totalAmount == null) {
            if (other.totalAmount != null)
                return false;
        } else if (!totalAmount.equals(other.totalAmount))
            return false;
        if (totalHours == null) {
            if (other.totalHours != null)
                return false;
        } else if (!totalHours.equals(other.totalHours))
            return false;
        if (twoClassId != other.twoClassId)
            return false;
        if (twoClassName == null) {
            if (other.twoClassName != null)
                return false;
        } else if (!twoClassName.equals(other.twoClassName))
            return false;
        if (unDistributedMoney == null) {
            if (other.unDistributedMoney != null)
                return false;
        } else if (!unDistributedMoney.equals(other.unDistributedMoney))
            return false;
		if (hashFlag == null) {
			if (other.hashFlag != null)
				return false;
		} else if (!hashFlag.equals(other.hashFlag))
			return false;
        return true;
    }

}