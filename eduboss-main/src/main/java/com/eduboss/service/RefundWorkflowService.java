package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.RefundWorkflow;
import com.eduboss.domainVo.RefundAuditDynamicVo;
import com.eduboss.domainVo.RefundAuditEvidenceVo;
import com.eduboss.domainVo.RefundEvidenceVo;
import com.eduboss.domainVo.RefundWorkflowVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;

public interface RefundWorkflowService {

	/**
	 * 保存结课，退费，电子账户操作工作流
	 * @param refundWorkflow
	 * @return
	 */
	public String saveOrUpdateRefundWorkflow(RefundWorkflowVo refundWorkflowVo, MultipartFile[] refundEvidenceFiles, String servicePath) ;
	
	/**
	 * 获取结课，退费，电子账户操作工作流列表
	 * @param refundWorkflowVo
	 * @param dp
	 * @return
	 */
	public DataPackage getRefundWorkflowList(RefundWorkflowVo refundWorkflowVo, DataPackage dp);
	
	/**
	 * 检查是否有在审批中的审批工作流
	 * @param userId
	 * @return
	 */
	public boolean checkHasAuditingRefundWorkflow(String userId);
	
	/**
	 * 通过ID查询结课，退费，电子账户操作工作流
	 * @param fundWorkflowId
	 * @return
	 */
	public RefundWorkflowVo findRefundWorkflowById(String fundWorkflowId);
	
	/**
	 * 删除结课，退费，电子账户操作工作流
	 * @param fundWorkflowId
	 * @return
	 */
	public void deleteRefundWorkflowById(String fundWorkflowId);
	
	/**
	 * 申请结课，退费，电子账户申请
	 * @param refundAuditDynamicVo
	 */
	public String applyRefundWorkFlow(RefundAuditDynamicVo refundAuditDynamicVo, boolean isAuto) throws ApplicationException;
	
	/**
	 * 改变当前审批用户
	 * @param refundWorkflowId
	 * @param auditUserId
	 * @throws ApplicationException
	 */
	public void changeAuditUser(String refundWorkflowId, String auditUserId) throws ApplicationException;
	
	/**
	 * 审批操作
	 * @param refundAuditDynamicVo
	 */
	public void doActionRefundWorkFlow(RefundAuditDynamicVo refundAuditDynamicVo);
	
	/**
	 * 财务出款，结束工作流
	 * @param auditEvidenceFiles
	 * @param refundAuditDynamicVo
	 */
	public String financialTakeOut(MultipartFile[] auditEvidenceFiles, RefundAuditDynamicVo refundAuditDynamicVo) throws ApplicationException;
	
	/**
	 * 根据工作流ID找到工作留的动态信息
	 * @param flowId
	 */
	public Map<String ,Object> getDynamicVoByFlowId(String fundWorkflowId);
	
	/**
	 * 根据工作流ID解冻
	 * @param flowId
	 */
	public void unlockStudentAccount(String workFlowId);
	
	/**
	 * 根据flowId查询工作流
	 * @param flowId
	 * @return
	 */
	public RefundWorkflow findRefundWorkflowByFlowId(long flowId);
	
	/**
	 * 解冻学生电子账户的这部分冻结金额
	 * @param refundWorkflow
	 */
	public void unfreezeStudnetAccMv(RefundWorkflow refundWorkflow);
	
	/**
	 * 完成电子账户转账工作流，资金流转
	 * @param refundWorkflow
	 */
	public void completeRefundWorkflow(RefundWorkflow refundWorkflow) throws Exception;
	
	/**
	 * 通过退款、电子账户转账流程ID获取退款凭证
	 * @param fundWorkflowId
	 * @return
	 */
	public List<RefundEvidenceVo> getEvidenceByFlowId(String fundWorkflowId);
	
	/**
	 * 通过审批动态ID获取财务出款凭证
	 * @param fundWorkflowId
	 * @return
	 */
	public List<RefundAuditEvidenceVo> getEvidenceByDynamicId(String refundAuditDynamicId);
	
	/**
	 * 删除退款凭证
	 * @param evidenceId
	 */
	public void deleteEvidenceById(String evidenceId);
	
	/**
	 * 查找当前步骤的用户
	 * @param refundWorkflowId
	 * @return
	 */
	public List<Map<String, String>> getCurrentStepUsers(String refundWorkflowId);
	
	/**
	 * 保存图片的旋转角度
	 * @param evidenceId
	 * @param rate
	 */
	public void reventEvidence(String evidenceId, int rate);
	
	/**
	 * 获取退款凭证
	 * @param evidenceId
	 * @return
	 */
	public RefundEvidenceVo getEvidenceById(@RequestParam String evidenceId);
	
	/**
	 * 检查当前用户有没有权限
	 * @param refundWorkflowId
	 * @return
	 */
	boolean checkAuditAuthority(String refundWorkflowId);

}
