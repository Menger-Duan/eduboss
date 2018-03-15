package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.OdsMonthPaymentRecieptDao;
import com.eduboss.dao.OdsMonthPaymentRecieptMainDao;
import com.eduboss.dao.OdsPaymentReceiptModifyDao;
import com.eduboss.dao.ReportDao;
import com.eduboss.domain.OdsMonthPaymentRecieptMain;
import com.eduboss.domain.OdsPaymentReceiptModify;
import com.eduboss.domain.User;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptMainPrintVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptMainVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptVo;
import com.eduboss.domainVo.OdsPaymentRecieptModifyVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OdsMonthPaymentRecieptService;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service("OdsMonthPaymentRecieptService")
public class OdsMonthPaymentRecieptServiceImpl implements OdsMonthPaymentRecieptService {

	@Autowired
	private OdsMonthPaymentRecieptDao odsMonthPaymentRecieptDao;
	
	@Autowired
	private OdsMonthPaymentRecieptMainDao odsMonthPaymentRecieptMainDao;
	

	@Autowired
	private OdsPaymentReceiptModifyDao odsPaymentReceiptModifyDao;
	

	@Autowired
	private UserService userService;
	
	@Autowired OperationCountService operationCountService;
	

	@Autowired ReportDao reportDao;

	@Override
	public List<OdsMonthPaymentRecieptVo> findPaymentRecieptByMonth(
			String yearMonth,String campusId) {
		return HibernateUtils.voListMapping(odsMonthPaymentRecieptDao.findInfoByMonth(yearMonth,campusId), OdsMonthPaymentRecieptVo.class);
	}
	
	
	@Override
	public List findPaymentRecieptMainByMonth(BasicOperationQueryVo searchVo) {
		List list = reportDao.findInfoByMonth(searchVo);
		return list;
	}

	@Override
	public List<Map<String,String>> findPaymentRecieptMainByMonth(String yearMonth,
																  String evidenceAuditStatus) {
		return odsMonthPaymentRecieptMainDao.findPaymentRecieptMainByMonth(yearMonth,evidenceAuditStatus);
	}
	
	@Override
	public List<OdsPaymentRecieptModifyVo> findInfoByMainId(String mainId) {
		return HibernateUtils.voListMapping(odsPaymentReceiptModifyDao.findInfoByMainId(mainId),OdsPaymentRecieptModifyVo.class);
	}


	@Override
	public Response saveModifyInfo(
			OdsPaymentReceiptModify modifyInfo) {
		BigDecimal amount=BigDecimal.ZERO;
		if(modifyInfo.getId()!=0){
			Map<String, Object> params = new HashMap<String, Object>();
			String hql =" from OdsPaymentReceiptModify where id = :id ";
			params.put("id", modifyInfo.getId());
			OdsPaymentReceiptModify oldModifyInfo = odsPaymentReceiptModifyDao.findOneByHQL(hql, params);
			amount=modifyInfo.getAmount().subtract(oldModifyInfo.getAmount());
			odsPaymentReceiptModifyDao.merge(modifyInfo);
			odsPaymentReceiptModifyDao.save(oldModifyInfo);
		}else{
			amount=modifyInfo.getAmount();
			odsPaymentReceiptModifyDao.save(modifyInfo);
		}
		updateMainInfo(modifyInfo.getReceiptMainId(), amount);
		return new Response();
	}

	
	/** 
	 * 更新主表信息
	* @param mainId
	* @param amount
	* @author  author :Yao 
	* @date  2016年9月22日 下午3:46:32 
	* @version 1.0 
	*/
	public void updateMainInfo(String mainId,BigDecimal amount){
		OdsMonthPaymentRecieptMain mainInfo = odsMonthPaymentRecieptMainDao.findById(mainId);
		if(amount!=null){
			mainInfo.setModifyMoney(mainInfo.getModifyMoney().add(amount));
			if(mainInfo.getAfterModifyMoney()==null || mainInfo.getAfterModifyMoney().compareTo(BigDecimal.ZERO)==0){
				mainInfo.setAfterModifyMoney(mainInfo.getTotalFinace().add(amount));
			}else{
				mainInfo.setAfterModifyMoney(mainInfo.getAfterModifyMoney().add(amount));
			}
			mainInfo.setModifyTime(DateTools.getCurrentDateTime());
			mainInfo.setModifyUser(userService.getCurrentLoginUser().getUserId());
		}
		odsMonthPaymentRecieptMainDao.save(mainInfo);
	}

	@Override
	public Response deleteModifyInfo(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql =" from OdsPaymentReceiptModify where id = :id ";
		params.put("id", Integer.parseInt(id));
		OdsPaymentReceiptModify modifyInfo = odsPaymentReceiptModifyDao.findOneByHQL(hql, params);
		odsPaymentReceiptModifyDao.delete(modifyInfo);
		updateMainInfo(modifyInfo.getReceiptMainId(), BigDecimal.ZERO.subtract(modifyInfo.getAmount()==null ? BigDecimal.ZERO :modifyInfo.getAmount()));
		return new Response();
	}
	
	
	@Override
	public OdsMonthPaymentRecieptMainVo findMainInfoById(String id) {
		return HibernateUtils.voObjectMapping(odsMonthPaymentRecieptMainDao.findById(id), OdsMonthPaymentRecieptMainVo.class);
	}
	
	private boolean checkCanAuditOrFlush(String evidenceId) {
		OdsMonthPaymentRecieptMain lastEvidence = odsMonthPaymentRecieptMainDao.findById(evidenceId);
		if (lastEvidence != null && lastEvidence.getReceiptStatus() != EvidenceAuditStatus.FINANCE_END_AUDITED) {
			return false;
		} 
		return true;
	}
	
	@Override
	public void auditPaymentReciept(String id,EvidenceAuditStatus status) {
		OdsMonthPaymentRecieptMain evidence = odsMonthPaymentRecieptMainDao.findById(id);
		
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
			 odsMonthPaymentRecieptMainDao.save(evidence);
			 Map<String, Object> params = new HashMap<String, Object>();
			 odsMonthPaymentRecieptDao.excuteHql(" update OdsMonthPaymentReciept set receiptStatus='"+nextStatus+"' where receiptMonth='"+evidence.getReceiptMonth()+"' and campus.id= '"+evidence.getCampusId()+"'", new HashMap<String, Object>());
			 odsMonthPaymentRecieptMainDao.updateRecieptMainStudentAuditStatus(evidence.getReceiptMonth(),evidence.getCampusId(),nextStatus);
		 }
	}
	
	@Override
	public void rollbackPaymentReciept(String id) {
		OdsMonthPaymentRecieptMain evidence = odsMonthPaymentRecieptMainDao.findById(id);
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
		 odsMonthPaymentRecieptMainDao.save(evidence);
		 odsMonthPaymentRecieptDao.excuteHql(" update OdsMonthPaymentReciept set receiptStatus='"+EvidenceAuditStatus.NOT_AUDIT+"' where receiptMonth='"+evidence.getReceiptMonth()+"' and campus.id= '"+evidence.getCampusId()+"'", new HashMap<String, Object>());
		 odsMonthPaymentRecieptMainDao.updateRecieptMainStudentAuditStatus(evidence.getReceiptMonth(),evidence.getCampusId(),EvidenceAuditStatus.NOT_AUDIT);
	}
	
	
	@Override
	public void flushInfoById(String id) {
		OdsMonthPaymentRecieptMain info = odsMonthPaymentRecieptMainDao.findById(id);
//		 if (info.getReceiptStatus() == EvidenceAuditStatus.FINANCE_END_AUDITED) {
//			 throw new ApplicationException("该校区的现金流凭证已终审，不可以再次刷新");
//		 }
		try {
			String nextMonth = DateTools.getNextMonth(info.getReceiptMonth() + "-01");
			nextMonth = nextMonth.replaceAll("-", "");
			String nextEvidenceId = info.getCampusId() + nextMonth;
			
			OdsMonthPaymentRecieptMain nextEvidence  = odsMonthPaymentRecieptMainDao.findById(nextEvidenceId);
			if (nextEvidence!=null) {
				throw new ApplicationException("该校区的现金流凭证已有新月份凭证数据，不能再对旧月份数据刷新");
			}
			
			String lastMonth = DateTools.getLastMonth(info.getReceiptMonth() + "-01");
			lastMonth = lastMonth.replaceAll("-", "");
			String lastEvidenceId = info.getCampusId() + lastMonth;
			if (!checkCanAuditOrFlush(lastEvidenceId)) {
				throw new ApplicationException("该校区的现金流凭证尚有历史月份未终审，请终审后才对新月份凭证进行刷新与审核");
			}
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(info!=null)
			operationCountService.updateOdsPaymentReciept(info.getReceiptMonth(), info.getCampusId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public Response updatePaymentMainInfo(String id, String remark) {
		OdsMonthPaymentRecieptMain info=odsMonthPaymentRecieptMainDao.findById(id);
		info.setRemark(remark);
		info.setModifyTime(DateTools.getCurrentDateTime());
		info.setModifyUser(userService.getCurrentLoginUser().getUserId());
		odsMonthPaymentRecieptMainDao.save(info);
		return new Response();
	}
	
	/**
	 * 根据id查找OdsMonthPaymentRecieptMainPrintVo
	 */
	@Override
	public OdsMonthPaymentRecieptMainPrintVo findOdsMonthPaymentRecieptMainPrintById(String evidenceId) {
		return HibernateUtils.voObjectMapping(odsMonthPaymentRecieptMainDao.findById(evidenceId), OdsMonthPaymentRecieptMainPrintVo.class);
	}
	
	
	@Override
	public List<Map<String, String>> getCampusFundsAuditRate(String campusId,
			String channel, String receiptMonth) {
		List<Map<String, String>> list = odsMonthPaymentRecieptMainDao.getCampusFundsAuditRate(campusId,channel,receiptMonth);
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

	@Override
	public List<Map<Object, Object>> getPersonPaymentFinaceBonus(BasicOperationQueryVo searchVo) {
		return odsMonthPaymentRecieptDao.getPersonPaymentFinaceBonus(searchVo);
	}

	@Override
	public List getPaymentRecieptReportExcel(BasicOperationQueryVo searchVo) {

		List<Map<Object,Object>>  returnList=new ArrayList<Map<Object,Object>>();

		List<Map<Object,Object>> list=odsMonthPaymentRecieptDao.getPersonPaymentFinaceBonus(searchVo);

		for (Map<Object,Object> map :list){
			if(map.get("groupName") != null && StringUtils.isNotBlank(map.get("groupName").toString())){
				map.put("groupName",map.get("groupName").toString().split("_")[1]);
			}else{
				map.put("groupName","");
			}
			if(map.get("branchName") != null && StringUtils.isNotBlank(map.get("branchName").toString())){
				map.put("branchName",map.get("branchName").toString().split("_")[1]);
			}else{
				map.put("branchName","");
			}
			if(map.get("campusName") != null && StringUtils.isNotBlank(map.get("campusName").toString())){
				map.put("campusName",map.get("campusName").toString().split("_")[1]);
			}else{
				map.put("campusName","");
			}
			if(map.get("bonusStaffName") != null && StringUtils.isNotBlank(map.get("bonusStaffName").toString())){
				map.put("bonusStaffName",map.get("bonusStaffName").toString().split("_")[1]);
			}else{
				map.put("bonusStaffName","");
			}

			if(map.get("product_type") != null && StringUtils.isNotBlank(map.get("product_type").toString())){
				map.put("product_type", ProductType.valueOf(map.get("product_type").toString()).getName());
			}else{
				map.put("product_type","");
			}
			if(map.get("contract_type") != null && StringUtils.isNotBlank(map.get("contract_type").toString())){
				map.put("contract_type", ContractType.valueOf(map.get("contract_type").toString()).getName());
			}else{
				map.put("contract_type","");
			}
			returnList.add(map);
		}



		return returnList;
	}
}
