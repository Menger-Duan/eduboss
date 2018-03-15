package com.eduboss.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.RemainEvidenceAdjustItemDao;
import com.eduboss.domain.RemainEvidenceAdjustItem;
import com.eduboss.domainVo.RemainEvidenceAdjustItemVo;
import com.eduboss.service.RemainEvidenceAdjustItemService;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Service
public class RemainEvidenceAdjustItemServiceImpl implements RemainEvidenceAdjustItemService {
	
	@Autowired
	private RemainEvidenceAdjustItemDao remainEvidenceAdjustItemDao;

	/**
	 * 根据summaryId删除概要关联的所有调整项
	 */
	@Override
	public void deleteItemsBySummaryId(String summaryId) {
		remainEvidenceAdjustItemDao.deleteItemsBySummaryId(summaryId);
	}
	
	/**
	 * 删除调整项
	 */
	@Override
	public void deleteItem(RemainEvidenceAdjustItem item) {
		remainEvidenceAdjustItemDao.delete(item);
	}
	
	/**
	 * 保存调整项
	 */
	@Override
	public void saveRemainEvidenceAdjustItem(RemainEvidenceAdjustItem item) {
		if (StringUtils.isNotBlank(item.getId())) {
			remainEvidenceAdjustItemDao.merge(item);
		} else {
			remainEvidenceAdjustItemDao.save(item);
		}
	}
	
	/**
	 * 根据id查找RemainEvidenceAdjustItemVo
	 */
	@Override
	public RemainEvidenceAdjustItemVo findItemVoById(String id) {
		return HibernateUtils.voObjectMapping(remainEvidenceAdjustItemDao.findById(id), RemainEvidenceAdjustItemVo.class);
	}
	
	/**
	 * 根据id查找IncomeEvidenceAdjustItem
	 */
	@Override
	public RemainEvidenceAdjustItem findItemById(String id) {
		return remainEvidenceAdjustItemDao.findById(id);
	}
}
