package com.eduboss.service.impl;

import java.util.List;

import org.boss.rpc.eduboss.service.dto.TeacherSearchRpcVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.RecommendStatus;
import com.eduboss.dao.UserTeacherAttributeDao;
import com.eduboss.domain.UserTeacherAttribute;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.UserTeacherAttributeService;

@Service
public class UserTeacherAttributeServiceImpl implements UserTeacherAttributeService {

	@Autowired
	private UserTeacherAttributeDao userTeacherAttributeDao;
	
	@Override
	public DataPackage listPageUserTeacherAttribute(TeacherSearchRpcVo teacher) {
		DataPackage dp = new DataPackage(teacher.getPageNo(), teacher.getPageSize());
		return userTeacherAttributeDao.listPageUserTeacherAttribute(teacher, dp);
	}

	@Override
	public void recommendTeacher(String teacherId,
			RecommendStatus recommendStatus) {
		UserTeacherAttribute userTeacherAttribute = userTeacherAttributeDao.findById(Integer.parseInt(teacherId));
		userTeacherAttribute.setRecommendStatus(recommendStatus);
		userTeacherAttributeDao.merge(userTeacherAttribute);
	}

	@Override
	public Integer getRecommendTeacherCount(String blBrenchId) {
		return userTeacherAttributeDao.getRecommendTeacherCount(blBrenchId);
	}

    @Override
    public List<UserTeacherAttribute> listRecommendTeachers(String blBrenchId, Integer limit) {
        return userTeacherAttributeDao.listRecommendTeachers(blBrenchId, limit);
    }

    @Override
    public UserTeacherAttribute findUserTeacherAttributeById(String id) {
        return userTeacherAttributeDao.findById(Integer.parseInt(id));
    }

    @Override
    public List<UserTeacherAttribute> listTeachersByNames(String[] teacherNames) {
        return userTeacherAttributeDao.listTeachersByNames(teacherNames);
    }

}
