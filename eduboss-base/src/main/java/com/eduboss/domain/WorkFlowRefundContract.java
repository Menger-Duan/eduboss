package com.eduboss.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 退费合同工作流
 *
 * @author Administrator
 */
@Entity
@Table(name = "WORK_FLOW_REFUND_CONTRACT")
public class WorkFlowRefundContract extends WorkFlowrRecordBase {


    private RefundContract refundContract; // 退费合同

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REFUND_CONTRACT_ID")
    public RefundContract getRefundContract() {
        return refundContract;
    }

    public void setRefundContract(RefundContract refundContract) {
        this.refundContract = refundContract;
    }


}
