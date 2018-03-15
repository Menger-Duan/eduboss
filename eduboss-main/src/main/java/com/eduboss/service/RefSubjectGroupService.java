package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.RefSubjectGroup;
import com.eduboss.domainVo.RefSubjectGroupVo;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
public interface RefSubjectGroupService {

	/**
	 * 保存或修改RefSubjectGroup
	 * @param refSubjectGroup
	 */
	void editRefSubjectGroup(RefSubjectGroup refSubjectGroup);
	
	/**
	 * 删除RefSubjectGroup
	 * @param refSubjectGroup
	 */
	void deleteRefSubjectGroup(RefSubjectGroup refSubjectGroup);
	
	/**
	 * 根据id查找RefSubjectGroup
	 * @param id
	 * @return
	 */
	RefSubjectGroup findRefSubjectGroupById(int id);
	
	/**
	 * 查询科组科目关联列表
	 * @param refSubjectGroupVo
	 * @return
	 */
	List<RefSubjectGroup> getRefSubjectGroupList(RefSubjectGroupVo refSubjectGroupVo);
	
}
