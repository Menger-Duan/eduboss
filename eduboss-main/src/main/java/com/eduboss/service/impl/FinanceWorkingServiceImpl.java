package com.eduboss.service.impl;

import java.math.BigDecimal;

import com.eduboss.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.eduboss.dao.FinanceBrenchDao;
import com.eduboss.dao.FinanceCampusDao;
import com.eduboss.dao.FinanceUserDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.FinanceMessage;
import com.eduboss.service.FinanceWorkingService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

@Service("FinanceWorkingService")
public class FinanceWorkingServiceImpl implements FinanceWorkingService {

	@Autowired
	private FinanceBrenchDao financeBrenchDao;
	
	@Autowired
	private FinanceCampusDao financeCampusDao;
	
	@Autowired
	private FinanceUserDao financeUserDao;
	

	@Autowired
	private OrganizationDao organizationDao;
	

	@Autowired
	private UserService userService;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Override
	public synchronized void saveFinanceByQueue(FinanceMessage message) {
		
		if(StringUtils.isBlank(message.getCountDate())){
			throw new ApplicationException("参数必须");
		}
		
		if(StringUtils.isNotBlank(message.getCampusId())){//校区名字
			Organization or = organizationDao.findById(message.getCampusId());
			Organization brench=userService.getBelongBranchByCampusId(message.getCampusId());
			if(or!=null){
				message.setCampusName(or.getName());
			}
			if(brench!=null){
				message.setBrenchId(brench.getId());
				message.setBrenchName(brench.getName());
			}
		}
		
		saveFinanceBrench(message);
		saveFinanceCampus(message);
		if(StringUtils.isNotBlank(message.getUserId())){
			saveFinanceUser(message);
		}
		
	}
	
	public synchronized void saveFinanceBrench(FinanceMessage messge){
		FinanceBrench brench=financeBrenchDao.findInfoByBrenchAndDate(messge.getBrenchId(),messge.getCountDate());
		brench=getNewFinanceBrench(brench, messge);
		financeBrenchDao.save(brench);
		financeBrenchDao.flush();
	}
	
	public synchronized FinanceBrench getNewFinanceBrench(FinanceBrench brench,FinanceMessage messge){
		if(brench==null){
			brench=new FinanceBrench();
			brench.setBrenchId(messge.getBrenchId());
			brench.setBrenchName(messge.getBrenchName());
			brench.setCountDate(messge.getCountDate());
			brench.setCreateTime(DateTools.getCurrentDateTime());
		}
		setAmountByFinance(brench,messge);
		return brench;
	}

	public void setAmountByFinance(Finance f,FinanceMessage messge){
		f.setModifyTime(DateTools.getCurrentDateTime());
		if(messge.getFlag().equals("1")){
			f.setCountPaidTotalAmount(f.getCountPaidTotalAmount().add(messge.getTotalMoney()));
			f.setCountPaidCashAmountNew(f.getCountPaidCashAmountNew().add(messge.getNewMoney()));
			f.setCountPaidCashAmountPre(f.getCountPaidCashAmountPre().add(messge.getPreMoney()));
			f.setReturnFee(f.getReturnFee().add(messge.getRefundMoney()));
			if(messge.getOnline()){
				f.setOnlineAmount(f.getOnlineAmount().add(messge.getTotalMoney()));
			}else{
				f.setLineAmount(f.getLineAmount().add(messge.getTotalMoney()));
			}
		}else{
			f.setCountPaidTotalAmount(f.getCountPaidTotalAmount().subtract(messge.getTotalMoney()));
			f.setCountPaidCashAmountNew(f.getCountPaidCashAmountNew().subtract(messge.getNewMoney()));
			f.setCountPaidCashAmountPre(f.getCountPaidCashAmountPre().subtract(messge.getPreMoney()));
			f.setReturnFee(f.getReturnFee().subtract(messge.getRefundMoney()));
			if(messge.getOnline()){
				f.setOnlineAmount(f.getOnlineAmount().subtract(messge.getTotalMoney()));
			}else{
				f.setLineAmount(f.getLineAmount().subtract(messge.getTotalMoney()));
			}
		}
	}
	
	public synchronized void saveFinanceCampus(FinanceMessage messge){
		FinanceCampus campus=financeCampusDao.findInfoByCampusAndDate(messge.getCampusId(),messge.getCountDate());
		campus=getNewFinanceCampus(campus, messge);
		financeCampusDao.save(campus);
		financeCampusDao.flush();
	}
	
	public synchronized FinanceCampus getNewFinanceCampus(FinanceCampus campus,FinanceMessage messge){
		if(campus==null){
			campus=new FinanceCampus();
			campus.setCampusId(messge.getCampusId());
			campus.setCampusName(messge.getCampusName());
			campus.setCountDate(messge.getCountDate());
			campus.setCreateTime(DateTools.getCurrentDateTime());
		}
		setAmountByFinance(campus,messge);
		return campus;
	}
	
	public synchronized void saveFinanceUser(FinanceMessage messge){
		FinanceUser user=financeUserDao.findInfoByUserAndDate(messge.getUserId(),messge.getCountDate(),messge.getCampusId());
		user=getNewFinanceUser(user, messge);
		financeUserDao.save(user);
		financeUserDao.flush();
	}
	
	public synchronized FinanceUser getNewFinanceUser(FinanceUser user,FinanceMessage messge){
		if(user==null){
			user=new FinanceUser();
			user.setCampusId(messge.getCampusId());
			user.setCampusName(messge.getCampusName());
			user.setUserId(messge.getUserId());
			user.setUserName(messge.getUserName());
			user.setCountDate(messge.getCountDate());
			user.setCreateTime(DateTools.getCurrentDateTime());
		}
		setAmountByFinance(user,messge);
		return user;
	}
}
