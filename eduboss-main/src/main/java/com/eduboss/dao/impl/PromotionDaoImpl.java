package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.PromotionDao;
import com.eduboss.domain.Promotion;
import com.eduboss.domainVo.PromotionVo;

/**
 * A data access object (DAO) providing persistence and search support for
 * AppUser entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.eduboss.domain.AppUser
 * @author MyEclipse Persistence Tools
 */
@Repository("PromotionDao")
public class PromotionDaoImpl extends GenericDaoImpl<Promotion, String> implements PromotionDao {

	/**
	 * 查询该优惠是否已经存在
	 * */
	@Override
	public List<Promotion> promotionIfExist(PromotionVo promotion) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from Promotion where promotionType= :promotionType ")
		.append(" and promotionValue= :promotionValue ")
		.append(" and name = :name and isActive='Y'");
		params.put("promotionType", promotion.getPromotionType());
		params.put("promotionValue", new BigDecimal(promotion.getPromotionValue()));
		params.put("name", promotion.getName());
		return this.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 检查是否可以修改优惠方案
	 */
	public boolean checkCanChangePromotion(String promotionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select count(1) from contract_product where PROMOTION_IDS like :promotionId";
		params.put("promotionId", "%" + promotionId + "%");
		int count = this.findCountSql(sql, params);
		if (count > 0) {
			return false;
		}
		return true;
	}
	
}
