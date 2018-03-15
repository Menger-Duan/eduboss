package com.eduboss.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.eduboss.domain.Organization;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.ResourcePoolRole;
import com.eduboss.domain.UserOrganizationRole;
import com.eduboss.domainVo.ResourcePoolJobVo;
import com.eduboss.domainVo.ResourcePoolRoleVo;
import com.eduboss.domainVo.ResourcePoolUserVo;
import com.eduboss.domainVo.ResourcePoolVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

public interface ResourcePoolService {

	public ResourcePool findResourcePoolById(String organizationId);
	
	/**
	 * 新组织架构查询资源池
	 * @author: duanmenrun
	 * @Title: findResourcePoolByIdNew 
	 * @Description: TODO 
	 * @param organizationId
	 * @return
	 */
	public Response findResourcePoolByIdNew(String organizationId);
	
	public void deleteResourcePool(ResourcePool resourcePool);
	
	public void saveEditResourcePool(ResourcePool resourcePool);
	
	public DataPackage getResourcePoolJobList(DataPackage dataPackage, ResourcePoolJobVo resourcePoolJobVo);
	
	public void deleteResourcePoolJob(ResourcePoolJobVo resourcePoolJobVo);
	
	public void saveEditResourcePoolJob(ResourcePoolJobVo resourcePoolJobVo, String oldUserJobId);
	
	public List<Organization> getOrganizationListForResPool(String organizationId);
	
	public Response getResourcePoolVolume(int customerNums,String resourceId);	
	
	public Response updateCustomerDeleverTarget(String customerIds,String resourceId);
	 	
	public List<Map> getOrganizationByDepartmentAbove();
	
	public List<ResourcePoolUserVo> getResourcePoolAndUser(String organizationId);
	
	public void resourceCallback() throws SQLException;
	
	public List<ResourcePoolUserVo> getValidateResourcePool();
	
	//重写getResourcePoolAndUser方法 带统计 展开至校区
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithVolumeCampus(String organizationId);
	
	//重写getResourcePoolAndUser方法 不带统计 展开至校区
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithoutVolumeCampus(String organizationId);
	
	//重写getResourcePoolAndUser方法 带统计 展开至校区
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithVolumeBranch(String organizationId);
	
	//重写getResourcePoolAndUser方法 不带统计 展开至校区
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithoutVolumeBranch(String organizationId);
	
	 //资源配置的 资源池 分配树
	public List<ResourcePoolUserVo> loadResourcePoolAndUserWithVolume(String organizationId);
	
	//检测咨询的资源量是否足够
	public Response checkPersonPoolVolume(String userId);
	
	public Response getPersonPoolVolume(int customerNums,String userId);
	
	//根据userId获取该用户的当前已分配客户数量和最大可获取数据
	public Map<String, Integer> getResourcePoolVolumeByUserId(String userId);
	
	public Map<String, Integer> getResourcePoolVolumeByOrgId(String orgId);
	
	public List<Organization> getOrgByPoolName(String key);
	
	//用于根据关键字搜索 
	public List<ResourcePoolUserVo> loadResourcePoolAndUserBySearch(String organizationId);
	
	//新的规则 20170306 根据关键字搜索
	public List<ResourcePoolUserVo> loadSearchResourcePoolAndUser(String key);
	
	
	//获取所属分公司的资源池
	public ResourcePool getBelongBranchResourcePool(String userId,String type);
	
	//客户管理 客户资源导入 分配树
	public List<ResourcePoolUserVo> getResourcePoolDataImport(String organizationId);
	
	/**
	 * 资源池可见角色
	 * @author: duanmenrun
	 * @Title: getResourcePoolRoleList 
	 * @Description: TODO 
	 * @param resourcePoolroleVo
	 * @return
	 */
	public List<ResourcePoolRole> getResourcePoolRoleList(ResourcePoolRoleVo resourcePoolroleVo);
	/**
	 * 保存资源池修改
	 * @author: duanmenrun
	 * @Title: saveResourcePoolNew 
	 * @Description: TODO 
	 * @param resourcePoolVo
	 * @return
	 */
	public Response saveResourcePoolNew(ResourcePoolVo resourcePoolVo);
	
	public DataPackage getResourcePoolRoleList(DataPackage dataPackage, ResourcePoolRoleVo resourcePoolRoleVo);

	public void deleteResourcePoolRole(ResourcePoolRoleVo resourcePoolRoleVo);

	public void saveEditResourcePoolRole(ResourcePoolRoleVo resourcePoolRoleVo, String oldRoleId);
}
