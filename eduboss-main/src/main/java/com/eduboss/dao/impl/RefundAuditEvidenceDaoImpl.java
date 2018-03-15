package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefundAuditEvidenceDao;
import com.eduboss.domain.RefundAuditEvidence;
import com.eduboss.domainVo.RefundAuditEvidenceVo;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun 2016-06-28
 *
 */
@Repository("RefundAuditEvidenceDao")
public class RefundAuditEvidenceDaoImpl  extends GenericDaoImpl<RefundAuditEvidence, String> implements RefundAuditEvidenceDao {

	/**
	 * 通过审批动态ID获取财务出款凭证
	 */
	public List<RefundAuditEvidenceVo> getEvidenceByDynamicId(String refundAuditDynamicId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql=new StringBuffer();
		hql.append(" from RefundAuditEvidence where refundAuditDynamic.id = :refundAuditDynamicId ");
		params.put("refundAuditDynamicId", refundAuditDynamicId);
		List<RefundAuditEvidence> list = this.findAllByHQL(hql.toString(), params);
		return HibernateUtils.voListMapping(list, RefundAuditEvidenceVo.class);
	}
	
}
