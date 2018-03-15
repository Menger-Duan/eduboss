package com.eduboss.service.impl;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.RemainEvidenceAdjustSummaryDao;
import com.eduboss.domain.RemainEvidenceAdjustItem;
import com.eduboss.domain.RemainEvidenceAdjustSummary;
import com.eduboss.domainVo.RemainEvidenceAdjustItemVo;
import com.eduboss.domainVo.RemainEvidenceAdjustSummaryVo;
import com.eduboss.service.RemainEvidenceAdjustItemService;
import com.eduboss.service.RemainEvidenceAdjustSummaryService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Service
public class RemainEvidenceAdjustSummaryServiceImpl implements RemainEvidenceAdjustSummaryService {
	
	@Autowired
	private RemainEvidenceAdjustSummaryDao remainEvidenceAdjustSummaryDao;
	
	@Autowired
	private RemainEvidenceAdjustItemService remainEvidenceAdjustItemService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 保存调整项
	 * @param item
	 * @return
	 */
	@Override
	public synchronized RemainEvidenceAdjustItem editRemainAdjustItem(RemainEvidenceAdjustItemVo itemVo) {
		String currentTime = DateTools.getCurrentDateTime();
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		RemainEvidenceAdjustItem item = HibernateUtils.voObjectMapping(itemVo, RemainEvidenceAdjustItem.class);
		if (StringUtils.isBlank(itemVo.getId())) {
			if (StringUtils.isBlank(itemVo.getSummaryId())) {
				RemainEvidenceAdjustSummary newSummary = new RemainEvidenceAdjustSummary();
				newSummary.setEvidenceId(itemVo.getEvidenceId());
				newSummary.setCreateTime(currentTime);
				newSummary.setCreateUserId(currentUserId);
				newSummary.setModifyTime(currentTime);
				newSummary.setModifyUserId(currentUserId);
				remainEvidenceAdjustSummaryDao.save(newSummary);
				item.setSummary(newSummary);
			}
			item.setCreateTime(currentTime);
			item.setCreateUserId(currentUserId);
		} else {
			RemainEvidenceAdjustItemVo itemInDbVo = remainEvidenceAdjustItemService.findItemVoById(itemVo.getId());
			item.setCreateTime(itemInDbVo.getCreateTime());
			item.setCreateUserId(itemInDbVo.getCreateUserId());
		}
		item.setModifyTime(currentTime);
		item.setModifyUserId(currentUserId);
		BigDecimal adjustTotalAmount = BigDecimal.ZERO;
		remainEvidenceAdjustItemService.saveRemainEvidenceAdjustItem(item);
		remainEvidenceAdjustSummaryDao.flush();
		RemainEvidenceAdjustSummary summary = remainEvidenceAdjustSummaryDao.findById(item.getSummary().getId());
		if (summary.getAdjustItems() != null && summary.getAdjustItems().size() > 0) {
			for (RemainEvidenceAdjustItem itemEach : summary.getAdjustItems()) {
				adjustTotalAmount = adjustTotalAmount.add(itemEach.getAdjustAmount());
			}
		} else {
			adjustTotalAmount = adjustTotalAmount.add(item.getAdjustAmount());
		}
		
		summary.setAdjustTotalAmount(adjustTotalAmount);
		summary.setModifyTime(currentTime);
		summary.setModifyUserId(currentUserId);
		remainEvidenceAdjustSummaryDao.merge(summary);
		return item;
	}
	
	/**
	 * 删除调整项
	 */
	@Override
	public void deleteRemainAdjustItem(String itemId) {
		String currentTime = DateTools.getCurrentDateTime();
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		RemainEvidenceAdjustItem item = remainEvidenceAdjustItemService.findItemById(itemId);
		RemainEvidenceAdjustSummary summary = remainEvidenceAdjustSummaryDao.findById(item.getSummary().getId());
		BigDecimal adjustTotalAmount = summary.getAdjustTotalAmount() != null ? summary.getAdjustTotalAmount() : BigDecimal.ZERO;
		summary.setAdjustTotalAmount(adjustTotalAmount.subtract(item.getAdjustAmount()));
		summary.setModifyTime(currentTime);
		summary.setModifyUserId(currentUserId);
		remainEvidenceAdjustSummaryDao.merge(summary);
		remainEvidenceAdjustItemService.deleteItem(item);
	}

	/**
	 * 保存调整概要
	 */
	@Override
	public void editRemainAdjustSummary(RemainEvidenceAdjustSummary summary) {
		String currentTime = DateTools.getCurrentDateTime();
		String currentUserId = userService.getCurrentLoginUser().getUserId();
		if (StringUtils.isBlank(summary.getId())) {
			summary.setId(null);
			summary.setCreateTime(currentTime);
			summary.setCreateUserId(currentUserId);
		} else {
			RemainEvidenceAdjustSummary summaryInDb = remainEvidenceAdjustSummaryDao.findById(summary.getId());
			summary.setAdjustTotalAmount(summaryInDb.getAdjustTotalAmount());
			summary.setCreateTime(summaryInDb.getCreateTime());
			summary.setCreateUserId(summaryInDb.getCreateUserId());
		}
		summary.setModifyTime(currentTime);
		summary.setModifyUserId(currentUserId);
		if (StringUtils.isBlank(summary.getId())) {
			remainEvidenceAdjustSummaryDao.save(summary);
		} else {
			remainEvidenceAdjustSummaryDao.merge(summary);
		}
	}
	
	/**
	 * 查找调整概要
	 */
	@Override
	public RemainEvidenceAdjustSummaryVo findRemainAdjustSummaryById(String summaryId) {
		RemainEvidenceAdjustSummary summary = remainEvidenceAdjustSummaryDao.findById(summaryId);
		RemainEvidenceAdjustSummaryVo summaryVo = HibernateUtils.voObjectMapping(summary, RemainEvidenceAdjustSummaryVo.class);
		summaryVo.setAdjustItems(HibernateUtils.voCollectionMapping(summary.getAdjustItems(), RemainEvidenceAdjustItemVo.class));
		return summaryVo;
	}
	
}
