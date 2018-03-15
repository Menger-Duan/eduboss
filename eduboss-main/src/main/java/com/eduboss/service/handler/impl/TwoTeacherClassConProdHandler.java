package com.eduboss.service.handler.impl;

import com.eduboss.common.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.domainVo.StudentOrganizationVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.handler.ContractProductHandler;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


public class TwoTeacherClassConProdHandler extends ContractProductHandler {
	
	@Override
    protected void doAfterCreateContractProduct(ContractProduct conProduct,
            ContractProductVo conProdVo) {
        doAfterCreateContractProduct(conProduct, conProdVo, true);
    }

	@Override
	protected void doAfterCreateContractProduct(ContractProduct conProduct,
												ContractProductVo conProdVo, boolean updateInventory) {
		super.doAfterCreateContractProduct(conProduct, conProdVo, true);
		int twoClassId = conProdVo.getTwoClassId();
		String firstCourseDate = conProdVo.getFirstSchoolTime();
		String studentId = conProduct.getContract().getStudent().getId();
		try {
			if (twoClassId>0) {
				twoTeacherClassService.AddStudentToClasss(studentId, twoClassId, conProduct.getContract().getId(), conProduct.getId(), firstCourseDate);
			}
		}catch (ApplicationException e){
			e.printStackTrace();
			throw new ApplicationException(e.getErrorMsg());
		}catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("双师产品报读失败！");
		}

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

	public void chargeTwoTeacherClass(ContractProduct targetConPrd, TwoTeacherClassTwo twoTeacherClassTwo, TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent, Double courseHour){
		if (targetConPrd.getIsFrozen() == 0) {
			throw new ApplicationException("扣费合同产品已经冻结，不能进行扣费操作！");
		}
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice();
		// 总价格 = 单价 * 课时
		BigDecimal detel = price.multiply(BigDecimal.valueOf(courseHour));
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())
				&& !OrganizationType.GROUNP.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		// 重设扣费记录所属校区为小班课程的校区
		belongCampus = twoTeacherClassTwo.getBlCampus();
		String transactionId = UUID.randomUUID().toString();
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO)>0){
			if (remainingAmountOfBasicAmount.compareTo(detel)>=0){
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = detel;
//				super.saveAccountChargeRecord(detel, targetConPrd, ProductType.TWO_TEACHER, null, BigDecimal.valueOf(courseHour),
//						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null, null);
				super.saveAccountChargeRecordForTwoTeacher(detel, targetConPrd, ProductType.TWO_TEACHER, BigDecimal.valueOf(courseHour), ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, twoTeacherClassStudentAttendent);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = detel.subtract(remainingAmountOfBasicAmount);
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);
				BigDecimal promotoinChargeHours = BigDecimal.valueOf(courseHour).subtract(realChargeHours);
				super.saveAccountChargeRecordForTwoTeacher(remainingAmountOfBasicAmount, targetConPrd, ProductType.TWO_TEACHER, realChargeHours, ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, twoTeacherClassStudentAttendent);
//				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, ProductType.TWO_TEACHER, null, realChargeHours,
//						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, null, null, null, null, null);
				super.saveAccountChargeRecordForTwoTeacher(detel.subtract(remainingAmountOfBasicAmount), targetConPrd, ProductType.TWO_TEACHER, promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, twoTeacherClassStudentAttendent);
//				super.saveAccountChargeRecord(detel.subtract(remainingAmountOfBasicAmount), targetConPrd, ProductType.TWO_TEACHER,
//						null, promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null, null);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = detel;
//			super.saveAccountChargeRecord(detel, targetConPrd, ProductType.TWO_TEACHER, null, BigDecimal.valueOf(courseHour),
//					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null, null);
			super.saveAccountChargeRecordForTwoTeacher(detel, targetConPrd, ProductType.TWO_TEACHER, BigDecimal.valueOf(courseHour), ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, twoTeacherClassStudentAttendent);
		}

		if (remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )){
			doAfterForNewCharge(detel, realChargeMoney, promotionChargeMoney, new BigDecimal(courseHour) , targetConPrd );
		} else {
			throw new ApplicationException("本合同产品无法继续扣费");
		}

	}

}
