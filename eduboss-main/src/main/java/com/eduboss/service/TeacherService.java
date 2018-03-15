package com.eduboss.service;

import java.util.Map;

import com.eduboss.dto.DataPackage;

/**
 * 
 * @description 将涉及到teacher用户的方法从userService中抽离出来
 * @author xiaojinwang
 * 
 */
public interface TeacherService {
	/**
	 * 开放给教育平台接口 教师列表
	 * @param dataPackage 封装 pageNum pageSize
	 * @param teacherId
	 * @return
	 */
	@Deprecated
    Map<String, Object> getTeacherInfo(DataPackage dataPackage,String teacherId,String phone,String name);
	Map<String, Object> getTeacherInfoNew(DataPackage dataPackage,String teacherId,String phone,String name);


	/**
	 *
	 * @param studentId
	 * @param subject
	 * @param map
	 * @return
	 */
    Map<String,Object> getTeacherByStudentIdAndSubject(String studentId, String subject,Map<String, Object> map);
}
