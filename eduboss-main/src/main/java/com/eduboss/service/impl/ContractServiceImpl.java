package com.eduboss.service.impl;

import static com.eduboss.utils.BonusQueueUtils.addBonusToMessage;
import static com.eduboss.utils.BonusQueueUtils.pushToQueue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import com.eduboss.common.*;
import com.eduboss.service.*;
import com.eduboss.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.ContractBonusDao;
import com.eduboss.dao.ContractDao;
import com.eduboss.dao.ContractProductDao;
import com.eduboss.dao.ContractProductSubjectDao;
import com.eduboss.dao.ContractRecordDao;
import com.eduboss.dao.CourseDao;
import com.eduboss.dao.CurriculumDao;
import com.eduboss.dao.CustomerDao;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.ElectronicAccountChangeLogDao;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.IncomeDistributeStatementsDao;
import com.eduboss.dao.IncomeDistributionDao;
import com.eduboss.dao.MiniClassCourseDao;
import com.eduboss.dao.MiniClassStudentDao;
import com.eduboss.dao.MoneyReadyToPayDao;
import com.eduboss.dao.ObjectHashCodeDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.OtmClassCourseDao;
import com.eduboss.dao.ProductDao;
import com.eduboss.dao.PromiseStudentDao;
import com.eduboss.dao.PromotionDao;
import com.eduboss.dao.StudentAccInfoDao;
import com.eduboss.dao.StudentAccInfoLogDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.StudentOrganizationDao;
import com.eduboss.dao.StudentSchoolTempDao;
import com.eduboss.dao.StudnetAccMvDao;
import com.eduboss.dao.TransactionRecordDao;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.BusinessAssocMapping;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractBonus;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.ContractProductSubject;
import com.eduboss.domain.ContractRecord;
import com.eduboss.domain.Course;
import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.domain.Curriculum;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerDynamicStatus;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.FundsAuditRecord;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.IncomeDistributeStatements;
import com.eduboss.domain.IncomeDistribution;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassRelation;
import com.eduboss.domain.ObjectHashCode;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.Product;
import com.eduboss.domain.PromiseStudent;
import com.eduboss.domain.Promotion;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentAccInfo;
import com.eduboss.domain.StudentAccInfoLog;
import com.eduboss.domain.StudentOrganization;
import com.eduboss.domain.StudentReturnFee;
import com.eduboss.domain.StudentSchool;
import com.eduboss.domain.StudentSchoolTemp;
import com.eduboss.domain.StudnetAccMv;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ChargeOrWashCurriculumVo;
import com.eduboss.domainVo.ContractBonusVo;
import com.eduboss.domainVo.ContractLiveVo;
import com.eduboss.domainVo.ContractMobileVo;
import com.eduboss.domainVo.ContractProductDistributeVo;
import com.eduboss.domainVo.ContractProductMobileVo;
import com.eduboss.domainVo.ContractProductSubjectVo;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.ContractRecordVo;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.ElectronicAccountChangeLogVo;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.domainVo.IncomeDistributeStatementsVo;
import com.eduboss.domainVo.IncomeDistributionVo;
import com.eduboss.domainVo.LiveContractChangeVo;
import com.eduboss.domainVo.LiveContractProductRefundVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.domainVo.RefundIncomeDistributeVo;
import com.eduboss.domainVo.StudentOrganizationVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.TwoTeacherClassTwoVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.jedis.Message;
import com.eduboss.service.handler.ContractProductHandler;
import com.eduboss.service.handler.impl.LectureClassConProdHandler;
import com.eduboss.service.handler.impl.LiveConProdHandler;
import com.eduboss.service.handler.impl.MiniClassConProdHandler;
import com.eduboss.service.handler.impl.OneOnOneConProdHandler;
import com.eduboss.service.handler.impl.OtherConProdHandler;
import com.eduboss.service.handler.impl.OtmClassConProdHandler;
import com.eduboss.service.handler.impl.PromiseClassConProdHandler;
import com.eduboss.service.handler.impl.TwoTeacherClassConProdHandler;
import com.eduboss.utils.BonusQueueUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.PushRedisMessageUtil;
import com.google.common.collect.Maps;

/**
 * 合同service
 * @author ndd
 * 2014-7-31
 */
@Service("com.eduboss.service.ContractService")
public class ContractServiceImpl implements ContractService {

	private final static Logger log=Logger.getLogger(ContractServiceImpl.class);

	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private FundsChangeHistoryDao fundsChangeHistoryDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContractBonusDao contractBonusDao;

	@Autowired
	private IncomeDistributionDao incomeDistributionDao;

	@Autowired
	private IncomeDistributeStatementsDao incomeDistributeStatementsDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private CourseService courseService;
	
	
	@Autowired
	private ContractProductDao contractProductDao;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerEventService customerEventService;
	
	@Autowired
	private MiniClassStudentDao miniClassStudentDao;
	
	@Autowired
	private StudnetAccMvDao studnetAccMvDao;
	

    @Autowired
    private SmallClassService smallClassService;

    @Autowired
    private RoleQLConfigService roleQLConfigService;
    
    @Autowired
    private ContractProductSubjectDao contractProductSubjectDao;

    @Autowired
    private OrganizationDao organizationDao;

	@Autowired
	private StudentAccInfoDao studentAccInfoDao;

	@Autowired
	private StudentAccInfoLogDao studentAccInfoLogDao;

	@Autowired
	private PromotionDao promotionDao;

	@Autowired
	private ObjectHashCodeDao objectHashCodeDao;



	@Autowired
    private ContractProductSubjectService contractProductSubjectService;
    
    private ContractProductHandler oneOnOneConProdHandler;

    private ContractProductHandler miniClassConProdHandler;
    
    private ContractProductHandler promiseClassConProdHandler;
    
    private ContractProductHandler otherConProdHandler;
    
    private ContractProductHandler otmClassProdHandler;
    
    private LectureClassConProdHandler lectureClassProdHandler;

	private ContractProductHandler twoTeacherClassProdHandler;

	private ContractProductHandler liveProdHandler;
    
    @Autowired
    private ElectronicAccountChangeLogDao electronicAccountChangeLogDao;
    
	
    @Autowired
    private MobilePushMsgService mobilePushMsgService;
    
    @Autowired
    private StudentDynamicStatusService studentDynamicStatusService; 

    @Autowired
    private PromiseStudentDao promiseStudentDao; 
    
    @Autowired
    private MoneyReadyToPayDao moneyReadyToPayDao;
    
    @Autowired
    private ContractRecordDao contractRecordDao;
    
    @Autowired
    private TransactionRecordDao transactionRecordDao;
    
    @Autowired
    private OtmClassCourseDao otmClassCourseDao;
    
    @Autowired
    private MiniClassCourseDao miniClassCourseDao;
    
    @Autowired
    private CourseDao courseDao;

	@Autowired
	private ContractBonusService contractBonusService;

    
    @Autowired
    private AccountChargeRecordsDao accountChargeRecordsDao;

	@Autowired
	private InitDataDeleteService initDataDeleteService;
	
	@Autowired
	private FundsAuditRecordService fundsAuditRecordService;

    @Autowired
	private StudentSchoolTempDao studentSchoolTempDao;
    
    @Autowired
	private CourseHoursDistributeRecordService courseHoursDistributeRecordService;
	
	@Autowired
    private UserEventRecordService userEventRecordService;

	@Autowired
	private TwoTeacherClassService twoTeacherClassService;
	
//	private static final Logger log = LoggerFactory.getLogger(ContractServiceImpl.class);
	
	@Autowired
	private StudentOrganizationDao studentOrganizationDao;

	@Autowired
	private MiniClassInventoryService miniClassInventoryService;

	@Autowired
	private BusinessAssocMappingService businessAssocMappingService;


	@Autowired
	private CurriculumDao curriculumDao;

	@Autowired
	private PromiseClassService promiseClassService;

	@Override
	public List<ContractProduct> findContractProductByProductAndStudent(String studentId, String productId) {
		return contractProductDao.findContractProductByProductAndStudent(studentId,productId);
	}


	/**
	 * 保存扣费和冲销时候的
	 * @param curriculum
	 */
	@Override
	public void saveCurriculum(Curriculum curriculum) {
		if (StringUtils.isNotBlank(curriculum.getStartTimeStamp())){
			String startTime = DateTools.TimeStamp2Date(curriculum.getStartTimeStamp(), "yyyy-MM-dd HH:mm:ss");
			curriculum.setStartDateTime(startTime);
		}
		if (StringUtils.isNotBlank(curriculum.getEndTimeStamp())){
			String endTime = DateTools.TimeStamp2Date(curriculum.getEndTimeStamp(), "yyyy-MM-dd HH:mm:ss");
			curriculum.setEndDateTime(endTime);
		}
//		if (StringUtils.isNotBlank(curriculum.getStartDateTime())&&StringUtils.isNotBlank(curriculum.getEndDateTime())){
//			int hours = DateTools.hourOfTwo(curriculum.getStartDateTime(), curriculum.getEndDateTime());
//			curriculum.setCourseHours(hours);
//		}
		curriculumDao.save(curriculum);
	}

	@Override
	public void saveAccountChargeRecord(BigDecimal chargeMoney, ContractProduct targetConPrd, ProductType productType, ChargeType chargeType, PayType payType, BigDecimal auditedCourseHours, Curriculum curriculum, String transactionId) {
		/**
		 * 保存curriculum
		 */
		this.saveCurriculum(curriculum);
		AccountChargeRecords record =  new AccountChargeRecords();
		record.setProduct(targetConPrd.getProduct());
		record.setAmount(chargeMoney);
		record.setContract(targetConPrd.getContract());
		record.setStudent(targetConPrd.getContract().getStudent());
		record.setOperateUser(userService.findUserByAccount(curriculum.getOperateUser()));
		record.setProductType(productType);
		record.setPayTime(curriculum.getOperateTime());//直播同步过来扣费或者冲销的时间
		record.setChargePayType(ChargePayType.CHARGE);

		record.setContractProduct(targetConPrd);
		auditedCourseHours = BigDecimal.valueOf(curriculum.getCourseHours());
		record.setQuality(auditedCourseHours);
		record.setCourseMinutes(BigDecimal.valueOf(60));
		record.setChargeType(chargeType);
		record.setPayType(payType);
		record.setTransactionId(transactionId);
		record.setBlCampusId(organizationDao.findById(targetConPrd.getContract().getBlCampusId()));

		if (ChargeType.NORMAL.equals(chargeType)){
			record.setCurriculum(curriculum);
		}

		accountChargeRecordsDao.save(record);
		accountChargeRecordsDao.flush();
		LiveConProdHandler liveConProdHandler = (LiveConProdHandler)selectHandlerByType(ProductType.LIVE);
		if(targetConPrd.getType().equals(ProductType.LIVE) && chargeMoney!=null &&  auditedCourseHours!=null) {
			liveConProdHandler.pushChargeMsgToQueue(targetConPrd.getContract().getBlCampusId(), record.getPayTime(), chargeMoney.divide(BigDecimal.valueOf(2)), auditedCourseHours.divide(BigDecimal.valueOf(2)), productType, chargeType, payType, ChargePayType.CHARGE);
		}
	}




	@Override
	public void chargeLiveOfCurriculum(ChargeOrWashCurriculumVo chargeOrWashCurriculumVo) {
		log.info("直播同步扣费参数："+JSON.toJSONString(chargeOrWashCurriculumVo));
		log.info("直播同步扣费hashCode："+chargeOrWashCurriculumVo.hashCode());
		ObjectHashCode objectHashCode = objectHashCodeDao.findByHashCode(chargeOrWashCurriculumVo.hashCode());
		if (objectHashCode==null){
			ObjectHashCode hashCode = new ObjectHashCode();
			hashCode.setHashCode(chargeOrWashCurriculumVo.hashCode());
			objectHashCodeDao.save(hashCode);
			List<Curriculum> curriculumList= chargeOrWashCurriculumVo.getCurriculumList();
			LiveConProdHandler liveConProdHandler = (LiveConProdHandler)selectHandlerByType(ProductType.LIVE);
			for (Curriculum curriculum : curriculumList){
				liveConProdHandler.chargeLiveCurriculum(curriculum);
			}
		}
	}

	public void saveContract(Contract contract) {
        User user = userService.getCurrentLoginUser();
		contractDao.save(contract);
		//保存后，修改对应客户的状态
		Customer customer = customerDao.findById(contract.getCustomer().getId());
		String before_deliverTarget = customer.getBeforeDeliverTarget();
		customer.setDealStatus(CustomerDealStatus.SIGNEUP);
        customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
        customer.setDeliverTarget(user!=null?user.getUserId():null);
        customer.setDeliverTargetName(user!=null?user.getName():null);
		customerDao.save(customer);
				
		//保存客户事件及发浏览器消息
		
		CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		dynamicStatus.setDynamicStatusType(CustomerEventType.CONTRACT_SIGN);
		dynamicStatus.setDescription("客户" + customer.getName() + "已签合同！");
		if(customer.getResEntrance()!=null){
		    dynamicStatus.setResEntrance(customer.getResEntrance());
		}
		dynamicStatus.setStatusNum(1);
		dynamicStatus.setTableName("contract");
		dynamicStatus.setTableId(contract.getId());
		dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.yes);
		customerEventService.addCustomerDynameicStatus(customer, dynamicStatus, user);
		
		//customerEventService.saveCustomerDynamicStatus(customer.getId(), CustomerEventType.CONTRACT_SIGN, "客户" + customer.getName() + "已签合同！", "");
		//咨询师签单客户触发上工序事件
		if(before_deliverTarget!=null){
			User referUser = userService.loadUserById(before_deliverTarget);						
			CustomerDynamicStatus dynamicStatus2 = new CustomerDynamicStatus();
			dynamicStatus2.setDynamicStatusType(CustomerEventType.CONTRACT_SIGN);
			dynamicStatus2.setDescription("客户" + customer.getName() + "已签合同！");
			if(customer.getResEntrance()!=null){
			    dynamicStatus2.setResEntrance(customer.getResEntrance());
			}
			dynamicStatus2.setStatusNum(1);
			dynamicStatus2.setTableName("contract");
			dynamicStatus2.setTableId(contract.getId());
			dynamicStatus2.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
			customerEventService.addCustomerDynameicStatus(customer, dynamicStatus2, referUser);
					  
		}
	    
		
	}
	
	/**
	 * 根据学生id查询 一对一剩余课时>0 的合同
	 * @param studentId
	 * @return
	 */
	public List<ContractVo> getContractByStudentId(String studentId) {
		List<Contract> contracts = contractDao.getNewContractByStudentId(studentId);
		List<ContractVo> contractVos = new ArrayList<ContractVo>();
		for(Contract contract : contracts){
			calculateContractDomain(contract);
			ContractVo vo= HibernateUtils.voObjectMapping(contract, ContractVo.class);
//			// 剩余课时
//			vo.setClassBalance(contract.getClassBalance() == null ? "0" : StringUtil.subZeroAndDot(contract.getClassBalance().toString()));
//			// 可排课时
//			vo.setClassArrangable(contract.getClassArrangable() == null ? "0" : StringUtil.subZeroAndDot(contract.getClassArrangable().toString()));
			String gradeName = "";
			if (contract.getStudent().getGradeDict() != null)
				gradeName = contract.getGradeDict().getName();
			vo.setGradeName(gradeName);
			Set<ContractProduct> contractProducts = contract.getContractProducts();
//			double classNum = 0;
//			String productList = "";
//			for (ContractProduct cp : contractProducts) {
//				Product product = cp.getProduct();
//				if (product != null && product.getCategory().equals(ProductType.ONE_ON_ONE_COURSE)) {//只显示一对一
//					classNum += new Double(cp.getQuantity().toString());
//					productList += "、" + product.getName() + ":" + cp.getQuantity() + "节  ";
//				}
//			}
//			if (productList.length() > 0) {
//				productList = productList.substring(1);
//			}
//			vo.setClassNum(String.valueOf(classNum));
//			vo.setProductText(productList);
			contractVos.add(vo);
		}
		return contractVos;
	}

	@Override
	public ContractLiveVo liveContract(ContractLiveVo contractVo) {
		ContractLiveVo vo = HibernateUtils.voObjectMapping(contractVo, ContractLiveVo.class);
		String stuId = contractVo.getStuId();
		Student student = studentDao.findById(stuId);
		ContractProductVo contractProductVo = vo.getContractProductVo();
		if (contractProductVo!=null){
			Set<ContractProductVo> contractProductVos = new HashSet<ContractProductVo>();
			contractProductVos.add(contractProductVo);
			vo.setContractProductVos(contractProductVos);
		}
		vo.setGradeId(student.getGradeDict().getId());
		vo.setStuName(student.getName());
		vo.setStuType("ENROLLED");
		Set<Customer> customers = student.getCustomers();
		for (Customer c : customers){
			vo.setCusId(c.getId());
			vo.setCusPhone(c.getContact());
			vo.setCusName(c.getName());
		}
		String firstContractId = contractVo.getFirstContractId();
		Contract contract = contractDao.findById(firstContractId);
		User signer = contract.getSignStaff();//签单人
		vo.setBlCampusId(contract.getBlCampusId());//签单校区


		vo.setSignByWho(signer.getUserId());
		vo.setContractType(ContractType.LIVE_CONTRACT);
		vo.setSchoolOrTemp("school");
		return vo;
	}

	@Override
	public FundsChangeHistory saveFundOfContract(FundsChangeHistoryVo fundsVo) {

		if (fundsVo.getChannel()!=null && !fundsVo.getChannel().equals(PayWay.POS) && fundsVo.getAuditStatusValue()==null && checkCanSave(fundsVo)){
			throw new ApplicationException("合同有待分配资金，请分配完资金再进行新的收款！");
		}

		if (com.eduboss.utils.StringUtil.isNotBlank(fundsVo.getTransactionUuid())) {
			transactionRecordDao.saveTransactionRecord(fundsVo.getTransactionUuid());
		}
		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
		User auditUser=userService.getCurrentLoginUser();
		if(StringUtils.isNotBlank(fundsVo.getId()) && fundsVo.getId()!=null){
			fundsChangeHistory=fundsChangeHistoryDao.findById(fundsVo.getId());
			fundsChangeHistory.setAuditStatus(FundsChangeAuditStatus.valueOf(fundsVo.getAuditStatusValue()));
			fundsChangeHistory.setAuditTime(DateTools.getCurrentDateTime());
			fundsChangeHistory.setAuditUser(auditUser);
			fundsChangeHistory.setArtificialAuditRemark(fundsVo.getArtificialAuditRemark());
			fundsChangeHistory.setAuditType(FundsChangeAuditType.ARTIFICIAL);
			if (StringUtils.isNotBlank(fundsVo.getBlCampusId())) {
				fundsChangeHistory.setFundBlCampusId(fundsVo.getBlCampusId());
			}
			FundsAuditRecord record = new FundsAuditRecord();
			record.setFundsChangeHistoryId(fundsChangeHistory.getId());
			record.setAuditUser(userService.getCurrentLoginUser());
			record.setAuditTime(DateTools.getCurrentDateTime());
			record.setRemark(fundsVo.getArtificialAuditRemark());
			record.setCreateTime(DateTools.getCurrentDateTime());
			record.setAuditStatus(FundsChangeAuditStatus.valueOf(fundsVo.getAuditStatusValue()));
			fundsAuditRecordService.saveFundsAuditRecord(record);
			return new FundsChangeHistory();
		}else{
			User chargeUser=new User();
			Contract contract = contractDao.findById(fundsVo.getContractId());
			
//			CalculateUtil.calPaidFundContract(contract, fundsVo.getTransactionAmount());
			Double money  = fundsVo.getTransactionAmount();

			if(fundsVo.getFundsChangeType()==null) {
				fundsChangeHistory.setFundsChangeType(FundsChangeType.HUMAN);//人工录入
			}else{
				fundsChangeHistory.setFundsChangeType(fundsVo.getFundsChangeType());
			}
			
			if(StringUtils.isNotBlank(fundsVo.getAuditStatusValue())){
				fundsChangeHistory.setAuditStatus(FundsChangeAuditStatus.valueOf(fundsVo.getAuditStatusValue()));
			}else{
				fundsChangeHistory.setAuditStatus(FundsChangeAuditStatus.UNAUDIT);
			}
			
			if(fundsVo.getAuditType()!=null){//如果新增的收款记录就有审核状态，那么就是实时审核。标记时间
				fundsChangeHistory.setAuditType(fundsVo.getAuditType());
				fundsChangeHistory.setAuditTime(DateTools.getCurrentDateTime());
			}
			
			if(StringUtils.isNotBlank(fundsVo.getSystemAuditRemark())){//系统审批备注
				fundsChangeHistory.setSystemAuditRemark(fundsVo.getSystemAuditRemark());
			}
			
			if (com.eduboss.utils.StringUtil.isNotBlank(fundsVo.getPosMochineTypeId())) {
			    if (contract.getPosMachineType() != null 
			            && com.eduboss.utils.StringUtil.isNotBlank(contract.getPosMachineType().getId())) {
		            if (!contract.getPosMachineType().getId().equals(fundsVo.getPosMochineTypeId())) {
		                throw new ApplicationException("再次收款必须用相同pos机类型");
		            }
			    } else {
			        contract.setPosMachineType(new DataDict(fundsVo.getPosMochineTypeId()));
			    }
                fundsChangeHistory.setPosMachineType(new DataDict(fundsVo.getPosMochineTypeId()));
            }
			
			fundsChangeHistory.setRemark(fundsVo.getRemark());
			fundsChangeHistory.setTransactionAmount(BigDecimal.valueOf(money));
			if (StringUtils.isNotBlank(fundsVo.getLiveReceiptTime())){
				fundsChangeHistory.setTransactionTime(fundsVo.getLiveReceiptTime());
				fundsChangeHistory.setReceiptTime(fundsVo.getLiveReceiptTime());
			}else {
				fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
				// 增加收款时间
				fundsChangeHistory.setReceiptTime(DateTools.getCurrentDateTime());
			}


			fundsChangeHistory.setContract(contract);
			fundsChangeHistory.setChannel(fundsVo.getChannel());
			fundsChangeHistory.setStudent(contract.getStudent());
			String posId = fundsVo.getPOSid();
			if(StringUtils.isNotBlank(posId)){
				posId = posId.replaceFirst("^0*", "");
			}
			if (fundsVo.getChannel() == PayWay.POS) {
				if (StringUtils.isNotBlank(posId) && StringUtils.isNotBlank(fundsVo.getPosNumber()) 
						&& !fundsChangeHistoryDao.isPosIAndPosNumberdUnique(null, posId, fundsVo.getPosNumber())) {
					throw new ApplicationException("该支付单号和终端号已存在，请检查支付单号和终端号是否正确");
				}
			}
			fundsChangeHistory.setPOSid(posId);
			if (StringUtils.isNotBlank(fundsVo.getPosNumber())) {
				fundsChangeHistory.setPosNumber(fundsVo.getPosNumber());
			}
			fundsChangeHistory.setPosReceiptDate(fundsVo.getPosReceiptDate());
			fundsChangeHistory.setFundBlCampusId(contract.getBlCampusId());//合同校区存入收款校区里面，以便现金流数据统计 2016-05-04
			if(StringUtils.isNotEmpty(fundsVo.getCollectCardNo()))
			fundsChangeHistory.setCollectCardNo(fundsVo.getCollectCardNo());
			if(StringUtils.isNotEmpty(fundsVo.getCollector()))
			fundsChangeHistory.setCollector(fundsVo.getCollector());
			// 新增字段区别收款冲销
			fundsChangeHistory.setFundsPayType(FundsPayType.RECEIPT);
			fundsChangeHistory.setTransactionId(UUID.randomUUID().toString());
			if(StringUtils.isNotBlank(fundsVo.getChargeByWho())){
				chargeUser = userService.findUserById(fundsVo.getChargeByWho());
				fundsChangeHistory.setChargeBy(new User(fundsVo.getChargeByWho()));
			}else{
				chargeUser=userService.getCurrentLoginUser();
				fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
			}
			String chargeByCampusId = chargeUser.getOrganizationId();
			fundsChangeHistory.setChargeByCampusId(chargeByCampusId);
			Double historySum = fundsChangeHistoryDao.historySumFundsChange(fundsVo.getContractId());
			fundsChangeHistory.setPaidAmount(BigDecimal.valueOf(historySum+money));
			
			if (!ContractType.INIT_CONTRACT.equals(contract.getContractType())) {
				// 这里有对 收费表有一个trigger， 对合同表的 已经交款的字段 进行更新操作， 需要对合同表 refresh 一下，再进行修改
				// 已经去除了trigger
				fundsChangeHistoryDao.save(fundsChangeHistory);
				fundsChangeHistoryDao.flush();
				FundsAuditRecord record = new FundsAuditRecord();
				record.setFundsChangeHistoryId(fundsChangeHistory.getId());
				record.setReceiptUser(chargeUser);
				record.setSubmitTime(DateTools.getCurrentDateTime());
				record.setRemark("确认收款操作");
				fundsAuditRecordService.saveFundsAuditRecord(record);
				contractDao.getHibernateTemplate().refresh(contract);
			}
			   
			// 查询或创建Student view 物理视图
			Student student =  fundsChangeHistory.getContract().getStudent();
			
			StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
			if(studnetAccMv == null) {
				studnetAccMv =  new StudnetAccMv(student);
				studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
				studnetAccMvDao.flush();
			} 
			
			// 电子账户付款
		 if(fundsVo.getChannel() == PayWay.ELECTRONIC_ACCOUNT){
		     BigDecimal electornicAccount = studnetAccMv.getElectronicAccount() != null ? studnetAccMv.getElectronicAccount() : BigDecimal.ZERO; // 获取学生电子账户余额
		     BigDecimal frozenAmount = studnetAccMv.getFrozenAmount() != null ? studnetAccMv.getFrozenAmount() : BigDecimal.ZERO;
		     BigDecimal maxCanUsedAmount = electornicAccount.subtract(frozenAmount);
		     if(maxCanUsedAmount.doubleValue() < money){
		         throw new ApplicationException("学生[" + contract.getStudent().getName() + "]电子账户最大可用金额[" + maxCanUsedAmount + "]元，不足以支付，支付金额[" + money + "]元");
		     }else{
		         // TODO 电子账户消费money元
		     	studnetAccMv.setElectronicAccount(electornicAccount.subtract(BigDecimal.valueOf(money)));
		     	electronicAccountChangeLogDao.saveElecAccChangeLog(studnetAccMv.getStudentId(), ElectronicAccChangeType.PAY_OUT, "D", BigDecimal.valueOf(money), studnetAccMv.getElectronicAccount(), "从电子账户收款");
		     }
		 }
			
			// 更改合同的 待付款，已交款，可分配资金
			contract.setPaidAmount(contract.getPaidAmount().add(fundsChangeHistory.getTransactionAmount()));
		//	contract.setPendingAmount(contract.getTotalAmount().subtract(contract.getPaidAmount()));
		//	if(contract.getAvailableAmount() == null)
		//		contract.setAvailableAmount(BigDecimal.ZERO);
		//	contract.setAvailableAmount(contract.getAvailableAmount().add(fundsChangeHistory.getTransactionAmount()));
		 
		 this.calculateContractDomain(contract);
			
			// 检查 合同缴费状态
			int flag = contract.getPendingAmount().compareTo(BigDecimal.ZERO);
			if(flag == 0) {
				contract.setPaidStatus(ContractPaidStatus.PAID);
			} else if (flag >0) {
				contract.setPaidStatus(ContractPaidStatus.PAYING);
			} else {
				throw new ApplicationException(ErrorCode.MONEY_ERROR);
			}
			
			// 如果交齐全款，就可以对合同的产品进行分配资金, 并且对其他产品进行扣费
			if(contract.getPendingAmount().compareTo(BigDecimal.ZERO) == 0  && !(fundsVo.getAuditStatusValue()!=null && fundsVo.getAuditStatusValue().equals(FundsChangeAuditStatus.UNVALIDATE.getValue()) && (fundsVo.getChannel().equals(PayWay.ALI_PAY) || fundsVo.getChannel().equals(PayWay.WEB_CHART_PAY)))) {

				List<BigDecimal> arrange=new ArrayList<BigDecimal>();
				List<String> conPrdId=new ArrayList<String>();
				
				for(ContractProduct conPrd : contract.getContractProducts()) {
					//不是初始化合同，合同产品正常  ,未支付完成的,就自动分配，并且记录绩效 (并且不是直播同步合同)
					if((conPrd.getStatus().equals(ContractProductStatus.NORMAL) || conPrd.getStatus().equals(ContractProductStatus.STARTED)) 
					    && !ContractType.INIT_CONTRACT.equals(contract.getContractType())
					    && !ContractProductPaidStatus.PAID.equals(conPrd.getPaidStatus())) {//&& !ContractType.LIVE_CONTRACT.equals(contract.getContractType())
						conPrdId.add(conPrd.getId());
						arrange.add(conPrd.getRealAmount().subtract(conPrd.getPaidAmount()));
					}
				}
				//电子账户收款不需要分配业绩 直播的同步合同也不需要
				if(arrange.size()>0){
					BigDecimal [] arrangeMoneys=arrange.toArray(new BigDecimal[0]);
					String [] conPrdIds=conPrdId.toArray(new String[0]);
					saveContractProductMoney(arrangeMoneys, conPrdIds, fundsChangeHistory.getId(), contract);
				}
			}
			
		 contractDao.flush();
			//如果是合同的第一次收款，自动提交排课需求
			addCourseRequirementIfFirstFundForContract(contract);
			
			//保存客户事件及发浏览器消息
			if (contract.getCustomer()!=null){
				customerEventService.saveCustomerDynamicStatus(contract.getCustomer().getId(), CustomerEventType.CHARGER, "客户" + contract.getCustomer().getName() + "交款"+money.toString()+"元！", "");
			}

			// 合同收款后，同步更新学生账户
			studnetAccMv = getStudentAccoutInfo(student.getId());
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.flush();
			
			//收款保存记录
			ContractRecord cr=new ContractRecord(contract);
			cr.setUpdateType(UpdateType.CHARGE);
			
			if(userService.getCurrentLoginUser()!=null){//登录人为空的时候，默认为付款操作人（支付宝，微信回调）
				cr.setCreateByStaff(userService.getCurrentLoginUser());
			}else{
				cr.setCreateByStaff(fundsChangeHistory.getChargeBy());
			}
			contractRecordDao.save(cr);
			//标记是否公帐合同
			remarkContractFlag(fundsChangeHistory,studnetAccMv);

			updateFundsChangeHistoryNotAssigned(fundsChangeHistory.getId());


			if(contract!=null && contract.getContractType()!=null && !contract.getContractType().equals(ContractType.LIVE_CONTRACT)) {
				//处理现金流
				PushRedisMessageUtil.putMsgToQueueForFinance(fundsChangeHistory, contract, "1");
			}
			//收款处理学生状态
			PushRedisMessageUtil.pushStudentToMsg(fundsChangeHistory.getStudent().getId());
			return fundsChangeHistory;
		}
		
		
		
	}

	public void remarkContractFlag(FundsChangeHistory fun,StudnetAccMv acc){
		Contract con= fun.getContract();
		StudentAccInfo info = studentAccInfoDao.findInfoByStudentId(fun.getStudent().getId());
		if(fun.getChannel() == PayWay.ELECTRONIC_ACCOUNT){
			if(info!=null && info.getAccountAmount().compareTo(BigDecimal.ZERO)>0 && acc.getElectronicAccount().compareTo(info.getAccountAmount())<0){
				//本次收款包含旧电子账户金额跟平行账户混合，
				if(con.getPubPayContract()==0 && fun.getPaidAmount().compareTo(fun.getTransactionAmount())>0) {//如果是非公帐合同，且不是第一次收款
					saveAllStudentAccountInfo(info, fun.getPaidAmount().subtract(info.getAccountAmount().subtract(acc.getElectronicAccount())), ElectronicAccChangeType.RECHARGE,fun.getContract());
					saveAllStudentAccountInfo(info, fun.getPaidAmount().subtract(info.getAccountAmount().subtract(acc.getElectronicAccount())), ElectronicAccChangeType.PAY_OUT,fun.getContract());
				}else{//公帐合同  就直接把旧电子账户的做充值消耗处理
					saveAllStudentAccountInfo(info, fun.getTransactionAmount().subtract(info.getAccountAmount().subtract(acc.getElectronicAccount())), ElectronicAccChangeType.RECHARGE,fun.getContract());
					saveAllStudentAccountInfo(info, fun.getTransactionAmount().subtract(info.getAccountAmount().subtract(acc.getElectronicAccount())), ElectronicAccChangeType.PAY_OUT,fun.getContract());
				}
				//平行账户需要消耗金额做消耗处理
				saveAllStudentAccountInfo(info,info.getAccountAmount().subtract(acc.getElectronicAccount()),ElectronicAccChangeType.PAY_OUT,fun.getContract());
				info.setAccountAmount(info.getAccountAmount().subtract(info.getAccountAmount().subtract(acc.getElectronicAccount())));
				con.setPubPayContract(1);//公帐合同
				fun.setPubPayContract(1);
			}else if(con.getPubPayContract()==1 && acc.getElectronicAccount().compareTo(info.getAccountAmount())>0){//全部旧金额且合同是公帐合同则把本次收款做充值消耗处理
				saveAllStudentAccountInfo(info,fun.getTransactionAmount(),ElectronicAccChangeType.RECHARGE,fun.getContract());
				saveAllStudentAccountInfo(info,fun.getTransactionAmount(),ElectronicAccChangeType.PAY_OUT,fun.getContract());
				fun.setPubPayContract(1);
			}else if(info.getAccountAmount().compareTo(BigDecimal.ZERO)>0 && acc.getElectronicAccount().compareTo(info.getAccountAmount().subtract(fun.getTransactionAmount()))==0){//全部旧金额且合同是公帐合同则把本次收款做充值消耗处理
				//全部是平行账户的消耗
				if(con.getPubPayContract()==0 && fun.getPaidAmount().compareTo(fun.getTransactionAmount())>0) {//如果是非公帐合同，且不是第一次收款
					saveAllStudentAccountInfo(info, fun.getPaidAmount().subtract(fun.getTransactionAmount()), ElectronicAccChangeType.RECHARGE,fun.getContract());
					saveAllStudentAccountInfo(info, fun.getPaidAmount().subtract(fun.getTransactionAmount()), ElectronicAccChangeType.PAY_OUT,fun.getContract());
				}
				//平行账户需要消耗金额做消耗处理
				saveAllStudentAccountInfo(info,fun.getTransactionAmount(),ElectronicAccChangeType.PAY_OUT,fun.getContract());
				info.setAccountAmount(info.getAccountAmount().subtract(fun.getTransactionAmount()));
				fun.setPubPayContract(1);
				con.setPubPayContract(1);//公帐合同
			}
		}else{
			if(con.getPubPayContract()==0){
				if(fun.getPaidAmount().compareTo(fun.getTransactionAmount())!=0){
					saveAllStudentAccountInfo(info,fun.getPaidAmount().subtract(fun.getTransactionAmount()),ElectronicAccChangeType.RECHARGE,fun.getContract());
					saveAllStudentAccountInfo(info,fun.getPaidAmount().subtract(fun.getTransactionAmount()),ElectronicAccChangeType.PAY_OUT,fun.getContract());
				}
			}
			fun.setPubPayContract(1);
			con.setPubPayContract(1);
		}
	}

	public void saveAllStudentAccountInfo(StudentAccInfo info ,BigDecimal amount,ElectronicAccChangeType type,Contract con){
		User user = userService.getCurrentLoginUser();
		StudentAccInfoLog log = new StudentAccInfoLog();
		log.setChangeAmount(amount);
		log.setAfterAmount(info.getAccountAmount());
		log.setStudent(info.getStudent());
		log.setFromStudent(info.getStudent());
		if(user!=null) {
			log.setCreateUserId(user.getUserId());
		}
		log.setType(type);
		if(con!=null && StringUtils.isNotBlank(con.getId())) {
			if(con.getSignStaff()==null){
				con=contractDao.findById(con.getId());
			}
			log.setUser(con.getSignStaff());
		}
		studentAccInfoLogDao.save(log);
	}

	@Override
	public void updateStudentAccountInfoAmount(String studentId, BigDecimal amount, ElectronicAccChangeType type,Contract con){
		StudentAccInfo info = studentAccInfoDao.findInfoByStudentId(studentId);
		if(type.getValue().contains("OUT")) {
			info.setAccountAmount(info.getAccountAmount().subtract(amount));
		}else{
			info.setAccountAmount(info.getAccountAmount().add(amount));
		}
		saveAllStudentAccountInfo(info,amount,type,con);
	}

	
	/**
	 * 检查是否还有未分配的资金
	 * @param fundsVo
	 * @return
	 */
	private boolean checkCanSave(FundsChangeHistoryVo fundsVo) {
		String contractId = fundsVo.getContractId();
		if (StringUtils.isBlank(contractId)) {
			return false;
		} 
		Contract contract = contractDao.findById(contractId);
		calculateContractDomain(contract);
		if (contract.getAvailableAmount().compareTo(BigDecimal.ZERO)>0){
			return true;
		}else {
			return false;
		}
	}

	/**
	  * 保存收款冲销记录
	  */
	@Override
	public FundsChangeHistory saveFundWashOfContract(String fundsId, BigDecimal fundsWashAmount) {
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundsId);
		ContractVo contractVo = this.getContractById(fundsChangeHistory.getContract().getId());
		if (contractVo.getIsNarrow()==1){
			throw new ApplicationException("合同缩单后，不能再对合同进行收款冲销");
		}
		BigDecimal canWashAmount = fundsChangeHistory.getTransactionAmount()
				.subtract(fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId()));
		BigDecimal maxWashAmount = canWashAmount.compareTo(contractVo.getAvailableAmount()) > 0 
									? contractVo.getAvailableAmount() : canWashAmount;
		if (fundsWashAmount.compareTo(maxWashAmount) > 0) {
			throw new ApplicationException("收款可对销金额大于0，小于等于收款可冲销金额" + maxWashAmount);
		};
		
		
		String transactionId = "";
		if (StringUtils.isBlank(fundsChangeHistory.getTransactionId())) {
			transactionId = UUID.randomUUID().toString();
			fundsChangeHistory.setTransactionId(transactionId);
		} else {
			transactionId = fundsChangeHistory.getTransactionId();
		}
		
		List<Message> messageList=new ArrayList<Message>();//个人业绩List
		Boolean deleteBonus=false;//是否删除个人业绩
//		List<ContractBonus> cbList = contractBonusDao.findByFundsChangeHistoryId(fundsId);
		List<IncomeDistribution> cbList = incomeDistributionDao.findByFundsChangeHistoryId(fundsId);

		if (cbList != null && cbList.size() > 0) {
			BigDecimal cbTotalBonusAmount = BigDecimal.ZERO;
			BigDecimal cbTotalCampusAmount = BigDecimal.ZERO;
			for (IncomeDistribution cb : cbList) {
				if(cb.getBonusStaff()!=null){//如果是个人业绩就添加到List
					addBonusToMessage(cb, messageList,"0");
				}
				cbTotalBonusAmount = cbTotalBonusAmount.add(cb.getAmount() != null ? cb.getAmount() : BigDecimal.ZERO);
				cbTotalCampusAmount = cbTotalCampusAmount.add(cb.getAmount() != null ? cb.getAmount() : BigDecimal.ZERO);
			}
			BigDecimal washAmount = fundsChangeHistoryDao.getWashSumFundsByTransactionId(transactionId);
			if (fundsChangeHistory.getTransactionAmount().subtract(washAmount).subtract(fundsWashAmount).compareTo(cbTotalBonusAmount) < 0) {
//				contractBonusDao.deleteByFundsChangeHistoryId(fundsId, "bonus");
				incomeDistributionDao.deleteByFundsChangeHistoryId(fundsId, "bonus");
				deleteBonus=true;
			}
			if (fundsChangeHistory.getTransactionAmount().subtract(washAmount).subtract(fundsWashAmount).compareTo(cbTotalCampusAmount) < 0) {
//				contractBonusDao.deleteByFundsChangeHistoryId(fundsId, "campus");
				incomeDistributionDao.deleteByFundsChangeHistoryId(fundsId, "campus");
			}
		}
		
		fundsChangeHistoryDao.merge(fundsChangeHistory);
		FundsChangeHistory washFunds = this.transToWashFunds(fundsChangeHistory);
		washFunds.setTransactionAmount(fundsWashAmount);
		washFunds.setTransactionId(transactionId);
		Contract contract = washFunds.getContract();
		Double historySum = fundsChangeHistoryDao.historySumFundsChange(contract.getId());
		washFunds.setPaidAmount(BigDecimal.valueOf(historySum).subtract(fundsWashAmount));
		washFunds.setCamNotAssignedAmount(BigDecimal.ZERO);
		washFunds.setUserNotAssignedAmount(BigDecimal.ZERO);
		
		if (!ContractType.INIT_CONTRACT.equals(contract.getContractType())) {
			fundsChangeHistoryDao.save(washFunds);
			fundsChangeHistoryDao.flush();
			contractDao.getHibernateTemplate().refresh(contract);
		}
		   
		// 查询或创建Student view 物理视图
		Student student =  washFunds.getContract().getStudent();
		
		StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
		
		// 更改合同的 待付款，已交款，可分配资金
		contract.setPaidAmount(contract.getPaidAmount().subtract(fundsWashAmount));
		this.calculateContractDomain(contract);
		
		// 检查 合同缴费状态
		int flag = contract.getPendingAmount().compareTo(BigDecimal.ZERO);
		if(flag == 0) {
			contract.setPaidStatus(ContractPaidStatus.PAID);
		} else if (flag >0) {
			contract.setPaidStatus(ContractPaidStatus.PAYING);
		} else {
			throw new ApplicationException(ErrorCode.MONEY_ERROR);
		}
		
		if(contract.getPaidAmount().compareTo(BigDecimal.ZERO)==0 && fundsWashAmount.compareTo(BigDecimal.ZERO) > 0) {
		    contract.setPaidStatus(ContractPaidStatus.UNPAY);
		}

		if(contract.getPaidAmount().compareTo(BigDecimal.ZERO)==0 && contract.getPubPayContract()==1){//如果是公帐合同并且合同收款为0
			contract.setPubPayContract(0);
			fundsChangeHistory.setPubPayContract(0);
		}

		contractDao.flush();
		
		//保存客户事件及发浏览器消息
//		customerEventService.saveCustomerDynamicStatus(contract.getCustomer().getId(), CustomerEventType.CHARGER, "客户" + contract.getCustomer().getName() + "交款"+money.toString()+"元！", "");
		
		// 合同收款后，同步更新学生账户
		studnetAccMv = getStudentAccoutInfo(student.getId());
		studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
		studnetAccMvDao.flush();
		
		//收款保存记录
		ContractRecord cr=new ContractRecord(contract);
		cr.setUpdateType(UpdateType.CHARGE);
		if(userService.getCurrentLoginUser()!=null){
			cr.setCreateByStaff(userService.getCurrentLoginUser());
		}else{
			cr.setCreateByStaff(fundsChangeHistory.getChargeBy());
		}
		contractRecordDao.save(cr);

		/**
		 * 更新冲销后的待分配业绩
		 */
		updateFundsChangeHistoryNotAssigned(fundsId);
		
		if(deleteBonus ){//&& !ContractType.LIVE_CONTRACT.equals(contract.getContractType())
			pushToQueue(messageList);//添加到队列
		}

		if(!ContractType.LIVE_CONTRACT.equals(contract.getContractType())){
			PushRedisMessageUtil.putMsgToQueueForFinance(washFunds, contract, "2");
		}
		//冲销处理学生状态
		PushRedisMessageUtil.pushStudentToMsg(fundsChangeHistory.getStudent().getId());
		
		return washFunds;
	}
	
	private FundsChangeHistory transToWashFunds(FundsChangeHistory fundsChangeHistory) {
		FundsChangeHistory washFunds = new FundsChangeHistory();
		washFunds.setContract(fundsChangeHistory.getContract());
		washFunds.setStudent(fundsChangeHistory.getStudent());
		washFunds.setTransactionTime(fundsChangeHistory.getTransactionTime());
		washFunds.setRemark(fundsChangeHistory.getRemark());
		washFunds.setChannel(fundsChangeHistory.getChannel());
		washFunds.setPOSid(fundsChangeHistory.getPOSid());
//		washFunds.setIsTurnPosPay(fundsChangeHistory.getIsTurnPosPay());
		washFunds.setCollectCardNo(fundsChangeHistory.getCollectCardNo());
		washFunds.setCollector(fundsChangeHistory.getCollector());
		washFunds.setFundBlCampusId(fundsChangeHistory.getFundBlCampusId());
		washFunds.setFundsPayType(FundsPayType.WASH);
		washFunds.setReceiptTime(DateTools.getCurrentDateTime());
		washFunds.setAuditStatus(FundsChangeAuditStatus.VALIDATE);
		if(userService.getCurrentLoginUser()!=null){
			washFunds.setAuditUser(userService.getCurrentLoginUser());
			washFunds.setChargeBy(userService.getCurrentLoginUser());
		}else{
			washFunds.setAuditUser(fundsChangeHistory.getAuditUser());
			washFunds.setChargeBy(fundsChangeHistory.getChargeBy());
		}
		return washFunds;
	}
	
	
	/**
	 * 如果是合同的第一次收款，自动提交排课需求
	 * @param contract
	 */
	public void addCourseRequirementIfFirstFundForContract(Contract contract) {
//		List<FundsChangeHistory>  funds = fundsChangeHistoryDao.findByCriteria(Expression.eq("contract.id", contract.getId()));
		//如果查收款记录表，合同如果第一次是小班，后面才加的一对一，就不会提交排课需求，改为从排课需求表直接找学生的需求，没有就加一条。
		List<CourseRequirementEditVo> requirementList = courseService.findCourseRequirementByStudentId(contract.getStudent().getId());
		// 如果是第一次收款并且有一对一产品
		if (requirementList.size() == 0 && contract.getOneOnOneNormalProduct() != null) {
			CourseRequirementEditVo courseRequirementEditVo = new CourseRequirementEditVo();
			courseRequirementEditVo.setRemark("新收款合同排课需求");
			courseRequirementEditVo.setCourseDateDesciption("新收款合同排课需求");
			courseRequirementEditVo.setRequirementCetegory(CourseRequirementCetegory.NEW_CONTRACT);
			courseRequirementEditVo.setLastArrangeTime(DateTools.getDateWithPlusDay(new Date(), 1));
			courseRequirementEditVo.setStudentId(contract.getStudent().getId());
			courseService.saveOrUpdateCourseRequirement(courseRequirementEditVo);
		}
	}

	@Override
	public CustomerVo saveCustomerInfo(ContractVo contractVo) {
        User user = userService.getCurrentLoginUser();
		Student student =  new Student();
		if(StringUtils.isNotBlank(contractVo.getStuId())){
			student=studentDao.findById(contractVo.getStuId());
			if (StringUtils.isNotEmpty(contractVo.getStudyManagerId())) {
				student.setStudyManegerId(contractVo.getStudyManagerId());
			}
			if (StringUtils.isNotEmpty(contractVo.getSchoolId())) {
				student.setSchool(new StudentSchool(contractVo.getSchoolId()));
			}
			if (StringUtils.isNotEmpty(contractVo.getSchoolOrTemp())){
				student.setSchoolOrTemp(contractVo.getSchoolOrTemp());
			}
		}else{
			if (StringUtils.isNotEmpty(contractVo.getStudyManagerId())) {
				student.setStudyManegerId(contractVo.getStudyManagerId());
			}
			if (StringUtils.isNotEmpty(contractVo.getSchoolId())) {
				student.setSchool(new StudentSchool(contractVo.getSchoolId()));
			}
			//学生必填字段
			student.setSchoolOrTemp(contractVo.getSchoolOrTemp());
			student.setName(contractVo.getStuName());
			student.setStudentType(StudentType.ENROLLED);
//			新的更改， 不需要 保存年纪的字段
			DataDict gradeDict = dataDictDao.findById(contractVo.getGradeId());
			student.setGradeDict(gradeDict);
//			studentDao.save(student);
			if(StringUtils.isNotBlank(contractVo.getBlCampusId())){
				student.setBlCampusId(contractVo.getBlCampusId());
			}
			student.setContact(contractVo.getCusPhone());
			studentService.saveOrUpdateStudent(student);
		}
		
		this.contractDao.flush();
		
		Customer customer =  new Customer();
		customer.setName(contractVo.getCusName());
		customer.setContact(contractVo.getCusPhone());
		customer.setDealStatus(CustomerDealStatus.SIGNEUP);
        customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
        customer.setDeliverTarget(user.getUserId());
        customer.setDeliverTargetName(user.getName());
        
        //新增需求#907  通过合同管理---添加合同页面录入的新客户，资源入口默认打标为直访（直接生效） xiaojinwang 20170503 
        customer.setPreEntrance(new DataDict(ResEntranceType.DIRECT_VISIT.getValue()));
        customer.setResEntrance(new DataDict(ResEntranceType.DIRECT_VISIT.getValue()));
        customer.setVisitType(VisitType.PARENT_COME); 
        customer.setVisitComeDate(new Date());
		String result=customerService.saveOrUpdateCustomer(customer);
		
		//增加上门数
		CustomerDynamicStatus dynamicStatus = new CustomerDynamicStatus();
		dynamicStatus.setDynamicStatusType(CustomerEventType.VISITCOME);
		dynamicStatus.setDescription("新增上门数");
		dynamicStatus.setResEntrance(new DataDict(ResEntranceType.DIRECT_VISIT.getValue()));
		dynamicStatus.setStatusNum(1);
		dynamicStatus.setVisitFlag(CustomerDynamicStatus.VISITFLAG.no);
        customerEventService.addCustomerDynameicStatus(customer, dynamicStatus,user);
		if(result!=null && "fail".equals(result)){
			throw new ApplicationException("该客户联系方式已经存在于系统当中，如果确定是同一人，请模糊输入时按提示选择已存在客户联系方式。");
		}
//		customerDao.save(customer);
		
		customer.getStudents().add(student);
		
		Contract contract = new Contract();
		contract.setCustomer(customer);
		contract.setStudent(student);
		
		CustomerVo voCus = (CustomerVo) HibernateUtils.voObjectMapping(customer, CustomerVo.class);
		return voCus;
				
	}
	
	@Override
	public void saveFullContractInfo(ContractVo contractVo) throws Exception {
        User user = userService.getCurrentLoginUser();

		saveCusAndStuInfoInContract(contractVo);
		
		Contract contract = setContractBasicInfo(contractVo);
		
		// 设置 oneOnOne 统一价格 和 赠送课时 的product ID
		for(ContractProductVo vo: contractVo.getContractProductVos()) {
			if(vo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_FREE) {
				vo.setProductId(productService.getFreeHourProduct(contractVo.getGradeId()).getId());
			} else if (vo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_NORMAL) {
				vo.setProductId(productService.getOneOnOneNormalProduct(contractVo.getGradeId()).getId());
			}  else if (vo.getProductType() ==  ProductType.SMALL_CLASS) {
				// 表单预处理：小班报读课时不能大于产品课时
				Product prd = productDao.findById(vo.getProductId());
				if(BigDecimal.valueOf(vo.getQuantity()).compareTo(prd.getMiniClassTotalhours()) > 0){
					throw new ApplicationException("小班报读课时不能大于产品课时");
				}
			}
		}

		//	合同产品的保存, 其他， 小班 和 一对一的统一课程 和 一对一赠送课程
        Map<ContractProduct,String> addForMiniClassMap = new HashMap<ContractProduct, String>();
        Set<ContractProduct> conProducts = new HashSet<ContractProduct>();
        Iterator<ContractProductVo> contractProductVoIterator = contractVo.getContractProductVos().iterator();
        while(contractProductVoIterator.hasNext()){
            ContractProductVo vo = contractProductVoIterator.next();
            ContractProduct conPro = HibernateUtils.voObjectMapping(vo,ContractProduct.class);
            conPro.setContract(contract);
            this.calSingleContractProduct(conPro);
            conProducts.add(conPro);
            if (vo.getProductType() ==  ProductType.SMALL_CLASS && StringUtils.isNotBlank(vo.getMiniClassId())) {
                addForMiniClassMap.put(conPro,vo.getMiniClassId() + "," + vo.getFirstSchoolTime());
            }
        }
//        Set<ContractProduct> conProducts = HibernateUtils.voSetMapping(contractVo.getContractProductVos(), ContractProduct.class);
//		for(ContractProduct conPro: conProducts) {
//			conPro.setContract(contract);
//			this.calSingleContractProduct(conPro);
//		}
		contract.setContractProducts(conProducts);
		
		//contract.setContractProducts(conProducts);
		
		if(contract.getStudent().getOneOnOneStatus()!=null && contract.getOneOnOneNormalProduct()!=null){//如果学生一对一状态为空，一对一合同产品不为空，就把状态初始化
			contract.getStudent().setOneOnOneStatus(StudentOneOnOneStatus.NEW);
		}
		if(contract.getStudent().getSmallClassStatus()!=null && contract.getSmallProducts()!=null){//如果学生小班状态为空，小班合同产品不为空，就把状态初始化
			contract.getStudent().setSmallClassStatus(StudentSmallClassStatus.NEW);
		}
		
		contractDao.save(contract);
		contractDao.flush();
		contractDao.getCurrentSession().refresh(contract);
		
		// 计算单价 从一对一的其中任意一个产品中获取单价
		// 统一价格 已经放在统一价格产品里面
//		BigDecimal price = this.getOneOnOnePrice(contract.getStudent().getGradeDict());
//		contract.setOneOnOneUnitPrice(price);
		
		// 	计算全款合同的总钱数
		this.calContractPlanAmount(contract);
		
		// 计算合同的待收款
		contract.setPendingAmount(contract.getTotalAmount());
		
		//修改客户处理状态
		Customer customer = customerDao.findById(contract.getCustomer().getId());
		customer.setDealStatus(CustomerDealStatus.SIGNEUP);
        customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
        customer.setDeliverTarget(user.getUserId());
        customer.setDeliverTargetName(user.getName());
		customerDao.save(customer);
		
		if (ContractType.INIT_CONTRACT.equals(contract.getContractType())) {
        	saveFundOfContract(new FundsChangeHistoryVo(contract.getId(), contract.getPendingAmount().doubleValue()));
        }
		
		// 更新Student acc view , 更新合同总金额
//		Student student =  contract.getStudent();
//		StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
//		if(studnetAccMv == null) {
//			studnetAccMv =  new StudnetAccMv(student);
//			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
//			studnetAccMvDao.flush();
//		} 
//		studnetAccMv.setTotalAmount(studnetAccMv.getTotalAmount().add(contract.getTotalAmount()));
		updateStudentAccMv(contract.getStudent());

        // 报小班
        for(ContractProduct conPro : addForMiniClassMap.keySet()){
        	String miniClassId = addForMiniClassMap.get(conPro).split(",")[0];
        	String firstCourseDate = addForMiniClassMap.get(conPro).split(",")[1];
            smallClassService.AddStudentForMiniClasss(contractVo.getStuId(),miniClassId,contract.getId(),conPro.getId(),firstCourseDate, true);
        }
        
        //通知学管主管分配学生    关闭dwr   2016-12-17
//        List<User> studentManagerHeads = userService.getUserByRoldCodes(RoleCode.STUDY_MANAGER_HEAD.getValue());
//        if (studentManagerHeads != null && studentManagerHeads.size() > 0) {
//        	for (User studentManagerHead : studentManagerHeads) {
//        		if(contract.getContractType() == ContractType.NEW_CONTRACT)
//        			messageService.sendMessage(MessageType.SYSTEM_MSG, "新签合同学生录入", "新签合同的"+contractVo.getStuName()+"学生 录入系统，请及时分配给学管", MessageDeliverType.SINGLE, studentManagerHead.getUserId());
//        	}
//        }
		
        
	}
	


//	@Override
//	public void saveDepositContractInfo(ContractVo contractVo) {
//		saveCusAndStuInfoInContract(contractVo);
//		
//		Contract contract = setContractBasicInfo(contractVo);
//		
//		contract.setPendingAmount(contract.getTotalAmount());
//		
//		Student stu = studentDao.findById(contractVo.getStuId());
//		
//		// 计算单价 从一对一的其中任意一个产品中获取单价
//		BigDecimal price = this.getOneOnOnePrice(stu.getGradeDict());
//		contract.setOneOnOneUnitPrice(price);
//		
//		contractDao.save(contract);
//		
//	}
	
	/* 
	 * 获取到当前支付记录之外的其他业绩分配，用于前端计算本次支付金额可以分配多少给每个合同产品
	 * (non-Javadoc)
	 * @see com.eduboss.service.ContractService#getOtherBonusTotal(java.lang.String, java.lang.String)
	 */
	@Override
	public Map getOtherBonusTotal(String contractId, String fundId) {
List<ContractBonus> list = contractBonusDao.findByContractIdExeptFundId(contractId, fundId,null);
		
		//其他校区业绩分配
		BigDecimal ooo=BigDecimal.ZERO;
		BigDecimal mini=BigDecimal.ZERO;
		BigDecimal otm=BigDecimal.ZERO;
		BigDecimal ecs=BigDecimal.ZERO;
		BigDecimal other=BigDecimal.ZERO;
		BigDecimal lecture = BigDecimal.ZERO;


		//其他用户业绩分配
		BigDecimal cooo=BigDecimal.ZERO;
		BigDecimal cmini=BigDecimal.ZERO;
		BigDecimal cotm=BigDecimal.ZERO;
		BigDecimal cecs=BigDecimal.ZERO;
		BigDecimal cother=BigDecimal.ZERO;
		BigDecimal clecture = BigDecimal.ZERO;

		Map returnMap=new HashMap();
		for (ContractBonus con : list) {
			if(con.getOrganization()!=null && con.getType()!=null){//绩效校区不为空就是 校区业绩
				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					ooo=ooo.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
					mini=mini.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
					otm=otm.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.ECS_CLASS)){
					ecs=ecs.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.OTHERS)){
					other=other.add(con.getCampusAmount());
				}else if (con.getType().equals(ProductType.LECTURE)){
					lecture=lecture.add(con.getCampusAmount());
				}
			}else if(con.getType()!=null){//其他就是用户业绩
				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					cooo=cooo.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
					cmini=cmini.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
					cotm=cotm.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.ECS_CLASS)){
					cecs=cecs.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.OTHERS)){
					cother=cother.add(con.getBonusAmount());
				}else if (con.getType().equals(ProductType.LECTURE)){
					clecture = clecture.add(con.getBonusAmount());
				}
			}
		}
		
		returnMap.put("cooo", cooo);
		returnMap.put("cmini", cmini);
		returnMap.put("cotm", cotm);
		returnMap.put("cecs", cecs);
		returnMap.put("cother", cother);
		returnMap.put("clecture", clecture);
		
		returnMap.put("ooo", ooo);
		returnMap.put("mini", mini);
		returnMap.put("otm", otm);
		returnMap.put("ecs", ecs);
		returnMap.put("other", other);
		returnMap.put("lecture", lecture);
		
		return returnMap;
	}

	/**
	 * 获取合同收款记录相关信息
	 *
	 * @param contractId
	 * @param fundId
	 * @return
	 */
	@Override
	public Map getContractInfo(String contractId, String fundId) {
		Map returnMap=new HashMap();

		ContractEachProductTypeMoney c= new ContractEachProductTypeMoney();
		c.setContractId(contractId);
		c = getEachProductTypeMoney(c);


		returnMap.put("contractInfo", c);

		List<IncomeDistribution> incomeExceptThisFund =  incomeDistributionDao.findIncomeExceptThisFund(contractId, fundId);
		IncomeDistributeExceptThisfund incomeDistributeExceptThisfund = new IncomeDistributeExceptThisfund();

		for (IncomeDistribution i : incomeExceptThisFund){
			if (BonusDistributeType.USER.equals(i.getBaseBonusDistributeType())){
				if (ProductType.ONE_ON_ONE_COURSE.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptOneOnOne(incomeDistributeExceptThisfund.getUserExceptOneOnOne().add(i.getAmount()));
				}
				if (ProductType.SMALL_CLASS.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptSmallClass(incomeDistributeExceptThisfund.getUserExceptSmallClass().add(i.getAmount()));
				}
				if (ProductType.ONE_ON_MANY.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptOneOnMany(incomeDistributeExceptThisfund.getUserExceptOneOnMany().add(i.getAmount()));
				}
				if (ProductType.ECS_CLASS.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptEcsClass(incomeDistributeExceptThisfund.getUserExceptEcsClass().add(i.getAmount()));
				}
				if (ProductType.OTHERS.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptOthers(incomeDistributeExceptThisfund.getUserExceptOthers().add(i.getAmount()));
				}
				if (ProductType.LECTURE.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptLecture(incomeDistributeExceptThisfund.getUserExceptLecture().add(i.getAmount()));
				}
				if (ProductType.TWO_TEACHER.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setUserExceptTwoTeacher(incomeDistributeExceptThisfund.getUserExceptTwoTeacher().add(i.getAmount()));
				}
			}else if (BonusDistributeType.CAMPUS.equals(i.getBaseBonusDistributeType())){
				if (ProductType.ONE_ON_ONE_COURSE.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptOneOnOne(incomeDistributeExceptThisfund.getCampusExceptOneOnOne().add(i.getAmount()));
				}
				if (ProductType.SMALL_CLASS.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptSmallClass(incomeDistributeExceptThisfund.getCampusExceptSmallClass().add(i.getAmount()));
				}
				if (ProductType.ONE_ON_MANY.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptOneOnMany(incomeDistributeExceptThisfund.getCampusExceptOneOnMany().add(i.getAmount()));
				}
				if (ProductType.ECS_CLASS.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptEcsClass(incomeDistributeExceptThisfund.getCampusExceptEcsClass().add(i.getAmount()));
				}
				if (ProductType.OTHERS.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptOthers(incomeDistributeExceptThisfund.getCampusExceptOthers().add(i.getAmount()));
				}
				if (ProductType.LECTURE.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptLecture(incomeDistributeExceptThisfund.getCampusExceptLecture().add(i.getAmount()));
				}
				if (ProductType.TWO_TEACHER.equals(i.getProductType())){
					incomeDistributeExceptThisfund.setCampusExceptTwoTeacher(incomeDistributeExceptThisfund.getCampusExceptTwoTeacher().add(i.getAmount()));
				}
			}
		}

		returnMap.put("incomeDistributeExceptThisfund", incomeDistributeExceptThisfund);

		return returnMap;
	}

	/**
	 * 获取可分配业绩
	 * @param contractId
	 * @param fundId
	 * @return
	 */
	@Override
	public Map getKeFenPeiBonus(String contractId, String fundId) {

		Map returnMap=new HashMap();

		ContractEachProductTypeMoney contractEachProductTypeMoney = new ContractEachProductTypeMoney();
		contractEachProductTypeMoney.setContractId(contractId);
		contractEachProductTypeMoney = getEachProductTypeMoney(contractEachProductTypeMoney);
		BigDecimal otmMoney = contractEachProductTypeMoney.getOtmMoney();
		BigDecimal miniMoney = contractEachProductTypeMoney.getMiniMoney();
		BigDecimal oneMoney = contractEachProductTypeMoney.getOneMoney();
		BigDecimal ecsMoney = contractEachProductTypeMoney.getEcsMoney();
		BigDecimal otherMoney = contractEachProductTypeMoney.getOtherMoney();
		BigDecimal lectureMoney = contractEachProductTypeMoney.getLectureMoney();

		//校区除了其中一样产品的已分业绩
		BigDecimal exceptOooCampus = BigDecimal.ZERO;
		BigDecimal exceptMiniCampus = BigDecimal.ZERO;
		BigDecimal exceptOtmCampus = BigDecimal.ZERO;
		BigDecimal exceptEcsCampus = BigDecimal.ZERO;
		BigDecimal exceptOtherCampus = BigDecimal.ZERO;
		BigDecimal exceptLectureCampus = BigDecimal.ZERO;

		//用户除了其中一样产品的已分业绩
		BigDecimal exceptOooUser = BigDecimal.ZERO;
		BigDecimal exceptMiniUser = BigDecimal.ZERO;
		BigDecimal exceptOtmUser = BigDecimal.ZERO;
		BigDecimal exceptEcsUser = BigDecimal.ZERO;
		BigDecimal exceptOtherUser = BigDecimal.ZERO;
		BigDecimal exceptLectureUser = BigDecimal.ZERO;


		calCampusAndUserKefenpeiyeji(contractId, fundId, otmMoney, miniMoney, oneMoney, ecsMoney, otherMoney,lectureMoney, returnMap);

		calSubmitYejiMax(contractId, fundId, otmMoney, miniMoney, oneMoney, ecsMoney, otherMoney,lectureMoney
				, exceptOooCampus, exceptMiniCampus, exceptOtmCampus, exceptEcsCampus, exceptOtherCampus,exceptLectureCampus
				, exceptOooUser, exceptMiniUser, exceptOtmUser, exceptEcsUser, exceptOtherUser,exceptLectureUser
				,returnMap);
		return returnMap;
	}

	/**
	 * 更新收款记录的归属校区
	 * @param id
	 * @param blCampusId
	 * @return
	 */
	@Override
	public void updateFundCampusIdById(String id, String blCampusId) {
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(id);
		if (fundsChangeHistory != null){
			fundsChangeHistory.setFundBlCampusId(blCampusId);
		}
	}

	/**
	 * 计算每次提交业绩的时候最多金额 (后面改，太多参数了)
	 * @param contractId
	 * @param fundId
	 * @param otmMoney
	 * @param miniMoney
	 * @param oneMoney
	 * @param ecsMoney
	 * @param otherMoney
	 * @param lectureMoney
	 * @param exceptOooCampus
	 * @param exceptMiniCampus
	 * @param exceptOtmCampus
	 * @param exceptEcsCampus
	 * @param exceptOtherCampus
	 * @param exceptLectureCampus
	 * @param exceptOooUser
	 * @param exceptMiniUser
	 * @param exceptOtmUser
	 * @param exceptEcsUser
     * @param exceptOtherUser
     * @param exceptLectureUser
     * @param returnMap
     */
	private void calSubmitYejiMax(String contractId, String fundId, BigDecimal otmMoney, BigDecimal miniMoney, BigDecimal oneMoney, BigDecimal ecsMoney, BigDecimal otherMoney,BigDecimal lectureMoney
			, BigDecimal exceptOooCampus, BigDecimal exceptMiniCampus, BigDecimal exceptOtmCampus, BigDecimal exceptEcsCampus, BigDecimal exceptOtherCampus,BigDecimal exceptLectureCampus
			, BigDecimal exceptOooUser, BigDecimal exceptMiniUser, BigDecimal exceptOtmUser, BigDecimal exceptEcsUser, BigDecimal exceptOtherUser,BigDecimal exceptLectureUser
			, Map returnMap) {
		/**
		 * 除了本次收款外合同的其他已分配的业绩
		 */
		List<ContractBonus> list = contractBonusDao.findByContractIdExeptFundId(contractId, fundId,null);
		for (ContractBonus con:list){
			if(con.getOrganization()!=null && con.getType()!=null){//绩效校区不为空就是 校区业绩
				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					exceptOooCampus=exceptOooCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
					exceptMiniCampus=exceptMiniCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
					exceptOtmCampus=exceptOtmCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.ECS_CLASS)){
					exceptEcsCampus=exceptEcsCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.OTHERS)){
					exceptOtherCampus=exceptOtherCampus.add(con.getCampusAmount());
				}else if (con.getType().equals(ProductType.LECTURE)){
					exceptLectureCampus=exceptLectureCampus.add(con.getCampusAmount());
				}
			}else if(con.getType()!=null){//其他就是用户业绩
				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					exceptOooUser=exceptOooUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
					exceptMiniUser=exceptMiniUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
					exceptOtmUser=exceptOtmUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.ECS_CLASS)){
					exceptEcsUser=exceptEcsUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.OTHERS)){
					exceptOtherUser=exceptOtherUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.LECTURE)){
					exceptLectureUser=exceptLectureUser.add(con.getBonusAmount());
				}
			}
		}
		returnMap.put("oooUserTotal",oneMoney.subtract(exceptOooUser));
		returnMap.put("miniUserTotal",miniMoney.subtract(exceptMiniUser));
		returnMap.put("otmUserTotal",otmMoney.subtract(exceptOtmUser));
		returnMap.put("ecsUserTotal",ecsMoney.subtract(exceptEcsUser));
		returnMap.put("otherUserTotal",otherMoney.subtract(exceptOtherUser));
		returnMap.put("lectureUserTotal",lectureMoney.subtract(exceptLectureUser));

		returnMap.put("oooCampusTotal",oneMoney.subtract(exceptOooCampus));
		returnMap.put("miniCampusTotal",miniMoney.subtract(exceptMiniCampus));
		returnMap.put("otmCampusTotal",otmMoney.subtract(exceptOtmCampus));
		returnMap.put("ecsCampusTotal",ecsMoney.subtract(exceptEcsCampus));
		returnMap.put("otherCampusTotal",otherMoney.subtract(exceptOtherCampus));
		returnMap.put("lectureCampusTotal",lectureMoney.subtract(exceptLectureCampus));
	}

	/**
	 * 本合同已分配资金后，每种产品已分配一定的业绩后（A），对于每次收款减去本次收款已分配的业绩后(B)。可分配业绩为 min(A,B)
	 * @param contractId 合同id
	 * @param fundId  收款记录id
	 * @param otmMoney   本合同一对多可分配金额
	 * @param miniMoney 本合同小班可分配金额
	 * @param oneMoney  本合同一对多可分配金额
	 * @param ecsMoney  本合同目标班可分配金额
	 * @param otherMoney  本合同其他可分配金额
	 * @param lectureMoney  本合同讲座可分配金额
     * @param returnMap
     */
	private void calCampusAndUserKefenpeiyeji(String contractId, String fundId, BigDecimal otmMoney, BigDecimal miniMoney, BigDecimal oneMoney, BigDecimal ecsMoney, BigDecimal otherMoney,BigDecimal lectureMoney, Map returnMap) {
		List<ContractBonusVo> contractBonusList= getContractBonusByFundsChangeHistoryId(fundId);  //本次收款的业绩分配
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundId);

		//本次冲销总额
		BigDecimal washSumFunds = fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId());
		//本次支付总额
		BigDecimal thisTimePayMoney = fundsChangeHistory.getTransactionAmount();

		//实际支付总额为 本次支付总额-本次冲销总额
		thisTimePayMoney = thisTimePayMoney.subtract(washSumFunds);

		BigDecimal campusYifenpei = BigDecimal.ZERO;  //本次收款校区已分配的业绩
		BigDecimal userYifenpei = BigDecimal.ZERO;    //本次收款用户已分配的业绩
		for (ContractBonusVo cbv:contractBonusList){
			if (cbv.getOrganizationId()!=null){
				campusYifenpei= campusYifenpei.add(cbv.getCampusAmount());
			}
			if (cbv.getBonusStaffId()!=null){
				userYifenpei =userYifenpei.add(cbv.getBonusAmount());
			}
		}

		//本次合同校区业绩分配
		BigDecimal oooCampus = BigDecimal.ZERO;
		BigDecimal miniCampus = BigDecimal.ZERO;
		BigDecimal otmCampus = BigDecimal.ZERO;
		BigDecimal ecsCampus = BigDecimal.ZERO;
		BigDecimal otherCampus = BigDecimal.ZERO;
		BigDecimal lectureCampus = BigDecimal.ZERO;

		//本次合同用户业绩分配
		BigDecimal oooUser = BigDecimal.ZERO;
		BigDecimal miniUser = BigDecimal.ZERO;
		BigDecimal otmUser = BigDecimal.ZERO;
		BigDecimal ecsUser = BigDecimal.ZERO;
		BigDecimal otherUser = BigDecimal.ZERO;
		BigDecimal lectureUser = BigDecimal.ZERO;
		/**
		 * 本合同的已分配业绩
		 */
		List<ContractBonus> list = contractBonusDao.findByContractIdExeptFundId(contractId, null,null);

		for (ContractBonus con : list) {
			if(con.getOrganization()!=null && con.getType()!=null){//绩效校区不为空就是 校区业绩
				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					oooCampus=oooCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
					miniCampus=miniCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
					otmCampus=otmCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.ECS_CLASS)){
					ecsCampus=ecsCampus.add(con.getCampusAmount());
				}else if(con.getType().equals(ProductType.OTHERS)){
					otherCampus=otherCampus.add(con.getCampusAmount());
				}
			}else if(con.getType()!=null){//其他就是用户业绩
				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					oooUser=oooUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
					miniUser=miniUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
					otmUser=otmUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.ECS_CLASS)){
					ecsUser=ecsUser.add(con.getBonusAmount());
				}else if(con.getType().equals(ProductType.OTHERS)){
					otherUser=otherUser.add(con.getBonusAmount());
				}
			}
		}

		returnMap.put("oooUser", Math.min(oneMoney.subtract(oooUser).doubleValue(),thisTimePayMoney.subtract(userYifenpei).doubleValue()));
		returnMap.put("miniUser",Math.min(miniMoney.subtract(miniUser).doubleValue(),thisTimePayMoney.subtract(userYifenpei).doubleValue()) );
		returnMap.put("otmUser",Math.min(otmMoney.subtract(otmUser).doubleValue(),thisTimePayMoney.subtract(userYifenpei).doubleValue()) );
		returnMap.put("ecsUser", Math.min(ecsMoney.subtract(ecsUser).doubleValue(),thisTimePayMoney.subtract(userYifenpei).doubleValue())  );
		returnMap.put("otherUser",Math.min(otherMoney.subtract(otherUser).doubleValue(),thisTimePayMoney.subtract(userYifenpei).doubleValue()) );
		returnMap.put("lectureUser",Math.min(lectureMoney.subtract(lectureUser).doubleValue(),thisTimePayMoney.subtract(userYifenpei).doubleValue()) );

		returnMap.put("oooCampus",Math.min(oneMoney.subtract(oooCampus).doubleValue(),thisTimePayMoney.subtract(campusYifenpei).doubleValue()) );
		returnMap.put("miniCampus", Math.min(miniMoney.subtract(miniCampus).doubleValue(),thisTimePayMoney.subtract(campusYifenpei).doubleValue()) );
		returnMap.put("otmCampus", Math.min(otmMoney.subtract(otmCampus).doubleValue(),thisTimePayMoney.subtract(campusYifenpei).doubleValue()) );
		returnMap.put("ecsCampus", Math.min(ecsMoney.subtract(ecsCampus).doubleValue(),thisTimePayMoney.subtract(campusYifenpei).doubleValue()) );
		returnMap.put("otherCampus",Math.min(otherMoney.subtract(otherCampus).doubleValue(),thisTimePayMoney.subtract(campusYifenpei).doubleValue()));
		returnMap.put("lectureCampus",Math.min(lectureMoney.subtract(lectureCampus).doubleValue(),thisTimePayMoney.subtract(campusYifenpei).doubleValue()));
	}

	@Override
	public ContractVo getContractById(String contractid) {
		Contract contract = contractDao.findById(contractid);
		if(contract==null){
			return null;
		}



		Set<ContractProductVo> proVos = HibernateUtils.voSetMapping(contract.getContractProducts(), ContractProductVo.class);

		ContractAlreadyFenpei fenpei = new ContractAlreadyFenpei();
		fenpei.setContractId(contractid);
		/**
		 * 合同已分配业绩
		 */
		fenpei=getContractAlreadyFenpei(fenpei);
		double ooo = Math.max(fenpei.getOooUser().doubleValue(),fenpei.getOooCampus().doubleValue());
		double mini =Math.max(fenpei.getMiniUser().doubleValue(),fenpei.getMiniCampus().doubleValue());
		double otm = Math.max(fenpei.getOtmUser().doubleValue(),fenpei.getOtmCampus().doubleValue());
		double ecs = Math.max(fenpei.getEcsUser().doubleValue(),fenpei.getEcsCampus().doubleValue());
		double other = Math.max(fenpei.getOtherUser().doubleValue(),fenpei.getOtherCampus().doubleValue());
		double lecture = Math.max(fenpei.getLectureUser().doubleValue(), fenpei.getLectureCampus().doubleValue());
		double live = Math.max(fenpei.getLiveUser().doubleValue(),fenpei.getLiveCampus().doubleValue());


		ContractEachProductTypeMoney c= new ContractEachProductTypeMoney();
		c.setContractId(contractid);
		c = getEachProductTypeMoney(c);
		BigDecimal oneMoney = c.getOneMoney();
		BigDecimal miniMoney =c.getMiniMoney();
		BigDecimal otmMoney=c.getOtmMoney();
		BigDecimal ecsMoney=c.getEcsMoney();
		BigDecimal otherMoney=c.getOtherMoney();
		BigDecimal lectureMoney = c.getLectureMoney();
		BigDecimal liveMoney=c.getLiveMoney();


		int numOfLiveProduct = 0;

		for (ContractProductVo proVo: proVos) {
			List<MiniClassRelation> miniClassRelations = smallClassService.getMiniClassRelatonsByStudentNewMiniClass(contract.getStudent().getId(), proVo.getMiniClassId());
			for (MiniClassRelation relation: miniClassRelations) {
				if (MiniClassRelationType.CONTINUE == relation.getRelationType()) {
					proVo.setContinueMiniClassId(relation.getOldMiniClass().getMiniClassId());
				} else if (MiniClassRelationType.EXTEND == relation.getRelationType()) {
					proVo.setExtendMiniClassId(relation.getOldMiniClass().getMiniClassId());
				}
			}

			if(proVo.getProductType().equals(ProductType.TWO_TEACHER)){
				TwoTeacherClassTwoVo v=twoTeacherClassService.findTwoTeacherTwoByContractProductId(proVo.getId());
				if(v!=null)
				proVo.setMiniClassName(v.getName());
			}
			List<AccountChargeRecords>  list = accountChargeRecordsDao.findAccountRecordsByContractProduct(proVo.getId(), "TRUE");
			if (list.size() > 0) {
				proVo.setIsWashed("TRUE");
			} else {
				proVo.setIsWashed("FALSE");
			}
			if (proVo.getProductType()==ProductType.ONE_ON_ONE_COURSE){
				proVo.setMaxKeTiquMoney(ooo);
				proVo.setProductTypeFenpeiMoney(oneMoney);
			}
			if (proVo.getProductType()==ProductType.SMALL_CLASS){
				proVo.setMaxKeTiquMoney(mini);
				proVo.setProductTypeFenpeiMoney(miniMoney);
			}
			if (proVo.getProductType()==ProductType.ONE_ON_MANY){
				proVo.setMaxKeTiquMoney(otm);
				proVo.setProductTypeFenpeiMoney(otmMoney);
			}
			if (proVo.getProductType()==ProductType.ECS_CLASS){
				proVo.setMaxKeTiquMoney(ecs);
				proVo.setProductTypeFenpeiMoney(ecsMoney);
			}
			if (proVo.getProductType()==ProductType.OTHERS){
				proVo.setMaxKeTiquMoney(other);
				proVo.setProductTypeFenpeiMoney(otherMoney);
			}

			if (proVo.getProductType()==ProductType.LECTURE){
				proVo.setMaxKeTiquMoney(lecture);
				proVo.setProductTypeFenpeiMoney(lectureMoney);
			}

			if (proVo.getProductType()==ProductType.LIVE){
				proVo.setMaxKeTiquMoney(live);
				proVo.setProductTypeFenpeiMoney(liveMoney);
				numOfLiveProduct++;
			}
		}

		this.calculateContractDomain(contract);
		//	dozer在Set2Set深层次转换那里出现了问题， 手动自己转一次。 
		ContractVo contractVo =  HibernateUtils.voObjectMapping(contract, ContractVo.class);
		contractVo.setSaleChannel(SaleChannel.OFF_LINE);
		contractVo.setContractProductVos(proVos);
		if (proVos.size()>0 && numOfLiveProduct==proVos.size()){
			contractVo.setLiveProductContract(true);
		}
		if (StringUtils.isNotBlank(contractVo.getBlCampusId())) {
			contractVo.setBlCampusName(organizationDao.findById(contractVo.getBlCampusId()).getName());
		}
		List<BusinessAssocMapping> businessList = businessAssocMappingService
		        .listBusinessAssocMappingByBusinessIds(contract.getId().split(","));
		if (businessList != null && businessList.size() > 0) {
		    contractVo.setSaleChannel(SaleChannel.ON_LINE);
		}

		//业绩分配时间截止后，关联的提取功能也同时禁止 redmine id #1976 产品：黄萌
		if(canTakeawayMoney(contractid)){
			contractVo.setTakeawayMoney(true);
		}else {
			contractVo.setTakeawayMoney(false);
		}
		if (contract.getPosMachineType() != null && com.eduboss.utils.StringUtil.isNotBlank(contract.getPosMachineType().getId())) {
		    contractVo.setPosMachineType(contract.getPosMachineType().getId());
		}
		return contractVo;
	}

	private boolean canTakeawayMoney(String contractid) {
		List<FundsChangeHistory> list = fundsChangeHistoryDao.getFundsChangeHistoryListByContractId(contractid);
		/**
		 * 只要收款的和有收款时间的,不要冲销
		 */
		List<FundsChangeHistory> receiptList =  new ArrayList<>();
		for (FundsChangeHistory f : list){
			if (f.getFundsPayType() == FundsPayType.RECEIPT && StringUtils.isNotBlank(f.getReceiptTime())){
				receiptList.add(f);
			}
		}

		if (receiptList.size()>0){
			//按收款时间顺序排序
			Collections.sort(receiptList, new Comparator<Object>(){

				@Override
				public int compare(Object o1, Object o2) {
					FundsChangeHistory fundsChangeHistory1 = (FundsChangeHistory)o1;
					FundsChangeHistory fundsChangeHistory2 = (FundsChangeHistory)o2;
					long between = DateTools.timeBetween(fundsChangeHistory1.getReceiptTime(), fundsChangeHistory2.getReceiptTime());
					if (between>0){
						return -1;
					}
					if (between<0){
						return 1;
					}
					return 0;
				}
			});
			FundsChangeHistory fundsChangeHistory = receiptList.get(0);
			boolean flag = canUpdateIncomeDistribution(fundsChangeHistory.getId());
			return flag;
		}
		return false;
	}


	/**
	 * 设置一些Contract 公用的属性, 不论是 全款的合同 和 订金合同
	 * @param contractVo
	 * @return
	 */
	private Contract setContractBasicInfo(ContractVo contractVo) {
		Contract contract =  new Contract();
		
		contract.setContractProducts(new HashSet<ContractProduct>());
		
		contract.setStudent(new Student(contractVo.getStuId()));
		contract.setCustomer(new Customer(contractVo.getCusId()));
		contract.setContractType(contractVo.getContractType());
		
		contract.setContractStatus(ContractStatus.NORMAL);
		contract.setPaidStatus(ContractPaidStatus.UNPAY);
		
		contract.setCreateTime(DateTools.getCurrentDateTime());
		contract.setCreateByStaff(userService.getCurrentLoginUser());
		contract.setSignStaff(userService.getCurrentLoginUser());
		contract.setModifyByStaff(userService.getCurrentLoginUser());
		contract.setModifyTime(DateTools.getCurrentDateTime());
		
		contract.setTotalAmount(contractVo.getTotalAmount()==null?BigDecimal. ZERO:contractVo.getTotalAmount());
		contract.setPaidAmount(contractVo.getPaidAmount()==null?BigDecimal. ZERO:contractVo.getPaidAmount());
		contract.setPendingAmount(contractVo.getPendingAmount()==null?BigDecimal. ZERO:contractVo.getPendingAmount());
		contract.setRemainingAmount(BigDecimal. ZERO);
		contract.setConsumeAmount(BigDecimal. ZERO);
		contract.setAvailableAmount(BigDecimal.ZERO);
		
		contract.setOneOnOneRemainingClass(BigDecimal. ZERO);
		contract.setOneOnOneTotalAmount(BigDecimal. ZERO);
		contract.setOneOnOneRemainingAmount(BigDecimal. ZERO);
		contract.setOneOnOnePaidAmount(BigDecimal. ZERO);
		contract.setOneOnOneConsumeAmount(BigDecimal. ZERO);
		contract.setOneOnOneUnitPrice(BigDecimal. ZERO);
		
		contract.setMiniClassTotalAmount(BigDecimal. ZERO);
		contract.setMiniClassPaidAmount(BigDecimal. ZERO);
		contract.setMiniClassConsumeAmount(BigDecimal. ZERO);
		contract.setMiniClassRemainingAmount(BigDecimal. ZERO);
		
		contract.setOtherTotalAmount(BigDecimal. ZERO);
		contract.setOtherPaidAmount(BigDecimal. ZERO);
		contract.setOtherConsumeAmount(BigDecimal. ZERO);
		contract.setOtherRemainingAmount(BigDecimal. ZERO);
		
		//一对多		
		contract.setOneOnManyTotalAmount(BigDecimal.ZERO);
		contract.setOneOnManyPaidAmount(BigDecimal.ZERO);
		contract.setOneOnManyConsumeAmount(BigDecimal.ZERO);
		contract.setOneOnManyRemainingAmount(BigDecimal.ZERO);
		
		
		contract.setFreeTotalHour(contractVo.getFreeTotalHour()==null?BigDecimal. ZERO: contractVo.getFreeTotalHour() );
		contract.setFreeConsumeHour(BigDecimal.ZERO);
		contract.setFreeRemainingHour(BigDecimal.ZERO );
		
		contract.setOneOnOneJson(contractVo.getOneOnOneJson());
		
		contract.setRemark(contractVo.getRemark());
		
		for(ContractProductVo vo: contractVo.getContractProductVos()){
			
			if (!ContractType.INIT_CONTRACT.equals(contractVo.getContractType())
					&& !ProductType.ONE_ON_ONE_COURSE_NORMAL.equals(vo.getProductType())) {
				vo.setPrice(0d);
			}
			
			// vo.setQuantity(1d);
			// vo.setDealDiscount(1d);
			// vo.setPayment(0d);
			vo.setPaidAmount(0d);
			vo.setConsumeAmount(0d);
			vo.setConsumeQuanity(0d);
			vo.setStatus(ContractProductStatus.NORMAL);
			vo.setPaidStatus(ContractProductPaidStatus.UNPAY);
		}
		
		
		return contract;
	}

	/**
	 * 根据学生id查询有剩余金额的合同
	 * @param studentId
	 * @return
	 */
	@Override
	public List<ContractVo> getSurplusContractByStudentId(String studentId) {
		List<Criterion> critList=new ArrayList<Criterion>();
		//合同类型
		critList.add(Expression.or(Expression.eq("contractType", ContractType.NEW_CONTRACT), Expression.eq("contractType", ContractType.RE_CONTRACT)));
		//合同状态
		critList.add(Expression.eq("contractStatus", ContractStatus.NORMAL));
		//合同付款状态
		critList.add(Expression.eq("paidStatus", ContractPaidStatus.PAID));
		
		if(StringUtils.isNotEmpty(studentId)){
			critList.add(Expression.eq("student.id", studentId));
		}
		List<Order> orderList=new ArrayList<Order>();
		orderList.add(Order.desc("createTime"));
		List<Contract> list=contractDao.findAllByCriteria(critList,orderList);
		return HibernateUtils.voListMapping(list, ContractVo.class);
	}


	
	/**
	 * 收款记录导出Excel
	 */
	@Override
	public DataPackage findPageMyFundHistoryExcel(DataPackage dataPackage,
												  FundsChangeSearchVo vo3) {
		dataPackage = this.findPageMyFundHistory(dataPackage,vo3);
		List<FundsChangeHistoryVo> fundVoList = (List<FundsChangeHistoryVo>) dataPackage.getDatas();
		for (FundsChangeHistoryVo vo : fundVoList) {
			if (vo.getFundsPayType() == FundsPayType.WASH) {
				vo.setChannelName("冲销");
			}
		}
		dataPackage.setDatas(fundVoList);
		return dataPackage;
	}
	
	@Override
	public DataPackage findPageMyFundHistory(DataPackage dp, FundsChangeSearchVo vo3) {
		dp =  fundsChangeHistoryDao.listFundsChangeHistory(dp,vo3);

		List<FundsChangeHistory> fundtList =  (List<FundsChangeHistory>) dp.getDatas();
		List<FundsChangeHistoryVo> fundVoList = new ArrayList<FundsChangeHistoryVo>();
		for(FundsChangeHistory fundsChangeHistory : fundtList){
//			Contract contract = contractDao.findById(fundsChangeHistory.getContract().getId());
//			fundsChangeHistory.setContract(contract);
//			calculateContractDomain(fundsChangeHistory.getContract());
			FundsChangeHistoryVo vo = HibernateUtils.voObjectMapping(fundsChangeHistory, FundsChangeHistoryVo.class);
			vo.setWashSumFunds(fundsChangeHistoryDao.getWashSumFundsByTransactionId(vo.getTransactionId()));
			if(vo!=null && StringUtils.isNotBlank(vo.getBlCampusId())){
				Organization o = organizationDao.findById(vo.getBlCampusId());
				if(o!=null)
					vo.setBlCampusName(o.getName());
			}

			fundVoList.add(vo);
		}
		dp.setDatas(fundVoList);
		return dp;
	}


	/**
	 * 收款记录未分配
	 * @param fundsChangeHistoryId
	 * @param type
	 * @return
	 */
	private BigDecimal notAssignedAmountForFundsChange(String fundsChangeHistoryId, BonusDistributeType type) {
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		BigDecimal result = BigDecimal.ZERO;
		if (fundsChangeHistory!=null){
			String money = feiPeiTotalAmount(fundsChangeHistory);
			BigDecimal total = new BigDecimal(money);
			List<IncomeDistribution> contractBonuses = incomeDistributionDao.findIncomeByFundsChangeHistoryIdAndType(fundsChangeHistoryId, type);
			BigDecimal assignedAmount = BigDecimal.ZERO;
			if (contractBonuses.size()>0){
				for (IncomeDistribution i : contractBonuses){
					assignedAmount = assignedAmount.add(i.getAmount());
				}
			}
			result = total.subtract(assignedAmount);
		}
		return result;
	}


	/**
	 * 待分配个人业绩：收款记录的收款金额(收款金额和已分配金额的最小者)-已分配的个人业绩
	 * @param fundsChangeHistoryId
	 * @return
     */
	private BigDecimal userNotAssignedAmountForFundsChange(String fundsChangeHistoryId) {
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		BigDecimal result = BigDecimal.ZERO;
		if (fundsChangeHistory!=null){
			String money = feiPeiTotalAmount(fundsChangeHistory);
			BigDecimal total = new BigDecimal(money);
			StringBuffer sb = new StringBuffer();
			
			
			Map<String, Object> params  = Maps.newHashMap();
			params.put("fundsChangeHistoryId", fundsChangeHistoryId);
			
			//本次收款分配到个人的业绩
			sb.append(" from ContractBonus cb where cb.bonusStaff is not null ");
			sb.append(" and cb.fundsChangeHistory.id= :fundsChangeHistoryId ");
			List<ContractBonus> contractBonuses = contractBonusDao.findAllByHQL(sb.toString(),params);
			BigDecimal assignedAmount = BigDecimal.ZERO;
			if (contractBonuses.size()>0){
				for (ContractBonus c : contractBonuses){
					assignedAmount = assignedAmount.add(c.getBonusAmount());
				}
			}
			result = total.subtract(assignedAmount);
		}
		return result;
	}
	
	/**
	 * 待分配个人业绩：收款记录的收款金额(收款金额和已分配金额的最小者)-已分配的个人业绩
	 * @param fundsChangeHistory
	 * @return
     */
	private BigDecimal userNotAssignedAmountForFundsChange(FundsChangeHistory fundsChangeHistory) {
		BigDecimal result = BigDecimal.ZERO;
		if (fundsChangeHistory!=null){
//			String money = fundsChangeHistory.getTransactionAmount();
			BigDecimal washAmount = fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId());
			//减去冲销金额
			BigDecimal total = fundsChangeHistory.getTransactionAmount().subtract(washAmount);
			StringBuffer sb = new StringBuffer();
			Map<String, Object> params  = Maps.newHashMap();
			params.put("fundsChangeHistoryId", fundsChangeHistory.getId());
			//本次收款分配到个人的业绩
			sb.append(" from ContractBonus cb where cb.bonusStaff is not null ");
			sb.append(" and cb.fundsChangeHistory.id= :fundsChangeHistoryId ");
			List<ContractBonus> contractBonuses = contractBonusDao.findAllByHQL(sb.toString(),params);
			BigDecimal assignedAmount = BigDecimal.ZERO;
			if (contractBonuses.size()>0){
				for (ContractBonus c : contractBonuses){
					assignedAmount = assignedAmount.add(c.getBonusAmount());
				}
			}
			result = total.subtract(assignedAmount);
		}
		return result;
	}

	/**
	 * 计算分配业绩的总和
	 * @param fundsChangeHistory
	 * @return
     */
	private String feiPeiTotalAmount(FundsChangeHistory fundsChangeHistory) {
//		Contract contract = fundsChangeHistory.getContract();
//		Set<ContractProduct> contractProducts = contract.getContractProducts();
//		BigDecimal sum = BigDecimal.ZERO;
//		for (ContractProduct cp : contractProducts){
//			sum = sum.add(cp.getPaidAmount());
//		}
//		double min = Math.min(sum.doubleValue(), fundsChangeHistory.getTransactionAmount().doubleValue());
		StringBuffer buffer = new StringBuffer();
		BigDecimal washAmount = fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId());
		buffer.append(fundsChangeHistory.getTransactionAmount().subtract(washAmount));
		return buffer.toString();
	}

	/**
	 * 待分配校区业绩：收款记录的收款金额-已分配的校区业绩
	 * @return
     */
	private BigDecimal camNotAssignedAmountForFundsChange(String fundsChangeHistoryId) {
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		BigDecimal result = BigDecimal.ZERO;
		if (fundsChangeHistory!=null){
			String money = feiPeiTotalAmount(fundsChangeHistory);
			BigDecimal total = new BigDecimal(money);
			StringBuffer sb = new StringBuffer();
			Map<String, Object> params  = Maps.newHashMap();
			params.put("fundsChangeHistoryId", fundsChangeHistoryId);
			//本次收款分配到校区的业绩
			sb.append(" from ContractBonus cb where cb.organization is not null ");
			sb.append(" and cb.fundsChangeHistory.id= :fundsChangeHistoryId ");
			List<ContractBonus> contractBonuses = contractBonusDao.findAllByHQL(sb.toString(),params);
			BigDecimal assignedAmount = BigDecimal.ZERO;
			if (contractBonuses.size()>0){
				for (ContractBonus c : contractBonuses){
					assignedAmount = assignedAmount.add(c.getCampusAmount());
				}
			}
			result = total.subtract(assignedAmount);
		}
		return result;
	}
	
	/**
	 * 待分配校区业绩：收款记录的收款金额-已分配的校区业绩
	 * @return
     */
	private BigDecimal camNotAssignedAmountForFundsChange(FundsChangeHistory fundsChangeHistory) {
//		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		BigDecimal result = BigDecimal.ZERO;
		if (fundsChangeHistory!=null){
			BigDecimal washAmount = fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId());
			BigDecimal total = fundsChangeHistory.getTransactionAmount().subtract(washAmount);
			StringBuffer sb = new StringBuffer();
			Map<String, Object> params  = Maps.newHashMap();
			params.put("fundsChangeHistoryId", fundsChangeHistory.getId());
			//本次收款分配到校区的业绩
			sb.append(" from ContractBonus cb where cb.organization is not null ");
			sb.append(" and cb.fundsChangeHistory.id= :fundsChangeHistoryId ");
			List<ContractBonus> contractBonuses = contractBonusDao.findAllByHQL(sb.toString(),params);
			BigDecimal assignedAmount = BigDecimal.ZERO;
			if (contractBonuses.size()>0){
				for (ContractBonus c : contractBonuses){
					assignedAmount = assignedAmount.add(c.getCampusAmount());
				}
			}
			result = total.subtract(assignedAmount);
		}
		return result;
	}

	public FundsChangeHistoryVo findFundHistoryById(String id) {
		FundsChangeHistory fundsChangeHistory=fundsChangeHistoryDao.findById(id);
		if(fundsChangeHistory!=null){
			calculateContractDomain(fundsChangeHistory.getContract());
			Student stu=fundsChangeHistory.getStudent();
			BigDecimal electronicAccount=BigDecimal.ZERO;
			if(stu!=null){
				String stuId=stu.getId();
				StudnetAccMv acc=studnetAccMvDao.findById(stuId);
				
				electronicAccount = acc.getElectronicAccount() == null?BigDecimal.ZERO:acc.getElectronicAccount();
				BigDecimal frozenAmount = acc.getFrozenAmount() == null ? BigDecimal.ZERO : acc.getFrozenAmount();
				electronicAccount = electronicAccount.subtract(frozenAmount);
			}
			FundsChangeHistoryVo vo= HibernateUtils.voObjectMapping(fundsChangeHistory, FundsChangeHistoryVo.class);
			if(electronicAccount.compareTo(BigDecimal.ZERO)>0){
				vo.setStuElectronicAmount(electronicAccount.doubleValue());
			}else{
				vo.setStuElectronicAmount(0.00);
			}
			if (StringUtils.isNotBlank(fundsChangeHistory.getFundBlCampusId())) {
				vo.setBlCampusName(organizationDao.findById(fundsChangeHistory.getFundBlCampusId()).getName());
			}
			ContractVo contractVo = this.getContractById(fundsChangeHistory.getContract().getId());
			vo.setAvailableAmount(contractVo.getAvailableAmount());
			vo.setWashSumFunds(fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId()));
			return vo;
		}
		return null;
	}
	
	public void updateFundHistoryPos(FundsChangeHistoryVo vo){
		FundsChangeHistory domain=fundsChangeHistoryDao.findById(vo.getId());
		
		if(domain!=null){
			if(!vo.getChannel().equals(domain.getChannel()) || vo.getChannel() == PayWay.POS){
				domain.setAuditStatus(FundsChangeAuditStatus.UNAUDIT);
				domain.setAuditTime(null);
				domain.setAuditUser(null);
			}
			domain.setChannel(vo.getChannel());
			if (!vo.getChannel().equals(PayWay.POS)) {
				domain.setPOSid(null);
				domain.setPosNumber(null);
				domain.setPosReceiptDate(null);
			}
			if(vo.getChannel().equals(PayWay.POS)){
				String posId = vo.getPOSid();
				posId = posId.replaceFirst("^0*", "");
				if (StringUtils.isNotBlank(posId) && StringUtils.isNotBlank(vo.getPosNumber()) 
						&& !fundsChangeHistoryDao.isPosIAndPosNumberdUnique(vo.getId(), posId, vo.getPosNumber())) {
					throw new ApplicationException("该支付单号和终端号已存在，请检查支付单号和终端号是否正确");
				}
				domain.setIsTurnPosPay("Y");
				domain.setTurnPosTime(DateTools.getCurrentDateTime());
				domain.setPOSid(posId);
				domain.setPosNumber(vo.getPosNumber());
				domain.setPosReceiptDate(vo.getPosReceiptDate());
			}else if(vo.getChannel().equals(PayWay.ELECTRONIC_ACCOUNT)){
				if (fundsChangeHistoryDao.getWashSumFundsByTransactionId(domain.getTransactionId()).compareTo(BigDecimal.ZERO) > 0) {
					throw new ApplicationException("收款已发生冲销，不允许转电子账户收款");
				}
				//现金转电子帐户  删除业绩分配；添加一条电子帐户变更记录；审批状态初始化
				this.cashToElectronic(domain);
				domain.setIsTurnPosPay("");
				domain.setTurnPosTime("");
			}else{
				domain.setIsTurnPosPay("");
				domain.setTurnPosTime("");
			}
			
			//修改了校区，现金流要重新处理
			if(!domain.getFundBlCampusId().equals(vo.getBlCampusId())){
				PushRedisMessageUtil.putMsgToQueueForFinance(domain, domain.getContract(), "2");
				domain.setFundBlCampusId(vo.getBlCampusId());
				PushRedisMessageUtil.putMsgToQueueForFinance(domain, domain.getContract(), "1");
			}
			
			domain.setRemark(vo.getRemark());
			domain.setCollector(vo.getCollector());
			domain.setCollectCardNo(vo.getCollectCardNo());	
			domain.setSystemAuditRemark("修改收款信息");
			fundsChangeHistoryDao.save(domain);
			FundsAuditRecord record = new FundsAuditRecord();
			record.setReceiptUser(domain.getChargeBy());
			record.setSubmitTime(DateTools.getCurrentDateTime());
			record.setRemark("修改收款信息");
			record.setFundsChangeHistoryId(domain.getId());
			record.setCreateTime(DateTools.getCurrentDateTime());
			fundsAuditRecordService.saveFundsAuditRecord(record);
		}
	}
	
	public void cashToElectronic(FundsChangeHistory fundsChargeHistory){
		List<Message> messageList=new ArrayList<Message>();
//		String sql="select * from contract_bonus WHERE FUNDS_CHANGE_ID='"+fundsChargeHistory.getId()+"'";
//		List<ContractBonus> list = contractBonusDao.findBySql(sql);
		List<IncomeDistribution> list = incomeDistributionDao.findByFundsChangeHistoryId(fundsChargeHistory.getId());

		//		删除业绩分配；添加一条电子帐户变更记录
		for (IncomeDistribution con : list) {
			incomeDistributionDao.delete(con);
			if(con.getBonusStaff()!=null){
				addBonusToMessage(con, messageList,"0");//用户业绩删除添加至队列
			}
		}
		
		
		Student stu=fundsChargeHistory.getStudent();
		BigDecimal money=fundsChargeHistory.getTransactionAmount();
		if(stu!=null){
			StudnetAccMv studnetAccMv = studnetAccMvDao.findById(stu.getId());
			if(studnetAccMv == null) {
				studnetAccMv =  new StudnetAccMv(stu);
				studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
				studnetAccMvDao.flush();
			} 
			 BigDecimal electornicAccount = studnetAccMv.getElectronicAccount() != null ? studnetAccMv.getElectronicAccount() : BigDecimal.ZERO; // 获取学生电子账户余额
			 studnetAccMv.setElectronicAccount(electornicAccount.subtract(money));
		     electronicAccountChangeLogDao.saveElecAccChangeLog(stu.getId(), ElectronicAccChangeType.PAY_OUT, "D", money, studnetAccMv.getElectronicAccount(), "从电子账户收款");
		
		}else{
			throw new ApplicationException("没有找到对应的学生");
		}
		
		contractBonusDao.commit();
		pushToQueue(messageList);
		//现金转电子账户，现金流的资金要回冲
		PushRedisMessageUtil.putMsgToQueueForFinance(fundsChargeHistory, fundsChargeHistory.getContract(), "2");
	}

	/**
	 * 保存业绩
	 *
	 * @param fundsChangeHistoryId
	 * @param list
	 */
	@Override
	public void saveIncomeDistribution(String fundsChangeHistoryId, List<IncomeDistributionVo> list) {

		if(!canUpdateIncomeDistribution(fundsChangeHistoryId)){
			throw new ApplicationException("收款2个月后不能分配业绩");
		}

		FundsChangeHistory fundsChangeHistory=fundsChangeHistoryDao.findById(fundsChangeHistoryId);

		/**
		 * 检查是否超过已分配金额
		 */
		checkIncomeOverArrange(fundsChangeHistory, list);

		/**
		 * 检查是否超过收款记录
		 */
		checkIncomeDistribution(fundsChangeHistory, list);

		/**
		 * 添加到流水表
		 */
		addToIncomeDistributeStatements(fundsChangeHistory, list);

		List<IncomeDistribution> incomeDistributionsOld = incomeDistributionDao.findByFundsChangeHistoryId(fundsChangeHistoryId);

		List<Message> messageList =new ArrayList<>();
		for (IncomeDistribution i : incomeDistributionsOld){
			if(i.getBonusStaff()!=null){
				addBonusToMessage(i,messageList,"0");//删除业绩
			}

			incomeDistributionDao.delete(i);
		}

		for (IncomeDistributionVo vo :list){
			IncomeDistribution incomeDistribution = HibernateUtils.voObjectMapping(vo, IncomeDistribution.class);
			incomeDistribution.setFundsChangeHistory(fundsChangeHistory);
			Contract contract = fundsChangeHistory.getContract();
			if (contract!=null){
				String blCampusId = contract.getBlCampusId();
				if (com.eduboss.utils.StringUtil.isNotBlank(blCampusId)){
					Organization contractCampus = organizationDao.findById(blCampusId);
					if (contractCampus!=null){
						incomeDistribution.setContractCampus(contractCampus);
					}
				}

			}
			incomeDistribution.setCreateUser(userService.getCurrentLoginUser());
            if (BonusDistributeType.USER.equals(incomeDistribution.getBaseBonusDistributeType())){
            	if (BonusDistributeType.USER_USER.equals(incomeDistribution.getSubBonusDistributeType())){
					if (incomeDistribution.getBonusStaff()==null){
						throw new ApplicationException("提成人业绩的业绩归属不能为空");
					}
					incomeDistribution.setBonusStaffCampus(userService.getBelongCampusByUserId(incomeDistribution.getBonusStaff().getUserId()));
					addBonusToMessage(incomeDistribution,messageList,"1");//删除增加
				}else {
            		if (incomeDistribution.getBonusOrg() == null){
						throw new ApplicationException("提成人业绩的业绩归属不能为空");
					}
				}

            }else if (BonusDistributeType.CAMPUS.equals(incomeDistribution.getBaseBonusDistributeType())){
                if (StringUtils.isBlank(incomeDistribution.getBonusOrg().getId())){
                    throw new ApplicationException("校区业绩的业绩归属不能为空");
                }
            }
			incomeDistributionDao.save(incomeDistribution);
		}

		/**
		 * 更新未分配业绩
		 */
		updateFundsChangeHistoryNotAssigned(fundsChangeHistoryId);


		/**
		 * 加入到队列中
		 */
		pushToQueue(messageList);
	}

	/**
	 * 检查是否超过合同分配的金额
	 * @param fundsChangeHistory
	 */
	private void checkIncomeOverArrange(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list) {

		String contractId = fundsChangeHistory.getContract().getId();
		String fundsChangeHistoryId = fundsChangeHistory.getId();
		Map map = getContractInfo(contractId, fundsChangeHistoryId);
		IncomeDistributeExceptThisfund incomeDistributeExceptThisfund = (IncomeDistributeExceptThisfund)map.get("incomeDistributeExceptThisfund");
		ContractEachProductTypeMoney contractInfo = (ContractEachProductTypeMoney)map.get("contractInfo");
		BigDecimal campusOne = BigDecimal.ZERO; //一对一
		BigDecimal userOne = BigDecimal.ZERO; //一对一

		BigDecimal campusSmall = BigDecimal.ZERO; //小班
		BigDecimal userSmall = BigDecimal.ZERO; //小班

		BigDecimal campusEcs = BigDecimal.ZERO;  //目标班
		BigDecimal userEcs = BigDecimal.ZERO;  //目标班

		BigDecimal campusOtm = BigDecimal.ZERO; //一对多
		BigDecimal userOtm = BigDecimal.ZERO; //一对多

		BigDecimal campusLecture = BigDecimal.ZERO; //讲座
		BigDecimal userLecture = BigDecimal.ZERO; //讲座

		BigDecimal campusOther = BigDecimal.ZERO; //其他
		BigDecimal userOther = BigDecimal.ZERO; //其他
		for (IncomeDistributionVo vo: list){
			if ("CAMPUS".equals(vo.getBaseBonusDistributeType())){
				switch (vo.getProductType()){
					case "ONE_ON_ONE_COURSE":
						campusOne = campusOne.add(vo.getAmount());
						break;
					case "SMALL_CLASS":
						campusSmall = campusSmall.add(vo.getAmount());
						break;
					case "ECS_CLASS":
						campusEcs = campusEcs.add(vo.getAmount());
						break;
					case "OTHERS":
						campusOther = campusOther.add(vo.getAmount());
						break;
					case "ONE_ON_MANY":
						campusOtm = campusOtm.add(vo.getAmount());
						break;
					case "LECTURE":
						campusLecture = campusLecture.add(vo.getAmount());
						break;
					default:
				}
			}else if ("USER".equals(vo.getBaseBonusDistributeType())){
				switch (vo.getProductType()){
					case "ONE_ON_ONE_COURSE":
						userOne = userOne.add(vo.getAmount());
						break;
					case "SMALL_CLASS":
						userSmall = userSmall.add(vo.getAmount());
						break;
					case "ECS_CLASS":
						userEcs = userEcs.add(vo.getAmount());
						break;
					case "OTHERS":
						userOther = userOther.add(vo.getAmount());
						break;
					case "ONE_ON_MANY":
						userOtm = userOtm.add(vo.getAmount());
						break;
					case "LECTURE":
						userLecture = userLecture.add(vo.getAmount());
						break;
					default:
				}
			}
		}

		if (contractInfo.isExitOneOnOne()){
			campusOne = campusOne.add(incomeDistributeExceptThisfund.getCampusExceptOneOnOne());
			if (campusOne.compareTo(contractInfo.getOneMoney())>0){
				throw new ApplicationException("一对一分配金额超过合同可分配资金");
			}
			userOne = userOne.add(incomeDistributeExceptThisfund.getUserExceptOneOnOne());
			if (userOne.compareTo(contractInfo.getOneMoney())>0){
				throw new ApplicationException("一对一分配金额超过合同可分配资金");
			}
		}
		if (contractInfo.isExitSmallClass()){
			campusSmall = campusSmall.add(incomeDistributeExceptThisfund.getCampusExceptSmallClass());
			if (campusSmall.compareTo(contractInfo.getMiniMoney())>0){
				throw new ApplicationException("小班分配金额超过合同可分配资金");
			}
			userSmall = userSmall.add(incomeDistributeExceptThisfund.getUserExceptSmallClass());
			if (userSmall.compareTo(contractInfo.getMiniMoney())>0){
				throw new ApplicationException("小班分配金额超过合同可分配资金");
			}
		}
		if (contractInfo.isExitEcsClass()){
			campusEcs = campusEcs.add(incomeDistributeExceptThisfund.getCampusExceptEcsClass());
			if (campusEcs.compareTo(contractInfo.getEcsMoney())>0){
				throw new ApplicationException("目标班分配金额超过合同可分配资金");
			}
			userEcs = userEcs.add(incomeDistributeExceptThisfund.getUserExceptEcsClass());
			if (userEcs.compareTo(contractInfo.getEcsMoney())>0){
				throw new ApplicationException("目标班分配金额超过合同可分配资金");
			}
		}
		if (contractInfo.isExitOneOnMany()){
			campusOtm = campusOtm.add(incomeDistributeExceptThisfund.getCampusExceptOneOnMany());
			if (campusOtm.compareTo(contractInfo.getOtmMoney())>0){
				throw new ApplicationException("一对多分配金额超过合同可分配资金");
			}
			userOtm = userOtm.add(incomeDistributeExceptThisfund.getUserExceptOneOnMany());
			if (userOtm.compareTo(contractInfo.getOtmMoney())>0){
				throw new ApplicationException("一对多分配金额超过合同可分配资金");
			}
		}
		if (contractInfo.isExitOthers()){
			campusOther = campusOther.add(incomeDistributeExceptThisfund.getCampusExceptOthers());
			if (campusOther.compareTo(contractInfo.getOtherMoney())>0){
				throw new ApplicationException("其他分配金额超过合同可分配资金");
			}
			userOther = userOther.add(incomeDistributeExceptThisfund.getUserExceptOthers());
			if (userOther.compareTo(contractInfo.getOtherMoney())>0){
				throw new ApplicationException("其他分配金额超过合同可分配资金");
			}
		}
		if (contractInfo.isExitLecture()){
			campusLecture = campusLecture.add(incomeDistributeExceptThisfund.getCampusExceptLecture());
			if (campusLecture.compareTo(contractInfo.getLectureMoney())>0){
				throw new ApplicationException("讲座分配金额超过合同可分配资金");
			}
			userLecture = userLecture.add(incomeDistributeExceptThisfund.getUserExceptLecture());
			if (userLecture.compareTo(contractInfo.getLectureMoney())>0){
				throw new ApplicationException("讲座分配金额超过合同可分配资金");
			}
		}

	}

	/**
	 * 添加到流水表
	 */
	@Override
	public void addToIncomeDistributeStatements(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list) {

		List<IncomeDistributeStatements> incomeDistributeStatementsList = incomeDistributeStatementsDao.getListOfLastIncomeDistributeStatements(fundsChangeHistory);
		Map<String, IncomeDistributeStatements> mapInDB = new HashMap<>();
		for (IncomeDistributeStatements i : incomeDistributeStatementsList){
			if (i.getBonusStaff()!=null){
				mapInDB.put(i.getBonusType().toString()+i.getBaseBonusDistributeType().toString()+i.getSubBonusDistributeType().toString()+i.getBonusStaff().getUserId()+i.getProductType().toString(), i);
			}else if (i.getBonusOrg()!=null){
				mapInDB.put(i.getBonusType().toString()+i.getBaseBonusDistributeType().toString()+i.getSubBonusDistributeType().toString()+i.getBonusOrg().getId()+i.getProductType().toString(), i);
			}
		}

		addIncomeFirst(fundsChangeHistory, list, mapInDB);

		addIncomeSecond(mapInDB);

	}

	/**
	 * 退费
	 * @param fundsChangeHistory
	 * @param list
	 */
	private void addFeedbackrefundIncomeDistributeStatements(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list) {

		List<IncomeDistributeStatements> incomeDistributeStatementsList = incomeDistributeStatementsDao.getFeedbackrefundIncome(fundsChangeHistory);
		Map<String, IncomeDistributeStatements> mapInDB = new HashMap<>();
		for (IncomeDistributeStatements i : incomeDistributeStatementsList){
			if (i.getBonusStaff()!=null){
				mapInDB.put(i.getBonusType().toString()+i.getBaseBonusDistributeType().toString()+i.getSubBonusDistributeType().toString()+i.getBonusStaff().getUserId(), i); //+i.getProductType().toString()
			}else if (i.getBonusOrg()!=null){
				mapInDB.put(i.getBonusType().toString()+i.getBaseBonusDistributeType().toString()+i.getSubBonusDistributeType().toString()+i.getBonusOrg().getId(), i); //+i.getProductType().toString()
			}
		}

		addIncomeFirst(fundsChangeHistory, list, mapInDB);

		addIncomeSecond(mapInDB);
	}



	@Override
	public DataPackage getListByFundsChangeHistoryId(DataPackage dp, String fundId) {
		dp = incomeDistributeStatementsDao.getListByFundsChangeHistoryId(dp, fundId);
		List<IncomeDistributeStatementsVo> incomeDistributeStatementsVos = HibernateUtils.voListMapping((List<IncomeDistributeStatements>)dp.getDatas(), IncomeDistributeStatementsVo.class);
		dp.setDatas(incomeDistributeStatementsVos);
		return dp;
	}


	@Override
	public DataPackage getStudentRechargeRecord(DataPackage dataPackage, ElectronicAccountChangeLogVo vo, TimeVo timeVo) {
		dataPackage = electronicAccountChangeLogDao.getStudentRechargeRecord(dataPackage, vo, timeVo);

		return dataPackage;
	}

	@Override
    public boolean checkHasLiveContractByStudent(String studentId) {
	    Map<String, Object> params = Maps.newHashMap();
	    String hql = " from ContractProduct where type = 'LIVE' and contract.student.id = :studentId ";
	    params.put("studentId", studentId);
	    List<ContractProduct> list = contractProductDao.findAllByHQL(hql, params);
	    if (list.size() > 0) {
	        return true;
	    }
	    return false;
	}

	private void addIncomeSecond(Map<String, IncomeDistributeStatements> mapInDB) {
		for (Map.Entry<String, IncomeDistributeStatements> entry : mapInDB.entrySet()){
			IncomeDistributeStatements deleteIncome = new IncomeDistributeStatements();
			IncomeDistributeStatements a = entry.getValue();
            BigDecimal b = BigDecimal.ZERO.subtract(a.getCurrentAmount());
            if (b.compareTo(BigDecimal.ZERO) == 0){
                continue;
            }else if (b.compareTo(BigDecimal.ZERO)>0){
                throw new ApplicationException("出错了，业绩提取的流水记录出错了");
            }
            deleteIncome.setAmount(b);
			deleteIncome.setCurrentAmount(BigDecimal.ZERO);
			deleteIncome.setOperation(IncomDistributeStatementsType.EXTRACT);
			deleteIncome.setBonusOrg(a.getBonusOrg());
			deleteIncome.setBonusStaff(a.getBonusStaff());
			deleteIncome.setBonusType(a.getBonusType());
			deleteIncome.setBaseBonusDistributeType(a.getBaseBonusDistributeType());
			deleteIncome.setSubBonusDistributeType(a.getSubBonusDistributeType());
			deleteIncome.setProductType(a.getProductType());
			deleteIncome.setFundsChangeTime(a.getFundsChangeTime());
			deleteIncome.setFundsChangeHistory(a.getFundsChangeHistory());
			deleteIncome.setCreateUser(userService.getCurrentLoginUser());
			incomeDistributeStatementsDao.save(deleteIncome);
		}
	}

	private void addIncomeFirst(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list, Map<String, IncomeDistributeStatements> mapInDB) {
		for (IncomeDistributionVo i : list){
			IncomeDistributeStatements incomeDistributeStatements = new IncomeDistributeStatements();
			IncomeDistribution incomeDistribution = HibernateUtils.voObjectMapping(i, IncomeDistribution.class);
			incomeDistributeStatements.setBonusType(incomeDistribution.getBonusType());
			incomeDistributeStatements.setBaseBonusDistributeType(incomeDistribution.getBaseBonusDistributeType());
			incomeDistributeStatements.setSubBonusDistributeType(incomeDistribution.getSubBonusDistributeType());
			incomeDistributeStatements.setProductType(incomeDistribution.getProductType());
			incomeDistributeStatements.setCreateUser(userService.getCurrentLoginUser());

			incomeDistributeStatements.setFundsChangeHistory(fundsChangeHistory);
			incomeDistributeStatements.setFundsChangeTime(fundsChangeHistory.getTransactionTime());
			String key = null;
			if (StringUtils.isNotBlank(i.getBonusStaffId())){
				key = i.getBonusType()+i.getBaseBonusDistributeType()+i.getSubBonusDistributeType()+i.getBonusStaffId()+i.getProductType();
				incomeDistributeStatements.setBonusStaff(incomeDistribution.getBonusStaff());

			}
			if (StringUtils.isNotBlank(i.getBonusOrgId())){
				key = i.getBonusType()+i.getBaseBonusDistributeType()+i.getSubBonusDistributeType()+i.getBonusOrgId()+i.getProductType();
				incomeDistributeStatements.setBonusOrg(incomeDistribution.getBonusOrg());

			}
			if (mapInDB.get(key) == null){
				incomeDistributeStatements.setOperation(IncomDistributeStatementsType.DISTRIBUTION);
				incomeDistributeStatements.setAmount(incomeDistribution.getAmount());
				incomeDistributeStatements.setCurrentAmount(incomeDistribution.getAmount());
				incomeDistributeStatementsDao.save(incomeDistributeStatements);
			}else {
				IncomeDistributeStatements incomeDB = mapInDB.get(key);
				BigDecimal currentAmount = incomeDB.getCurrentAmount();
				BigDecimal submitAmount = incomeDistribution.getAmount();
				if (submitAmount.compareTo(currentAmount)>0){
					incomeDistributeStatements.setOperation(IncomDistributeStatementsType.DISTRIBUTION);
					incomeDistributeStatements.setAmount(submitAmount.subtract(currentAmount));
					incomeDistributeStatements.setCurrentAmount(submitAmount);
				}else if (submitAmount.compareTo(currentAmount)<0){
					incomeDistributeStatements.setOperation(IncomDistributeStatementsType.EXTRACT);
					incomeDistributeStatements.setAmount(submitAmount.subtract(currentAmount));
					incomeDistributeStatements.setCurrentAmount(submitAmount);
				}else {
					mapInDB.remove(key);
					continue;
				}
				incomeDistributeStatementsDao.save(incomeDistributeStatements);
				mapInDB.remove(key);
			}
		}
	}


	private void checkIncomeDistribution(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list) {
		BigDecimal totalAmount = fundsChangeHistory.getTransactionAmount();
		BigDecimal washAmount = fundsChangeHistoryDao.getWashSumFundsByTransactionId(fundsChangeHistory.getTransactionId());
		totalAmount = totalAmount.subtract(washAmount);
		BigDecimal userTypeTotalAmount = BigDecimal.ZERO;
		BigDecimal orgTypeTotalAmount = BigDecimal.ZERO;
		for (IncomeDistributionVo incomeDistributionVo:list){
			if (BonusDistributeType.USER.getValue().equals(incomeDistributionVo.getBaseBonusDistributeType())){// StringUtils.isNotBlank(incomeDistributionVo.getBonusStaffId())
				userTypeTotalAmount = userTypeTotalAmount.add(incomeDistributionVo.getAmount());
			}else if (BonusDistributeType.CAMPUS.getValue().equals(incomeDistributionVo.getBaseBonusDistributeType())){ // StringUtils.isNotBlank(incomeDistributionVo.getBonusOrgId())
				orgTypeTotalAmount = orgTypeTotalAmount.add(incomeDistributionVo.getAmount());
			}
		}

		if (userTypeTotalAmount.compareTo(totalAmount)>0 || orgTypeTotalAmount.compareTo(totalAmount)>0){
			throw new ApplicationException("分配金额大于收款金额");
		}
	}



	
	

	/** 
	 * 保存各类型的产品业绩
	* @param type
	* @param campus
	* @param users
	* @param campusMoney
	* @param usersMoney
	* @param fundsChangeHistory
	* @param newMap
	* @author  author :Yao 
	* @date  2016年7月14日 下午2:47:41 
	* @version 1.0 
	*/
	public void saveBonusByProductType(ProductType type,String[] campus,String [] users,String[] campusMoney,String[] usersMoney,
			FundsChangeHistory fundsChangeHistory,Map<String,BigDecimal> newMap){
		Organization contractOrg=null;
		if (campus!=null){
			for (int i = 0;i<campus.length; i++){
				Organization schoolOrg=new Organization();
				
				if (campus[i]!=null){
				    schoolOrg = organizationDao.findById(campus[i]);
				}
				if (campus[0]!=null){
					contractOrg = organizationDao.findById(campus[0]);
				}
				saveNormalBonus(type, fundsChangeHistory,null,null,campusMoney[i],schoolOrg,contractOrg,null);
			}
		}

		if (users!=null){
			for (int i = 0;i<users.length;i++){
				Organization userCampus=new Organization();
				if (users[i]!=null){
					userCampus = userService.getBelongCampusByUserId(users[i]);
				}
				saveNormalBonus(type, fundsChangeHistory, usersMoney[i], users[i], null, null,contractOrg, userCampus);
				putMoneyToMap(newMap, users[i], new BigDecimal(usersMoney[i]));
			}
		}
	}
	
	/**
	 * 统计用户的当次总业绩
	 * @param map
	 * @param userId
	 * @param money
	 * @author  author :Yao
	 * @date  2016年7月14日 下午2:48:09
	 * @version 1.0
	 */
	public void putMoneyToMap(Map<String,BigDecimal> map ,String userId,BigDecimal money){
		if(map.get(userId)!=null){
			map.put(userId, money.add(map.get(userId)));
		}else{
			map.put(userId, money);
		}
	}

	public void checkBonusCanSave(BigDecimal campusMoney,BigDecimal userMoney,String[] campusAmount,String[] userAmount,BigDecimal oldcMoney,BigDecimal olduMoney){
		BigDecimal userM=BigDecimal.ZERO;
		BigDecimal campusM=BigDecimal.ZERO;
		if(campusAmount!=null) {
			for (int i = 0; i < campusAmount.length; i++) {
				campusM = campusM.add(new BigDecimal(campusAmount[i]));
			}
		}
		if(userAmount!=null) {
			for (int i = 0; i < userAmount.length; i++) {
				userM = userM.add(new BigDecimal(userAmount[i]));
			}
		}

		if(userM.compareTo(BigDecimal.ZERO)>0){
			userM=userM.subtract(olduMoney);
		}

		if(campusM.compareTo(BigDecimal.ZERO)>0){
			campusM=campusM.subtract(oldcMoney);
		}

		if(userM.compareTo(userMoney)>0){
			throw new ApplicationException("分配金额大于可分配金额，请刷新后重试！");
		}

		if(campusM.compareTo(campusMoney)>0){
			throw new ApplicationException("分配金额大于可分配金额，请刷新后重试！");
		}

	}

	/**
	 * 每次保存业绩需要更新收款记录待分配业绩字段(校区和个人)
	 */
	private void updateFundsChangeHistoryNotAssigned(String fundsChangeHistoryId) {
		FundsChangeHistory changeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		if (PayWay.ELECTRONIC_ACCOUNT.equals(changeHistory.getChannel())){
			//电子账号支付业绩不用分配
			changeHistory.setUserNotAssignedAmount(BigDecimal.ZERO);
			changeHistory.setCamNotAssignedAmount(BigDecimal.ZERO);
		}else {
		    changeHistory.setUserNotAssignedAmount(notAssignedAmountForFundsChange(fundsChangeHistoryId, BonusDistributeType.USER));//userNotAssignedAmountForFundsChange(fundsChangeHistoryId)
		    changeHistory.setCamNotAssignedAmount(notAssignedAmountForFundsChange(fundsChangeHistoryId, BonusDistributeType.CAMPUS));//camNotAssignedAmountForFundsChange(fundsChangeHistoryId)
		}
		fundsChangeHistoryDao.save(changeHistory);
	}



	/**
	 * 每次保存业绩需要更新收款记录待分配业绩字段(校区和个人)
	 */
	private void updateFundsChangeHistoryNotAssigned(FundsChangeHistory changeHistory) {
	    changeHistory.setUserNotAssignedAmount(userNotAssignedAmountForFundsChange(changeHistory));
	    changeHistory.setCamNotAssignedAmount(camNotAssignedAmountForFundsChange(changeHistory));
		fundsChangeHistoryDao.save(changeHistory);
	}
	
	/**
	 * 每次保存业绩需要更新收款记录待分配业绩字段(校区和个人)
	 */
	private void updateFundsChangeHistoryNotAssigned(String fundsChangeHistoryId,BigDecimal amount,String type) {
		FundsChangeHistory changeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		if (PayWay.ELECTRONIC_ACCOUNT.equals(changeHistory.getChannel())){
			//电子账号支付业绩不用分配
			changeHistory.setUserNotAssignedAmount(BigDecimal.ZERO);
			changeHistory.setCamNotAssignedAmount(BigDecimal.ZERO);
		}else if(StringUtils.isNotBlank(type) && "campus".equals(type)){
		    changeHistory.setCamNotAssignedAmount(changeHistory.getCamNotAssignedAmount().add(amount));
		}else if(StringUtils.isNotBlank(type) && "user".equals(type)){
			changeHistory.setUserNotAssignedAmount(changeHistory.getUserNotAssignedAmount().add(amount));
		}
		fundsChangeHistoryDao.save(changeHistory);
	}

	public void saveNormalBonus(ProductType type,FundsChangeHistory fundChangeHistory,String bonusAmount,String bonusStaff,String campusAmount,Organization organization,Organization contractOrg,Organization userCampus){
		ContractBonus bonus = new ContractBonus();
		bonus.setFundsChangeHistory(fundChangeHistory);
		bonus.setBonusType(BonusType.NORMAL);
		bonus.setType(type);
        if(StringUtils.isNotBlank(bonusAmount))
			bonus.setBonusAmount(new BigDecimal(bonusAmount));
		if(StringUtils.isNotBlank(bonusStaff))
			bonus.setBonusStaff(new User(bonusStaff));
		if(StringUtils.isNotBlank(campusAmount))
			bonus.setCampusAmount(new BigDecimal(campusAmount));
		if(organization!=null)
        	bonus.setOrganization(organization);
		if(contractOrg!=null)
			bonus.setContractCampus(contractOrg);
		if(userCampus!=null)
			bonus.setBonusStaffCampus(userCampus);
		contractBonusDao.save(bonus);


	}

	@Override
	public void saveIncomeDistributionForCloseContractProduct(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list, String studentReturnId, ContractProduct contractProduct) {
		List<Message> messageList=new ArrayList<>();

	    for (IncomeDistributionVo vo : list){
	        vo.setBonusType(BonusType.FEEDBACK_REFUND.getValue());
	        if ("CAMPUS_CAMPUS".equals(vo.getSubBonusDistributeType())){
				vo.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			}else {
				vo.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			}
        }

		/**
		 * 添加到流水表
		 */
		addToIncomeDistributeStatements(fundsChangeHistory, list);
		List<IncomeDistribution> bonuses = incomeDistributionDao.findByFundsChangeHistoryId(fundsChangeHistory.getId());
		for (IncomeDistribution i : bonuses){
			if (BonusDistributeType.USER_USER.equals(i.getSubBonusDistributeType())){
				addBonusToMessage(i,messageList,"1");//退费旧的要加
			}
			incomeDistributionDao.delete(i);
		}

		saveBonusMoney(list, studentReturnId, fundsChangeHistory, contractProduct, messageList);
		incomeDistributionDao.commit();
		//保存队列 退费
		pushToQueue(messageList);
	}

	@Override
	public Response saveContractAndLive(ContractVo contractVo) {
		// 判断是否为重复提交
		if (com.eduboss.utils.StringUtil.isNotBlank(contractVo.getTransactionUuid())) {
			transactionRecordDao.saveTransactionRecord(contractVo.getTransactionUuid());
		}
		// 保存客户和学生信息
		saveCusAndStuInfoInContract(contractVo);
		Student stu = studentDao.findById(contractVo.getStuId());
		updateStudentProductStatus(contractVo,stu);

		Set<ContractProductVo> normalPro=new HashSet<>();
		Set<ContractProductVo> livePro=new HashSet<>();
		for(ContractProductVo vo:contractVo.getContractProductVos()){
			if(vo.getProductType().equals(ProductType.LIVE)){
				livePro.add(vo);
			}else{
				normalPro.add(vo);
			}
		}
		ContractVo normalCon=contractVo;
		ContractVo liveCon=contractVo;
		Contract nContract=null;
		Contract lContract=null;
		StudentSchoolTemp schoolTemp=null;
		if(normalPro.size()>0){
			normalCon.setContractProductVos(normalPro);
			nContract=saveContract(normalCon,stu);
			schoolTemp=saveTempSchool(contractVo,stu,nContract);
		}

		if(livePro.size()>0){
			liveCon.setContractProductVos(livePro);
			lContract=saveContract(liveCon,stu);
			lContract.setSchoolOrTemp("school");
			if(schoolTemp!=null){
				stu.setSchoolTemp(schoolTemp);
				stu.setSchoolOrTemp("schoolTemp");
				lContract.setSchoolTemp(schoolTemp);
				lContract.setSchoolOrTemp("schoolTemp");
			}
		}

		//修改客户跟进校区信息
		if(StringUtils.isNotEmpty(contractVo.getBlCampusId())){
			Customer customer = customerDao.findById(contractVo.getCusId());
			customer.setBlSchool(contractVo.getBlCampusId());
			customer.setBlCampusId(organizationDao.findById(contractVo.getBlCampusId()));
			customerDao.save(customer);
		}

		return new Response();
	}


	public Contract saveContract(ContractVo contractVo,Student stu){
		Contract contract = HibernateUtils.voObjectMapping(contractVo, Contract.class);
		setContractBasicInfo_new(contract);
		saveContract(contract);
		contractDao.flush();

		// 生成不同的合同产品项目
		try {
			saveContractProductsForNewContract(contract, contractVo.getContractProductVos(), true);
		} catch (Exception e) {
			throw  new ApplicationException("创建产品报错");
		}


		// 更新合同的状态
		contractDao.getHibernateTemplate().refresh(contract);
		this.calculateContractDomain(contract);

		// 优惠资金插入
		BigDecimal promotionAmount = contract.getPromotionAmount();
		if(promotionAmount.compareTo(BigDecimal.ZERO)!=0) {
			insertOneFundRecord(contract, promotionAmount, contract.getStudent(), PayWay.PROMOTION_MONEY);
		}

		// 检查 合同缴费状态
		if(contract.getPendingAmount().compareTo(BigDecimal.ZERO) == 0) {
			contract.setPaidStatus(ContractPaidStatus.PAID);
		}else if(contract.getPaidAmount().compareTo(BigDecimal.ZERO)==0){//支付金额为0，设置为待付款
			contract.setPaidStatus(ContractPaidStatus.UNPAY);
		}else if (contract.getPendingAmount().compareTo(BigDecimal.ZERO) >0) {
			contract.setPaidStatus(ContractPaidStatus.PAYING);
		} else {
			throw new ApplicationException(ErrorCode.MONEY_ERROR);
		}



		ContractRecord cr=new ContractRecord(contract);
		cr.setUpdateType(UpdateType.NEW);
		cr.setCreateByStaff(userService.getCurrentLoginUser());
		contractRecordDao.save(cr);

		// 加入学生的记录信息 开始
		studentDynamicStatusService.createContractDynamicStatus(contract, "CREATE_NEW_CONTRACT" );

		//更新学生产品第一次时间
		studentService.updateStudentProductFirstTime(stu);
		return contract;
	}

	/**
	 * 检查是否有新学校进入保存
	 * @param contractVo
	 * @param stu
	 * @param contract
	 */
	public StudentSchoolTemp saveTempSchool(ContractVo contractVo,Student stu,Contract contract){
		if (contractVo.getVirtualSchool()!=null && StringUtils.isNotBlank(contractVo.getVirtualSchool().getVirtualSchoolName()) && contractVo.getVirtualSchool().getCity()!=null ){
			// TODO: 2016/10/8 保存新的 待审核学校
			StudentSchoolTemp studentSchoolTemp = new StudentSchoolTemp();
			VirtualSchool virtualSchool = contractVo.getVirtualSchool();
			studentSchoolTemp.setName(virtualSchool.getVirtualSchoolName());
			studentSchoolTemp.setStudentId(contractVo.getStuId());
//			studentSchoolTemp.setRegion(virtualSchool.getRegion());
			studentSchoolTemp.setCity(virtualSchool.getCity());
			studentSchoolTemp.setProvince(virtualSchool.getProvince()); //省份
			studentSchoolTemp.setContractId(contract.getId());
			studentSchoolTemp.setSchoolTempAuditStatus(SchoolTempAuditStatus.UNAUDIT);
			studentSchoolTemp.setCreateTime(DateTools.getCurrentDateTime());
			studentSchoolTemp.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			studentSchoolTempDao.save(studentSchoolTemp);
			stu.setSchoolTemp(studentSchoolTemp);
			stu.setSchoolOrTemp("schoolTemp");
			contract.setSchoolTemp(studentSchoolTemp);
			contract.setSchoolOrTemp("schoolTemp");
			return studentSchoolTemp;
		}else {
			contract.setSchoolOrTemp("school");
			stu.setSchoolOrTemp("school");
			return null;
		}
	}

	/**
	 * 更新学生产品上课状态
	 * @param contractVo
	 * @param stu
	 */
	public void updateStudentProductStatus(ContractVo contractVo,Student stu){

			Set<ContractProductVo> conps = contractVo.getContractProductVos();
			for (ContractProductVo cPVo : conps) {
				if(stu.getOneOnOneStatus()==null && cPVo.getProductType().equals(ProductType.ONE_ON_ONE_COURSE)){
					stu.setOneOnOneStatus(StudentOneOnOneStatus.NEW);
					studentDao.save(stu);
				}
				if(stu.getSmallClassStatus()==null && cPVo.getProductType().equals(ProductType.SMALL_CLASS)){
					stu.setSmallClassStatus(StudentSmallClassStatus.NEW);
					studentDao.save(stu);
				}
				if(stu.getOneOnManyStatus()==null && cPVo.getProductType().equals(ProductType.ONE_ON_MANY)){
					stu.setOneOnManyStatus(StudentOneOnManyStatus.NEW);
					studentDao.save(stu);
				}
		}
	}

	private void saveBonusMoney(List<IncomeDistributionVo> list, String studentReturnId, FundsChangeHistory fundsChangeHistory, ContractProduct contractProduct, List<Message> messages) {
		for (IncomeDistributionVo vo : list){
			IncomeDistribution incomeDistribution = new IncomeDistribution();
			incomeDistribution.setFundsChangeHistory(fundsChangeHistory);
			incomeDistribution.setAmount(vo.getAmount());
			incomeDistribution.setBonusType(BonusType.FEEDBACK_REFUND);
			if(StringUtils.isNotBlank(studentReturnId)){
				StudentReturnFee studentReturnFee=new StudentReturnFee();
				studentReturnFee.setId(studentReturnId);
				incomeDistribution.setStudentReturnFee(studentReturnFee);
			}
			if (contractProduct!=null){
				incomeDistribution.setProductType(contractProduct.getType());
				incomeDistribution.setContractCampus(organizationDao.findById(contractProduct.getContract().getStudent().getBlCampusId()));
			}
			if (!StringUtil.isBlank(vo.getBonusStaffId())){
//				newMap.put(vo.getBonusStaffId(), vo.getAmount());
				incomeDistribution.setBonusStaff(userService.getUserById(vo.getBonusStaffId()));
				incomeDistribution.setBaseBonusDistributeType(BonusDistributeType.USER);
				incomeDistribution.setSubBonusDistributeType(BonusDistributeType.USER_USER);
				incomeDistribution.setBonusStaffCampus(userService.getTongjiguishuxiaoqu(vo.getBonusStaffId()));
				addBonusToMessage(incomeDistribution,messages,"0");//退费则要扣除业绩金额
			}else if (StringUtils.isNotBlank(vo.getBonusOrgId())){
				Organization organization = organizationDao.findById(vo.getBonusOrgId());
				incomeDistribution.setBonusOrg(organization);
				incomeDistribution.setBonusStaffCampus(organization);
				incomeDistribution.setBaseBonusDistributeType(BonusDistributeType.valueOf(vo.getBaseBonusDistributeType()));
				incomeDistribution.setSubBonusDistributeType(BonusDistributeType.valueOf(vo.getSubBonusDistributeType()));
				incomeDistribution.setContractCampus(organization);
			}
			incomeDistributionDao.save(incomeDistribution);
		}
		incomeDistributionDao.commit();
	}

	//退费业绩保存
	@Override
	public void saveContractBonus(String fundChangeHistoryId,
								  String bonusType, String studentReturnId, ContractProduct contractProduct, RefundIncomeDistributeVo refundIncomeDistributeVo) {
//		contractBonusDao.deleteByFundsChangeHistoryId(fundChangeHistoryId);
		//删除原记录
//		List<ContractBonus> bonuses = contractBonusDao.findByFundsChangeHistoryId(fundChangeHistoryId);

		List<Message> messageList=new ArrayList<>();

		List<IncomeDistributionVo> list = getIncomeDistributionVos(refundIncomeDistributeVo);

		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundChangeHistoryId);

		for (IncomeDistributionVo vo : list){
			vo.setBonusType(BonusType.FEEDBACK_REFUND.getValue());
			if (contractProduct!=null){
				vo.setProductType(contractProduct.getType().getValue());
			}
			if ("CAMPUS_CAMPUS".equals(vo.getSubBonusDistributeType())){
				vo.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			}else {
				vo.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			}
		}

		/**
		 * 退费的流水记录
		 */
//		addToIncomeDistributeStatements(fundsChangeHistory, list);
		addFeedbackrefundIncomeDistributeStatements(fundsChangeHistory, list);

		List<IncomeDistribution> bonuses = incomeDistributionDao.findByFundsChangeHistoryId(fundChangeHistoryId);
		for (IncomeDistribution i : bonuses) {
			if(i.getSubBonusDistributeType() == BonusDistributeType.USER_USER){//用户业绩金额加入map
				addBonusToMessage(i,messageList,"1");//删除掉旧业绩，把当天的业绩增加回来。
			}
			incomeDistributionDao.delete(i);
		}

		saveBonusMoney(list, studentReturnId, fundsChangeHistory, contractProduct, messageList);

		incomeDistributionDao.commit();
		//保存队列 退费
		pushToQueue(messageList);
	}



	private void setInfoForIncomeDistribution(IncomeDistributionVo i1, String firstUserId, BonusDistributeType firstSubBonusDistributeTypePerson) {
		switch (firstSubBonusDistributeTypePerson){
			case USER_CAMPUS:
				i1.setBonusOrgId(firstUserId);
				i1.setSubBonusDistributeType(BonusDistributeType.USER_CAMPUS.getValue());
				break;
			case USER_BRANCH:
				i1.setBonusOrgId(firstUserId);
				i1.setSubBonusDistributeType(BonusDistributeType.USER_BRANCH.getValue());
				break;
			case USER_USER:
				i1.setBonusStaffId(firstUserId);
				i1.setSubBonusDistributeType(BonusDistributeType.USER_USER.getValue());
		}
	}

	private List<IncomeDistributionVo> getIncomeDistributionVos(RefundIncomeDistributeVo refundIncomeDistributeVo) {
		List<IncomeDistributionVo> list = new ArrayList<>();
//		String firstUserId = refundWorkflow.getFirstRefundDutyPerson() != null ? refundWorkflow.getFirstRefundDutyPerson() : null; //refundWorkflow.getFirstRefundDutyPerson().getUserId()

		if (StringUtils.isNotBlank(refundIncomeDistributeVo.getFirstRefundDutyPersonId())){
			IncomeDistributionVo i1 = new IncomeDistributionVo();
			String firstUserId = refundIncomeDistributeVo.getFirstRefundDutyPersonId();

			BonusDistributeType firstSubBonusDistributeTypePerson = refundIncomeDistributeVo.getFirstSubBonusDistributeTypePerson();
			i1.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i1.setAmount(refundIncomeDistributeVo.getFirstRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i1, firstUserId, firstSubBonusDistributeTypePerson);
			list.add(i1);
		}
		if (StringUtils.isNotBlank(refundIncomeDistributeVo.getSecondRefundDutyPersonId())){
			IncomeDistributionVo i2 = new IncomeDistributionVo();
			String secondUserId = refundIncomeDistributeVo.getSecondRefundDutyPersonId();

			BonusDistributeType secondSubBonusDistributeTypePerson = refundIncomeDistributeVo.getSecondSubBonusDistributeTypePerson();
			i2.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i2.setAmount(refundIncomeDistributeVo.getSecondRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i2, secondUserId, secondSubBonusDistributeTypePerson);
			list.add(i2);
		}
		if (StringUtils.isNotBlank(refundIncomeDistributeVo.getThirdRefundDutyPersonId())){
			IncomeDistributionVo i3 = new IncomeDistributionVo();
			String thirdUserId = refundIncomeDistributeVo.getThirdRefundDutyPersonId();

			BonusDistributeType thirdSubBonusDistributeTypePerson = refundIncomeDistributeVo.getThirdSubBonusDistributeTypePerson();
			i3.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i3.setAmount(refundIncomeDistributeVo.getThirdRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i3, thirdUserId, thirdSubBonusDistributeTypePerson);
			list.add(i3);
		}
		if (StringUtils.isNotBlank(refundIncomeDistributeVo.getFourthRefundDutyPersonId())){
			IncomeDistributionVo i4 = new IncomeDistributionVo();
			String fourthUserId = refundIncomeDistributeVo.getFourthRefundDutyPersonId();

			BonusDistributeType fourthSubBonusDistributeTypePerson = refundIncomeDistributeVo.getFourthSubBonusDistributeTypePerson();
			i4.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i4.setAmount(refundIncomeDistributeVo.getFourthRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i4, fourthUserId, fourthSubBonusDistributeTypePerson);
			list.add(i4);
		}
		if (StringUtils.isNotBlank(refundIncomeDistributeVo.getFifthRefundDutyPersonId())){
			IncomeDistributionVo i5 = new IncomeDistributionVo();
			String fifthUserId = refundIncomeDistributeVo.getFifthRefundDutyPersonId();

			BonusDistributeType fifthSubBonusDistributeTypePerson = refundIncomeDistributeVo.getFifthSubBonusDistributeTypePerson();
			i5.setBaseBonusDistributeType(BonusDistributeType.USER.getValue());
			i5.setAmount(refundIncomeDistributeVo.getFifthRefundDutyAmountPerson());
			setInfoForIncomeDistribution(i5, fifthUserId, fifthSubBonusDistributeTypePerson);
			list.add(i5);
		}

//		if (refundWorkflow.getSecondRefundDutyPerson() != null){
//			IncomeDistributionVo i2 = new IncomeDistributionVo();
//			String secondUserId = refundWorkflow.getSecondRefundDutyPerson();
//		}

		if (refundIncomeDistributeVo.getFirstRefundDutyCampusId()!=null){
			IncomeDistributionVo c1 = new IncomeDistributionVo();
			String firstOrganizationId = refundIncomeDistributeVo.getFirstRefundDutyCampusId();
			c1.setBonusOrgId(firstOrganizationId);
			c1.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c1.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c1.setAmount(refundIncomeDistributeVo.getFirstRefundDutyAmountCampus());
			list.add(c1);
		}
		if (refundIncomeDistributeVo.getSecondRefundDutyCampusId()!=null){
			IncomeDistributionVo c2 = new IncomeDistributionVo();
			String secondOrganizationId = refundIncomeDistributeVo.getSecondRefundDutyCampusId();
			c2.setBonusOrgId(secondOrganizationId);
			c2.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c2.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c2.setAmount(refundIncomeDistributeVo.getSecondRefundDutyAmountCampus());
			list.add(c2);
		}
		if (refundIncomeDistributeVo.getThirdRefundDutyCampusId()!=null){
			IncomeDistributionVo c3 = new IncomeDistributionVo();
			String thirdOrganizationId = refundIncomeDistributeVo.getThirdRefundDutyCampusId();
			c3.setBonusOrgId(thirdOrganizationId);
			c3.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c3.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c3.setAmount(refundIncomeDistributeVo.getThirdRefundDutyAmountCampus());
			list.add(c3);
		}

		if (refundIncomeDistributeVo.getFourthRefundDutyCampusId()!=null){
			IncomeDistributionVo c4 = new IncomeDistributionVo();
			String fourthOrganizationId = refundIncomeDistributeVo.getFourthRefundDutyCampusId();
			c4.setBonusOrgId(fourthOrganizationId);
			c4.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c4.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c4.setAmount(refundIncomeDistributeVo.getFourthRefundDutyAmountCampus());
			list.add(c4);
		}

		if (refundIncomeDistributeVo.getFifthRefundDutyCampusId()!=null){
			IncomeDistributionVo c5 = new IncomeDistributionVo();
			String fifthOrganizationId = refundIncomeDistributeVo.getFifthRefundDutyCampusId();
			c5.setBonusOrgId(fifthOrganizationId);
			c5.setBaseBonusDistributeType(BonusDistributeType.CAMPUS.getValue());
			c5.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS.getValue());
			c5.setAmount(refundIncomeDistributeVo.getFifthRefundDutyAmountCampus());
			list.add(c5);
		}


		return list;
	}



	
	/** 
	 * 保存个人业绩
	* @param userId
	* @param bonus
	* @param bonusType
	* @param studentReturnId
	* @param fundsChangeHistory
	* @param contractProduct
	* @author  author :Yao 
	* @date  2016年7月14日 下午4:25:13 
	* @version 1.0 
	*/
	public void saveUserBonusMoney(String userId,String bonus,String bonusType,
			String studentReturnId,FundsChangeHistory fundsChangeHistory,ContractProduct contractProduct,Map<String,BigDecimal> newMap){
		if(!StringUtil.isBlank(userId)){
			ContractBonus bonusItem = new ContractBonus();
			bonusItem.setFundsChangeHistory(fundsChangeHistory);
			if(StringUtils.isNotBlank(bonus)){
				bonusItem.setBonusAmount(new BigDecimal(bonus));
				newMap.put(userId, bonusItem.getBonusAmount());
			}
			if(StringUtils.isNotBlank(userId))
				bonusItem.setBonusStaff(new User(userId));
			if(StringUtils.isNotBlank(bonusType))
				bonusItem.setBonusType(BonusType.valueOf(bonusType));
			if(StringUtils.isNotBlank(studentReturnId)){//学生退费Id
				StudentReturnFee studentReturnFee=new StudentReturnFee();
				studentReturnFee.setId(studentReturnId);
				bonusItem.setStudentReturnFee(studentReturnFee);
			}
			if (contractProduct!=null){
				bonusItem.setType(contractProduct.getType());
				bonusItem.setContractCampus(organizationDao.findById(contractProduct.getContract().getStudent().getBlCampusId()));
			}
			bonusItem.setBonusStaffCampus(userService.getTongjiguishuxiaoqu(userId));
			contractBonusDao.save(bonusItem);
		}
	}
	
	/** 
	 * 保存校区业绩
	* @param school
	* @param bonus
	* @param bonusType
	* @param studentReturnId
	* @param fundsChangeHistory
	* @param contractProduct
	* @author  author :Yao 
	* @date  2016年7月14日 下午4:25:23 
	* @version 1.0 
	*/
	public void saveCampusBonusMoney(String school,String bonus,String bonusType,
			String studentReturnId,FundsChangeHistory fundsChangeHistory,ContractProduct contractProduct){
		if(StringUtils.isNotBlank(school))   {
			ContractBonus bonusItem = new ContractBonus();
			if(StringUtils.isNotBlank(bonus))
			bonusItem.setCampusAmount(new BigDecimal(bonus));
			bonusItem.setFundsChangeHistory(fundsChangeHistory);
            Organization schoolOrg = organizationDao.findById(school);
            if(schoolOrg != null){
                bonusItem.setOrganization(schoolOrg);
            }
            if(StringUtils.isNotBlank(bonusType))
            	bonusItem.setBonusType(BonusType.valueOf(bonusType));
            if(StringUtils.isNotBlank(studentReturnId)){//学生退费Id
				StudentReturnFee studentReturnFee=new StudentReturnFee();
				studentReturnFee.setId(studentReturnId);
				bonusItem.setStudentReturnFee(studentReturnFee);
			}
			if (contractProduct!=null){
				bonusItem.setType(contractProduct.getType());
				bonusItem.setContractCampus(organizationDao.findById(contractProduct.getContract().getStudent().getBlCampusId()));
			}

            contractBonusDao.save(bonusItem);
        }
	}
	
	@Override
	public BigDecimal getOneOnOnePrice(DataDict gradeDict) {
		return productDao.getOneOnOnePrice(gradeDict.getId());
	}

	@Override
	public List<Contract> getOrderContracts(Student student, Order order) {
		return contractDao.getOrderContracts(student.getId(), order);
	}

	@Override
	public ContractVo getContractCustomerInfo(String customerId,
			String studentId) {
		ContractVo conVo = new ContractVo();
		if(StringUtils.isNotBlank(customerId) && StringUtils.isNotBlank(studentId) ) {
			// 有customerId 和 studentId, 就会对 contractVo里面的里面的 cus 和 stu 进行赋值
			Customer cus = customerDao.findById(customerId);
			Student stu = studentDao.findById(studentId);
			conVo.setCusId(cus.getId());
			conVo.setCusName(cus.getName());
			conVo.setCusPhone(cus.getContact());
			conVo.setStuId(stu.getId());
			conVo.setStuName(stu.getName());
			conVo.setGradeId(stu.getGradeDict().getId());
			conVo.setStuGrade(stu.getGradeDict().getName());
			if(stu.getSchool()!=null){
				conVo.setSchoolId(stu.getSchool().getId());
				conVo.setSchoolName(stu.getSchool().getName());
			}
			conVo.setStudyManagerId(stu.getStudyManegerId());
		} else if(StringUtils.isNotBlank(customerId) && StringUtils.isBlank(studentId)){
			// 有customerId 和 无studentId, 就会对 contractVo 里面的里面的 cus 进行赋值， 并且取出 pending stu 进行赋值
			CustomerVo cusVo = customerService.findCustomerById(customerId);
			// get the pending student info
			conVo.setCusId(cusVo.getId());
			conVo.setCusName(cusVo.getName());
			conVo.setCusPhone(cusVo.getContact());

		} else if(StringUtils.isBlank(customerId) && StringUtils.isNotBlank(studentId)){
			// 无customerId 和 有studentId, 根据studentId 取出最新的一份合同 
			Student stu = studentDao.findById(studentId);
			Contract con = contractDao.getLastedContracts(studentId);
			if(con!=null) {
				conVo = HibernateUtils.voObjectMapping(con, ContractVo.class);
			}
			if(stu.getSchool()!=null){
				conVo.setSchoolId(stu.getSchool().getId());
				conVo.setSchoolName(stu.getSchool().getName());
			}
			conVo.setStudyManagerId(stu.getStudyManegerId());
		} else {
			// 无customerId 和 无studentId， 就当做普通合同的处理
		}
		conVo.setSignByWho(userService.getCurrentLoginUser().getName());
		conVo.setSignTime(DateTools.getCurrentDateTime());
		return conVo;
	}

	private ContractVo initContractVo(Customer cus, Student stu) {
		ContractVo conVo = new ContractVo();
		if(cus!=null) {
			conVo.setCusId(cus.getId());
			conVo.setCusName(cus.getName());
		}
		if(stu!=null) {
			conVo.setStuId(stu.getId());
			conVo.setStuName(stu.getName());
		}
		return conVo;
		
	}
	
	/**
	 * 保存在合同中的 学生 和 客户 信息
	 * @param contractVo
	 */
	private void saveCusAndStuInfoInContract(ContractVo contractVo) {
		
		if(StringUtils.isBlank(contractVo.getCusId()) && StringUtils.isBlank(contractVo.getStuId())) {
			//客户和学生信息 都不存在 ( id 不存在， name 存在 )，都会保存
			CustomerVo cus=  this.saveCustomerInfo(contractVo );
			contractVo.setCusId(cus.getId());
			StudentVo stu = (StudentVo) cus.getStudentVos().toArray()[0];		
			if(StringUtils.isNotBlank(contractVo.getSchoolId())){//合同学校不为空就同步到学生学校里面
				stu.setSchoolId(contractVo.getSchoolId());
			}




			contractVo.setStuId(stu.getId());
		} else if(StringUtils.isNotBlank(contractVo.getCusId()) && StringUtils.isBlank(contractVo.getStuId())) {
			//客户信息存在 学生信息不存在，都会保存
			String stuID = studentService.saveNewStudent(contractVo.getStuName(), contractVo.getGradeId(), contractVo.getCusId(), contractVo.getStudyManagerId(), contractVo.getSchoolId(),contractVo.getBlCampusId());
			contractVo.setStuId(stuID);
		} else if(StringUtils.isBlank(contractVo.getCusId()) && StringUtils.isNotBlank(contractVo.getStuId())) {
			//客户信息不存在 学生信息存在，有error
			CustomerVo cus=  this.saveCustomerInfo(contractVo );
			contractVo.setCusId(cus.getId());
			//throw new ApplicationException();
		}else if(StringUtils.isNotBlank(contractVo.getCusId()) && StringUtils.isNotBlank(contractVo.getStuId())){
			//客户和学生信息 都存在  更新学生学校信息
			Student stu = studentDao.findById(contractVo.getStuId());
			if(StringUtils.isNotBlank(contractVo.getSchoolId())){//合同学校不为空就同步到学生学校里面
				stu.setSchool(new StudentSchool(contractVo.getSchoolId()));				
			}
			//修改合同校区
			Customer  customer = customerDao.findById(contractVo.getCusId());
			String blCampusId = stu.getBlCampusId();
			if(customer!=null){
				if(CustomerActive.ACTIVE != customer.getCustomerActive()){
					//如果是僵尸客户
					if(StringUtils.isNotBlank(contractVo.getBlCampusId())){
						stu.setBlCampusId(contractVo.getBlCampusId());
						if(!contractVo.getBlCampusId().equals(blCampusId)){
							//学籍校区发生变更
							
							//排除新的主学籍校区是否在原来的副学籍校区里面
							StudentOrganization studentOrganization = studentOrganizationDao.findByStuIdandOrgId(stu.getId(),contractVo.getBlCampusId());
							if(studentOrganization!=null){
								studentOrganizationDao.delStudentOrganizationByStuIdandOrgId(stu.getId(), contractVo.getBlCampusId());
							}							
							studentOrganizationDao.saveStudentOrganization(stu);
							//TODO 调用学君接口 课时调整  修改未消耗完的合同产品科目的校区
							contractProductSubjectService.updateContractSubjectForTurnCampus(stu.getId(), contractVo.getBlCampusId());
						}
					}
					
					
					
					
					
					
				}
			}
			
			
			
			
			
			
			//新签合同
			if(stu.getStudentType()==StudentType.POTENTIAL){
				stu.setStudentType(StudentType.ENROLLED);
				stu.setStatus(StudentStatus.NEW);
				if (StringUtils.isNotEmpty(contractVo.getGradeId())) {
					DataDict gradeDict = dataDictDao.findById(contractVo.getGradeId());
					stu.setGradeDict(gradeDict);
					
				}
				if (StringUtils.isNotEmpty(contractVo.getStuName())) {
					stu.setName(contractVo.getStuName());
				}				
				stu.setBlCampusId(contractVo.getBlCampusId());
				if (StringUtils.isNotEmpty(contractVo.getStudyManagerId())) {
					stu.setStudyManegerId(contractVo.getStudyManagerId());
				}
				if (StringUtils.isNotEmpty(contractVo.getSchoolId())) {
					stu.setSchool(new StudentSchool(contractVo.getSchoolId()));
					stu.setSchoolOrTemp("school");
				}
				
			}
			studentDao.save(stu);
		} 
	}

	@Override
	public List<ContractBonusVo> getContractBonusByFundsChangeHistoryId(String fundsChangeHistoryId) {
		List<ContractBonus> bonusList = contractBonusDao.findByFundsChangeHistoryId(fundsChangeHistoryId);
		return HibernateUtils.voListMapping(bonusList, ContractBonusVo.class);
	}

	@Override
	public DataPackage findPageContractForTest(DataPackage dp,
			ContractVo contractVo, TimeVo timeVo) {
		StringBuffer hql = new StringBuffer();
		
		Map<String, Object> params  = Maps.newHashMap();
		
		params.put("userId", userService.getCurrentLoginUser().getUserId());
		
		hql.append( "from Contract as contract ");
		hql.append(" where 1 = 1 ");
		// 加上权限控制
		String loginUserRoleStr = userService.getCurrentLoginUser().getRoleCode();
		if(RoleCode.CONSULTOR.toString().equals(loginUserRoleStr) ) {
			// 如果咨询师 只看到自己的
			hql.append(" and signStaff.id = :userId ");
			
		} else if( RoleCode.STUDY_MANAGER.toString().equals(loginUserRoleStr) ) {
			// 如果 学管 只看到自己的
			hql.append(" and student.studyManegerId = :userId ");
		} else {
			//if (RoleCode.RECEPTIONIST.toString().equals(loginUserRoleStr) ||RoleCode.CAMPUS_DIRECTOR.toString().equals(loginUserRoleStr)||RoleCode.BREND_ADMIN.toString().equals(loginUserRoleStr)) {
			// 前台， 学区主任 只看到本校区
			Organization campus = userService.getBelongCampus();
			String orgLevel = campus.getOrgLevel();
			params.put("orgLevel", orgLevel+"%");
			hql.append(" and student.blCampusId in (select id from Organization where orgLevel like :orgLevel ");
		} 
//		else {
//			throw new ApplicationException("你的权限无法看到合同信息!");
//		}
		
		
		
		hql.append(" order by createTime desc");
		dp =  contractDao.findPageByHQL(hql.toString(), dp,true,params);
		

		List<Contract> contractList =  (List) dp.getDatas();
		for(Contract contract: contractList){
			System.out.println(contract.getStudent().getName() +"#" +contract.getId());
		}
		List contractVoList = HibernateUtils.voListMapping(contractList, ContractVo.class);
		dp.setDatas(contractVoList);
		return dp;
	}

	@Override
	public void saveEditFullContractInfo(ContractVo contractVo)  {
		// 加载合同
		Contract contract = contractDao.findById(contractVo.getContractId());
		// 新的合同产品
		Set<ContractProduct> destContractProducts = new HashSet<ContractProduct>();
		// 设置 oneOnOne 统一价格 和 赠送课时 的product ID
		for(ContractProductVo contractProductVo: contractVo.getContractProductVos()) {
			if(contractProductVo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_FREE) {
				contractProductVo.setProductId(productService.getFreeHourProduct(contractVo.getGradeId()).getId());
			} else if (contractProductVo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_NORMAL) {
				contractProductVo.setProductId(productService.getOneOnOneNormalProduct(contractVo.getGradeId()).getId());
			}
		}
		// 遍历合同产品，处理删除动作
		for(ContractProduct contractProduct : contract.getContractProducts()){
			boolean exists = false; // 产品是否存在于界面传回的vo集合中
			for(ContractProductVo vo: contractVo.getContractProductVos()) {
				if(contractProduct.getId().equals(vo.getId())){ // 数据库中小班产品存在于界面传回的产品列表中
					exists = true;
				}
			}
			if(!exists){ // 该合同产品在界面中被删除
                if(contractProduct.getType() == ProductType.ONE_ON_ONE_COURSE){ // 小班
                    if(contractProduct.getConsumeQuanity().compareTo(BigDecimal.ZERO) > 0){
                        throw new ApplicationException("已消费的一对一课程不允许删除");
                    }
                }else if(contractProduct.getType() == ProductType.SMALL_CLASS){ // 小班
					if(contractProduct.getStatus() == ContractProductStatus.NORMAL || contractProduct.getStatus() == null){ // 正常状态，允许删除
						smallClassService.deleteStudentByContract(contract); // 删除小班报名记录

					}else{
						throw new ApplicationException(ErrorCode.CANT_DEL_WHEN_CHARGED);
					}
				}else if(contractProduct.getType() == ProductType.OTHERS){ // 其他
					if(contractProduct.getStatus() == ContractProductStatus.ENDED){ // 已扣费，不能删
						throw new ApplicationException(ErrorCode.CANT_DEL_WHEN_CHARGED);
					}
				}
                contractDao.getHibernateTemplate().evict(contract);
        		contract = contractDao.findById(contractVo.getContractId());
				contract.setAvailableAmount(contract.getAvailableAmount().add(contractProduct.getPaidAmount())); // 资金回流至合同待分配
				contractDao.save(contract);
				contractProductDao.deleteById(contractProduct.getId()); // 删除该产品
				contractDao.flush();
			}
		}
		// 清除缓存，重新加载合同
		contractDao.getHibernateTemplate().evict(contract);
		contract = contractDao.findById(contractVo.getContractId());
		// 遍历界面传回的vo，处理新增和修改动作
		ContractProduct processingProduct;
		for(ContractProductVo contractProductVo: contractVo.getContractProductVos()) {
			// 初始化当前处理的合同产品
			processingProduct = null;
			// 表单预处理：设置 oneOnOne 统一价格 和 赠送课时 的product ID
			if(contractProductVo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_FREE) {
				contractProductVo.setProductId(productService.getFreeHourProduct(contractVo.getGradeId()).getId());
			} else if (contractProductVo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_NORMAL) {
				contractProductVo.setProductId(productService.getOneOnOneNormalProduct(contractVo.getGradeId()).getId());
			} else if (contractProductVo.getProductType() ==  ProductType.SMALL_CLASS) {
				// 表单预处理：小班报读课时不能大于产品课时
				Product prd = productDao.findById(contractProductVo.getProductId());
				if(BigDecimal.valueOf(contractProductVo.getQuantity()).compareTo(prd.getMiniClassTotalhours()) > 0){
					throw new ApplicationException("小班报读课时不能大于产品课时");
				}
			}
			// 处理单个合同产品的新增和修改
			ContractProduct voMapping = HibernateUtils.voObjectMapping(contractProductVo, ContractProduct.class);
			if(StringUtils.isBlank(contractProductVo.getId())){ // 新增合同产品
				processingProduct = voMapping;
				processingProduct.setId(null);
				processingProduct.setStatus(ContractProductStatus.NORMAL);
				processingProduct.setPaidStatus(ContractProductPaidStatus.UNPAY);
				processingProduct.setPaidAmount(BigDecimal.ZERO);
				processingProduct.setConsumeAmount(BigDecimal.ZERO);
				processingProduct.setConsumeQuanity(BigDecimal.ZERO);
			}else{ // 修改合同产品
				boolean setNewVal = true;
				processingProduct =  getContractProductByContractList(contract, contractProductVo.getId());
				// 哪些情况下已有合同产品不允许修改
				if(processingProduct.getType() == ProductType.ONE_ON_ONE_COURSE_NORMAL){ // 一对一
					if(voMapping.getQuantity().compareTo(processingProduct.getConsumeQuanity()) < 0){
						throw new ApplicationException(ErrorCode.ONE_ON_ONE_TOTAL_LESS_CONSUME); // 总数小于已消费
					}
				}else if(processingProduct.getType() == ProductType.ONE_ON_ONE_COURSE_FREE){ // 一对一赠送
					if(voMapping.getQuantity().compareTo(processingProduct.getConsumeQuanity()) < 0){
						throw new ApplicationException(ErrorCode.FREE_TOTAL_LESS_CONSUME); // 总数小于已消费
					}
				}else if(processingProduct.getType() == ProductType.SMALL_CLASS){ // 小班
					if(processingProduct.getStatus() != ContractProductStatus.NORMAL){ // 已预扣费
						setNewVal = false;
//						throw new ApplicationException("已预扣费的小班课程不允许修改");
					}
				}else if(processingProduct.getType() == ProductType.OTHERS){ // 其他
					if(processingProduct.getStatus() == ContractProductStatus.ENDED){ // 已扣费
						setNewVal = false;
//						throw new ApplicationException("已扣费的其他收费不允许修改");
					}
				}
				if(setNewVal){
					processingProduct.setPlanAmount(voMapping.getPlanAmount());
					processingProduct.setDealDiscount(voMapping.getDealDiscount());
					processingProduct.setQuantity(voMapping.getQuantity());
					processingProduct.setProduct(voMapping.getProduct());
				}
			}
			processingProduct.setContract(contract);
			// 计算当前产品的钱并push到新的合同产品set
			this.calSingleContractProduct(processingProduct); 
			destContractProducts.add(processingProduct);
		}
		
		// 重设一对一json
		contract.setOneOnOneJson(contractVo.getOneOnOneJson()); 
		// 重新计算合同总价
		contract.setContractProducts(destContractProducts);
		
		this.calContractPlanAmount(contract);
		
		if (ContractType.INIT_CONTRACT.equals(contract.getContractType())) {
        	saveFundOfContract(new FundsChangeHistoryVo(contract.getId(), contract.getPendingAmount().doubleValue()));
        }

        // 更新Student acc view , 更新合同总金额
        updateStudentAccMv(contract.getStudent());
	}
	
	@Override
	public void saveInitContractInfo(ContractVo contractVo) throws Exception {

        User user = userService.getCurrentLoginUser();

		saveCusAndStuInfoInContract(contractVo);
		
		Contract contract = setContractBasicInfo(contractVo);
		
		// 设置 oneOnOne 统一价格 和 赠送课时 的product ID
		for(ContractProductVo vo: contractVo.getContractProductVos()) {
			if(vo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_FREE) {
				vo.setProductId(productService.getFreeHourProduct(contractVo.getGradeId()).getId());
			} else if (vo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_NORMAL) {
				vo.setProductId(productService.getOneOnOneNormalProduct(contractVo.getGradeId()).getId());
			}  else if (vo.getProductType() ==  ProductType.SMALL_CLASS) {
				// 表单预处理：小班报读课时不能大于产品课时
				Product prd = productDao.findById(vo.getProductId());
				if(BigDecimal.valueOf(vo.getQuantity()).compareTo(prd.getMiniClassTotalhours()) > 0){
					throw new ApplicationException("小班报读课时不能大于产品课时");
				}
			}
		}

		//	合同产品的保存, 其他， 小班 和 一对一的统一课程 和 一对一赠送课程
        Map<ContractProduct,String> addForMiniClassMap = new HashMap<ContractProduct, String>();
        Set<ContractProduct> conProducts = new HashSet<ContractProduct>();
        Iterator<ContractProductVo> contractProductVoIterator = contractVo.getContractProductVos().iterator();
        while(contractProductVoIterator.hasNext()){
            ContractProductVo vo = contractProductVoIterator.next();
            ContractProduct conPro = HibernateUtils.voObjectMapping(vo,ContractProduct.class);
            conPro.setContract(contract);
            this.calSingleContractProduct(conPro);
            conProducts.add(conPro);
            if (vo.getProductType() ==  ProductType.SMALL_CLASS && StringUtils.isNotBlank(vo.getMiniClassId())) {
                addForMiniClassMap.put(conPro,vo.getMiniClassId() + "," + vo.getFirstSchoolTime());
            }
        }
//        Set<ContractProduct> conProducts = HibernateUtils.voSetMapping(contractVo.getContractProductVos(), ContractProduct.class);
//		for(ContractProduct conPro: conProducts) {
//			conPro.setContract(contract);
//			this.calSingleContractProduct(conPro);
//		}
		contract.setContractProducts(conProducts);
		
		//contract.setContractProducts(conProducts);
		contractDao.save(contract);
		contractDao.flush();
		contractDao.getCurrentSession().refresh(contract);
		
		// 计算单价 从一对一的其中任意一个产品中获取单价
		// 统一价格 已经放在统一价格产品里面
//		BigDecimal price = this.getOneOnOnePrice(contract.getStudent().getGradeDict());
//		contract.setOneOnOneUnitPrice(price);
		
		// 	计算全款合同的总钱数
		this.calContractPlanAmount(contract);
		
		// 计算合同的待收款
		contract.setPendingAmount(contract.getTotalAmount());
		
		//修改客户处理状态
		Customer customer = customerDao.findById(contract.getCustomer().getId());
		customer.setDealStatus(CustomerDealStatus.SIGNEUP);
        customer.setDeliverType(CustomerDeliverType.PERSONAL_POOL);
        customer.setDeliverTarget(user.getUserId());
        customer.setDeliverTargetName(user.getName());
		customerDao.save(customer);
		
		if (ContractType.INIT_CONTRACT.equals(contract.getContractType())) {
        	saveFundOfContract(new FundsChangeHistoryVo(contract.getId(), contract.getPendingAmount().doubleValue()));
        }
		
		// 更新Student acc view , 更新合同总金额
//		Student student =  contract.getStudent();
//		StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
//		if(studnetAccMv == null) {
//			studnetAccMv =  new StudnetAccMv(student);
//			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
//			studnetAccMvDao.flush();
//		} 
//		studnetAccMv.setTotalAmount(studnetAccMv.getTotalAmount().add(contract.getTotalAmount()));
		updateStudentAccMv(contract.getStudent());

        // 报小班
        for(ContractProduct conPro : addForMiniClassMap.keySet()){
        	String miniClassId = addForMiniClassMap.get(conPro).split(",")[0];
        	String firstCourseDate = addForMiniClassMap.get(conPro).split(",")[1];
            smallClassService.AddStudentForMiniClasss(contractVo.getStuId(),miniClassId,contract.getId(),conPro.getId(),firstCourseDate, true);
        }
        
        //通知学管主管分配学生     关闭dwr   2016-12-17
//        List<User> studentManagerHeads = userService.getUserByRoldCodes(RoleCode.STUDY_MANAGER_HEAD.getValue());
//        if (studentManagerHeads != null && studentManagerHeads.size() > 0) {
//        	for (User studentManagerHead : studentManagerHeads) {
//        		if(contract.getContractType() == ContractType.NEW_CONTRACT)
//        			messageService.sendMessage(MessageType.SYSTEM_MSG, "新签合同学生录入", "新签合同的"+contractVo.getStuName()+"学生 录入系统，请及时分配给学管", MessageDeliverType.SINGLE, studentManagerHead.getUserId());
//        	}
//        }
		
        
	
	}
	
	@Override
	public void saveEditInitContractInfo(ContractVo contractVo)  {
		// 判断是否为重复提交
		if (com.eduboss.utils.StringUtil.isNotBlank(contractVo.getTransactionUuid())) {
			transactionRecordDao.saveTransactionRecord(contractVo.getTransactionUuid());
		}
		
		// 删除合同产品
		Contract contract = contractDao.findById(contractVo.getContractId());
		
		if(contract.getContractStatus() == ContractStatus.UNVALID){
			throw new ApplicationException("关闭合同不能修改！");
		}
		
		Student student = contract.getStudent();
		smallClassService.deleteStudentByContract(contract); // 删除小班报名记录
//		for(ContractProduct contractProduct : contract.getContractProducts()){
//			contractProductDao.deleteById(contractProduct.getId()); // 删除该产品
//			//contractDao.flush();
//		}
		// 删除合同
		// 清除缓存，重新加载合同
		contractDao.delete(contract);
		contractDao.flush();
		// 删除学生账户   这里有问题：不能删除
		StudnetAccMv oldStuAcc = studnetAccMvDao.findById(student.getId());
		studnetAccMvDao.delete(oldStuAcc);
		studnetAccMvDao.flush();
		// 添加合同
		// 更新合同产品
		
		// 新建合同， 将合同ID设置为NULL
		contractVo.setContractId(null);
		for(ContractProductVo conPrdVo : contractVo.getContractProductVos()) {
			conPrdVo.setContractId(null);
			conPrdVo.setId(null);
		}
		
		try {
			this.saveFullContractInfoNew(contractVo);
		} catch(ApplicationException ae) {
			throw ae;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("初始合同修改出错了，亲");
		}
		
	}
	
	
	private void updateStudentAccMv(Student student) {
		StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
        if(studnetAccMv == null) {
            studnetAccMv =  new StudnetAccMv(student);
            studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
            studnetAccMvDao.flush();
        }
        studnetAccMv.setTotalAmount(BigDecimal.ZERO);
        List<ContractVo> contractList = this.getContractByStudentId(student.getId());
        for(ContractVo cv : contractList){
            studnetAccMv.setTotalAmount(studnetAccMv.getTotalAmount().add(cv.getTotalAmount()));
        }
		
	}

	private ContractProduct getContractProductByContractList(Contract contract,String contractProductId ){
		if(contract!=null && contract.getContractProducts()!=null){
			for(ContractProduct cp : contract.getContractProducts()){
				if(cp.getId().equals(contractProductId)){
					return cp;
				}
			}
		}
		return null;
	}
	
	@Override
	public void calSingleContractProduct(ContractProduct contractProduct) {
		Product product = productDao.findById(contractProduct.getProduct().getId());
		// 设置单价,其他的折扣是1
		if(product.getCategory() == ProductType.OTHERS) {
			contractProduct.setPrice(contractProduct.getPlanAmount());
			contractProduct.setDealDiscount(BigDecimal.ONE);
		} else if (!ContractType.INIT_CONTRACT.equals(contractProduct.getContract().getContractType())) {
			contractProduct.setPrice(product.getPrice());
		}
		// 默认是没有折扣的（100%）, 小班是获取自身的 Discount
		BigDecimal discount = BigDecimal.ONE;
		if(product.getCategory() == ProductType.SMALL_CLASS) {
			discount = contractProduct.getDealDiscount();
		} else {
			discount = product.getDiscount()==null? BigDecimal.ONE: product.getDiscount();
		}
		
		contractProduct.setDealDiscount(discount);
		// 设置quality
		BigDecimal quantity = contractProduct.getQuantity()==null? BigDecimal.ONE: contractProduct.getQuantity();
		contractProduct.setQuantity(quantity);
		// 一对一项目收费 =  原有的收费 + 产品的收费
		if (contractProduct.getPrice()==null) contractProduct.setPrice(BigDecimal.ZERO);
		BigDecimal money = contractProduct.getPrice().multiply(discount.multiply(quantity));
		contractProduct.setPlanAmount(money);
	}

	@Override
	public void calContractPlanAmount(Contract contract) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal oneTotalAmount = BigDecimal.ZERO;
		BigDecimal smallTotalAmount = BigDecimal.ZERO;
		BigDecimal otherTotalAmount = BigDecimal.ZERO;
		BigDecimal oneOnManyTotalAmount=BigDecimal.ZERO;
		for(ContractProduct conPro : contract.getContractProducts()) {
			totalAmount = totalAmount.add(conPro.getTotalAmount());
			if(conPro.getProduct().getCategory() == ProductType.ONE_ON_ONE_COURSE_NORMAL) {
				oneTotalAmount = oneTotalAmount.add(conPro.getTotalAmount());
			} else if(conPro.getProduct().getCategory() == ProductType.SMALL_CLASS) {
				smallTotalAmount = smallTotalAmount.add(conPro.getTotalAmount());
			} else if(conPro.getProduct().getCategory() == ProductType.OTHERS) {
				otherTotalAmount = otherTotalAmount.add(conPro.getTotalAmount());
			} else if(conPro.getProduct().getCategory() == ProductType.ONE_ON_MANY){
				oneOnManyTotalAmount=oneOnManyTotalAmount.add(conPro.getTotalAmount());
			}
				
		}
		
		if(totalAmount.compareTo(contract.getPaidAmount()) < 0){
			throw new ApplicationException(ErrorCode.CONTRACT_TOTAL_LESS_PAID); // 合同金额小于已付款
		}else{
			if (totalAmount.compareTo(contract.getPaidAmount()) == 0) {
				contract.setPaidStatus(ContractPaidStatus.PAID); // 合同金额等已付，设置为已付款
			}else if(contract.getPaidAmount().compareTo(BigDecimal.ZERO)==0){
				contract.setPaidStatus(ContractPaidStatus.UNPAY); // 付款金额等于0，设置为待款中
			}else if(totalAmount.compareTo(contract.getPaidAmount()) > 0){
				contract.setPaidStatus(ContractPaidStatus.PAYING); // 合同金额大于已付，设置为付款中
			}  
			// 检查合同产品已分配金额是否大于产品总额
			for(ContractProduct contractProduct : contract.getContractProducts()) {
				// 若已分配金额大于产品总额，则抽取差值回流至合同待分配金额里
				if(contractProduct.getPaidAmount().compareTo(contractProduct.getPlanAmount()) > 0){
					BigDecimal diff = contractProduct.getPaidAmount().subtract(contractProduct.getPlanAmount());
					contractProduct.setPaidAmount(contractProduct.getPaidAmount().subtract(diff));
					contract.setAvailableAmount(contract.getAvailableAmount().add(diff));
				}
			}
		}
		contract.setPendingAmount(totalAmount.subtract(contract.getPaidAmount()));
		contract.setTotalAmount(totalAmount);
		contract.setOneOnOneTotalAmount(oneTotalAmount);
		contract.setMiniClassTotalAmount(smallTotalAmount);
		contract.setOtherTotalAmount(otherTotalAmount);
		contract.setOneOnManyTotalAmount(oneOnManyTotalAmount);
        if(totalAmount.equals(BigDecimal.ZERO)){
            throw new ApplicationException("合同总价不能等于0");
        }
	}

	@Override
	public DataPackage findContractByStu(DataPackage dp,
			String studentId) {
		Map<String, Object> params  = Maps.newHashMap();
		params.put("studentId", studentId);
		StringBuffer hql = new StringBuffer();
		hql.append( "from Contract as contract where student.id= :studentId ");		
		hql.append(" order by createTime desc");
		
		dp =  contractDao.findPageByHQL(hql.toString(), dp,true,params);
		

		List<Contract> contractList =  (List) dp.getDatas();
		List contractVoList = new ArrayList();
		for(Contract contract : contractList){
			this.calculateContractDomain(contract);
			//dozer在Set2Set深层次转换那里出现了问题， 手动自己转一次。 
			ContractVo contractVo =  HibernateUtils.voObjectMapping(contract, ContractVo.class);
			contractVoList.add(contractVo);
		}
		dp.setDatas(contractVoList);
		return dp;
	
	}

	public DataPackage findContractByCus(DataPackage dp,
			String customerId) {

		Map<String, Object> params  = Maps.newHashMap();
		params.put("customerId", customerId);
		StringBuffer hql = new StringBuffer();	
		hql.append( "from Contract as contract where student.id in (select student.id from CustomerStudent where customer.id= :customerId )");
		hql.append(" order by createTime desc");
		
		dp =  contractDao.findPageByHQL(hql.toString(), dp,true,params);
		

		List<Contract> contractList =  (List) dp.getDatas();
		List contractVoList = new ArrayList();
		for(Contract contract : contractList){
			this.calculateContractDomain(contract);
			//dozer在Set2Set深层次转换那里出现了问题， 手动自己转一次。 
			ContractVo contractVo =  HibernateUtils.voObjectMapping(contract, ContractVo.class);
			if(contractVo.getBlCampusId()!=null){
				Organization organization = organizationDao.findById(contractVo.getBlCampusId());
				contractVo.setBlCampusName(organization.getName());
			}else{
				contractVo.setBlCampusName("");
			}
			
			contractVoList.add(contractVo);
		}
		dp.setDatas(contractVoList);
		return dp;
	
	}

	@Override 
	public void addArrangeMoneyForProduct(int oneArrangeMoney,
			String oneConPrdId) {
		ContractProduct conPrd = contractProductDao.findById(oneConPrdId);
		conPrd.setPaidAmount(conPrd.getPaidAmount().add(BigDecimal.valueOf(oneArrangeMoney)));
		contractProductDao.save(conPrd);
		contractProductDao.flush();
		// 这里有个trigger， 会相应地扣除合同的 available Money
		// 在AOP 里面实现了  Trigger和AOP都已经去除了
		
		// 检查合同产品的状态
		if(conPrd.getPaidAmount().compareTo(conPrd.getRealAmount())==0) {
			conPrd.setPaidStatus(ContractProductPaidStatus.PAID);
		}else if(conPrd.getPaidAmount().compareTo(BigDecimal.ZERO)==0){
			conPrd.setPaidStatus(ContractProductPaidStatus.UNPAY);//如果支付金额为0 则是待支付
		}else if(conPrd.getPaidAmount().compareTo(conPrd.getRealAmount()) != 0) {
			conPrd.setPaidStatus(ContractProductPaidStatus.PAYING);
		} 
		
		// 把合同的可分配资金 进行调整
		Contract contract =  conPrd.getContract();
		contract.setAvailableAmount(contract.getAvailableAmount().subtract(new BigDecimal(oneArrangeMoney))); 
		
		// 更新Student view , 更新不同产品的已付费的情况
		Student student =  conPrd.getContract().getStudent();
		StudnetAccMv studnetAccMv = studnetAccMvDao.findById(student.getId());
		if(studnetAccMv == null) {
			studnetAccMv =  new StudnetAccMv(student);
			studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
			studnetAccMvDao.flush();
		} 
		if(conPrd.getType() == ProductType.ONE_ON_ONE_COURSE_NORMAL) {
			studnetAccMv.setOneOnOnePaidAmount(studnetAccMv.getOneOnOnePaidAmount().add(BigDecimal.valueOf(oneArrangeMoney)));
			studnetAccMv.setOneOnOneRemainingHour(studnetAccMv.getOneOnOneRemainingHour().add( BigDecimal.valueOf(oneArrangeMoney/conPrd.getPrice().doubleValue())  ) );
		} else if(conPrd.getType() == ProductType.ONE_ON_ONE_COURSE_FREE) {
			studnetAccMv.setOneOnOneFreePaidHour(studnetAccMv.getOneOnOneFreePaidHour().add(conPrd.getQuantity()));
		} else if(conPrd.getType() == ProductType.SMALL_CLASS) {
			studnetAccMv.setMiniPaidAmount(studnetAccMv.getMiniPaidAmount().add(BigDecimal.valueOf(oneArrangeMoney)));
		} else if(conPrd.getType() == ProductType.OTHERS) {
			studnetAccMv.setOtherPaidAmount(studnetAccMv.getOtherPaidAmount().add(BigDecimal.valueOf(oneArrangeMoney)));
		}  
	}
	
	@Override
	public void saveDefaultArrangeMonoyForProduct(ContractProduct conPrd) {
//		this.addArrangeMoneyForProduct(conPrd.getPlanAmount().intValue() - conPrd.getPaidAmount().intValue() , conPrd.getId());
		//  有可能读了 之前的一对一统计单价的东西， 所以要读合同产品的产品类型
		ContractProductHandler prodHandler = this.selectHandlerByType(conPrd.getType());
		prodHandler.assignMoney(conPrd.getContract(), conPrd, conPrd.getRealAmount().subtract(conPrd.getPaidAmount()));
	}
	
	@Override
	public void saveDefaultArrangeMonoyForProduct(ContractProduct conPrd,String userId) {
		ContractProductHandler prodHandler = this.selectHandlerByType(conPrd.getType());
		prodHandler.assignMoney(conPrd.getContract(), conPrd, conPrd.getRealAmount().subtract(conPrd.getPaidAmount()),userId);
	}
	
	

	@Override
	public List<ContractProduct> getOneContractProducts(Student student) {
		List<ContractProduct> contractProducts = contractProductDao.getOneContractProducts(student);
		return null;
	}
	

	@Override
	public int countContractByProductId(String productId) {
		return contractProductDao.countByProductId(productId);
	}

	@Override
	public ContractVo findInitContractByStudentId(String studentId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("student.id", studentId));
		criterionList.add(Expression.eq("contractType", ContractType.INIT_CONTRACT));
		 List<Contract> initContractList =  contractDao.findAllByCriteria(criterionList);
		 ContractVo initContractVo = null;
		 if (initContractList.size() > 0) {
			 initContractVo = HibernateUtils.voObjectMapping(initContractList.get(0), ContractVo.class);
		 } /*else {
			 throw new ApplicationException("该学生没有初始合同。");
		 }*/
		return initContractVo;
	}

	/**
	 * 同步直播合同
	 * @param contractVo
	 * @return
	 */
	@Override
	public String synchronizeLiveContract(ContractLiveVo contractVo) throws Exception  {
		// 判断是否为重复提交
//		if (com.eduboss.utils.StringUtil.isNotBlank(contractVo.getOrderNum())) {//直播订单号
//			transactionRecordDao.saveTransactionRecord(contractVo.getOrderNum());
//		}

        Contract contract = HibernateUtils.voObjectMapping(contractVo, Contract.class, "contractLiveVo");
        contract.setCurriculumId(contractVo.getOrderNum());

        contract.setContractType(contract.getContractType());


        contract.setCreateTime(contractVo.getSignTime());
        String userId = contractVo.getSignByWho();

        contract.setGradeDict(new DataDict(contractVo.getGradeId()));

        User currentLoginUser = userService.findUserById(userId);

        contract.setCreateByStaff(currentLoginUser);
        contract.setSignStaff(currentLoginUser);
        contract.setModifyByStaff(currentLoginUser);
        contract.setModifyTime(DateTools.getCurrentDateTime());
        if(StringUtils.isBlank(contract.getBlCampusId())){
            Organization blCampus = userService.getBelongCampusByUserId(userId);
            if (blCampus!=null){
                contract.setBlCampusId(blCampus.getId());
            }

        }
        contract.setSchoolOrTemp("school");


        contractDao.save(contract);
        contractDao.flush();

        //先检查产品是否存在
        contractVo = checkProduct(contractVo);

        // 生成不同的合同产品项目
        saveContractProductsForLiveContract(contract, contractVo.getContractProductVos());

        // 更新合同的状态
        contractDao.getHibernateTemplate().refresh(contract);
//      this.calculateContractDomain(contract);

        calculateContractDomainForLive(contract);


        // 优惠资金插入
        BigDecimal promotionAmount = contract.getPromotionAmount();
        if(promotionAmount.compareTo(BigDecimal.ZERO)!=0) {
            insertOneFundRecord(contract, promotionAmount, contract.getStudent(), PayWay.PROMOTION_MONEY);
        }

        // 检查 合同缴费状态
        int flag = contract.getPendingAmount().compareTo(BigDecimal.ZERO);
        if(flag == 0) {
            contract.setPaidStatus(ContractPaidStatus.PAID);
        }else if(contract.getPaidAmount().compareTo(BigDecimal.ZERO)==0){//支付金额为0，设置为待付款
            contract.setPaidStatus(ContractPaidStatus.UNPAY);
        }else if (flag >0) {
            contract.setPaidStatus(ContractPaidStatus.PAYING);
        } else {
            throw new ApplicationException(ErrorCode.MONEY_ERROR);
        }

        return contract.getId();


    }

    private ContractLiveVo checkProduct(ContractLiveVo contractVo) {
        Set<ContractProductVo> contractProductVos = contractVo.getContractProductVos();
        for (ContractProductVo contractProductVo:contractProductVos){
            String productId = productService.getLiveProductIdByLiveId(contractProductVo.getLiveId());
            contractProductVo.setProductId(productId);
        }
        contractVo.setContractProductVos(contractProductVos);
        return contractVo;
    }

    private void saveContractProductsForLiveContract(Contract contract, Set<ContractProductVo> contractProductVos) throws Exception  {
        for (ContractProductVo conProdVo : contractProductVos){
            ContractProductHandler prodHandler = this.selectHandlerByType(ProductType.LIVE);
            conProdVo.setProductType(ProductType.LIVE);
            conProdVo.setQuantityInProduct(conProdVo.getQuantity());
            BigDecimal promotionAmount=conProdVo.getPromotionAmount();
            if(promotionAmount.compareTo(BigDecimal.ZERO)>0) {
//              product.setPromotionAmount(promotionAmount);
                Promotion promotion = promotionDao.findOneByHQL(" from Promotion where endTime>='"+DateTools.getCurrentDate()+"' and organization.id='000001' and promotionType='REDUCTION' and isActive='Y' and promotionValue=" + promotionAmount, null);
                if (promotion != null) {
                    conProdVo.setPromotionIds(promotion.getId());
                } else {//如果系统中没有，就创建新的优惠
                    Promotion p=productService.saveNewPromotion(promotionAmount);
                    conProdVo.setPromotionIds(p.getId());
                }
            }

            prodHandler.createNewWithContract(contract, conProdVo);
        }
    }

    private void calculateContractDomainForLive(Contract contract) {
        // 更新合同Domain 的 不同值
        Set<ContractProduct> conProds = contract.getContractProducts();
        BigDecimal productTotalAmount = BigDecimal.ZERO;
        BigDecimal promotionAmount = BigDecimal.ZERO;
        BigDecimal arrangedAmount = BigDecimal.ZERO;
        BigDecimal consumeAmount = BigDecimal.ZERO;
        BigDecimal remainingAmount = BigDecimal.ZERO;
        BigDecimal liveAmount=BigDecimal.ZERO;

        for (ContractProduct conProdInLoop : conProds){
            if (conProdInLoop.getStatus() == ContractProductStatus.UNVALID) {
                continue;
            }
            liveAmount=liveAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
            //  合同产品的 剩余资金要 等于 “实际支付” - “已经支付”， 不能用total Amount， 因为totalAmount 是用于计算 promotion的金额。
            productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
            promotionAmount = promotionAmount.add(conProdInLoop.getPromotionAmount());
            arrangedAmount = arrangedAmount.add(conProdInLoop.getPaidAmount());
            consumeAmount = consumeAmount.add(conProdInLoop.getConsumeAmount());
            remainingAmount = remainingAmount.add(conProdInLoop.getRemainingAmount());
        }

        contract.setTotalAmount(productTotalAmount);
        contract.setPromotionAmount(promotionAmount);
        contract.setConsumeAmount(consumeAmount);
        contract.setRemainingAmount(remainingAmount);
        contract.setLiveAmount(liveAmount);
        contract.setAvailableAmount(contract.getPaidAmount().subtract(arrangedAmount));
        contractDao.flush();

    }

	@Override
    public String saveFullContractInfoNew(ContractVo contractVo) throws Exception {
	    return saveFullContractInfoNew(contractVo, true);
	}

	@Override
	public String saveFullContractInfoNew(ContractVo contractVo, boolean updateInventory) throws Exception {
		// 判断是否为重复提交
		if (com.eduboss.utils.StringUtil.isNotBlank(contractVo.getTransactionUuid())) {
			transactionRecordDao.saveTransactionRecord(contractVo.getTransactionUuid());
		}
		// 保存客户和学生信息
		saveCusAndStuInfoInContract(contractVo);
		
		
		// 生成合同
//		Contract contract = setContractBasicInfo(contractVo);
		
		Contract contract = HibernateUtils.voObjectMapping(contractVo, Contract.class);
		
		setContractBasicInfo_new(contract);
		
		Student stu = studentDao.findById(contractVo.getStuId());
		if(stu.getOneOnOneStatus()==null){//如果学生一对一状态为空，一对一合同产品不为空，就把状态初始化
			Set<ContractProductVo> conps = contractVo.getContractProductVos();
			for (ContractProductVo cPVo : conps) {
				if(cPVo.getProductType().equals(ProductType.ONE_ON_ONE_COURSE)){
					
					stu.setOneOnOneStatus(StudentOneOnOneStatus.NEW);
					studentDao.save(stu);
					break;
				}
			}
		}
		if(stu.getSmallClassStatus()==null){//如果学生小班状态为空，小班合同产品不为空，就把状态初始化
			Set<ContractProductVo> conps = contractVo.getContractProductVos();
			for (ContractProductVo cPVo : conps) {
				if(cPVo.getProductType().equals(ProductType.SMALL_CLASS)){
					stu.setSmallClassStatus(StudentSmallClassStatus.NEW);
					studentDao.save(stu);
					break;
				}
			}
		}
		if(stu.getOneOnManyStatus()==null){//如果学生一对多状态为空，一对多合同产品不为空，就把状态初始化
			Set<ContractProductVo> conps = contractVo.getContractProductVos();
			for (ContractProductVo cPVo : conps) {
				if(cPVo.getProductType().equals(ProductType.ONE_ON_MANY)){
					stu.setOneOnManyStatus(StudentOneOnManyStatus.NEW);
					studentDao.save(stu);
					break;
				}
			}
		}
		saveContract(contract);
//		contractDao.save(contract);
		contractDao.flush();

		if (contractVo.getVirtualSchool()!=null && StringUtils.isNotBlank(contractVo.getVirtualSchool().getVirtualSchoolName()) && contractVo.getVirtualSchool().getCity()!=null ){
			// TODO: 2016/10/8 保存新的 待审核学校
			StudentSchoolTemp studentSchoolTemp = new StudentSchoolTemp();
			VirtualSchool virtualSchool = contractVo.getVirtualSchool();
			studentSchoolTemp.setName(virtualSchool.getVirtualSchoolName());
			studentSchoolTemp.setStudentId(contractVo.getStuId());
//			studentSchoolTemp.setRegion(virtualSchool.getRegion());
			studentSchoolTemp.setCity(virtualSchool.getCity());
			studentSchoolTemp.setProvince(virtualSchool.getProvince()); //省份
			studentSchoolTemp.setContractId(contract.getId());
			studentSchoolTemp.setSchoolTempAuditStatus(SchoolTempAuditStatus.UNAUDIT);
			studentSchoolTemp.setCreateTime(DateTools.getCurrentDateTime());
			studentSchoolTemp.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			studentSchoolTempDao.save(studentSchoolTemp);
			stu.setSchoolTemp(studentSchoolTemp);
            stu.setSchoolOrTemp("schoolTemp");
			contract.setSchoolTemp(studentSchoolTemp);
            contract.setSchoolOrTemp("schoolTemp");
			studentDao.save(stu);
		}else {
            contract.setSchoolOrTemp("school");
            stu.setSchoolOrTemp("school");
        }
		
		// 生成不同的合同产品项目
		saveContractProductsForNewContract(contract, contractVo.getContractProductVos(), updateInventory);
//		throw new ApplicationException("test");
		// 更新合同的状态
        contractDao.getHibernateTemplate().refresh(contract);
		this.calculateContractDomain(contract);
		
		// 优惠资金插入
        BigDecimal promotionAmount = contract.getPromotionAmount();
        if(promotionAmount.compareTo(BigDecimal.ZERO)!=0) {
            insertOneFundRecord(contract, promotionAmount, contract.getStudent(), PayWay.PROMOTION_MONEY);
        }

        // 检查 合同缴费状态
        int flag = contract.getPendingAmount().compareTo(BigDecimal.ZERO);
        if(flag == 0) {
            contract.setPaidStatus(ContractPaidStatus.PAID);
        }else if(contract.getPaidAmount().compareTo(BigDecimal.ZERO)==0){//支付金额为0，设置为待付款
            contract.setPaidStatus(ContractPaidStatus.UNPAY);
        }else if (flag >0) {
            contract.setPaidStatus(ContractPaidStatus.PAYING);
        } else {
            throw new ApplicationException(ErrorCode.MONEY_ERROR);
        }

        //推送到手机端   add by Yao 2015-05-06
        //String value=systemConfigService.getSystemConfigValueByTag("newContractIsPush");//得到系统是否需要推送信息
        String value= "0";
        if(StringUtils.isNotEmpty(value) && "0".equals(value) && contract.getCreateByStaff()!=null && userService.getCurrentLoginUser() !=null){
            List<String> arrayOfUserId =  new ArrayList<String> ();
            arrayOfUserId.add(contract.getCreateByStaff().getUserId());
            mobilePushMsgService.pushRemindToUserIds(arrayOfUserId, userService.getCurrentLoginUser().getUserId(), "学生"+contract.getStudent().getName()+"已签合同。");
        }
        //推送到手机端结束

        //修改客户跟进校区信息
        if(StringUtils.isNotEmpty(contract.getBlCampusId())){
            Customer customer = customerDao.findById(contract.getCustomer().getId());
            customer.setBlSchool(contract.getBlCampusId());
            customer.setBlCampusId(organizationDao.findById(contract.getBlCampusId()));
            customerDao.save(customer);
        }

        ContractRecord cr=new ContractRecord(contract);
        cr.setUpdateType(UpdateType.NEW);
        cr.setCreateByStaff(userService.getCurrentLoginUser());
        contractRecordDao.save(cr);
        // 加入学生的记录信息 开始
        studentDynamicStatusService.createContractDynamicStatus(contract, "CREATE_NEW_CONTRACT" );
        // 加入学生的记录信息 结束

//      if(StringUtils.isNotBlank(contractVo.getContractId()) && !contractVo.getContractType().equals(contract.getContractType())){//修改合同类型
//          findFundHistoryById()
//      }

        //更新学生产品第一次时间
        studentService.updateStudentProductFirstTime(stu);

        return contract.getId();

	}
	
	@Override
	public void insertOneFundRecord(Contract contract,
			BigDecimal transactionAmount, Student student, PayWay payWayChannel) {

		FundsChangeHistory fundsChangeHistory = new FundsChangeHistory();
		fundsChangeHistory.setRemark("");
		fundsChangeHistory.setTransactionAmount(transactionAmount);
		fundsChangeHistory.setTransactionTime(DateTools.getCurrentDateTime());
		fundsChangeHistory.setContract(contract);
		fundsChangeHistory.setChannel(payWayChannel);
		fundsChangeHistory.setStudent(contract.getStudent());
		fundsChangeHistory.setChargeBy(userService.getCurrentLoginUser());
		Double historySum = fundsChangeHistoryDao.historySumFundsChange(contract.getId());
		if(payWayChannel == PayWay.PROMOTION_MONEY) {
			fundsChangeHistory.setPaidAmount(BigDecimal.valueOf(historySum));
		} else {
			fundsChangeHistory.setPaidAmount(BigDecimal.valueOf(historySum + transactionAmount.doubleValue() ));
		}
		fundsChangeHistoryDao.save(fundsChangeHistory);
		fundsChangeHistoryDao.flush();
	}

	/**
	 * 设置一些Contract 公用的属性, 不论是 全款的合同 和 订金合同
	 * @param contract
	 * @return
	 */
	private void setContractBasicInfo_new(Contract contract) {
//		Contract contract =  new Contract();
//		
//		contract.setStudent(new Student(contract.getStuId()));
//		contract.setCustomer(new Customer(contract.getCusId()));
		contract.setContractType(contract.getContractType());

		contract.setCreateTime(DateTools.getCurrentDateTime());
		User currentLoginUser = userService.getCurrentLoginUser();
		if (currentLoginUser == null){
			String userId = contract.getSignStaff().getName();
			currentLoginUser = userService.findUserById(userId);
		}
		contract.setCreateByStaff(currentLoginUser);
		contract.setSignStaff(currentLoginUser);
		contract.setModifyByStaff(currentLoginUser);
		contract.setModifyTime(DateTools.getCurrentDateTime());
        if(StringUtils.isBlank(contract.getBlCampusId())){
            contract.setBlCampusId(userService.getBelongCampus().getId());
        }
	}
	
	
	private void saveContractProductsForNewContract(Contract contract,
			Set<ContractProductVo> contractProductVos, boolean updateInventory) throws Exception {
		// TODO Auto-generated method stub
		for(ContractProductVo conProdVo : contractProductVos) {
			ContractProductHandler prodHandler = this.selectHandlerByType(conProdVo.getProductType());
			prodHandler.createNewWithContract(contract, conProdVo, updateInventory);
		}
	}
	
	@Override
	public ContractProductHandler selectHandlerByType(ProductType productType) {
		switch(productType) {
		case ONE_ON_ONE_COURSE :
			if(this.oneOnOneConProdHandler== null) {
				this.oneOnOneConProdHandler =  new OneOnOneConProdHandler();
			}
			return this.oneOnOneConProdHandler; 
		case SMALL_CLASS:  
			if(this.miniClassConProdHandler== null) {
				this.miniClassConProdHandler =  new MiniClassConProdHandler();
			}
			return this.miniClassConProdHandler; 
		case ECS_CLASS:  
			if(this.promiseClassConProdHandler== null) {
				this.promiseClassConProdHandler =  new PromiseClassConProdHandler();
			}
			return this.promiseClassConProdHandler; 
		case OTHERS: 
			if(this.otherConProdHandler== null) {
				this.otherConProdHandler =  new OtherConProdHandler();
			}
			return this.otherConProdHandler; 
		case ONE_ON_MANY:  
			if(this.otmClassProdHandler== null) {
				this.otmClassProdHandler =  new OtmClassConProdHandler();
			}
			return this.otmClassProdHandler; 
			
		case LECTURE:  
			if(this.lectureClassProdHandler== null) {
				this.lectureClassProdHandler =  new LectureClassConProdHandler();
			}
			return this.lectureClassProdHandler;
		case TWO_TEACHER:
			if(this.twoTeacherClassProdHandler== null) {
				this.twoTeacherClassProdHandler =  new TwoTeacherClassConProdHandler();
			}
			return this.twoTeacherClassProdHandler;
		case LIVE:
				if(this.liveProdHandler== null) {
					this.liveProdHandler =  new LiveConProdHandler();
				}
				return this.liveProdHandler;
		default: throw new ApplicationException("不能支持这种类型的产品！");
		
		}
	}
	
	@Override
	public void saveContractArrangeMoney_new(BigDecimal[] arrangeMoneys,
			String[] conPrdIds) {
		String studentId="";
		if(conPrdIds == null)
			return;
		int length =  conPrdIds.length;
		for(int i =0; i<length; i++) {
			BigDecimal arrangeMoney = arrangeMoneys[i];
			String conPrdId = conPrdIds[i];
			if( arrangeMoney.compareTo(BigDecimal.ZERO)>=0 ) {
				ContractProduct conProductInDb = contractProductDao.findById(conPrdId);
				Contract contractInDb = conProductInDb.getContract();
				ContractProductHandler handler = this.selectHandlerByType(conProductInDb.getType()); 
				handler.assignMoney(contractInDb, conProductInDb, arrangeMoney);
				// TODO: 2016/7/25 更新收款列表的待分配资金
				Map<String, Object> params  = Maps.newHashMap();
				params.put("contractId", contractInDb.getId());
				List<FundsChangeHistory> fundsChangeHistoryList = fundsChangeHistoryDao.findAllByHQL(" from FundsChangeHistory f where f.contract.id = :contractId ",params);
				for (FundsChangeHistory f : fundsChangeHistoryList){
					updateFundsChangeHistoryNotAssigned(f.getId());
				}

				if(StringUtils.isBlank(studentId)) {
					studentId = contractInDb.getStudent().getId();
				}

				// 分配资金后，实时更新到学生账户
				StudnetAccMv studnetAccMv = getStudentAccoutInfo(contractInDb.getStudent().getId());
				studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
				studnetAccMvDao.flush();
				
				//this.addArrangeMoneyForProduct(arrangeMoney, conPrdId);
				// 判断本产品是不是other， 如果是其他，且paid， 还没有end， 就charge。
//				ContractProduct conPrd = contractProductDao.findById(conPrdId);
//				if(conPrd.getType()== ProductType.OTHERS &&  conPrd.getPaidStatus()== ContractProductPaidStatus.PAID &&conPrd.getStatus()!= ContractProductStatus.ENDED){
//					chargeService.chargeOtherProdect(conPrdId, userService.getCurrentLoginUser());
//				}
			}
		}


		if(StringUtils.isNotBlank(studentId)) {
			//学生状态处理
			PushRedisMessageUtil.pushStudentToMsg(studentId);
		}

	}
	
	
	@Override
	public void saveContractProductMoney(BigDecimal[] arrangeMoneys,
			String[] conPrdIds,String fundId, Contract contract) {
		
		BigDecimal ooo=BigDecimal.ZERO;
		BigDecimal otm=BigDecimal.ZERO;
		BigDecimal mini=BigDecimal.ZERO;
		BigDecimal ecs=BigDecimal.ZERO;
		BigDecimal other=BigDecimal.ZERO;

		BigDecimal lecture = BigDecimal.ZERO;
		BigDecimal twoTeacher = BigDecimal.ZERO;
		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal liveAmount = BigDecimal.ZERO;
		
		String userId="";
		String blcampus="";
		
		if(StringUtils.isBlank(fundId))
			return;
		if(conPrdIds == null)
			return;
		
		FundsChangeHistory fund = fundsChangeHistoryDao.findById(fundId);
		if(contract==null) {
			contract = fund.getContract();
		}
		int length =  conPrdIds.length;
		for(int i =0; i<length; i++) {
			BigDecimal arrangeMoney = arrangeMoneys[i];
			String conPrdId = conPrdIds[i];
			totalAmount=totalAmount.add(arrangeMoney);
			if( arrangeMoney.compareTo(BigDecimal.ZERO)>0 ) {
				ContractProduct conProductInDb = contractProductDao.findById(conPrdId);
				Contract contractInDb = conProductInDb.getContract();
				ContractProductHandler handler = this.selectHandlerByType(conProductInDb.getType()); 
				handler.assignMoney(contractInDb, conProductInDb, arrangeMoney);
				
				// 分配资金后，实时更新到学生账户
				StudnetAccMv studnetAccMv = getStudentAccoutInfo(contractInDb.getStudent().getId());
				studnetAccMvDao.getHibernateTemplate().save(studnetAccMv);
				studnetAccMvDao.flush();
				
				if(conProductInDb.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
					ooo=ooo.add(arrangeMoney);
				}else if(conProductInDb.getType().equals(ProductType.ONE_ON_MANY)){
					otm=otm.add(arrangeMoney);
				}else if(conProductInDb.getType().equals(ProductType.SMALL_CLASS)){
					mini=mini.add(arrangeMoney);
				}else if(conProductInDb.getType().equals(ProductType.ECS_CLASS)){
					ecs=ecs.add(arrangeMoney);
				}else if(conProductInDb.getType().equals(ProductType.OTHERS)){
					other=other.add(arrangeMoney);
				}else if(conProductInDb.getType().equals(ProductType.LECTURE)){
					lecture=lecture.add(arrangeMoney);
				}else if (conProductInDb.getType().equals(ProductType.TWO_TEACHER)){
					twoTeacher = twoTeacher.add(arrangeMoney);
				}else if (conProductInDb.getType().equals(ProductType.LIVE)){
					liveAmount = liveAmount.add(arrangeMoney);
					//20171017  园园跟亚菲确认  业绩是要100%   课消才是50%   所以放开    modify by Yao
					//redmine #1212 新签或者续费合同业绩分配比例为合同校区：直播平台=50%:50%，合同业绩挂回到原boss系统直播课程合同签单人身上。
				}
				


				if(StringUtils.isBlank(userId)){
					userId=contractInDb.getSignStaff().getUserId();
				}
				
				if(StringUtils.isBlank(blcampus)){
					blcampus=contractInDb.getBlCampusId();
				}
				
			}
		}
		List<Message> bonusMsg=new ArrayList<>();
		// 业绩分配
		Map<ProductType,BigDecimal> bonusMap=new HashMap<>();
		bonusMap.put(ProductType.ECS_CLASS,ecs);
		bonusMap.put(ProductType.ONE_ON_ONE_COURSE,ooo);
		bonusMap.put(ProductType.ONE_ON_MANY,otm);
		bonusMap.put(ProductType.SMALL_CLASS,mini);
		bonusMap.put(ProductType.OTHERS,other);
		bonusMap.put(ProductType.LECTURE,lecture);
		bonusMap.put(ProductType.TWO_TEACHER,twoTeacher);
		bonusMap.put(ProductType.LIVE,liveAmount);
//		if(!ContractType.LIVE_CONTRACT.equals(contract.getContractType())) {
			contractBonusArrange(bonusMap, userId, blcampus, fund, bonusMsg, contract);
			incomeDistributionDao.commit();
			updateFundsChangeHistoryNotAssigned(fundId);
//		}

		//学生状态处理
		PushRedisMessageUtil.pushStudentToMsg(fund.getStudent().getId());
//		if(!ContractType.LIVE_CONTRACT.equals(contract.getContractType())) {
			BonusQueueUtils.pushToQueue(bonusMsg);
//		}
		
	}

	/**
	 * 业绩分配
	 * @param bonusMap
	 * @param userId
	 * @param blcampus
	 * @param fund
	 */
	private void contractBonusArrange(Map<ProductType,BigDecimal> bonusMap, String userId, String blcampus, FundsChangeHistory fund,List<Message> msg, Contract contract) {
		//电子账户支付的就不用做分配
		//&& contract!=null && contract.getContractType() != ContractType.LIVE_CONTRACT 直播同步合同不分配业绩
		if(!fund.getChannel().equals(PayWay.ELECTRONIC_ACCOUNT) ){
			List<IncomeDistributionVo> voList = new ArrayList<>();
			for(ProductType key:bonusMap.keySet()){
				if(bonusMap.get(key).compareTo(BigDecimal.ZERO)>0){
					voList.addAll(saveIncomArrangeMoney(bonusMap.get(key), key, fund, userId, blcampus,msg));
				}
			}
			addToIncomeDistributeStatements(fund, voList);
		}
	}

	private List<IncomeDistributionVo> saveIncomArrangeMoney(BigDecimal arrangeMoney, ProductType type, FundsChangeHistory fund, String userId, String blcampus,List<Message> msg) {
		User currentLoginUser = userService.getCurrentLoginUser();
		IncomeDistribution incomeDistribution = new IncomeDistribution(); //个人业绩  为签单人
		incomeDistribution.setFundsChangeHistory(fund);
		incomeDistribution.setAmount(arrangeMoney);
		incomeDistribution.setBonusStaff(userService.findUserById(userId));
		Organization campus=userService.getBelongCampusByUserId(userId);
		incomeDistribution.setBonusStaffCampus(campus);
		incomeDistribution.setBonusType(BonusType.NORMAL);
		incomeDistribution.setProductType(type);
		incomeDistribution.setBaseBonusDistributeType(BonusDistributeType.USER);
		incomeDistribution.setSubBonusDistributeType(BonusDistributeType.USER_USER);
		incomeDistribution.setCreateUser(currentLoginUser);
		if (StringUtils.isNotBlank(blcampus)){
			Organization schoolOrg = organizationDao.findById(blcampus);
			if (schoolOrg != null){
				incomeDistribution.setContractCampus(schoolOrg);
			}
		}

		incomeDistributionDao.save(incomeDistribution);
		BonusQueueUtils.addBonusToMessage(incomeDistribution,msg,"1");

		IncomeDistribution campusIncome = new IncomeDistribution();
		campusIncome.setBaseBonusDistributeType(BonusDistributeType.CAMPUS);
		campusIncome.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS);
		campusIncome.setFundsChangeHistory(fund);
		campusIncome.setAmount(arrangeMoney);
		if(StringUtils.isNotBlank(blcampus)){
			Organization schoolOrg = organizationDao.findById(blcampus);
			if(schoolOrg != null){
				campusIncome.setBonusOrg(schoolOrg);
				campusIncome.setContractCampus(schoolOrg);
			}
		}
		campusIncome.setProductType(type);
		campusIncome.setBonusType(BonusType.NORMAL);
		campusIncome.setCreateUser(currentLoginUser);
		incomeDistributionDao.save(campusIncome);

		List<IncomeDistribution> list = new ArrayList<>();
		list.add(incomeDistribution);
		list.add(campusIncome);


		return HibernateUtils.voListMapping(list, IncomeDistributionVo.class);


	}

	/**
	 * @param arrangeMoney 分配金额
	 * @param type 合同产品类型
	 * @param fund 付款记录
	 * @param userId  合同签单人
	 * @param blcampus 合同校区
	 */
	public void saveArrangeMoney(BigDecimal arrangeMoney,ProductType type,FundsChangeHistory fund,String userId,String blcampus){
			ContractBonus bonusItemF = new ContractBonus();//个人业绩    为签单人
			bonusItemF.setFundsChangeHistory(fund);
			bonusItemF.setBonusAmount(arrangeMoney);
			bonusItemF.setBonusStaff(userService.findUserById(userId));
			Organization campus=userService.getBelongCampusByUserId(userId);
			bonusItemF.setBonusStaffCampus(campus);
			bonusItemF.setBonusType(BonusType.NORMAL);
			bonusItemF.setType(type);
			if(StringUtils.isNotBlank(blcampus)){
				Organization schoolOrg = organizationDao.findById(blcampus);
	            if(schoolOrg != null){
	            	bonusItemF.setContractCampus(schoolOrg);
	            }
			}
				
			contractBonusDao.save(bonusItemF);
			
			ContractBonus bonusItemB = new ContractBonus();//校区业绩  为合同所属校区
			bonusItemB.setFundsChangeHistory(fund);
			bonusItemB.setCampusAmount(arrangeMoney);
			if(StringUtils.isNotBlank(blcampus)){
				Organization schoolOrg = organizationDao.findById(blcampus);
	            if(schoolOrg != null){
	            	bonusItemB.setOrganization(schoolOrg);
	            	bonusItemB.setContractCampus(schoolOrg);
	            }
			}
			bonusItemB.setType(type);
			bonusItemB.setBonusType(BonusType.NORMAL);
			contractBonusDao.save(bonusItemB);
	}
	
	@Override
	public void saveContractTakeawayMoney(BigDecimal takeawayMoney,
			String conPrdId,BigDecimal maxKeTiquMoney) {
		String contractId= null;
		String studentId="";
		List<Message> messageList=new ArrayList<Message>();
		if( takeawayMoney.compareTo(BigDecimal.ZERO)>0 ) {
			ContractProduct conProductInDb = contractProductDao.findById(conPrdId);
			// 该合同产品已经冻结，不能提取资金
			if(conProductInDb!=null){
				if (conProductInDb.getIsFrozen() == 0) {
					throw new ApplicationException("该合同产品已经冻结，不能提取资金！");
				}
				if (conProductInDb.getPromotionConsumeAmount().compareTo(BigDecimal.ZERO) > 0) {
					throw new ApplicationException("已产品优惠消耗，不可以提取资金！");
				}
				if (conProductInDb.getType() == ProductType.ONE_ON_ONE_COURSE) {
					BigDecimal oooSubjectDistributedHours = conProductInDb.getOooSubjectDistributedHours() != null ? 
					conProductInDb.getOooSubjectDistributedHours() : BigDecimal.ZERO;
					BigDecimal unDistributedMoney = conProductInDb.getPaidAmount().subtract(conProductInDb.getPrice().multiply(oooSubjectDistributedHours).setScale(2, BigDecimal.ROUND_DOWN));
					if (takeawayMoney.compareTo(unDistributedMoney) > 0) {
						throw new ApplicationException("提取金额大于合同可提取金额！");
					}
				}
				contractId = conProductInDb.getContract().getId();
				Contract contractInDb = conProductInDb.getContract();
				studentId=contractInDb.getStudent().getId();
				ContractProductHandler handler = this.selectHandlerByType(conProductInDb.getType()); 
				handler.takeawayMoney(contractInDb, conProductInDb, takeawayMoney);
			}
			System.out.println(conProductInDb.getPaidAmount());

			/**
			 * 最大可提取业绩
			 */
			maxKeTiQuMoney(takeawayMoney, maxKeTiquMoney, contractId, messageList, conProductInDb);

			// TODO: 2016/7/25 提现时更新收款列表的待分配金额
			Map<String, Object> params  = Maps.newHashMap();
			params.put("contractId", contractId);
			params.put("channel", PayWay.ELECTRONIC_ACCOUNT);
			List<FundsChangeHistory> fundsChangeHistoryList = fundsChangeHistoryDao.findAllByHQL(" from FundsChangeHistory f where f.contract.id = :contractId and f.channel<> :channel ",params);
			for (FundsChangeHistory f : fundsChangeHistoryList){
//				updateFundsChangeHistoryNotAssigned(f);
				updateFundsChangeHistoryNotAssigned(f.getId());
			}

		}
		
		contractBonusDao.commit();
		
		pushToQueue(messageList);//添加到队列
		//学生状态处理
		PushRedisMessageUtil.pushStudentToMsg(studentId);
		
	}

	private void maxKeTiQuMoney(BigDecimal takeawayMoney, BigDecimal maxKeTiquMoney, String contractId, List<Message> messageList, ContractProduct conProductInDb) {

		BigDecimal a = BigDecimal.ZERO;

		ContractAlreadyFenpei fenpei = new ContractAlreadyFenpei();
		fenpei.setContractId(contractId);
		/**
		 * 合同已分配业绩
		 */
		fenpei=getContractAlreadyFenpei(fenpei);
		double ooo = Math.max(fenpei.getOooUser().doubleValue(),fenpei.getOooCampus().doubleValue());
		double mini =Math.max(fenpei.getMiniUser().doubleValue(),fenpei.getMiniCampus().doubleValue());
		double otm = Math.max(fenpei.getOtmUser().doubleValue(),fenpei.getOtmCampus().doubleValue());
		double ecs = Math.max(fenpei.getEcsUser().doubleValue(),fenpei.getEcsCampus().doubleValue());
		double other = Math.max(fenpei.getOtherUser().doubleValue(),fenpei.getOtherCampus().doubleValue());
		double lecture = Math.max(fenpei.getLectureUser().doubleValue(), fenpei.getLectureCampus().doubleValue());
		double live = Math.max(fenpei.getLiveUser().doubleValue(), fenpei.getLiveCampus().doubleValue());

		switch (conProductInDb.getProduct().getCategory()){
			case ONE_ON_ONE_COURSE:
				a = BigDecimal.valueOf(ooo);
				break;
			case SMALL_CLASS:
				a = BigDecimal.valueOf(mini);
				break;
			case ONE_ON_MANY:
				a = BigDecimal.valueOf(otm);
				break;
			case ECS_CLASS:
				a = BigDecimal.valueOf(ecs);
				break;
			case OTHERS:
				a = BigDecimal.valueOf(other);
				break;
			case LECTURE:
				a = BigDecimal.valueOf(lecture);
				break;
			case LIVE:
				a = BigDecimal.valueOf(live);
				break;
		}



		if (a!=null){//maxKeTiquMoney
            if (StringUtils.isNotBlank(contractId)){

                /**
                 * 本合同的已分配业绩
                 */
//                List<ContractBonus> list = contractBonusDao.findByContractIdExeptFundId(contractId, null,conProductInDb.getType().getValue());

				List<IncomeDistribution> list = incomeDistributionDao.findByContractIdExeptFundId(contractId, null,conProductInDb.getType().getValue());

                /**
                 * 如果有大于等于本次提取金额的业绩，就删掉该条（个人或校区）业绩，后面的记录都不做处理
                 */
                Boolean camFlag=false;
                Boolean uFlag=false;
                for (IncomeDistribution i:list){ //ContractBonus con
					if (BonusDistributeType.CAMPUS.equals(i.getBaseBonusDistributeType())){
						if (i.getAmount().compareTo(takeawayMoney)>=0){
							incomeDistributionDao.delete(i);
							camFlag=true;
						}
					}else if (BonusDistributeType.USER.equals(i.getBaseBonusDistributeType())){
						if (i.getAmount().compareTo(takeawayMoney)>=0){
							incomeDistributionDao.delete(i);
							uFlag = true;
							addBonusToMessage(i, messageList,"0");
						}
					}
                }

                /**
                 * 如果没有一条业绩金额大于当前提取金额，那么用多条来拼
                 */
                BigDecimal cmoney=takeawayMoney;
                BigDecimal umoney=takeawayMoney;
                if(!camFlag || !uFlag){
                    for (IncomeDistribution con:list){//ContractBonus con
                        if(cmoney.compareTo(BigDecimal.ZERO)>0 && BonusDistributeType.CAMPUS.equals(con.getBaseBonusDistributeType()) && con.getProductType()!=null && !camFlag) {//绩效校区不为空就是 校区业绩
                            if(con.getAmount().compareTo(cmoney)>=0){
                                incomeDistributionDao.delete(con);
                                camFlag=true;
                            }else{
                                cmoney=cmoney.subtract(con.getAmount());
								incomeDistributionDao.delete(con);
                            }

                        }else if(umoney.compareTo(BigDecimal.ZERO)>0 && BonusDistributeType.USER.equals(con.getBaseBonusDistributeType()) && con.getProductType()!=null && !uFlag){//其他就是用户业绩
                            if(con.getAmount().compareTo(umoney)>=0){
								incomeDistributionDao.delete(con);
                                uFlag=true;
                            }else{
                                umoney=umoney.subtract(con.getAmount());
								incomeDistributionDao.delete(con);
                            }
                            if (con.getBonusStaff()!=null){
								addBonusToMessage(con, messageList,"0");//用户业绩删除添加至队列
							}

                        }
                    }
                }
            }
        }
	}


	@Override
	public List<ContractProduct> getOrderValidOneOnOneContractProducts(Student student) {
		Map<String, Object> params  = Maps.newHashMap();
		params.put("studentId", student.getId());
		params.put("status1", ContractProductStatus.STARTED.getValue());
		params.put("status2", ContractProductStatus.NORMAL.getValue());
		StringBuffer hql = new StringBuffer();
		hql.append( "from ContractProduct as contractProduct ")
			.append(" where 1 = 1 ")
			.append(" and contractProduct.type = '")
			.append("ONE_ON_ONE_COURSE").append("'")
			.append(" and contractProduct.contract.student = :studentId ")
			.append(" and (contractProduct.status = :status1 ")
			.append(" or contractProduct.status = :status2 ").append(" )");
		hql.append(" order by contractProduct.contract.createTime asc");
		
		List<ContractProduct> list = contractProductDao.findAllByHQL(hql.toString(),params);
		return list ;
	}

    /**
     * 按照课程拿有效的合同产品
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     * 若课程无对应产品则根据学生获取所有报读且有效的一对一产品
     *
     * @param course
     * @return
     */
    @Override
    public List<ContractProduct> getOrderValidOneOnOneContractProducts(Course course) {
        Product courseProduct = course.getProduct();
        if(courseProduct == null){
            return getOrderValidOneOnOneContractProducts(course.getStudent());
        }else{
            List<Order> orders = new ArrayList<Order>();
            orders.add(Order.asc("contract.createTime"));
            List<Criterion> criterions = new ArrayList<Criterion>();
            criterions.add(Restrictions.eq("type",ContractProductType.ONE_ON_ONE_COURSE));
            criterions.add(Restrictions.eq("contract.student.id",course.getStudent().getId()));
            criterions.add(Restrictions.in("status", new Object[]{ContractProductStatus.STARTED,ContractProductStatus.NORMAL}));
//			criterions.add(Restrictions.eq("isFrozen", 1));//取不冻结的合同产品  #5435 学生在走退费流程，1对1课程考勤对应的产品问题
            DataDict productGroup = courseProduct.getProductGroup();
            if(productGroup == null){
                criterions.add(Restrictions.eq("product.id",course.getProduct().getId()));
            }else{
                criterions.add(Restrictions.eq("product.productGroup",course.getProduct().getProductGroup()));
            }
            return contractProductDao.findAllByCriteria(criterions,orders);
        }
    }
    
    /**
     * 按照课程拿有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     */
    public List<ContractProduct> getOrderValidSubjectOneOnOneContractProducts(Course course) {
    	List<ContractProduct> list = contractProductDao.getOrderValidSubjectOneOnOneContractProducts(course);
    	for (ContractProduct cp : list) {
    		ContractProductSubject cpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(cp.getId(), course.getSubject().getId());
    		cp.setSubjectRemainHours(cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()));
    	}
    	return list;
    }
    
    /**
     * 计算按照课程有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     */
    public BigDecimal countOrderValidSubjectOneOnOneContractProducts(Course course) {
    	return contractProductDao.countOrderValidSubjectOneOnOneContractProducts(course);
    }

    @Override
	public void saveEditFullContractInfoNew(ContractVo contractVo) throws Exception {
    	// 判断是否为重复提交
		if (com.eduboss.utils.StringUtil.isNotBlank(contractVo.getTransactionUuid())) {
			transactionRecordDao.saveTransactionRecord(contractVo.getTransactionUuid());
		}
    	
		// 加载合同
		Contract contract = contractDao.findById(contractVo.getContractId());
		
		if(contract.getIsNarrow()==1){
			throw new ApplicationException("缩单合同不能修改！");
		}
		
		if(contract.getContractStatus() == ContractStatus.UNVALID){
			throw new ApplicationException("关闭合同不能修改！");
		}
		Boolean isChangeContractType=false;
		ContractType type=contractVo.getContractType();
		ContractType oldType=contract.getContractType();
		if(!contractVo.getContractType().equals(contract.getContractType()) && !contractVo.getContractType().equals(ContractType.INIT_CONTRACT)){
			isChangeContractType=true;
		}
		// 新的合同产品
		Set<ContractProduct> destContractProducts = new HashSet<ContractProduct>();
		
		BigDecimal oldPaidAmount =  contract.getPaidAmount();
		boolean hasDeletePaidAmountContractProduct=false;
		// 遍历合同产品，处理删除动作
		for(ContractProduct contractProduct : contract.getContractProducts()){
			ContractProductHandler handler =  this.selectHandlerByType(contractProduct.getType());
			boolean exists = false; // 产品是否存在于界面传回的vo集合中
			for(ContractProductVo vo: contractVo.getContractProductVos()) {
				if(contractProduct.getId().equals(vo.getId())){ // 数据库中小班产品存在于界面传回的产品列表中
					exists = true;
				}
			}
			if(!exists){ // 该合同产品在界面中被删除
                
				// 检查不同的合同产品状态, 看是否可以删除, 例如扣费后就不能删除, 跑出 exception. 
//				handler.checkIfCanDelete(contractProduct);
				
//				if(contractProduct.getType() == ProductType.ONE_ON_ONE_COURSE){ // 小班
//                    if(contractProduct.getConsumeQuanity().compareTo(BigDecimal.ZERO) > 0){
//                        throw new ApplicationException("已消费的一对一课程不允许删除");
//                    }
//                }else if(contractProduct.getType() == ProductType.SMALL_CLASS){ // 小班
//					if(contractProduct.getStatus() == ContractProductStatus.NORMAL || contractProduct.getStatus() == null){ // 正常状态，允许删除
//						miniClassStudentDao.deleteStudentByContract(contract); // 删除小班报名记录
//					}else{
//						throw new ApplicationException(ErrorCode.CANT_DEL_WHEN_CHARGED);
//					}
//				}else if(contractProduct.getType() == ProductType.OTHERS){ // 其他
//					if(contractProduct.getStatus() == ContractProductStatus.ENDED){ // 已扣费，不能删
//						throw new ApplicationException(ErrorCode.CANT_DEL_WHEN_CHARGED);
//					}
//				}
				MiniClassRelationSearchVo omcsVo = new MiniClassRelationSearchVo();
				String miniClassId = contractProduct.getMiniClassId();
				if (StringUtils.isNotBlank(miniClassId)) {
					omcsVo.setStudentId(contract.getStudent().getId());
					omcsVo.setNewMiniClassId(miniClassId);
					smallClassService.deleteOldMiniClass(omcsVo);
				}
				if(contractProduct.getType().equals(ProductType.TWO_TEACHER)){
					twoTeacherClassService.deleteTwoClassStudentByCpId(contractProduct.getId());
				}

				handler.deleteContractProduct(contractProduct);

				if(contractProduct.getPaidAmount().compareTo(BigDecimal.ZERO)>0){
					hasDeletePaidAmountContractProduct=true;
				}
//				contractDao.getHibernateTemplate().evict(contract);
//        		contract = contractDao.findById(contractVo.getContractId());
//				contract.setAvailableAmount(contract.getAvailableAmount().add(contractProduct.getPaidAmount())); // 资金回流至合同待分配
//				contractDao.save(contract);
//				contractProductDao.deleteById(contractProduct.getId()); // 删除该产品
//				contractDao.flush();
			}else{
				if(contract.getStudent().getOneOnOneStatus()==null){//如果学生一对一状态为空，一对一合同产品不为空，就把状态初始化
						if(contractProduct.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
							Student stu = studentDao.findById(contractVo.getStuId());
							stu.setOneOnOneStatus(StudentOneOnOneStatus.NEW);
							studentDao.save(stu);
					}
				}
				if(contract.getStudent().getSmallClassStatus()==null){//如果学生小班状态为空，小班合同产品不为空，就把状态初始化
						if(contractProduct.getType().equals(ProductType.SMALL_CLASS)){
							Student stu = studentDao.findById(contractVo.getStuId());
							stu.setSmallClassStatus(StudentSmallClassStatus.NEW);
							studentDao.save(stu);
						}
				}
				
				if(contract.getStudent().getOneOnManyStatus()==null){//如果学生一对多状态为空，一对多合同产品不为空，就把状态初始化
					if(contractProduct.getType().equals(ProductType.ONE_ON_MANY)){
						Student stu = studentDao.findById(contractVo.getStuId());
						stu.setOneOnManyStatus(StudentOneOnManyStatus.NEW);
						studentDao.save(stu);
					}
				}
			}
		}
		// 清除缓存，重新加载合同
		contractDao.getHibernateTemplate().evict(contract);
		contract = contractDao.findById(contractVo.getContractId());
		contract.setGradeDict(new DataDict(contractVo.getGradeId()));//年级
		contract.setRemark(contractVo.getRemark()); // 保存备注
		contract.setContractType(contractVo.getContractType());//合同类型
        // 保存校区ID
        if(StringUtils.isNotBlank(contractVo.getBlCampusId())){
            contract.setBlCampusId(contractVo.getBlCampusId());
        }
        if(StringUtils.isNotBlank(contractVo.getSchoolId())){//学校信息
            contract.setSchool(new StudentSchool(contractVo.getSchoolId()));
            contract.getStudent().setSchool(new StudentSchool(contractVo.getSchoolId()));//学生的学校
        }



		if (contractVo.getVirtualSchool()!=null && StringUtils.isNotBlank(contractVo.getVirtualSchool().getVirtualSchoolName()) && contractVo.getVirtualSchool().getCity()!=null ){
			// TODO: 2016/10/13
			Student stu  = contract.getStudent();
			StudentSchoolTemp studentSchoolTemp = null;
			if (StringUtils.isNotBlank(contractVo.getSchoolTempId())){
				studentSchoolTemp = studentSchoolTempDao.findById(contractVo.getSchoolTempId());
			}else if (studentSchoolTemp == null){
				studentSchoolTemp = new StudentSchoolTemp();
			}
			VirtualSchool virtualSchool = contractVo.getVirtualSchool();
			studentSchoolTemp.setName(virtualSchool.getVirtualSchoolName());
			studentSchoolTemp.setStudentId(contractVo.getStuId());
//			studentSchoolTemp.setRegion(virtualSchool.getRegion());
			studentSchoolTemp.setCity(virtualSchool.getCity());
			studentSchoolTemp.setProvince(virtualSchool.getProvince());//省份
			studentSchoolTemp.setContractId(contract.getId());
			studentSchoolTemp.setSchoolTempAuditStatus(SchoolTempAuditStatus.UNAUDIT);
			studentSchoolTemp.setCreateTime(DateTools.getCurrentDateTime());
			studentSchoolTemp.setCreateUserId(userService.getCurrentLoginUser().getUserId());
			studentSchoolTempDao.save(studentSchoolTemp);
            stu.setSchoolOrTemp("schoolTemp");
			stu.setSchoolTemp(studentSchoolTemp);
			contract.setSchoolTemp(studentSchoolTemp);
            contract.setSchoolOrTemp("schoolTemp"); //控制显示学生学校
			studentDao.save(stu);
		}else {
            Student stu  = contract.getStudent();
            stu.setSchoolOrTemp("school");
            contract.setSchoolOrTemp("school");  //控制显示学生学校
        }

		// 遍历界面传回的vo，处理新增和修改动作
		ContractProduct processingProduct;
		Boolean oooStatus=true;
		boolean isUnValid = false;
		for(ContractProductVo contractProductVo: contractVo.getContractProductVos()) {
			// 取消合同产品的处理
			if (contractProductVo.getIsUnvalid() != null && contractProductVo.getIsUnvalid().equals("TRUE")) {
				this.unvalidContractProduct(contractProductVo.getId());
				isUnValid = true;
				continue;
			}
			// 初始化当前处理的合同产品
			processingProduct = null;
			ContractProductHandler handler = this.selectHandlerByType(contractProductVo.getProductType());
//			// 表单预处理：设置 oneOnOne 统一价格 和 赠送课时 的product ID
//			if(contractProductVo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_FREE) {
//				contractProductVo.setProductId(productService.getFreeHourProduct(contractVo.getGradeId()).getId());
//			} else if (contractProductVo.getProductType() ==  ProductType.ONE_ON_ONE_COURSE_NORMAL) {
//				contractProductVo.setProductId(productService.getOneOnOneNormalProduct(contractVo.getGradeId()).getId());
//			} else if (contractProductVo.getProductType() ==  ProductType.SMALL_CLASS) {
//				// 表单预处理：小班报读课时不能大于产品课时
//				Product prd = productDao.findById(contractProductVo.getProductId());
//				if(BigDecimal.valueOf(contractProductVo.getQuantity()).compareTo(prd.getMiniClassTotalhours()) > 0){
//					throw new ApplicationException("小班报读课时不能大于产品课时");
//				}
//			}
			// 处理单个合同产品的新增和修改
			ContractProduct voMapping = HibernateUtils.voObjectMapping(contractProductVo, ContractProduct.class);
			if(StringUtils.isBlank(contractProductVo.getId())){ // 新增合同产品
				handler.createNewWithContract(contract, contractProductVo);
//				processingProduct = voMapping;
//				processingProduct.setId(null);
//				processingProduct.setStatus(ContractProductStatus.NORMAL);
//				processingProduct.setPaidStatus(ContractProductPaidStatus.UNPAY);
//				processingProduct.setPaidAmount(BigDecimal.ZERO);
//				processingProduct.setConsumeAmount(BigDecimal.ZERO);
//				processingProduct.setConsumeQuanity(BigDecimal.ZERO);
				
				//修改合同时选择一个新的产品类型，仅通过上面判断的那种无法将新产品对应的的类型添加到学生状态中  add by tangyuping
				if(contract.getStudent().getOneOnOneStatus()==null){//如果学生一对一状态为空，一对一合同产品不为空，就把状态初始化
					if(contractProductVo.getProductType().equals(ProductType.ONE_ON_ONE_COURSE)){
						Student stu = studentDao.findById(contractVo.getStuId());
						stu.setOneOnOneStatus(StudentOneOnOneStatus.NEW);
						studentDao.save(stu);
					}
				}
				if(contract.getStudent().getSmallClassStatus()==null){//如果学生小班状态为空，小班合同产品不为空，就把状态初始化
						if(contractProductVo.getProductType().equals(ProductType.SMALL_CLASS)){
							Student stu = studentDao.findById(contractVo.getStuId());
							stu.setSmallClassStatus(StudentSmallClassStatus.NEW);
							studentDao.save(stu);
						}
				}
				
				if(contract.getStudent().getOneOnManyStatus()==null){//如果学生一对多状态为空，一对多合同产品不为空，就把状态初始化
					if(contractProductVo.getProductType().equals(ProductType.ONE_ON_MANY)){
						Student stu = studentDao.findById(contractVo.getStuId());
						stu.setOneOnManyStatus(StudentOneOnManyStatus.NEW);
						studentDao.save(stu);
					}
				}
			}else{ // 修改合同产品
				processingProduct =  getContractProductByContractList(contract, contractProductVo.getId());
				// 哪些情况下已有合同产品不允许修改
				/*if(processingProduct.getType() == ProductType.ONE_ON_ONE_COURSE_NORMAL){ // 一对一
					if(voMapping.getQuantity().compareTo(processingProduct.getConsumeQuanity()) < 0){
						throw new ApplicationException(ErrorCode.ONE_ON_ONE_TOTAL_LESS_CONSUME); // 总数小于已消费
					}
				}else if(processingProduct.getType() == ProductType.ONE_ON_ONE_COURSE_FREE){ // 一对一赠送
					if(voMapping.getQuantity().compareTo(processingProduct.getConsumeQuanity()) < 0){
						throw new ApplicationException(ErrorCode.FREE_TOTAL_LESS_CONSUME); // 总数小于已消费
					}
				}else if(processingProduct.getType() == ProductType.SMALL_CLASS){ // 小班
					if(processingProduct.getStatus() != ContractProductStatus.NORMAL){ // 已预扣费
						setNewVal = false;
//						throw new ApplicationException("已预扣费的小班课程不允许修改");
					}
				}else if(processingProduct.getType() == ProductType.OTHERS){ // 其他
					if(processingProduct.getStatus() == ContractProductStatus.ENDED){ // 已扣费
						setNewVal = false;
//						throw new ApplicationException("已扣费的其他收费不允许修改");
					}
				}
				if(setNewVal){
					processingProduct.setPlanAmount(voMapping.getPlanAmount());
					processingProduct.setDealDiscount(voMapping.getDealDiscount());
					processingProduct.setQuantity(voMapping.getQuantity());
					processingProduct.setProduct(voMapping.getProduct());
				}*/
//				handler.checkIfCanEdit(processingProduct, voMapping);
			/*	//如果修改过后的合同产品总金额=消费金额，则设置合同产品状态为完结
				if(voMapping.getPlanAmount().subtract(processingProduct.getConsumeAmount()).compareTo(BigDecimal.ZERO) == 0) {
					processingProduct.setStatus(ContractProductStatus.ENDED);
				}*/
				
				//如果修改过后的合同产品总金额+优惠金额=消费金额，则设置合同产品状态为完结
				if(voMapping.getPlanAmount().add(processingProduct.getPromotionAmount()).subtract(processingProduct.getConsumeAmount()).compareTo(BigDecimal.ZERO) == 0) {
					processingProduct.setStatus(ContractProductStatus.ENDED);
				}
				
				handler.editContratProduct(processingProduct, voMapping);
				smallClassService.addMiniClassRelation(contract.getStudent().getId(), contractProductVo.getMiniClassId(), contractProductVo.getContinueMiniClassId(), contractProductVo.getExtendMiniClassId());
			}
//			processingProduct.setContract(contract);
			// 计算当前产品的钱并push到新的合同产品set
//			this.calSingleContractProduct(processingProduct); 
//			destContractProducts.add(processingProduct);

		}		
		// 重设一对一json
//		contract.setOneOnOneJson(contractVo.getOneOnOneJson()); 
		// 重新计算合同总价
//		contract.setContractProducts(destContractProducts);
		
//		this.calContractPlanAmount(contract);
		
//		if (ContractType.INIT_CONTRACT.equals(contract.getContractType())) {
//        	saveFundOfContract(new FundsChangeHistoryVo(contract.getId(), contract.getPendingAmount().doubleValue()));
//        }

        // 更新Student acc view , 更新合同总金额
//        updateStudentAccMv(contract.getStudent());
		
//		throw new ApplicationException("test");
		contractDao.flush();
//		saveContract(contract);
		contractDao.getHibernateTemplate().refresh(contract);
		calculateContractDomain(contract);
		
		// 判断修改后的金额， 要支付的实际金额 是不是比  已经交付的金额低
		if(oldPaidAmount.compareTo(contract.getTotalAmount().subtract(contract.getPromotionAmount()))>0){
			throw new ApplicationException("合同修改后的应付金额不能少于已收款，请修改后再保存！"); //修改后的实付资金不能低于已付资金 #5643
		}
		
		
		// 合同的 优惠支付状态的更新， 每个合同应该只有一条优惠的记录
		// 先删除
		deleteOneFundRecordForPromotion(contract);
		// 再出入
		// 要优惠数据大于0才插入数据
		if(contract.getPromotionAmount().compareTo(BigDecimal.ZERO)>0) {
			insertOneFundRecord(contract, contract.getPromotionAmount(), contract.getStudent(), PayWay.PROMOTION_MONEY);
		}
		
		// 更新合同的修改时间
		contract.setModifyTime(DateTools.getCurrentDateTime());


		//修改合同后如果每种产品的实付金额少于已分配的业绩,对应的业绩要删除
		List<Message> bonusMsg=contractBonusService.afterEditContractDeleteContractBonus(contractVo.getContractId());// FIXME: 2016/7/1

		// 检查 合同缴费状态
		int flag = contract.getPendingAmount().compareTo(BigDecimal.ZERO);
		if(flag == 0) {
			modifyContractToPaid(contract);//合同付款完成了
		}else if(contract.getPendingAmount().compareTo(contract.getTotalAmount())==0){//待支付金额等于合同总额，设置为待付款
			contract.setPaidStatus(ContractPaidStatus.UNPAY);
		} else if (flag >0) {
			contract.setPaidStatus(ContractPaidStatus.PAYING);
		} else {
			throw new ApplicationException(ErrorCode.MONEY_ERROR);
		}
		
		//修改客户跟进校区信息
		if(StringUtils.isNotEmpty(contract.getBlCampusId())){
			Customer customer = customerDao.findById(contract.getCustomer().getId());
			customer.setBlSchool(contract.getBlCampusId());
			customer.setBlCampusId(organizationDao.findById(contract.getBlCampusId()));
			customerDao.save(customer);
		}
		
		
		ContractRecord cr=new ContractRecord(contract);
		List<ContractRecord> records=contractRecordDao.findLastRecordByContractId(contract.getId());
		//如果主要数据有改动，保存一份修改记录
		if((records.size()>0 && !cr.equals(records.get(0))) || isUnValid){
			cr.setUpdateType(UpdateType.UPDATE);
			cr.setCreateByStaff(userService.getCurrentLoginUser());
			contractRecordDao.save(cr);
		}
		
		// 加入学生的记录信息 开始
		studentDynamicStatusService.createContractDynamicStatus(contract, "EDIT_CONTRACT" );
		// 加入学生的记录信息 结束
		
		//学生修改合同产品的时候校验此学生拥有的学生状态有没有对应的合同产品(包括之前添加的合同)，
		//没有则设置学生的（一对一，小班，一对多）状态为null
		Boolean otoStatus=true;
		Boolean miniStatus=true;
		Boolean otmStatus=true;
		Map<String, Object> params  = Maps.newHashMap();
		params.put("studentId", contract.getStudent().getId());
		String contractSql="select * from contract where STUDENT_ID= :studentId ";
		List<Contract> listContract=new ArrayList<Contract>();
		listContract=contractDao.findBySql(contractSql,params);
		if(listContract!=null && listContract.size()>0){
			for(Contract con:listContract){
				Set<ContractProduct> pros=con.getContractProducts();
				for(ContractProduct pro:pros){
					if(pro.getType()!=null && pro.getType()==ProductType.ONE_ON_MANY){
						otmStatus=false;
					}else if(pro.getType()!=null && pro.getType()==ProductType.ONE_ON_ONE_COURSE){
						otoStatus=false;
					}else if(pro.getType()!=null && pro.getType()==ProductType.SMALL_CLASS){
						miniStatus=false;
					}
				}
			}			
			Map<String, Object> param  = Maps.newHashMap();
			
			param.put("Id", contract.getStudent().getId());
			
			int num=0;
			StringBuffer sql=new StringBuffer();
			sql.append(" UPDATE student set ");
			if(otmStatus && contract.getStudent().getOneOnManyStatus()!=null){
				sql.append(" ONEONMANY_SATUS = NULL ");
				num++;
			}
			if(miniStatus && contract.getStudent().getSmallClassStatus()!=null){
				if(num>0){
					sql.append(" , ");
				}
				sql.append(" SMALL_CLASS_STATUS = NULL  ");
				num++;
			}
			if(otoStatus && contract.getStudent().getOneOnOneStatus()!=null){
				if(num>0){
					sql.append(" , ");
				}
				sql.append(" ONEONONE_STATUS = NULL  ");
				num++;
			}
			sql.append(" where ID= :Id ");
			if(num>0){
				studentDao.excuteSql(sql.toString(),param);
			}
		}

		studentService.updateStudentProductFirstTime(studentDao.findById(contractVo.getStuId()));

		/**
		 * 更新合同的待分配业绩
		 */
		Set<FundsChangeHistory> fundsChangeHistories = contract.getFundsChangeHistories();
		if (fundsChangeHistories.size()>0){
			for (FundsChangeHistory f : fundsChangeHistories) {
				updateFundsChangeHistoryNotAssigned(f.getId());
			}
		}

		if(isChangeContractType){//修改合同产品类型
			List<FundsChangeHistory> listFund=fundsChangeHistoryDao.getFundsChangeHistoryListByContractId(contractVo.getContractId());
			for (FundsChangeHistory fun:listFund){
				if(!(fun.getChannel().equals(PayWay.CASH)
						||fun.getChannel().equals(PayWay.POS)
						||fun.getChannel().equals(PayWay.WEB_CHART_PAY)
						||fun.getChannel().equals(PayWay.ALI_PAY)
						||fun.getChannel().equals(PayWay.BANK_TRANSFER))) {
					continue;
				}
				String f="2";
				String f2="1";
				if(fun.getFundsPayType().equals(FundsPayType.RECEIPT)){
					f="2";
					f2="1";
				}else{
					f="1";
					f2="2";
				}
				PushRedisMessageUtil.putMsgToQueueForFinance(fun, contract.getSignStaff(), oldType,f);
				PushRedisMessageUtil.putMsgToQueueForFinance(fun, contract.getSignStaff(), type,f2);
			}
		}

		BonusQueueUtils.pushToQueue(bonusMsg);//业绩修改队列
	}
    
    
    /**
     * 修改合同至付款完成，人工缩单操作
     * @param contract
     */
    public void modifyContractToPaid(Contract contract){
		contract.setPaidStatus(ContractPaidStatus.PAID);
		BigDecimal ooo = BigDecimal.ZERO;
		BigDecimal otm = BigDecimal.ZERO;
		BigDecimal mini = BigDecimal.ZERO;
		BigDecimal ecs = BigDecimal.ZERO;
		BigDecimal other = BigDecimal.ZERO;
		BigDecimal lecture = BigDecimal.ZERO;
		BigDecimal twoTeacher = BigDecimal.ZERO;
		BigDecimal liveAmount = BigDecimal.ZERO;
		
		int statusNum=0;
        // 修改合同后如果已付金额=合同总额时自动分配资金到产品
        for(ContractProduct conPrd : contract.getContractProducts()) {
			if (!ContractProductStatus.UNVALID.equals(conPrd.getStatus())){
				this.saveDefaultArrangeMonoyForProduct(conPrd);
				switch (conPrd.getType()){
					case ONE_ON_ONE_COURSE:
						ooo = ooo.add(conPrd.getRealAmount());
						break;
					case ONE_ON_MANY:
						otm = otm.add(conPrd.getRealAmount());
						break;
					case SMALL_CLASS:
						mini = mini.add(conPrd.getRealAmount());
						break;
					case ECS_CLASS:
						ecs = ecs.add(conPrd.getRealAmount());
						break;
					case OTHERS:
						other = other.add(conPrd.getRealAmount());
						break;
					case LECTURE:
						lecture = lecture.add(conPrd.getRealAmount());
						break;
					case TWO_TEACHER:
						twoTeacher = twoTeacher.add(conPrd.getRealAmount());
						break;
					case LIVE:
						liveAmount=liveAmount.add(conPrd.getRealAmount());
						break;
				}

				//修改合同时，如果合同下的合同产品都是结束或结课，则设置合同状态为FINISHED
				if(conPrd.getStatus() == ContractProductStatus.ENDED || conPrd.getStatus() == ContractProductStatus.CLOSE_PRODUCT){
					statusNum+=1;
				}
			}
        }
        if(statusNum == contract.getContractProducts().size()){
        	contract.setContractStatus(ContractStatus.FINISHED);
        }
		Map<String, Object> params  = Maps.newHashMap();
		params.put("contractId", contract.getId());
        List<FundsChangeHistory> changeHistories = fundsChangeHistoryDao.findAllByHQL(" from FundsChangeHistory f where f.channel <> 'PROMOTION_MONEY' and f.contract.id = :contractId ",params);
		
		/**
		 * 如果这个合同只有一条收款记录(排除合同中有优惠的)，可以帮用户自动分配资金 .优惠还有收款记录的
		 */
		List<Message> returnMsg=new ArrayList<>();
		if (changeHistories.size()==1){
			for (FundsChangeHistory f : changeHistories){
//				List<ContractBonus> bonuss = contractBonusDao.findByFundsChangeHistoryId(f.getId());
				List<IncomeDistribution> bonuss = incomeDistributionDao.findByFundsChangeHistoryId(f.getId());
				for (IncomeDistribution i : bonuss) {
//					contractBonusDao.delete(bonus);
					incomeDistributionDao.delete(i);
					if(i.getSubBonusDistributeType()==BonusDistributeType.USER_USER && !f.getChannel().equals(PayWay.ELECTRONIC_ACCOUNT)){//bonus.getBonusStaff()!=null
						//删掉业绩，也要去掉业绩排名的数据
						addBonusToMessage(i,returnMsg,"0");
					}
				}
				incomeDistributionDao.deleteByFundsChangeHistoryId(f.getId());
//				contractBonusDao.deleteByFundsChangeHistoryId(f.getId());
			}
			FundsChangeHistory f = changeHistories.iterator().next();
			Map<ProductType,BigDecimal> bonusMap=new HashMap<>();
			bonusMap.put(ProductType.ECS_CLASS,ecs);
			bonusMap.put(ProductType.ONE_ON_ONE_COURSE,ooo);
			bonusMap.put(ProductType.ONE_ON_MANY,otm);
			bonusMap.put(ProductType.SMALL_CLASS,mini);
			bonusMap.put(ProductType.OTHERS,other);
			bonusMap.put(ProductType.LECTURE,lecture);
			bonusMap.put(ProductType.TWO_TEACHER,twoTeacher);
			bonusMap.put(ProductType.LIVE,liveAmount);
			contractBonusArrange(bonusMap, contract.getSignStaff().getUserId(), contract.getBlCampusId(), f,returnMsg, contract);
		}

		BonusQueueUtils.pushToQueue(returnMsg);
    }
    


	@Override
	public void deleteOneFundRecordForPromotion(Contract contract) {
		fundsChangeHistoryDao.deletePromotionRecord(contract);
		fundsChangeHistoryDao.flush();
	}

	@Override
	public void deleteSubjectsByContractProduct(String controctProductId) {
		ContractProduct conProd = contractProductDao.findById(controctProductId);
		Set<ContractProductSubject> subs = conProd.getProdSubjects();
		conProd.setProdSubjects(null);
		for(ContractProductSubject sub : subs) {
			contractProductSubjectDao.delete(sub);
		}
	}

	@Override
	public ContractProductVo getContractProductById(String contractProductId) {
		ContractProduct conProd = contractProductDao.findById(contractProductId);
		if(conProd!=null) {
			ContractProductVo vo = HibernateUtils.voObjectMapping(conProd,ContractProductVo.class);
			BigDecimal oooSubjectDistributedHours = vo.getOooSubjectDistributedHours() != null ? 
						vo.getOooSubjectDistributedHours() : BigDecimal.ZERO;
			BigDecimal unDistributedMoney = conProd.getPaidAmount().subtract(conProd.getPrice().multiply(oooSubjectDistributedHours).setScale(2, BigDecimal.ROUND_DOWN));
			if (unDistributedMoney.compareTo(BigDecimal.ZERO) >= 0) {
				vo.setUnDistributedMoney(unDistributedMoney);
			} else {
				vo.setUnDistributedMoney(BigDecimal.ZERO);
			}
			return vo;
		}
		return null;
	}

	@Override
	public void closeContractProductForLive(String contractId, String liveId, LiveContractProductRefundVo liveContractProductRefundVo) throws Exception {
    	log.info("直播同步退费参数："+JSON.toJSONString(liveContractProductRefundVo));
		log.info("直播同步退费hashCode："+liveContractProductRefundVo.hashCode());
		ObjectHashCode objectHashCode = objectHashCodeDao.findByHashCode(liveContractProductRefundVo.hashCode());
		if (objectHashCode==null){
			ObjectHashCode hashCode = new ObjectHashCode();
			hashCode.setHashCode(liveContractProductRefundVo.hashCode());
			objectHashCodeDao.save(hashCode);
			LiveConProdHandler liveConProdHandler = (LiveConProdHandler)selectHandlerByType(ProductType.LIVE);
			List<ContractProduct> list = liveConProdHandler.getContractProductsForLiveContract(contractId, liveId);
			if (list.size()==1){
				ContractProduct contractProduct = list.get(0);
				if (contractProduct.getStatus() != ContractProductStatus.NORMAL) {
					throw new ApplicationException("重复的请求,合同产品是正常状态才可以做退款操作！");
				}
				Contract contract = contractProduct.getContract();
				String returnUserId = contract.getSignStaff().getUserId();
				String returnCampusId = contract.getBlCampusId();
				String contractProductId = contractProduct.getId();
				BigDecimal returnMoney = liveContractProductRefundVo.getReturnMoney();
//			BigDecimal amountFromPromotionAcc = BigDecimal.valueOf(Math.floor(contractProduct.getRealAmount().subtract(contractProduct.getRemainingAmountOfBasicAmount()).divide(contractProduct.getTotalAmount(), 2).multiply(contractProduct.getRemainingAmountOfPromotionAmount()).doubleValue()));
				BigDecimal returnSpecialAmount = liveContractProductRefundVo.getReturnSpecialAmount();
				BigDecimal returnAmountFromPromotionAcc = contractProduct.getPromotionAmount().subtract(returnSpecialAmount);
				String returnReason = liveContractProductRefundVo.getReturnReason();
				BigDecimal amount = returnMoney.add(returnSpecialAmount);
				IncomeDistributionVo campus = new IncomeDistributionVo();
				campus.setBaseBonusDistributeType("CAMPUS");
				campus.setSubBonusDistributeType("CAMPUS_CAMPUS");
				campus.setAmount(amount);
				campus.setBonusOrgId(returnCampusId);
				IncomeDistributionVo user = new IncomeDistributionVo();
				user.setBaseBonusDistributeType("USER");
				user.setSubBonusDistributeType("USER_USER");
				user.setBonusStaffId(returnUserId);
				user.setAmount(amount);
				List<IncomeDistributionVo> incomeDistributionVoList = new ArrayList<>();
				incomeDistributionVoList.add(campus);
				incomeDistributionVoList.add(user);
				User operateUser = userService.findUserByAccount(liveContractProductRefundVo.getOperateUser());
				String createUserId = null;
				if (operateUser!=null){
					createUserId = operateUser.getUserId();
				}else {
					createUserId = returnUserId;
				}


				this.closeContractProcuct(BigDecimal.ZERO, returnAmountFromPromotionAcc,
						contractProductId, true, returnMoney,
						returnSpecialAmount, returnReason, null,
						null, null, null,
						returnCampusId, createUserId, null, incomeDistributionVoList, liveContractProductRefundVo);
			}else {
				throw new ApplicationException("找不到合同产品");
			}


		}

	}

	@Override
	public void closeLiveContractProductToElectronic(LiveContractChangeVo liveContractChangeVo) throws Exception {
		LiveContractProductRefundVo refundVo = liveContractChangeVo.getLiveContractProductRefundVo();
		String contractId = refundVo.getContractId();
		String liveId = refundVo.getLiveId();
		LiveConProdHandler liveConProdHandler = (LiveConProdHandler)selectHandlerByType(ProductType.LIVE);
		List<ContractProduct> list = liveConProdHandler.getContractProductsForLiveContract(contractId, liveId);
		if (list.size()==1){
			ContractProduct contractProduct = list.get(0);
			if (contractProduct.getStatus() != ContractProductStatus.NORMAL) {
				throw new ApplicationException("重复的请求,合同产品是正常状态才可以做转班操作！");
			}
			String contractProductId = contractProduct.getId();
			BigDecimal returnMoney = contractProduct.getPaidAmount();//实退到电子账户  转移正常资金到学生电子账户
			BigDecimal returnAmountFromPromotionAcc = contractProduct.getPromotionAmount();// 划归收入  优惠对消
			BigDecimal amountFromPromotionAcc = BigDecimal.ZERO;//转移优惠资金到学生电子账户
			closeContractProcuct(amountFromPromotionAcc, returnAmountFromPromotionAcc, contractProductId, false,
					returnMoney, BigDecimal.ZERO,
					null, null,
					null, null, null,
					null, null, null, null, null);
		}else {
			throw new ApplicationException("找不到合同产品");
		}
	}

	/**
	 * @param amountFromPromotionAcc 退优惠金额
	 * @param returnAmountFromPromotionAcc 划归收入
	 * @param contractProductId
	 * @param isRefundMoney
	 * @param returnMoney 退实际金额
	 * @param returnReason 退费备注
	 * @param returnType 退费原因类型
	 * @param list
	 * @param liveContractProductRefundVo
	 */
	public void closeContractProcuct(BigDecimal amountFromPromotionAcc, BigDecimal returnAmountFromPromotionAcc, String contractProductId, Boolean isRefundMoney,
									 BigDecimal returnMoney, BigDecimal returnSpecialAmount,
									 String returnReason, String returnType,
									 MultipartFile certificateImageFile, String accountName, String account,
									 String returnCampusId, String returnUserId, String remark, List<IncomeDistributionVo> list, LiveContractProductRefundVo liveContractProductRefundVo) throws Exception{
		ContractProductHandler prodHandler =  new OneOnOneConProdHandler();
		ContractProduct contractProduct = contractProductDao.findById(contractProductId);
		if (contractProduct.getStatus() != ContractProductStatus.NORMAL) {
			throw new ApplicationException("合同产品已改变状态，不是正常状态，不可以做退款操作！");
		}
		BigDecimal maxReturnProAmount = BigDecimal.ZERO;
		if (contractProduct.getRemainingAmountOfBasicAmount().compareTo(BigDecimal.ZERO) > 0 
				&& contractProduct.getRemainingAmountOfPromotionAmount().compareTo(BigDecimal.ZERO) > 0 
				&& contractProduct.getRemainingAmountOfBasicAmount().compareTo(contractProduct.getRealAmount()) < 0) {
			maxReturnProAmount = BigDecimal.valueOf(Math.floor(contractProduct.getRealAmount().subtract(contractProduct.getRemainingAmountOfBasicAmount()).divide(contractProduct.getTotalAmount(), 2).multiply(contractProduct.getRemainingAmountOfPromotionAmount()).doubleValue()));
		}
		if (contractProduct.getRemainingAmountOfBasicAmount().compareTo(returnMoney) < 0 
				|| maxReturnProAmount.compareTo(amountFromPromotionAcc) < 0 
				|| contractProduct.getPaidAmount().compareTo(returnMoney.add(returnSpecialAmount)) < 0) {
			throw new ApplicationException("超过最大退费金额，请重新修改退费金额！");
		}
		
		//换为页面输入的金额  contractProduct.getRemainingAmountOfBasicAmount()
		returnMoney = returnMoney==null?BigDecimal.ZERO:returnMoney;
		returnSpecialAmount = returnSpecialAmount==null?BigDecimal.ZERO:returnSpecialAmount;
		amountFromPromotionAcc = amountFromPromotionAcc==null?BigDecimal.ZERO:amountFromPromotionAcc;
		
		if(isRefundMoney){
			String transactionId = UUID.randomUUID().toString();
			prodHandler.saveReturnAccRecord(contractProduct, contractProduct.getRemainingAmountOfBasicAmount(), amountFromPromotionAcc, false, transactionId);
		}
		prodHandler.completeContractProduct(contractProduct.getRemainingAmountOfBasicAmount(),amountFromPromotionAcc, returnAmountFromPromotionAcc, contractProduct,returnMoney);
		
		if(isRefundMoney) {
			//真正退给家长
			studentService.closeContractProduct(contractProduct.getContract().getStudent().getId(), returnMoney, returnSpecialAmount, amountFromPromotionAcc, returnMoney.add(amountFromPromotionAcc), returnReason, returnType, contractProductId,certificateImageFile,accountName,account, returnCampusId, returnUserId, list, liveContractProductRefundVo);
//					contractProduct.getPaidAmount().subtract(contractProduct.getConsumeAmount())   换为页面输入的金额
		} else {
			prodHandler.transferAmountToElectronicAdd(amountFromPromotionAcc, returnAmountFromPromotionAcc, contractProduct,returnMoney, remark);
		}

		/**
		 *  学生小班回电子账合同退费或者退户，晓培优需要同步退班
		 * 培优特有的逻辑
		 */
		if ("advance".equals(PropertiesUtils.getStringValue("institution"))){
			// 退费操作，同时退班
			MiniClass miniClass = smallClassService.getMiniClassByContractProduct(contractProduct);
			if(miniClass!=null) {
				smallClassService.deleteStudentInMiniClasss(contractProduct.getContract().getStudent().getId(), miniClass.getMiniClassId());
			}
		}
//		if(contractProduct.getType().equals(ProductType.SMALL_CLASS) || contractProduct.getType().equals(ProductType.ECS_CLASS)) {
//			smallClassService.deleteMiniStudentAttendent(contractProduct.getId());
//		}
		StudnetAccMv avv = getStudentAccoutInfo(contractProduct.getContract().getStudent().getId());//重新计算一下学生剩余资金
		studnetAccMvDao.save(avv);

		//学生状态处理
		PushRedisMessageUtil.pushStudentToMsg(contractProduct.getContract().getStudent().getId());

	}

	@Override
	public String changeCurriculum(LiveContractChangeVo liveContractChangeVo) throws Exception {
		log.info("直播同步转班参数："+JSON.toJSONString(liveContractChangeVo));
		log.info("直播同步转班hashCode："+liveContractChangeVo.hashCode());
		ObjectHashCode objectHashCode = objectHashCodeDao.findByHashCode(liveContractChangeVo.hashCode());
		if (objectHashCode==null){
			ObjectHashCode hashCode = new ObjectHashCode();
			hashCode.setHashCode(liveContractChangeVo.hashCode());

			objectHashCodeDao.save(hashCode);

			closeLiveContractProductToElectronic(liveContractChangeVo);
			ContractLiveVo contractVo = liveContractChangeVo.getContractLiveVo();

			ContractLiveVo c= liveContract(contractVo);
			//处理合同状态
            String conId = liveContractChangeVo.getLiveContractProductRefundVo().getContractId();
            Contract contract = contractDao.findById(conId);
            if(null != contract && (contract.getContractType() == ContractType.NEW_CONTRACT || contract.getContractType() == ContractType.RE_CONTRACT)) {
                c.setContractType(contract.getContractType());
            }

			String contractId = synchronizeLiveContract(c);
			hashCode.setContractId(contractId);
			FundsChangeHistoryVo fundsVo = new FundsChangeHistoryVo();
			fundsVo.setContractId(contractId);
			Set<ContractProductVo> set = c.getContractProductVos();
			Double changedAmount = 0.0;
			for (ContractProductVo vo : set) {
			    log.info("changedAmount:" + vo.getChangedAmount());
			    changedAmount += vo.getChangedAmount();
			}
			fundsVo.setTransactionAmount(changedAmount); // 课程转课实收金额
			fundsVo.setChargeByWho(c.getSignByWho());
			fundsVo.setChannel(PayWay.ELECTRONIC_ACCOUNT);
			fundsVo.setLiveReceiptTime(contractVo.getLiveReceiptTime());
			fundsVo.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
			fundsVo.setAuditStatusValue(FundsChangeAuditStatus.VALIDATE.getValue());//审核通过
			saveFundOfContract(fundsVo);
			return contractId;
		}else {
			return objectHashCode.getContractId();
		}

	}

	@Override
	public String saveLiveContract(ContractLiveVo contractVo) throws Exception {
		log.info("直播同步合同参数："+JSON.toJSONString(contractVo));
		log.info("直播同步合同hashCode："+contractVo.hashCode());
		ObjectHashCode objectHashCode = objectHashCodeDao.findByHashCode(contractVo.hashCode());
		if (objectHashCode==null){
			ObjectHashCode hashCode = new ObjectHashCode();
			hashCode.setHashCode(contractVo.hashCode());
			objectHashCodeDao.save(hashCode);
			ContractLiveVo c= liveContract(contractVo);
			String contractId = synchronizeLiveContract(c);
			hashCode.setContractId(contractId);

			FundsChangeHistoryVo fundsVo = new FundsChangeHistoryVo();
			fundsVo.setContractId(contractId);
			fundsVo.setTransactionAmount(c.getTransactionAmount());
			fundsVo.setChargeByWho(c.getSignByWho());
			if ("ALI_PAY".equals(contractVo.getPayWay())||"WEB_CHART_PAY".equals(contractVo.getPayWay())){
				fundsVo.setChannel(PayWay.valueOf(contractVo.getPayWay()));
			}else {
				fundsVo.setChannel(PayWay.CASH);
			}
			fundsVo.setLiveReceiptTime(contractVo.getLiveReceiptTime());
			fundsVo.setFundsChangeType(FundsChangeType.SYSTEM);//系统录入
			fundsVo.setAuditStatusValue(FundsChangeAuditStatus.VALIDATE.getValue());//审核通过
			fundsVo.setAuditType(FundsChangeAuditType.SYSTEM);// #1898系统审核
			saveFundOfContract(fundsVo);

			return contractId;
		}else {
			return objectHashCode.getContractId();
		}
	}

	/**
	 * 查询学生帐户情况, 除电子账户外，其他均实时计算
	 * 
	 * @param studentId
	 * @return
	 */
	public StudnetAccMv getStudentAccoutInfo(String studentId) {
		StudnetAccMv account = studnetAccMvDao
				.getStudnetAccMvByStudentId(studentId);
		if (account == null) {
			account = new StudnetAccMv();
		}
		// StudnetAccMvVo accVo = HibernateUtils.voObjectMapping(account,
		// StudnetAccMvVo.class);

		// 查询所有有效合同产品
		List<Criterion> produceCriterionList = new ArrayList<Criterion>();
		produceCriterionList.add(Restrictions.eq("contract.student.id",
				studentId));
		// produceCriterionList.add(Expression.in("status", new
		// ContractProductStatus[]{ContractProductStatus.NORMAL,
		// ContractProductStatus.STARTED}));
		List<ContractProduct> allContractProduces = contractProductDao
				.findAllByCriteria(produceCriterionList);

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal paidAmount = BigDecimal.ZERO;
		BigDecimal promotinAmount = BigDecimal.ZERO;
		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal remainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
		BigDecimal oneOnOnePromotinAmount = BigDecimal.ZERO;
		BigDecimal miniRemainingAmount = BigDecimal.ZERO;
		BigDecimal miniRemainingHour = BigDecimal.ZERO;
		BigDecimal otherConsumeAmount = BigDecimal.ZERO;
		BigDecimal ecsRemainingAmount = BigDecimal.ZERO;
		if (allContractProduces.size() > 0) {
			for (ContractProduct cp : allContractProduces) {
				if (cp.getPromotionAmount() == null)
					cp.setPromotionAmount(BigDecimal.ZERO);
				// paidAmount = paidAmount.add(cp.getPaidAmount());
				consumeAmount = consumeAmount.add(cp.getConsumeAmount());
				remainingAmount = remainingAmount.add(cp.getRemainingAmount());
				promotinAmount = promotinAmount.add(cp.getPromotionAmount());
				// totalAmount = totalAmount.add(cp.getPlanAmount());
				switch (cp.getType()) {
				case ONE_ON_ONE_COURSE:
					oneOnOneRemainingAmount = oneOnOneRemainingAmount.add(cp
							.getRemainingAmount());
					if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
						oneOnOneRemainingHour = oneOnOneRemainingHour.add(cp
								.getRemainingAmount().divide(cp.getPrice(), 2,
										RoundingMode.HALF_UP));
					}
					oneOnOnePromotinAmount = oneOnOnePromotinAmount.add(cp
							.getPromotionAmount());
					break;
				case SMALL_CLASS:
					miniRemainingAmount = miniRemainingAmount.add(cp
							.getRemainingAmount());
					if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
						miniRemainingHour = miniRemainingHour.add(cp
								.getRemainingAmount().divide(cp.getPrice(), 2,
										RoundingMode.HALF_UP));
					}
					break;
				case ECS_CLASS:
					ecsRemainingAmount = ecsRemainingAmount.add(cp
							.getRemainingAmount());
					break;
				case OTHERS:
					otherConsumeAmount = otherConsumeAmount.add(cp
							.getRemainingAmount());
					break;
				}
			}
			// totalAmount = totalAmount.add(paidAmount).add(promotinAmount);

			// accVo.setTotalAmount(totalAmount);
			// accVo.setPaidAmount(paidAmount);
			account.setRemainingAmount(remainingAmount);
			account.setOneOnOneRemainingAmount(oneOnOneRemainingAmount);
			account.setOneOnOneRemainingHour(oneOnOneRemainingHour);
			account.setMiniRemainingAmount(miniRemainingAmount);
			// account.setMiniRemainingHour(miniRemainingHour);
			account.setOtherConsumeAmount(otherConsumeAmount);
			 account.setEcsRemainingAmount(ecsRemainingAmount);
			// account.setPromotinAmount(promotinAmount);
		}

		// 统计待付款
		List<Criterion> contractCriterionList = new ArrayList<Criterion>();
		contractCriterionList.add(Restrictions.eq("student.id", studentId));
		List<Contract> contarctList = contractDao
				.findAllByCriteria(contractCriterionList);
		BigDecimal totalPaddingAmount = BigDecimal.ZERO;
		BigDecimal availableAmount = BigDecimal.ZERO;
		for (Contract contract : contarctList) {
			calculateContractDomain(contract);
			if (contract.getPendingAmount().compareTo(BigDecimal.ZERO) > 0) {
				totalPaddingAmount = totalPaddingAmount.add(contract
						.getPendingAmount());
			}
			totalAmount = totalAmount.add(contract.getTotalAmount());
			paidAmount = paidAmount.add(contract.getPaidAmount());
		}
		account.setPaidAmount(paidAmount);
		account.setTotalAmount(totalAmount);
		// account.setAvailableAmount(availableAmount);
		// account.setArrangedAmount(totalPaddingAmount.subtract(availableAmount));
		// account.setPlanAmount(totalPaddingAmount);

		return account;
	}
	
	@Override
	public void calculateContractDomainJdbc(Contract contract) {
		// 更新合同Domain 的 不同值
	
		List<ContractProduct> conProds = contractProductDao.getContractProductByContractId(contract.getId());
		BigDecimal productTotalAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneTotalAmount = BigDecimal.ZERO;
		BigDecimal miniClassTotalAmount = BigDecimal.ZERO;
		BigDecimal oneOnManyTotalAmount = BigDecimal.ZERO;
		BigDecimal otherTotalAmount = BigDecimal.ZERO;
		BigDecimal promiseClassTotalAmount = BigDecimal.ZERO;
		BigDecimal promotionAmount = BigDecimal.ZERO;
		BigDecimal arrangedAmount = BigDecimal.ZERO;
		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal remainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
		BigDecimal lectureClassTotalAmount = BigDecimal.ZERO;

		for(ContractProduct conProdInLoop : conProds) {
			if (conProdInLoop.getStatus() == ContractProductStatus.UNVALID) continue;
			switch (conProdInLoop.getType() ){
			case ONE_ON_ONE_COURSE : 
				oneOnOneRemainingHour = oneOnOneRemainingHour.add(conProdInLoop.getRemainingAmount().divide(conProdInLoop.getPrice(),2,RoundingMode.HALF_UP));
				oneOnOneTotalAmount = oneOnOneTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case SMALL_CLASS :  
				miniClassTotalAmount = miniClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case OTHERS:  
				otherTotalAmount = otherTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case ECS_CLASS:  
				promiseClassTotalAmount = promiseClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case ONE_ON_MANY:
				oneOnManyTotalAmount = oneOnManyTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				break;
			case LECTURE:
				lectureClassTotalAmount = lectureClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				break;
			}
			//  合同产品的 剩余资金要 等于 “实际支付” - “已经支付”， 不能用total Amount， 因为totalAmount 是用于计算 promotion的金额。
			productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
			promotionAmount = promotionAmount.add(conProdInLoop.getPromotionAmount());
			arrangedAmount = arrangedAmount.add(conProdInLoop.getPaidAmount());
			consumeAmount = consumeAmount.add(conProdInLoop.getConsumeAmount());
			remainingAmount = remainingAmount.add(conProdInLoop.getRemainingAmount());
		}
			
		
		contract.setTotalAmount(productTotalAmount);
		contract.setOneOnOneTotalAmount(oneOnOneTotalAmount);
		contract.setMiniClassTotalAmount(miniClassTotalAmount);
		contract.setOneOnManyTotalAmount(oneOnManyTotalAmount);
		contract.setOtherTotalAmount(otherTotalAmount);
		contract.setLectureClassTotalAmount(lectureClassTotalAmount);//讲座金额
		contract.setPromiseClassTotalAmount(promiseClassTotalAmount);
		
		contract.setPromotionAmount(promotionAmount);
		contract.setConsumeAmount(consumeAmount);
		contract.setRemainingAmount(remainingAmount);

		contract.setOneOnOneRemainingHour(oneOnOneRemainingHour);
		
		contract.setAvailableAmount(contract.getPaidAmount().subtract(arrangedAmount));
		contractDao.flush();
			
	}

	@Override
	public void calculateContractDomain(Contract contract) {
		// 更新合同Domain 的 不同值
		Set<ContractProduct> conProds = contract.getContractProducts();
		BigDecimal productTotalAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneTotalAmount = BigDecimal.ZERO;
		BigDecimal miniClassTotalAmount = BigDecimal.ZERO;
		BigDecimal oneOnManyTotalAmount = BigDecimal.ZERO;
		BigDecimal otherTotalAmount = BigDecimal.ZERO;
		BigDecimal promiseClassTotalAmount = BigDecimal.ZERO;
		BigDecimal promotionAmount = BigDecimal.ZERO;
		BigDecimal arrangedAmount = BigDecimal.ZERO;
		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal remainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
		BigDecimal lectureClassTotalAmount = BigDecimal.ZERO;
		BigDecimal twoTeacherClassTotalAmount = BigDecimal.ZERO;
		BigDecimal liveAmount=BigDecimal.ZERO;

		for(ContractProduct conProdInLoop : conProds) {
			if (conProdInLoop.getStatus() == ContractProductStatus.UNVALID) continue;
			switch (conProdInLoop.getType() ){
			case ONE_ON_ONE_COURSE : 
				oneOnOneRemainingHour = oneOnOneRemainingHour.add(conProdInLoop.getRemainingAmount().divide(conProdInLoop.getPrice(),2,RoundingMode.HALF_UP));
				oneOnOneTotalAmount = oneOnOneTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case SMALL_CLASS :  
				miniClassTotalAmount = miniClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case OTHERS:  
				otherTotalAmount = otherTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case ECS_CLASS:  
				promiseClassTotalAmount = promiseClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount()); 
				break;
			case ONE_ON_MANY:
				oneOnManyTotalAmount = oneOnManyTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				break;
			case LECTURE:
				lectureClassTotalAmount = lectureClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				break;
			case TWO_TEACHER:
				twoTeacherClassTotalAmount=twoTeacherClassTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				break;
			case LIVE:
				liveAmount=liveAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
				break;
			}
			//  合同产品的 剩余资金要 等于 “实际支付” - “已经支付”， 不能用total Amount， 因为totalAmount 是用于计算 promotion的金额。
			productTotalAmount = productTotalAmount.add(conProdInLoop.getPromotionAmount()).add(conProdInLoop.getRealAmount());
			promotionAmount = promotionAmount.add(conProdInLoop.getPromotionAmount());
			arrangedAmount = arrangedAmount.add(conProdInLoop.getPaidAmount());
			consumeAmount = consumeAmount.add(conProdInLoop.getConsumeAmount());
			remainingAmount = remainingAmount.add(conProdInLoop.getRemainingAmount());
		}
			
		
		contract.setTotalAmount(productTotalAmount);
		contract.setOneOnOneTotalAmount(oneOnOneTotalAmount);
		contract.setMiniClassTotalAmount(miniClassTotalAmount);
		contract.setOneOnManyTotalAmount(oneOnManyTotalAmount);
		contract.setOtherTotalAmount(otherTotalAmount);
		contract.setLectureClassTotalAmount(lectureClassTotalAmount);//讲座金额
		contract.setPromiseClassTotalAmount(promiseClassTotalAmount);
		contract.setTwoTeacherClassTotalAmount(twoTeacherClassTotalAmount);
		contract.setPromotionAmount(promotionAmount);
		contract.setConsumeAmount(consumeAmount);
		contract.setRemainingAmount(remainingAmount);

		contract.setOneOnOneRemainingHour(oneOnOneRemainingHour);
		contract.setLiveAmount(liveAmount);
		
		contract.setAvailableAmount(contract.getPaidAmount().subtract(arrangedAmount));
		contractDao.flush();
			
	}
	
	
	@Override
	public List<ContractProductVo> getEscProductByCampus(String miniClassId) {
		List<ContractProductVo> returnList= new ArrayList<ContractProductVo>();
		MiniClassVo smallClass = smallClassService.findMiniClassById(miniClassId);
		List<ContractProductVo> productVos=HibernateUtils.voListMapping(contractProductDao.getEscProductByCampus(smallClass.getBlCampusId(),miniClassId), ContractProductVo.class);
		for (Iterator iterator = productVos.iterator(); iterator.hasNext();) {
			Object o =iterator.next();
			ContractProductVo contractProductVo = (ContractProductVo) o;
			List<PromiseStudent> proStudent = promiseStudentDao.getPromiseStudentByContractPro(contractProductVo.getId());
			if(proStudent.size()==0){//没有报目标班的合同产品，去掉不返回    新增需求 #277
				 continue;
			 }
			 
			PromiseStudent promiseStudent=proStudent.get(0);
			contractProductVo.setEscClassId(promiseStudent.getPromiseClass().getId());
			contractProductVo.setEscClassName(promiseStudent.getPromiseClass().getpName());
			contractProductVo.setTeacherName(promiseStudent.getPromiseClass().getHead_teacher().getName());
			returnList.add(contractProductVo);
			
		}
		return returnList;
	}

	
	@Override
	public List<Map<String, String>> getEscProductMiniClassId(String miniClassId) {
		MiniClassVo smallClass = smallClassService.findMiniClassById(miniClassId);
		List<Map<Object, Object>> list =contractProductDao.getCanUseEcsContractProduct(smallClass.getBlCampusId(), miniClassId);
	    
		List<Map<String, String>> result = new LinkedList<>();
		for(Map<Object, Object> map:list){
	    	result.add((Map)map);
	    }
		return result;
	}
	@Override
	public List<ContractMobileVo> findContractForMobile(String studentId, DataPackage dp) {
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("studentId", studentId);
		StringBuffer hql = new StringBuffer();
		hql.append( "from Contract as contract ");
		
		hql.append(" where 1=1");
		
		hql.append(" and student.id = :studentId ");
		
		//这个应该不需要，因为是查询这个学生的，如果可以看到这个学生应该就可以
//		hql.append(roleQLConfigService.getValueResult("合同列表", "hql"));
		
		hql.append(" order by createTime desc");
		
		dp =  contractDao.findPageByHQL(hql.toString(), dp,true,params);

		List<Contract> contractList =  (List) dp.getDatas();
		for(Contract contract: contractList){
			this.calculateContractDomain(contract);
		}
		
		List<ContractMobileVo> contractMobileVoList = new ArrayList<ContractMobileVo>();
		String blCampusName="";
		for(Contract contract : contractList){
			
			if(contract.getBlCampusId()!=null){
				Organization org=new Organization();
				org=organizationDao.findById(contract.getBlCampusId());
				blCampusName=org.getName();
			}else{
				blCampusName="-";
			}		
			try {
				ContractVo vo = HibernateUtils.voObjectMapping(contract, ContractVo.class);
				vo.setBlCampusName(blCampusName);
				ContractMobileVo mobileVo = HibernateUtils.voObjectMapping(vo, ContractMobileVo.class, "withContractForMobile");
				mobileVo.setContractProductVos(
						HibernateUtils.voListMapping(
						HibernateUtils.voListMapping(contract.getContractProducts(),ContractProductVo.class)
						, ContractProductMobileVo.class));
				contractMobileVoList.add(mobileVo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return contractMobileVoList;
	}
	

	public ContractMobileVo findContractByIdForMobile(String contractId) {
		StringBuffer hql = new StringBuffer();
		hql.append( "from Contract as contract ");
		
		hql.append(" where 1=1 ");
		
		Map<String, Object> params = Maps.newHashMap();
		
		if(!StringUtil.isBlank(contractId))
		{
			hql.append(" and id = :contractId ");
			params.put("contractId", contractId);
		}
		
		//这个应该不需要，因为是查询这个学生的，如果可以看到这个学生应该就可以
//		hql.append(roleQLConfigService.getValueResult("合同列表", "hql"));
		
		hql.append(" order by createTime desc");
		

		
		Contract contract = contractDao.findAllByHQL(hql.toString(),params).get(0);
		
		if(contract != null)
		{
			this.calculateContractDomain(contract);
		}else
		{
			throw new ApplicationException("找不到该合同信息！");
		}
		
		ContractMobileVo mobileVo = new ContractMobileVo();
		String blCampusName="";
		if(contract.getBlCampusId()!=null){
			Organization org=new Organization();
			org=organizationDao.findById(contract.getBlCampusId());
			blCampusName=org.getName();
		}else{
			blCampusName="-";
		}		
		try {
			ContractVo vo = HibernateUtils.voObjectMapping(contract, ContractVo.class);
			vo.setBlCampusName(blCampusName);
			mobileVo = HibernateUtils.voObjectMapping(vo, ContractMobileVo.class, "withContractForMobile");
			
			mobileVo.setContractProductVos(
					HibernateUtils.voListMapping(
					HibernateUtils.voListMapping(contract.getContractProducts(),ContractProductVo.class)
					, ContractProductMobileVo.class));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mobileVo;
	}

	@Override
	public DataPackage findPageContract(DataPackage dp, ContractVo contractVo, TimeVo timeVo) {
		
		Map<String, Object> params = new HashMap<>();
		
		StringBuffer sql=new StringBuffer();
		StringBuffer sqlWhere=new StringBuffer();
		sql.append(" select c.* ");
		sql.append(" from contract c LEFT JOIN business_assoc_mapping bam ON c.ID = bam.business_id  ");
		sql.append(" left join organization o on c.bl_campus_id = o.id");
		sql.append(" LEFT JOIN data_dict dd_g on c.GRADE_ID = dd_g.ID LEFT JOIN user u on c.SIGN_STAFF_ID = u.USER_ID, student stu ");
		sqlWhere.append(" where  c.STUDENT_ID=stu.id    ");
		if (contractVo.getSaleChannel() != null) {
		    if (contractVo.getSaleChannel() == SaleChannel.ON_LINE) {
		        sqlWhere.append(" AND bam.id IS NOT NULL ");
		    } else {
		        sqlWhere.append(" AND bam.id IS NULL ");
		    }
		}
		if (StringUtils.isNotBlank(timeVo.getStartDate())) {
			sqlWhere.append(" and c.CREATE_TIME >= :startDate ");
			params.put("startDate", timeVo.getStartDate());
		}
		if (StringUtils.isNotBlank(timeVo.getEndDate())) {
			sqlWhere.append(" and c.CREATE_TIME < :endDate ");
			params.put("endDate", DateTools.addDateToString(timeVo.getEndDate(), 1));
		}
		if (contractVo.getContractType()!=null && StringUtils.isNotBlank(contractVo.getContractType().getName())) {
			sqlWhere.append(" and c.CONTRACT_TYPE = :contractType ");
			params.put("contractType", contractVo.getContractType());
		}
		if (contractVo.getContractStatus()!=null && StringUtils.isNotBlank(contractVo.getContractStatus().getName())) {
			sqlWhere.append(" and c.CONTRACT_STATUS = :contractStatus ");
			params.put("contractStatus", contractVo.getContractStatus());
		}
		
		if (contractVo.getPaidStatus()!=null && StringUtils.isNotBlank(contractVo.getPaidStatus().getName())) {
			sqlWhere.append(" and c.PAID_STATUS = :paidStatus ");
			params.put("paidStatus", contractVo.getPaidStatus());
		}
		
		if (StringUtils.isNotBlank(contractVo.getCusName())) {
			sql.append(" , customer cus ");
			sqlWhere.append(" and c.CUSTOMER_ID=cus.id and cus.NAME like :cusName ");
			params.put("cusName", "%"+contractVo.getCusName()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getCusPhone())) {
			if(sql.indexOf("customer")<0){
				sql.append(" , customer cus ");
				sqlWhere.append(" and c.CUSTOMER_ID=cus.id ");
			}
			sqlWhere.append(" and cus.CONTACT like :cusContact ");
			params.put("cusContact", "%"+contractVo.getCusPhone()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getGradeId())) {
			sqlWhere.append(" and c.GRADE_ID = :gradeId ");
			params.put("gradeId", contractVo.getGradeId());
		}
		if (StringUtils.isNotBlank(contractVo.getStuName())) {
			sqlWhere.append(" and stu.name like :stuName ");
			params.put("stuName", "%"+contractVo.getStuName()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getBlCampusId())) {
			sqlWhere.append(" and c.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", contractVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(contractVo.getSignByWho())) {
//			sql.append(" ,`user` u ");
			sqlWhere.append(" and c.SIGN_STAFF_ID=u.USER_ID and u.name like :uName ");
			params.put("uName", "%"+contractVo.getSignByWho()+"%");		
		}
		if(StringUtils.isNotBlank(contractVo.getAvailable())){
			sql.append(" ,(select cpp.contract_id,SUM(cpp.paid_amount) paidAmount from contract_product cpp,contract c where cpp.contract_id=c.id   ");
			if (StringUtils.isNotBlank(timeVo.getStartDate())) {
				sql.append(" and c.CREATE_TIME >= :startDate2 ");
				params.put("startDate2", timeVo.getStartDate());
			}
			if (StringUtils.isNotBlank(timeVo.getEndDate())) {
				sql.append(" and c.CREATE_TIME < :endDate2 ");
				params.put("endDate2", DateTools.addDateToString(timeVo.getEndDate(), 1));
			}
			sql.append(" GROUP BY cpp.CONTRACT_ID ) cp ");
			sqlWhere.append(" and c.id=cp.contract_id and c.PAID_AMOUNT-cp.paidAmount>0 ");			
		}
		if(StringUtils.isNotEmpty(contractVo.getContractId())){
			sqlWhere.append(" and c.id like :cId ");
			params.put("cId", "%"+contractVo.getContractId()+"%");		
		}
		

//		sqlWhere.append(roleQLConfigService.getValueResult("合同列表","sql"));
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","o.orgLevel");
		sqlMap.put("signUser","c.SIGN_STAFF_ID");
		sqlMap.put("stuId","c.student_id");
		sqlMap.put("manegerId","stu.STUDY_MANEGER_ID");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("合同列表","nsql","sql");
		sqlWhere.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		sqlWhere.append(" order by c.CREATE_TIME desc");
		
//		dp = jdbcTemplateDao.queryPage(sql.toString()+sqlWhere.toString(), ContractJdbc.class, dp, true);
		dp =  contractDao.findPageBySql(sql.toString()+sqlWhere.toString(), dp,true,params);
		
//		List<ContractJdbc> contractJdbcList =  (List) dp.getDatas();
		
//		List<Contract> contractList = HibernateUtils.voListMapping(contractJdbcList, Contract.class, "contractJdbc");
		
		List<Contract> contractList =  (List) dp.getDatas();
		

		for(Contract contract: contractList){
			System.out.println(contract.getStudent().getName() +"#" +contract.getId());
			this.calculateContractDomain(contract);
//			this.calculateContractDomainJdbc(contract);
		}
		String contractIds = "";
		Map<String, ContractVo> contractMap = Maps.newHashMap();
		List<ContractVo> contractVoList = new ArrayList<ContractVo>();
		String blCampusName="";
		String stuBlCampusName="";
		for(Contract contract : contractList){


			if(contract.getBlCampusId()!=null){
				Organization org=new Organization();
				org=organizationDao.findById(contract.getBlCampusId());
				blCampusName=org.getName();
			}else{
				blCampusName="-";
			}

			if (contract.getStudent().getBlCampus()!=null){
				stuBlCampusName =  contract.getStudent().getBlCampus().getName();
			}else {
				stuBlCampusName = "-";
			}
			try {
				ContractVo vo = HibernateUtils.voObjectMapping(contract, ContractVo.class);
				List<FundsChangeHistory> fundList = fundsChangeHistoryDao.getFundsChangeHistoryListByContractId(contract.getId());
				if (accountChargeRecordsDao.hasChargeRecord(contract)) {
					vo.setIsCharged("TRUE");
				} else {
					vo.setIsCharged("FALSE");
				}
				if (fundList != null && fundList.size() > 0) {
					vo.setIsReceipted("TRUE");
				} else {
					vo.setIsReceipted("FALSE");
				}
				vo.setBlCampusName(blCampusName);
				vo.setStuBlCampusName(stuBlCampusName);
				vo.setSaleChannel(SaleChannel.OFF_LINE);

				//是否包含2019及以后年份的目标班产品合同
				if (contract.isEcsContract()){
					if (checkHasContractProductAfter2018(contract)){
						vo.setHasEcsCPAfter2018(true);
					}else {
						vo.setHasEcsCPAfter2018(false);
					}
				}


				contractVoList.add(vo);
				contractIds += contract.getId() + ",";
				contractMap.put(contract.getId(), vo);
			} catch (Exception e) {
				e.printStackTrace();
			}




		}

		if (com.eduboss.utils.StringUtil.isNotBlank(contractIds)) {
		    contractIds = contractIds.substring(0, contractIds.length() - 1);
		    List<BusinessAssocMapping> list = businessAssocMappingService.listBusinessAssocMappingByBusinessIds(contractIds.split(","));
		    for (BusinessAssocMapping mapping : list) {
		        contractMap.get(mapping.getBusinessId()).setSaleChannel(SaleChannel.ON_LINE);
		    }
		}

		//优惠排序 rdc 736737
		if ("promotionAmount".equals(dp.getSidx())){
			if ("desc".equals(dp.getSord())){
				Collections.sort(contractVoList, new Comparator<Object>(){
					@Override
					public int compare(Object o1, Object o2) {
						ContractVo c1 = (ContractVo) o1;
						ContractVo c2 = (ContractVo) o2;

						BigDecimal result = c1.getPromotionAmount().subtract(c2.getPromotionAmount());
						if (result.compareTo(BigDecimal.ZERO)>0){
							return -1;
						}else if(result.compareTo(BigDecimal.ZERO)<0){
							return 1;
						}else{
							return 0;
						}

					}
				});
			}else {
				Collections.sort(contractVoList, new Comparator<Object>(){
					@Override
					public int compare(Object o1, Object o2) {
						ContractVo c1 = (ContractVo) o1;
						ContractVo c2 = (ContractVo) o2;
						BigDecimal result = c1.getPromotionAmount().subtract(c2.getPromotionAmount());
						if (result.compareTo(BigDecimal.ZERO)>0){
							return 1;
						}else if(result.compareTo(BigDecimal.ZERO)<0){
							return -1;
						}else{
							return 0;
						}
					}
				});
			}




		}


		dp.setDatas(contractVoList);
		return dp;
	}

	//是否包含2019及以后年份的目标班产品合同
	private boolean checkHasContractProductAfter2018(Contract contract) {
		Set<ContractProduct> contractProducts = contract.getContractProducts();

		boolean flag = false;

		for (ContractProduct cp : contractProducts){
			if (promiseClassService.isYearAfter2018(cp)){
				flag = true;
				break;
			}
		}

		return flag;
	}

	@Override
	public List findExcelContract(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo) {
		Map<String, Object> params = new HashMap<>();

		StringBuffer sql=new StringBuffer();
		sql.append(" select");
		sql.append(" 			c.id contractId,co.name blCampusName,stu.`NAME` stuName,so.`name` stuBlCampusName,dd_g.`NAME` contractGrade,");
		sql.append(" c.CREATE_TIME signTime,c.PAID_AMOUNT paidAmount,");
		sql.append(" c.CONTRACT_TYPE contractTypeName,c.CONTRACT_STATUS contractStatusName,c.PAID_STATUS paidStatusName,u.`NAME` signByWho,c.isNarrow");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='ONE_ON_ONE_COURSE' ) oneOnOneTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='SMALL_CLASS' ) miniClassTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='ECS_CLASS' )promiseClassTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='OTHERS' )otherTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='ONE_ON_MANY' )oneOnManyTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='LECTURE' )lectureTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='TWO_TEACHER' )twoTeacherTotalAmount");
		sql.append(" ,(select ifnull(sum(cp.REAL_AMOUNT),0) from contract_product cp where cp.CONTRACT_ID=c.ID and cp.TYPE='LIVE' )liveTotalAmount");
		sql.append(" ,(select sum(cp.PROMOTION_AMOUNT) from contract_product cp where cp.CONTRACT_ID=c.ID  )promotionAmount");
		sql.append(" ,(select sum(cp.PLAN_AMOUNT) from contract_product cp where cp.CONTRACT_ID=c.ID  )totalAmount");
		sql.append(" ,c.PAID_AMOUNT-(select sum(cp.PAID_AMOUNT) from contract_product cp where cp.CONTRACT_ID=c.ID  ) availableAmount");
		sql.append(" ,(select sum(cp.REAL_AMOUNT) from contract_product cp where cp.CONTRACT_ID=c.ID  )-c.paid_amount pendingAmount");
		sql.append("     from");
		sql.append(" 		contract c ");
		sql.append(" 		left join student stu on c.STUDENT_ID=stu.id    ");
		sql.append(" 		left join organization co on c.BL_CAMPUS_ID= co.id");
		sql.append(" 		left join organization so on stu.BL_CAMPUS_ID= so.id");
		sql.append("     LEFT JOIN data_dict dd_g  on c.GRADE_ID = dd_g.ID ");
		sql.append("     LEFT JOIN user u  on c.SIGN_STAFF_ID = u.USER_ID");
		sql.append("     LEFT JOIN customer cus  on c.CUSTOMER_ID=cus.id");

		StringBuffer sqlWhere=new StringBuffer();
		sqlWhere.append(" where  1=1   ");
		if (StringUtils.isNotBlank(timeVo.getStartDate())) {
			sqlWhere.append(" and c.CREATE_TIME >= :startDate ");
			params.put("startDate", timeVo.getStartDate());
		}
		if (StringUtils.isNotBlank(timeVo.getEndDate())) {
			sqlWhere.append(" and c.CREATE_TIME < :endDate ");
			params.put("endDate", DateTools.addDateToString(timeVo.getEndDate(), 1));
		}
		if (contractVo.getContractType()!=null && StringUtils.isNotBlank(contractVo.getContractType().getName())) {
			sqlWhere.append(" and c.CONTRACT_TYPE = :contractType ");
			params.put("contractType", contractVo.getContractType());
		}
		if (contractVo.getContractStatus()!=null && StringUtils.isNotBlank(contractVo.getContractStatus().getName())) {
			sqlWhere.append(" and c.CONTRACT_STATUS = :contractStatus ");
			params.put("contractStatus", contractVo.getContractStatus());
		}

		if (contractVo.getPaidStatus()!=null && StringUtils.isNotBlank(contractVo.getPaidStatus().getName())) {
			sqlWhere.append(" and c.PAID_STATUS = :paidStatus ");
			params.put("paidStatus", contractVo.getPaidStatus());
		}

		if (StringUtils.isNotBlank(contractVo.getCusName())) {
			sqlWhere.append(" and cus.NAME like :cusName ");
			params.put("cusName", "%"+contractVo.getCusName()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getCusPhone())) {
			sqlWhere.append(" and cus.CONTACT like :cusContact ");
			params.put("cusContact", "%"+contractVo.getCusPhone()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getGradeId())) {
			sqlWhere.append(" and c.GRADE_ID = :gradeId ");
			params.put("gradeId", contractVo.getGradeId());
		}
		if (StringUtils.isNotBlank(contractVo.getStuName())) {
			sqlWhere.append(" and stu.name like :stuName ");
			params.put("stuName", "%"+contractVo.getStuName()+"%");
		}
		if (StringUtils.isNotBlank(contractVo.getBlCampusId())) {
			sqlWhere.append(" and c.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", contractVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(contractVo.getSignByWho())) {
			sqlWhere.append(" and c.SIGN_STAFF_ID=u.USER_ID and u.name like :uName ");
			params.put("uName", "%"+contractVo.getSignByWho()+"%");
		}
		if(StringUtils.isNotBlank(contractVo.getAvailable())){
			sql.append(" ,(select cpp.contract_id,SUM(cpp.paid_amount) paidAmount from contract_product cpp,contract c where cpp.contract_id=c.id   ");
			if (StringUtils.isNotBlank(timeVo.getStartDate())) {
				sql.append(" and c.CREATE_TIME >= :startDate2 ");
				params.put("startDate2", timeVo.getStartDate());
			}
			if (StringUtils.isNotBlank(timeVo.getEndDate())) {
				sql.append(" and c.CREATE_TIME < :endDate2 ");
				params.put("endDate2", DateTools.addDateToString(timeVo.getEndDate(), 1));
			}
			sql.append(" GROUP BY cpp.CONTRACT_ID ) cp ");
			sqlWhere.append(" and c.id=cp.contract_id and c.PAID_AMOUNT-cp.paidAmount>0 ");
		}
		if(StringUtils.isNotEmpty(contractVo.getContractId())){
			sqlWhere.append(" and c.id like :cId ");
			params.put("cId", "%"+contractVo.getContractId()+"%");
		}


		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","co.orgLevel");
		sqlMap.put("signUser","c.SIGN_STAFF_ID");
		sqlMap.put("stuId","c.student_id");
		sqlMap.put("manegerId","stu.STUDY_MANEGER_ID");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("合同列表","nsql","sql");
		sqlWhere.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));
		sqlWhere.append(" order by c.CREATE_TIME desc");

		return  contractDao.findMapBySql(sql.toString()+sqlWhere.toString(), params);
	}


	@Override
	public DataPackage findPageContractRecord(DataPackage dp,
			String contractId) {
		Map<String, Object> params = new HashMap<>();
		params.put("contractId", contractId);
		String hql="from ContractRecord where contract.id= :contractId order by createTime desc ";
		dp =  contractDao.findPageByHQL(hql, dp,true,params);
		List<ContractRecord> contractRecordList =  (List) dp.getDatas();
		List<ContractRecordVo> contractRecordVoList = new ArrayList<ContractRecordVo>();
		String blCampusName="";
		for(ContractRecord contractRecord : contractRecordList){			
			if(contractRecord.getBlCampusId()!=null){
				Organization org=new Organization();
				org=organizationDao.findById(contractRecord.getBlCampusId());
				blCampusName=org.getName();
			}else{
				blCampusName="-";
			}		
			try {
				ContractRecordVo vo = HibernateUtils.voObjectMapping(contractRecord, ContractRecordVo.class);
				vo.setBlCampusName(blCampusName);					
				contractRecordVoList.add(vo);				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		dp.setDatas(contractRecordVoList);
		return dp;
	}



	private ContractAlreadyFenpei getContractAlreadyFenpei(ContractAlreadyFenpei c ){
		//本次合同校区业绩分配
		BigDecimal	oooCampus = BigDecimal.ZERO;
		BigDecimal	miniCampus = BigDecimal.ZERO;
		BigDecimal	otmCampus = BigDecimal.ZERO;
		BigDecimal	ecsCampus = BigDecimal.ZERO;
		BigDecimal	otherCampus = BigDecimal.ZERO;
		BigDecimal  lectureCampus = BigDecimal.ZERO;
		BigDecimal liveCampus=BigDecimal.ZERO;

		//本次合同用户业绩分配
		BigDecimal 	oooUser = BigDecimal.ZERO;
		BigDecimal	miniUser = BigDecimal.ZERO;
		BigDecimal	otmUser = BigDecimal.ZERO;
		BigDecimal	ecsUser = BigDecimal.ZERO;
		BigDecimal	otherUser = BigDecimal.ZERO;
		BigDecimal  lectureUser = BigDecimal.ZERO;
		BigDecimal liveUser = BigDecimal.ZERO;

		/**
		 * 本合同的已分配业绩
		 */
//		List<ContractBonus> list = contractBonusDao.findByContractIdExeptFundId(c.getContractId(), null,null);

		List<IncomeDistribution> list = incomeDistributionDao.findIncomeDistributionByContractId(c.getContractId());

		for (IncomeDistribution i : list){
			if (BasicOperationQueryLevelType.CAMPUS.equals(i.getBaseBonusDistributeType())){
				BigDecimal campusAmount = i.getAmount()!= null?i.getAmount() : BigDecimal.ZERO;
				if (ProductType.ONE_ON_ONE_COURSE.equals(i.getProductType())){
					oooCampus=oooCampus.add(campusAmount);
				}else if (ProductType.SMALL_CLASS.equals(i.getProductType())){
					miniCampus = miniCampus.add(campusAmount);
				}else if (ProductType.ONE_ON_MANY.equals(i.getProductType())){
					otmCampus = otmCampus.add(campusAmount);
				}else if (ProductType.ECS_CLASS.equals(i.getProductType())){
					ecsCampus = ecsCampus.add(campusAmount);
				}else if (ProductType.OTHERS.equals(i.getProductType())){
					otherCampus=otherCampus.add(campusAmount);
				}else if (ProductType.LECTURE.equals(i.getProductType())){
					lectureCampus = lectureCampus.add(campusAmount);
				}else if (ProductType.LIVE.equals(i.getProductType())){
					liveCampus = liveCampus.add(campusAmount);
				}
			}else if (BasicOperationQueryLevelType.USER.equals(i.getBaseBonusDistributeType())){
				BigDecimal userAmount = i.getAmount()!= null?i.getAmount() : BigDecimal.ZERO;

				if (ProductType.ONE_ON_ONE_COURSE.equals(i.getProductType())){
					oooUser=oooUser.add(userAmount);
				}else if (ProductType.SMALL_CLASS.equals(i.getProductType())){
					miniUser = miniUser.add(userAmount);
				}else if (ProductType.ONE_ON_MANY.equals(i.getProductType())){
					otmUser = otmUser.add(userAmount);
				}else if (ProductType.ECS_CLASS.equals(i.getProductType())){
					ecsUser = ecsUser.add(userAmount);
				}else if (ProductType.OTHERS.equals(i.getProductType())){
					otherUser=otherUser.add(userAmount);
				}else if (ProductType.LECTURE.equals(i.getProductType())){
					lectureUser = lectureUser.add(userAmount);
				}else if (ProductType.LIVE.equals(i.getProductType())){
					liveUser = liveUser.add(userAmount);
				}
			}
		}


//		for (ContractBonus con : list) {
//			if(con.getOrganization()!=null && con.getType()!=null){//绩效校区不为空就是 校区业绩
//				BigDecimal campusAmount = con.getCampusAmount() != null ? con.getCampusAmount() : BigDecimal.ZERO;
//				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
//					oooCampus=oooCampus.add(campusAmount);
//				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
//					miniCampus=miniCampus.add(campusAmount);
//				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
//					otmCampus=otmCampus.add(campusAmount);
//				}else if(con.getType().equals(ProductType.ECS_CLASS)){
//					ecsCampus=ecsCampus.add(campusAmount);
//				}else if(con.getType().equals(ProductType.OTHERS)){
//					otherCampus=otherCampus.add(campusAmount);
//				}else if (con.getType().equals(ProductType.LECTURE)){
//					lectureCampus=lectureCampus.add(campusAmount);
//				}
//			}else if(con.getType()!=null){//其他就是用户业绩
//				BigDecimal bonusAmount = con.getBonusAmount() != null ? con.getBonusAmount() : BigDecimal.ZERO;
//				if(con.getType().equals(ProductType.ONE_ON_ONE_COURSE)){
//					oooUser=oooUser.add(bonusAmount);
//				}else if(con.getType().equals(ProductType.SMALL_CLASS)){
//					miniUser=miniUser.add(bonusAmount);
//				}else if(con.getType().equals(ProductType.ONE_ON_MANY)){
//					otmUser=otmUser.add(bonusAmount);
//				}else if(con.getType().equals(ProductType.ECS_CLASS)){
//					ecsUser=ecsUser.add(bonusAmount);
//				}else if(con.getType().equals(ProductType.OTHERS)){
//					otherUser=otherUser.add(bonusAmount);
//				}else if (con.getType().equals(ProductType.LECTURE)){
//					lectureUser = lectureUser.add(bonusAmount);
//				}
//			}
//		}

		c.setOooCampus(oooCampus);
		c.setMiniCampus(miniCampus);
		c.setOtmCampus(otmCampus);
		c.setEcsCampus(ecsCampus);
		c.setOtherCampus(otherCampus);
		c.setLectureCampus(lectureCampus);
		c.setLiveCampus(liveCampus);

		c.setOooUser(oooUser);
		c.setMiniUser(miniUser);
		c.setOtmUser(otmUser);
		c.setEcsUser(ecsUser);
		c.setOtherUser(otherUser);
		c.setLectureUser(lectureUser);
		c.setLiveUser(liveUser);
		return c;
	}



	private ContractEachProductTypeMoney getEachProductTypeMoney(ContractEachProductTypeMoney c){

		 BigDecimal oneMoney = BigDecimal.ZERO;
		 BigDecimal miniMoney =BigDecimal.ZERO;
		 BigDecimal otmMoney=BigDecimal.ZERO;
		 BigDecimal ecsMoney=BigDecimal.ZERO;
		 BigDecimal otherMoney=BigDecimal.ZERO;
		BigDecimal lectureMoney=BigDecimal.ZERO;
		BigDecimal twoTeacherMoney=BigDecimal.ZERO;
		BigDecimal liveMoney=BigDecimal.ZERO;

		/**
		 *
		 * 获取合同
		 * 得到每个合同产品的可分配金额
		 */
		Contract contract = contractDao.findById(c.getContractId());
		for (ContractProduct contractProduct : contract.getContractProducts()){
			if (contractProduct.getType()==ProductType.ONE_ON_MANY){
				otmMoney = otmMoney.add(contractProduct.getPaidAmount());
				c.setExitOneOnMany(true);
			}else if (contractProduct.getType()==ProductType.SMALL_CLASS){
				miniMoney = miniMoney.add(contractProduct.getPaidAmount());
				c.setExitSmallClass(true);
			}else if (contractProduct.getType()==ProductType.ONE_ON_ONE_COURSE){
				oneMoney= oneMoney.add(contractProduct.getPaidAmount());
				c.setExitOneOnOne(true);
			}else if (contractProduct.getType()==ProductType.ECS_CLASS){
				ecsMoney = ecsMoney.add(contractProduct.getPaidAmount());
				c.setExitEcsClass(true);
			}else if (contractProduct.getType()==ProductType.OTHERS){
				otherMoney=otherMoney.add(contractProduct.getPaidAmount());
				c.setExitOthers(true);
			}else if (contractProduct.getType()==ProductType.LECTURE){
				lectureMoney = lectureMoney.add(contractProduct.getPaidAmount());
				c.setExitLecture(true);
			}else if (contractProduct.getType() == ProductType.TWO_TEACHER){
				twoTeacherMoney = twoTeacherMoney.add(contractProduct.getPaidAmount());
				c.setExitTwoTeacher(true);
			}else if (contractProduct.getType() == ProductType.LIVE){
				liveMoney = liveMoney.add(contractProduct.getPaidAmount());
				c.setExitLive(true);
			}
		}

		c.setOneMoney(oneMoney);
		c.setMiniMoney(miniMoney);
		c.setOtmMoney(otmMoney);
		c.setEcsMoney(ecsMoney);
		c.setOtherMoney(otherMoney);
		c.setLectureMoney(lectureMoney);
		c.setTwoTeacherMoney(twoTeacherMoney);
		c.setLiveMoney(liveMoney);

		return c;
	}
	
	/**
	 * 获取学生已考勤未扣费课程
	 * @param studentId
	 * @param contractProductId
	 * @return
	 */
	@Override
	public Response getCourseByStuPro(String studentId,String contractProductId){
		ContractProduct contPro=contractProductDao.findById(contractProductId);
		String proId=contPro.getProduct().getId();
		Response res=new Response();
		StringBuffer sql=new StringBuffer();
		
		Map<String, Object> params  = new HashMap<>();
		params.put("studentId", studentId);
		int small=0;
		int one=0;
		int many=0;
		if(contPro!=null){
			if(contPro.getType()!=null){				
				if(ProductType.SMALL_CLASS==contPro.getType()){
					sql.append(" SELECT mcc.* FROM mini_class_student_attendent mcsa,mini_class_course mcc,mini_class mc , mini_class_student mcs");
					sql.append("  where mcsa.MINI_CLASS_COURSE_ID=mcc.MINI_CLASS_COURSE_ID and mcc.MINI_CLASS_ID=mc.MINI_CLASS_ID AND mcs.MINI_CLASS_ID= mc.mini_class_id and ");
					sql.append(" mcsa.STUDENT_ID= :studentId and mcc.COURSE_STATUS != 'CANCEL' and mcsa.CHARGE_STATUS='UNCHARGE' and mcsa.ATTENDENT_STATUS!='NEW' and mc.PRODUCE_ID= :proId and mcs.CONTRACT_PRODUCT_ID= :contractProductId ");
					sql.append("  ORDER BY mcc.COURSE_DATE ASC ");
					params.put("proId", proId);
					params.put("contractProductId", contractProductId);
					
					small+=1;
				}else if(ProductType.ECS_CLASS==contPro.getType()){     
					sql.append(" SELECT mcc.* FROM ");
					sql.append("  mini_class_student_attendent mcsa, mini_class_course mcc ");
					sql.append("  where  mcsa.MINI_CLASS_COURSE_ID=mcc.MINI_CLASS_COURSE_ID ");
					sql.append("  and  mcsa.STUDENT_ID= :studentId and mcc.COURSE_STATUS != 'CANCEL'  and mcsa.CHARGE_STATUS='UNCHARGE'  and mcsa.ATTENDENT_STATUS!='NEW' ");
					sql.append("  ORDER BY mcc.COURSE_DATE ASC ");
					small+=1;
				}else if(ProductType.ONE_ON_ONE_COURSE==contPro.getType()){
					sql.append(" select * from course where ");
					sql.append(" STUDENT_ID= :studentId and PRODUCT_ID= :proId  ");
					params.put("proId", proId);
					sql.append(" and COURSE_STATUS != 'CANCEL' and ( COURSE_STATUS='TEACHER_ATTENDANCE' or COURSE_STATUS='STUDY_MANAGER_AUDITED') ");
					sql.append("  ORDER BY COURSE_DATE ASC ");
					one+=1;
				}else if(ProductType.ONE_ON_MANY==contPro.getType()){							 
					sql.append(" select occ.* from otm_class_student_attendent ocsa,otm_class_course occ,otm_class oc ");
					sql.append("  where ocsa.OTM_CLASS_COURSE_ID=occ.OTM_CLASS_COURSE_ID and oc.OTM_CLASS_ID=occ.OTM_CLASS_ID and ");
					sql.append("  ocsa.STUDENT_ID= :studentId and occ.COURSE_STATUS != 'CANCEL' and ocsa.CHARGE_STATUS='UNCHARGE' and ocsa.ATTENDENT_STATUS!='NEW' ");
					sql.append("  and oc.OTM_TYPE = (SELECT ONE_ON_MANY_TYPE from product where id= :proId ) ");
					params.put("proId", proId);
					sql.append("  ORDER BY occ.COURSE_DATE ASC ");
					many+=1;
				}else{				
					res.setResultCode(0);
					return res;
				}
			}else{
				throw new ApplicationException("该合同产品没有对应的产品类型");
			}
		}else{
			throw new ApplicationException("找不到改合同产品");
		}

		if(many>0){
			List<OtmClassCourse> manyList=new ArrayList<OtmClassCourse>();
			manyList=otmClassCourseDao.findBySql(sql.toString(),params);
			if(manyList!=null && manyList.size()>0){
				//存在已考勤未结算课程
				res.setResultCode(-1);
				res.setResultMessage(manyList.get(0).getCourseDate());
			}else{
				res.setResultCode(0);
			}
		}else if(one>0){
			List<Course> courseList=new ArrayList<Course>();
			courseList=courseDao.findBySql(sql.toString(),params);
			if(courseList!=null && courseList.size()>0){
				//存在已考勤未结算课程
				res.setResultCode(-1);
				res.setResultMessage(courseList.get(0).getCourseDate());
			}else{
				res.setResultCode(0);
			}
		}else if(small>0){
			List<MiniClassCourse> miniList=new ArrayList<MiniClassCourse>();
			miniList=miniClassCourseDao.findBySql(sql.toString(),params);
			if(miniList!=null && miniList.size()>0){
				//存在已考勤未结算课程
				res.setResultCode(-1);
				res.setResultMessage(miniList.get(0).getCourseDate());
			}else{
				res.setResultCode(0);
			}
		}
		
		return res;
	}
	
	@Override
	public Response narrowContractProduct(String contractId) {
		Response res=new Response();

			Contract con=contractDao.findById(contractId);
			for(ContractProduct cp:con.getContractProducts()){
				ContractProductHandler prodHandler = this.selectHandlerByType(cp.getType());
				prodHandler.narrowContractProduct(cp);
			}
			
			con.setPaidStatus(ContractPaidStatus.PAID);
			if(con.isEcsContract()){//目标班合同设为完结
				con.setContractStatus(ContractStatus.FINISHED);
			}




			con.setIsNarrow(1);
			contractDao.save(con);

			calculateContractDomain(con);


			updateContractStatus(con);// 合同的 优惠支付状态的更新， 每个合同应该只有一条优惠的记录
			// 先删除
			deleteOneFundRecordForPromotion(con);
			// 再出入
			// 要优惠数据大于0才插入数据
			if(con.getPromotionAmount().compareTo(BigDecimal.ZERO)>0) {
				insertOneFundRecord(con, con.getPromotionAmount(), con.getStudent(), PayWay.PROMOTION_MONEY);
			}


			//收款保存记录
			ContractRecord cr=new ContractRecord(con);
			cr.setUpdateType(UpdateType.UPDATE);
			cr.setCreateByStaff(userService.getCurrentLoginUser());
			contractRecordDao.save(cr);

		return res;
	}

	private void updateContractStatus(Contract con) {


		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT * FROM contract_product WHERE CONTRACT_ID= '"+con.getId()+"' ");

		List<ContractProduct> contractProducts = contractProductDao.findBySql(sql.toString(), new HashMap<>());

		int zeroContractProduct = 0;

		for (ContractProduct cp : contractProducts){
			if (cp.getRemainingAmount().compareTo(BigDecimal.ZERO) == 0 ){
				cp.setStatus(ContractProductStatus.ENDED);
				contractProductDao.save(cp);
				zeroContractProduct++;
			}
		}

		if (zeroContractProduct == contractProducts.size()){
			con.setContractStatus(ContractStatus.FINISHED);
			contractDao.save(con);
		}

	}

	/**
	 * 取消合同产品
	 */
	@Override
	public void unvalidContractProduct(String contractProductId) {
		ContractProduct cp = contractProductDao.findById(contractProductId);
		if (!(cp.getStatus() == ContractProductStatus.NORMAL && cp.getPaidAmount().compareTo(BigDecimal.ZERO) == 0)) {
			throw new ApplicationException("合同产品没符合可取消状态（已收款金额为0的正常合同）");
		}
		Contract contract = cp.getContract();
		// 交给最后去判断
		/*this.calculateContractDomain(contract);
		if(contract.getPaidAmount().compareTo(contract.getTotalAmount().subtract(cp.getRealAmount()).subtract(contract.getPromotionAmount()))>0){
			throw new ApplicationException("合同产品不能取消，实付资金不能低于已付资金");
		}*/
		cp.setPaidStatus(ContractProductPaidStatus.CANCELED);
		cp.setStatus(ContractProductStatus.UNVALID);
		if (cp.getType() == ProductType.SMALL_CLASS) {
			String miniClassId = cp.getMiniClassId();
			if (com.eduboss.utils.StringUtil.isBlank(cp.getMiniClassId())) {
				MiniClassVo miniClassVo = smallClassService.getMiniClassByContractProductId(contractProductId);
				if(miniClassVo!=null && StringUtils.isNotBlank(miniClassVo.getMiniClassId())) {
					miniClassId = miniClassVo.getMiniClassId();
				}
			}
			try {
				if(StringUtils.isNotBlank(miniClassId)) {
					smallClassService.deleteStudentInMiniClasss(contract.getStudent().getId(), miniClassId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		contractProductDao.merge(cp);
//		this.calculateContractDomain(cp.getContract());
	}
	
	/**
	 * 关闭合同
	 */
	@Override
	public void unvalidContract(String contractId){
		Contract contract = contractDao.findById(contractId);
		if (contract.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
			throw new ApplicationException("合同已付款金额大于0，不可以关闭合同。");
		}
		Set<ContractProduct> cpSet =contract.getContractProducts();
		for (ContractProduct cp : cpSet) {
			cp.setPaidStatus(ContractProductPaidStatus.CANCELED);
			cp.setStatus(ContractProductStatus.UNVALID);
			contractProductDao.merge(cp);
		}
		contract.setPaidStatus(ContractPaidStatus.CANCELED);
		contract.setContractStatus(ContractStatus.UNVALID);
		ContractRecord cr=new ContractRecord(contract);
		cr.setUpdateType(UpdateType.UNVALID);
		cr.setCreateByStaff(userService.getCurrentLoginUser());
		contractRecordDao.save(cr);
		contractDao.merge(contract);
	}
	
	/**
	 * 删除合同
	 */
	@Override
	public void deleteContract(String contractId, String reason)  throws Exception {
		initDataDeleteService.deleteInitData(contractId,reason);
		Contract contract = contractDao.findById(contractId);
		if(contract!=null && contract.getCustomer()!=null){
			Customer customer = contract.getCustomer();
			customer.setDealStatus(CustomerDealStatus.FOLLOWING);
			customerDao.save(customer);
		}
		
	}

	/**
	 * 分页查询一对一合同产品
	 */
	@Override
	public DataPackage getMyOooContractProductList(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo, String isAllDistributed) {
		dataPackage = contractProductDao.getMyOooContractProductList(dataPackage, contractVo, timeVo, isAllDistributed);
		List<ContractProduct> list = (List<ContractProduct>)dataPackage.getDatas();
		List<ContractProductVo> voList = new ArrayList<ContractProductVo>();
		ContractProductVo vo = null;
		for (ContractProduct cp : list) {
			vo = HibernateUtils.voObjectMapping(cp, ContractProductVo.class);
			if (cp.getContract().getStudent() != null) {
				vo.setGradeName(cp.getContract().getStudent().getGradeDict().getName());
			}
			vo.setTotalHours(cp.getTotalAmount().divide(cp.getPrice(), 2, BigDecimal.ROUND_HALF_UP));
//			BigDecimal availableAmount = BigDecimal.ZERO;
//			if (cp.getPaidStatus() == ContractProductPaidStatus.PAID) {
//				BigDecimal promotionAmount = cp.getPromotionAmount() != null ? cp.getPromotionAmount() : BigDecimal.ZERO;
//				availableAmount = cp.getPaidAmount().add(promotionAmount);
//			} else {
//				availableAmount = cp.getPaidAmount();
//			}
//			BigDecimal availableHours = availableAmount != null && cp.getPrice() != null && cp.getPrice().compareTo(BigDecimal.ZERO) > 0 ? 
//					availableAmount.divide(cp.getPrice(), 2, BigDecimal.ROUND_HALF_DOWN) : BigDecimal.ZERO;
//			vo.setAvailableHours(availableHours);
			
			BigDecimal availableAmount = BigDecimal.ZERO;
			availableAmount = cp.getRemainingAmount().add(cp.getConsumeAmount());
			BigDecimal availableHours = availableAmount != null && cp.getPrice() != null && cp.getPrice().compareTo(BigDecimal.ZERO) > 0 ? 
					availableAmount.divide(cp.getPrice(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
			vo.setAvailableHours(availableHours);
			
			BigDecimal distributedHours = cp.getOooSubjectDistributedHours() != null ? cp.getOooSubjectDistributedHours() : BigDecimal.ZERO;
			BigDecimal distributableHours = availableHours.subtract(distributedHours);
			vo.setDistributableHours(distributableHours);
			if(cp.getContract().getSignStaff()!=null) {
				vo.setSignByWhoName(cp.getContract().getSignStaff().getName());
			}
			Organization org = organizationDao.findById(cp.getContract().getBlCampusId());
			vo.setBlCampusName(org.getName());
			vo.setSignTime(cp.getContract().getCreateTime());
			voList.add(vo);
		}
		
		dataPackage.setDatas(voList);
		return dataPackage;
	}
	
	/**
	 * 查询一对一课时分配合同产品
	 */
	@Override
	public ContractProductDistributeVo findContractProductDistributeVoById(String contractProductId) {
		ContractProduct conProd = contractProductDao.findById(contractProductId);
		ContractProductDistributeVo vo = HibernateUtils.voObjectMapping(conProd, ContractProductDistributeVo.class);
		vo.setSubjectIds(conProd.getProduct().getSubjectIds());
		vo.setTotalHours(conProd.getTotalAmount().divide(conProd.getPrice(), 2));
		vo.setPromotionHours(conProd.getPromotionAmount().divide(conProd.getPrice(), 2));
		vo.setConsumeHours(conProd.getConsumeAmount().divide(conProd.getPrice(), 2));
		vo.setRemark(conProd.getContract().getRemark());
		vo.setStudentId(conProd.getContract().getStudent().getId());
		List<StudentOrganizationVo> orgList = studentService.getStudentOrganization(conProd.getContract().getStudent().getId());
		boolean containOrgId = false;
		for (StudentOrganizationVo orgVo : orgList) {
			if (orgVo.getOrganization().equals(conProd.getContract().getBlCampusId())) {
				containOrgId = true;
				break;
			}
		}
		if (containOrgId) {
			vo.setBlCampusId(conProd.getContract().getBlCampusId());
		} else {
			vo.setBlCampusId(conProd.getContract().getStudent().getBlCampusId());
		}
		
		BigDecimal availableAmount = BigDecimal.ZERO;
		if (conProd.getPaidStatus() == ContractProductPaidStatus.PAID) {
			BigDecimal promotionAmount = conProd.getPromotionAmount() != null ? conProd.getPromotionAmount() : BigDecimal.ZERO;
			availableAmount = conProd.getPaidAmount().add(promotionAmount);
		} else {
			availableAmount = conProd.getPaidAmount();
		}
		BigDecimal availableHours = availableAmount != null && conProd.getPrice() != null && conProd.getPrice().compareTo(BigDecimal.ZERO) > 0 ? 
				availableAmount.divide(conProd.getPrice(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
				
		vo.setAvailableHours(availableHours);
		BigDecimal distributedHours = BigDecimal.ZERO;
		List<ContractProductSubject> cpSubjectList = contractProductSubjectService.findContractProductSubjectByCpId(conProd.getId());
		if (cpSubjectList != null && cpSubjectList.size() > 0) {
			for (ContractProductSubject cpSubject : cpSubjectList) {
				BigDecimal quantity = cpSubject.getQuantity() != null ? cpSubject.getQuantity() : BigDecimal.ZERO;
				distributedHours = distributedHours.add(quantity);
			}
			List<ContractProductSubjectVo> cpSubjectVoList = HibernateUtils.voListMapping(cpSubjectList, ContractProductSubjectVo.class);
			vo.setContractProductSubVos(cpSubjectVoList);
		}
		vo.setDistributedHours(distributedHours);
		BigDecimal distributableHours = availableHours.subtract(distributedHours);
		vo.setDistributableHours(distributableHours);
		return vo;
	}
	
	/**
	 * 保存或修改一对一课时分配合同产品
	 */
	@Override
	public void editContractProductDistribute(ContractProductDistributeVo contractProductDistributeVo) {
		User currentUser = userService.getCurrentLoginUser();
		String currrentTime = DateTools.getCurrentDateTime();
		List<ContractProductSubjectVo> cpSubjectVoList = contractProductDistributeVo.getContractProductSubVos();
		List<ContractProductSubject> cpSubjectList = contractProductSubjectService.findContractProductSubjectByCpId(contractProductDistributeVo.getId());
		Map<String, ContractProductSubject> cpSubjectMap = new HashMap<String, ContractProductSubject>();
		for(ContractProductSubject cpSubject : cpSubjectList){
			cpSubjectMap.put(cpSubject.getId(), cpSubject);
			boolean exists = false; // 课时科目是否存在于界面传回的vo集合中
			for(ContractProductSubjectVo vo: cpSubjectVoList) {
				if(cpSubject.getId().equals(vo.getId())){
					exists = true;
				}
			}
			if(!exists){ // 该课时科目在界面中被删除
				if (cpSubject.getConsumeHours() != null && cpSubject.getConsumeHours().compareTo(BigDecimal.ZERO) > 0) {
					throw new ApplicationException("不可以删除已发生消耗的科目");
				}
				Organization cpSubjectOrg = cpSubject.getBlCampus();
				CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProductDistributeVo.getId(), cpSubject.getSubject(), DistributeType.TRANSFER_OUT,
						cpSubject.getQuantity(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, currentUser, currrentTime, cpSubjectOrg);
				courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
				contractProductSubjectService.deleteContractProductSubject(cpSubject);
				contractProductDao.flush();
			}
		}
		
		BigDecimal totalDistributedHours = BigDecimal.ZERO;
		String distributedHoursStr = "";
		for (ContractProductSubjectVo vo : cpSubjectVoList) {
			DataDict subject = dataDictDao.findById(vo.getSubjectId());
			distributedHoursStr += subject.getName() + ":" + vo.getQuantity() + ",";
			totalDistributedHours =totalDistributedHours.add(vo.getQuantity());
			Organization voOrg = StringUtils.isNotBlank(vo.getBlCampusId()) ? new Organization(vo.getBlCampusId()) : null;
			if (StringUtils.isNotBlank(vo.getId())) { // 数据库中有，变更课程科目
				ContractProductSubject cpSubject = cpSubjectMap.get(vo.getId());
				int compareResult = vo.getQuantity().compareTo(cpSubject.getQuantity());
				if (compareResult == 0) { //相同没改变
					continue;
				} else if (compareResult > 0) { // 增加了
					CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProductDistributeVo.getId(), cpSubject.getSubject(), DistributeType.TRANSFER_IN,
							vo.getQuantity().subtract(cpSubject.getQuantity()), vo.getQuantity(), cpSubject.getConsumeHours(), vo.getQuantity().subtract(cpSubject.getConsumeHours()), currentUser, currrentTime, voOrg);
					courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
					cpSubject.setModifyTime(currrentTime);
					cpSubject.setModifyUser(currentUser);
					
					cpSubject.setBlCampus(voOrg);
					cpSubject.setQuantity(vo.getQuantity());
					contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
				} else { //减少了
					if (vo.getQuantity().compareTo(cpSubject.getConsumeHours()) < 0) {
						throw new ApplicationException("分配课时不可以少于已消耗课时！");
					}
					CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProductDistributeVo.getId(), cpSubject.getSubject(), DistributeType.TRANSFER_OUT,
							cpSubject.getQuantity().subtract(vo.getQuantity()), vo.getQuantity(), cpSubject.getConsumeHours(), vo.getQuantity().subtract(cpSubject.getConsumeHours()), currentUser, currrentTime, voOrg);
					courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
					cpSubject.setModifyTime(currrentTime);
					cpSubject.setModifyUser(currentUser);
					cpSubject.setBlCampus(voOrg);
					cpSubject.setQuantity(vo.getQuantity());
					contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
				}
			} else { //数据库没有，新增课程科目
				ContractProductSubject cpSubject = HibernateUtils.voObjectMapping(vo, ContractProductSubject.class);
				cpSubject.setBlCampus(voOrg);
				cpSubject.setConsumeHours(BigDecimal.ZERO);
				cpSubject.setCreateTime(currrentTime);
				cpSubject.setCreateByStaff(currentUser);
				CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProductDistributeVo.getId(), cpSubject.getSubject(), DistributeType.TRANSFER_IN,
						cpSubject.getQuantity(), vo.getQuantity(), BigDecimal.ZERO, cpSubject.getQuantity(), currentUser, currrentTime, voOrg);
				courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
				cpSubject.setModifyTime(currrentTime);
				cpSubject.setModifyUser(currentUser);
				contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
			}
		}
		distributedHoursStr = distributedHoursStr.substring(0, distributedHoursStr.length() - 1);
		
		ContractProduct cp = contractProductDao.findById(contractProductDistributeVo.getId());
		cp.setOooSubjectDistributedHours(totalDistributedHours);
		cp.setOooSubjectDistributedHoursDes(distributedHoursStr);
		contractProductDao.merge(cp);
	}
	
	/**
	 * 分配、提取一对一课时
	 */
	@Override
	public synchronized void distributeOrExtractContractProductSubject(ContractProductDistributeVo contractProductDistributeVo) {
		if (!(contractProductDistributeVo.getDistributeType() == DistributeType.DISTRIBUTION) 
				&& !(contractProductDistributeVo.getDistributeType() == DistributeType.EXTRACT)) {
			throw new ApplicationException("不支持非分配或提取操作");
		}
		ContractProduct cp = contractProductDao.findById(contractProductDistributeVo.getId());
		BigDecimal totalDistributedHours = cp.getOooSubjectDistributedHours() != null ? cp.getOooSubjectDistributedHours() : BigDecimal.ZERO;
		String distributedHoursStr = cp.getOooSubjectDistributedHoursDes() != null ? cp.getOooSubjectDistributedHoursDes() : "";
		List<ContractProductSubjectVo> cpSubjectVoList = contractProductDistributeVo.getContractProductSubVos();
		if (cpSubjectVoList.size() <= 0) return;
		String currrentTime = DateTools.getCurrentDateTime();
		User currentUser = userService.getCurrentLoginUser();
		boolean distributed = false;
		if (contractProductDistributeVo.getDistributeType() == DistributeType.DISTRIBUTION) {
			distributed = true;
		}
		Organization voOrg = null;
		BigDecimal quantity = BigDecimal.ZERO;
		BigDecimal remainHours = BigDecimal.ZERO;
		for (ContractProductSubjectVo vo : cpSubjectVoList) {
			voOrg = StringUtils.isNotBlank(vo.getBlCampusId()) ? new Organization(vo.getBlCampusId()) : null;
			ContractProductSubject cpSubject = null;
			if (com.eduboss.utils.StringUtil.isNotBlank(vo.getId())) {
				cpSubject = contractProductSubjectService.findById(vo.getId());
			} else {
				cpSubject = HibernateUtils.voObjectMapping(vo, ContractProductSubject.class);
				cpSubject.setContractProduct(new ContractProduct(contractProductDistributeVo.getId()));
				cpSubject.setQuantity(BigDecimal.ZERO);
				cpSubject.setConsumeHours(BigDecimal.ZERO);
				cpSubject.setCreateTime(currrentTime);
				cpSubject.setCreateByStaff(currentUser);
			}
			if (distributed) { // 分配
				totalDistributedHours = totalDistributedHours.add(vo.getTransactionHours());
				quantity = cpSubject.getQuantity().add(vo.getTransactionHours());
			} else { // 提取
				quantity = cpSubject.getQuantity().subtract(vo.getTransactionHours());
				totalDistributedHours = totalDistributedHours.subtract(vo.getTransactionHours());
			}
			remainHours = quantity.subtract(cpSubject.getConsumeHours());
			if (remainHours.compareTo(BigDecimal.ZERO) < 0) {
			    throw new ApplicationException("已消耗课时已被变更，不足完成这次提取。");
			}
			if (vo.getTransactionHours().compareTo(BigDecimal.ZERO) > 0) { // 分配，提取课时大于0才产生流水和变更
				// 产生流水
				CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProductDistributeVo.getId(), cpSubject.getSubject(), 
						contractProductDistributeVo.getDistributeType(), vo.getTransactionHours(), quantity, cpSubject.getConsumeHours(), 
						remainHours, currentUser, currrentTime, voOrg);
				courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
				if (!distributed && quantity.compareTo(BigDecimal.ZERO) == 0) {
					contractProductSubjectService.deleteContractProductSubject(cpSubject);
				} else {
					cpSubject.setModifyTime(currrentTime);
					cpSubject.setModifyUser(currentUser);
					cpSubject.setBlCampus(voOrg);
					cpSubject.setQuantity(quantity);
					contractProductSubjectService.saveOrUpdateContractProductSubject(cpSubject);
				}
				DataDict subject = dataDictDao.findById(vo.getSubjectId());
				int subjectStart = distributedHoursStr.indexOf(subject.getName());
				if (subjectStart >= 0) {
					int subjectEnd = distributedHoursStr.indexOf(",", subjectStart) + 1;
					subjectEnd = subjectEnd > 0 ? subjectEnd : distributedHoursStr.length();
					if (!distributed && quantity.compareTo(BigDecimal.ZERO) == 0) {
						distributedHoursStr = distributedHoursStr.replace(distributedHoursStr.substring(subjectStart, subjectEnd), "");
					} else {
						distributedHoursStr = distributedHoursStr.replace(distributedHoursStr.substring(subjectStart, subjectEnd), subject.getName() + ":" + quantity);
					}
				} else {
					if (StringUtils.isNotBlank(distributedHoursStr)) {
						distributedHoursStr += "," + subject.getName() + ":" + quantity;
					} else {
						distributedHoursStr += subject.getName() + ":" + quantity;
					}
				}
			}
		}
		
//		if (contractProductDistributeVo.getDistributedHours().compareTo(cp.getQuantity()) > 0) {
//			log.info("分配课时：" + contractProductDistributeVo.getDistributedHours() + "， 产品可分配课时: " + cp.getQuantity());
//			throw new ApplicationException("待分配课时不足分配");
//		}
		
		//暂时注释，等解决
		if (contractProductDistributeVo.getDistributeType() == DistributeType.DISTRIBUTION) {
			BigDecimal realAmount = cp.getPaidStatus() == ContractProductPaidStatus.PAID ? cp.getTotalAmount() : cp.getPaidAmount();
			if (realAmount.divide(cp.getPrice(), 2).compareTo(totalDistributedHours) < 0) {
				throw new ApplicationException("未分配课时已被修改，不足完成这次分配。");
			} 
		} else {
			if (totalDistributedHours.compareTo(BigDecimal.ZERO) < 0) {
				throw new ApplicationException("已分配课时已被修改，不足完成这次提取。");
			}
		}
		if (com.eduboss.utils.StringUtil.isNotBlank(distributedHoursStr) && distributedHoursStr.substring(distributedHoursStr.length() - 1, distributedHoursStr.length()).equals(",")) {
			distributedHoursStr = distributedHoursStr.substring(0, distributedHoursStr.length() - 1);
		}
		totalDistributedHours = totalDistributedHours.compareTo(BigDecimal.ZERO) >= 0 ? totalDistributedHours : BigDecimal.ZERO;
		cp.setOooSubjectDistributedHours(totalDistributedHours);
		cp.setOooSubjectDistributedHoursDes(distributedHoursStr);
		contractProductDao.merge(cp);
	}
	
	/**
	 * 转移一对一课时
	 */
	@Override
	public void transferContractProductSubject(ContractProductSubjectVo contractProductSubjectVo) {
		if (StringUtils.isBlank(contractProductSubjectVo.getId()) 
				|| StringUtils.isBlank(contractProductSubjectVo.getContractProductId()) 
				|| StringUtils.isBlank(contractProductSubjectVo.getToSubjectId())) {
			throw new ApplicationException("请求参数不符合条件");
		}
		if (contractProductSubjectVo.getTransactionHours().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ApplicationException("转移课时必须大于0");
		}
		Organization fromOrg = StringUtils.isNotBlank(contractProductSubjectVo.getBlCampusId()) ? new Organization(contractProductSubjectVo.getBlCampusId()) : null;
		String currrentTime = DateTools.getCurrentDateTime();
		User currentUser = userService.getCurrentLoginUser();
		// 转出科目
		ContractProductSubject fromCpSubject = contractProductSubjectService.findById(contractProductSubjectVo.getId());
		if(fromCpSubject==null){
			throw new ApplicationException("转出科目为空！");
		}
		BigDecimal fromQuantity = fromCpSubject.getQuantity().subtract(contractProductSubjectVo.getTransactionHours());
		BigDecimal fromRemainHours = fromQuantity.subtract(fromCpSubject.getConsumeHours());
		CourseHoursDistributeRecord fromRecord = new CourseHoursDistributeRecord(contractProductSubjectVo.getContractProductId(), fromCpSubject.getSubject(),  DistributeType.TRANSFER_OUT, 
				contractProductSubjectVo.getTransactionHours(), fromQuantity, fromCpSubject.getConsumeHours(), 
				fromRemainHours, currentUser, currrentTime, fromOrg);
		courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(fromRecord);
		if (fromRemainHours.compareTo(BigDecimal.ZERO) < 0) {
			throw new ApplicationException("已分配课时已被修改，不足完成这次转移。");
		}
		if (fromQuantity.compareTo(BigDecimal.ZERO) == 0) { // 当已分配课时为0时，自动删除
			contractProductSubjectService.deleteContractProductSubject(fromCpSubject);
		} else {
			fromCpSubject.setModifyTime(currrentTime);
			fromCpSubject.setModifyUser(currentUser);
			fromCpSubject.setBlCampus(fromOrg);
			fromCpSubject.setQuantity(fromQuantity);
			contractProductSubjectService.saveOrUpdateContractProductSubject(fromCpSubject);
		}
		ContractProduct cp = contractProductDao.findById(contractProductSubjectVo.getContractProductId());
		String distributedHoursStr = cp.getOooSubjectDistributedHoursDes() != null ? cp.getOooSubjectDistributedHoursDes() : "";
		int subjectStart = distributedHoursStr.indexOf(fromCpSubject.getSubject().getName());
		if (subjectStart >= 0) {
			int subjectEnd = distributedHoursStr.indexOf(",", subjectStart);
			subjectEnd = subjectEnd > 0 ? subjectEnd : distributedHoursStr.length();
			if (fromQuantity.compareTo(BigDecimal.ZERO) == 0) {
				distributedHoursStr = distributedHoursStr.replace(distributedHoursStr.substring(subjectStart, subjectEnd), "");
			} else {
				distributedHoursStr = distributedHoursStr.replace(distributedHoursStr.substring(subjectStart, subjectEnd), fromCpSubject.getSubject().getName() + ":" + fromQuantity);
			}
		}
		// 转入科目
		ContractProductSubject toCpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(contractProductSubjectVo.getContractProductId(), contractProductSubjectVo.getToSubjectId());
		Organization toOrg = null;
		if (toCpSubject == null) {
			toCpSubject = new ContractProductSubject();
			toCpSubject.setContractProduct(new ContractProduct(contractProductSubjectVo.getContractProductId()));
			toCpSubject.setSubject(new DataDict(contractProductSubjectVo.getToSubjectId()));
			toCpSubject.setQuantity(BigDecimal.ZERO);
			toCpSubject.setConsumeHours(BigDecimal.ZERO);
			toCpSubject.setCreateTime(currrentTime);
			toCpSubject.setCreateByStaff(currentUser);
			toOrg = StringUtils.isNotBlank(contractProductSubjectVo.getToCampusId()) ? new Organization(contractProductSubjectVo.getToCampusId()) : null;
			toCpSubject.setBlCampus(toOrg);
		}
		BigDecimal toQuantity = toCpSubject.getQuantity().add(contractProductSubjectVo.getTransactionHours());
		BigDecimal toRemainHours = toQuantity.subtract(toCpSubject.getConsumeHours());
		CourseHoursDistributeRecord toRecord = new CourseHoursDistributeRecord(contractProductSubjectVo.getContractProductId(), toCpSubject.getSubject(), 
				DistributeType.TRANSFER_IN, contractProductSubjectVo.getTransactionHours(), toQuantity, toCpSubject.getConsumeHours(), 
				toRemainHours, currentUser, currrentTime, toCpSubject.getBlCampus());
		courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(toRecord);
		toCpSubject.setModifyTime(currrentTime);
		toCpSubject.setModifyUser(currentUser);
		toCpSubject.setQuantity(toQuantity);
		contractProductSubjectService.saveOrUpdateContractProductSubject(toCpSubject);
		DataDict subject = dataDictDao.findById(contractProductSubjectVo.getToSubjectId());
		subjectStart = distributedHoursStr.indexOf(subject.getName());
		if (subjectStart >= 0) {
			int subjectEnd = distributedHoursStr.indexOf(",", subjectStart);
			subjectEnd = subjectEnd > 0 ? subjectEnd : distributedHoursStr.length();
			if (toQuantity.compareTo(BigDecimal.ZERO) > 0) {
				distributedHoursStr = distributedHoursStr.replace(distributedHoursStr.substring(subjectStart, subjectEnd), subject.getName() + ":" + toQuantity);
			}
		} else {
			distributedHoursStr += "," + subject.getName() + ":" + toQuantity;
		}
		if (distributedHoursStr.substring(distributedHoursStr.length() - 1, distributedHoursStr.length()).equals(",")) {
			distributedHoursStr = distributedHoursStr.substring(0, distributedHoursStr.length() - 1);
		}
		cp.setOooSubjectDistributedHoursDes(distributedHoursStr);
		contractProductDao.merge(cp);
	}
	
	/**
	 * 获取当前用户的课时管理操作权限
	 */
	@Override
	public String getCourseTimeManageAuthTags() {
		User currentUser = userService.getCurrentLoginUser();
		List<Map<Object, Object>> list ;
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			list = contractProductDao.getCourseTimeManageAuthTagsNew(currentUser.getUserId());
		}else{
			list = contractProductDao.getCourseTimeManageAuthTags(currentUser.getUserId());
		}
		String tags = "";
		if (list != null && list.size() > 0) {
			for (Map<Object, Object> map : list) {
				tags += map.get("RTAG") + ",";
			}
		}
		return tags;
	}
	
	/**
	 * 退费一对一课时
	 */
	@Override
	public void refundContractProductSubject(ContractProduct contractProduct) {
		String currrentTime = DateTools.getCurrentDateTime();
		User currentUser = userService.getCurrentLoginUser();
		List<ContractProductSubject> list = contractProductSubjectService.findContractProductSubjectByCpId(contractProduct.getId());
		String oooSubjectDistributedHoursDes = "";
		for (ContractProductSubject cps : list) {
			if (cps.getQuantity().compareTo(cps.getConsumeHours()) > 0) {
				CourseHoursDistributeRecord record = new CourseHoursDistributeRecord(contractProduct.getId(), cps.getSubject(), 
						DistributeType.REFUND, cps.getQuantity().subtract(cps.getConsumeHours()), cps.getConsumeHours(), cps.getConsumeHours(), 
						BigDecimal.ZERO, currentUser, currrentTime, cps.getBlCampus());
				courseHoursDistributeRecordService.saveOrUpdateCourseHoursDistributeRecord(record);
				cps.setModifyTime(currrentTime);
				cps.setModifyUser(currentUser);
				cps.setQuantity(cps.getConsumeHours());
				contractProductSubjectService.saveOrUpdateContractProductSubject(cps);
			}
			if (cps.getConsumeHours().compareTo(BigDecimal.ZERO) > 0) {
				if (com.eduboss.utils.StringUtil.isNotBlank(oooSubjectDistributedHoursDes)) {
					oooSubjectDistributedHoursDes += "," + cps.getSubject().getName()  + ":" + cps.getConsumeHours();
				} else {
					oooSubjectDistributedHoursDes += cps.getSubject().getName()  + ":" + cps.getConsumeHours();
				}
			}
		}
		contractProduct.setOooSubjectDistributedHours(contractProduct.getConsumeQuanity());
		contractProduct.setOooSubjectDistributedHoursDes(oooSubjectDistributedHoursDes);
	}

	/**
	 * 根据收款记录获取业绩分配记录
	 * @param fundsChangeHistoryId
	 * @return
	 */
	@Override
	public List<IncomeDistributionVo> getIncomeDistributionByFundsId(String fundsChangeHistoryId) {
		List<IncomeDistribution> incomeDistributionList = incomeDistributionDao.findByFundsChangeHistoryId(fundsChangeHistoryId);
		List<IncomeDistributionVo> incomeDistributionVos = HibernateUtils.voListMapping(incomeDistributionList, IncomeDistributionVo.class);
		return incomeDistributionVos;
	}

	/**
	 * 直播合同校区信息
	 * 线下校区信息
	 *
	 * @param contractIdList
	 * @return
	 */
	@Override
	public Map<Object, Object> getContractCampusInfoByIdList(String contractIdList) {
		List<String> ls = new ArrayList<>();
		try {
			JSONObject object = JSONObject.parseObject(contractIdList);
			ls = JSON.parseArray(object.get("contractIdList").toString(), String.class);
		}catch (Exception e){
			throw new  ApplicationException("参数格式错误");
		}



		StringBuffer sql = new StringBuffer();
		sql.append(" select c.ID contractId, c.STUDENT_ID studentId, branch.`name` branchName, branch.id branchId, campus.`name` campusName, campus.id campusId, cus.`NAME` customerName, signUper.`NAME` signUperName, signUper.CONTACT signUperContact, signUper.employee_No  employeeNo   ");
		sql.append(" from contract c, organization campus, organization branch, customer cus, `user` signUper where c.sign_staff_id=signUper.USER_ID AND c.CUSTOMER_ID=cus.ID AND  c.BL_CAMPUS_ID=campus.id AND campus.parentID=branch.id  AND c.ID in (:contractId) ");
		Map<String, Object> map = new HashMap<>();
		map.put("contractId", ls);
		List<Map<Object, Object>> list = contractDao.findMapBySql(sql.toString(), map);
		Map<Object, Object> returnMap = new HashMap<>();
		for(Map maps :list){
			returnMap.put(maps.get("contractId"),maps);
		}
		return returnMap;
	}

	/**
	 * 删除双师考勤记录的时候需要检查扣费记录里面不会再有这些双师考勤的id redmine id #1414
	 *
	 * @param id
	 * @param twoClassId
	 */
	@Override
	public void updateNullTwoTeacherClassStudentAttendentByStudentAndTwoClassId(String id, int twoClassId) {
		Map<String, Object> map = new HashMap<>();
		map.put("studentId", id);
		map.put("twoClassId", twoClassId);
//		String sql =" DELETE  FROM two_teacher_class_student_attendent WHERE STUDENT_ID=:studentId AND CLASS_TWO_ID=:twoClassId  AND CHARGE_STATUS='UNCHARGE' ";
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT acr.* FROM account_charge_records acr, two_teacher_class_student_attendent ttcsa where 1=1 ");
		sql.append(" and acr.TWO_TEACHER_STUDENT_ATTENDENT = ttcsa.ID and acr.TWO_TEACHER_STUDENT_ATTENDENT is not null ");
		sql.append(" and ttcsa.STUDENT_ID=:studentId AND ttcsa.CLASS_TWO_ID=:twoClassId  AND CHARGE_STATUS='UNCHARGE' ");
		List<AccountChargeRecords> records = accountChargeRecordsDao.findBySql(sql.toString(), map);
		for (AccountChargeRecords a : records){
			a.setTwoTeacherClassStudentAttendent(null);
			accountChargeRecordsDao.save(a);
		}
	}

	/**
	 * 根据关联系统的订单编号查找合同产品列表
	 */
    @Override
    public List<ContractProduct> listContractProductByAssocRelatedNo(String relatedNo) {
        return contractProductDao.listContractProductByAssocRelatedNo(relatedNo);
    }

	/**
	 * 是否能修改业绩
	 *
	 * @param fundsChangeHistoryId
	 * @return
	 */
	@Override
	public boolean canUpdateIncomeDistribution(String fundsChangeHistoryId)  {
		FundsChangeHistory fundsChangeHistory = fundsChangeHistoryDao.findById(fundsChangeHistoryId);
		if (fundsChangeHistory!=null){
			String receiptTime = fundsChangeHistory.getReceiptTime();//收款时间

			Date receiptTimeDate = DateTools.getDate(receiptTime);
			Date twoMonth = DateTools.add(receiptTimeDate, Calendar.MONTH, 2);//收款后2个月时间
			try {
				Date firstDayOfMonth = DateTools.getFirstDayOfMonth(DateTools.dateConversString(twoMonth, "yyyy-MM-dd"));//收款后2个月第一天时间
				String endTime = DateTools.dateConversString(firstDayOfMonth, "yyyy-MM-dd")+" 23:59:59";
				Date date2 = DateTools.getDate(endTime);//截止时间
				String currentDateTime = DateTools.getCurrentDateTime();
				Date date3 = DateTools.getDate(currentDateTime);
				if (date2.compareTo(date3)>0){
					return true;
				}else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
                return false;
			}
		}
		return false;
	}

}

