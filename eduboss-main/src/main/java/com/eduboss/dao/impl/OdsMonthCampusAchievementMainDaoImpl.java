package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.dao.OdsMonthCampusAchievementMainDao;
import com.eduboss.domain.OdsMonthCampusAchievementMain;
import com.google.common.collect.Maps;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class OdsMonthCampusAchievementMainDaoImpl extends GenericDaoImpl<OdsMonthCampusAchievementMain, String> implements OdsMonthCampusAchievementMainDao {

	@Override
	public List findInfoByMonth(String yearMonth,String type,String campusId) {
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT ");
		if(StringUtils.isNotBlank(type) && "GROUNP".equals(type)){
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     '' campusId,");
			sql.append("     '' campusName,");
		}else{
			sql.append(" `id`,");
			sql.append("     `group_id` groupId,");
			sql.append("     `group_name` groupName,");
			sql.append("     `branch_id` branchId,");
			sql.append("     `branch_name` branchName,");
			sql.append("     `campus_id` campusId,");
			sql.append("     `campus_name` campusName,");
		}
		
		
		sql.append("     sum(`new_money`) newMoney,");
		sql.append("     sum(`re_money`) reMoney,");
		sql.append("     sum(`all_money`) allMoney,");
		sql.append("     sum(`wash_money`) washMoney,");
		sql.append("     sum(`total_money`) totalMoney,");
		sql.append("     sum(`bonus_money`) bonusMoney,");
		sql.append("     sum(`refund_money`) refundMoney,");
		sql.append("     sum(`special_refund_money`) specialRefundMoney,");
		sql.append("     sum(`total_refund_money`) totalRefundMoney,");
		sql.append("     sum(`refund_bonus_money`) refundBonusMoney,");
		sql.append("     `receipt_month` receiptMonth,");
		sql.append("     `receipt_date` receiptDate,");
		sql.append("     `receipt_status` receiptStatus,");
		sql.append("     `remark`,");
		sql.append("     `modify_time` modifyTime,");
		sql.append("     `modify_user` modifyUser,");
		sql.append("     sum(`total_finace`) totalFinace,");
		sql.append("     sum(`total_bonus`) totalBonus,");
		sql.append("     sum(`modify_money`) modifyMoney,");
		sql.append("     sum(`after_modify_money`) afterModifyMoney");
		sql.append(" FROM `ods_month_payment_receipt_main` where 1=1 ");
		
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(yearMonth)){
			sql.append(" and receipt_Month= :yearMonth ");
			params.put("yearMonth", yearMonth);
		}
		
		if(StringUtils.isNotBlank(type) && "GROUNP".equals(type)){
			sql.append(" group by branch_id");
		}else{
			sql.append(" group by campus_id");
		}
		
		
		return 	this.findMapBySql(sql.toString(),params);
	}


	@Override
	public List<Map<String,String>> findPaymentRecieptMainByMonth(String yearMonth,String receiptStatus) {
		StringBuffer sql=new StringBuffer();
		sql.append(" SELECT ");
		sql.append("     `campus_id` campusId,");
		sql.append("     `receipt_month` receiptMonth ");
		sql.append(" FROM `ods_month_payment_receipt_main` where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(yearMonth)){
			sql.append(" and receipt_Month= :yearMonth ");
			params.put("yearMonth", yearMonth);
		}
		if(StringUtils.isNotBlank(receiptStatus)){
			sql.append(" and receipt_status= :receiptStatus ");
			params.put("receiptStatus", receiptStatus);
		}

		sql.append(" group by campus_id");


		List<Map<Object, Object>> list =this.findMapBySql(sql.toString(),params);
		List<Map<String, String>> result = new ArrayList<>();
		for(Map<Object, Object> map:list){
			Map<String, String> key =(Map)map;
			result.add(key);
		}
		return result;
	}
	
	@Override
	public List<Map<String, String>> getCampusFundsAuditRate(String campusId,
			String channel, String receiptMonth) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select fun.CHANNEL,");
		sql.append(" sum(case when fun.AUDIT_STATUS='UNAUDIT' then 1 else 0 end) unAudit,");
		sql.append(" sum(case when fun.AUDIT_STATUS<>'UNAUDIT' then 1 else 0 end) audit ");
		sql.append(" from funds_change_history fun");
		sql.append(" where 1=1 AND fun.CHANNEL in('CASH','POS','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER')  ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(campusId)){
			sql.append(" and fun.fund_campus_id = :campusId ");
			params.put("campusId", campusId);
		}
		if(StringUtils.isNotBlank(channel)){
			sql.append(" and fun.channel = :channel ");
			params.put("channel", channel);
		}
		
		if(StringUtils.isNotBlank(receiptMonth)){
			sql.append(" and (fun.TRANSACTION_TIME like :receiptMonth or (fun.receipt_time like :receiptMonth and fun.receipt_status=0))  ");
			params.put("receiptMonth", receiptMonth+"%");
		}
		
		sql.append(" group by fun.CHANNEL");
		
		List<Map<Object, Object>> list =this.findMapBySql(sql.toString(),params);
		List<Map<String, String>> result = new ArrayList<>();
		for(Map<Object, Object> map:list){
			Map<String, String> key =(Map)map;
			result.add(key);
		}
		return result;
	}


	@Override
	public void updateRecieptMainStudentAuditStatus(String receiptMonth, String campusId,
			EvidenceAuditStatus nextStatus) {
		// TODO Auto-generated method stub
		Map<String, Object> params = Maps.newHashMap();
		params.put("auditStatus", nextStatus.getValue());
		params.put("campusId", campusId);
		params.put("countDate", receiptMonth);
		String sql = " UPDATE ods_month_payment_receipt_main_student SET receipt_status = :auditStatus WHERE campus_id = :campusId AND receipt_month = :countDate ";
		super.excuteSql(sql,params);
	}

}
