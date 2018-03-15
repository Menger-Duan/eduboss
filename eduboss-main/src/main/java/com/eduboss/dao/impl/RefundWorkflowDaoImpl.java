package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefundWorkflowDao;
import com.eduboss.domain.RefundWorkflow;
import com.eduboss.domainVo.RefundWorkflowVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;

/**
 * 
 * @author lixuejun 2016-06-14
 *
 */
@Repository("RefundWorkflowDao")
public class RefundWorkflowDaoImpl extends GenericDaoImpl<RefundWorkflow, String> implements RefundWorkflowDao {
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;

	/**
	 * 获取结课，退费，电子账户操作工作流列表
	 */
	public DataPackage getRefundWorkflowList(RefundWorkflowVo refundWorkflowVo, DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from RefundWorkflow refWf ";
		String hqlWhere=" where 1=1 ";
		if (StringUtils.isNotBlank(refundWorkflowVo.getRefundTitle())) {
			hqlWhere+=" and refundTitle like :refundTitle ";
			params.put("refundTitle", "%" + refundWorkflowVo.getRefundTitle() + "%");
		}
		if(StringUtils.isNotBlank(refundWorkflowVo.getStartDate())){
			hqlWhere+=" and initiateTime>= :startDate ";
			params.put("startDate", refundWorkflowVo.getStartDate() + " 00:00:00");
		}
		if(StringUtils.isNotBlank(refundWorkflowVo.getEndDate())){
			hqlWhere+=" and initiateTime <= :endDate ";
			params.put("endDate", refundWorkflowVo.getEndDate() + " 23:59:59");
		}
		
		if(StringUtils.isNotBlank(refundWorkflowVo.getApplicantId())){
			hqlWhere+=" and applicant.userId = :applicantId ";
			params.put("applicantId", refundWorkflowVo.getApplicantId());
		}
		
		if(StringUtils.isNotBlank(refundWorkflowVo.getRefundCampusId())){
			hqlWhere+=" and refundCampus.id = :refundCampusId ";
			params.put("refundCampusId", refundWorkflowVo.getRefundCampusId());
		}
		
		if(StringUtils.isNotBlank(refundWorkflowVo.getAuditUserId())) {
			hqlWhere+=" and auditUser.userId = :auditUserId ";
			params.put("auditUserId", refundWorkflowVo.getAuditUserId());
			hqlWhere+="and auditStatus = 'AUDITING' ";
		}
		
		if (StringUtils.isNotBlank(refundWorkflowVo.getHistoryUserId())) {
			hqlWhere+=" and id in (select refundWorkflow.id from RefundAuditDynamic where operator.userId = :historyUserId)";
			hqlWhere+=" and (auditUser.userId <> :historyUserId2 "
					+ " or (auditUser.userId = :historyUserId3 and auditStatus <> 'AUDITING'))";
			params.put("historyUserId", refundWorkflowVo.getHistoryUserId());
			params.put("historyUserId2", refundWorkflowVo.getHistoryUserId());
			params.put("historyUserId3", refundWorkflowVo.getHistoryUserId());
			
		}
		
		if (StringUtils.isNotBlank(refundWorkflowVo.getIsManager()) && refundWorkflowVo.getIsManager().equals("true")) {
			hqlWhere+=roleQLConfigService.getAppendSqlByAllOrg("审批列表管理","hql","applicantCampus");
		}
		
		if(refundWorkflowVo.getFormType() != null) {
			hqlWhere+=" and formType = :formType ";
			params.put("formType", refundWorkflowVo.getFormType());
		}
		
		if(refundWorkflowVo.getAuditStatus() != null) {
			hqlWhere+=" and auditStatus = :auditStatus ";
			params.put("auditStatus", refundWorkflowVo.getAuditStatus());
		}
		hqlWhere += " order by initiateTime desc";
		hql += hqlWhere;
		return this.findPageByHQL(hql, dp, true, params);
	}
	
	/**
	 * 根据flowId查询工作流
	 */
	@Override
	public RefundWorkflow findRefundWorkflowByflowId(long flowId) {
		List<Criterion> criteriaList = new ArrayList<Criterion>();
		criteriaList.add(Expression.eq("flowId", flowId));
		List<RefundWorkflow> list = this.findAllByCriteria(criteriaList);
		return list.get(0);
	}
	
}
