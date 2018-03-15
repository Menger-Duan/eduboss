package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.UserWorkType;
import com.eduboss.common.RoleCode;
import com.eduboss.domain.MobilePushMsgSession;
import com.eduboss.domain.MobileUser;
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

/**
 * @author Administrator
 *
 */
@Repository
public interface MobileUserDao extends GenericDAO< MobileUser, String> {

	
	/**
	 * 根据学生ID 查找 有无 移动账户 
	 * @param studentId
	 * @return
	 */
	public MobileUser findMobileUserByStuId(String studentId);

	/**
	 * 根据用户ID 查找 有无 移动账户
	 * @param userId
	 * @return
	 */
	public MobileUser findMobileUserByUserId(String userId);
	
	/**
	 * 根据用户ID 查找 有无 移动账户
	 * @param Id
	 * @return
	 */
	public MobileUser findMobileUserById(String id);
	
	
}
