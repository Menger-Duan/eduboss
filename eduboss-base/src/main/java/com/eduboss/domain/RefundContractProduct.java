package com.eduboss.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

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

import com.eduboss.common.ProductType;
import com.eduboss.common.RefundProductType;

/**
 * RefundContractProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REFUND_CONTRACT_PRODUCT")
public class RefundContractProduct implements java.io.Serializable {

    // Fields

    private String id;
    private ContractProduct contractProduct;
    private RefundContract refundContract;
    private BigDecimal quantity;
    private BigDecimal refundAmount;

    // Constructors

    /**
     * default constructor
     */
    public RefundContractProduct() {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CON_PRODUCT_ID")
    public ContractProduct getContractProduct() {
        return this.contractProduct;
    }

    public void setContractProduct(ContractProduct contractProduct) {
        this.contractProduct = contractProduct;
    }


    @Column(name = "QUANTITY")
    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REFUND_CONTRACT_ID")
    public RefundContract getRefundContract() {
        return refundContract;
    }

    public void setRefundContract(RefundContract refundContract) {
        this.refundContract = refundContract;
    }


    @Column(name = "REFUND_AMOUNT", precision = 10)
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}