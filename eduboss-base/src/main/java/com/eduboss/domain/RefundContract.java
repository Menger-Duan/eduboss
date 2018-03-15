package com.eduboss.domain;

import com.eduboss.common.ContractStatus;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RefundContract entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "REFUND_CONTRACT")
public class RefundContract implements java.io.Serializable {

    // Fields

    private String id;
    private Contract contract;
    private BigDecimal refundAmount;
    private User userByCreateUserId;
    private User userByModifyUserId;
    private ContractStatus status;
    private String createTime;
    private String modifyTime;
    private String remark;
    private Set<RefundContractProduct> refundContractProducts = new HashSet<RefundContractProduct>(0);
    private List<WorkFlowRefundContract> workFlowRefundContracts = new ArrayList<WorkFlowRefundContract>(0);
    private WorkFlowConfig workFlowConfig; // 当前节点

    // Constructors

    /**
     * default constructor
     */
    public RefundContract() {
    }


    public RefundContract(String refundContractId) {
        this.id = refundContractId;
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
    @JoinColumn(name = "CONTRACT_ID")
    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATE_USER_ID")
    public User getUserByCreateUserId() {
        return this.userByCreateUserId;
    }

    public void setUserByCreateUserId(User userByCreateUserId) {
        this.userByCreateUserId = userByCreateUserId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODIFY_USER_ID")
    public User getUserByModifyUserId() {
        return this.userByModifyUserId;
    }

    public void setUserByModifyUserId(User userByModifyUserId) {
        this.userByModifyUserId = userByModifyUserId;
    }


    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 32)
    public ContractStatus getStatus() {
        return status;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    @Column(name = "CREATE_TIME", length = 32)
    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name = "MODIFY_TIME", length = 32)
    public String getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "REMARK", length = 1000)
    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "refundContract")
    public Set<RefundContractProduct> getRefundContractProducts() {
        return refundContractProducts;
    }

    public void setRefundContractProducts(
            Set<RefundContractProduct> refundContractProducts) {
        this.refundContractProducts = refundContractProducts;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "refundContract")
    public List<WorkFlowRefundContract> getWorkFlowRefundContracts() {
        return workFlowRefundContracts;
    }

    public void setWorkFlowRefundContracts(List<WorkFlowRefundContract> workFlowRefundContracts) {
        this.workFlowRefundContracts = workFlowRefundContracts;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORK_FLOW_CONFIG_ID")
    public WorkFlowConfig getWorkFlowConfig() {
        return workFlowConfig;
    }

    public void setWorkFlowConfig(WorkFlowConfig workFlowConfig) {
        this.workFlowConfig = workFlowConfig;
    }

    @Column(name = "REFUND_AMOUNT")
    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }
}