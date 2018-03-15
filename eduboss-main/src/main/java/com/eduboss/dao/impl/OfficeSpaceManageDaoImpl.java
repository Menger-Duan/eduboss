package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OfficeSpaceManageDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.OfficeSpaceManage;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.OfficeSpaceManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class OfficeSpaceManageDaoImpl extends GenericDaoImpl<OfficeSpaceManage, String> implements OfficeSpaceManageDao{
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Autowired
	private UserService userService;

	public DataPackage getOfficeSpaceManageList(DataPackage dataPackage,
			OfficeSpaceManageVo officeSpaceManageVo, Map<String, Object> params) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" from OfficeSpaceManage where 1=1");
		Map<String, Object> param = Maps.newHashMap();
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			Organization org = organizationDao.findById(params.get("organizationId").toString());
			hql.append(" and organization.orgLevel like :orgLevel ");
			param.put("orgLevel", org.getOrgLevel()+"%");
		}
		if(StringUtil.isNotBlank(params.get("officeSpace").toString())){
			hql.append(" and officeSpace like :officeSpace ");
			param.put("officeSpace","%"+params.get("officeSpace").toString()+"%");
		}
		if(StringUtil.isNotBlank(params.get("areaStart").toString())){
			hql.append(" and spaceArea >= :areaStart ");
			param.put("areaStart", new BigDecimal(params.get("areaStart").toString()));
		}
		if(StringUtil.isNotBlank(params.get("areaEnd").toString())){
			hql.append(" and spaceArea <= :areaEnd ");
			param.put("areaEnd", new BigDecimal(params.get("areaEnd").toString()));
		}
		if(StringUtil.isNotBlank(params.get("status").toString())){
			hql.append(" and status = :status ");
			param.put("status", Integer.parseInt(params.get("status").toString()));
		}
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","organization.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("场地管理","nsql","hql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		hql.append(" order by organization.id, modifyTime desc ");
		return super.findPageByHQL(hql.toString(), dataPackage,true,param);
	}
	
	@Override
	public OfficeSpaceManageVo findOfficeSpaceByOrgId(String id) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		
		if(StringUtils.isNotEmpty(id)){
			criterionList.add(Expression.eq("organization.id", id));
		}
		
		List list=this.findAllByCriteria(criterionList);
		if(list!=null && list.size()>0){
			return  HibernateUtils.voObjectMapping(list.get(0), OfficeSpaceManageVo.class);
		}
		return null;
	}

	/**
	 * 场地信息统计
	 */     
	@Override
	public DataPackage officeSpaceCountList(DataPackage dataPackage,String campusType,String organizationIdFinder,OrganizationType organizationType) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select ");
		 if (OrganizationType.BRENCH.equals(organizationType)) {
			 sql.append("case when org_group.id is null then CONCAT(org_brench.id,'_' ,org_brench.`name`) else CONCAT(org_group.id,'_' ,org_group.`name`) end  GROUNP,");
			 sql.append(" case when org_group.id is null  then CONCAT(o.id,'_',o.`name`) else CONCAT(org_brench.id, '_' ,org_brench.`name`) end BRENCH,");
			 sql.append(" '' CAMPUS,");
			 sql.append(" ''  officeSpace ,");   //场地名称			
		} else if (OrganizationType.CAMPUS.equals(organizationType)) {
			sql.append(" case when org_group.id is null then CONCAT(org_brench.id,'_' ,org_brench.`name`) else CONCAT(org_group.id,'_' ,org_group.`name`) end  GROUNP,");
			sql.append(" case when org_group.id is null  then CONCAT(o.id,'_',o.`name`) else CONCAT(org_brench.id, '_' ,org_brench.`name`) end BRENCH,");
			sql.append(" case when org_group.id is null then CONCAT(o.id,'_',o.`name`) else CONCAT(o.id,'_',o.`name`) end CAMPUS,");
			sql.append(" osm.OFFICE_SPACE officeSpace ,");   //场地名称		
		}
		 	sql.append(" case when org_group.id is null  then o.id else org_brench.id end as brenchId,");
		    sql.append(" IFNULL(sum(SPACE_AREA),0) spaceArea,");  //面积
			sql.append(" IFNULL(sum(BOOTH_NUMBER),0) boothNumber, "); //				 卡座数,
			sql.append(" IFNULL(sum(CLASSROOM_NUMBER),0) classroomNumber,"); //				 教室数,
			sql.append(" IFNULL(sum(DESK_NUMBER),0) deskNumber,"); //				 教室课桌数,
			sql.append(" IFNULL(sum(ONE_ON_ONE_PRE_PRODUCTION),0) oneProduction,"); //				 一对一预产值,
			sql.append(" IFNULL(sum(MINI_CLASS_PRE_PRODUCTION),0) miniProduction,"); //				 小班预产值,
			sql.append(" IFNULL(sum(PRE_SQUARE_PERFORMANCE),0) preSquarePerformance,"); //				 预估平数,
			sql.append(" IFNULL(sum(REAL_SQUARE_PERFORMANCE),0) realSquarePerformance,"); //				 实际平数,
			sql.append(" IFNULL(sum(MONTHLY_RENT),0) monthRent,"); //				 月租金,
			sql.append(" IFNULL(sum(YEARLY_RENT),0) yearlyRent,"); //				 年租金,
			sql.append(" IFNULL(sum(ADMINISTRATIVE_FEE),0) administrative,"); //				 管理费,
			sql.append(" IFNULL(sum(AIR_CONDITION_FEE),0) airCondition "); //				 空调费
			sql.append(" from (select	* from office_space_manage osm group by osm.organization_id,osm.office_space ) osm  ");
			sql.append(" LEFT JOIN organization o on o.id=osm.ORGANIZATION_ID ");
			sql.append(" LEFT JOIN organization org_brench on o.parentID=org_brench.id  ");
			sql.append(" LEFT JOIN organization org_group  on org_brench.parentID=org_group.id  ");
		sql.append(" where 1=1 ");
		if (OrganizationType.CAMPUS.equals(organizationType) && (StringUtils.isBlank(campusType) || (StringUtils.isNotBlank(campusType) && "1".equals(campusType)))) {
			sql.append(" AND org_brench.id = :id or osm.organization_id= :id ");
            params.put("id", organizationIdFinder);
		}
		
		//权限查询所属组织架构下的所有校区信息
		List<Organization> userOrganizationss= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizationss != null && userOrganizationss.size() > 0){
			Organization org = userOrganizationss.get(0);
			sql.append("  and o.id in ( select id from organization where orgLevel like :orgLevel ");
			params.put("orgLevel", org.getOrgLevel()+"%");
			for(int i = 1; i < userOrganizationss.size(); i++){
				sql.append(" or orgLevel like :orgLevel"+i+" " );
				params.put("orgLevel"+i, userOrganizationss.get(i).getOrgLevel()+"%");
			}
			sql.append(")");
		}

	if (OrganizationType.BRENCH.equals(organizationType)) {
		sql.append(" GROUP BY brenchId ");
	}else if (OrganizationType.CAMPUS.equals(organizationType)) {
		sql.append(" GROUP By osm.organization_id,osm.office_space ");
	}  
		List<Map<Object,Object>> list=super.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setDatas(list);	
		dataPackage.setRowCount(this.findCountSql("select count(1) from ("+sql+") count",params));
		return dataPackage;
	}
	
	
	/**
	 * 添加场地前判断
	 */
	public int beforSaveOfficeSpace(String orgId,String space,String fid){		
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtil.isNotBlank(orgId) && orgId!=null && StringUtil.isNotBlank(space) && space != null && StringUtil.isNotBlank(fid) && fid != null && !fid.equals("")){
			sql.append("select count(1) from office_space_manage where ORGANIZATION_ID = :orgId AND OFFICE_SPACE = :space and id = :fid ");
			params.put("orgId", orgId);
			params.put("space", space);
			params.put("fid", fid);
			int count=findCountSql(sql.toString(),params);
			if(count==1){
				count=0;
			}
			return count;
		}
		if(StringUtil.isNotBlank(orgId) && orgId!=null && StringUtil.isNotBlank(space) && space != null){
			sql.append("select count(1) from office_space_manage where ORGANIZATION_ID = :orgId2 AND OFFICE_SPACE = :space2 ");
			params.put("orgId2", orgId);
			params.put("space2", space);
			int count=findCountSql(sql.toString(),params);
			return count;
		}
		else{
			return 1;
		}
		
	}


}
