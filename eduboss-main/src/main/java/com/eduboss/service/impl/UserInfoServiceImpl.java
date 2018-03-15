/**
 * 
 */
package com.eduboss.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.dao.ContractSigningDao;
import com.eduboss.dao.EducationExperienceDao;
import com.eduboss.dao.FamilyMenberDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.PersonnelTransferDao;
import com.eduboss.dao.TrainingExperienceDao;
import com.eduboss.dao.UserDao;
import com.eduboss.dao.UserInfoDao;
import com.eduboss.dao.UserRoleDao;
import com.eduboss.dao.WorkExperienceDao;
import com.eduboss.domain.ContractSigning;
import com.eduboss.domain.EducationExperience;
import com.eduboss.domain.FamilyMenber;
import com.eduboss.domain.Organization;
import com.eduboss.domain.PersonnelTransfer;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.TrainingExperience;
import com.eduboss.domain.User;
import com.eduboss.domain.UserInfo;
import com.eduboss.domain.WorkExperience;
import com.eduboss.domainVo.PersonnelTransferVo;
import com.eduboss.domainVo.TeacherSubjectVo;
import com.eduboss.domainVo.UserInfoExpansionVo;
import com.eduboss.domainVo.UserInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.TeacherSubjectService;
import com.eduboss.service.UserInfoService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

	 
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserInfoDao userInfoDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRoleDao userRoleDao;
	
	@Autowired
	private WorkExperienceDao workExperienceDao;
	
	@Autowired
	private EducationExperienceDao educationExperienceDao;
	
	@Autowired
	private TrainingExperienceDao trainingExperienceDao;
	
	@Autowired
	private FamilyMenberDao familyMenberDao;
	
	@Autowired
	private PersonnelTransferDao personnelTransferDao;
	
	@Autowired
	private ContractSigningDao contractSigningDao;
	
	@Autowired
	private TeacherSubjectService teacherSubjectService;
	
	@Override
	public UserInfoVo getUserInfoById(String id) throws ApplicationException {
		if(StringUtils.isBlank(id)){
			return new UserInfoVo();
		}
		UserInfo userInfo = userInfoDao.findById(id);
		if (null != userInfo) {
			return HibernateUtils.voObjectMapping(userInfo, UserInfoVo.class);
		} else {
			return new UserInfoVo();
		}
		
	}
	
	/**
	 * 获取员工信息（卫星表）
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	@Override
	public DataPackage getUserInfoList(UserInfoVo userInfoVo, DataPackage dp)
			throws ApplicationException { 
		String hql = " from UserInfo u where 1=1 ";
		
		if(StringUtils.isNotEmpty(userInfoVo.getOrganizationId())){
			Organization org=organizationDao.findById(userInfoVo.getOrganizationId());
			if(org != null)
				hql+=" and u.organizationId in (select id from Organization where  orgLevel like '"+org.getOrgLevel()+"%') ";
		}
		
		if(StringUtils.isNotEmpty(userInfoVo.getUserName())){
			hql+=" and u.userName like '%"+userInfoVo.getUserName()+"%'";
		}			
		if(StringUtils.isNotEmpty(userInfoVo.getMobilePhone())){
			hql+=" and u.mobilePhone like '%"+userInfoVo.getMobilePhone()+"%'";
		}		
		if(StringUtils.isNotEmpty(userInfoVo.getWorkTypeId())){
			hql+=" and u.workType = '"+userInfoVo.getWorkTypeId()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getStartHiredate())){//入职时间
			hql+=" and u.hiredate >= '"+userInfoVo.getStartHiredate()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getEndHiredate())){//入职时间
			hql+=" and u.hiredate <= '"+userInfoVo.getEndHiredate()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getStartConversionDate())){//转正日期
			hql+=" and u.conversionDate >= '"+userInfoVo.getStartConversionDate()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getEndConversionDate())){//转正日期
			hql+=" and u.conversionDate <= '"+userInfoVo.getEndConversionDate()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getStartLeaveDate())){//离职日期
			hql+=" and u.leaveDate >= '"+userInfoVo.getStartLeaveDate()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getEndLeaveDate())){//离职日期
			hql+=" and u.leaveDate <= '"+userInfoVo.getEndLeaveDate()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getStartContractTime())){//合同到期时间
			hql+=" and u.contractTime >= '"+userInfoVo.getStartContractTime()+"'";
		}
		if(StringUtils.isNotEmpty(userInfoVo.getEndContractTime())){//合同到期时间
			hql+=" and u.contractTime <= '"+userInfoVo.getEndContractTime()+"'";
		}		
		if(StringUtils.isNotEmpty(userInfoVo.getRoleId())){
			hql+=" and u.userId in (select userId from UserRole where roleId = '"+userInfoVo.getRoleId()+"') ";
		}		
		  //如果不是超级管理员，员工管理只能查看本分公司的员工
//        User usr = userService.getCurrentLoginUser();
//		if(-1 == usr.getRoleCode().indexOf(RoleCode.SUPER_ADMIN.toString())){
//			if(RoleCode.CAMPUS_DIRECTOR.toString().equals(usr.getRoleCode())){//校区主任
//				hql+=" and  u.userId in(select userId from UserRole where roleId in" +
//						" (select roleId from Role where roleCode ='"+ RoleCode.TEATCHER+"' ) " +
//						") ";
//			}
//			Organization brench =userService.getBelongBranch();//分公司
//			hql+=" and organizationId in (select id from Organization where orgLevel like '"+brench.getOrgLevel()+"%' )";
//		}
		
		// 用户权限限制
		User user = userService.getCurrentLoginUser();
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(user.getUserId());
		Map<String, Object> params = Maps.newHashMap();
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            hql+= " and  (";
            params.put("orgLevel", org.getOrgLevel()+"%");
            hql+= " u.organizationId in (select id from Organization where orgLevel like :orgLevel  )";
            for(int i = 1; i < userOrganizations.size(); i++){
            	params.put("orgLevel"+i, userOrganizations.get(i).getOrgLevel()+"%");
            	hql+= " or u.organizationId in (select id from Organization where orgLevel like :orgLevel"+i+"  )";
            }
            hql+=" )";
        }
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by "+dp.getSidx()+" "+dp.getSord();
		} 
		
		dp=userInfoDao.findPageByHQL(hql, dp,true,params);
		
		
		List<UserInfoVo> userInfoList = HibernateUtils.voListMapping((List<UserInfo>) dp.getDatas(), UserInfoVo.class);
		int i = 0;
        for (UserInfoVo vo : userInfoList) {
        	i = i+1;
        	vo.setRole(userRoleDao.findRoleByUserId(vo.getUserId()));
        	String sql=new String();
    		sql+="select * from user_organization_role uo LEFT JOIN organization o on o.id=uo.organization_Id where 1=1 ";
    		sql+="and uo.user_Id = '" + vo.getUserId() + "' ";
    		List<Organization> list=organizationDao.findBySql(sql, new HashMap<String, Object>());
        	vo.setOrganization(list);
        }
        dp.setDatas(userInfoList);
		return dp;
	}


	@Override
	public String saveOrUpdateUserInfo(UserInfo userInfo,MultipartFile headPortraitPic, MultipartFile idCardPic, MultipartFile bankCardPic) throws ApplicationException {		
		User user=userDao.findById(userInfo.getUserId());		
		UserInfo userInfoDb = null;		
		if(userInfo.getId()!= null) {
			userInfoDb = userInfoDao.findById(userInfo.getId());
		}		 
		
		User u =  userService.getCurrentLoginUser();
		userInfo.setModifyTime(DateTools.getCurrentDateTime());
		userInfo.setModifyUserId(u.getUserId());
		 
		if(headPortraitPic.getSize()>0){
			String fileName=headPortraitPic.getOriginalFilename().substring(0,headPortraitPic.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=headPortraitPic.getOriginalFilename().replace(fileName, "");
			String aliName = "HEAD_PORTRAIT" + userInfo.getUserId()+puff;//阿里云上面的文件名			
			userInfo.setHeadPortrait(aliName);
			this.saveFileToRemoteServer(headPortraitPic, aliName);
		} else {
			if (userInfoDb != null) {
				userInfo.setHeadPortrait(userInfoDb.getHeadPortrait());
			}
		}
		
		if(idCardPic.getSize()>0){
			String fileName=idCardPic.getOriginalFilename().substring(0,idCardPic.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=idCardPic.getOriginalFilename().replace(fileName, "");
			String aliName = "ID_CARD" + userInfo.getUserId()+puff;//阿里云上面的文件名			
			userInfo.setIdCardPic(aliName);
			this.saveFileToRemoteServer(idCardPic, aliName);
		} else {
			if (userInfoDb != null) {
				userInfo.setIdCardPic(userInfoDb.getIdCardPic());
			}
		}
		
		if(bankCardPic.getSize()>0){
			String fileName=bankCardPic.getOriginalFilename().substring(0,bankCardPic.getOriginalFilename().lastIndexOf("."));//上传的文件名
			String puff=bankCardPic.getOriginalFilename().replace(fileName, "");
			String aliName = "BANK_CARD" + userInfo.getUserId()+puff;//阿里云上面的文件名			
			userInfo.setBankCardPic(aliName);
			this.saveFileToRemoteServer(bankCardPic, aliName);
		} else {
			if (userInfoDb != null) {
				userInfo.setBankCardPic(userInfoDb.getBankCardPic());
			}
		}
		 
		if(userInfoDb != null){
			userInfoDao.save(userInfoDb);		
			userInfoDao.merge(userInfo);
		}else{
			userInfo.setId(null);
			userInfo.setUserName(user.getName());//姓名
			userInfo.setUserSex(user.getSex());//性别
			userInfo.setOrganizationId(user.getOrganizationId());//组织架构
			userInfo.setRoleId(user.getRoleId());//职位
			userInfo.setUserAccount(user.getAccount());//账号
			userInfo.setCreateTime(DateTools.getCurrentDateTime());
			userInfo.setCreateUserId(u.getUserId());		
			userInfoDao.save(userInfo);
		}
		return userInfo.getId();
	}


	@Override
	public void deleteUserInfo(String id) throws ApplicationException { 
	    UserInfo target = userInfoDao.findById(id);
		if(target != null){				 
			userInfoDao.delete(target);
		}		 
	}
	
	
	@Override
	public int findCountByUserId(String userId){ 
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		return userInfoDao.findCountHql("select count(*) from UserInfo u where u.userId= :userId ",params);	
	}

	/**
	 * 把上传的档案存到阿里云
	 * @param myfile1
	 * @param fileName
	 */
	public void saveFileToRemoteServer(MultipartFile myfile1, String fileName)  {
		try {
			AliyunOSSUtils.put(fileName, myfile1.getInputStream(), myfile1.getSize());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
	}
	
	/**
	 * 获取员工信息（卫星表）
	 * @param id
	 * @return
	 * @throws ApplicationException
	 */
	public UserInfoExpansionVo getUserInfoExpansion(String id) throws ApplicationException {
		UserInfoExpansionVo returnVo = new UserInfoExpansionVo();
		List<WorkExperience> workExperiences = workExperienceDao.getWorkExperienceByUserInfoId(id);
		List<EducationExperience> eudExperiences = educationExperienceDao.getEducationExperienceByUserInfoId(id);
		List<TrainingExperience> trainingExperiences =trainingExperienceDao.getTrainingExperienceByUserInfoId(id);
		List<FamilyMenber> familyMenbers = familyMenberDao.getFamilyMenberByUserInfoId(id);
		List<PersonnelTransfer> personnelTransfers = personnelTransferDao.getPersonnelTransferByUserInfoId(id);
		List<PersonnelTransferVo> personnelTransferVos = (List<PersonnelTransferVo>) HibernateUtils.voListMapping(personnelTransfers, PersonnelTransferVo.class);
		List<ContractSigning> contractSignings = contractSigningDao.getContractSigningByUserInfoId(id);
		TeacherSubject teacherSubject = new TeacherSubject();
		UserInfo userInfo = userInfoDao.findById(id);
		teacherSubject.setTeacher(new User(userInfo.getUserId()));
		DataPackage dp = new DataPackage(0, 999);
		dp = teacherSubjectService.getTeacherSubjectList(teacherSubject, dp);
		List<TeacherSubject> teacherSubjects = (List<TeacherSubject>) dp.getDatas();
		List<TeacherSubjectVo> teacherSubjectVos = HibernateUtils.voListMapping(teacherSubjects, TeacherSubjectVo.class);
		returnVo.setWorkExperiences(workExperiences);	
		returnVo.setEducationExperiences(eudExperiences);
		returnVo.setTrainingExperiences(trainingExperiences);
		returnVo.setFamilyMenbers(familyMenbers);
		returnVo.setPersonnelTransfers(personnelTransferVos);
		returnVo.setContractSignings(contractSignings);
		returnVo.setTeacherSubjectVos(teacherSubjectVos);
		return returnVo;
	}
	
	/**
	 * 删除工作经历
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteWorkExperience(String id) throws ApplicationException {
		WorkExperience target = workExperienceDao.findById(id);
		if(target != null){				 
			workExperienceDao.delete(target);
		}
	}
	
	/**
	 * 新增或更改工作经历，如果有Id信息，则为更改，否则为新增
	 * @param workExperience
	 * @throws ApplicationException
	 */
	public void saveOrUpdateWorkExperience(WorkExperience workExperience) throws ApplicationException {
		WorkExperience wrokExperienceDb = null;
		if (null != workExperience.getId()) {
			wrokExperienceDb = workExperienceDao.findById(workExperience.getId());
		}
		User u =  userService.getCurrentLoginUser();
		workExperience.setModifyTime(DateTools.getCurrentDateTime());
		workExperience.setModifyUserId(u.getUserId());
		if (null != wrokExperienceDb) {
			workExperienceDao.save(wrokExperienceDb);		
			workExperienceDao.merge(workExperience);
		} else {
			workExperience.setId(null);
			workExperience.setCreateTime(DateTools.getCurrentDateTime());
			workExperience.setCreateUserId(u.getUserId());
			workExperienceDao.save(workExperience);	
		}
	}
	
	/**
	 * 删除教育经历
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteEducationExperience(String id) throws ApplicationException {
		EducationExperience target = educationExperienceDao.findById(id);
		if(target != null){				 
			educationExperienceDao.delete(target);
		}
	}
	
	/**
	 * 新增或更改教育经历，如果有Id信息，则为更改，否则为新增
	 * @param educationExperience
	 * @throws ApplicationException
	 */
	public void saveOrUpdateEducationExperience(EducationExperience educationExperience) throws ApplicationException {
		EducationExperience educationExperienceDb = null;
		if (null != educationExperience.getId()) {
			educationExperienceDb = educationExperienceDao.findById(educationExperience.getId());
		}
		User u =  userService.getCurrentLoginUser();
		educationExperience.setModifyTime(DateTools.getCurrentDateTime());
		educationExperience.setModifyUserId(u.getUserId());
		if (null != educationExperienceDb) {
			educationExperienceDao.save(educationExperienceDb);		
			educationExperienceDao.merge(educationExperience);
		} else {
			educationExperience.setId(null);
			educationExperience.setCreateTime(DateTools.getCurrentDateTime());
			educationExperience.setCreateUserId(u.getUserId());
			educationExperienceDao.save(educationExperience);	
		}
	}
	
	/**
	 * 删除培训经历
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteTrainingExperience(String id) throws ApplicationException {
		TrainingExperience target = trainingExperienceDao.findById(id);
		if(target != null){				 
			trainingExperienceDao.delete(target);
		}
	}
	
	/**
	 * 新增或更改培训经历，如果有Id信息，则为更改，否则为新增
	 * @param educationExperience
	 * @throws ApplicationException
	 */
	public void saveOrUpdateTrainingExperience(TrainingExperience trainingExperience) throws ApplicationException {
		TrainingExperience trainingExperienceDb = null;
		if (null != trainingExperience.getId()) {
			trainingExperienceDb = trainingExperienceDao.findById(trainingExperience.getId());
		}
		User u =  userService.getCurrentLoginUser();
		trainingExperience.setModifyTime(DateTools.getCurrentDateTime());
		trainingExperience.setModifyUserId(u.getUserId());
		if (null != trainingExperienceDb) {
			trainingExperienceDao.save(trainingExperienceDb);		
			trainingExperienceDao.merge(trainingExperience);
		} else {
			trainingExperience.setId(null);
			trainingExperience.setCreateTime(DateTools.getCurrentDateTime());
			trainingExperience.setCreateUserId(u.getUserId());
			trainingExperienceDao.save(trainingExperience);	
		}
	}
	
	/**
	 * 删除家庭成员
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteFamilyMenber(String id) throws ApplicationException {
		FamilyMenber target = familyMenberDao.findById(id);
		if(target != null){				 
			familyMenberDao.delete(target);
		}
	}
	
	/**
	 * 新增或更改家庭成员，如果有Id信息，则为更改，否则为新增
	 * @param familyMenber
	 * @throws ApplicationException
	 */
	public void saveOrUpdateFamilyMenber(FamilyMenber familyMenber) throws ApplicationException {
		FamilyMenber familyMenberDb = null;
		if (null != familyMenber.getId()) {
			familyMenberDb = familyMenberDao.findById(familyMenber.getId());
		}
		User u =  userService.getCurrentLoginUser();
		familyMenber.setModifyTime(DateTools.getCurrentDateTime());
		familyMenber.setModifyUserId(u.getUserId());
		if (null != familyMenberDb) {
			familyMenberDao.save(familyMenberDb);		
			familyMenberDao.merge(familyMenber);
		} else {
			familyMenber.setId(null);
			familyMenber.setCreateTime(DateTools.getCurrentDateTime());
			familyMenber.setCreateUserId(u.getUserId());
			familyMenberDao.save(familyMenber);	
		}
	}
	
	/**
	 * 删除人事调动
	 * @param id
	 * @throws ApplicationException
	 */
	public void deletePersonnelTransfer(String id) throws ApplicationException {
		
	};
	
	/**
	 * 新增或更改人事调动，如果有Id信息，则为更改，否则为新增
	 * @param personnelTransferVo
	 * @throws ApplicationException
	 */
	public void saveOrPersonnelTransfer(PersonnelTransferVo personnelTransferVo) throws ApplicationException {
		
	};
	
	/**
	 * 删除合同签订
	 * @param id
	 * @throws ApplicationException
	 */
	public void deleteContractSigning(String id) throws ApplicationException {
		ContractSigning target = contractSigningDao.findById(id);
		if(target != null){				 
			contractSigningDao.delete(target);
		}
	};
	
	/**
	 * 新增或更改合同签订，如果有Id信息，则为更改，否则为新增
	 * @param contractSigning
	 * @throws ApplicationException
	 */
	public void saveOrContractSigning(ContractSigning contractSigning) throws ApplicationException {
		ContractSigning contractSigningDb = null;
		if (null != contractSigning.getId()) {
			contractSigningDb = contractSigningDao.findById(contractSigning.getId());
		}
		User u =  userService.getCurrentLoginUser();
		contractSigning.setModifyTime(DateTools.getCurrentDateTime());
		contractSigning.setModifyUserId(u.getUserId());
		if (null != contractSigningDb) {
			contractSigningDao.save(contractSigningDb);		
			contractSigningDao.merge(contractSigning);
		} else {
			contractSigning.setId(null);
			contractSigning.setCreateTime(DateTools.getCurrentDateTime());
			contractSigning.setCreateUserId(u.getUserId());
			contractSigningDao.save(contractSigning);	
		}
	};
	
}
