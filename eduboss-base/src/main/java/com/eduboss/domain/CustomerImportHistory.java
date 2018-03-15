package com.eduboss.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.CustomerDeliverType;
import com.eduboss.dto.Response;

/**
 * 客户导入记录
 *
 */
@Entity
@Table(name = "customer_import_history")
public class CustomerImportHistory extends Response implements java.io.Serializable {

	// Fields

	private String id;
	private String importName;
	private String resultName;
	private String createUser;
	private String createTime;
	private BigDecimal totalNum = BigDecimal.ZERO;
	private BigDecimal successNum = BigDecimal.ZERO;
	private BigDecimal failNum = BigDecimal.ZERO;
	private String status;
	
	private String deliverTarget;
	private String deliverTargetName;
	private CustomerDeliverType deliverType;
	
	
	public CustomerImportHistory() {
		super();
	}


	public CustomerImportHistory(String importName, String resultName, String createUser, String createTime,
			BigDecimal totalNum, BigDecimal successNum, BigDecimal failNum, String status, String deliverTarget,
			String deliverTargetName, CustomerDeliverType deliverType) {
		super();
		this.importName = importName;
		this.resultName = resultName;
		this.createUser = createUser;
		this.createTime = createTime;
		this.totalNum = totalNum;
		this.successNum = successNum;
		this.failNum = failNum;
		this.status = status;
		this.deliverTarget = deliverTarget;
		this.deliverTargetName = deliverTargetName;
		this.deliverType = deliverType;
	}




	@Id
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", unique = true, nullable = false, length = 32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "import_name", length = 255)
	public String getImportName() {
		return importName;
	}


	public void setImportName(String importName) {
		this.importName = importName;
	}

	@Column(name = "result_name", length = 255)
	public String getResultName() {
		return resultName;
	}


	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	@Column(name = "create_user", length = 32)
	public String getCreateUser() {
		return createUser;
	}


	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name = "create_time", length = 20)
	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "total_num", precision = 10)
	public BigDecimal getTotalNum() {
		return totalNum;
	}


	public void setTotalNum(BigDecimal totalNum) {
		this.totalNum = totalNum;
	}

	@Column(name = "success_num", precision = 10)
	public BigDecimal getSuccessNum() {
		return successNum;
	}


	public void setSuccessNum(BigDecimal successNum) {
		this.successNum = successNum;
	}

	@Column(name = "fail_num", precision = 10)
	public BigDecimal getFailNum() {
		return failNum;
	}


	public void setFailNum(BigDecimal failNum) {
		this.failNum = failNum;
	}

	@Column(name = "status", precision = 10)
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "deliver_target", length = 32)
	public String getDeliverTarget() {
		return deliverTarget;
	}

	public void setDeliverTarget(String deliverTarget) {
		this.deliverTarget = deliverTarget;
	}
	@Column(name = "deliver_target_name", length = 32)
	public String getDeliverTargetName() {
		return deliverTargetName;
	}

	public void setDeliverTargetName(String deliverTargetName) {
		this.deliverTargetName = deliverTargetName;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "deliver_type", length = 32)
	public CustomerDeliverType getDeliverType() {
		return deliverType;
	}

	public void setDeliverType(CustomerDeliverType deliverType) {
		this.deliverType = deliverType;
	}


	@Override
	public String toString() {
		return "CustomerImportHistory [id=" + id + ", importName=" + importName + ", resultName=" + resultName
				+ ", createUser=" + createUser + ", createTime=" + createTime + ", totalNum=" + totalNum
				+ ", successNum=" + successNum + ", failNum=" + failNum + ", status=" + status + ", deliverTarget="
				+ deliverTarget + ", deliverTargetName=" + deliverTargetName + ", deliverType=" + deliverType + "]";
	}


	
}