package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.RefSubjectGroupDao;
import com.eduboss.domain.RefSubjectGroup;
import com.eduboss.domainVo.RefSubjectGroupVo;
import com.eduboss.service.RefSubjectGroupService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

/**
 * 2016-12-17
 * @author lixuejun
 *
 */
@Service
public class RefSubjectGroupServiceImpl implements RefSubjectGroupService {
	
	@Autowired
	private RefSubjectGroupDao refSubjectGroupDao;
	
	@Autowired
	private UserService userService;

	@Override
	public void editRefSubjectGroup(RefSubjectGroup refSubjectGroup) {
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		String currentTime = DateTools.getCurrentDateTime();
		refSubjectGroup.setModifyTime(currentTime);
		refSubjectGroup.setModifyUserId(currentUserId);
		if (refSubjectGroup.getId() > 0) {
			RefSubjectGroup rsgInDb = refSubjectGroupDao.findById(refSubjectGroup.getId());
			refSubjectGroup.setCreateTime(rsgInDb.getCreateTime());
			refSubjectGroup.setCreateUserId(rsgInDb.getCreateUserId());
			refSubjectGroupDao.merge(refSubjectGroup);
		} else {
			refSubjectGroup.setCreateTime(currentTime);
			refSubjectGroup.setCreateUserId(currentUserId);
			refSubjectGroupDao.save(refSubjectGroup);
		}
		
	}

	/**
	 * 删除RefSubjectGroup
	 */
	@Override
	public void deleteRefSubjectGroup(RefSubjectGroup refSubjectGroup) {
		refSubjectGroupDao.delete(refSubjectGroup);
	}
	
	/**
	 * 根据id查找RefSubjectGroup
	 */
	@Override
	public RefSubjectGroup findRefSubjectGroupById(int id) {
		return refSubjectGroupDao.findById(id);
	}

	/**
	 * 查询科组科目关联列表
	 */
	@Override
	public List<RefSubjectGroup> getRefSubjectGroupList(
			RefSubjectGroupVo refSubjectGroupVo) {
		return refSubjectGroupDao.getRefSubjectGroupList(refSubjectGroupVo);
	}

}
