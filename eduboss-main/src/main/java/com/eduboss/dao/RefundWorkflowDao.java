package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.RefundWorkflow;
import com.eduboss.domainVo.RefundWorkflowVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun 2016-06-14
 *
 */
@Repository
public interface RefundWorkflowDao extends GenericDAO<RefundWorkflow, String> {

	/**
	 * 获取结课，退费，电子账户操作工作流列表
	 */
	public DataPackage getRefundWorkflowList(RefundWorkflowVo refundWorkflowVo, DataPackage dp);
	
	/**
	 * 根据flowId查询工作流
	 * @param flowId
	 * @return
	 */
	public RefundWorkflow findRefundWorkflowByflowId(long flowId);
	
}
