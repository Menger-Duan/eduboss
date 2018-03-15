package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.ClassroomManage;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.dto.DataPackage;

public interface ClassroomManageService {

	/**
	 * 查询
	 * @param dataPackage
	 * @param classroomManageVo
	 * @param params
	 * @return
	 */
	DataPackage getClassroomManageList(DataPackage dataPackage,
			ClassroomManageVo classroomManageVo, Map<String, Object> params);

	/**
	 * 新增信息
	 * @param classroomManageVo
	 */
	void saveClassroomManage(ClassroomManageVo classroomManageVo);

	/**
	 * 查找1条信息
	 * @param id
	 * @return
	 */
	ClassroomManageVo findClassroomById(String id);

	/**
	 * 修改
	 * @param classroomManageVo
	 */
	void modifyClassroomManage(ClassroomManageVo classroomManageVo);

	/**
	 * 删除
	 * @param id
	 */
	void deleteClassroomManage(String id);

	/**
	 * 自动查找教室
	 * @param input
	 * @param checkType
	 * @return
	 */
	List<ClassroomManage> getClassroomAutoComplate(String input,String checkType);
	List<ClassroomManage> getClassroomAutoComplate(String checkType);
	List<ClassroomManage> getClassroomByCampus(String campusId);

	/**
	 * 以教室名称找到教室ID（不安全，如果存在同名教室只获取集合的第一个）
	 * @param classroomName
	 * @return
	 */
	String getClassroomIdByName(String classroomName);

    List getAllCurrentClassroom(String[] campusId, String brenchId);
}
