package com.eduboss.controller;

import com.eduboss.domainVo.OrganizationAppendDto;
import com.eduboss.domainVo.OrganizationSearchDto;
import com.eduboss.dto.Response;
import com.eduboss.service.OrganizationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/OrganizationController")
public class OrganizationController {

	Logger logger= Logger.getLogger(OrganizationController.class);

	@Autowired
	private OrganizationService organizationService;

	/**
	 * 勾选人事组织架构到BOSS使用
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/saveHrmsOrgToBoss")
	@ResponseBody
	public Response saveHrmsOrgToBoss(@RequestParam String id) {
			return organizationService.saveHrmsOrgToBoss(id);
	}


	/**
	 * 取消人事组织架构到BOSS使用
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/cacelHrmsOrgInBoss")
	@ResponseBody
	public Response cacelHrmsOrgInBoss(@RequestParam String id) {
		return organizationService.cacelHrmsOrgInBoss(id);
	}


	/**
	 * 获取BOSS组织架构树
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/getBossOrganizationList")
	@ResponseBody
	public Response getBossOrganizationList(OrganizationSearchDto dto) {
		return organizationService.getBossOrganizationList(dto);
	}

	/**
	 * 获取人事组织架构详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getOrganizationById")
	@ResponseBody
	public Response getOrganizationById(@RequestParam String id) {
		return organizationService.getOrganizationById(id);
	}

	/**
	 * 获取人事组织架构附加详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getOrganizationAppendInfoById")
	@ResponseBody
	public Response getOrganizationAppendInfoById(@RequestParam String id) {
		return organizationService.getOrganizationAppendInfoById(id);
	}



	/**
	 * 保存boss组织架构信息
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/saveBossOrganizationInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response saveBossOrganizationInfo(@RequestBody OrganizationAppendDto dto) {
		return organizationService.saveBossOrganizationInfo(dto);
	}


	/**
	 * 获取BOSS组织架构树   选择
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/getBossSelectOrganizationList")
	@ResponseBody
	public Response getBossSelectOrganizationList(OrganizationSearchDto dto) {
		return organizationService.getBossSelectOrganizationList(dto);
	}


}
