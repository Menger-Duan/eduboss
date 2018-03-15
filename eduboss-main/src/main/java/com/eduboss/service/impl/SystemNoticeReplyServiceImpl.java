package com.eduboss.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.SystemNoticeReplyDao;
import com.eduboss.dao.SystemNoticeUserDao;
import com.eduboss.domain.SystemNoticeReply;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SystemNoticeReplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.SystemNoticeReplyService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

@Service
public class SystemNoticeReplyServiceImpl implements SystemNoticeReplyService{
	@Autowired
	private SystemNoticeReplyDao systemNoticeReplyDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SystemNoticeUserDao systemNoticeUserDao;
	
	@Autowired
	private OrganizationDao organizationDao;

	/**
	 * 新增或修改公告回复（暂时只新增）
	 * @throws IOException 
	 * */
	@Override
	public void saveOrEditSystemNoticeReply(SystemNoticeReply sNoticeReply) {
		SystemNoticeReply systemNoticeReply = new SystemNoticeReply();
		User user = new User();
		if(StringUtil.isNotBlank(sNoticeReply.getId())){
//			systemNoticeVo.setModifyUserId(userService.getCurrentLoginUser().getUserId());
			systemNoticeReply = systemNoticeReplyDao.findById(sNoticeReply.getId());
			systemNoticeReply.setContent(sNoticeReply.getContent());
			user.setUserId(userService.getCurrentLoginUser().getUserId());
			systemNoticeReply.setUser(user);
			systemNoticeReply.setCreateTime(DateTools.getCurrentDateTime());
		}else{
//			systemNoticeVo.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			systemNoticeReply = sNoticeReply;
			user.setUserId(userService.getCurrentLoginUser().getUserId());
			systemNoticeReply.setUser(user);
			systemNoticeReply.setCreateTime(DateTools.getCurrentDateTime());
			systemNoticeReply.setId(null);
		}
		systemNoticeReplyDao.save(systemNoticeReply);
		
	}

	@Override
	public DataPackage getSystemNoticeReplyList(
			SystemNoticeReplyVo systemNoticeReplyVo, DataPackage dp) {
		return systemNoticeReplyDao.getSystemNoticeReplyList(systemNoticeReplyVo, dp);
	}

	@Override
	public void deleteSystemNoticeReply(SystemNoticeReply reply) {
		User user=userService.getCurrentLoginUser();
		if("SUPER_ADMIN".equals(user.getRoleCode()) || user.getUserId().equals(reply.getUser().getUserId())){
			systemNoticeReplyDao.delete(reply);
		}
		else{
			throw new ApplicationException("无权限操作");
		}
	}
}
