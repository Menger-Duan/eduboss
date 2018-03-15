package com.eduboss.service.handler.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.eduboss.common.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.ProductVo;
import org.apache.commons.lang3.StringUtils;

import com.eduboss.dao.StudentOrganizationDao;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.domainVo.StudentOrganizationVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.utils.DateTools;


public class MiniClassConProdHandler extends ContractProductHandler {
		
	
	@Override
	protected void checkIfCanDelete(ContractProduct contractProduct){
		super.checkIfCanDelete(contractProduct);
		// 检查是不是有考勤记录
		MiniClass miniClass = smallClassService.getMiniClassByContractProduct(contractProduct);
		if(miniClass != null) {
			//有已扣费的考勤记录才不允许删除，其他状态应该是可以删除的    modify by Yao    2016-06-17
            boolean hasAttendenceRecord = smallClassService.hasAttendenceRecordByMiniClassAndStudent(miniClass.getMiniClassId(), contractProduct.getContract().getStudent().getId());
            if (hasAttendenceRecord) {
                throw new ApplicationException("这个学生的小班有考勤记录");
            }
        }
		
	}
	
	
	
	@Override
	protected void doBeforeDeleteContractProduct(
			ContractProduct contractProduct, Contract contractInDb) {
		super.doBeforeDeleteContractProduct(contractProduct, contractInDb);
		
		// 删除小班学生的报名
		MiniClass miniClass = smallClassService.getMiniClassByContractProduct(contractProduct);
		if(miniClass != null) {
			//删除考勤记录  （未扣费的）
			smallClassService.deleteAttendenceRecordByMiniClassAndStudent(miniClass.getMiniClassId(), contractInDb.getStudent().getId());
			//删除报班信息
            smallClassService.deleteSingleStudentInMiniClasss(contractInDb.getStudent().getId(), miniClass.getMiniClassId());
        }
	}
	
	@Override
    protected void doAfterCreateContractProduct(ContractProduct conProduct,
            ContractProductVo conProdVo) {
        doAfterCreateContractProduct(conProduct, conProdVo, true);
    }

	@Override
	protected void doAfterCreateContractProduct(ContractProduct conProduct,
			ContractProductVo conProdVo, boolean updateInventory) {
		super.doAfterCreateContractProduct(conProduct, conProdVo, updateInventory);
		String miniClassId = conProdVo.getMiniClassId();
		String continueMiniClassId = conProdVo.getContinueMiniClassId();
		String extendMiniClassId = conProdVo.getExtendMiniClassId();
    	String firstCourseDate = conProdVo.getFirstSchoolTime();
    	String studentId = conProduct.getContract().getStudent().getId();
		try {
			if (StringUtils.isNotBlank(miniClassId)) {
				smallClassService.AddStudentForMiniClasss(studentId, miniClassId, conProduct.getContract().getId() ,conProduct.getId(),firstCourseDate, true, updateInventory);
				MiniClassVo miniClassvo = smallClassService.findMiniClassById(miniClassId);
				List<StudentOrganizationVo> voList = studentService.getStudentOrganization(studentId);
				boolean isExit = false;
				for (StudentOrganizationVo vo : voList) {
					if (vo.getOrganization().equals(miniClassvo.getBlCampusId())) {
						isExit = true;
						break;
					}
				}
				if (!isExit) {
					StudentOrganization studentOrganization =new StudentOrganization();
					studentOrganization.setOrganization(new Organization(miniClassvo.getBlCampusId()));
					studentOrganization.setStudent(new Student(studentId));
					studentOrganization.setIsMainOrganization("0");
					studentOrganizationDao.save(studentOrganization);
				}
			}
			smallClassService.addMiniClassRelation(studentId, miniClassId, continueMiniClassId, extendMiniClassId);
		} catch (ApplicationException ae) {
		    throw ae;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("该学生已在本小班报读，请先退班处理再报进班或者先报合同产品，再进去小班报名调整关联产品！");
		}
		
	}

	@Override
	public void calBasicContractProductForCreateNewContract(
			ContractProduct conProduct) throws Exception {
		super.calBasicContractProductForCreateNewContract(conProduct);
		Product miniProduct  = this.productDao.findById(conProduct.getProduct().getId());
		
		// 增加判断是否超出小班产品的总课时
		if (miniProduct.getMiniClassTotalhours().compareTo(conProduct.getQuantity()) < 0) {
			throw new ApplicationException("小班合同产品（" + miniProduct.getName() + "）的数量不可大于产品建议总课时数");
		}
		
//		BigDecimal price = miniProduct.getPrice();
//		// 初始化合同 可以修改单价
//		if(conProduct.getContract().getContractType()==ContractType.INIT_CONTRACT){price=conProduct.getPrice();}
		// 合同产品单价改成直接用合同产品填的单价
		BigDecimal price=conProduct.getPrice();
		conProduct.setPrice(price);
		conProduct.setDealDiscount(BigDecimal.ONE);
		conProduct.setPlanAmount(price.multiply(conProduct.getQuantity().multiply(conProduct.getDealDiscount())));
	}


	@Override
	public void saveAccountChargeRecord(BigDecimal chargeAmount, ContractProduct targetConPrd, ProductType productType,
										Course course, BigDecimal auditedCourseHours, ChargeType chargeType,
										PayType payType, Organization belongCampus, String transactionId, MiniClass miniClass, MiniClassCourse miniClassCourse,
										PromiseClassRecord promiseClassRecord, OtmClass otmClass, OtmClassCourse otmClassCourse){
		super.saveAccountChargeRecord(chargeAmount, targetConPrd, productType, course, auditedCourseHours, chargeType, payType, belongCampus, transactionId, miniClass, miniClassCourse, promiseClassRecord, otmClass, otmClassCourse);
	}
	@Override
	public void doAfterForNewCharge(BigDecimal chargeMoneyAmount, BigDecimal realChargeAmount, BigDecimal promotionChargeAmount,
									BigDecimal chargeQuantity, ContractProduct contractProduct){
		super.doAfterForNewCharge(chargeMoneyAmount, realChargeAmount, promotionChargeAmount, chargeQuantity, contractProduct);
	}


	@Override
	public void chargeMiniClass(ContractProduct targetConPrd, MiniClass miniClass,
			MiniClassCourse miniClassCourse, Double courseHour) {

		if (promiseClassService.isYearAfter2018(targetConPrd)){
			chargePromiseClassAfter2018(targetConPrd, miniClass, miniClassCourse, courseHour);
			return;
		}

		super.chargeMiniClass(targetConPrd, miniClass, miniClassCourse, courseHour);
//		BigDecimal remainingAmount = getRemainAmountOfContractProduct(targetConPrd);
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		
		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
		// 总价格 = 单价 * 课时 * 折扣
		BigDecimal detel = price.multiply(targetConPrd.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));
		
		if(targetConPrd.getType().equals(ProductType.ECS_CLASS) && targetConPrd.getProduct().getCourseSeries()!=null && targetConPrd.getProduct().getCourseSeries().getId().equals("fixedmoney")){//如果是目标班报读小班，则扣费金额0
			detel=BigDecimal.ZERO;
		}else{
			double consumeHours=miniClassStudentAttendentDao.getSumAttendentHours(miniClassCourse.getMiniClass().getMiniClassId(),targetConPrd.getContract().getStudent().getId(), MiniClassStudentChargeStatus.CHARGED);
			List<PromiseClassSubject> list = promiseClassSubjectDao.findPromiseSubjectByClassIdAndStudentId(miniClassCourse.getMiniClass().getMiniClassId(), targetConPrd.getContract().getStudent().getId());
			if (list.size() > 0 ) {
				PromiseClassSubject pro = list.get(0);
				if(pro.getCourseHours().compareTo(BigDecimal.valueOf(courseHour).add(pro.getConsumeCourseHours()))<0){
					throw new ApplicationException("学生“"+targetConPrd.getContract().getStudent().getName()+"”在目标班分配给本小班的课时数不足，请重新分配后再执行扣费！ ");
				}
			}
		}
		
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())
				&& !OrganizationType.GROUNP.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		// 重设扣费记录所属校区为小班课程的校区
		belongCampus= miniClass.getBlCampus();
		String transactionId = UUID.randomUUID().toString();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(detel) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = detel;
				super.saveAccountChargeRecord(detel, targetConPrd, targetConPrd.getType(), null, BigDecimal.valueOf(courseHour),
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = detel.subtract(remainingAmountOfBasicAmount);
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);
				BigDecimal promotoinChargeHours = BigDecimal.valueOf(courseHour).subtract(realChargeHours);
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, targetConPrd.getType(), null, realChargeHours,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
				super.saveAccountChargeRecord(detel.subtract(remainingAmountOfBasicAmount), targetConPrd, targetConPrd.getType(),
						null, promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = detel;
			super.saveAccountChargeRecord(detel, targetConPrd, targetConPrd.getType(), null, BigDecimal.valueOf(courseHour),
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
		}
		
		if(remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
//			updateContractAndStuViewForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
			doAfterForNewCharge(detel, realChargeMoney, promotionChargeMoney, new BigDecimal(courseHour) , targetConPrd );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
		} 
		
	}

	private void chargePromiseClassAfter2018(ContractProduct targetConPrd, MiniClass miniClass, MiniClassCourse miniClassCourse, Double courseHour) {
		super.chargeMiniClass(targetConPrd, miniClass, miniClassCourse, courseHour);
//
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();

		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice();
		// 总价格 = 单价 * 课时 * 折扣
		BigDecimal detel = price.multiply(targetConPrd.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));

		Contract contract = targetConPrd.getContract();
		String studentName = "";
		if (contract!=null){
			Student student = contract.getStudent();
			studentName +=student.getName();
		}

		if (!isEnoughAmount(targetConPrd, detel)){
			throw new ApplicationException(studentName+"的合同产品无法继续扣费");
		}


		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		remainingAmountOfBasicAmount = getRealRemainingAmountOfBasicAmount(targetConPrd, remainingAmountOfBasicAmount);
		BigDecimal promotionAmount = targetConPrd.getPromotionAmount();
		promotionAmount = getPromotionRemainingAmount(targetConPrd, promotionAmount);
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())
				&& !OrganizationType.GROUNP.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		// 重设扣费记录所属校区为小班课程的校区
		belongCampus= miniClass.getBlCampus();
		String transactionId = UUID.randomUUID().toString();

		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;

		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(detel) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				detel = BigDecimal.ZERO;
				realChargeMoney = detel;
				super.saveAccountChargeRecord(detel, targetConPrd, targetConPrd.getType(), null, BigDecimal.valueOf(courseHour),
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
			} else {
				promotionChargeMoney = detel.subtract(remainingAmountOfBasicAmount);
				detel = BigDecimal.ZERO;
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;

				if (promotionAmount.compareTo(promotionChargeMoney)<0){
					throw new ApplicationException("优惠金额不够扣");
				}
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);//实收课时
				BigDecimal promotoinChargeHours = BigDecimal.valueOf(courseHour).subtract(realChargeHours);//优惠课时
				remainingAmountOfBasicAmount = BigDecimal.ZERO;
				realChargeMoney = BigDecimal.ZERO;
				promotionChargeMoney = BigDecimal.ZERO;
				super.saveAccountChargeRecord(remainingAmountOfBasicAmount, targetConPrd, targetConPrd.getType(), null, realChargeHours,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
				super.saveAccountChargeRecord(detel.subtract(remainingAmountOfBasicAmount), targetConPrd, targetConPrd.getType(),
						null, promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			if (promotionAmount.compareTo(detel)<0){
				throw new ApplicationException("优惠金额不够扣");
			}
			detel= BigDecimal.ZERO;
			promotionChargeMoney = detel;

			super.saveAccountChargeRecord(detel, targetConPrd, targetConPrd.getType(), null, BigDecimal.valueOf(courseHour),
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null);
		}

		if(remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED ) ) {
//			updateContractAndStuViewForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
			doAfterForNewCharge(detel, realChargeMoney, promotionChargeMoney, new BigDecimal(courseHour) , targetConPrd );

			//当季第一次课消的时候扣取VIP服务费，VIP服务费不够则不能考勤
			chargeVipFeeAfter2018(targetConPrd);

		} else {
			throw new ApplicationException("本合同产品无法继续扣费");
		}



	}

	private void chargeVipFeeAfter2018(ContractProduct cp) {
		//扣除的季节数
		List<Map> list=promiseClassSubjectDao.getEcsContractChargeInfo(cp.getId());
		BigDecimal i =BigDecimal.ZERO;
		for(Map map:list){
			if (map.get("totalQuantity")!=null){
				BigDecimal totalQuantity=(BigDecimal) map.get("totalQuantity");
				if (totalQuantity.compareTo(BigDecimal.ZERO)>0){
					i=i.add(BigDecimal.ONE);
				}
			}
		}
		ProductVo productVo=productService.findProductById(cp.getProduct().getId());

		//确定关联的VIP服务费ID
		String sonProductId ="";
		Map<String,String> sonProductMap = new HashMap<>();
		for(ProductVo p:productVo.getSonProduct()){
			sonProductMap.put(p.getId(),p.getId());
			sonProductId=p.getId();
		}

		ContractProduct chargeVipContractProduct = null;

		if (cp!=null && cp.getContract().getOtherProducts()!=null && sonProductMap.size()==1&& org.apache.commons.lang.StringUtils.isNotBlank(sonProductId)){
			for (ContractProduct conp :cp.getContract().getOtherProducts()){
				if (sonProductId.equals(conp.getProduct().getId()) ){//&& (conp.getStatus() == ContractProductStatus.NORMAL||conp.getStatus() == ContractProductStatus.STARTED)
					if (conp.getQuantity().compareTo(i)>=0 && conp.getProduct()!=null&& conp.getProduct().getId().equals(sonProductId)){
						chargeVipContractProduct = conp;
						break;
					}
				}
			}
		}else {
			throw new ApplicationException("关联vip服务费不正确");
		}
		if (chargeVipContractProduct==null){
			throw new ApplicationException("关联vip服务费不正确");
		}

		BigDecimal remainingAmount = chargeVipContractProduct.getRemainingAmount();
		BigDecimal quantityOfVip = getQuantityOfVip(chargeVipContractProduct);
		BigDecimal price = chargeVipContractProduct.getPrice();
		BigDecimal chargeMoneyAmount = i.subtract(quantityOfVip).multiply(price);

		if (i.subtract(quantityOfVip).compareTo(BigDecimal.ZERO)==0){
			return;
		}

		if(!(remainingAmount.compareTo(chargeMoneyAmount)>=0 && (chargeVipContractProduct.getStatus() == ContractProductStatus.NORMAL ||chargeVipContractProduct.getStatus() == ContractProductStatus.STARTED ))) {
			throw new ApplicationException(cp.getContract().getStudent().getName()+"的目标班VIP服务费扣费失败，请重新分配资金或补交费用后再执行扣费！");
		}
		if (chargeVipContractProduct!=null&&i.compareTo(BigDecimal.ZERO)>0){

			if (i.compareTo(quantityOfVip)>0){
				OtherConProdHandler handler=new OtherConProdHandler();
				handler.chargeEcsOtherProduct(chargeVipContractProduct,i.subtract(quantityOfVip));
			}
		}


	}

	private BigDecimal getQuantityOfVip(ContractProduct chargeVipContractProduct) {

		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		sql.append("SELECT * FROM account_charge_records WHERE CONTRACT_PRODUCT_ID=:contractProductId  AND CHARGE_PAY_TYPE='CHARGE' AND IS_WASHED='FALSE' group by TRANSACTION_ID ");
		params.put("contractProductId", chargeVipContractProduct.getId());
		List<AccountChargeRecords> accountChargeRecordsList = accountChargeRecordsDao.findBySql(sql.toString(), params);

		BigDecimal vipCharge = BigDecimal.ZERO;

		for (AccountChargeRecords a : accountChargeRecordsList){
			vipCharge = vipCharge.add(a.getQuality());
		}
		return vipCharge;

	}

	/**
	 * 2019年后扣费后优惠剩余金额
	 * @param targetConPrd
	 * @param promotionAmount
	 * @return
	 */
	private BigDecimal getPromotionRemainingAmount(ContractProduct targetConPrd, BigDecimal promotionAmount) {
		List<AccountChargeRecords> accountChargeRecordsList = accountChargeRecordsDao.getAccountChargeRecords(targetConPrd);
		BigDecimal price = targetConPrd.getPrice();
		BigDecimal dealDiscount = targetConPrd.getDealDiscount();
		price = price.multiply(dealDiscount);

		BigDecimal promotionConsumeAmount = BigDecimal.ZERO;
		for (AccountChargeRecords record : accountChargeRecordsList){
			if (record.getPayType()==PayType.PROMOTION){
				promotionConsumeAmount =promotionConsumeAmount.add(record.getQuality().multiply(price));
			}
		}
		promotionAmount = promotionAmount.subtract(promotionConsumeAmount);
		return promotionAmount;
	}

	/**
	 * 2019年后扣费后实际剩余金额
	 * @param targetConPrd
	 * @param remainingAmountOfBasicAmount
	 * @return
	 */
	private BigDecimal getRealRemainingAmountOfBasicAmount(ContractProduct targetConPrd, BigDecimal remainingAmountOfBasicAmount) {
		List<AccountChargeRecords> accountChargeRecordsList = accountChargeRecordsDao.getAccountChargeRecords(targetConPrd);
		BigDecimal price = targetConPrd.getPrice();
		BigDecimal dealDiscount = targetConPrd.getDealDiscount();
		price = price.multiply(dealDiscount);
		BigDecimal realConsumeAmount = BigDecimal.ZERO;
		for (AccountChargeRecords record : accountChargeRecordsList){
			if (record.getPayType()==PayType.REAL){
				realConsumeAmount = realConsumeAmount.add(record.getQuality().multiply(price));
			}
		}
		remainingAmountOfBasicAmount =remainingAmountOfBasicAmount.subtract(realConsumeAmount);

		return remainingAmountOfBasicAmount;
	}

	/**
	 * 是否足够的钱来扣
	 * @param targetConPrd
	 * @param detel  要扣的钱
	 * @return
	 */
	private boolean isEnoughAmount(ContractProduct targetConPrd, BigDecimal detel) {
		List<AccountChargeRecords> accountChargeRecordsList = accountChargeRecordsDao.getAccountChargeRecords(targetConPrd);
		BigDecimal alreadyCharge = BigDecimal.ZERO;
		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice();
		for (AccountChargeRecords a : accountChargeRecordsList){
			alreadyCharge = a.getQuality().multiply(price).add(alreadyCharge);
		}
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		if (remainingAmount.compareTo(BigDecimal.ZERO)>0){
			BigDecimal allAmount = BigDecimal.ZERO;
			if (targetConPrd.getPaidStatus()== ContractProductPaidStatus.PAID){
				allAmount = remainingAmount.add(targetConPrd.getPromotionAmount());
			}else {
				allAmount = remainingAmount;
			}

			return allAmount.compareTo(detel.add(alreadyCharge))>=0;
		}else {
			return false;
		}
	}

	@Override
	public void chargeMiniClass_Edu(ContractProduct targetConPrd, MiniClass miniClass,
			MiniClassCourse miniClassCourse, Double courseHour,User user) {
		super.chargeMiniClass_Edu(targetConPrd, miniClass, miniClassCourse, courseHour,user);
//		BigDecimal remainingAmount = getRemainAmountOfContractProduct(targetConPrd);
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		
		// 旧数据 可能会是 price 价格为 0
		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
		// 总价格 = 单价 * 课时 * 折扣
		BigDecimal detel = price.multiply(targetConPrd.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));
		
		if(targetConPrd.getType().equals(ProductType.ECS_CLASS) && targetConPrd.getProduct().getCourseSeries()!=null && targetConPrd.getProduct().getCourseSeries().getId().equals("fixedmoney")){//如果是目标班报读小班，则扣费金额0
			detel=BigDecimal.ZERO;
		}else{
			double consumeHours=miniClassStudentAttendentDao.getSumAttendentHours(miniClassCourse.getMiniClass().getMiniClassId(),targetConPrd.getContract().getStudent().getId(), MiniClassStudentChargeStatus.CHARGED);
			List<PromiseClassSubject> list = promiseClassSubjectDao.findPromiseSubjectByClassIdAndStudentId(miniClassCourse.getMiniClass().getMiniClassId(), targetConPrd.getContract().getStudent().getId());
			if (list.size() > 0 ) {
				PromiseClassSubject pro = list.get(0);
				if(pro.getCourseHours().compareTo(BigDecimal.valueOf(courseHour).add(pro.getConsumeCourseHours()))<0){
					throw new ApplicationException("学生“"+targetConPrd.getContract().getStudent().getName()+"”在目标班分配给本小班的课时数不足，请重新分配后再执行扣费！ ");
				}
			}
		}
		
		BigDecimal remainingAmountOfBasicAmount =  targetConPrd.getRemainingAmountOfBasicAmount();
		Organization belongCampus= userService.getBelongBranchByUserId(user.getUserId());
		
		
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())
				&& !OrganizationType.GROUNP.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		// 重设扣费记录所属校区为小班课程的校区
		belongCampus= miniClass.getBlCampus();
		String transactionId = UUID.randomUUID().toString();
		
		BigDecimal realChargeMoney = BigDecimal.ZERO;
		BigDecimal promotionChargeMoney = BigDecimal.ZERO;
		
		if (remainingAmountOfBasicAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (remainingAmountOfBasicAmount.compareTo(detel) >= 0) {
				// 实收金额大于等于需扣费金额,产生一条实收金额的扣费记录
				realChargeMoney = detel;
				super.saveAccountChargeRecord_Edu(detel, targetConPrd, targetConPrd.getType(), null, BigDecimal.valueOf(courseHour),
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null,user);
			} else {
				// 实收金额小于需扣费金额,先产生一条实收金额的扣费记录再产生一条优惠金额扣费记录
				realChargeMoney = remainingAmountOfBasicAmount;
				promotionChargeMoney = detel.subtract(remainingAmountOfBasicAmount);
				BigDecimal realChargeHours = remainingAmountOfBasicAmount.divide(targetConPrd.getPrice(), 2);
				BigDecimal promotoinChargeHours = BigDecimal.valueOf(courseHour).subtract(realChargeHours);
				super.saveAccountChargeRecord_Edu(remainingAmountOfBasicAmount, targetConPrd, targetConPrd.getType(), null, realChargeHours,
						ChargeType.NORMAL, PayType.REAL, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null,user);
				super.saveAccountChargeRecord_Edu(detel.subtract(remainingAmountOfBasicAmount), targetConPrd, targetConPrd.getType(),
						null, promotoinChargeHours, ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null,user);
			}
		} else {
			// 实收金额小于0,产生一条优惠金额的扣费记录
			promotionChargeMoney = detel;
			super.saveAccountChargeRecord_Edu(detel, targetConPrd, targetConPrd.getType(), null, BigDecimal.valueOf(courseHour),
					ChargeType.NORMAL, PayType.PROMOTION, belongCampus, transactionId, miniClass, miniClassCourse, null, null, null,user);
		}
		
		if(remainingAmount.compareTo(detel)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
//			updateContractAndStuViewForNewCharge(detel, new BigDecimal(courseHour) , targetConPrd );
			doAfterForNewCharge(detel, realChargeMoney, promotionChargeMoney, new BigDecimal(courseHour) , targetConPrd );
		} else { 
			throw new ApplicationException("本合同产品无法继续扣费");
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


}
