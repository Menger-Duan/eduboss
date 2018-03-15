package com.eduboss.mail;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eduboss.mail.pojo.MailBoxMsg;
import com.eduboss.mail.pojo.MailExternalContract;
import com.eduboss.mail.pojo.MailInfoView;
import com.eduboss.mail.pojo.MailList;


/**@author wmy
 *@date 2015年9月24日上午10:57:02
 *@version 1.0 
 *@description
 */
public interface MailHandler {
	
	/**
	 * 部门操作
	 */
	public boolean addUnit(Object unitObj) throws Exception;
	
	public boolean updateUnit(Object unitObj) throws Exception;
	
	public boolean deleteUnit(Object unitObj) throws Exception;
	
	public String getTopUnitIds(String mailOrgId) throws Exception;
	
	public void removeAllUnit(String mailOrgId) throws Exception;
	
	public boolean unitExist(String mailOrgId, String unitId) throws Exception;
	
	 //邮箱用户操作
	
	public boolean addUser(Object userObj) throws Exception;
	
	public boolean updateUser(Object userObj) throws Exception;
    /**
     * 删除邮箱用户
     * preserveDays 删除后用户还可保留的天数
     */
	public boolean deleteUser(Object userObj, Integer preserveDays) throws Exception;
    /**
     * 获取用户邮箱信息
     * @param mailAddr
     * @return
     */
    public MailBoxMsg getMailBoxMsgByMailAddr(String mailAddr) throws Exception;
    
	/**
	 * 删除组织下某部门指定服务等级下的所有邮箱用户或邮件列表
	 */
    public void deleteUnitUsersByCosId(String mailOrgId, String orgUnitId, Integer cosId, Integer preserveDays) throws Exception;
    
    /**
     * 根据ID删除外部联系人
     */
    public boolean deleteMailExternalContractById(String objUid) throws Exception;
    
    /**
     * 创建外部联系人
     */
    public String createMailExternalContract(MailExternalContract contract) throws Exception;
    
    /**
     * 把指定部门的邮箱用户从原来部门移动到根组织下
     */
    public void moveAllUserToOrg(String mailOrgId, String unitId) throws Exception;
	
	/**
	 * 邮箱列表操作
	 */
    public boolean addMailList(Object mailListObj) throws Exception;
    
    public void addMailListList(List mailListList) throws Exception;
    
    public boolean updateMailList(Object mailListObj) throws Exception;
    
    public void updateMailListList(List mailListList) throws Exception;
    /**
     * 删除邮箱列表
     * preserveDays 删除后还可保留的天数
     */
    public boolean deleteMailList(Object mailListObj, Integer preserveDays) throws Exception;
    
	public void deleteMailListList(List mailListList, Integer preserveDays) throws Exception;
    //attrsKeys接收参数如：a&b&c,结果返回a=X&b=X&c=X
    public String getMailListInfoByAttrs(String mailAddr, String attrKey) throws Exception;
    
    /**
     * 获取邮件信息操作
     */
	public List<MailInfoView> listMailInfos(Object infoObj) throws Exception;  
	/**
	 * 获取用户未读信件列表
	 */
	public List<MailInfoView> getNewMailInfos(Object infoObj) throws Exception;
	
	/**
	 * 判断邮件地址是否存在
	 */
	public boolean mailAddrExist(String mailAddr) throws Exception;
	
	/**
	 * 自动登录邮箱
	 */
	public void mailAutoLogin(HttpServletRequest request, HttpServletResponse response, String mailAddr, Integer deviceType) throws Exception;
	
	/**
	 * 获取邮箱自动登录信息
	 */
	public String getMailAutoLoginInfo(HttpServletRequest request, HttpServletResponse response, String mailAddr, Integer deviceType) throws Exception;
	
    /**
     * 获取新的排序号
     */
	public Integer getNewRank(Integer originalRank);
	
	/**
	 * 获取外部联系人
	 * @param objUid
	 * @param mailAddr
	 * @return List<MailExternalContract>
	 */
	public List<MailExternalContract> listMailExternalContract(String objUid, String mailAddr) throws Exception;

    
}


