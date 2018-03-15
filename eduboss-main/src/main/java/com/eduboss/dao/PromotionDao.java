package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Promotion;
import com.eduboss.domainVo.PromotionVo;


/**
 * @classname	PointialStudentDao.java 
 * @Description
 * @author	chenguiban
 * @Date	2014-6-20 19:32:39
 * @LastUpdate	chenguiban
 * @Version	1.0
 */

@Repository
public interface PromotionDao extends GenericDAO<Promotion, String> {
	//the common dao method had init in thd GenericDAO, add the special method in this class
	
	public List<Promotion> promotionIfExist(PromotionVo promotion);
	
	/**
	 * 检查是否可以修改优惠方案
	 * @param promotionId
	 * @return
	 */
	public boolean checkCanChangePromotion(String promotionId);
	
}
