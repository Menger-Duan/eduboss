package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundEvidence;
import com.eduboss.domainVo.RefundEvidenceVo;

/**
 * 
 * @author lixuejun 2016-06-15
 *
 */
@Repository
public interface RefundEvidenceDao extends GenericDAO<RefundEvidence, String> {

	/**
	 * 通过退款、电子账户转账流程ID获取退款凭证
	 * @param fundWorkflowId
	 * @return
	 */
	public List<RefundEvidenceVo> getEvidenceByFlowId(String fundWorkflowId);
	
}
