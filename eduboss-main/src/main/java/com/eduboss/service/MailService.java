package com.eduboss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.mail.pojo.MailBoxMsg;
import com.eduboss.mail.pojo.MailExternalContract;
import com.eduboss.mail.pojo.MailInfoView;

/**@author wmy
 *@date 2015年9月24日下午4:30:53
 *@version 1.0 
 *@description
 */
public interface MailService {
	
	/**
	 * 初始化同步组织架构到邮箱系统
	 */
	public void syncOrg();
	/**
	 * 深度同步组织架构到邮件系统(适用于组织架构)
	 */
	public void syncOrgDeeply();
	
	/**
	 * 更新部门的名称
	 */
	public void updateUnitName(String orgId, String orgName);
	
	/**
	 * 更新部门所属上级部门
	 */
	public void updateParentUnit(String orgId, String newParentOrgId);
	
	/**
	 * 添加新部门
	 */
	public void createMailUnit(Organization org);
	/**
	 * 删除部门
	 */
	public void deleteMailUnit(Organization org);
    /**
     * 更新部门排序
     */
	public void updateMailUnitOrder(String orgId, Integer newOrder);
	
	/**
	 * 创建用户
	 */
	public String createMailUser(User user);
	
	/**
	 * 更新用户，开放更新属性有（用户所在部门，用户名称，用户使用状态：0启用，1禁用）
	 */
	public boolean updateMailUser(User user);
	
	/**
	 * 更新用户角色时，更新对应的用户列表里的数据
	 * （注：该方法已弃用，邮件列表已由对应角色改为对应职位）
	 */
	public void updateMailUserOnRoleChange(User user, String oldRoleIds);
	
	/**
	 * 启用或关闭用户。0启用，1禁用
	 */
	public boolean changeMailUserStatus(String userId, Integer status);
	
	/**
	 * 同步邮箱用户密码
	 */
	public boolean chageMailUserPassword(User user);
	
	/**
	 *删除用户
	 */
	public void deleteMailUser(User user);
	
	/**
	 * 创建邮件列表
	 * （注：该方法已弃用，邮件列表已由对应角色改为对应职位）
	 */
	public boolean createMailList(Role role);
	
	/**
	 *  添加用户到邮件列表
	 */
	public void addUserToMailList(User user, boolean isIncludeMailRootOrg);

	/**
	 * 更新邮件列表的名称、状态（0启用，1关闭）
	 */
	public boolean updateMailListNameOrStatus(String jobId, String newName, Integer newStatus);
	

	/**
	 * 更新邮件列表的名称、状态（0启用，1关闭）
	 */
	public boolean updateMailListNameOrStatus(UserJob userJob);
	
	/**
	 * 删除邮件列表
	 */
	public void deleteMailList(String userJobId);
	
	public MailBoxMsg getMailCountInfo(User user); 
	/**
	 * 获取接收邮件信息
	 */
	public List<MailInfoView> listMailInfos(String mailAddr, Integer skip, Integer limit);
	
	/**
	 * 获取用户没读邮件信息
	 */
	public List<MailInfoView> listNewMailInfos(String mailAddr,  Integer limit);

	/**
	 * 角色或组织架构变更时
	 * @param user
	 * @param oleRoleList
	 * @param oleOrganizationList
	 * （注：该方法已弃用，邮件列表已由对应角色改为对应职位）
	 */
	public void updateMailListUserOnRoleOrOrgChange(User user,
			String oldRoleIds, String oleOrgIds);
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mailAddr  邮箱地址
	 * @param loginDeviceType  登录设备类型  （默认pc版；1:iphone/android版；2:ipad版）
	 * @description 描述 自动登录邮箱
	 * @author wmy
	 * @date 2015年10月13日上午10:50:59
	 * @return void
	 */
	public void mailAutoLogin(HttpServletRequest request, HttpServletResponse response, User user, Integer loginDeviceType);
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param mailAddr  邮箱地址
	 * @param loginDeviceType  登录设备类型  （默认pc版；1:iphone/android版；2:ipad版）
	 * @description 描述 获取邮箱自动登录信息
	 * @author lixuejun
	 * @date 2016-07-22 18:11
	 * @return void
	 */
	public String getMailAutoLoginInfo(HttpServletRequest request, HttpServletResponse response, String mailAddr, Integer loginDeviceType);
	
	/**
	 * 邮件系统状态（0：启用，1:禁用）
	 */
	public boolean isMailSysInUse();
	
	/**
	 * 用户部门或职位变更时
	 * @param user
	 * @param oldDeptJobList
	 * @param newDeptJobList
	 */
	public void updateMailListUserOnDeptJobChange(User user,
			List<UserDeptJob> oldDeptJobList, List<UserDeptJob> newDeptJobList);

	/**
	 * 同步用户密码到邮箱系统
	 */
	public void syncUserPassword();
	
	/**
	 * 获取外部联系人
	 * @parma objUid
	 * @param mailAddr
	 * @return List<MailExternalContract>
	 */
	public List<MailExternalContract> listMailExternalContract(String objUid, String mailAddr);
	
	/**
	 * 创建外部联系人
	 * @param user
	 */
	void createMailExternalContract(User user);
	
	/**
	 * 删除邮箱用户，包括外部联系人
	 * @param user
	 */
	void deleteFullMailUser(User user);
	
	/**
     * 删除禁用的用户邮件
     */
    void delMailAddrByDisableUsers();
    
    String appendDomain(String account);
	
}


