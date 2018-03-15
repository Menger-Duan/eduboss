package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefundAuditDynamicDao;
import com.eduboss.domain.RefundAuditDynamic;

/**
 * 
 * @author lixuejun 2016-06-14
 *
 */
@Repository("RefundAuditDynamicDao")
public class RefundAuditDynamicDaoImpl extends GenericDaoImpl<RefundAuditDynamic, String> implements RefundAuditDynamicDao {

	@Override
	public List<RefundAuditDynamic> findListByFlowId(String fundWorkflowId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql=new StringBuffer();
		hql.append(" from RefundAuditDynamic where refundWorkflow.id = :fundWorkflowId order by operationTime asc");
		params.put("fundWorkflowId", fundWorkflowId);
		return this.findAllByHQL(hql.toString(), params);
	}

	
}
