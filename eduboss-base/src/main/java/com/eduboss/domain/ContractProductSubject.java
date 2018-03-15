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

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractProductType;
import com.eduboss.common.ProductType;

/**
 * ContractProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CONTRACT_PRODUCT_SUBJECT")
public class ContractProductSubject implements java.io.Serializable {

	// Fields

	private String id;
	
	private ContractProduct contractProduct;
	private DataDict subject;
	private BigDecimal quantity;
	private BigDecimal consumeHours; // 消耗课时
	private Organization blCampus;
	private String createTime;
	private User createByStaff;
	private String modifyTime;
	private User modifyUser;
	
	@Id
	@GenericGenerator(name = "generator", strategy = "com.eduboss.dto.DispNoGenerator")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTRACT_PRODUCT_ID")
	public ContractProduct getContractProduct() {
		return contractProduct;
	}
	public void setContractProduct(ContractProduct contractProduct) {
		this.contractProduct = contractProduct;
	}
	
	@Column(name = "QUANTITY", precision = 10)
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "CONSUME_HOURS", precision = 10)
	public BigDecimal getConsumeHours() {
		return consumeHours;
	}
	public void setConsumeHours(BigDecimal consumeHours) {
		this.consumeHours = consumeHours;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BL_CAMPUS_ID")
	public Organization getBlCampus() {
		return blCampus;
	}
	public void setBlCampus(Organization blCampus) {
		this.blCampus = blCampus;
	}
	
	@Column(name = "CREATE_TIME", length = 20)
	public String getCreateTime() {
		return createTime;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBJECT_ID")
	public DataDict getSubject() {
		return subject;
	}
	public void setSubject(DataDict subject) {
		this.subject = subject;
	}
	
	@Column(name = "MODIFY_TIME", length = 20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODIFY_USER_ID")
	public User getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(User modifyUser) {
		this.modifyUser = modifyUser;
	}
	

}