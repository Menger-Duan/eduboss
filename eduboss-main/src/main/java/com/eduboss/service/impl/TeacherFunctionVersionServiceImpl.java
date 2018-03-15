package com.eduboss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.TeacherFunctionVersionDao;
import com.eduboss.domain.TeacherFunctionVersion;
import com.eduboss.service.TeacherFunctionVersionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

/**
 * 2016-12-22
 * @author lixuejun
 *
 */
@Service
public class TeacherFunctionVersionServiceImpl implements TeacherFunctionVersionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeacherFunctionVersionDao teacherFunctionVersionDao;
	
	/**
	 * 保存或修改教师职能关联
	 */
	@Override
	public void editTeacherFunctionVersion(
			TeacherFunctionVersion teacherFunctionVersion) {
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		String currentTime = DateTools.getCurrentDateTime();
		teacherFunctionVersion.setModifyTime(currentTime);
		teacherFunctionVersion.setModifyUserId(currentUserId);
		if (teacherFunctionVersion.getId() > 0) {
			TeacherFunctionVersion teacherFunctionVersionInDb = 
					teacherFunctionVersionDao.findById(teacherFunctionVersion.getId());
			teacherFunctionVersion.setCreateTime(teacherFunctionVersionInDb.getCreateTime());
			teacherFunctionVersion.setCreateUserId(teacherFunctionVersionInDb.getCreateUserId());
			teacherFunctionVersionDao.merge(teacherFunctionVersion);
		} else {
			teacherFunctionVersion.setCreateTime(currentTime);
			teacherFunctionVersion.setCreateUserId(currentUserId);
			teacherFunctionVersionDao.save(teacherFunctionVersion);
		}
	}

	/**
	 * 删除教师职能关联
	 */
	@Override
	public void deleteTeacherFunctionVersion(
			TeacherFunctionVersion teacherFunctionVersion) {
		teacherFunctionVersionDao.delete(teacherFunctionVersion);
	}

}
