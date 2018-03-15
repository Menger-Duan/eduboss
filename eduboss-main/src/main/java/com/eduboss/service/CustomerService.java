/**
 * 
 */
package com.eduboss.service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;




import com.eduboss.common.AppointmentType;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.CustomerEventType;
import com.eduboss.common.PhoneEvent;
import com.eduboss.common.PhoneType;
import com.eduboss.domain.Customer;
import com.eduboss.domain.CustomerAppointment;
import com.eduboss.domain.CustomerImportTransform;
import com.eduboss.domain.GainCustomer;
import com.eduboss.domain.Organization;
import com.eduboss.domain.TransferCustomerCampus;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ChangeUserRoleRecordVo;
import com.eduboss.domainVo.CustomerAppointmentVo;
import com.eduboss.domainVo.CustomerCallsLogVo;
import com.eduboss.domainVo.CustomerDataImport;
import com.eduboss.domainVo.CustomerDeliverTarget;
import com.eduboss.domainVo.CustomerFollowUpRecrodsVo;
import com.eduboss.domainVo.CustomerFolowupVo;
import com.eduboss.domainVo.CustomerLessVo;
import com.eduboss.domainVo.CustomerPoolDataVo;
import com.eduboss.domainVo.CustomerPoolVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.DataImportResult;
import com.eduboss.domainVo.DistributeCustomerVo;
import com.eduboss.domainVo.FollowupCustomerVo;
import com.eduboss.domainVo.PhoneCustomerVo;
import com.eduboss.domainVo.ReceptionistCustomerVo;
import com.eduboss.domainVo.SignCustomerVo;
import com.eduboss.domainVo.SignupCustomerVo;
import com.eduboss.domainVo.StudentImportVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.UserDetailForMobileVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;


/**
 * @classname	CommonService.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-11-13 12:04:19
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */
@Service
public interface CustomerService {
	
	public String updateCustomer(Customer customer);
	public String saveOrUpdateCustomer(Customer cus)  ;
	
	public String saveOrUpdateCustomer(CustomerVo customerVo) ;
	

	public Response updateNormalCustomerForApp(CustomerVo customerVo) ;
	
	public void deleteCustomer(String resIds) throws ApplicationException;
	
	public List<Customer> getCustomersForAutoCompelate(String term);
	
	@Deprecated
	public DataPackage getCustomers(CustomerVo res, DataPackage dp);
	@Deprecated
	public DataPackage getCustomers(CustomerVo res, DataPackage dp, boolean onlyShowUndeliver);
	
	public CustomerVo findCustomerById(String id) throws ApplicationException;
	public CustomerVo findCustomerById(Customer cus) throws ApplicationException;
	
	public void savePaddingStudentJsonInfo(Set<StudentImportVo> vos,String customerId) throws ApplicationException;
	
	public void delPotentialStudent(String stuId) throws ApplicationException;
	
	public DataPackage gtCustomerFollowingRecords(Customer res, DataPackage dp) throws ApplicationException;
	
	public DataPackage gtCustomerFollowingRecords(CustomerFolowupVo res, DataPackage dp) throws ApplicationException;
	
	public void saveCustomerFollowRecord(CustomerFolowupVo follow) throws ApplicationException;
	
	/**
	 * 获取公共资源池资源
	 * 根据poolType获取用户所在的分公司公共资源和校区公共资源
	 * 客户字段DELIVER_TYPE取值：0-分公司资源，1-分公司公共资源，2-校区资源，3-校区公共资源，4-分配给咨询师
	 * 根据用户ID获取归属分公司ID及校区ID，用来作为查询的条件
	 */
	public DataPackage getCustomerInPublicPool(String userId, String poolType, DataPackage dp) throws ApplicationException;
	
	/**
	 * 获取自动完成查询客户功能的记录，把输入数据与姓名或电话作比较，返回包括输入数据的客户记录
	 */
	public DataPackage getAutoCompeleteCustomer(String inputStr, DataPackage dp);
	
	/**
	 * 保存客户预约信息前并推送一个通知给前台
	 */
	public void addCustomerAppointment(CustomerAppointmentVo appointment) throws ApplicationException;
	
	/**
	 * 查看今天的预约
	 * 咨询师返回自己的记录，前台返回本校区的记录
	 */
	public DataPackage getTodayAppointment(CustomerAppointment CustomerAppointment, DataPackage dp) throws ApplicationException;
	
	/**
	 * 确认预约
	 * 对已上门的预约进行确认
	 */
	public void appointmentConfirm(String appointmentId,String visitType) throws ApplicationException;
	
	/**
	 * 查询各分配渠道的对象列表，并查询各对象在指定客户条件下所跟进的客户列表
	 */
	public List<CustomerDeliverTarget> getCustomerDeliveTargets(CustomerDeliverType deliverType, String campusId, String cusRecordDate, String cusOrg);
	
	/**
	 * 页面查询条件- 根据跟进对象类型查询具体的跟进对象名称
	 * @param deliverType
	 * @return
	 */
	public List<CustomerDeliverTarget> getCustomerTargetByDeliveType(CustomerDeliverType deliverType);
		
	/**
	 * 释放客户成为新客户
	 */
	public void releaseCustomer(Customer cus);

	/**
	 * 根据 customerId 拿到Customer的Vo
	 * @param customeId 客户ID
	 * @return
	 */
	public CustomerVo getVoById(String customeId);
	
	public List<CustomerVo> getCustomerNames(CustomerVo vo);

	public CustomerVo findCustomerByPhone(String phone);
	
	/**
	 * 前台确认客户
	 * @param customerId
	 */
	public void recepitionistConfirmCustomer(String customerId) ;
	
	/**
	 * 设定下次跟进时间
	 */
	public void setCustomerNextFollowupTime(CustomerAppointmentVo appointment);
	

	/**
	 * 获取客户跟进概览
	 * @return 
	 */
	public Map findCustomerDynamicCount(String StartDate,String endDate)  ;
	
	public void saveCustomerCallsLog(String phone,String callsTime,PhoneType phoneType, PhoneEvent phoneEvent);

	public DataPackage getPhoneRecords(DataPackage dataPackage,
			CustomerCallsLogVo customerCallsLog);

	/**
	 * 根据orgLevel 获取所有上级客户资源池 (为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	public List<CustomerPoolVo> getCustomerPool(String orgLevel);
	
	/**
	 * 根据orgLevel 获取所有上级客户资源池 和可分配的人(为空  默认当前登陆用户的)
	 * @param orgLevel
	 * @return
	 */
	public List<CustomerPoolVo> getCustomerPoolAndUser(String orgLevel);
	
	public List<CustomerPoolVo> getCustomerPoolAndUserWithDI(String orgLevel);
	
	public Customer findById(String id);

	public DataPackage getTodayFollowup(DataPackage dataPackage);

	public boolean checkCustomerTurnCampus(String customerId);

	
	/**
	 * 更新 客户的信息， 单独的对某个字段进行更新
	 * @param cusVo
	 */
	public void updateCustomerForSimple(CustomerVo cusVo);
	

	//获取  下次跟进或预约
	public CustomerFollowUpRecrodsVo getAppointment(String customerId,AppointmentType appointmentType);
	
	/**
	 * 前台预约信息
	 */
	public DataPackage getCustomerAppointment(CustomerAppointmentVo customerAppointmentVo,DataPackage dp);
	
	/**
	 * 预约信息登记
	 */
	public CustomerAppointmentVo findCustomeraAppointmentById(String id);
	
	/**
	 * 预约信息登记保存，分配咨询师
	 */
	public void customerAppointmentVisit( String id, String customerId,String visitId,String deliverTargetId);
	
	/**
	 *  将导入的list数据进行处理，导入成功返回真
	 */
	public DataImportResult customerDataImportByList(List<List<Object>> list,CustomerDeliverType cdt,String[] strs);
	
	/**
	 *  将导入的list数据进行处理，导入成功返回真
	 */
	public DataImportResult customerDataImportByList(List<List<Object>> list,CustomerDeliverType cdt,String[] strs,DataImportResult dir);
	
	/**
	 * 前台客户资源
	 */
	public DataPackage ReCeptionistGetCustomers(String startDate,String endDate,CustomerVo customerVo, DataPackage dp, boolean onlyShowUndeliver);

	/**
	 * 得到签单的客户列表
	 */
	public DataPackage getAllContractCustomer(StudentVo customerVo,
			DataPackage dataPackage,String type);

	public CustomerFolowupVo findCustomeraFollowUpById(String id);

	public void signCustomerComeon(String id, String customerId,
			String visitId, String deliverTargetId);

	/**
	 * 手机接口
	 * @param id 客户id
	 * @return
     */
	CustomerLessVo findCustomerLessVoById(String id);

	public DataPackage getCustomerListForPlat(CustomerVo cus,
			DataPackage dataPackage);
	
	/**
	 * 客户导入明细
	 */
	public DataPackage getCustomerImportInfo(CustomerImportTransform cif,String startDate,String endDate,String gradeName,DataPackage dp);
	
	/**
	 * 查询单个导入的客户
	 * @param id
	 * @return
	 */
	public CustomerImportTransform getImportCustomerById(String id);
	
	/**
	 * 修改导入的客户
	 */
	public void editImportCustomer(CustomerImportTransform cif,StudentImportVo stu,Boolean listImp);
	
	/**
	 * 删除导入的客户信息
	 */
	public void delImportCustomer(CustomerImportTransform cif);
	
	/**
	 * 批量导入客户
	 */
	public void batchImportCustomer(String ids);
	
	/**
	 * 获取客户资源改变状态
	 */
	public Response changeCustomerStatus(Customer cus);
	
	/**
	 * 跑批，客户资源导入从临时表到customer
	 */
	public void importToCustomer() throws SQLException;
	
	public Customer loadCustomerByContact(String contact);
	/**
	 * 保存转介绍客户
	 *  2016/07/30
	 */
	public String saveTransferCustomer(Customer customer,CustomerVo customerVo);
	/**
	 * 分配客户管理--多条件获取客户列表   个人池
	 * @param vo  装载不同的查询条件的vo
	 * @param dataPackage
	 * @param workbrenchType 平台类型 外呼 网络  咨询师
	 * @date 2016/08/16
	 * @return DataPackage
	 */
	public DataPackage getDistributeCustomersPersonPool(DistributeCustomerVo vo,DataPackage dataPackage,String workbrenchType);
	
	/**
	 * 分配客户管理--多条件获取客户列表  其他资源池
	 * @param vo  装载不同的查询条件的vo
	 * @param dataPackage
	 * @param workbrenchType 平台类型 外呼 网络  咨询师
	 * @date 2016/08/16
	 * @return DataPackage
	 */	
	public DataPackage getDistributeCustomersResourcePool(DistributeCustomerVo vo,DataPackage dataPackage,String workbrenchType);
	
	/**
	 * 分配客户管理--获取多个客户(从个人或者其他资源池获取)
	 */
	Response distributeCustomer(String cusId,String poolType,String workbrenchType);
	
	/**
	 * 分配客户管理--获取客户资源
	 * 替代方法  changeCustomerStatus
	 * @param customer  被获取的用户
	 * @param user  当前登录的用户
	 * @return response的code==0则执行成功
	 * @author xiaojinwang
	 */
	public Response distributeCustomer(Customer customer,User user,String poolType,String workbrenchType);
	
	/**
	 * 分配客户 添加分配人记录
	 * @param customerVo  封装新的分配类型 分配对象的 前序分配类型 前序分配对象  
	 * @param customerId  被分配的客户
	 * @param allocateUserId 分配客户的分配人员UserId
	 * @return
	 */
	public Response allocateCustomer(CustomerVo customerVo,String customerId,String allocateUserId,String allocateReason,CustomerEventType eventType);
	
	/**
	 * 跟进客户管理--多条件获取不同工作台的跟进客户列表
	 * @param vo
	 * @param dataPackage
	 * @param workbrenchType  平台类型 外呼 网络  咨询师
	 * @date 2016/08/22
	 * @return DataPackage
	 * @author xiaojinwang
	 */
	public DataPackage getFollowupCustomers(FollowupCustomerVo vo,DataPackage dataPackage,String workbrenchType);
	
	/**
	 * 客户管理--多条件获取已签单客户列表
	 * @param vo
	 * @param dataPackage
	 * @date 2016/08/24
	 * @return DataPackage
	 * @author xiaojinwang
	 */	
	public DataPackage getAllCustomers(CustomerVo vo,DataPackage dataPackage);
	
	/**
	 * 客户列表 网络专用 
	 * @param vo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getCustomersForInternet(CustomerVo vo,DataPackage dataPackage);
	
	/**
	 * 跟进客户管理--释放客户资源
	 * @param deliverTarget 分配目标
	 * @param cusIds 多个待释放的客户资源
	 * @return
	 */
	public Response releaseCustomer(String deliverTarget,String[] cusIds,User user);
	
	/**
	 * 根据客户id获取学生
	 */
	public Map<String, Object> getStudentsByCusId(String customerId);
	
	/**
	 * 跟进客户管理--多条件获取不同工作台的 流失客户列表
	 * @param vo
	 * @param dataPackage
	 * @param workbrenchType  平台类型 外呼 网络  咨询师
	 * @date 2016/09/27
	 * @return DataPackage
	 * @author xiaojinwang
	 */
	public DataPackage getLostCustomers(FollowupCustomerVo vo,DataPackage dataPackage);
	/**
	 * 跟进客户管理--多条件获取不同工作台的 无效客户列表
	 * @param vo
	 * @param dataPackage
	 * @param workbrenchType  平台类型 外呼 网络  咨询师
	 * @date 2016/09/27
	 * @return DataPackage
	 * @author xiaojinwang
	 */
	public DataPackage getInvalidCustomers(FollowupCustomerVo vo,DataPackage dataPackage);
	
	
	/**
	 * 	获取不同资源入口的客户概览数
	 * @param startDate
	 * @param endDate
	 * @param workbrenchType  外呼 网络  咨询师
	 * @return
	 */
	public Map<String, Object> getCustomerOverviewForPlat(String startDate,String endDate,String workbrenchType);
	/**
	 * 	获取前台首页的客户概览数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Map<String, Object> getCustomerOverviewForReception(String startDate,String endDate);	
	
	
	/**
	 * 跟进客户管理--签单客户列表
	 * @param vo
	 * @param dataPackage
	 * @param workbrenchType
	 * @return
	 */	
	public DataPackage getSignupCustomers(SignupCustomerVo vo,DataPackage dataPackage);
	
	/**
	 * 获取前台工作台的分配客户列表
	 * @param vo  封装查询条件的vo
	 * @param dataPackage  分页查询列表结果 
	 * @param isDeliver  true 已分配  false 待分配
	 * @return
	 */	
	public DataPackage getDistributeCustomers(DistributeCustomerVo vo,DataPackage dataPackage);
	
	
	/**
	 * 客户管理     网络资源管理  获取网络分配给营运经理的分配客户列表
	 * @param vo  封装查询条件的vo
	 * @param dataPackage  分页查询列表结果 
	 * @param isDeliver  true 已分配  false 待分配
	 * @return
	 */	
	public DataPackage getOperateManagerCustomers(DistributeCustomerVo vo,DataPackage dataPackage);
	
	
	/**
	 * 转校客户管理
	 */
    public DataPackage transferCustomerList(ReceptionistCustomerVo vo,DataPackage dp,String workbrenchType);

	/**
	 * 加载客户预约信息根据条件判读是否修改客户的资源入口
	 * @param visitId
	 * @param customerVo
	 * @return
	 */
	public SignCustomerVo loadFollowupCustomerById(String visitId,CustomerVo customerVo);
	/**
	 * 获取客户预约记录
	 * @param cus
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getAppointmentRecords(CustomerFolowupVo cus,DataPackage dataPackage);
    
	
	/**
	 * 保存登记记录
	 */
	public Response signCustomerComeon(SignCustomerVo signCustomerVo);
	
	/**
	 * 前台客户登记记录
	 */
	public DataPackage findPageCustomerForReceptionistWb(ReceptionistCustomerVo vo, DataPackage dp);

	public String addTransferCustomer(TransferCustomerCampus transferCampus);
	
	/**
	 * 前台工作台  客户管理  分配管理  --接收操作
	 * @param transferCustomerId
	 * @param customerVo  封装Id deliverTarget deliverType dealStatus  res_Entrance 后两者不一定需要
	 * @return
	 */
	public Response receiveTransferCampusCustomer(String transferCustomerId, CustomerVo customerVo) ;
    
	/**
	 * 前台抢客
	 */
	public Response gainCustomer(GainCustomer gainCustomer,String res_Entrance);
	
	public DataPackage getCustomerFollowingRecords(CustomerFolowupVo res, DataPackage dp) throws ApplicationException;
	
	public DataPackage findCustomerByContactList(CustomerVo contact,DataPackage dataPackage);
	
	/**
	 * 校验客户的手机号码是否存在 支持全范围的查询   客户录入--校验手机号码
	 * @param contact
	 * @param workbrenchType
     * @return
	 */
	public Response checkCustomerContact(String contact, String workbrenchType);
	
	/**
	 * 保存客户的时候判断学生是否重名
	 * @param customerVo
	 * @return
	 */
	public Response checkCustomerStudent(CustomerVo customerVo);
	
	/**
	 * 查询前台和客户管理的新客户录入记录
	 */
	public DataPackage loadCustomerRecord (DataPackage dataPackage,CustomerVo customerVo);
	
	public Boolean isExceedPeriod(String customerId);
	
	/**
	 * 客户管理--客户资源调配
	 * @param cusId
	 * @param customerVo
	 * @return
	 */
	public Response allocateCustomerResource(String cusId,CustomerVo customerVo);
	
    /**
     * 查看资源池的客户
     * @param vo
     * @param dataPackage
     * @param resourceId
     * @return
     */
	public DataPackage getResourcePoolCustomer(DistributeCustomerVo vo,DataPackage dataPackage,String resourceId);
	
	public void updateTransferCustomer(TransferCustomerCampus transferCampus);
	
	public List<TransferCustomerCampus> getTransferCustomerRecord(String customerId);

	/**
	 * 手工设置客户上门
	 */
	public Response setCustomerVisitCome(String customerId);
	
	/**
	 * 查询切换前台模式记录
	 */
	public DataPackage getChangeUserRoleRecords(DataPackage dataPackage,ChangeUserRoleRecordVo recordVo);
	
	/**
	 * 根据userId和用户类型userRoleSign获取该用户手里的客户Customer
	 */
	public List<Customer> getCustomersByUserId(String userId,String userRoleType);


	/**
	 * 批量删除
	 * @param ws
	 */
    void delImCus(String ws);
    
	/**
	 * 客户管理--客户列表 市场专用
	 * @param vo
	 * @param dataPackage
	 * @date 2017/05/17
	 * @return DataPackage
	 * @author xiaojinwang
	 */	
	public DataPackage getAllCustomersForMarket(CustomerVo vo,DataPackage dataPackage);

	public List<Map<Object,Object>> getAppAllContractCustomer(StudentVo studentVo,DataPackage dp);
	
	//客户详情删除学生
	public Response setDeleteStudent(String studentId);
	/**
	 * 
	 * @author: duanmenrun
	 * @Title: getUserStayCustomerListByStatus 
	 * @Description: TODO 查询用户待获取客户（营主待分配）
	 * @throws
	 * @param dataPackage
	 * @param status distribution待分配,obtain获取
	 * @return      
	 */
	public DataPackage getUserStayCustomerListByStatus(DataPackage dataPackage, String status);
	/**
	 * 
	 * @author: duanmenrun
	 * @Title: getUserFollowCustomerList 
	 * @Description: TODO 查询用户跟进客户客户
	 * @throws
	 * @param dataPackage
	 * @param status SIGNEUP签单客户     FOLLOWING跟进客户
	 * @return      
	 */
	public DataPackage getUserFollowCustomerList(DataPackage dataPackage, String status);
	/**
	 * 
	 * @author: duanmenrun
	 * @Title: updateCustomerDeleverTarget 
	 * @Description: TODO 营主分配客户给咨询师
	 * @throws 
	 * @param customerVo
	 * @return
	 */
	public Response updateCustomerDeleverTarget(CustomerVo customerVo);
	
	/**
	 * 手机通话录入
	 * @param cus
	 */
	public void saveOrUpdatePhoneCusomer(PhoneCustomerVo cus);
	/**
	 * 判断用户是否为营主推送消息
	 * @author: duanmenrun
	 * @Title: judgeUserSendMsg 
	 * @Description: TODO 
	 * @throws 
	 * @param receptionUserId
	 * @param currentUserId
	 */
	public void judgeUserSendMsg(String receptionUserId ,String currentUserId);
	/**
	 * 查询可分配咨询师
	 * @author: duanmenrun
	 * @Title: getAllDistributableZXS 
	 * @Description: TODO 
	 * @return
	 */
	public List<UserDetailForMobileVo> getAllDistributableZXS();
	/**
	 * 根据organizationId查询咨询师（分公司查询校区）
	 * @author: duanmenrun
	 * @Title: getDistributableZXSByParentId 
	 * @Description: TODO 
	 * @param organizationId
	 * @return
	 */
	public Response getDistributableZXSByParentId(String organizationId);
	/**
	 * 根据名字查询可分配的用户
	 * @author: duanmenrun
	 * @Title: getDistributableUserByName 
	 * @Description: TODO 
	 * @param token
	 * @param userName
	 * @return
	 */
	public Response getDistributableUserByName(String userName);
	/**
	 * app 查询客户
	 * @author: duanmenrun
	 * @Title: getCustomerDetailById 
	 * @Description: TODO 
	 * @param customerId
	 * @return
	 */
	public Response getCustomerDetailById(String customerId);
	/**
	 * 分页查询客户跟进记录
	 * @author: duanmenrun
	 * @Title: getCustomerFollowUpRecrodsList 
	 * @Description: TODO 
	 * @param customerId
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getCustomerFollowUpRecrodsList(String customerId, DataPackage dataPackage);
	/**
	 * 查询用户待获取客户（营主待分配）、跟进中客户数量
	 * @author: duanmenrun
	 * @Title: getUserCustomerCount 
	 * @Description: TODO 
	 * @return
	 */
	public Response getUserCustomerCount();
	/**
	 * app咨询师获取跟进客户
	 * @author: duanmenrun
	 * @Title: mobileDistributeCustomer 
	 * @Description: TODO 
	 * @param cusId
	 * @param status
	 * @return
	 */
	public Response mobileDistributeCustomer(String cusId, String status);
	/**
	 * 导入客户
	 * @author: duanmenrun
	 * @Title: customerDataImportByFile 
	 * @Description: TODO 
	 * @param file
	 * @param customerPoolDataVo
	 * @return
	 */
	public CustomerDataImport customerDataImportByFile(File file,CustomerPoolDataVo customerPoolDataVo);
	/**
	 * 查询导入历史记录
	 * @author: duanmenrun
	 * @Title: customerImportHistoryList 
	 * @Description: TODO 
	 * @param dataPackage
	 * @param timeVo
	 * @return
	 */
	public DataPackage customerImportHistoryList(DataPackage dataPackage, TimeVo timeVo);
	/**
	 * 校验客户学生姓名
	 * @author: duanmenrun
	 * @Title: checkCustomerStudengName 
	 * @Description: TODO 
	 * @param cusId
	 * @param stuId
	 * @param stuName
	 * @return
	 */
	public Response checkCustomerStudengName(String cusId, String stuId, String stuName);
	
}
