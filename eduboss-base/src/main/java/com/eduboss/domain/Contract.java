package com.eduboss.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.eduboss.common.*;
import com.eduboss.utils.DateTools;

/**
 * Contract entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "contract")
public class Contract implements java.io.Serializable {

	// Fields

	private String id;
	private Student student;
	private ContractType contractType;
	private Contract referenceContract;
	private Customer customer;
	private User signStaff;
	
	
	private ContractStatus contractStatus;
	private ContractPaidStatus paidStatus;

	private DataDict gradeDict;//合同年级   
	private String createTime;
	private User createByStaff;
	private String modifyTime;
	private User modifyByStaff;
	private String remark;
	
	private BigDecimal totalAmount;
	private BigDecimal paidAmount;
	private BigDecimal pendingAmount;
	private BigDecimal remainingAmount;
	private BigDecimal consumeAmount;
	private BigDecimal availableAmount;
	private BigDecimal promotionAmount;
	
	
	private BigDecimal oneOnOneUnitPrice;
	private BigDecimal oneOnOneRemainingClass;
	private BigDecimal oneOnOneTotalAmount;
	private BigDecimal oneOnOnePaidAmount;
	private BigDecimal oneOnOneConsumeAmount;
	private BigDecimal oneOnOneRemainingAmount;
	private BigDecimal liveAmount;
	
	private BigDecimal miniClassTotalAmount;
	private BigDecimal miniClassPaidAmount;
	private BigDecimal miniClassConsumeAmount;
	private BigDecimal miniClassRemainingAmount;
	
	private BigDecimal otherTotalAmount;
	private BigDecimal otherPaidAmount;
	private BigDecimal otherConsumeAmount;
	private BigDecimal otherRemainingAmount;
	
	private BigDecimal oneOnManyTotalAmount;	//总金额
	private BigDecimal oneOnManyPaidAmount;		//已付款
	private BigDecimal oneOnManyConsumeAmount;	//消耗
	private BigDecimal oneOnManyRemainingAmount; //剩余金额
	
	private BigDecimal freeTotalHour;
	private BigDecimal freeConsumeHour;
	private BigDecimal freeRemainingHour;
	private BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
	
	private BigDecimal promiseClassTotalAmount;	//总金额
	private BigDecimal lectureClassTotalAmount;	//讲座总金额
	private BigDecimal twoTeacherClassTotalAmount;//双师总额
	
	private String oneOnOneJson;
	
	private Set<ContractProduct> contractProducts = new HashSet<ContractProduct>(0);
	private Set<FundsChangeHistory> fundsChangeHistories = new HashSet<FundsChangeHistory>(0);
	
	private ContractProduct oneOnOneNormalProduct = null;	//  一对一 产品
	private ContractProduct oneOnOneFreeProduct = null;
	private Set<ContractProduct> smallProducts = null;
	private Set<ContractProduct> otherProducts = null;
	private Set<ContractProduct> oneOnManyProducts=null;
	private String blCampusId;  //签单人归属校区
	private StudentSchool school;
	private StudentSchoolTemp schoolTemp; //待审核学校
	private String schoolOrTemp;//显示school or schoolTemp   ： school ： 显示 合同的school字段     schoolTemp ： 显示合同 的schoolTemp 字段
	private boolean ecsContract; //是否目标班合同
	private int isNarrow;//是否缩单  ： 0：否    1：是
	private int pubPayContract;//是否公帐 0：否 1：是

	private String curriculumId;//直播同步合同的订单号
	
	private DataDict posMachineType; // pos机类型


	public Contract() {
		super();
		this.student = null;
		this.contractType = null;
		this.referenceContract = null;
		this.customer = null;
		this.signStaff = null;
		this.contractStatus = ContractStatus.NORMAL;
		this.paidStatus = ContractPaidStatus.UNPAY;
		this.createTime = DateTools.getCurrentDateTime();
		this.createByStaff = null;
		this.modifyTime = DateTools.getCurrentDateTime();
		this.modifyByStaff = null;
		this.remark = null;
		this.totalAmount = BigDecimal.ZERO;
		this.paidAmount = BigDecimal.ZERO;
		this.pendingAmount = BigDecimal.ZERO;
		this.remainingAmount = BigDecimal.ZERO;
		this.consumeAmount = BigDecimal.ZERO;
		this.availableAmount = BigDecimal.ZERO;
		this.oneOnOneUnitPrice = BigDecimal.ZERO;
		this.oneOnOneRemainingClass = BigDecimal.ZERO;
		this.oneOnOneTotalAmount = BigDecimal.ZERO;
		this.oneOnOnePaidAmount = BigDecimal.ZERO;
		this.oneOnOneConsumeAmount = BigDecimal.ZERO;
		this.oneOnOneRemainingAmount = BigDecimal.ZERO;
		this.miniClassTotalAmount = BigDecimal.ZERO;
		this.miniClassPaidAmount = BigDecimal.ZERO;
		this.miniClassConsumeAmount = BigDecimal.ZERO;
		this.miniClassRemainingAmount = BigDecimal.ZERO;
		this.otherTotalAmount = BigDecimal.ZERO;
		this.otherPaidAmount = BigDecimal.ZERO;
		this.otherConsumeAmount = BigDecimal.ZERO;
		this.otherRemainingAmount = BigDecimal.ZERO;
		this.freeTotalHour = BigDecimal.ZERO;
		this.freeConsumeHour = BigDecimal.ZERO;
		this.freeRemainingHour = BigDecimal.ZERO;
		this.promotionAmount = BigDecimal.ZERO;
		this.oneOnManyConsumeAmount=BigDecimal.ZERO;
		this.oneOnManyPaidAmount=BigDecimal.ZERO;
		this.oneOnManyRemainingAmount=BigDecimal.ZERO;
		this.oneOnManyTotalAmount=BigDecimal.ZERO;
		this.oneOnOneJson = null;
		this.contractProducts = new HashSet<ContractProduct>(0);
		this.fundsChangeHistories = new HashSet<FundsChangeHistory>(0);;
		this.oneOnOneNormalProduct = null;
		this.oneOnOneFreeProduct = null;
		this.smallProducts =null;
		this.otherProducts = null;
		this.oneOnManyProducts=null;
		this.school=null;
		this.isNarrow=0;
	}

	// Constructors

	/** default constructor */
	

	public Contract(String contractId) {
		this.id = contractId;
	}

	/** full constructor */
	/*public Contract(Student student, ContractType contractType, String customerId, String signStaffId, BigDecimal totalAmount, BigDecimal depositAmount,
			BigDecimal paidAmount, String bonusStaffaId, BigDecimal bonusStaffaAmount, String bonusStaffbId, BigDecimal bonusStaffbAmount,
			String createTime, String createUserId, String modifyTime, String modifyUserId, Set<ContractProduct> contractProducts,
			Set<FundsChangeHistory> fundsChangeHistories) {
		this.student = student;
		this.contractType = contractType;
		this.customerId = customerId;
		this.signStaffId = signStaffId;
		this.totalAmount = totalAmount;
		this.depositAmount = depositAmount;
		this.paidAmount = paidAmount;
		this.bonusStaffaId = bonusStaffaId;
		this.bonusStaffaAmount = bonusStaffaAmount;
		this.bonusStaffbId = bonusStaffbId;
		this.bonusStaffbAmount = bonusStaffbAmount;
		this.createTime = createTime;
		this.createUserId = createUserId;
		this.modifyTime = modifyTime;
		this.modifyUserId = modifyUserId;
		this.contractProducts = contractProducts;
		this.fundsChangeHistories = fundsChangeHistories;
	}*/

	// Property accessors
	@Id
//	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
//	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDENT_ID")
	public Student getStudent() {
		return this.student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CONTRACT_TYPE", length = 32)
	public ContractType getContractType() {
		return this.contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REFEREN_CONTRACT_ID")
	public Contract getReferenceContract() {
		return referenceContract;
	}

	public void setReferenceContract(Contract referenceContract) {
		this.referenceContract = referenceContract;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID")
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIGN_STAFF_ID")
	public User getSignStaff() {
		return signStaff;
	}

	public void setSignStaff(User signStaff) {
		this.signStaff = signStaff;
	}	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "CONTRACT_STATUS", length = 32)
	public ContractStatus getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(ContractStatus contractStatus) {
		this.contractStatus = contractStatus;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PAID_STATUS", length = 32)
	public ContractPaidStatus getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(ContractPaidStatus paidStatus) {
		this.paidStatus = paidStatus;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_USER_ID")
	public User getCreateByStaff() {
		return createByStaff;
	}

	public void setCreateByStaff(User createByStaff) {
		this.createByStaff = createByStaff;
	}

	
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyByStaff() {
		return modifyByStaff;
	}
	
	public void setModifyByStaff(User modifyByStaff) {
		this.modifyByStaff = modifyByStaff;
	}

	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
//	@Column(name = "TOTAL_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getTotalAmount() {
		BigDecimal amount=BigDecimal.ZERO;
		for (ContractProduct contractProduct : getContractProducts()) {
			if(contractProduct.getStatus().equals(ContractProductStatus.UNVALID) || contractProduct.getPaidStatus().equals(ContractPaidStatus.CANCELED)){continue;}
			amount=amount.add(contractProduct.getRealAmount().add(contractProduct.getPromotionAmount()));
		}
		return amount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Column(name = "PAID_AMOUNT", precision = 10)
	public BigDecimal getPaidAmount() {
		return this.paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	@Transient
//	@Column(name = "PENDING_AMOUNT", precision = 10)
	public BigDecimal getPendingAmount() {
//		return this.getTotalAmount().subtract(this.getPaidAmount()).subtract(this.getPromotionAmount());
		// 合同总金额 - 已付金额 - 优惠金额
		return this.getTotalAmount().subtract(this.getPaidAmount()).subtract(this.getPromotionAmount());
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}
	
//	@Column(name = "REMAINING_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
//	@Column(name = "ONE_ON_ONE_UNIT_PRICE", precision = 10)
	@Transient
	public BigDecimal getOneOnOneUnitPrice() {
		return oneOnOneUnitPrice;
	}

	public void setOneOnOneUnitPrice(BigDecimal oneOnOneUnitPrice) {
		this.oneOnOneUnitPrice = oneOnOneUnitPrice;
	}

//	@Column(name = "ONE_ON_ONE_REMAINING_CLASS", precision = 10)
	@Transient
	public BigDecimal getOneOnOneRemainingClass() {
		return oneOnOneRemainingClass;
	}

	public void setOneOnOneRemainingClass(BigDecimal oneOnOneRemainingClass) {
		this.oneOnOneRemainingClass = oneOnOneRemainingClass;
	}

//	@Column(name = "ONE_ON_ONE_TOTAL_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOneOnOneTotalAmount() {
		return oneOnOneTotalAmount;
	}

	public void setOneOnOneTotalAmount(BigDecimal oneOnOneTotalAmount) {
		this.oneOnOneTotalAmount = oneOnOneTotalAmount;
	}

//	@Column(name = "ONE_ON_ONE_CONSUME_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOneOnOneConsumeAmount() {
		return oneOnOneConsumeAmount;
	}

	public void setOneOnOneConsumeAmount(BigDecimal oneOnOneConsumeAmount) {
		this.oneOnOneConsumeAmount = oneOnOneConsumeAmount;
	}

//	@Column(name = "MINI_CLASS_TOTAL_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getMiniClassTotalAmount() {
		return miniClassTotalAmount;
	}

	public void setMiniClassTotalAmount(BigDecimal miniClassTotalAmount) {
		this.miniClassTotalAmount = miniClassTotalAmount;
	}

//	@Column(name = "MINI_CLASS_CONSUME_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getMiniClassConsumeAmount() {
		return miniClassConsumeAmount;
	}

	public void setMiniClassConsumeAmount(BigDecimal miniClassConsumeAmount) {
		this.miniClassConsumeAmount = miniClassConsumeAmount;
	}

//	@Column(name = "OTHER_TOTAL_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOtherTotalAmount() {
		return otherTotalAmount;
	}

	public void setOtherTotalAmount(BigDecimal otherTotalAmount) {
		this.otherTotalAmount = otherTotalAmount;
	}

//	@Column(name = "OTHER_CONSUME_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOtherConsumeAmount() {
		return otherConsumeAmount;
	}

	public void setOtherConsumeAmount(BigDecimal otherConsumeAmount) {
		this.otherConsumeAmount = otherConsumeAmount;
	}

//	@Column(name = "FREE_TOTAL_HOUR", precision = 10)
	@Transient
	public BigDecimal getFreeTotalHour() {
		return freeTotalHour;
	}

	public void setFreeTotalHour(BigDecimal freeTotalHour) {
		this.freeTotalHour = freeTotalHour;
	}

//	@Column(name = "FREE_CONSUME_HOUR", precision = 10)
	@Transient
	public BigDecimal getFreeConsumeHour() {
		return freeConsumeHour;
	}

	public void setFreeConsumeHour(BigDecimal freeConsumeHour) {
		this.freeConsumeHour = freeConsumeHour;
	}
	
//	@Column(name = "FREE_REMAINING_HOUR", precision = 10)
	@Transient
	public BigDecimal getFreeRemainingHour() {
		return freeRemainingHour;
	}

	public void setFreeRemainingHour(BigDecimal freeRemainingHour) {
		this.freeRemainingHour = freeRemainingHour;
	}

//	@Column(name = "ONE_ON_ONE_REMAINING_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOneOnOneRemainingAmount() {
		return oneOnOneRemainingAmount;
	}

	public void setOneOnOneRemainingAmount(BigDecimal oneOnOneRemainingAmount) {
		this.oneOnOneRemainingAmount = oneOnOneRemainingAmount;
	}

//	@Column(name = "MINI_CLASS_REMAINING_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getMiniClassRemainingAmount() {
		return miniClassRemainingAmount;
	}

	public void setMiniClassRemainingAmount(BigDecimal miniClassRemainingAmount) {
		this.miniClassRemainingAmount = miniClassRemainingAmount;
	}
	
//	@Column(name = "OTHER_REMAINING_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOtherRemainingAmount() {
		return otherRemainingAmount;
	}

	public void setOtherRemainingAmount(BigDecimal otherRemainingAmount) {
		this.otherRemainingAmount = otherRemainingAmount;
	}
	
//	@Column(name = "ONE_ON_ONE_PAID_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOneOnOnePaidAmount() {
		return oneOnOnePaidAmount;
	}

	public void setOneOnOnePaidAmount(BigDecimal oneOnOnePaidAmount) {
		this.oneOnOnePaidAmount = oneOnOnePaidAmount;
	}

//	@Column(name = "MINI_CLASS_PAID_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getMiniClassPaidAmount() {
		return miniClassPaidAmount;
	}

	public void setMiniClassPaidAmount(BigDecimal miniClassPaidAmount) {
		this.miniClassPaidAmount = miniClassPaidAmount;
	}

//	@Column(name = "OTHER_PAID_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getOtherPaidAmount() {
		return otherPaidAmount;
	}

	public void setOtherPaidAmount(BigDecimal otherPaidAmount) {
		this.otherPaidAmount = otherPaidAmount;
	}

//	@Column(name = "CONSUME_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	
//	@Column(name = "ONE_ON_MANY_TotalAmount", precision = 10)
	@Transient
	public BigDecimal getOneOnManyTotalAmount() {
		return oneOnManyTotalAmount;
	}

	public void setOneOnManyTotalAmount(BigDecimal oneOnManyTotalAmount) {
		this.oneOnManyTotalAmount = oneOnManyTotalAmount;
	}

//	@Column(name = "ONE_ON_MANY_PaidAmount", precision = 10)
	@Transient
	public BigDecimal getOneOnManyPaidAmount() {
		return oneOnManyPaidAmount;
	}

	public void setOneOnManyPaidAmount(BigDecimal oneOnManyPaidAmount) {
		this.oneOnManyPaidAmount = oneOnManyPaidAmount;
	}

//	@Column(name = "ONE_ON_MANY_ConsumeAmount", precision = 10)
	@Transient
	public BigDecimal getOneOnManyConsumeAmount() {
		return oneOnManyConsumeAmount;
	}

	public void setOneOnManyConsumeAmount(BigDecimal oneOnManyConsumeAmount) {
		this.oneOnManyConsumeAmount = oneOnManyConsumeAmount;
	}

//	@Column(name = "ONE_ON_MANY_RemainingAmount", precision = 10)
	@Transient
	public BigDecimal getOneOnManyRemainingAmount() {
		return oneOnManyRemainingAmount;
	}

	public void setOneOnManyRemainingAmount(BigDecimal oneOnManyRemainingAmount) {
		this.oneOnManyRemainingAmount = oneOnManyRemainingAmount;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
	@OrderBy("id ASC")
	public Set<ContractProduct> getContractProducts() {
		return this.contractProducts;
	}

	public void setContractProducts(Set<ContractProduct> contractProducts) {
		this.contractProducts = contractProducts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
	public Set<FundsChangeHistory> getFundsChangeHistories() {
		return this.fundsChangeHistories;
	}

	public void setFundsChangeHistories(Set<FundsChangeHistory> fundsChangeHistories) {
		this.fundsChangeHistories = fundsChangeHistories;
	}

//	@Column(name = "ONEONONE_JSON", length = 1000)
	@Transient
	public String getOneOnOneJson() {
		return oneOnOneJson;
	}

	
	public void setOneOnOneJson(String oneOnOneJson) {
		this.oneOnOneJson = oneOnOneJson;
	}
	
	// 辅助属性， 用于获得一对一(赠送课时)， 小班， 和 其他的产品
	@Transient
	public Set<ContractProduct> getSmallProducts() {
		if(this.smallProducts == null) {
			smallProducts = new HashSet<ContractProduct>();
			for(ContractProduct conPro : this.contractProducts) {
				if(conPro.getProduct().getCategory() == ProductType.SMALL_CLASS){
					smallProducts.add(conPro);
				}
			}
		}
		return smallProducts;
	}
	@Transient
	public Set<ContractProduct> getOtherProducts() {
		if(this.otherProducts == null) {
			otherProducts = new HashSet<ContractProduct>();
			for(ContractProduct conPro : this.contractProducts) {
				if(conPro.getProduct().getCategory() == ProductType.OTHERS){
					otherProducts.add(conPro);
				}
			}
		}
		return otherProducts;
	}
	@Transient
	public ContractProduct getOneOnOneNormalProduct() {
		if(this.oneOnOneNormalProduct == null) {
			for(ContractProduct conPro : this.contractProducts) {
				if(conPro.getProduct().getCategory() == ProductType.ONE_ON_ONE_COURSE){
					oneOnOneNormalProduct = conPro;
					break;
				}
			}
		}
		return oneOnOneNormalProduct;
	}
	@Transient
	public ContractProduct getOneOnOneFreeProduct() {
		if(this.oneOnOneFreeProduct == null) {
			for(ContractProduct conPro : this.contractProducts) {
				if(conPro.getProduct().getCategory() == ProductType.ONE_ON_ONE_COURSE_FREE){
					oneOnOneFreeProduct = conPro;
					break;
				}
			}
		}
		return oneOnOneFreeProduct;
	}
	
	@Transient
	public Set<ContractProduct> getOneOnManyProducts() {
		if(this.oneOnManyProducts == null) {
			oneOnManyProducts = new HashSet<ContractProduct>();
			for(ContractProduct conPro : this.contractProducts) {
				if(conPro.getProduct().getCategory() == ProductType.ONE_ON_MANY){
					oneOnManyProducts.add(conPro);
				}
			}
		}
		return oneOnManyProducts;
	}

//	@Column(name = "AVAILABLE_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}
	
//	@Column(name = "PROMOTION_AMOUNT", precision = 10)
	@Transient
	public BigDecimal getPromotionAmount() {
		BigDecimal amount=BigDecimal.ZERO;
		for (ContractProduct contractProduct : getContractProducts()) {
			if(contractProduct.getStatus().equals(ContractProductStatus.UNVALID) || contractProduct.getPaidStatus().equals(ContractPaidStatus.CANCELED)){continue;}
			amount=amount.add(contractProduct.getPromotionAmount());
		}
		return amount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	@Transient
	public BigDecimal getOneOnOneRemainingHour() {
		return oneOnOneRemainingHour;
	}

	public void setOneOnOneRemainingHour(BigDecimal oneOnOneRemainingHour) {
		this.oneOnOneRemainingHour = oneOnOneRemainingHour;
	}
	
	@Transient
	public BigDecimal getPromiseClassTotalAmount() {
		return promiseClassTotalAmount;
	}

	public void setPromiseClassTotalAmount(BigDecimal promiseClassTotalAmount) {
		this.promiseClassTotalAmount = promiseClassTotalAmount;
	}

	//@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID")
	public DataDict getGradeDict() {
		return gradeDict;
	}

	public void setGradeDict(DataDict gradeDict) {
		this.gradeDict = gradeDict;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SCHOOL")
	public StudentSchool getSchool() {
		return this.school;
	}

	public void setSchool(StudentSchool school) {
		this.school = school;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SCHOOL_TEMP")
	public StudentSchoolTemp getSchoolTemp() {
		return schoolTemp;
	}

	public void setSchoolTemp(StudentSchoolTemp schoolTemp) {
		this.schoolTemp = schoolTemp;
	}

	@Transient
	public BigDecimal getLectureClassTotalAmount() {
		return lectureClassTotalAmount;
	}

	public void setLectureClassTotalAmount(BigDecimal lectureClassTotalAmount) {
		this.lectureClassTotalAmount = lectureClassTotalAmount;
	}

	@Column(name = "is_ECS_Contract")
	public boolean isEcsContract() {
		return ecsContract;
	}

	public void setEcsContract(boolean ecsContract) {
		this.ecsContract = ecsContract;
	}

	@Column(name = "isNarrow")
	public int getIsNarrow() {
		return isNarrow;
	}

	public void setIsNarrow(int isNarrow) {
		this.isNarrow = isNarrow;
	}

	@Column(name = "schoolOrTemp")
	public String getSchoolOrTemp() {
		return schoolOrTemp;
	}

	public void setSchoolOrTemp(String schoolOrTemp) {
		this.schoolOrTemp = schoolOrTemp;
	}

	@Transient
	public BigDecimal getTwoTeacherClassTotalAmount() {
		return twoTeacherClassTotalAmount;
	}

	public void setTwoTeacherClassTotalAmount(BigDecimal twoTeacherClassTotalAmount) {
		this.twoTeacherClassTotalAmount = twoTeacherClassTotalAmount;
	}

	@Transient
	public BigDecimal getLiveAmount() {
		return liveAmount;
	}

	public void setLiveAmount(BigDecimal liveAmount) {
		this.liveAmount = liveAmount;
	}

	@Transient
	public BigDecimal getRealAmountByProductType(ProductType type){
		BigDecimal amount=BigDecimal.ZERO;
		for (ContractProduct contractProduct : getContractProducts()) {
			if(contractProduct.getType().equals(type)){
				amount=amount.add(contractProduct.getRealAmount());
			}
		}
		return amount;
	}


	@Column(name = "BL_CAMPUS_ID", length = 32)
	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	@Column(name = "pub_pay_contract")
	public int getPubPayContract() {
		return pubPayContract;
	}

	public void setPubPayContract(int pubPayContract) {
		this.pubPayContract = pubPayContract;
	}

	@Column(name = "curriculum_id")
	public String getCurriculumId() {
		return curriculumId;
	}

	public void setCurriculumId(String curriculumId) {
		this.curriculumId = curriculumId;
	}

	/**
     * pos机类型
     * @return
     */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="pos_machine_type")
    public DataDict getPosMachineType() {
        return posMachineType;
    }

    public void setPosMachineType(DataDict posMachineType) {
        this.posMachineType = posMachineType;
    }
	
}