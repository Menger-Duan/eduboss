package com.eduboss.service;

import java.util.List;

import com.eduboss.domainVo.SubjectGroupSummaryVo;
import com.eduboss.domainVo.SubjectGroupVo;
import com.eduboss.dto.SelectOptionResponse;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
public interface SubjectGroupService {

	/**
	 * 保存或修改科组
	 * @param subjectGroupVo
	 * @param isUpToNextMonth 1：更新到下个月，0不更新到下个月
	 * @param subjectGroups 移动的科组
	 */
	void editSubjectGroup(SubjectGroupVo subjectGroupVo);
	
	/**
	 * 删除科组
	 * @param subjectGroupVo
	 */
	void deleteSubjectGroup(SubjectGroupVo subjectGroupVo);
	
	/**
	 * 移动科组
	 * @param id
	 * @param type 1：向下移，-1：向上移
	 */
	void moveSubjectGroup(int id, int type);
	
	/**
	 * 按校区版本查询科组列表概要
	 * @param brenchId
	 * @param campusId
	 * @param version
	 * @return
	 */
	SubjectGroupSummaryVo updateAndgetSubjectGroupSummary(String brenchId, String campusId, int version);
	
	/**
	 * 根据id查找科组
	 * @param groupId
	 * @return
	 */
	SubjectGroupVo findSubjectGroup(int subjectGroupId);
	
	/**
	 * 根据科组ID查询可转移科组
	 * @param groupId
	 * @return
	 */
	List<SubjectGroupVo> getTransferGroupSubjects(int groupId);
	
	/**
	 * 根据科目ID，版本查询科组名称
	 * @param subjectId
	 * @param version
	 * @return
	 */
	String findSubjectGroupName(String subjectId, String campusId, String versionDate, String teacherId);
	
	/**
	 * 根据校区ID查询科组下拉选择
	 * @param campusId
	 * @return
	 */
	SelectOptionResponse getSubjectGroupForSelection(String campusId);
	
}
