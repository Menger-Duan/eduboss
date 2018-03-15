package com.eduboss.service;

import java.util.Map;

import com.opensymphony.workflow.spi.Step;

public interface OsWorkflowService {

	/**
	 * 获取工作流当前step的actions和可操作form元素
	 * @param osWorkflowId
	 * @return
	 */
	public Map getActionsAndFromElements(long osWorkflowId);
	
	/**
	 * 获取工作流当前step
	 * @param osWorkflowId
	 * @return
	 */
	public int getCurrentSteps(long osWorkflowId);
}
