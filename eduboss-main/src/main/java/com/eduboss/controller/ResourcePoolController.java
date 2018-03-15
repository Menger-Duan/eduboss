package com.eduboss.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.domain.Organization;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.ResourcePoolJobVo;
import com.eduboss.domainVo.ResourcePoolRoleVo;
import com.eduboss.domainVo.ResourcePoolUserVo;
import com.eduboss.domainVo.ResourcePoolVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.service.CustomerService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/ResourcePoolController")
public class ResourcePoolController {
	
	@Autowired
	private ResourcePoolService resourcePoolService;
	
	@Autowired
	private CustomerService customerService;
	

	/**
	 * 查找1条
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findResourcePoolById", method =  RequestMethod.GET)
	@ResponseBody	
	public ResourcePool findResourcePoolById(@RequestParam String organizationId){
		return resourcePoolService.findResourcePoolById(organizationId);
	}
	
	/**
	 * 新组织架构资源池查询
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findResourcePoolByIdNew", method =  RequestMethod.GET)
	@ResponseBody	
	public Response findResourcePoolByIdNew(@RequestParam String organizationId){
		return resourcePoolService.findResourcePoolByIdNew(organizationId);
	}
	
	/**
     * 保存新组织架构资源池
     * @param record
     * @return
     */
    @RequestMapping(value ="/saveResourcePoolNew", method =  RequestMethod.POST)
    @ResponseBody
    public Response saveResourcePoolNew(@RequestBody ResourcePoolVo resourcePoolVo) {
    	Response res  = new Response();
    	res = resourcePoolService.saveResourcePoolNew(resourcePoolVo);
    	return res;
    }
	
	/**
     * 保存或修改个人工作日程
     * @param record
     * @return
     */
    @RequestMapping(value ="/editResourcePool")
    @ResponseBody
    public Response editResourcePool(@ModelAttribute GridRequest gridRequest, @ModelAttribute ResourcePool resourcePool) {
    	if ("del".equalsIgnoreCase(gridRequest.getOper())) {
    		resourcePoolService.deleteResourcePool(resourcePool);
    	} else {
    		resourcePoolService.saveEditResourcePool(resourcePool);
    	}
    	return new Response();
    }
    
    /* 查询
	 * @param request
	 * @param gridRequest
	 * @param classroomManageVo
	 * @return
	 */
	@RequestMapping(value = "/getResourcePoolJobList")
	@ResponseBody
	public DataPackageForJqGrid getResourcePoolJobList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest, ResourcePoolJobVo resourcePoolJobVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = resourcePoolService.getResourcePoolJobList(dataPackage, resourcePoolJobVo);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
    
    /**
     * 保存或修改个人工作日程
     * @param record
     * @return
     */
    @RequestMapping(value = "/editResourcePoolJob")
    @ResponseBody
    public Response editResourcePoolJob(@ModelAttribute GridRequest gridRequest, @ModelAttribute ResourcePoolJobVo resourcePoolJobVo, String oldUserJobId) {
    	if ("del".equalsIgnoreCase(gridRequest.getOper())) {
    		resourcePoolService.deleteResourcePoolJob(resourcePoolJobVo);
    	} else {
    		resourcePoolService.saveEditResourcePoolJob(resourcePoolJobVo, oldUserJobId);
    	}
    	return new Response();
    }
    
    @RequestMapping(value = "/getOrganizationListForResPool")
    @ResponseBody
    public SelectOptionResponse getOrganizationListForResPool(@RequestParam String organizationId) {
    	SelectOptionResponse selectOptionResponse = null;
    	List<Organization> list =  resourcePoolService.getOrganizationListForResPool(organizationId);
    	List<NameValue> nvs = new ArrayList<NameValue>();
//		if(list!=null){
//			nvs.addAll(list);
//		}
		if(list!=null){
			for (Organization org : list) {
				nvs.add(SelectOptionResponse.buildNameValue(org.getResourcePoolName(), org.getId()));
			}
		}
		selectOptionResponse = new SelectOptionResponse(nvs); 
		return selectOptionResponse;
    }
    
    /**
     * 根据登陆用户权限查询部门以上的组织架构
     * @return
     */
    @ResponseBody
	@RequestMapping(value="/getOrganizationByDepartmentAbove")
	public List<Map> getOrganizationByDepartmentAbove(){
		return resourcePoolService.getOrganizationByDepartmentAbove();
	}
    
    
    /**
	 * 根据orgLevel 获取所有可分配的客户资源池和人(为空  默认当前登陆用户的)
	 * @return
	 */
	@RequestMapping(value = "/getResourcePoolAndUser")
	@ResponseBody
	public List<ResourcePoolUserVo> getResourcePoolAndUser(String id){
		
//		return resourcePoolService.getResourcePoolAndUser(id);
		return resourcePoolService.loadResourcePoolAndUserWithoutVolumeCampus(id);		
//		return resourcePoolService.loadResourcePoolAndUserWithoutVolumeBranch(id);
//		return resourcePoolService.loadResourcePoolAndUserWithVolumeBranch(id);
	}
    /**
	 * 根据orgLevel 获取所有可分配的客户资源池和人(为空  默认当前登陆用户的)
	 * @return
	 */
	@RequestMapping(value = "/getResourcePoolWithVolume")
	@ResponseBody
	public List<ResourcePoolUserVo> getResourcePoolAndUserWithVolume(String id){
				//带上资源量
				//添加数字：当前资源数和最大资源数
//		return resourcePoolService.loadResourcePoolAndUserWithVolumeCampus(id);//目前的
		return resourcePoolService.loadResourcePoolAndUserWithVolume(id);//调整后的 xiaojinwang 2017-03-03
		//下面无用的
//		return resourcePoolService.loadResourcePoolAndUserWithoutVolumeBranch(id);
//		return resourcePoolService.loadResourcePoolAndUserWithVolumeBranch(id);
	}

	@RequestMapping(value = "/getResourcePoolAndUserBySearch")
	@ResponseBody
	public List<ResourcePoolUserVo> getResourcePoolAndUserBySearch(String key){
		
//		if(StringUtil.isNotBlank(key)){
//		//根据key来获取organization 
//		List<Organization> list = resourcePoolService.getOrgByPoolName(key);
//	    if(list!=null && list.size()>0){
//			List<ResourcePoolUserVo> result = new ArrayList<>();
//			for(Organization organization:list){
//				result.addAll(resourcePoolService.loadResourcePoolAndUserBySearch(organization.getId()));
//			//	替换 为新的逻辑
//			}
//			System.out.println(result.size());
//			for (int i = 0; i < result.size() - 1; i++) {
//				for (int j = result.size() - 1; j > i; j--) {
//					if (result.get(j).getId().equals(result.get(i).getId())) {
//						result.remove(j);
//					}
//				}
//			}
//			return result;
//		}else{
//			return new ArrayList<ResourcePoolUserVo>();
//		}		
//	   }else{
//		   return resourcePoolService.loadResourcePoolAndUserWithVolumeCampus(key);	
//	   }
		
		//新规则 20170306 xiaojinwang
		return resourcePoolService.loadSearchResourcePoolAndUser(key) ;
	}
    
    /**
     * 查询资源池容量是否足够
     */
    
    @RequestMapping(value="/getResourcePoolVolume")
    @ResponseBody
    public Response getResourcePoolVolume(@RequestParam int customerNums,@RequestParam String resourceId){
    	//传入，资源数量和分配到的资源池id
    	return resourcePoolService.getResourcePoolVolume(customerNums, resourceId);
    	
    }
    
    /**
     * 调配资源
     */
    @RequestMapping(value="/updateCustomerDeleverTarget")
    @ResponseBody
    public Response updateCustomerDeleverTarget(String customerIds,@RequestParam String targetId, String sourceId, CustomerDeliverType deliverType){
    	if (StringUtil.isBlank(customerIds) && StringUtil.isNotBlank(sourceId)) {
    		DataPackage dp = new DataPackage(0, 999);
    		CustomerVo customerVo = new CustomerVo();
    		customerVo.setDeliverTarget(sourceId);
    		customerVo.setDeliverType(deliverType);
//    		customerVo.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
    		dp = customerService.getCustomers(customerVo, dp, false);
    		List<CustomerVo> list = (List<CustomerVo>) dp.getDatas();
    		customerIds = "";
    		if (list != null && list.size() > 0) {
    			for (CustomerVo vo : list) {
    				customerIds += vo.getCusId() + ","; 
    			}
    			customerIds.substring(0, customerIds.length() - 1);
    		}
    	}
    	return resourcePoolService.updateCustomerDeleverTarget(customerIds, targetId);
    		
    }
    
    /**
	 * 根据orgLevel 获取所有登陆用户组织架构同一条线的资源池
	 * @param orgLevel
	 * @return
	 */
	@RequestMapping(value = "/getValidateResourcePool", method =  RequestMethod.GET)
	@ResponseBody
	public List<ResourcePoolUserVo> getValidateResourcePool(){
		return resourcePoolService.getValidateResourcePool();
	}
	
	/**
	 * 检测咨询师的资源量是否足够
	 */
    @RequestMapping(value="/checkPersonPoolVolume",method = RequestMethod.GET)
    @ResponseBody
    public Response checkPersonPoolVolume(String userId){	
    	return resourcePoolService.checkPersonPoolVolume(userId);
    }	
    
    /**
	 * 根据orgLevel 获取所有可分配的客户资源池和人(为空  默认当前登陆用户的)  客户管理 客户资源导入 分配资源树
	 * @return
	 */
	@RequestMapping(value = "/getResourcePoolDataImport")
	@ResponseBody
	public List<ResourcePoolUserVo> getResourcePoolDataImport(String id){
				//带上资源量
				//添加数字：当前资源数和最大资源数
		return resourcePoolService.getResourcePoolDataImport(id);	
		//return resourcePoolService.loadResourcePoolAndUserWithVolume(id);
	}
	/**
	 * 资源池可见角色
	 * @author: duanmenrun
	 * @Title: getResourcePoolRoleList 
	 * @Description: TODO 
	 * @param resourcePoolRoleVo
	 * @return
	 */
	@RequestMapping(value = "/getResourcePoolRoleList")
	@ResponseBody
	public Response getResourcePoolRoleList(@RequestParam String organizationId){
		Response res  = new Response();
		DataPackage dataPackage = new DataPackage(0, 999);
		ResourcePoolRoleVo resourcePoolRoleVo = new ResourcePoolRoleVo();
		resourcePoolRoleVo.setOrganizationId(organizationId);
		dataPackage = resourcePoolService.getResourcePoolRoleList(dataPackage, resourcePoolRoleVo);
		res.setData(dataPackage.getDatas());
		return res ;
	}
	
	/**
	 * 修改资源池可见角色
	 * @author: duanmenrun
	 * @Title: editResourcePoolRole 
	 * @Description: TODO 
	 * @param resourcePoolJobVo
	 * @param oldUserJobId
	 * @return
	 */
    @RequestMapping(value = "/editResourcePoolRole")
    @ResponseBody
    public Response editResourcePoolRole(@ModelAttribute ResourcePoolRoleVo resourcePoolRoleVo, String oldRoleId , String type) {
    	if ("del".equalsIgnoreCase(type)) {
    		resourcePoolService.deleteResourcePoolRole(resourcePoolRoleVo);
    	} else {
    		resourcePoolService.saveEditResourcePoolRole(resourcePoolRoleVo, oldRoleId);
    	}
    	return new Response();
    }
}
