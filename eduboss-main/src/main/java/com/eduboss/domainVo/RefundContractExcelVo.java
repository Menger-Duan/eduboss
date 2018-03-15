package com.eduboss.domainVo;

import java.math.BigDecimal;

import com.eduboss.domain.WorkFlowConfig;


/**
 * RefundContract entity. @author MyEclipse Persistence Tools
 */

public class RefundContractExcelVo implements java.io.Serializable {

    // Fields

    private String id;
    private BigDecimal totalAmount;
    private BigDecimal refundAmount;
    private String studentName;
    private String studyManagerName;
    private String createUserName;
    private String createTime;
    private String nood;
    private WorkFlowConfig workFlowConfig; // 当前节点
    
    
    public String getNood() {
        if(workFlowConfig==null){
            return "无流程记录";
        }else{
    		return workFlowConfig.getAction();
        }
	}
	public void setNood(String nood) {
		this.nood = nood;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public WorkFlowConfig getWorkFlowConfig() {
		return workFlowConfig;
	}
	public void setWorkFlowConfig(WorkFlowConfig workFlowConfig) {
		this.workFlowConfig = workFlowConfig;
	}
    

}