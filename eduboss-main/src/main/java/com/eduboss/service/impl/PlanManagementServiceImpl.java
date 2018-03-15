package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.DateVeidooType;
import com.eduboss.common.EnumHelper;
import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.PlanManagementDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.PlanManagement;
import com.eduboss.domain.User;
import com.eduboss.domainVo.PlanManagementVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.service.PlanManagementService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

@Service
public class PlanManagementServiceImpl implements PlanManagementService {
	
	@Autowired
	private PlanManagementDao planManagementDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private UserService userService;
	
	
	
	public List<Map> getOrganizationByCampusAbove(){
		List<Map> returnList=new ArrayList<Map>();
		String hql="from Organization where orgType in ('"+OrganizationType.BRENCH+"','"+OrganizationType.CAMPUS+"','"+OrganizationType.GROUNP+"')";
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
				returnList.add(returnMap);
		}
		
		return returnList;
	}

	@Override
	public DataPackage getPlanManagementList(PlanManagement planManagement, DataPackage dataPackage) {
		//如果有字段有值，用like进行条件查询
//		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(planManagement);
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if(StringUtils.isNotEmpty(planManagement.getGoalId())){
			criterionList.add(Expression.eq("goalId", planManagement.getGoalId()));
		}
		List<Order> scoreOrderList = new ArrayList<Order>();
		scoreOrderList.add(Order.desc("year.name"));
		scoreOrderList.add(Order.asc("planOrder"));
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			Order propertityOrder = "asc".equalsIgnoreCase(dataPackage.getSord()) ? Order
					.asc(dataPackage.getSidx()) : Order.desc(dataPackage.getSidx());
			scoreOrderList.add(propertityOrder);
		} 
		
		dataPackage = planManagementDao.findPageByCriteria(dataPackage, scoreOrderList, criterionList);
		List<PlanManagement> list=(List<PlanManagement>) dataPackage.getDatas();
		dataPackage.setDatas(HibernateUtils.voListMapping(list, PlanManagementVo.class));
		return dataPackage;
	}
	
	@Override
	public void deletePlanManagement(PlanManagement planManagement) {
		planManagementDao.delete(planManagement);
	}

	@Override
	public Response saveOrUpdatePlanManagement(PlanManagement planManagement) {
		Response res=checkPlanManagement(planManagement);
		if(res.getResultCode()==0){
			User user = userService.getCurrentLoginUser();
			if(planManagement.getId()==null){
				planManagement.setCreateTime(DateTools.getCurrentDateTime());
				planManagement.setCreateUserId(user.getUserId());
			}
			planManagement.setModifyTime(DateTools.getCurrentDateTime());
			planManagement.setModifyUserId(user.getUserId());
			if(DateVeidooType.MONTH.equals(planManagement.getTimeType()) && planManagement.getMonthId()!=null){
				List<NameValue> lists=EnumHelper.getEnumOptions("MonthType");
				Map<String, Integer> map=new LinkedHashMap<String, Integer>();
				for(int i=0;i<lists.size();i++){
					NameValue val=lists.get(i);
					map.put(val.getValue(), i+1);
				}
				Integer order=map.get(planManagement.getMonthId().toString());
				if(order!=null){
					planManagement.setPlanOrder(100+10*order);
				}
			}else{
				if(DateVeidooType.QUARTER.equals(planManagement.getTimeType()) && planManagement.getQuarterId()!=null){
					List<NameValue> lists=EnumHelper.getEnumOptions("QuarterType");
					Map<String, Integer> map=new LinkedHashMap<String, Integer>();
					for(int i=0;i<lists.size();i++){
						NameValue val=lists.get(i);
						map.put(val.getValue(), i+1);
					}
					Integer order=map.get(planManagement.getQuarterId().toString());
					if(order!=null){
						planManagement.setPlanOrder(10*order);
					}
				}
			}
			
			planManagementDao.save(planManagement);
		}	
		return res;
	}

	@Override
	public PlanManagementVo findPlanManagementById(String id) {
		PlanManagement planManagement= planManagementDao.findById(id);
		if(planManagement!=null)
			return HibernateUtils.voObjectMapping(planManagement, PlanManagementVo.class);
		return null;
	}
	
	private Response checkPlanManagement(PlanManagement planManagement){
		Response res =new Response();
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if(StringUtils.isNotEmpty(planManagement.getId())){
			criterionList.add(Expression.ne("id", planManagement.getId()));
		}
		if(planManagement.getTimeType()!=null){
			criterionList.add(Expression.eq("timeType", planManagement.getTimeType()));
		}
		if(planManagement.getYear()!=null && StringUtils.isNotEmpty(planManagement.getYear().getId())){
			criterionList.add(Expression.eq("year.id", planManagement.getYear().getId()));
		}
		if(planManagement.getMonthId()!=null){
			criterionList.add(Expression.eq("monthId", planManagement.getMonthId()));
		}
		if(planManagement.getQuarterId()!=null){
			criterionList.add(Expression.eq("quarterId", planManagement.getQuarterId()));
		}
		if(planManagement.getTargetType()!=null && StringUtils.isNotEmpty(planManagement.getTargetType().getId())){
			criterionList.add(Expression.eq("targetType.id", planManagement.getTargetType().getId()));
		}
		
		if(StringUtils.isNotEmpty(planManagement.getGoalId())){
			criterionList.add(Expression.eq("goalId", planManagement.getGoalId()));
		}
		List list=planManagementDao.findAllByCriteria(criterionList);
		if(list!=null && list.size()>0){
			res.setResultCode(1);
			res.setResultMessage("当前类型的数据已存在");
		}
		return res;
	}
}
