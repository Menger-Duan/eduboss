package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.utils.StringUtil;
import org.springframework.stereotype.Repository;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.dao.OdsMonthIncomeCampusDao;
import com.eduboss.domain.OdsMonthIncomeCampus;
import com.google.common.collect.Maps;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("OdsMonthIncomeCampusDao")  
public class OdsMonthIncomeCampusDaoImpl extends GenericDaoImpl<OdsMonthIncomeCampus, String> implements OdsMonthIncomeCampusDao {

	
	/**
	 * 更新明细的审核状态
	 */
	public void updateOdsMonthIncomeStudentAuditStatus(String campusId, String countDate, EvidenceAuditStatus auditStatus) {
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("auditStatus", auditStatus.getValue());
		params.put("campusId", campusId);
		params.put("countDate", countDate);
		String sql = " UPDATE ods_month_income_student SET EVIDENCE_AUDIT_STATUS = :auditStatus WHERE CAMPUS_ID = :campusId AND COUNT_DATE = :countDate ";
		super.excuteSql(sql,params);
	}

	@Override
	public List<Map<String, String>> findOdsMonthIncomeCampusPrintByCountDate(String countDate, String status) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("countDate", countDate);
		params.put("status", status);
		sql.append("select campus_id campusId from ods_month_income_campus where COUNT_DATE= :countDate and  EVIDENCE_AUDIT_STATUS= :status ");
		List<Map<Object, Object>> list =this.findMapBySql(sql.toString(),params);
		List<Map<String, String>> result = new ArrayList<>();
		for(Map<Object, Object> map:list){
			Map<String, String> key =(Map)map;
			result.add(key);
		}
		return result;
	}

	
	
	@Override
	public List<Map<String, String>> findIncomeAuditRate(String campusId, String receiptDate,String type) {
		
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();

		if(type.equals("ONE_ON_ONE_COURSE")){
//			sql.append("  SELECT ");
//			sql.append("    ifnull(sum(case when c.AUDIT_STATUS <> 'UNAUDIT' then 1 else 0 end  ),0) audit,");
//			sql.append("    ifnull(sum(case when c.AUDIT_STATUS = 'UNAUDIT' then 1 else 0 end  ),0) unAudit");
//			sql.append(" FROM");
//			sql.append("     income_mapping_date imd");
//			sql.append(" 	LEFT JOIN  account_charge_records acr ON imd.ACCOUNT_CHARGE_RECORD_ID = acr.ID");
//			sql.append("     LEFT JOIN COURSE c on c.COURSE_ID= acr.COURSE_ID");
//			sql.append(" WHERE");
//			sql.append("     acr.PRODUCT_TYPE ='ONE_ON_ONE_COURSE' and acr.IS_WASHED='FALSE' and acr.CHARGE_PAY_TYPE='CHARGE'");
//			sql.append(" and imd.COUNT_MONTH='"+receiptDate+"' and imd.BL_CAMPUS_ID='"+campusId+"' and c.COURSE_STATUS<>'CANCEL' ");


			sql.append(" select ");
			sql.append(" ifnull(sum(case when COURSE_STATUS ='CHARGED' and AUDIT_STATUS ='VALIDATE'   then 1 else 0 end),0) audit,");
			sql.append(" ifnull(sum(case when COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and (AUDIT_STATUS is null or AUDIT_STATUS  <> 'VALIDATE') then 1 else 0 end),0) unAudit");
			sql.append("  from course   where COURSE_DATE like :receiptDate and COURSE_STATUS <> 'CANCEL' ");
			sql.append("  and BL_CAMPUS_ID= :campusId ");
			params.put("receiptDate", receiptDate+"%");
			params.put("campusId", campusId);
		}
		else if(type.equals("SMALL_CLASS")){
//				sql.append(" SELECT 	");
//				sql.append(" 				ifnull(sum(case when AUDIT_STATUS <> 'UNAUDIT' then 1 else 0 end  ),0) audit,");
//				sql.append(" 			   ifnull(sum(case when AUDIT_STATUS = 'UNAUDIT' then 1 else 0 end  ),0) unAudit");
//				sql.append("                 FROM");
//				sql.append("                     mini_class_course");
//				sql.append("                 WHERE MINI_CLASS_COURSE_ID  in(select acr.MINI_CLASS_COURSE_ID from ");
//				sql.append(" 			income_mapping_date imd");
//				sql.append("         LEFT JOIN");
//				sql.append("     account_charge_records acr ON imd.ACCOUNT_CHARGE_RECORD_ID = acr.ID");
//				sql.append(" WHERE");
//				sql.append("     acr.PRODUCT_TYPE ='SMALL_CLASS' and acr.IS_WASHED='FALSE' and acr.CHARGE_PAY_TYPE='CHARGE'");
//				sql.append(" AND imd.COUNT_MONTH = '"+receiptDate+"' AND imd.BL_CAMPUS_ID = '"+campusId+"') and COURSE_STATUS<>'CANCEL'");

				sql.append(" select ");
				sql.append(" ifnull(sum(case when mcc.COURSE_STATUS = 'CHARGED' then 1 else 0 end),0) audit,");
				sql.append(" count(distinct(mcc.mini_class_course_id)) unAudit");
				sql.append("  from mini_class_course mcc ,mini_class mc    where mcc.COURSE_DATE like :receiptDate and mcc.COURSE_STATUS <> 'CANCEL' and mcc.mini_class_id = mc.mini_class_id ");
				sql.append("  and mc.BL_CAMPUS_ID= :campusId ");
				params.put("receiptDate", receiptDate+"%");
				params.put("campusId", campusId);
		}else if(type.equals("ONE_ON_MANY")){
//					sql.append(" SELECT ");
//					sql.append("                    ifnull(sum(case when AUDIT_STATUS <> 'UNAUDIT' then 1 else 0 end  ),0) audit,");
//					sql.append("                    ifnull(sum(case when AUDIT_STATUS = 'UNAUDIT' then 1 else 0 end  ),0) unAudit");
//					sql.append("                 FROM");
//					sql.append("                     otm_class_course");
//					sql.append("                 WHERE OTM_CLASS_COURSE_ID  in(select acr.OTM_CLASS_COURSE_ID from ");
//					sql.append("     income_mapping_date imd");
//					sql.append("         LEFT JOIN");
//					sql.append("     account_charge_records acr ON imd.ACCOUNT_CHARGE_RECORD_ID = acr.ID");
//					sql.append(" WHERE");
//					sql.append("     acr.PRODUCT_TYPE ='ONE_ON_MANY' and acr.IS_WASHED='FALSE' and acr.CHARGE_PAY_TYPE='CHARGE'");
//					sql.append(" AND imd.COUNT_MONTH = '"+receiptDate+"' AND imd.BL_CAMPUS_ID = '"+campusId+"') and COURSE_STATUS<>'CANCEL'");
			sql.append(" select ");
			sql.append(" ifnull(sum(case when mcc.COURSE_STATUS ='CHARGED' and mcc.AUDIT_STATUS ='VALIDATE'  then 1 else 0 end),0) audit,");
			sql.append(" ifnull(sum(case when mcc.COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS  <> 'VALIDATE') then 1 else 0 end),0) unAudit");
			sql.append("   from otm_class_course mcc ,otm_class mc    where mcc.COURSE_DATE like :receiptDate and mcc.COURSE_STATUS <> 'CANCEL' and mcc.otm_class_id = mc.otm_class_id ");
			sql.append("  and mc.BL_CAMPUS_ID= :campusId ");
			params.put("receiptDate", receiptDate+"%");
			params.put("campusId", campusId);
		} else if(type.equals("TWO_TEACHER")){
            sql.append(" select ");
            sql.append(" count(distinct ttct.CLASS_TWO_ID, ttcc.course_id) total,");
            sql.append(" count(distinct (case when ttcsa.CHARGE_STATUS='CHARGED' THEN concat(ttct.CLASS_TWO_ID,ttcc.course_id) END)) audit ");
            sql.append("  from two_teacher_class_student_attendent ttcsa, two_teacher_class_course ttcc ,two_teacher_class_two ttct ");
            sql.append(" where ttcc.COURSE_DATE like :receiptDate and ttcc.COURSE_STATUS <> 'CANCEL' and ttcc.CLASS_ID = ttct.CLASS_ID ");
            sql.append(" AND ttcsa.TWO_CLASS_COURSE_ID = ttcc.COURSE_ID AND ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID ");
            sql.append("  and ttct.BL_CAMPUS_ID= :campusId ");
            params.put("receiptDate", receiptDate+"%");
            params.put("campusId", campusId);
		} else {
			return null;
		}
		List<Map<Object, Object>> list =this.findMapBySql(sql.toString(),params);
		List<Map<String, String>> result = new ArrayList<>();
		for(Map<Object, Object> map:list){
			Map<String, String> key =(Map)map;
			result.add(key);
		}
		return result;

	}

	@Override
	public List isFinishAudit(String campusId, String courseDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from ods_month_income_campus where EVIDENCE_AUDIT_STATUS='FINANCE_END_AUDITED' ");

		if(StringUtil.isNotBlank(courseDate)){
			sql.append(" and count_date = :courseDate ");
			params.put("courseDate", courseDate);
		}

		if(StringUtil.isNotBlank(campusId)){
			sql.append(" and campus_id='"+campusId+"'");
		}

		return this.findBySql(sql.toString(), params);
	}


}
