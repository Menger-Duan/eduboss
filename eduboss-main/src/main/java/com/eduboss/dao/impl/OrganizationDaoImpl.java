package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.Constants;
import com.eduboss.dao.UserOrganizationRoleDao;
import com.eduboss.domain.UserOrganizationRole;
import com.eduboss.domainVo.OrganizationSearchDto;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ResourcePoolJobType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.RegionDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Region;
import com.eduboss.domainVo.ResourcePoolUserVo;
import com.eduboss.dto.DispNoGenerator;
import com.eduboss.utils.HibernateUtils;
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
@Repository("OrganizationDao")
public class OrganizationDaoImpl extends GenericDaoImpl<Organization, String> implements OrganizationDao {

	private static final Logger log = LoggerFactory.getLogger(OrganizationDaoImpl.class);

	@Autowired
	private RegionDao regionDao;

	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Autowired
	private UserService userService;
	
	//no longer used
	// property constants
	public String queryOrganizationLevel(String curOrganizationId){
	    String organizationLevel = "";
		StringBuffer sql = new StringBuffer();
	    sql.append("select ");
	    sql.append("a.id as id1,a.parentId as parentId1,");
	    sql.append("b.id as id2,b.parentId as parentId2,");
	    sql.append("c.id as id3,c.parentId as parentId3 ");
	    sql.append("from organization a ");
	    sql.append("left join eduboss.organization b on b.id = a.parentID ");
	    sql.append("left join eduboss.organization c on c.id = b.parentID ");
	    sql.append("where a.id = ? ");
	    Map<String,Object> organizationData = null;//this.findMapBySQL(sql.toString(), curOrganizationId);
	    if(organizationData != null){
			Object id1 = organizationData.get("id1");
			Object parentID1 = organizationData.get("parentID1");
			Object id2 = organizationData.get("id2");
			Object parentID2 = organizationData.get("parentID2");
			Object id3 = organizationData.get("id3");
			Object parentID3 = organizationData.get("parentID3");
			StringBuffer resultId = new StringBuffer();
			if(id3 != null){
			    resultId.append(id3.toString());
			    resultId.append("-");
			}
			if(id2 != null){
			    resultId.append(id2.toString());
			    resultId.append("-");
			}
			if(id1 != null){
			    resultId.append(id1.toString());
			}
			if(parentID3 == null)
				return resultId.toString();
			if(parentID3.toString().length() == 0)
				return resultId.toString();
			return queryOrganizationLevel(parentID3.toString())+"-"+resultId.toString();
		}
	    return organizationLevel;
	}
	
	@Override
	public String getNextOrgLevel(String parentOrgId, String parentOrgLevel) {
		String nextOrgLevel = "";
		List<Order> scoreOrderList = new ArrayList<Order>();
		scoreOrderList.add(Order.desc("orgLevel"));
		//查找所有同级节点，拿最大的+1
		List<Organization> organizations = super.findByCriteria(scoreOrderList, Expression.eq("parentId", parentOrgId));
		if (organizations.size() > 0) {
			String lastLevel = organizations.get(0).getOrgLevel();
			if (StringUtils.isNotBlank(lastLevel)) {
				nextOrgLevel = String.valueOf((Integer.valueOf(lastLevel.substring(lastLevel.length() - 4, lastLevel.length())) + 1));
				nextOrgLevel = "0000".substring(0, 4 - nextOrgLevel.length()) + nextOrgLevel;//不够4位前面补0
			} else {
				nextOrgLevel = "0001";
			}
		} else {
			nextOrgLevel = "0001";
		}
		
		if (!"-1".equalsIgnoreCase(parentOrgLevel)) {
			nextOrgLevel = parentOrgLevel + nextOrgLevel;
		}
		
		return nextOrgLevel;
	}

	@Override
	public List<Organization> getOrganizationsByOrgLevelAndType(String orgLevel, OrganizationType orgType) {
		return super.findByCriteria(Expression.like("orgLevel", orgLevel, MatchMode.START), Expression.eq("orgType", orgType));
	}

	@Override
	public Organization getBelongCampus(String orgLevel) {
		return getBelongOrgazitionByOrgType(orgLevel, OrganizationType.CAMPUS);
	}

	@Override
	public Organization getBelongBrench(String orgLevel) {
		return getBelongOrgazitionByOrgType(orgLevel, OrganizationType.BRENCH);
	}
	
	/**
	 * 获取所在归属分公司
	 * @param orgLevel
	 * @return
	 */
	@Override
	public Organization getBelongGrounp(String orgLevel) {
		return getBelongOrgazitionByOrgType(orgLevel, OrganizationType.GROUNP);
	}
	
	public Organization getBelongOrgazitionByOrgType(String orgLevel, OrganizationType orgType) {
		return getBelongOrgazitionByOrgType(orgLevel, orgType,false);
	}
		
	/**
	 * 
	 * @param orgLevel
	 * @param orgType
	 * @param isMainOrganization 没有找到类型时是否返回一个（类型：校区，分公司，集团）的值
	 * @return
	 */
	public Organization getBelongOrgazitionByOrgType(String orgLevel, OrganizationType orgType,boolean isMainOrganization) {
		List<String> parentLevels = cutOrgLevel(orgLevel);
		if (parentLevels.size() > 0 ) {
			List<Order> orderList=new ArrayList<Order>();
			orderList.add(Order.desc("orgLevel"));
			List<Organization> orgs = super.findByCriteria(orderList,Expression.in("orgLevel", parentLevels));
			for (Organization org:orgs) {
				if (org.getOrgType() == orgType) {
					return org;
				}
			}
			if(isMainOrganization){
				for (Organization org:orgs) {
					if (org.getOrgType() == OrganizationType.CAMPUS
							|| org.getOrgType() == OrganizationType.BRENCH
							|| org.getOrgType() == OrganizationType.GROUNP) {
						return org;
					}
				}
			}
		}
		return null;
	}
	
	private List<String> cutOrgLevel(String orgLevel) {
		List<String> orgIdStrings = new ArrayList<String> ();
		for (int i = 0; i < orgLevel.length() / 4; i ++) {
			orgIdStrings.add(orgLevel.substring(0, 4 * (i+1)));
		}
		//顺序or
		//for (int i = (orgLevel.length() / 4)-1; i >=0 ; i --) {
		//	orgIdStrings.add(orgLevel.substring(0, 4 * (i+1)));
		//}
		return orgIdStrings;
	}

	@Override
	public List<Organization> getOrganizationBoy(Organization org,Organization brench) {
		String hql="from Organization  ";
		Map<String, Object> params = Maps.newHashMap();
		if (brench != null) {
			hql += " where orgLevel like :orgLevel ";
			params.put("orgLevel", brench.getOrgLevel()+ "%");
		}
		hql+=" order by length(orgLevel),orgOrder";
		List<Organization> list=this.findAllByHQL(hql,params) ;
		return list;
	}
	
	@Override
	public List<Organization> getOrganizationBoy(List<Organization> orgs) {
		String hql="from Organization  where 1=1 ";
		Map<String, Object> params = Maps.newHashMap();
		if(orgs.size()>0){
			Organization o=orgs.get(0);
			hql +=  " and (orgLevel like :orgLevel";
			params.put("orgLevel", o.getOrgLevel()+ "%");
		}
		for (int i=1;i<orgs.size();i++) {
			Organization o=orgs.get(i);
			hql +=  " or orgLevel like :orgLevel"+i+" ";
			params.put("orgLevel"+i, o.getOrgLevel()+ "%");
		}
		hql +=  ")";
		
		hql+=" order by length(orgLevel),orgOrder";
		List<Organization> list=this.findAllByHQL(hql,params) ;
		return list;
	}
	
	@Override
	public List<Organization> getBelongOrg(List<Organization> orgs) {
		String hql="from Organization  where 1=1 and bossUse = 0";
//		if (brench != null) {
//			hql += " where orgLevel like '" + brench.getOrgLevel() + "%' ";
//		}
		Map<String, Object> params = Maps.newHashMap();
		if(orgs.size()>0){
			hql += " and  (";
			int size=0;
			for (Organization org : orgs) {
				if(size==0){
					hql += " orgLevel like :orgLevel ";
				    params.put("orgLevel", org.getOrgLevel()+ "%");
				}else{
					hql += " or orgLevel like :orgLevel"+size+" ";
					params.put("orgLevel"+size, org.getOrgLevel()+ "%");
				}
				size++;
			}
			
			hql += " )";
		}
		hql+=" order by length(orgLevel),orgOrder";
		List<Organization> list=this.findAllByHQL(hql,params) ;
		return list;
	}
	

	@Override
	public String getOrganizationIdByName(String name) {
		List<Organization> list= findByCriteria(Expression.eq("name", name));
		if(list!=null && list.size()>0){
			return list.get(0).getId();
		}
		return null;
	}
	
	/**
	 * 
	 */
	public Organization getOrganizationFirstFloorMax(){
		Map<String, Object> params = Maps.newHashMap();
		String hql="from Organization  where length(orgLevel)=4 ";
		hql+=" order by orgLevel desc ";
		List<Organization> list=this.findAllByHQL(hql,params) ;
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public Organization getOrganizationByUserId(String userId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		String hql="from Organization  where id in(select organizationId from User where userId= :userId and organizationId is not null )";
		List<Organization> list=this.findAllByHQL(hql,params) ;
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据类型查询组织架构数量 （organizationId有值除外）
	 * @param organizationType
	 * @param organizationId
	 * @return
	 */
	public int getOrganizationCountByOrganizationType(OrganizationType organizationType,String organizationId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("organizationType", organizationType);
		String hql="select count(*) from Organization where orgType= :organizationType ";
		if(StringUtils.isNotBlank(organizationId)){
			hql+=" and id<> :organizationId ";
			params.put("organizationId", organizationId);
		}
		return this.findCountHql(hql,params);
	}
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByCustomerPool(String orgLevel){
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgLevel", orgLevel);
		String sql="SELECT * from organization where IS_PUBLIC_POOL='1' "+
			" and ("+
			" id in(SELECT id from organization where :orgLevel LIKE concat(orgLevel,'%'))"+
//			" or id in(SELECT id from organization where orgType='DEPARTMENT' and  parentID in(SELECT id from organization where '"+orgLevel.substring(0,orgLevel.length()-4)+"' LIKE concat(orgLevel,'%')))"+
			")  ORDER BY LENGTH(orgLevel),orgOrder ";
		//or orgType ='GROUNP'
		return findBySql(sql,params);
	}
	
	/**
	 * 根据orgLevel 获取所有下级客户资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByCustomerPoolLower(String orgLevel){
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgLevel1", orgLevel+"%");
		params.put("orgLevel2", orgLevel);
		String sql="SELECT * from organization where IS_PUBLIC_POOL='1' "+
			" and ("+
			" id in(SELECT id from organization where orgLevel LIKE :orgLevel1 )"+
			//" or id in(SELECT id from organization where orgType='DEPARTMENT' and  parentID in(SELECT id from organization where orgLevel LIKE '"+orgLevel+"%'))"+
			" and orgLevel != :orgLevel2 "+
			")  ORDER BY LENGTH(orgLevel),orgOrder ";
		//or orgType ='GROUNP'
		return findBySql(sql,params);
	}
	
	/**
	 * 根据orgLevel 获取校区级别以上的上级资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByResourcePool(String orgLevel) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgLevel", orgLevel);
		String sql="SELECT * from organization where 1=1 " + 
					" and LENGTH(orgLevel) <= 12 " + 
//					" and (orgType = 'GROUNP' or orgType = 'BRENCH' or orgType = 'CAMPUS') "+
				" and ("+
				" id in(SELECT id from organization where :orgLevel LIKE concat(orgLevel,'%'))"+
				") and orglevel<> :orgLevel ORDER BY LENGTH(orgLevel),orgOrder ";
			return findBySql(sql,params);
	}
	
	/**
	 * 根据orgLevel 获取校区级别以上的下级资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationCampusByResourcePoolLower(String orgLevel) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgLevel", orgLevel+"%");
		String sql="SELECT * from organization where 1=1 " + 
					" and LENGTH(orgLevel) <= 12 " + 
//					" and (orgType = 'GROUNP' or orgType = 'BRENCH' or orgType = 'CAMPUS') "+
				" and ("+
				" id in(SELECT id from organization where orgLevel LIKE :orgLevel )"+
//				" and orgLevel = '"+orgLevel+"'"+
				")  ORDER BY LENGTH(orgLevel),orgOrder ";
			return findBySql(sql,params);
	}
	
	/**
	 * 根据orgLevel 获取所有的下级资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByResourcePoolLower(String orgLevel) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgLevel1", orgLevel+"%");
		params.put("orgLevel2", orgLevel);
		String sql="SELECT * from organization where 1=1 "+
				" and ("+
				" id in(SELECT id from organization where orgLevel LIKE :orgLevel1 )"+
				" and orgLevel != :orgLevel2 "+
				")  ORDER BY LENGTH(orgLevel),orgOrder ";
			return findBySql(sql,params);
	}
	
	@Override
	public List<Organization> getOrganizationsByParentOrgIdAndType(String orgId, OrganizationType orgType) {
		return super.findByCriteria(Expression.eq("parentId", orgId), Expression.eq("orgType", orgType));
	}

	@Override
	public List<Organization> getIncludeAllParentOrgByOrgId(String organizationId) {
		List<Organization> orgList = new ArrayList<Organization>();
		if(StringUtil.isNotBlank(organizationId)) {
			Organization org = this.findById(organizationId);
			if(org != null) {
				orgList.add(org);
				String parentOrgId = org.getParentId();
				while(StringUtil.isNotBlank(parentOrgId)){					
					Organization pOrg = this.findById(parentOrgId);	
					if(pOrg != null) {
						orgList.add(pOrg);
						parentOrgId = pOrg.getParentId();
					}else {
						parentOrgId = null;
					}
					
				}
			}
		}
		return orgList;
	}
	
	@Override
	public List<ResourcePoolUserVo> getValidateResourcePool(String orgLevel, String jobId, ResourcePoolJobType type) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgLevel1", orgLevel);
		params.put("orgLevel2", orgLevel+"%");
		params.put("jobId", jobId);
		params.put("type", type.getValue());
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from organization ");
		sql.append("   where 1=1 ");
		sql.append("     and (id in ( ");
		sql.append("       select id from organization where :orgLevel1 like CONCAT(orgLevel,'%') ");
		sql.append("     ) ");
		sql.append("     or id in ( ");
		sql.append("       select id from organization where orgLevel like :orgLevel2 ");
		sql.append(" 	)) ");
		sql.append(" 	and  id in ( ");
		sql.append(" 	  select ORGANIZATION_ID from resource_pool_job where JOB_ID = :jobId and TYPE = :type ");
		sql.append(" 	) and (RESOURCE_POOL_STATUS='VALID' OR RESOURCE_POOL_STATUS IS NULL) ");
		List<Organization> list =  super.findBySql(sql.toString(),params);
		return HibernateUtils.voListMapping(list, ResourcePoolUserVo.class);
	}
	
	@Override
	public List<ResourcePoolUserVo> getDisValidateResourcePool(String orgLevel, String jobId, ResourcePoolJobType type) {
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("jobId", jobId);
		params.put("type", type.getValue());
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from organization ");
		sql.append(" 	where  id in ( ");
		sql.append(" 	  select ORGANIZATION_ID from resource_pool_job where JOB_ID = :jobId and TYPE = :type ");
		sql.append(" 	) and (RESOURCE_POOL_STATUS='VALID' OR RESOURCE_POOL_STATUS IS NULL) ");
		if(StringUtils.isNotBlank(orgLevel)){
			sql.append(" and orgLevel like :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}	
		List<Organization> list =  super.findBySql(sql.toString(),params);
		return HibernateUtils.voListMapping(list, ResourcePoolUserVo.class);
	}

	@Override
	public String getOrgNameById(String orgId) {
		String result="";
		if(StringUtils.isNotBlank(orgId)){
			Organization organization =findById(orgId);
			if (organization == null) {
				result = "未知 - 未知";
			} else if(StringUtils.isNotBlank(organization.getParentId())){
				//如果当前组织的parent不是null/集团或者分公司，进行升级操作。
				String parentId = organization.getParentId();

				Organization parentOrganization = this
						.findById(parentId);
				while (true) {
					if (parentOrganization == null
							|| parentOrganization.getOrgType() == OrganizationType.GROUNP
							|| parentOrganization.getOrgType() == OrganizationType.BRENCH) {
						if (parentOrganization == null) {
							result = "未知 - " + organization.getName();
						} else {
							result = parentOrganization.getName() + " - "
									+ organization.getName();
						}
						break;
					} else {
						organization = parentOrganization;
						parentId = organization.getParentId();
						parentOrganization = this.findById(parentId);
					}
				}
			}else{
				return organization.getName();
			}
		}
		return result;
	}

	@Override
	public List<Organization> getAllGroupAndBrench() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orgType1", OrganizationType.GROUNP);
		params.put("orgType2", OrganizationType.BRENCH);
		List<Organization> list= super.findBySql("select * from organization where orgType = :orgType1 or orgType= :orgType2 ",params);
		HibernateUtils.voListMapping(list, ResourcePoolUserVo.class);
		return list;
	}
	
	/**
	 * 根据学生查询所有关联校区
	 */
	@Override
	public List<Organization> getAllStudentCampus(String studentId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentId", studentId);
		String sql = "select * from organization where id in (select ORGANIZATION_ID from  STUDENT_ORGANIZATION where STUDENT_ID = :studentId ) or id = (select BL_CAMPUS_ID from student where id = :studentId ) ";
		return super.findBySql(sql,params);
	}

//	@Override
//	public Organization findById(String id){
//		if (StringUtils.isBlank(id)){
//			return null;
//		}
//		Organization organization = super.findById(id);
//
//	}

	public Organization setProvinceAndCityToOrganization(Organization organization){
		if (organization == null){
			return null;
		}
		if (StringUtils.isNotBlank(organization.getAreaId())){
			Region area = regionDao.findById(organization.getAreaId());
			organization.setArea(area);
			organization.setCity(area.getParent());
			organization.setProvince(area.getParent().getParent());
			return organization;
		}

		if (StringUtils.isNotBlank(organization.getCityId())){
			Region city = regionDao.findById(organization.getCityId());
			organization.setCity(city);
			organization.setProvince(city.getParent());
			return organization;
		}

		if (StringUtils.isNotBlank(organization.getProvinceId())){
			Region province = regionDao.findById(organization.getProvinceId());
			organization.setProvince(province);
			return organization;
		}
		return organization;
	}
	
	@Override
	public List<Organization> findOrganizationByUserId(String userId) {
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return getUserOrganizationList(userId);
		}else {
			return findOrganizationByUserIdOld(userId);
		}
	}

	@Override
	public List<Organization> findOrganizationByUserIdOld(String userId) {
			Map<String, Object> params = new HashMap<String, Object>();
			String sql = " select * from organization o left join user_organization uo on o.ID = uo.ORGANIZATIONID where uo.USERID = :userId and o.boss_use = 0 ";
			params.put("userId", userId);
			return super.findBySql(sql, params);
	}

	@Autowired
	private HibernateTemplate hibernateTemplate;


	@Override
	public void save(Organization organization){
		DispNoGenerator dispNoGenerator = new DispNoGenerator();
		if (organization.getId() == null){
			String id = dispNoGenerator.generate("organization", hibernateTemplate.getSessionFactory().openSession());
			organization.setId(id);
		}
		super.save(organization);
	}
	
	/**
	 * 获取所有集团和分公司
	 */
	public List<Organization> getGroupAndAllBrench() {
		String hql = " from Organization where 1=1 and (orgType = 'GROUNP' or orgType = 'BRENCH') order by id asc ";
		return super.findAllByHQL(hql, new HashMap<String, Object>());
	}

	@Override
	public Organization findOrganizationByHrmsId(String id) {
		String hql = " from Organization where hrmsId = :hrmsId";
		Map param  = new HashMap();
		param.put("hrmsId",id);
		return this.findOneByHQL(hql,param);
	}

    @Override
    public List getOrganizationByCondition(OrganizationSearchDto dto) {
			Map<String, Object> params = new HashMap<String, Object>();

			StringBuilder sql = new StringBuilder("select o.id,o.name,o.parentId,o.orgLevel,o.orgType,o.hrms_id,o.boss_use bossUse,o.status from organization o,organization_hrms hrms where o.boss_use = 0 ");

			sql.append(" and o.hrms_id = hrms.id ");
			if(StringUtils.isNotBlank(dto.getOrgTypes())){
				String[] types = StringUtil.replaceSpace(dto.getOrgTypes()).split(",");
				sql.append(" and  o.orgType in (:types)");
				params.put("types", types);
			}

			if(StringUtils.isNotBlank(dto.getParentId())){
				sql.append(" and o.parentID= :parentId");
				params.put("parentId",dto.getParentId());
			}

			if(StringUtils.isNotBlank(dto.getName())){
				sql.append(" and o.name like :name");
				params.put("name","%"+dto.getName()+"%");
			}

			if(dto.getLevel()!=null && dto.getLevel()>0){
				sql.append(" and length(o.orgLevel)<="+dto.getLevel()*4);
			}

			sql.append(" order by length(hrms.org_Level), hrms.org_Order,hrms.name");
			return this.findMapBySql(sql.toString(), params);
    }

	@Override
	public List<Organization> findOrganizationByOrgLevel(String orgLevel) {
		String sql = " select * from organization where orgLevel like :orgLevel";
		Map param = new HashMap();
		param.put("orgLevel","%"+orgLevel+"%");
		return this.findBySql(sql,param);
	}

	@Override
	public List<Organization> getOrgByParentId(String parentId) {
		String sql = "select * from organization where parentId = :parentId and boss_use = 0";
		Map param = new HashMap();
		param.put("parentId",parentId);

		return this.findBySql(sql,param);
	}

    @Override
    public List<Organization> getUserOrganizationList(String userId) {
		String sql = "select o.* from organization o left join user_organization_role uor on o.id = uor.organization_id where uor.user_id = :userId and o.boss_use = 0 ";
		Map param = new HashMap();
		param.put("userId",userId);
		return this.findBySql(sql,param);
    }

	@Override
	public List getBossSelectOrganizationList(OrganizationSearchDto dto) {
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder("select id,name,parentId,orgLevel,orgType,hrms_id,boss_use bossUse,status from organization where boss_use = 0 ");
		if(StringUtils.isNotBlank(dto.getOrgTypes())){
			String[] types = StringUtil.replaceSpace(dto.getOrgTypes()).split(",");
			sql.append(" and  orgType in (:types)");
			params.put("types", types);
		}

		if(StringUtils.isNotBlank(dto.getParentId())){
			sql.append(" and parentID= :parentId");
			params.put("parentId",dto.getParentId());
		}

		if(StringUtils.isNotBlank(dto.getName())){
			sql.append(" and name like :name");
			params.put("name","%"+dto.getName()+"%");
		}

		if(dto.getLevel()!=null && dto.getLevel()>0){
			sql.append(" and length(orgLevel)<="+dto.getLevel()*4);
		}

		sql.append(" and exists (select 1 from user_organization_role where user_id = '"+userService.getCurrentLoginUserId()+"' and organization_id in " +roleQLConfigService.getAllOrgAppendSql()+")");

		sql.append(" order by length(orgLevel), orgOrder");
		return this.findMapBySql(sql.toString(), params);
	}

}
