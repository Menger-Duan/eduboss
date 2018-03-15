package com.eduboss.domainVo;

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
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.User;

/**
 * ContractProduct entity. @author MyEclipse Persistence Tools
 */

public class ContractProductSubjectVo implements java.io.Serializable {

	private static final long serialVersionUID = 1376619568151886352L;
	
	// Fields
	private String id;
	private String contractProductId;
	private String subjectId;
	private String subjectName;
	private BigDecimal quantity;
	private BigDecimal consumeHours;
	private BigDecimal transactionHours;
	private String toSubjectId;
	private String toCampusId;
	private String blCampusId;
	private String blCampusName;
	private String createTime;
	private String createUserId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
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
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getConsumeHours() {
		return consumeHours;
	}
	public void setConsumeHours(BigDecimal consumeHours) {
		this.consumeHours = consumeHours;
	}
	public BigDecimal getTransactionHours() {
		return transactionHours;
	}
	public void setTransactionHours(BigDecimal transactionHours) {
		this.transactionHours = transactionHours;
	}
	public String getToSubjectId() {
		return toSubjectId;
	}
	public void setToSubjectId(String toSubjectId) {
		this.toSubjectId = toSubjectId;
	}
	public String getToCampusId() {
		return toCampusId;
	}
	public void setToCampusId(String toCampusId) {
		this.toCampusId = toCampusId;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getBlCampusName() {
		return blCampusName;
	}
	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}