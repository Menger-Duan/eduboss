package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.MsgNo;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.ResourcePoolJobType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.SysMsgType;
import com.eduboss.common.ValidStatus;
import com.eduboss.dao.CustomerDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ProcedureDao;
import com.eduboss.dao.ResourcePoolDao;
import com.eduboss.dao.ResourcePoolJobDao;
import com.eduboss.dao.ResourcePoolRoleDao;
import com.eduboss.dao.UserDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.dao.UserJobDao;
import com.eduboss.domain.Customer;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Organization;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.ResourcePoolJob;
import com.eduboss.domain.ResourcePoolRole;
import com.eduboss.domain.Role;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.domain.UserOrganizationRole;
import com.eduboss.domainVo.ResourcePoolJobVo;
import com.eduboss.domainVo.ResourcePoolRoleVo;
import com.eduboss.domainVo.ResourcePoolUserVo;
import com.eduboss.domainVo.ResourcePoolVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.DistributableUserJobService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.UserDeptJobService;
import com.eduboss.service.UserService;
import com.eduboss.task.SendSysMsgCostomerDistributionThread;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service("ResourcePoolService")
public class ResourcePoolServiceImpl implements ResourcePoolService {

	private final static Logger log = Logger.getLogger(ResourcePoolServiceImpl.class);
	
	@Autowired
	private MobileUserService mobileUserService;
	
	@Autowired
	private ResourcePoolDao resourcePoolDao;
	
	@Autowired
	private ResourcePoolJobDao resourcePoolJobDao;
	
	@Autowired
	private ResourcePoolRoleDao resourcePoolRoleDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private UserDao userdao;
	
	@Autowired
	private UserDeptJobService userDeptJobService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private DistributableUserJobService distributableUserJobService;
	
	@Override
	public ResourcePool findResourcePoolById(String organizationId) {
		return resourcePoolDao.findById(organizationId);
	}

	@Override
	public void deleteResourcePool(ResourcePool resourcePool) {
		resourcePoolDao.delete(resourcePool);
	}

	@Override
	public void saveEditResourcePool(ResourcePool resourcePool) {
		if (StringUtil.isBlank(resourcePool.getCreateTime())) {
			resourcePool.setCreateTime(DateTools.getCurrentDateTime());
			resourcePool.setCreateUserId(userService.getCurrentLoginUser().getUserId());
		}
		resourcePool.setModifyTime(DateTools.getCurrentDateTime());
		resourcePool.setModifyUserId(userService.getCurrentLoginUser().getUserId());
		Organization organization = organizationDao.findById(resourcePool.getOrganizationId());
		organization.setResourcePoolName(resourcePool.getName());
		organization.setResourcePoolstatus(resourcePool.getStatus());
		organizationDao.save(organization);
		resourcePoolDao.save(resourcePool);
	}

	@Override
	public DataPackage getResourcePoolJobList(DataPackage dataPackage,
			ResourcePoolJobVo resourcePoolJobVo) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtil.isNotBlank(resourcePoolJobVo.getOrganizationId())) {
			criterionList.add(Restrictions.eq("organizationId",
					resourcePoolJobVo.getOrganizationId()));
		}
		if (null != resourcePoolJobVo.getType()) {
			criterionList.add(Restrictions.eq("type",
					resourcePoolJobVo.getType()));
		}
		
		dataPackage =  resourcePoolJobDao.findPageByCriteria(dataPackage,
				HibernateUtils.prepareOrder(dataPackage, "createTime", "desc"),
				criterionList);
		List<ResourcePoolJob> list = (List<ResourcePoolJob>) dataPackage.getDatas();
		List<ResourcePoolJobVo> voList = HibernateUtils.voListMapping(list, ResourcePoolJobVo.class);
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	@Override
	public void deleteResourcePoolJob(ResourcePoolJobVo resourcePoolJobVo) {
		ResourcePoolJob resourcePoolJob = HibernateUtils.voObjectMapping(resourcePoolJobVo, ResourcePoolJob.class);
		resourcePoolJobDao.delete(resourcePoolJob);
	}

	@Override
	public void saveEditResourcePoolJob(ResourcePoolJobVo resourcePoolJobVo, String oldUserJobId) {
		if (StringUtil.isNotBlank(oldUserJobId) && !oldUserJobId.equals(resourcePoolJobVo.getUserJobId())) {
//			ResourcePoolJob delResourcePoolJob = new ResourcePoolJob();
//			
//			delResourcePoolJob.setJobId(oldUserJobId);
//			delResourcePoolJob.setOrganizationId(resourcePoolJobVo.getOrganizationId());
//            delResourcePoolJob.setType(resourcePoolJobVo.getType());			
			//delResourcePoolJob.setId(resourcePoolJobVo.getId());
            Map<String, Object> params = Maps.newHashMap();
            params.put("orgId", resourcePoolJobVo.getOrganizationId());
            params.put("jobId", oldUserJobId);
            params.put("type", resourcePoolJobVo.getType().getValue());
            String sql = "delete from resource_pool_job where ORGANIZATION_ID = :orgId and JOB_ID = :jobId and TYPE = :type ";
			resourcePoolJobDao.excuteSql(sql, params);
            //resourcePoolJobDao.delete(delResourcePoolJob);
		}
		ResourcePoolJob resourcePoolJob = resourcePoolJobDao.findById(resourcePoolJobVo.getId());
		if(resourcePoolJob==null){
			resourcePoolJob = new ResourcePoolJob();
		}
		resourcePoolJob.setJobId(resourcePoolJobVo.getUserJobId());
		resourcePoolJob.setOrganizationId(resourcePoolJobVo.getOrganizationId());
		resourcePoolJob.setType(resourcePoolJobVo.getType());
		resourcePoolJob.setOneTimeResource(resourcePoolJobVo.getOneTimeResource());
		//ResourcePoolJob resourcePoolJob = HibernateUtils.voObjectMapping(resourcePoolJobVo, ResourcePoolJob.class);
		if (StringUtil.isBlank(resourcePoolJob.getCreateTime())) {
			resourcePoolJob.setCreateTime(DateTools.getCurrentDateTime());
			resourcePoolJob.setCreateUserId(userService.getCurrentLoginUser().getUserId());
		}
		resourcePoolJob.setModifyTime(DateTools.getCurrentDateTime());
		resourcePoolJob.setModifyUserId(userService.getCurrentLoginUser().getUserId());
		try {
			resourcePoolJobDao.save(resourcePoolJob);
		} catch (DataIntegrityViolationException e){
			throw new ApplicationException("不能添加重复可见职位！");
		}

	}
	
	@Override
	public List<Organization> getOrganizationListForResPool(String organizationId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="from Organization where 1=1 and status = 0 and bossUse = 0";
		Organization org=organizationDao.findById(organizationId);
		if(OrganizationType.BRENCH==org.getOrgType()){
			hql += " and orgType = '"+OrganizationType.GROUNP+"' ";
		}else if(OrganizationType.CAMPUS==org.getOrgType()){
			hql += " and orgType = '"+OrganizationType.BRENCH+"' ";
			hql += " and id in (select organizationId from ResourcePool) ";
		}else if(OrganizationType.DEPARTMENT==org.getOrgType()){
			hql += " and ( orgType = '"+OrganizationType.CAMPUS+"' or orgType = '" + OrganizationType.BRENCH + "') ";
			hql += " and id in (select organizationId from ResourcePool) ";
		}else{
			hql += " and orgType = '"+OrganizationType.GROUNP+"' ";
		}
		hql += " and id <> :organizationId ";
		params.put("organizationId", organizationId);
		hql+=" order by length(orgLevel),orgOrder";
		
		return organizationDao.findAllByHQL(hql, params);
	}
	
	/**
	 * 根据登陆用户权限查询部门以上的组织架构
	 */
	@Override
	public List<Map> getOrganizationByDepartmentAbove() {
		List<Map> returnList=new ArrayList<Map>();
		String hql="from Organization where orgType in ('"+OrganizationType.BRENCH+"','"+OrganizationType.CAMPUS+"','" +OrganizationType.DEPARTMENT +"','"+OrganizationType.GROUNP+"')";
		hql+=" order by length(orgLevel),orgOrder";
		List<Organization> planManagementList = organizationDao.findAllByHQL(hql, new HashMap<String, Object>());
		List<Organization> organizationList = organizationDao.getBelongOrg(organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId()));
		
		for (Organization po : planManagementList) {
			Map returnMap=new HashMap();
			returnMap.put("id", po.getId());
			returnMap.put("name", po.getName());
			returnMap.put("orgType", po.getOrgType());
			returnMap.put("parentId", po.getParentId());
			for (Organization o : organizationList) {
				if(o.getId().equals(po.getId()))
					returnMap.put("isShow",true);
			}
			
			if (!(po.getOrgType() == OrganizationType.BRENCH && StringUtil.isBlank(po.getParentId()))) {
				returnList.add(returnMap);
			}
			
		}
		
		return returnList;
	}
	
	/**
	 * 根据orgLevel 获取所有可分配的资源池和人(为空  默认当前登陆用户的)
	 */
	@Override
	public List<ResourcePoolUserVo> getResourcePoolAndUser(String organizationId) {
		List<ResourcePoolUserVo> resourcePoolVoList = new ArrayList<ResourcePoolUserVo>();
		List<ResourcePoolUserVo> resourcePoolLowerVoList = null;
		List<ResourcePoolUserVo> tmpList = null;
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			tmpList = getResourcePoolLower(organization.getOrgLevel(), false);
			if (tmpList != null) {
				resourcePoolVoList.addAll(tmpList);
			}
		} else {
			tmpList = getCustomerResourcePool(null);
			if (tmpList != null) {
				resourcePoolVoList.addAll(tmpList);
			}
			resourcePoolLowerVoList = getResourcePoolLower(null, true);
		}
		if(resourcePoolLowerVoList!=null && resourcePoolLowerVoList.size()>0){
			resourcePoolVoList.addAll(resourcePoolLowerVoList);
		}
//		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
//			resourcePoolVoList=removeDuplicate(resourcePoolVoList);
//		}
		
		List<User> userList = new ArrayList<User>();
//		String userName = null;
//		List<Role> roles = null;
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : resourcePoolVoList){
				userList.clear();
				if (StringUtil.isBlank(organizationId)) {
					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH || resourcePoolUserVo.getOrgLevel().length() <= 8) {
						userList = getUserByResourcePoolUser(resourcePoolUserVo);
					} else  if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
							|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
						resourcePoolUserVo.setParent(true);
					}
					
				} else {
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					userList = getUserByResourcePoolUser(resourcePoolUserVo);
					if (!isGetUserByOrganizationId) {
						ResourcePoolUserVo vo = new ResourcePoolUserVo();
						vo.setId(organizationId);
						userList.addAll(getUserByResourcePoolUser(vo));
						isGetUserByOrganizationId = true;
					}
				}
				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
//					userName = user.getName()+"(";
//					roles = userService.getRoleByUserId(user.getUserId());
//					for(int i=0; i<roles.size(); i++){
//						if(i==0){
//							userName += roles.get(i).getName();
//						}else{
//							userName += ","+roles.get(i).getName();
//						}
//					}
//					userName+=")";
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
//					userVo.setResourcePoolName(userName);
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}else if(StringUtil.isNotBlank(organizationId)){//如果是最后一层组织架构的话要处理这个逻辑。
				ResourcePoolUserVo vo = new ResourcePoolUserVo();
				vo.setId(organizationId);
				userList.addAll(getUserByResourcePoolUser(vo));
				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}
		return resourcePoolVoList; 
	}
	
	private List<User> getUserByResourcePoolUser(ResourcePoolUserVo resourcePoolUserVo) {
		DataPackage dataPackage = new DataPackage(0, 999);
		ResourcePoolJobVo resourcePoolJobVo = new ResourcePoolJobVo();
		resourcePoolJobVo.setOrganizationId(resourcePoolUserVo.getId());
		dataPackage = getResourcePoolJobList(dataPackage, resourcePoolJobVo);
		List<ResourcePoolJobVo> voList = (List<ResourcePoolJobVo>) dataPackage.getDatas();
		String userJobIds = "";
		if (voList != null && voList.size() > 0) {
			for (ResourcePoolJobVo vo : voList) {
				userJobIds += vo.getUserJobId() + ",";
			}
			userJobIds = userJobIds.substring(0, userJobIds.length() - 1);
		}
		return userService.getUserByBlcampusAndUserJobValidate(resourcePoolUserVo.getId(), userJobIds);
	}
	
	/**
	 * 根据orgLevel 获取所有上级资源池 (为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	public List<ResourcePoolUserVo> getCustomerResourcePool(String orgLevel){
		Organization organization = userService.getCurrentLoginUserOrganization();

		if(StringUtils.isBlank(orgLevel)){
			String belong = organization.getBelong();
			if(organization!=null && StringUtils.isNotBlank(belong)) {
				Organization belongOrg = organizationDao.findById(belong);
				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel())) {
					orgLevel = belongOrg.getOrgLevel();
				}
			}
//			if(organization!=null && StringUtils.isNotBlank(organization.getOrgLevel()))
//				orgLevel = organization.getOrgLevel();
		}
		if(StringUtils.isNotBlank(orgLevel)){
			List<Organization> organizationList = organizationDao.getOrganizationByResourcePool(orgLevel);
			if(organizationList != null && organizationList.size()>0){
				List<ResourcePoolUserVo> list =  HibernateUtils.voListMapping(organizationList, ResourcePoolUserVo.class);
				for (ResourcePoolUserVo resourcePoolUserVo : list) {
					if (!resourcePoolUserVo.getId().equals(organization.getId())) {
						resourcePoolUserVo.setResourcePoolstatus(ValidStatus.INVALID);
					}
				}
				return list;
			}
		}
		return null;
	}
	
	/**
	 * 根据orgLevel 获取所有下级资源池 (为空  默认当前登陆用户的) 不包括自己
	 * @param orgLevel
	 * @return
	 */
	public List<ResourcePoolUserVo> getResourcePoolLower(String orgLevel, boolean isCampusAbove){
		if(StringUtils.isBlank(orgLevel)){
			Organization organization = userService.getCurrentLoginUserOrganization();
			String belong = organization.getBelong();
			if(organization!=null && StringUtils.isNotBlank(belong)) {
				Organization belongOrg = organizationDao.findById(belong);
				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel())) {
					orgLevel = belongOrg.getOrgLevel();
				}
			}
//			if(organization.getOrgType()!=OrganizationType.BRENCH
//					&&organization.getOrgType()!=OrganizationType.CAMPUS
//					&&organization.getOrgType()!=OrganizationType.GROUNP){
//				
//				organization=userService.getOrganizationById(organization.getParentId());
//			}
//			if(organization!=null && StringUtils.isNotBlank(organization.getOrgLevel()))
//				orgLevel = organization.getOrgLevel();
		}
		if(StringUtils.isNotBlank(orgLevel)){
			List<Organization> organizationList = null;
			if (!isCampusAbove) {
				organizationList = organizationDao.getOrganizationByResourcePoolLower(orgLevel);
			} else {
				organizationList = organizationDao.getOrganizationCampusByResourcePoolLower(orgLevel);
			}
			if(organizationList != null && organizationList.size()>0){
				
				return HibernateUtils.voListMapping(organizationList, ResourcePoolUserVo.class);
			}
		}
		return null;
	}
	
	private List<ResourcePoolUserVo> removeDuplicate(List<ResourcePoolUserVo> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getId().equals(list.get(i).getId())) {
					list.remove(j);
				}
			}
		}
		
		return list;
	}
	
	private List<Organization> removeDuplicateOrganizaiton(List<Organization> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getId().equals(list.get(i).getId())) {
					list.remove(j);
				}
			}
		}		
		return list;
	}
	private List<UserJob> removeDuplicateUserJob(List<UserJob> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getId().equals(list.get(i).getId())) {
					list.remove(j);
				}
			}
		}		
		return list;
	}
	

		
	/**
	 * 查询资源池容量是否足够
	 */
	
	@Override
	public Response getResourcePoolVolume(int customerNums, String resourceId) {	
		Response res=new Response();	
		ResourcePool resourcePool=resourcePoolDao.findById(resourceId);
		Organization org=organizationDao.findById(resourceId);
		if(org!=null){
			
			if(resourcePool!=null){
				//分配到资源池
				BigDecimal capacity=resourcePool.getCapacity();
				int num=0;
				StringBuffer sql=new StringBuffer();
				sql.append(" select count(1) from customer where DELEVER_TARGET = '"+resourcePool.getOrganizationId()+"' and DEAL_STATUS NOT in ('SIGNEUP','INVALID')  ");
				num=resourcePoolDao.findCountSql(sql.toString(), new HashMap<String, Object>());
				BigDecimal allowNum=BigDecimal.ZERO;
				allowNum=capacity.subtract(BigDecimal.valueOf(num));	
				if(customerNums+num>capacity.intValue()){
					if(allowNum.intValue()<=0){
						//回流导致资源池总容量已经溢出
						res.setResultMessage("资源池容量已超出，不可再往里添加新资源");
						res.setResultCode(num);
						return res;
					}else{
						//分配客户数量超出允许数量，错误提示信息
						res.setResultMessage("资源池总共可分配"+capacity+"个,还可分配资源"+allowNum+"个,您现在选择的资源数（"+customerNums+"）超出已有规范 ,请重新选择");
						res.setResultCode(allowNum.intValue());
						return res;
					}
					
				}
			}else{
				res.setResultCode(-1); //没有配置资源池的情况
				res.setResultMessage("没有配置资源池");
				return res;
			}
		}
		else{
			//分配给某个人，取到这人所在职位一次可获取的最大容量数			
			User user=userService.getUserById(resourceId);
			ResourcePoolJob resourcePoolJob=new ResourcePoolJob();
			List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(resourceId);
			List<BigDecimal> resourceList=new ArrayList<BigDecimal>();
			List<ResourcePoolJob> list=new ArrayList<ResourcePoolJob>();
			
			//查询当前用户所属职位中的最大资源容量数
			StringBuffer sql=new StringBuffer();
			sql.append("select uj.* from user_dept_job udj INNER JOIN user_job uj on udj.job_id=uj.id  ");
			sql.append(" and udj.user_id='"+user.getUserId()+"' and uj.IS_CUSTOMER_FOLLOW='0' ORDER BY RESOURCE_NUM DESC ");
			List<UserJob> userJobList=userJobDao.findBySql(sql.toString(), new HashMap<String, Object>());
								
			
			//还可分配的资源数,判断用户有没有勾选主职位的客户资源跟进
			if(userJobList!=null && userJobList.size()>0 && userJobList.get(0)!=null){
				//查询当前用户已有资源数
				int count=resourcePoolJobDao.findCountSql(" select COUNT(1) from customer where DELEVER_TARGET='"+user.getUserId()+"' and DEAL_STATUS NOT in ('SIGNEUP','INVALID') ", new HashMap<String, Object>());				
				UserJob userJob=userJobList.get(0);
				int maxNums=userJob.getResourceNum();
				int overPlus=maxNums-count;
				if(customerNums+count>maxNums){
					if(overPlus<=0){
						//回流导致资源池总容量已经溢出
						res.setResultMessage("所属职位中最大资源池总容量已超出，不可再往里添加新资源");
						res.setResultCode(-2);
						return res;
					}else{
						//超出此人所在部门主职位的最大资源获取数
						res.setResultMessage("["+user.getName()+"]所属职位中最大资源池总容量是"+maxNums+"个,已经拥有的资源数是"+count+",还可再分配资源"+overPlus+"个，您现在选择的资源数（"+customerNums+"）超出已有规范,请重新选择 ");
						res.setResultCode(overPlus);
						return res;
					}
					
				}
			}else{
				//当前分配对象的主职位没有配置客户资源跟进
				res.setResultCode(-1);
				res.setResultMessage("客户分配对象没有配置资源池");
				return res;
			}
			
			if(userDeptJob!=null && userDeptJob.size()>0){
				for(UserDeptJob job:userDeptJob ){
					StringBuffer hql=new StringBuffer();
					//只判断到咨询师所在主职位一次可获取的最大容量
					hql.append(" select * from resource_pool_job where  type='VISIBLE' and ORGANIZATION_ID='"+user.getOrganizationId()+"' ");
					hql.append(" and JOB_ID in (SELECT JOB_ID from user_dept_job where JOB_ID='"+job.getJobId()+"' and USER_ID='"+user.getUserId()+"' and isMajorRole='0') ");
					list=resourcePoolJobDao.findBySql(hql.toString(), new HashMap<String, Object>());
					if(list!=null&&list.size()>0){
						 resourcePoolJob=list.get(0);
						 resourceList.add(resourcePoolJob.getOneTimeResource());
					}					
				}
			}else{
				//分配到的人没有主职位
				res.setResultCode(-1);
				return res;
			}
			if(resourceList!=null && resourceList.size()>0){
				Collections.sort(resourceList);
				BigDecimal oneTimeResource=resourceList.get(resourceList.size()-1);
				if(oneTimeResource.intValue()<customerNums){
					//分配客户数量超出允许数量，错误提示信息(弹框显示)
					res.setResultMessage("["+user.getName()+"]所属职位中一次最多可分配资源数是"+oneTimeResource+"个,您现在选择的资源数（"+customerNums+"）超出已有规范,请重新选择 ");
					res.setResultCode(oneTimeResource.intValue());
					return res;
				}
			}				
			
			
		}		
		return new Response();
		
	}
		

	/**
	 * 分配资源
	 */
	@Override
	public Response updateCustomerDeleverTarget(String customerIds,
			String resourceId) {
		String nowDateTime=DateTools.getCurrentDateTime();
		String[] cusIdArray=customerIds.split(",");
		
		String loginUserName = userService.getCurrentLoginUser().getName();
		//判断资源池容量是否足够
		Response res=this.getResourcePoolVolume(cusIdArray.length, resourceId);
		
		if(res.getResultCode()!=0 && StringUtil.isNotBlank(res.getResultMessage())){
			//不可以分配，不足够 
			return res;
		}else{
			//直接分配
			ResourcePool resourcePool=resourcePoolDao.findById(resourceId);
			String deliverType=new String();
			if(resourcePool!=null){
				deliverType="CUSTOMER_RESOURCE_POOL";  //公共池
			}else{
				//分配给个人
				deliverType="PERSONAL_POOL";
			}
			if(cusIdArray.length>0){
				String customerIdsForSql = "";
				for (String cusId: cusIdArray) {
					customerIdsForSql += "'" + cusId+ "',";
				}
				customerIdsForSql = customerIdsForSql.substring(0, customerIdsForSql.length()-1);
				//批量分配客户到资源池  ResultCode==0 跟进状态设为待跟进
				Map<String, Object> params = new HashMap<String, Object>();
				StringBuffer sql=new StringBuffer();
				sql.append(" update customer set DELEVER_TARGET = :resourceId, LAST_DELIVER_NAME='"+loginUserName+"',DELIVER_TIME='"+nowDateTime+"',DELIVER_TYPE='"+deliverType+"',MODIFY_TIME='"+nowDateTime+"',DEAL_STATUS='STAY_FOLLOW'  where id in ("+customerIdsForSql+") ");
				params.put("resourceId", resourceId);
				customerDao.excuteSql(sql.toString(), params);
			}
			if (resourcePool == null) { // 分配给个人，推送客户资源调配通知
				MobileUser mobileUser = mobileUserService.findMobileUserByStaffId(resourceId);
				if (mobileUser != null) { // 存在手机用户才发送
					User currentUser = userService.getCurrentLoginUser();
					SentRecord record = new SentRecord();
					record.setMsgNo(MsgNo.M13);
					record.setMsgName("客户资源调配");
					record.setMsgType(new DataDict("CUSTOMER_MSG"));
					List<Role> roles = userService.getCurrentLoginUserRoles();
					String mainRoleName = "";
					for (Role role : roles) {
						if (role.getRoleId().equals(currentUser.getRoleId())) {
							mainRoleName = role.getName();
							break;
						}
					}
					record.setMsgContent(mainRoleName + "-" + currentUser.getName() + "已分配" + cusIdArray.length + "位新的客户至您的客户列表，请及时对该客户进行跟进，或在pc端进行再次分配");
					record.setMsgRecipient(new User(resourceId));
					String currentUserId = currentUser.getUserId();
					record.setCreateUserId(currentUserId);
					record.setCreateTime(DateTools.getCurrentDateTime());
					record.setModifyUserId(currentUserId);
					record.setModifyTime(DateTools.getCurrentDateTime());
					record.setPushMsgType(PushMsgType.SYSTEM_NOTICE);
					record.setSysMsgType(SysMsgType.CUSTOMER);
					record.setSentTime(DateTools.getCurrentDateTime());
					this.sendCostomerDistributionSysMsg(record, mobileUser);
				}
			}
			return new Response();
		}
	}
	
	private void sendCostomerDistributionSysMsg(SentRecord record, MobileUser mobileUser) {
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgCostomerDistributionThread thread = new SendSysMsgCostomerDistributionThread(record, mobileUser);
		taskExecutor.execute(thread);
	}
	
	 /**
     * 资源定时回流
     */
	@Override
	 public void resourceCallback() throws SQLException{
		log.info("########## proc_customer_resource_back_org ########## " + "begin" );
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{call proc_customer_resource_back()}";
		procedureDao.executeProc(sql);
		log.info("########## proc_customer_resource_back_org ########## " + "end" );
	  }
	
	@Override
	public List<ResourcePoolUserVo> getValidateResourcePool() {
		String userId = userService.getCurrentLoginUser().getUserId();
		List<ResourcePoolUserVo> retList = new ArrayList<ResourcePoolUserVo>();
		
		List<UserDeptJob> userDeptJobList = userDeptJobDao.findDeptJobByUserId(userId);
		
		for (UserDeptJob userDeptJob : userDeptJobList) {
			Organization org = organizationDao.findById(userDeptJob.getDeptId());
			String orgLevel = org.getOrgLevel();
			String jobId = userDeptJob.getJobId();
			retList.addAll(organizationDao.getValidateResourcePool(orgLevel, jobId, ResourcePoolJobType.VISIBLE));
		}
		
		
		if (retList != null && retList.size() > 0) {
			retList=removeDuplicate(retList);
		}
		return retList;
	}
	
	
	
	
	
	public boolean checkResourcePoolByUserAndOrg(String userId, String organizationId, ResourcePoolJobType type, List<UserDeptJob> userDeptJobList) {
		DataPackage dp = new DataPackage(0, 999);
		ResourcePoolJobVo resourcePoolJobVo = new ResourcePoolJobVo();
		resourcePoolJobVo.setOrganizationId(organizationId);
		resourcePoolJobVo.setType(type);
		dp = getResourcePoolJobList(dp, resourcePoolJobVo);
		List<ResourcePoolJobVo> resourcePoolJobVoList = (List<ResourcePoolJobVo>) dp.getDatas();
		for (ResourcePoolJobVo vo : resourcePoolJobVoList) {
			for (UserDeptJob userDeptJob : userDeptJobList) {
				if (vo.getUserJobId().equals(userDeptJob.getJobId())) {
					return true;
				}
			}
		}
		return false;
	}

	
	@Override
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithVolumeCampus(String organizationId) {		
		//获取所有的资源池的同时获取他们的容量
		//循环遍历每个资源池找出全部的user并且获取他们的容量
		//查询资源量的时候把无效客户的的算进来 2017-02-23
		
		StringBuilder query = new StringBuilder(512);
		StringBuilder query_org = new StringBuilder(512);
		//装载 资源量和对应id的两个数组
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Object> amounts = new ArrayList<Object>();
		
		List<ResourcePoolUserVo> resourcePoolVoList = new ArrayList<ResourcePoolUserVo>();
		
		long time1 = System.currentTimeMillis();
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			query.append(" SELECT IFNULL(rp.capacity,0) as capacity,");
			query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (SELECT * from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' and orgLevel !='"+organization.getOrgLevel()+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id ");
			
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());
			for(Map<Object, Object> idmap:list){
				query_org.append("'"+idmap.get("id")+"',");
			}
			
			List<Map<Object, Object>> query_amount =null;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+")  and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"') GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			ResourcePoolUserVo vo = null;
			String id =  null;
			int index = -1;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}								
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));				
				vo.setId(StringUtil.toString(map.get("id")));
				vo.setName(StringUtil.toString(map.get("name")));
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(StringUtil.toString(map.get("poolName")));
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(StringUtil.toString(map.get("parentID")));
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		} else {
			
            String id = null;
            int index = -1;
			String orgLevel =null;
			Organization currentOrg = userService.getCurrentLoginUserOrganization();
//			String belong = currentOrg.getBelong();
//			if(currentOrg!=null && StringUtils.isNotBlank(belong)) {
//				Organization belongOrg = organizationDao.findById(belong);
//				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel())) {
//					orgLevel = belongOrg.getOrgLevel();
//				}
//			}else{
//				orgLevel = currentOrg.getOrgLevel();
//			}
			orgLevel = currentOrg.getOrgLevel();
			
			if(orgLevel==null){
				return new ArrayList<ResourcePoolUserVo>();
			}
			//下面的要做处理设置 invalid
			
			//第一个查询
			query.append(" select IFNULL(rp.capacity,0) as capacity, ");
			query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (select * from organization where LENGTH(orgLevel) <= 12 ");
			query.append(" and '"+orgLevel+"' LIKE concat(orgLevel,'%') and orglevel<> '"+orgLevel+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id ");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());
			
			for(Map<Object, Object> idmap:list){
				query_org.append("'"+idmap.get("id")+"',");
			}
			List<Map<Object, Object>> query_amount =null ;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+") and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"') GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());				
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			//还要判断是否相同 如果不同做invalid处理
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}

				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
			//清空上面的遍历
			ids.clear();
			amounts.clear();
		    
		    //第二个查询
		    query.append(" select IFNULL(rp.capacity,0) as capacity,");
		    query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (select * from organization where LENGTH(orgLevel) <= 12 and orgLevel LIKE '"+orgLevel+"%' ORDER BY LENGTH(orgLevel),orgOrder) o ");
		    query.append(" left join resource_pool rp on o.id = rp.organization_id ");
		    List<Map<Object, Object>> result = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
		    query.delete(0, query.length());
			for(Map<Object, Object> idmap:result){
				query_org.append("'"+idmap.get("id")+"',");
			}
			List<Map<Object, Object>> query_amount2 = null;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+") and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"') GROUP BY c.DELEVER_TARGET ");
			    query_amount2 = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());				
			}
			
			if (query_amount2 != null) {
				for (Map<Object, Object> amountmap : query_amount2) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			for(Map<Object, Object> map:result){
				vo = new ResourcePoolUserVo();				
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}
				
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
			ids.clear();
			amounts.clear();
		}
		long time2 = System.currentTimeMillis();
		
		System.out.println("查询资源池并且查询对应的资源量和分配客户数量 时间花费:"+(time2-time1)+"毫秒");
		
		long time3 = System.currentTimeMillis();
		//重写 并且查询出资源量
		List<User> userList = new ArrayList<User>();
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
			
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : resourcePoolVoList){
				userList.clear();
				if (StringUtil.isBlank(organizationId)) {
					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH || resourcePoolUserVo.getOrgLevel().length() <= 8) {
						userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					} else  if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
							|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
						resourcePoolUserVo.setParent(true);
					}
					
				} else {
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadUserByResourcePoolUser(organizationId));
						isGetUserByOrganizationId = true;
					}
				}
				ResourcePoolUserVo userVo = null;
				Map<String, Integer> userMap =null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();
				    //设置个人的资源池容量
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}else if(StringUtil.isNotBlank(organizationId)){//如果是最后一层组织架构的话要处理这个逻辑。
				userList.addAll(loadUserByResourcePoolUser(organizationId));
				Map<String, Integer> userMap =null;
				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}	
		long time4 = System.currentTimeMillis();
		
		System.out.println("查询所有客户并且查询对应的资源量和分配客户数量 时间花费:"+(time4-time3)+"毫秒");
		
		return resourcePoolVoList;
	}


    private List<User> loadUserByResourcePoolUser(String orgId){
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuilder query = new StringBuilder(128);    	
    	query.append("SELECT * from `user` where organizationID = :orgId ");
    	params.put("orgId", orgId);
    	query.append(" and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID in(SELECT rpj.JOB_ID as userJobId from resource_pool_job  rpj where rpj.ORGANIZATION_ID = '"+orgId+"' ORDER BY rpj.CREATE_TIME desc) ");
    	query.append(" and DEPT_ID = :orgId2 ) and enable_flag=0 ");
    	params.put("orgId2", orgId);
    	List<User> list = userdao.findBySql(query.toString(), params);
    	return list;

    }
    
    private List<User> loadDistributableUserByUserJob(String orgId,String[] jobs){
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuilder query = new StringBuilder(128);
    	query.append("SELECT * from `user` where organizationID = :orgId "); 
    	query.append("and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID in(:jobs) and DEPT_ID = :orgId2 ) and enable_flag=0 ");
    	params.put("orgId", orgId);
    	params.put("jobs", jobs);
    	params.put("orgId2", orgId);
    	List<User> list = userdao.findBySql(query.toString(), params);
    	return list;
    }

    
    
	 //资源配置的 资源池 分配树
    @Override
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithVolume(String organizationId){
    	User currentUser = userService.getCurrentLoginUser();
    	//String redisKey =currentUser.getUserId()+"resourcePool";//加入缓存
        Organization currentOrg = organizationDao.findById(currentUser.getOrganizationId());
        Organization belongOrg = null;
        if(currentOrg.getBelong()!=null){
        	belongOrg = organizationDao.findById(currentOrg.getBelong());
        }
        
        
        Organization branch = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(), OrganizationType.BRENCH,true);
        //分本部门校区也要确认是否是校区
        Organization campus = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(), OrganizationType.CAMPUS,false);
        Organization group = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(), OrganizationType.GROUNP,false);
    	//获取当前登录者的所有职位 根据职位来筛选
    	List<UserDeptJob> userDeptJobs = userDeptJobService.findDeptJobByUserId(currentUser.getUserId());
    	
    	//此处写死 判断当前登录者 是否具有网络职位 
    	Boolean isNetwork = false;
        List<UserDeptJob> deptJobs = userDeptJobService.getDeptJobByKey("网络", currentUser.getUserId());
        if(deptJobs!=null && deptJobs.size()>0){
        	isNetwork = true;
        }
        //判断当前登录者是否是集团的人
        Boolean isWLZG = userService.isUserRoleCode(currentUser.getUserId(), RoleCode.NETWORK_MANAGER);


    	
    	
    	
    	//获取所有可以配置的资源池  
		List<ResourcePoolUserVo> retList = new ArrayList<ResourcePoolUserVo>();
	
		for (UserDeptJob userDeptJob : userDeptJobs) {
			Organization org = organizationDao.findById(userDeptJob.getDeptId());
			String orgLevel = org.getOrgLevel();
			String jobId = userDeptJob.getJobId();
			if (isNetwork || isWLZG) {
				orgLevel = group.getOrgLevel();
			} else {
				if (belongOrg != null) {
					if (belongOrg.getOrgType() == OrganizationType.CAMPUS) {
						orgLevel = belongOrg.getOrgLevel();
					} else if (belongOrg.getOrgType() == OrganizationType.BRENCH) {
						orgLevel = branch.getOrgLevel();
					}
				} else {
					if (currentOrg.getOrgType() == OrganizationType.CAMPUS) {
						orgLevel = currentOrg.getOrgLevel();
					} else if (currentOrg.getOrgType() == OrganizationType.BRENCH) {
						orgLevel = branch.getOrgLevel();
					}
				}

			}

			
			retList.addAll(organizationDao.getDisValidateResourcePool(orgLevel, jobId, ResourcePoolJobType.VISIBLE));
		}	
		if (retList != null && retList.size() > 0) {
			retList=removeDuplicate(retList);//所有可以配置的资源池  已经去重 
		}
		
		
		
		
		
		//可以配置的资源池
		List<ResourcePoolUserVo> distributable_Pool = new ArrayList<ResourcePoolUserVo>();
		ResourcePoolUserVo userPoolVo = null;
		for(ResourcePoolUserVo tVo:retList){
			userPoolVo = tVo;
			distributable_Pool.add(userPoolVo);
			System.out.println("可配置资源池:"+userPoolVo.getName());
		}
		

				
		//获取所有可以看见的也就是可以配置的职位----begin
		List<UserJob> distributableJobs = new ArrayList<UserJob>();
		List<UserJob> currentOrgDistributableJobs = new ArrayList<UserJob>();
		
		for(UserDeptJob userDeptJob:userDeptJobs){
			String jobId = userDeptJob.getJobId();
		    distributableJobs.addAll(distributableUserJobService.findRelateUserJobByMainJobId(jobId));
		    currentOrgDistributableJobs.addAll(distributableUserJobService.findRelateUserJobByJobIdAndOrgLevel(jobId, currentOrg.getOrgLevel()));
        }
		StringBuffer jobs = new StringBuffer(256);
		String jobIds = null;
		String currentOrgJobIds =null;
		String otherOrgJobIds =null;
		List<String> all_ids = new ArrayList<String>();
		List<String> current_ids = new ArrayList<String>();
		List<String> other_ids = new ArrayList<String>();
		
		if(distributableJobs.size()>0){
			distributableJobs = removeDuplicateUserJob(distributableJobs);
			for(UserJob job:distributableJobs){
				jobs.append("'"+job.getId()+"',");
				all_ids.add(job.getId());
			}
			if(jobs.length()>=1){
				jobIds =jobs.substring(0, jobs.length()-1);
			}
		}
		jobs.delete(0, jobs.length());
		if(currentOrgDistributableJobs.size()>0){
			currentOrgDistributableJobs = removeDuplicateUserJob(currentOrgDistributableJobs);
			for(UserJob job:currentOrgDistributableJobs){
				jobs.append("'"+job.getId()+"',");
				current_ids.add(job.getId());
			}
			if(jobs.length()>=1){
				currentOrgJobIds =jobs.substring(0, jobs.length()-1);
			}
		}
		jobs.delete(0, jobs.length());
		
		if(all_ids.size()>0){
			for(String keys:all_ids){
				if(current_ids.size()>0){
					if(current_ids.indexOf(keys)==-1){
						other_ids.add(keys);
					}
				}else{
					other_ids.add(keys);
				}
			}
		}
		if(other_ids.size()>0){
			for(String keystring:other_ids){
				jobs.append("'"+keystring+"',");
			}
			if(jobs.length()>=1){
				otherOrgJobIds =jobs.substring(0, jobs.length()-1);
			}
		}
		
	
		
		//获取所有可以看见的也就是可以配置的职位----end
		
		
		
		Mapper mapper = HibernateUtils.getMapper();
		
		List<ResourcePoolUserVo> AllResourcePoolUserVos =  new ArrayList<ResourcePoolUserVo>();
		List<ResourcePoolUserVo> disResourcePoolUserVos =  new ArrayList<ResourcePoolUserVo>();
		//查询这些子节点的资源容量
		StringBuilder query = new StringBuilder(512);
		StringBuilder query_org = new StringBuilder(512);
		//装载 资源量和对应id的两个数组
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Object> amounts = new ArrayList<Object>();
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			//获取被选取节点的所有子节点
			List<Organization> childOrgs = organizationService.getAllChildOrganizationsByOrgLevel(organization.getOrgLevel());
				

			
			if(childOrgs!=null && childOrgs.size()>0){
				
				for(Organization tempOrg:childOrgs){
					query_org.append("'"+tempOrg.getId()+"',");
				}
				
				String queryOrg = query_org.substring(0, query_org.length()-1);
				
				query.append(" select IFNULL(rp.capacity,0) as capacity,concat('',o.id) as id ,");
				query.append(" o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from organization o ");
				query.append(" left join resource_pool rp on o.id = rp.organization_id where o.id in( " +queryOrg+ " ) order by length(o.orgLevel),o.orgOrder");
				List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				
				List<Map<Object, Object>> query_amount =null ;
				if(query_org.length()>1){
					query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+queryOrg+") and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"','"+CustomerDealStatus.INVALID+"') GROUP BY c.DELEVER_TARGET ");
					//query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+queryOrg+")  GROUP BY c.DELEVER_TARGET ");
					query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
					query.delete(0, query.length());
					query_org.delete(0, query_org.length());				
				}
				if (query_amount != null) {
					for (Map<Object, Object> amountmap : query_amount) {
						ids.add(StringUtil.toString(amountmap.get("id")));
						amounts.add(StringUtil.toString(amountmap.get("current")));
					}
				}
				
					
	            String id = null;
	            int index = -1;
	            
				ResourcePoolUserVo vo = null;
				for(Map<Object, Object> map:list){
					vo = new ResourcePoolUserVo();
					//设置当前容量 --begin
					if(map.get("id")!=null){
						id = StringUtil.toString(map.get("id"));
						index = ids.indexOf(id);
						if(index!=-1){
							vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
						}else{
							vo.setCurrent(0);
						}					
					}else{
						vo.setCurrent(0);
					}
					//设置当前容量 --end
					vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
					vo.setId(map.get("id")!=null?map.get("id").toString():"");
					vo.setName(map.get("name")!=null?map.get("name").toString():"");
					vo.setOrgLevel(map.get("orgLevel").toString());
					vo.setChildNodeNums(organizationService.getChildNodeNumsByOrgId(vo.getId(), queryOrg.replace(",", "").split(",")));
					vo.setOrgType(OrganizationType.valueOf(map.get("orgType").toString()));
					vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():map.get("name").toString());
					vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
					vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
					vo.setParent(false);				
					disResourcePoolUserVos.add(vo);			
				}
				
				Integer current = 0;
				Integer total = 0;			
				for(ResourcePoolUserVo rUserVo:disResourcePoolUserVos){
					current = rUserVo.getCurrent();
					total = rUserVo.getTotal();
					if (distributable_Pool != null && distributable_Pool.size() > 0) {
						for (ResourcePoolUserVo tVo : distributable_Pool) {
							if (rUserVo.getId().equals(tVo.getId())) {
								rUserVo.setResourcePoolstatus(ValidStatus.VALID);
								rUserVo.setCurrent(current);
								rUserVo.setTotal(total);
								rUserVo.setNodeState(1);
								break;
							} else {
								rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
								rUserVo.setCurrent(0);
								rUserVo.setTotal(0);
							}
						}
					}else{
						rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
						rUserVo.setCurrent(0);
						rUserVo.setTotal(0);					
					}
				}
				
				
				
				AllResourcePoolUserVos.addAll(disResourcePoolUserVos);
				
			}else{
				//说明是叶子节点
				ResourcePoolUserVo poolUserVo = mapper.map(organization,ResourcePoolUserVo.class);
				disResourcePoolUserVos.add(poolUserVo);		
			}			
			
		}else{
			
//		    byte[] object =JedisUtil.get(redisKey.getBytes());
//		    if(object!=null){
//		    	Object value = JedisUtil.ByteToObject(object);
//		    	return (List<ResourcePoolUserVo>)value;
//		    }


			//这个可以缓存一下retList	
			//获取所有可以配置的资源池的父节点---begin
			List<Organization> organizations = new ArrayList<Organization>();
			for(ResourcePoolUserVo temp:retList){
				organizations.addAll(organizationService.getAllParentOrganizationsByOrgLevel(temp.getOrgLevel()));
			}
			
			//加入自己所在的组织架构
			organizations.add(currentOrg);
			organizations.addAll(organizationService.getAllParentOrganizationsByOrgLevel(currentOrg.getOrgLevel()));
			
			//还要加上所有可以分配的职位所属的部门也要加载
			if(jobIds!=null){
				List<Organization> distributableJobOrg = userDeptJobService.getDeptByJobIds(branch.getOrgLevel(),campus,currentOrg.getOrgLevel(),isNetwork,jobIds,currentOrgJobIds,otherOrgJobIds);
				organizations.addAll(distributableJobOrg);
				if(distributableJobOrg!=null && distributableJobOrg.size()>0){
					for(Organization org:distributableJobOrg){
						organizations.addAll(organizationService.getAllParentOrganizationsByOrgLevel(org.getOrgLevel()));
					}
				}
			}
			
			//所有的父节点去重复
			if(organizations.size()>0){
				organizations = removeDuplicateOrganizaiton(organizations);
			}
			
			for(Organization organization:organizations){
				ResourcePoolUserVo poolUserVo = mapper.map(organization,ResourcePoolUserVo.class);
				//poolUserVo.setResourcePoolstatus(ValidStatus.INVALID);
				retList.add(poolUserVo);
			}
			
			
			//获取所有可以配置的资源池的父节点---end
			
			//可配置节点和这些节点的父子节点合并在一起  还要继续去重
			if (retList != null && retList.size() > 0) {
				retList=removeDuplicate(retList);//
			}else{
				return AllResourcePoolUserVos;
			}
			//
			for(ResourcePoolUserVo tempOrg:retList){//要保证这里没有重复
				query_org.append("'"+tempOrg.getId()+"',");
			}
			
			String queryOrg = query_org.substring(0, query_org.length()-1);
			
			query.append(" select IFNULL(rp.capacity,0) as capacity,concat('',o.id) as id ,");
			query.append(" o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from organization o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id where o.id in( " +queryOrg+ " ) order by length(o.orgLevel),o.orgOrder ");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());
			
			List<Map<Object, Object>> query_amount =null ;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+queryOrg+") and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"','"+CustomerDealStatus.INVALID+"') GROUP BY c.DELEVER_TARGET ");
				//query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+queryOrg+")  GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());			
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			
				
            String id = null;
            int index = -1;
            
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				//设置当前容量 --begin
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}
				//设置当前容量 --end
				
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setChildNodeNums(organizationService.getChildNodeNumsByOrgId(vo.getId(), queryOrg.replaceAll("'", "").split(",")));
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(map.get("orgType").toString()));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():map.get("name").toString());
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				disResourcePoolUserVos.add(vo);			
			}
				
			Integer current = 0;
			Integer total = 0;	
			for (ResourcePoolUserVo rUserVo : disResourcePoolUserVos) {
				current = rUserVo.getCurrent();
				total = rUserVo.getTotal();
				if (distributable_Pool != null && distributable_Pool.size() > 0) {
					for (ResourcePoolUserVo tVo : distributable_Pool) {
						if (rUserVo.getId().equals(tVo.getId())) {
							rUserVo.setResourcePoolstatus(ValidStatus.VALID);
							rUserVo.setCurrent(current);
							rUserVo.setTotal(total);
							rUserVo.setNodeState(1);
							break;
						} else {
							rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
							rUserVo.setCurrent(0);
							rUserVo.setTotal(0);
						}
					}
				}else{
					rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
					rUserVo.setCurrent(0);
					rUserVo.setTotal(0);					
				}
			}
			
			AllResourcePoolUserVos.addAll(disResourcePoolUserVos);		
		}
		
		//如果职位为空 说明一个人都没有 直接返回
		if (jobIds == null) {

			for (ResourcePoolUserVo resourcePoolUserVo : AllResourcePoolUserVos) {
				if (!resourcePoolUserVo.isParent() && (resourcePoolUserVo.getChildNodeNums() == 0)
						&& resourcePoolUserVo.getResourcePoolstatus() == ValidStatus.INVALID) {
					resourcePoolUserVo.setNodeState(0);
				} else {
					resourcePoolUserVo.setNodeState(1);
				}
				if (resourcePoolUserVo.getId().equals("000001")) {
					resourcePoolUserVo.setNodeState(1);
				}
				if(resourcePoolUserVo.getId().equals(currentUser.getOrganizationId())){
					resourcePoolUserVo.setNodeState(1);
				}

			}
			ResourcePoolUserVo userVo=new ResourcePoolUserVo();
		    //设置个人的资源池容量
			Map<String, Integer> userMap= getResourcePoolVolumeByUserId(currentUser.getUserId());
		    userVo.setCurrent(userMap.get("current"));
		    userVo.setTotal(userMap.get("total"));	    
			userVo.setId(currentUser.getUserId());
			userVo.setName(currentUser.getName());
			userVo.setParentId(currentUser.getOrganizationId());
			
			userVo.setNodeState(1);
			AllResourcePoolUserVos.add(userVo);
			
			return AllResourcePoolUserVos;
		}
		
		List<User> userList = new ArrayList<User>();			
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (disResourcePoolUserVos != null && disResourcePoolUserVos.size() > 0) {		
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : disResourcePoolUserVos){
				userList.clear();
				if (StringUtil.isBlank(organizationId)) {
//					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
//							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH || resourcePoolUserVo.getOrgLevel().length() <= 8) {
//						
//						userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds);
//					} else  if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
//							|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
//						resourcePoolUserVo.setParent(true);
//					}
					
					
//					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
//							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH ||resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
//						userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds);
//					} else  if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
//							 ) {
//						resourcePoolUserVo.setParent(true);
//					}
					userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds.replaceAll("'", "").split(","));
					
					if(!resourcePoolUserVo.isParent()&&(userList.size()==0)&&(resourcePoolUserVo.getChildNodeNums()==0)
							&&resourcePoolUserVo.getResourcePoolstatus()==ValidStatus.INVALID){
						resourcePoolUserVo.setNodeState(0);
					}else{
						resourcePoolUserVo.setNodeState(1);
					}
					if(resourcePoolUserVo.getId().equals("000001")){
						resourcePoolUserVo.setNodeState(1);
					}
					
					
					//userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds);
				} else {
					
	
					
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds.replaceAll("'", "").split(","));
					
					
					if(!resourcePoolUserVo.isParent()&&(userList.size()==0)&&(resourcePoolUserVo.getChildNodeNums()==0)
							&&resourcePoolUserVo.getResourcePoolstatus()==ValidStatus.INVALID){
						resourcePoolUserVo.setNodeState(0);
					}else{
						resourcePoolUserVo.setNodeState(1);
					}
					
					
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadDistributableUserByUserJob(organizationId,jobIds.replaceAll("'", "").split(",")));
						isGetUserByOrganizationId = true;
					}
				}
				ResourcePoolUserVo userVo = null;
				Map<String, Integer> userMap =null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();
				    //设置个人的资源池容量
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					
					userVo.setNodeState(1);
					
					resourcePoolUserVoList.add(userVo);
				}			
			}
		}
		
		
		//加入自己		
		ResourcePoolUserVo currentUserVo=new ResourcePoolUserVo();
	    //设置个人的资源池容量
		Map<String, Integer> currentUserMap= getResourcePoolVolumeByUserId(currentUser.getUserId());
	    currentUserVo.setCurrent(currentUserMap.get("current"));
	    currentUserVo.setTotal(currentUserMap.get("total"));
	    
	    currentUserVo.setId(currentUser.getUserId());
	    currentUserVo.setName(currentUser.getName());
	    currentUserVo.setParentId(currentUser.getOrganizationId());
		
		currentUserVo.setNodeState(1);
		
		resourcePoolUserVoList.add(currentUserVo);
		resourcePoolUserVoList = removeDuplicate(resourcePoolUserVoList);
		
	    AllResourcePoolUserVos.addAll(resourcePoolUserVoList); 
	    System.out.println("节点全部大小:"+AllResourcePoolUserVos.size());
	    for(ResourcePoolUserVo wang:AllResourcePoolUserVos){	
	    	System.out.println(wang.getId()+"-"+wang.getName()+"-"+wang.getParentId()+"-"+(wang.getResourcePoolstatus()!=null?wang.getResourcePoolstatus().getValue():"NULL")+"-"
	    			+wang.getChildNodeNums()+"-nodeState:"+wang.getNodeState());
	    }
	    
//	    JedisUtil.set(redisKey.getBytes(), JedisUtil.ObjectToByte(AllResourcePoolUserVos),5*60);
	    
	    
	    
    	return AllResourcePoolUserVos;
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	@Override
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithoutVolumeCampus(String organizationId) {
	// TODO 重写getResourcePoolAndUser方法
		
		//获取所有的资源池的
		//循环遍历每个资源池找出全部的user
		StringBuilder query = new StringBuilder(512);
		List<ResourcePoolUserVo> resourcePoolVoList = new ArrayList<ResourcePoolUserVo>();
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			
			query.append(" select concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (SELECT * from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' and orgLevel !='"+organization.getOrgLevel()+"' and RESOURCE_POOL_STATUS ='VALID' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				System.out.println("org非空-资源池org名字:"+map.get("name"));
				vo = new ResourcePoolUserVo();
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(map.get("orgType").toString()));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		} else {
			String orgLevel =null;
			Organization currentOrg = userService.getCurrentLoginUserOrganization();
			String belong = currentOrg.getBelong();
			if(currentOrg!=null && StringUtils.isNotBlank(belong)) {
				Organization belongOrg = organizationDao.findById(belong);
				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel())) {
					orgLevel = belongOrg.getOrgLevel();
				}
			}else{
				orgLevel = currentOrg.getOrgLevel();
			}
			if(orgLevel==null){
				return new ArrayList<ResourcePoolUserVo>();
			}
			//下面的要做处理设置 invalid
			
            //获取当前org以上的组织
			int orgLength =12;
			query.append(" select concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (select * from organization where RESOURCE_POOL_STATUS ='VALID' and LENGTH(orgLevel) <="+orgLength );
			query.append(" and '"+orgLevel+"' LIKE concat(orgLevel,'%') and orglevel<> '"+orgLevel+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			//还要判断是否相同 如果不同做invalid处理
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map : list){
				System.out.println("org空-资源池org名字:"+map.get("name"));
				vo = new ResourcePoolUserVo();
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(StringUtil.toString(map.get("poolStatus"))):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		    query.delete(0, query.length());
		   
		    //当前org以下的组织
		    query.append(" select concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus ");
		    query.append(" from (select * from organization where LENGTH(orgLevel) <= "+orgLength+" and orgLevel LIKE '"+orgLevel+"%' and RESOURCE_POOL_STATUS ='VALID' ORDER BY LENGTH(orgLevel),orgOrder) o ");
		    List<Map<Object, Object>> result = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			for(Map<Object, Object> map:result){
				System.out.println("org空-资源池org名字:"+map.get("name"));
				vo = new ResourcePoolUserVo();
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
			
		}

		//重写
		List<User> userList = new ArrayList<User>();
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
			
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : resourcePoolVoList){
				userList.clear();
				if (StringUtil.isBlank(organizationId)) {
					
					//原来的逻辑是 展开至集团 分公司  校区
					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH || resourcePoolUserVo.getOrgLevel().length() <= 8) {
						userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					}
					if(resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
							|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
						resourcePoolUserVo.setParent(true);
					}

					
				} else {
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadUserByResourcePoolUser(organizationId));
						isGetUserByOrganizationId = true;
					}
				}
				ResourcePoolUserVo userVo = null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}else if(StringUtil.isNotBlank(organizationId)){//如果是最后一层组织架构的话要处理这个逻辑。
				userList.addAll(loadUserByResourcePoolUser(organizationId));

				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}	

		
		return resourcePoolVoList;
	}

	@Override
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithVolumeBranch(String organizationId) {
		//获取所有的资源池的同时获取他们的容量
		//循环遍历每个资源池找出全部的user并且获取他们的容量
		StringBuilder query = new StringBuilder(512);
		StringBuilder query_org = new StringBuilder(512);
		//装载 资源量和对应id的两个数组
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Object> amounts = new ArrayList<Object>();
		
		List<ResourcePoolUserVo> resourcePoolVoList = new ArrayList<ResourcePoolUserVo>();
		

		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			
			query.append(" SELECT IFNULL(rp.capacity,0) as capacity,");
			query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (SELECT * from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' and orgLevel !='"+organization.getOrgLevel()+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id ");
			
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());
			for(Map<Object, Object> idmap:list){
				query_org.append("'"+idmap.get("id")+"',");
			}
			
			List<Map<Object, Object>> query_amount =null;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+")  and c.DEAL_STATUS NOT in ('SIGNEUP','INVALID') GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(StringUtil.toString(amountmap.get("id")));
					amounts.add(StringUtil.toString(amountmap.get("current")));
				}
			}
			ResourcePoolUserVo vo = null;
			String id =  null;
			int index = -1;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}								
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));				
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		} else {
			
            String id = null;
            int index = -1;
			String orgLevel =null;
			Organization currentOrg = userService.getCurrentLoginUserOrganization();
			String belong = currentOrg.getBelong();
			if(currentOrg!=null && StringUtils.isNotBlank(belong)) {
				Organization belongOrg = organizationDao.findById(belong);
				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel())) {
					orgLevel = belongOrg.getOrgLevel();
				}
//			}else{
//				throw new RuntimeException("数据异常");
//			}
			}
			if(orgLevel==null){
				return new ArrayList<ResourcePoolUserVo>();
			}
			//下面的要做处理设置 invalid
			
			//第一个查询
			query.append(" select IFNULL(rp.capacity,0) as capacity, ");
			query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (select * from organization where LENGTH(orgLevel) <= 12 ");
			query.append(" and '"+orgLevel+"' LIKE concat(orgLevel,'%') and orglevel<> '"+orgLevel+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id ");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());
			
			for(Map<Object, Object> idmap:list){
				query_org.append("'"+idmap.get("id")+"',");
			}
			List<Map<Object, Object>> query_amount =null ;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+") and c.DEAL_STATUS NOT in ('SIGNEUP','INVALID') GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());				
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			//还要判断是否相同 如果不同做invalid处理
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				
				if(map.get("id")!=null){
					id = map.get("id").toString();
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}

				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
			//清空上面的遍历
			ids.clear();
			amounts.clear();
		    
		    //第二个查询
		    query.append(" select IFNULL(rp.capacity,0) as capacity,");
		    query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (select * from organization where LENGTH(orgLevel) <= 12 and orgLevel LIKE '"+orgLevel+"%' ORDER BY LENGTH(orgLevel),orgOrder) o ");
		    query.append(" left join resource_pool rp on o.id = rp.organization_id ");
		    List<Map<Object, Object>> result = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
		    query.delete(0, query.length());
			for(Map<Object, Object> idmap:result){
				query_org.append("'"+idmap.get("id")+"',");
			}
			List<Map<Object, Object>> query_amount2 = null;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+") and c.DEAL_STATUS NOT in ('SIGNEUP','INVALID') GROUP BY c.DELEVER_TARGET ");
			    query_amount2 = customerDao.findMapBySql(query.toString(), new HashMap<String,Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());				
			}
			
			if (query_amount2 != null) {
				for (Map<Object, Object> amountmap : query_amount2) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			for(Map<Object, Object> map:result){
				vo = new ResourcePoolUserVo();				
				if(map.get("id")!=null){
					id = map.get("id").toString();
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}
				
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
			ids.clear();
			amounts.clear();
		}

		//重写 并且查询出资源量
		List<User> userList = new ArrayList<User>();
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
			
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : resourcePoolVoList){
				if (StringUtil.isBlank(organizationId)) {
					//原来的逻辑是 展开至集团 分公司  校区
					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
							|| resourcePoolUserVo.getOrgLevel().length() <= 8) {
						userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					}
					//resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
					//|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT
					if(resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH ) {
						resourcePoolUserVo.setParent(true);
					}
					
				} else {
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadUserByResourcePoolUser(organizationId));
						isGetUserByOrganizationId = true;
					}
				}
				ResourcePoolUserVo userVo = null;
				Map<String, Integer> userMap =null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();
				    //设置个人的资源池容量
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}else if(StringUtil.isNotBlank(organizationId)){//如果是最后一层组织架构的话要处理这个逻辑。
				userList.addAll(loadUserByResourcePoolUser(organizationId));
				Map<String, Integer> userMap =null;
				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}	

		return resourcePoolVoList;
	}

	@Override
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithoutVolumeBranch(String organizationId) {
	// TODO 重写getResourcePoolAndUser方法
		
		//获取所有的资源池的
		//循环遍历每个资源池找出全部的user
		StringBuilder query = new StringBuilder(512);
		List<ResourcePoolUserVo> resourcePoolVoList = new ArrayList<ResourcePoolUserVo>();
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			query.append(" select concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (SELECT * from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' and orgLevel !='"+organization.getOrgLevel()+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				System.out.println("org非空-资源池org名字:"+map.get("name"));
				vo = new ResourcePoolUserVo();
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(map.get("orgType").toString()));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		} else {
			String orgLevel =null;
			Organization currentOrg = userService.getCurrentLoginUserOrganization();
			String belong = currentOrg.getBelong();
			if(currentOrg!=null && StringUtils.isNotBlank(belong)) {
				Organization belongOrg = organizationDao.findById(belong);
				if(belongOrg!=null && StringUtils.isNotBlank(belongOrg.getOrgLevel())) {
					orgLevel = belongOrg.getOrgLevel();
				}
//			}else{
//				throw new RuntimeException("数据异常");
//			}
			}
			if(orgLevel==null){
				return new ArrayList<ResourcePoolUserVo>();
			}
			//下面的要做处理设置 invalid
			
            //获取当前org以上的组织
			int orgLength =8;
			query.append(" select concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (select * from organization where LENGTH(orgLevel) <="+orgLength );
			query.append(" and '"+orgLevel+"' LIKE concat(orgLevel,'%') and orglevel<> '"+orgLevel+"' ORDER BY LENGTH(orgLevel),orgOrder) o ");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			//还要判断是否相同 如果不同做invalid处理
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				System.out.println("org空-资源池org名字:"+StringUtil.toString(map.get("name")));
				vo = new ResourcePoolUserVo();
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		    query.delete(0, query.length());
		   
		    //当前org以下的组织
		    query.append(" select concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus ");
		    query.append(" from (select * from organization where LENGTH(orgLevel) <= "+orgLength+" and orgLevel LIKE '"+orgLevel+"%' ORDER BY LENGTH(orgLevel),orgOrder) o ");
		    List<Map<Object, Object>> result = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			for(Map<Object, Object> map:result){
				System.out.println("org空-资源池org名字:"+map.get("name"));
				vo = new ResourcePoolUserVo();
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
			
		}

		//重写
		List<User> userList = new ArrayList<User>();
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
			
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : resourcePoolVoList){
				userList.clear();
				if (StringUtil.isBlank(organizationId)) {
					
					//原来的逻辑是 展开至集团 分公司  校区
					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
							|| resourcePoolUserVo.getOrgLevel().length() <= 8) {
						userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					}
					//resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
					//|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT
					if(resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH ) {
						resourcePoolUserVo.setParent(true);
					}

					
				} else {
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadUserByResourcePoolUser(organizationId));
						isGetUserByOrganizationId = true;
					}
				}
				ResourcePoolUserVo userVo = null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}else if(StringUtil.isNotBlank(organizationId)){//如果是最后一层组织架构的话要处理这个逻辑。
				userList.addAll(loadUserByResourcePoolUser(organizationId));

				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}	
		
		return resourcePoolVoList;
	}
	
	@Override
	public Map<String, Integer> getResourcePoolVolumeByUserId(String userId) {
		Map<String, Object> params = new HashMap<String,Object>();
		Map<String, Integer> map = new HashMap<String,Integer>();			
		//查询当前用户所属职位中的最大资源容量数
		StringBuffer sql=new StringBuffer();
		sql.append("select uj.* from user_dept_job udj INNER JOIN user_job uj on udj.job_id=uj.id  ");
		sql.append(" and udj.user_id = :userId and uj.IS_CUSTOMER_FOLLOW='0' ORDER BY RESOURCE_NUM DESC ");
		params.put("userId", userId);
		List<UserJob> userJobList=userJobDao.findBySql(sql.toString(), params);
							
		
		//还可分配的资源数,判断用户有没有勾选主职位的客户资源跟进
		if(userJobList!=null && userJobList.size()>0 && userJobList.get(0)!=null){
			//查询当前用户已有资源数
			int current=resourcePoolJobDao.findCountSql(" select COUNT(1) from customer where DELEVER_TARGET= :userId and DEAL_STATUS NOT in ('SIGNEUP','INVALID') ", params);
			//int current=resourcePoolJobDao.findCountSql(" select COUNT(1) from customer where DELEVER_TARGET= :userId ",params);				
			UserJob userJob=userJobList.get(0);
			int total=userJob.getResourceNum();
			map.put("total", total);
			map.put("current", current);
		}else{
			map.put("total", 0);
			map.put("current", 0);
		}	
		return map;
	}

	@Override
	public Map<String, Integer> getResourcePoolVolumeByOrgId(String orgId) {
		Map<String, Integer> map = new HashMap<String,Integer>();
		ResourcePool resourcePool=resourcePoolDao.findById(orgId);
		if(resourcePool!=null){
			BigDecimal capacity=resourcePool.getCapacity();
			StringBuffer sql=new StringBuffer();
			sql.append(" select count(1) from customer where DELEVER_TARGET = '"+resourcePool.getOrganizationId()+"' and DEAL_STATUS NOT in ('SIGNEUP','INVALID')  ");
			int current=resourcePoolDao.findCountSql(sql.toString(), new HashMap<String,Object>());
			map.put("total", capacity.intValue());
			map.put("current", current);
		}else{
			map.put("total", 0);
			map.put("current", 0);
		}
		return map;
	}

	@Override
	public Response checkPersonPoolVolume(String userId) {
		User user = userService.loadUserById(userId);
		Response res = null;
		if(user!=null){
		    res = this.getPersonPoolVolume(1, userId);
		}else{
			res = this.getResourcePoolVolume(1, userId);
		}
    	return res;
	}

	@Override
	public Response getPersonPoolVolume(int customerNums, String userId) {
		Response res = new Response();
		// 分配给某个人，取到这人所在职位一次可获取的最大容量数
		User user = userService.loadUserById(userId);
		ResourcePoolJob resourcePoolJob = new ResourcePoolJob();
		List<UserDeptJob> userDeptJob = userDeptJobDao.findDeptJobByUserId(userId);
		List<BigDecimal> resourceList = new ArrayList<BigDecimal>();
		List<ResourcePoolJob> list = new ArrayList<ResourcePoolJob>();

		// 查询当前用户所属职位中的最大资源容量数
		StringBuffer sql = new StringBuffer();
		sql.append("select uj.* from user_dept_job udj INNER JOIN user_job uj on udj.job_id=uj.id  ");
		sql.append(" and udj.user_id='" + user.getUserId()
				+ "' and uj.IS_CUSTOMER_FOLLOW='0' ORDER BY RESOURCE_NUM DESC ");
		List<UserJob> userJobList = userJobDao.findBySql(sql.toString(), new HashMap<String,Object>());

		// 还可分配的资源数,判断用户有没有勾选主职位的客户资源跟进
		if (userJobList != null && userJobList.size() > 0 && userJobList.get(0) != null) {
			// 查询当前用户已有资源数
			int count = resourcePoolJobDao.findCountSql(" select COUNT(1) from customer where DELEVER_TARGET='"
					+ user.getUserId() + "' and DEAL_STATUS NOT in ('SIGNEUP','INVALID') ", new HashMap<String,Object>());
			UserJob userJob = userJobList.get(0);
			int maxNums = userJob.getResourceNum();
			int overPlus = maxNums - count;
			if (customerNums + count > maxNums) {
				if (overPlus <= 0) {
					// 回流导致资源池总容量已经溢出
					res.setResultMessage("所属职位中最大资源池总容量已超出，不可再往里添加新资源");
					res.setResultCode(-2);
					return res;
				} else {
					// 超出此人所在部门主职位的最大资源获取数
					res.setResultMessage("[" + user.getName() + "]所属职位中最大资源池总容量是" + maxNums + "个,已经拥有的资源数是" + count
							+ ",还可再分配资源" + overPlus + "个，您现在选择的资源数（" + customerNums + "）超出已有规范,请重新选择 ");
					res.setResultCode(overPlus);
					return res;
				}

			}
		} else {
			// 当前分配对象的主职位没有配置客户资源跟进
			res.setResultCode(-1);
			res.setResultMessage("客户分配对象没有配置资源池");
			return res;
		}

		if (userDeptJob != null && userDeptJob.size() > 0) {
			for (UserDeptJob job : userDeptJob) {
				StringBuffer hql = new StringBuffer();
				// 只判断到咨询师所在主职位一次可获取的最大容量
				hql.append(" select * from resource_pool_job where  type='VISIBLE' and ORGANIZATION_ID='"
						+ user.getOrganizationId() + "' ");
				hql.append(" and JOB_ID in (SELECT JOB_ID from user_dept_job where JOB_ID='" + job.getJobId()
						+ "' and USER_ID='" + user.getUserId() + "' and isMajorRole='0') ");
				list = resourcePoolJobDao.findBySql(hql.toString(), new HashMap<String,Object>());
				if (list != null && list.size() > 0) {
					resourcePoolJob = list.get(0);
					resourceList.add(resourcePoolJob.getOneTimeResource());
				}
			}
		} else {
			// 分配到的人没有主职位
			res.setResultCode(-1);
			return res;
		}
		if (resourceList != null && resourceList.size() > 0) {
			Collections.sort(resourceList);
			BigDecimal oneTimeResource = resourceList.get(resourceList.size() - 1);
			if (oneTimeResource.intValue() < customerNums) {
				// 分配客户数量超出允许数量，错误提示信息(弹框显示)
				res.setResultMessage("[" + user.getName() + "]所属职位中一次最多可分配资源数是" + oneTimeResource + "个,您现在选择的资源数（"
						+ customerNums + "）超出已有规范,请重新选择 ");
				res.setResultCode(oneTimeResource.intValue());
				return res;
			}
		}

		return new Response();
	}
	
	@Override
	public List<Organization> getOrgByPoolName(String key) {
		Map<String, Object> params = new HashMap<String,Object>();
		StringBuilder query = new StringBuilder(64);
		//进行权限限制
		Organization currentOrg = userService.getCurrentLoginUserOrganization();
		String orgLevel = currentOrg.getOrgLevel();		
		query.append(" select * from organization o where o.orgLevel like '"+orgLevel+"%' and ( o.CUSTOMER_POOL_NAME  like :poolName or o.`name` like :name ) ");
		params.put("poolName", "%" + key + "%");
		params.put("name", "%" + key + "%");
		List<Organization> result = organizationDao.findBySql(query.toString(), params);
        return result;
	}

	@Override
	public List<ResourcePoolUserVo> loadResourcePoolAndUserBySearch(String organizationId) {
		//获取所有的资源池的同时获取他们的容量
		//循环遍历每个资源池找出全部的user并且获取他们的容量
		StringBuilder query = new StringBuilder(512);
		StringBuilder query_org = new StringBuilder(512);
		//装载 资源量和对应id的两个数组
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Object> amounts = new ArrayList<Object>();
		
		List<ResourcePoolUserVo> resourcePoolVoList = new ArrayList<ResourcePoolUserVo>();
		
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			
			query.append(" SELECT IFNULL(rp.capacity,0) as capacity,");//and orgLevel !='"+organization.getOrgLevel()+"'
			query.append(" concat('',o.id) as id,o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from (SELECT * from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%'  ORDER BY LENGTH(orgLevel),orgOrder) o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id ");
			
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String,Object>());
			query.delete(0, query.length());
			for(Map<Object, Object> idmap:list){
				query_org.append("'"+idmap.get("id")+"',");
			}
			
			List<Map<Object, Object>> query_amount =null;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+")  and c.DEAL_STATUS NOT in ('SIGNEUP','INVALID') GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String,Object>());
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}
			ResourcePoolUserVo vo = null;
			String id =  null;
			int index = -1;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}								
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));				
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():"");
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				resourcePoolVoList.add(vo);
			}
		} 
		
		//重写 并且查询出资源量
		List<User> userList = new ArrayList<User>();
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (resourcePoolVoList != null && resourcePoolVoList.size() > 0) {
			
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : resourcePoolVoList){
				userList.clear();
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadUserByResourcePoolUser(resourcePoolUserVo.getId());
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadUserByResourcePoolUser(organizationId));
						isGetUserByOrganizationId = true;
					}
				ResourcePoolUserVo userVo = null;
				Map<String, Integer> userMap =null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();
				    //设置个人的资源池容量
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}else if(StringUtil.isNotBlank(organizationId)){//如果是最后一层组织架构的话要处理这个逻辑。
				userList.addAll(loadUserByResourcePoolUser(organizationId));
				Map<String, Integer> userMap =null;
				for(User user : userList){
					ResourcePoolUserVo userVo=new ResourcePoolUserVo();
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					resourcePoolUserVoList.add(userVo);
				}
			resourcePoolVoList.addAll(resourcePoolUserVoList);
		}	
		
		return resourcePoolVoList;
	}
	
	@Override
	public ResourcePool getBelongBranchResourcePool(String userId, String type) {
		if(type.equals(OrganizationType.GROUNP.getValue())){
			//集团网络资源池
			String sql ="select * from resource_pool where `NAME` LIKE '%集团网络%' and `STATUS` ='VALID' ";
			List<ResourcePool> list = resourcePoolDao.findBySql(sql, new HashMap<String, Object>());
			if(list!=null && list.size()>0){
				return list.get(0);
			}else{
				return null;
			}			
		}		
		if(type.equals(OrganizationType.BRENCH.getValue())){
			//或者所属分公司资源池
			Organization branch = userService.getBelongBranchByUserId(userId);
			ResourcePool resourcePool = resourcePoolDao.findById(branch.getId());
			return resourcePool;
		}
		if(type.equals(OrganizationType.CAMPUS.getValue())){
			//获取所属校区资源池
			Organization campus = userService.getBelongCampusByUserId(userId);
			ResourcePool resourcePool = resourcePoolDao.findById(campus.getId());
			return resourcePool;
		}
		if(type.equals(OrganizationType.DEPARTMENT.getValue())){
			//获取所属部门的 TMK外呼资源池			
			User user = userService.loadUserById(userId);			
			Organization department = organizationDao.findById(user.getOrganizationId());
			String sql  ="select * from resource_pool rp INNER  JOIN organization o on rp.ORGANIZATION_ID = o.id where rp.`NAME` LIKE '%外呼%' and rp.`STATUS` ='VALID'"
			+" and  o.orgLevel LIKE '"+department.getOrgLevel()+"%' and o.orgType ='"+OrganizationType.DEPARTMENT.getValue()+"'";
			List<ResourcePool> list = resourcePoolDao.findBySql(sql, new HashMap<String, Object>());
			if(list!=null && list.size()>0){
				return list.get(0);
			}else{
				return null;
			}			
		}
		
		
		return null;
	}


	@Override
	public List<ResourcePoolUserVo> getResourcePoolDataImport(String organizationId) {
	
   	
    	

		
    	User currentUser = userService.getCurrentLoginUser();
    	
//    	String redisKey =currentUser.getUserId()+"DataImportResourcePool";//加入缓存
    	Organization currentOrg = organizationDao.findById(currentUser.getOrganizationId());
        Organization belongOrg = null;
        if(currentOrg.getBelong()!=null){
        	belongOrg = organizationDao.findById(currentOrg.getBelong());
        }
        
        
        Organization branch = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(), OrganizationType.BRENCH,true);
        //分本部门校区也要确认是否是校区
        Organization campus = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(), OrganizationType.CAMPUS,false);
        Organization group = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(), OrganizationType.GROUNP,false);
        //获取当前登录者的所有职位 根据职位来筛选
    	List<UserDeptJob> userDeptJobs = userDeptJobService.findDeptJobByUserId(currentUser.getUserId());
    	
    	//此处写死 判断当前登录者 是否具有网络职位 
    	Boolean isNetwork = false;
        List<UserDeptJob> deptJobs = userDeptJobService.getDeptJobByKey("网络", currentUser.getUserId());
        if(deptJobs!=null && deptJobs.size()>0){
        	isNetwork = true;
        }

      //判断当前登录者是否是集团的人
        Boolean isWLZG = userService.isUserRoleCode(currentUser.getUserId(), RoleCode.NETWORK_MANAGER);


    	
    	
    	
    	//获取所有可以配置的资源池  
		List<ResourcePoolUserVo> retList = new ArrayList<ResourcePoolUserVo>();
	
		for (UserDeptJob userDeptJob : userDeptJobs) {
			Organization org = organizationDao.findById(userDeptJob.getDeptId());
			String orgLevel = org.getOrgLevel();
			String jobId = userDeptJob.getJobId();
			if (isNetwork || isWLZG) {
				orgLevel = group.getOrgLevel();
			} else {
				if (belongOrg != null) {
					if (belongOrg.getOrgType() == OrganizationType.CAMPUS) {
						orgLevel = belongOrg.getOrgLevel();
					} else if (belongOrg.getOrgType() == OrganizationType.BRENCH) {
						orgLevel = branch.getOrgLevel();
					}
				} else {
					if (currentOrg.getOrgType() == OrganizationType.CAMPUS) {
						orgLevel = currentOrg.getOrgLevel();
					} else if (currentOrg.getOrgType() == OrganizationType.BRENCH) {
						orgLevel = branch.getOrgLevel();
					}
				}

			}

			
			retList.addAll(organizationDao.getDisValidateResourcePool(orgLevel, jobId, ResourcePoolJobType.VISIBLE));
		}	
		if (retList != null && retList.size() > 0) {
			retList=removeDuplicate(retList);//所有可以配置的资源池  已经去重 
		}
    	
       	
//    	//获取所有可以配置的资源池  
//		List<ResourcePoolUserVo> retList = new ArrayList<ResourcePoolUserVo>();
//	
//		for (UserDeptJob userDeptJob : userDeptJobs) {
//			Organization org = organizationDao.findById(userDeptJob.getId().getDeptId());
//			String orgLevel = org.getOrgLevel();
//			String jobId = userDeptJob.getId().getJobId();
//			retList.addAll(organizationDao.getDisValidateResourcePool(orgLevel, jobId, ResourcePoolJobType.VISIBLE));
//		}	
//		if (retList != null && retList.size() > 0) {
//			retList=removeDuplicate(retList);//所有可以配置的资源池  已经去重 
//		}
		
		
		
		
		
		//可以配置的资源池
		List<ResourcePoolUserVo> distributable_Pool = new ArrayList<ResourcePoolUserVo>();
		ResourcePoolUserVo userPoolVo = null;
		for(ResourcePoolUserVo tVo:retList){
			userPoolVo = tVo;
			distributable_Pool.add(userPoolVo);
		}

				
		//获取所有可以看见的也就是可以配置的职位----begin
		List<UserJob> distributableJobs = new ArrayList<UserJob>();
		List<UserJob> currentOrgDistributableJobs = new ArrayList<UserJob>();
		
		for(UserDeptJob userDeptJob:userDeptJobs){
			String jobId = userDeptJob.getJobId();
		    distributableJobs.addAll(distributableUserJobService.findRelateUserJobByMainJobId(jobId));
		    currentOrgDistributableJobs.addAll(distributableUserJobService.findRelateUserJobByJobIdAndOrgLevel(jobId, currentOrg.getOrgLevel()));
        }
		StringBuffer jobs = new StringBuffer(256);
		String jobIds = null;
		String currentOrgJobIds =null;
		String otherOrgJobIds =null;
		List<String> all_ids = new ArrayList<String>();
		List<String> current_ids = new ArrayList<String>();
		List<String> other_ids = new ArrayList<String>();
		
		
		if(distributableJobs.size()>0){
			distributableJobs = removeDuplicateUserJob(distributableJobs);
			for(UserJob job:distributableJobs){
				jobs.append(""+job.getId()+",");
				all_ids.add(job.getId());
			}
			if(jobs.length()>=1){
				jobIds =jobs.substring(0, jobs.length()-1);
			}
		}
		String[] jobIdArr = null;
		if(jobIds!=null){
			jobIdArr = jobIds.split(",");
		}		
		jobs.delete(0, jobs.length());
		if(currentOrgDistributableJobs.size()>0){
			currentOrgDistributableJobs = removeDuplicateUserJob(currentOrgDistributableJobs);
			for(UserJob job:currentOrgDistributableJobs){
				jobs.append("'"+job.getId()+"',");
				current_ids.add(job.getId());
			}
			if(jobs.length()>=1){
				currentOrgJobIds =jobs.substring(0, jobs.length()-1);
			}
		}
		jobs.delete(0, jobs.length());
		
		if(all_ids.size()>0){
			for(String keys:all_ids){
				if(current_ids.size()>0){
					if(current_ids.indexOf(keys)==-1){
						other_ids.add(keys);
					}
				}else{
					other_ids.add(keys);
				}
			}
		}
		if(other_ids.size()>0){
			for(String keystring:other_ids){
				jobs.append("'"+keystring+"',");
			}
			if(jobs.length()>=1){
				otherOrgJobIds =jobs.substring(0, jobs.length()-1);
			}
		}
			
		
		//获取所有可以看见的也就是可以配置的职位----end
		
		
		
		Mapper mapper = HibernateUtils.getMapper();
		
		List<ResourcePoolUserVo> AllResourcePoolUserVos =  new ArrayList<ResourcePoolUserVo>();
		List<ResourcePoolUserVo> disResourcePoolUserVos =  new ArrayList<ResourcePoolUserVo>();
		//查询这些子节点的资源容量
		StringBuilder query = new StringBuilder(512);
		StringBuilder query_org = new StringBuilder(512);
		//装载 资源量和对应id的两个数组
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Object> amounts = new ArrayList<Object>();
		if (StringUtil.isNotBlank(organizationId)) {
			Organization organization = organizationDao.findById(organizationId);
			//获取被选取节点的所有子节点
			List<Organization> childOrgs = organizationService.getAllChildOrganizationsByOrgLevel(organization.getOrgLevel());
				

			
			if(childOrgs!=null && childOrgs.size()>0){
				
				for(Organization tempOrg:childOrgs){
					query_org.append("'"+tempOrg.getId()+"',");
				}
				
				query.append(" select IFNULL(rp.capacity,0) as capacity,concat('',o.id) as id ,");
				query.append(" o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from organization o ");
				query.append(" left join resource_pool rp on o.id = rp.organization_id where o.id in( " +query_org.substring(0, query_org.length()-1)+ " )");
				List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String,Object>());
				query.delete(0, query.length());
				
				List<Map<Object, Object>> query_amount =null ;
				if(query_org.length()>1){
					query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+query_org.substring(0, query_org.length()-1)+") and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"','"+CustomerDealStatus.INVALID+"') GROUP BY c.DELEVER_TARGET ");
					query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String,Object>());
					query.delete(0, query.length());
					query_org.delete(0, query_org.length());				
				}
				if (query_amount != null) {
					for (Map<Object, Object> amountmap : query_amount) {
						ids.add(amountmap.get("id").toString());
						amounts.add(amountmap.get("current"));
					}
				}
				
					
	            String id = null;
	            int index = -1;
	            
				ResourcePoolUserVo vo = null;
				for(Map<Object, Object> map:list){
					vo = new ResourcePoolUserVo();
					//设置当前容量 --begin
					if(map.get("id")!=null){
						id = map.get("id").toString();
						index = ids.indexOf(id);
						if(index!=-1){
							vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
						}else{
							vo.setCurrent(0);
						}					
					}else{
						vo.setCurrent(0);
					}
					//设置当前容量 --end
					vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
					vo.setId(map.get("id")!=null?map.get("id").toString():"");
					vo.setName(map.get("name")!=null?map.get("name").toString():"");
					vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
					vo.setOrgType(OrganizationType.valueOf(map.get("orgType").toString()));
					vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():map.get("name").toString());
					vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
					vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
					vo.setParent(false);				
					disResourcePoolUserVos.add(vo);			
				}
				
				Integer current = 0;
				Integer total = 0;			
				for(ResourcePoolUserVo rUserVo:disResourcePoolUserVos){
					current = rUserVo.getCurrent();
					total = rUserVo.getTotal();
					if (distributable_Pool != null && distributable_Pool.size() > 0) {
						for (ResourcePoolUserVo tVo : distributable_Pool) {
							if (rUserVo.getId().equals(tVo.getId())) {
								rUserVo.setResourcePoolstatus(ValidStatus.VALID);
								rUserVo.setCurrent(current);
								rUserVo.setTotal(total);
								break;
							} else {
								rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
								rUserVo.setCurrent(0);
								rUserVo.setTotal(0);
							}
						}
					}else{
						rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
						rUserVo.setCurrent(0);
						rUserVo.setTotal(0);					
					}
				}
				
				
				
				AllResourcePoolUserVos.addAll(disResourcePoolUserVos);
				
			}else{
				//说明是叶子节点
				ResourcePoolUserVo poolUserVo = mapper.map(organization,ResourcePoolUserVo.class);
				disResourcePoolUserVos.add(poolUserVo);		
			}			
			
		}else{
			
//		    byte[] object =JedisUtil.get(redisKey.getBytes());
//		    if(object!=null){
//		    	Object value = JedisUtil.ByteToObject(object);
//		    	
//		    	List<ResourcePoolUserVo> list = (List<ResourcePoolUserVo>)value;
//		    	
//			    for(ResourcePoolUserVo wang:list){	
//			    	System.out.println("缓存:"+wang.getId()+"-"+wang.getName()+"-"+wang.getParentId()+"-"+(wang.getResourcePoolstatus()!=null?wang.getResourcePoolstatus().getValue():"NULL")+"-"
//			    			+wang.getNodeState());
//			    }
//			    
//			    return list;
//		    	
//		    }


			//这个可以缓存一下retList	
			//获取所有可以配置的资源池的父节点---begin
			List<Organization> organizations = new ArrayList<Organization>();
			for(ResourcePoolUserVo temp:retList){
				organizations.addAll(organizationService.getAllParentOrganizationsByOrgLevel(temp.getOrgLevel()));
			}

			
			//还要加上所有可以分配的职位所属的部门也要加载
			if(jobIds!=null){
				List<Organization> distributableJobOrg = userDeptJobService.getDeptByJobIds(branch.getOrgLevel(),campus,currentOrg.getOrgLevel(),isNetwork,jobIds,currentOrgJobIds,otherOrgJobIds);
				organizations.addAll(distributableJobOrg);
				if(distributableJobOrg!=null && distributableJobOrg.size()>0){
					for(Organization org:distributableJobOrg){
						organizations.addAll(organizationService.getAllParentOrganizationsByOrgLevel(org.getOrgLevel()));
					}
				}
			}
			
			//所有的父节点去重复
			if(organizations.size()>0){
				organizations = removeDuplicateOrganizaiton(organizations);
			}
			
			for(Organization organization:organizations){
				ResourcePoolUserVo poolUserVo = mapper.map(organization,ResourcePoolUserVo.class);
				//poolUserVo.setResourcePoolstatus(ValidStatus.INVALID);
				retList.add(poolUserVo);
			}
			
			
			//获取所有可以配置的资源池的父节点---end
			
			//可配置节点和这些节点的父子节点合并在一起  还要继续去重
			if (retList != null && retList.size() > 0) {
				retList=removeDuplicate(retList);//
			}else{
				return AllResourcePoolUserVos;
			}
			//
			for(ResourcePoolUserVo tempOrg:retList){//要保证这里没有重复
				query_org.append("'"+tempOrg.getId()+"',");
			}
			
			String queryOrg = query_org.substring(0, query_org.length()-1);
			
			query.append(" select IFNULL(rp.capacity,0) as capacity,concat('',o.id) as id ,");
			query.append(" o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from organization o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id where o.id in( " +queryOrg+ " )");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());
			
			List<Map<Object, Object>> query_amount =null ;
			if(query_org.length()>1){
				query.append(" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("+queryOrg+") and c.DEAL_STATUS NOT in ('"+CustomerDealStatus.SIGNEUP+"','"+CustomerDealStatus.INVALID+"') GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());			
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(StringUtil.toString(amountmap.get("id")));
					amounts.add(StringUtil.toString(amountmap.get("current")));
				}
			}
			
				
            String id = null;
            int index = -1;
            
			ResourcePoolUserVo vo = null;
			for(Map<Object, Object> map:list){
				vo = new ResourcePoolUserVo();
				//设置当前容量 --begin
				if(map.get("id")!=null){
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if(index!=-1){
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					}else{
						vo.setCurrent(0);
					}					
				}else{
					vo.setCurrent(0);
				}
				//设置当前容量 --end
				
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id")!=null?map.get("id").toString():"");
				vo.setName(map.get("name")!=null?map.get("name").toString():"");
				vo.setChildNodeNums(organizationService.getChildNodeNumsByOrgId(vo.getId(), queryOrg.replaceAll("'", "").split(",")));
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(map.get("orgType").toString()));
				vo.setResourcePoolName(map.get("poolName")!=null?map.get("poolName").toString():map.get("name").toString());
				vo.setResourcePoolstatus(map.get("poolStatus")!=null?ValidStatus.valueOf(map.get("poolStatus").toString()):null);
				vo.setParentId(map.get("parentID")!=null?map.get("parentID").toString():"");
				vo.setParent(false);
				disResourcePoolUserVos.add(vo);			
			}
				
			Integer current = 0;
			Integer total = 0;	
			for (ResourcePoolUserVo rUserVo : disResourcePoolUserVos) {
				current = rUserVo.getCurrent();
				total = rUserVo.getTotal();
				if (distributable_Pool != null && distributable_Pool.size() > 0) {
					for (ResourcePoolUserVo tVo : distributable_Pool) {
						if (rUserVo.getId().equals(tVo.getId())) {
							rUserVo.setResourcePoolstatus(ValidStatus.VALID);
							rUserVo.setCurrent(current);
							rUserVo.setTotal(total);
							break;
						} else {
							rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
							rUserVo.setCurrent(0);
							rUserVo.setTotal(0);
						}
					}
				}else{
					rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
					rUserVo.setCurrent(0);
					rUserVo.setTotal(0);					
				}
			}
			
			AllResourcePoolUserVos.addAll(disResourcePoolUserVos);		
		}
		
		//如果职位为空 说明一个人都没有 直接返回 所有的资源池
		if (jobIds == null) {

			for (ResourcePoolUserVo resourcePoolUserVo : AllResourcePoolUserVos) {
				if (!resourcePoolUserVo.isParent() && (resourcePoolUserVo.getChildNodeNums() == 0)
						&& resourcePoolUserVo.getResourcePoolstatus() == ValidStatus.INVALID) {
					resourcePoolUserVo.setNodeState(0);
				} else {
					resourcePoolUserVo.setNodeState(1);
				}
				if (resourcePoolUserVo.getId().equals("000001")) {
					resourcePoolUserVo.setNodeState(1);

				}

			}
			return AllResourcePoolUserVos;
		}
		
		List<User> userList = new ArrayList<User>();		
		List<ResourcePoolUserVo> resourcePoolUserVoList =new  ArrayList<ResourcePoolUserVo>();
		if (disResourcePoolUserVos != null && disResourcePoolUserVos.size() > 0) {		
			boolean isGetUserByOrganizationId = false;
			for(ResourcePoolUserVo resourcePoolUserVo : disResourcePoolUserVos){
				userList.clear();
				if (StringUtil.isBlank(organizationId)) {
//					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
//							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH || resourcePoolUserVo.getOrgLevel().length() <= 8) {
//						
//						userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds);
//					} else  if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
//							|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
//						resourcePoolUserVo.setParent(true);
//					}
//					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP 
//							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH ||resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
//						userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds);
//					} else  if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS 
//							 ) {
//						resourcePoolUserVo.setParent(true);
//					}

//					if(!resourcePoolUserVo.isParent()&&(userList.size()==0)){
//						resourcePoolUserVo.setNodeState(0);
					
					userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIdArr);
					
					if(!resourcePoolUserVo.isParent()&&(userList.size()==0)&&(resourcePoolUserVo.getChildNodeNums()==0)
							&&resourcePoolUserVo.getResourcePoolstatus()==ValidStatus.INVALID){
						resourcePoolUserVo.setNodeState(0);
					}else{
						resourcePoolUserVo.setNodeState(1);
					}
					if(resourcePoolUserVo.getId().equals("000001")){
						resourcePoolUserVo.setNodeState(1);
					}
					
					
					//userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds);
				} else {
										
					String resourcePoolName = resourcePoolUserVo.getResourcePoolName();
					if (StringUtils.isNotBlank(resourcePoolName)) {
						resourcePoolUserVo.setName(resourcePoolName);
					}
					//获取资源池的人
					userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(),jobIds.replaceAll("'", "").split(","));
					
					
					if(!resourcePoolUserVo.isParent()&&(userList.size()==0)&&(resourcePoolUserVo.getChildNodeNums()==0)
							&&resourcePoolUserVo.getResourcePoolstatus()==ValidStatus.INVALID){
						resourcePoolUserVo.setNodeState(0);
					}else{
						resourcePoolUserVo.setNodeState(1);
					}
					
					
					if (!isGetUserByOrganizationId) {
						userList.addAll(loadDistributableUserByUserJob(organizationId,jobIds.replaceAll("'", "").split(",")));
						isGetUserByOrganizationId = true;
					}
					
					
				}
				ResourcePoolUserVo userVo = null;
				Map<String, Integer> userMap =null;
				for(User user : userList){
				    userVo=new ResourcePoolUserVo();
				    //设置个人的资源池容量
				    userMap= getResourcePoolVolumeByUserId(user.getUserId());
				    userVo.setCurrent(userMap.get("current"));
				    userVo.setTotal(userMap.get("total"));
				    
					userVo.setId(user.getUserId());
					userVo.setName(user.getName());
					userVo.setParentId(user.getOrganizationId());
					
					userVo.setNodeState(1);
					
					resourcePoolUserVoList.add(userVo);
				}
			}
		}
	    AllResourcePoolUserVos.addAll(resourcePoolUserVoList); 
	    
	    for(ResourcePoolUserVo wang:AllResourcePoolUserVos){	
	    	System.out.println(wang.getId()+"-"+wang.getName()+"-"+wang.getParentId()+"-"+(wang.getResourcePoolstatus()!=null?wang.getResourcePoolstatus().getValue():"NULL")+"-"
	    			+wang.getNodeState());
	    }
	    
//	    JedisUtil.set(redisKey.getBytes(), JedisUtil.ObjectToByte(AllResourcePoolUserVos),5*60);
	    
	    
	    
    	return AllResourcePoolUserVos;
		
	}

	@Deprecated
    private List<User> loadResourcePoolUserDataImport(String orgId){
    	Map<String, Object> params = new HashMap<String, Object>();
    	StringBuilder query = new StringBuilder(128);    	
    	query.append("SELECT * from `user` where organizationID = :orgId ");
    	params.put("orgId", orgId);
    	query.append(" and USER_ID in(SELECT USER_ID from user_dept_job WHERE JOB_ID in(SELECT rpj.JOB_ID as userJobId from resource_pool_job  rpj where rpj.ORGANIZATION_ID = '"+orgId+"' ORDER BY rpj.CREATE_TIME desc) ");
    	query.append(" and DEPT_ID = :orgId2 ) and enable_flag=0 ");
    	params.put("orgId2", orgId);
		query.append(" and USER_ID in (SELECT ur.userID from user_role ur WHERE ur.roleID in (SELECT r.id from role r WHERE r.roleCode='"
				+ RoleCode.OUTCALL_MANAGER + "' or r.roleCode='"+RoleCode.CONSULTOR+"' )) ");
    	List<User> list = userdao.findBySql(query.toString(), params);
    	return list;

    }

	@Override
	public List<ResourcePoolUserVo> loadSearchResourcePoolAndUser(String key) {

		User currentUser = userService.getCurrentLoginUser();
		List<ResourcePoolUserVo> AllResourcePoolUserVos = new ArrayList<ResourcePoolUserVo>();
//		String redisKey = currentUser.getUserId() + "resourcePool";// 加入缓存
//		byte[] object = JedisUtil.get(redisKey.getBytes());
		byte[] object = null;
		if (object != null) {
			Object value = JedisUtil.ByteToObject(object);
			AllResourcePoolUserVos = (List<ResourcePoolUserVo>) value;
		} else {

			Organization currentOrg = organizationDao.findById(currentUser.getOrganizationId());
			Organization branch = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(),
					OrganizationType.BRENCH,true);
			Organization campus = userService.getOrgazitionByOrgType(currentOrg.getOrgLevel(),
					OrganizationType.CAMPUS,false);
			// 获取当前登录者的所有职位 根据职位来筛选
			List<UserDeptJob> userDeptJobs = userDeptJobService.findDeptJobByUserId(currentUser.getUserId());

			// 此处写死 判断当前登录者 是否具有网络职位
			Boolean isNetwork = false;
			List<UserDeptJob> deptJobs = userDeptJobService.getDeptJobByKey("网络", currentUser.getUserId());
			if (deptJobs != null && deptJobs.size() > 0) {
				isNetwork = true;
			}

			// 获取所有可以配置的资源池
			List<ResourcePoolUserVo> retList = new ArrayList<ResourcePoolUserVo>();

			for (UserDeptJob userDeptJob : userDeptJobs) {
				Organization org = organizationDao.findById(userDeptJob.getDeptId());
				String orgLevel = org.getOrgLevel();
				String jobId = userDeptJob.getJobId();
				retList.addAll(
						organizationDao.getDisValidateResourcePool(orgLevel, jobId, ResourcePoolJobType.VISIBLE));
			}
			if (retList != null && retList.size() > 0) {
				retList = removeDuplicate(retList);// 所有可以配置的资源池 已经去重
			}

			// 可以配置的资源池
			List<ResourcePoolUserVo> distributable_Pool = new ArrayList<ResourcePoolUserVo>();
			ResourcePoolUserVo userPoolVo = null;
			for (ResourcePoolUserVo tVo : retList) {
				userPoolVo = tVo;
				distributable_Pool.add(userPoolVo);
			}

			// 获取所有可以看见的也就是可以配置的职位----begin
			List<UserJob> distributableJobs = new ArrayList<UserJob>();
			List<UserJob> currentOrgDistributableJobs = new ArrayList<UserJob>();
			List<String> all_ids = new ArrayList<String>();
			List<String> current_ids = new ArrayList<String>();
			List<String> other_ids = new ArrayList<String>();

			for (UserDeptJob userDeptJob : userDeptJobs) {
				String jobId = userDeptJob.getJobId();
				distributableJobs.addAll(distributableUserJobService.findRelateUserJobByMainJobId(jobId));
				currentOrgDistributableJobs.addAll(distributableUserJobService
						.findRelateUserJobByJobIdAndOrgLevel(jobId, currentOrg.getOrgLevel()));
			}
			StringBuffer jobs = new StringBuffer(256);
			String jobIds = null;
			String currentOrgJobIds = null;
			String otherOrgJobIds = null;
			if (distributableJobs.size() > 0) {
				distributableJobs = removeDuplicateUserJob(distributableJobs);
				for (UserJob job : distributableJobs) {
					jobs.append("'" + job.getId() + "',");
				}
				if (jobs.length() >= 1) {
					jobIds = jobs.substring(0, jobs.length() - 1);
				}
			}
			jobs.delete(0, jobs.length());
			if (currentOrgDistributableJobs.size() > 0) {
				currentOrgDistributableJobs = removeDuplicateUserJob(currentOrgDistributableJobs);
				for (UserJob job : currentOrgDistributableJobs) {
					jobs.append("'" + job.getId() + "',");
				}
				if (jobs.length() >= 1) {
					currentOrgJobIds = jobs.substring(0, jobs.length() - 1);
				}
			}
			jobs.delete(0, jobs.length());

			if(all_ids.size()>0){
				for(String keys:all_ids){
					if(current_ids.size()>0){
						if(current_ids.indexOf(keys)==-1){
							other_ids.add(keys);
						}
					}
				}
			}
			if(other_ids.size()>0){
				for(String keystring:other_ids){
					jobs.append("'"+keystring+"',");
				}
				if(jobs.length()>=1){
					otherOrgJobIds =jobs.substring(0, jobs.length()-1);
				}
			}

			// 获取所有可以看见的也就是可以配置的职位----end

			Mapper mapper = HibernateUtils.getMapper();

			List<ResourcePoolUserVo> disResourcePoolUserVos = new ArrayList<ResourcePoolUserVo>();
			// 查询这些子节点的资源容量
			StringBuilder query = new StringBuilder(512);
			StringBuilder query_org = new StringBuilder(512);
			// 装载 资源量和对应id的两个数组
			ArrayList<String> ids = new ArrayList<String>();
			ArrayList<Object> amounts = new ArrayList<Object>();

			// 这个可以缓存一下retList
			// 获取所有可以配置的资源池的父节点---begin
			List<Organization> organizations = new ArrayList<Organization>();
			for (ResourcePoolUserVo temp : retList) {
				organizations.addAll(organizationService.getAllParentOrganizationsByOrgLevel(temp.getOrgLevel()));
			}

			// 还要加上所有可以分配的职位所属的部门也要加载
			if (jobIds != null) {
				List<Organization> distributableJobOrg = userDeptJobService.getDeptByJobIds(branch.getOrgLevel(),campus,
						currentOrg.getOrgLevel(), isNetwork, jobIds, currentOrgJobIds, otherOrgJobIds);
				organizations.addAll(distributableJobOrg);
				if (distributableJobOrg != null && distributableJobOrg.size() > 0) {
					for (Organization org : distributableJobOrg) {
						organizations
								.addAll(organizationService.getAllParentOrganizationsByOrgLevel(org.getOrgLevel()));
					}
				}
			}

			// 所有的父节点去重复
			if (organizations.size() > 0) {
				organizations = removeDuplicateOrganizaiton(organizations);
			}

			for (Organization organization : organizations) {
				ResourcePoolUserVo poolUserVo = mapper.map(organization, ResourcePoolUserVo.class);
				// poolUserVo.setResourcePoolstatus(ValidStatus.INVALID);
				retList.add(poolUserVo);
			}

			// 获取所有可以配置的资源池的父节点---end

			// 可配置节点和这些节点的父子节点合并在一起 还要继续去重
			if (retList != null && retList.size() > 0) {
				retList = removeDuplicate(retList);//
			} else {
				return AllResourcePoolUserVos;
			}
			//
			for (ResourcePoolUserVo tempOrg : retList) {// 要保证这里没有重复
				query_org.append("'" + tempOrg.getId() + "',");
			}

			String queryOrg = query_org.substring(0, query_org.length() - 1);
			query.append(" select IFNULL(rp.capacity,0) as capacity,concat('',o.id) as id ,");
			query.append(
					" o.`name`,o.orgLevel,o.orgType,o.parentID,o.RESOURCE_POOL_NAME as poolName,o.RESOURCE_POOL_STATUS as poolStatus from organization o ");
			query.append(" left join resource_pool rp on o.id = rp.organization_id where o.id in( "
					+ queryOrg + " )");
			List<Map<Object, Object>> list = organizationDao.findMapBySql(query.toString(), new HashMap<String, Object>());
			query.delete(0, query.length());

			List<Map<Object, Object>> query_amount = null;
			if (query_org.length() > 1) {
				query.append(
						" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("
								+ queryOrg + ") and c.DEAL_STATUS NOT in ('"+ CustomerDealStatus.SIGNEUP + "','"+ CustomerDealStatus.INVALID + "') GROUP BY c.DELEVER_TARGET ");
//				query.append(
//						" select count(1) as current,c.DELEVER_TARGET as id  from customer c where c.DELEVER_TARGET in("
//								+ queryOrg + ")  GROUP BY c.DELEVER_TARGET ");
				query_amount = customerDao.findMapBySql(query.toString(), new HashMap<String, Object>());
				query.delete(0, query.length());
				query_org.delete(0, query_org.length());
			}
			if (query_amount != null) {
				for (Map<Object, Object> amountmap : query_amount) {
					ids.add(amountmap.get("id").toString());
					amounts.add(amountmap.get("current"));
				}
			}

			String id = null;
			int index = -1;

			ResourcePoolUserVo vo = null;
			for (Map<Object, Object> map : list) {
				vo = new ResourcePoolUserVo();
				// 设置当前容量 --begin
				if (map.get("id") != null) {
					id = StringUtil.toString(map.get("id"));
					index = ids.indexOf(id);
					if (index != -1) {
						vo.setCurrent(Integer.valueOf(amounts.get(index).toString()));
					} else {
						vo.setCurrent(0);
					}
				} else {
					vo.setCurrent(0);
				}
				// 设置当前容量 --end
				vo.setTotal(Integer.valueOf(StringUtil.toString(map.get("capacity"))));
				vo.setId(map.get("id") != null ? map.get("id").toString() : "");
				vo.setChildNodeNums(organizationService.getChildNodeNumsByOrgId(vo.getId(), queryOrg.replaceAll("'", "").split(",")));
				vo.setName(map.get("name") != null ? map.get("name").toString() : "");
				vo.setOrgLevel(StringUtil.toString(map.get("orgLevel")));
				vo.setOrgType(OrganizationType.valueOf(StringUtil.toString(map.get("orgType"))));
				vo.setResourcePoolName(
						map.get("poolName") != null ? map.get("poolName").toString() : map.get("name").toString());
				vo.setResourcePoolstatus(
						map.get("poolStatus") != null ? ValidStatus.valueOf(map.get("poolStatus").toString()) : null);
				vo.setParentId(map.get("parentID") != null ? map.get("parentID").toString() : "");
				vo.setParent(false);
				disResourcePoolUserVos.add(vo);
			}

			Integer current = 0;
			Integer total = 0;
			for (ResourcePoolUserVo rUserVo : disResourcePoolUserVos) {
				current = rUserVo.getCurrent();
				total = rUserVo.getTotal();
				if (distributable_Pool != null && distributable_Pool.size() > 0) {
					for (ResourcePoolUserVo tVo : distributable_Pool) {
						if (rUserVo.getId().equals(tVo.getId())) {
							rUserVo.setResourcePoolstatus(ValidStatus.VALID);
							rUserVo.setCurrent(current);
							rUserVo.setTotal(total);
							break;
						} else {
							rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
							rUserVo.setCurrent(0);
							rUserVo.setTotal(0);
						}
					}
				} else {
					rUserVo.setResourcePoolstatus(ValidStatus.INVALID);
					rUserVo.setCurrent(0);
					rUserVo.setTotal(0);
				}
			}

			AllResourcePoolUserVos.addAll(disResourcePoolUserVos);

			// 如果职位为空 说明一个人都没有 直接返回
			if (jobIds == null) {
				return AllResourcePoolUserVos;
			}

			List<User> userList = new ArrayList<User>();
			List<ResourcePoolUserVo> resourcePoolUserVoList = new ArrayList<ResourcePoolUserVo>();
			if (disResourcePoolUserVos != null && disResourcePoolUserVos.size() > 0) {
				boolean isGetUserByOrganizationId = false;
				for (ResourcePoolUserVo resourcePoolUserVo : disResourcePoolUserVos) {
					userList.clear();

					if (resourcePoolUserVo.getOrgType() == OrganizationType.GROUNP
							|| resourcePoolUserVo.getOrgType() == OrganizationType.BRENCH
							|| resourcePoolUserVo.getOrgType() == OrganizationType.DEPARTMENT) {
						userList = loadDistributableUserByUserJob(resourcePoolUserVo.getId(), jobIds.replaceAll("'", "").split(","));
					} else if (resourcePoolUserVo.getOrgType() == OrganizationType.CAMPUS) {
						resourcePoolUserVo.setParent(true);
					}

					if(!resourcePoolUserVo.isParent()&&(userList.size()==0)&&(resourcePoolUserVo.getChildNodeNums()==0)
							&&resourcePoolUserVo.getResourcePoolstatus()==ValidStatus.INVALID){
						resourcePoolUserVo.setNodeState(0);
//					if (!resourcePoolUserVo.isParent() && (userList.size() == 0)) {
//						resourcePoolUserVo.setNodeState(0);
					} else {
						resourcePoolUserVo.setNodeState(1);
					}
					if (resourcePoolUserVo.getId().equals("000001")) {
						resourcePoolUserVo.setNodeState(1);
					}
					ResourcePoolUserVo userVo = null;
					Map<String, Integer> userMap = null;
					for (User user : userList) {
						userVo = new ResourcePoolUserVo();
						// 设置个人的资源池容量
						userMap = getResourcePoolVolumeByUserId(user.getUserId());
						userVo.setCurrent(userMap.get("current"));
						userVo.setTotal(userMap.get("total"));

						userVo.setId(user.getUserId());
						userVo.setName(user.getName());
						userVo.setParentId(user.getOrganizationId());

						userVo.setNodeState(1);

						resourcePoolUserVoList.add(userVo);
					}
				}
			}
			AllResourcePoolUserVos.addAll(resourcePoolUserVoList);

		}

		// 开始搜索
		List<ResourcePoolUserVo> searchList = new ArrayList<>();
		List<ResourcePoolUserVo> resultList = new ArrayList<>();
		if (AllResourcePoolUserVos.size() == 0) {
			return searchList;
		}
		List<String> AllIds = new ArrayList<>();
		for (ResourcePoolUserVo search : AllResourcePoolUserVos) {
			AllIds.add(search.getId());
			if ((search.getName() != null && search.getName().indexOf(key) != -1)
					|| (search.getResourcePoolName() != null && search.getResourcePoolName().indexOf(key) != -1)) {
				searchList.add(search);
			}
		}
		if (searchList.size() == 0) {
			return searchList;
		}
		resultList.addAll(searchList);

		resultList.addAll(getParent(resultList, AllIds, AllResourcePoolUserVos, searchList));
		resultList = removeDuplicate(resultList);
		return resultList;
	}
	
	private List<ResourcePoolUserVo> getParent(List<ResourcePoolUserVo> AllList,List<String> allIds,List<ResourcePoolUserVo> list,List<ResourcePoolUserVo> searchList){
		List<ResourcePoolUserVo> temp = new ArrayList<>();
		for (ResourcePoolUserVo search : searchList) {
            if(StringUtils.isNotBlank(search.getParentId())){          	
            	temp.add(list.get(allIds.indexOf(search.getParentId())));
            }
		}
		if(temp.size()==0){
			return AllList;
		}else{
			AllList.addAll(temp);
			return getParent(AllList, allIds, list, temp);
		}
		
		
	
	}
	
	public static void main(String[] args) {

		
	}

	@Override
	public Response findResourcePoolByIdNew(String organizationId) {
		// TODO Auto-generated method stub
		Response res  = new Response();
    	
		ResourcePool resourcePool = resourcePoolDao.findById(organizationId);
		if(resourcePool == null) {
			res.setResultCode(-1);
    		res.setResultMessage("找不到对应的资源池！");
    		return res;
		}
		ResourcePoolVo resourcePoolVo = HibernateUtils.voObjectMapping(resourcePool, ResourcePoolVo.class);
		res.setData(resourcePoolVo);
		
		return res;
	}

	@Override
	public List<ResourcePoolRole> getResourcePoolRoleList(ResourcePoolRoleVo resourcePoolroleVo) {
		// TODO Auto-generated method stub
		List<ResourcePoolRole> resourcePoolRoles = resourcePoolRoleDao.getResourcePoolRoleList(resourcePoolroleVo);
		return resourcePoolRoles;
	}

	@Override
	public Response saveResourcePoolNew(ResourcePoolVo resourcePoolVo) {
		// TODO Auto-generated method stub
		Response res = new Response();
		if(resourcePoolVo == null || StringUtil.isEmpty(resourcePoolVo.getOrganizationId())){
    		res.setResultCode(-1);
    		res.setResultMessage("参数有误，资源池参数为空或组织架构ID未传！");
    		return res;
		}
		//ResourcePool resourcePool = HibernateUtils.voObjectMapping(resourcePoolVo, ResourcePool.class);
		ResourcePool resourcePool = resourcePoolDao.findById(resourcePoolVo.getOrganizationId());
		if(resourcePool == null) {
			resourcePool = HibernateUtils.voObjectMapping(resourcePoolVo, ResourcePool.class);
		}else {
			resourcePool.setName(resourcePoolVo.getName());
			resourcePool.setStatus(resourcePoolVo.getStatus());
			resourcePool.setCapacity(resourcePoolVo.getCapacity());
			resourcePool.setCycleType(resourcePoolVo.getCycleType());
			resourcePool.setRecoveyPeriod(resourcePoolVo.getRecoveyPeriod());
			resourcePool.setReturnNote(resourcePoolVo.getReturnNote());
		}
		this.saveEditResourcePool(resourcePool);
		
		List<ResourcePoolRoleVo> roleVos = resourcePoolVo.getRoleVos();
		ResourcePoolRoleVo resourcePoolroleVo = new ResourcePoolRoleVo();
		resourcePoolroleVo.setOrganizationId(resourcePoolVo.getOrganizationId());
		List<ResourcePoolRole> oldRoles = resourcePoolRoleDao.getResourcePoolRoleList(resourcePoolroleVo);
		
		Map<String , ResourcePoolRole> map = new HashMap<>();
		for(ResourcePoolRole rpr : oldRoles) {
			map.put(rpr.getRoleId()+rpr.getOrganizationId(), rpr);
		}
		if(roleVos != null && roleVos.size()>0) {
			for(ResourcePoolRoleVo vo : roleVos) {
				ResourcePoolRole rpr = map.get(vo.getRoleId()+vo.getOrganizationId());
				if(rpr !=null) {
					rpr.setOneTimeResource(vo.getOneTimeResource());
					rpr.setModifyUserId(userService.getCurrentLoginUser().getUserId());
					resourcePoolRoleDao.save(rpr);
					map.remove(vo.getRoleId()+vo.getOrganizationId());
				}else {
					rpr = new ResourcePoolRole();
					rpr.setOneTimeResource(vo.getOneTimeResource());
					rpr.setRoleId(vo.getRoleId());
					rpr.setOrganizationId(vo.getOrganizationId());
					rpr.setModifyUserId(userService.getCurrentLoginUser().getUserId());
					rpr.setCreateUserId(userService.getCurrentLoginUser().getUserId());
					resourcePoolRoleDao.save(rpr);
				}
			}
			for(String key :map.keySet()) {
				ResourcePoolRole rpr = map.get(key);
				resourcePoolRoleDao.delete(rpr);
			}
		}else {
			resourcePoolRoleDao.deleteAll(oldRoles);
		}
		
		return res;
	}

	@Override
	public DataPackage getResourcePoolRoleList(DataPackage dataPackage, ResourcePoolRoleVo resourcePoolRoleVo) {
		// TODO Auto-generated method stub
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtil.isNotBlank(resourcePoolRoleVo.getOrganizationId())) {
			criterionList.add(Restrictions.eq("organizationId",
					resourcePoolRoleVo.getOrganizationId()));
		}
		
		dataPackage =  resourcePoolRoleDao.findPageByCriteria(dataPackage,
				HibernateUtils.prepareOrder(dataPackage, "createTime", "desc"),
				criterionList);
		List<ResourcePoolRole> list = (List<ResourcePoolRole>) dataPackage.getDatas();
		List<ResourcePoolRoleVo> voList = HibernateUtils.voListMapping(list, ResourcePoolRoleVo.class);
		dataPackage.setDatas(voList);
		return dataPackage;
	}

	@Override
	public void deleteResourcePoolRole(ResourcePoolRoleVo resourcePoolRoleVo) {
		// TODO Auto-generated method stub
		ResourcePoolRole resourcePoolRole = HibernateUtils.voObjectMapping(resourcePoolRoleVo, ResourcePoolRole.class);
		resourcePoolRoleDao.delete(resourcePoolRole);
	}

	@Override
	public void saveEditResourcePoolRole(ResourcePoolRoleVo resourcePoolRoleVo, String oldRoleId) {
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(oldRoleId) && !oldRoleId.equals(resourcePoolRoleVo.getRoleId())) {
            Map<String, Object> params = Maps.newHashMap();
            params.put("orgId", resourcePoolRoleVo.getOrganizationId());
            params.put("roleId", oldRoleId);
            String sql = "delete from resource_pool_role where ORGANIZATION_ID = :orgId and JOB_ID = :jobId and TYPE = :type ";
            resourcePoolRoleDao.excuteSql(sql, params);
            //resourcePoolJobDao.delete(delResourcePoolJob);
		}
		ResourcePoolRole resourcePoolRole = resourcePoolRoleDao.findById(resourcePoolRoleVo.getId());
		if(resourcePoolRole==null){
			resourcePoolRole = new ResourcePoolRole();
		}
		resourcePoolRole.setRoleId(resourcePoolRoleVo.getRoleId());
		resourcePoolRole.setOrganizationId(resourcePoolRoleVo.getOrganizationId());
		resourcePoolRole.setOneTimeResource(resourcePoolRoleVo.getOneTimeResource());
		if (StringUtil.isBlank(resourcePoolRole.getCreateTime())) {
			resourcePoolRole.setCreateTime(DateTools.getCurrentDateTime());
			resourcePoolRole.setCreateUserId(userService.getCurrentLoginUser().getUserId());
		}
		resourcePoolRole.setModifyTime(DateTools.getCurrentDateTime());
		resourcePoolRole.setModifyUserId(userService.getCurrentLoginUser().getUserId());
		try {
			resourcePoolRoleDao.save(resourcePoolRole);
		} catch (DataIntegrityViolationException e){
			throw new ApplicationException("不能添加重复角色！");
		}

	}
	
}
