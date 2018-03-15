package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.TeacherSubjectVersionDao;
import com.eduboss.domain.TeacherSubjectVersion;
import com.eduboss.service.TeacherSubjectVersionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
@Service
public class TeacherSubjectVersionServiceImpl implements TeacherSubjectVersionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TeacherSubjectVersionDao teacherSubjectVersionDao;
	
	/**
	 * 查找月份有效的老师科目
	 */
	@Override
	public List<Map<Object, Object>> findTeacherSubjectVersionBySubject(
			String campusId, String subjectId, int versionMonth) {
		return teacherSubjectVersionDao.findTeacherSubjectVersionBySubject(campusId, subjectId, versionMonth);
	}

	/**
	 * 科组通过科目和月份版本查找老师名称
	 */
	@Override
	public String findAssociatedTeacherNameDes(String campusId, String subjectId, int versionMonth) {
		List<Map<Object, Object>> list = this.findTeacherSubjectVersionBySubject(campusId, subjectId, versionMonth);
		String teacherNameDes = "";
		if (list.size() > 0) {
			for (Map<Object, Object> map : list) {
				teacherNameDes += map.get("teacherName") + ",";
			}
			teacherNameDes = teacherNameDes.substring(0, teacherNameDes.length() - 1);
		}
		return teacherNameDes;
	}

	/**
	 * 保存或修改授课老师科目关联
	 */
	@Override
	public void editTeacherSubjectVersion(
			TeacherSubjectVersion teacherSubjectVersion) {
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		String currentTime = DateTools.getCurrentDateTime();
		teacherSubjectVersion.setModifyTime(currentTime);
		teacherSubjectVersion.setModifyUserId(currentUserId);
		if (teacherSubjectVersion.getId() > 0) {
			TeacherSubjectVersion teacherSubjectVersionInDb = teacherSubjectVersionDao.findById(teacherSubjectVersion.getId());
			teacherSubjectVersion.setCreateTime(teacherSubjectVersionInDb.getCreateTime());
			teacherSubjectVersion.setCreateUserId(teacherSubjectVersionInDb.getCreateUserId());
			teacherSubjectVersionDao.merge(teacherSubjectVersion);
		} else {
			teacherSubjectVersion.setCreateTime(currentTime);
			teacherSubjectVersion.setCreateUserId(currentUserId);
			teacherSubjectVersionDao.save(teacherSubjectVersion);
		}
	}

	/**
	 * 删除授课老师科目关联
	 */
	@Override
	public void deleteTeacherSubjectVersion(
			TeacherSubjectVersion teacherSubjectVersion) {
		teacherSubjectVersionDao.delete(teacherSubjectVersion);
	}

	/**
	 * 根据老师和年级获取当前可教老师版本
	 */
	@Override
	public List<TeacherSubjectVersion> getCanTaughtSubjectByTeacherAndGrade(
			String teacherId, String gradeId) {
		return teacherSubjectVersionDao.getCanTaughtSubjectByTeacherAndGrade(teacherId, gradeId);
	}
	
}
