package com.eduboss.service.handler.impl;

import com.eduboss.common.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.IncomeDistributionVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.StringUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;



public class PromiseClassConProdHandler extends ContractProductHandler {
	
	
	@Override
	protected void checkIfCanDelete(ContractProduct contractProduct){
		super.checkIfCanDelete(contractProduct);
		// 检查是不是有考勤记录
		
		// 删除小班学生的报名
		List<MiniClass> miniClass = smallClassService.getAllMiniClassByContractProduct(contractProduct);
		//目标班可能报多个班
		for (MiniClass miniClass2 : miniClass) {
			 boolean hasAttendenceRecord = smallClassService.hasAttendenceRecordByMiniClassAndStudent(miniClass2.getMiniClassId(), contractProduct.getContract().getStudent().getId());
	            if (hasAttendenceRecord) {
	                throw new ApplicationException("这个学生的小班有考勤记录");
	            }
		}
		
	}
	
	@Override
	public void calBasicContractProductForCreateNewContract(
			ContractProduct conProduct) throws Exception {
		super.calBasicContractProductForCreateNewContract(conProduct);
//		Product promiseClassProduct  = this.productDao.findById(conProduct.getProduct().getId());
//		BigDecimal price = promiseClassProduct.getPrice();
//		// 初始化合同 可以修改单价
//		if(conProduct.getContract().getContractType()==ContractType.INIT_CONTRACT){price=conProduct.getPrice();}
		// 合同产品单价改成直接用合同产品填的单价
		BigDecimal price=conProduct.getPrice();
		conProduct.setPrice(price);
		conProduct.setDealDiscount(BigDecimal.ONE);
		conProduct.setPlanAmount(price.multiply(conProduct.getQuantity().multiply(conProduct.getDealDiscount())));
		// 加入目标班的 产品内部课时！！！！！！！！！！！！！！
		// conProduct.setQuantityInProduct(promiseClassProduct.get.....);
	}
	
	@Override
	public void chargeOneOnOneClass(Contract contract, ContractProduct targetConPrd,
			BigDecimal chargeMeney, BigDecimal auditedCourseHours, Course course, String transactionId) {
		//BigDecimal remainingAmount = getRemainAmountOfContractProduct(targetConPrd);
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
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);		
	}

	@Override
	public void chargePromiseClassProduct(Contract contract,
			ContractProduct targetConPrd, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, PromiseClassRecord promiseClassRecord) {
		
		super.chargePromiseClassProduct(contract, targetConPrd, chargeMoneyAmount, chargeQuantity, promiseClassRecord);
		//BigDecimal remainingAmount = getRemainAmountOfContractProduct(targetConPrd);
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		
		
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		
		PromiseClass promiseClass = promiseClassRecord.getPromiseClass();
		if (null != promiseClass && StringUtil.isNotBlank(promiseClass.getId())) {
			promiseClass = promiseClassService.findPromiseClassById(promiseClass.getId());
		}
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType()) && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		
		// 重设扣费记录所属校区为目标班校区
		belongCampus= promiseClass.getpSchool();
		if (null == promiseClass || null == promiseClass.getpSchool()) {
			throw new ApplicationException("目标班没有关联到校区！");
		}
		
		String transactionId = UUID.randomUUID().toString();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(chargeMoneyAmount) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = chargeMoneyAmount;
				super.saveAccountChargeRecord(chargeMoneyAmount, targetConPrd, ProductType.ECS_CLASS, null, chargeQuantity,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, promiseClassRecord, null , null);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = chargeMoneyAmount.subtract(remainingAmountOfBasicAmount);
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, ProductType.ECS_CLASS, null, chargeQuantity, 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, promiseClassRecord, null , null);
				super.saveAccountChargeRecord(chargeMoneyAmount.subtract(remainingAmountOfBasicAmount), targetConPrd, ProductType.ECS_CLASS, null, 
						chargeQuantity, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, promiseClassRecord, null , null);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = chargeMoneyAmount;
			super.saveAccountChargeRecord(chargeMoneyAmount, targetConPrd, ProductType.ECS_CLASS, null, chargeQuantity, 
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, promiseClassRecord, null , null);
		}
		
		// 只有 Normal 和 started 的值才可以 扣费
		if(remainingAmount.compareTo(chargeMoneyAmount)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
			// updateContractAndStuViewForNewCharge(chargeMeney, auditedCourseHours , targetConPrd );
			doAfterForNewCharge(chargeMoneyAmount, realChargeMoney, promotionChargeMoney, chargeQuantity , targetConPrd );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
		} 
		
	}
	
	public void chargePromiseClassProductWithoutClass(Contract contract,
			ContractProduct targetConPrd, BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, PromiseClassRecord promiseClassRecord,Organization belongCampus,String transactionTime) {
		
		//BigDecimal remainingAmount = getRemainAmountOfContractProduct(targetConPrd);
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		String transactionId = UUID.randomUUID().toString();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(chargeMoneyAmount) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = chargeMoneyAmount;
				super.saveAccountChargeRecord(chargeMoneyAmount, targetConPrd, ProductType.ECS_CLASS, null, chargeQuantity,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, promiseClassRecord, null , null,transactionTime);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = chargeMoneyAmount.subtract(remainingAmountOfBasicAmount);
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, ProductType.ECS_CLASS, null, chargeQuantity, 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, promiseClassRecord, null , null,transactionTime);
				super.saveAccountChargeRecord(chargeMoneyAmount.subtract(remainingAmountOfBasicAmount), targetConPrd, ProductType.ECS_CLASS, null, 
						chargeQuantity, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, promiseClassRecord, null , null,transactionTime);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = chargeMoneyAmount;
			super.saveAccountChargeRecord(chargeMoneyAmount, targetConPrd, ProductType.ECS_CLASS, null, chargeQuantity, 
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, promiseClassRecord, null , null,transactionTime);
		}
		
		// 只有 Normal 和 started 的值才可以 扣费
		if(remainingAmount.compareTo(chargeMoneyAmount)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
			doAfterForNewCharge(chargeMoneyAmount, realChargeMoney, promotionChargeMoney, chargeQuantity , targetConPrd );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
		} 
		
	}
	
	@Override
	protected void doBeforeDeleteContractProduct(
			ContractProduct contractProduct, Contract contractInDb) {
		super.doBeforeDeleteContractProduct(contractProduct, contractInDb);
		 
//		String promiseClassId = null;
		// 删除目标班报班，未扣费结算记录
		List<PromiseStudent> studentList = promiseClassService.getStudentListByContractProduct(contractProduct);
		for (PromiseStudent promiseStudent: studentList) {
			promiseClassService.deletePrmoseClassRecordByProClaIdAndStuId(promiseStudent.getPromiseClass().getId(), promiseStudent.getStudent().getId());
			promiseClassService.deletePromiseStudent(promiseStudent);
//			if (!promiseClassId.equals(promiseStudent.getPromiseClass().getId())) {
//				promiseClassId = promiseStudent.getPromiseClass().getId();
//				promiseClassService.deletePromiseClassByProClaId(promiseClassId);
//			}
		}
		
		// 删除小班学生的报名
		List<MiniClass> miniClass = smallClassService.getAllMiniClassByContractProduct(contractProduct);
		//目标班可能报多个班
		for (MiniClass miniClass2 : miniClass) {
			//删除考勤记录  （未扣费的）
			smallClassService.deleteAttendenceRecordByMiniClassAndStudent(miniClass2.getMiniClassId(), contractInDb.getStudent().getId());
			//删除报班信息
            smallClassService.deleteSingleStudentInMiniClasss(contractInDb.getStudent().getId(), miniClass2.getMiniClassId());
		}
		
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
			BigDecimal realAmount=cp.getPaidAmount();//实际金额等于收款金额，未收款转为优惠
			cp.setPromotionAmount(cp.getPlanAmount().subtract(realAmount));
			cp.setRealAmount(cp.getPaidAmount());
			cp.setPaidStatus(ContractProductPaidStatus.PAID);
			try {
				transferAmountToElectronicAdd(BigDecimal.ZERO, BigDecimal.ZERO, cp,cp.getRemainingAmountOfBasicAmount(), null);
				completeContractProduct(BigDecimal.ZERO, BigDecimal.ZERO, cp.getPromotionAmount(), cp, BigDecimal.ZERO);
			} catch (Exception e) {
				throw new ApplicationException("缩单时，结束目标班合同产品错误。");
			}
		}else if(cp.getRealAmount().compareTo(BigDecimal.ZERO)>0  && cp.getPaidAmount().compareTo(BigDecimal.ZERO)==0){
			moneyArrangeLogService.deleteByContractProductId(cp.getId());
			contractProductDao.deleteById(cp.getId()); // 删除该产品
			contractDao.flush();
		}
		
	}

	public void midReturnFee(ContractProduct cp){
		try {
			transferAmountToElectronicAdd(BigDecimal.ZERO, BigDecimal.ZERO, cp,cp.getRemainingAmountOfBasicAmount(), null);
			saveNormalIncome(cp, BigDecimal.ZERO, cp.getPromotionAmount());
			cp.setStatus(ContractProductStatus.CLOSE_PRODUCT);
			doAfterCompleteContractProduct(BigDecimal.ZERO, BigDecimal.ZERO ,BigDecimal.ZERO,  cp);
		} catch (Exception e) {
			throw new ApplicationException("中途退费时，结束目标班合同产品错误。");
		}
	}

	public void midReturnFeeToCustomer(ContractProduct cp, List<IncomeDistributionVo> list){

		String transactionId = UUID.randomUUID().toString();
		saveReturnAccRecord(cp, cp.getRemainingAmountOfBasicAmount(), cp.getRemainingAmountOfPromotionAmount(), false, transactionId);
		studentService.closeContractProduct(cp.getContract().getStudent().getId(), cp.getRemainingAmountOfBasicAmount(), BigDecimal.ZERO,
				BigDecimal.ZERO, cp.getRemainingAmountOfBasicAmount(), "精英班中途退费", null, cp.getId(),
				null,null,null, null, null, list, null);
		try {
			if(cp.getRemainingAmountOfPromotionAmount().compareTo(BigDecimal.ZERO)>0) {
				saveNormalIncome(cp,BigDecimal.ZERO,cp.getRemainingAmountOfPromotionAmount());  // 划归
			}
		}catch(Exception e){
			throw new ApplicationException("优惠对消出错");
		}
		cp.setStatus(ContractProductStatus.CLOSE_PRODUCT);
		updateContractStatus(cp.getContract());

	}
}
