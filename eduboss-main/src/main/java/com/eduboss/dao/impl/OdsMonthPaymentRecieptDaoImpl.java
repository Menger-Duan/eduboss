package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.dao.OdsMonthPaymentRecieptDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.OdsMonthPaymentReciept;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class OdsMonthPaymentRecieptDaoImpl extends GenericDaoImpl<OdsMonthPaymentReciept, String> implements OdsMonthPaymentRecieptDao {


	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private UserService userService;
	
	@Override
	public List<OdsMonthPaymentReciept> findInfoByMonth(String yearMonth,String campusId) {
		StringBuffer hql=new StringBuffer();
		hql.append(" from OdsMonthPaymentReciept where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(yearMonth)){
			hql.append(" and receiptMonth= :yearMonth ");
			params.put("yearMonth", yearMonth);
		}

		if(StringUtils.isNotBlank(campusId)){
			Organization org=organizationDao.findById(campusId);
			hql.append(" and (campus.id = :campusId ");
			params.put("campusId", campusId);
			if(org!=null){
				hql.append(" or bonusStaffOrg.id in (select id from Organization where orgLevel like :orgLevel )");
				params.put("orgLevel", org.getOrgLevel()+"%");
			}
			hql.append(" )");
		}
		return 	this.findAllByHQL(hql.toString(),params);
	}

	@Override
	public List<Map<Object, Object>> getPersonPaymentFinaceBonus(BasicOperationQueryVo searchVo) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer("select ");
		if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.GROUNP)){
			sql.append("  (select concat(id,'_',name) from organization where id =group_id) groupName,");
			sql.append(" (select concat(id,'_',name) from organization where id =branch_id) branchName,");
		}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.BRENCH)){
			sql.append("  (select concat(id,'_',name) from organization where id =group_id) groupName,");
			sql.append(" (select concat(id,'_',name) from organization where id =branch_id) branchName,");
			sql.append(" (case when type='BRENCH' then (select concat(id,'_',name) from organization where id =bonus_dept_id) else (select concat(id,'_',name) from organization where id =campus_id) end) campusName,");
		}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.CAMPUS)){
			sql.append("  (select concat(id,'_',name) from organization where id =group_id) groupName,");
			sql.append(" (select concat(id,'_',name) from organization where id =branch_id) branchName,");
			sql.append(" (case when type='BRENCH' then (select concat(id,'_',name) from organization where id =bonus_dept_id) else (select concat(id,'_',name) from organization where id =campus_id) end) campusName,");
			sql.append(" (case when type='CAMPUS' then (select concat(id,'_',name) from organization where id =bonus_dept_id) else (select concat(user_id,'_',name) from user where user_id =bonus_staff_id) end) bonusStaffName,");

		}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.USER) || searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.EDUCAT_SPEC)){
			sql.append("  (select concat(id,'_',name) from organization where id =group_id) groupName,");
			sql.append(" (select concat(id,'_',name) from organization where id =branch_id) branchName,");
			sql.append(" (case when type='BRENCH' then (select concat(id,'_',name) from organization where id =bonus_dept_id) else (select concat(id,'_',name) from organization where id =campus_id) end) campusName,");
			sql.append(" (case when type='CAMPUS' or type='BRENCH' then (select concat(id,'_',name) from organization where id =bonus_dept_id) else (select concat(user_id,'_',name) from user where user_id =bonus_staff_id) end) bonusStaffName,");
			sql.append(" (select name from organization where id = transaction_campus) school_id,");
			sql.append(" student_id studentName,");
			sql.append(" grade_id,contract_type,contract_id,product_type,transaction_time,");
		}

		sql.append(" sum(case when contract_type='REFUND' then  -bonus_amount else bonus_amount end) bonus_amount,receipt_month,type");
		sql.append(" from ");


		sql.append(" (");
		sql.append(" select ");
		sql.append(" 			ref.GROUP_ID,");
		sql.append("             ref.BRANCH_ID,");
		sql.append("             ref.CAMPUS_ID,");
		sql.append("             bonus_amount,");
		sql.append("             receipt_month,");
		sql.append("             bonus_dept_id,");
		sql.append("             bonus_staff_id,");
		sql.append("             student_id,");
		sql.append("             school_id,");
		sql.append("             grade_id,");
		sql.append("             contract_type,");
		sql.append("             contract_id,");
		sql.append("             product_type,");
		sql.append("             transaction_time,");
		sql.append("             transaction_campus,");
		sql.append("             'USER' type");
		sql.append(" ");
		sql.append("  from ods_month_payment_receipt ods");
		sql.append(" left join ref_user_org ref on ods.bonus_staff_id=ref.USER_ID");
		sql.append(" where  ods.bonus_staff_id is not null           ");
		if(StringUtil.isNotBlank(searchVo.getCountDate())) {
			sql.append(" and receipt_month = :countDate ");
			params.put("countDate", searchVo.getCountDate());
		}
		sql.append("  union           ");
		sql.append(" select ");
		sql.append("  o.parentid GROUP_ID,");
		sql.append("             bonus_dept_id BRANCH_ID,");
		sql.append("             bonus_dept_id CAMPUS_ID,");
		sql.append("             bonus_amount,");
		sql.append("             receipt_month,");
		sql.append("             bonus_dept_id,");
		sql.append("             bonus_staff_id,");
		sql.append("             student_id,");
		sql.append("             school_id,");
		sql.append("             grade_id,");
		sql.append("             contract_type,");
		sql.append("             contract_id,");
		sql.append("             product_type,");
		sql.append("             transaction_time,");
		sql.append("             transaction_campus,");
		sql.append("             'BRENCH' type");
		sql.append(" from ods_month_payment_receipt ods");
		sql.append(" left join organization o on ods.bonus_dept_id=o.id");
		sql.append(" where bonus_amount>0 and ods.bonus_staff_id is null and o.orgType='BRENCH' ");
		if(StringUtil.isNotBlank(searchVo.getCountDate())) {
			sql.append(" and receipt_month = :countDate2 ");
			params.put("countDate2", searchVo.getCountDate());
		}
		sql.append("  union  ");
		sql.append(" select ");
		sql.append(" o2.parentID group_id,");
		sql.append("            o2.id BRANCH_ID,");
		sql.append("             o.id CAMPUS_ID,");
		sql.append("             bonus_amount,");
		sql.append("             receipt_month,");
		sql.append("             bonus_dept_id,");
		sql.append("             bonus_staff_id,");
		sql.append("             student_id,");
		sql.append("             school_id,");
		sql.append("             grade_id,");
		sql.append("             contract_type,");
		sql.append("             contract_id,");
		sql.append("             product_type,");
		sql.append("             transaction_time,");
		sql.append("             transaction_campus,");
		sql.append("             'CAMPUS' type");
		sql.append("  from ods_month_payment_receipt ods");
		sql.append(" left join organization o on ods.bonus_dept_id=o.id");
		sql.append(" left join organization o2 on o.parentID=o2.id");
		sql.append(" where bonus_amount>0 and ods.bonus_staff_id is null and o.orgType='CAMPUS' ");
		if(StringUtil.isNotBlank(searchVo.getCountDate())) {
			sql.append(" and receipt_month = :countDate3 ");
			params.put("countDate3", searchVo.getCountDate());
		}
		sql.append(" )a ");




		sql.append("     where 1=1 ");
		if(searchVo.getProductType()!=null){
			sql.append(" and product_type = :productType ");
			params.put("productType", searchVo.getProductType());
		}

		if(searchVo.getContractType()!=null){
			sql.append(" and contract_type = :contractType ");
			params.put("contractType", searchVo.getContractType());
		}

		if (StringUtil.isNotBlank(searchVo.getOrganizationId())) {
			sql.append(" AND campus_id = :organizationId ");
			params.put("organizationId", searchVo.getOrganizationId());
		}

		if (StringUtil.isNotBlank(searchVo.getBrenchId())) {
			sql.append(" AND branch_id = :brenchId ");
			params.put("brenchId", searchVo.getBrenchId());
		}

		if(StringUtils.isNotBlank(searchVo.getBlCampusId())){
			if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.BRENCH)){
				sql.append("  and (branch_id = '"+searchVo.getBlCampusId()+"'or ( type = 'BRENCH' and  bonus_dept_id = :blCampusId)) ");
				params.put("blCampusId", searchVo.getBlCampusId());
			}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.CAMPUS)){
				sql.append("  and (campus_id = '"+searchVo.getBlCampusId()+"'or (type = 'CAMPUS' and  bonus_dept_id = :blCampusId)) ");
				params.put("blCampusId", searchVo.getBlCampusId());
			}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.USER)){
				sql.append("and type ='USER' and (bonus_staff_id = '"+searchVo.getBlCampusId()+"')");
			}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.EDUCAT_SPEC)){
				sql.append(" and bonus_dept_id = '"+searchVo.getBlCampusId()+"' and (type='BRENCH' or type='CAMPUS') ");
			}
		}



		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and campus_id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}


		if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.GROUNP)){
			sql.append(" group by branch_id");
			sql.append(" order by branch_id ");
		}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.BRENCH)){
			sql.append(" group by campus_id");
			sql.append(" order by campus_id ");
		}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.CAMPUS)){
			sql.append(" group by campus_id,bonus_staff_id,bonus_dept_id,type");
			sql.append(" order by bonus_staff_id ");
		}else if(searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.USER)|| searchVo.getBasicOperationQueryLevelType().equals(BasicOperationQueryLevelType.EDUCAT_SPEC)){
			sql.append(" group by campus_id,bonus_staff_id,bonus_dept_id,student_id,transaction_time,product_type,type");
			sql.append(" order by transaction_time desc");
		}

		return this.findMapBySql(sql.toString(), params);
	}
}
