package com.eduboss.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.service.OsWorkflowService;
import com.opensymphony.workflow.spi.Step;
 
/**
 * 
 * @author lixuejun 2016-06-21
 *
 */
@Controller
@RequestMapping(value = "/OsWorkflowController")
public class OsWorkflowController {

	@Autowired
	private OsWorkflowService osWorkflowService;
	
	/**
	 * 获取工作流当前step的actions和可操作form元素
	 */
	@RequestMapping(value = "/getActionsAndFromElements")
	@ResponseBody
	public Map getActionsAndFromElements(@RequestParam long osWorkflowId) {
		return osWorkflowService.getActionsAndFromElements(osWorkflowId);
	}
	
	/**
	 * 获取工作流当前step
	 */
	@RequestMapping(value = "/getCurrentSteps")
	@ResponseBody
	public int getCurrentSteps(@RequestParam long osWorkflowId) {
		return osWorkflowService.getCurrentSteps(osWorkflowId);
	}

}
