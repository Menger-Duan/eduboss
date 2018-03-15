package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ReceptionistEveryDayDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.ReceptionistEveryDay;
import com.eduboss.domainVo.ReceptionistEveryDayVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;

@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class ReceptionistEveryDayDaoImpl extends GenericDaoImpl<ReceptionistEveryDay, String> implements ReceptionistEveryDayDao {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;

	/**
	 * 前台信息
	 */
	@Override
	public DataPackage getReceptionistEveryDays(DataPackage dataPackage,
			ReceptionistEveryDayVo receptionistEveryDayVo,Map<String, Object> params) {
			StringBuilder hql = new StringBuilder();
		hql.append(" from ReceptionistEveryDay  where 1=1");
		if(StringUtil.isNotBlank(params.get("startDate").toString())){
			hql.append(" and loginDate >= :startDate ");
		}
		if(StringUtil.isNotBlank(params.get("endDate").toString())){
			hql.append(" and loginDate <= :endDate");
		}
		if(StringUtil.isNotBlank(params.get("organizationIdFinder").toString())){
			hql.append(" and organization.id = :organizationIdFinder ");
		}
		if(StringUtil.isNotBlank(params.get("loginId").toString())){
			hql.append(" and user.userId = :loginId ");
		}
		hql.append(" order by  loginDate desc ");
		
		return super.findPageByHQL(hql.toString(), dataPackage, true, params);
	}

	/**
	 * 校区资源利用
	 */
	@Override
	public DataPackage shoolResourceList(DataPackage dataPackage,
			ReceptionistEveryDayVo receptionistEveryDayVo,Map<String, Object> params) {
		Map<String, Object> hqlParams = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		 if (BasicOperationQueryLevelType.GROUNP.equals(params.get("organizationType"))) {
			 sql.append("case when org_group.id is null then CONCAT(org_brench.id,'_' ,org_brench.`name`) else CONCAT(org_group.id,'_' ,org_group.`name`) end  GROUNP,");
			 sql.append(" case when org_group.id is null  then CONCAT(o.id,'_',o.`name`) else CONCAT(org_brench.id, '_' ,org_brench.`name`) end BRENCH,");
			 sql.append(" '' CAMPUS,");			
		} else if (BasicOperationQueryLevelType.BRENCH.equals(params.get("organizationType"))) {
			sql.append(" case when org_group.id is null then CONCAT(org_brench.id,'_' ,org_brench.`name`) else CONCAT(org_group.id,'_' ,org_group.`name`) end  GROUNP,");
			sql.append(" case when org_group.id is null  then CONCAT(o.id,'_',o.`name`) else CONCAT(org_brench.id, '_' ,org_brench.`name`) end BRENCH,");
			sql.append(" case when org_group.id is null then CONCAT(o.id,'_',o.`name`) else CONCAT(o.id,'_',o.`name`) end CAMPUS,");	
		} else if(BasicOperationQueryLevelType.CAMPUS.equals(params.get("organizationType"))){
			sql.append(" case when org_group.id is null then CONCAT(org_brench.id,'_' ,org_brench.`name`) else CONCAT(org_group.id,'_' ,org_group.`name`) end  GROUNP,");
			sql.append(" case when org_group.id is null  then CONCAT(o.id,'_',o.`name`) else CONCAT(org_brench.id, '_' ,org_brench.`name`) end BRENCH,");
			sql.append(" case when org_group.id is null then CONCAT(o.id,'_',o.`name`) else CONCAT(o.id,'_',o.`name`) end CAMPUS,");			
		}
		sql.append(" IFNULL(sum(rec.tel_num),0) as tel_num, ");
		sql.append(" IFNULL(sum(rec.tel_numw),0) as tel_numw, ");
		sql.append(" IFNULL(sum(rec.visit_numz),0) as visit_numz, ");
		sql.append(" IFNULL(sum(rec.visit_numy),0) as visit_numy, ");
		sql.append(" IFNULL((sum(rec.loginNumsw)),0) as loginNumsw,  ");
		sql.append(" IFNULL(sum(customerNums),0) customerNums,   ");
		sql.append(" IFNULL(sum(contractNums),0) contractNums  ");
				
		sql.append(" from organization o  ");
		sql.append(" LEFT JOIN organization org_brench on o.parentID=org_brench.id ");
		sql.append(" LEFT JOIN organization org_group on org_brench.parentID=org_group.id ");
		sql.append(" LEFT JOIN  ");
		sql.append(" (select *,IFNULL((DATEDIFF(:endDate, :startDate)+1)-count(1),0) as loginNumsw  from receptionist rec "
				+ "where login_date >= :startDate2 and login_date <= :endDate2 group by org_id) rec on rec.org_id=o.id ");
		hqlParams.put("endDate", params.get("endDate"));
		hqlParams.put("startDate", params.get("startDate"));
		hqlParams.put("endDate2", params.get("endDate"));
		hqlParams.put("startDate2", params.get("startDate"));
		sql.append(" LEFT JOIN (select count(1) as contractNums,s.BL_CAMPUS_ID  from contract co "
				+ " INNER JOIN student s on co.STUDENT_ID = s.ID where co.CONTRACT_TYPE ='NEW_CONTRACT' and co.CREATE_TIME >= :startDate3 "
						+ " and co.CREATE_TIME <= :endDate3 group by s.BL_CAMPUS_ID) co on co.BL_CAMPUS_ID=o.id ");
		hqlParams.put("startDate3", params.get("startDate") + " 00:00:00");
		hqlParams.put("endDate3", params.get("endDate") + " 23:59:59");
		sql.append(" LEFT JOIN (select count(1) customerNums,bl_school from customer  where RECORD_DATE >= :startDate4 and RECORD_DATE <= :endDate4 and DEAL_STATUS <> 'INVALID' group by BL_SCHOOL) cus on cus.bl_school=o.id  ");
		hqlParams.put("startDate4", params.get("startDate") + " 00:00:00");
		hqlParams.put("endDate4", params.get("endDate") + " 23:59:59");
		sql.append(" where 1=1  ");

		//公司和校区维度查询
		if (params.get("organizationType")!=null && OrganizationType.BRENCH.getValue().equals(params.get("organizationType").toString()) && params.get("searchType")!=null  
				&& StringUtils.isNotBlank(params.get("searchType").toString()) && "2".equals(params.get("searchType").toString())){
		}else if(params.get("organizationType")!=null && OrganizationType.BRENCH.getValue().equals(params.get("organizationType").toString())){
			sql.append(" AND  org_brench.id = :organizationIdFinder ");
			hqlParams.put("organizationIdFinder", params.get("organizationIdFinder"));
		}
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
				}
			sql.append(")");		
		}
		sql.append(" and o.orgType='CAMPUS' ");
		if(BasicOperationQueryLevelType.GROUNP.equals(params.get("organizationType"))){
			sql.append(" group by org_brench.id ");

		}
		if (BasicOperationQueryLevelType.BRENCH.equals(params.get("organizationType"))) {
			sql.append(" group by o.id ");
		} 
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(), dataPackage, hqlParams);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(super.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", hqlParams));
		return dataPackage;
		
	}
	
	@Override
	public ReceptionistEveryDay findByloginDateAndOrganizationId(String loginDate, String organizationId){
		ReceptionistEveryDay receptionistEveryDay = null;
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(loginDate)) {
			criterionList.add(Restrictions.eq("loginDate",
					loginDate));
		}
		if (StringUtils.isNotBlank(organizationId)) {
			criterionList.add(Restrictions.eq("organization.id",
					organizationId));
		}
		List<ReceptionistEveryDay> redList = super.findAllByCriteria(criterionList);
		if (redList != null && redList.size() > 0) {
			receptionistEveryDay = redList.get(0);
		}
		return receptionistEveryDay;
	}
	
}
