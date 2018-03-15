package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.dao.OdsMonthRemainAmountCampusDao;
import com.eduboss.domain.OdsMonthRemainAmountCampus;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domainVo.OdsMonthRemainAmountCampusPrintVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OdsMonthRemainAmountCampusService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.UserDeptJobService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service
public class OdsMonthRemainAmountCampusServiceImpl implements OdsMonthRemainAmountCampusService {

	@Autowired
	private OdsMonthRemainAmountCampusDao odsMonthRemainAmountCampusDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDeptJobService userDeptJobService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 审批营收凭证
	 */
	@Override
	public void auditRemainEvidence(String evidenceId) {
		OdsMonthRemainAmountCampus evidence = odsMonthRemainAmountCampusDao.findById(evidenceId);
		 if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
			 throw new ApplicationException("该校区的剩余资金凭证已终审，不可以再次审核");
		 }
		 try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(evidence.getCountDate()));
			String lastEvidenceId = evidence.getCampusId() + "_" + lastMonthEndDate;
			if (!checkCanAuditOrFlush(lastEvidenceId)) {
				throw new ApplicationException("该校区的剩余资金凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 User currentUser = userService.getCurrentLoginUser();
		 String currentTime = DateTools.getCurrentDateTime();
		 List<UserDeptJob> userDeptJobList = userDeptJobService.findDeptJobByUserId(userService.getCurrentLoginUser().getUserId());
		 String jobId = "";
		 EvidenceAuditStatus nextStatus = null;
		 if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.NOT_AUDIT) {
//			 evidence.setCampusConfirmUser(currentUser);
//			 evidence.setCampusConfirmTime(currentTime);
//			 jobId = "xqpzshz";
//			 nextStatus =  EvidenceAuditStatus.CAMPUS_AUDITED;
//		 } else if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.CAMPUS_AUDITED) {
			 evidence.setFinanceFirstAuditUser(currentUser);
			 evidence.setFinanceFirstAuditTime(currentTime);
			 jobId = "pzcwcsz";
			 nextStatus =  EvidenceAuditStatus.FINANCE_FIRST_AUDITED;
		 } else if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.FINANCE_FIRST_AUDITED) {
			 evidence.setBrenchConfirmUser(currentUser);
			 evidence.setBrenchConfirmTime(currentTime);
			 jobId = "fgspzshz";
			 nextStatus =  EvidenceAuditStatus.BRENCH_AUDITED;
		 } else if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.BRENCH_AUDITED) {
			 evidence.setFinanceEndAuditUser(currentUser);
			 evidence.setFinanceEndAuditTime(currentTime);
			 jobId = "pzcwzsz";
			 nextStatus =  EvidenceAuditStatus.FINANCE_END_AUDITED;
		 }
		 boolean canAudit = false;
		 for (UserDeptJob userDeptJob : userDeptJobList) {
			 Organization org = organizationService.findById(userDeptJob.getDeptId());
			 String belongId = org.getBelong() != null ? org.getBelong() : userDeptJob.getDeptId();
			 if ((belongId.equals(evidence.getBrenchId()) && userDeptJob.getJobId().equals(jobId))
					 || (belongId.equals(evidence.getCampusId()) && userDeptJob.getJobId().equals(jobId))) {
				 canAudit = true;
				 break;
			 }
		 }
		 if (!canAudit) {
			 throw new ApplicationException("状态已不符合，请重新刷新页面再审核");
		 } else {
			 evidence.setCurrentAuditUser(currentUser);
			 evidence.setCurrentAuditTime(currentTime);
			 evidence.setEvidenceAuditStatus(nextStatus);
			 odsMonthRemainAmountCampusDao.merge(evidence);
			 odsMonthRemainAmountCampusDao.updateOdsMonthRemainStudentAuditStatus(evidence.getCampusId(), evidence.getCountDate(), nextStatus);
		 }
	}
	
	/**
	 * 审批不通过剩余资金凭证
	 * @param evidenceId
	 */
	@Override
	public void rollbackRemainEvidence(String evidenceId) {
		OdsMonthRemainAmountCampus evidence = odsMonthRemainAmountCampusDao.findById(evidenceId);
		 if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
			 throw new ApplicationException("该校区的剩余资金凭证已终审，不可以再次审核");
		 }
		 try {
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(evidence.getCountDate()));
			String lastEvidenceId = evidence.getCampusId() + "_" + lastMonthEndDate;
			if (!checkCanAuditOrFlush(lastEvidenceId)) {
				throw new ApplicationException("该校区的剩余资金凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
		 List<UserDeptJob> userDeptJobList = userDeptJobService.findDeptJobByUserId(userService.getCurrentLoginUser().getUserId());
		 String jobId = "";
		 if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.NOT_AUDIT) {
//			 jobId = "xqpzshz";
			 jobId = "pzcwcsz";
//		 } else if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.CAMPUS_AUDITED) {
//			 jobId = "pzcwcsz";
		 } else if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.FINANCE_FIRST_AUDITED) {
			 jobId = "fgspzshz";
		 } else if (evidence.getEvidenceAuditStatus() == EvidenceAuditStatus.BRENCH_AUDITED) {
			 jobId = "pzcwzsz";
		 }
		 boolean canAudit = false;
		 for (UserDeptJob userDeptJob : userDeptJobList) {
			 Organization org = organizationService.findById(userDeptJob.getDeptId());
			 String belongId = org.getBelong() != null ? org.getBelong() : userDeptJob.getDeptId();
			 if ((belongId.equals(evidence.getBrenchId()) && userDeptJob.getJobId().equals(jobId))
					 || (belongId.equals(evidence.getCampusId()) && userDeptJob.getJobId().equals(jobId))) {
				 canAudit = true;
				 break;
			 }
		 }
		 if (!canAudit) {
			 throw new ApplicationException("状态已不符合，请重新刷新页面再审核");
		 } else {
			 evidence.setCampusConfirmUser(null);
			 evidence.setCampusConfirmTime(null);
			 evidence.setFinanceFirstAuditUser(null);
			 evidence.setFinanceFirstAuditTime(null);
			 evidence.setBrenchConfirmUser(null);
			 evidence.setBrenchConfirmTime(null);
			 evidence.setFinanceEndAuditUser(null);
			 evidence.setFinanceEndAuditTime(null);
			 evidence.setCurrentAuditUser(null);
			 evidence.setCurrentAuditTime(null);
			 evidence.setEvidenceAuditStatus(EvidenceAuditStatus.NOT_AUDIT);
			 odsMonthRemainAmountCampusDao.merge(evidence);
			 odsMonthRemainAmountCampusDao.updateOdsMonthRemainStudentAuditStatus(evidence.getCampusId(), evidence.getCountDate(), EvidenceAuditStatus.NOT_AUDIT);
		 }
	}
	
	/**
	 * 检查营收凭证能否审批或刷新
	 */
	@Override
	public boolean checkCanAuditOrFlush(String evidenceId) {
		OdsMonthRemainAmountCampus lastEvidence = odsMonthRemainAmountCampusDao.findById(evidenceId);
		if (lastEvidence != null && lastEvidence.getEvidenceAuditStatus() != EvidenceAuditStatus.FINANCE_END_AUDITED) {
			return false;
		} 
		return true;
	}
	
	/**
	 * 根据id查找OdsMonthIncomeCampus
	 */
	@Override
	public OdsMonthRemainAmountCampus findOdsMonthRemainAmountCampusById(String evidenceId) {
		return odsMonthRemainAmountCampusDao.findById(evidenceId);
	}
	
	/**
	 * 根据id查找OdsMonthRemainAmountCampusPrintVo
	 */
	@Override
	public OdsMonthRemainAmountCampusPrintVo findOdsMonthRemainAmountCampusPrintById(String evidenceId) {
		return HibernateUtils.voObjectMapping(odsMonthRemainAmountCampusDao.findById(evidenceId), OdsMonthRemainAmountCampusPrintVo.class);
	}
	
}
