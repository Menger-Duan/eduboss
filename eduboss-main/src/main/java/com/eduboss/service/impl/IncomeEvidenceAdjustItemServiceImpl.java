package com.eduboss.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.IncomeEvidenceAdjustItemDao;
import com.eduboss.domain.IncomeEvidenceAdjustItem;
import com.eduboss.domainVo.IncomeEvidenceAdjustItemVo;
import com.eduboss.service.IncomeEvidenceAdjustItemService;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Service
public class IncomeEvidenceAdjustItemServiceImpl implements IncomeEvidenceAdjustItemService {
	
	@Autowired
	private IncomeEvidenceAdjustItemDao incomeEvidenceAdjustItemDao;

	/**
	 * 根据summaryId删除概要关联的所有调整项
	 */
	@Override
	public void deleteItemsBySummaryId(String summaryId) {
		incomeEvidenceAdjustItemDao.deleteItemsBySummaryId(summaryId);
	}
	
	/**
	 * 删除调整项
	 */
	@Override
	public void deleteItem(IncomeEvidenceAdjustItem item) {
		incomeEvidenceAdjustItemDao.delete(item);
	}
	
	/**
	 * 保存调整项
	 */
	@Override
	public void saveIncomeEvidenceAdjustItem(IncomeEvidenceAdjustItem item) {
		if (StringUtils.isNotBlank(item.getId())) {
			incomeEvidenceAdjustItemDao.merge(item);
		} else {
			incomeEvidenceAdjustItemDao.save(item);
		}
	}
	
	/**
	 * 根据id查找IncomeEvidenceAdjustItemVo
	 */
	@Override
	public IncomeEvidenceAdjustItemVo findItemVoById(String id) {
		return HibernateUtils.voObjectMapping(incomeEvidenceAdjustItemDao.findById(id), IncomeEvidenceAdjustItemVo.class);
	}
	
	/**
	 * 根据id查找IncomeEvidenceAdjustItem
	 */
	@Override
	public IncomeEvidenceAdjustItem findItemById(String id) {
		return incomeEvidenceAdjustItemDao.findById(id);
	}
}
