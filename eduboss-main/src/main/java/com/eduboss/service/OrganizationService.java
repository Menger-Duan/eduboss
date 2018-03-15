package com.eduboss.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.eduboss.domainVo.OrganizationAppendDto;
import com.eduboss.domainVo.OrganizationSearchDto;
import com.eduboss.dto.MessagePushVo;
import org.springframework.stereotype.Service;

import com.eduboss.common.OrganizationType;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.LiveCapmusVo;
import com.eduboss.domainVo.OranizationRelationVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;


/**
 * 
 * @author xiaojinwang
 * @description  避免不同的service调用不同的dao 封装为service再相互调用
 * @date 2016-08-11
 *
 */

@Service
public interface OrganizationService {
	
	public Organization findById(String id);

	public Organization findBrenchByCampusId(String campusId);
	
	Map<String, Object> getOrganizationInfo(DataPackage dataPackage,String orgId);
	
	Map<String, Object> getOrgTeacherInfo(DataPackage dataPackage,String teacherId);
	
	/**
	 * 组织架构及其所属人员列表  当前人员只查老师
	 * @param orgId
	 * @return
	 */
	@Deprecated
	public Map<String, Object> getOrgsAndTeachers(String orgId);


	public Map<String, Object> getOrgsAndTeachersNew(String orgId);
	
	/**
	 * 根据OrgLevel和OrgType获取Organization
	 * @param orgLevel
	 * @param orgType
	 * @return
	 */
	public Organization fingOrgByOrgLevelAndOrgType(String orgLevel,String orgType);
	
	
	/**
	 * 根据父ID和类型获取组织架构
	 * @param orgId
	 * @param orgType
	 * @return
	 */
	public List<Organization> getOrganizationsByParentOrgIdAndType(String orgId, OrganizationType orgType);

	/**
	 * 获取Id的分公司
	 * @param id
	 * @return
	 */
	public Organization findBrenchById(String id);
	
	/**
	 * 获取所有上级组织架构 不包括自己
	 */
	public List<Organization> getAllParentOrganizationsByOrgLevel(String orgLevel);
	
	/**
	 * 获取所有下级组织架构 不包括自己
	 */
	public List<Organization> getAllChildOrganizationsByOrgLevel(String orgLevel);
	
	/**
	 * 或者子节点个数
	 */
	public Integer getChildNodeNumsByOrgId(String orgId,String[] orgIds);
	
	/**
	 * 获取所有集团和分公司
	 * @return
	 */
	List<Organization> getGroupAndAllBrench();
	

	/**
	 *
	 * @param userId
	 * @return
	 */
	public List<Organization> findAllBrenchByUserId(String userId);
	
	public void saveOrUpdateSchoolArea(Organization org);

	/**
	 * 组织机构 紧急状态切换列表
	 * @param branchId
	 * @param campusId
	 * @param stateOfEmergency
	 * @return
	 */
    List<Map<String,Object>> findOrganization(String branchId, String campusId, String stateOfEmergency);

	/**
	 * 获取所有分公司
	 * @return
	 */
	List<Map<String,Object>> getAllBranch();

	/**
	 * 获取指定分公司下一级的校区和合起来的部门
	 * @param organizationId
	 * @return
	 */
	List<Map<String,Object>> getDeptAndCampusByBranchId(String organizationId);

	/**
	 * 更新单个
	 * @param organizationId
	 */
    void updateOrgStatusById(String organizationId);

	/**
	 * 更新分公司下面的所有组织架构状态
     * @param branchId
     * @param stateOfEmergency
     */
	void updateBatchOrgStatusById(String branchId, String stateOfEmergency);
	/**
	 * 直播获取校区分公司详情
	 * @author: duanmenrun
	 * @Title: getCampusIdListDetail 
	 * @Description: TODO 
	 * @param vos
	 * @return
	 */
	public Response getCampusIdListDetail(LiveCapmusVo vos);
	/**
	 * 分公司、校区列表
	 * @author: duanmenrun
	 * @Title: getAllBranchCampus 
	 * @Description: TODO 
	 * @return 
	 */
	public List<OranizationRelationVo> getAllBranchCampus();


	/**
	 * 同步人事单个组织架构
	 * @param data
	 */
	void updateOrgnizationByHrms(String data);

    void pushAllOrganizationInfo(MessagePushVo vo);

    Response saveHrmsOrgToBoss(String id);

	/**
	 * 取消组织架构在BOSS可见
	 * @param id
	 * @return
	 */
	Response cacelHrmsOrgInBoss(String id);

	/**
	 * 查询组织架构树
	 * @param dto parentId  父类ID
	 * @param dto orgTypes  类型，多个用逗号隔开。
	 * @param dto type  类型，查BOSS还是人事
	 * @return
	 */
	Response getBossOrganizationList(OrganizationSearchDto dto);

	/**
	 * 根据ID获取人事组织架构信息。
	 * @param id
	 * @return
	 */
	Response getOrganizationById(String id);

	/**
	 * 获取关联信息
	 * @param id
	 * @return
	 */
	Response getOrganizationAppendInfoById(String id);

	/**
	 * 保存BOSS组织架构信息
	 * @param dto
	 * @return
	 */
	Response saveBossOrganizationInfo(OrganizationAppendDto dto);

	/**
	 * 获取当前登录用户组织架构树
	 * @param dto
	 * @return
	 */
	Response getBossSelectOrganizationList(OrganizationSearchDto dto);
}
