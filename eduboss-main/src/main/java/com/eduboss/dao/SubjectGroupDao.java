package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.SubjectGroup;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
public interface SubjectGroupDao extends GenericDAO<SubjectGroup, Integer> {

	/**
	 * 查找科组列表
	 * @param brenchId
	 * @param campusId
	 * @param version
	 * @param name
	 * @return
	 */
	List<SubjectGroup> getSubjectGroupList(String brenchId, String campusId, int version, String name);
	
	/**
	 * 计算科组列表
	 * @param brenchId
	 * @param campusId
	 * @param version
	 * @param name
	 * @return
	 */
	int countSubjectGroupList(String brenchId, String campusId, int version, String name);
	
	/**
	 * 根据科组ids清空他们的科目描述
	 * @param groupIds
	 */
	void updateSubjectGroupSubjectDesByIds(String[] groupIds);
	
}
