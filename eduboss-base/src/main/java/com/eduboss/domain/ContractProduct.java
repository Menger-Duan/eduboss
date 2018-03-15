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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ProductType;

/**
 * ContractProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "contract_product")
public class ContractProduct implements java.io.Serializable {

	// Fields

	private String id;
	private Product product;
	private Contract contract;
	private BigDecimal quantity;
	private BigDecimal quantityInProduct;	// 产品内部的数量， 可以是目标班的 预定上课数量等
	private BigDecimal dealDiscount;
	private BigDecimal price;
	private BigDecimal planAmount;
	private BigDecimal paidAmount;
	private BigDecimal consumeAmount;
	private BigDecimal realConsumeAmount;
	private BigDecimal promotionConsumeAmount;
	private BigDecimal consumeQuanity;
	private BigDecimal remainingAmount;
	
	private BigDecimal remainingAmountOfBasicAmount;
	private BigDecimal remainingAmountOfPromotionAmount;
	
	private BigDecimal totalAmount; 
	
	
	private String promotionIds ;
	private String promotionJson ;
	private BigDecimal promotionAmount;

	private String promotionDesc ;
	
	private BigDecimal realAmount;
	
	private ProductType type;
	private ContractProductStatus status;
	private ContractProductPaidStatus paidStatus;
	private String paidTime;
	
	private int version;
	
//	private MiniClassModalVo miniClass;
	
	private String miniClassId;
	private String miniClassName;
	
	private String createTime;
	
	
	private Set<ContractProductSubject> prodSubjects = new HashSet<ContractProductSubject>();
	
	private Integer isFrozen; // 0：冻结，1：不冻结
	
	private BigDecimal subjectRemainHours; // 指定科目剩余课时 一对一合同产品
	
	private BigDecimal oooSubjectDistributedHours; // 一对一合同产品已分配到科目的课时
	
	private String oooSubjectDistributedHoursDes; // 已分配课时描述
	
	// Constructors

	public ContractProduct(String id) {
		this.id=id;
	}
	
	/** default constructor */
	public ContractProduct() {
		this.quantity = BigDecimal.ZERO;
		this.quantityInProduct = BigDecimal.ZERO;
		this.dealDiscount = BigDecimal.ZERO;
		this.planAmount = BigDecimal.ZERO;		// 是在合同产品订立的时候  从价格数量计算出来的值， 因为要使用它来就算优惠
		this.price = BigDecimal.ZERO;
		
		this.paidAmount = BigDecimal.ZERO;
		this.consumeAmount = BigDecimal.ZERO;
		this.realConsumeAmount = BigDecimal.ZERO;
		this.promotionConsumeAmount = BigDecimal.ZERO;
		this.consumeQuanity = BigDecimal.ZERO;
		this.remainingAmount =  BigDecimal.ZERO;
		
		this.remainingAmountOfBasicAmount = BigDecimal.ZERO; 
		this.remainingAmountOfPromotionAmount = BigDecimal.ZERO;
		
		this.promotionIds = "";
		this.promotionJson = "";

		this.promotionDesc = "";
		this.promotionAmount = BigDecimal.ZERO;
		
		this.realAmount = BigDecimal.ZERO;
		
		this.status = ContractProductStatus.NORMAL;
		this.paidStatus = ContractProductPaidStatus.UNPAY;
		this.isFrozen = 1;
		
	}

	/** full constructor */
	public ContractProduct(Product product, Contract contract, BigDecimal quantity, BigDecimal dealDiscount, BigDecimal planAmount) {
		this.product = product;
		this.contract = contract;
		this.quantity = quantity;
		this.dealDiscount = dealDiscount;
		
	}

//	public ContractProduct(String id) {
//		this.id = id;
//	}

	// Property accessors
	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID")
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return this.contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Column(name = "QUANTITY",  precision = 10)
	public BigDecimal getQuantity() {
		return this.quantity == null? BigDecimal.ONE : this.quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	@Column(name = "DEAL_DISCOUNT", precision = 10)
	public BigDecimal getDealDiscount() {
		return this.dealDiscount == null ? BigDecimal.ONE : this.dealDiscount;
	}

	public void setDealDiscount(BigDecimal dealDiscount) {
		this.dealDiscount = dealDiscount;
	}
	
	@Column(name = "PRICE", precision = 10)
	public BigDecimal getPrice() {
		return this.price == null ? BigDecimal.ZERO : this.price ;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	@Column(name = "PLAN_AMOUNT", precision = 10)
	public BigDecimal getPlanAmount() {
		return this.planAmount == null ? BigDecimal.ZERO : this.planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}
	

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", length = 32)
	public ContractProductStatus getStatus() {
		return status;
	}

	public void setStatus(ContractProductStatus status) {
		this.status = status;
	}
	
	@Column(name = "PAID_AMOUNT", precision = 10)
	public BigDecimal getPaidAmount() {
		return this.paidAmount == null ? BigDecimal.ZERO : this.paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	
	@Column(name = "CONSUME_AMOUNT", precision = 10)
	public BigDecimal getConsumeAmount() {
		return this.consumeAmount == null ? BigDecimal.ZERO : this.consumeAmount;
	}

	public void setConsumeAmount(BigDecimal consumeAmount) {
		this.consumeAmount = consumeAmount;
	}
	
	@Column(name = "REAL_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getRealConsumeAmount() {
		return realConsumeAmount;
	}

	public void setRealConsumeAmount(BigDecimal realConsumeAmount) {
		this.realConsumeAmount = realConsumeAmount;
	}

	@Column(name = "PROMOTION_CONSUME_AMOUNT", precision = 10)
	public BigDecimal getPromotionConsumeAmount() {
		return promotionConsumeAmount;
	}

	public void setPromotionConsumeAmount(BigDecimal promotionConsumeAmount) {
		this.promotionConsumeAmount = promotionConsumeAmount;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "PAID_STATUS", length = 32)
	public ContractProductPaidStatus getPaidStatus() {
		return paidStatus;
	}

	public void setPaidStatus(ContractProductPaidStatus paidStatus) {
		this.paidStatus = paidStatus;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPE", length = 32)
	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}
	
	

	@Column(name = "PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getPromotionAmount() {
//		if (promotionAmount == null) promotionAmount = BigDecimal.ZERO;
		return promotionAmount == null ? BigDecimal.ZERO : promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}

	@Column(name = "CONSUME_QUANTITY", precision = 10)
	public BigDecimal getConsumeQuanity() {
		return this.consumeQuanity == null ? BigDecimal.ZERO : this.consumeQuanity;
	}

	public void setConsumeQuanity(BigDecimal consumeQuanity) {
		this.consumeQuanity = consumeQuanity;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contractProduct")
	public Set<ContractProductSubject> getProdSubjects() {
		return prodSubjects;
	}

	public void setProdSubjects(Set<ContractProductSubject> prodSubjects) {
		this.prodSubjects = prodSubjects;
	}

	@Column(name = "PROMOTION_IDS", length = 1000)
	public String getPromotionIds() {
		return promotionIds;
	}

	public void setPromotionIds(String promotionIds) {
		this.promotionIds = promotionIds;
	}
	
	@Column(name = "PROMOTION_JSON", length = 10000)
	public String getPromotionJson() {
		return promotionJson;
	}

	public void setPromotionJson(String promotionJson) {
		this.promotionJson = promotionJson;
	}
	
	@Column(name = "REAL_AMOUNT", precision = 10)
	public BigDecimal getRealAmount() {
		if (realAmount == null) realAmount = BigDecimal.ZERO;
		return this.realAmount == null ? BigDecimal.ZERO : this.realAmount;
	}

	public void setRealAmount(BigDecimal realAmount) {
		this.realAmount = realAmount;
	}

	@Transient
	public BigDecimal getRemainingAmount() {
		// 如果 这个产品是 退费 或者 结课 的 ， 剩余资金都是 0. 
		if(this.status != ContractProductStatus.CLOSE_PRODUCT && this.status != ContractProductStatus.REFUNDED && this.status != ContractProductStatus.ENDED ) {
			if(this.getPaidStatus() == ContractProductPaidStatus.PAID){
				return this.getPaidAmount().add(this.getPromotionAmount()).subtract(this.getConsumeAmount()); 
			} else {
				return this.getPaidAmount().subtract(this.getConsumeAmount());
			}
		} else {
			return BigDecimal.ZERO;
		}
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
	@Transient
	public BigDecimal getTotalAmount() {
		if(this.promotionAmount==null){this.promotionAmount=BigDecimal.ZERO;}
		if(this.realAmount==null){this.realAmount=BigDecimal.ZERO;}
		return this.promotionAmount.add(this.realAmount);
	}

	public void setTotalAmont(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Transient
	public BigDecimal getRemainingAmountOfBasicAmount() {
		// 如果 这个产品是 退费 或者 结课 的 ， 剩余资金都是 0. 
		if(this.status != ContractProductStatus.CLOSE_PRODUCT && this.status != ContractProductStatus.REFUNDED ) {
			return paidAmount.subtract(realConsumeAmount);
		} else {
			// 其他状态下 的 正常账户 和消费账户都为 0
			return BigDecimal.ZERO;
		}
	}
	
	/*@Transient
	public BigDecimal getRemainingAmountOfBasicAmount() {
		// 如果 这个产品是 退费 或者 结课 的 ， 剩余资金都是 0. 
		if(this.status != ContractProductStatus.CLOSE_PRODUCT && this.status != ContractProductStatus.REFUNDED ) {
			if(this.getPaidStatus() == ContractProductPaidStatus.PAID){
				// Normal 正常状态的，   PAID状态 ，  如果 实际分配的资金 大于 消费资金 ，  RemainingAmountOfBasicAmount = Paid - consumeAmount,  remainingAmountOfPromotionAmount = promotionAmount;
				// Normal 正常状态的，   PAID状态 ，  如果 实际分配的资金 小于 消费资金 ，  RemainingAmountOfBasicAmount = 0, remainingAmountOfPromotionAmount = promotionAmount + Paid - consumeAmount;
				return this.paidAmount.compareTo(this.consumeAmount)>=0?this.paidAmount.subtract(this.consumeAmount): BigDecimal.ZERO ;
			} else {
				// Normal 正常状态的，   unpaid or paying 状态 ，  如果 实际分配的资金 大于 消费资金，  RemainingAmountOfBasicAmount = Paid - consumeAmount, remainingAmountOfPromotionAmount =0;
				// Normal 正常状态的，   unpaid or paying状态 ，  如果 实际分配的资金 小于 消费资金，  报错
//				if(this.paidAmount.compareTo(this.consumeAmount)>=0) {
//					return this.paidAmount.subtract(this.consumeAmount);
//				} else {
//					throw new ApplicationException("本合同产品数据有错！");
//				}
				return this.paidAmount.subtract(this.consumeAmount);
			}
		} else {
			// 其他状态下 的 正常账户 和消费账户都为 0
			return BigDecimal.ZERO;
		}
	}*/

//	public static void main(String[] args) {
//		BigDecimal a = new BigDecimal(0);
//		BigDecimal b = new BigDecimal(20);
//		System.out.println(a.subtract(b));
//	}
	
	public void setRemainingAmountOfBasicAmount(
			BigDecimal remainingAmountOfBasicAmount) {
		this.remainingAmountOfBasicAmount = remainingAmountOfBasicAmount;
	}
	
	@Transient
	public BigDecimal getRemainingAmountOfPromotionAmount() {
		// 如果 这个产品是 退费 或者 结课 的 ， 剩余资金都是 0. 
		if(this.status != ContractProductStatus.CLOSE_PRODUCT && this.status != ContractProductStatus.REFUNDED ) {
			return this.promotionAmount.subtract(this.promotionConsumeAmount);
		} else {
			// 其他状态下 的 正常账户 和消费账户都为 0
			return BigDecimal.ZERO;
		}
	}
	
/*	@Transient
	public BigDecimal getRemainingAmountOfPromotionAmount() {
		// 如果 这个产品是 退费 或者 结课 的 ， 剩余资金都是 0. 
		if(this.status != ContractProductStatus.CLOSE_PRODUCT && this.status != ContractProductStatus.REFUNDED ) {
			if(this.getPaidStatus() == ContractProductPaidStatus.PAID){
				// Normal 正常状态的，   PAID状态 ，  如果 消费资金小于 实际分配的资金，  RemainingAmountOfBasicAmount = Paid - consumeAmount,  remainingAmountOfPromotionAmount = promotionAmount;
				// Normal 正常状态的，   PAID状态 ，  如果 消费资金大于 实际分配的资金，  RemainingAmountOfBasicAmount = 0, remainingAmountOfPromotionAmount = promotionAmount + Paid - consumeAmount;
				return this.paidAmount.compareTo(this.consumeAmount)>=0?this.promotionAmount: this.promotionAmount.add(this.paidAmount).subtract(this.consumeAmount);
			} else {// 未支付完成优惠不能使用所以剩余也是0
				// Normal 正常状态的，   unpaid or paying 状态 ，  如果 消费资金小于 实际分配的资金，  RemainingAmountOfBasicAmount = Paid - consumeAmount, remainingAmountOfPromotionAmount =0;
				// Normal 正常状态的，   unpaid or paying状态 ，  如果 消费资金大于 实际分配的资金，  报错
//				if(this.paidAmount.compareTo(this.consumeAmount)>=0) {
//					return BigDecimal.ZERO;
//				} else {
//					return paidAmount.subtract(consumeAmount);
//					throw new ApplicationException("本合同产品数据有错！");
//				}
				return BigDecimal.ZERO;
			}
		} else {
			// 其他状态下 的 正常账户 和消费账户都为 0
			return BigDecimal.ZERO;
		}
	}*/

	public void setRemainingAmountOfPromotionAmount(
			BigDecimal remainingAmountOfPromotionAmount) {
		this.remainingAmountOfPromotionAmount = remainingAmountOfPromotionAmount;
	}

	@Column(name = "QUNNTITY_IN_PRODUCT", precision = 10)
	public BigDecimal getQuantityInProduct() {
		return quantityInProduct;
	}

	public void setQuantityInProduct(BigDecimal quantityInProduct) {
		this.quantityInProduct = quantityInProduct;
	}
	
	@Version
	@Column(name = "VERSION", nullable=false,unique=true)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "MINI_CLASS_ID")
	public String getMiniClassId() {
		return miniClassId;
	}

	public void setMiniClassId(String miniClassId) {
		this.miniClassId = miniClassId;
	}

	@Column(name = "MINI_CLASS_NAME")
	public String getMiniClassName() {
		return miniClassName;
	}

	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}

	@Column(name = "PAID_TIME", length = 20)
	public String getPaidTime() {
		return paidTime;
	}

	public void setPaidTime(String paidTime) {
		this.paidTime = paidTime;
	}

	@Column(name = "CREATE_TIME", length = 20, updatable=false, insertable=false)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "IS_FROZEN")
	public Integer getIsFrozen() {
		return isFrozen;
	}

	public void setIsFrozen(Integer isFrozen) {
		this.isFrozen = isFrozen;
	}

	@Transient
	public BigDecimal getSubjectRemainHours() {
		return subjectRemainHours;
	}

	public void setSubjectRemainHours(BigDecimal subjectRemainHours) {
		this.subjectRemainHours = subjectRemainHours;
	}

	@Column(name = "OOO_SUBJECT_DISTRIBUTED_HOURS", precision = 10)
	public BigDecimal getOooSubjectDistributedHours() {
		return oooSubjectDistributedHours;
	}

	public void setOooSubjectDistributedHours(BigDecimal oooSubjectDistributedHours) {
		this.oooSubjectDistributedHours = oooSubjectDistributedHours;
	}

	@Column(name = "OOO_SUBJECT_DISTRIBUTED_HOURS_DES", length = 200)
	public String getOooSubjectDistributedHoursDes() {
		return oooSubjectDistributedHoursDes;
	}

	public void setOooSubjectDistributedHoursDes(
			String oooSubjectDistributedHoursDes) {
		this.oooSubjectDistributedHoursDes = oooSubjectDistributedHoursDes;
	}


}