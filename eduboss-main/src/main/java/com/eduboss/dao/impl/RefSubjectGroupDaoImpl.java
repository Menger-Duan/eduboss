package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.RefSubjectGroupDao;
import com.eduboss.domain.RefSubjectGroup;
import com.eduboss.domainVo.RefSubjectGroupVo;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
@Repository
public class RefSubjectGroupDaoImpl extends GenericDaoImpl<RefSubjectGroup, Integer> implements RefSubjectGroupDao {

	/**
	 * 查询科组科目关联列表
	 */
	@Override
	public List<RefSubjectGroup> getRefSubjectGroupList(
			RefSubjectGroupVo refSubjectGroupVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from RefSubjectGroup ";
		hql += " where 1=1 ";
		if (refSubjectGroupVo.getSubjectGroupId() > 0) {
			hql += " and subjectGroup.id = :subjectGroupId ";
			params.put("subjectGroupId", refSubjectGroupVo.getSubjectGroupId());
		}
		if (StringUtils.isNotBlank(refSubjectGroupVo.getBlCampusId())) {
			hql += " and subjectGroup.blCampus.id = :blCampusId ";
			params.put("blCampusId", refSubjectGroupVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(refSubjectGroupVo.getSubjectId())) {
			hql += " and subject.id = :subjectId ";
			params.put("subjectId", refSubjectGroupVo.getSubjectId());
		}
		if (refSubjectGroupVo.getVersion() > 0) {
			hql += " and version = :version ";
			params.put("version", refSubjectGroupVo.getVersion());
		}
		return super.findAllByHQL(hql, params);
	}

	
	
}
