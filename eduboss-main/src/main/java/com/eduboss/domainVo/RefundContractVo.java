package com.eduboss.domainVo;

import com.eduboss.common.BaseStatus;
import com.eduboss.domain.WorkFlowConfig;
import com.eduboss.domain.WorkFlowRefundContract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * RefundContract entity. @author MyEclipse Persistence Tools
 */

public class RefundContractVo implements java.io.Serializable {

    // Fields

    private String id;
    private String status;
    private BaseStatus approve;
    //	private Student student;
//	private User userByCreateUserId;
//	private User userByModifyUserId;
    private String studentName;
    private String studyManagerName;
    private String createUserName;
    private String createTime;
    private String modifyTime;
    private WorkFlowConfig workFlowConfig; // 当前节点
    private String remark;
    private BigDecimal totalAmount;
    private BigDecimal refundAmount;
    //	private Set<RefundContractWf> refundContractWfs = new HashSet<RefundContractWf>(0);
    private Set<RefundContractProductVo> refundContractProducts = new HashSet<RefundContractProductVo>(0);
    private List<WorkFlowRefundContract> workFlowRefundContracts = new ArrayList<WorkFlowRefundContract>(0);

    private String stuBlCampusId;//学生归属校区 查询退费合同列表用到
    // Constructors

    /**
     * default constructor
     */
    public RefundContractVo() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Set<RefundContractProductVo> getRefundContractProducts() {
        return refundContractProducts;
    }

    public void setRefundContractProducts(
            Set<RefundContractProductVo> refundContractProducts) {
        this.refundContractProducts = refundContractProducts;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudyManagerName() {
        return studyManagerName;
    }

    public void setStudyManagerName(String studyManagerName) {
        this.studyManagerName = studyManagerName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public WorkFlowConfig getWorkFlowConfig() {
        return workFlowConfig;
    }

    public void setWorkFlowConfig(WorkFlowConfig workFlowConfig) {
        this.workFlowConfig = workFlowConfig;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BaseStatus getApprove() {
        return approve;
    }

    public void setApprove(BaseStatus approve) {
        this.approve = approve;
    }

    public List<WorkFlowRefundContract> getWorkFlowRefundContracts() {
        return workFlowRefundContracts;
    }

    public void setWorkFlowRefundContracts(List<WorkFlowRefundContract> workFlowRefundContracts) {
        this.workFlowRefundContracts = workFlowRefundContracts;
    }

	public String getStuBlCampusId() {
		return stuBlCampusId;
	}

	public void setStuBlCampusId(String stuBlCampusId) {
		this.stuBlCampusId = stuBlCampusId;
	}
}