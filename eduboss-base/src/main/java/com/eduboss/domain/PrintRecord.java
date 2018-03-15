package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.eduboss.common.PrintType;

/**
 * PrintRecord entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRINT_RECORD")
public class PrintRecord implements java.io.Serializable {

	// Fields

	private String id;
	private PrintType printType;
	private String businessId;
	private String operatorId;
	private String printTime;

	// Constructors

	/** default constructor */
	public PrintRecord() {
	}

	/** full constructor */
	public PrintRecord(String id, PrintType printType, String businessId,
			String operatorId, String printTime) {
		this.id = id;
		this.printType = printType;
		this.businessId = businessId;
		this.operatorId = operatorId;
		this.printTime = printTime;
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

	@Enumerated(EnumType.STRING)
	@Column(name = "PRINT_TYPE", length = 32)
	public PrintType getPrintType() {
		return this.printType;
	}

	public void setPrintType(PrintType printType) {
		this.printType = printType;
	}

	@Column(name = "BUSINESS_ID", length = 50)
	public String getBusinessId() {
		return this.businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Column(name = "OPERATOR_ID", length = 32)
	public String getOperatorId() {
		return this.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name = "PRINT_TIME", length = 32)
	public String getPrintTime() {
		return this.printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}

}