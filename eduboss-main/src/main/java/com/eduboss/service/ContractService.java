package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.eduboss.common.*;
import com.eduboss.domain.*;
import com.eduboss.domainVo.*;
import com.eduboss.dto.FundsChangeSearchVo;

import org.hibernate.criterion.Order;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.handler.ContractProductHandler;


public interface ContractService {

	List<ContractProduct> findContractProductByProductAndStudent(String studentId,String productId);
	
	public void saveContract(Contract contract);
	
	/**
	 * 根据学生id查询剩余课时>0的合同
	 * @param studentId
	 * @return
	 */
	public List<ContractVo> getContractByStudentId(String studentId);
	
	/**
	 * 保存合同收款记录
	 * @param fundsVo
	 */
	public FundsChangeHistory saveFundOfContract(FundsChangeHistoryVo fundsVo);

    void updateStudentAccountInfoAmount(String studentId, BigDecimal amount, ElectronicAccChangeType type,Contract con);

    /**
	  * 保存收款冲销记录
	  * @param fundsId
	  * @param fundsWashAmount
	  */
	public FundsChangeHistory saveFundWashOfContract(String fundsId, BigDecimal fundsWashAmount);
	
	
	
	/**
	 * 保存客户信息 在合同界面
	 * @param CustomerVo
	 * @return 
	 */
	public CustomerVo saveCustomerInfo(ContractVo contractVo);

	/**
	 * 保存全款合同信息
	 * @param contractVo
	 */
	public void saveFullContractInfo(ContractVo contractVo) throws Exception;

//	/**
//	 * 保存 订金合同
//	 * @param contractVo
//	 */
//	public void saveDepositContractInfo(ContractVo contractVo);



	/**
	 * 找到相应的 合同
	 * @param dataPackage
	 * @param contractVo
	 * @param timeVo
	 * @return
	 */
	public DataPackage findPageContract(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo);

	/**
	 * @param dataPackage
	 * @param contractVo
	 * @param timeVo
	 * @return
	 */
	public List findExcelContract(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo);

	/**
	 * 获取手机用户签约的合同
	 * @param dataPackage
	 * @param userId
	 * @return
	 */
	public List<ContractMobileVo> findContractForMobile(String studentId, DataPackage dataPackage);
	
	
	public ContractMobileVo findContractByIdForMobile(String contractId);

	/**
	 * 根据合同ID 获取合同信息
	 * @param contractid
	 * @return
	 */
	public ContractVo getContractById(String contractid);

	/**
	 * 根据学生id查询有剩余金额的合同
	 * @param studentId
	 * @return
	 */
	public List<ContractVo> getSurplusContractByStudentId(String studentId);

	
	/**
	 * 收款记录导出Excel
	 * @param dataPackage
	 * @return
	 */
	public DataPackage findPageMyFundHistoryExcel(DataPackage dataPackage,
												  FundsChangeSearchVo vo);

	/**
	 * 列出相应 的 的收款记录
	 * @param dataPackage
     * @return
     */
	public DataPackage findPageMyFundHistory(DataPackage dataPackage, FundsChangeSearchVo vo);
	
	
	
	public FundsChangeHistoryVo findFundHistoryById(String id);

	/**
	 * 保存合同提成信息
     * @param contractId
     * @param refundIncomeDistributeVo
     */
	public void saveContractBonus(String contractId,
                                  String bonusType, String studentReturnId, ContractProduct contractProduct, RefundIncomeDistributeVo refundIncomeDistributeVo);
	
	

	/**
	 * 根据年级 获取到一对一的单价
	 * @param gradeDict
	 * @return
	 */
	public BigDecimal getOneOnOnePrice(DataDict gradeDict);
	
	
	
	/**
	 * 根据学生来获取最久的 正常的， 已经付完款的合同
	 * @param student
	 * @param order 
	 * @return
	 */
	public List<Contract> getOrderContracts(Student student, Order order);

	/**
	 * 在添加合同界面，根据不同的customerId 和 studentId来决定了不同的拿取初步的合同信息
	 * @param customerId
	 * @param studentId
	 * @return
	 */
	public ContractVo getContractCustomerInfo(String customerId,
			String studentId);

	/**
	 *
	 * @param fundsChangeHistoryId
	 * @return
	 */
	public List<ContractBonusVo> getContractBonusByFundsChangeHistoryId(String fundsChangeHistoryId);

	/**
	 * just for testing the auth filter of data
	 * @param dataPackage
	 * @param contractVo
	 * @param timeVo
	 * @return
	 */
	public DataPackage findPageContractForTest(DataPackage dataPackage,
			ContractVo contractVo, TimeVo timeVo);

	/**
	 * 保存修改 合同信息
	 * @param contractVo
	 */
	public void saveEditFullContractInfo(ContractVo contractVo);

	
	/**
	 * 计算 单个产品的价格
	 * @param contractProduct
	 */
	public void calSingleContractProduct(ContractProduct contractProduct);
	
	
	/**
	 * 计算合同的总计划总金额
	 * @param contract
	 */
	public void calContractPlanAmount(Contract contract);
	
	
	/**
	 * 据学生查询合同 
	 *  考虑到以后的分页 输入参数定为DataPackage
	 * @param dataPackage
	 * @param studentId
	 * @return
	 */
	 
	public DataPackage findContractByStu(DataPackage dataPackage,String studentId);

	/**
	 * 据客户查询合同 
	 *  考虑到以后的分页 输入参数定为DataPackage
	 * @param dataPackage
	 * @param customerId
	 * @return
	 */
	 
	public DataPackage findContractByCus(DataPackage dataPackage,String customerId);
	
	/**
	 * 增加资金到某产品
	 * @param oneArrangeMoney
	 * @param oneConPrdId
	 */
	void addArrangeMoneyForProduct(int oneArrangeMoney, String oneConPrdId);

	/**
	 * 分配资金到某产品, 按照默认的金额
	 * @param oneConPrdId
	 */
	void saveDefaultArrangeMonoyForProduct(ContractProduct conPrd);
	
	/**
	 * 分配资金到某产品, 按照默认的金额
	 * @param oneConPrdId
	 */
	void saveDefaultArrangeMonoyForProduct(ContractProduct conPrd,String userId);
	
	
	/**
	 * 获取 一对一 的统一价格的产品 (这个方法暂时未使用)
	 * @param student
	 * @return
	 */
	public List<ContractProduct> getOneContractProducts(Student student);

	/**
	 * 根据产品ID查询合同数量
	 * @param productId
	 * @return
	 */
	public int countContractByProductId(String productId);

	public ContractVo findInitContractByStudentId(String studentId);

	/**
	 * 处理初始化合同 
	 * @param contractVo
	 */
	public void saveEditInitContractInfo(ContractVo contractVo);
	
	/**
	 * 处理初始化合同 
	 * @param contractVo
	 */
	public void saveInitContractInfo(ContractVo contractVo) throws Exception;

	/**
     * 保存 合同的新接口
     * @param contractVo
     * @throws Exception
     */
	String saveFullContractInfoNew(ContractVo contractVo)throws Exception;

	/**
	 * 保存 合同的新接口
	 * @param contractVo
	 * @param updateInventory是否更新小班库存
	 * @throws Exception
	 */
	String saveFullContractInfoNew(ContractVo contractVo, boolean updateInventory) throws Exception;

	/**
	 * 新的 分配资金
	 * @param arrangeMoney
	 * @param conPrdId
	 */
	void saveContractArrangeMoney_new(BigDecimal[] arrangeMoney, String[] conPrdId);
	
	void saveContractProductMoney(BigDecimal[] arrangeMoney, String[] conPrdId,String fundId, Contract contract);
	
	

	/**
	 * 根据不同的类型的产品 产生不同的handler
	 * @param productType
	 * @return
	 */
	ContractProductHandler selectHandlerByType(ProductType productType);

	/**
	 * 按照时间顺序倒序 拿有效的合同产品
	 * @param student
	 * @param asc
	 * @return
	 */
	public List<ContractProduct> getOrderValidOneOnOneContractProducts(Student student);

    /**
     * 按照课程拿有效的合同产品
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     * 若课程无对应产品则根据学生获取所有报读且有效的一对一产品
     * @param course
     * @return
     */
    public List<ContractProduct> getOrderValidOneOnOneContractProducts(Course course);
    
    /**
     * 按照课程拿有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     * @param course
     * @return
     */
    public List<ContractProduct> getOrderValidSubjectOneOnOneContractProducts(Course course);
    
    /**
     * 计算按照课程有效的对应课程科目的合同产品(新加逻辑)
     * 若课程有对应产品且无归属产品组 返回当前学生报读且有效的对应合同产品
     * 若课程有对应产品且有归属产品组 返回当前学生报读且有效的对应合同产品及同产品组下产品
     * @param course
     * @return
     */
    public BigDecimal countOrderValidSubjectOneOnOneContractProducts(Course course);

	/**
	 * 对 修改合同的一个保存
	 * @param contractVo
	 */
	void saveEditFullContractInfoNew(ContractVo contractVo) throws Exception;

	/**
	 * 根据controctProductId 删除它绑定的 科目信息
	 * @param controctProductId
	 */
	public void deleteSubjectsByContractProduct(String controctProductId);

	public ContractProductVo getContractProductById(String contractProductId);
	
	/**
	 * 结课
     * @param returnAmountFromPromotionAcc
     * @param contractProductId
     * @param list
     * @param liveContractProductRefundVo
     */
	public void closeContractProcuct(BigDecimal amountFromPromotionAcc, BigDecimal returnAmountFromPromotionAcc, String contractProductId, Boolean isRefundMoney,
                                     BigDecimal returnMoney, BigDecimal returnSpecialAmount,
                                     String returnReason, String returnType, MultipartFile certificateImageFile, String accountName, String account,
                                     String returnCampusId, String returnUserId, String remark,
                                     List<IncomeDistributionVo> list, LiveContractProductRefundVo liveContractProductRefundVo) throws Exception;
	
	/**
	 * 查询学生帐户情况, 除电子账户外，其他均实时计算
	 * 
	 * @param studentId
	 * @return
	 */
	public StudnetAccMv getStudentAccoutInfo(String studentId);

	/**
	 * 更新 合同domain 的 值， 包括 总金额， 一对一总金额， 小班总金额， 其他总结， 已付款，总消费金额， 优惠金额
	 * @param contract
	 */
	public void calculateContractDomain(Contract contract);
	
	/**
	 * 更新 合同domain 的 值， 包括 总金额， 一对一总金额， 小班总金额， 其他总结， 已付款，总消费金额， 优惠金额 Jdbc
	 * @param contract
	 */
	public void calculateContractDomainJdbc(Contract contract);

	
	/**
	 * 插入一条收费记录
	 * @param contract
	 * @param promotionAmount
	 * @param student
	 * @param promotionMoney
	 */
	void insertOneFundRecord(Contract contract, BigDecimal promotionAmount,
			Student student, PayWay promotionMoney);

	
	/**
	 * 根据合同 删除 优惠收款记录
	 * @param contract
	 */
	void deleteOneFundRecordForPromotion(Contract contract);

	public void updateFundHistoryPos(FundsChangeHistoryVo vo);

	/**
	 * 从合同产品提取金额
	 * @param takeawayMoney
	 * @param conPrdId
	 */
	public void saveContractTakeawayMoney(BigDecimal takeawayMoney,
			String conPrdId,BigDecimal maxKeTiquMoney);

	/**
	 * 根据校区和小班得到可以报读该小班的目标班学生
	 * @param campus
	 * @param miniClassId
	 * @return
	 */
	public List<ContractProductVo> getEscProductByCampus(
			String miniClassId);
	
	
	public List<Map<String,String>> getEscProductMiniClassId(
			String miniClassId);
	
	/**
	 * 获取学生已考勤未扣费课程
	 * @param studentId
	 * @param contractProductId
	 * @return
	 */
	public Response getCourseByStuPro(String studentId,String contractProductId);
	
	/**
	 * 找到相应的 合同修改记录
	 * @param dataPackage
	 * @param contractId
	 * @return
	 */
	public DataPackage findPageContractRecord(DataPackage dataPackage, String contractId);

	public Map getOtherBonusTotal(String contractId, String fundId);

	/**
	 * 获取可分配业绩
	 * @param contractId
	 * @param fundId
     * @return
     */
	Map getKeFenPeiBonus(String contractId, String fundId);

	/**
	 * 更新收款记录的归属校区
	 * @param id
	 * @param blCampusId
	 * @return
	 */
    void updateFundCampusIdById(String id, String blCampusId);

	/** 
	 * 合同缩单
	* @param contractId
	* @return
	* @author  author :Yao 
	* @date  2016年8月9日 上午10:33:52 
	* @version 1.0 
	*/
	public Response narrowContractProduct(String contractId);
	
	/**
	 * 取消合同产品
	 * @param contractProductId
	 */
	public void unvalidContractProduct(String contractProductId);
	
	/**
	 * 关闭合同
	 * @param contractId
	 */
	public void unvalidContract(String contractId);
	
	/**
	 * 删除合同
	 * @param contractId
	 */
	public void deleteContract(String contractId, String reason) throws Exception;
	
	/**
	 * 分页查询一对一合同产品
	 * @param dataPackage
	 * @param contractVo
	 * @param timeVo
	 * @return
	 */
	public DataPackage getMyOooContractProductList(DataPackage dataPackage, ContractVo contractVo, TimeVo timeVo, String isAllDistributed);
	
	/**
	 * 查询一对一课时分配合同产品
	 * @param contractProductId
	 * @return
	 */
	ContractProductDistributeVo findContractProductDistributeVoById(String contractProductId);
	
	/**
	 * 保存或修改一对一课时分配合同产品
	 * @param contractProductDistributeVo
	 */
	void editContractProductDistribute(ContractProductDistributeVo contractProductDistributeVo);

	/**
	 * 根据收款记录获取业绩分配记录
	 * @param fundsChangeHistoryId
	 * @return
	 */
    List<IncomeDistributionVo> getIncomeDistributionByFundsId(String fundsChangeHistoryId);

	/**
	 * 保存业绩
	 * @param fundsChangeHistoryId
	 * @param list
	 */
	void saveIncomeDistribution(String fundsChangeHistoryId, List<IncomeDistributionVo> list);

	/**
	 * 获取合同收款记录相关信息
	 * @param contractId
	 * @param fundId
	 * @return
	 */
    Map getContractInfo(String contractId, String fundId);

	DataPackage getListByFundsChangeHistoryId(DataPackage dp, String fundId);
	
	/**
	 * 分配、提取一对一课时
	 * @param contractProductDistributeVo
	 */
	void distributeOrExtractContractProductSubject(ContractProductDistributeVo contractProductDistributeVo);

	/**
	 * 转移一对一课时
	 * @param contractProductSubjectVo
	 */
	void transferContractProductSubject(ContractProductSubjectVo contractProductSubjectVo);

	/**
	 * 获取当前用户的课时管理操作权限
	 * @return
	 */
	String getCourseTimeManageAuthTags();
	
	/**
	 * 退费一对一课时
	 * @param contractProduct
	 */
	void refundContractProductSubject(ContractProduct contractProduct);

	/**
	 *
	 * @param dataPackage
	 * @param vo
	 * @param timeVo
	 * @return
	 */
	DataPackage getStudentRechargeRecord(DataPackage dataPackage, ElectronicAccountChangeLogVo vo, TimeVo timeVo);

    void saveIncomeDistributionForCloseContractProduct(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list, String studentReturnId, ContractProduct contractProduct);

    Response saveContractAndLive(ContractVo contractVo);

	/**
	 * 直播合同校区信息
	 * 线下校区信息
	 * @param contractIdList
	 * @return
	 */
	Map<Object, Object> getContractCampusInfoByIdList(String contractIdList);




    void saveAccountChargeRecord(BigDecimal realChargeMoney, ContractProduct targetConPrd, ProductType live, ChargeType normal, PayType real, BigDecimal auditedCourseHours, Curriculum curriculum, String transactionId);


    void chargeLiveOfCurriculum(ChargeOrWashCurriculumVo chargeOrWashCurriculumVo);


    void closeContractProductForLive(String contractId, String liveId, LiveContractProductRefundVo liveContractProductRefundVo) throws Exception;

	/**
	 * 删除双师考勤记录的时候需要检查扣费记录里面不会再有这些双师考勤的id redmine id #1414
	 * @param id
	 * @param twoClassId
	 */
    void updateNullTwoTeacherClassStudentAttendentByStudentAndTwoClassId(String id, int twoClassId);


	/**
	 * 保存扣费和冲销时候的
	 * @param curriculum
	 */
	void saveCurriculum(Curriculum curriculum);

	ContractLiveVo liveContract(ContractLiveVo contractVo);

	/**
	 * 同步直播合同
	 * @param contractVo
	 * @return
	 */
	String synchronizeLiveContract(ContractLiveVo contractVo) throws Exception;

    void closeLiveContractProductToElectronic(LiveContractChangeVo liveContractChangeVo) throws Exception;

	String changeCurriculum(LiveContractChangeVo liveContractChangeVo) throws Exception;

	String saveLiveContract(ContractLiveVo contractVo) throws Exception;

	void addToIncomeDistributeStatements(FundsChangeHistory fundsChangeHistory, List<IncomeDistributionVo> list);

	boolean checkHasLiveContractByStudent(String studentId);

	/**
     * 根据关联系统的订单编号查找合同产品列表
     * @param relatedNo
     * @return
     */
    List<ContractProduct> listContractProductByAssocRelatedNo(String relatedNo);

	/**
	 * 是否能修改业绩
	 * @param fundsChangeHistoryId
	 * @return
	 */
	boolean canUpdateIncomeDistribution(String fundsChangeHistoryId);
	
}
