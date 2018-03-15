package com.eduboss.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.AuditOperation;
import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.MsgNo;
import com.eduboss.common.RefundAuditStatus;
import com.eduboss.common.RefundFormType;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.RefundAuditDynamicDao;
import com.eduboss.dao.RefundAuditEvidenceDao;
import com.eduboss.dao.RefundEvidenceDao;
import com.eduboss.dao.RefundWorkflowDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Organization;
import com.eduboss.domain.RefundAuditDynamic;
import com.eduboss.domain.RefundAuditEvidence;
import com.eduboss.domain.RefundEvidence;
import com.eduboss.domain.RefundWorkflow;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudnetAccMv;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domainVo.IncomeDistributionVo;
import com.eduboss.domainVo.RefundAuditDynamicVo;
import com.eduboss.domainVo.RefundAuditEvidenceVo;
import com.eduboss.domainVo.RefundEvidenceVo;
import com.eduboss.domainVo.RefundIncomeDistributeVo;
import com.eduboss.domainVo.RefundWorkflowVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ContractService;
import com.eduboss.service.RefundWorkflowService;
import com.eduboss.service.StudentService;
import com.eduboss.service.UserService;
import com.eduboss.task.SendRefundSysMsgThread;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;
import com.opensymphony.workflow.InvalidActionException;
import com.opensymphony.workflow.InvalidInputException;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.WorkflowException;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.opensymphony.workflow.loader.StepDescriptor;
import com.opensymphony.workflow.spi.Step;



@Service("com.eduboss.service.RefundWorkflowService")
public class RefundWorkflowServiceImpl implements RefundWorkflowService {

	@Autowired
	private RefundWorkflowDao refundWorkflowDao;
	
	@Autowired
	private StudnetAccMvDao studnetAccMvDao;
	
	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private RefundAuditDynamicDao refundAuditDynamicDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefundEvidenceDao refundEvidenceDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	
	@Autowired
	private RefundAuditEvidenceDao refundAuditEvidenceDao;
	
	@Autowired
	private StudentService studentService;
	
	private static final Logger log = LoggerFactory.getLogger(RefundWorkflowServiceImpl.class);
	
	/**
	 * 保存结课，退费，电子账户操作工作流
	 */
	@Override
	public String saveOrUpdateRefundWorkflow(RefundWorkflowVo refundWorkflowVo, MultipartFile[] refundEvidenceFiles, String servicePath) {
		String logStr = "";
		List<RefundEvidence> refundEvidences = new ArrayList<RefundEvidence>();
		if (refundEvidenceFiles != null && refundEvidenceFiles.length > 0) {
			String fileName = "";
			RefundEvidence evidence = null;
			for (MultipartFile refundEvidenceFile : refundEvidenceFiles) {
			    String oriFilename = refundEvidenceFile.getOriginalFilename();
			    if (StringUtils.isNotBlank(oriFilename) && oriFilename.indexOf(".") < 0) {
                    throw new ApplicationException("上传附件不符合要求,名称需要后缀(如.jpg),不符合文件是：" + oriFilename);
                }
			}
			for (MultipartFile refundEvidenceFile : refundEvidenceFiles) {
				if (refundEvidenceFile.getSize() > 0) {
					evidence = new RefundEvidence();
					String oriFilename = refundEvidenceFile.getOriginalFilename();
					evidence.setFileName(oriFilename); // 原文件名称
					String suffix = oriFilename.substring(oriFilename.indexOf("."));
					fileName = UUID.randomUUID().toString() + suffix;
					evidence.setEvidencePath(fileName); // 保存到阿里云上的文件名称
					refundEvidences.add(evidence);
					String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
					isNewFolder(folder);
					File realFile=new File(folder + "/" + fileName);
					try {
						refundEvidenceFile.transferTo(realFile);
						AliyunOSSUtils.put(fileName, realFile); // 上传图片到阿里云
					} catch (IOException e) {
						e.printStackTrace();
						throw new ApplicationException("上传图片出错，请联系管理员");
					}finally{				
						realFile.delete();
					} 			
				}
			}
		}
		RefundWorkflow refundWorkflowDb = null;
		User user = userService.getCurrentLoginUser();
		refundWorkflowVo = updateRefundWorkflowVo(refundWorkflowVo);
		RefundWorkflow refundWorkflow = HibernateUtils.voObjectMapping(refundWorkflowVo, RefundWorkflow.class);

		if (StringUtils.isBlank(refundWorkflow.getId())) {
			logStr += "新增工作流,";
			refundWorkflow.setCreateUserId(user.getUserId());
			refundWorkflow.setCreateTime(DateTools.getCurrentDateTime());
			refundWorkflow.setId(null);
		} else {
			refundWorkflowDb = refundWorkflowDao.findById(refundWorkflow.getId());
			if (refundWorkflowDb.getAuditStatus() == RefundAuditStatus.AUDITING && refundWorkflowDb.getStepId() > 0) {
				throw new ApplicationException("工作流已是审批中状态，不能再修改");
			}
			logStr += "修改工作流,";
			refundWorkflow.setCreateUserId(refundWorkflowDb.getCreateUserId());
			refundWorkflow.setCreateTime(refundWorkflowDb.getCreateTime());
			refundWorkflow.setInitiateTime(refundWorkflowDb.getInitiateTime());
			refundWorkflow.setStepId(refundWorkflowDb.getStepId());
		}
		String refundTitle = getRefundTitle(refundWorkflow);
		refundWorkflow.setRefundTitle(refundTitle);
		refundWorkflow.setModifyUserId(user.getUserId());
		refundWorkflow.setModifyTime(DateTools.getCurrentDateTime());
		refundWorkflow.setAuditStatus(RefundAuditStatus.TO_SUBMIT);
		UserDeptJob userDeptJob = userDeptJobDao.findDeptJobByParam(refundWorkflow.getApplicant().getUserId(), 0);
		if (userDeptJob != null &&  StringUtils.isNotBlank(userDeptJob.getDeptId())) {
			Organization applicantCampus = organizationDao.findById(userDeptJob.getDeptId());
			refundWorkflow.setApplicantCampus(applicantCampus);
		}
		if (refundWorkflowDb != null) {
//			refundWorkflowDao.save(refundWorkflowDb);
			refundWorkflowDao.merge(refundWorkflow);
		} else {
			refundWorkflowDao.save(refundWorkflow);
		}
		refundWorkflowDao.commit();
		if (refundEvidences.size() > 0) {
			for (RefundEvidence evidence : refundEvidences) {
				String evidenceFileName = evidence.getFileName();
				if (evidenceFileName.length() > 20) {
					throw new ApplicationException("附件名字（最大20个字）过长，请修改后重新上传！");
				}
				evidence.setRefundWorkflow(refundWorkflow);
				evidence.setCreateUserId(user.getUserId());
				evidence.setCreateTime(DateTools.getCurrentDateTime());
				refundEvidenceDao.save(evidence);
			}
		}
		logStr += refundWorkflow.getId();
		log.info(logStr);
		return refundWorkflow.getId();
	}
	
	public void isNewFolder(String folder){
        File f1=new File(folder);
        if(!f1.exists())
        {
            f1.mkdirs();
        }
    }

	/**
	 *当归属目标为空的时候，设置属性为空
	 * @param refundWorkflowVo
	 * @return
	 */
	private RefundWorkflowVo updateRefundWorkflowVo(RefundWorkflowVo refundWorkflowVo) {
		if (StringUtils.isBlank(refundWorkflowVo.getFirstRefundDutyCampusId())){
			refundWorkflowVo.setFirstRefundDutyAmountCampus(null);
			refundWorkflowVo.setFirstSubBonusDistributeTypeCampus(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getSecondRefundDutyCampusId())){
			refundWorkflowVo.setSecondRefundDutyAmountCampus(null);
			refundWorkflowVo.setSecondSubBonusDistributeTypeCampus(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getThirdRefundDutyCampusId())){
			refundWorkflowVo.setThirdRefundDutyAmountCampus(null);
			refundWorkflowVo.setThirdSubBonusDistributeTypeCampus(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getFourthRefundDutyCampusId())){
			refundWorkflowVo.setFourthRefundDutyAmountCampus(null);
			refundWorkflowVo.setFourthSubBonusDistributeTypeCampus(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getFifthRefundDutyCampusId())){
			refundWorkflowVo.setFifthRefundDutyAmountCampus(null);
			refundWorkflowVo.setFifthSubBonusDistributeTypeCampus(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getFirstRefundDutyPersonId())){
			refundWorkflowVo.setFirstRefundDutyAmountPerson(null);
			refundWorkflowVo.setFirstSubBonusDistributeTypePerson(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getSecondRefundDutyPersonId())){
			refundWorkflowVo.setSecondRefundDutyAmountPerson(null);
			refundWorkflowVo.setSecondSubBonusDistributeTypePerson(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getThirdRefundDutyPersonId())){
			refundWorkflowVo.setThirdRefundDutyAmountPerson(null);
			refundWorkflowVo.setThirdSubBonusDistributeTypePerson(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getFourthRefundDutyPersonId())){
			refundWorkflowVo.setFourthRefundDutyAmountPerson(null);
			refundWorkflowVo.setFourthSubBonusDistributeTypePerson(null);
		}
		if (StringUtils.isBlank(refundWorkflowVo.getFifthRefundDutyPersonId())){
			refundWorkflowVo.setFifthRefundDutyAmountPerson(null);
			refundWorkflowVo.setFifthSubBonusDistributeTypePerson(null);
		}
		return refundWorkflowVo;
	}

	/**
	 * 获取结课，退费，电子账户操作工作流列表
	 */
	@Override
	public DataPackage getRefundWorkflowList(RefundWorkflowVo refundWorkflowVo, DataPackage dp) {
		dp = refundWorkflowDao.getRefundWorkflowList(refundWorkflowVo, dp);
		List<RefundWorkflowVo> voList = HibernateUtils.voListMapping((List<RefundWorkflow>)dp.getDatas(), RefundWorkflowVo.class);
		/*for (RefundWorkflowVo vo : voList) {
			int stepId = 0; 
			
			if (vo.getFlowId() > 0) {
				Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
				List<Step> list = workflow.getCurrentSteps(vo.getFlowId());
				if (list.size() > 0) {
					stepId = list.get(0).getStepId();
				} else {
					stepId = -1;
				}
			}
			vo.setStepId(stepId);
		}*/
		dp.setDatas(voList);
		return dp;
	}
	
	/**
	 * 检查是否有在审批中的审批工作流
	 */
	public boolean checkHasAuditingRefundWorkflow(String userId) {
		String hql=" select count(refWf) from RefundWorkflow refWf ";
		String hqlWhere=" where 1=1 ";
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(userId)) {
			params.put("userId", userId);
			hqlWhere+=" and (auditUser.userId = :userId or applicant.userId = :userId ) ";
			hqlWhere+="and auditStatus = 'AUDITING' ";
			hql += hqlWhere;
			int count = refundWorkflowDao.findCountHql(hql,params);
			if(count > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 通过ID查询结课，退费，电子账户操作工作流
	 */
	@Override
	public RefundWorkflowVo findRefundWorkflowById(String fundWorkflowId) {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(fundWorkflowId);
		RefundWorkflowVo refundWorkflowVo = HibernateUtils.voObjectMapping(refundWorkflow, RefundWorkflowVo.class);
		if ( BonusDistributeType.USER_USER==(refundWorkflow.getFirstSubBonusDistributeTypePerson())){
			String firstRefundDutyPersonId = refundWorkflow.getFirstRefundDutyPerson();
			User firstRefundDutyPerson = userService.findUserById(firstRefundDutyPersonId);
			if (firstRefundDutyPerson!=null){
				refundWorkflowVo.setFirstRefundDutyPersonId(firstRefundDutyPersonId);
				refundWorkflowVo.setFirstRefundDutyPersonName(firstRefundDutyPerson.getName());
			}
		}else if (StringUtils.isNotBlank(refundWorkflow.getFirstRefundDutyPerson())){
			String firstRefundDutyPersonId = refundWorkflow.getFirstRefundDutyPerson();
			Organization organization = organizationDao.findById(firstRefundDutyPersonId);
			if (organization!=null){
				refundWorkflowVo.setFirstRefundDutyPersonId(firstRefundDutyPersonId);
				refundWorkflowVo.setFirstRefundDutyPersonName(organization.getName());
			}
		}
		if ( BonusDistributeType.USER_USER==(refundWorkflow.getSecondSubBonusDistributeTypePerson())){
			String secondRefundDutyPersonId = refundWorkflow.getSecondRefundDutyPerson();
			User secondRefundDutyPerson = userService.findUserById(secondRefundDutyPersonId);
			if (secondRefundDutyPerson!=null){
				refundWorkflowVo.setSecondRefundDutyPersonId(secondRefundDutyPersonId);
				refundWorkflowVo.setSecondRefundDutyPersonName(secondRefundDutyPerson.getName());
			}
		}else if (StringUtils.isNotBlank(refundWorkflow.getSecondRefundDutyPerson())){
			String secondRefundDutyPersonId = refundWorkflow.getSecondRefundDutyPerson();
			Organization organization = organizationDao.findById(secondRefundDutyPersonId);
			if (organization!=null){
				refundWorkflowVo.setSecondRefundDutyPersonId(secondRefundDutyPersonId);
				refundWorkflowVo.setSecondRefundDutyPersonName(organization.getName());
			}
		}
		if (BonusDistributeType.USER_USER == (refundWorkflow.getThirdSubBonusDistributeTypePerson())){
			String thirdRefundDutyPersonId = refundWorkflow.getThirdRefundDutyPerson();
			User thirdRefundDutyPerson = userService.findUserById(thirdRefundDutyPersonId);
			if (thirdRefundDutyPerson!=null){
				refundWorkflowVo.setThirdRefundDutyPersonId(thirdRefundDutyPersonId);
				refundWorkflowVo.setThirdRefundDutyPersonName(thirdRefundDutyPerson.getName());
			}
		}else if (StringUtils.isNotBlank(refundWorkflow.getThirdRefundDutyPerson())){
			String thirdRefundDutyPersonId = refundWorkflow.getThirdRefundDutyPerson();
			Organization organization = organizationDao.findById(thirdRefundDutyPersonId);
			if (organization!=null){
				refundWorkflowVo.setThirdRefundDutyPersonId(thirdRefundDutyPersonId);
				refundWorkflowVo.setThirdRefundDutyPersonName(organization.getName());
			}
		}
		if (BonusDistributeType.USER_USER == (refundWorkflow.getFourthSubBonusDistributeTypePerson())){
			String fourthRefundDutyPersonId = refundWorkflow.getFourthRefundDutyPerson();
			User fourthRefundDutyPerson = userService.findUserById(fourthRefundDutyPersonId);
			if (fourthRefundDutyPerson!=null){
				refundWorkflowVo.setFourthRefundDutyPersonId(fourthRefundDutyPersonId);
				refundWorkflowVo.setFourthRefundDutyPersonName(fourthRefundDutyPerson.getName());
			}
		}else if (StringUtils.isNotBlank(refundWorkflow.getFourthRefundDutyPerson())){
			String fourthRefundDutyPersonId = refundWorkflow.getFourthRefundDutyPerson();
			Organization organization = organizationDao.findById(fourthRefundDutyPersonId);
			if (organization!=null){
				refundWorkflowVo.setFourthRefundDutyPersonId(fourthRefundDutyPersonId);
				refundWorkflowVo.setFourthRefundDutyPersonName(organization.getName());
			}
		}
		if (BonusDistributeType.USER_USER == (refundWorkflow.getFifthSubBonusDistributeTypePerson())){
			String fifthRefundDutyPersonId = refundWorkflow.getFifthRefundDutyPerson();
			User fifthRefundDutyPerson = userService.findUserById(fifthRefundDutyPersonId);
			if (fifthRefundDutyPerson!=null){
				refundWorkflowVo.setFourthRefundDutyPersonId(fifthRefundDutyPersonId);
				refundWorkflowVo.setFourthRefundDutyPersonName(fifthRefundDutyPerson.getName());
			}
		}else if (StringUtils.isNotBlank(refundWorkflow.getFifthRefundDutyPerson())){
			String fifthRefundDutyPersonId = refundWorkflow.getFifthRefundDutyPerson();
			Organization organization = organizationDao.findById(fifthRefundDutyPersonId);
			if (organization!=null){
				refundWorkflowVo.setFifthRefundDutyPersonId(fifthRefundDutyPersonId);
				refundWorkflowVo.setFifthRefundDutyPersonName(organization.getName());
			}
		}


		return refundWorkflowVo;
	}
	
	/**
	 * 删除结课，退费，电子账户操作工作流
	 */
	@Override
	public void deleteRefundWorkflowById(String fundWorkflowId) {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(fundWorkflowId);
		if (RefundAuditStatus.TO_SUBMIT != refundWorkflow.getAuditStatus()) {
			throw new ApplicationException("已在审批状态，不可以删除");
		}
		refundWorkflowDao.delete(refundWorkflow);
		log.info("删除工作流， " + fundWorkflowId);
	}

	/**
	 * 申请结课，退费，电子账户申请
	 */
	@Override
	public String applyRefundWorkFlow(RefundAuditDynamicVo refundAuditDynamicVo, boolean isAuto) throws ApplicationException {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(refundAuditDynamicVo.getRefundWorkflowId());
		String refundWorkflowId = refundAuditDynamicVo.getRefundWorkflowId();
		String oldAuditUserId = refundWorkflow.getAuditUser() != null ? refundWorkflow.getAuditUser().getUserId() : "";
		User currentUser = userService.getCurrentLoginUser();
		if (RefundAuditStatus.TO_SUBMIT == refundWorkflow.getAuditStatus()) {
			// 检查是否符合提交条件
			checkCanApplyFor(refundWorkflow);
			log.info("工作流申请是符合提交条件," + refundWorkflowId);
			// 开始审批流程
			long workflowId = this.initializeWorkFlow(refundWorkflow,refundAuditDynamicVo);
			if (workflowId != -1) {
				//冻结合同产品或学生账户
				this.frozenCpOrStudentAcc(refundWorkflow);
				log.info("冻结合同产品或学生账户," + refundWorkflowId);
				RefundAuditDynamic refundAuditDynamic = HibernateUtils.voObjectMapping(refundAuditDynamicVo, RefundAuditDynamic.class);
				refundAuditDynamic.setOperator(currentUser);
				refundAuditDynamic.setOperationTime(DateTools.getCurrentDateTime());
				Organization applicantCampus = refundWorkflow.getApplicantCampus();
				refundAuditDynamic.setUserDept(applicantCampus);
				refundAuditDynamicDao.save(refundAuditDynamic);
				refundWorkflow.setFlowId(workflowId);
				refundWorkflow.setAuditStatus(RefundAuditStatus.AUDITING);
				
				refundWorkflow.setAuditUser(userService.findUserById(refundAuditDynamicVo.getAuditUserId()));
				if (StringUtils.isBlank(refundWorkflow.getInitiateTime())) {
					refundWorkflow.setInitiateTime(DateTools.getCurrentDateTime());
				}
				refundWorkflow.setAcceptTime(DateTools.getCurrentDateTime());
				refundWorkflow.setModifyTime(DateTools.getCurrentDateTime());
				refundWorkflow.setModifyUserId(currentUser.getUserId());
				Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
				List<Step> list = workflow.getCurrentSteps(workflowId);
				refundWorkflow.setStepId(list.get(0).getStepId() - 1);
				if (StringUtils.isNotBlank(refundAuditDynamicVo.getAuditCampusId())) {
					Organization auditCampus = organizationDao.findById(refundAuditDynamicVo.getAuditCampusId());
					refundWorkflow.setAuditCampus(auditCampus);
				}
				log.info("工作流申请成功，状态设置为审批中，" + refundWorkflowId);
				refundWorkflowDao.merge(refundWorkflow);
			}

		}else if(RefundAuditStatus.AUDITING == refundWorkflow.getAuditStatus()){
			// 开始审批流程
			Organization auditCampus = null;
			if (refundWorkflow.getAuditCampus() != null) {
				auditCampus = organizationDao.findById(refundWorkflow.getAuditCampus().getId());
			}
			long workflowId = this.initializeWorkFlow(refundWorkflow,refundAuditDynamicVo);
			if (workflowId != -1) {
				//冻结合同产品或学生账户
//					this.frozenCpOrStudentAcc(refundWorkflow);
				RefundAuditDynamic refundAuditDynamic = HibernateUtils.voObjectMapping(refundAuditDynamicVo, RefundAuditDynamic.class);
				refundAuditDynamic.setOperator(currentUser);
				refundAuditDynamic.setOperationTime(DateTools.getCurrentDateTime());
				refundAuditDynamic.setUserDept(auditCampus);
				refundAuditDynamicDao.save(refundAuditDynamic);
				//refundWorkflow.setFlowId(workflowId);
//					refundWorkflow.setAuditStatus(RefundAuditStatus.AUDITING);
				refundWorkflow.setAuditUser(new User(refundAuditDynamicVo.getAuditUserId()));
				refundWorkflow.setAcceptTime(DateTools.getCurrentDateTime());
				refundWorkflow.setModifyTime(DateTools.getCurrentDateTime());
				refundWorkflow.setModifyUserId(currentUser.getUserId());
				Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
				List<Step> list = workflow.getCurrentSteps(workflowId);
				refundWorkflow.setStepId(list.get(0).getStepId() - 1);
				
				log.info("工作流审批流转， stepId:" + (list.get(0).getStepId() - 1) + "," + refundWorkflowId);
				if (StringUtils.isNotBlank(refundAuditDynamicVo.getAuditCampusId())) {
					auditCampus = organizationDao.findById(refundAuditDynamicVo.getAuditCampusId());
					refundWorkflow.setAuditCampus(auditCampus);
				}
				refundWorkflowDao.merge(refundWorkflow);
			}
			
		} else if(RefundAuditStatus.AUDIT_REVOKE == refundWorkflow.getAuditStatus())  {
			throw new ApplicationException("该审批申请已被申请人撤销，系统禁止审批操作");
		} else {
			throw new ApplicationException("审批状态为非可审批状态,不可再操作审批");
		}
		if (!isAuto && (refundAuditDynamicVo.getOperation() == AuditOperation.AUDIT_SUBMIT || "".equals(oldAuditUserId) || !oldAuditUserId.equals(refundWorkflow.getAuditUser().getUserId()))) {
//			this.sendRefundSysMsg(refundWorkflow.getId(), refundAuditDynamicVo.getOperation());
		}
		if(refundAuditDynamicVo.getOperation()!=null &&
		   (refundAuditDynamicVo.getOperation().equals(AuditOperation.AUDIT_PASS) || refundAuditDynamicVo.getOperation().equals(AuditOperation.AUDIT_SUBMIT) )  && 
		   currentUser.getUserId().equals(refundAuditDynamicVo.getAuditUserId())){//同一个操作人  ,且操作是审批通过,就执行自动操作方法
			return autoAuditSameUser(refundWorkflow, refundAuditDynamicVo);
		}
		
		return null;
	}
	
	private void sendRefundSysMsg(String refundWorkflowId, AuditOperation auditOperation) {
		refundWorkflowDao.flush();
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		if (auditOperation == AuditOperation.AUDIT_SUBMIT 
				|| auditOperation == AuditOperation.AUDIT_PASS
				|| auditOperation == AuditOperation.FINANCE_RECEIVE
				|| auditOperation == AuditOperation.CHANGE_AUDIT_USER) {
			SendRefundSysMsgThread thread = new SendRefundSysMsgThread(refundWorkflowId, userId, MsgNo.M4);
			taskExecutor.execute(thread);
		} else if (auditOperation == AuditOperation.AUDIT_ROLLBACK) {
			SendRefundSysMsgThread thread = new SendRefundSysMsgThread(refundWorkflowId, userId, MsgNo.M5);
			taskExecutor.execute(thread);
		}
	}
	
	/**
	 * 改变当前审批用户
	 */
	public void changeAuditUser(String refundWorkflowId, String auditUserId) throws ApplicationException {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(refundWorkflowId);
		User currentUser = userService.getCurrentLoginUser();
		User fromAuditUser = refundWorkflow.getAuditUser();
		User toAuditUser = userService.findUserById(auditUserId);
		refundWorkflow.setAuditUser(toAuditUser);
		refundWorkflow.setAcceptTime(DateTools.getCurrentDateTime());
		refundWorkflow.setModifyTime(DateTools.getCurrentDateTime());
		refundWorkflow.setModifyUserId(currentUser.getUserId());
		RefundAuditDynamic refundAuditDynamic = new RefundAuditDynamic();
		refundAuditDynamic.setOperator(currentUser);
		refundAuditDynamic.setOperation(AuditOperation.CHANGE_AUDIT_USER);
		refundAuditDynamic.setOperationTime(DateTools.getCurrentDateTime());
		UserDeptJob userDeptJob = userDeptJobDao.findDeptJobByParam(currentUser.getUserId(), 0);
		if (userDeptJob != null) {
			Organization org = organizationDao.findById(userDeptJob.getDeptId());
			refundAuditDynamic.setUserDept(org);
		}
		refundAuditDynamic.setRefundWorkflow(refundWorkflow);
		refundAuditDynamic.setAuditRemark("变更当前步骤接收人");
		long flowId = refundWorkflow.getFlowId();
		Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
		List<Step> list = workflow.getCurrentSteps(flowId);
		StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflow.getWorkflowName(flowId)).getStep(list.get(0).getStepId());
		refundAuditDynamic.setStepName(stepDescriptor.getName());
		refundAuditDynamicDao.save(refundAuditDynamic);
		refundWorkflowDao.merge(refundWorkflow);
		log.info("工作流审批修改审批用户，由审批人：" + fromAuditUser.getName() + "，改为：" + toAuditUser.getName() + ",stepId:" + refundWorkflow.getStepId() + "," + refundWorkflowId);
//		this.sendRefundSysMsg(refundWorkflow.getId(), AuditOperation.CHANGE_AUDIT_USER);
	}
	
	/**
	 * 如果下一步骤操作人相同，自动执行
	 * @param refundVo
	 * @param refundAuditDynamicVo
	 */
	public String autoAuditSameUser(RefundWorkflow refundVo,RefundAuditDynamicVo refundAuditDynamicVo){
		String workflowName=getWorkFlowName(refundVo);
		long workflowId = -1;
		workflowId=refundVo.getFlowId();
		Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
		List<Step> list = workflow.getCurrentSteps(workflowId);
		StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflowName).getStep(list.get(0).getStepId());
		Map<String,String> stepParams=stepDescriptor.getMetaAttributes();//获取步骤的参数
		
		boolean flag=false;//标记是否可以用参数来查询数据
		
		String key = "";
		if (stepParams.containsKey("auditPassBtn")) {
			key = "auditPassBtn";
		} else if (stepParams.containsKey("financialReceiveBtn")) {
			key = "financialReceiveBtn";
		} else if (stepParams.containsKey("financialTakeOutBtn")) {
			key = "financialTakeOutBtn";
		}
		
		String[] param=getStepParamById(stepParams, key);//获取提交动作的参数
		if(param.length==4){
			flag=true;
		}
		
		
		List<ActionDescriptor> actions = stepDescriptor.getActions();//获取当前步骤的所有动作
		for (Iterator iterator = actions.iterator(); iterator.hasNext();) {//遍历步骤
			ActionDescriptor action = (ActionDescriptor) iterator.next();//获取步骤的动作元素存储器
			Map map=action.getMetaAttributes();//获取步骤的动作元素
			if(map.get("doSubmit")!=null){//找到提交的动作
				if(map.get("nextJob")!=null && flag){//通过动作的参数找到下一步的操作人
					String campusId=refundVo.getRefundCampus().getId();
					if (refundVo.getFormType() == RefundFormType.ACCOUNT_TRANSFER) {
						if(param.length>3 && param[3]!=null && param[3].equals("3")){//第三步的校区是转入校区
							campusId=refundVo.getIntoCampus().getId();
						}
					}
					List<Map<String, String>> listUser=userService.getUsersByJobCode(param[1],param[2],campusId);
					if(listUser.size()==1){//如果下一步只有一位操作人,设置当前动作，下一步执行人，跳转到审批操作方法。
						Organization auditCampus = organizationDao.findById(listUser.get(0).get("campusId"));
//						refundVo.setAuditCampus(auditCampus);
						refundAuditDynamicVo.setActionId(action.getId());
						refundAuditDynamicVo.setAuditUserId(listUser.get(0).get("userId"));
						log.info("工作流审批自动流转， stepId:" + (list.get(0).getStepId()) + "," + refundAuditDynamicVo.getRefundWorkflowId());
						applyRefundWorkFlow(refundAuditDynamicVo, true);
					}else{//其他情况都跳转回页面做处理吧。
						return refundVo.getId();
					}
					break;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 获取提交动作的参数
	 * @param stepParams
	 * @param key
	 * @return
	 */
	public String[] getStepParamById(Map<String,String> stepParams,String key){
		if(stepParams.get(key)!=null){
			return stepParams.get(key).split("_");//获取提交动作的参数
		}
		return null;
	}
	
	public String getWorkFlowName(RefundWorkflow refundVo){
		String workflowName = "";
		if ("advance".equals(PropertiesUtils.getStringValue("institution"))){
			if (RefundFormType.ACCOUNT_TRANSFER == refundVo.getFormType()) {
				workflowName = "advance_account_transfer";
			} else if (RefundFormType.MONEY_REFUND == refundVo.getFormType()) {
				workflowName = "advance_money_refund";
			} else if (RefundFormType.MOENY_ACC_REFUND == refundVo.getFormType()) {
				workflowName = "advance_money_acc_refund";
			}
		}else {
			if (RefundFormType.ACCOUNT_TRANSFER == refundVo.getFormType()) {
				workflowName = "account_transfer";
			} else if (RefundFormType.MONEY_REFUND == refundVo.getFormType()) {
				workflowName = "money_refund";
			} else if (RefundFormType.MOENY_ACC_REFUND == refundVo.getFormType()) {
				workflowName = "money_acc_refund";
			}
		}

		return workflowName;
	}
	
	@Override
	public Map<String ,Object> getDynamicVoByFlowId(String fundWorkflowId) {
		Map<String ,Object>  returnMap =new HashMap<String, Object>();
		if (StringUtil.isNotBlank(fundWorkflowId)) {
			RefundWorkflow refundWorkflow = refundWorkflowDao.findById(fundWorkflowId);
			if (refundWorkflow != null) {
				List<RefundAuditDynamic> list=refundAuditDynamicDao.findListByFlowId(fundWorkflowId);
				String endDate = DateTools.getCurrentDate();
				if (refundWorkflow.getAuditStatus() == RefundAuditStatus.AUDIT_COMPLETED 
						|| refundWorkflow.getAuditStatus() == RefundAuditStatus.AUDIT_REVOKE) {
					endDate = refundWorkflow.getAcceptTime();
				}
				
				returnMap.put("activeDays", DateTools.daysBetween(refundWorkflow.getCreateTime(), endDate));
				List<RefundAuditDynamicVo> voList =  HibernateUtils.voListMapping(list, RefundAuditDynamicVo.class);
				for (RefundAuditDynamicVo vo: voList) {
					List<RefundAuditEvidenceVo> evidenceVos = this.getEvidenceByDynamicId(vo.getId());
					if (evidenceVos != null && evidenceVos.size() > 0) {
						vo.setEvidenceVos(evidenceVos);
					}
				}
				returnMap.put("operations", voList);
			}
		}
		return returnMap;
	}
	
	/**
	 * 审批操作
	 */
	@Override
	public void doActionRefundWorkFlow(RefundAuditDynamicVo refundAuditDynamicVo) {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(refundAuditDynamicVo.getRefundWorkflowId());
		Organization auditCampus = organizationDao.findById(refundWorkflow.getAuditCampus().getId());
		if(RefundAuditStatus.AUDIT_REVOKE == refundWorkflow.getAuditStatus()) {
			throw new ApplicationException("该审批申请已被申请人撤销，系统禁止审批操作");
		}
		RefundFormType refundFormType = refundWorkflow.getFormType();
		Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
		long workflowId = refundWorkflow.getFlowId();
		List<Step> list = workflow.getCurrentSteps(refundWorkflow.getFlowId());
		
		StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflow.getWorkflowName(refundWorkflow.getFlowId())).getStep(list.get(0).getStepId());
		try {
			workflow.doAction(workflowId, refundAuditDynamicVo.getActionId(), null);
		} catch (WorkflowException e) {
			e.printStackTrace();
		}
		if (refundWorkflow.getAuditStatus() == RefundAuditStatus.AUDIT_COMPLETED) {
			refundWorkflow.setStepId(refundWorkflow.getStepId() + 1);
		}
		refundAuditDynamicVo.setOperationName(stepDescriptor.getName());
		RefundAuditDynamic refundAuditDynamic = HibernateUtils.voObjectMapping(refundAuditDynamicVo, RefundAuditDynamic.class);
		refundAuditDynamic.setStepName(stepDescriptor.getName());
		refundAuditDynamic.setOperator(userService.getCurrentLoginUser());
		refundAuditDynamic.setOperationTime(DateTools.getCurrentDateTime());
		refundAuditDynamic.setUserDept(auditCampus);
		refundAuditDynamicDao.save(refundAuditDynamic);
		log.info("工作流撤销审批， " + refundAuditDynamicVo.getRefundWorkflowId());
		refundWorkflowDao.save(refundWorkflow);
//		this.sendRefundSysMsg(refundWorkflow.getId(), refundAuditDynamicVo.getOperation());
//		long stepId = list.get(0).getActionId();
		
	}
	
	/**
	 * 财务出款，结束工作流
	 */
	public String financialTakeOut(MultipartFile[] auditEvidenceFiles, RefundAuditDynamicVo refundAuditDynamicVo) throws ApplicationException {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(refundAuditDynamicVo.getRefundWorkflowId());
		Organization auditCampus = organizationDao.findById(refundWorkflow.getAuditCampus().getId());
		if(RefundAuditStatus.AUDIT_REVOKE == refundWorkflow.getAuditStatus()) {
			throw new ApplicationException("该审批申请已被申请人撤销，系统禁止审批操作");
		}
		List<RefundAuditEvidence> evidences = new ArrayList<RefundAuditEvidence>();
		if (auditEvidenceFiles != null && auditEvidenceFiles.length > 0) {
			String fileName = "";
			RefundAuditEvidence evidence = null;
			for (MultipartFile auditEvidenceFile : auditEvidenceFiles) {
				if (auditEvidenceFile.getSize() > 0) {
					evidence = new RefundAuditEvidence();
					String oriFilename = auditEvidenceFile.getOriginalFilename();
					evidence.setFileName(oriFilename); // 原文件名称
					String suffix = oriFilename.substring(oriFilename.indexOf("."));
					fileName = UUID.randomUUID().toString() + suffix;
					evidence.setEvidencePath(fileName); // 保存到阿里云上的文件名称
					evidences.add(evidence);
					File realFile=new File(fileName);
					try {
						auditEvidenceFile.transferTo(realFile);
						AliyunOSSUtils.put(fileName, realFile); // 上传图片到阿里云
					} catch (IOException e) {
						e.printStackTrace();
					}finally{				
						realFile.delete();
					} 			
				}
			}
		}
		Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
		long workflowId = refundWorkflow.getFlowId();
		List<Step> list = workflow.getCurrentSteps(refundWorkflow.getFlowId());
		StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflow.getWorkflowName(refundWorkflow.getFlowId())).getStep(list.get(0).getStepId());
		String stepName = stepDescriptor.getName();
		try {
			workflow.doAction(workflowId, refundAuditDynamicVo.getActionId(), null);
		} catch (WorkflowException e) {
			throw new ApplicationException(e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
		if (refundWorkflow.getAuditStatus() == RefundAuditStatus.AUDIT_COMPLETED) {
			refundWorkflow.setStepId(refundWorkflow.getStepId() + 1);
		}
		User currentUser = userService.getCurrentLoginUser();
		refundAuditDynamicVo.setOperationName(stepDescriptor.getName());
		RefundAuditDynamic refundAuditDynamic = HibernateUtils.voObjectMapping(refundAuditDynamicVo, RefundAuditDynamic.class);
		refundAuditDynamic.setOperation(AuditOperation.FINANCE_EXPENDITURE);
		refundAuditDynamic.setStepName(stepName);
		refundAuditDynamic.setOperator(currentUser);
		refundAuditDynamic.setUserDept(auditCampus);
		refundAuditDynamic.setOperationTime(DateTools.getCurrentDateTime());
		refundAuditDynamicDao.save(refundAuditDynamic);
		refundAuditDynamicDao.commit();
		log.info("财务出款，结束工作流， " + refundAuditDynamicVo.getRefundWorkflowId());
		if (evidences.size() > 0) {
			for (RefundAuditEvidence evidence : evidences) {
				evidence.setRefundAuditDynamic(refundAuditDynamic);
				evidence.setCreateUserId(currentUser.getUserId());
				evidence.setCreateTime(DateTools.getCurrentDateTime());
				refundAuditEvidenceDao.save(evidence);
			}
		}
		return refundAuditDynamic.getId();
		
	}
	
	/**
	 * 根据flowId查询工作流
	 */
	@Override
	public RefundWorkflow findRefundWorkflowByFlowId(long flowId) {
		return refundWorkflowDao.findRefundWorkflowByflowId(flowId);
	}
	
	/**
	 * 解冻学生电子账户的这部分冻结金额
	 */
	public void unfreezeStudnetAccMv(RefundWorkflow refundWorkflow) {
	    if (refundWorkflow.getStepId()  != 0) {
	        if (refundWorkflow.getFormType() == RefundFormType.MONEY_REFUND) {
	            ContractProduct cp = refundWorkflow.getRefundContractProduct();
	            cp.setIsFrozen(1);
	            contractProductDao.merge(cp);
	            contractProductDao.flush();
	        } else {
	            StudnetAccMv studnetAccMv = studnetAccMvDao.getStudnetAccMvByStudentId(refundWorkflow.getRefundStudent().getId());
	            studnetAccMv.setFrozenAmount(studnetAccMv.getFrozenAmount().subtract(refundWorkflow.getBasicOperateAmount()));
	            studnetAccMvDao.merge(studnetAccMv);
	            studnetAccMvDao.flush();
	        }
	    }
	}
	
	/**
	 * 完成电子账户转账工作流，资金流转
	 * @param refundWorkflow
	 * @throws Exception 
	 */
	public void completeRefundWorkflow(RefundWorkflow refundWorkflow) throws Exception {
		if (refundWorkflow.getFormType() == RefundFormType.ACCOUNT_TRANSFER) {
			studentService.transferElectronicAmount(refundWorkflow.getRefundStudent().getId(), refundWorkflow.getIntoStudent().getId(), refundWorkflow.getBasicOperateAmount());
			StudnetAccMv refundAccMv = studnetAccMvDao.getStudnetAccMvByStudentId(refundWorkflow.getRefundStudent().getId());
			refundAccMv.setFrozenAmount(refundAccMv.getFrozenAmount().subtract(refundWorkflow.getBasicOperateAmount()));
			studnetAccMvDao.merge(refundAccMv);
		} else if (refundWorkflow.getFormType() == RefundFormType.MONEY_REFUND) {
			ContractProduct cp = refundWorkflow.getRefundContractProduct();
			cp.setIsFrozen(1);
			BigDecimal basicOperateAmount = refundWorkflow.getBasicOperateAmount() != null ? refundWorkflow.getBasicOperateAmount() : BigDecimal.ZERO;
			BigDecimal promotionOperateAmount = refundWorkflow.getPromotionOperateAmount() != null ? refundWorkflow.getPromotionOperateAmount() : BigDecimal.ZERO;
			BigDecimal specialOperateAmount = refundWorkflow.getSpecialOperateAmount() != null ? refundWorkflow.getSpecialOperateAmount() : BigDecimal.ZERO;
			
//			BigDecimal returnAmountFromPromotionAcc = cp.getPromotionAmount().subtract(promotionOperateAmount);
			// 优惠回归收入改用优惠金额减去超额退费
			BigDecimal returnAmountFromPromotionAcc = refundWorkflow.getCpPromotionMaxAmount().subtract(specialOperateAmount);
			String refundCase = refundWorkflow.getRefundCause() != null ? refundWorkflow.getRefundCause().getId() : null;


			List<IncomeDistributionVo> list = getIncomeDistributionVos(refundWorkflow);
			
			contractService.closeContractProcuct(promotionOperateAmount, returnAmountFromPromotionAcc, 
					cp.getId(), true, basicOperateAmount, specialOperateAmount,
					refundWorkflow.getRefundDetailReason(), refundCase, null, refundWorkflow.getBankInfo(), refundWorkflow.getRefundAccount(),
					refundWorkflow.getRefundCampus().getId(), refundWorkflow.getApplicant().getUserId(), null, list, null);
			contractProductDao.merge(cp);
			
		} else if (refundWorkflow.getFormType() == RefundFormType.MOENY_ACC_REFUND) {
			BigDecimal basicOperateAmount = refundWorkflow.getBasicOperateAmount() != null ? refundWorkflow.getBasicOperateAmount() : BigDecimal.ZERO;
			BigDecimal specialOperateAmount = refundWorkflow.getSpecialOperateAmount() != null ? refundWorkflow.getSpecialOperateAmount() : BigDecimal.ZERO;
			String firstUserId = refundWorkflow.getFirstRefundDutyPerson() != null ? refundWorkflow.getFirstRefundDutyPerson() : null; //refundWorkflow.getFirstRefundDutyPerson().getUserId()
			String secondUserId = refundWorkflow.getSecondRefundDutyPerson() != null ? refundWorkflow.getSecondRefundDutyPerson() : null; //refundWorkflow.getSecondRefundDutyPerson().getUserId()
			String thirdUserId = refundWorkflow.getThirdRefundDutyPerson() != null ? refundWorkflow.getThirdRefundDutyPerson() : null; //refundWorkflow.getThirdRefundDutyPerson().getUserId()
			String firstOrganizationId = refundWorkflow.getFirstRefundDutyCampus() != null ? refundWorkflow.getFirstRefundDutyCampus().getId() : null;
			String secondOrganizationId = refundWorkflow.getSecondRefundDutyCampus() != null ? refundWorkflow.getSecondRefundDutyCampus().getId() : null;
			String thirdOrganizationId = refundWorkflow.getThirdRefundDutyCampus() != null ? refundWorkflow.getThirdRefundDutyCampus().getId() : null;
			
//			String fourthRefundDutAmount = refundWorkflow.getFourthRefundDutAmount() != null ? refundWorkflow.getFourthRefundDutAmount().toString() : null;
//			String firstRefundDutyAmount = refundWorkflow.getFirstRefundDutyAmount() != null ? refundWorkflow.getFirstRefundDutyAmount().toString() : null;
//			String fifthRefundDutyAmount = refundWorkflow.getFifthRefundDutyAmount() != null ? refundWorkflow.getFifthRefundDutyAmount().toString() : null;
//			String secondRefundDutyAmount = refundWorkflow.getSecondRefundDutyAmount() != null ? refundWorkflow.getSecondRefundDutyAmount().toString() : null;
//			String sixthRefundDutyAmount = refundWorkflow.getSixthRefundDutyAmount() != null ? refundWorkflow.getSixthRefundDutyAmount().toString() : null;
//			String thirdRefundDutyAmount = refundWorkflow.getThirdRefundDutyAmount() != null ? refundWorkflow.getThirdRefundDutyAmount().toString() : null;
			String refundCase = refundWorkflow.getRefundCause() != null ? refundWorkflow.getRefundCause().getId() : null;

			RefundIncomeDistributeVo refundIncomeDistributeVo =getRefundIncomeDistributionVo(refundWorkflow);
			studentService.withDrawElectronicAmount(refundWorkflow.getRefundStudent().getId(),
					basicOperateAmount, specialOperateAmount, BigDecimal.ZERO, basicOperateAmount, refundWorkflow.getRefundDetailReason(),
					refundCase,
					null, null, refundWorkflow.getBankInfo(), refundWorkflow.getRefundAccount(),
					refundWorkflow.getRefundCampus().getId(), refundWorkflow.getApplicant().getUserId(), refundIncomeDistributeVo);
			StudnetAccMv refundAccMv = studnetAccMvDao.getStudnetAccMvByStudentId(refundWorkflow.getRefundStudent().getId());
			refundAccMv.setFrozenAmount(refundAccMv.getFrozenAmount().subtract(refundWorkflow.getBasicOperateAmount()));
			studnetAccMvDao.merge(refundAccMv);
		}
		
		
	}

	private RefundIncomeDistributeVo getRefundIncomeDistributionVo(RefundWorkflow refundWorkflow) {
		RefundIncomeDistributeVo refundIncomeDistributeVo = new RefundIncomeDistributeVo();
		if (StringUtils.isNotBlank(refundWorkflow.getFirstRefundDutyPerson())){
			refundIncomeDistributeVo.setFirstRefundDutyPersonId(refundWorkflow.getFirstRefundDutyPerson());
			refundIncomeDistributeVo.setFirstSubBonusDistributeTypePerson(refundWorkflow.getFirstSubBonusDistributeTypePerson());
			refundIncomeDistributeVo.setFirstRefundDutyAmountPerson(refundWorkflow.getFirstRefundDutyAmountPerson());
		}
		if (StringUtils.isNotBlank(refundWorkflow.getSecondRefundDutyPerson())){
			refundIncomeDistributeVo.setSecondRefundDutyPersonId(refundWorkflow.getSecondRefundDutyPerson());
			refundIncomeDistributeVo.setSecondSubBonusDistributeTypePerson(refundWorkflow.getSecondSubBonusDistributeTypePerson());
			refundIncomeDistributeVo.setSecondRefundDutyAmountPerson(refundWorkflow.getSecondRefundDutyAmountPerson());
		}

		if (StringUtils.isNotBlank(refundWorkflow.getThirdRefundDutyPerson())){
			refundIncomeDistributeVo.setThirdRefundDutyPersonId(refundWorkflow.getThirdRefundDutyPerson());
			refundIncomeDistributeVo.setThirdSubBonusDistributeTypePerson(refundWorkflow.getThirdSubBonusDistributeTypePerson());
			refundIncomeDistributeVo.setThirdRefundDutyAmountPerson(refundWorkflow.getThirdRefundDutyAmountPerson());
		}

		if (StringUtils.isNotBlank(refundWorkflow.getFourthRefundDutyPerson())){
			refundIncomeDistributeVo.setFourthRefundDutyPersonId(refundWorkflow.getFourthRefundDutyPerson());
			refundIncomeDistributeVo.setFourthSubBonusDistributeTypePerson(refundWorkflow.getFourthSubBonusDistributeTypePerson());
			refundIncomeDistributeVo.setFourthRefundDutyAmountPerson(refundWorkflow.getFourthRefundDutyAmountPerson());
		}

		if (StringUtils.isNotBlank(refundWorkflow.getFifthRefundDutyPerson())){
			refundIncomeDistributeVo.setFifthRefundDutyPersonId(refundWorkflow.getFifthRefundDutyPerson());
			refundIncomeDistributeVo.setFifthSubBonusDistributeTypePerson(refundWorkflow.getFifthSubBonusDistributeTypePerson());
			refundIncomeDistributeVo.setFifthRefundDutyAmountPerson(refundWorkflow.getFifthRefundDutyAmountPerson());
		}

		if (refundWorkflow.getFirstRefundDutyCampus()!=null){
			refundIncomeDistributeVo.setFirstRefundDutyCampusId(refundWorkflow.getFirstRefundDutyCampus().getId());
			refundIncomeDistributeVo.setFirstSubBonusDistributeTypeCampus(refundWorkflow.getFirstSubBonusDistributeTypeCampus());
			refundIncomeDistributeVo.setFirstRefundDutyAmountCampus(refundWorkflow.getFirstRefundDutyAmountCampus());
		}

		if (refundWorkflow.getSecondRefundDutyCampus()!=null){
			refundIncomeDistributeVo.setSecondRefundDutyCampusId(refundWorkflow.getSecondRefundDutyCampus().getId());
			refundIncomeDistributeVo.setSecondSubBonusDistributeTypeCampus(refundWorkflow.getSecondSubBonusDistributeTypeCampus());
			refundIncomeDistributeVo.setSecondRefundDutyAmountCampus(refundWorkflow.getSecondRefundDutyAmountCampus());
		}

		if (refundWorkflow.getThirdRefundDutyCampus()!=null){
			refundIncomeDistributeVo.setThirdRefundDutyCampusId(refundWorkflow.getThirdRefundDutyCampus().getId());
			refundIncomeDistributeVo.setThirdSubBonusDistributeTypeCampus(refundWorkflow.getThirdSubBonusDistributeTypeCampus());
			refundIncomeDistributeVo.setThirdRefundDutyAmountCampus(refundWorkflow.getThirdRefundDutyAmountCampus());
		}

		if (refundWorkflow.getFourthRefundDutyCampus()!=null){
			refundIncomeDistributeVo.setFourthRefundDutyCampusId(refundWorkflow.getFourthRefundDutyCampus().getId());
			refundIncomeDistributeVo.setFourthSubBonusDistributeTypeCampus(refundWorkflow.getFourthSubBonusDistributeTypeCampus());
			refundIncomeDistributeVo.setFourthRefundDutyAmountCampus(refundWorkflow.getFourthRefundDutyAmountCampus());
		}

		if (refundWorkflow.getFifthRefundDutyCampus()!=null){
			refundIncomeDistributeVo.setFifthRefundDutyCampusId(refundWorkflow.getFifthRefundDutyCampus().getId());
			refundIncomeDistributeVo.setFifthSubBonusDistributeTypeCampus(refundWorkflow.getFifthSubBonusDistributeTypeCampus());
			refundIncomeDistributeVo.setFifthRefundDutyAmountCampus(refundWorkflow.getFifthRefundDutyAmountCampus());
		}

		return refundIncomeDistributeVo;
	}

	private List<IncomeDistributionVo> getIncomeDistributionVos(RefundWorkflow refundWorkflow) {
		List<IncomeDistributionVo> list = new ArrayList<>();
//		String firstUserId = refundWorkflow.getFirstRefundDutyPerson() != null ? refundWorkflow.getFirstRefundDutyPerson() : null; //refundWorkflow.getFirstRefundDutyPerson().getUserId()

		if (StringUtils.isNotBlank(refundWorkflow.getFirstRefundDutyPerson())){
			IncomeDistributionVo i1 = new IncomeDistributionVo();
			String firstUserId = refundWorkflow.getFirstRefundDutyPerson();

			BonusDistributeType firstSubBonusDistributeTypePerson = refundWorkflow.getFirstSubBonusDistributeTypePerson();
			i1.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i1.setAmount(refundWorkflow.getFirstRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i1, firstUserId, firstSubBonusDistributeTypePerson);
			list.add(i1);
		}
		if (StringUtils.isNotBlank(refundWorkflow.getSecondRefundDutyPerson())){
			IncomeDistributionVo i2 = new IncomeDistributionVo();
			String secondUserId = refundWorkflow.getSecondRefundDutyPerson();

			BonusDistributeType secondSubBonusDistributeTypePerson = refundWorkflow.getSecondSubBonusDistributeTypePerson();
			i2.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i2.setAmount(refundWorkflow.getSecondRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i2, secondUserId, secondSubBonusDistributeTypePerson);
			list.add(i2);
		}
		if (StringUtils.isNotBlank(refundWorkflow.getThirdRefundDutyPerson())){
			IncomeDistributionVo i3 = new IncomeDistributionVo();
			String thirdUserId = refundWorkflow.getThirdRefundDutyPerson();

			BonusDistributeType thirdSubBonusDistributeTypePerson = refundWorkflow.getThirdSubBonusDistributeTypePerson();
			i3.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i3.setAmount(refundWorkflow.getThirdRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i3, thirdUserId, thirdSubBonusDistributeTypePerson);
			list.add(i3);
		}
		if (StringUtils.isNotBlank(refundWorkflow.getFourthRefundDutyPerson())){
			IncomeDistributionVo i4 = new IncomeDistributionVo();
			String fourthUserId = refundWorkflow.getFourthRefundDutyPerson();

			BonusDistributeType fourthSubBonusDistributeTypePerson = refundWorkflow.getFourthSubBonusDistributeTypePerson();
			i4.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i4.setAmount(refundWorkflow.getFourthRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i4, fourthUserId, fourthSubBonusDistributeTypePerson);
			list.add(i4);
		}
		if (StringUtils.isNotBlank(refundWorkflow.getFifthRefundDutyPerson())){
			IncomeDistributionVo i5 = new IncomeDistributionVo();
			String fifthUserId = refundWorkflow.getFifthRefundDutyPerson();

			BonusDistributeType fifthSubBonusDistributeTypePerson = refundWorkflow.getFifthSubBonusDistributeTypePerson();
			i5.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i5.setAmount(refundWorkflow.getFifthRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i5, fifthUserId, fifthSubBonusDistributeTypePerson);
			list.add(i5);
		}

//		if (refundWorkflow.getSecondRefundDutyPerson() != null){
//			IncomeDistributionVo i2 = new IncomeDistributionVo();
//			String secondUserId = refundWorkflow.getSecondRefundDutyPerson();
//		}

		if (refundWorkflow.getFirstRefundDutyCampus()!=null){
			IncomeDistributionVo c1 = new IncomeDistributionVo();
			String firstOrganizationId = refundWorkflow.getFirstRefundDutyCampus().getId();
			c1.setBonusOrgId(firstOrganizationId);
			c1.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c1.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c1.setAmount(refundWorkflow.getFirstRefundDutyAmountCampus());
			list.add(c1);
		}
		if (refundWorkflow.getSecondRefundDutyCampus()!=null){
			IncomeDistributionVo c2 = new IncomeDistributionVo();
			String secondOrganizationId = refundWorkflow.getSecondRefundDutyCampus().getId();
			c2.setBonusOrgId(secondOrganizationId);
			c2.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c2.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c2.setAmount(refundWorkflow.getSecondRefundDutyAmountCampus());
			list.add(c2);
		}
		if (refundWorkflow.getThirdRefundDutyCampus()!=null){
			IncomeDistributionVo c3 = new IncomeDistributionVo();
			String thirdOrganizationId = refundWorkflow.getThirdRefundDutyCampus().getId();
			c3.setBonusOrgId(thirdOrganizationId);
			c3.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c3.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c3.setAmount(refundWorkflow.getThirdRefundDutyAmountCampus());
			list.add(c3);
		}

		if (refundWorkflow.getFourthRefundDutyCampus()!=null){
			IncomeDistributionVo c4 = new IncomeDistributionVo();
			String fourthOrganizationId = refundWorkflow.getFourthRefundDutyCampus().getId();
			c4.setBonusOrgId(fourthOrganizationId);
			c4.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c4.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c4.setAmount(refundWorkflow.getFourthRefundDutyAmountCampus());
			list.add(c4);
		}

		if (refundWorkflow.getFifthRefundDutyCampus()!=null){
			IncomeDistributionVo c5 = new IncomeDistributionVo();
			String fifthOrganizationId = refundWorkflow.getFifthRefundDutyCampus().getId();
			c5.setBonusOrgId(fifthOrganizationId);
			c5.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c5.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c5.setAmount(refundWorkflow.getFifthRefundDutyAmountCampus());
			list.add(c5);
		}


		return list;
	}

	private void setInfoForIncomeDistribution(IncomeDistributionVo i1, String firstUserId, BonusDistributeType firstSubBonusDistributeTypePerson) {
		switch (firstSubBonusDistributeTypePerson){
            case USER_CAMPUS:
                i1.setBonusOrgId(firstUserId);
                i1.setSubBonusDistributeType(BonusDistributeType.USER_CAMPUS.getValue());
                break;
            case USER_BRANCH:
                i1.setBonusOrgId(firstUserId);
                i1.setSubBonusDistributeType(BonusDistributeType.USER_BRANCH.getValue());
                break;
            case USER_USER:
                i1.setBonusStaffId(firstUserId);
                i1.setSubBonusDistributeType(BonusDistributeType.USER_USER.getValue());
        }
	}

	/**
	 * 通过退款、电子账户转账流程ID获取退款凭证
	 */
	public List<RefundEvidenceVo> getEvidenceByFlowId(String fundWorkflowId) {
		return refundEvidenceDao.getEvidenceByFlowId(fundWorkflowId);
	}
	
	/**
	 * 通过审批动态ID获取财务出款凭证
	 */
	public List<RefundAuditEvidenceVo> getEvidenceByDynamicId(String refundAuditDynamicId) {
		return refundAuditEvidenceDao.getEvidenceByDynamicId(refundAuditDynamicId);
	}
	
	/**
	 * 删除退款凭证
	 */
	public void deleteEvidenceById(String evidenceId) {
		RefundEvidence evidence = refundEvidenceDao.findById(evidenceId);
		AliyunOSSUtils.remove(evidence.getEvidencePath());
		refundEvidenceDao.delete(evidence);
	}
	
	/**
	 * 查找当前步骤的用户
	 */
	public List<Map<String, String>> getCurrentStepUsers(String refundWorkflowId) {
		RefundWorkflow refundWorkflow = refundWorkflowDao.findById(refundWorkflowId);
		long workflowId = refundWorkflow.getFlowId();
		if (workflowId > 0 ) {
			Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
			List<Step> list = workflow.getCurrentSteps(refundWorkflow.getFlowId());
			StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflow.getWorkflowName(workflowId)).getStep(list.get(0).getStepId() - 1);
			Map<String,String> stepParams=stepDescriptor.getMetaAttributes();//获取步骤的参数

			String key = "";
			if (list.get(0).getStepId() - 1 == 1) {
				key = "submitAuditBtn";
			} else {
				if (stepParams.containsKey("auditPassBtn")) {
					key = "auditPassBtn";
				} else if (stepParams.containsKey("financialReceiveBtn")) {
					key = "financialReceiveBtn";
				} else if (stepParams.containsKey("financialTakeOutBtn")) {
					key = "financialTakeOutBtn";
				}
			}
			
			String[] steps = getStepParamById(stepParams, key);//获取提交动作的参数
			if (steps.length == 4) {
				String jobCode = steps[1];
				String orgType = steps[2];
				int goStep =  Integer.valueOf(steps[3]);
				String campusId = null;
				campusId=refundWorkflow.getRefundCampus().getId();
				if (refundWorkflow.getFormType() == RefundFormType.ACCOUNT_TRANSFER) {
					if(goStep==3){//第二步是操作校区，其他的是转入校区
						campusId=refundWorkflow.getIntoCampus().getId();
					}
				}
				return userService.getUsersByJobCode(jobCode, orgType, campusId);
			}
		}
		return null;
	}
	
	/**
	 * 保存图片的旋转角度
	 */
	public void reventEvidence(String evidenceId, int rate) {
		RefundEvidence evidence = refundEvidenceDao.findById(evidenceId);
		evidence.setRate(rate);
		evidence.setModifyUserId(userService.getCurrentLoginUser().getUserId());
		evidence.setModifyTime(DateTools.getCurrentDateTime());
		refundEvidenceDao.merge(evidence);
	}
	
	/**
	 * 获取退款凭证
	 */
	public RefundEvidenceVo getEvidenceById(@RequestParam String evidenceId) {
		return HibernateUtils.voObjectMapping(refundEvidenceDao.findById(evidenceId), RefundEvidenceVo.class);
	}
	
	//冻结合同产品或学生账户
	private void frozenCpOrStudentAcc(RefundWorkflow refundWorkflow) {
		if (RefundFormType.MONEY_REFUND == refundWorkflow.getFormType()) {
			ContractProduct contractProduct = refundWorkflow.getRefundContractProduct();
			contractProduct.setIsFrozen(0);
			contractProductDao.merge(contractProduct);
		} else {
			StudnetAccMv studnetAccMv = studnetAccMvDao.getStudnetAccMvByStudentId(refundWorkflow.getRefundStudent().getId());
			BigDecimal frozenAmount = studnetAccMv.getFrozenAmount() != null ? studnetAccMv.getFrozenAmount() : BigDecimal.ZERO;
			studnetAccMv.setFrozenAmount(frozenAmount.add(refundWorkflow.getBasicOperateAmount()));
			studnetAccMvDao.merge(studnetAccMv);
		}
	}
	
	// 检查是否符合提交条件
	private void checkCanApplyFor(RefundWorkflow refundWorkflow) {
		String refundWorkflowId = refundWorkflow.getId();
		log.info("判断工作流申请是否符合提交条件," + refundWorkflowId);
		if (refundWorkflow.getFormType() == RefundFormType.MONEY_REFUND) {
			ContractProduct contractProduct = refundWorkflow.getRefundContractProduct();
			BigDecimal remainingAmountOfBasicAmount = contractProduct.getRemainingAmountOfBasicAmount();
			BigDecimal remainingAmountOfPromotionAmount = contractProduct.getRemainingAmountOfPromotionAmount();
			if (remainingAmountOfBasicAmount.compareTo(refundWorkflow.getBasicOperateAmount()) < 0) {
				log.info("基本账户退费金额不可以大于最大可退金额," + refundWorkflowId);
				throw new ApplicationException("基本账户退费金额不可以大于最大可退金额！");
			}
			if (remainingAmountOfPromotionAmount.compareTo(refundWorkflow.getPromotionOperateAmount()) < 0) {
				log.info("赠送账户退费金额不可以大于最大可退金额," + refundWorkflowId);
				throw new ApplicationException("赠送账户退费金额不可以大于最大可退金额！");
			}
			if (StringUtils.isNotBlank(contractProduct.getId()) && contractProduct.getIsFrozen() == 0) {
				log.info("合同产品已经冻结，请不要重复提交申请," + refundWorkflowId);
				throw new ApplicationException("合同产品已经冻结，请不要重复提交申请！");
			}
		} else {
			StudnetAccMv studnetAccMv = studnetAccMvDao.getStudnetAccMvByStudentId(refundWorkflow.getRefundStudent().getId());
			BigDecimal remainingAmount = studnetAccMv.getElectronicAccount();//电子账户金额
			BigDecimal frozenAmount = studnetAccMv.getFrozenAmount() != null? studnetAccMv.getFrozenAmount() : BigDecimal.ZERO ;
			BigDecimal basicOperateAmount = refundWorkflow.getBasicOperateAmount();
			if (remainingAmount.subtract(frozenAmount).compareTo(basicOperateAmount) < 0) {
				log.info("转账金额不可以大于电子账户最大可用金额," + refundWorkflowId);
				throw new ApplicationException("转账金额不可以大于电子账户最大可用金额！");
			}
		}
	}
	
	// 开始审批流程
	private long initializeWorkFlow(RefundWorkflow refundVo ,RefundAuditDynamicVo refundAuditDynamicVo) {
		String workflowName=getWorkFlowName(refundVo);
		long workflowId = -1;
		if(refundVo.getFlowId()==0){
			Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
			try {
				workflowId = workflow.initialize(workflowName, 0, null);
				List<Step> list = workflow.getCurrentSteps(workflowId);
				StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflowName).getStep(list.get(0).getStepId());
				refundAuditDynamicVo.setStepName(stepDescriptor.getName());
				workflow.doAction(workflowId, 1, null);
			} catch (InvalidActionException | WorkflowException e) {
				e.printStackTrace();
			}
		}else{
			try {
			workflowId=refundVo.getFlowId();
			Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
			List<Step> list = workflow.getCurrentSteps(workflowId);
			StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflowName).getStep(list.get(0).getStepId());
			if (!this.checkAuditAuthority(refundVo.getId())) {
			    throw new ApplicationException("您没有审核权限");
			}
			refundAuditDynamicVo.setStepName(stepDescriptor.getName());
				workflow.doAction(workflowId, refundAuditDynamicVo.getActionId(), null);//执行提交操作
			} catch (InvalidInputException e) {
				e.printStackTrace();
			} catch (WorkflowException e) {
				e.printStackTrace();
			}
		}
		return workflowId;
	}
	
	private String getRefundTitle(RefundWorkflow refundWorkflow) {
		String refundTitle = "";
		Organization refundCampus = organizationDao.findById(refundWorkflow.getRefundCampus().getId());
		refundTitle += refundCampus.getName();
		Student refundStudent = studentDao.findById(refundWorkflow.getRefundStudent().getId());
		refundTitle += refundStudent.getName();
		RefundFormType formType = refundWorkflow.getFormType();
		refundTitle += formType.getName() + "申请";
		return refundTitle;
	}
	
	
	@Override
	public void unlockStudentAccount(String workFlowId) {
		System.out.println("解冻吧，");
	}
	
	/**
     * 查找当前步骤的用户
     */
    public boolean checkAuditAuthority(String refundWorkflowId) {
        boolean result = false;
        RefundWorkflow refundWorkflow = refundWorkflowDao.findById(refundWorkflowId);
        long workflowId = refundWorkflow.getFlowId();
        if (workflowId > 0 ) {
            Workflow workflow = (Workflow) ApplicationContextUtil.getContext().getBean("workflow");
            List<Step> list = workflow.getCurrentSteps(refundWorkflow.getFlowId());
            StepDescriptor stepDescriptor = workflow.getWorkflowDescriptor(workflow.getWorkflowName(workflowId)).getStep(list.get(0).getStepId());
            Map<String,String> stepParams=stepDescriptor.getMetaAttributes();//获取步骤的参数
            String[] steps = getStepParamById(stepParams, "authority");//获取提交动作的参数
            if (steps.length == 2) {
                String jobCode = steps[0];
                String orgType = steps[1];
                int goStep =  list.get(0).getStepId();
                String campusId = null;
                campusId=refundWorkflow.getRefundCampus().getId();
                if (refundWorkflow.getFormType() == RefundFormType.ACCOUNT_TRANSFER) {
                    if(goStep==3){//第二步是操作校区，其他的是转入校区
                        campusId=refundWorkflow.getIntoCampus().getId();
                    }
                }
                List<Map<String, String>> userList = userService.getUsersByJobCode(jobCode, orgType, campusId);
                for (Map<String, String> map : userList) {
                    if (map.get("userId").equals(userService.getCurrentLoginUser().getUserId())) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }
	
}
