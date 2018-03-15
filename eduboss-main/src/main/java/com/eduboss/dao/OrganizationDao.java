package com.eduboss.dao;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.ResourcePoolJobType;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.OrganizationSearchDto;
import com.eduboss.domainVo.ResourcePoolUserVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface OrganizationDao extends GenericDAO<Organization, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	public String queryOrganizationLevel(String curOrganizationId);
	
	/**
	 * 根据获取下一个组织架构id，规则：
	 *      集团：0001         父：-1
	 * 东莞分公司：00010001     父：0001
	 *  新华校区：000100010001 父：00010001
	 */
	public String getNextOrgLevel(String parentId, String parentOrgLevel);
	
	/**
	 * 根据父节点和类型获取组织架构
	 */
	public List<Organization> getOrganizationsByOrgLevelAndType(String OrgLevel, OrganizationType orgType);
	
	/**
	 * 根据父ID和类型获取组织架构
	 * @param orgId
	 * @param orgType
	 * @return
	 */
	public List<Organization> getOrganizationsByParentOrgIdAndType(String orgId, OrganizationType orgType);
	
	/**
	 * 
	 * @param orgLevel
	 * @param orgType
	 * @param isMainOrganization 没有找到类型时是否返回一个（类型：校区，分公司，集团）的值
	 * @return
	 */
	public Organization getBelongOrgazitionByOrgType(String orgLevel, OrganizationType orgType,boolean isMainOrganization);
	
	/**
	 * 获取用户所在归属校区
	 */
	public Organization getBelongCampus(String orgLevel);
	
	/**
	 * 获取用户所在归属分公司
	 */
	public Organization getBelongBrench(String orgLevel);
	
	/**
	 * 获取所在归属分公司
	 * @param orgLevel
	 * @return
	 */
	public Organization getBelongGrounp(String orgLevel);

	public List<Organization> getOrganizationBoy(Organization org,Organization brench);
	
	public List<Organization> getOrganizationBoy(List<Organization> orgs);
	
	public String getOrgNameById(String orgId);

	public List<Organization> getAllGroupAndBrench();
	/**
	 * 得到所属的所有组织架构
	 * @param orgs
	 * @return
	 */
	public List<Organization> getBelongOrg(List<Organization> orgs);
	
	public String getOrganizationIdByName(String name);
	
	
	public Organization getOrganizationFirstFloorMax();
	
	/**
	 * 根据类型查询组织架构数量 （organizationId有值除外）
	 * @param organizationType
	 * @param organizationId
	 * @return
	 */
	public int getOrganizationCountByOrganizationType(OrganizationType organizationType,String organizationId);
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByCustomerPool(String orgLevel);
	
	/**
	 * 根据orgLevel 获取所有下级客户资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByCustomerPoolLower(String orgLevel);
	
	/**
	 * 根据orgLevel 获取校区级别以上的上级资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByResourcePool(String orgLevel);
	
	/**
	 * 根据orgLevel 获取校区级别以上的下级资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationCampusByResourcePoolLower(String orgLevel);
	
	/**
	 * 根据orgLevel 获取所有的下级资源池
	 * @param orgLevel
	 * @return
	 */
	public List<Organization> getOrganizationByResourcePoolLower(String orgLevel);
	
	/**
	 * 获取包含当前组织在内的所有上级组织
	 */
	public List<Organization> getIncludeAllParentOrgByOrgId(String organizationId);
	
	/**
	 * 根据组织架构级别,职位ID和资源池类型查找资源池
	 * @param orgLevel
	 * @param jobId
	 * @return
	 */
	public List<ResourcePoolUserVo> getValidateResourcePool(String orgLevel, String jobId, ResourcePoolJobType type);
	
	public List<ResourcePoolUserVo> getDisValidateResourcePool(String orgLevel, String jobId, ResourcePoolJobType type);
	
	/**
     * 根据学生查询所有关联校区
     * @param studentId
     * @return
     */
    List<Organization> getAllStudentCampus(String studentId);


	/**
	 * 省份和城市
	 * @param organization
	 * @return
	 */
	public Organization setProvinceAndCityToOrganization(Organization organization);
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<Organization> findOrganizationByUserId(String userId);

	@Deprecated
	public List<Organization> findOrganizationByUserIdOld(String userId);
	
	/**
	 * 获取所有集团和分公司
	 * @return
	 */
	List<Organization> getGroupAndAllBrench();

	/**
	 * 根据人事组织架构ID获取BOSS组织架构
	 * @param id
	 * @return
	 */
	Organization findOrganizationByHrmsId(String id);

	List getOrganizationByCondition(OrganizationSearchDto dto);

    List<Organization>
	findOrganizationByOrgLevel(String orgLevel);

	/**
	 * 根据父节点获取组织架构列表
	 * @param parentId
	 * @return
	 */
	List<Organization> getOrgByParentId(String parentId);

	List<Organization> getUserOrganizationList(String userId);

	/**
	 * @param dto
	 * @return
	 */
	List<Map> getBossSelectOrganizationList(OrganizationSearchDto dto);
}
