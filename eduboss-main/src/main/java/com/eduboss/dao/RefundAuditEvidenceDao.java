package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundAuditEvidence;
import com.eduboss.domainVo.RefundAuditEvidenceVo;

/**
 * 
 * @author lixuejun 2016-06-28
 *
 */
@Repository
public interface RefundAuditEvidenceDao extends GenericDAO<RefundAuditEvidence, String> {

	/**
	 * 通过审批动态ID获取财务出款凭证
	 * @param fundWorkflowId
	 * @return
	 */
	public List<RefundAuditEvidenceVo> getEvidenceByDynamicId(String refundAuditDynamicId);
	
}
