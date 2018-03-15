package com.eduboss.service.handler.impl;

import java.math.BigDecimal;
import java.util.UUID;

import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Course;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.PropertiesUtils;


public class OtherConProdHandler extends ContractProductHandler {
	
	/**
	 * 当合同产品建立好后， 可以有一个回调函数. 
	 * @param conProduct
	 * @param conProdVo 
	 */
	@Override
	protected void doAfterCreateContractProduct(ContractProduct conProduct, ContractProductVo conProdVo, boolean updateInventory) {
		super.doAfterCreateContractProduct(conProduct, conProdVo, true);
		if (ContractType.INIT_CONTRACT.equals(conProduct.getContract().getContractType())){
			//如果是其他产品要实时扣费
			conProduct.setPaidTime(DateTools.getCurrentDate());
			chargeOtherProduct(conProduct.getContract(), conProduct, conProduct.getPaidAmount(), conProduct.getQuantity());
		}
	}
	
	protected void doAfterCreateContractProduct(ContractProduct conProduct, ContractProductVo conProdVo) {
        doAfterCreateContractProduct(conProduct, conProdVo, true);
    }
	
	@Override
	protected void checkIfCanEdit(ContractProduct processingProductInHibernate,
			ContractProduct newProduct) {
		super.checkIfCanEdit(processingProductInHibernate, newProduct);
		boolean hasCharge = chargeService.hasChargeRecordOfContractProduct(processingProductInHibernate);
		if(processingProductInHibernate.getPlanAmount().compareTo(newProduct.getPlanAmount()) != 0 && hasCharge)
			throw new ApplicationException("其他类型合同产品已经有扣费消费， 不能修改！");
		
	}

	@Override
	public void calBasicContractProductForCreateNewContract(
			ContractProduct conProduct) throws Exception {
		super.calBasicContractProductForCreateNewContract(conProduct);
		conProduct.setQuantity(conProduct.getQuantity());
		conProduct.setDealDiscount(BigDecimal.ONE);
		conProduct.setPrice(conProduct.getPrice());
	}

	@Override
	public void doAfterAssignMoneyAmount(Contract contract,
			ContractProduct contractProduct, BigDecimal assignAmount,String remark) {
		super.doAfterAssignMoneyAmount(contract, contractProduct, assignAmount,remark);
		if(contractProduct.getType()== ProductType.OTHERS &&  contractProduct.getPaidStatus()== ContractProductPaidStatus.PAID
				&& contractProduct.getStatus()!= ContractProductStatus.CLOSE_PRODUCT && contractProduct.getStatus()!= ContractProductStatus.ENDED
				&& contractProduct.getProduct().getOther_type()!=null && !("DAT0000000432".equals(contractProduct.getProduct().getOther_type().getId())||"目标班VIP服务费".equals(contractProduct.getProduct().getOther_type().getName()))){
//			chargeService.chargeOtherProdect(contractProduct.getId(), userService.getCurrentLoginUser());
			//改为按其他产品的实际课时来扣
			// 培优逻辑：禁用其他产品在收款后自动扣费逻辑，其他产品在合同第一次发生扣费再扣费
			if (!"advance".equals(PropertiesUtils.getStringValue("institution"))||assignAmount.compareTo(BigDecimal.ZERO) == 0){
				//星火就要扣费
				chargeOtherProduct(contract, contractProduct, contractProduct.getTotalAmount().subtract(contractProduct.getConsumeAmount()), contractProduct.getQuantity().subtract(contractProduct.getConsumeQuanity()) );
			}

		}
	}

	@Override
	public void chargeOneOnOneClass(Contract contract, ContractProduct conProd,
			BigDecimal remainingMoney, BigDecimal zero, Course course, String transactionId) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);
	}

	@Override
	public void chargeMiniClass(ContractProduct targetConPrd,
			MiniClass miniClass, MiniClassCourse miniClassCourse,
			Double courseHour) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);
	}

	@Override
	public void chargeOtherProduct(Contract contract,
			ContractProduct contractProduct, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity) {
		if (contractProduct.getIsFrozen() == 0) {
			throw new ApplicationException("本次扣费涉及的合同产品在冻结状态，不能进行扣费。");
		}
		addChargeRecord(contractProduct,chargeQuantity,chargeMoneyAmount);
	}
	
	public void chargeOtherProduct_Edu(Contract contract,
			ContractProduct contractProduct, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity,User user) {
		if (contractProduct.getIsFrozen() == 0) {
			throw new ApplicationException("本次扣费涉及的合同产品在冻结状态，不能进行扣费。");
		}
		addChargeRecord_Edu(contractProduct,chargeQuantity,chargeMoneyAmount,user);
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
	public void narrowContractProduct(ContractProduct cp) {
		//如果合同产品未收款完成时，可以进行缩单
		if(cp.getStatus().equals(ContractProductStatus.NORMAL) 
			&& cp.getPaidStatus().equals(ContractProductPaidStatus.PAYING) 
			&& cp.getPaidAmount().compareTo(BigDecimal.ZERO)>0){
			throw new ApplicationException("其他产品有部分付款，请支付完成或者提取到其他产品上再执行缩单操作");		
		}else if(cp.getRealAmount().compareTo(BigDecimal.ZERO)>0  && cp.getPaidAmount().compareTo(BigDecimal.ZERO)==0){
			moneyArrangeLogService.deleteByContractProductId(cp.getId());
			contractProductDao.deleteById(cp.getId()); // 删除该产品
			contractDao.flush();
		}	
	}


	public void chargeEcsOtherProduct(ContractProduct contractProduct,BigDecimal chargeQuantity) {
		if (contractProduct.getIsFrozen() == 0) {
			throw new ApplicationException("本次扣费涉及的合同产品在冻结状态，不能进行扣费。");
		}
		BigDecimal chargeMoneyAmount = contractProduct.getPrice().multiply(chargeQuantity);
		addChargeRecord(contractProduct,chargeQuantity,chargeMoneyAmount);
	}

	public void addChargeRecord( ContractProduct contractProduct,BigDecimal chargeQuantity,BigDecimal chargeMoneyAmount){
		BigDecimal remainingAmount = contractProduct.getRemainingAmount();
		BigDecimal remainingAmountOfBasicAmount =  contractProduct.getRemainingAmountOfBasicAmount();
		Organization belongCampus = organizationDao.findById(contractProduct.getContract().getBlCampusId());
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		String transactionId = UUID.randomUUID().toString();
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(chargeMoneyAmount) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = chargeMoneyAmount;
				super.saveAccountChargeRecord(chargeMoneyAmount, contractProduct, ProductType.OTHERS, null, chargeQuantity,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null , null);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = chargeMoneyAmount.subtract(remainingAmountOfBasicAmount);
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, contractProduct, ProductType.OTHERS, null, chargeQuantity,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null , null);
				super.saveAccountChargeRecord(chargeMoneyAmount.subtract(remainingAmountOfBasicAmount), contractProduct, ProductType.OTHERS, null,
						chargeQuantity, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null , null);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = chargeMoneyAmount;
			super.saveAccountChargeRecord(chargeMoneyAmount, contractProduct, ProductType.OTHERS, null, chargeQuantity,
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null , null);
		}
		if(remainingAmount.compareTo(chargeMoneyAmount)>=0 && (contractProduct.getStatus() == ContractProductStatus.NORMAL ||contractProduct.getStatus() == ContractProductStatus.STARTED )) {
			doAfterForNewCharge(chargeMoneyAmount, realChargeMoney, promotionChargeMoney, chargeQuantity, contractProduct );
		} else {
			throw new ApplicationException("本合同产品无法继续扣费");
		}
	}
	
	public void addChargeRecord_Edu( ContractProduct contractProduct,BigDecimal chargeQuantity,BigDecimal chargeMoneyAmount,User user){
		BigDecimal remainingAmount = contractProduct.getRemainingAmount();
		BigDecimal remainingAmountOfBasicAmount =  contractProduct.getRemainingAmountOfBasicAmount();
		Organization belongCampus = organizationDao.findById(contractProduct.getContract().getBlCampusId());
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		String transactionId = UUID.randomUUID().toString();
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(chargeMoneyAmount) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = chargeMoneyAmount;
				super.saveAccountChargeRecord_Edu(chargeMoneyAmount, contractProduct, ProductType.OTHERS, null, chargeQuantity,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null , null,user);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = chargeMoneyAmount.subtract(remainingAmountOfBasicAmount);
				super.saveAccountChargeRecord_Edu(remainingAmountOfBasicAmount, contractProduct, ProductType.OTHERS, null, chargeQuantity,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null , null,user);
				super.saveAccountChargeRecord_Edu(chargeMoneyAmount.subtract(remainingAmountOfBasicAmount), contractProduct, ProductType.OTHERS, null,
						chargeQuantity, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null , null,user);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = chargeMoneyAmount;
			super.saveAccountChargeRecord_Edu(chargeMoneyAmount, contractProduct, ProductType.OTHERS, null, chargeQuantity,
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null , null,user);
		}
		if(remainingAmount.compareTo(chargeMoneyAmount)>=0 && (contractProduct.getStatus() == ContractProductStatus.NORMAL ||contractProduct.getStatus() == ContractProductStatus.STARTED )) {
			doAfterForNewCharge(chargeMoneyAmount, realChargeMoney, promotionChargeMoney, chargeQuantity, contractProduct );
		} else {
			throw new ApplicationException("本合同产品无法继续扣费");
		}
	}
}
