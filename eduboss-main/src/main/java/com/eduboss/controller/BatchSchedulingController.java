package com.eduboss.controller;

import com.eduboss.dto.Response;
import com.eduboss.service.SchedulerCountService;
import com.eduboss.service.impl.SchedulerCountServiceImpl;
import com.eduboss.utils.BaseAuthUtil;
import com.eduboss.utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Controller
@RequestMapping(value = "/BatchSchedulingController")
public class BatchSchedulingController {

	@Autowired
	SchedulerCountService schedulerCountService;

	@RequestMapping(value = "/callBatchScheduling")
	@ResponseBody
	public Response callBatchScheduling(@RequestParam String procedureName,HttpServletRequest request,HttpServletResponse response) {
		Response res=new Response();
		if(BaseAuthUtil.checkAuthorization(request, response, PropertiesUtils.getStringValue("BATCH_USER"), PropertiesUtils.getStringValue("BATCH_PWD"))) {
			Method mh = ReflectionUtils.findMethod(schedulerCountService.getClass(), procedureName);
			if(mh!=null) {
				 ReflectionUtils.invokeMethod(mh, schedulerCountService);
			}else{
				res.setResultCode(1);
				res.setResultMessage("没有此方法");
			}
		}else{
			res.setResultMessage("鉴权失败");
			res.setResultCode(1);
		}

		response.reset();
		return res;
	}

}
