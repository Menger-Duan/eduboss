package com.eduboss.domainVo;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.DistributeType;
import com.eduboss.common.ProductType;



public class ContractProductDistributeVo implements java.io.Serializable {

	private static final long serialVersionUID = -5514234416840883567L;
	
	// Fields
	private String id;
	private BigDecimal totalHours; // 总课时
	private BigDecimal promotionHours; // 优惠课时
	private BigDecimal consumeHours; //消耗课时
	private BigDecimal availableHours; // 可分配课时
	private BigDecimal distributedHours; // 已分配课时
	private BigDecimal distributableHours; // 未分配课时
	private String subjectIds; // 可分配科目id
	private ContractProductStatus status;
	private String studentId;
	private String blCampusId;
	private String remark;
	private DistributeType distributeType;
    private List<ContractProductSubjectVo> contractProductSubVos =  new ArrayList<ContractProductSubjectVo>();
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(BigDecimal totalHours) {
		this.totalHours = totalHours;
	}
	public BigDecimal getPromotionHours() {
		return promotionHours;
	}
	public void setPromotionHours(BigDecimal promotionHours) {
		this.promotionHours = promotionHours;
	}
	public BigDecimal getConsumeHours() {
		return consumeHours;
	}
	public void setConsumeHours(BigDecimal consumeHours) {
		this.consumeHours = consumeHours;
	}
	public BigDecimal getAvailableHours() {
		return availableHours;
	}
	public void setAvailableHours(BigDecimal availableHours) {
		this.availableHours = availableHours;
	}
	public BigDecimal getDistributedHours() {
		return distributedHours;
	}
	public void setDistributedHours(BigDecimal distributedHours) {
		this.distributedHours = distributedHours;
	}
	public BigDecimal getDistributableHours() {
		return distributableHours;
	}
	public void setDistributableHours(BigDecimal distributableHours) {
		this.distributableHours = distributableHours;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public ContractProductStatus getStatus() {
		return status;
	}
	public void setStatus(ContractProductStatus status) {
		this.status = status;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getBlCampusId() {
		return blCampusId;
	}
	public void setBlCampusId(String blCampusId) {
		this.blCampusId = blCampusId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<ContractProductSubjectVo> getContractProductSubVos() {
		return contractProductSubVos;
	}
	public void setContractProductSubVos(
			List<ContractProductSubjectVo> contractProductSubVos) {
		this.contractProductSubVos = contractProductSubVos;
	}
	public DistributeType getDistributeType() {
		return distributeType;
	}
	public void setDistributeType(DistributeType distributeType) {
		this.distributeType = distributeType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
}