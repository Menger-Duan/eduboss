package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.RefundAuditDynamicVo;
import com.eduboss.domainVo.RefundAuditEvidenceVo;
import com.eduboss.domainVo.RefundEvidenceVo;
import com.eduboss.domainVo.RefundWorkflowVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.RefundWorkflowService;

/**
 * 
 * @author lixuejun 2016-06-14
 *
 */
@Controller
@RequestMapping(value = "/RefundWorkflowController")
public class RefundWorkflowController {

	@Autowired
	private RefundWorkflowService refundWorkflowService;
	
	/**
	 * 保存结课，退费，电子账户操作工作流
	 */
	@RequestMapping(value = "/editRefundWorkflow", method =  RequestMethod.POST)
	@ResponseBody
	public String editRefundWorkflow(@RequestParam(required=false) MultipartFile[] refundEvidenceFiles, 
			@ModelAttribute RefundWorkflowVo refundWorkflowVo, HttpServletRequest request) {
	    String servicePath = request.getSession().getServletContext().getRealPath("/");
		if(StringUtils.isNotBlank(refundWorkflowVo.getOperateType()) && "2".equals(refundWorkflowVo.getOperateType())){
			 String refundWorkflowId =  refundWorkflowService.saveOrUpdateRefundWorkflow(refundWorkflowVo, refundEvidenceFiles, servicePath);
			 //处理action
			 RefundAuditDynamicVo vo=new RefundAuditDynamicVo();
			 vo.setActionId(refundWorkflowVo.getActionId());
			 vo.setOperation(refundWorkflowVo.getOperation());
			 vo.setAuditUserId(refundWorkflowVo.getAuditUserId());
			 vo.setRefundWorkflowId(refundWorkflowVo.getId());
			 vo.setAuditRemark(refundWorkflowVo.getAuditRemark());
			 refundWorkflowService.applyRefundWorkFlow(vo, false);
			 return refundWorkflowId;
		}else{
			return refundWorkflowService.saveOrUpdateRefundWorkflow(refundWorkflowVo, refundEvidenceFiles, servicePath);
		}
		
	}
	
	/**
	 * 删除结课，退费，电子账户操作工作流
	 */
	@RequestMapping(value = "/deleteRefundWorkflowById")
	@ResponseBody
	public Response deleteRefundWorkflowById(@RequestParam String refundWorkflowId) {
		Response res=new Response();
		refundWorkflowService.deleteRefundWorkflowById(refundWorkflowId);
		return res;
	}
	
	/**
	 * 
	 */
	/**
	 * 获取结课，退费，电子账户操作工作流列表
	 */
	@RequestMapping(value = "/getRefundWorkflowList")
	@ResponseBody
	public DataPackageForJqGrid getRefundWorkflowList(GridRequest gridRequest, @ModelAttribute RefundWorkflowVo refundWorkflowVo) throws Exception {
		DataPackage dp = new DataPackage(gridRequest);
		dp = refundWorkflowService.getRefundWorkflowList(refundWorkflowVo, dp);
		return new DataPackageForJqGrid(dp);
	}
	
	/**
	 * 通过ID查询结课，退费，电子账户操作工作流
	 */
	@RequestMapping(value = "/findRefundWorkflowById")
	@ResponseBody
	public RefundWorkflowVo findRefundWorkflowById(@RequestParam String refundWorkflowId) {
		return refundWorkflowService.findRefundWorkflowById(refundWorkflowId);
	}
	
	/**
	 * 申请结课，退费，电子账户申请
	 */
	@RequestMapping(value = "/applyRefundWorkFlow", method =  RequestMethod.POST)
	@ResponseBody
	public Response applyRefundWorkFlow(@ModelAttribute RefundAuditDynamicVo refundAuditDynamicVo) {
		Response res=new Response();
		res.setResultMessage(refundWorkflowService.applyRefundWorkFlow(refundAuditDynamicVo, false));
		return res;
	}
	
	/**
	 * 改变当前审批用户
	 */
	@RequestMapping(value = "/changeAuditUser")
	@ResponseBody
	public Response changeAuditUser(@RequestParam String refundWorkflowId, @RequestParam String auditUserId) {
		Response res=new Response();
		refundWorkflowService.changeAuditUser(refundWorkflowId, auditUserId);
		return res;
	}
	
	/**
	 * 审批操作
	 */
	@RequestMapping(value = "/doActionRefundWorkflow", method =  RequestMethod.POST)
	@ResponseBody
	public Response doActionRefundWorkflow(@ModelAttribute RefundAuditDynamicVo refundAuditDynamicVo) {
		refundWorkflowService.doActionRefundWorkFlow(refundAuditDynamicVo);
		return new Response();
	}
	
	/**
	 * 财务出款，撤销结束工作流
	 */
	@RequestMapping(value = "/financialTakeOut", method =  RequestMethod.POST)
	@ResponseBody
	public String financialTakeOut(@RequestParam(required=false) MultipartFile[] auditEvidenceFiles, 
			@ModelAttribute RefundAuditDynamicVo refundAuditDynamicVo) {
		return refundWorkflowService.financialTakeOut(auditEvidenceFiles, refundAuditDynamicVo);
	}

	/**
	 * 获取流程动态
	 */
	@RequestMapping(value = "/getDynamicVoByFlowId")
	@ResponseBody
	public Map<String ,Object> getDynamicVoByFlowId(@RequestParam String refundWorkflowId) {
		return refundWorkflowService.getDynamicVoByFlowId(refundWorkflowId);
	}
	
	/**
	 * 通过退款、电子账户转账流程ID获取退款凭证
	 */
	@RequestMapping(value = "/getEvidenceByFlowId")
	@ResponseBody
	public List<RefundEvidenceVo> getEvidenceByFlowId(@RequestParam String refundWorkflowId) {
		return refundWorkflowService.getEvidenceByFlowId(refundWorkflowId);
	}
	
	/**
	 * 通过审批动态ID获取财务出款凭证
	 */
	@RequestMapping(value = "/getEvidenceByDynamicId")
	@ResponseBody
	public List<RefundAuditEvidenceVo> getEvidenceByDynamicId(@RequestParam String refundAuditDynamicId) {
		return refundWorkflowService.getEvidenceByDynamicId(refundAuditDynamicId);
	}
	
	/**
	 * 删除退款凭证
	 */
	@RequestMapping(value = "/deleteEvidenceById")
	@ResponseBody
	public Response deleteEvidenceById(@RequestParam String evidenceId) {
		 refundWorkflowService.deleteEvidenceById(evidenceId);
		 return new Response();
	}
	
	 /**
	  * 查找当前步骤的用户
	  */
	 @RequestMapping(value = "/getCurrentStepUsers", method =  RequestMethod.GET)
	 @ResponseBody
	 public List<Map<String, String>> getCurrentStepUsers(@RequestParam String refundWorkflowId){
		 return  refundWorkflowService.getCurrentStepUsers(refundWorkflowId);
	 }
	 
	 /**
	  * 保存图片的旋转角度
	  */
	 @RequestMapping(value = "/reventEvidence", method =  RequestMethod.GET)
	 @ResponseBody
	 public Response reventEvidence(@RequestParam String evidenceId, @RequestParam int rate){
		 refundWorkflowService.reventEvidence(evidenceId, rate);
		 return new Response();
	 }
	 
	 /**
	  * 获取退款凭证
	  */
	 @RequestMapping(value = "/getEvidenceById", method =  RequestMethod.GET)
	 @ResponseBody
	 public RefundEvidenceVo getEvidenceById(@RequestParam String evidenceId){
		 return refundWorkflowService.getEvidenceById(evidenceId);
	 }
	 
	 /**
	  * 校验当前用户能否审核
	  * @param refundWorkflowId
	  * @return
	  */
	 @RequestMapping(value = "/checkAuditAuthority", method =  RequestMethod.GET)
     @ResponseBody
     public boolean checkAuditAuthority(@RequestParam String refundWorkflowId){
         return refundWorkflowService.checkAuditAuthority(refundWorkflowId);
     }
	
}
