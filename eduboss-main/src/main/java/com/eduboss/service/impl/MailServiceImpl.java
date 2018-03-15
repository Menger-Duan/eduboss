package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.RoleDao;
import com.eduboss.dao.UserDao;
import com.eduboss.dao.UserDeptJobDao;
import com.eduboss.dao.UserJobDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Role;
import com.eduboss.domain.SystemConfig;
import com.eduboss.domain.User;
import com.eduboss.domain.UserDeptJob;
import com.eduboss.domain.UserJob;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.mail.MailHandler;
import com.eduboss.mail.MailHandlerFactory;
import com.eduboss.mail.pojo.MailBoxMsg;
import com.eduboss.mail.pojo.MailExternalContract;
import com.eduboss.mail.pojo.MailInfo;
import com.eduboss.mail.pojo.MailInfoView;
import com.eduboss.mail.pojo.MailList;
import com.eduboss.mail.pojo.MailUnit;
import com.eduboss.mail.pojo.MailUser;
import com.eduboss.service.MailService;
import com.eduboss.service.SystemConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.StringUtil;

/**@author wmy
 *@date 2015年9月24日下午4:31:23
 *@version 1.0 
 *@description 2015-10-23改动，用户所在组织换为关联部门，角色换为职位
 */
@Service("mailService")
public class MailServiceImpl implements MailService{
	
	private static Log log = LogFactory.getLog(MailServiceImpl.class);
	
	private String MAIL_ORG_ID = null; 
	
	private Integer MAIL_COS_ID = null;
	
	private Integer MAIL_LIST_COS_ID = null;
	
	private String MAIL_DOMAIN_NAME = null;
	
	private String DEFAULT_PASSWORD = null;
		
	private String PROVIDER_ID = null;
	
	private MailHandler mailHandler = null;
	
	@Autowired
	private SystemConfigService systemConfigService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private OrganizationDao organizationDao;
		
	@Autowired
	private UserDeptJobDao userDeptJobDao;
	
	@Autowired
	private UserJobDao userJobDao;
	
	private void getMailHandler(){
		if(mailHandler == null) {
			MailHandlerFactory mailHandlerFactory = new MailHandlerFactory();
			mailHandler = mailHandlerFactory.getHandler("coremail", systemConfigService);
			this.setMailSystemConfig();
		}
	}
	@Override
	public void syncOrg() {
	    if(this.isMailSysInUse()) {
	        getMailHandler();
	        //获取组织架构树信息
	        List<Organization> orgList = userService.getOrganizationTree(null);
	        Organization topOrg = null;
	        for(Organization org : orgList){
	            if(StringUtils.isBlank(org.getParentId())){  //如果是空，则为最顶级组织
	                topOrg = org;
	                doSyncUnit(orgList, topOrg);				
	            }
	        }
	    }
	}
		
	
	private void doSyncUnit(List<Organization> orgList, Organization targetOrg){
	    if(this.isMailSysInUse()) {
	        boolean returnFlag = true;
	        if(StringUtils.isNotBlank(targetOrg.getId())) {
	            for(Organization org : orgList) {
	                if(targetOrg.getId().equals(org.getParentId())) {
	                    returnFlag = false;
	                    break;
	                }
	            }
	        }		
	        if(returnFlag == true) {
	            MailUnit unit = new MailUnit();
	            unit.setOrg_id(MAIL_ORG_ID);
	            unit.setOrg_unit_id(targetOrg.getId());
	            unit.setOrg_unit_name(targetOrg.getName());
	            unit.setParent_org_unit_id(targetOrg.getParentId());
	            unit.setOrg_unit_list_rank(mailHandler.getNewRank(targetOrg.getOrgOrder()));
	            try {
	                boolean flag = mailHandler.addUnit(unit);
	                if(flag == true){
	                    log.info("成功添加部门  " + unit.getOrg_unit_name());	
	                    /*//公司以上级别
					if((OrganizationType.BRENCH.equals(targetOrg.getOrgType()) || OrganizationType.GROUNP.equals(targetOrg.getOrgType())) && StringUtil.isNotBlank(targetOrg.getOrgSign())){
						addMailListToNewMailUnit(targetOrg);
					}*/
	                }else{
	                    log.info("添加部门  " + unit.getOrg_unit_name() + "失败");
	                }
	            } catch (Exception e) {
	                log.info("发生异常，添加部门 " + targetOrg.getName() + "失败");	
	                e.printStackTrace();
	            }
	            return;
	        }else {
	            //创建部门
	            String parentOrgUnitId = null;  //默认部门建立在组织下
	            if(StringUtils.isNotBlank(targetOrg.getParentId())) {  //如果父级不为空
	                parentOrgUnitId = targetOrg.getParentId();
	            }
	            MailUnit unit = new MailUnit();
	            unit.setOrg_id(MAIL_ORG_ID);
	            unit.setOrg_unit_id(targetOrg.getId());
	            unit.setOrg_unit_name(targetOrg.getName());
	            unit.setParent_org_unit_id(parentOrgUnitId);
	            unit.setOrg_unit_list_rank(mailHandler.getNewRank(targetOrg.getOrgOrder()));
	            try {
	                boolean flag = mailHandler.addUnit(unit);
	                if(flag == true){
	                    log.info("成功添加部门  " + unit.getOrg_unit_name());
	                    /*	//公司以上级别
					if((OrganizationType.BRENCH.equals(targetOrg.getOrgType()) || OrganizationType.GROUNP.equals(targetOrg.getOrgType())) && StringUtil.isNotBlank(targetOrg.getOrgSign())){
						addMailListToNewMailUnit(targetOrg);
					}*/
	                }else{
	                    log.info("添加部门  " + unit.getOrg_unit_name() + "失败");
	                }
	                for(Organization org_1 : orgList) {
	                    if(targetOrg.getId().equals(org_1.getParentId())) {
	                        doSyncUnit(orgList, org_1);
	                    }
	                }
	            } catch (Exception e) {
	                log.info("发生异常，添加部门 " + targetOrg.getName() + "失败。");	
	                e.printStackTrace();
	                throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
	            }
	        }
	    }
	}
	
	/**
	 * 深度同步：
	 * 1、先把邮件系统那边非根组织下的部门里的所有邮件列表删除；
	 * 2、把邮箱用户全部移动到根组织下；
     * 3、由下往上逐层删除邮件系统的部门；
     * 4、重新同步新的组织架构到邮件系统，同时添加新的邮件列表；
     * 5、把移动到根组织下的邮箱用户重新关联到新的部门下；
     * 6、最后把邮箱用户关联到对应的邮件列表里面；
	 */
	public void syncOrgDeeply() {
	    if(this.isMailSysInUse()) {
	        getMailHandler();
	        //删掉原来邮件列表
	        try {
	            //获取顶级部门
	            String topUnitIds = mailHandler.getTopUnitIds(MAIL_ORG_ID);
	            if(StringUtils.isNotBlank(topUnitIds)){
	                String[] topUnitIdArray = topUnitIds.split(","); 
	                for(String topUnitId : topUnitIdArray){		
	                    //删掉邮件列表
	                    mailHandler.deleteUnitUsersByCosId(MAIL_ORG_ID, topUnitId, MAIL_LIST_COS_ID, 0);
	                    //把邮箱用户移动到根组织下
	                    mailHandler.moveAllUserToOrg(MAIL_ORG_ID, topUnitId);
	                    
	                }
	                //删除外部联系人
	                deleteAllMailExternalContract();
	                //删除部门
	                mailHandler.removeAllUnit(MAIL_ORG_ID);
	            }
	            //重新同步组织架构
	            syncOrg();
	            rebuildMailExternalContract();
	            //把已开通的企业邮箱的用户重新移动到对应得部门
	            moveAllMailUserToUnitFromOrg();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
	        }		
	    }
	}
	
	/**
	 * 创建外部联系人
	 */
	@Override
	public void createMailExternalContract(User user) {
	    if(this.isMailSysInUse()) {
	        List<UserDeptJob> userDeptJobList = userDeptJobDao.findDeptJobByUserId(user.getUserId());
	        if (null != userDeptJobList && userDeptJobList.size() > 0) {
	            String departMentId = userDeptJobList.get(0).getDeptId() + ",";
	            for (UserDeptJob userDeptJob : userDeptJobList) {
	                if (departMentId.indexOf(userDeptJob.getDeptId() + ",") < 0) {
	                    departMentId +=  userDeptJob.getDeptId() + ",";
	                    if (userDeptJob.getIsMajorRole() == 1) {
	                        MailExternalContract contract = new MailExternalContract();
	                        contract.setObj_email(user.getMailAddr());
	                        contract.setOrg_id(MAIL_ORG_ID);
	                        contract.setOrg_unit_id(userDeptJob.getDeptId());
	                        contract.setObj_class("1");
	                        contract.setTrue_name(user.getRealName());
	                        String contact = user.getContact();
	                        if (StringUtils.isNotBlank(contact) && contact.length() >= 11) {
	                            contact = contact.substring(0, 3) + "****" + contact.substring(7, 11);
	                        }
	                        contract.setMobile_number(contact);
	                        UserJob userJob = userJobDao.findById(userDeptJob.getJobId());
	                        if (userJob != null) {
	                            contract.setDuty(userJob.getName());
	                        }
	                        try {
	                            String objUid = mailHandler.createMailExternalContract(contract);
	                            userDeptJob.setObjUid(objUid);
	                            userDeptJobDao.merge(userDeptJob);
	                            userDeptJobDao.flush();
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                            throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
	                        }
	                    }
	                }
	            }
	        }
	    }
	}
	
	/**
	 * 删除邮箱用户，包括外部联系人
	 */
	@Override
	public void deleteFullMailUser(User user) {
		 if(this.isMailSysInUse()) {
//				if (null != user.getMailStatus() && 0 == user.getMailStatus()) {
					boolean flag = this.changeMailUserStatus(user.getUserId(), 1);
					this.deleteMailUser(user);
					if(flag == true) {
						user.setMailStatus(1);
						user.setMailAddr(null);
					}
				}
//			 }
	}
	
	/**
	 * 删除禁用的用户邮件
	 */
	@Override
	public void delMailAddrByDisableUsers() {
	    if(this.isMailSysInUse()) {
    	    try {
                List<User> userList = userService.listDisabledHadMailUser();
                for (User user : userList) {
                    this.deleteFullMailUser(user);
                    userDao.merge(user);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
            }
	    }
	}
	
	@Override
    public String appendDomain(String account) {
        return account + "@" + MAIL_DOMAIN_NAME;
    }
	
	private void rebuildMailExternalContract() {
		List<User> userList = userService.getUserListHaveMail();
		for (User user : userList) {
			this.createMailExternalContract(user);
		}
	}
	
	private void deleteAllMailExternalContract() {
		List<User> userList = userService.getUserListHaveMail();
		for (User user : userList) {
			this.delteMailExternalContract(user, null);
		}
	}
	
	private void delteMailExternalContract(User user, List<UserDeptJob> oldDeptJobList) {
		List<UserDeptJob> userDeptJobList = oldDeptJobList != null ? oldDeptJobList : userDeptJobDao.findDeptJobByUserId(user.getUserId());
		for (UserDeptJob userDeptJob : userDeptJobList) {
			String objUid = userDeptJob.getObjUid();
			if (StringUtil.isNotBlank(objUid)) {
				try {
					// 删除成功，更新用户部门职位表
					if(mailHandler.deleteMailExternalContractById(objUid)) {
						if (oldDeptJobList == null) {
							userDeptJob.setObjUid(null);
							userDeptJobDao.save(userDeptJob);
						}
					} else {
						throw new ApplicationException("删除外部联系人出错!");
					}
				} catch(Exception e) {
					e.printStackTrace();
					throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
				}
			}
		}
		
		// 防止漏删除外部联系人
		List<MailExternalContract> list = this.listMailExternalContract(null, user.getMailAddr());
		if (list != null && list.size() > 0) {
			for (MailExternalContract contract : list) {
				try {
					mailHandler.deleteMailExternalContractById(contract.getObjUid());
				} catch(Exception e) {
					e.printStackTrace();
					throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
				}
			}
		}
	}
	
	private void moveAllMailUserToUnitFromOrg() {
		//获取所有开通了企业邮箱的用户
		//List<User> userList = userDao.getUserByRoleAndOrgFroMail(null, null);
		List<User> userList = userService.getUserListHaveMail();
		for(User user : userList){
			MailUser mailUser = new MailUser();
			mailUser.setOrg_unit_id(user.getDeptId());  //部门
			mailUser.setUser_id(user.getAccount().toLowerCase());
			mailUser.setDomain_name(MAIL_DOMAIN_NAME);
			String contact = user.getContact();
			if (StringUtils.isNotBlank(contact) && contact.length() >= 11) {
				contact = contact.substring(0, 3) + "****" + contact.substring(7, 11);
			}
			mailUser.setMobile_number(contact);
			mailUser.setDuty(user.getJobName());
			try {
				String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
				if(mailHandler.mailAddrExist(mailAddr)){
					mailHandler.updateUser(mailUser);
					if (user.getMailStatus() != null && user.getMailStatus() == 0) { //只需要更新启用邮箱的用户的邮箱列表
						//更新用户到邮件列表,不包含根组织
						addUserToMailList(user, false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
			}		
		}
	}

	@Override
	public String createMailUser(User user) {
		// TODO 1、新增邮箱用户；
		//2、根据职位找到对应的邮件列表，看是否有邮件列表，没有则新加，并把用户添加到邮件列表；
		//3、如果有，则对比邮件列表里的用户和该用户是否一样，是一样则不添加，如果没有或不一样则添加到邮件列表。
		getMailHandler();
		String retMailAddr = null;
		if(this.isMailSysInUse()) {
		    try {
		        retMailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		        if (mailHandler.mailAddrExist(retMailAddr)) {
		            return retMailAddr;
		        }
		        if(mailHandler.unitExist(MAIL_ORG_ID, user.getOrganizationId()) == true){  //存在部门
		            MailUser mailUser = new MailUser();
		            mailUser.setCos_id(MAIL_COS_ID);
		            mailUser.setDomain_name(MAIL_DOMAIN_NAME);
		            mailUser.setOrg_id(MAIL_ORG_ID);
		            mailUser.setOrg_unit_id(user.getDeptId());
//				mailUser.setPassword(DEFAULT_PASSWORD);
		            //改用用户在星火boss系统的密码
		            mailUser.setPassword(user.getPassword());
		            
		            mailUser.setUser_id(user.getAccount());
		            mailUser.setUser_status(0);
		            mailUser.setProviderId(PROVIDER_ID);
		            mailUser.setTrue_name(user.getRealName());
		            mailUser.setDuty(user.getJobName());  //职位
		            
		            boolean flag = mailHandler.addUser(mailUser);
		            if(flag == true){
//					retMailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		                //更新用户到邮件列表
		                addUserToMailList(user, true);
		            } else {
		                return "";
		            }
		        } else {
		            throw new ApplicationException(ErrorCode.MAIL_SYS_UNIT_NOT_EXIST_ERR);
		        }
		    } catch (ApplicationException ae) {
		        ae.printStackTrace();
		        if (StringUtil.isNotBlank(ae.getErrorMsg())) {
		            throw ae;
		        } else {
		            throw new ApplicationException(ErrorCode.MAIL_SYS_UNIT_NOT_EXIST_ERR);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
		    }
		}
		return retMailAddr;
		
	}
	/**
	 * 更新邮箱用户,userId不可谓空
	 * 目前主要更新邮箱名称、邮箱部门
	 */
	@Override
	public boolean updateMailUser(User user) {
		getMailHandler();
		if(this.isMailSysInUse()) {
    		MailUser mailUser = new MailUser();
    		mailUser.setOrg_unit_id(user.getDeptId());  //部门
    		mailUser.setDuty(user.getJobName());
    		mailUser.setUser_id(user.getAccount().toLowerCase());
    		mailUser.setDomain_name(MAIL_DOMAIN_NAME);
    		mailUser.setTrue_name(user.getRealName());  //名称
    		mailUser.setUser_status(user.getMailStatus()); 
    		String contact = user.getContact();
    		if (StringUtils.isNotBlank(contact) && contact.length() >= 11) {
    			contact = contact.substring(0, 3) + "****" + contact.substring(7, 11);
    		}
    		mailUser.setMobile_number(contact);
    		try {
    			return mailHandler.updateUser(mailUser);
    		} catch (Exception e) {
    			e.printStackTrace();
    			throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
    		}
		}
		return false;
	}
	
	@Override
	public void deleteMailUser(User user) {
		// TODO 同时从关联的邮件列表删除该用户
		getMailHandler();
		if(this.isMailSysInUse()) {
    		MailUser mailUser = new MailUser();
    		mailUser.setUser_id(user.getAccount());  
    		mailUser.setDomain_name(MAIL_DOMAIN_NAME);
    		try {
    			//从旧邮件列表移除用户
    			List<UserDeptJob> userDeptJobList = userDeptJobDao.findDeptJobByUserId(user.getUserId());
    			removeUserFromMailList(user, userDeptJobList);
    			mailHandler.deleteUser(mailUser, null);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		}
		
	}
	
	/**
	 * 更新角色时的操作,先把用户从先前关联的邮箱列表中移除，再重新关联
	 */
	@Override
	public void updateMailUserOnRoleChange(User user, String oldRoleIds) {
	    if(this.isMailSysInUse()) {
	        if(user != null && oldRoleIds != null){
	            getMailHandler();
	            String[] oldRoleIdArray = oldRoleIds.split(",");
	            //从旧邮件列表移除用户
	            removeUserFromMailList(user, oldRoleIdArray);
	            //更新用户到邮件列表
	            addUserToMailList(user, true);
	        }
	    }
	}
	/**
	 * 更新角色或组织时的操作
	 * @param user
	 * @param oleRoleList
	 * @param oleOrganizationList
	 */
	@Override
	public void updateMailListUserOnRoleOrOrgChange(User user,
			String oldRoleIds, String oldOrgIds) {
		getMailHandler();
		//组织
		if(this.isMailSysInUse()) {
		    List<Organization> oleOrganizationList = new ArrayList<Organization>();
		    String[] oldOrgIdArray = oldOrgIds.split(",");
		    for(int i = 0; i < oldOrgIdArray.length; i++) {
		        Organization org = new Organization();
		        org.setId(oldOrgIdArray[i]);
		        oleOrganizationList.add(org);
		    }
		    List<Organization> orgList = getMailListBelongOrgs(oleOrganizationList);
		    //角色
		    List<Role> oldRoleList = new ArrayList<Role>();
		    String[] oldRoleIdArray = oldRoleIds.split(",");
		    for(int j = 0; j < oldRoleIdArray.length; j++) {
		        Role role = roleDao.findById(oldRoleIdArray[j]);
		        oldRoleList.add(role);
		    }
		    //从旧邮件列表移除用户
		    removeUserFromMailList(user, oldRoleList, orgList);
		    //更新用户到邮件列表
		    addUserToMailList(user, true);
		}
		
	}
	
	@Override
	public void updateMailListUserOnDeptJobChange(User user,
			List<UserDeptJob> oldDeptJobList, List<UserDeptJob> newDeptJobList) {
		// TODO Auto-generated method stub
	    getMailHandler();
	    if(this.isMailSysInUse()) {
	        //从旧邮件列表移除用户
	        removeUserFromMailList(user, oldDeptJobList);
	        //更新用户到邮件列表
	        addUserToMailList(user, true);
	        //删除该用户的外部联系人
	        this.delteMailExternalContract(user, oldDeptJobList);
	        this.createMailExternalContract(user);
	    }
	}
	
	//重置邮件列表里的用户
	private void reSetMailListUsers(List<MailList> mailListList, String mailAddr, List<UserDeptJob> userDeptJobList, String method) {		
		for(UserDeptJob userDeptJob: userDeptJobList) {
			//职位
			UserJob userJob = userJobDao.findById(userDeptJob.getJobId());
			String userJobSign = userJob.getJobSign();
			if(StringUtil.isNotBlank(userJobSign)) {
				//1、加入到本级部门职位(只含本级部门人员)
				Organization dept = organizationDao.findById(userDeptJob.getDeptId());
				if(dept!=null && StringUtil.isNotBlank(dept.getOrgSign())) {
					if("add".equalsIgnoreCase(method)){
						//部门职位（本级）
						getAddUserToMailList(mailListList, mailAddr, dept, userJob, true);
						//部门所有人（本级）
						getAddUserToMailList(mailListList, mailAddr, dept, null, true);	
					}
					if("remove".equalsIgnoreCase(method)) {
						//部门职位（本级）
						getRemoveUserFromMailList(mailListList, mailAddr, dept, userJob, true);
						//部门所有人（本级）
						getRemoveUserFromMailList(mailListList, mailAddr, dept, null, true);	
					}			
				}else {
					log.error("部门" +  dept.getName() + "的标识(orgSign)不可为空");
				}				
				//2、加入到本级及以上的部门职位(包含本级及以下部门人员)				
				List<Organization> orgList = organizationDao.getIncludeAllParentOrgByOrgId(userDeptJob.getDeptId());		//获取包括本级的上属部门
				//对每个组织下的邮件列表添加用户列表
				for(Organization org : orgList) {
					String orgSign = org.getOrgSign();
					if(StringUtil.isNotBlank(orgSign)) {
						if("add".equalsIgnoreCase(method)){
							//部门职位（包含下级）
							getAddUserToMailList(mailListList, mailAddr, org, userJob, false);
							//部门所有人（包含下级）
							getAddUserToMailList(mailListList, mailAddr, org, null, false);
						}
						if("remove".equalsIgnoreCase(method)) {
							//部门职位（包含下级）
							getRemoveUserFromMailList(mailListList, mailAddr, org, userJob, false);
							//部门所有人（包含下级）
							getRemoveUserFromMailList(mailListList, mailAddr, org, null, false);
						}							
					}else {
						log.error("部门" +  org.getName() + "的标识(orgSign)不可为空");
					}
				}
			}else {
				log.error("职位" + userJob.getJobName() + "的标识(jobSign)不可为空");
			}

		}
	}
	
	private void removeUserFromMailList(User user, List<UserDeptJob> oldDeptJobList) {
		List<MailList> mailListList = new ArrayList<MailList>();
		//用户邮件地址
		String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		reSetMailListUsers(mailListList, mailAddr, oldDeptJobList, "remove");
		if(mailListList != null && mailListList.size() > 0){
			try {
				mailHandler.updateMailListList(mailListList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//从邮件列表中移除用户
	private void removeUserFromMailList(User user, List<Role> roleList, List<Organization> orgList) {
		List<MailList> mailListList = new ArrayList<MailList>();
		//用户邮件地址
		String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		//某角色对应的邮件列表
		for(Role role : roleList){    //从旧邮件列表移除用户
			String roleSign = role.getRoleSign();
			if(StringUtil.isNotBlank(roleSign)) {
				for(Organization org : orgList) {
					String orgSign = org.getOrgSign();
					//邮件列表地址
					if(StringUtil.isNotBlank(orgSign)) {
						//getRemoveUserFromMailList(mailListList, mailAddr, orgSign.toLowerCase(), roleSign.toLowerCase());
					}				
				}
				//主组织结构下某角色的邮件列表
				//getRemoveUserFromMailList(mailListList, mailAddr, null, roleSign.toLowerCase());
			}			
		}	
		//部门所有人邮件列表
		for(Organization org : orgList) {
			String orgSign = org.getOrgSign();
			//邮件列表地址
			if(StringUtil.isNotBlank(orgSign)) {
				//getRemoveUserFromMailList(mailListList, mailAddr, orgSign.toLowerCase(), null);
			}				
		}
		//主组织结构下包含所有人的邮件列表
		//getRemoveUserFromMailList(mailListList, mailAddr, null, MAIL_ORG_ID.toLowerCase());  //mailOrgId作为邮件列表地址名
		if(mailListList != null && mailListList.size() > 0){
			try {
				mailHandler.updateMailListList(mailListList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * @param mailListList
	 * @param mailAddr
	 * @param dept
	 * @param userJob
	 * @param limitedSameLevelUser (true：只限本级部门，false: 包含本级及下属部门)
	 */
	private void getRemoveUserFromMailList(List<MailList> mailListList, String mailAddr, Organization dept, UserJob userJob, boolean limitedSameLevelUser) {
		String orgSign = null;  
		String jobSign = null;
		if(dept != null && StringUtils.isNotBlank(dept.getOrgSign())) {
			orgSign = dept.getOrgSign();
		}
		if(userJob != null && StringUtils.isNotBlank(userJob.getJobSign())) {
			jobSign = userJob.getJobSign();
		}
		String mailListId = getDefineMailListId(orgSign, jobSign, limitedSameLevelUser);
		//邮件列表地址
		if(StringUtils.isNotBlank(mailListId)){
			String mailListAddr = mailListId + "@" + MAIL_DOMAIN_NAME; 
			try {
				if(mailHandler.mailAddrExist(mailListAddr)) {
					String forwardMailList = mailHandler.getMailListInfoByAttrs(mailListAddr, "forwardmaillist");
					if(forwardMailList != null) {
						String[] strs = forwardMailList.split("=");
						if(strs.length > 1) {
							forwardMailList = strs[1];
							if(forwardMailList.indexOf(mailAddr) >= 0){
								StringBuffer sb = new StringBuffer();
								String[] array = forwardMailList.split("%2C");  //","的urlDecode解码为 "%2C"
								for(int j = 0; j < array.length; j++){
									if(!mailAddr.equals(array[j])){
										sb.append("," + array[j]);
									}
								}
								if(sb != null && sb.length() > 0){						
									forwardMailList = sb.substring(1);
									MailList mailList= new MailList();
									mailList.setForwardMailList(forwardMailList);
									mailList.setMailListId(mailListId);
									mailList.setDomainName(MAIL_DOMAIN_NAME);
									mailList.setMailListRank(1000);
									mailListList.add(mailList);
								}else{
									/*//更新为空
									MailList mailList= new MailList();
									mailList.setForwardMailList("empty");  //置空
									mailList.setMailListId(mailListId);
									mailList.setDomainName(MAIL_DOMAIN_NAME);
									mailListList.add(mailList);*/
									//删除空职位
									MailList emptyMailList= new MailList();
									emptyMailList.setMailListId(mailListId);
									emptyMailList.setDomainName(MAIL_DOMAIN_NAME);
									mailHandler.deleteMailList(emptyMailList, 0);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	
	/**
	 * @description 把用户从旧的邮件列表移除
	 * @author wmy
	 */
	private void removeUserFromMailList(User user, String[] roleIds){
		List<MailList> mailListList = new ArrayList<MailList>();
		//获取用户的分公司（包括跨公司）及以上组织
		List<Organization> orgList = user.getOrganization();
		if(orgList == null || orgList.size() == 0) orgList = userService.getUserById(user.getUserId()).getOrganization();
		List<Organization> newOrgList = getMailListBelongOrgs(orgList);
		//用户邮件地址
		String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		for(int i = 0; i < roleIds.length; i++){    //从旧邮件列表移除用户
			String roleId = roleIds[i];
			Role dbRole = roleDao.findById(roleId);
			String roleSign = dbRole.getRoleSign();
			for(Organization org : newOrgList) {
				String orgSign = org.getOrgSign();
				//邮件列表地址
				String mailListAddr = orgSign.toLowerCase() + "_" + roleSign.toLowerCase() + "@" + MAIL_DOMAIN_NAME;  //部门_角色
				if(mailListAddr != null){
					try {
						String forwardMailList = mailHandler.getMailListInfoByAttrs(mailListAddr, "forwardmaillist");
						if(forwardMailList != null) {
							String[] strs = forwardMailList.split("=");
							if(strs.length > 1) {
								forwardMailList = strs[1];
								if(forwardMailList.indexOf(mailAddr) >= 0){
									StringBuffer sb = new StringBuffer();
									String[] array = forwardMailList.split("%2C");  //","的urlDecode解码为 "%2C"
									for(int j = 0; j < array.length; j++){
										if(!mailAddr.equals(array[j])){
											sb.append("," + array[j]);
										}
									}
									if(sb != null && sb.length() > 0){						
										forwardMailList = sb.substring(1);
										MailList mailList= new MailList();
										mailList.setForwardMailList(forwardMailList);
										mailList.setMailListId(orgSign.toLowerCase() + "_" + roleSign.toLowerCase());
										mailList.setDomainName(MAIL_DOMAIN_NAME);
										mailListList.add(mailList);
									}
									else{
										MailList mailList= new MailList();
										mailList.setForwardMailList("empty");  //置空
										mailList.setMailListId(orgSign.toLowerCase() + "_" + roleSign.toLowerCase());
										mailList.setDomainName(MAIL_DOMAIN_NAME);
										mailListList.add(mailList);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}	
		if(mailListList != null && mailListList.size() > 0) {
			try{
				mailHandler.updateMailListList(mailListList);			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean createMailList(Role role) {
	    if(this.isMailSysInUse()) {
	        if(role != null && StringUtils.isNotBlank(role.getRoleId())){
	            getMailHandler();
	            Role dbRole = roleDao.findById(role.getRoleId());
	            createMailListOnOrg(dbRole);
	            createMailListOnUnitByRole(dbRole);
	        }
	        return true;
	    }
	    return false;
	}
    
	private MailList getBaseMailList() {
		MailList mailList = new MailList();
		mailList.setCosId(MAIL_LIST_COS_ID);
		mailList.setOrgId(MAIL_ORG_ID);
		mailList.setProviderId(PROVIDER_ID);
		mailList.setDomainName(MAIL_DOMAIN_NAME);
		mailList.setForwardActive(1);
		mailList.setKeepLocal(0);
		mailList.setRejectJunk(1);
		return mailList;
	}
	
	/**
	 * 不区分部门的邮件列表，包含所有人
	 */
	private void createMailListOnOrg(Role role) {
		//按角色分
		if(role != null && StringUtils.isNotBlank(role.getRoleId())){
			MailList mailList = getBaseMailList();
			mailList.setMailListId(role.getRoleSign());
			mailList.setTrueName(role.getName());
			String forwardMailList = null;
			//查找该角色下且开通了企业邮箱的用户
			List<User> userList = userDao.getUserByRoleAndOrgFroMail(role.getRoleId(), null);
			for(User user : userList){
				String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
				if(forwardMailList == null) forwardMailList = mailAddr;
				else forwardMailList = forwardMailList + "," + mailAddr;			
			}
			if(forwardMailList != null) mailList.setForwardMailList(forwardMailList);  //设置邮箱列表下的用户列表
			try {
				 mailHandler.addMailList(mailList);
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
			}
		}
		//不区分角色
		createMailListOnOrgForAllUser();
		
	} 
	
	//邮件系统组织下的包括所有人的邮件列表
	private void createMailListOnOrgForAllUser() {
		//判断是否存在，避免重复创建
		String mailListAddr = MAIL_ORG_ID.toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		try {
			//判断是否存在，避免多角色对应的邮件列表新建时重复创建
			if(mailHandler.mailAddrExist(mailListAddr) == false) {
				MailList mailList = getBaseMailList();
				mailList.setMailListId(MAIL_ORG_ID.toLowerCase());  //定义一个以mailOrgId为名称的的邮件列表
				mailList.setTrueName("所有人");
				String forwardMailList = null;
				//查找所有开通了企业邮箱的用户
				List<User> userList = userDao.getUserByRoleAndOrgFroMail(null, null);
				for(User user : userList){
					String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
					if(forwardMailList == null) forwardMailList = mailAddr;
					else forwardMailList = forwardMailList + "," + mailAddr;			
				}
				if(forwardMailList != null) mailList.setForwardMailList(forwardMailList);  //设置邮箱列表下的用户列表
				try {
					 mailHandler.addMailList(mailList);
				} catch (Exception e) {
					e.printStackTrace();
					throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param role
	 * @return
	 * @description 区分部门，在分公司及以上组织建立邮件列表
	 */
	private void createMailListOnUnitByRole(Role role) {
		List<MailList> mailListList = new ArrayList<MailList>();
		String roleSign = role.getRoleSign();
		String roleName = role.getName();
		//查找所有分公司列表
		List<Organization> orgList = organizationDao.findAll();
		for(Organization org : orgList) {
			String orgSign = org.getOrgSign();
			String orgName = org.getName();
			if((OrganizationType.BRENCH.equals(org.getOrgType()) || OrganizationType.GROUNP.equals(org.getOrgType())) && StringUtil.isNotBlank(orgSign)){  //分公司
				MailList mailList = getBaseMailList();
				mailList.setMailListId(orgSign.toLowerCase() + "_" + roleSign.toLowerCase());  //组织+角色
				mailList.setTrueName(orgName + "_" + roleName);//组织+角色
				mailList.setOrgUnitId(org.getId());  //设置邮件列表所在部门
				String forwardMailList = null;
				//查找该分公司下、该类角色开通了企业邮箱的用户
				List<User> userList = userDao.getUserByRoleAndOrgFroMail(role.getRoleId(), org.getOrgLevel());
				for(User user : userList){
					String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
					if(forwardMailList == null) forwardMailList = mailAddr;
					else forwardMailList = forwardMailList + "," + mailAddr;			
				}
				if(forwardMailList != null) mailList.setForwardMailList(forwardMailList);  //设置邮箱列表下的用户列表
				mailListList.add(mailList);
				
				//分公司及以上组织所有人
				String maillistAddr_unit = orgSign.toLowerCase() + "@" + MAIL_DOMAIN_NAME;
				try {
					if(mailHandler.mailAddrExist(maillistAddr_unit) == false){
						MailList mailList_unit = getBaseMailList();
						mailList_unit.setMailListId(orgSign.toLowerCase());  //组织
						mailList_unit.setTrueName(orgName);//组织
						mailList_unit.setOrgUnitId(org.getId());  //设置邮件列表所在部门
						String forwardMailList_unit = null;
						//查找该分公司下开通了企业邮箱的用户
						List<User> userList_unit = userDao.getUserByRoleAndOrgFroMail(null, org.getOrgLevel());
						for(User user_unit : userList_unit){
							String mailAddr_unit = user_unit.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
							if(forwardMailList_unit == null) forwardMailList_unit = mailAddr_unit;
							else forwardMailList_unit = forwardMailList_unit + "," + mailAddr_unit;			
						}
						if(forwardMailList_unit != null) mailList_unit.setForwardMailList(forwardMailList_unit);  //设置邮箱列表下的用户列表
						mailListList.add(mailList_unit);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
					
		}
		if(mailListList != null && mailListList.size() > 0 ) {
			try {
				mailHandler.addMailListList(mailListList);			 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	//获取邮件列表归属的组织（部门）
	private List<Organization> getMailListBelongOrgs(List<Organization> orgs) {
		List<Organization> orgList = new ArrayList<Organization>();
		Map<String, Organization> map = new HashMap<String, Organization>();
	    //获取对应分公司
	    for(Organization org : orgs) {
	    	//获取对应分公司
	    	Organization branch = userService.getBelongBranchByOrgId(org.getId());
	    	if(map.isEmpty() || !map.containsKey(branch.getId())) {
	    		map.put(branch.getId(), branch);
	    		orgList.add(branch);
	    	}
	    	//获取对应的集团
	    	Organization group = userService.getBelongGrounpByOrgId(org.getId());
	    	if(map.isEmpty() || !map.containsKey(group.getId())) {
	    		map.put(group.getId(), group);
	    		orgList.add(group);
	    	}
	    }    
		return orgList;		
	}
	
	/**
	 * 更新用户到对应的邮件列表
	 */
	@Override
	public void addUserToMailList(User user, boolean isIncludeMailRootOrg) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    List<MailList> mailListList = new ArrayList<MailList>();
		    //用户邮箱地址
		    String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		    List<UserDeptJob> userDeptJobList = userDeptJobDao.findDeptJobByUserId(user.getUserId());
		    reSetMailListUsers(mailListList, mailAddr, userDeptJobList, "add");
		    if(mailListList != null && mailListList.size() > 0) {
		        try {
		            mailHandler.updateMailListList(mailListList);		 
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}
	}	
	
	private void getAddUserToMailList(List<MailList> mailListList, String mailAddr, Organization dept, UserJob userJob, boolean limitedSameLevelUser) {
		String orgSign = null;  
		String jobSign = null;
		String orgName = null;
		String jobName = null;
		if(dept != null && StringUtils.isNotBlank(dept.getOrgSign())) {
			orgSign = dept.getOrgSign();
			orgName = dept.getName();
		}
		if(userJob != null && StringUtils.isNotBlank(userJob.getJobSign())) {
			jobSign = userJob.getJobSign();
			jobName = userJob.getJobName();
		}
		String mailListId = getDefineMailListId(orgSign, jobSign, limitedSameLevelUser);
		String mailListName = getDefineMailListName(orgName, jobName, limitedSameLevelUser);  
		if(StringUtils.isNotBlank(mailListId)){
			//邮件列表地址
			String mailListAddr = mailListId + "@" + MAIL_DOMAIN_NAME; 
			try {
				String forwardMailList = null;
				if(mailHandler.mailAddrExist(mailListAddr)) {
					forwardMailList = mailHandler.getMailListInfoByAttrs(mailListAddr, "forwardmaillist");
					if(forwardMailList != null) {   //有对应的邮件列表
						String[] strs = forwardMailList.split("=");
						if(strs.length > 1) {
							forwardMailList = strs[1];
							if(forwardMailList.indexOf(mailAddr) >= 0) return;
							else {
								StringBuffer sb = new StringBuffer();
								String[] array = forwardMailList.split("%2C");  //","的urlDecode解码未 "%2C"
								for(int i = 0; i < array.length; i++){
									if(!mailAddr.equals(array[i])){
										sb.append("," + array[i]);
									}
								}
								if(sb != null && sb.length() > 0){						
									forwardMailList = sb.substring(1);
								}
								forwardMailList = forwardMailList + "," + mailAddr;
							}
						}else {
							forwardMailList = mailAddr;
						}				
						MailList mailList= new MailList();
						mailList.setForwardMailList(forwardMailList);
						mailList.setMailListId(mailListId);
						mailList.setDomainName(MAIL_DOMAIN_NAME);
						mailList.setMailListRank(1000);
						mailListList.add(mailList);
					}
				} else {   //新加
					Integer status = 0; //状态
					if(userJob != null) status = Integer.valueOf(userJob.getFlag());				
					MailList newMailList = getBaseMailList();
					newMailList.setMailListId(mailListId);
					newMailList.setTrueName(mailListName);
					newMailList.setOrgUnitId(dept.getId());
					newMailList.setForwardMailList(mailAddr);
					newMailList.setUserStatus(status);
					newMailList.setMailListRank(1000);
					mailHandler.addMailList(newMailList);							
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	}
	@Override
	public void updateUnitName(String orgId, String orgName) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailUnit unit = new MailUnit();
	        unit.setOrg_id(MAIL_ORG_ID);
	        unit.setOrg_unit_id(orgId);
	        unit.setOrg_unit_name(orgName);
	        try {
	            mailHandler.updateUnit(unit);
	            updateMailListNameByOrgName(orgId, orgName);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
	@Override
	public void updateMailUnitOrder(String orgId, Integer newOrder) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailUnit unit = new MailUnit();
		    unit.setOrg_id(MAIL_ORG_ID);
		    unit.setOrg_unit_id(orgId);
		    unit.setOrg_unit_list_rank(mailHandler.getNewRank(newOrder));
		    try {
		        mailHandler.updateUnit(unit);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	}	
	//coreMail部门移动有bug！暂不可用
	@Override
	public void updateParentUnit(String orgId, String newParentOrgId) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailUnit unit = new MailUnit();
		    unit.setOrg_id(MAIL_ORG_ID);
		    unit.setOrg_unit_id(orgId);
		    unit.setParent_org_unit_id(newParentOrgId);
		    try {
		        mailHandler.updateUnit(unit);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	}
	
	//更新邮箱组织部门下的邮件列表名称
	private void updateMailListNameByOrgName(String orgId, String orgName) {
		List<MailList> mailListList = new ArrayList<MailList>();
		Organization org = organizationDao.findById(orgId);
		String orgSign = org.getOrgSign();
		List<Role> roleList = roleDao.findAll();
		for(Role role : roleList){
			String roleSign = role.getRoleSign();
			String roleName = role.getName();
			if(StringUtil.isNotBlank(roleSign) && role.getMailListStatus() != null) { //有标记且状态不为空 
				MailList mailList = new MailList();
				mailList.setMailListId(orgSign.toLowerCase() + "_" + roleSign.toLowerCase());  //组织_角色
				mailList.setDomainName(MAIL_DOMAIN_NAME);
				if(roleName != null) mailList.setTrueName(orgName + "_" + roleName);//组织_角色
				mailListList.add(mailList);
			}
		}
		if(mailListList != null && mailListList.size() > 0) {
			try {
				mailHandler.updateMailListList(mailListList);					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//添加邮件列表到新的部门
	private void addMailListToNewMailUnit(Organization org) {
		if(org != null && (OrganizationType.BRENCH.equals(org.getOrgType()) || OrganizationType.GROUNP.equals(org.getOrgType())) 
				&& StringUtil.isNotBlank(org.getOrgSign())) {
			List<MailList> mailListList = new ArrayList<MailList>();
			String orgSign = org.getOrgSign();
			String orgName = org.getName();
			List<Role> roleList = roleDao.findAll();
			for(Role role : roleList){
				String roleSign = role.getRoleSign();
				String roleName = role.getName();
				if(StringUtil.isNotBlank(roleSign) && role.getMailListStatus() != null) { //有标记且状态不为空 
					MailList mailList = getBaseMailList();
					mailList.setMailListId(orgSign.toLowerCase() + "_" + roleSign.toLowerCase());  //组织_角色
					mailList.setTrueName(orgName + "_" + roleName);//组织_角色
					mailList.setOrgUnitId(org.getId());
					mailListList.add(mailList);
				}
			}
			//部门所有人		
			MailList mailList_all = getBaseMailList();
			mailList_all.setMailListId(orgSign.toLowerCase());  //组织
			mailList_all.setTrueName(orgName);//组织
			mailList_all.setOrgUnitId(org.getId());
			mailListList.add(mailList_all);
			if(mailListList != null && mailListList.size() > 0) {
				try {
					mailHandler.addMailListList(mailListList);				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}				
	}	
	
	@Override
	public void createMailUnit(Organization org) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailUnit mailUnit = new MailUnit();
		    mailUnit.setOrg_id(MAIL_ORG_ID);
		    mailUnit.setOrg_unit_id(org.getId());
		    mailUnit.setOrg_unit_name(org.getName());
		    if(org.getParentId() != null) mailUnit.setParent_org_unit_id(org.getParentId());
		    mailUnit.setOrg_unit_list_rank(mailHandler.getNewRank(org.getOrgOrder()));
		    try {
		        mailHandler.addUnit(mailUnit);
		        /*//新建的部门下添加已经创建好的邮件列表
			addMailListToNewMailUnit(org);*/
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	}
	@Override
	public void deleteMailUnit(Organization org) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailUnit mailUnit = new MailUnit();
		    mailUnit.setOrg_id(MAIL_ORG_ID);
		    mailUnit.setOrg_unit_id(org.getId());
		    try {
		        mailHandler.deleteUnit(mailUnit);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	}
	@Override
	public boolean changeMailUserStatus(String userId, Integer status) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    User user = userService.getUserById(userId);
		    MailUser mailUser = new MailUser(); 
		    mailUser.setUser_id(user.getAccount());
		    mailUser.setDomain_name(MAIL_DOMAIN_NAME);
		    mailUser.setUser_status(status);  //更新状态
		    mailUser.setPassword(user.getPassword()); // 更新密码
		    if (1 == status) {
		        List<UserDeptJob> userDeptJobList = userDeptJobDao.findDeptJobByUserId(user.getUserId());
		        removeUserFromMailList(user, userDeptJobList);
		        // 删除该用户的外部联系人
		        this.delteMailExternalContract(user, null);
		    } else {
		        addUserToMailList(user, true);
		        // 创建该用户的外部联系人
		        this.createMailExternalContract(user);
		    }
		    try {
		        if(!mailHandler.mailAddrExist(user.getMailAddr())){ 
		            if (0 == status) {
		                mailUser.setCos_id(MAIL_COS_ID);
		                mailUser.setOrg_id(MAIL_ORG_ID);
		                mailUser.setOrg_unit_id(user.getDeptId());
//					mailUser.setPassword(DEFAULT_PASSWORD);
		                //改用用户在星火boss系统的密码
		                
		                mailUser.setUser_id(user.getAccount());
		                mailUser.setProviderId(PROVIDER_ID);
		                mailUser.setTrue_name(user.getRealName());
		                mailUser.setDuty(user.getJobName());  //职位
		                return mailHandler.addUser(mailUser);
		            }
		        } 
		        return mailHandler.updateUser(mailUser);
		    } catch(ApplicationException ae) {
		        ae.printStackTrace();
		        throw ae;
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
		    }
		}
		return false;
	}
	
	/**
	 * 同步邮箱用户密码
	 */
	@Override
	public boolean chageMailUserPassword(User user) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailUser mailUser = new MailUser(); 
		    mailUser.setUser_id(user.getAccount());
		    mailUser.setDomain_name(MAIL_DOMAIN_NAME);
		    mailUser.setPassword(user.getPassword()); // 更新密码
		    try {
		        return mailHandler.updateUser(mailUser);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
		    }
		}
		return false;
	}

	/**
	 * 更新邮件列表的状态和名称
	 */
	@Override
	public boolean updateMailListNameOrStatus(UserJob userJob) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    if(userJob != null) {
		        String jobSign = userJob.getJobSign();
		        String jobName = userJob.getJobName();
		        if(StringUtil.isNotBlank(jobSign)) {
		            List<Organization> orgList = organizationDao.findAll();
		            for(Organization org : orgList) {  //组织即是部门
		                String orgSign = org.getOrgSign();
		                String orgName = org.getName();
		                if(StringUtils.isNotBlank(orgSign)) {
		                    //部门职位（本级）
		                    String mailListId_1 = getDefineMailListId(orgSign.toLowerCase(), jobSign.toLowerCase(), true);
		                    String mailListName_1 = getDefineMailListName(orgName, jobName, true); 
		                    doUpdateMailListNameOrStatus(mailListId_1, mailListName_1, userJob.getFlag());
		                    //部门职位（包含下级）
		                    String mailListId_2 = getDefineMailListId(orgSign.toLowerCase(), jobSign.toLowerCase(), false);
		                    String mailListName_2 = getDefineMailListName(orgName, jobName, false); 
		                    doUpdateMailListNameOrStatus(mailListId_2, mailListName_2, userJob.getFlag());
		                }else {
		                    log.warn("部门" + org.getName() + "的标识（orgSign）为空");
		                }					
		            }
		            //更新用户的职位名称
		            List<User> userList = userService.getUserListHaveMail();
		            for(User user : userList){
		                if(userJob.getId().equals(user.getJobId())){
		                    MailUser mailUser = new MailUser();
		                    mailUser.setDuty(jobName);
		                    mailUser.setUser_id(user.getAccount().toLowerCase());
		                    mailUser.setDomain_name(MAIL_DOMAIN_NAME);
		                    try {
		                        mailHandler.updateUser(mailUser);
		                    } catch (Exception e) {
		                        e.printStackTrace();
		                        log.error(ErrorCode.MAIL_SYS_CONNECT_ERR);
		                    }
		                }					
		            }
		        }else {
		            log.warn("职位" + userJob.getJobName() + "的标识（jobSign）为空");
		        }
		    }
		    return true;
		}
		return false;
	}
	
	private void doUpdateMailListNameOrStatus(String mailListId, String mailListName, Integer status) {
		MailList mailList = new MailList();
		mailList.setMailListId(mailListId);  
		mailList.setDomainName(MAIL_DOMAIN_NAME);
		if(mailListName != null) mailList.setTrueName(mailListName);
		if(status != null) mailList.setUserStatus(status);	
		try {
			String mailListAddr = mailListId + "@" + MAIL_DOMAIN_NAME;
			if(mailHandler.mailAddrExist(mailListAddr) == true) {
				 mailHandler.updateMailList(mailList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteMailList(String jobId) {
	    getMailHandler();
	    if(this.isMailSysInUse()) {
	        if(StringUtils.isNotBlank(jobId)) {
	            //deleteMailListList(role);
	            UserJob userJob = userJobDao.findById(jobId);
	            String jobSign = userJob.getJobSign();
	            if(userJob != null && StringUtils.isNotBlank(jobSign)) {
	                List<MailList> mailListList = new ArrayList<MailList>();
	                List<Organization> orgList = organizationDao.findAll();
	                for(Organization org : orgList) {
	                    String orgSign = org.getOrgSign();
	                    if(StringUtils.isNotBlank(orgSign)) {
	                        //部门职位（本级）
	                        String mailListId_1 = getDefineMailListId(orgSign.toLowerCase(), jobSign.toLowerCase(), true);
	                        getDeleteMailListList(mailListList, mailListId_1);
	                        //部门职位（包含下级）
	                        String mailListId_2 = getDefineMailListId(orgSign.toLowerCase(), jobSign.toLowerCase(), false);
	                        getDeleteMailListList(mailListList, mailListId_2);
	                    }							
	                }
	                if(mailListList != null && mailListList.size() > 0 ) {
	                    try {
	                        mailHandler.deleteMailListList(mailListList, 0);			 
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    }
	}
	
	private void getDeleteMailListList(List<MailList> mailListList, String mailListId) {
		MailList mailList = new MailList();
		mailList.setMailListId(mailListId);  //部门+角色
		mailList.setDomainName(MAIL_DOMAIN_NAME);
		mailListList.add(mailList);
	}
	
	/**
	 * 统一实现定义邮件列表id
	 */
	private String getDefineMailListId(String deptId, String jobId, boolean limitedSameLevelUser) {
		String mailListId = null;
		if(StringUtils.isNotBlank(deptId)) {
			mailListId = deptId.toLowerCase();
			if(StringUtils.isNotBlank(jobId)){
				mailListId = mailListId + "_" + jobId.toLowerCase();
			}
//			if(limitedSameLevelUser == true) {
//				mailListId = mailListId + "_samelevel";
//			}
		}
		return mailListId;
	}
	/**
	 * 统一实现定义邮件列表名称
	 */
	private String getDefineMailListName(String deptName, String jobName, boolean limitedSameLevelUser) {
		String mailListName = null;
		if(StringUtils.isNotBlank(deptName)) {
			mailListName = deptName;
			if(StringUtils.isNotBlank(jobName)){
				mailListName = mailListName + "_" + jobName;
			}
//			if(limitedSameLevelUser == true) {
//				mailListName = mailListName + "_本级部门";
//			}
		}
		return mailListName;
				
	}
	
	//************************************************************************************要删掉
	private void deleteMailListList(Role role) {
		List<MailList> mailListList = new ArrayList<MailList>();
		String roleSign = role.getRoleSign();
		if(StringUtil.isNotBlank(roleSign)) {
			//查找所有分公司列表
			List<Organization> orgList = organizationDao.findAll();
			//邮件部门下对应角色的邮件列表
			for(Organization org : orgList) {
				String orgSign = org.getOrgSign();
				if((OrganizationType.BRENCH.equals(org.getOrgType()) || OrganizationType.GROUNP.equals(org.getOrgType())) && StringUtil.isNotBlank(orgSign)){  //分公司
					MailList mailList = new MailList();
					mailList.setMailListId(orgSign.toLowerCase() + "_" + roleSign.toLowerCase());  //部门+角色
					mailList.setDomainName(MAIL_DOMAIN_NAME);
					mailListList.add(mailList);
				}
						
			}
			//邮件组织下对应角色的邮件列表
			MailList mailList_2 = new MailList();
			mailList_2.setMailListId(roleSign.toLowerCase());  //角色
			mailList_2.setDomainName(MAIL_DOMAIN_NAME);
			mailListList.add(mailList_2);
			if(mailListList != null && mailListList.size() > 0 ) {
				try {
					mailHandler.deleteMailListList(mailListList, 0);			 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public List<MailInfoView> listMailInfos(String mailAddr, Integer skip, Integer limit) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailInfo mailInfo = new MailInfo();
		    mailInfo.setMailAddr(mailAddr);
		    mailInfo.setSkip(skip);
		    mailInfo.setLimit(limit);
		    try {
		        List<MailInfoView> mailList = mailHandler.listMailInfos(mailInfo);
		        if(mailList != null && mailList.size() > 0){
		            List<MailInfoView> newMailList = listNewMailInfos(mailAddr, 10000);
		            if(newMailList != null && newMailList.size() > 0){
		                for(MailInfoView view : mailList){
		                    String msId = view.getMid();
		                    for(MailInfoView newView : newMailList){
		                        if(msId.equals(newView.getMid())){
		                            view.setReadStatus("0");  //标记为未读
		                            break;
		                        }
		                    }
		                }
		            }
		            for(MailInfoView view : mailList){
		                String from = view.getFrom();
		                String fromMailAddr = from.substring(from.indexOf("<") + 1, from.length() -1);
		                User user = userService.findUserByMailAddr(fromMailAddr);
		                if (user != null) {
		                    List<UserDeptJob> deptJobList = userDeptJobDao.findDeptJobByUserId(user.getUserId());
		                    for (UserDeptJob udj : deptJobList) {
		                        if (udj.getIsMajorRole() == 0) {
		                            Organization org = organizationDao.findById(udj.getDeptId());
		                            view.setFromUserDeptName(org.getName());
		                        }
		                    }
		                }
		            }
		            return mailList;
		        }			
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		return null;
	}
	
	@Override
	public MailBoxMsg getMailCountInfo(User user) {
		getMailHandler();
//		if(this.isMailSysInUse()) {
		if (true) {
		    String mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
		    try {
		        return mailHandler.getMailBoxMsgByMailAddr(mailAddr);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		return null;
	}

	@Override
	public List<MailInfoView> listNewMailInfos(String mailAddr, Integer limit) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    MailInfo mailInfo = new MailInfo();
		    mailInfo.setMailAddr(mailAddr);
		    if(limit == null) {
		        limit = 20;
		    }
		    mailInfo.setLimit(limit);
		    try {
		        return mailHandler.getNewMailInfos(mailInfo);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		return null;
	}

	private void setMailSystemConfig() {
		SystemConfig findSc = new SystemConfig();
		findSc.setTag("coreMailOrgId");
		SystemConfig orgIdSc = systemConfigService.getSystemPath(findSc);
		MAIL_ORG_ID = orgIdSc.getValue();
		findSc.setTag("coreMailUserCosId");
		SystemConfig userCosSc = systemConfigService.getSystemPath(findSc);
		MAIL_COS_ID = Integer.parseInt(userCosSc.getValue());
		findSc.setTag("coreMailMailListCosId");
		SystemConfig listCosSc = systemConfigService.getSystemPath(findSc);
		MAIL_LIST_COS_ID = Integer.parseInt(listCosSc.getValue());
		findSc.setTag("coreMailDomain");
		SystemConfig domainSc = systemConfigService.getSystemPath(findSc);
		MAIL_DOMAIN_NAME = domainSc.getValue();
		findSc.setTag("coreMailUserPassWord");
		SystemConfig passwordSc = systemConfigService.getSystemPath(findSc);
		DEFAULT_PASSWORD = passwordSc.getValue();
		findSc.setTag("coreMailProviderId");
		SystemConfig providerIdSc = systemConfigService.getSystemPath(findSc);
		PROVIDER_ID = providerIdSc.getValue();
	}
	
	@Override
	public void mailAutoLogin(HttpServletRequest request,
			HttpServletResponse response, User user, Integer deviceType) {
		getMailHandler();
//		if(this.isMailSysInUse()) {
		if (true) {
		    String mailAddr = user.getMailAddr();
            if(StringUtils.isBlank(mailAddr)) {
                mailAddr = user.getAccount().toLowerCase() + "@" + MAIL_DOMAIN_NAME;
            }
		    try {
		        mailHandler.mailAutoLogin(request, response, mailAddr, deviceType);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
		    }
		}
		
	}
	
	/**
	 * 获取邮箱自动登录信息
	 */
	public String getMailAutoLoginInfo(HttpServletRequest request, HttpServletResponse response, String mailAddr, Integer loginDeviceType) {
		getMailHandler();
	    try {
	        return mailHandler.getMailAutoLoginInfo(request, response, mailAddr, loginDeviceType);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
	    }
	}
	
	
	
	@Override
	public boolean isMailSysInUse() {
		SystemConfig findSc = new SystemConfig();
		findSc.setTag("mailSysStatus");
		SystemConfig mailSysStatusConf = systemConfigService.getSystemPath(findSc);
		String mailSysStatus = mailSysStatusConf.getValue();
		//开启了邮件系统
		if(StringUtil.isNotBlank(mailSysStatus) && mailSysStatus.equals("0")) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	public boolean updateMailListNameOrStatus(String jobId, String newName,
			Integer newStatus) {
		// TODO Auto-generated method stub
		return false;
	}	
	
	/**
	 * 同步用户密码到邮箱系统
	 */
	@Override
	public void syncUserPassword() {
	    if(this.isMailSysInUse()) {
	        try {
	            List<User> userList = userService.getUserListHaveMail();
	            for (User user : userList) {
	                this.chageMailUserPassword(user);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
	        }
	    }
	}
	
	/**
	 * 获取外部联系人
	 */
	@Override
	public List<MailExternalContract> listMailExternalContract(String objUid, String mailAdd) {
		getMailHandler();
		if(this.isMailSysInUse()) {
		    try {
		        return mailHandler.listMailExternalContract(objUid, mailAdd);
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new ApplicationException(ErrorCode.MAIL_SYS_CONNECT_ERR);
		    }
		}
		return null;
	}
	
}


