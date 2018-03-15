package com.eduboss.dao;

import java.util.List;

import com.eduboss.domainVo.UserSearchDto;
import org.springframework.stereotype.Repository;

import com.eduboss.common.UserWorkType;
import com.eduboss.common.RoleCode;
import com.eduboss.domain.User;
import com.eduboss.dto.DataPackage;


/**
 * @classname	SysemConfigDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface UserDao extends GenericDAO<User, String> {

	@Deprecated
	public List<User> getStaffByRoleIdAndOrgId(String roleId, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit);
	public List<User> getStaffByRoleIdAndOrgIdNew(String roleId, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit);

	@Deprecated
	public List<User> getStaffByRoldCodeAndOrgId(String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit);
	public List<User> getStaffByRoldCodeAndOrgIdNew(String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit);
	//the common dao method had init in thd GenericDAO, add the special method in this class

	@Deprecated
	public List<User> getStaffByNameAndRoleCodeAndOrgId(String name, String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit);

	public List<User> getStaffByNameAndRoleCodeAndOrgIdNew(String name, String roleCode, String orgId, String orgLevel, boolean isIncludeChildLevel, int limit);
	/**
	 * 根据类型和组织架构查询用户
	 *@param workType 用户类型
	 *@param orgId 组织架构id
	 *@param orgLevel 组织架构code
	 *@param isIncludeChildLevel 是否查询 组织架构下的用户
	 *@return 用户列表
	 */
	public List<User> getUserByWorkTypeAndOrgId(UserWorkType workType, String orgId, String orgLevel, boolean isIncludeChildLevel);

	@Deprecated
	public List<User> findByNameOrIdAndRodeCode(String term, RoleCode roleCode);

	public List<User> findByNameOrIdAndRodeCodeNew(String term, RoleCode roleCode);

	/**
	 * 查找用户列表（自动搜索）
	 * @param input
	 * @param parentOrgId 
	 * @param roleCode 
	 * @return
	 */
	@Deprecated
	public List<User> getUserAutoComplate(String input, String roleCode, String parentOrgId);
	
	/**
	 * 查找用户列表（自动搜索）
	 */
	@Deprecated
	public List<User> getLimitUserAutoComplate(String input,String roleCode, String parentOrgId);

	/**
	 * 查找用户列表（自动搜索）新
	 */
	public List<User> getLimitUserAutoComplateNew(String input,String roleCode, String parentOrgId,String userId,int limit);
	
	/**
	 * 查找用户列表（自动搜索）,按组织架构走
	 */
	@Deprecated
	public List<User> getLimitUserByOrgAutoComplate(String input,String roleCode, String parentOrgId, String userId);

	public int  updateUserPassword(String account, String oldPassword,
			String newPassword);
	
	/**
	 * 查询拥有角色的用户
	 * @param roleCode 多个用，号隔开
	 * @param orgLevel 组织架构层级
	 * @return
	 */
	public List<User> getUserByRoldCodes(String roleCodes,String orgLevel);
	
	public List<User> getUserByRoldCodes2(String orgLevel);
	
	/**
	 * 查询拥有角色的用户 -加name模糊查询
	 * @param roleCode 多个用，号隔开
	 * @param orgLevel 组织架构层级
	 * @return
	 */
	@Deprecated
	public List<User> getUserByRoldCodes(String roleCodes,String orgLevel,String name);

	public List<User> getUserByRoldCodesNew(String roleCodes,String orgLevel,String name);

	public String getUserIdByName(String studyManegerName);
	
	/**
	 * 通过ID删除
	 * @param userId
	 */
	public void deleteById(String userId);

	/**
	 * 查询学生关联的用户
	 * @param attendanceNo
	 * @return
	 */
	public List<User> getStudentReferenceUser(String studentId);

	@Deprecated
	public List<User> getUserByBlcampusAndRole(String blcampus,String roleIds);

	public List<User> getUserByBlcampusAndRoleNew(String blcampus,String roleIds);
	
	/**
	 * 根据校区和职位来查询用户
	 * @param blcampus
	 * @param userJobIds
	 * @return
	 */
	public List<User> getUserByBlcampusAndUserJob(String blcampus, String userJobIds);

	/**
	 * 根据校区和职位来查询用户
	 * @param blcampus
	 * @param userJobIds
	 * @return 与 getUserByBlcampusAndUserJob相比，只返回资源池状态有效的客户
	 */
	public List<User> getUserByBlcampusAndUserJobValidate(String blcampus, String userJobIds);
	
	/**
	 * 获取学生 的相关的 培训机构联系方式 (例如 : 老师, 教务 等)
	 * @param studentId
	 * @return
	 */
	public List<User> getContactsForStudent(String studentId);
	
	/**
	 * 获取某组织架构下具有某一角色且开启了企业邮箱的用户
	 */
	public List<User> getUserByRoleAndOrgFroMail(String roleId, String orgLevel);
	
	/**
	 * 获取开启了企业邮箱的用户
	 */
	public List<User> getUserListHaveMail();
	
	public List<User> getUserAutoComplateByJobId(String jobId);
	
	/**
	 * 获取某校区某职位的用户列表
	 * @param deptId
	 * @param userjobSign
	 * @return
	 */
	public List<User> getUserBycampusAndjobSign(String deptId, String userjobSign);
	
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
	 * 根据账户查找用户
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);
	
	List<User> getLimitUserAutoComplate(String term, String extraHql);
	
	/**
     * 获取所有禁用的邮件用户
     * @return
     */
    List<User> listDisabledHadMailUser();
    
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
	 * @Title: findUserByEmployeeNo
	 * @Description: 根据工号和状态查询用户
	 * @throws
	 * @param employeeNo
	 * @return
	 */
	public User findUserByEmployeeNo(String employeeNo,String enableFlag);

	/**
	 * 根绝姓名和电话查询用户
	 * @author: duanmenrun
	 * @Title: findUserByNameContact 
	 * @Description: TODO 
	 * @param name
	 * @param contact
	 * @return
	 */
	public User findUserByNameContact(String name, String contact);

	/**
	 * 获取用户列表新，分页
	 * @param dto
	 * @param dp
	 * @return
	 */
	DataPackage getUserInfoList(UserSearchDto dto, DataPackage dp);
}
