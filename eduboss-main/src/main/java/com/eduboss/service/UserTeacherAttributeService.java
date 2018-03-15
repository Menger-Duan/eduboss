package com.eduboss.service;

import java.util.List;

import org.boss.rpc.eduboss.service.dto.TeacherSearchRpcVo;

import com.eduboss.common.RecommendStatus;
import com.eduboss.domain.UserTeacherAttribute;
import com.eduboss.dto.DataPackage;

public interface UserTeacherAttributeService {

	DataPackage listPageUserTeacherAttribute(TeacherSearchRpcVo teacher);
	
	void recommendTeacher(String teacherId, RecommendStatus recommendStatus);
	
	Integer getRecommendTeacherCount(String blBrenchId);
	
	List<UserTeacherAttribute> listRecommendTeachers(String blBrenchId, Integer limit);
	
	UserTeacherAttribute findUserTeacherAttributeById(String id);
	
	List<UserTeacherAttribute> listTeachersByNames(String[] teacherNames);
	
}
