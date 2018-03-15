package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SubjectGroupDao;
import com.eduboss.domain.SubjectGroup;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
@Repository
public class SubjectGroupDaoImpl extends GenericDaoImpl<SubjectGroup, Integer> implements SubjectGroupDao {

	/**
	 * 查找科组列表
	 */
	@Override
	public List<SubjectGroup> getSubjectGroupList(String brenchId,
			String campusId, int version, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from SubjectGroup where 1=1 ";
		if (StringUtils.isNotBlank(brenchId)) {
			hql += " and blBrench.id = :brenchId ";
			params.put("brenchId", brenchId);
		}
		if (StringUtils.isNotBlank(campusId)) {
			hql += " and blCampus.id = :campusId ";
			params.put("campusId", campusId);
		}
		if (version > 0) {
			hql += " and version = :version ";
			params.put("version", version);
		}
		if (StringUtils.isNotBlank(name)) {
			hql += " and name.name = :name ";
			params.put("name", name);
		}
		return super.findAllByHQL(hql, params);
	}

	/**
	 * 计算科组列表
	 */
	@Override
	public int countSubjectGroupList(String brenchId, String campusId,
			int version, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select count(*) from SubjectGroup where 1=1 ";
		if (StringUtils.isNotBlank(brenchId)) {
			hql += " and blBrench.id = :brenchId ";
			params.put("brenchId", brenchId);
		}
		if (StringUtils.isNotBlank(campusId)) {
			hql += " and blCampus.id = :campusId ";
			params.put("campusId", campusId);
		}
		if (version > 0) {
			hql += " and version = :version ";
			params.put("version", version);
		}
		if (StringUtils.isNotBlank(name)) {
			hql += " and name.id = :name ";
			params.put("name", name);
		}
		return super.findCountHql(hql, params);
	}

	/**
	 * 根据科组ids清空他们的科目描述
	 */
	@Override
	public void updateSubjectGroupSubjectDesByIds(String[] groupIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (groupIds.length > 0) {
			String sql = " update subject_group set SUBJECT_DES = '' where 1=1 ";
			if (groupIds.length > 1) {
				sql += " and id in(:groupIds) ";
				params.put("groupIds", groupIds);
			} else {
				sql += " and id = :groupId ";
				params.put("groupId", groupIds[0]);
			}
			super.excuteSql(sql, params);
		}
	}

}
