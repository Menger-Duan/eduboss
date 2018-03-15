package com.eduboss.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author lixuejun
 * 避免重复请求的交易号表
 *
 */
@Entity
@Table(name = "transaction_record")
public class TransactionRecord {

	private String transactionUuid;
	
	public TransactionRecord() {
		super();
	}

	public TransactionRecord(String transactionUuid) {
		super();
		this.transactionUuid = transactionUuid;
	}

	@Id
	@Column(name = "TRANSACTION_UUID", unique = true, nullable = false, length = 32)
	public String getTransactionUuid() {
		return transactionUuid;
	}

	public void setTransactionUuid(String transactionUuid) {
		this.transactionUuid = transactionUuid;
	}
	
	
}
