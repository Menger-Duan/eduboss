package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MoneyWashRecordsDao;
import com.eduboss.domain.MoneyWashRecords;
import com.google.common.collect.Maps;

@Repository("MoneyWashRecordsDao")
public class MoneyWashRecordsDaoImpl extends GenericDaoImpl<MoneyWashRecords, String> implements MoneyWashRecordsDao {

	/**
	 * 根据transactionId查找冲销详情记录
	 */
	@Override
	public MoneyWashRecords findByTransactionId(String transactionId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("transactionId", transactionId);
		String hql = " from MoneyWashRecords where transactionId = :transactionId ";
		List<MoneyWashRecords> list = super.findAllByHQL(hql,params);
		MoneyWashRecords record = null; 
		if (list.size() > 0) {
			record = list.get(0);
		}
		return record;
	}
	
	/**
	 * 根据courseId查询冲销详情
	 */
	@Override
	public MoneyWashRecords findMoneyWashRecordsByCourseId(String courseId) {
		MoneyWashRecords returnRecord = null;
		Map<String, Object> params = Maps.newHashMap();
		params.put("courseId", courseId);
		String sql = " select * from money_wash_records mwr left join ACCOUNT_CHARGE_RECORDS acr on mwr.TRANSACTION_ID = acr.TRANSACTION_ID ";
		sql += " where acr.COURSE_ID = :courseId and CHARGE_PAY_TYPE = 'WASH' order by WASH_TIME desc ";
		List<MoneyWashRecords> list = super.findBySql(sql,params);
		if (list.size() > 0) {
			returnRecord = list.get(0);
		}
		return returnRecord;
	}
	
	/**
	 * 根据miniCourseId查询冲销详情
	 */
	@Override
	public MoneyWashRecords findMoneyWashRecordsByMiniCourseId(String miniCourseId) {
		MoneyWashRecords returnRecord = null;
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniCourseId", miniCourseId);
		String sql = " select * from money_wash_records mwr left join ACCOUNT_CHARGE_RECORDS acr on mwr.TRANSACTION_ID = acr.TRANSACTION_ID ";
		sql += " where acr.MINI_CLASS_COURSE_ID = :miniCourseId and CHARGE_PAY_TYPE = 'WASH' order by WASH_TIME desc ";
		List<MoneyWashRecords> list = super.findBySql(sql,params);
		if (list.size() > 0) {
			returnRecord = list.get(0);
		}
		return returnRecord;
	}
	
	/**
	 * 根据otmCourseId查询冲销详情
	 */
	@Override
	public MoneyWashRecords findMoneyWashRecordsByOtmCourseId(String otmCourseId) {
		MoneyWashRecords returnRecord = null;
		Map<String, Object> params = Maps.newHashMap();
		params.put("otmCourseId", otmCourseId);
		String sql = " select * from money_wash_records mwr left join ACCOUNT_CHARGE_RECORDS acr on mwr.TRANSACTION_ID = acr.TRANSACTION_ID ";
		sql += " where acr.OTM_CLASS_COURSE_ID = :otmCourseId and CHARGE_PAY_TYPE = 'WASH' order by WASH_TIME desc ";
		List<MoneyWashRecords> list = super.findBySql(sql,params);
		if (list.size() > 0) {
			returnRecord = list.get(0);
		}
		return returnRecord;
	}
	
}
