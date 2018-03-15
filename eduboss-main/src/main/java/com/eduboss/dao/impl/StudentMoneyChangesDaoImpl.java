package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentMoneyChangesDao;
import com.eduboss.domain.Role;
import com.eduboss.domain.StudentMoneyChanges;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;

@Repository("StudentMoneyChangesDao")
public class StudentMoneyChangesDaoImpl extends GenericDaoImpl<StudentMoneyChanges, String> implements StudentMoneyChangesDao{

	@Autowired
	private UserService userService;
	
	/**
	 * 获取学生资金变更记录
	 * */
	@Override
	public DataPackage getStudentMondyChanges(StudentMoneyChanges moneyChanges, Map map, DataPackage dataPackage) {
		String startTime = map.get("startTime").toString();
		String endTime = map.get("endTime").toString();
		StringBuilder accountHql = new StringBuilder(); // 扣费的SQL
		StringBuilder fundHql = new StringBuilder(); // 收费的SQL
		Map<String, Object> params = new HashMap<String, Object>();
		
		// 因为要 使用 到 两个表之间的查询 所以必要使用到 native sql
		accountHql.append("SELECT")
				.append("	'OUTCOME' AS `type`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`ID` AS `ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`PAY_TIME` AS `HAPPEN_TIME`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`STUDENT_ID` AS `STUDENT_ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`CONTRACT_ID` AS `CONTRACT_ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`CONTRACT_PRODUCT_ID` AS `CONTRACT_PRODUCT_ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`AMOUNT` AS `AMOUNT`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`PAY_CHANNEL` AS `CHANNEL`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`CHARGE_TYPE` AS `CHARGE_TYPE`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`PRODUCT_TYPE` AS `PRODUCT_TYPE`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`COURSE_ID` AS `COURSE_ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`MINI_CLASS_COURSE_ID` AS `MINI_CLASS_COURSE_ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`PROMISE_CLASS_RECORD_ID` AS `PROMISE_CLASS_RECORD_ID`,")
				.append("	`ACCOUNT_CHARGE_RECORDS`.`OTM_CLASS_COURSE_ID` AS `OTM_CLASS_COURSE_ID`,")
				.append("	NULL AS `FUNDS_PAY_TYPE`,")
				.append("	`account_charge_records`.CHARGE_PAY_TYPE AS `CHARGE_PAY_TYPE`,")
				.append("	`account_charge_records`.BL_CAMPUS_ID as campus_id , ")
				.append("	`account_charge_records`.OPERATE_USER_ID as operate_user_id ")
				.append("FROM")
				.append("	ACCOUNT_CHARGE_RECORDS ")
				.append(" inner join student on ACCOUNT_CHARGE_RECORDS.STUDENT_ID = student.id where 1 = 1 ")
				//.append(" inner join user on user.user_id = student.id  where 1 = 1 ")
				;
		
		fundHql.append("	SELECT ")
			.append("		'INCOME' AS `INCOME`,")
			.append("		`funds_change_history`.`ID` AS `ID`,")
			.append("		`funds_change_history`.`TRANSACTION_TIME` AS `TRANSACTION_TIME`,")
			.append("		`funds_change_history`.`STUDENT_ID` AS `STUDENT_ID`,")
			.append("		`funds_change_history`.`CONTRACT_ID` AS `CONTRACT_ID`,")
			.append("		NULL AS `NULL`,")
			.append("		`funds_change_history`.`TRANSACTION_AMOUNT` AS `TRANSACTION_AMOUNT`,")
			.append("		`funds_change_history`.`CHANNEL` AS `CHANNEL`,")
			.append("		`funds_change_history`.`CATEGORY` AS `CATEGORY`,")
			.append("		NULL AS `NULL`,")
			.append("		NULL AS `NULL`,")
			.append("		NULL AS `NULL`,")
			.append("		NULL AS `NULL`,")
			.append("		NULL AS `NULL`,")
			.append("		`funds_change_history`.`FUNDS_PAY_TYPE` AS `FUNDS_PAY_TYPE`,")
			.append("		NULL AS `CHARGE_PAY_TYPE`,")
			.append("		`funds_change_history`.fund_campus_id as campus_id ,")
			.append("		`funds_change_history`.CHARGE_USER_ID as operate_user_id ")
			.append("	FROM")
			.append("		funds_change_history ")
			.append(" inner join student on funds_change_history.STUDENT_ID = student.id where 1 = 1 ")
			;
		
		//根据学生ID查询
		if(!"".equals(map.get("studentId"))){
			accountHql.append(" and STUDENT_ID = :studentId1 ");
			params.put("studentId1", map.get("studentId"));
			fundHql.append(" and STUDENT_ID = :studentId2 ");
			params.put("studentId2", map.get("studentId"));
		}
		// 学生姓名 
		if( moneyChanges.getStudent() != null ){
			// 校区的查询
			if( moneyChanges.getStudent().getBlCampus() != null && moneyChanges.getStudent().getBlCampus().getId() != null && StringUtil.isNotBlank(moneyChanges.getStudent().getBlCampus().getId())){
				accountHql.append(" and student.bl_campus_id = :blCampusId1 ");
				params.put("blCampusId1", moneyChanges.getStudent().getBlCampus().getId());
				fundHql.append(" and student.bl_campus_id =  :blCampusId2 ");
				params.put("blCampusId2", moneyChanges.getStudent().getBlCampus().getId());
			}
			// 学管的查询
			if( moneyChanges.getStudent().getStudyManeger() != null && moneyChanges.getStudent().getStudyManeger().getUserId() != null && StringUtil.isNotBlank(moneyChanges.getStudent().getStudyManeger().getUserId())){
				accountHql.append(" and student.STUDY_MANEGER_ID = :studyManagerId1 ");
				params.put("studyManagerId1", moneyChanges.getStudent().getStudyManeger().getUserId());
				fundHql.append(" and student.STUDY_MANEGER_ID =  :studyManagerId2 ");
				params.put("studyManagerId2", moneyChanges.getStudent().getStudyManeger().getUserId());
			}
		}
		if( moneyChanges.getType() != null && StringUtil.isNotEmpty(moneyChanges.getType())){
			if ("OUTCOME".equals(moneyChanges.getType())) {
				fundHql.append(" and 1=2");
			} else {
				accountHql.append(" and 1=2 ");
			}
		}
		if( moneyChanges.getChannel()!=null && moneyChanges.getChannel().getValue()!=null && StringUtil.isNotEmpty(moneyChanges.getChannel().getValue())){
			accountHql.append(" and PAY_CHANNEL = :channel1 ");
			params.put("channel1", moneyChanges.getChannel().getValue());
			fundHql.append(" and CHANNEL = :channel2");
			params.put("channel2", moneyChanges.getChannel().getValue());
		}
		if( moneyChanges.getProductType() != null && moneyChanges.getProductType().getValue() != null && StringUtil.isNotEmpty(moneyChanges.getProductType().getValue())){
			accountHql.append(" and PRODUCT_TYPE = :productType ");
			params.put("productType", moneyChanges.getProductType().getValue());
			fundHql.append(" and 1=2");
		}
		if(!"".equals(startTime)){
			accountHql.append(" and PAY_TIME >= :startTime1 ");
			params.put("startTime1", startTime + " 00:00:00");
			fundHql.append(" and TRANSACTION_TIME >= :startTime2 ");
			params.put("startTime2", startTime + " 00:00:00");
		}
		if(!"".equals(endTime)){
			accountHql.append(" and PAY_TIME <= :endTime1 ");
			params.put("endTime1", endTime + " 23:59:59");
			fundHql.append(" and TRANSACTION_TIME <= :endTime2 ");
			params.put("endTime2", endTime + " 23:59:59");
		}
		List<Role> roles = userService.getCurrentLoginUserRoles();
		if (roles.size()==1 && roles.get(0).getRoleCode()!=null  && roles.get(0).getRoleCode().getValue().equals("STUDY_MANAGER")) {//学管师看自己的学生
			accountHql.append(" and student.STUDY_MANEGER_ID = :studyManagerId3 ");
			params.put("studyManagerId3", userService.getCurrentLoginUser().getUserId());
			fundHql.append(" and student.STUDY_MANEGER_ID = :studyManagerId4 ");
			params.put("studyManagerId4", userService.getCurrentLoginUser().getUserId());
		}
		String sql = accountHql.append(" union all ").append(fundHql).append(" order by HAPPEN_TIME desc").toString();
		dataPackage = this.findPageBySql(sql, dataPackage, false, params);		
		dataPackage.setRowCount(this.findCountSql("select count(*) from ("+sql+") as aa", params));
		return dataPackage;
	}
}
