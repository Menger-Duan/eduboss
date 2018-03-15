package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.RefSubjectGroup;
import com.eduboss.domainVo.RefSubjectGroupVo;

/**
 * 2016-12-15
 * @author lixuejun
 *
 */
public interface RefSubjectGroupDao extends GenericDAO<RefSubjectGroup, Integer> {

	/**
	 * 查询科组科目关联列表
	 * @param refSubjectGroupVo
	 * @return
	 */
	List<RefSubjectGroup> getRefSubjectGroupList(RefSubjectGroupVo refSubjectGroupVo);
	
}
