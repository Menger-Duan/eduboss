package com.eduboss.service.handler.impl;

import com.eduboss.common.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.IncomeDistributionServiceImpl;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.StringUtil;

import net.sf.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class LiveConProdHandler extends ContractProductHandler {


	@Override
	public void createNewWithContract(Contract contract, ContractProductVo conProdVo) throws Exception{
		List<ContractProduct> list=contractService.findContractProductByProductAndStudent(contract.getStudent().getId(),conProdVo.getProductId());
		for(ContractProduct cp :list){
			if(!cp.getStatus().equals(ContractProductStatus.REFUNDED)
					&& !cp.getStatus().equals(ContractProductStatus.CLOSE_PRODUCT)
					&& !cp.getStatus().equals(ContractProductStatus.UNVALID)){
				throw new ApplicationException("学生:“"+cp.getContract().getStudent().getName()+"”的直播产品:“"+cp.getProduct().getName()+"”不能重复报读!");
			}
		}
		super.createNewWithContract(contract,conProdVo);
	}

	@Override
	protected void doAfterCreateContractProduct(ContractProduct conProduct,
												ContractProductVo conProdVo) {
		super.doAfterCreateContractProduct(conProduct, conProdVo);
	}
	
	@Override
	public void chargeOneOnOneClass(Contract contract, ContractProduct conProd,
			BigDecimal remainingMoney, BigDecimal zero, Course course, String transactionId) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);		
	}

	@Override
	public void chargeOtherProduct(Contract contract,
			ContractProduct contractProduct, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);		
	}

	@Override
	public void chargePromiseClassProduct(Contract contract,
			ContractProduct contractProduct, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, PromiseClassRecord promiseClassRecord) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);
	}



	@Override
	public void chargeOtmClass(ContractProduct targetConPrd, OtmClass otmClass,
			OtmClassCourse otmClassCourse, Double courseHour, String transactionId) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);
	}
	

	@Override
	public void chargeLectureClass(LectureClassStudent stu) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);		
	}

	@Override
	protected void checkPaidStatusOfContractProduct(
			ContractProduct contractProduct) {
		super.checkPaidStatusOfContractProduct(contractProduct);
		if(contractProduct.getPaidStatus()!=null && contractProduct.getPaidStatus().equals(ContractProductPaidStatus.PAID) && contractProduct.getContract().getContractType()!=ContractType.LIVE_CONTRACT){
			JSONObject o =new JSONObject();
			o.put("curriculum_id",contractProduct.getProduct().getLiveId());
			o.put("name",contractProduct.getContract().getStudent().getName());
			o.put("paid_price",contractProduct.getPaidAmount());
//			o.put("phone",contractProduct.getContract().getCustomer().getContact());
			if (contractProduct.getContract().getStudent() != null && 
			        StringUtil.isNotBlank(contractProduct.getContract().getStudent().getContact())) {
			    o.put("phone",contractProduct.getContract().getStudent().getContact()); // 换成学生的号码
			} else {
			    o.put("phone",contractProduct.getContract().getCustomer().getContact());
			}
			o.put("contract_id",contractProduct.getContract().getId());
			o.put("boss_student_id",contractProduct.getContract().getStudent().getId());
			if(contractProduct.getContract().getStudent().getBlCampus()!=null) {
				if(contractProduct.getContract().getStudent().getBlCampus().getAreaId()!=null) {
					o.put("district_id", contractProduct.getContract().getStudent().getBlCampus().getAreaId());
				}else if(contractProduct.getContract().getStudent().getBlCampus().getCityId()!=null){
					o.put("district_id", contractProduct.getContract().getStudent().getBlCampus().getCityId());
				}else if(contractProduct.getContract().getStudent().getBlCampus().getProvinceId()!=null){
					o.put("district_id", contractProduct.getContract().getStudent().getBlCampus().getProvinceId());
				}
			}
			//JedisUtil.lpush("liveProduct",o.toString());
		}
	}

	public void chargeLiveCurriculum(Curriculum curriculum){
		String contractId = curriculum.getContractId();
		String liveId = curriculum.getLiveId();
		List<ContractProduct> contractProductList = getContractProductsForLiveContract(contractId, liveId);
		BigDecimal chargeMoney = curriculum.getCourseAmount();//
		for (ContractProduct cp : contractProductList){
			BigDecimal remainingMoney = cp.getRemainingAmount();
			if (remainingMoney.compareTo(chargeMoney)>=0 ){//&& remainingMoney.compareTo(BigDecimal.ZERO)>0
				chargeLiveCourse(cp, curriculum);
				break;
			}
		}
	}

	public List<ContractProduct> getContractProductsForLiveContract(String contractId, String liveId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT cp.* FROM contract_product cp, product p ");
		sql.append(" WHERE cp.PRODUCT_ID=p.ID AND cp.CONTRACT_ID=:contractId AND p.live_Id=:liveId ");
		Map<String, Object> map = new HashMap<>();
		map.put("contractId", contractId);
		map.put("liveId", liveId);
		return contractProductDao.findBySql(sql.toString(), map);
	}

	public void chargeLiveCourse(ContractProduct targetConPrd, Curriculum curriculum) {
		if (targetConPrd.getIsFrozen() == 0) {
			throw new ApplicationException("扣费合同产品已经冻结，不能进行扣费操作！");
		}

		BigDecimal chargeMoney = curriculum.getCourseAmount();//扣费金额
		BigDecimal auditedCourseHours = BigDecimal.ONE;//扣费课时数 直播每次都是扣一个课时

		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();//剩余金额
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();//实际剩余

		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		String transactionId = UUID.randomUUID().toString();
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO)>0){
			if (remainingAmountOfBasicAmount.compareTo(chargeMoney)>=0){
				realChargeMoney = chargeMoney;
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				contractService.saveAccountChargeRecord(realChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.REAL, auditedCourseHours, curriculum, transactionId);
				//实收才算直播的课消业绩 要一半   这里要换成营收
				//incomeDistributionService.addIncomeDistributionForLive(realChargeMoney.divide(BigDecimal.valueOf(2)), targetConPrd);
			}else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);
				BigDecimal promotoinChargeHours = auditedCourseHours.subtract(realChargeHours);
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = chargeMoney.subtract(realChargeMoney);
				contractService.saveAccountChargeRecord(realChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.REAL, realChargeHours, curriculum, transactionId);
				//实收才算直播的课消业绩 要一半  这里要换成营收
				//incomeDistributionService.addIncomeDistributionForLive(realChargeMoney.divide(BigDecimal.valueOf(2)), targetConPrd);

//				super.addAccountChargeRecord(realChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.REAL, realChargeHours, curriculum);
				contractService.saveAccountChargeRecord(promotionChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.PROMOTION, promotoinChargeHours, curriculum, transactionId);
//				super.addAccountChargeRecord(promotionChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.PROMOTION, promotoinChargeHours, curriculum);
			}
		}else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = chargeMoney;
			contractService.saveAccountChargeRecord(promotionChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.PROMOTION, auditedCourseHours, curriculum, transactionId);
//			super.addAccountChargeRecord(promotionChargeMoney, targetConPrd, ProductType.LIVE, ChargeType.NORMAL, PayType.PROMOTION, auditedCourseHours, curriculum);
		}

		// 只有 Normal 和 started 的值才可以 扣费
		if (remainingAmount.compareTo(chargeMoney)>=0 ){//&& (targetConPrd.getStatus() == ContractProductStatus.NORMAL || targetConPrd.getStatus() == ContractProductStatus.STARTED)
			doAfterForNewCharge(chargeMoney, realChargeMoney, promotionChargeMoney, auditedCourseHours, targetConPrd);
		}else {
			throw new ApplicationException("本合同产品无法继续扣费");
		}

	}





}
