package com.eduboss.controller;

import com.eduboss.common.Constants;
import com.eduboss.dto.MessagePushVo;
import com.eduboss.dto.Response;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserService;
import com.eduboss.utils.BaseAuthUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.dto.Request;
import com.pad.common.CmsContentStatus;
import com.pad.dto.CmsContentVo;
import com.pad.service.CmsContentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/MessageReceiveController")
public class MessageReceiveController {



	Logger logger= Logger.getLogger(MessageReceiveController.class);

	@Autowired
	private CmsContentService cmsContentService;

	@Autowired
	private UserService userService;


	@Autowired
	private OrganizationService organizationService;

	@RequestMapping(value = "/pushOrganizationInfo")
	@ResponseBody
	public void pushOrganizationInfo(@RequestBody MessagePushVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("人事系统同步单个组织架构"+vo.getData());
		if(BaseAuthUtil.checkAuthorizationOA(request,response)){
			organizationService.updateOrgnizationByHrms(vo.getData());
		}
	}

	/**
	 * 推送所有的组织架构改动
	 * @param vo
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/pushAllOrganizationInfo")
	@ResponseBody
	public void pushAllOrganizationInfo(@RequestBody MessagePushVo vo, HttpServletRequest request, HttpServletResponse response) {
		logger.info("人事系统同步单个组织架构"+vo.getData());
		if(BaseAuthUtil.checkAuthorizationOA(request,response)){
			organizationService.pushAllOrganizationInfo(vo);
		}
	}

	@RequestMapping(value = "/publishCmsContent")
	@ResponseBody
	public void publishCmsContent(@RequestBody MessagePushVo vo, HttpServletRequest request) {
		logger.info("预约发布内容"+vo.getData());
		cmsContentService.publishContent(vo.getData());
	}

	@RequestMapping(value = "/pushUserInfo",method = RequestMethod.POST)
	@ResponseBody
	public void pushUserInfo(@RequestBody MessagePushVo vo, HttpServletRequest request, HttpServletResponse response){
		logger.info("人事系统同步单个用户"+vo.getData());
		if(BaseAuthUtil.checkAuthorizationOA(request,response)){
			Response res = userService.pushUserInfo(vo);
		}
	}

}
