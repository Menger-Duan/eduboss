package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.common.PayWay;
import com.eduboss.dao.OdsCampusAchievementModifyDao;
import com.eduboss.dao.OdsMonthCampusAchievementMainDao;
import com.eduboss.dao.OdsMonthCampusAchievementMainStudentDao;
import com.eduboss.dao.ReportDao;
import com.eduboss.domain.OdsCampusAchievementModify;
import com.eduboss.domain.OdsMonthCampusAchievementMain;
import com.eduboss.domain.User;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainPrintVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainStudentVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementModifyVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OdsMonthCampusAchievementService;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service("OdsMonthCampusAchievementService")
public class OdsMonthCampusAchievementServiceImpl implements OdsMonthCampusAchievementService {

	
	@Autowired
	private OdsMonthCampusAchievementMainStudentDao odsMonthCampusAchievementMainStudentDao;
	
	@Autowired
	private OdsMonthCampusAchievementMainDao odsMonthCampusAchievementMainDao;
	

	@Autowired
	private OdsCampusAchievementModifyDao odsCampusAchievementModifyDao;
	

	@Autowired
	private UserService userService;
	
	@Autowired OperationCountService operationCountService;
	

	@Autowired ReportDao reportDao;

	@Override
	public List<OdsMonthCampusAchievementMainStudentVo> findAchievementMainStudentByMonth(
			String yearMonth,String campusId) {
		return HibernateUtils.voListMapping(odsMonthCampusAchievementMainStudentDao.findInfoByMonth(yearMonth,campusId), OdsMonthCampusAchievementMainStudentVo.class);
	}
	
	
	@Override
	public List findCampusAchievementMainByMonth(BasicOperationQueryVo searchVo) {
		List list = reportDao.findCampusAchievementMainByMonth(searchVo);
		return list;
	}
	
	@Override
	public List<OdsMonthCampusAchievementModifyVo> findModifyInfoByMainId(String mainId) {
		return HibernateUtils.voListMapping(odsCampusAchievementModifyDao.findInfoByMainId(mainId),OdsMonthCampusAchievementModifyVo.class);
	}


	@Override
	public Response saveAchievementModifyInfo(
			OdsCampusAchievementModify modifyInfo) {
		BigDecimal amount=BigDecimal.ZERO;
		if(modifyInfo.getId()!=0){
			Map<String, Object> params = new HashMap<String, Object>();
			String hql =" from OdsCampusAchievementModify where id = :id ";
			params.put("id", modifyInfo.getId());
			OdsCampusAchievementModify oldModifyInfo = odsCampusAchievementModifyDao.findOneByHQL(hql, params);
			amount=modifyInfo.getAmount().subtract(oldModifyInfo.getAmount());
			odsCampusAchievementModifyDao.merge(modifyInfo);
			odsCampusAchievementModifyDao.save(oldModifyInfo);
		}else{
			amount=modifyInfo.getAmount();
			odsCampusAchievementModifyDao.save(modifyInfo);
		}
		updateMainInfo(modifyInfo.getAchievementMainId(), amount);
		return new Response();
	}

	
	/** 
	 * 更新主表信息
	 * @author: duanmenrun
	 * @Title: updateMainInfo 
	 * @Description: TODO 
	 * @param mainId
	 * @param amount
	 */
	public void updateMainInfo(String mainId,BigDecimal amount){
		OdsMonthCampusAchievementMain mainInfo = odsMonthCampusAchievementMainDao.findById(mainId);
		if(amount!=null){
			mainInfo.setModifyMoney(mainInfo.getModifyMoney().add(amount));
			if(mainInfo.getAfterModifyMoney()==null || mainInfo.getAfterModifyMoney().compareTo(BigDecimal.ZERO)==0){
				mainInfo.setAfterModifyMoney(mainInfo.getTotalBonus().add(amount));
			}else{
				mainInfo.setAfterModifyMoney(mainInfo.getAfterModifyMoney().add(amount));
			}
			mainInfo.setModifyTime(DateTools.getCurrentDateTime());
			mainInfo.setModifyUser(userService.getCurrentLoginUser().getUserId());
		}
		odsMonthCampusAchievementMainDao.save(mainInfo);
	}

	@Override
	public Response deleteAchievementModifyInfo(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql =" from OdsCampusAchievementModify where id = :id ";
		params.put("id", Integer.parseInt(id));
		OdsCampusAchievementModify modifyInfo = odsCampusAchievementModifyDao.findOneByHQL(hql, params);
		odsCampusAchievementModifyDao.delete(modifyInfo);
		updateMainInfo(modifyInfo.getAchievementMainId(), BigDecimal.ZERO.subtract(modifyInfo.getAmount()==null ? BigDecimal.ZERO :modifyInfo.getAmount()));
		return new Response();
	}
	
	
	@Override
	public OdsMonthCampusAchievementMainVo findAchievementMainInfoById(String id) {
		return HibernateUtils.voObjectMapping(odsMonthCampusAchievementMainDao.findById(id), OdsMonthCampusAchievementMainVo.class);
	}
	
	private boolean checkCanAuditOrFlush(String evidenceId) {
		OdsMonthCampusAchievementMain lastEvidence = odsMonthCampusAchievementMainDao.findById(evidenceId);
		if (lastEvidence != null && lastEvidence.getReceiptStatus() != EvidenceAuditStatus.FINANCE_END_AUDITED) {
			return false;
		} 
		return true;
	}
	
	@Override
	public void auditCampusAchievement(String id,EvidenceAuditStatus status) {
		OdsMonthCampusAchievementMain evidence = odsMonthCampusAchievementMainDao.findById(id);
		
		 if (evidence.getReceiptStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
			 throw new ApplicationException("该校区的校区业绩凭证已终审，不可以再次审核");
		 }
		 try {
			String lastMonth = DateTools.getLastMonth(evidence.getReceiptMonth() + "-01");
			lastMonth = lastMonth.replaceAll("-", "");
			String lastEvidenceId = evidence.getCampusId() + lastMonth;
			if (!checkCanAuditOrFlush(lastEvidenceId)) {
				throw new ApplicationException("该校区的校区业绩凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 User currentUser = userService.getCurrentLoginUser();
		 String currentTime = DateTools.getCurrentDateTime();
		 EvidenceAuditStatus nextStatus = null;
		 if (evidence.getReceiptStatus() == EvidenceAuditStatus.NOT_AUDIT) {
			 nextStatus =  EvidenceAuditStatus.CAMPUS_AUDITED;
			 evidence.setCampusConfirmUser(currentUser);
			 evidence.setCampusConfirmTime(currentTime);
		 } else if (evidence.getReceiptStatus() == EvidenceAuditStatus.CAMPUS_AUDITED) {
			 evidence.setFinanceFirstAuditUser(currentUser);
			 evidence.setFinanceFirstAuditTime(currentTime);
			 nextStatus =  EvidenceAuditStatus.FINANCE_FIRST_AUDITED;
		 } else if (evidence.getReceiptStatus() == EvidenceAuditStatus.FINANCE_FIRST_AUDITED) {
			 evidence.setBrenchConfirmUser(currentUser);
			 evidence.setBrenchConfirmTime(currentTime);
			 nextStatus =  EvidenceAuditStatus.BRENCH_AUDITED;
		 } else if (evidence.getReceiptStatus() == EvidenceAuditStatus.BRENCH_AUDITED) {
			 evidence.setFinanceEndAuditUser(currentUser);
			 evidence.setFinanceEndAuditTime(currentTime);
			 nextStatus =  EvidenceAuditStatus.FINANCE_END_AUDITED;
		 }
		 
		 if (status!=nextStatus) {
			 throw new ApplicationException("状态已不符合，请重新刷新页面再审核");
		 } else {
			 evidence.setCurrentAuditUser(currentUser);
			 evidence.setCurrentAuditTime(currentTime);
			 evidence.setReceiptStatus(nextStatus);
			 odsMonthCampusAchievementMainDao.save(evidence);
			 odsMonthCampusAchievementMainStudentDao.updateRecieptMainStudentAuditStatus(evidence.getReceiptMonth(),evidence.getCampusId(),nextStatus);
		 }
	}
	
	@Override
	public void rollbackPaymentReciept(String id) {
		OdsMonthCampusAchievementMain evidence = odsMonthCampusAchievementMainDao.findById(id);
		 if (evidence.getReceiptStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
			 throw new ApplicationException("该校区的现金流凭证已终审，不可以再次审核");
		 }
		try {
			String lastMonth = DateTools.getLastMonth(evidence.getReceiptMonth() + "-01");
			lastMonth = lastMonth.replaceAll("-", "");
			String lastEvidenceId = evidence.getCampusId() + lastMonth;
			if (!checkCanAuditOrFlush(lastEvidenceId)) {
				throw new ApplicationException("该校区的现金流凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		 evidence.setReceiptStatus(EvidenceAuditStatus.NOT_AUDIT);
		 odsMonthCampusAchievementMainDao.save(evidence);
		 odsMonthCampusAchievementMainStudentDao.updateRecieptMainStudentAuditStatus(evidence.getReceiptMonth(),evidence.getCampusId(),EvidenceAuditStatus.NOT_AUDIT);
	}
	
	
	@Override
	public void flushCampusAchievementById(String id) {
		OdsMonthCampusAchievementMain info = odsMonthCampusAchievementMainDao.findById(id);
//		 if (info.getReceiptStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
//			 throw new ApplicationException("该校区的现金流凭证已终审，不可以再次刷新");
//		 }
		try {
			String nextMonth = DateTools.getNextMonth(info.getReceiptMonth() + "-01");
			nextMonth = nextMonth.replaceAll("-", "");
			String nextEvidenceId = info.getCampusId() + "_" + nextMonth;
			
			OdsMonthCampusAchievementMain nextEvidence  = odsMonthCampusAchievementMainDao.findById(nextEvidenceId);
			if (nextEvidence!=null) {
				throw new ApplicationException("该校区的校区业绩凭证已有新月份凭证数据，不能再对旧月份数据刷新");
			}
			
			String lastMonth = DateTools.getLastMonth(info.getReceiptMonth() + "-01");
			lastMonth = lastMonth.replaceAll("-", "");
			String lastEvidenceId = info.getCampusId() + "_" + lastMonth;
			if (!checkCanAuditOrFlush(lastEvidenceId)) {
				throw new ApplicationException("该校区的校区业绩凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(info!=null)
			operationCountService.updateOdsCampusAchievement(info.getReceiptMonth(), info.getCampusId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Response updateAchievementMainInfo(String id, String remark) {
		OdsMonthCampusAchievementMain info=odsMonthCampusAchievementMainDao.findById(id);
		info.setRemark(remark);
		info.setModifyTime(DateTools.getCurrentDateTime());
		info.setModifyUser(userService.getCurrentLoginUser().getUserId());
		odsMonthCampusAchievementMainDao.save(info);
		return new Response();
	}
	
	/**
	 * 根据id查找审核记录
	 */
	@Override
	public OdsMonthCampusAchievementMainPrintVo findOdsMonthCampusAchievementMainPrintById(String evidenceId) {
		return HibernateUtils.voObjectMapping(odsMonthCampusAchievementMainDao.findById(evidenceId), OdsMonthCampusAchievementMainPrintVo.class);
	}
	
	
	@Override
	public List<Map<String, String>> getCampusFundsAuditRate(String campusId,
			String channel, String receiptMonth) {
		List<Map<String, String>> list = odsMonthCampusAchievementMainDao.getCampusFundsAuditRate(campusId,channel,receiptMonth);
		List<Map<String, String>> returnList=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : list) {
			Map<String,String> returnMap= new HashMap<String,String>();
			if(map.get("CHANNEL")!=null)
			map.put("channelName", PayWay.valueOf(map.get("CHANNEL")).getName());
			returnMap.putAll(map);
			returnList.add(returnMap);
		}
		
		return returnList;
	}

}
