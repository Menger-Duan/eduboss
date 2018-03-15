package com.eduboss.service;

import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.MobileType;
import com.eduboss.common.MobileUserType;
import com.eduboss.domain.MobileUser;
import com.eduboss.domainVo.MobileUserVo;

public interface MobileUserService {

	/**
	 * 根据学生ID 查找 有无 移动账户， 如果不存在此ID， 就会新建一个
	 * @param studentId
	 * @return
	 */
	public MobileUser findMobileUserByStuId(String studentId);

	/**
	 * 根据用户ID 查找 有无 移动账户， 如果不存在此ID， 就会新建一个
	 * @param userId
	 * @return
	 */
	public MobileUser findMobileUserByStaffId(String userId);

	/**
	 * 更新 用户的 mobile 信息
	 * @param mobileUserVo
	 */
	public void updateChannelInfo(MobileUserVo mobileUserVo);

	/**
	 * 保存用户头像
	 * @param myfile1
	 * @param mobileUserId
	 * @param servicePath
	 */
	public void saveHeaderImgFile(MultipartFile myfile1,String mobileUserId,String servicePath);
	
	/**
	 * 根据学生ID 查找 有无 移动账户
	 * @param id
	 * @return
	 */
	public MobileUser findMobileUserById(String id);
	
	/**
	 * 保存公告记录到MOBILE_PUSH_MSG_SESSION_USER	
	 * @param noticeId
	 * @param sessionId
	 */
	@Deprecated
	public void saveSessionUser(String noticeId,String sessionId);

	public void saveSessionUserNew(String noticeId,String sessionId);
	
	/**
	 * 根据用户Id 和 用户类型查找用户
	 * @param userId
	 * @param mobileType
	 * @return
	 */
	public MobileUser findMobileUserByStaffId(String userId,MobileUserType mobileUserType);
}
