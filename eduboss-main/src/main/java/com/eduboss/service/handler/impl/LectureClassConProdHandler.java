package com.eduboss.service.handler.impl;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.DistributeType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.ContractProductSubject;
import com.eduboss.domain.Course;
import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.Product;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.LectureClassStudentVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.DataDictService;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.DateTools;


public class LectureClassConProdHandler extends ContractProductHandler {
		

	@Override
	protected void doBeforeDeleteContractProduct(
			ContractProduct contractProduct, Contract contractInDb) {
		super.doBeforeDeleteContractProduct(contractProduct, contractInDb);
		// 删除小班学生的报名
		lectureClassService.deleteStudentByContractProduct(contractProduct.getId());
	}
	
	@Override
	protected void doAfterCreateContractProduct(ContractProduct conProduct,
			ContractProductVo conProdVo, boolean updateInventory) {
		super.doAfterCreateContractProduct(conProduct, conProdVo, true);
		String lectureId = conProdVo.getLectureId();
		String studentId=conProduct.getContract().getStudent().getId();
		LectureClassStudentVo vo=new LectureClassStudentVo();
		vo.setStudentId(studentId);
		vo.setLectureId(lectureId);
		vo.setContractProductId(conProduct.getId());
		if (StringUtils.isNotBlank(lectureId)) {
			Response res=lectureClassService.addStudentToLectureClass(vo);
			if(res.getResultCode()!=0){//学生已报读了该讲座？
				throw new ApplicationException(res.getResultMessage());
			}
		}
	}
	
	@Override
    protected void doAfterCreateContractProduct(ContractProduct conProduct,
            ContractProductVo conProdVo) {
        doAfterCreateContractProduct(conProduct, conProdVo, true);
    }
	
	@Override
	public void calBasicContractProductForCreateNewContract(
			ContractProduct conProduct) throws Exception {
		super.calBasicContractProductForCreateNewContract(conProduct);
		Product miniProduct  = this.productDao.findById(conProduct.getProduct().getId());
		BigDecimal price=conProduct.getPrice();
		conProduct.setPrice(price);
		conProduct.setDealDiscount(BigDecimal.ONE);
		conProduct.setPlanAmount(price.multiply(conProduct.getQuantity().multiply(conProduct.getDealDiscount())));
	}
	
	@Override
	public void chargeLectureClass(LectureClassStudent stu) {
		super.chargeLectureClass(stu);
		ContractProduct cp=stu.getContractProduct();
		BigDecimal courseHour=BigDecimal.ONE;
		BigDecimal remainingAmount = cp.getRemainingAmount();
		
		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = cp.getPrice() == null? BigDecimal.ZERO:cp.getPrice(); 
		
		// 总价格 = 单价 * 课时 * 折扣  讲座默认为1   如果不是讲座类型就扣报名时的选择课时。
		if(!cp.getType().equals(ProductType.LECTURE)){
			if(cp.getType().equals(ProductType.ECS_CLASS)){
				courseHour=BigDecimal.ZERO;//目标班扣0
			}else{
				courseHour=stu.getAuditHours();
			}
		}
		
		BigDecimal detel = price.multiply(courseHour);
		
		BigDecimal remainingAmountOfBasicAmount =  cp.getRemainingAmountOfBasicAmount();
		
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		
		String transactionId = UUID.randomUUID().toString();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(detel) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = detel;
				this.saveAccountChargeRecord(detel, cp, ProductType.LECTURE, courseHour, 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId,stu);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = detel.subtract(remainingAmountOfBasicAmount);
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(cp.getPrice(), 2);
				BigDecimal promotoinChargeHours = courseHour.subtract(realChargeHours);
				this.saveAccountChargeRecord(remainingAmountOfBasicAmount, cp, ProductType.LECTURE, realChargeHours, 
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId,stu);
				this.saveAccountChargeRecord(detel.subtract(remainingAmountOfBasicAmount), cp, ProductType.LECTURE, 
						 promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, stu);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = detel;
			this.saveAccountChargeRecord(detel, cp, ProductType.LECTURE, courseHour, 
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId,stu);
		}
		
		if(remainingAmount.compareTo(detel)>=0 && (cp.getStatus() == ContractProductStatus.NORMAL ||cp.getStatus() == ContractProductStatus.STARTED )) {
			doAfterForNewCharge(detel, realChargeMoney, promotionChargeMoney,  courseHour, cp );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
		}
		
		if (cp.getType() == ProductType.ONE_ON_ONE_COURSE) {
			String currrentTime = DateTools.getCurrentDateTime();
			User currentUser = userService.getCurrentLoginUser();
			DataDict subject = dataDictService.findById("DAT0000000127");
			ContractProductSubject cpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(cp.getId(), "DAT0000000127");
			if (cpSubject != null) {
				if (cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()).compareTo(courseHour) < 0) {
					throw new ApplicationException("本合同产品的" + subject.getName() + "科目对应的分配课时不足这次扣费");
				} 
				cpSubject.setConsumeHours(cpSubject.getConsumeHours().add(courseHour));
			} else {
				throw new ApplicationException("本合同产品的" + subject.getName() + "科目没有分配课时，无法扣费");
			}
			cpSubject.setModifyTime(currrentTime);
			cpSubject.setModifyUser(currentUser);
			contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);

			CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(cp.getId(), cpSubject.getSubject(), 
					DistributeType.CONSUME, courseHour, cpSubject.getQuantity(), cpSubject.getConsumeHours(), 
					cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()), currentUser, currrentTime, cpSubject.getBlCampus());
			courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
		}
		
	}
	
	protected void saveAccountChargeRecord(BigDecimal chargeAmount, ContractProduct cp, ProductType productType,BigDecimal auditHours, ChargeType chargeType,
			PayType payType, Organization belongCampus, String transactionId,LectureClassStudent stu) {
		AccountChargeRecords record =  new AccountChargeRecords();
		// 旧数据 可能会是 price 价格为 0
//		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
		
		if(stu.getContractProduct()!=null && stu.getContractProduct().getType()!=null && stu.getContractProduct().getType().equals(ProductType.ECS_CLASS)){//目标班强制为1
			auditHours=BigDecimal.ONE;
		}
		
		record.setProduct(cp.getProduct());
		record.setAmount(chargeAmount);
		record.setContract(cp.getContract());
		record.setStudent(cp.getContract().getStudent());
		record.setOperateUser(userService.getCurrentLoginUser());
		record.setProductType(productType);
		record.setPayTime(DateTools.getCurrentDateTime());
		
		String transactionTime = null;
		record.setChargePayType(ChargePayType.CHARGE);
		record.setContractProduct(cp);
		record.setQuality(auditHours);
		record.setChargeType(chargeType);
		record.setPayType(payType);
		record.setBlCampusId(belongCampus);
		record.setTransactionId(transactionId);
		record.setLectureClassStudent(stu);
		record.setCourseMinutes(new BigDecimal(stu.getLectureClass().getLectureTimeLong()));
		if (ChargeType.NORMAL.equals(chargeType)) {
			transactionTime = stu.getLectureClass().getStartDate() + " 00:00:00";
		} else if (ChargeType.IS_NORMAL_INCOME.equals(chargeType)) {
			transactionTime = DateTools.getCurrentDateTime();
		}
		
		if (null != transactionTime) {
			record.setTransactionTime(transactionTime);
		}
		
		accountChargeRecordsDao.save(record);
		accountChargeRecordsDao.flush();
		pushChargeMsgToQueue(belongCampus.getId(), transactionTime, chargeAmount, auditHours, productType, chargeType, payType, ChargePayType.CHARGE);
	}

	@Override
	public void chargeMiniClass(ContractProduct targetConPrd, MiniClass miniClass,
			MiniClassCourse miniClassCourse, Double courseHour) {
		throw new ApplicationException(ErrorCode.NOT_SUPPORT_METHOD);		
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

}
