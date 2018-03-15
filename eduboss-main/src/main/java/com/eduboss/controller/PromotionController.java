/**
 * 
 */
package com.eduboss.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.PromotionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.ProductSettleAmount;
import com.eduboss.dto.Response;
import com.eduboss.service.PromotionService;

/**
 * @author chenguiban
 *
 */
@Controller
@RequestMapping(value = "/PromotionController")
public class PromotionController {

	@Autowired
	private PromotionService promotionService;
	
	@RequestMapping(value = "/saveOrUpdatePromotion", method =  RequestMethod.GET)
	@ResponseBody
	public Response saveOrUpdatePromotion(@ModelAttribute PromotionVo promotionVo) {
		promotionService.savePromotion(promotionVo);
		return new Response();
	}
	
	@RequestMapping(value = "/deletePromotion", method =  RequestMethod.GET)
	@ResponseBody
	public Response deletePromotion(@RequestParam String deleteIds) {
		promotionService.deletePromotin(deleteIds);
		return new Response();
	}
	
	@RequestMapping(value = "/inactivePromotion", method =  RequestMethod.GET)
	@ResponseBody
	public Response inactivePromotion(@RequestParam String promotionId) {
		promotionService.inactivePromotion(promotionId);
		return new Response();
	}
	
	@RequestMapping(value = "/activePromotion", method =  RequestMethod.GET)
	@ResponseBody
	public Response activePromotion(@RequestParam String promotionId) {
		promotionService.activePromotion(promotionId);
		return new Response();
	}

	@RequestMapping(value = "/findPromotionById", method =  RequestMethod.GET)
	@ResponseBody
	public PromotionVo findPromotionById(@RequestParam String promotionId) {
		return promotionService.findPromotionById(promotionId);
	}
	
	@RequestMapping(value = "/findPromotionList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findPromotionList(@ModelAttribute PromotionVo promotionVo, @ModelAttribute GridRequest gridRequest, ModelVo modelVo) {
		return new DataPackageForJqGrid(promotionService.getPromotionList(promotionVo, new DataPackage(gridRequest), modelVo));
	}
	
	@RequestMapping(value = "/getProductActivePromotionList", method =  RequestMethod.GET)
	@ResponseBody
	public List<PromotionVo> getProductActivePromotionList(String productId,String organizationId) {
		return promotionService.getProductActivePromotionList(productId,organizationId);
	}

	/**
	 * 获取产品的优惠只顾优惠的有效无效，不管时间区间
	 * @param productId
	 * @param organizationId
     * @return
     */
	@RequestMapping(value = "/getPromotionsForProduct", method =  RequestMethod.GET)
	@ResponseBody
	public List<PromotionVo> getPromotionsForProduct(String productId,String organizationId){
		return promotionService.getPromotionsForProduct(productId,organizationId);
	}

	@RequestMapping(value = "/calculatePromotion", method =  RequestMethod.GET)
	@ResponseBody
	public ProductSettleAmount calculatePromotion(String promotionIds, BigDecimal price, BigDecimal totalCourseCount, BigDecimal totalAmount) {
		return promotionService.calculatePromotions(promotionIds, price, totalCourseCount, totalAmount);
	}
	
}
