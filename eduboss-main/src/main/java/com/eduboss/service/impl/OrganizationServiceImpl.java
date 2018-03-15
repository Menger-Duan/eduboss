package com.eduboss.service.impl;

import com.eduboss.common.*;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MessagePushVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.RegionService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HttpHeadersUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.boss.rpc.base.dto.SchoolAreaRpcVo;
import org.boss.rpc.mobile.service.BossSchoolAreaRpc;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;


@Service("com.eduboss.service.OrganizationService")
public class OrganizationServiceImpl implements OrganizationService{

    private final static Logger logger = Logger.getLogger(OrganizationServiceImpl.class);
    
    @javax.annotation.Resource(name = "bossSchoolAreaRpc")
    private BossSchoolAreaRpc bossSchoolAreaRpc;
    
    @Autowired
    private RegionService regionService;
    
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private DataDictDao datadictDao;

	@Autowired
	private UserOrganizationDao userOrganizationDao;

	@Autowired
	private OrganizationHrmsDao organizationHrmsDao;
	@Autowired
	private OrganizationFileDao organizationFileDao;
	@Autowired
	private OrganizationModifyLogDao organizationModifyLogDao;
	@Autowired
	private UserService userService;

	@Autowired
	private ResourcePoolService resourcePoolService;
	
	@Override
	public Organization findById(String id) {
		return organizationDao.findById(id);
	}

    @Override
    public Organization findBrenchByCampusId(String campusId) {
		Organization org   = findById(campusId);
		if(org!=null && org.getOrgType().equals(OrganizationType.CAMPUS)){
			return findById(org.getParentId());
		}else if(org!=null && !org.getOrgType().equals(OrganizationType.GROUNP)){
			return findBrenchByCampusId(org.getParentId());
		}
        return null;
    }

    @Override
	public Map<String, Object> getOrganizationInfo(DataPackage dataPackage, String orgId) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String,Object>();
		Integer start = (dataPackage.getPageNo()-1)*dataPackage.getPageSize();	
		List<OrganizationInfoVo> result = new ArrayList<OrganizationInfoVo>();
		StringBuffer query = new StringBuffer();
		query.append(" select * from organization ");
		if(StringUtils.isNotBlank(orgId)){
			query.append(" where id = :orgId ");
			params.put("orgId", orgId);
		}
		String count ="select count(*) from ( " + query.toString() + " ) countall ";
		query.append(" limit "+start+","+dataPackage.getPageSize());
		List<Organization> organizations = organizationDao.findBySql(query.toString(), params);
		query.delete(0, query.length());
		for(Organization org:organizations){
			OrganizationInfoVo infoVo =new OrganizationInfoVo();
			infoVo.setId(org.getId());
			infoVo.setName(org.getName());
			infoVo.setParentId(org.getParentId());
			infoVo.setOrgType(org.getOrgType().getValue());
			infoVo.setLevelNo(org.getOrgLevel());
			infoVo.setOrgOrder(String.valueOf(org.getOrgOrder()));
			infoVo.setRegionId(org.getRegionId());
			if(org.getRegionId()!=null){
				DataDict dataDict = datadictDao.findById(org.getRegionId());
				if(dataDict!=null){infoVo.setRegionName(dataDict.getName());}
			}else{
				infoVo.setRegionName("");
			}
			result.add(infoVo);
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Integer totalCount = organizationDao.findCountSql(count, params);
		Integer pageSize = dataPackage.getPageSize();
	    Integer totalPage = totalCount / pageSize;  
        if (totalCount % pageSize > 0) {  
        	totalPage++;  
        } 
		resultMap.put("totalPage",totalPage );
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", result);
		map.put("resultStatus", 200);
		map.put("resultMessage", "组织架构列表");
		map.put("result", resultMap);
		
		return map;
	}

	@Override
	public Map<String, Object> getOrgTeacherInfo(DataPackage dataPackage, String teacherId) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String,Object>();
		Integer start = (dataPackage.getPageNo()-1)*dataPackage.getPageSize();	
		StringBuffer query = new StringBuffer();
		List<OrgTeacherInfoVo> result = new ArrayList<OrgTeacherInfoVo>();
				
		query.append(" select uor.user_id as teacherId,uor.organization_id as organizationId,o.name as organizationName,o.orgLevel as levelNo from user_organization_role uor ,organization o ,role r ");
		query.append(" where  r.id = uor.role_id and uor.organization_id= o.id and r.roleCode='"+RoleCode.TEATCHER+"' ");
		if(StringUtils.isNotBlank(teacherId)){
			query.append(" and uor.user_ID = :teacherId ");
			params.put("teacherId", teacherId);
		}
		String count ="select count(*) from ( " + query.toString() + " ) countall ";
		query.append(" limit "+start+","+dataPackage.getPageSize());
		List<Map<Object, Object>> orgTeacherInfoVos = organizationDao.findMapBySql(query.toString(), params);
		for(Map<Object, Object> vo:orgTeacherInfoVos){
			OrgTeacherInfoVo orgTeacherInfoVo = new OrgTeacherInfoVo();
			orgTeacherInfoVo.setTeacherId(String.valueOf(vo.get("teacherId")));
			orgTeacherInfoVo.setOrganizationId(String.valueOf(vo.get("organizationId")));
			orgTeacherInfoVo.setOrganizationName(String.valueOf(vo.get("organizationName")));
			orgTeacherInfoVo.setLevelNo(String.valueOf(vo.get("levelNo")));
			result.add(orgTeacherInfoVo);
		}
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Integer totalCount = organizationDao.findCountSql(count, params);
		Integer pageSize = dataPackage.getPageSize();
	    Integer totalPage = totalCount / pageSize;  
        if (totalCount % pageSize > 0) {  
        	totalPage++;  
        } 
		resultMap.put("totalPage",totalPage );
		resultMap.put("totalCount", totalCount);
		resultMap.put("item", result);
		map.put("resultStatus", 200);
		map.put("resultMessage", "教师所属组织架构列表");
		map.put("result", resultMap);
		return map;
	}
	
	@Override
	public Map<String, Object> getOrgsAndTeachers(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder();
		query.append(" select concat(o.id,'') as id,o.`name`,o.parentID as parentId,o.orgType as orgTypeId,o.orgLevel as levelNo,o.orgOrder,o.city_id as regionId,r.`name` as regionName ");
		query.append(" ,count(DISTINCT u.user_id) AS teacherNum ");
		query.append(" from organization o left join region r on o.city_id = r.id ");
		query.append(" left join  (SELECT og.orgLevel,u.user_id FROM ");
		query.append(" user_dept_job udj,USER u,organization og where ");
		query.append(" udj.USER_ID = u.USER_ID ");
		query.append(" and udj.isMajorRole = 0 ");
		query.append(" and u.ENABLE_FLAG = '0' ");
		query.append(" and og.id=udj.dept_id ");
		query.append(" and u.USER_ID in (SELECT ur.userID from user_role ur WHERE ur.roleID in (SELECT r.id from role r WHERE r.roleCode='"
							+ RoleCode.TEATCHER + "' )) ");
		query.append(" ) u on u.orgLevel like CONCAT(o.orgLevel,'%')  ");
		query.append(" where 1=1 and o.parentId = :orgId group by o.id order by o.orgOrder ");
		params.put("orgId", orgId);
		
		List<Map<Object, Object>> org = organizationDao.findMapBySql(query.toString(), params);
		for(Map<Object, Object> object:org){
			if(object.get("orgTypeId")!=null){
				object.put("orgTypeName", OrganizationType.valueOf(object.get("orgTypeId").toString()).getName());
			}else{
				object.put("orgTypeName", "");
			}
		}
		query.delete(0, query.length());
		
		query.append(" select u.USER_ID as id,u.`NAME` as `name`,uj.ID as jobId,uj.JOB_NAME as jobName from user_dept_job udj,`user` u,organization og,user_job uj where ");
		query.append(" udj.USER_ID = u.USER_ID ");
		query.append(" and udj.isMajorRole = 0 ");
		query.append(" and u.ENABLE_FLAG = '0' ");
		query.append(" and og.id=udj.dept_id ");
		query.append(" and udj.JOB_ID = uj.ID ");
		query.append(" and og.id = :orgId ");
		params.put("orgId", orgId);
		query.append(" and u.USER_ID in (SELECT ur.userID from user_role ur WHERE ur.roleID in (SELECT r.id from role r WHERE r.roleCode='TEATCHER' )) ");
		List<Map<Object, Object>> teacher = organizationDao.findMapBySql(query.toString(), params);
		resultMap.put("org", org);
		resultMap.put("teacher", teacher);	
		map.put("resultStatus", 200);
		map.put("resultMessage", "组织架构及其所属人员列表");
		map.put("result", resultMap);
		return map;
	}

	@Override
	public Map<String, Object> getOrgsAndTeachersNew(String orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder();
		Organization o = organizationDao.findById(orgId);
		query.append(" SELECT");
		query.append(" 	concat(o.id, '') AS id,");
		query.append(" 	o.`name`,");
		query.append(" 	o.parentID AS parentId,");
		query.append(" 	o.orgType AS orgTypeId,");
		query.append(" 	o.orgLevel AS levelNo,");
		query.append(" 	o.orgOrder,");
		query.append(" 	o.city_id AS regionId,");
		query.append(" 	re.`name` AS regionName,");
		query.append(" 	count(DISTINCT u.user_id) AS teacherNum");
		query.append(" FROM");
		query.append(" 		USER u");
		query.append(" LEFT JOIN user_organization_role uor ON uor.user_id = u.USER_ID");
		query.append(" LEFT JOIN organization o ON uor.organization_id = o.id");
		query.append(" LEFT JOIN role r ON r.id = uor.role_id");
		query.append(" LEFT JOIN region re ON o.city_id = re.id");
		query.append(" WHERE");
		query.append(" 	1 = 1 and uor.is_main=0 and u.ENABLE_FLAG = '0' ");
		query.append(" and EXISTS (select 1 from user_organization_role oo LEFT JOIN role rr ON rr.id = oo.role_id where oo.user_id = u.USER_ID and rr.roleCode='TEATCHER') ");
		query.append(" and o.parentID = '"+orgId+"' ");
		query.append(" GROUP BY o.id");
		query.append(" ORDER BY o.orgOrder");

		List<Map<Object, Object>> org = organizationDao.findMapBySql(query.toString(), params);
		for(Map<Object, Object> object:org){
			if(object.get("orgTypeId")!=null){
				object.put("orgTypeName", OrganizationType.valueOf(object.get("orgTypeId").toString()).getName());
			}else{
				object.put("orgTypeName", "");
			}
		}
		query.delete(0, query.length());
		query.append(" SELECT");
		query.append(" 	u.USER_ID AS id,");
		query.append(" 	u.`NAME` AS `name`,");
		query.append(" 	r.ID AS jobId,");
		query.append(" 	r.name AS jobName");
		query.append(" FROM");
		query.append(" 	USER u");
		query.append(" LEFT JOIN user_organization_role uor ON uor.user_id = u.USER_ID");
		query.append(" LEFT JOIN organization o ON uor.organization_id = o.id");
		query.append(" LEFT JOIN role r ON r.id = uor.role_id");
		query.append(" LEFT JOIN region re ON o.city_id = re.id");
		query.append(" WHERE");
		query.append(" 	1 = 1");
		query.append(" AND uor.is_main = 0");
		query.append(" AND u.ENABLE_FLAG = '0'");
		query.append(" and EXISTS (select 1 from user_organization_role oo LEFT JOIN role rr ON rr.id = oo.role_id where oo.user_id = u.USER_ID and rr.roleCode='TEATCHER')");
		query.append(" and o.parentID = '"+orgId+"' ");
		List<Map<Object, Object>> teacher = organizationDao.findMapBySql(query.toString(), params);
		resultMap.put("org", org);
		resultMap.put("teacher", teacher);
		map.put("resultStatus", 200);
		map.put("resultMessage", "组织架构及其所属人员列表");
		map.put("result", resultMap);
		return map;
	}
	
	@Override
	public Organization fingOrgByOrgLevelAndOrgType(String orgLevel, String orgType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder query = new StringBuilder();
		query.append(" select * from organization where orgType = :orgType and orgLevel = :orgLevel ");
		params.put("orgType", orgType);
		params.put("orgLevel", orgLevel);
		List<Organization> organizations = organizationDao.findBySql(query.toString(), params);
		if(organizations!=null && organizations.size()>0){
			return organizations.get(0);			
		}else{
			return null;
		}
		
	}
	
	@Override
	public List<Organization> getOrganizationsByParentOrgIdAndType(String orgId, OrganizationType orgType) {
		return organizationDao.getOrganizationsByParentOrgIdAndType(orgId, orgType);
	}

	@Override
	public Organization findBrenchById(String id) {
		// TODO Auto-generated method stub
		return findBrenchByOrganizationId(id);
	}

	@Override
	public List<Organization> getAllParentOrganizationsByOrgLevel(String orgLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="SELECT * from organization where :orgLevel LIKE concat(orgLevel,'%')"+
				" and orgLevel <> :orgLevel2 ORDER BY LENGTH(orgLevel),orgOrder ";
		params.put("orgLevel", orgLevel);
		params.put("orgLevel2", orgLevel);
		return organizationDao.findBySql(sql, params);
	}

	@Override
	public List<Organization> getAllChildOrganizationsByOrgLevel(String orgLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql="SELECT * from organization where orgLevel like :orgLevel "+
				" and orgLevel <> :orgLevel2 ORDER BY LENGTH(orgLevel),orgOrder ";
		params.put("orgLevel", orgLevel + "%");
		params.put("orgLevel2", orgLevel);
		return organizationDao.findBySql(sql, params);
	}

	@Override
	public Integer getChildNodeNumsByOrgId(String orgId, String[] orgIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "SELECT COUNT(1) as nodesNum from organization o WHERE o.parentID = :orgId and o.id in(:orgIds)";
		params.put("orgId", orgId);
		params.put("orgIds", orgIds);
		List<Map<Object, Object>> maps = organizationDao.findMapBySql(sql, params);
//		return (Integer) maps.get(0).get("nodesNum");
		BigInteger bigInteger=(BigInteger) maps.get(0).get("nodesNum");
		return bigInteger.intValue();
	}

	/**
	 * 获取所有集团和分公司
	 */
	@Override
	public List<Organization> getGroupAndAllBrench() {
		List<Organization> list = organizationDao.getGroupAndAllBrench();
		return list;
	}

    @Override
    public List<Organization> findAllBrenchByUserId(String userId) {
			List<Organization> campus =organizationDao.findOrganizationByUserId(userId);
			List<Organization> brenchList =new ArrayList<>();
			for(Organization o :campus){
				Organization brench=findBrenchByOrganizationId(o.getId());
				if(brench!=null && !brenchList.contains(brench)){
					brenchList.add(brench);
				}
			}
			return brenchList;
    }


    public Organization findBrenchByOrganizationId(String organizationId){
		Organization o =organizationDao.findById(organizationId);
		if(o!=null && o.getOrgType().equals(OrganizationType.BRENCH)){
			return o;
		}else if(o!=null && !o.getOrgType().equals(OrganizationType.GROUNP)){
			return findBrenchByOrganizationId(o.getParentId());
		}
		return null;
	}

	/**
	 * 获取指定分公司下一级的校区和合起来的部门
	 *
	 * @param organizationId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getDeptAndCampusByBranchId(String organizationId) {
		Organization branch = organizationDao.findById(organizationId);
		StringBuffer department = new StringBuffer();
		department.append(" select count(*) from organization where parentID=:branchId and orgType ='DEPARTMENT' ");
		Map<String, Object> map = new HashMap<>();
		map.put("branchId", organizationId);
		int countDept = organizationDao.findCountSql(department.toString(), map);
		List<Map<String, Object>> result = new ArrayList<>();
		if (countDept>0){
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("organizationId", organizationId);
			item.put("organizationName", branch.getName()+"部门");
			result.add(item);
		}
		StringBuffer campusSql = new StringBuffer();
		campusSql.append(" select * from organization where parentID=:branchId and orgType='CAMPUS' ");
		List<Organization> campusList = organizationDao.findBySql(campusSql.toString(), map);
		for (Organization o : campusList){
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("organizationId", o.getId());
			item.put("organizationName", o.getName());
			result.add(item);
		}
		return result;
	}

	/**
	 * 获取所有分公司
	 *
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getAllBranch() {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from organization where 1=1 ");
		sql.append(" and orgType='BRENCH' ");
		List<Organization> branchList = organizationDao.findBySql(sql.toString(), new HashMap<String, Object>());
		List<Map<String, Object>> result = new ArrayList<>();
		for (Organization o : branchList){
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("organizationId", o.getId());
			item.put("organizationName", o.getName());
			result.add(item);
		}
		return result;
	}

	/**
	 * 组织机构 紧急状态切换列表
	 *
	 * @param branchId
	 * @param campusId
	 * @param stateOfEmergency
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findOrganization(String branchId, String campusId, String stateOfEmergency) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select * from organization where 1=1 ");
		sql.append(" and orgType='BRENCH' ");
		if (StringUtil.isNotBlank(branchId)){
			sql.append(" and id =:branchId ");
			params.put("branchId", branchId);
		}

		Map<String, Object> map = new HashMap();

		List<Map<String, Object>> result = new ArrayList<>();

		List<Organization> branchList = organizationDao.findBySql(sql.toString(), params);
		for (Organization o : branchList){
			Map<String, Object> item = new LinkedHashMap<>();
			item.put("branchName", o.getName());//分公司名字
			item.put("branchId", o.getId());//分公司id
			List<OrganizationEmergency> deptAndCampusList = new ArrayList<>();

			//分公司部门---start
			if (StringUtil.isBlank(campusId)||(StringUtil.isNotBlank(campusId) && campusId.equals(o.getId()))){

				OrganizationEmergency branchDepartment = new OrganizationEmergency();
				branchDepartment.setOrganizationId(o.getId());
				branchDepartment.setOrganizationName(o.getName()+"部门");
				branchDepartment.setStateOfEmergency(o.getStateOfEmergency());

				if (StringUtil.isNotBlank(stateOfEmergency)){
					if (StateOfEmergency.valueOf(stateOfEmergency)==o.getStateOfEmergency()){
						deptAndCampusList.add(branchDepartment);
					}
				}else {
					deptAndCampusList.add(branchDepartment);
				}

			}

			//分公司部门---end


			//校区--start
			StringBuffer campusSql = new StringBuffer();
			campusSql.append(" select * from organization where parentID=:branchId and orgType='CAMPUS' ");
			map.put("branchId", o.getId());
			if (StringUtil.isNotBlank(campusId)){
				campusSql.append(" and id=:campusId ");
				map.put("campusId", campusId);
			}
			if (StringUtil.isNotBlank(stateOfEmergency)){
				campusSql.append(" and state_of_emergency=:stateOfEmergency ");
				map.put("stateOfEmergency", stateOfEmergency);
			}
			List<Organization> campusList = organizationDao.findBySql(campusSql.toString(), map);
			for (Organization campus : campusList){
				OrganizationEmergency branchDepartment = new OrganizationEmergency();
				branchDepartment.setOrganizationId(campus.getId());
				branchDepartment.setOrganizationName(campus.getName());
				branchDepartment.setStateOfEmergency(campus.getStateOfEmergency());
				deptAndCampusList.add(branchDepartment);
			}
			//校区--end

			item.put("organizations", deptAndCampusList);
			int normal = 0;
			int emergency =0;
			for (OrganizationEmergency i : deptAndCampusList){
				if (i.getStateOfEmergency()== StateOfEmergency.EMERGENCY){
					emergency++;
				}
				if (i.getStateOfEmergency()== StateOfEmergency.NORMAL){
					normal++;
				}
			}
			item.put("normal", normal);
			item.put("emergency", emergency);
			if (deptAndCampusList.size()>0){
				result.add(item);
			}
		}
		return result;
	}

	/**
	 * 更新单个
	 *
	 * @param organizationId
	 */
	@Override
	public void updateOrgStatusById(String organizationId) {

		Organization organization = organizationDao.findById(organizationId);
		Map<String, Object> map = new HashMap<>();
		map.put("organizationId", organizationId);
		List<String> userOrganizationList = new ArrayList<>();
		if (organization.getOrgType()==OrganizationType.BRENCH){
			StringBuffer sql = new StringBuffer();
			sql.append(" select * from organization where orgType='DEPARTMENT' and  parentID=:organizationId ");
			List<Organization> list1 = organizationDao.findBySql(sql.toString(), map);
			StateOfEmergency stateOfEmergency = organization.getStateOfEmergency();
			StateOfEmergency newStateOfEmergency = null;
			if (stateOfEmergency == StateOfEmergency.NORMAL){
				newStateOfEmergency = StateOfEmergency.EMERGENCY;
			}else {
				newStateOfEmergency = StateOfEmergency.NORMAL;
			}
			organization.setStateOfEmergency(newStateOfEmergency);
			organizationDao.save(organization);
			if (list1.size()>0){
				for (Organization branchDeptItem : list1){
					branchDeptItem.setStateOfEmergency(newStateOfEmergency);
					userOrganizationList.add(branchDeptItem.getId());
					organizationDao.save(branchDeptItem);
				}
			}
		}

		if (organization.getOrgType() == OrganizationType.CAMPUS){
			StateOfEmergency stateOfEmergencyCampus = organization.getStateOfEmergency();
			StateOfEmergency newCampusStateOfEmergency = null;
			if (stateOfEmergencyCampus == StateOfEmergency.NORMAL){
				newCampusStateOfEmergency = StateOfEmergency.EMERGENCY;
			}else {
				newCampusStateOfEmergency = StateOfEmergency.NORMAL;
			}
			StringBuffer campusSql = new StringBuffer();
			campusSql.append(" select * from organization where orgLevel like :orgLevel ");
			Map<String, Object> campusMap = new HashMap<>();
			campusMap.put("orgLevel", organization.getOrgLevel()+"%");
			List<Organization> campusList = organizationDao.findBySql(campusSql.toString(), campusMap);
			for (Organization o : campusList){
				o.setStateOfEmergency(newCampusStateOfEmergency);
				userOrganizationList.add(o.getId());
				organizationDao.save(o);
			}
		}
		if (userOrganizationList.size()>0){
			deleteKeyFromRedisByList(userOrganizationList);
		}
	}

	/**
	 *
	 * @param userOrganizationList
	 */
	private void deleteKeyFromRedisByList(List<String> userOrganizationList) {
		StringBuffer sql = new StringBuffer();
		if(org.apache.commons.lang3.StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			sql.append(" select distinct CONCAT(user_ID, '') userID from user_organization_role WHERE  organization_ID in (:list) ");
		}else{
			sql.append(" select distinct CONCAT(userID, '') userID from user_organization WHERE  organizationID in (:list) ");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("list", userOrganizationList);
		List<Map<Object, Object>> ls = userOrganizationDao.findMapBySql(sql.toString(), map);
		for (Map m:ls){
			String userId = (String)m.get("userID");
			JedisUtil.del(Constants.STATUS_EMERGENCY_+userId);
		}
	}

	/**
	 * 更新分公司下面的所有组织架构状态
	 *
	 * @param branchId
	 * @param stateOfEmergency
	 */
	@Override
	public void updateBatchOrgStatusById(String branchId, String stateOfEmergency) {
		Organization organization = organizationDao.findById(branchId);

		StateOfEmergency newStateOfEmergency = StateOfEmergency.valueOf(stateOfEmergency);

		List<String> userOrganizationList = new ArrayList<>();
		if (organization.getOrgType() == OrganizationType.BRENCH){
			StringBuffer sql = new StringBuffer();
			sql.append(" select * from organization where orgLevel like :orgLevel ");
			Map<String, Object> map = new HashMap<>();
			map.put("orgLevel", organization.getOrgLevel()+"%");
			List<Organization> organizations = organizationDao.findBySql(sql.toString(), map);
			for (Organization o:organizations){
				userOrganizationList.add(o.getId());
				o.setStateOfEmergency(newStateOfEmergency);
				organizationDao.save(o);
			}
		}
		if (userOrganizationList.size()>0){
			deleteKeyFromRedisByList(userOrganizationList);
		}
	}

	@Override
    public void saveOrUpdateSchoolArea(Organization org) {
        SchoolAreaRpcVo schoolArea = new SchoolAreaRpcVo();
        schoolArea.setAddress(org.getAddress());
        if (StringUtil.isNotBlank(org.getCityId())) {
            Region region = regionService.getRegionById(org.getCityId());
            schoolArea.setCityName(region.getName());
            schoolArea.setCityNo(org.getRegionId());
        }
        if (StringUtil.isNotBlank(org.getProvinceId())) {
            Region region = regionService.getRegionById(org.getProvinceId());
            schoolArea.setProvinceName(region.getName());
            schoolArea.setProvinceNo(org.getProvinceId());
        }
        if (StringUtil.isNotBlank(org.getLat())) {
            schoolArea.setLat(org.getLat());
            schoolArea.setLon(org.getLon());
        } else {
            schoolArea.setLat("35.029996");
            schoolArea.setLon("63.632812");
        }
        schoolArea.setName(org.getName());
        if (PropertiesUtils.getStringValue("institution").equals("xinghuo")) {
            schoolArea.setOrgSysOrg("XINGHUO");
        } else {
            schoolArea.setOrgSysOrg("ADVANCE");
        }
        schoolArea.setRelateOrgNo(org.getId());
        schoolArea.setServicePhone(org.getContact());
        if (!bossSchoolAreaRpc.saveOrUpdateSchoolArea(schoolArea)) {
			logger.error("bossSchoolAreaRpc---校区修改同步失败" + org.getName());
        }
    }

	@Override
	public Response getCampusIdListDetail(LiveCapmusVo vos) {
		// TODO Auto-generated method stub
		Response response = new Response(200,"");
		if(vos==null||vos.getIds()==null||vos.getIds().size()<=0) {
			response.setResultCode(ErrorCode.LIVE_PARAM_EMPTY.getErrorCode());
			response.setResultMessage(ErrorCode.LIVE_PARAM_EMPTY.getErrorString());
			return response;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from organization where id in(:ids) ");
		params.put("ids", vos.getIds().toArray());
		List<Organization> organizations = organizationDao.findBySql(sql.toString(), params);
		Map<String, Object> result = new HashMap<String, Object>();
		for(Organization org : organizations) {
			Map<String, String> r = new HashMap<String, String>();
			r.put("name", org.getName());
			result.put(org.getId(),r);
		}
		response.setData(result);
		return response;
	}

	@Override
	public List<OranizationRelationVo> getAllBranchCampus() {
		// TODO Auto-generated method stub
		List<Organization> branchs = organizationDao.findByCriteria(Restrictions.eq("orgType",OrganizationType.BRENCH));
		List<Organization> campus = organizationDao.findByCriteria(Restrictions.eq("orgType",OrganizationType.CAMPUS));
		
		List<OranizationRelationVo> list = new ArrayList<>() ;
		
		OranizationRelationVo  vo = null;
		OranizationRelationVo  campusVo = null;
		for(Organization branchsOrg : branchs) {
			vo = new OranizationRelationVo();
			vo.setId(branchsOrg.getId());
			vo.setName(branchsOrg.getName());
			vo.setLevel(branchsOrg.getOrgLevel());
			vo.setChildren(new ArrayList<OranizationRelationVo>());
			for(Organization campusOrg : campus) {
				if(branchsOrg.getId().equals(campusOrg.getParentId())) {
					campusVo = new OranizationRelationVo();
					campusVo.setId(campusOrg.getId());
					campusVo.setName(campusOrg.getName());
					campusVo.setLevel(campusOrg.getOrgLevel());
					vo.getChildren().add(campusVo);
				}
			}
			
			list.add(vo);
		}
		
		return list;
	}

    @Override
    public void updateOrgnizationByHrms(String data){
		HttpClient client = HttpHeadersUtils.wrapHttpClient();
		String user = PropertiesUtils.getStringValue("HRMS_SECRET_ACCOUNT");
		String key = PropertiesUtils.getStringValue("HRMS_SECRET_KEY");
		String url =PropertiesUtils.getStringValue("HRMS_HOST")+"/hrms/syncOuter/getOrganizationById?id="+data;
		HttpGet getrequest= (HttpGet) HttpHeadersUtils.setLoginHeader("application/json", RequestMethod.GET,url,user,key);

		HttpResponse getResponse = null;
		try {
			getResponse = client.execute(getrequest);
		} catch (IOException e) {
			logger.error("访问人事服务器有问题"+url);
			e.printStackTrace();
		}
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = "";
				try {
					str = EntityUtils.toString(getResponse.getEntity());
				} catch (IOException e) {
					logger.error("同步人事组织架构转换参数有问题："+getResponse.getEntity());
					e.printStackTrace();
				}
				logger.info("同步人事组织架构信息："+str);
				Gson g = new Gson();
				OrganizationHrms hrms = g.fromJson(str,OrganizationHrms.class);
				organizationHrmsDao.save(hrms);
				updateBossOrganization(hrms);
		}else{
			throw new ApplicationException("同步组织架构访问人事系统错误！"+url);
		}
    }

	@Override
	public void pushAllOrganizationInfo(MessagePushVo vo) {
		HttpClient client = HttpHeadersUtils.wrapHttpClient();
		String user = PropertiesUtils.getStringValue("HRMS_SECRET_ACCOUNT");
		String key = PropertiesUtils.getStringValue("HRMS_SECRET_KEY");
		String url =PropertiesUtils.getStringValue("HRMS_HOST")+"/hrms/syncOuter/getAllOrg";
		HttpGet getrequest= (HttpGet) HttpHeadersUtils.setLoginHeader("application/json", RequestMethod.GET,url,user,key);

		HttpResponse getResponse = null;
		try {
			getResponse = client.execute(getrequest);
		} catch (IOException e) {
			logger.error("访问人事服务器有问题"+url);
			e.printStackTrace();
		}
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = "";
			try {
				str = EntityUtils.toString(getResponse.getEntity());
			} catch (IOException e) {
				logger.error("同步人事组织架构转换参数有问题："+getResponse.getEntity());
				e.printStackTrace();
			}
			logger.info("同步人事组织架构信息："+str);
			Gson g = new Gson();
			List<OrganizationHrms> list = g.fromJson(str,new TypeToken<List<OrganizationHrms>>() {
			}.getType());

			for(OrganizationHrms org :list){
				logger.info("同步组织架构："+g.toJson(org));
				organizationHrmsDao.save(org);
				updateBossOrganization(org);
			}

		}else{
			throw new ApplicationException("同步组织架构访问人事系统错误！"+url);
		}
	}

	@Override
	public Response saveHrmsOrgToBoss(String id) {
    		Response res = new Response();
			OrganizationHrms hrms = organizationHrmsDao.findById(id);

			if(hrms.getOrgType().equals(OrganizationTypeHrms.REGION)){
				res.setResultCode(-1);
				res.setResultMessage("省区暂时不能同步到BOSS，敬请期待");
				return res;
			}

			Organization organization = organizationDao.findOrganizationByHrmsId(id);
			if(organization!=null){
				if(StringUtils.isNotBlank(organization.getParentId())){
					Organization parent= organizationDao.findById(organization.getParentId());
					if(parent.getBossUse()!=0){
						throw new ApplicationException("父节点“"+parent.getName()+"”没有同步到组织架构信息到BOSS，请先勾选父节点");
					}
				}

				organization.setHrmsOrganizationInfo(hrms);
			}else{
				//新使用到BOSS的组织架构，处理组织架构类型，级别，
				organization = new Organization();
				organization.setHrmsOrganizationInfo(hrms);
				organization.setName(hrms.getName());
				setOrgTypeAndLevel(hrms,organization);
			}

			organization.setHrmsId(hrms.getId());
			organization.setBossUse(Constants.NUM_ZERO);//boss 使用
			organization.setHasAppend(Constants.NUM_ZERO);// 默认有附加信息
			organizationDao.save(organization);

			if(organization.getOrgType().equals(OrganizationType.BRENCH) || organization.getOrgType().equals(OrganizationType.CAMPUS)|| organization.getOrgType().equals(OrganizationType.GROUNP)){
				organization.setBelong(organization.getId());
			}else{
				organization.setBelong(organization.getParentId());
			}

			OrganizationModifyLog log = new OrganizationModifyLog(hrms.getId(),"勾选组织架构",userService.getCurrentLoginUserId());
			organizationModifyLogDao.save(log);
			return res;
	}

	@Override
	public Response cacelHrmsOrgInBoss(String id) {
    	Response res = new Response();
		Organization organization = organizationDao.findOrganizationByHrmsId(id);
		if(organization==null){
			res.setResultCode(-1);
			res.setResultMessage("BOSS找不到对应的组织架构信息，请确认！");
			return res;
		}
		organization.setBossUse(1);
		organizationDao.save(organization);

		List<Organization> sonOrg = organizationDao.findOrganizationByOrgLevel(organization.getOrgLevel());
		for(Organization son:sonOrg){
			son.setBossUse(1);
			organizationDao.save(son);
		}

		OrganizationModifyLog log = new OrganizationModifyLog(organization.getHrmsId(),"取消勾选组织架构",userService.getCurrentLoginUserId());
		organizationModifyLogDao.save(log);
		return res;
	}

	@Override
	public Response getBossOrganizationList(OrganizationSearchDto dto) {
		Response res = new Response();
		List<Map> list;
		if(StringUtils.isNotBlank(dto.getType())) {
			list = organizationDao.getOrganizationByCondition(dto);
			for(Map map :list){
				List l= organizationDao.getOrgByParentId(map.get("id").toString());
				map.put("hasChildren",l.size());
			}
		}else{
			list = organizationHrmsDao.getOrganizationByCondition(dto);
			for(Map map :list){
				List l= organizationHrmsDao.getOrgByParentId(map.get("id").toString());
				map.put("hasChildren",l.size());
			}
		}

		res.setData(list);
		return res;
	}

    @Override
    public Response getOrganizationById(String id) {
        Response res = new Response();
        List list  = organizationHrmsDao.getOrganizationById(id);
        if(list.size()>0){
            res.setData(list.get(0));
        }else{
            res.setResultCode(-1);
            res.setResultMessage("查不到对应的信息，可能还在同步中，请稍后再试!");
        }

        return res;
    }

	@Override
	public Response getOrganizationAppendInfoById(String id) {
    	Response res  = new Response();
    	Organization org = organizationDao.findOrganizationByHrmsId(id);
    	if( org != null){
    		Map map = new HashMap();
    		map.put("bossId",org.getId());
    		map.put("province",org.getProvinceId());
			map.put("cityId",org.getCityId());
			map.put("contact",org.getContact());
			map.put("contactUser",org.getContactUser());
			map.put("address",org.getAddress());
			map.put("lat",org.getLat());
			map.put("lon",org.getLon());
			map.put("trafficInfo",org.getTrafficInfo());
			map.put("hasAppend",org.getHasAppend());
			map.put("fileList",organizationFileDao.getAllContentFileByOrgId(org.getId()));
			res.setData(map);
		}else{
    		res.setResultCode(-1);
    		res.setResultMessage("找不到对应的信息！");
		}
		return res;
	}

	@Override
	public Response saveBossOrganizationInfo(OrganizationAppendDto dto) {
    	Response res = new Response();
    	if(StringUtil.isEmpty(dto.getId()) || StringUtil.isEmpty(dto.getModifyType())){
    		res.setResultCode(-1);
    		res.setResultMessage("参数有误，组织架构ID或者修改类型未传！");
    		return res;
		}

		Organization org  = organizationDao.findById(dto.getId());

    	if(org==null){
			res.setResultCode(-1);
			res.setResultMessage("找不到对应的组织架构信息！");
			return res;
		}

		switch (dto.getModifyType())
		{
			case Constants.INFO:
				dto.setNormalInfo(org);
				break;
			case Constants.APPEND:
				dto.setAppendInfo(org);
				break;
			case Constants.RESOURCE_POOL:
				//dto.setAppendInfo(org);
				if(OrganizationType.GROUNP != org.getOrgType()) {
					res = resourcePoolService.saveResourcePoolNew(dto.getResourcePoolVo());
				}
				return res;
				//break;
			case Constants.ALL:
				dto.setNormalInfo(org);
				dto.setAppendInfo(org);
				if(OrganizationType.GROUNP != org.getOrgType()) {
					res = resourcePoolService.saveResourcePoolNew(dto.getResourcePoolVo());
					if(res.getResultCode() !=0) {
						return res;
					}
				}
				break;
			default:
				res.setResultCode(-1);
				res.setResultMessage("参数有误，修改类型参数异常！");
				res.setData(dto.getModifyType());
				return res;
		}
		organizationDao.save(org);

    	if(dto.getFileList().size()>0){
			List<OrganizationFile> list = organizationFileDao.getAllContentFileByOrgId(org.getId());
			Map<Integer,Integer> newMap = new HashMap();
			for (OrganizationFile file : dto.getFileList()){
				if(file.getId()!=null && file.getId()>0){
					newMap.put(file.getId(),file.getId());
				}else {
					organizationFileDao.save(file);
				}
			}

			for(OrganizationFile file :list ){
				if(newMap.get(file.getId())==null){
					organizationFileDao.delete(file);
				}
			}
		}

		return res;
	}


	/**
	 * 设置组织架构类型，（校区分公司就直接使用校区分公司，其他类型全部转为部门）
	 * 组织架构级别，所有转为部门的节点，父类设置为往上找父类找到小区或者分公司或者集团为止。级别跟父类走。
	 * belong 归属设置为自己。
	 * @param hrms
	 * @param organization
	 */
	public void setOrgTypeAndLevel(OrganizationHrms hrms,Organization organization){

		if(PropertiesUtils.getStringValue("PROJECT").equals("XHJY")){
			if(hrms.getOrgType().equals(OrganizationTypeHrms.BRANCH)){
				organization.setOrgType(OrganizationType.BRENCH);
			}else if(hrms.getOrgType().equals(OrganizationTypeHrms.CAMPUS)){
				organization.setOrgType(OrganizationType.CAMPUS);
			}else{
				organization.setOrgType(OrganizationType.DEPARTMENT);
			}
		}else{//星火以外的项目用 分校 跟 教学点
			if(hrms.getOrgType().equals(OrganizationTypeHrms.SUB_CAMPUS)){
				organization.setOrgType(OrganizationType.BRENCH);
			}else if(hrms.getOrgType().equals(OrganizationTypeHrms.TEACHING_VENUE)){
				organization.setOrgType(OrganizationType.CAMPUS);
			}else{
				organization.setOrgType(OrganizationType.DEPARTMENT);
			}
		}

		Organization parentO = getParentOrganization(hrms.getParentId());
		organization.setParentId(parentO.getId());
		String orgLevel=organizationDao.getNextOrgLevel(parentO.getId(),parentO.getOrgLevel());
		organization.setOrgLevel(orgLevel);
	}


	/**
	 * 获取父类组织架构
	 * @param hrms_id
	 * @return
	 */
	public Organization getParentOrganization(String hrms_id){
		OrganizationHrms hrms= organizationHrmsDao.findById(hrms_id);
		//集团，分公司，校区就返回
		if(hrms!=null
				&&((PropertiesUtils.getStringValue("PROJECT").equals("XHJY") && (hrms.getOrgType().equals(OrganizationTypeHrms.BRANCH) || hrms.getOrgType().equals(OrganizationTypeHrms.CAMPUS) || hrms.getOrgType().equals(OrganizationTypeHrms.HIERARCHY)))
				|| (!PropertiesUtils.getStringValue("PROJECT").equals("XHJY") &&  (hrms.getOrgType().equals(OrganizationTypeHrms.SUB_CAMPUS) || hrms.getOrgType().equals(OrganizationTypeHrms.TEACHING_VENUE) || hrms.getOrgType().equals(OrganizationTypeHrms.HIERARCHY)))
			)){
			Organization o = organizationDao.findOrganizationByHrmsId(hrms.getId());
			if(o==null || o.getBossUse()!=0){
				throw new ApplicationException("父节点“"+hrms.getName()+"”没有同步到组织架构信息到BOSS，请先勾选父节点");
			}
			return o;
		}else{
			if(hrms==null){
				throw new ApplicationException("父节点“"+hrms.getName()+"”没有同步到组织架构信息到BOSS，请先勾选父节点");
			}
			return getParentOrganization(hrms.getParentId());
		}

	}


	/**
	 * 更新BOSS的组织架构
	 * @param org
	 */
	public void updateBossOrganization(OrganizationHrms org){
    	Organization organization = organizationDao.findOrganizationByHrmsId(org.getId());
    	if(organization!=null){
			organization.setHrmsOrganizationInfo(org);
			organizationDao.save(organization);
		}
	}


	@Override
	public Response getBossSelectOrganizationList(OrganizationSearchDto dto) {
		Response res = new Response();
		List<Map> list = organizationDao.getBossSelectOrganizationList(dto);

		Map parentMap = new HashMap<>();
		for(Map map :list){
			parentMap.put(map.get("id"),map.get("id"));
		}

		for(Map map :list){
			if(map.get("orgType")!=null && "DEPARTMENT".equals(map.get("orgType").toString())) {
				List l = organizationDao.getOrgByParentId(map.get("id").toString());
				map.put("hasChildren", l.size());
			}else{
				map.put("hasChildren", 1);
			}

			if(map.get("parentId")!=null && parentMap.get(map.get("parentId"))==null){
				map.put("parentId",null);
			}

		}
		res.setData(list);
		return res;
	}
}
