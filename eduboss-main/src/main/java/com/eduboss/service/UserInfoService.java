package com.eduboss.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.ContractSigning;
import com.eduboss.domain.EducationExperience;
import com.eduboss.domain.FamilyMenber;
import com.eduboss.domain.PersonnelTransfer;
import com.eduboss.domain.TrainingExperience;
import com.eduboss.domain.UserInfo;
import com.eduboss.domain.WorkExperience;
import com.eduboss.domainVo.PersonnelTransferVo;
import com.eduboss.domainVo.UserInfoExpansionVo;
import com.eduboss.domainVo.UserInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;

@Service
public interface UserInfoService {
	
	
	
	 
	/**
	 * 根据用户ID返回用户详情
	 */
	public UserInfoVo getUserInfoById(String id) throws ApplicationException;
	
		
	/**
	 * 根据条件返回员工列表
	 * 需要根据当前用户附加权限条件
	 */
	@Deprecated
	public DataPackage getUserInfoList(UserInfoVo userInfoVo, DataPackage dp) throws ApplicationException;
	
	/**
	 * 新增或更改用户，如果有Id信息，则为更改，否则为新增
	 * 需要根据当前用户附加权限条件，需要先检查是否有修改或新增用户的权限
	 */
	public String saveOrUpdateUserInfo(UserInfo userInfo,MultipartFile headPortraitPic, MultipartFile idCardPic, MultipartFile bankCardPic) throws ApplicationException;
	
	/**
	 * 新增或更改用户，如果有Id信息，则为更改，否则为新增
	 * 需要根据当前用户附加权限条件，需要查验是否有删除员工的权限
	 */
	public void deleteUserInfo(String id) throws ApplicationException;
	
	
	/**
	 * 新增员工信息时需要判断是否存在该员工档案*/
	public int findCountByUserId(String userId);
	
	/**
	 * 获取员工信息（卫星表）
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	public UserInfoExpansionVo getUserInfoExpansion(String id) throws ApplicationException;
	
	/**
	 * 删除工作经历
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteWorkExperience(String id) throws ApplicationException;
	
	/**
	 * 新增或更改工作经历，如果有Id信息，则为更改，否则为新增
	 * @param workExperience
	 * @throws ApplicationException
	 */
	public void saveOrUpdateWorkExperience(WorkExperience workExperience) throws ApplicationException;
	
	/**
	 * 删除教育经历
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteEducationExperience(String id) throws ApplicationException;
	
	/**
	 * 新增或更改教育经历，如果有Id信息，则为更改，否则为新增
	 * @param educationExperience
	 * @throws ApplicationException
	 */
	public void saveOrUpdateEducationExperience(EducationExperience educationExperience) throws ApplicationException;
	
	/**
	 * 删除培训经历
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteTrainingExperience(String id) throws ApplicationException;
	
	/**
	 * 新增或更改培训经历，如果有Id信息，则为更改，否则为新增
	 * @param trainingExperience
	 * @throws ApplicationException
	 */
	public void saveOrUpdateTrainingExperience(TrainingExperience trainingExperience) throws ApplicationException;
	
	/**
	 * 删除家庭成员
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteFamilyMenber(String id) throws ApplicationException;
	
	/**
	 * 新增或更改家庭成员，如果有Id信息，则为更改，否则为新增
	 * @param familyMenber
	 * @throws ApplicationException
	 */
	public void saveOrUpdateFamilyMenber(FamilyMenber familyMenber) throws ApplicationException;
	
	/**
	 * 删除人事调动
	 * @param id
	 * @throws ApplicationException
	 */
	public void deletePersonnelTransfer(String id) throws ApplicationException;
	
	/**
	 * 新增或更改人事调动，如果有Id信息，则为更改，否则为新增
	 * @param familyMenber
	 * @throws ApplicationException
	 */
	public void saveOrPersonnelTransfer(PersonnelTransferVo personnelTransferVo) throws ApplicationException;
	
	/**
	 * 删除合同签订
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteContractSigning(String id) throws ApplicationException;
	
	/**
	 * 新增或更改合同签订，如果有Id信息，则为更改，否则为新增
	 * @param familyMenber
	 * @throws ApplicationException
	 */
	public void saveOrContractSigning(ContractSigning contractSigning) throws ApplicationException;
	
}
