package com.eduboss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eduboss.utils.AliyunOSSUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.ContractSigning;
import com.eduboss.domain.EducationExperience;
import com.eduboss.domain.FamilyMenber;
import com.eduboss.domain.TrainingExperience;
import com.eduboss.domain.UserInfo;
import com.eduboss.domain.WorkExperience;
import com.eduboss.domainVo.PersonnelTransferVo;
import com.eduboss.domainVo.UserInfoExpansionVo;
import com.eduboss.domainVo.UserInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.UserInfoService;
import com.eduboss.utils.HibernateUtils;

@Controller
@RequestMapping(value="/UserInfoController")
public class UserInfoController {

	private final static Logger log = Logger.getLogger(UserInfoController.class);
	@Autowired
	UserInfoService userInfoService;
	
	@RequestMapping(value = "/getUserInfoList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getUserInfoList(HttpServletRequest request, @ModelAttribute UserInfoVo userInfoVo, @ModelAttribute GridRequest gridRequest) throws Exception {
		log.info("getUserInfoList() start.");		
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = userInfoService.getUserInfoList(userInfoVo, dataPackage);
		
		log.info("getUserInfoList() end.");
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@RequestMapping(value = "/editUserInfo")
	@ResponseBody
	public Response editUserInfo(@ModelAttribute GridRequest gridRequest, @ModelAttribute UserInfoVo userInfoVo, 
			@RequestParam(required=false) MultipartFile headPortraitPicFile, @RequestParam(required=false) MultipartFile idCardPicFile, 
				@RequestParam(required=false)MultipartFile bankCardPicFile) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deleteUserInfo(userInfoVo.getId());
		} else if ("edit".equalsIgnoreCase(gridRequest.getOper())) {
			UserInfo userInfo = HibernateUtils.voObjectMapping(userInfoVo, UserInfo.class);
			userInfoService.saveOrUpdateUserInfo(userInfo, headPortraitPicFile, idCardPicFile, bankCardPicFile); 
			response.setResultMessage("员工信息已保存！");			
		} else  {	
			//一个员工只能有一份档案,如果是新增时，先判断是否已经存在该员工档案
			if(StringUtils.isBlank(userInfoVo.getId())){
				int count = userInfoService.findCountByUserId(userInfoVo.getUserId());
				if(count>0){
					response.setResultCode(1);
					response.setResultMessage("已存在该员工信息！");
					return response;
				}
			}	
			UserInfo userInfo = HibernateUtils.voObjectMapping(userInfoVo, UserInfo.class);
			String userInfoId = userInfoService.saveOrUpdateUserInfo(userInfo, headPortraitPicFile, idCardPicFile, bankCardPicFile); 
			response.setResultCode(0);
			response.setResultMessage(userInfoId);			
		}
		return response;
	}
	
	
	@RequestMapping(value = "/findUserInfoById", method =  RequestMethod.GET)
	@ResponseBody
	public UserInfoVo findUserInfoById(@RequestParam(required=false) String id) throws Exception {
		return userInfoService.getUserInfoById(id);
	}
	
	/**
	 * 获取员工信息（卫星表）
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	@RequestMapping(value = "/getUserInfoExpansion", method =  RequestMethod.GET)
	@ResponseBody
	public UserInfoExpansionVo getUserInfoExpansion(String id)  throws Exception {
		return userInfoService.getUserInfoExpansion(id);
	}
	
	/**
	 * 新增，修改，删除工作经历
	 * @param gridRequest
	 * @param workExperience
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editWorkExperience")
	@ResponseBody
	public Response editWorkExperience(@ModelAttribute GridRequest gridRequest, @ModelAttribute WorkExperience workExperience) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deleteWorkExperience(workExperience.getId());
		} else  {
			userInfoService.saveOrUpdateWorkExperience(workExperience);
			response.setResultMessage("工作经历已保存！");
		}
		return response;
	}
	
	/**
	 * 新增，修改，删除教育经历
	 * @param gridRequest
	 * @param educationExperience
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editEducationExperience")
	@ResponseBody
	public Response editEducationExperience(@ModelAttribute GridRequest gridRequest, @ModelAttribute EducationExperience educationExperience) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deleteEducationExperience(educationExperience.getId());
		} else  {
			userInfoService.saveOrUpdateEducationExperience(educationExperience);
			response.setResultMessage("教育经历已保存！");
		}
		return response;
	}
	
	/**
	 * 新增，修改，删除教育经历
	 * @param gridRequest
	 * @param trainingExperience
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editTrainingExperience")
	@ResponseBody
	public Response editTrainingExperience(@ModelAttribute GridRequest gridRequest, @ModelAttribute TrainingExperience trainingExperience) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deleteTrainingExperience(trainingExperience.getId());
		} else  {
			userInfoService.saveOrUpdateTrainingExperience(trainingExperience);
			response.setResultMessage("培训经历已保存！");
		}
		return response;
	}
	
	/**
	 * 新增，修改，删除家庭成员
	 * @param gridRequest
	 * @param familyMenber
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editFamilyMenber")
	@ResponseBody
	public Response editFamilyMenber(@ModelAttribute GridRequest gridRequest, @ModelAttribute FamilyMenber familyMenber) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deleteFamilyMenber(familyMenber.getId());
		} else  {
			userInfoService.saveOrUpdateFamilyMenber(familyMenber);
			response.setResultMessage("家庭成员已保存！");
		}
		return response;
	}
	
	/**
	 * 新增，修改，删除人事调动
	 * @param gridRequest
	 * @param personnelTransferVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editPersonnelTransfer")
	@ResponseBody
	public Response editPersonnelTransfer(@ModelAttribute GridRequest gridRequest, @ModelAttribute PersonnelTransferVo personnelTransferVo) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deletePersonnelTransfer(personnelTransferVo.getId());
		} else  {
			userInfoService.saveOrPersonnelTransfer(personnelTransferVo);
			response.setResultMessage("人事调动已保存！");
		}
		return response;
	}
	
	/**
	 * 新增，修改，删除合同签订
	 * @param gridRequest
	 * @param contractSigning
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/editContractSigning")
	@ResponseBody
	public Response editContractSigning(@ModelAttribute GridRequest gridRequest, @ModelAttribute ContractSigning contractSigning) throws Exception {
		Response response=new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			userInfoService.deleteContractSigning(contractSigning.getId());
		} else  {
			userInfoService.saveOrContractSigning(contractSigning);
			response.setResultMessage("人事调动已保存！");
		}
		return response;
	}


	
}
