package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eduboss.domainVo.*;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.dto.*;
import com.eduboss.utils.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.directwebremoting.guice.RequestParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.ContractType;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.MoneyRollbackRecords;
import com.eduboss.domain.MoneyWashRecords;
import com.eduboss.domain.Student;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractService;
import com.eduboss.service.CourseHoursDistributeRecordService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.FundsAuditRecordService;
import com.eduboss.service.MoneyReadyToPayService;
import com.eduboss.service.ProductService;
import com.eduboss.service.StudentService;

import static com.eduboss.service.handler.ContractProductHandler.objectMapper;

/**
 * @classname	ContractAction.java 
 * @Description
 * @author	ZhangYiheng
 * @Date	2014-7-5 01:09:47
 * @LastUpdate	ZhangYiheng
 * @Version	1.0
 */

@Controller
@RequestMapping(value = "/ContractAction")
@GrayClassAnnotation
public class ContractController {
	
	/**合同**/
	@Autowired
	private ContractService  contractService;
	
	/**学员**/
	@Autowired
	private StudentService studentService;
	
	/**客户资源**/
	@Autowired
	private CustomerService customerService;
	
	/**产品**/
	@Autowired
	private ProductService productService;
	
	
	/**扣费记录**/
	@Autowired
	private ChargeService chargeService;
	

	/**扣费记录**/
	@Autowired
	private MoneyReadyToPayService moneyReadyToPayService;
	
	/**
	 * 收款记录审核流水
	 */
	@Autowired
	private FundsAuditRecordService fundsAuditRecordService;
	
	@Autowired
	private CourseHoursDistributeRecordService courseHoursDistributeRecordService;
	
	
	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(ContractController.class);
	
	/**
	 * 返回 一对一课程 根据所选的年级
	 * @return
	 */
	@RequestMapping(value = "/getOneOnOneCources", method =  RequestMethod.GET)
	@ResponseBody
	public List<ProductVo> getOneOnOneCources(@RequestParam String gradeId,String proStatus) {
		List<ProductVo> productVoList = productService.getOneOnOneCourcesByGrade(gradeId,proStatus);
		return productVoList;
	}
	
	/**
	 * 根据关键字选取不同的小班
	 * @return
	 */
	@RequestMapping(value = "/getSmallClassCources", method =  RequestMethod.GET)
	@ResponseBody
	public List<ProductVo> getSmallClassCources(@RequestParam String gradeId,String proStatus) {
		List<ProductVo> productList = productService.getSmallClassCourcesByKeyWord("", gradeId,proStatus);
		//List<ProductVo> productList = productService.getSmallClassCources(gradeId);
		//List<AutoCompleteOptionVo> optionVos =  HibernateUtils.voListMapping(productList, AutoCompleteOptionVo.class);
		return productList;
	}
	
	/**
	 * 返回 其他收费项目
	 * @return
	 */
	@RequestMapping(value = "/getOtherProducts", method =  RequestMethod.GET)
	@ResponseBody
	public List<ProductVo> getOtherProducts() {
		List<ProductVo> productVoList = productService.getOtherProducts();
		return productVoList;
	}
	
	/**
	 * 保存合同收费
	 * @return
	 */
	@RequestMapping(value = "/saveFundOfContract", method =  RequestMethod.GET)
	@ResponseBody
	public String saveFundOfContract(@ModelAttribute FundsChangeHistoryVo fundsVo) {
		FundsChangeHistory fund = contractService.saveFundOfContract(fundsVo);
		return fund.getId() ;
	}
	

		
	/**
	 * 保存合同收费
	 * @return
	 */
	@RequestMapping(value = "/saveFundWashOfContract", method =  RequestMethod.GET)
	@ResponseBody
	public String saveFundWashOfContract(@RequestParam String fundsId, @RequestParam BigDecimal fundsWashAmount) {
		FundsChangeHistory fund = contractService.saveFundWashOfContract(fundsId, fundsWashAmount);
		return fund.getId() ;
	}

	/**
	 * 更新收款记录的归属校区
	 * @param id
	 * @param blCampusId
	 * @return
	 */
	@RequestMapping(value = "/updateFundCampusIdById", method = RequestMethod.POST)
	@ResponseBody
	public Response updateFundCampusIdById(@RequestParam String id, @RequestParam String blCampusId){
		contractService.updateFundCampusIdById(id, blCampusId);
		return new Response();
	}

	/**	 * 保存POS机支付信息返回信息ID供生成二维码
	 * @return
	 */
	@RequestMapping(value = "/saveMoneyReadyToPay", method =  RequestMethod.GET)
	@ResponseBody
	public String saveMoneyReadyToPay(@ModelAttribute MoneyReadyToPayVo mrtVo) {
		String pk=moneyReadyToPayService.saveMoneyReadyToPay(mrtVo);
		return pk;
	}
	
	@RequestMapping(value = "/getPayInfo", method =  RequestMethod.GET)
	@ResponseBody
	public MoneyReadyToPayVo findMoneyReadyToPayById(@RequestParam String payNo,String SignMsg){
		return moneyReadyToPayService.findById(payNo);
	}
	
	/**
	 * @param payNo  星火支付单号
	 * @param payResultCode 支付结果
	 * @param payResultMessage 支付信息
	 * @param SignMsg 加密信息
	 * @param posCode 终端号
	 * @param payCode 银联支付流水号
	 * @param busNo 商户编号
	 * @param cardNo 支付卡号
	 * @param amount 金额
	 * @param transactionTime  付款时间
	 * @return
	 */
	@RequestMapping(value = "/payResultNotify", method =  RequestMethod.GET)
	@ResponseBody
	public Map payResultNotify(@RequestParam String payNo,
			@RequestParam String payResultCode, String payResultMessage,
			String SignMsg, String posCode, String payCode,String busNo,String cardNo,String amount,String transactionTime,String encryptedMsg) {
		return moneyReadyToPayService.payResultNotify(payNo,payResultCode,payResultMessage,SignMsg,posCode,payCode,busNo,cardNo,amount,transactionTime,encryptedMsg);
	}

	/**
	 * 得到对应的小班所在校区所有能报读的目标班学生
	 * @param miniClassId
	 * @return
     */
	@RequestMapping(value = "/getEscProductByCampus", method =  RequestMethod.GET)
	@ResponseBody
	public List<Map<String,String>> getEscProductByCampus(@RequestParam String miniClassId) {
//		 List<ContractProductVo> cpList=contractService.getEscProductByCampus(miniClassId);
		 return contractService.getEscProductMiniClassId(miniClassId);
	}

	
	/**
	 * 返回 资金收入列表
	 * @return
	 */
	@RequestMapping(value = "/getMyFundList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMyFundList(@ModelAttribute GridRequest gridRequest, @ModelAttribute FundsChangeSearchVo vo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage= contractService.findPageMyFundHistory(dataPackage,vo);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	@RequestMapping(value = "/findFundHistoryById", method =  RequestMethod.GET)
	@ResponseBody
	public FundsChangeHistoryVo findFundHistoryById(@RequestParam String id){
		return contractService.findFundHistoryById(id);
	}
	
	@RequestMapping(value = "/updateFundHistoryPos")
	@ResponseBody
	public Response updateFundHistoryPos( @ModelAttribute FundsChangeHistoryVo vo){
		 contractService.updateFundHistoryPos(vo);
		return new Response();
	}
	
	/**
	 * 返回 所有扣费记录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getMyChargeList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMyChargeList(@ModelAttribute GridRequest gridRequest, @ModelAttribute AccountChargeRecordsVo accountChargeRecordsVo, @ModelAttribute TimeVo timeVo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		//dataPackage= contractService.findPageContract( dataPackage, contractVo, timeVo  );
		dataPackage=chargeService.findPageMyCharge(dataPackage, accountChargeRecordsVo, timeVo);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}
	
	/**
	 * 回滚扣费操作
	 * @param moneyRollbackRecords
	 * @return
	 */
	@RequestMapping(value = "/rollbackCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response rollbackCharge(MoneyRollbackRecords moneyRollbackRecords) {
		chargeService.rollbackCharge(moneyRollbackRecords);
		return new Response();
	}
	
	/**
	 * 冲销扣费
	 * @param transactionId
	 * @return
	 */
	@RequestMapping(value = "/washCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response washCharge(MoneyWashRecords moneyWashRecords) {
		chargeService.washCharge(moneyWashRecords);
		return new Response();
	}
	
	/**
	 * 获取冲销扣费金额
	 * @param transactionId
	 * @return
	 */
	@RequestMapping(value = "/getWashChargeAmount", method =  RequestMethod.GET)
	@ResponseBody
	public Map getWashChargeAmount(@RequestParam String transactionId) {
		return chargeService.getWashChargeAmount(transactionId);
	}
	
	/**
	 * 一对一课程审批回滚
	 * @param moneyRollbackRecords
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "/rollbackCourseCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response rollbackCourseCharge(@ModelAttribute MoneyRollbackRecords moneyRollbackRecords, @RequestParam String courseId) {
		chargeService.rollbackCourseCharge(moneyRollbackRecords, courseId);
		return new Response();
	}
	
	/**
	 * 小班课程审批回滚
	 * @param moneyRollbackRecords
	 * @param miniClassCourseId
	 * @return
	 */
	@RequestMapping(value = "/rollbackMiniClassCourseCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response rollbackMiniClassCourseCharge(@ModelAttribute MoneyRollbackRecords moneyRollbackRecords, @RequestParam String miniClassCourseId) {
		return chargeService.rollbackMiniClassCourseCharge(moneyRollbackRecords, miniClassCourseId);
	}
	
	/**
	 * 一对多课程审批回滚
	 * @param moneyRollbackRecords
	 * @param otmClassCourseId
	 * @return
	 */
	@RequestMapping(value = "/rollbackOtmClassCourseCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response rollbackOtmClassCourseCharge(@ModelAttribute MoneyRollbackRecords moneyRollbackRecords, @RequestParam String otmClassCourseId) {
		return chargeService.rollbackOtmClassCourseCharge(moneyRollbackRecords, otmClassCourseId);
	}
	
	/**
	 * 一对一课程审批冲销
	 * @param moneyRollbackRecords
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value = "/washCourseCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response washCourseCharge(@ModelAttribute MoneyWashRecords moneyWashRecords, @RequestParam String courseId) {
		chargeService.washCourseCharge(moneyWashRecords, courseId);
		return new Response();
	}
	
	/**
	 * 小班课程审批冲销
	 * @param moneyWashRecords
	 * @param miniClassCourseId
	 * @return
	 */
	@RequestMapping(value = "/washMiniClassCourseCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response washMiniClassCourseCharge(@ModelAttribute MoneyWashRecords moneyWashRecords, @RequestParam String miniClassCourseId) {
		return chargeService.washMiniClassCourseCharge(moneyWashRecords, miniClassCourseId);
	}
	
	/**
	 * 一对多课程审批冲销
	 * @param moneyWashRecords
	 * @param otmClassCourseId
	 * @return
	 */
	@RequestMapping(value = "/washOtmClassCourseCharge", method =  RequestMethod.GET)
	@ResponseBody
	public Response washOtmClassCourseCharge(@ModelAttribute MoneyWashRecords moneyWashRecords, @RequestParam String otmClassCourseId) {
		return chargeService.washOtmClassCourseCharge(moneyWashRecords, otmClassCourseId);
	}
	
	/**
	 * 保存全款合同信息, 是 新签 的 或者 续费的
	 * @return
	 */
	@RequestMapping(value = "/saveFullContractInfo", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveFullContractInfo(@RequestBody ContractVo contractVo ) throws Exception {
		contractService.saveFullContractInfoNew(contractVo);
		return new Response();
	}

	/**
	 * 保存有直播产品的合同
	 * @param contractVo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveContractAndLive", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveContractAndLive(@RequestBody ContractVo contractVo ){
		contractService.saveContractAndLive(contractVo);
		return new Response();
	}


	
	
	/**
	 * 保存 修改合同 信息
	 * @return
	 */
	@RequestMapping(value = "/saveEditFullContractInfo", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveEditFullContractInfo(@RequestBody ContractVo contractVo) throws Exception {
		if(contractVo.getContractType() == ContractType.INIT_CONTRACT) {
			// 初始化合同的修改
			contractService.saveEditInitContractInfo(contractVo);
		} else {
			contractService.saveEditFullContractInfoNew(contractVo);
		}
		return new Response();
	}
	
	/**
	 * 保存订金合同信息 
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/saveDepositContractInfo", method =  RequestMethod.GET)
	@ResponseBody
	public Response saveDepositContractInfo(@ModelAttribute ContractVo contractVo ) throws Exception {
		throw new ApplicationException(ErrorCode.CANCEL_CONTRACT_TYPE_OF_DEPOSIT);
//		contractVo.setContractType(ContractType.DEPOSIT);
//		contractService.saveDepositContractInfo(contractVo);
//		return new Response();
	}
	

	/**
	 * 保存顾客信息 在合同界面
	 * @param 	里面应该有 用户资源姓名，用户的电话 ， 学生姓名，学生年级ID
	 * @return
	 */
	@RequestMapping(value = "/saveContractNewCustomerInfo", method =  RequestMethod.GET)
	@ResponseBody
	public CustomerVo saveContractNewCustomerInfo(@ModelAttribute ContractVo contractVo ) {
		
		CustomerVo cus=  contractService.saveCustomerInfo(contractVo );
		return cus;
	}
	

	/**
	 * 获取客户信息 在合同界面
	 * @param customerId 	客户Id 
	 * @return
	 */
	@RequestMapping(value = "/getContractCustomerInfo", method =  RequestMethod.GET)
	@ResponseBody
	public ContractVo getContractCustomerInfo(@RequestParam(required=false) String customerId, @RequestParam(required=false) String studentId) {
		ContractVo conVo =  null;
		conVo = contractService.getContractCustomerInfo(customerId, studentId);
		return conVo;
	}
	
	/**
	 * 获取学生列表 在合同界面
	 * @return
	 */
	@RequestMapping(value = "/getContractStudentInfo", method =  RequestMethod.GET)
	@ResponseBody
	public List<Student> getContractStudentInfo(@RequestParam String customerId) {
		System.out.println(customerId);
		List<Student> stuList = new ArrayList<Student>();
//		Student stu1 =  new Student(); stu1.setId("1"); stu1.setGrade("a"); stu1.setName("A1");
//		Student stu2 =  new Student(); stu2.setId("2"); stu2.setGrade("b"); stu2.setName("A2");
//		Student stu3 =  new Student(); stu3.setId("3"); stu3.setGrade("c"); stu3.setName("A3");
//		stuList.add(stu1);
//		stuList.add(stu2);
//		stuList.add(stu3);
		return stuList;
	}
	

	/**
	 * 获取合同 详情
	 * @param contractid
	 * @return
	 */
	@RequestMapping(value = "/getContractById", method =  RequestMethod.GET)
	@ResponseBody
	public ContractVo getContractById(@RequestParam String contractId) {
		ContractVo contractVo = contractService.getContractById(contractId);
		return contractVo;
	}
	
	/**
	 * 根据学生id查询有剩余金额的合同
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/getSurplusContractByStudentId", method =  RequestMethod.GET)
	@ResponseBody
	public List<ContractVo> getSurplusContractByStudentId(String studentId){
		return contractService.getSurplusContractByStudentId(studentId);
	}

	
	/**
	 * 废弃
	 * 保存合同的提成信息
	 * @param 
	 * @return
	 */
	/*@RequestMapping(value = "/saveContractBonus", method =  RequestMethod.GET)
	@ResponseBody
	public Response saveContractBonus(@RequestParam String fundsChangeHistoryId, @RequestParam String cbsIdA, @RequestParam String userIdA, @RequestParam String bonusA,@RequestParam String bonusAA,
			@RequestParam String cbsIdB, @RequestParam String userIdB, @RequestParam String bonusB, @RequestParam String bonusBB,
			@RequestParam String cbsIdC,@RequestParam String userIdC,@RequestParam String bonusC,@RequestParam String bonusCC,
			 String schoolA, String schoolB, String schoolC){
		RefundIncomeDistributeVo refundIncomeDistributeVo = new RefundIncomeDistributeVo();
		contractService.saveContractBonus(fundsChangeHistoryId, BonusType.NORMAL.toString(),null,null, refundIncomeDistributeVo);
		return new Response();
	}*/
	

	@RequestMapping(value = "/saveIncomeDistribution", method =  RequestMethod.POST)
	@ResponseBody
	@RepeatSubmitAnnotation
	public Response saveIncomeDistribution(@RequestParam String fundsChangeHistoryId, @RequestParam String incomeDistributionStr){
		IncomeDistributionVo[] list=null;
		try {
			list = objectMapper.readValue(incomeDistributionStr, IncomeDistributionVo[].class);
		} catch (Exception e) {
			throw new ApplicationException(ErrorCode.PARAMETER_FORMAT_ERROR);
		}
		List<IncomeDistributionVo> incomeDistributionVos = Arrays.asList(list);
		contractService.saveIncomeDistribution(fundsChangeHistoryId, incomeDistributionVos);
		return new Response();
	}

	@RequestMapping(value = "/canUpdateIncomeDistribution")
	@ResponseBody
	public boolean canUpdateIncomeDistribution(@RequestParam String fundsChangeHistoryId){
		return contractService.canUpdateIncomeDistribution(fundsChangeHistoryId);
	}
	
	/**
	 *
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/getContractBonusByFundsId", method =  RequestMethod.GET)
	@ResponseBody
//	public List<ContractBonusVo> getContractBonusByFundsId(@RequestParam String fundsChangeHistoryId){
//		List<ContractBonusVo> bousList = contractService.getContractBonusByFundsChangeHistoryId(fundsChangeHistoryId);
//		return bousList;
//	}
	public List<IncomeDistributionVo> getIncomeDistributionByFundsId(@RequestParam String fundsChangeHistoryId){
		List<IncomeDistributionVo> list = contractService.getIncomeDistributionByFundsId(fundsChangeHistoryId);
		return list;
	}

	/**
	 * 根据收款记录获取流水记录
	 * @param fundsChangeHistoryId
	 * @return
	 */
	@RequestMapping(value = "/getListByFundsChangeHistoryId", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getListByFundsChangeHistoryId(@ModelAttribute GridRequest gridRequest, String fundsChangeHistoryId){
		DataPackage dp = new DataPackage(gridRequest);

		dp = contractService.getListByFundsChangeHistoryId(dp, fundsChangeHistoryId);

		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);

		return dataPackageForJqGrid;
	}


	
	
	/**
	 * 获取到当前支付记录之外的其他业绩分配，用于前端计算本次支付金额可以分配多少给每个合同产品
	 * @param 
	 * @return
	 */
//	@RequestMapping(value = "/getOtherBonusTotal", method =  RequestMethod.GET)
//	@ResponseBody
//	public Map getOtherBonusTotal(@RequestParam String contractId,@RequestParam String fundId){
//		Map bousList = contractService.getOtherBonusTotal(contractId,fundId);
//		return bousList;
//	}

	@RequestMapping(value = "/getOtherBonusTotal", method =  RequestMethod.GET)
	@ResponseBody
	public Map getOtherBonusTotal(@RequestParam String contractId,@RequestParam String fundId){
		Map map = contractService.getKeFenPeiBonus(contractId,fundId);
		return map;
	}

	@RequestMapping(value = "/getContractInfoForDistribution", method =  RequestMethod.GET)
	@ResponseBody
	public Map getContractInfoForDistribution(@RequestParam String contractId,@RequestParam String fundId){
		Map map = contractService.getContractInfo(contractId, fundId);
		return map;
	}
	
	/**
	 * 取回 年级单价
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/getOneOnOnePrice", method =  RequestMethod.GET)
	@ResponseBody
	public BigDecimal getOneOnOnePrice(@RequestParam String gradeId){
		return contractService.getOneOnOnePrice(new DataDict(gradeId)) ;
	}
	
	/**
	 * 
	 * @param dp
	 * @param studentId
	 * @return
	 */
	@RequestMapping(value = "/findContractByStu", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findContractByStu(@ModelAttribute GridRequest gridRequest,@RequestParam String studentId) {
		DataPackage dp = new DataPackage(gridRequest);
		dp.setPageSize(999);
		 dp=contractService.findContractByStu(dp, studentId);
		 DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
		 return dataPackageForJqGrid;
	}
	
	/**
	 * 
	 * @param dp
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/findContractByCus", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findContractByCus(@ModelAttribute GridRequest gridRequest,@RequestParam String customerId) {
		DataPackage dp = new DataPackage(gridRequest);
		 dp=contractService.findContractByCus(dp, customerId);
		 DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
		 return dataPackageForJqGrid;
	}
	
	
	@RequestMapping(value = "/saveContractArrangeMoney", method =  RequestMethod.GET)
	@ResponseBody
	public void saveContractArrangeMoney(@RequestParameters BigDecimal [] arrangeMoney, @RequestParameters String [] conPrdId) {
		contractService.saveContractArrangeMoney_new(arrangeMoney, conPrdId);
	}
	
	
	@RequestMapping(value = "/saveContractProductMoney", method =  RequestMethod.GET)
	@ResponseBody
	public void saveContractProductMoney(@RequestParameters BigDecimal [] arrangeMoney, @RequestParameters String [] conPrdId,@RequestParameters String fundHistoryId) {
		contractService.saveContractProductMoney(arrangeMoney, conPrdId,fundHistoryId, null);
	}
	
	
	/**
	 * 从合同产品提取金额
	 * @param takeawayMoney
	 * @param conPrdId
	 */
	@RequestMapping(value = "/saveContractTakeawayMoney", method =  RequestMethod.GET)
	@ResponseBody
	public void saveContractTakeawayMoney(@RequestParameters BigDecimal  takeawayMoney, @RequestParameters String  conPrdId, BigDecimal maxKeTiquMoney) {
		contractService.saveContractTakeawayMoney(takeawayMoney, conPrdId,maxKeTiquMoney);
	}
	
	 @ResponseBody
	 @RequestMapping(value="/findInitContractByStudentId")
	 public ContractVo findInitContractByStudentId(String studentId){
		 ContractVo contractVo= contractService.findInitContractByStudentId(studentId);
	     return contractVo;
	 }
	
	 @ResponseBody
	 @RequestMapping(value="/getContractProductById")
	 public ContractProductVo getContractProductById(@RequestParam String contractProductId){
		return  contractService.getContractProductById(contractProductId);
	 }
	 
	 @ResponseBody
	 @RequestMapping(value="/closeContractProcuct")
	 public Response closeContractProcuct(@RequestParam BigDecimal amountFromPromotionAcc,@RequestParam BigDecimal returnAmountFromPromotionAcc,@RequestParam String contractProductId,@RequestParam Boolean isRefundMoney,@RequestParam BigDecimal returnMoney
			 , String cbsIdA, String userIdA, String bonusA,String bonusAA,
				String cbsIdB, String userIdB, String bonusB, String bonusBB,
				String cbsIdC,String userIdC,String bonusC,String bonusCC,
				 String schoolA, String schoolB, String schoolC,String returnReason,String returnType, 
					@RequestParam(required=false)MultipartFile certificateImageFile,String accountName,String account, String remark
			 ) throws Exception{
		 List<IncomeDistributionVo>  list = new ArrayList<>();
		 try{
		 contractService.closeContractProcuct(amountFromPromotionAcc,returnAmountFromPromotionAcc, contractProductId,isRefundMoney,returnMoney, BigDecimal.ZERO,
				 returnReason,returnType,certificateImageFile,accountName,account, null, null, remark, list, null);
		 }catch(ApplicationException e){
			 return new Response(-1,e.getErrorMsg());
		 }
		 return new Response();
	 }
		
	/**
	 * 返回 所有我的合同
	 * 根据不同的权限获取到不同的合同数据
	 * @return
	 */
	@RequestMapping(value = "/getMyContractList", method =  RequestMethod.GET)
	@ResponseBody
	@GrayMethodAnnotation(id="contractList",name="合同列表")
	@RepeatSubmitAnnotation
	public DataPackageForJqGrid getMyContractList(@ModelAttribute GridRequest gridRequest, @ModelAttribute ContractVo contractVo, @ModelAttribute TimeVo timeVo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage= contractService.findPageContract( dataPackage, contractVo, timeVo  );

		return new DataPackageForJqGrid(dataPackage) ;
	}
		
	/**
	  * 合同列表导出Excel
	  * @param gridRequest
	  * @param request
	  * @return
	 * @throws IOException 
	  */
	 @ResponseBody
	 @RequestMapping(value="/getContractListToExcel")
	 public void getContractListToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute ContractVo contractVo, @ModelAttribute TimeVo timeVo, HttpServletRequest request, HttpServletResponse response) throws IOException{
//		String product = "";
//		Map<String, String> condtion = new HashMap<String, String>(); 
//		if(StringUtil.isNotBlank(request.getParameter("productFinder"))){
//			product = request.getParameter("productFinder");
//		}
//		condtion.put("product", product);
	     DataPackage dataPackage = new DataPackage(gridRequest);
	        dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
	        dataPackage= contractService.findPageContract( dataPackage, contractVo, timeVo  );
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<ContractExcelVo> exporter = new ExportExcel<ContractExcelVo>();
		String[] hearders = new String[] {"编号","合同校区","学生", "学生校区",  "年级", "签约日期","一对一收费","小班收费","一对多收费","精英班收费", "讲座收费", "双师收费", "其他收费", "总额","优惠","已收款","待缴费","待分配资金","类型", "合同状态", "收款状态", "签单人", "报读渠道"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			Set<ContractExcelVo> datas = HibernateUtils.voCollectionMapping(dataPackage.getDatas(), ContractExcelVo.class);
			exporter.exportExcel(hearders, datas, out);
		}
	 }
	 
	 /** 
		 * 返回 资金收入列表
		 * @return
		 */
	 
		@RequestMapping(value = "/getFundListToExcel")
		@ResponseBody
		public void getFundListToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute FundsChangeSearchVo vo,HttpServletRequest request,HttpServletResponse response) throws IOException {

			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
			dataPackage= contractService.findPageMyFundHistoryExcel( dataPackage,vo);
			ExportExcel<FundsChangeHistoryExcelVo> ex = new ExportExcel<FundsChangeHistoryExcelVo>();
			String[] hearders = new String[] {"合同编号","客户姓名","学生姓名","年级","合同类型","签订人","支付日期", "收款日期","本次支付金额","支付方式","支付参考号","收款人","收款校区","合同总额","应收款","优惠金额","已收款金额","待缴费金额"};//表头数组
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String filename = timeFormat.format(new Date())+".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			OutputStream out = response.getOutputStream();
			Set<FundsChangeHistoryExcelVo> datas = HibernateUtils.voCollectionMapping(dataPackage.getDatas(), FundsChangeHistoryExcelVo.class);
			ex.exportExcel(hearders, datas, out);

	        out.close();
		}
		
		/**
		 * 返回 所有扣费记录Excel
		 * 
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value = "/getChargeToExcel")
		@ResponseBody
		public void getChargeToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute AccountChargeRecordsVo accountChargeRecordsVo, @ModelAttribute TimeVo timeVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());

			ExportExcel<AccountChargeRecordsExcelVo> ex = new ExportExcel<AccountChargeRecordsExcelVo>();
			String[] hearders = new String[] {"流水号","合同编号", "扣费校区", "扣费类型","资金类型", "扣费课时","扣费金额","扣费时间","具体上课日期","业务日期","老师姓名","学生姓名","操作人","产品类型","产品名称","课时时长","班级名称"};//表头数组
			SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String filename = timeFormat.format(new Date())+".xls";
			response.setContentType("application/ms-excel;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
			OutputStream out = response.getOutputStream();
			Set<AccountChargeRecordsExcelVo> datas = HibernateUtils.voCollectionMapping(chargeService.findPageMyChargeExcel(dataPackage, accountChargeRecordsVo, timeVo), AccountChargeRecordsExcelVo.class);
	        ex.exportExcel(hearders, datas, out);
	        out.close();
		}
		
		
		
		
		/**
		 * 合同修改痕迹表
		 * @return
		 */
		@RequestMapping(value = "/findPageContractRecord", method =  RequestMethod.GET)
		@ResponseBody
		public DataPackageForJqGrid findPageContractRecord(@ModelAttribute GridRequest gridRequest, @RequestParameters String contractId) {
			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage= contractService.findPageContractRecord( dataPackage, contractId);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid ;
		}
		
		/**
		 * 获取学生已考勤未扣费课程
		 * @param studentId
		 * @param contractProductId
		 * @return
		 */
		@RequestMapping(value = "/getCourseByStuPro", method =  RequestMethod.GET)
		@ResponseBody
		public Response getCourseByStuPro(@RequestParameters String studentId,@RequestParameters String contractProductId) {
			return contractService.getCourseByStuPro(studentId, contractProductId);
		}
		
		/**
		 * 合同缩单
		 * @param contractId
		 * @return
		 */
		@RequestMapping(value = "/narrowContractProduct")
		@ResponseBody
		public Response narrowContractProduct(@RequestParameters String contractId) {
			return contractService.narrowContractProduct(contractId);
		}
		
		/**
		 * 根据transactionId查询资金冲销详情记录
		 * @param transactionId
		 * @return
		 */
		@RequestMapping(value = "/findMoneyWashRecordsByTransactionId", method =  RequestMethod.GET)
		@ResponseBody
		public Map<String, Object> findMoneyWashRecordsByTransactionId(@RequestParam("transactionId") String transactionId){
			return chargeService.findMoneyWashRecordsByTransactionId(transactionId);
		}
		
		/**
		 * 修改冲销详情
		 * @param moneyWashRecords
		 * @return
		 */
		@RequestMapping(value = "/editMoneyWashRecords", method =  RequestMethod.GET)
		@ResponseBody
		public Response editMoneyWashRecords(@ModelAttribute MoneyWashRecords moneyWashRecords){
			chargeService.editMoneyWashRecords(moneyWashRecords);
			return new Response();
		}
		
		/**
		 * 取消合同产品
		 * @param contractProductId
		 * @return
		 */
		@RequestMapping(value = "/unvalidContractProduct", method =  RequestMethod.GET)
		@ResponseBody
		public Response unvalidContractProduct(@RequestParam String contractProductId){
			contractService.unvalidContractProduct(contractProductId);
			return new Response();
		}
		
		/**
		 * 关闭合同
		 * @param contractId
		 * @return
		 */
		@RequestMapping(value = "/unvalidContract", method =  RequestMethod.GET)
		@ResponseBody
		public Response unvalidContract(@RequestParam String contractId){
			contractService.unvalidContract(contractId);
			return new Response();
		}
		
		/**
		 * 删除合同
		 * @param contractId
		 * @return
		 */
		@RequestMapping(value = "/deleteContract", method =  RequestMethod.GET)
		@ResponseBody
		public Response deleteContract(@RequestParam String contractId, @RequestParam String reason) throws Exception{
			contractService.deleteContract(contractId, reason);
			return new Response();
		}
		
		/**
		 * 根据收款id查找收款审核流水
		 * @return
		 */
		@RequestMapping(value = "/findFundsAuditRecordByFundsId", method =  RequestMethod.GET)
		@ResponseBody
		public DataPackageForJqGrid findFundsAuditRecordByFundsId(@ModelAttribute GridRequest gridRequest, @RequestParam String fundsId) {
			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage= fundsAuditRecordService.findFundsAuditRecordByFundsId(fundsId, dataPackage);
			return new DataPackageForJqGrid(dataPackage) ;
		}
		
		/**
		 * 分页查询一对一合同产品
		 * 
		 * @return
		 */
		@RequestMapping(value = "/getMyOooContractProductList", method =  RequestMethod.GET)
		@ResponseBody
		public DataPackageForJqGrid getMyOooContractProductList(@ModelAttribute GridRequest gridRequest, @ModelAttribute ContractVo contractVo,
				@ModelAttribute TimeVo timeVo, String isAllDistributed) {
			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage = contractService.getMyOooContractProductList(dataPackage, contractVo, timeVo, isAllDistributed);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid ;
		}
		
		/**
		 * 查询一对一课时分配合同产品
		 * 
		 * @return
		 */
		@RequestMapping(value = "/findContractProductDistributeVoById", method =  RequestMethod.GET)
		@ResponseBody
		public ContractProductDistributeVo findContractProductDistributeVoById(@RequestParam String contractProductId) {
			return contractService.findContractProductDistributeVoById(contractProductId);
		}
		
		/**
		 * 更新一对一课时分配合同产品
		 * 
		 * @return
		 */
		@RequestMapping(value = "/editContractProductDistribute")
		@ResponseBody
		public Response editContractProductDistribute(@RequestBody ContractProductDistributeVo contractProductDistributeVo) {
			contractService.editContractProductDistribute(contractProductDistributeVo);
			return new Response();
		}
		
		/**
		 * 分配、提取一对一课时
		 * 
		 * @return
		 */
		@RequestMapping(value = "/distributeOrExtractContractProductSubject")
		@ResponseBody
		public Response distributeOrExtractContractProductSubject(@RequestBody ContractProductDistributeVo contractProductDistributeVo) {
			contractService.distributeOrExtractContractProductSubject(contractProductDistributeVo);
			return new Response();
		}
		
		/**
		 * 转移一对一课时
		 * 
		 * @return 
		 */
		@RequestMapping(value = "/transferContractProductSubject")
		@ResponseBody
		public Response transferContractProductSubject(@RequestBody ContractProductSubjectVo contractProductSubjectVo) {
			contractService.transferContractProductSubject(contractProductSubjectVo);
			return new Response();
		} 
		
		 /** 获取当前用户的课时管理操作权限
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/getCourseTimeManageAuthTags", method =  RequestMethod.GET)
		@ResponseBody
		public String getCourseTimeManageAuthTags() {
			return contractService.getCourseTimeManageAuthTags();
		}
		
		
		/**
		 * 根据合同产品id分页查询课时分配流水
		 * 
		 * @return
		 */
		@RequestMapping(value = "/findPageDistributeRecordByCpId", method =  RequestMethod.GET)
		@ResponseBody
		public DataPackageForJqGrid findPageDistributeRecordByCpId(@ModelAttribute GridRequest gridRequest, @RequestParam String contractProductId) {
			DataPackage dataPackage = new DataPackage(gridRequest);
			dataPackage = courseHoursDistributeRecordService.findPageDistributeRecordByCpId(dataPackage, contractProductId);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid ;
		}


    	@RequestMapping(value = "/getStudentRechargeRecord", method = RequestMethod.GET)
    	@ResponseBody
    	public DataPackageForJqGrid getStudentRechargeRecord (@ModelAttribute GridRequest gridRequest, @ModelAttribute ElectronicAccountChangeLogVo vo, String startDate, String endDate){
    		DataPackage dataPackage = new DataPackage(gridRequest);
			TimeVo timeVo = new TimeVo();
			if (StringUtils.isNotBlank(startDate)){
    			timeVo.setStartDate(startDate);
			}
			if (StringUtils.isNotBlank(endDate)){
    			timeVo.setEndDate(endDate);
			}
    		dataPackage = contractService.getStudentRechargeRecord(dataPackage, vo, timeVo);
    		DataPackageForJqGrid dataPackageForJqGrid = new DataPackageForJqGrid(dataPackage);
    		return dataPackageForJqGrid;
    	}

}
