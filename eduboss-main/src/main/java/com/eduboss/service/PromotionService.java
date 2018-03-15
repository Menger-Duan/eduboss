package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.eduboss.domainVo.PromotionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.ProductSettleAmount;

@Service
public interface PromotionService {
	
	/**
	 * 保存促销
	 * @param promotionVo
	 */
	public void savePromotion(PromotionVo promotionVo);
	
	/**
	 * 删除促销
	 * @param promotionVo
	 */
	public void deletePromotin(String promotionIds);
	
	/**
	 * 禁用促销
	 * @param promotionId
	 */
	public void inactivePromotion(String promotionId);
	
	/**
	 * 启用促销
	 * @param promotionId
	 */
	public void activePromotion(String promotionId);
	
	/**
	 * 根据ID查找优惠方案
	 * @param promotionId
	 * @return
	 */
	public PromotionVo findPromotionById(String promotionId);
	
	/**
	 * 查询促销（用于维护包括过期与禁用）
	 * @param promotionVo
	 */
	public DataPackage getPromotionList(PromotionVo promotionVo, DataPackage dataPackage, ModelVo modelVo);
	
	/**
	 * 获取该productId的所有有效促销方案
	 * @param promotionVo
	 * @return
	 */
	public List<PromotionVo> getProductActivePromotionList(String productId,String organizationId);
	
	/**
	 * 计算优惠金额，返回的如果是正数，就是赠送，把优惠金额加到产品总额上，实收金额不变；如果返回的是负数，就是减免，产品总额不变，实收金额减去优惠金额
	 * @param promotionIds - 优惠方案编号,多个优惠用英文逗号隔开
	 * @param price - 产品单价
	 * @param totalCourseCount - 报读总课时
	 * @param totalAmount - 产品总价
	 * @return
	 */
	public ProductSettleAmount calculatePromotions(String promotionIds, BigDecimal price, BigDecimal totalCourseCount, BigDecimal totalAmount);

	/**
	 * 获取产品的优惠只顾优惠的有效无效，不管时间区间
	 * @param productId
	 * @param organizationId
     * @return
     */
	List<PromotionVo> getPromotionsForProduct(String productId, String organizationId);
	
	/**
	 * 检查是否可以修改优惠方案
	 * @param promotionId
	 * @return
	 */
	public boolean checkCanChangePromotion(String promotionId);
}
