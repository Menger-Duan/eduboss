package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.PromotionType;
import com.eduboss.dao.ProductDao;
import com.eduboss.dao.PromotionDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Promotion;
import com.eduboss.domainVo.PromotionSnapshotVo;
import com.eduboss.domainVo.PromotionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.ProductSettleAmount;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.PromotionService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PromotionDao promotionDao;
	
	@Autowired 
	private ProductDao productDao;
	
	@Override
	public void savePromotion(PromotionVo promotionVo) {
		List<Promotion> promotions = promotionDao.promotionIfExist(promotionVo);
		if (StringUtils.isNotBlank(promotionVo.getId())) {
			/*if (!this.checkCanChangePromotion(promotionVo.getId())) {
				throw new ApplicationException("该促销已经已经被使用，不能修改");
			}*/
			Promotion promotion = promotionDao.findById(promotionVo.getId());
			if (promotion == null) {
				throw new ApplicationException("根据促销编号找不到记录");
			}
			//#3812 在产品中使用了优惠也可以禁用优惠
//			if(promotionVo.getIsActive().equals("N")){
//				int count = promotionDao.findCountHql("select count(*) from Product where promotionId like '%"+promotionVo.getId()+"%'");
//				if(count>0)
//					throw new ApplicationException("不允许禁用，产品中已使用该优惠！");
//			}
			//判断该优惠是否已存在
            if(promotions.size()>0){
                if(promotions.get(0).getName()!=promotion.getName() || promotions.get(0).getPromotionValue()!=promotion.getPromotionValue()
                        || promotions.get(0).getPromotionType()!=promotion.getPromotionType())
                    if(promotions.size()>0){
                        throw new ApplicationException("该优惠已经存在！");
                    }
            }
			HibernateUtils.copyPropertysWithoutNull(promotion, promotionVo);
			
			if (StringUtils.isNotBlank(promotionVo.getPromotionValue())) {
				promotion.setPromotionValue(new BigDecimal(promotionVo.getPromotionValue()));
			}
			promotion.setOrganization(new Organization(promotionVo.getOrganizationId()));
			promotion.setModifyUser(userService.getCurrentLoginUser());
			promotion.setModifyTime(DateTools.getCurrentDateTime());
		} else {
			//判断该优惠是否已存在 
			if(promotions.size()>0){
				throw new ApplicationException("该优惠已经存在！");
			}
			
			String promotionValueStr = promotionVo.getPromotionValue();
			List<Promotion> newPromotionList = new ArrayList<Promotion>(); 
			if (promotionValueStr.indexOf(",") > 0) {
				String[] promotionValueArray =  promotionValueStr.split(",");
				String[] promotionNameArray =  promotionVo.getName().split(",");
				try {
					for (int i = 0; i < promotionValueArray.length; i ++) {
						PromotionVo singlePromotionVo = new PromotionVo();
						HibernateUtils.copyPropertysWithoutNull(singlePromotionVo, promotionVo);
						singlePromotionVo.setPromotionValue(promotionValueArray[i]);
						singlePromotionVo.setName(promotionNameArray[i]);
						newPromotionList.add(HibernateUtils.voObjectMapping(singlePromotionVo, Promotion.class));
					}
				} catch (Exception e) {
					throw new ApplicationException("优惠方案值与名称不对应。");
				}
			} else {
				newPromotionList.add(HibernateUtils.voObjectMapping(promotionVo, Promotion.class));
			}

			for (Promotion newPromotion : newPromotionList) {
				//newPromotion.setOrganization(userService.getBelongBrench());
				newPromotion.setCreateUser(userService.getCurrentLoginUser());
				newPromotion.setCreateTime(DateTools.getCurrentDateTime());
				promotionDao.save(newPromotion);
			}
		}
	}

	@Override
	public void deletePromotin(String promotionIds) {
		if (StringUtils.isNotBlank(promotionIds)) {
			Map<String, Object> params = Maps.newHashMap();
			int i = 0;
			for (String id : promotionIds.split(",")) {
				i = i + 1;
				params.put("promotionId"+i, "%"+id+"%");
				int count = promotionDao.findCountHql("select count(*) from Product where promotionId like :promotionId"+i+" ",params);
				if(count>0)
					throw new ApplicationException("不允许删除，产品中已使用该优惠！");
			}
			for (String id : promotionIds.split(",")) {
				promotionDao.delete(new Promotion(id));
			}
		} else {
			throw new ApplicationException("要删除的促销编号不能为空。");
		}
	}
	
	/**
	 * 禁用促销
	 * @param promotionId
	 */
	public void inactivePromotion(String promotionId) {
		Promotion promotion = promotionDao.findById(promotionId);
		promotion.setIsActive("N");
		promotionDao.merge(promotion);
	}
	
	/**
	 * 启用促销
	 * @param promotionId
	 */
	public void activePromotion(String promotionId) {
		Promotion promotion = promotionDao.findById(promotionId);
		promotion.setIsActive("Y");
		promotionDao.merge(promotion);
	}

	@Override
	public DataPackage getPromotionList(PromotionVo promotionVo, DataPackage dataPackage, ModelVo modelVo) {
        String orgId = null;
        if(StringUtils.isNotBlank(promotionVo.getOrganizationId())){
            orgId = promotionVo.getOrganizationId();
            promotionVo.setOrganizationId(null);
        }
		List<Criterion> criterionList = HibernateUtils.buildAndLikeCriterionWhenPropertiesNotEmty(promotionVo);
        if(StringUtils.isNotBlank(orgId)){
//            criterionList.add(Expression.eq("organization.id", orgId));
            criterionList.add(Expression.or(Expression.sqlRestriction("(select orgLevel from organization where id = '"+orgId+"') like concat((select orgLevel from organization where id = organization_id),'%')")
			,Expression.le("data", "LIVE")));
        }
		if (StringUtils.isNotBlank(modelVo.getStartDate())) {
			criterionList.add(Expression.le("startTime", modelVo.getStartDate()));
		}
		if (StringUtils.isNotBlank(modelVo.getEndDate())) {
			criterionList.add(Expression.ge("endTime", modelVo.getEndDate()));
		}

		if (StringUtils.isNotBlank(modelVo.getDateStartEnd())){
			criterionList.add(Expression.ge("endTime", modelVo.getDateStartEnd()));
		}

		if (StringUtils.isNotBlank(modelVo.getDateEndEnd())){
			criterionList.add(Expression.le("endTime", modelVo.getDateEndEnd()));
		}

		if (StringUtils.isNotBlank(promotionVo.getIsActive())){
			criterionList.add(Expression.eq("isActive", promotionVo.getIsActive()));
		}
		dataPackage = promotionDao.findPageByCriteria(dataPackage, HibernateUtils.prepareOrder(dataPackage, "createTime", "desc"), criterionList);
		dataPackage.setDatas(HibernateUtils.voListMapping((List<Promotion>)dataPackage.getDatas(), PromotionVo.class));
		return dataPackage;
	}

	@Override
	public List<PromotionVo> getProductActivePromotionList(String productId,String organizationId) {
		PromotionVo promotionVo = new PromotionVo();
		//未禁用
		promotionVo.setIsActive("Y");
		promotionVo.setOrganizationId(organizationId);
		//当前时间内有效
		ModelVo modelVo = new ModelVo();
		modelVo.setStartDate(DateTools.getCurrentDate());
		modelVo.setEndDate(DateTools.getCurrentDate());
		
		return (List<PromotionVo>) getPromotionList(promotionVo, new DataPackage(0, 2000), modelVo).getDatas();
	}

	/**
	 * 获取产品的优惠只顾优惠的有效无效，不管时间区间
	 *
	 * @param productId
	 * @param orgId
	 * @return
	 */
	@Override
	public List<PromotionVo> getPromotionsForProduct(String productId, String orgId) {
		Map<String, Object> params = Maps.newHashMap();
		/*if(StringUtils.isNotBlank(productId)){			
			Product product=productDao.findById(productId);
			if(StringUtils.isNotBlank(product.getPromotionId()) && !product.getPromotionId().equals("-1")){
				String ids[]=product.getPromotionId().split(",");
//				for (int i = 0; i < ids.length; i++) {
//					promotionId+="'"+ids[i]+"'";
//					if(i!=ids.length-1){
//						promotionId+=",";
//					}
//				}
				params.put("promotionId", ids);
			}
		}*/
		params.put("orgId", orgId);
		StringBuffer sql=new StringBuffer();
		sql.append("select * from promotion where (select orgLevel from organization where id = :orgId ) like concat((select orgLevel from organization where id = organization_id),'%')");
//		sql.append(" AND IS_ACTIVE='Y' AND END_TIME >= '" + DateTools.getCurrentDate() + "' ");
		/*if(params.containsKey("promotionId")){
			sql.append(" and id in(:promotionId)  ");
		}*/
		
		sql.append(" order by id desc" );
		
		List<PromotionVo> resultList = HibernateUtils.voListMapping(promotionDao.findBySql(sql.toString(),params),PromotionVo.class);
		String isOverdue = "N";
		String currentDate = DateTools.getCurrentDate();
		for (PromotionVo vo : resultList) {
			isOverdue = DateTools.daysBetween(vo.getEndTime(), currentDate) > 0 ? "Y" : "N"; // 判断是否过期了
			vo.setIsOverdue(isOverdue);
		}
		return resultList;
	}
	

	@Override
	public PromotionVo findPromotionById(String promotionId) {
		Promotion promotion = promotionDao.findById(promotionId);
		if (promotion == null) {
			throw new ApplicationException("根据ID找不到优惠记录。");
		}
		PromotionVo vo =  HibernateUtils.voObjectMapping(promotion, PromotionVo.class);
		vo.setCanChange(this.checkCanChangePromotion(promotionId));
		return vo;
	}
	
	@Override
	public ProductSettleAmount calculatePromotions(String promotionIds, BigDecimal price, BigDecimal totalCourseCount, BigDecimal totalAmount) {
		if (StringUtils.isBlank(promotionIds)) {
			throw new ApplicationException("优惠ID不能为空。");
		}
		if (price == null || totalCourseCount == null || totalAmount == null) {
			throw new ApplicationException("产品单价、报读课时数、总金额 均不能为空。");
		}
		ProductSettleAmount psa = new ProductSettleAmount();
		psa.setRealPayAmount(totalAmount);
		psa.setTotalAmount(totalAmount);
		String promotionIdArray[] = promotionIds.split(",");
		Promotion promotion = null;
		for (String promotionId : promotionIdArray) {
			promotion = promotionDao.findById(promotionId);
			BigDecimal settlePromotionAmount = calculateSinglePromotion(promotion, price, totalCourseCount, totalAmount);
			if (promotion.getPromotionType() == PromotionType.DISCOUNT) {
			    totalAmount = totalAmount.add(settlePromotionAmount);
			}
			if (settlePromotionAmount.compareTo(BigDecimal.ZERO) > 0) {
                psa.setTotalAmount(psa.getTotalAmount().add(settlePromotionAmount)); //大于零的，为赠送，加在总额上
			} else {
                psa.setRealPayAmount(psa.getRealPayAmount().add(settlePromotionAmount)); //小于零的，为减免，从实收中减去
			}
            psa.setPromotionAmount(psa.getPromotionAmount().add(settlePromotionAmount.abs())); //优惠金额的绝对值总和就是优惠金额
			
			//保存优惠计算过程及结果
			PromotionSnapshotVo vo = HibernateUtils.voObjectMapping(promotion, PromotionSnapshotVo.class);
			vo.setCalExpression(getCalculationExpressionByPromotionType(promotion.getPromotionType()));
			vo.setSettlePromotionAmount(settlePromotionAmount);
			psa.getPromotionVos().add(vo);
		}
		psa.setTotalAmount(psa.getTotalAmount().setScale(2, RoundingMode.HALF_UP)); 
		psa.setRealPayAmount(psa.getRealPayAmount().setScale(2, RoundingMode.HALF_UP));
		psa.setPromotionAmount(psa.getPromotionAmount().setScale(2, RoundingMode.HALF_UP));
		return psa;
	}
	
	/**
	 * 检查是否可以修改优惠方案
	 */
	public boolean checkCanChangePromotion(String promotionId) {
		return promotionDao.checkCanChangePromotion(promotionId);
	}
	
	private BigDecimal calculateSinglePromotion(Promotion promotion, BigDecimal price, BigDecimal totalCourseCount, BigDecimal totalAmount) {
		if (promotion == null) {
			throw new ApplicationException("根据ID找不到优惠记录。");
		}
		BigDecimal returnValue = BigDecimal.ZERO;
		switch(promotion.getPromotionType()) {
		case DISCOUNT: //折扣
			returnValue = BigDecimal.ZERO.subtract(totalAmount.multiply(BigDecimal.ONE.subtract(promotion.getPromotionValue().divide(new BigDecimal("100"), 5, RoundingMode.HALF_UP))));
			break;
		case GIFT_CASH: // 赠送现金
			returnValue = promotion.getPromotionValue();
			break;
		case GIFT_COURSES: // 赠送课时
			returnValue = price.multiply(promotion.getPromotionValue());
			break;
		case GIFT_COURSES_BY_RATE: // 按比例赠送课时
			returnValue = price.multiply(totalCourseCount.multiply(promotion.getPromotionValue().divide(new BigDecimal("100"), 5, RoundingMode.HALF_UP)));
			break;
		case REDUCTION: // 减免
			returnValue = BigDecimal.ZERO.subtract(promotion.getPromotionValue());
			break;
		}
		return returnValue.setScale(5, RoundingMode.HALF_UP);
	}
	
	private String getCalculationExpressionByPromotionType(PromotionType promotionType) {
		String expression = "";
		switch(promotionType) {
		case DISCOUNT: //折扣
			expression = "优惠金额=产品总额*(1-优惠值/100)";
			break;
		case GIFT_CASH: // 赠送现金
			expression = "优惠金额=优惠值";
			break;
		case GIFT_COURSES: // 赠送课时
			expression = "优惠金额=单价*优惠值";
			break;
		case GIFT_COURSES_BY_RATE: // 按比例赠送课时
			expression = "优惠金额=单价*(总课时*优惠值/100)";
			break;
		case REDUCTION: // 减免
			expression = "优惠金额=优惠值";
			break;
		}
		return expression;
	}

}
