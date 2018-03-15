package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.dto.*;
import org.springframework.stereotype.Service;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.RoleResourceType;
import com.eduboss.common.UserWorkType;
import com.eduboss.common.ValidStatus;
import com.eduboss.domain.MobileOrganization;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Resource;
import com.eduboss.domain.Role;
import com.eduboss.domain.RoleResource;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.domain.UserOrg;
import com.eduboss.domain.UserOrganization;
import com.eduboss.domain.UserRole;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SparkUserVo;
import com.eduboss.dto.UserMobileVo;
import com.eduboss.exception.ApplicationException;

@Service
public interface UserService {

	/**
	 * 判断当前用户是否是指定的角色（职位）
	 * 
	 * @param roleCode
	 * @return
	 */
	public boolean isCurrentUserRoleCode(RoleCode roleCode);
	
	/**
	 * 判断当前用户是否是指定的角色（职位）
	 * 
	 * @param roleId
	 * @return
	 */
	public boolean isCurrentUserRoleId(String roleId);

	/**
	 * 判断用户是否是指定的角色（职位）
	 * 
	 * @param userId
	 * @param roleCode
	 * @return
	 */
	public boolean isUserRoleCode(String userId, RoleCode roleCode);

	/**
	 * 获取当前登录用户所有角色
	 * 
	 * @return
	 */
	public List<Role> getCurrentLoginUserRoles();

	// 用户维护
	/**
	 * 根据用户ID返回用户详情
	 * 包括用户的角色编码（roleCode）、角色ID、角色名称、当前组织架构ID、当前组织架构名称、组织架构层次ID（例如：1111
	 * -22222-33333）、组织架构层次名称（例如：集团-东莞分公司-新华校区） 数据库没有的字段需要从关联表中取，并在damain表中添加@Transient字段存放
	 */
	public User getUserById(String userId) throws ApplicationException;

	public User findUserById(String userId) throws ApplicationException;

	/**
	 * 获取用户名、组织架构等信息
	 * 
	 * @param userId
	 * @return
	 * @throws ApplicationException
	 */
	public User getUserInfoById(String userId) throws ApplicationException;

	/**
	 * 根据用户名返回用户详情
	 * 包括用户的角色编码（roleCode）、角色ID、角色名称、当前组织架构ID、当前组织架构名称、组织架构层次ID（例如：1111
	 * -22222-33333）、组织架构层次名称（例如：集团-东莞分公司-新华校区） 数据库没有的字段需要从关联表中取，并在damain表中添加@Transient字段存放
	 */
	public User getUserByUserName(String userName) throws ApplicationException;


	public User getUserByEmployeeNo(String employeeNo) throws ApplicationException;

	public User getUserByName(String userName) throws ApplicationException;

	/**
	 * 把用户的密码重新设置为123456的md5加密。
	 */
	public void resetPassword(String userId);

	/**
	 * 禁用用户（把用户的enable_flag改成1）
	 */
	public void setUserEnable(String userId, boolean isEnable);

	/**
	 * 获取用户部门职位
	 */
	public List<UserDeptJob> getUserDeptJobList(String userId);

	/**
	 * 根据用户名、角色ID和组织架构ID返回某个角色员工列表
	 * isIncludeChildLevel为true时，需要返回组织架构id及下属组织架构中该角色的值 员工角色对返回的列表的影响:
	 * 前台-只能查看本校区的咨询师 市场专员-只能查看分公司的咨询师
	 */
	public List<User> getStaffByRoldIdAndOrgId(String roldId, String orgId,
			boolean isIncludeChildLevel, int limit)
			throws ApplicationException;

	/**
	 * 根据类型和组织架构查询用户
	 *
	 * @param workType
	 *            用户类型
	 * @param orgId
	 *            组织架构id
	 * @param orgLevel
	 *            组织架构code
	 * @param isIncludeChildLevel
	 *            是否查询 组织架构下属组织架构的用户
	 * @return 用户列表
	 */
	public List<UserVo> getUserByWorkTypeAndOrgId(UserWorkType workType,
			String orgId, boolean isIncludeChildLevel);

	public List<User> getStaffByRoldCodeAndOrgId(String roldCode, String orgId,
			boolean isIncludeChildLevel, int limit)
			throws ApplicationException;

	/**
	 * 根据条件返回员工列表 需要根据当前用户附加权限条件
	 */
	public DataPackage getUserList(User user, DataPackage dp)
			throws ApplicationException;

	/**
	 * 新增或更改用户，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要先检查是否有修改或新增用户的权限
	 */
	public void saveOrUpdateUser(User user) throws ApplicationException;
	
	/**
	 * 新增或更改用户，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要查验是否有删除员工的权限
	 */
	public void deleteUsers(String userIds) throws ApplicationException;

	/**
	 * 创建用户企业邮箱
	 */
	public void createUserMailAddr(String userId, String mailAddr);
	
	/**
	 * 根据邮箱地址查找用户
	 * @param mailAddr
	 */
	public User findUserByMailAddr(String mailAddr);

	/**
	 * 用户企业邮箱使用状态（0启用，1禁用）
	 */
	public void setUserMailStatus(String userId, Integer status);

	// 角色维护
	/**
	 * 根据查询条件返回角色列表 需要根据当前用户附加权限条件
	 */
	public DataPackage getRoleList(Role role, DataPackage dp)
			throws ApplicationException;

	/**
	 * 根据查询条件返回角色列表 需要根据当前用户附加权限条件
	 */
	public DataPackage getRoleListForSelection(Role role, DataPackage dp);

	/**
	 * 新增或更改角色，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要先检查是否有修改或新增角色的权限
	 */
	public void saveOrUpdateRole(Role role) throws ApplicationException;

	/**
	 * 新增或更改角色，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要查验是否有删除角色的权限
	 */
	public void deleteRoles(String roleIds) throws ApplicationException;

	/**
	 * 根据角色ID获取角色
	 */
	public Role getRoleById(String roleId) throws ApplicationException;

	/**
	 * 根据用户id获取角色列表
	 */
	public List<Role> getRoleByUserId(String userId);
	
	/**
	 * 根据用户id获取角色列表
	 */
	@Deprecated
	public List<Role> loadRoleByUserId(String userId);

	/**
	 * 根据角色TAG获取角色
	 */
	public Role getRoleByTag(RoleCode oleTag);

	// 组织架构维护
	/**
	 * 根据查询条件返回架构维护列表 需要根据当前用户附加权限条件
	 */
	public DataPackage getOrganizationList(Organization org, DataPackage dp)
			throws ApplicationException;

	/**
	 * 新增或更改架构维护，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要先检查是否有修改或新增架构维护的权限
	 */
	public void saveOrUpdateOrganization(Organization org)
			throws ApplicationException;

	/**
	 * 新增或更改架构维护，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要查验是否有删除架构维护的权限
	 */
	public void deleteOrganizations(String organizationIds)
			throws ApplicationException;

	/**
	 * 根据组织架构获取ID
	 */
	public Organization getOrganizationById(String organizationId)
			throws ApplicationException;

	/**
	 * 根据组织架构获取ID
	 */
	public Organization getOrganizationAndRegionById(String organizationId)
			throws ApplicationException;

	/**
	 * 根据学校Id获取省市
	 * @param schoolId
	 * @return
	 */
	public OrganizationVo findOrganizationBySchoolId(String schoolId);
	// 权限资源维护
	/**
	 * 根据查询条件返回权限资源列表 需要根据当前用户附加权限条件
	 */
	public DataPackage getResourceList(Resource rsc, DataPackage dp,
			boolean includeNonRes) throws ApplicationException;

	/**
	 * 根据查询条件返回权限资源列表 需要根据当前用户附加权限条件
	 */
	public DataPackage getResourceList(Resource rsc, DataPackage dp)
			throws ApplicationException;

	/**
	 * 根据用户ID获取菜单权限
	 */
	public List<Resource> getUserMenus();

	/**
	 * 根据用户ID获取button权限
	 */
	public List<Resource> getUserButtons();

	/**
	 * 查询所有只需要登录就可以访问的资源
	 */
	public List<Resource> getAllAnonymouslyResourceList();

	/**
	 * 新增或更改权限资源，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要先检查是否有修改或新增权限资源的权限
	 */
	public void saveOrUpdateResource(Resource rsc) throws ApplicationException;

	/**
	 * 新增或更改权限资源，如果有Id信息，则为更改，否则为新增 需要根据当前用户附加权限条件，需要查验是否有删除权限资源的权限
	 */
	public void deleteResources(String resourceIds) throws ApplicationException;

	/**
	 * 根据id获取资源
	 */
	public Resource getResourceById(String id) throws ApplicationException;

	// 权限管理维护
	/**
	 * 保存角色与权限资源关系
	 */
	public void saveRoleResource(List<RoleResource> roleResourceList)
			throws ApplicationException;

	/**
	 * 删除角色与权限资源关系
	 */
	public void deleteRoleResource(List<RoleResource> roleResourceList)
			throws ApplicationException;

	/**
	 * 保存用户与组织架构关系
	 */
	public void saveUserOrganization(List<UserOrganization> userOrganizationList)
			throws ApplicationException;

	/**
	 * 删除用户与组织架构关系
	 */
	public void deleteUserOrganization(
			List<UserOrganization> userOrganizationList)
			throws ApplicationException;

	/**
	 * 保存用户与角色关系
	 */
	public void saveUserRole(List<UserRole> userRoleList)
			throws ApplicationException;

	/**
	 * 删除用户与角色关系
	 */
	public void deleteUserRole(List<UserRole> userRoleList)
			throws ApplicationException;

	/**
	 * 根据角色ID获取资源列表
	 */
	public List<Resource> getResourcesByRoleId(String roleId);

	/**
	 * 根据用户ID获取资源列表
	 */
	public List<Resource> getResourcesByUserId(String userId, String type);

	public List<Resource> getResourcesByRoleIdArray(String[] roleIds);

	public List<Organization> findOrganizationByUserId(String userId);

	/**
	 * 获取当前登录用户
	 */
	public User getCurrentLoginUser();

	/**
	 * 获取当前登录用户Id
	 */
	public String getCurrentLoginUserId();

	/**
	 * 获取当前登录用户归属主组织架构
	 */
	public Organization getCurrentLoginUserOrganization();

	/**
	 * 获取当前登录用户组织架构ID
	 */
	public String getCurrentLoginUserOrgId(String orgLevel);

	/**
	 * 获取当前登录用户归属校区
	 */
	public Organization getBelongCampus();

	/**
	 * 获取用户归属校区
	 */
	public Organization getBelongCampusByUserId(String userId);

	/**
	 * 获取当前登录用户归属分公司
	 */
	public Organization getBelongBranch();

	/**
	 * 获取用户归属分公司
	 */
	public Organization getBelongBranchByUserId(String userId);

	/**
	 * 获取用户归属集团
	 * 
	 * @return
	 */
	public Organization getBelongGroup();

	public Organization getBelongGroupUserId(String userId);

	/**
	 * 获取校区归属分公司
	 */
	public Organization getBelongBranchByCampusId(String orgId);

	/**
	 * 根据组织架构id查询所属校区
	 * 
	 * @param orgId
	 * @return
	 */
	public Organization getBelongCampusByOrgId(String orgId);

	/**
	 * 根据组织架构id查询所属分公司
	 * 
	 * @param orgId
	 * @return
	 */
	public Organization getBelongBranchByOrgId(String orgId);

	/**
	 * 根据组织架构id查询所属集团
	 * 
	 * @param orgId
	 * @return
	 */
	public Organization getBelongGrounpByOrgId(String orgId);

	public void saveUserPersonal(User user);

	public List<Organization> getOrganizationBoy(Organization org);

	/**
	 * 如果checkAllPermit为true，则全部机构获取
	 * 
	 * @param org
	 * @param checkAllPermit
	 * @return
	 */
	public List<Organization> getOrganizationBoy(Organization org,
			String checkAllPermit);

	/**
	 * 得到所属的组织架构列表
	 * 
	 * @return
	 */
	public List<Organization> getBelongOrg();

	/**
	 * 根据名字、角色、组织架构查找员工列表
	 * 
	 * @param name
	 * @param roleCode
	 * @param orgId
	 * @param isIncludeChildLevel
	 * @param dp
	 * @return
	 */
	public List<User> getStaffByNameAndRoleCodeAndOrgId(String name,
			String roleCode, String orgId, boolean isIncludeChildLevel,
			int limit);

	public String getUniqueOrgSign(String newOrgSign);

	public void saveUser(User user);

	/**
	 * 
	 * @param term
	 * @param roleCode
	 * @return
	 */
	public List<UserVo> findUserByRoleForAutoCompelete(String term,
			RoleCode roleCode);

	/**
	 * 查找用户列表（自动搜索）
	 * 
	 * @param input
	 * @param parentOrgId
	 *            父组织
	 * @param roleCode
	 *            角色
	 * @return
	 */
	public List<User> getUserAutoComplate(String input, String roleCode,
			String parentOrgId);

	public boolean isUserByAccount(String userId, String account);

	public Response updateUserPassword(String account, String oldPassWord,
			String newPassWord);

	public List<Role> getRoleByRoleCode(RoleCode roleCode);

	/**
	 * 查询拥有角色的用户
	 * 
	 * @param roleCode
	 *            多个用，号隔开
	 * @param orgLevel
	 *            组织架构层级
	 * @return
	 */
	public List<User> getUserByRoldCodes(String roleCodes);
	public List<User> getUserByRoldCodesNoOrgLevel(String roleCodes);

	/**
	 * 查询拥有角色的用户 -加姓名模糊查询
	 * 
	 * @param roleCode
	 *            多个用，号隔
	 * @param orgLevel
	 *            组织架构层级
	 * @return
	 */
	public List<User> getUserByRoldCodes(String roleCodes, String name);

	/**
	 * 根据roleCode和orgId获取对应角色用户
	 * 
	 * @param roleCodes
	 * @param organizationId
	 * @return
	 */
	public List<User> getUserByRoldCodesAndOrgId(String roleCodes,
			String organizationId);

	/**
	 * 获取同一组织架构下的职位是校区主任的用户
	 * 
	 * @param organizationId
	 * @return
	 */
	public List<User> getUserByRoldCodesAndOrgId2(String organizationId);

	/**
	 * 根据类型获取用户对应角色的资源
	 * 
	 * @param resType
	 * @return
	 */
	public List<Resource> getUserResourcesByResType(RoleResourceType resType);
	public List<Resource> getUserResourcesByResTypeNew(RoleResourceType resType);

	/**
	 * 删除虚拟老师 已排课的老师不允许删除
	 * 
	 * @param user
	 */
	public Response deleteDummyUser(User user);

	public List<Organization> getOrganizationTree(String organizationTypes);

	public List<AutoCompleteOptionVo> getUserForPerformance(String term,
			String roleCode, String parentOrgId);

	public List<AutoCompleteOptionVo> getUserByOrgForPerformance(String term,
			String roleCode, String parentOrgId);

	public List<User> getUserByBlcampusAndRole(String blcampus, String roleIds);

	/**
	 * 根据校区和职位来查询用户
	 * 
	 * @param blcampus
	 * @param userJobIds
	 * @return
	 */
	public List<User> getUserByBlcampusAndUserJob(String blcampus,
			String userJobIds);

	public void updateUserArchivesPath(User user);

	/**
	 * 获取学生 的相关的 培训机构联系方式 (例如 : 老师, 教务 等)
	 * 
	 * @param studentId
	 * @return
	 */
	public List<UserForMobileVo> getMobileContactsForStudent(String studentId);

	/**
	 * 根据职位ID查询用户数据
	 * 
	 * @param dataPackage
	 * @param jobId
	 *            职位ID
	 * @return
	 */
	public DataPackage getUserListByJob(DataPackage dataPackage, String jobId);

	/**
	 * 修改用户的登录令牌
	 * 
	 * @param user
	 */
	public void updateUserToken(User user);

	/**
	 * 找到用户主职位部门
	 * 
	 * @param userId
	 * @return
	 */
	public Map getMainDeptAndJob(String userId);

	/**
	 * 根据令牌找到用户
	 * 
	 * @param token
	 * @return
	 */
	public User getUserByToken(String token);

	/**
	 * 根据 用户的 角色 和 Organization 来找相应的用户
	 * 
	 * @param role
	 * @param organization
	 * @return
	 */
	@Deprecated
	public List<User> getUserListForMobileByRoleAndOrg(List<Role> role,
			List<Organization> organization);

	/**
	 * 获取学生 的相关的 培训机构联系方式 (例如 : 老师, 教务 等)
	 * 
	 * @param studentId
	 * @return
	 */
	public List<MobileUserVo> getMobileContactsByStudent(String studentId);

	/**
	 * （开启/禁止用户列表）设置角色对应的邮件列表状态（0开启，1禁用）
	 */
	public void setRoleMailListStatus(String roleId, Short mailListStatus);

	/**
	 * 添加roleSign
	 */
	public void onlyAddRoleSign(String roleId, String roleSign);

	/**
	 * 启用APP聊天
	 * 
	 * @param userId
	 */
	public void regAppAccount(String userId);


    /**
     * 获取有企业邮箱的用户
     */
	public List<User> getUserListHaveMail();

	/**
	 * 根据职位ID找到用户列表
	 * 
	 * @param dataPackage
	 * @param jobId
	 * @return
	 */
	public List<UserMobileVo> findUsersByJobId(DataPackage dataPackage,
			String jobId);

	public List<MobileOrganization> getAllMobileDeptList();
	
	public List<MobileOrganizationVo> getAllMobileOrganizationList();
	
	public List<UserMobileVo> findUsersByDept(DataPackage dataPackage,
			String deptId, String deptLevel, String deptType, int level);
	
	public List<UserMobileVo> findUsersByDeptPC(DataPackage dataPackage,
			String deptId, String deptLevel, String deptType, int level);

	public List<UserMobileVo> findUsersInPC(DataPackage dataPackage);
	
	public List<UserMobileVo> findUsersInPC1(DataPackage dataPackage);
	
	public List<MobileOrganization> getAllMobileDeptAndUserList();

	public List<MobileOrganization> getAllMobileDeptAndUserList(String type,
			String servicePath);

	public List<MobileOrganization> getAllMobileDeptListByUserIds(String userIds);

	public List<MobileOrganization> getSubMobileDeptListByOrgId(
			String organizationId);

	public List<UserJob> getAllJobAndUserList();

	public List<UserJob> getAllJobAndUserList(String type, String servicePath);

	public List<UserMobileVo> findUsersByUserName(String userName);

	public List<UserMobileVo> findUsersByUserNameWithJiaXue(String userName);

	public void activeUserAppStatus();

	public UserMobileVo findUsersByCcpAccount(String ccpAccount);

	public List<User> getUserByBlcampusAndUserJobValidate(String blcampus,
			String userJobIds);

	/**
	 * 获取所有校区
	 * 
	 * @return
	 */
	List<Organization> getAllCampusForMap();

	// 获取登录人具体部门职位对应的组织架构
	public String getOrgByUserJob(String deptJobId, String orgType);

	public List<User> getUserAutoComplateByJobId(String jobId);

	public List<UserOrg> getAllUserInfo();

	public List<UserOrg> getBatchUserInfoByIds(String userIds);

	/**
	 * 获取统计归属校区
	 * @param userIdC
	 * @return
     */
	Organization getTongjiguishuxiaoqu(String userId);

	public Map<String, Object> getAllMobileDeptAndUserByParentId(
			DataPackage dp, String parentId, String deptLevel, int level);
	
	/**
	 * 获取某校区某职位的用户列表
	 * @param campusId
	 * @param userjobSign
	 * @return
	 */
	public List<User> getUserBycampusAndjobSign(String campusId, String userjobSign);

	public List<Map<String, String>> getUsersByJobCode(String jobCode,
			String orgType, String campusId);


	/** 
	 * 获取所有的组织架构列表
	* @return
	* @author  author :Yao 
	* @date  2016年7月30日 下午2:55:18 
	* @version 1.0 
	*/
	public List<OrganizationVo> getAllOrganizationVo();


	/**
	 * 获取所有的用户
	 * @return
	 * @author  author :Yan
	 * @date  2016年8月1日 下午2:55:18
	 * @version 1.0
	 */
	public List<NeedSyncUserVo> getAllUserForOAVo();
	/**
	 * 更新用户信息
	 * @param info
	 */
	@Deprecated
	public void updateUserBySpark(SparkUserVo vo);
	
	/**
	 * 查找待编制老师
	 * @param teacherVersionVo
	 * @return
	 */
	public DataPackage getPagePreTeacher(PreTeacherVersionVo preTeacherVersionVo, DataPackage dp);
	
	/**
	 * 更新老师的编制状态
	 * @param teacherSubjectStatus
	 */
	public void updateTeacherSubjectStatus(String userId, int teacherSubjectStatus);
	
	/**
	 * 根据用户ids查找老师名字
	 * @param userIds
	 * @return
	 */
	public String getUserNamesByUserIds(String[] userIds);
	
	/**
	 * 开放给教育平台接口 用户列表
	 * @param dataPackage 封装 pageNum pageSize
	 * @param teacherId
	 * @return
	 */
    Map<String, Object> getUserInfo(DataPackage dataPackage,String teacherId,String phone,String name,Integer type);
    
    /**
	 * 开放给教育平台接口 用户列表
	 * @param dataPackage 封装 pageNum pageSize
	 * @param teacherId
	 * @return
	 */
    Map<String, Object> getUserInfo(DataPackage dataPackage,String teacherId,String phone,String name,Integer type,String employeeNo);

    /**
	 * 根据账户查找用户
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);
	
		
    /**
     * 根据 userId获取用户的RoleSign 代码 比如咨询师 zxs
     * @author xiaojinwang
     * @param userId
     * @date 2016/09/08
     * @return
     */
	public String getUserRoleSign(String userId);
	/**
	 * 根据userId 获取客户的信息 只有User表的记录 不包含其他关联信息
	 * @param userId
	 * @return
	 */
	public User loadUserById(String userId);


	/**
	 * 根据user_job里面的roleSign 某组织架构（比如校区）下的具有roleSign的user的id
	 */
	@Deprecated
	public List<Map<String, String>> getUserByOrganizationAndRoleCode(String orgLevel,List<String> roleCodes);

	public List<Map<String, String>> getUserByOrganizationAndRoleCodeNew(String orgLevel,List<String> roleCodes);


	/**
	 * 查找待编制老师
	 * @param teacherVersionVo
	 * @return
	 *//*
	public List<User> getPreTeacher(TeacherVersionVo teacherVersionVo);*/
	
    /**
     * 根据 userId获取用户的RoleSign 从role表获取
     * @author xiaojinwang
     * @param userId
     * @date 2016/01/09
     * @return
     */
	public List<String> getUserRoleSignFromRole(String userId);
	public List<String> getUserRoleSignFromRoleNew(String userId);
	
    /**
     * 根据roles/Type 获取 userId的资源
     * @param userId
     * @param Type
     * @return
     */
    public List<Resource> getResourcesByUserIdRoleId(String userId,String type,List<Role> roles);
    
    /**
     * 前台用户具有多角色 切换角色 
     * @param password
     * @return
     */
    public Response changeUserRole(String password,String type);
    
    /**
     * 获取所有无效的用户
     */
    public List<User> getAllInvalidUsers();
    
    /**
     * 根据roleCode和orgLevel获取对应角色用户
     * @param roleCodes
     * @param orgLevel
     * @return
     */
    public List<User> getUserByRoldCodesAndOrgLevel(String roleCodes, String orgLevel);
    
    
    /**
     * 根据roleCode和orgLevel获取对应角色用户
     * @param roleCodes
     * @param orgLevel
     * @return
     */
	@Deprecated
    public List<User> getUserByJobSignAndOrgLevel(String jobSign, String orgLevel);

	/**
	 * 判断当前用户是不是前台模式
	 * @return
	 */
	public boolean currentLoginUserIsReceptionistMode(List<Role> roleByUserId);
	
	//将这个方法暴露出去 20170306 xiaojinwang
	public Organization getBelongOrgazitionByOrgType(String orgLevel, OrganizationType orgType) ;
	public Organization getOrgazitionByOrgType(String orgLevel, OrganizationType orgType,Boolean boolean1);
	
	//获取用户的主职位的部门
	public Organization getUserMainDeptByUserId(String userId);
	
	//根据用户的userId获取用户的主职位id和主职位部门对应的统计归属
	public Map<String, String> getUserMainDeptAndBelongByUserId(String userId);

	public List<String> getUserAllRoleSign(String userId);

	void saveOrUpdateUserForAdvance(User user) throws ApplicationException ;
	
	void processOssCallbackWithRedis(String ossCallbackBody);

    void editUserTeacherAttribute(UserTeacherAttributeVo attributeVo);

	UserTeacherAttributeVo findUserTeacherAttribute(String userId);

	/**
	 * 判断用户是否处于紧急状态
	 * @param orgs
	 * @return
	 */
	Boolean isUserStateOfEmergency(List<Organization> orgs);
	
	/**
	 * 获取所有禁用的邮件用户
	 * @return
	 */
	List<User> listDisabledHadMailUser();


	String findUserNameById(String userId);
	
	/**
	 * 根绝账号和电话查询用户
	 * @author: duanmenrun
	 * @Title: findUserByAccountContact 
	 * @Description: TODO 
	 * @throws 
	 * @param account 
	 * @param contact
	 * @return
	 */
	public User findUserByAccountContact(String account, String contact);
	
	/**
	 * 根据工号查询用户
	 * @author: duanmenrun
	 * @Title: findUserByEmployeeNo 
	 * @Description: TODO 
	 * @throws 
	 * @param employeeNo
	 * @return
	 */
	public User findUserByEmployeeNo(String employeeNo);
	
	/**
	 * 根据工号或账号和电话查询用户 星火用工号，培优用账号和电话
	 * @author: duanmenrun
	 * @Title: findUserByUserForTransaferVo 
	 * @Description: TODO 
	 * @throws 
	 * @param userForTransaferVo
	 * @return
	 */
	public Response findUserByUserForTransaferVo(UserForTransaferVo userForTransaferVo);
	/**
	 * 查询用户组织架构和个人信息
	 * @author: duanmenrun
	 * @Title: getUserDetail 
	 * @Description: TODO 
	 * @throws 
	 * @param userForTransaferVo
	 * @return
	 */
	public Response getUserDetail(UserForTransaferVo userForTransaferVo);
	/**
	 * 查询可以分配给咨询师的Organization
	 * @author: duanmenrun
	 * @Title: getDeptExistsDistributableZXS 
	 * @Description: TODO 
	 * @param userId
	 * @return
	 */
	public List<Organization> getDeptExistsDistributableZXS(String userId);

	/**
	 * 按照职位和范围查询用户
	 * @author: duanmenrun
	 * @Title: getUserOrganizationByJobs 
	 * @Description: TODO 
	 * @param orgLevel
	 * @param strings
	 * @param userName
	 * @return
	 */
	public List<Map<Object, Object>> getUserOrganizationByJobs(String orgLevel, String[] strings, String userName);
	/**
	 * 查询直属组织的指定职位用户
	 * @author: duanmenrun
	 * @Title: getUserByJobsOrgLevel 
	 * @Description: TODO 
	 * @param orgLevel
	 * @param strings
	 * @return
	 */
	public List<Map<Object, Object>> getUserByJobsOrgLevel(String orgLevel, String[] strings);
	/**
	 * 模糊查询组织用户
	 * @author: duanmenrun
	 * @Title: getUserOrganizationByJobsAndName 
	 * @Description: TODO 
	 * @param orgLevel
	 * @param strings
	 * @param userName
	 * @param brenchName
	 * @return
	 */
	public List<Map<Object, Object>> getUserOrganizationByJobsAndName(String orgLevel, String[] strings,
			String userName, String brenchName);

	/**
	 * 修改用户信息，组织架构角色
	 * 20180104   新
	 * @param dto
	 * @return
	 */
	Response modifyUserInfo(UserEditDto dto);

	/**
	 * 获取用户详细信息
	 * @param userId
	 * @return
	 */
	Response getUserInfoByUserId(String userId);

	/**
	 * 获取用户组织架构   userOrganizationRole
	 * @param userId
	 * @return
	 */
	List<Organization> getUserOrganizationList(String userId);

	/**
	 * 获取用户列表新
	 * @param dto
	 * @return
	 */
	DataPackage getUserInfoList(UserSearchDto dto,DataPackage dp);

	/**
	 * 同步人事用户
	 * @param vo
	 * @return
	 */
	Response pushUserInfo(MessagePushVo vo);

	/**
	 * 检查用户是否可以修改组织架构。
	 * @param vo
	 * @return
	 */
	Response checkUserOrgCanModify(UserOrganizationRoleVo vo);


	/**
	 * 获取用户的组织架构角色信息
	 * @param userId
	 * @return
	 */
	List<UserOrganizationRole> getUserOrganizationRoleByUserId(String userId);

	/**
	 * 获取用户的目录列表
	 * @param employeeNo 员工号
	 * @param type  菜单类型
	 * @return
	 */
	List getResourcesByEmployeeNo(String employeeNo, String type);
	/**
	 * 修改角色状态
	 * @author: duanmenrun
	 * @Title: updateRoleStatusById 
	 * @Description: TODO 
	 * @param roleId
	 * @param roleStatus
	 * @return
	 */
	public Response updateRoleStatusById(String roleId, ValidStatus roleStatus);
}
