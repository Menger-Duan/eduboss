package com.eduboss.service.handler.impl;

import java.math.BigDecimal;
import java.util.UUID;

import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.Course;
import com.eduboss.domain.LectureClass;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.handler.ContractProductHandler;

public class OtmClassConProdHandler extends ContractProductHandler {

	@Override
	public void chargeOneOnOneClass(Contract contract, ContractProduct conProd,
			BigDecimal remainingMoney, BigDecimal zero, Course course,
			String transactionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chargeMiniClass(ContractProduct targetConPrd,
			MiniClass miniClass, MiniClassCourse miniClassCourse,
			Double courseHour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chargeOtherProduct(Contract contract,
			ContractProduct contractProduct, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chargePromiseClassProduct(Contract contract,
			ContractProduct contractProduct, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, PromiseClassRecord promiseClassRecord) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void chargeOtmClass(ContractProduct targetConPrd, OtmClass otmClass,
			OtmClassCourse otmClassCourse, Double courseHour, String transactionId) {
		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
		// 总价格 = 单价 * 课时 * 折扣
		BigDecimal detel = BigDecimal.ZERO;
		if (targetConPrd.getDealDiscount().compareTo(BigDecimal.ZERO) > 0) {
			detel = price.multiply(targetConPrd.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));
		} else {
			detel = price.multiply(BigDecimal.valueOf(courseHour));
		}
		
		chargeOtmClass(targetConPrd, otmClass, otmClassCourse, courseHour, transactionId, detel);
	}

	/** 
	 * 如果存在跨产品扣费的情况，在第一个产品不够钱扣时，要避免课时除不尽的情况，直接扣金额。课时用扣掉的金额除以单价。
	*/
	public void chargeOtmClass(ContractProduct targetConPrd, OtmClass otmClass,
			OtmClassCourse otmClassCourse, Double courseHour, String transactionId,BigDecimal detel) {
		super.chargeOtmClass(targetConPrd, otmClass, otmClassCourse, courseHour, transactionId);
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		
		if(targetConPrd.getType().equals(ProductType.ECS_CLASS)){//如果是目标班报读小班，则扣费金额0
			detel=BigDecimal.ZERO;
		}
		
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		// 重设扣费记录所属校区为小班课程的校区
		Organization belongCampus= otmClass.getBlCampus();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(detel) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = detel;
				super.saveAccountChargeRecord(detel, targetConPrd, ProductType.ONE_ON_MANY, null, BigDecimal.valueOf(courseHour), 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, otmClass, otmClassCourse);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = detel.subtract(remainingAmountOfBasicAmount);
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);
				BigDecimal promotoinChargeHours = BigDecimal.valueOf(courseHour).subtract(realChargeHours);
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, ProductType.ONE_ON_MANY, null, realChargeHours, 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, otmClass, otmClassCourse);
				super.saveAccountChargeRecord(detel.subtract(remainingAmountOfBasicAmount), targetConPrd, ProductType.ONE_ON_MANY, 
						null, promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, otmClass, otmClassCourse);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = detel;
			super.saveAccountChargeRecord(detel, targetConPrd, ProductType.ONE_ON_MANY, null, BigDecimal.valueOf(courseHour), 
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, otmClass, otmClassCourse);
		}
		
		if(remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
			doAfterForNewCharge(detel, realChargeMoney, promotionChargeMoney, new BigDecimal(courseHour) , targetConPrd );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
		} 
	}

	@Override
	public void chargeLectureClass(LectureClassStudent stu) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);		
	}
}
