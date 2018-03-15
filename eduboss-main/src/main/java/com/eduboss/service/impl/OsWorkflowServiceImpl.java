package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eduboss.service.OsWorkflowService;
import com.eduboss.utils.ApplicationContextUtil;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.spi.Step;

@Service("com.eduboss.service.OsWorkflowService")
public class OsWorkflowServiceImpl implements OsWorkflowService {

	/**
	 * 获取工作流当前step的actions和可操作form元素
	 */
	@Override
	public Map getActionsAndFromElements(long osWorkflowId) {
		Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
		String workflowName = workflow.getWorkflowName(osWorkflowId);
		List<Step> list = workflow.getCurrentSteps(osWorkflowId);
		if(list.size()>0){
			StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflowName).getStep(list.get(0).getStepId());
			Map map=stepDescriptor.getMetaAttributes();
			map.put("step", String.valueOf(list.get(0).getStepId() - 1));
			return map;
		}
		return null;
	}

	
	/**
	 * 获取工作流当前step
	 */
	@Override
	public int getCurrentSteps(long osWorkflowId) {
		Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
		List<Step> list = workflow.getCurrentSteps(osWorkflowId);
		if(list.size()>0)
			return list.get(0).getStepId();
		else 
			return 1;
	}
}
