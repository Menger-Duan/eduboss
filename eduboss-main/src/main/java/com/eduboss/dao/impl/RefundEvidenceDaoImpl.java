package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefundEvidenceDao;
import com.eduboss.domain.RefundEvidence;
import com.eduboss.domainVo.RefundEvidenceVo;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun 2016-06-15
 *
 */
@Repository("RefundEvidenceDao")
public class RefundEvidenceDaoImpl  extends GenericDaoImpl<RefundEvidence, String> implements RefundEvidenceDao {

	/**
	 * 通过退款、电子账户转账流程ID获取退款凭证
	 */
	public List<RefundEvidenceVo> getEvidenceByFlowId(String fundWorkflowId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql=new StringBuffer();
		hql.append(" from RefundEvidence where refundWorkflow.id = :fundWorkflowId ");
		params.put("fundWorkflowId", fundWorkflowId);
		List<RefundEvidence> list = this.findAllByHQL(hql.toString(), params);
		return HibernateUtils.voListMapping(list, RefundEvidenceVo.class);
	}
	
}
