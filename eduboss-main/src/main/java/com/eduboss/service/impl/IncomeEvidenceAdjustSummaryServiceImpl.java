package com.eduboss.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.IncomeEvidenceAdjustSummaryDao;
import com.eduboss.domain.IncomeEvidenceAdjustItem;
import com.eduboss.domain.IncomeEvidenceAdjustSummary;
import com.eduboss.domainVo.IncomeEvidenceAdjustItemVo;
import com.eduboss.domainVo.IncomeEvidenceAdjustSummaryVo;
import com.eduboss.service.IncomeEvidenceAdjustItemService;
import com.eduboss.service.IncomeEvidenceAdjustSummaryService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Service
public class IncomeEvidenceAdjustSummaryServiceImpl implements IncomeEvidenceAdjustSummaryService {
	
	@Autowired
	private IncomeEvidenceAdjustSummaryDao incomeEvidenceAdjustSummaryDao;
	
	@Autowired
	private IncomeEvidenceAdjustItemService incomeEvidenceAdjustItemService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存调整项
	 * @param item
	 * @return
	 */
	@Override
	public IncomeEvidenceAdjustItem editIncomeAdjustItem(IncomeEvidenceAdjustItemVo itemVo) {
		String currentTime = DateTools.getCurrentDateTime();
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		IncomeEvidenceAdjustItem item = HibernateUtils.voObjectMapping(itemVo, IncomeEvidenceAdjustItem.class);
		if (StringUtils.isBlank(itemVo.getId())) {
			if (StringUtils.isBlank(itemVo.getSummaryId())) {
				IncomeEvidenceAdjustSummary newSummary = new IncomeEvidenceAdjustSummary();
				newSummary.setEvidenceId(itemVo.getEvidenceId());
				newSummary.setCreateTime(currentTime);
				newSummary.setCreateUserId(currentUserId);
				newSummary.setModifyTime(currentTime);
				newSummary.setModifyUserId(currentUserId);
				incomeEvidenceAdjustSummaryDao.save(newSummary);
				item.setSummary(newSummary);
			}
			item.setCreateTime(currentTime);
			item.setCreateUserId(currentUserId);
		} else {
			IncomeEvidenceAdjustItemVo itemInDbVo = incomeEvidenceAdjustItemService.findItemVoById(itemVo.getId());
			item.setCreateTime(itemInDbVo.getCreateTime());
			item.setCreateUserId(itemInDbVo.getCreateUserId());
		}
		item.setModifyTime(currentTime);
		item.setModifyUserId(currentUserId);
		BigDecimal adjustTotalAmount = BigDecimal.ZERO;
		incomeEvidenceAdjustItemService.saveIncomeEvidenceAdjustItem(item);
		incomeEvidenceAdjustSummaryDao.flush();
		IncomeEvidenceAdjustSummary summary = incomeEvidenceAdjustSummaryDao.findById(item.getSummary().getId());
		if (summary.getAdjustItems() != null && summary.getAdjustItems().size() > 0) {
			for (IncomeEvidenceAdjustItem itemEach : summary.getAdjustItems()) {
				adjustTotalAmount = adjustTotalAmount.add(itemEach.getAdjustAmount());
			}
		} else {
			adjustTotalAmount = adjustTotalAmount.add(item.getAdjustAmount());
		}
		
		summary.setAdjustTotalAmount(adjustTotalAmount);
		summary.setModifyTime(currentTime);
		summary.setModifyUserId(currentUserId);
		incomeEvidenceAdjustSummaryDao.merge(summary);
		return item;
	}
	
	/**
	 * 删除调整项
	 */
	@Override
	public void deleteIncomeAdjustItem(String itemId) {
		String currentTime = DateTools.getCurrentDateTime();
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		IncomeEvidenceAdjustItem item = incomeEvidenceAdjustItemService.findItemById(itemId);
		IncomeEvidenceAdjustSummary summary = incomeEvidenceAdjustSummaryDao.findById(item.getSummary().getId());
		BigDecimal adjustTotalAmount = summary.getAdjustTotalAmount() != null ? summary.getAdjustTotalAmount() : BigDecimal.ZERO;
		summary.setAdjustTotalAmount(adjustTotalAmount.subtract(item.getAdjustAmount()));
		summary.setModifyTime(currentTime);
		summary.setModifyUserId(currentUserId);
		incomeEvidenceAdjustSummaryDao.merge(summary);
		incomeEvidenceAdjustItemService.deleteItem(item);
	}

	/**
	 * 保存调整概要
	 */
	@Override
	public void editIncomeAdjustSummary(IncomeEvidenceAdjustSummary summary) {
		String currentTime = DateTools.getCurrentDateTime();
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		if (StringUtils.isBlank(summary.getId())) {
			summary.setId(null);
			summary.setCreateTime(currentTime);
			summary.setCreateUserId(currentUserId);
		} else {
			IncomeEvidenceAdjustSummary summaryInDb = incomeEvidenceAdjustSummaryDao.findById(summary.getId());
			summary.setAdjustTotalAmount(summaryInDb.getAdjustTotalAmount());
			summary.setCreateTime(summaryInDb.getCreateTime());
			summary.setCreateUserId(summaryInDb.getCreateUserId());
		}
		summary.setModifyTime(currentTime);
		summary.setModifyUserId(currentUserId);
		if (StringUtils.isBlank(summary.getId())) {
			incomeEvidenceAdjustSummaryDao.save(summary);
		} else {
			incomeEvidenceAdjustSummaryDao.merge(summary);
		}
	}
	
	/**
	 * 查找调整概要
	 */
	@Override
	public IncomeEvidenceAdjustSummaryVo findIncomeAdjustSummaryById(String summaryId) {
		IncomeEvidenceAdjustSummary summary = incomeEvidenceAdjustSummaryDao.findById(summaryId);
		IncomeEvidenceAdjustSummaryVo summaryVo = HibernateUtils.voObjectMapping(summary, IncomeEvidenceAdjustSummaryVo.class);
		summaryVo.setAdjustItems(HibernateUtils.voCollectionMapping(summary.getAdjustItems(), IncomeEvidenceAdjustItemVo.class));
		return summaryVo;
	}
	
}
