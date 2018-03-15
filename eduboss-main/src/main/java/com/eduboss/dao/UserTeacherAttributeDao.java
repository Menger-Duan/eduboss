package com.eduboss.dao;

import java.util.List;

import org.boss.rpc.eduboss.service.dto.TeacherSearchRpcVo;
import org.springframework.stereotype.Repository;

import com.eduboss.domain.UserTeacherAttribute;
import com.eduboss.dto.DataPackage;

/**
 * Created by Administrator on 2017/5/19.
 */
@Repository
public interface UserTeacherAttributeDao extends GenericDAO<UserTeacherAttribute, Integer>  {
	
	DataPackage listPageUserTeacherAttribute(TeacherSearchRpcVo teacher, DataPackage dp);
	
	Integer getRecommendTeacherCount(String blBrenchId);
	
	List<UserTeacherAttribute> listRecommendTeachers(String blBrenchId, Integer limit);
	
	List<UserTeacherAttribute> listTeachersByNames(String[] teacherNames);
	
}
