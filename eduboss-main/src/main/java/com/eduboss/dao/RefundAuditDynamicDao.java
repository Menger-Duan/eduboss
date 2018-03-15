package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundAuditDynamic;

/**
 * 
 * @author lixuejun 2016-06-14
 *
 */
@Repository
public interface RefundAuditDynamicDao extends GenericDAO<RefundAuditDynamic, String> {

	List<RefundAuditDynamic> findListByFlowId(String fundWorkflowId);

}
