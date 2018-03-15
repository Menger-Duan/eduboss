package com.eduboss.service.handler.impl;

import java.math.BigDecimal;

import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.DistributeType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.ContractProductSubject;
import com.eduboss.domain.Course;
import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domain.User;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.DateTools;


public class OneOnOneConProdHandler extends ContractProductHandler {
	
	
	@Override
	public void calBasicContractProductForCreateNewContract(
			ContractProduct conProduct) throws Exception {
		super.calBasicContractProductForCreateNewContract(conProduct);
//		Product oneProduct  = this.productDao.findById(conProduct.getProduct().getId());
//		BigDecimal price = oneProduct.getPrice();
//		// 初始化合同 可以修改单价
//		if(conProduct.getContract().getContractType()==ContractType.INIT_CONTRACT){
//			price=conProduct.getPrice();
//		}
		// 合同产品单价改成直接用合同产品填的单价
		BigDecimal price=conProduct.getPrice();
		conProduct.setPrice(price);
		conProduct.setDealDiscount(BigDecimal.ONE);
		conProduct.setPlanAmount(price.multiply(conProduct.getQuantity().multiply(conProduct.getDealDiscount())));
	}
	
	@Override
	public void chargeOneOnOneClass(Contract contract, ContractProduct targetConPrd,
			BigDecimal chargeMeney, BigDecimal auditedCourseHours, Course course, String transactionId) {
		
		super.chargeOneOnOneClass(contract, targetConPrd, chargeMeney, auditedCourseHours, course, transactionId);
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		// 重设扣费记录所属校区为课程的校区
		belongCampus= course.getBlCampusId();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(chargeMeney) >= 0) {
				realChargeMoney = chargeMeney;
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				super.saveAccountChargeRecord(chargeMeney, targetConPrd, ProductType.ONE_ON_ONE_COURSE, course, auditedCourseHours,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null , null);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);
				BigDecimal promotoinChargeHours = auditedCourseHours.subtract(realChargeHours);
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = chargeMeney.subtract(remainingAmountOfBasicAmount);
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, ProductType.ONE_ON_ONE_COURSE, course, realChargeHours, 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null , null);
				super.saveAccountChargeRecord(chargeMeney.subtract(remainingAmountOfBasicAmount), targetConPrd, ProductType.ONE_ON_ONE_COURSE, course, 
						promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null , null);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = chargeMeney;
			super.saveAccountChargeRecord(chargeMeney, targetConPrd, ProductType.ONE_ON_ONE_COURSE, course, auditedCourseHours, 
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null , null);
		}
		
		// 只有 Normal 和 started 的值才可以 扣费
		if(remainingAmount.compareTo(chargeMeney)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
			// updateContractAndStuViewForNewCharge(chargeMeney, auditedCourseHours , targetConPrd );
			doAfterForNewCharge(chargeMeney, realChargeMoney, promotionChargeMoney, auditedCourseHours , targetConPrd );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
		} 
		
		String currrentTime = DateTools.getCurrentDateTime();
		User currentUser = userService.getCurrentLoginUser();
		
		ContractProductSubject cpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(targetConPrd.getId(), course.getSubject().getId());
		if (cpSubject != null) {
			if (cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()).compareTo(auditedCourseHours) < 0) {
				throw new ApplicationException("本合同产品的" + course.getSubject().getName() + "科目对应的分配课时不足这次扣费");
			} 
			cpSubject.setConsumeHours(cpSubject.getConsumeHours().add(auditedCourseHours));
		} else {
			throw new ApplicationException("本合同产品的" + course.getSubject().getName() + "科目没有分配课时，无法扣费");
//			cpSubject = new ContractProductSubject();
//			cpSubject.setQuantity(chargeHours);
//			cpSubject.setContractProduct(targetConPrd);
//			cpSubject.setSubject(course.getSubject());
//			cpSubject.setConsumeHours(chargeHours);
//			cpSubject.setCreateTime(currrentTime);
//			cpSubject.setCreateByStaff(currentUser);
		}
		cpSubject.setModifyTime(currrentTime);
		cpSubject.setModifyUser(currentUser);
		contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
		
		CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(targetConPrd.getId(), cpSubject.getSubject(), 
				DistributeType.CONSUME, auditedCourseHours, cpSubject.getQuantity(), cpSubject.getConsumeHours(), 
				cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()), currentUser, currrentTime, cpSubject.getBlCampus());
		courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
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
}
