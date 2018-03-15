package com.eduboss.domain;

import java.math.BigDecimal;
import java.util.Set;

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

import com.eduboss.common.*;
import org.hibernate.annotations.GenericGenerator;

import com.eduboss.utils.DateTools;

@Entity
@Table(name = "contract_record")
public class ContractRecord implements java.io.Serializable {

	/**
	 * 合同修改记录表，记录一些主要字段的变动
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private ContractType contractType;
	private ContractStatus contractStatus;
	private ContractPaidStatus paidStatus;	
	private Contract contract;
	private User signStaff;
	private Student student;
	private DataDict gradeDict;//合同年级   
	private User createByStaff;
	private String createTime;
	private String remark;
	
	private BigDecimal totalAmount;//总额
	private BigDecimal paidAmount;//支付总额
	private BigDecimal oooAmount;//一对一金额
	private BigDecimal miniAmount;//小班金额
	private BigDecimal omAmount;//一对多金额
	private BigDecimal promiseAmount;//目标班金额
	private BigDecimal otherAmount;//其他金额
	private BigDecimal promotionAmount;//优惠金额
	private BigDecimal toPayAmount;//待支付金额
	private BigDecimal twoTeacherAmount;//双师金额
	private String blCampusId;  //合同校区
	private UpdateType updateType;//更新类型
	
	public ContractRecord() {
		super();
		this.student = null;
		this.contractType = null;
		this.signStaff = null;
		this.contractStatus = ContractStatus.NORMAL;
		this.paidStatus = ContractPaidStatus.UNPAY;
		this.createTime = DateTools.getCurrentDateTime();
		this.createByStaff = null;
		this.remark = null;
		this.totalAmount = BigDecimal.ZERO;
		this.paidAmount = BigDecimal.ZERO;
		this.promotionAmount = BigDecimal.ZERO;
	}

	public ContractRecord(String id) {
		this.id = id;
	}
	
	public ContractRecord(Contract contract){
//			ContractRecord this=new ContractRecord();
			this.setBlCampusId(contract.getBlCampusId());
			this.setPaidStatus(contract.getPaidStatus());
			this.setContractStatus(contract.getContractStatus());
			this.setContractType(contract.getContractType());
			this.setContract(contract);
			this.setGradeDict(contract.getGradeDict());
			this.setSignStaff(contract.getSignStaff());
			this.setPaidAmount(contract.getPaidAmount());
			this.setToPayAmount(contract.getPendingAmount());
			this.createTime = DateTools.getCurrentDateTime();
			this.setStudent(contract.getStudent());
			// 计算金额值
			Set<ContractProduct> conProds = contract.getContractProducts();
			BigDecimal productTotalAmount = BigDecimal.ZERO;
			BigDecimal oneOnOneTotalAmount = BigDecimal.ZERO;
			BigDecimal miniClassTotalAmount = BigDecimal.ZERO;
			BigDecimal oneOnManyTotalAmount = BigDecimal.ZERO;
			BigDecimal otherTotalAmount = BigDecimal.ZERO;
			BigDecimal promiseClassTotalAmount = BigDecimal.ZERO;
			BigDecimal promotionAmount = BigDecimal.ZERO;
			BigDecimal twoTeacherAmount = BigDecimal.ZERO;
//			BigDecimal arrangedAmount = BigDecimal.ZERO;
//			BigDecimal consumeAmount = BigDecimal.ZERO;
//			BigDecimal remainingAmount = BigDecimal.ZERO;
//			BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;

			for(ContractProduct conProdInLoop : conProds) {
				if(conProdInLoop.getPaidStatus().equals(ContractPaidStatus.CANCELED) || conProdInLoop.getStatus().equals(ContractProductStatus.UNVALID)){
					continue;
				}
				switch (conProdInLoop.getType() ){
				case ONE_ON_ONE_COURSE : 
					oneOnOneTotalAmount = oneOnOneTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
					break;
				case SMALL_CLASS :  
					miniClassTotalAmount = miniClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
					break;
				case OTHERS:  
					otherTotalAmount = otherTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
					break;
				case ECS_CLASS:  
					promiseClassTotalAmount = promiseClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
					break;
				case ONE_ON_MANY:
					oneOnManyTotalAmount = oneOnManyTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				case TWO_TEACHER:
					twoTeacherAmount=twoTeacherAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				}
				//  合同产品的 剩余资金要 等于 “实际支付” - “已经支付”， 不能用total Amount， 因为totalAmount 是用于计算 promotion的金额。
				productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				promotionAmount = promotionAmount.add(conProdInLoop.getPromotionAmount());
//				arrangedAmount = arrangedAmount.add(conProdInLoop.getPaidAmount());
//				consumeAmount = consumeAmount.add(conProdInLoop.getConsumeAmount());
//				remainingAmount = remainingAmount.add(conProdInLoop.getRemainingAmount());
			}
				
			this.setTotalAmount(productTotalAmount);
			this.setOooAmount(oneOnOneTotalAmount);
			this.setMiniAmount(miniClassTotalAmount);
			this.setOmAmount(oneOnManyTotalAmount);
			this.setOtherAmount(otherTotalAmount);
			this.setPromiseAmount(promiseClassTotalAmount);
			this.setPromotionAmount(promotionAmount);
			this.setTwoTeacherAmount(twoTeacherAmount);
//			cr.setCreateByStaff(userService.getCurrentLoginUser());
//			contract.setTotalAmount(productTotalAmount);
//			contract.setOneOnOneTotalAmount(oneOnOneTotalAmount);
//			contract.setMiniClassTotalAmount(miniClassTotalAmount);
//			contract.setOneOnManyTotalAmount(oneOnManyTotalAmount);
//			contract.setOtherTotalAmount(otherTotalAmount);
//			
//			contract.setPromiseClassTotalAmount(promiseClassTotalAmount);
//			
//			contract.setPromotionAmount(promotionAmount);
//			contract.setConsumeAmount(consumeAmount);
//			contract.setRemainingAmount(remainingAmount);
	//
//			contract.setOneOnOneRemainingHour(oneOnOneRemainingHour);
//			
//			contract.setAvailableAmount(contract.getPaidAmount().subtract(arrangedAmount));
	}
	
	@Override
	public boolean equals(Object obj) {
		ContractRecord record=(ContractRecord)obj;
		if(this.blCampusId.equals(record.getBlCampusId()) &&
		   this.contractStatus.equals(record.getContractStatus())	&&
		   this.contractType.equals(record.getContractType()) &&
		   this.gradeDict.getId().equals(record.getGradeDict().getId()) &&
		   this.miniAmount.compareTo(record.getMiniAmount())==0
		   && this.omAmount.compareTo(record.omAmount)==0
		   && this.oooAmount.compareTo(record.getOooAmount())==0
		   && this.promiseAmount.compareTo(record.getPromiseAmount())==0
		   && this.promotionAmount.compareTo(record.getPromotionAmount())==0
		   && this.totalAmount.compareTo(record.getTotalAmount())==0
			&& this.twoTeacherAmount.compareTo(record.getTwoTeacherAmount())==0
				){
			return true;
		}
		return false;
	}

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

	@Column(name = "REMARK", length = 256)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
	@Column(name = "TOTAL_AMOUNT", precision = 10)
	public BigDecimal getTotalAmount() {
		return this.totalAmount;
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

	
	@Column(name = "PROMOTION_AMOUNT", precision = 10)
	public BigDecimal getPromotionAmount() {
		return promotionAmount;
	}

	public void setPromotionAmount(BigDecimal promotionAmount) {
		this.promotionAmount = promotionAmount;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRADE_ID")
	public DataDict getGradeDict() {
		return gradeDict;
	}

	public void setGradeDict(DataDict gradeDict) {
		this.gradeDict = gradeDict;
	}
	
	@Column(name = "BL_CAMPUS_ID", length = 32)
	public String getBlCampusId() {
		return blCampusId;
	}

	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_ID")
	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	@Column(name = "OOO_AMOUNT", precision = 10)
	public BigDecimal getOooAmount() {
		return oooAmount;
	}

	public void setOooAmount(BigDecimal oooAmount) {
		this.oooAmount = oooAmount;
	}

	@Column(name = "OM_AMOUNT", precision = 10)
	public BigDecimal getOmAmount() {
		return omAmount;
	}

	public void setOmAmount(BigDecimal omAmount) {
		this.omAmount = omAmount;
	}

	@Column(name = "PROMISE_AMOUNT", precision = 10)
	public BigDecimal getPromiseAmount() {
		return promiseAmount;
	}

	public void setPromiseAmount(BigDecimal promiseAmount) {
		this.promiseAmount = promiseAmount;
	}

	@Column(name = "OTHER_AMOUNT", precision = 10)
	public BigDecimal getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}

	@Column(name = "TOPAY_AMOUNT", precision = 10)
	public BigDecimal getToPayAmount() {
		return toPayAmount;
	}

	public void setToPayAmount(BigDecimal toPayAmount) {
		this.toPayAmount = toPayAmount;
	}

	
	@Enumerated(EnumType.STRING)
	@Column(name = "UPDATE_TYPE", length = 10)
	public UpdateType getUpdateType() {
		return updateType;
	}

	public void setUpdateType(UpdateType updateType) {
		this.updateType = updateType;
	}

	@Column(name = "MINI_AMOUNT", precision = 10)
	public BigDecimal getMiniAmount() {
		return miniAmount;
	}

	public void setMiniAmount(BigDecimal miniAmount) {
		this.miniAmount = miniAmount;
	}


	@Column(name = "TWO_TEACHER_AMOUNT", precision = 10)
	public BigDecimal getTwoTeacherAmount() {
		return twoTeacherAmount;
	}

	public void setTwoTeacherAmount(BigDecimal twoTeacherAmount) {
		this.twoTeacherAmount = twoTeacherAmount;
	}
}