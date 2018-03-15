package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ArrangeType;
import com.eduboss.common.PayType;
import com.eduboss.dao.MoneyArrangeLogDao;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.MoneyArrangeLog;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("MoneyArrangeLogDao")
public class MoneyArrangeLogDaoImpl extends GenericDaoImpl<MoneyArrangeLog, String> implements MoneyArrangeLogDao {

	private static final Logger log = LoggerFactory.getLogger(MoneyArrangeLogDaoImpl.class);
	// property constants

	@Override
	public void saveOneRecord(ContractProduct contractProduct, Student student, 
			BigDecimal assignAmount, User currentLoginUser, String remark, PayType payType) {
		MoneyArrangeLog moneyArrangeLog =  new MoneyArrangeLog();
		moneyArrangeLog.setChangeTime(DateTools.getCurrentDateTime());
		moneyArrangeLog.setChangeUser(currentLoginUser);
		moneyArrangeLog.setStudent(student);
		moneyArrangeLog.setChangeAmount(assignAmount);
		moneyArrangeLog.setAfterAmount(contractProduct.getPaidAmount());
		moneyArrangeLog.setContractProduct(contractProduct);
		moneyArrangeLog.setRemark(remark);
		if (StringUtil.isNotBlank(remark)) {
			if (remark.equals("分配")) {
				moneyArrangeLog.setArrangeType(ArrangeType.DISTRIBUTION);
			} else {
				moneyArrangeLog.setArrangeType(ArrangeType.EXTRACT);
			}
		}
		moneyArrangeLog.setPayType(payType);
		super.save(moneyArrangeLog);
	}

	@Override
	public int getNumberOfRecords(String contractProductId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("contractProductId", contractProductId);
		StringBuffer hql = new StringBuffer(); 
		hql.append("select count(*) from MoneyArrangeLog as moneyArrangeLog where contractProduct.id = :contractProductId ");
		return this.findCountHql(hql.toString(),params);
	}

	@Override
	public List<MoneyArrangeLog> findAllRecordsByContractProductId(
			String contractProductId) {
		StringBuffer hql = new StringBuffer(); 
		Map<String, Object> params = Maps.newHashMap();
		params.put("contractProductId", contractProductId);
		hql.append("from MoneyArrangeLog as moneyArrangeLog where contractProduct.id = :contractProductId ");
		return this.findAllByHQL(hql.toString(),params);
	}
	
}
