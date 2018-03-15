package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.ChargeOrWashCurriculumVo;
import com.eduboss.domainVo.IncomeDistributionVo;
import com.eduboss.service.*;
import com.eduboss.service.handler.impl.OtherConProdHandler;
import com.eduboss.task.*;
import com.eduboss.utils.*;
import com.google.common.collect.Maps;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.BaseStatus;
import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractPaidStatus;
import com.eduboss.common.ContractProductPaidStatus;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.DistributeType;
import com.eduboss.common.ElectronicAccChangeType;
import com.eduboss.common.LectureClassStudentChargeStatus;
import com.eduboss.common.MiniClassStudentChargeStatus;
import com.eduboss.common.OtmClassStudentChargeStatus;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.common.StudentStatus;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.domainVo.MoneyWashRecordsVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.jedis.IncomeMessage;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.service.handler.impl.MiniClassConProdHandler;
import com.eduboss.service.handler.impl.OtmClassConProdHandler;

@Service("com.eduboss.service.ChargeService")
public class ChargeServiceImpl implements ChargeService {

	Logger log= Logger.getLogger(ChargeServiceImpl.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContractService contractService;
	
	@Autowired
	private CourseDao courseDao; 
	
	@Autowired
	private SmallClassDao smallClassDao;
	
	@Autowired
	private AccountChargeRecordsDao accountChargeRecordsDao;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private MiniClassStudentAttendentDao miniClassStudentAttendentDao;
	
	@Autowired
	private MiniClassCourseDao miniClassCourseDao;
	
	@Autowired
	private StudnetAccMvDao studnetAccMvDao;
	
	@Autowired
	private MiniClassStudentDao miniClassStudentDao;
	
	@Autowired
	private RollbackBackupRecordsDao rollbackBackupRecordsDao;
	
	@Autowired
	private MoneyRollbackRecordsDao moneyRollbackRecordsDao;
	
	@Autowired
	private CourseAttendanceRecordDao courseAttendanceRecordDao;
	
	@Autowired
	private PromiseClassRecordDao promiseClassRecordDao;
	
	@Autowired
	private OperationCountService operationCountService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseChargeDao courseChargeDao;
	
	@Autowired
	private SmallClassService smallClassService;
	
	@Autowired
	private OtmClassStudentAttendentDao otmClassStudentAttendentDao;
	
	@Autowired
	private OtmClassCourseDao otmClassCourseDao;
	
	@Autowired
	private OtmClassStudentDao otmClassStudentDao;
	
	@Autowired
	private StudentOrganizationDao studentOrganizationDao;
	
	@Autowired
	private OtmClassService otmClassService;
	
	@Autowired
	private PromiseStudentDao promiseStudentDao;
	
	@Autowired
	private ElectronicAccountChangeLogDao electronicAccountChangeLogDao;
	
	@Autowired
	private MoneyWashRecordsDao moneyWashRecordsDao;
	
	@Autowired
	private LectureClassStudentDao lectureClassStudentDao;
	
	@Autowired
	private RedisDataSource redisDataSource;
	
	@Autowired
	private ContractProductSubjectService contractProductSubjectService;
	
	@Autowired
	private CourseHoursDistributeRecordService courseHoursDistributeRecordService;

	@Autowired
	private TwoTeacherClassStudentAttendentDao twoTeacherClassStudentAttendentDao;

	@Autowired
	private TwoTeacherClassStudentDao twoTeacherClassStudentDao;

	private final static ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private PromiseClassSubjectDao promiseClassSubjectDao;

	@Autowired
	private ObjectHashCodeDao objectHashCodeDao;

	@Autowired
	private FundsChangeHistoryDao fundsChangeHistoryDao;

	@Autowired
	private IncomeDistributionDao incomeDistributionDao;

	@Autowired
	private PromiseClassService promiseClassService;


	@Override
	public BigDecimal chargeOneOnOneCourse(String courseId, User user) {
		BigDecimal courseAmout=BigDecimal.ZERO;
		// 星火的特有的逻辑
		Course course =  courseDao.findById(courseId);
		BigDecimal chargeCourseHour = course.getRealHours();
		
//		if (CourseStatus.CHARGED.equals(course.getCourseStatus())) {
//			throw new ApplicationException("课程:" + course.getCourseId() + "，已经扣费!");
//		} 
		
		if (chargeCourseHour == null) {
			throw new ApplicationException(ErrorCode.COURSE_STUDY_MANAGEMENT_NOT_AUDIT);
		}

        if(course.getProduct() == null) {
            throw new ApplicationException(courseId + "课程未关联产品，无法扣费!");
        }
		
		// 按照时间顺序获取不同的合同产品
		// old: 已经废除， 应该考虑学生的剩余课时.(有剩余资金， 从合同产品里面拿出单价， 接着计算他的剩余资金不是足以扣费)
		// 如果可以就直接扣费 
		// 如果不行就, 就扣除部分再继续扣费
//		List<ContractProduct> oneOnOneProductList = contractService.getOrderValidOneOnOneContractProducts(course);
		
        BigDecimal oneOnOneHoursSum = contractService.countOrderValidSubjectOneOnOneContractProducts(course);
        if (course.getRealHours().compareTo(oneOnOneHoursSum) > 0) {
        	throw new ApplicationException("本合同产品的" + course.getSubject().getName() + "科目对应的分配课时不足这次扣费");
        }
        
        // 按照时间顺序获取不同的对应课程科目合同产品
		List<ContractProduct> oneOnOneProductList = contractService.getOrderValidSubjectOneOnOneContractProducts(course);

		//#5435 学生在走退费流程，1对1课程考勤对应的产品问题 （只要有一份没有冻结的合同产品就跳过）
		boolean allContractProductFrozen = true;
		List<ContractProduct> withOutFrozenList = new ArrayList<>();
		for (ContractProduct cp: oneOnOneProductList){
			if (cp.getIsFrozen() == 1){
				allContractProductFrozen = false;
				withOutFrozenList.add(cp);
			}
		}
		if (oneOnOneProductList.size()>0 && allContractProductFrozen){
			throw new ApplicationException("扣费所有合同产品已经冻结，不能进行扣费操作！");
		}

		ContractProductHandler handler = contractService.selectHandlerByType(ProductType.ONE_ON_ONE_COURSE);
		String transactionId = UUID.randomUUID().toString();
		for(ContractProduct conProd :withOutFrozenList) {
			BigDecimal price =  conProd.getPrice();
			BigDecimal chargeHours = BigDecimal.ZERO;
			if (chargeCourseHour.compareTo(conProd.getSubjectRemainHours()) >= 0) {
				chargeHours = conProd.getSubjectRemainHours();
			} else {
				chargeHours = chargeCourseHour;
			}
			BigDecimal chargeMoney = price.multiply(chargeHours).setScale(2, BigDecimal.ROUND_DOWN);
			BigDecimal remainingMoney = conProd.getRemainingAmount();
					
			if(remainingMoney.compareTo(chargeMoney)>=0 && remainingMoney.compareTo(BigDecimal.ZERO)>0 && conProd.getSubjectRemainHours().compareTo(chargeCourseHour) >= 0) {
				handler.chargeOneOnOneClass(conProd.getContract(), conProd, chargeMoney, chargeCourseHour , course, transactionId);
				chargeCourseHour = BigDecimal.ZERO;
				if(chargeMoney!=null){
					courseAmout=courseAmout.add(chargeMoney);
				}
				break;
			} else if(remainingMoney.compareTo(chargeMoney)>=0 && remainingMoney.compareTo(BigDecimal.ZERO)>0){
				chargeCourseHour = chargeCourseHour.subtract(conProd.getSubjectRemainHours());
				if(chargeMoney!=null){
					courseAmout=courseAmout.add(chargeMoney);
				}
				handler.chargeOneOnOneClass(conProd.getContract(), conProd, chargeMoney, conProd.getSubjectRemainHours() , course, transactionId);				
			}
			
		}
		if(oneOnOneProductList == null || oneOnOneProductList.size() == 0 || chargeCourseHour.compareTo(BigDecimal.ZERO) > 0 ) {
			throw new ApplicationException(course.getStudent().getName()+" - "+ErrorCode.ONE_ON_ONE_MONEY_NOT_ENOUGH.getErrorString());			
		}
		if(course.getStudent().getStatus() != null && course.getStudent().getStatus() == StudentStatus.GRADUATION){
			String errMsg = "学生（"+course.getStudent().getName()+"）状态为“毕业”，相关课程操作已被禁止，请核实后再继续操作。";
			throw new ApplicationException(errMsg);
		}
		return courseAmout;
	}
	
	@Override
	public boolean chargeMiniClass(String miniClassStudentAttendentId , User user) {
		
		boolean hasCharge = false;
		
		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.findById(miniClassStudentAttendentId);
//		MiniClassModalVo miniClass = miniClassStudentAttendent.getMiniClassCourse().getMiniClass();
		MiniClassCourse miniClassCourse= miniClassCourseDao.findById(miniClassStudentAttendent.getMiniClassCourse().getMiniClassCourseId()); 
		MiniClass miniClass = miniClassCourse.getMiniClass();
		miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.CHARGED);
		// 获取到课时 和 小班Product
		Double courseHour  = miniClassCourse.getCourseHours();
		
		// 获取 targe Contract product 
		ContractProduct targetConPrd = null; 
		MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClass.getMiniClassId(), miniClassStudentAttendent.getStudentId());
//		Set<MiniClassStudent> miniClassStudents = miniClass.getMiniClassStudents();
//		if(miniClassStudents!=null){
//			// 获取到 contract
//			for(MiniClassStudent student : miniClassStudents) {
//				targetConPrd = student.getContractProduct();
//				break;
//			}
//		}
		targetConPrd = miniClassStudent.getContractProduct();
		if(targetConPrd!=null && 
				(targetConPrd.getStatus() == ContractProductStatus.STARTED
				||	targetConPrd.getStatus() == ContractProductStatus.NORMAL)) {
			
			MiniClassConProdHandler prodHandler = (MiniClassConProdHandler) contractService.selectHandlerByType(ProductType.SMALL_CLASS);
			prodHandler.chargeMiniClass(targetConPrd, miniClass, miniClassCourse, courseHour);

			/**
			 * 培优的逻辑
			 */
			if ("advance".equals(PropertiesUtils.getStringValue("institution"))){
				// 小班扣费，如果其他合同产品没扣费则直接扣费
				Contract contract = contractDao.findById(targetConPrd.getContract().getId());
				Set<ContractProduct> cpSet = contract.getContractProducts();
				for (ContractProduct cp : cpSet) {
					if (cp.getType() == ProductType.OTHERS && cp.getStatus() == ContractProductStatus.NORMAL && cp.getPaidStatus() == ContractProductPaidStatus.PAID && cp.getIsFrozen() == 1) {
						OtherConProdHandler otherProdHandler = (OtherConProdHandler) contractService.selectHandlerByType(ProductType.OTHERS);
						otherProdHandler.chargeOtherProduct(contract, cp, cp.getTotalAmount(), cp.getQuantity().subtract(cp.getConsumeQuanity()) );
					}
				}
			}
			
			/*AccountChargeRecords record =  new AccountChargeRecords();
			record.setProduct(targetConPrd.getProduct());
			// 旧数据 可能会是 price 价格为 0
			BigDecimal price = targetConPrd.getPrice() == null? BigDecimal.ZERO:targetConPrd.getPrice(); 
			// 总价格 = 单价 * 课时 * 折扣
			BigDecimal detel = price.multiply(targetConPrd.getDealDiscount()).multiply(BigDecimal.valueOf(courseHour));
			record.setAmount(detel);
			record.setContract(targetConPrd.getContract());
			record.setStudent(targetConPrd.getContract().getStudent());
			record.setOperateUser(user);
			record.setProductType(ProductType.SMALL_CLASS);
			record.setPayTime(DateTools.getCurrentDateTime());
			record.setMiniClass(miniClass);
			record.setMiniClassCourse(miniClassCourse);
			
			record.setContractProduct(targetConPrd);
			record.setQuality(BigDecimal.valueOf(courseHour));
			
			accountChargeRecordsDao.save(record);
			
			accountChargeRecordsDao.flush();
			
			// 更新 并且 检测合同产品
			updateContractProductTrigger(new BigDecimal(courseHour), detel, targetConPrd);
			// 更新 并且 检测合同
			updateContractTrigger(detel, targetConPrd);
			
			// 更新Student view , 更新已消费的金额
			Student student =  record.getStudent();
			StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
			if(studnetAccMv == null) {
				studnetAccMv =  new StudnetAccMv(student);
				studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
				studnetAccMvDao.flush();
			} 
			studnetAccMv.setConsumeAmount(studnetAccMv.getConsumeAmount().add(record.getAmount()));
			studnetAccMv.setRemainingAmount(studnetAccMv.getPaidAmount().subtract(studnetAccMv.getConsumeAmount()));
			studnetAccMv.setMiniConsumeAmount(studnetAccMv.getMiniConsumeAmount().add( detel));
			*/
			hasCharge = true;
		}
		return hasCharge;
		
		
		

		
//		boolean hasCharge = false;
//		
//		List<Contract> contracts = contractDao.getOrderContracts(targetStuId, Order.desc("createTime"));
//		// 找到有报读这个产品的contract
//		for(Contract contract : contracts) {
//			if(hasCharge){
//				break;
//			} else {
//				for(ContractProduct contractProduct : contract.getContractProducts()) {
//					if(contractProduct.getProduct().getId().equals(miniProduct.getId()) && 
//							contractProduct.getStatus() == ContractProductStatus.STARTED) {
//						
//						AccountChargeRecords record =  new AccountChargeRecords();
//						record.setProduct(contractProduct.getProduct());
//						// 旧数据 可能会是 price 价格为 0
//						BigDecimal price = contractProduct.getPrice() == null? BigDecimal.ZERO:contractProduct.getPrice(); 
//						// 总价格 = 单价 * 课时 * 折扣
//						BigDecimal detel = price.multiply(contractProduct.getDealDiscount()).multiply(new BigDecimal(courseHour));
//						record.setAmount(detel);
//						record.setContract(contract);
//						record.setStudent(contractProduct.getContract().getStudent());
//						record.setOperateUser(user);
//						record.setProductType(ProductType.SMALL_CLASS);
//						record.setPayTime(DateTools.getCurrentDateTime());
//						record.setMiniClass(miniClass);
//						
//						record.setContractProduct(contractProduct);
//						record.setQuality(new BigDecimal(courseHour));
//						
//						accountChargeRecordsDao.save(record);
//						
//						accountChargeRecordsDao.flush();
//						
//						contractProduct.setConsumeAmount(contractProduct.getConsumeAmount().add(detel));
//						contractProduct.setConsumeQuanity(contractProduct.getConsumeQuanity().add(new BigDecimal(courseHour)));
//						
//						hasCharge = true;
//						break;
//
//					}
//				}
//			}
//		}
//		
//		return hasCharge;
		
	}


	@Override
	public boolean chargeMiniClass_Edu(String miniClassStudentAttendentId ,User user) {

		boolean hasCharge = false;

		MiniClassStudentAttendent miniClassStudentAttendent = miniClassStudentAttendentDao.findById(miniClassStudentAttendentId);
//		MiniClassModalVo miniClass = miniClassStudentAttendent.getMiniClassCourse().getMiniClass();
		MiniClassCourse miniClassCourse= miniClassCourseDao.findById(miniClassStudentAttendent.getMiniClassCourse().getMiniClassCourseId());
		MiniClass miniClass = miniClassCourse.getMiniClass();
		miniClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.CHARGED);
		// 获取到课时 和 小班Product
		Double courseHour  = miniClassCourse.getCourseHours();

		// 获取 targe Contract product
		ContractProduct targetConPrd = null;
		MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClass.getMiniClassId(), miniClassStudentAttendent.getStudentId());
//		Set<MiniClassStudent> miniClassStudents = miniClass.getMiniClassStudents();
//		if(miniClassStudents!=null){
//			// 获取到 contract
//			for(MiniClassStudent student : miniClassStudents) {
//				targetConPrd = student.getContractProduct();
//				break;
//			}
//		}
		targetConPrd = miniClassStudent.getContractProduct();
		if(targetConPrd!=null &&
				(targetConPrd.getStatus() == ContractProductStatus.STARTED
				||	targetConPrd.getStatus() == ContractProductStatus.NORMAL)) {

			MiniClassConProdHandler prodHandler = (MiniClassConProdHandler) contractService.selectHandlerByType(ProductType.SMALL_CLASS);
			prodHandler.chargeMiniClass_Edu(targetConPrd, miniClass, miniClassCourse, courseHour,user);

			/**
			 * 培优的逻辑
			 */
			if ("advance".equals(PropertiesUtils.getStringValue("institution"))){
				// 小班扣费，如果其他合同产品没扣费则直接扣费
				Contract contract = contractDao.findById(targetConPrd.getContract().getId());
				Set<ContractProduct> cpSet = contract.getContractProducts();
				for (ContractProduct cp : cpSet) {
					if (cp.getType() == ProductType.OTHERS && cp.getStatus() == ContractProductStatus.NORMAL && cp.getPaidStatus() == ContractProductPaidStatus.PAID && cp.getIsFrozen() == 1) {
						OtherConProdHandler otherProdHandler = (OtherConProdHandler) contractService.selectHandlerByType(ProductType.OTHERS);
						otherProdHandler.chargeOtherProduct(contract, cp, cp.getTotalAmount(), cp.getQuantity().subtract(cp.getConsumeQuanity()) );
					}
				}
			}
			hasCharge = true;
		}
		return hasCharge;

	}


	/**
	 * 扣每次一对多费用， 每上一次课就会扣钱
	 * 先判断 这个课程 是不是 开课的（STARTED）,而且有 剩余资金的
	 * 获取同一一对多产品类型的同和产品循环扣费
	 * @param otmClassStudentAttendentId
	 * @param user
     * @return
     */
	@Override
	public boolean chargeOtmClass(String otmClassStudentAttendentId, User user) {
		boolean hasCharge = false;
		OtmClassStudentAttendent otmClassStudentAttendent = otmClassStudentAttendentDao.findById(otmClassStudentAttendentId);
		OtmClassCourse otmClassCourse= otmClassCourseDao.findById(otmClassStudentAttendent.getOtmClassCourse().getOtmClassCourseId()); 
		OtmClass otmClass = otmClassCourse.getOtmClass();
		
		// 获取到课时
		Double courseHour  = otmClassCourse.getCourseHours();
		
		// 获取 targe Contract product list
		List<ContractProduct> cpList = contractProductDao.getOtmContractProductByOtmTypeAndStu(otmClassCourse.getOtmClass().getOtmType(), otmClassStudentAttendent.getStudentId());
		BigDecimal lastDetel = null;
		OtmClassConProdHandler prodHandler = (OtmClassConProdHandler) contractService.selectHandlerByType(ProductType.ONE_ON_MANY);
		String transactionId = UUID.randomUUID().toString();
		for (ContractProduct cp : cpList) {
			if(cp!=null && (cp.getStatus() == ContractProductStatus.STARTED
					||	cp.getStatus() == ContractProductStatus.NORMAL) && cp.getRemainingAmount().compareTo(BigDecimal.ZERO)>0) {
				BigDecimal remainingAmount = cp.getRemainingAmount();
				// 旧数据 可能会是 price 价格为 0
				BigDecimal price = cp.getPrice() == null? BigDecimal.ZERO:cp.getPrice();
				BigDecimal detel = BigDecimal.ZERO;
				BigDecimal realPrice = cp.getDealDiscount().compareTo(BigDecimal.ZERO) > 0 ? price.multiply(cp.getDealDiscount()) : price;
				detel = realPrice.multiply(BigDecimal.valueOf(courseHour));
				if (lastDetel == null) {
					lastDetel = detel;
				}
				if(remainingAmount.compareTo(lastDetel)>=0 && (cp.getStatus() == ContractProductStatus.NORMAL ||cp.getStatus() == ContractProductStatus.STARTED)) {
					lastDetel = BigDecimal.ZERO;
					prodHandler.chargeOtmClass(cp, otmClass, otmClassCourse, courseHour, transactionId);
					hasCharge = true;
					break;
				} else {//此逻辑直接扣金额，不走课时换算
					if (cp.getStatus() == ContractProductStatus.NORMAL ||cp.getStatus() == ContractProductStatus.STARTED) {
						BigDecimal chargeHour = remainingAmount.divide(realPrice,2,RoundingMode.UP);//进位
						//如果是跨产品扣费，直接扣剩下的金额，再用金额算课时，课时只保留两位。
						prodHandler.chargeOtmClass(cp, otmClass, otmClassCourse, chargeHour.doubleValue(), transactionId,remainingAmount);
						lastDetel = lastDetel.subtract(remainingAmount);
						courseHour = courseHour - chargeHour.doubleValue();
					}
					
				}
			}
		}
		
		return hasCharge;
		
	}

	@Override
	public DataPackage findPageMyCharge(DataPackage dataPackage,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo) {


		dataPackage =  accountChargeRecordsDao.listAccountChargeRecord(dataPackage, accountChargeRecordsVo, timeVo);
//		dp =  contractDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "createTime", "desc"), criterionList);
		List<Object[]> accChargeList =  (List<Object[]>) dataPackage.getDatas();

		List<AccountChargeRecordsVo> fundVoList = new ArrayList<AccountChargeRecordsVo>();
		for(Object[] obj : accChargeList){
			AccountChargeRecords accountChargeRecords=(AccountChargeRecords) obj[0];
			AccountChargeRecordsVo vo =HibernateUtils.voObjectMapping(accountChargeRecords, AccountChargeRecordsVo.class);
			if(accountChargeRecords.getCourse()!=null){//accountChargeRecords.getCourse()!=null
				Course course = accountChargeRecords.getCourse();//(Course)obj[2];
				vo.setCourseDate(course.getCourseDate());
				vo.setTeacherName(accountChargeRecords.getCourse().getTeacher().getName());
				vo.setPayHour(accountChargeRecords.getQuality());
				vo.setCourseTime(accountChargeRecords.getCourse().getCourseDate()+" "+accountChargeRecords.getCourse().getCourseTime());
				vo.setGrade(course.getGrade().getName());
			}else if(accountChargeRecords.getMiniClassCourse()!=null){//accountChargeRecords.getMiniClassCourse()!=null
				MiniClassCourse miniClassCourse = accountChargeRecords.getMiniClassCourse();//(MiniClassCourse)obj[3]
				vo.setCourseDate(miniClassCourse.getCourseDate());
				vo.setTeacherName(accountChargeRecords.getMiniClassCourse().getTeacher().getName());
				if(accountChargeRecords.getMiniClassCourse().getCourseHours()!=null)
//					vo.setPayHour(BigDecimal.valueOf(accountChargeRecords.getMiniClassCourse().getCourseHours()));
					vo.setPayHour(accountChargeRecords.getQuality());
				vo.setCourseTime(accountChargeRecords.getMiniClassCourse().getCourseDate()+" "
			+accountChargeRecords.getMiniClassCourse().getCourseTime()+"-"+accountChargeRecords.getMiniClassCourse().getCourseEndTime());
				if(miniClassCourse.getGrade()!=null){
					vo.setGrade(miniClassCourse.getGrade().getName());
				}else{
					vo.setGrade("");
				}
			} else if (accountChargeRecords.getOtmClassCourse() != null) {//accountChargeRecords.getOtmClassCourse() != null
				OtmClassCourse otmClassCourse = accountChargeRecords.getOtmClassCourse();//(OtmClassCourse)obj[4]
				vo.setCourseDate(otmClassCourse.getCourseDate());
				vo.setTeacherName(accountChargeRecords.getOtmClassCourse().getTeacher().getName());
				if(accountChargeRecords.getOtmClassCourse().getCourseHours()!=null)
					vo.setPayHour(accountChargeRecords.getQuality());
				vo.setCourseTime(accountChargeRecords.getOtmClassCourse().getCourseDate()+" "
			+accountChargeRecords.getOtmClassCourse().getCourseTime()+"-"+accountChargeRecords.getOtmClassCourse().getCourseEndTime());
				if(otmClassCourse.getGrade()!=null){
					vo.setGrade(otmClassCourse.getGrade().getName());
				}else{
					vo.setGrade("");
				}
			}else if(accountChargeRecords.getPromiseClassRecord()!=null){
				PromiseClassRecord pcr =accountChargeRecords.getPromiseClassRecord();
				vo.setPayHour(pcr.getChargeHours());
				if(pcr.getPromiseClass()!=null && pcr.getPromiseClass().getHead_teacher()!=null){
					vo.setTeacherName(pcr.getPromiseClass().getHead_teacher().getName());
				}

			}else if(accountChargeRecords.getLectureClassStudent()!=null){
				LectureClassStudent lecStu =accountChargeRecords.getLectureClassStudent();
				vo.setPayHour(accountChargeRecords.getQuality());
				vo.setCourseTime(accountChargeRecords.getTransactionTime());
				vo.setMiniClassName(lecStu.getLectureClass().getLectureName());
			}else if (accountChargeRecords.getTwoTeacherClassStudentAttendent()!=null){
				TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent = accountChargeRecords.getTwoTeacherClassStudentAttendent();
				vo.setMiniClassName(twoTeacherClassStudentAttendent.getTwoTeacherClassTwo().getName());
//				TwoTeacherClassCourse twoTeacherClassCourse = twoTeacherClassStudentAttendent.getTwoTeacherClassCourse();
//				vo.setCourseTime(twoTeacherClassCourse.getCourseDate()+" "
//						+twoTeacherClassCourse.getCourseTime()+"-"+twoTeacherClassCourse.getCourseEndTime());
				vo.setCourseTime(twoTeacherClassStudentAttendent.getCourseDateTime());
				vo.setPayHour(accountChargeRecords.getQuality());
				TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassStudentAttendent.getTwoTeacherClassTwo();
				vo.setTeacherName(twoTeacherClassTwo.getTeacher().getName());
			}else if (accountChargeRecords.getCurriculum()!=null){
				Curriculum curriculum = accountChargeRecords.getCurriculum();
				vo.setPayHour(accountChargeRecords.getQuality());
				vo.setCourseTime(curriculum.getStartDateTime());
				User teacher = userService.findUserByAccount(curriculum.getTeacher());
				if (teacher!=null){
					vo.setTeacherName(teacher.getName());
				}

			}
			vo.setChargeTypeName(accountChargeRecords.getChargeType().getName());
			vo.setChargeTypeValue(accountChargeRecords.getChargeType().getValue());

			vipCanWash(vo);

			fundVoList.add(vo);
		}
		dataPackage.setDatas(fundVoList);
		return dataPackage;
	}

	private void vipCanWash(AccountChargeRecordsVo vo) {

		String contractId = vo.getContractId();
		Contract contract = contractDao.findById(contractId);
		if (contract.isEcsContract()){
            Set<ContractProduct> contractProducts = contract.getContractProducts();

            boolean flag = false;

            for (ContractProduct cp : contractProducts){
                if (promiseClassService.isYearAfter2018(cp)){
                    flag = true;
                    break;
                }
            }

            if (flag){
                vo.setHasEcsCPAfter2018(true);
            }else {
                vo.setHasEcsCPAfter2018(false);
            }
        }
	}
	
	/*@Override
	public DataPackage findPageMyCharge(DataPackage dataPackage,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo) {
		dataPackage =  accountChargeRecordsDao.listAccountChargeRecord(dataPackage, accountChargeRecordsVo, timeVo);
		List<AccountChargeRecordsJdbc> accChargeRecordJdbcList = (List<AccountChargeRecordsJdbc>) dataPackage.getDatas();
		List<AccountChargeRecordsVo> fundVoList = new ArrayList<AccountChargeRecordsVo>();
		for(AccountChargeRecordsJdbc record : accChargeRecordJdbcList){
			AccountChargeRecordsVo vo =HibernateUtils.voObjectMapping(record, AccountChargeRecordsVo.class, "accountChargeRecordsJdbc");
			if(StringUtil.isNotBlank(record.getCourseId())){
				Course course = courseDao.findById(record.getCourseId());
				vo.setCourseDate(course.getCourseDate());
				vo.setTeacherName(course.getTeacher().getName());
				vo.setPayHour(record.getQuantity());
				vo.setCourseTime(course.getCourseDate()+" "+course.getCourseTime());
				vo.setGrade(course.getGrade().getName());
			}else if(StringUtil.isNotBlank(record.getMiniClassCourseId())){
				MiniClassCourse miniClassCourse = miniClassCourseDao.findById(record.getMiniClassCourseId());
				vo.setCourseDate(miniClassCourse.getCourseDate());
				vo.setTeacherName(miniClassCourse.getTeacher().getName());
				if(miniClassCourse.getCourseHours()!=null)
					vo.setPayHour(record.getQuantity());
				vo.setCourseTime(miniClassCourse.getCourseDate()+" "
					+miniClassCourse.getCourseTime()+"-"+miniClassCourse.getCourseEndTime());
				if(miniClassCourse.getGrade()!=null){
					vo.setGrade(miniClassCourse.getGrade().getName());
				}else{
					vo.setGrade("");
				}
				vo.setMiniClassName(miniClassCourse.getMiniClassName());
			} else if (StringUtil.isNotBlank(record.getOtmClassCourseId())) {
				OtmClassCourse otmClassCourse = otmClassCourseDao.findById(record.getOtmClassCourseId());
				vo.setCourseDate(otmClassCourse.getCourseDate());
				vo.setTeacherName(otmClassCourse.getTeacher().getName());
				if(otmClassCourse.getCourseHours()!=null)
					vo.setPayHour(record.getQuantity());
				vo.setCourseTime(otmClassCourse.getCourseDate()+" "
						+otmClassCourse.getCourseTime()+"-"+otmClassCourse.getCourseEndTime());
				if(otmClassCourse.getGrade()!=null){
					vo.setGrade(otmClassCourse.getGrade().getName());
				}else{
					vo.setGrade("");
				}
				vo.setOtmClassName(otmClassCourse.getOtmClassName());
			}else if(StringUtil.isNotBlank(record.getPromiseClassRecordId())){
				PromiseClassRecord pcr = promiseClassRecordDao.findById(record.getPromiseClassRecordId());
				if(pcr!=null){
					vo.setPayHour(pcr.getChargeHours());
					if(pcr.getPromiseClass()!=null && pcr.getPromiseClass().getHead_teacher()!=null){
						vo.setTeacherName(pcr.getPromiseClass().getHead_teacher().getName());
					}
				}
			}else if(StringUtil.isNotBlank(record.getLectureClassStudentId())){
				LectureClassStudent lecStu = lectureClassStudentDao.findById(record.getLectureClassStudentId());
				vo.setPayHour(record.getQuantity());
				vo.setCourseTime(record.getTransactionTime());
				vo.setMiniClassName(lecStu.getLectureClass().getLectureName());
			}
			vo.setChargeTypeName(record.getChargeType().getName());
			vo.setChargeTypeValue(record.getChargeType().getValue());
			fundVoList.add(vo);
		}
		dataPackage.setDatas(fundVoList);
		return dataPackage;
	}*/
	
	/**
	 * 回滚扣费操作
	 */
	@Override
	public void rollbackCharge(MoneyRollbackRecords moneyRollbackRecords) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByTransactionId(moneyRollbackRecords.getTransactionId());
		for (AccountChargeRecords record : records) {
			if (record.getContractProduct() != null && record.getContractProduct().getStatus().equals(ContractProductStatus.CLOSE_PRODUCT)) {
				throw new ApplicationException("本次扣费涉及的合同产品已经操作过结课退费，不可再操作资金回滚。");
			}
			if (record.getContractProduct() != null && record.getContractProduct().getIsFrozen() == 0) {
				throw new ApplicationException("本次扣费涉及的合同产品在冻结状态，不可再操作资金回滚。");
			}
		}
		
		if (records.size() > 0) {
			Organization organization = null;
			ChargeType chargeType = null;
			ProductType productType = null;
			Student student = null;
			User originalOperateUser = null;
			String transactionTime = null;
			BigDecimal chargeHourse = BigDecimal.ZERO;
			BigDecimal chargeAmount = BigDecimal.ZERO;
			String courseDate = null;
			String courseId = null;
			String otmCourseDate = null;
			String otmCourseId = null;
			String micCourseDate = null;
			String micCourseId = null;
			String lectureDate = null;//讲座日期
			String lectureStuId = null;//讲座学生考勤ID
			for (AccountChargeRecords record : records) {
				organization = organization == null ? record.getBlCampusId() : organization;
				chargeType = chargeType == null ? record.getChargeType() : chargeType;
				productType = productType == null ? record.getProductType() : productType;
				student = student == null ? record.getStudent(): student;
				originalOperateUser = originalOperateUser == null ? record.getOperateUser() : originalOperateUser;
				transactionTime = transactionTime == null ? record.getTransactionTime() : transactionTime;
				if (ProductType.ONE_ON_ONE_COURSE.equals(record.getProductType()) 
						|| ProductType.SMALL_CLASS.equals(record.getProductType())
						|| ProductType.ONE_ON_MANY.equals(record.getProductType())
						|| ProductType.ECS_CLASS.equals(record.getProductType())
						|| ProductType.LECTURE.equals(record.getProductType())) {
					chargeHourse = chargeHourse.add(record.getQuality());
				}else if(ProductType.OTHERS.equals(record.getProductType()) && record.getPayType().equals(PayType.PROMOTION)){//其他产品的优惠不需要回滚
					continue;
				}
				chargeAmount = chargeAmount.add(record.getAmount());
				Course course = record.getCourse();
				OtmClassCourse otmCourse=record.getOtmClassCourse();
				MiniClassCourse miniClassCourse = record.getMiniClassCourse();
				LectureClassStudent lectureStu=record.getLectureClassStudent();
				if (course != null) {
					courseDate = courseDate == null ? course.getCourseDate() : courseDate;
					courseId = courseId == null ? course.getCourseId() : courseId;
				}
				if(otmCourse != null){
					otmCourseDate = otmCourseDate==null?otmCourse.getCourseDate():otmCourseDate;
					otmCourseId = otmCourseId==null?otmCourse.getOtmClassCourseId():otmCourseId;
				}
				if (miniClassCourse != null){
					micCourseDate = micCourseDate==null?miniClassCourse.getCourseDate():micCourseDate;
					micCourseId = micCourseId==null?miniClassCourse.getMiniClassCourseId():micCourseId;
				}
				
				if(lectureStu!=null){
					lectureDate = lectureDate==null?lectureStu.getLectureClass().getStartDate():lectureDate;
					lectureStuId = lectureStuId==null?lectureStu.getId():lectureStuId;
				}

				this.rollbackAccountChargeRecords(record);
				this.backupRecord(record);
				
//				RollbackBackupRecords rollbackRecords = HibernateUtils.voObjectMapping(record, RollbackBackupRecords.class);
//				rollbackBackupRecordsDao.save(rollbackRecords);
//				accountChargeRecordsDao.delete(record);
			}
			moneyRollbackRecords.setBlCampusId(organization);
			moneyRollbackRecords.setChargeType(chargeType);
			moneyRollbackRecords.setModifyTime(DateTools.getCurrentDateTime());
			moneyRollbackRecords.setModifyOperator(userService.getCurrentLoginUser());
			moneyRollbackRecords.setProductType(productType);
			moneyRollbackRecords.setRollbackTime(DateTools.getCurrentDateTime());
			moneyRollbackRecords.setRollbackOperator(userService.getCurrentLoginUser());
			moneyRollbackRecords.setStudent(student);
			moneyRollbackRecords.setOriginalOperateUser(originalOperateUser);
			moneyRollbackRecords.setTransactionTime(transactionTime);
			moneyRollbackRecords.setChargeHourse(chargeHourse);
			moneyRollbackRecords.setChargeAmount(chargeAmount);
			moneyRollbackRecordsDao.save(moneyRollbackRecords);
			
			// 主流程跑完，commit数据库
			moneyRollbackRecordsDao.commit();
			// 回滚完后，同步更新学生账户
			StudnetAccMv studnetAccMv = contractService.getStudentAccoutInfo(student.getId());
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.flush();
			
			
			
//			if (null != courseDate) {
//				this.updateReportForms(courseDate); //更新影响到的报表
//			}
			if (null != courseId) {
//				Course course = courseDao.findById(courseId);
				// 一对一审批无效操作，更新“老师1对1课消 年级分布” 和 “1对1 课消科目分布”
//				courseChargeDao.updateTeacherCourseCharge(course, false);
				this.sendSysMsg(courseId, moneyRollbackRecords.getTransactionId());
			}	
			//一对多回滚信息发送
			if(otmCourseId != null){
				this.sendSysMsgOtmCourse(otmCourseId, moneyRollbackRecords.getTransactionId());
			}
			// 小班回滚信息发送
			if (micCourseId != null){
				List<String> transactionList =  new ArrayList<>();
				transactionList.add(moneyRollbackRecords.getTransactionId());
				this.sendMicCourseSysMsg(micCourseId,transactionList);
			}

		}
		
	}

	@Override
	public void washLiveChargeList(ChargeOrWashCurriculumVo chargeOrWashCurriculumVo) {
		log.info("直播同步冲销参数："+ JSON.toJSONString(chargeOrWashCurriculumVo));
		log.info("直播同步冲销hashCode："+chargeOrWashCurriculumVo.hashCode());
		ObjectHashCode objectHashCode = objectHashCodeDao.findByHashCode(chargeOrWashCurriculumVo.hashCode());
		if (objectHashCode==null){
			ObjectHashCode hashCode = new ObjectHashCode();
			hashCode.setHashCode(chargeOrWashCurriculumVo.hashCode());
			objectHashCodeDao.save(hashCode);
			List<Curriculum> curriculumList = chargeOrWashCurriculumVo.getCurriculumList();
			for (Curriculum curriculum : curriculumList){
				washLiveCharge(curriculum);
			}
		}
	}

	@Override
	public void washLiveCharge(Curriculum curriculum) {
		String courseId = curriculum.getCourseId();
		String liveId = curriculum.getLiveId();
		String contractId = curriculum.getContractId();
		/**
		 * 保存curriculum
		 */
		contractService.saveCurriculum(curriculum);
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByCurriculumId(courseId, liveId, contractId);//实收的扣费的冲销 
//		subtractIncomeDistribution(records, contractId); // 冲销不要减掉业绩
		if (records.size()>0){
			Organization organization = null;
			Student student = null;
			User originalOperateUser = null;
			String transactionTime = null;
			String transactionId = null;
			BigDecimal chargeHourse = BigDecimal.ZERO;
			BigDecimal chargeAmount = BigDecimal.ZERO;
			User operateUser = userService.findUserByAccount(curriculum.getOperateUser());
			for (AccountChargeRecords record : records){
				student = student == null ? record.getStudent(): student;
				organization = organization == null ? record.getBlCampusId(): organization;
				originalOperateUser = originalOperateUser == null ? operateUser : originalOperateUser;
				transactionTime = transactionTime == null ? record.getTransactionTime() : transactionTime;
				transactionId = transactionId == null ? record.getTransactionId() : transactionId;

				this.washAccountChargeRecords(record);//合同产品钱还原

				this.washRecordForLive(record, curriculum);
				chargeHourse = chargeHourse.add(record.getQuality());
				chargeAmount = chargeAmount.add(record.getAmount());
			}
			MoneyWashRecords moneyWashRecords = new MoneyWashRecords();

			moneyWashRecords.setBlCampusId(organization);
			moneyWashRecords.setChargeType(ChargeType.NORMAL);
			moneyWashRecords.setModifyTime(DateTools.getCurrentDateTime());
			moneyWashRecords.setModifyOperator(operateUser);
			moneyWashRecords.setProductType(ProductType.LIVE);
			moneyWashRecords.setWashTime(curriculum.getOperateTime());
			moneyWashRecords.setWashOperator(operateUser);
			moneyWashRecords.setStudent(student);
			moneyWashRecords.setOriginalOperateUser(originalOperateUser);
			moneyWashRecords.setTransactionTime(transactionTime);
			moneyWashRecords.setChargeHourse(chargeHourse);
			moneyWashRecords.setChargeAmount(chargeAmount);
			moneyWashRecords.setTransactionId(transactionId);
			moneyWashRecordsDao.save(moneyWashRecords);

			// 主流程跑完，commit数据库
			moneyWashRecordsDao.commit();

			StudnetAccMv studnetAccMv = contractService.getStudentAccoutInfo(student.getId());
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.flush();

			//学生状态
			PushRedisMessageUtil.pushStudentToMsg(student.getId());

		}else {
			throw new ApplicationException("没找对应的扣费记录");
		}
	}

	/**
	 *
	 * @param records
	 * @param contractId
	 */
	private void subtractIncomeDistribution(List<AccountChargeRecords> records, String contractId) {
		BigDecimal washMoney=BigDecimal.ZERO;
		for (AccountChargeRecords a:records){
			if (a.getPayType()==PayType.REAL){
				washMoney = washMoney.add(a.getAmount());
			}
		}
		washMoney = washMoney.divide(BigDecimal.valueOf(2));//一半
		List<FundsChangeHistory> fundsChangeHistoryList = fundsChangeHistoryDao.getFundsChangeHistoryListByContractId(contractId);
		if (fundsChangeHistoryList.size()==1){
			FundsChangeHistory fundsChangeHistory = fundsChangeHistoryList.get(0);
			List<IncomeDistribution> list = incomeDistributionDao.findByFundsChangeHistoryId(fundsChangeHistory.getId());
			List<IncomeDistributionVo> result = new ArrayList<>();
			if (list.size()>0){
				for (IncomeDistribution i : list){
					BigDecimal amount = i.getAmount();
					amount= amount.subtract(washMoney);
					i.setAmount(amount);
					IncomeDistributionVo incomeDistributionVo = HibernateUtils.voObjectMapping(i, IncomeDistributionVo.class);
					incomeDistributionVo.setId(null);
					result.add(incomeDistributionVo);
				}
				contractService.saveIncomeDistribution(fundsChangeHistory.getId(), result);
				contractService.addToIncomeDistributeStatements(fundsChangeHistory, result);
			}

		}else {
			throw new ApplicationException("直播合同收款记录多于一条或者没有收款记录");
		}
	}


	/**
	 * 冲销扣费
	 */
	@Override
	public void washCharge(MoneyWashRecords moneyWashRecords) {
		String transactionId = moneyWashRecords.getTransactionId();
		ContractProduct cp = new ContractProduct();
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByTransactionId(transactionId);
		for (AccountChargeRecords record : records) {
			if (record.getContractProduct() != null && record.getContractProduct().getStatus().equals(ContractProductStatus.CLOSE_PRODUCT)) {
				throw new ApplicationException("本次扣费涉及的合同产品已经操作过结课退费，不可再操作资金回滚。");
			}
			if (record.getContractProduct() != null && record.getContractProduct().getIsFrozen() == 0) {
				throw new ApplicationException("本次扣费涉及的合同产品在冻结状态，不可再操作资金回滚。");
			}
			if(record.getIsWashed().equals("TRUE")){
				throw new ApplicationException("本次扣费已经回滚过了，不可再操作资金回滚，请刷新列表状态查看回滚信息。");
			}
		}
		
		if (records.size() > 0) {
			Organization organization = null;
			ChargeType chargeType = null;
			ProductType productType = null;
			Student student = null;
			User originalOperateUser = null;
			String transactionTime = null;
			BigDecimal chargeHourse = BigDecimal.ZERO;
			BigDecimal chargeAmount = BigDecimal.ZERO;
			String courseDate = null;
			String courseId = null;
			String otmCourseDate = null;
			String otmCourseId = null;
			String micCourseDate = null;
			String micCourseId = null;
			String lectureDate = null;//讲座日期
			String lectureStuId = null;//讲座学生考勤ID
			int twoTeacherCourseId = 0;//双师课程id
			int twoTeacherClassTwoId =0;//辅班id
			int twoTeacherClassStudentAttendentId = 0;//考勤id
			for (AccountChargeRecords record : records) {
                cp= record.getContractProduct();
				organization = organization == null ? record.getBlCampusId() : organization;
				chargeType = chargeType == null ? record.getChargeType() : chargeType;
				productType = productType == null ? record.getProductType() : productType;
				student = student == null ? record.getStudent(): student;
				originalOperateUser = originalOperateUser == null ? record.getOperateUser() : originalOperateUser;
				transactionTime = transactionTime == null ? record.getTransactionTime() : transactionTime;
				if (ProductType.ONE_ON_ONE_COURSE.equals(record.getProductType()) 
						|| ProductType.SMALL_CLASS.equals(record.getProductType())
						|| ProductType.ONE_ON_MANY.equals(record.getProductType())
						|| ProductType.ECS_CLASS.equals(record.getProductType())
						|| ProductType.LECTURE.equals(record.getProductType())
						||ProductType.TWO_TEACHER.equals(record.getProductType())) {
					chargeHourse = chargeHourse.add(record.getQuality());
				}
//				else if(ProductType.OTHERS.equals(record.getProductType()) && record.getPayType().equals(PayType.PROMOTION)){//其他产品的优惠不需要回滚
//					continue;
//				}
				chargeAmount = chargeAmount.add(record.getAmount());
				Course course = record.getCourse();
				OtmClassCourse otmCourse=record.getOtmClassCourse();
				MiniClassCourse miniClassCourse = record.getMiniClassCourse();
				LectureClassStudent lectureStu=record.getLectureClassStudent();
				TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent = record.getTwoTeacherClassStudentAttendent();
				String currrentTime = DateTools.getCurrentDateTime();
				User currentUser = userService.getCurrentLoginUser();
				if (course != null) {
					courseDate = courseDate == null ? course.getCourseDate() : courseDate;
					courseId = courseId == null ? course.getCourseId() : courseId;
					// 将课程置为审批不通过
					courseService.oneOnOneAuditSubmit(courseId, AuditStatus.UNVALIDATE.getValue());
					ContractProductSubject cpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(record.getContractProduct().getId(), course.getSubject().getId());
					BigDecimal rollbackHours = record.getQuality();
					if (cpSubject != null) {
						if (cpSubject.getConsumeHours().compareTo(rollbackHours) >= 0) {
							cpSubject.setConsumeHours(cpSubject.getConsumeHours().subtract(rollbackHours));
						} else {
							cpSubject.setConsumeHours(BigDecimal.ZERO);
						}
					}
					cpSubject.setModifyTime(currrentTime);
					cpSubject.setModifyUser(currentUser);
					contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
					
					CourseHoursDistributeRecord distributerecord = new CourseHoursDistributeRecord(record.getContractProduct().getId(), cpSubject.getSubject(), 
							DistributeType.WASH, rollbackHours, cpSubject.getQuantity(), cpSubject.getConsumeHours(), 
							cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()), currentUser, currrentTime, cpSubject.getBlCampus());
					courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(distributerecord);
				}
				if(otmCourse != null){
					otmCourseDate = otmCourseDate==null?otmCourse.getCourseDate():otmCourseDate;
					otmCourseId = otmCourseId==null?otmCourse.getOtmClassCourseId():otmCourseId;
				}
				if (miniClassCourse != null){
					micCourseDate = micCourseDate==null?miniClassCourse.getCourseDate():micCourseDate;
					micCourseId = micCourseId==null?miniClassCourse.getMiniClassCourseId():micCourseId;
				}
				if (twoTeacherClassStudentAttendent!=null){
					twoTeacherClassStudentAttendentId = twoTeacherClassStudentAttendent.getId();
					twoTeacherCourseId= twoTeacherClassStudentAttendent.getTwoTeacherClassCourse().getCourseId();
					twoTeacherClassTwoId= twoTeacherClassStudentAttendent.getTwoTeacherClassTwo().getId();
				}
				
				if(lectureStu!=null){
					lectureDate = lectureDate==null?lectureStu.getLectureClass().getStartDate():lectureDate;
					lectureStuId = lectureStuId==null?lectureStu.getId():lectureStuId;
					
					ContractProductSubject cpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(record.getContractProduct().getId(), "DAT0000000127");
					BigDecimal rollbackHours = record.getQuality();
					if (cpSubject != null) {
						if (cpSubject.getConsumeHours().compareTo(rollbackHours) >= 0) {
							cpSubject.setConsumeHours(cpSubject.getConsumeHours().subtract(rollbackHours));
						} else {
							cpSubject.setConsumeHours(BigDecimal.ZERO);
						}
						cpSubject.setModifyTime(currrentTime);
						cpSubject.setModifyUser(currentUser);
						contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
						CourseHoursDistributeRecord distributerecord = new CourseHoursDistributeRecord(record.getContractProduct().getId(), cpSubject.getSubject(), 
								DistributeType.WASH, rollbackHours, cpSubject.getQuantity(), cpSubject.getConsumeHours(), 
								cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()), currentUser, currrentTime, cpSubject.getBlCampus());
						courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(distributerecord);
					}
					
				}

				this.washAccountChargeRecords(record);
				
				this.washRecord(record);
				// 回滚完后，同步更新学生账户
			}
			moneyWashRecords.setBlCampusId(organization);
			moneyWashRecords.setChargeType(chargeType);
			moneyWashRecords.setModifyTime(DateTools.getCurrentDateTime());
			moneyWashRecords.setModifyOperator(userService.getCurrentLoginUser());
			moneyWashRecords.setProductType(productType);
			moneyWashRecords.setWashTime(DateTools.getCurrentDateTime());
			moneyWashRecords.setWashOperator(userService.getCurrentLoginUser());
			moneyWashRecords.setStudent(student);
			moneyWashRecords.setOriginalOperateUser(originalOperateUser);
			moneyWashRecords.setTransactionTime(transactionTime);
			moneyWashRecords.setChargeHourse(chargeHourse);
			moneyWashRecords.setChargeAmount(chargeAmount);
			moneyWashRecordsDao.save(moneyWashRecords);
			
			// 主流程跑完，commit数据库
			moneyWashRecordsDao.commit();

			StudnetAccMv studnetAccMv = contractService.getStudentAccoutInfo(student.getId());
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.flush();
			
			// 一对一冲销信息发送
			if (null != courseId) {
				this.sendSysMsgWashCourse(courseId, transactionId);
			}	
			//一对多冲销信息发送
			if(otmCourseId != null){
				this.sendSysMsgWashOtmCourse(otmCourseId, transactionId);
			}
			// 小班冲销信息发送
			if (micCourseId != null){
				MiniClassCourse mcc= miniClassCourseDao.findById(micCourseId);
				smallClassService.updateMiniClassConsume(micCourseId);
				List<String> transactionList =  new ArrayList<>();
				transactionList.add(transactionId);
				this.sendSysMsgWashMiniCourse(micCourseId,transactionList);
				//更新科目分配扣费课时
                if(cp!=null && cp.getType().equals(ProductType.ECS_CLASS)) {
                    changeConsumeCourseNum(mcc.getMiniClass().getMiniClassId(), student.getId(), chargeHourse.doubleValue());
                }
			}
			if (twoTeacherClassStudentAttendentId > 0 && twoTeacherCourseId>0 && twoTeacherClassTwoId>0){
				this.sendSysMsgWashTwoTeacherCourse(twoTeacherClassStudentAttendentId, transactionId, twoTeacherCourseId, twoTeacherClassTwoId);
			}
			//学生状态
			PushRedisMessageUtil.pushStudentToMsg(student.getId());

		}

		if (cp!=null&& cp.getType()==ProductType.ECS_CLASS && promiseClassService.isYearAfter2018(cp)){
			//处理冲销导致的vip服务费与每个季度扣费数不一致
			washVipAfter2018(cp);
		}
	}

	/**
	 * 处理冲销导致的vip服务费与每个季度扣费数不一致
	 * @param cp
	 */
	private void washVipAfter2018(ContractProduct cp) {
		Contract contract = cp.getContract();
		StringBuffer hql = new StringBuffer();
		hql.append(" from AccountChargeRecords where chargePayType = 'CHARGE' AND  isWashed = 'FALSE' and contract.id = :contractId  ");
		Map<String, Object> params = new HashMap<>();
		params.put("contractId", contract.getId());
		List<AccountChargeRecords> allRecords = accountChargeRecordsDao.findAllByHQL(hql.toString(), params);
		List<AccountChargeRecords> vipRecords =  new ArrayList<>();

		Map<String, List<AccountChargeRecords>> map = new HashMap<>();

		for (AccountChargeRecords chargeRecords:allRecords){
			if (chargeRecords.getProductType()==ProductType.OTHERS){
				vipRecords.add(chargeRecords);
			}else {

				String quarter = chargeRecords.getMiniClass().getProduct().getProductQuarter().getName();

				if (map.containsKey(quarter)){
					List<AccountChargeRecords> oldList = map.get(quarter);
					oldList.add(chargeRecords);
				}else {
					List<AccountChargeRecords> list = new ArrayList<>();
					list.add(chargeRecords);
					map.put(quarter, list);
				}

			}
		}


		Map<String, List<AccountChargeRecords>> groupTransactionIdMap = new HashMap<>();
		for (AccountChargeRecords a :vipRecords){
			String transactionId = a.getTransactionId();
			if (groupTransactionIdMap.containsKey(transactionId)){
				List<AccountChargeRecords> old = groupTransactionIdMap.get(transactionId);
				old.add(a);
			}else {
				List<AccountChargeRecords> list = new ArrayList<>();
				list.add(a);
				groupTransactionIdMap.put(transactionId, list);
			}
		}

		List<String> promotionList = new ArrayList<>();
		List<String> promotionAndRealList = new ArrayList<>();
		List<String> realList = new ArrayList<>();
		for (Map.Entry entry:groupTransactionIdMap.entrySet()){
			List<AccountChargeRecords>  list= (List<AccountChargeRecords>)entry.getValue();
			if (list.size()==1 && list.get(0).getPayType()==PayType.PROMOTION){
				promotionList.add((String)entry.getKey());
			}else if (list.size()>1){
				promotionAndRealList.add((String)entry.getKey());
			}else if (list.size() == 1 && list.get(0).getPayType() == PayType.REAL){
				realList.add((String)entry.getKey());
			}
		}

		promotionList.addAll(promotionAndRealList);
		promotionList.addAll(realList);


		if (map.size()<promotionList.size()){
			int needToWash=promotionList.size()-map.size();
			for (int i=0; i<needToWash; i++){

				String accountChargeRecords = promotionList.get(i);
				MoneyWashRecords moneyWashRecords = new MoneyWashRecords();
				moneyWashRecords.setTransactionId(accountChargeRecords);

				washCharge(moneyWashRecords);
			}
		}else if (map.size()>promotionList.size()){
			throw new ApplicationException("存在漏扣的vip服务费,请联系管理员");
		}


	}

	//更新科目分配扣费课时
	private void changeConsumeCourseNum(String miniClassId,String studentId,Double courseHours){
		List<PromiseClassSubject>  list = promiseClassSubjectDao.findPromiseSubjectByClassIdAndStudentId(miniClassId,studentId);
		if(list.size()>0){
			PromiseClassSubject pcs = list.get(0);
			pcs.setConsumeCourseHours(pcs.getConsumeCourseHours().subtract(BigDecimal.valueOf(courseHours)));
		}else{
		    throw new ApplicationException("该目标班学生已退班，不能进行冲销");
        }
	}
	
	/**
	 * 获取冲销扣费金额
	 */
	@Override
	public Map<String, Object> getWashChargeAmount(String transactionId) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByTransactionId(transactionId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		BigDecimal totalWashAmount = BigDecimal.ZERO;
		BigDecimal realWashAmount = BigDecimal.ZERO;
		BigDecimal promotionWashAmount = BigDecimal.ZERO;
		for (AccountChargeRecords record : records) {
			if (record.getChargePayType() == ChargePayType.WASH) {
				retMap.put("error", "本次扣费已操作过冲销");
				return retMap;
			}
			if (record.getContractProduct() != null && record.getContractProduct().getStatus().equals(ContractProductStatus.CLOSE_PRODUCT)) {
				retMap.put("error", "本次扣费涉及的合同产品已经操作过结课退费，不可再操作资金冲销");
				return retMap;
			}
			if (record.getContractProduct() != null && record.getContractProduct().getIsFrozen() == 0) {
				retMap.put("error", "本次扣费涉及的合同产品在冻结状态，不可再操作资金冲销");
				return retMap;
			}
			totalWashAmount = totalWashAmount.add(record.getAmount());
			if (record.getPayType() == PayType.REAL) {
				realWashAmount = realWashAmount.add(record.getAmount());
			} else if (record.getPayType() == PayType.PROMOTION) {
				promotionWashAmount = promotionWashAmount.add(record.getAmount());
			}
		}
		retMap.put("totalWashAmount", totalWashAmount);
		retMap.put("realWashAmount", realWashAmount);
		retMap.put("promotionWashAmount", promotionWashAmount);
		return retMap;
	}
	
	/**
	 * 备份扣费记录
	 */
	private void backupRecord(AccountChargeRecords record) {
		RollbackBackupRecords rollbackRecords = new RollbackBackupRecords();
		rollbackRecords.setId(record.getId());
		rollbackRecords.setAmount(record.getAmount());
		rollbackRecords.setBlCampusId(record.getBlCampusId());
		rollbackRecords.setChargeType(record.getChargeType());
		rollbackRecords.setContract(record.getContract());
		rollbackRecords.setContractProduct(record.getContractProduct());
		rollbackRecords.setCourse(record.getCourse());
		rollbackRecords.setMiniClass(record.getMiniClass());
		rollbackRecords.setMiniClassCourse(record.getMiniClassCourse());
		rollbackRecords.setOtmClass(record.getOtmClass());
		rollbackRecords.setOtmClassCourse(record.getOtmClassCourse());
		rollbackRecords.setOperateUser(record.getOperateUser());
		rollbackRecords.setPayChannel(record.getPayChannel());
		rollbackRecords.setPayTime(record.getPayTime());
		rollbackRecords.setPayType(record.getPayType());
		rollbackRecords.setProduct(record.getProduct());
		rollbackRecords.setProductType(record.getProductType());
		rollbackRecords.setPromiseClassRecord(record.getPromiseClassRecord());
		rollbackRecords.setQuality(record.getQuality());
		rollbackRecords.setRemark(record.getRemark());
		rollbackRecords.setStudent(record.getStudent());
		rollbackRecords.setTransactionId(record.getTransactionId());
		rollbackRecords.setTransactionTime(record.getTransactionTime());
		rollbackBackupRecordsDao.save(rollbackRecords);
		accountChargeRecordsDao.delete(record);
	}



	private void washRecordForLive(AccountChargeRecords record, Curriculum curriculum) {
		record.setIsWashed("TRUE");
		AccountChargeRecords rollbackRecords = new AccountChargeRecords();
		rollbackRecords.setAmount(record.getAmount());
		rollbackRecords.setBlCampusId(record.getBlCampusId());
		rollbackRecords.setChargeType(record.getChargeType());
		rollbackRecords.setContract(record.getContract());
		rollbackRecords.setChargePayType(ChargePayType.WASH);
		rollbackRecords.setContractProduct(record.getContractProduct());
		rollbackRecords.setCourse(record.getCourse());
		rollbackRecords.setMiniClass(record.getMiniClass());
		rollbackRecords.setMiniClassCourse(record.getMiniClassCourse());
		rollbackRecords.setOtmClass(record.getOtmClass());
		rollbackRecords.setOtmClassCourse(record.getOtmClassCourse());
		rollbackRecords.setOperateUser(userService.findUserByAccount(curriculum.getOperateUser()));
		rollbackRecords.setPayChannel(record.getPayChannel());
		rollbackRecords.setPayTime(curriculum.getOperateTime());//直播同步过来扣费或者冲销的时间
		rollbackRecords.setPayType(record.getPayType());
		rollbackRecords.setProduct(record.getProduct());
		rollbackRecords.setProductType(record.getProductType());
		rollbackRecords.setPromiseClassRecord(record.getPromiseClassRecord());
		rollbackRecords.setQuality(record.getQuality());
		rollbackRecords.setRemark(record.getRemark());
		rollbackRecords.setStudent(record.getStudent());
		rollbackRecords.setLectureClassStudent(record.getLectureClassStudent());
		rollbackRecords.setTransactionId(record.getTransactionId());
		rollbackRecords.setTransactionTime(record.getTransactionTime());
		rollbackRecords.setTeacher(record.getTeacher());
		rollbackRecords.setTwoTeacherClassStudentAttendent(record.getTwoTeacherClassStudentAttendent());//双师
		rollbackRecords.setCurriculum(record.getCurriculum());//直播课程
		accountChargeRecordsDao.save(rollbackRecords);
		accountChargeRecordsDao.merge(record);
		pushChargeMsgToQueueForLive(rollbackRecords);
	}


	/**
	 * 保存冲销扣费
	 * @param record
	 */
	private void washRecord(AccountChargeRecords record) {
		record.setIsWashed("TRUE");
		AccountChargeRecords rollbackRecords = new AccountChargeRecords();
		rollbackRecords.setAmount(record.getAmount());
		rollbackRecords.setBlCampusId(record.getBlCampusId());
		rollbackRecords.setChargeType(record.getChargeType());
		rollbackRecords.setContract(record.getContract());
		rollbackRecords.setChargePayType(ChargePayType.WASH);
		rollbackRecords.setContractProduct(record.getContractProduct());
		rollbackRecords.setCourse(record.getCourse());
		rollbackRecords.setMiniClass(record.getMiniClass());
		rollbackRecords.setMiniClassCourse(record.getMiniClassCourse());
		rollbackRecords.setOtmClass(record.getOtmClass());
		rollbackRecords.setOtmClassCourse(record.getOtmClassCourse());
		rollbackRecords.setOperateUser(userService.getCurrentLoginUser());
		rollbackRecords.setPayChannel(record.getPayChannel());
		rollbackRecords.setPayTime(DateTools.getCurrentDateTime());
		rollbackRecords.setPayType(record.getPayType());
		rollbackRecords.setProduct(record.getProduct());
		rollbackRecords.setProductType(record.getProductType());
		rollbackRecords.setPromiseClassRecord(record.getPromiseClassRecord());
		rollbackRecords.setQuality(record.getQuality());
		rollbackRecords.setRemark(record.getRemark());
		rollbackRecords.setStudent(record.getStudent());
		rollbackRecords.setLectureClassStudent(record.getLectureClassStudent());
		rollbackRecords.setTransactionId(record.getTransactionId());
		rollbackRecords.setTransactionTime(record.getTransactionTime());
		rollbackRecords.setTeacher(record.getTeacher());
		rollbackRecords.setTwoTeacherClassStudentAttendent(record.getTwoTeacherClassStudentAttendent());//双师
		rollbackRecords.setCurriculum(record.getCurriculum());//直播课程
		accountChargeRecordsDao.save(rollbackRecords);
		accountChargeRecordsDao.merge(record);
		pushChargeMsgToQueue(rollbackRecords);
	}

	public void pushChargeMsgToQueueForLive(AccountChargeRecords record){
		IncomeMessage message=new IncomeMessage();
		message.setCampusId(record.getBlCampusId().getId());
		message.setAmount(record.getAmount().divide(BigDecimal.valueOf(2)));
		message.setQuantity(record.getQuality().divide(BigDecimal.valueOf(2)));
		message.setType(record.getProductType());
		message.setChargePayType(record.getChargePayType());
		message.setChargeType(record.getChargeType());
		message.setPayType(record.getPayType());
		message.setCountDate(record.getTransactionTime());

		try {// 加入队列
			JedisUtil.lpush(ObjectUtil.objectToBytes("incomeQueue"),ObjectUtil.objectToBytes(message));
		} catch (Exception e) {
			// 错了
		}

	}
	
	public void pushChargeMsgToQueue(AccountChargeRecords record){
		IncomeMessage message=new IncomeMessage();
		message.setCampusId(record.getBlCampusId().getId());
		message.setAmount(record.getAmount());
		message.setQuantity(record.getQuality());
		message.setType(record.getProductType());
		message.setChargePayType(record.getChargePayType());
		message.setChargeType(record.getChargeType());
		message.setPayType(record.getPayType());
		message.setCountDate(record.getTransactionTime());
		
		try {// 加入队列
			JedisUtil.lpush(ObjectUtil.objectToBytes("incomeQueue"),ObjectUtil.objectToBytes(message));
		} catch (Exception e) {
			// 错了
		}
		
	}
	
	/**
	  * 一对一课程审批回滚
	  */
	@Override
	public void rollbackCourseCharge(MoneyRollbackRecords moneyRollbackRecords, String courseId) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByCourseId(courseId);
		if (records.size() > 0) {
			String transactionId = records.get(0).getTransactionId();
			if (StringUtil.isNotBlank(transactionId)) {
				moneyRollbackRecords.setTransactionId(transactionId);
				this.rollbackCharge(moneyRollbackRecords);
			} else {
				// 旧数据只做课程置为无效操作
				courseService.oneOnOneAuditSubmit(courseId, AuditStatus.UNVALIDATE.getValue());
			}
		}
	}
	
	/**
	 * 小班课程审批回滚
	 */
	@Override
	public Response rollbackMiniClassCourseCharge(MoneyRollbackRecords moneyRollbackRecords, String miniClassCourseId) {
		Response res = new Response();
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByMiniClassCourseId(miniClassCourseId);
		if (records.size() > 0) {
			int successCount = 0;
			int failureCount = 0;
			List<String> transactionIdList = new ArrayList<String>();
			String micCourseDate = null;
			String micCourseId = null;
			for (AccountChargeRecords record : records) {
				MiniClassCourse miniClassCourse = record.getMiniClassCourse();
				if (miniClassCourse != null){
					micCourseDate = micCourseDate==null?miniClassCourse.getCourseDate():micCourseDate;
					micCourseId = micCourseId==null?miniClassCourse.getMiniClassCourseId():micCourseId;
				}
				String transactionId = record.getTransactionId();
				if (StringUtil.isNotBlank(transactionId)) {
					if (!transactionIdList.contains(transactionId)) {
						transactionIdList.add(transactionId);
						moneyRollbackRecords.setTransactionId(transactionId);
						if(miniOrOtmClassRollbackCharge(moneyRollbackRecords)) {
							successCount++;
						} else {
							failureCount++;
						}
					}
				}
			}
			if (transactionIdList.size() == 0) { // 都是旧数据，则执行原审批无效逻辑
				smallClassService.miniClassCourseAudit(miniClassCourseId, AuditStatus.UNVALIDATE.getValue());
			} else {
				smallClassService.updateMiniClassConsume(miniClassCourseId);
			}

			// 小班回滚信息发送
			if (micCourseId != null&&transactionIdList!=null&&transactionIdList.size()>0){
				this.sendMicCourseSysMsg(micCourseId,transactionIdList);
			}

			res.setResultCode(0);
			res.setResultMessage(successCount + "条扣费记录回滚成功，" + failureCount + "条记录回滚失败。");
		}
		return res;
	}
	
	/**
	 * 一对多课程审批回滚
	 */
	@Override
	public Response rollbackOtmClassCourseCharge(MoneyRollbackRecords moneyRollbackRecords, String otmClassCourseId) {
		Response res = new Response();
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByOtmClassCourseId(otmClassCourseId);
		if (records.size() > 0) {
			int successCount = 0;
			int failureCount = 0;
			List<String> transactionIdList = new ArrayList<String>();
			for (AccountChargeRecords record : records) {
				String transactionId = record.getTransactionId();
				if (StringUtil.isNotBlank(transactionId)) {
					if (!transactionIdList.contains(transactionId)) {
						transactionIdList.add(transactionId);
						moneyRollbackRecords.setTransactionId(transactionId);
						if(miniOrOtmClassRollbackCharge(moneyRollbackRecords)) {
							successCount++;
						} else {
							failureCount++;
						}
					}
				}
			}
			if (transactionIdList.size() == 0) { // 都是旧数据，则执行原审批无效逻辑
				otmClassService.otmClassCourseAudit(otmClassCourseId, AuditStatus.UNVALIDATE.getValue());
			}

			//一对多扣费回滚发送短信
			 if(otmClassCourseId != null ){
				 this.sendSysMsgOtmCourse(otmClassCourseId, moneyRollbackRecords.getTransactionId());
			 }
			res.setResultCode(0);
			res.setResultMessage(successCount + "条扣费记录回滚成功，" + failureCount + "条记录回滚失败。");
		}
		return res;
	}
	
	/**
	  * 一对一课程审批冲销
	 */
	@Override
	public void washCourseCharge(MoneyWashRecords moneyWashRecords, String courseId) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByCourseId(courseId);
		if (records.size() > 0) {
			String transactionId = records.get(0).getTransactionId();
			if (StringUtil.isNotBlank(transactionId)) {
				moneyWashRecords.setTransactionId(transactionId);
				this.washCharge(moneyWashRecords);
			} 
		}
	}
	
	/**
	 * 小班课程审批冲销
	 */
	@Override
	public Response washMiniClassCourseCharge(MoneyWashRecords moneyWashRecords, String miniClassCourseId) {
		Response res = new Response();
		String miniClassId = "";
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByMiniClassCourseId(miniClassCourseId);
		if (records.size() > 0) {
			int successCount = 0;
			int failureCount = 0;
			List<String> transactionIdList = new ArrayList<String>();
			String micCourseDate = null;
			String micCourseId = null;
			Map<String,Double> resultMap = new HashMap<>();
			for (AccountChargeRecords record : records) {
			    if(StringUtil.isEmpty(miniClassId)){
			        miniClassId=record.getMiniClass().getMiniClassId();
                }
				MiniClassCourse miniClassCourse = record.getMiniClassCourse();
				if (miniClassCourse != null){
					micCourseDate = micCourseDate==null?miniClassCourse.getCourseDate():micCourseDate;
					micCourseId = micCourseId==null?miniClassCourse.getMiniClassCourseId():micCourseId;
				}
				String transactionId = record.getTransactionId();
				if (StringUtil.isNotBlank(transactionId)) {
					if (!transactionIdList.contains(transactionId)) {
						transactionIdList.add(transactionId);
						moneyWashRecords.setTransactionId(transactionId);
						if(miniOrOtmClassWashCharge(moneyWashRecords)) {
						    if(resultMap.get(record.getStudent().getId())!=null && record.getContractProduct()!=null && ProductType.ECS_CLASS.equals(record.getContractProduct().getType())) {
                                resultMap.put(record.getStudent().getId(),resultMap.get(record.getStudent().getId())+record.getQuality().doubleValue());
                            }else if(record.getContractProduct()!=null && ProductType.ECS_CLASS.equals(record.getContractProduct().getType())){
                                resultMap.put(record.getStudent().getId(), record.getQuality().doubleValue());
                            }
							successCount++;
						} else {
							failureCount++;
						}
					}
				}
			}
			if (transactionIdList.size() == 0) { // 都是旧数据，则执行原审批无效逻辑
				smallClassService.miniClassCourseAudit(miniClassCourseId, AuditStatus.UNVALIDATE.getValue());
			} else {
				smallClassService.updateMiniClassConsume(miniClassCourseId);
			}

			miniClassCourseDao.flush();
			
			// 小班回滚信息发送
			log.info("washMiniClassCourseCharge micCourseId:" + micCourseId + "transactionIdList:" + transactionIdList.toString() + "size:" + transactionIdList.size());
			if (micCourseId != null&&transactionIdList!=null&&transactionIdList.size()>0){
				this.sendSysMsgWashMiniCourse(micCourseId,transactionIdList);
			}

			for(String key :resultMap.keySet()){//回滚科目分配已扣费课时。
                    changeConsumeCourseNum(miniClassId,key,resultMap.get(key));
            }

			res.setResultCode(0);
			res.setResultMessage(successCount + "条扣费记录冲销成功，" + failureCount + "条记录冲销失败。");
		}
		return res;
	}
	
	/**
	 * 一对多课程审批冲销
	 */
	@Override
	public Response washOtmClassCourseCharge(MoneyWashRecords moneyWashRecords, String otmClassCourseId) {
		Response res = new Response();
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByOtmClassCourseId(otmClassCourseId);
		if (records.size() > 0) {
			int successCount = 0;
			int failureCount = 0;
			List<String> transactionIdList = new ArrayList<String>();
			for (AccountChargeRecords record : records) {
				String transactionId = record.getTransactionId();
				if (StringUtil.isNotBlank(transactionId)) {
					if (!transactionIdList.contains(transactionId)) {
						transactionIdList.add(transactionId);
						moneyWashRecords.setTransactionId(transactionId);
						if(miniOrOtmClassWashCharge(moneyWashRecords)) {
							successCount++;
						} else {
							failureCount++;
						}
					}
				}
			}
			if (transactionIdList.size() == 0) { // 都是旧数据，则执行原审批无效逻辑
				otmClassService.otmClassCourseAudit(otmClassCourseId, AuditStatus.UNVALIDATE.getValue());
			}
			
			otmClassCourseDao.flush();

			//一对多扣费回滚发送短信
			if(otmClassCourseId != null ){
				 this.sendSysMsgWashOtmCourse(otmClassCourseId, moneyWashRecords.getTransactionId());
			}
			res.setResultCode(0);
			res.setResultMessage(successCount + "条扣费记录冲销成功，" + failureCount + "条记录冲销失败。");
		}
		return res;
	}
	
	/**
	 *  小班或一对多课程审批回滚的资金回滚处理
	 */
	private boolean miniOrOtmClassWashCharge(MoneyWashRecords moneyWashRecords) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByTransactionId(moneyWashRecords.getTransactionId());
		if (records.size() > 0) {
			Organization organization = null;
			ChargeType chargeType = null;
			ProductType productType = null;
			Student student = null;
			User originalOperateUser = null;
			String transactionTime = null;
			BigDecimal chargeHourse = BigDecimal.ZERO;
			BigDecimal chargeAmount = BigDecimal.ZERO;
			MoneyWashRecords newMoneyWashRecords = new MoneyWashRecords();
			newMoneyWashRecords.setCause(moneyWashRecords.getCause());
			newMoneyWashRecords.setDetailReason(moneyWashRecords.getDetailReason());
			newMoneyWashRecords.setTransactionId(moneyWashRecords.getTransactionId());
			for (AccountChargeRecords record : records) {
				if (record.getContractProduct().getStatus().equals(ContractProductStatus.CLOSE_PRODUCT)) {
					return false;
				} 
				organization = organization == null ? record.getBlCampusId() : organization;
				chargeType = chargeType == null ? record.getChargeType() : chargeType;
				productType = productType == null ? record.getProductType() : productType;
				student = student == null ? record.getStudent(): student;
				originalOperateUser = originalOperateUser == null ? record.getOperateUser() : originalOperateUser;
				transactionTime = transactionTime == null ? record.getTransactionTime() : transactionTime;
				if (ProductType.ONE_ON_ONE_COURSE.equals(record.getProductType()) 
						|| ProductType.SMALL_CLASS.equals(record.getProductType())
						|| ProductType.ONE_ON_MANY.equals(record.getProductType())
						|| ProductType.ECS_CLASS.equals(record.getProductType())) {
					chargeHourse = chargeHourse.add(record.getQuality());
				}
				chargeAmount = chargeAmount.add(record.getAmount());
				this.washAccountChargeRecords(record);
				this.washRecord(record);
//				RollbackBackupRecords rollbackRecords = HibernateUtils.voObjectMapping(record, RollbackBackupRecords.class, "ROLLBACK_BACKUP");
//				rollbackBackupRecordsDao.save(rollbackRecords);
//				accountChargeRecordsDao.delete(record);
			}
			newMoneyWashRecords.setBlCampusId(organization);
			newMoneyWashRecords.setChargeType(chargeType);
			newMoneyWashRecords.setModifyTime(DateTools.getCurrentDateTime());
			newMoneyWashRecords.setModifyOperator(userService.getCurrentLoginUser());
			newMoneyWashRecords.setProductType(productType);
			newMoneyWashRecords.setWashTime(DateTools.getCurrentDateTime());
			newMoneyWashRecords.setWashOperator(userService.getCurrentLoginUser());
			newMoneyWashRecords.setStudent(student);
			newMoneyWashRecords.setOriginalOperateUser(originalOperateUser);
			newMoneyWashRecords.setTransactionTime(transactionTime);
			newMoneyWashRecords.setChargeHourse(chargeHourse);
			newMoneyWashRecords.setChargeAmount(chargeAmount);
			moneyWashRecordsDao.save(newMoneyWashRecords);
			// 主流程跑完，commit数据库
			moneyWashRecordsDao.commit();
			// 回滚完后，同步更新学生账户
			StudnetAccMv studnetAccMv = contractService.getStudentAccoutInfo(student.getId());
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.commit();
		}
//		for (AccountChargeRecords record : records) {
//			deleteMiniClassStudentAttendent(record.getMiniClassCourse().getMiniClassCourseId());
//		}
		return true;
	}
	
	
	/**
	 *  小班或一对多课程审批回滚的资金回滚处理
	 */
	private boolean miniOrOtmClassRollbackCharge(MoneyRollbackRecords moneyRollbackRecords) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByTransactionId(moneyRollbackRecords.getTransactionId());
		if (records.size() > 0) {
			Organization organization = null;
			ChargeType chargeType = null;
			ProductType productType = null;
			Student student = null;
			User originalOperateUser = null;
			String transactionTime = null;
			BigDecimal chargeHourse = BigDecimal.ZERO;
			BigDecimal chargeAmount = BigDecimal.ZERO;
			MoneyRollbackRecords newMoneyRollbackRecords = new MoneyRollbackRecords();
			newMoneyRollbackRecords.setCause(moneyRollbackRecords.getCause());
			newMoneyRollbackRecords.setDetailReason(moneyRollbackRecords.getDetailReason());
			newMoneyRollbackRecords.setTransactionId(moneyRollbackRecords.getTransactionId());
			for (AccountChargeRecords record : records) {
				if (record.getContractProduct().getStatus().equals(ContractProductStatus.CLOSE_PRODUCT)) {
					return false;
				} 
				organization = organization == null ? record.getBlCampusId() : organization;
				chargeType = chargeType == null ? record.getChargeType() : chargeType;
				productType = productType == null ? record.getProductType() : productType;
				student = student == null ? record.getStudent(): student;
				originalOperateUser = originalOperateUser == null ? record.getOperateUser() : originalOperateUser;
				transactionTime = transactionTime == null ? record.getTransactionTime() : transactionTime;
				if (ProductType.ONE_ON_ONE_COURSE.equals(record.getProductType()) 
						|| ProductType.SMALL_CLASS.equals(record.getProductType())
						|| ProductType.ONE_ON_MANY.equals(record.getProductType())
						|| ProductType.ECS_CLASS.equals(record.getProductType())) {
					chargeHourse = chargeHourse.add(record.getQuality());
				}
				chargeAmount = chargeAmount.add(record.getAmount());
				this.rollbackAccountChargeRecords(record);
				this.backupRecord(record);
//				RollbackBackupRecords rollbackRecords = HibernateUtils.voObjectMapping(record, RollbackBackupRecords.class, "ROLLBACK_BACKUP");
//				rollbackBackupRecordsDao.save(rollbackRecords);
//				accountChargeRecordsDao.delete(record);
			}
			newMoneyRollbackRecords.setBlCampusId(organization);
			newMoneyRollbackRecords.setChargeType(chargeType);
			newMoneyRollbackRecords.setModifyTime(DateTools.getCurrentDateTime());
			newMoneyRollbackRecords.setModifyOperator(userService.getCurrentLoginUser());
			newMoneyRollbackRecords.setProductType(productType);
			newMoneyRollbackRecords.setRollbackTime(DateTools.getCurrentDateTime());
			newMoneyRollbackRecords.setRollbackOperator(userService.getCurrentLoginUser());
			newMoneyRollbackRecords.setStudent(student);
			newMoneyRollbackRecords.setOriginalOperateUser(originalOperateUser);
			newMoneyRollbackRecords.setTransactionTime(transactionTime);
			newMoneyRollbackRecords.setChargeHourse(chargeHourse);
			newMoneyRollbackRecords.setChargeAmount(chargeAmount);
			moneyRollbackRecordsDao.save(newMoneyRollbackRecords);
			// 主流程跑完，commit数据库
			moneyRollbackRecordsDao.commit();
			// 回滚完后，同步更新学生账户
			StudnetAccMv studnetAccMv = contractService.getStudentAccoutInfo(student.getId());
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.commit();
		}
//		for (AccountChargeRecords record : records) {
//			deleteMiniClassStudentAttendent(record.getMiniClassCourse().getMiniClassCourseId());
//		}
		return true;
	}
	
	/**
	 * 扣费冲销，还原资金，还原状态，删除记录
	 * @param accountChargeRecords
	 */
	private void washAccountChargeRecords(AccountChargeRecords accountChargeRecords) {
		Contract contract = accountChargeRecords.getContract();
		if (contract == null) { // 电子账户的划归收入
			Student studentInDb = accountChargeRecords.getStudent();
			StudnetAccMv studentAccInDb = studnetAccMvDao.findById(studentInDb.getId());
			studentAccInDb.setElectronicAccount(studentAccInDb.getElectronicAccount().add(accountChargeRecords.getAmount()));
			studnetAccMvDao.save(studentAccInDb);
			electronicAccountChangeLogDao.saveElecAccChangeLog(studentInDb.getId(), ElectronicAccChangeType.WASH_IN, "A", accountChargeRecords.getAmount(), studentAccInDb.getElectronicAccount(), "转移资金到电子账户");			
		} else {
			if (ChargeType.IS_NORMAL_INCOME.equals(accountChargeRecords.getChargeType())) {
				throw new ApplicationException("结课退费的回归收入不能进行冲销操作");
			}
			if (ContractStatus.FINISHED.equals(contract.getContractStatus())) {
				contract.setContractStatus(ContractStatus.NORMAL);
			}
			ContractProduct contractProduct = accountChargeRecords.getContractProduct();
			if (ContractProductStatus.ENDED.equals(contractProduct.getStatus()) 
					|| ContractProductStatus.CLOSE_PRODUCT.equals(contractProduct.getStatus())) {
				contractProduct.setStatus(ContractProductStatus.NORMAL);
			}
			ProductType productType = accountChargeRecords.getProductType();
			BigDecimal rollbackAmount = accountChargeRecords.getAmount();
			BigDecimal rollbackQuality = accountChargeRecords.getQuality();
			contractProduct.setConsumeAmount(contractProduct.getConsumeAmount().subtract(rollbackAmount));
			contractProduct.setConsumeQuanity(contractProduct.getConsumeQuanity().subtract(rollbackQuality));
			if (accountChargeRecords.getPayType() == PayType.REAL) {
				contractProduct.setRealConsumeAmount(contractProduct.getRealConsumeAmount().subtract(rollbackAmount));
			} else if (accountChargeRecords.getPayType() == PayType.PROMOTION) {
				contractProduct.setPromotionConsumeAmount(contractProduct.getPromotionConsumeAmount().subtract(rollbackAmount));
			}
			if (ProductType.ONE_ON_ONE_COURSE.equals(productType)) {
				Course course = accountChargeRecords.getCourse();
				course.setAuditHours(BigDecimal.ZERO);
				course.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
				course.setAuditStatus(AuditStatus.UNVALIDATE);
				List<CourseAttendanceRecord> studyRecords = courseAttendanceRecordDao.getCourseAttendanceRecordListByCourseId(course.getCourseId());
				for (CourseAttendanceRecord studyRecord : studyRecords) {
					courseAttendanceRecordDao.delete(studyRecord);
				}
			} else if (ProductType.SMALL_CLASS.equals(productType) || (ProductType.ECS_CLASS.equals(productType) && accountChargeRecords.getMiniClassCourse()!=null)) {
				MiniClassCourse miniClassCourse = accountChargeRecords.getMiniClassCourse();
				MiniClassStudentAttendent mcsa = miniClassStudentAttendentDao
						.getOneMiniClassStudentAttendent(miniClassCourse.getMiniClassCourseId(), accountChargeRecords.getStudent().getId());
				if (mcsa != null) {
					MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClassCourse.getMiniClass().getMiniClassId(), accountChargeRecords.getStudent().getId());
					if (null != miniClassStudent) {
							mcsa.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);
//							mcsa.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.NEW);
//							mcsa.setHasTeacherAttendance(BaseStatus.FALSE);
							miniClassStudentAttendentDao.save(mcsa);
					} else {
						miniClassStudentAttendentDao.delete(mcsa);
					}
				}
				
				// 如果小班课程的考勤学生都冲销到未扣费，则把小班课程设为老师已考勤。
				int count = miniClassStudentAttendentDao.countMiniClassStudentAttendentByMiniClassCourse(miniClassCourse.getMiniClassCourseId(), 
						"CHARGED");
				if (count == 0) {
					miniClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
					miniClassCourse.setAuditStatus(AuditStatus.UNVALIDATE);
					miniClassCourseDao.save(miniClassCourse);
					log.info(miniClassCourse.getMiniClassCourseId()+",状态："+miniClassCourse.getCourseStatus());
				}
			} else if (ProductType.ONE_ON_MANY.equals(productType)) {
				OtmClassCourse otmClassCourse = accountChargeRecords.getOtmClassCourse();
				OtmClassStudentAttendent ocsa = otmClassStudentAttendentDao
						.getOneOtmClassStudentAttendent(otmClassCourse.getOtmClassCourseId(), accountChargeRecords.getStudent().getId());
				if (ocsa != null) {
					OtmClassStudent otmClassStudent = otmClassStudentDao.getOneOtmClassStudent(otmClassCourse.getOtmClass().getOtmClassId(), accountChargeRecords.getStudent().getId());
					if (null != otmClassStudent) {
						User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(accountChargeRecords.getStudent().getId(), otmClassStudent.getOtmClass().getBlCampus().getId());
						ocsa.setStudyManager(studyManager);
						ocsa.setChargeStatus(OtmClassStudentChargeStatus.UNCHARGE);
//						ocsa.setOtmClassAttendanceStatus(OtmClassAttendanceStatus.NEW);
//						ocsa.setHasTeacherAttendance(BaseStatus.FALSE);
						otmClassStudentAttendentDao.save(ocsa);
					} else {
						otmClassStudentAttendentDao.delete(ocsa);
					}
				}
				
				// 如果小班课程的考勤学生都冲销到未扣费，则把小班课程设为老师已考勤。
				int count = otmClassStudentAttendentDao.countOtmClassStudentAttendentByOtmClassCourse(otmClassCourse.getOtmClassCourseId(), 
						"CHARGED");
				if (count == 0) {
					otmClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
					otmClassCourse.setAuditStatus(AuditStatus.UNVALIDATE);
					otmClassCourseDao.save(otmClassCourse);
				}
			}  else if (ProductType.ECS_CLASS.equals(productType) && accountChargeRecords.getPromiseClassRecord()!=null) {
				PromiseClassRecord pcr = promiseClassRecordDao.findById(accountChargeRecords.getPromiseClassRecord().getId());
				if (null != pcr) {
					if (pcr.getPromiseClass() != null) {
						PromiseStudent promiseStudent = promiseStudentDao.getPromiseClassContractProId(pcr.getPromiseClass().getId(), pcr.getStudentId());
						if (promiseStudent != null && "1".equals(promiseStudent.getResultStatus())) {
							throw new ApplicationException("该目标班已经结课，不允许再做冲销操作！");
						}
					}
					pcr.setStatus("1");
					promiseClassRecordDao.save(pcr);
				}
			} else if (ProductType.OTHERS.equals(productType)) {
//				contractProduct.setPaidStatus(ContractProductPaidStatus.PAID);
//				contractProduct.setPaidAmount(BigDecimal.ZERO);
//				ContractProductHandler handler = contractService.selectHandlerByType(ProductType.OTHERS);
//				handler.rollbackOhterContractProduct(contractProduct, contractProduct.getPaidAmount(), contractProduct.getPromotionAmount(),accountChargeRecords.getPayType());
			}  else if (ProductType.LECTURE.equals(productType)) {
				LectureClassStudent lecStu = accountChargeRecords.getLectureClassStudent();
				lecStu.setChargeStatus(LectureClassStudentChargeStatus.UNCHARGE);
				lecStu.setHasTeacherAttendance(BaseStatus.FALSE);
				lecStu.setChargeTime(null);
				lecStu.setChargeUser(null);
			} else if (ProductType.TWO_TEACHER.equals(productType)){
				TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent = accountChargeRecords.getTwoTeacherClassStudentAttendent();
				if (twoTeacherClassStudentAttendent!=null){
					twoTeacherClassStudentAttendent.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);
					twoTeacherClassStudentAttendentDao.save(twoTeacherClassStudentAttendent);
				}

				// 如果双师课程的考勤学生都冲销到未扣费，则把双师课程设为老师已考勤。
				countTwoTeacherClassStudentAttendent(twoTeacherClassStudentAttendent);

				//如果该双师冲销记录的学生已经退班了，要把考勤记录删掉，并且扣费记录的双师字段置空 redmine Id #1405
				deleteTwoTeacherClassStudentAttendentIfStudentOutofClass(twoTeacherClassStudentAttendent);
			}
			contractProductDao.save(contractProduct);
			contractDao.save(contract);
		}
		
	}

	/**
	 * redmine Id #1405
	 * 如果该双师冲销记录的学生已经退班了，要把考勤记录删掉，并且扣费记录的双师字段置空
	 * 如果双师学生已经退班 删掉 two_teacher_class_student_attendent
	 * @param twoTeacherClassStudentAttendent
	 */
	private void deleteTwoTeacherClassStudentAttendentIfStudentOutofClass(TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent) {
		Student student = twoTeacherClassStudentAttendent.getStudent();
		TwoTeacherClassTwo twoTeacherClassTwo = twoTeacherClassStudentAttendent.getTwoTeacherClassTwo();
		if (student!=null && twoTeacherClassTwo!=null){
			StringBuffer sql = new StringBuffer();
			Map<String, Object> params = new HashMap<>();
			sql.append(" SELECT count(*) FROM two_teacher_class_student WHERE STUDENT_ID=:studentId and CLASS_TWO_ID=:classTwoId ");
			params.put("studentId", student.getId());
			params.put("classTwoId", twoTeacherClassTwo.getId());
			int count = twoTeacherClassStudentDao.findCountSql(sql.toString(), params);
			if (count==0){
				Map<String, Object> accountMap = new HashMap<>();
				accountMap.put("twoTeacherStudentAttendentId", twoTeacherClassStudentAttendent.getId());
				accountMap.put("studentId", student.getId());
				String accountHql = " from AccountChargeRecords where student.id=:studentId and twoTeacherClassStudentAttendent.id=:twoTeacherStudentAttendentId ";
				List<AccountChargeRecords> all = accountChargeRecordsDao.findAllByHQL(accountHql, accountMap);
				if (all.size()>0){
					for (AccountChargeRecords a : all){
						a.setTwoTeacherClassStudentAttendent(null);
						accountChargeRecordsDao.save(a);
					}
				}
				twoTeacherClassStudentAttendentDao.delete(twoTeacherClassStudentAttendent);
			}
		}

	}

	/**
	 *
	 * @param twoTeacherClassStudentAttendent
	 */
	private void countTwoTeacherClassStudentAttendent(TwoTeacherClassStudentAttendent twoTeacherClassStudentAttendent) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from TwoTeacherClassStudentAttendent where twoTeacherClassCourse =:twoTeacherClassCourseId and twoTeacherClassTwo=:twoTeacherClassTwoId  ");
		params.put("twoTeacherClassCourseId", twoTeacherClassStudentAttendent.getTwoTeacherClassCourse());
		params.put("twoTeacherClassTwoId", twoTeacherClassStudentAttendent.getTwoTeacherClassTwo());
		List<TwoTeacherClassStudentAttendent> list = twoTeacherClassStudentAttendentDao.findAllByHQL(hql.toString(), params);
		hql.append(" and chargeStatus=:chargeStatus ");
		params.put("chargeStatus", MiniClassStudentChargeStatus.CHARGED);
		List<TwoTeacherClassStudentAttendent> list1 = twoTeacherClassStudentAttendentDao.findAllByHQL(hql.toString(), params);
		if (list.size()>0 && list1.size() == 0){
			Map<String, Object> updateParams = Maps.newHashMap();
			String updateSql = " UPDATE two_teacher_class_student_attendent SET COURSE_STATUS='TEACHER_ATTENDANCE' WHERE two_class_course_id=:twoTeacherClassCourseId AND class_two_id=:twoTeacherClassTwoId ";
			updateParams.put("twoTeacherClassCourseId", twoTeacherClassStudentAttendent.getTwoTeacherClassCourse().getCourseId());
			updateParams.put("twoTeacherClassTwoId", twoTeacherClassStudentAttendent.getTwoTeacherClassTwo().getId());
			twoTeacherClassStudentAttendentDao.excuteSql(updateSql, updateParams);
		}
	}

	/**
	 * 扣费回滚，还原资金，还原状态，删除记录
	 * @param accountChargeRecords
	 */
	private void rollbackAccountChargeRecords(AccountChargeRecords accountChargeRecords) {
		Contract contract = accountChargeRecords.getContract();
		if (contract == null) { // 电子账户的划归收入
			Student studentInDb = accountChargeRecords.getStudent();
			StudnetAccMv studentAccInDb = studnetAccMvDao.findById(studentInDb.getId());
			studentAccInDb.setElectronicAccount(studentAccInDb.getElectronicAccount().add(accountChargeRecords.getAmount()));
			studnetAccMvDao.save(studentAccInDb);
			electronicAccountChangeLogDao.saveElecAccChangeLog(studentInDb.getId(), ElectronicAccChangeType.WASH_IN, "A", accountChargeRecords.getAmount(), studentAccInDb.getElectronicAccount(), "转移资金到电子账户");			
		} else {
			if (ChargeType.IS_NORMAL_INCOME.equals(accountChargeRecords.getChargeType())) {
				throw new ApplicationException("结课退费的回归收入不能进行回滚操作");
			}
			if (ContractStatus.FINISHED.equals(contract.getContractStatus())) {
				contract.setContractStatus(ContractStatus.NORMAL);
			}
			ContractProduct contractProduct = accountChargeRecords.getContractProduct();
			if (ContractProductStatus.ENDED.equals(contractProduct.getStatus()) 
					|| ContractProductStatus.CLOSE_PRODUCT.equals(contractProduct.getStatus())) {
				contractProduct.setStatus(ContractProductStatus.NORMAL);
			}
			ProductType productType = accountChargeRecords.getProductType();
			BigDecimal rollbackAmount = accountChargeRecords.getAmount();
			BigDecimal rollbackQuality = accountChargeRecords.getQuality();
			contractProduct.setConsumeAmount(contractProduct.getConsumeAmount().subtract(rollbackAmount));
			contractProduct.setConsumeQuanity(contractProduct.getConsumeQuanity().subtract(rollbackQuality));
			//冲销，课程状态回到老师已考勤
			if (ProductType.ONE_ON_ONE_COURSE.equals(productType)) {
				Course course = accountChargeRecords.getCourse();
				course.setAuditHours(BigDecimal.ZERO);
				course.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
				course.setAuditStatus(AuditStatus.UNAUDIT);
				List<CourseAttendanceRecord> studyRecords = courseAttendanceRecordDao.getCourseAttendanceRecordListByCourseId(course.getCourseId());
				if (studyRecords.size() > 2) {
					courseAttendanceRecordDao.delete(studyRecords.get(0));
					courseAttendanceRecordDao.delete(studyRecords.get(1));
					Set<CourseAttendanceRecord> studyRecordSet =  course.getCourseAttendanceRecords();
					studyRecordSet.remove(studyRecords.get(0));
					studyRecordSet.remove(studyRecords.get(1));
				}
//				CourseAttendanceRecord delStudyRecord = null;
//				for (CourseAttendanceRecord studyRecord : studyRecords) {
//					if (delStudyRecord == null) {
//						delStudyRecord = studyRecord;
//					} else {
//						if (DateTools.getDateTime(delStudyRecord.getOprateTime()).before(DateTools.getDateTime(studyRecord.getOprateTime()))) {
//							delStudyRecord = studyRecord;
//						}
//					}
//				}
//				if (delStudyRecord != null) {
//					courseAttendanceRecordDao.delete(delStudyRecord);
//				}
			} else if (ProductType.SMALL_CLASS.equals(productType)) {
				MiniClassCourse miniClassCourse = accountChargeRecords.getMiniClassCourse();
				MiniClassStudentAttendent mcsa = miniClassStudentAttendentDao
						.getOneMiniClassStudentAttendent(miniClassCourse.getMiniClassCourseId(), accountChargeRecords.getStudent().getId());
				if (mcsa != null) {
					MiniClassStudent miniClassStudent = miniClassStudentDao.getOneMiniClassStudent(miniClassCourse.getMiniClass().getMiniClassId(), accountChargeRecords.getStudent().getId());
					if (null != miniClassStudent) {
							mcsa.setChargeStatus(MiniClassStudentChargeStatus.UNCHARGE);
							miniClassStudentAttendentDao.save(mcsa);
					} else {
						miniClassStudentAttendentDao.delete(mcsa);
					}
				}
				
				// 如果小班课程的考勤学生都回滚到未扣费，则把小班课程设为老师已考勤。
				int count = miniClassStudentAttendentDao.countMiniClassStudentAttendentByMiniClassCourse(miniClassCourse.getMiniClassCourseId(), 
						"CHARGED");
				if (count == 0) {
//					miniClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
					miniClassCourse.setCourseStatus(CourseStatus.NEW); //只有未上课和已结算
					miniClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
					miniClassCourseDao.save(miniClassCourse);
				}
			} else if (ProductType.ONE_ON_MANY.equals(productType)) {
				OtmClassCourse otmClassCourse = accountChargeRecords.getOtmClassCourse();
				OtmClassStudentAttendent ocsa = otmClassStudentAttendentDao
						.getOneOtmClassStudentAttendent(otmClassCourse.getOtmClassCourseId(), accountChargeRecords.getStudent().getId());
				if (ocsa != null) {
					OtmClassStudent otmClassStudent = otmClassStudentDao.getOneOtmClassStudent(otmClassCourse.getOtmClass().getOtmClassId(), accountChargeRecords.getStudent().getId());
					if (null != otmClassStudent) {
						User studyManager = studentOrganizationDao.findStudentOrganizationByStudentAndOrganization(accountChargeRecords.getStudent().getId(), otmClassStudent.getOtmClass().getBlCampus().getId());
						ocsa.setStudyManager(studyManager);
						ocsa.setChargeStatus(OtmClassStudentChargeStatus.UNCHARGE);
						otmClassStudentAttendentDao.save(ocsa);
					} else {
						otmClassStudentAttendentDao.delete(ocsa);
					}
				}
				
				// 如果小班课程的考勤学生都回滚到未扣费，则把小班课程设为老师已考勤。
				int count = otmClassStudentAttendentDao.countOtmClassStudentAttendentByOtmClassCourse(otmClassCourse.getOtmClassCourseId(), 
						"CHARGED");
				if (count == 0) {
					otmClassCourse.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);
					otmClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
					otmClassCourseDao.save(otmClassCourse);
				}
			}  else if (ProductType.ECS_CLASS.equals(productType)) {
				PromiseClassRecord pcr = promiseClassRecordDao.findById(accountChargeRecords.getPromiseClassRecord().getId());
				if (null != pcr) {
					if (pcr.getPromiseClass() != null) {
						PromiseStudent promiseStudent = promiseStudentDao.getPromiseClassContractProId(pcr.getPromiseClass().getId(), pcr.getStudentId());
						if (promiseStudent != null && "1".equals(promiseStudent.getResultStatus())) {
							throw new ApplicationException("该目标班已经结课，不允许再做回滚操作！");
						}
					}
					pcr.setStatus("1");
					promiseClassRecordDao.save(pcr);
//					promiseClassRecordDao.delete(pcr);
				}
			} else if (ProductType.OTHERS.equals(productType)) {
				ContractProductHandler handler = contractService.selectHandlerByType(ProductType.OTHERS);
				
				// TODO ASK WENFENG
				handler.rollbackOhterContractProduct(contractProduct, contractProduct.getPaidAmount(), contractProduct.getPromotionAmount(),accountChargeRecords.getPayType());
//				contractProduct.setPaidAmount(contractProduct.getPaidAmount().subtract(rollbackAmount));
//				contractProduct.setPaidStatus(ContractProductPaidStatus.UNPAY);
			}  else if (ProductType.LECTURE.equals(productType)) {
				LectureClassStudent lecStu = accountChargeRecords.getLectureClassStudent();
				lecStu.setChargeStatus(LectureClassStudentChargeStatus.UNCHARGE);
				lecStu.setChargeTime(null);
				lecStu.setChargeUser(null);
			}
			contractProductDao.save(contractProduct);
			contractDao.save(contract);
		}
		
	}
	
	//更新影响到的报表
	private void updateReportForms(String courseDate) {
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		ReportFormThread thead = new ReportFormThread(courseDate);
		taskExecutor.execute(thead);
	}
	
	private void sendSysMsg(String courseId, String transactionId) {
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgThread thread = new SendSysMsgThread(courseId, userId, transactionId);
		taskExecutor.execute(thread);
	}
	
	//一对多资金回滚信息发送
	private void sendSysMsgOtmCourse(String otmCourseId, String transactionId) {
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendOtmSysMsgThread thread = new SendOtmSysMsgThread(otmCourseId, userId, transactionId);
		taskExecutor.execute(thread);
	}

	/**
	 * 小班资金回滚信息发送
	 * @param micCourseId
	 * @param transactionIdList
     */
	private void sendMicCourseSysMsg(String micCourseId, List<String> transactionIdList){
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendMiniClassMsgThread thread = new SendMiniClassMsgThread(micCourseId, userId, transactionIdList);
		taskExecutor.execute(thread);
	}
	
	// 一对一资金冲销信息发送
	private void sendSysMsgWashCourse(String courseId, String transactionId) {
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgWashCourseThread thread = new SendSysMsgWashCourseThread(courseId, userId, transactionId);
		taskExecutor.execute(thread);
	}
	
	//一对多资金冲销信息发送
	private void sendSysMsgWashOtmCourse(String otmCourseId, String transactionId) {
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgWashOtmCourseThread thread = new SendSysMsgWashOtmCourseThread(otmCourseId, userId, transactionId);
		taskExecutor.execute(thread);
	}

	/**
	 * 小班资金冲销信息发送
	 * @param micCourseId
	 * @param transactionIdList
     */
	private void sendSysMsgWashMiniCourse(String micCourseId, List<String> transactionIdList){
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgWashMiniCourseThread thread = new SendSysMsgWashMiniCourseThread(micCourseId, userId, transactionIdList);
		taskExecutor.execute(thread);
	}

	/**
	 * 双师资金冲销信息发送
	 * @param twoTeacherClassStudentAttendentId
	 * @param transactionId
	 */
	private void sendSysMsgWashTwoTeacherCourse(int twoTeacherClassStudentAttendentId, String transactionId, int twoTeacherCourseId, int twoTeacherClassTwoId){
		String userId = userService.getCurrentLoginUser().getUserId();
		ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) ApplicationContextUtil.getContext().getBean("taskExecutor");
		SendSysMsgWashTwoTeacherCourseThread thread = new SendSysMsgWashTwoTeacherCourseThread(userId, twoTeacherClassStudentAttendentId, transactionId, twoTeacherCourseId, twoTeacherClassTwoId);
		taskExecutor.execute(thread);
	}
	
	@Override
	public List findPageMyChargeExcel(DataPackage dataPackage,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo) {
		return accountChargeRecordsDao.getExcelChargeList(dataPackage, accountChargeRecordsVo, timeVo);
	}

	@Override
	public boolean chargeToMiniClassAccount(String miniClassId, String stuId, User user) {
		boolean hasCharge = false;
		
		MiniClass miniClass = smallClassDao.findById(miniClassId);
		Product miniProduct = miniClass.getProduct();
		
//		Product miniProduct = productDao.findById("PRO0000000125");
		
		List<Contract> contracts = contractDao.getOrderContracts(stuId, Order.desc("createTime"));
		// 找到有报读这个产品的contract
		for(Contract contract : contracts) {
			if(hasCharge){
				break;
			} else {
				for(ContractProduct contractProduct : contract.getContractProducts()) {
					if(contractProduct.getProduct().getId().equals(miniProduct.getId())) {
						BigDecimal planAmount =  contractProduct.getTotalAmount();
						BigDecimal paidAmount =  contractProduct.getPaidAmount();
						// 当进行报名扣费的时候， 需要确保这个小班的产品是已经 paid状态 且 paidAmount 相等于 planAmount 的时候， 就可以把它的状态改为 上课
						if(planAmount.compareTo(paidAmount) == 0 && contractProduct.getPaidStatus() == ContractProductPaidStatus.PAID) {
							contractProduct.setStatus(ContractProductStatus.STARTED);
							hasCharge = true;
							break;
//						if(contract.getOneOnOneRemainingAmount().compareTo(BigDecimal.ZERO)< 0 ) {
//							throw new ApplicationException(ErrorCode.MONEY_NOT_ENOUGH);
//						}
						}
					}
				}
			}
		}
		
		return hasCharge;
		
//		ContractProduct contractProduct =  contractProductDao.findById(contractProductId);
//		Contract contract =  contractProduct.getContract();

	}

	@Override
	public void assignMoneyToOtherProdect(String conProId, User currentLoginUser) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 更新 并且检测 合同状态
	 * @param detal
	 * @param contractProduct
	 */
	@Override
	public void updateContractTrigger(BigDecimal detal,
			ContractProduct contractProduct) {
		Contract contract =  contractProduct.getContract();
		// 更新合同消费总金额,剩余资金, 剩余课时
		contract.setConsumeAmount(contract.getConsumeAmount().add(detal));
		contract.setRemainingAmount(contract.getPaidAmount().subtract((contract.getConsumeAmount())));
		
		// 如果有一对一的赠送课时的时候， 要多计算他的赠送课时
		boolean isEndFreeHour = true;
		ContractProduct oneFreePrd = contract.getOneOnOneFreeProduct();  
		if(oneFreePrd !=null && oneFreePrd.getPaidStatus() == ContractProductPaidStatus.PAID  && oneFreePrd.getQuantity().compareTo(oneFreePrd.getConsumeQuanity()) > 0 ) {
			isEndFreeHour = false;
		}
		
		// 检测合同状态 是否完结
		if(contract.getPaidStatus() == ContractPaidStatus.PAID && contract.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 && isEndFreeHour ) {
			contract.setContractStatus(ContractStatus.FINISHED);
		}
	}


	@Override
	public boolean hasChargeRecordOfContractProduct(
			ContractProduct contractProduct) {
		return accountChargeRecordsDao.hasChargeRecord(contractProduct);		
	}


	@Override
	public BigDecimal getChargeAmount(
			ContractProduct conPrd) {
		BigDecimal chargeAmount = accountChargeRecordsDao.getChargeAmount(conPrd);
		return chargeAmount;
	}
	
	@Override
	public BigDecimal getRealChargeAmount(String id,PayType payType) {
		return accountChargeRecordsDao.getRealChargeAmount(id,payType);
	}

	@Override
	public List<AccountChargeRecordsVo> findChargeRecordsForTeacher(
			String productType, String teacherId) {
		
		List<AccountChargeRecords> records =  accountChargeRecordsDao.findChargeRecordsForTeacher(productType, teacherId);
		List<AccountChargeRecordsVo> recordVos = null;
		if(productType.equals(ProductType.ONE_ON_ONE_COURSE.toString())) { 
			recordVos = HibernateUtils.voListMapping(records, AccountChargeRecordsVo.class, "ONE_ON_ONE_COURSE_FOR_MOBILE");
		} else {
			recordVos = HibernateUtils.voListMapping(records, AccountChargeRecordsVo.class, "SMALL_CLASS_FOR_MOBILE");
		}
		return recordVos;
	}
	
	
	@Override
	public List<AccountChargeRecordsVo> findChargeRecordsForTeacher(
			String productType, String teacherId,String searchDate) {
		
		List<AccountChargeRecords> records =  accountChargeRecordsDao.findChargeRecordsForTeacher(productType, teacherId,searchDate);
		List<AccountChargeRecordsVo> recordVos = null;
		if(productType.equals(ProductType.ONE_ON_ONE_COURSE.toString())) { 
			recordVos = HibernateUtils.voListMapping(records, AccountChargeRecordsVo.class, "ONE_ON_ONE_COURSE_FOR_MOBILE");
		} else {
			recordVos = HibernateUtils.voListMapping(records, AccountChargeRecordsVo.class, "SMALL_CLASS_FOR_MOBILE");
		}
		return recordVos;
	}
	
	/**
     * 根据合同产品找扣费记录
     */
    @Override
    public List<AccountChargeRecords> findAccountRecordsByContractProduct(
            String contractProductId, String isWashed) {
        return accountChargeRecordsDao.findAccountRecordsByContractProduct(contractProductId, isWashed);
    }
	
	/**
	 * 根据id查询资金冲销详情记录
	 * @param transactionId
	 * @return
	 */
	public Map<String, Object> findMoneyWashRecordsByTransactionId(String transactionId) {
		List<AccountChargeRecords> records = accountChargeRecordsDao.getAccountChargeRecordsByTransactionId(transactionId);
		Map<String, Object> retMap = new HashMap<String, Object>();
		BigDecimal totalWashAmount = BigDecimal.ZERO;
		BigDecimal realWashAmount = BigDecimal.ZERO;
		BigDecimal promotionWashAmount = BigDecimal.ZERO;
		for (AccountChargeRecords record : records) {
			totalWashAmount = totalWashAmount.add(record.getAmount());
			if (record.getPayType() == PayType.REAL) {
				realWashAmount = realWashAmount.add(record.getAmount());
			} else if (record.getPayType() == PayType.PROMOTION) {
				promotionWashAmount = promotionWashAmount.add(record.getAmount());
			}
		}
		retMap.put("totalWashAmount", totalWashAmount);
		retMap.put("realWashAmount", realWashAmount);
		retMap.put("promotionWashAmount", promotionWashAmount);
		retMap.put("moneyWashRecordsVo", HibernateUtils.voObjectMapping(moneyWashRecordsDao.findByTransactionId(transactionId), MoneyWashRecordsVo.class));
		return retMap;
	}
	
	/**
	 * 修改冲销详情
	 * @param moneyWashRecords
	 */
	public void editMoneyWashRecords(MoneyWashRecords moneyWashRecords) {
		MoneyWashRecords MoneyWashRecordsInDb = moneyWashRecordsDao.findById(moneyWashRecords.getId());
		MoneyWashRecordsInDb.setCause(moneyWashRecords.getCause());
		MoneyWashRecordsInDb.setDetailReason(moneyWashRecords.getDetailReason());
		MoneyWashRecordsInDb.setModifyOperator(userService.getCurrentLoginUser());
		MoneyWashRecordsInDb.setModifyTime(DateTools.getCurrentDateTime());
		moneyWashRecordsDao.flush();
	}
	
	class ReportFormThread implements Runnable {
		public ReportFormThread(){}
		public ReportFormThread(String courseDate) {
			this.courseDate = courseDate;
		}
		private String courseDate;
		@Override
		public void run() {
			try {
				operationCountService.updateOdsDayCourseConsume(courseDate);
				operationCountService.updateOdsDayCourseConsumeTeacherSubject(courseDate);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//删除MINI_CLASS_STUDENT_ATTENDENT中学生已退班的记录
	private void deleteMiniClassStudentAttendent(String miniClassCourseId){
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassCourseId", miniClassCourseId);
		String sql = "delete from MINI_CLASS_STUDENT_ATTENDENT where MINI_CLASS_COURSE_ID= :miniClassCourseId "+
					 "' and STUDENT_ID not in (select STUDENT_ID from MINI_CLASS_STUDENT where MINI_CLASS_ID = " +
					 "(select MINI_CLASS_ID from MINI_CLASS_COURSE where MINI_CLASS_COURSE_ID= :miniClassCourseId ))";
		accountChargeRecordsDao.excuteSql(sql,params);
	}


}
