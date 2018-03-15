package com.eduboss.service.handler;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.ContractType;
import com.eduboss.common.ElectronicAccChangeType;
import com.eduboss.common.PayType;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.CurriculumDao;
import com.eduboss.dao.ElectronicAccountChangeLogDao;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.MiniClassStudentAttendentDao;
import com.eduboss.dao.MoneyArrangeLogDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ProductDao;
import com.eduboss.dao.PromiseClassSubjectDao;
import com.eduboss.dao.StudentOrganizationDao;
import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.ContractProductSubject;
import com.eduboss.domain.Course;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.LectureClassStudent;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.Product;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudnetAccMv;
import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.domain.TwoTeacherClassStudentAttendent;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.dto.ProductSettleAmount;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.IncomeMessage;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractProductSubjectService;
import com.eduboss.service.ContractService;
import com.eduboss.service.CourseHoursDistributeRecordService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.IncomeDistributionService;
import com.eduboss.service.LectureClassService;
import com.eduboss.service.MoneyArrangeLogService;
import com.eduboss.service.ProductService;
import com.eduboss.service.PromiseClassService;
import com.eduboss.service.PromotionService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.StudentService;
import com.eduboss.service.TwoTeacherClassService;
import com.eduboss.service.UserService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.StringUtil;


public abstract class ContractProductHandler {
	
	protected static ContractProductDao contractProductDao =  ApplicationContextUtil.getContext().getBean(ContractProductDao.class);
	protected static StudnetAccMvDao studnetAccMvDao =  ApplicationContextUtil.getContext().getBean(StudnetAccMvDao.class);
	protected static ProductService productService =  ApplicationContextUtil.getContext().getBean(ProductService.class);
	protected static ProductDao productDao =  ApplicationContextUtil.getContext().getBean(ProductDao.class);
	protected static UserService userService = ApplicationContextUtil.getContext().getBean(UserService.class);
	protected static AccountChargeRecordsDao accountChargeRecordsDao =  ApplicationContextUtil.getContext().getBean(AccountChargeRecordsDao.class);
	protected static FundsChangeHistoryDao fundsChangeHistoryDao =   ApplicationContextUtil.getContext().getBean(FundsChangeHistoryDao.class);
	protected static ChargeService chargeService =   ApplicationContextUtil.getContext().getBean(ChargeService.class);
	protected static PromotionService promotionService =   ApplicationContextUtil.getContext().getBean(PromotionService.class);
	protected static ContractDao contractDao =   ApplicationContextUtil.getContext().getBean(ContractDao.class);
	protected static ContractProductSubjectService contractProductSubjectService =   ApplicationContextUtil.getContext().getBean(ContractProductSubjectService.class);
	protected static ContractService contractService =   ApplicationContextUtil.getContext().getBean(ContractService.class);
	protected static ElectronicAccountChangeLogDao electronicAccountChangeLogDao =   ApplicationContextUtil.getContext().getBean(ElectronicAccountChangeLogDao .class);
	protected static SmallClassService smallClassService =   ApplicationContextUtil.getContext().getBean(SmallClassService .class);
	protected static StudentService studentService =   ApplicationContextUtil.getContext().getBean(StudentService .class);
	protected static MoneyArrangeLogDao moneyArrangeLogDao= ApplicationContextUtil.getContext().getBean(MoneyArrangeLogDao.class);
	protected static MoneyArrangeLogService moneyArrangeLogService= ApplicationContextUtil.getContext().getBean(MoneyArrangeLogService.class);
	protected static PromiseClassService promiseClassService = ApplicationContextUtil.getContext().getBean(PromiseClassService.class);
	protected static OrganizationDao organizationDao = ApplicationContextUtil.getContext().getBean(OrganizationDao.class);
	protected static LectureClassService lectureClassService =   ApplicationContextUtil.getContext().getBean(LectureClassService .class);
	protected static TwoTeacherClassService twoTeacherClassService =   ApplicationContextUtil.getContext().getBean(TwoTeacherClassService .class);
	protected static CourseHoursDistributeRecordService courseHoursDistributeRecordService =   ApplicationContextUtil.getContext().getBean(CourseHoursDistributeRecordService .class);
	protected static DataDictService dataDictService = ApplicationContextUtil.getContext().getBean(DataDictService .class);
	protected static StudentOrganizationDao studentOrganizationDao = ApplicationContextUtil.getContext().getBean(StudentOrganizationDao.class);
    private static RedisDataSource redisDataSource=   ApplicationContextUtil.getContext().getBean(RedisDataSource .class);
	protected static MiniClassStudentAttendentDao miniClassStudentAttendentDao=   ApplicationContextUtil.getContext().getBean(MiniClassStudentAttendentDao .class);
	protected static PromiseClassSubjectDao promiseClassSubjectDao=   ApplicationContextUtil.getContext().getBean(PromiseClassSubjectDao .class);
	protected static CurriculumDao curriculumDao=   ApplicationContextUtil.getContext().getBean(CurriculumDao .class);

	protected static IncomeDistributionService incomeDistributionService = ApplicationContextUtil.getContext().getBean(IncomeDistributionService .class);

	public final static ObjectMapper objectMapper = new ObjectMapper();
	
	/**
     * 保存 产品合同 和 合同绑定起来
     * @param contract
     * @param conProdVo
     */
    public void createNewWithContract(Contract contract, ContractProductVo conProdVo) throws Exception{
        createNewWithContract(contract, conProdVo, true);

    }

	/**
	 * 保存 产品合同 和 合同绑定起来
	 * @param contract
	 * @param conProdVo
	 */
	public void createNewWithContract(Contract contract, ContractProductVo conProdVo, boolean updateInventory) throws Exception{
		ContractProduct conProduct = HibernateUtils.voObjectMapping(conProdVo, ContractProduct.class);
		
		// 设置合同
		conProduct.setContract(contract);
		// 设置产品  临时性的值， 后面的 product ID 都是从界面上传过来的。
		// conProduct.setProduct(product);
		/*if(conProdVo.getProductType() == ProductType.ONE_ON_ONE_COURSE_NORMAL) {
			conProduct.setProduct(productService.getOneOnOneNormalProduct(contract.getStudent().getGradeDict().getId()));
		}*/
		
		calPromotionOfContractProduct(conProduct);
		calBasicContractProductForCreateNewContract(conProduct);

		String str = new String();
		str += this.checkMaxPromotionDiscount(conProduct);
		if(str != null && StringUtils.isNotBlank(str)){
			throw new ApplicationException(str);
		}
		
		contractProductDao.save(conProduct);
		contractProductDao.flush();
		// 保存合同产品的 科目信息
//		updateContractProductSubjects(conProduct, conProduct.getProdSubjects());
		for(ContractProductSubject prodSub : conProduct.getProdSubjects()) {
			prodSub.setContractProduct(conProduct);
			prodSub.setCreateTime(DateTools.getCurrentDateTime());
			prodSub.setCreateByStaff(userService.getCurrentLoginUser());
		}
		
		//updateContractAndStuViewForCreateNewContract(conProduct);
		
		doAfterCreateContractProduct(conProduct, conProdVo, updateInventory);
		
	}
	
	/**
	 * 检查是否符合合同的最大优惠率
	 * @param conProduct
	 * @return
	 */
	public String checkMaxPromotionDiscount(ContractProduct conProduct){
		Product product = productDao.findById(conProduct.getProduct().getId());
		if(product != null ){			
			Double max = product.getMaxPromotionDiscount() == null ? 0 : product.getMaxPromotionDiscount(); //最大折扣率
			BigDecimal planAmount = conProduct.getRealAmount().add(conProduct.getPromotionAmount());
			if (planAmount.compareTo(BigDecimal.ZERO)==0 || conProduct.getType().equals(ProductType.LIVE)){
				return "";
			}
			BigDecimal discont = conProduct.getPromotionAmount().divide(planAmount, 0); //实际折扣率
			if(discont.compareTo(BigDecimal.valueOf(max)) > 0 ){
				BigDecimal d = discont.multiply(BigDecimal.valueOf(100));
				return ""+product.getName()+" 产品折扣率为"+(int)Math.floor(d.doubleValue()) +"%，已超出本产品最高折扣率"+(int)Math.floor(max*100)+"%限制，"
						+ "请修改产品数量或优惠后再保存合同。 PS：如需调整产品折扣率需联系集团产品中心。 ";
			}
		}
		return "";
	}

	/**
	 * 计算 合同产品的优惠金额
	 * @param conProduct
	 */
	protected void calPromotionOfContractProduct(
		ContractProduct conProduct) {
		if(StringUtil.isNotBlank(conProduct.getPromotionIds())) {
			ProductSettleAmount productSettleAmount = promotionService.calculatePromotions(conProduct.getPromotionIds(),conProduct.getPrice(),conProduct.getQuantity() , conProduct.getPlanAmount());
			try {
				conProduct.setPromotionJson(objectMapper.writeValueAsString(productSettleAmount));
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException("JSON  解析错误！");
			}
			conProduct.setPromotionAmount(productSettleAmount.getPromotionAmount());
			conProduct.setRealAmount((productSettleAmount.getRealPayAmount()));
		} else {
			conProduct.setRealAmount(conProduct.getPlanAmount());
            conProduct.setPromotionAmount(BigDecimal.ZERO);
            conProduct.setPromotionJson(null);
		}
		if (conProduct.getRealAmount().compareTo(conProduct.getPaidAmount()) < 0) { //付款金额大于实际金额，则付款金额变更为实际金额
			// 修改合同产品，合同产品总金额小于已分配金额转移所有未消耗课时
			if (conProduct.getType() == ProductType.ONE_ON_ONE_COURSE) {
				contractProductSubjectService.transferOutContractProductSubject(conProduct);
			}
			moneyArrangeLogDao.saveOneRecord(conProduct, conProduct.getContract().getStudent(), 
					conProduct.getPaidAmount().subtract(conProduct.getRealAmount()), userService.getCurrentLoginUser(), "提取", PayType.REAL);
			conProduct.setPaidAmount(conProduct.getRealAmount());
		}
	}

	/**
     * 当合同产品建立好后， 可以有一个回调函数.
     * @param conProduct
     * @param conProdVo
     */
    protected void doAfterCreateContractProduct(ContractProduct conProduct, ContractProductVo conProdVo) {
        doAfterCreateContractProduct(conProduct, conProdVo, true);
    }


	/**
	 * 当合同产品建立好后， 可以有一个回调函数.
	 * @param conProduct
	 * @param conProdVo
	 */
	protected void doAfterCreateContractProduct(ContractProduct conProduct, ContractProductVo conProdVo, boolean updateInventory) {
		// 创建一个学生账户
		Student studentInDb = conProduct.getContract().getStudent();
		StudnetAccMv studentAccInDb = studnetAccMvDao.findById(studentInDb.getId());
        if(studentAccInDb == null) {
        	studentAccInDb =  new StudnetAccMv(studentInDb);
        	if (studentAccInDb.getElectronicAccount() == null) {
        		studentAccInDb.setElectronicAccount(BigDecimal.ZERO);
        	}
            studnetAccMvDao.getHibernateTemplate().save(studentAccInDb);
            studnetAccMvDao.flush();
        }
        
        // 如果是初始合同不用收费
		if (ContractType.INIT_CONTRACT.equals(conProduct.getContract().getContractType())){
			conProduct.setPaidAmount(conProduct.getRealAmount());
			conProduct.setPaidStatus(ContractProductPaidStatus.PAID);
			Contract targetContract = conProduct.getContract();
			targetContract.setPaidAmount(targetContract.getPaidAmount().add(conProduct.getRealAmount()));
		} else {
			// 如果 合同产品的待付款是0 的话，可以把合同产品的支付状态都设为 paid
			if(conProduct.getRealAmount().compareTo(BigDecimal.ZERO) == 0) {
				this.assignMoney(conProduct.getContract(), conProduct, BigDecimal.ZERO);
			} else {
				checkPaidStatusOfContractProduct(conProduct);
			}
		}

		contractProductDao.flush();
       
	}

	/**
	 * 计算合同产品的其他field， 比如只有数量的时候，需要把单价乘以数量来计算总价
	 * @param conProduct
	 */
	protected void calBasicContractProductForCreateNewContract (
			ContractProduct conProduct) throws Exception {
		// 修改 初始化合同 的时候  是把旧有合同产品都删掉 再重新建立的， 所以需要对 产品科目 信息 进行去除（因为从前端带过来， 可能带有 老旧的信息）
		if(conProduct.getProdSubjects() != null) {
			for(ContractProductSubject prodSub : conProduct.getProdSubjects()) {
				prodSub.setId(null);
				prodSub.setContractProduct(null);
			}
			contractProductDao.flush();
		}
	}

	
	
	/**
	 * 分配资金 到不同的合同产品
	 * @param contract
	 * @param contractProduct
	 */
	public void assignMoney(Contract contract, ContractProduct contractProduct, BigDecimal assignAmount) {
		if(assignAmount.compareTo(BigDecimal.ZERO)<0) {
			throw new ApplicationException("分配资金不能小于0");
		} else {
			// 如果可分配资金大于等于分配资金 才可以进行分配
			// TODO 加入东东的方法  更新合同domian的信息
			contractService.calculateContractDomain(contract);

			if(contract.getAvailableAmount().compareTo(assignAmount)>=0) {
				// 分配到 合同产品

				// 分配金额不能超过合同产品总额
				if (contractProduct.getRealAmount().compareTo(contractProduct.getPaidAmount().add(assignAmount))<0){
					throw new ApplicationException("分配资金超过合同产品总金额！");
				}
				checkIfHasWeChatOrAliPayNotPass(contract, contractProduct, assignAmount);
				contractProduct.setPaidAmount(contractProduct.getPaidAmount().add(assignAmount));
				// 更改合同的可分配资金
				//contract.setAvailableAmount(contract.getAvailableAmount().subtract(assignAmount));
				doAfterAssignMoneyAmount(contract, contractProduct, assignAmount,"分配");
				contractProductDao.flush();
			} else {
				throw new ApplicationException("不够分配资金");
			}
		}
		
	}

	/**
	 * 检查是否有微信和支付宝
	 * @param contract
	 * @param contractProduct
	 * @param assignAmount
	 */
	private void checkIfHasWeChatOrAliPayNotPass(Contract contract, ContractProduct contractProduct, BigDecimal assignAmount) {
		//计算有多少不能分配的金额
		BigDecimal canNotAssignAmount = fundsChangeHistoryDao.sumCanNotAssignAmount(contract.getId());
		//合同未分配前的待分配资金
		BigDecimal availableAmount = contract.getAvailableAmount();
		if (availableAmount.subtract(assignAmount).compareTo(canNotAssignAmount)<0){
			throw new ApplicationException("存在收款是微信或者支付宝支付，并且尚未审批通过或冲销，此部分收款资金不能进行分配！");
		}
	}

	/**
	 * 分配资金 到不同的合同产品
	 * @param contract
	 * @param contractProduct
	 */
	public void assignMoney(Contract contract, ContractProduct contractProduct, BigDecimal assignAmount,String userId) {
		if(assignAmount.compareTo(BigDecimal.ZERO)<0) {
			throw new ApplicationException("分配资金不能小于0");
		} else {
			// 如果可分配资金大于等于分配资金 才可以进行分配
			// TODO 加入东东的方法  更新合同domian的信息
			contractService.calculateContractDomain(contract);
			if(contract.getAvailableAmount().compareTo(assignAmount)>=0) {
				// 分配到 合同产品
				contractProduct.setPaidAmount(contractProduct.getPaidAmount().add(assignAmount));
				// 更改合同的可分配资金
				//contract.setAvailableAmount(contract.getAvailableAmount().subtract(assignAmount));
				doAfterAssignMoneyAmount(contract, contractProduct, assignAmount,"分配",userId);
				contractProductDao.flush();
			} else {
				throw new ApplicationException("不够分配资金");
			}
		}
		
	}
	
	/**
	 * 提取资金 到合同待分配资金
	 * @param contract
	 * @param contractProduct
	 */
	public void takeawayMoney(Contract contract, ContractProduct contractProduct, BigDecimal takeawayAmount) {
		if(takeawayAmount.compareTo(BigDecimal.ZERO)<0) {
			throw new ApplicationException("提取资金不能小于0");
		} else {
			// 如果可提取资金小于等于剩余资金 才可以进行提取
			// TODO 加入东东的方法  更新合同domian的信息
			contractService.calculateContractDomain(contract);
			
			if(contractProduct.getPaidAmount().subtract(contractProduct.getConsumeAmount()).compareTo(takeawayAmount)>=0){
				contractProduct.setPaidAmount(contractProduct.getPaidAmount().subtract(takeawayAmount));//原支付金额减去提取金额
				
				doAfterAssignMoneyAmount(contract, contractProduct, takeawayAmount,"提取");
				
				contract.setAvailableAmount(contract.getAvailableAmount().add(takeawayAmount));//更新合同待分配金额
				
				contractProductDao.flush();
			}else {
				throw new ApplicationException("不够分配资金");
			}
			
		}
		
	}

	/**
	 * 用于分配资金完成后
	 * @param contract
	 * @param contractProduct
	 * @param assignAmount
	 * @param remark   提取还是分配
	 */
	protected void doAfterAssignMoneyAmount(Contract contract,
			ContractProduct contractProduct, BigDecimal assignAmount,String remark) {
		if (remark.equals("提取") && contractProduct.getPaidStatus() == ContractProductPaidStatus.PAID) {
			moneyArrangeLogDao.saveOneRecord(contractProduct, contract.getStudent(), contractProduct.getPromotionAmount(), 
					userService.getCurrentLoginUser(), remark, PayType.PROMOTION);
		}
		checkPaidStatusOfContractProduct(contractProduct);
		if(contractProduct.getPaidAmount().compareTo(contractProduct.getRealAmount()) == 0) {
			moneyArrangeLogDao.saveOneRecord(contractProduct, contract.getStudent(), contractProduct.getPromotionAmount(), 
					userService.getCurrentLoginUser(), remark, PayType.PROMOTION);
		}
		// 用于分配资金的记录
		moneyArrangeLogDao.saveOneRecord(contractProduct, contract.getStudent(), assignAmount, 
				userService.getCurrentLoginUser(), remark, PayType.REAL);
	}
	
	/**
	 * 用于分配资金完成后
	 * @param contract
	 * @param contractProduct
	 * @param assignAmount
	 * @param remark   提取还是分配
	 */
	protected void doAfterAssignMoneyAmount(Contract contract,
			ContractProduct contractProduct, BigDecimal assignAmount,String remark,String userId) {
		checkPaidStatusOfContractProduct(contractProduct);
		// 用于分配资金的记录
		moneyArrangeLogDao.saveOneRecord(contractProduct, contract.getStudent(), assignAmount, new User(userId), remark, PayType.REAL);
		
	}

	/**
	 * 判断合同产品的支付状态
	 * @param contractProduct
	 */
	protected void checkPaidStatusOfContractProduct(
			ContractProduct contractProduct) {
		// 如果分配完成就判断合同产品的状态
		 if(contractProduct.getPaidAmount().compareTo(contractProduct.getRealAmount()) == 0) {
			contractProduct.setPaidStatus(ContractProductPaidStatus.PAID);
			contractProduct.setPaidTime(DateTools.getCurrentDate());
			
		//合同产品金额可以提取了，就不限制了	
//		} else if(contractProduct.getPaidAmount().compareTo(contractProduct.getRealAmount()) > 0){
//			throw new ApplicationException("合同产品调整后的金额不能小于该产品之前已分配的资金。可以通过先退费到电子账户，再新建合同来解决。");
		}else if(contractProduct.getPaidAmount().compareTo(BigDecimal.ZERO)==0){
			contractProduct.setPaidStatus(ContractProductPaidStatus.UNPAY);//如果支付金额为0则是待付款
		}else {
			contractProduct.setPaidStatus(ContractProductPaidStatus.PAYING);
		}
		contractProductDao.flush();
	}
		
	

	/**
	 * 获取正常账户的剩余资金
	 * @param contractProduct
	 * @return
	 */
	protected BigDecimal getRemainAmountFromNormalAccOfContractProduct(
			ContractProduct contractProduct) {
		
		if(contractProduct.getPaidAmount().compareTo(contractProduct.getConsumeAmount())>=0) {
			return contractProduct.getPaidAmount().subtract(contractProduct.getConsumeAmount());
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * ####  已经废弃不用的方法， 可以使用 contractProduct.getRemainingAmount 来使用
	 * 获取 合同产品的剩余资金， 包括正常账户 和 优惠账户, 如果把资金交完全款了， 就可以激活优惠账户， 如果还没有交齐，就不能使用优惠账户
	 * @param contractProduct
	 * @return
	 */
	public BigDecimal getRemainAmountOfContractProduct(
			ContractProduct contractProduct) {
		if(contractProduct.getPaidStatus() == ContractProductPaidStatus.PAID){
			return contractProduct.getPaidAmount().add(contractProduct.getPromotionAmount()).subtract(contractProduct.getConsumeAmount()); 
		} else {
			return contractProduct.getPaidAmount().subtract(contractProduct.getConsumeAmount());
		}
	}

	
	/**
	 * 判断合同的状态是不是完结的
	 * @param targetContract
	 */
	protected void updateContractStatus(Contract targetContract) {
		// 检测合同状态 是否完结
		// 判断所有的合同产品是不是 都是 完结啦
		boolean hasUnendedProduct = false;
		targetContract= contractDao.findById(targetContract.getId());
		for(ContractProduct contractProductInDB : targetContract.getContractProducts()) {
			// 不是结课的  不是结束的 退费的
			if(contractProductInDB.getStatus() != ContractProductStatus.ENDED && contractProductInDB.getStatus() != ContractProductStatus.CLOSE_PRODUCT && 
					contractProductInDB.getStatus() != ContractProductStatus.REFUNDED  ) {
				hasUnendedProduct = true;
				break;
			}
		}
		
		if(targetContract.getPaidStatus() == ContractPaidStatus.PAID && !hasUnendedProduct ) {
			targetContract.setContractStatus(ContractStatus.FINISHED);
		}
	}
	
	
	/**
	 * 扣费后的回调函数 
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 */
	protected void doAfterForNewCharge(
			BigDecimal chargeMoneyAmount, BigDecimal realChargeAmount, BigDecimal promotionChargeAmount,  
			BigDecimal chargeQuantity, ContractProduct contractProduct){
		// 更新合同产品的值 已付金额 已消费的数量
		contractProduct.setConsumeAmount(contractProduct.getConsumeAmount().add(chargeMoneyAmount));
		contractProduct.setConsumeQuanity(contractProduct.getConsumeQuanity().add(chargeQuantity));
		contractProduct.setRealConsumeAmount(contractProduct.getRealConsumeAmount().add(realChargeAmount));
		contractProduct.setPromotionConsumeAmount(contractProduct.getPromotionConsumeAmount().add(promotionChargeAmount));
		
		// 更新后实收消耗和优惠消耗对不上总消耗就抛出日志
		if (contractProduct.getConsumeAmount().compareTo(contractProduct.getRealConsumeAmount().add(contractProduct.getPromotionConsumeAmount())) != 0) {
			throw new ApplicationException("这次扣费存在扣费资金流问题，请联系管理员");
		}
		
		// 检测合同产品状态 是否完结
		if(contractProduct.getPaidStatus() == ContractProductPaidStatus.PAID 
				&& contractProduct.getPaidAmount().add(contractProduct.getPromotionAmount()).subtract(contractProduct.getConsumeAmount()).compareTo(BigDecimal.ZERO) == 0) {
			contractProduct.setStatus(ContractProductStatus.ENDED);
//			if(contractProduct.getType().equals(ProductType.SMALL_CLASS) || contractProduct.getType().equals(ProductType.ECS_CLASS)) {//退掉考勤记录
//				smallClassService.deleteMiniStudentAttendent(contractProduct.getId());
//			}
		}
		
		// 更新合同的状态
		updateContractStatus(contractProduct.getContract());
	}
	
	
	/**
	 * 扣费后 更新合同产品， 合同， 和 学生账户的信息
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 *//*
	protected void updateContractAndStuViewForNewCharge(
			BigDecimal chargeMoneyAmount, BigDecimal chargeQuantity, ContractProduct contractProduct) {
		// TODO Auto-generated method stub
		updateContractProductForNewCharge(chargeMoneyAmount, chargeQuantity, contractProduct);
		updateContractForNewCharge(chargeMoneyAmount, chargeQuantity, contractProduct);
		
	}*/



	/**
	 * 在扣费后 更新合同信息
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 */
	/*protected void updateContractForNewCharge(BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, ContractProduct contractProduct) {
		
		Contract targetContract = contractProduct.getContract();
		
		// 更新合同消费总金额  ? 这个有待考证， 需要统计合同的剩余资金？
		targetContract.setConsumeAmount(targetContract.getConsumeAmount().add(chargeMoneyAmount));
		// 可能不需要 剩余资金, 剩余课时
		//targetContract.setRemainingAmount(contract.getPaidAmount().subtract((contract.getConsumeAmount())));
		
		updateContractStatus(targetContract);
		
	}*/

	
	/**
	 * 在扣费后 更新 合同产品信息
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 */
/*	protected void updateContractProductForNewCharge(
			BigDecimal chargeMoneyAmount, BigDecimal chargeQuantity,
			ContractProduct contractProduct) {
		// 更新合同产品的值 已付金额 已消费的数量
		contractProduct.setConsumeAmount(contractProduct.getConsumeAmount().add(chargeMoneyAmount));
		contractProduct.setConsumeQuanity(contractProduct.getConsumeQuanity().add(chargeQuantity));
		
		// 检测合同产品状态 是否完结
		if(contractProduct.getPaidStatus() == ContractProductPaidStatus.PAID && contractProduct.getPaidAmount().add(contractProduct.getPromotionAmount()).subtract(contractProduct.getConsumeAmount()).compareTo(BigDecimal.ZERO) == 0) {
			contractProduct.setStatus(ContractProductStatus.ENDED);
		}
		
	}*/

	
	/**
	 * 在扣费的时候 生成一条扣费记录 
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 */
/*	public void createAccChargeRecordForNewCharge(BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, ContractProduct contractProduct) {
		
	}*/
	
	
	/**
	 * 生成 扣费记录
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 * @param chargeType
	 */
	protected AccountChargeRecords buildChargeChargeRecord(BigDecimal chargeMoneyAmount,
			BigDecimal chargeQuantity, ContractProduct contractProduct, ChargeType chargeType ) {
		Contract newContract =  contractProduct.getContract();
		BigDecimal detel = chargeMoneyAmount;
		AccountChargeRecords record =  new AccountChargeRecords();
		record.setChargePayType(ChargePayType.CHARGE);
		record.setProduct(contractProduct.getProduct());
		record.setAmount(detel);
		record.setContract(newContract);
		record.setStudent(contractProduct.getContract().getStudent());
		record.setOperateUser(userService.getCurrentLoginUser());
		record.setProductType(contractProduct.getType());
		record.setPayTime(DateTools.getCurrentDateTime());
		record.setQuality(chargeQuantity);
		record.setContractProduct(contractProduct);
		record.setChargeType(chargeType);
		return record; 
	}
	
	
	
	/**
	 * 结课的相关操作
	 * @param amountFromNormalAcc	已付账户的剩余资金
	 * @param amountFromPromotionAcc 	优惠账户的剩余资金
	 * @param returnAmountFromPromotionAcc	优惠对消的金额
	 * @param contractProduct	合同产品
	 */
	public void completeContractProduct(BigDecimal amountFromNormalAcc, BigDecimal amountFromPromotionAcc ,BigDecimal returnAmountFromPromotionAcc,  ContractProduct contractProduct,BigDecimal returnMoney) throws Exception{
		// 去掉退电子账户的逻辑
		if((amountFromNormalAcc.compareTo(returnMoney)>=0 && amountFromNormalAcc.compareTo(BigDecimal.ZERO)>0) || returnAmountFromPromotionAcc.compareTo(BigDecimal.ZERO)>0){
			//如果退费金额小于剩余资金，则要把剩下的资金转为划归收入。
			saveNormalIncome(contractProduct, amountFromNormalAcc.subtract(returnMoney), returnAmountFromPromotionAcc);
		}
		if (contractProduct.getType() == ProductType.ONE_ON_ONE_COURSE) {
			contractService.refundContractProductSubject(contractProduct);
		}
		promotionRetrun(contractProduct, returnAmountFromPromotionAcc);	// 优惠对消资金
		contractProduct.setStatus(ContractProductStatus.CLOSE_PRODUCT);
		contractProductDao.flush();
		doAfterCompleteContractProduct(returnMoney, amountFromPromotionAcc ,returnAmountFromPromotionAcc,  contractProduct);
	}
	
	/**
	 * 生成退费扣费记录
	 * @param contractProduct 合同产品
	 * @param amountFromPromotionAcc 优惠资金
	 * @param amountFromNormalAcc 实收资金
	 * @param isRollBack
	 * @param transactionId
	 */
	public void saveReturnAccRecord(
			ContractProduct contractProduct, BigDecimal amountFromNormalAcc, BigDecimal amountFromPromotionAcc, boolean isRollBack, String transactionId) {
		// 实收
		if (amountFromNormalAcc.compareTo(BigDecimal.ZERO) > 0) {
			// 生成一条扣费记录
			AccountChargeRecords realAccRecord = buildChargeChargeRecord(amountFromNormalAcc, BigDecimal.ONE, contractProduct, ChargeType.FEEDBACK_REFUND);
			realAccRecord.setTransactionId(transactionId);
			realAccRecord.setPayType(PayType.REAL);
			realAccRecord.setTransactionTime(DateTools.getCurrentDateTime());
			Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
			realAccRecord.setBlCampusId(belongCampus);
			accountChargeRecordsDao.save(realAccRecord);
			accountChargeRecordsDao.flush();
		}
		// 优惠
		if (amountFromPromotionAcc.compareTo(BigDecimal.ZERO) > 0) {
			// 生成一条扣费记录
			AccountChargeRecords promotionAccRecord = buildChargeChargeRecord(amountFromPromotionAcc, BigDecimal.ONE, contractProduct, ChargeType.FEEDBACK_REFUND);
			promotionAccRecord.setTransactionId(transactionId);
			promotionAccRecord.setPayType(PayType.PROMOTION);
			promotionAccRecord.setTransactionTime(DateTools.getCurrentDateTime());
			Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
			promotionAccRecord.setBlCampusId(belongCampus);
			accountChargeRecordsDao.save(promotionAccRecord);
			accountChargeRecordsDao.flush();
		}
	}
	
	/**
	 * 退电子账户
	 * @param amountFromPromotionAcc 	优惠账户的剩余资金
	 * @param returnAmountFromPromotionAcc	优惠对消的金额
	 * @param contractProduct	合同产品
	 */
	public void transferAmountToElectronicAdd(BigDecimal amountFromPromotionAcc ,BigDecimal returnAmountFromPromotionAcc,  ContractProduct contractProduct,BigDecimal returnMoney, String remark) throws Exception{
		transferNormalAmountToElectronicAcc(contractProduct,  returnMoney, false, ElectronicAccChangeType.REFUND_IN, remark);  // 转移正常资金到 电子账户
		transferPromotionAmountToElectronicAcc(contractProduct,  amountFromPromotionAcc, false, ElectronicAccChangeType.REFUND_IN, remark);  // 转移优惠资金到 电子账户
	}
		
	
	/**
	 * 回滚
	 * @param contractProduct 其他产品
	 * @param  回滚的资金
	 */
	public void rollbackOhterContractProduct(ContractProduct contractProduct, BigDecimal returnRealMoney, BigDecimal returnPromotionMeny,PayType payType) {
		if(payType.equals(PayType.REAL))
		transferNormalAmountToElectronicAcc(contractProduct,  returnRealMoney, true, ElectronicAccChangeType.WASH_IN, null);  // 转移正常资金到 电子账户
//		if(payType.equals(PayType.PROMOTION))
//		transferPromotionAmountToElectronicAcc(contractProduct,  returnPromotionMeny, true);  // 转移优惠资金到 电子账户
		contractProduct.setStatus(ContractProductStatus.CLOSE_PRODUCT);
		contractProductDao.flush();
		doAfterCompleteContractProduct(returnRealMoney, returnPromotionMeny ,BigDecimal.ZERO,  contractProduct);
	}
	
	/**
	 * 在 结课 后的 回调函数
	 * @param amountFromNormalAcc
	 * @param amountFromPromotionAcc2
	 * @param returnAmountFromPromotionAcc
	 * @param contractProduct
	 */
	protected void doAfterCompleteContractProduct(BigDecimal amountFromNormalAcc,
			BigDecimal amountFromPromotionAcc2,
			BigDecimal returnAmountFromPromotionAcc,
			ContractProduct contractProduct) {
		// 更新合同状态
		updateContractStatus(contractProduct.getContract());
	}




	/**
	 * 优惠返还
	 * @param contractProduct
	 * @param returnAmountFromPromotionAcc
	 */
	protected void promotionRetrun(ContractProduct contractProduct,
			BigDecimal returnAmountFromPromotionAcc) throws Exception {
		// 扣费生成一跳记录
		AccountChargeRecords accRecord = buildChargeChargeRecord(returnAmountFromPromotionAcc, BigDecimal.ONE, contractProduct, ChargeType.PROMOTION_RETURN); 
		Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
		//userService.getBelongCampus();
//		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())){
//			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
//		}
		String transactionId = UUID.randomUUID().toString();
		accRecord.setTransactionId(transactionId);
		accRecord.setTransactionTime(DateTools.getCurrentDateTime());
		accRecord.setBlCampusId(belongCampus);
		accountChargeRecordsDao.save(accRecord);
		accountChargeRecordsDao.flush();
	}
	
	/**
	 * 划归收入  ，如果退款的时候没有退完全，则剩下的资金就是划归收入  
	 * @param contractProduct
	 * @param returnAmountFromPromotionAcc
	 */
	protected void saveNormalIncome(ContractProduct contractProduct,
			BigDecimal realNormalIncome, BigDecimal promoNormalIncome) {
		
		Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
//		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())){
//			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
//		}
//		Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
		String transactionId = UUID.randomUUID().toString();
		
		if (realNormalIncome.compareTo(BigDecimal.ZERO) > 0) {
			saveAccountChargeRecord(realNormalIncome, contractProduct, contractProduct.getType(), null, BigDecimal.ONE,
					ChargeType.IS_NORMAL_INCOME, PayType.REAL, belongCampus, transactionId, null, null, null, null, null);
		}
		if (promoNormalIncome.compareTo(BigDecimal.ZERO) > 0) {
			saveAccountChargeRecord(promoNormalIncome, contractProduct, contractProduct.getType(), null, BigDecimal.ONE, 
					ChargeType.IS_NORMAL_INCOME, PayType.PROMOTION, belongCampus, transactionId, null, null, null, null, null);
		}
	}

	/**
	 * 转移#优惠资金#资金到学生账户里面
	 * @param contractProduct
	 * @param amountFromNormalAcc
	 */
	protected void transferPromotionAmountToElectronicAcc(
			ContractProduct contractProduct, BigDecimal amountFromPromotionAcc, boolean isRollBack, ElectronicAccChangeType type, String remark) {
		// 扣费生成一跳记录
		AccountChargeRecords accRecord = buildChargeChargeRecord(amountFromPromotionAcc, BigDecimal.ONE, contractProduct, ChargeType.TRANSFER_PROMOTION_TO_ELECT_ACC); 
		String transactionId = UUID.randomUUID().toString();
		accRecord.setTransactionId(transactionId);
		accRecord.setPayType(PayType.REAL);
		accRecord.setTransactionTime(DateTools.getCurrentDateTime());
		Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
//		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType()) && !isRollBack){
//			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
//		}
		accRecord.setBlCampusId(belongCampus);
		
		accountChargeRecordsDao.save(accRecord);
		accountChargeRecordsDao.flush();
		
		// 转移资金到 电子账户里面
		transferToElectronicAcc(contractProduct.getContract().getStudent(), amountFromPromotionAcc, type, remark);
		contractService.updateStudentAccountInfoAmount(contractProduct.getContract().getStudent().getId(),amountFromPromotionAcc,type,contractProduct.getContract());//平行账户
	}

	/**
	 * 转移#正常资金#资金到学生账户里面
	 * @param contractProduct
	 * @param amountFromNormalAcc
	 */
	protected void transferNormalAmountToElectronicAcc(
			ContractProduct contractProduct, BigDecimal amountFromNormalAcc, boolean isRollBack, ElectronicAccChangeType type, String remark) {
		// 扣费生成一跳记录
		AccountChargeRecords accRecord = buildChargeChargeRecord(amountFromNormalAcc, BigDecimal.ONE, contractProduct, ChargeType.TRANSFER_NORMAL_TO_ELECT_ACC); 
		Organization belongCampus= contractProduct.getContract().getStudent().getBlCampus();
//		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType()) && !isRollBack){
//			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
//		}
		accRecord.setBlCampusId(belongCampus);
		accountChargeRecordsDao.save(accRecord);
		accountChargeRecordsDao.flush();
		
		// 转移资金到 电子账户里面
		transferToElectronicAcc(contractProduct.getContract().getStudent(), amountFromNormalAcc, type, remark);
		contractService.updateStudentAccountInfoAmount(contractProduct.getContract().getStudent().getId(),amountFromNormalAcc,type,contractProduct.getContract());//平行账户
	}
		
	/**
	 * 转移资金到学生账户
	 * @param student
	 * @param amountFromNormalAcc
	 */
	protected void transferToElectronicAcc(Student student,
			BigDecimal amountForTransfer, ElectronicAccChangeType type, String remark) {
		StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
		studnetAccMvDao.transferAmountToElectronicAcc(student.getId(), amountForTransfer);
		if (StringUtils.isBlank(remark)) {
			remark = "转移资金到电子账户";
		}
		electronicAccountChangeLogDao.saveElecAccChangeLog(student.getId(), type, "A", amountForTransfer, studnetAccMv.getElectronicAccount(), remark);
	}


	
	
	
	/** 
	 *  退费后的的相关操作
	 * 
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 * @param contractProduct
	 */
	public void refundContractProduct(BigDecimal amountFromNormalAcc, BigDecimal amountFromPromotionAcc ,BigDecimal returnAmountFromPromotionAcc,  ContractProduct contractProduct) throws Exception{
//		refundNormalAmountToCustomer(contractProduct,  amountFromNormalAcc);
//		refundPromotionAmountCustomer(contractProduct,  amountFromPromotionAcc);
//		promotionRetrun(contractProduct, returnAmountFromPromotionAcc);
//		
//		contractProduct.setStatus(ContractProductStatus.REFUNDED);
//		contractProductDao.flush();
		
		transferNormalAmountToElectronicAcc(contractProduct,  amountFromNormalAcc, false, ElectronicAccChangeType.REFUND_IN, null);  // 转移正常资金到 电子账户
		transferPromotionAmountToElectronicAcc(contractProduct,  amountFromPromotionAcc, false, ElectronicAccChangeType.REFUND_IN, null);  // 转移优惠资金到 电子账户
		promotionRetrun(contractProduct, returnAmountFromPromotionAcc);	// 优惠对消资金
		
		contractProduct.setStatus(ContractProductStatus.REFUNDED);
		contractProductDao.flush();
		
		studentService.withDrawElectronicAmount(contractProduct.getContract().getStudent().getId(),amountFromNormalAcc.add(amountFromPromotionAcc) );
		
		doAfterRefundContractProduct(amountFromNormalAcc, amountFromPromotionAcc ,returnAmountFromPromotionAcc,  contractProduct);
	}

	/**
	 * 退费后的  回调函数
	 * @param amountFromNormalAcc
	 * @param amountFromPromotionAcc2
	 * @param returnAmountFromPromotionAcc
	 * @param contractProduct
	 */
	protected void doAfterRefundContractProduct(BigDecimal amountFromNormalAcc,
			BigDecimal amountFromPromotionAcc2,
			BigDecimal returnAmountFromPromotionAcc,
			ContractProduct contractProduct) {
		// 更新合同状态
		updateContractStatus(contractProduct.getContract());
	}

	
	/**
	 * 从优惠账户 退费 到客户里面
	 * @param contractProduct
	 * @param amountFromPromotionAcc2
	 */
	protected void refundPromotionAmountCustomer(ContractProduct contractProduct,
			BigDecimal amountFromPromotionAcc) {

		createFundRecord(contractProduct, amountFromPromotionAcc, PayWay.REFUND_PROMOTION_AMOUNT);
	}

	/**
	 * 从正常账户 退费 到客户里面
	 * @param contractProduct
	 * @param amountFromNormalAcc
	 */
	protected void refundNormalAmountToCustomer(ContractProduct contractProduct,
			BigDecimal amountFromNormalAcc) {
		createFundRecord(contractProduct, amountFromNormalAcc, PayWay.REFUND_NORMAL_AMOUNT);
		
	}
	
	 /**
	  * 生成收费记录
	 * @param contractProductInDb
	 * @param amountFromNormalAcc
	 * @param refundNormalAmount
	 */
	protected void createFundRecord(ContractProduct contractProductInDb,
			BigDecimal amount, PayWay payWay) {
		Contract contract = contractProductInDb.getContract();
		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
		fundsChangeHistory.setRemark("");
		fundsChangeHistory.setTransactionAmount(amount);
		fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
		fundsChangeHistory.setContract(contract);
		fundsChangeHistory.setChannel(payWay);
		fundsChangeHistory.setStudent(contract.getStudent());
		fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
		// 需要历史记录？
		Double historySum = fundsChangeHistoryDao.historySumFundsChange(contract.getId());
		fundsChangeHistory.setPaidAmount(BigDecimal.valueOf(historySum).add(amount));
		fundsChangeHistoryDao.save(fundsChangeHistory);
		fundsChangeHistoryDao.flush();
	}
	
	private void checkFrozenContractProduct(ContractProduct contractProduct, String throwMessage) {
		if (contractProduct.getIsFrozen() == 0) {
			throw new ApplicationException(throwMessage);
		}
	}
	
	
	/**
	* 扣除一对一的费用, 如果没有实现这个方法就可以 抛出一个 applicationException 说明补支持这个方法
	 * @param contract
	 * @param conProd
	 * @param remainingMoney
	 * @param zero
	 * @param course
	 * @param transactionId
	 */
	public void chargeOneOnOneClass(Contract contract, ContractProduct conProd,
			BigDecimal remainingMoney, BigDecimal zero, Course course, String transactionId) {
		this.checkFrozenContractProduct(conProd, "扣费合同产品已经冻结，不能进行扣费操作！");
	}

	/**
	 * 扣费 小班收费, 如果没有实现这个方法就可以 抛出一个 applicationException 说明补支持这个方法
	 * @param targetConPrd
	 * @param miniClass
	 * @param miniClassCourse
	 * @param courseHour
	 */
	public void chargeMiniClass(ContractProduct targetConPrd,
			MiniClass miniClass, MiniClassCourse miniClassCourse,
			Double courseHour) {
		this.checkFrozenContractProduct(targetConPrd, "扣费合同产品已经冻结，不能进行扣费操作！");
	}
	
	/**
	 * 扣费 小班收费, 如果没有实现这个方法就可以 抛出一个 applicationException 说明补支持这个方法
	 * @param targetConPrd
	 * @param miniClass
	 * @param miniClassCourse
	 * @param courseHour
	 */
	public void chargeMiniClass_Edu(ContractProduct targetConPrd,
			MiniClass miniClass, MiniClassCourse miniClassCourse,
			Double courseHour,User user) {
		this.checkFrozenContractProduct(targetConPrd, "扣费合同产品已经冻结，不能进行扣费操作！");
	}



	
	/**
	 * 扣费 一对多收费, 如果没有实现这个方法就可以 抛出一个 applicationException 说明补支持这个方法
	 * @param targetConPrd
	 * @param otmClass
	 * @param otmClassCourse
	 * @param courseHour
	 */
	public void chargeOtmClass(ContractProduct targetConPrd,
			OtmClass otmClass, OtmClassCourse otmClassCourse,
			Double courseHour, String transactionId) {
		this.checkFrozenContractProduct(targetConPrd, "扣费合同产品已经冻结，不能进行扣费操作！");
	}
	
	
	/**
	 * 扣费 讲座收费, 如果没有实现这个方法就可以 抛出一个 applicationException 说明不支持这个方法
	 * @param targetConPrd
	 * @param otmClass
	 * @param transactionId
	 */
	public void chargeLectureClass(LectureClassStudent stu) {
		this.checkFrozenContractProduct(stu.getContractProduct(), "扣费合同产品已经冻结，不能进行扣费操作！");
	}

	/**
	 * 扣费 其他收费, 如果没有实现这个方法就可以 抛出一个 applicationException 说明补支持这个方法
	 * @param contract
	 * @param contractProduct
	 * @param chargeMoneyAmount
	 * @param chargeQuantity
	 */
	public abstract void chargeOtherProduct(Contract contract, ContractProduct contractProduct, BigDecimal chargeMoneyAmount, BigDecimal chargeQuantity);

	/**
	 * 扣费 目标班 收费, 如果没有实现这个方法就可以 抛出一个 applicationException 说明补支持这个方法
	 * @param contract 	合同
	 * @param contractProduct  合同产品
	 * @param chargeMoneyAmount  扣费金额
	 * @param chargeQuantity	扣费数量
	 */
	public void chargePromiseClassProduct(Contract contract, ContractProduct contractProduct, BigDecimal chargeMoneyAmount, BigDecimal chargeQuantity, PromiseClassRecord promiseClassRecord) {
		this.checkFrozenContractProduct(contractProduct, "扣费合同产品已经冻结，不能进行扣费操作！");
	};
	
	
	
	/**
	 * 判断 是否可以 delete 这个产品, 主要是判断他 是否已经扣费啦， 如果有扣费了就会抛出exception
	 * @param contractProduct
	 */
	protected void checkIfCanDelete(ContractProduct contractProduct) {
		boolean hasCharge = chargeService.hasChargeRecordOfContractProduct(contractProduct);
		if(hasCharge) 
			throw new ApplicationException("合同产品已经有扣费消费， 不能删除！");
		//如果不需要分配资金的检测
//		boolean hasAssign =  moneyArrangeLogService.hasAssignRecordOfContractProduct(contractProduct);
//		if(hasAssign) 
//			throw new ApplicationException("合同产品已经有分配资金， 不能删除！");
	}

	/**
	 * delete 一个合同产品
	 * @param contractProduct
	 */
	public void deleteContractProduct(ContractProduct contractProduct) {
		checkIfCanDelete(contractProduct);
		Contract contractInDb =  contractProduct.getContract();
		String contractId = contractInDb.getId();
		contractDao.getHibernateTemplate().evict(contractInDb);
		contractInDb = contractDao.findById(contractId);
		doBeforeDeleteContractProduct(contractProduct, contractInDb);
		moneyArrangeLogService.deleteByContractProductId(contractProduct.getId());
		contractProductDao.deleteById(contractProduct.getId()); // 删除该产品
		contractDao.flush();
	}
	/**
	 * 更新合同信息 在 删除合同产品之前
	 * @param contractProduct
	 * @param contractInDb
	 */
	protected void doBeforeDeleteContractProduct(
			ContractProduct contractProduct, Contract contractInDb) {
		// 现在全部都是用 合同计算方法来更新合同的不同的值， 不需要这种每次都更新计算
		// 返回 可分配资金
		//BigDecimal returenAmount = contractProduct.getPaidAmount();
		//contractInDb.setAvailableAmount(contractInDb.getAvailableAmount().add(contractProduct.getPaidAmount())); // 资金回流至合同待分配
		//contractProduct.getPaidAmount();
		
		// 删除合同产品 的 绑定科目
		//contractService.deleteByContractProduct(contractProduct.getId());
		contractService.deleteSubjectsByContractProduct(contractProduct.getId());
		contractProductDao.flush();
	}
	/**
	 * 判断 是否可以 更新 这个产品, 
	 * @param processingProduct
	 * @param newProduct 
	 */
	protected void checkIfCanEdit(ContractProduct processingProductInHibernate, ContractProduct newProduct) {
		// 判断修改后的总资金 是不是 大于 已消费资金
		BigDecimal chargeAmount = chargeService.getChargeAmount(processingProductInHibernate);
		BigDecimal realChargeAmount = chargeService.getRealChargeAmount(processingProductInHibernate.getId(),PayType.REAL);
		BigDecimal promotionChargeAmount = chargeService.getRealChargeAmount(processingProductInHibernate.getId(),PayType.PROMOTION);
		
		
		if(chargeAmount.compareTo(newProduct.getTotalAmount())>0) {
			throw new ApplicationException("修改后的合同产品金额不能低于合同产品已消费金额！");
		}else if(realChargeAmount.compareTo(newProduct.getRealAmount())>0){
			throw new ApplicationException("产品“"+processingProductInHibernate.getProduct().getName()+"”修改后的产品实收金额少于已经消费实收金额，请重新修改后再保存合同！");
		}else if(promotionChargeAmount.compareTo(newProduct.getPromotionAmount())>0){
			throw new ApplicationException("产品“"+processingProductInHibernate.getProduct().getName()+"”修改后的产品优惠金额少于已经消费优惠金额，请重新修改后再保存合同！");
		}
		
        if((processingProductInHibernate.getQuantity().compareTo(newProduct.getQuantity()) !=0 ||    //如果数量变化了
    	   !processingProductInHibernate.getPromotionIds().equals(newProduct.getPromotionIds())) &&	// 或者优惠变化了  才算修改了该合同产品  否则不需要判断下面条件
    	   processingProductInHibernate.getConsumeAmount().compareTo(processingProductInHibernate.getPaidAmount()) > 0){
            throw new ApplicationException("修改前的产品[" + processingProductInHibernate.getProduct().getName() + "]已有赠送金额的消费！");
        }
		
		
	}
	/**
	 * 更新某个合同产品
	 * @param processingProductInHibernate
	 * @param newProduct 
	 */
	public void editContratProduct(ContractProduct processingProductInHibernate, ContractProduct newProduct) throws Exception {
		BigDecimal oldPromotionAmount= processingProductInHibernate.getPromotionAmount();
		
		this.checkFrozenContractProduct(processingProductInHibernate, "合同产品已经冻结，不能修改");
		// 对新的合同产品计算 收费
		calPromotionOfContractProduct(newProduct);
		checkIfCanEdit(processingProductInHibernate, newProduct);
		processingProductInHibernate.setPlanAmount(newProduct.getPlanAmount());
		// 如果传进的折扣是0，就用1 来代替。 因为还有一些旧的合同正在使用
		processingProductInHibernate.setDealDiscount(newProduct.getDealDiscount()==BigDecimal.ZERO? BigDecimal.ONE: newProduct.getDealDiscount());
		processingProductInHibernate.setQuantity(newProduct.getQuantity());
		processingProductInHibernate.setProduct(newProduct.getProduct());
		// 计算优惠金额
		processingProductInHibernate.setPromotionIds(newProduct.getPromotionIds());
		calPromotionOfContractProduct(processingProductInHibernate);
		// 未完结设为NORMAL
        if(processingProductInHibernate.getStatus() == ContractProductStatus.ENDED && processingProductInHibernate.getQuantity().subtract(processingProductInHibernate.getConsumeQuanity()).compareTo(BigDecimal.ZERO) > 0){
            processingProductInHibernate.setStatus(ContractProductStatus.NORMAL);
        }
     // 增加判断是否超出小班产品的总课时
        
        if (newProduct.getType() == ProductType.SMALL_CLASS) {
        	Product miniProduct = productDao.findById(newProduct.getProduct().getId());
        	if (miniProduct.getMiniClassTotalhours().compareTo(newProduct.getQuantity()) < 0) {
        		throw new ApplicationException("小班合同产品（" + miniProduct.getName() + "）的数量不可大于产品建议总课时数");
        	}
        }
        String str = new String();
		str += this.checkMaxPromotionDiscount(processingProductInHibernate);
		if(str != null && StringUtils.isNotBlank(str)){
			throw new ApplicationException(str);
		}
		contractProductDao.save(processingProductInHibernate);
		contractProductDao.flush();
		doAfterEditContractProduct(processingProductInHibernate, newProduct);
		
		
		if(processingProductInHibernate.getPromotionAmount().compareTo(oldPromotionAmount)>0){//多了就分配
			moneyArrangeLogDao.saveOneRecord(processingProductInHibernate, processingProductInHibernate.getContract().getStudent(), processingProductInHibernate.getPromotionAmount().subtract(oldPromotionAmount), userService.getCurrentLoginUser(), "分配", PayType.PROMOTION);
		}else if(processingProductInHibernate.getPromotionAmount().compareTo(oldPromotionAmount)<0){//减少优惠，要从money_arrage_log表里面去掉减去的优惠
			moneyArrangeLogDao.saveOneRecord(processingProductInHibernate, processingProductInHibernate.getContract().getStudent(), oldPromotionAmount.subtract(processingProductInHibernate.getPromotionAmount()), userService.getCurrentLoginUser(), "提取", PayType.PROMOTION);
		}
	}
	/**
	 * 在修改合同产品后的操作
	 * @param processingProductInHibernate
	 * @param newProduct
	 */
	private void doAfterEditContractProduct(
			ContractProduct processingProductInHibernate,
			ContractProduct newProduct) {
		
		// 删除合同产品 的 绑定科目
//		contractService.deleteSubjectsByContractProduct(processingProductInHibernate.getId());
		
		
		contractProductDao.flush();
		
		// 保存合同产品的 科目信息
//		for(ContractProductSubject prodSub : newProduct.getProdSubjects() ) {
//			prodSub.setContractProduct(processingProductInHibernate);
//			prodSub.setCreateTime(DateTools.getCurrentDateTime());
//			prodSub.setCreateByStaff(userService.getCurrentLoginUser());
//			prodSub.setId(null);
//			//contractProductDao.getHibernateTemplate().evict(prodSub);
//		}
		processingProductInHibernate.setProdSubjects(newProduct.getProdSubjects());
		
		// 判断合同产品的支付状态
		checkPaidStatusOfContractProduct(processingProductInHibernate);
		
		contractProductDao.flush();
	}

	protected void saveAccountChargeRecord(BigDecimal chargeAmount, ContractProduct targetConPrd, ProductType productType,
			Course course, BigDecimal auditedCourseHours, ChargeType chargeType,
			PayType payType, Organization belongCampus, String transactionId, MiniClass miniClass, MiniClassCourse miniClassCourse,
			PromiseClassRecord promiseClassRecord, OtmClass otmClass, OtmClassCourse otmClassCourse,String transactionTime) {
		AccountChargeRecords record =  new AccountChargeRecords();
		record.setProduct(targetConPrd.getProduct());
		record.setAmount(chargeAmount);
		record.setContract(targetConPrd.getContract());
		record.setStudent(targetConPrd.getContract().getStudent());
		record.setOperateUser(userService.getCurrentLoginUser());
		record.setProductType(productType);
		record.setPayTime(DateTools.getCurrentDateTime());
		
		
		record.setContractProduct(targetConPrd);
		record.setQuality(auditedCourseHours);
		record.setChargeType(chargeType);
		record.setPayType(payType);
		record.setBlCampusId(belongCampus);
		record.setTransactionId(transactionId);
		record.setChargePayType(ChargePayType.CHARGE);
		
		if (ChargeType.NORMAL.equals(chargeType)) {
			if (null != course) {
				record.setCourse(course);
				record.setTeacher(course.getTeacher());
				if(StringUtils.isBlank(transactionTime))
				transactionTime = course.getCourseDate()+" "+course.getCourseTime().substring(0, 5) + ":00";
			}
			if (null != miniClass) {
				record.setMiniClass(miniClass);
				record.setTeacher(miniClass.getTeacher());
			}
			if (null != miniClassCourse) {
				record.setMiniClassCourse(miniClassCourse);
				if(StringUtils.isBlank(transactionTime))
				transactionTime = miniClassCourse.getCourseDate()+" "
						+miniClassCourse.getCourseTime() + ":00";
			}
			if (null != promiseClassRecord) {
				record.setPromiseClassRecord(promiseClassRecord);
				if (null != promiseClassRecord.getClassDate())  {
					if(StringUtils.isBlank(transactionTime))
					transactionTime = promiseClassRecord.getClassDate() + " 00:00:00";
				} else {
					if(StringUtils.isBlank(transactionTime))
					transactionTime = DateTools.getCurrentDateTime();
				}
			}
			if (null != otmClass) {
				record.setOtmClass(otmClass);
				record.setTeacher(otmClass.getTeacher());
			}
			if (null != otmClassCourse) {
				record.setOtmClassCourse(otmClassCourse);
				if(StringUtils.isBlank(transactionTime))
				transactionTime = otmClassCourse.getCourseDate()+" "
						+otmClassCourse.getCourseTime() + ":00";
			}
			
			if (ProductType.OTHERS.equals(productType)) {
				if(StringUtils.isBlank(transactionTime))
				transactionTime = targetConPrd.getPaidTime() + " 00:00:00";
			}
		} else if (ChargeType.IS_NORMAL_INCOME.equals(chargeType)) {
			if(StringUtils.isBlank(transactionTime))
			transactionTime = DateTools.getCurrentDateTime();
		}
		
		if (null != transactionTime) {
			record.setTransactionTime(transactionTime);
		}
		
		accountChargeRecordsDao.save(record);
		accountChargeRecordsDao.flush();
		pushChargeMsgToQueue(belongCampus.getId(), transactionTime, chargeAmount, auditedCourseHours, productType, chargeType, payType, ChargePayType.CHARGE);
	}

	protected void saveAccountChargeRecordForTwoTeacher(BigDecimal chargeAmount, ContractProduct targetConPrd, ProductType productType, BigDecimal auditedCourseHours, ChargeType chargeType, PayType payType, Organization belongCampus, String transactionId, TwoTeacherClassStudentAttendent studentAttendent){
		AccountChargeRecords record =  new AccountChargeRecords();

		record.setProduct(targetConPrd.getProduct());
		record.setAmount(chargeAmount);
		record.setContract(targetConPrd.getContract());
		record.setStudent(targetConPrd.getContract().getStudent());
		record.setOperateUser(userService.getCurrentLoginUser());
		record.setProductType(productType);
		record.setPayTime(DateTools.getCurrentDateTime());
		record.setChargePayType(ChargePayType.CHARGE);

		String transactionTime = null;
		record.setContractProduct(targetConPrd);
		record.setQuality(auditedCourseHours);
		record.setChargeType(chargeType);
		record.setPayType(payType);
		record.setBlCampusId(belongCampus);
		record.setTransactionId(transactionId);

		if (ChargeType.NORMAL.equals(chargeType)){
			record.setTwoTeacherClassStudentAttendent(studentAttendent);
			record.setTeacher(studentAttendent.getTwoTeacherClassTwo().getTeacher());
			TwoTeacherClassCourse twoTeacherClassCourse = studentAttendent.getTwoTeacherClassCourse();
			transactionTime = twoTeacherClassCourse.getCourseDate()+" "+twoTeacherClassCourse.getCourseTime()+":00";
//			transactionTime = DateTools.getCurrentDateTime();
		}else if(ChargeType.IS_NORMAL_INCOME.equals(chargeType)){
			transactionTime = DateTools.getCurrentDateTime();
		}

		if (transactionTime!=null){
			record.setTransactionTime(transactionTime);
		}
		accountChargeRecordsDao.save(record);
		accountChargeRecordsDao.flush();
		pushChargeMsgToQueue(belongCampus.getId(), transactionTime, chargeAmount, auditedCourseHours, productType, chargeType, payType, ChargePayType.CHARGE);
	}


	
	protected void saveAccountChargeRecord(BigDecimal chargeAmount, ContractProduct targetConPrd, ProductType productType,
			Course course, BigDecimal auditedCourseHours, ChargeType chargeType,
			PayType payType, Organization belongCampus, String transactionId, MiniClass miniClass, MiniClassCourse miniClassCourse,
			PromiseClassRecord promiseClassRecord, OtmClass otmClass, OtmClassCourse otmClassCourse) {
		AccountChargeRecords record =  new AccountChargeRecords();
		// 旧数据 可能会是 price 价格为 0
//		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
		
		record.setProduct(targetConPrd.getProduct());
		record.setAmount(chargeAmount);
		record.setContract(targetConPrd.getContract());
		record.setStudent(targetConPrd.getContract().getStudent());
		record.setOperateUser(userService.getCurrentLoginUser());
		record.setProductType(productType);
		record.setPayTime(DateTools.getCurrentDateTime());
		record.setChargePayType(ChargePayType.CHARGE);
		
		String transactionTime = null;
		
		record.setContractProduct(targetConPrd);
		record.setQuality(auditedCourseHours);
		record.setChargeType(chargeType);
		record.setPayType(payType);
		record.setBlCampusId(belongCampus);
		record.setTransactionId(transactionId);
		
		if (ChargeType.NORMAL.equals(chargeType)) {
			if (null != course) {
				record.setCourse(course);
				record.setTeacher(course.getTeacher());
				transactionTime = course.getCourseDate()+" "+course.getCourseTime().substring(0, 5) + ":00";
			}
			if (null != miniClass) {
				record.setMiniClass(miniClass);
				record.setTeacher(miniClass.getTeacher());
			}
			if (null != miniClassCourse) {
				record.setTeacher(miniClassCourse.getTeacher());
				record.setMiniClassCourse(miniClassCourse);
				transactionTime = miniClassCourse.getCourseDate()+" "
						+miniClassCourse.getCourseTime() + ":00";
			}
			if (null != promiseClassRecord) {
				record.setPromiseClassRecord(promiseClassRecord);
				if (null != promiseClassRecord.getClassDate())  {
					transactionTime = promiseClassRecord.getClassDate() + " 00:00:00";
				} else {
					transactionTime = DateTools.getCurrentDateTime();
				}
			}
			if (null != otmClass) {
				record.setOtmClass(otmClass);
				record.setTeacher(otmClass.getTeacher());
			}
			if (null != otmClassCourse) {
				record.setOtmClassCourse(otmClassCourse);
				transactionTime = otmClassCourse.getCourseDate()+" "
						+otmClassCourse.getCourseTime() + ":00";
			}
			
			if (ProductType.OTHERS.equals(productType) ) {
				transactionTime = DateTools.getCurrentDateTime();
			}
		} else if (ChargeType.IS_NORMAL_INCOME.equals(chargeType)) {
			transactionTime = DateTools.getCurrentDateTime();
		}
		
		if (null != transactionTime) {
			record.setTransactionTime(transactionTime);
		}
		
		accountChargeRecordsDao.save(record);
		accountChargeRecordsDao.flush();
		pushChargeMsgToQueue(belongCampus.getId(), transactionTime, chargeAmount, auditedCourseHours, productType, chargeType, payType, ChargePayType.CHARGE);
	}
	
	protected void saveAccountChargeRecord_Edu(BigDecimal chargeAmount, ContractProduct targetConPrd, ProductType productType,
			Course course, BigDecimal auditedCourseHours, ChargeType chargeType,
			PayType payType, Organization belongCampus, String transactionId, MiniClass miniClass, MiniClassCourse miniClassCourse,
			PromiseClassRecord promiseClassRecord, OtmClass otmClass, OtmClassCourse otmClassCourse,User user) {
		AccountChargeRecords record =  new AccountChargeRecords();
		// 旧数据 可能会是 price 价格为 0
//		BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
		
		record.setProduct(targetConPrd.getProduct());
		record.setAmount(chargeAmount);
		record.setContract(targetConPrd.getContract());
		record.setStudent(targetConPrd.getContract().getStudent());
		record.setOperateUser(user);
		record.setProductType(productType);
		record.setPayTime(DateTools.getCurrentDateTime());
		record.setChargePayType(ChargePayType.CHARGE);
		
		String transactionTime = null;
		
		record.setContractProduct(targetConPrd);
		record.setQuality(auditedCourseHours);
		record.setChargeType(chargeType);
		record.setPayType(payType);
		record.setBlCampusId(belongCampus);
		record.setTransactionId(transactionId);
		
		if (ChargeType.NORMAL.equals(chargeType)) {
			if (null != course) {
				record.setCourse(course);
				record.setTeacher(course.getTeacher());
				transactionTime = course.getCourseDate()+" "+course.getCourseTime().substring(0, 5) + ":00";
			}
			if (null != miniClass) {
				record.setMiniClass(miniClass);
				record.setTeacher(miniClass.getTeacher());
			}
			if (null != miniClassCourse) {
				record.setTeacher(miniClassCourse.getTeacher());
				record.setMiniClassCourse(miniClassCourse);
				transactionTime = miniClassCourse.getCourseDate()+" "
						+miniClassCourse.getCourseTime() + ":00";
			}
			if (null != promiseClassRecord) {
				record.setPromiseClassRecord(promiseClassRecord);
				if (null != promiseClassRecord.getClassDate())  {
					transactionTime = promiseClassRecord.getClassDate() + " 00:00:00";
				} else {
					transactionTime = DateTools.getCurrentDateTime();
				}
			}
			if (null != otmClass) {
				record.setOtmClass(otmClass);
				record.setTeacher(otmClass.getTeacher());
			}
			if (null != otmClassCourse) {
				record.setOtmClassCourse(otmClassCourse);
				transactionTime = otmClassCourse.getCourseDate()+" "
						+otmClassCourse.getCourseTime() + ":00";
			}
			
			if (ProductType.OTHERS.equals(productType)  && StringUtils.isNotBlank(targetConPrd.getPaidTime())) {
				transactionTime = targetConPrd.getPaidTime().substring(0, 10) + " 00:00:00";
			}
		} else if (ChargeType.IS_NORMAL_INCOME.equals(chargeType)) {
			transactionTime = DateTools.getCurrentDateTime();
		}
		
		if (null != transactionTime) {
			record.setTransactionTime(transactionTime);
		}
		
		accountChargeRecordsDao.save(record);
		accountChargeRecordsDao.flush();
		pushChargeMsgToQueue(belongCampus.getId(), transactionTime, chargeAmount, auditedCourseHours, productType, chargeType, payType, ChargePayType.CHARGE);
	}
	
	
	/** 
	 * 合同产品缩单
	* @param contractProduct
	* @author  author :Yao 
	* @date  2016年8月8日 下午5:41:36 
	* @version 1.0 
	*/
	public void narrowContractProduct(ContractProduct cp) {
		BigDecimal promotionAmount=cp.getPromotionAmount();
		//如果合同产品未收款完成时，可以进行缩单
		if(cp.getStatus().equals(ContractProductStatus.NORMAL) 
			&& cp.getPaidStatus().equals(ContractProductPaidStatus.PAYING) 
			&& cp.getPaidAmount().compareTo(BigDecimal.ZERO)>0){
			//  实际课时   = 支付金额/单价  ，  如果有优惠金额     优惠比例=优惠金额/实际金额    ，实际优惠=支付金额*优惠比例。     
			BigDecimal realHours=cp.getPaidAmount().divide(cp.getPrice(),2,BigDecimal.ROUND_UP);//实际课时
			BigDecimal realPromotion=BigDecimal.ZERO;
			cp.setRealAmount(cp.getPaidAmount());
			
			if(cp.getPromotionAmount().compareTo(BigDecimal.ZERO)>0){
				realPromotion=cp.getPromotionAmount().divide(cp.getPlanAmount(),4,BigDecimal.ROUND_UP).multiply(cp.getPaidAmount());
				realHours=realHours.add(realPromotion.divide(cp.getPrice(),2,BigDecimal.ROUND_UP));
//				if(realPromotion.compareTo(BigDecimal.ZERO)==0){//如果比例非常小，特殊处理
//					realPromotion=new BigDecimal("0.01");
//				}
				cp.setPromotionAmount(realPromotion);
			}
			
			cp.setQuantity(realHours);
			cp.setPromotionAmount(realHours.multiply(cp.getPrice()).subtract(cp.getRealAmount()));
			cp.setPlanAmount(cp.getRealAmount().add(cp.getPromotionAmount()));
			
			cp.setPaidStatus(ContractProductPaidStatus.PAID);
			contractProductDao.save(cp);
			//减去资金分配的优惠金额
			moneyArrangeLogDao.saveOneRecord(cp, cp.getContract().getStudent(), promotionAmount.subtract(cp.getPromotionAmount()), userService.getCurrentLoginUser(), "提取", PayType.PROMOTION);
		}else if(cp.getRealAmount().compareTo(BigDecimal.ZERO)>0  && cp.getPaidAmount().compareTo(BigDecimal.ZERO)==0){
			
			if(cp.getType().equals(ProductType.SMALL_CLASS)){
				outOfMiniClass(cp);
			}
			
			
			
			moneyArrangeLogService.deleteByContractProductId(cp.getId());
			contractProductDao.deleteById(cp.getId()); // 删除该产品
			contractDao.flush();
		}
	}
	
	
	//通过合同产品退出小班
	public void outOfMiniClass(ContractProduct cp){
		
		MiniClass mini=smallClassService.getMiniClassByContractProduct(cp);
		if(mini!=null){
			try {
				smallClassService.deleteStudentInMiniClasss(cp.getContract().getStudent().getId(), mini.getMiniClassId());
			} catch (Exception e) {
				e.printStackTrace();
				throw new ApplicationException("缩单时退出小班有问题，可以再次尝试，或者把未交款的合同产品手动删除后再做缩单处理。");
			}
		}
	}
	
    
	public void pushChargeMsgToQueue(String campusId,String countDate,BigDecimal amount,BigDecimal quantity,ProductType type,ChargeType chargeType,PayType payType,ChargePayType chargePayType){
		IncomeMessage message=new IncomeMessage();
		message.setCampusId(campusId);
		message.setAmount(amount);
		message.setQuantity(quantity);
		message.setType(type);
		message.setChargePayType(chargePayType);
		message.setChargeType(chargeType);
		message.setPayType(payType);
		message.setCountDate(countDate);
		
		try {// 加入队列
			JedisUtil.lpush(ObjectUtil.objectToBytes("incomeQueue"),ObjectUtil.objectToBytes(message));
		} catch (Exception e) {
			// 错了
		}
		
	}
}
