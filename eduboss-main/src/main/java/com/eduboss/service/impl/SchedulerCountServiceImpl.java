package com.eduboss.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.impl.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.common.utils.HttpHeaders;
import com.eduboss.common.CustomerDealStatus;
import com.eduboss.common.CustomerDeliverType;
import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.common.LiveContactType;
import com.eduboss.common.LiveFinanceType;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.common.SchedulerExecuteStatus;
import com.eduboss.dao.ProcedureDao;
import com.eduboss.domain.Customer;
import com.eduboss.domain.DeliverTargetChangeRecord;
import com.eduboss.domain.LivePaymentRecord;
import com.eduboss.domain.ResourcePool;
import com.eduboss.domain.Role;
import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.domain.SystemConfig;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AttentanceInfoVo;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.domainVo.SchedulerExecuteLogVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.jedis.CleanSmallclassHandler;
import com.eduboss.jedis.QueueBonusHandler;
import com.eduboss.jedis.QueueFinanceHandler;
import com.eduboss.jedis.QueueIncomeHandler;
import com.eduboss.jedis.QueueStudentStatusHandler;
import com.eduboss.jedis.TongLianPayStatusHandler;
import com.eduboss.service.CustomerService;
import com.eduboss.service.DeliverTargetChangeService;
import com.eduboss.service.LivePaymentRecordService;
import com.eduboss.service.OdsMonthIncomeCampusService;
import com.eduboss.service.OdsMonthPaymentRecieptService;
import com.eduboss.service.OperationCountService;
import com.eduboss.service.PromiseClassService;
import com.eduboss.service.ResourcePoolService;
import com.eduboss.service.SchedulerCountService;
import com.eduboss.service.SchedulerExecuteLogService;
import com.eduboss.service.SystemConfigService;
import com.eduboss.service.UploadFileService;
import com.eduboss.service.UserService;
import com.eduboss.sms.AliyunSmsUtil;
import com.eduboss.sms.MessageUtil;
import com.eduboss.task.SycInfoThread;
import com.eduboss.task.SycJobThread;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.HttpClientUtil;
import com.eduboss.utils.HttpClientUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.MessageQueueUtils;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.StringUtil;
import com.eduboss.utils.WebClientDevWrapper;


@Service
public class SchedulerCountServiceImpl implements SchedulerCountService {
    
    private final static Logger LOGGER = Logger.getLogger(SchedulerCountServiceImpl.class);

	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	
	@Autowired
	private OperationCountService operationCountService;

	@Autowired
	private SchedulerExecuteLogService schedulerExecuteLogService;

	@Autowired
	private SystemConfigService systemConfigService;

	@Autowired
	private UploadFileService uploadFileService;

	@Autowired
	private ResourcePoolService resourcePoolService;

	@Autowired
	private PromiseClassService promiseClassService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private OdsMonthIncomeCampusService odsMonthIncomeCampusService;


	@Autowired
	private OdsMonthPaymentRecieptService odsMonthPaymentRecieptService;
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private DeliverTargetChangeService deliverTargetChangeService;
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private LivePaymentRecordService livePaymentRecordService;
	/**
	 * 更新学生的剩余课时等信息   
	 * @throws SQLException
	 */
	@Scheduled(cron="0 0 01 * * ?")
	public void updateStudentAccMv() throws SQLException{
		System.out.println("开始"+DateTools.getCurrentDateTime());
		operationCountService.updateStudentAccMvRemainAmount();
		System.out.println("结束"+DateTools.getCurrentDateTime());
	}

	/**
	 * 执行系统审批收款记录
	 * @throws SQLException
	 */
	@Scheduled(cron="0 0 01,12,18 * * ?")
	public void auditFundsChangeHistory() throws SQLException{
		System.out.println("开始执行系统审批收款记录"+DateTools.getCurrentDateTime());
		operationCountService.auditFundsChangeHistory();
		System.out.println("结束执行系统审批收款记录"+DateTools.getCurrentDateTime());
	}

	/**
	 * 更新学生的状态（新签、在读、停课、结课、毕业）
	 */
	@Scheduled(cron="0 35 01 * * ?")
	@Override
	public void updateStudentStatus() throws SQLException {
		operationCountService.updateStudentStatus();

	}

	/**
	 * 更新用户组织架构报表数据
	 */
	@Scheduled(cron="0 0 02 * * ?")
	@Override
	public void updateUserOrg() throws SQLException {
		operationCountService.updateUserOrg();
	}

	/**
	 * 更新一对多剩余资金报表数据
	 */
	@Scheduled(cron="0 05 02 * * ?")
	@Override
	public void updateOdsOtmRealStudentRemaining() throws SQLException {
		operationCountService.updateOdsOtmRealStudentRemaining(getYesterday());
	}

	/**
	 * 更新系统监控报表数据
	 */
	@Scheduled(cron="0 10 02 * * ?")
	@Override
	public void updateSystemMonitor() throws SQLException {
		operationCountService.updateSystemMonitor();
	}



	/**
	 * 更新一对一剩余资金报表数据
	 */
	@Scheduled(cron="0 40 02 * * ?")
	@Override
	public void updateOdsRealStudentRemaining() throws SQLException {
		operationCountService.updateOdsRealStudentRemaining(DateTools.getCurrentDate());
		try {
			Thread.currentThread().sleep(20000);//休息20秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		operationCountService.updateOdsRealStudentRemaining(getYesterday());
	}


	/**
	 * 更新小班剩余资金报表数据
	 */
	@Scheduled(cron="0 10 03 * * ?")
	@Override
	public void updateOdsDayMiniClassSurplusMoney() throws SQLException {
		operationCountService.updateOdsDayMiniClassSurplusMoney(DateTools.getCurrentDate());
		try {
			Thread.currentThread().sleep(20000);//休息20秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		operationCountService.updateOdsDayMiniClassSurplusMoney(getYesterday());
	}

	/**
	 * 更新小班人数报表数据
	 */
	@Scheduled(cron="0 20 03 * * ?")
	@Override
	public void updateOdsDayMiniClassStudentCount() throws SQLException {
		operationCountService.updateOdsDayMiniClassStudentCount(DateTools.getCurrentDate());
		try {
			Thread.currentThread().sleep(20000);//休息20秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		operationCountService.updateOdsDayMiniClassStudentCount(getYesterday());
	}

	/**
	 * 更新小班课消报表数据
	 */
	@Scheduled(cron="0 30 03 * * ?")
	@Override
	public void updateOdsDayMiniClassConsume() throws SQLException {
		operationCountService.updateOdsDayMiniClassConsume(DateTools.getCurrentDate());
		try {
			Thread.currentThread().sleep(20000);//休息20秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		operationCountService.updateOdsDayMiniClassConsume(getYesterday());
	}



	/**
	 * 更新老师未考勤课时报表数据
	 */
	@Scheduled(cron="0 0 04 * * ?")
	@Override
	public void updateTeacherNotAttendanceCourseHour() throws SQLException {
		operationCountService.updateTeacherNotAttendanceCourseHour();
	}

	/**
	 * 更新学管未绑定考勤编号的学生数量报表数据
	 */
	@Scheduled(cron="0 15 04 * * ?")
	@Override
	public void updateUnboundAttendanceNumber() throws SQLException {
		operationCountService.updateUnboundAttendanceNumber();
	}



	/**
	 * 小班报读人数统计
	 */
	@Scheduled(cron="0 25 04 * * ?")
	public void updateMiniClassStudentRealCount() throws SQLException {
		operationCountService.updateOdsDayMiniClassStudentRealCount(getYesterday());
	};

	/**
	 * 更新资金分配表统计， 每天分配到一对一 小班的具体的钱， 购买的课时
	 */
	@Scheduled(cron="0 30 04 * * ?")
	@Override
	public void updateMoneyArrangeRecord() throws SQLException {
		operationCountService.updateMoneyArrangeRecord(getYesterday());
	}
	protected String getYesterday() {
		return DateTools.addDateToString( DateTools.getCurrentDate(), -1);
	}



	/**
	 * 学生剩余资金
	 * @throws SQLException
	 */
	@Scheduled(cron="0 10 05 * * ?")
	@Override
	public void updateStudentSurplusFunding() throws SQLException {
		operationCountService.updateStudentSurplusFunding(getYesterday());
	}

	/**
	 * 更新一对一学生人数报表数据
	 */
	@Scheduled(cron="0 20 05 * * ?")
	@Override
	public void updateOdsRealStudentCount() throws SQLException {
		try {
			operationCountService.updateOdsRealStudentCount(DateTools.getCurrentDate());
			Thread.currentThread().sleep(20000);//休息20秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		operationCountService.updateOdsRealStudentCount(getYesterday());
	}

	/*
	 * 更新市场统计报表数据
	 */
	@Scheduled(cron="0 25 05 * * ?")
	@Override
	public void updateCustomerCusTypeTotal() throws SQLException {
		//每天晚上定时刷新30天前的数据一次
		operationCountService.updateOdsDayCustypeTotal(DateTools.getDateToString(DateTools.addDay(new Date(), -30)),DateTools.getCurrentDate());
	}

	/*
	 * 更新总览（排在营收现金流市场三个报表之后，顺序不要修改）
	 */
	@Scheduled(cron="0 40 05 * * ?")
	@Override
	public void updateAllTotal() throws SQLException {
		//每天晚上定时刷新30天前的数据一次
		operationCountService.updateAllTotal(DateTools.getDateToString(DateTools.addDay(new Date(), -30)),DateTools.getCurrentDate());
	}


	/**
	 * 每5分钟更新营收统计表当天的记录
	 * @throws SQLException
	 */
//	@Scheduled(cron="0 0/5 * * * ?")
	@Override
	public void updateOdsDayIncomingQuantityAnalyzeVery5Second() throws SQLException {
		//每五分钟刷新一次当天的市场统计数据
		operationCountService.updateOdsDayCustypeTotal(DateTools.getCurrentDate(),DateTools.getCurrentDate());//市场
		operationCountService.updateMobileOranization();

	}


	/**
	 * 停用组织架构职位同步OA，改为从人事同步   20180301   yyq
	 */
	//@Scheduled(cron="0 0/5 * * * ?")//_back_with_aplus_not_run_
	@Override
	public void sycOrganizationAndJob() {
		try {
			sycOrg();
			sycJob();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Scheduled(cron="0/1 * * * * ?")
	@Override
	public void excuteBonusQueueTask() throws Exception {
		QueueBonusHandler mt = new QueueBonusHandler();
		Thread t1 = new Thread(mt);
		t1.start();
	}

	@Scheduled(cron="0/1 * * * * ?")
	@Override
	public void excuteStuQueueTask() throws Exception {
		QueueStudentStatusHandler mt = new QueueStudentStatusHandler();
		Thread t1 = new Thread(mt);
		t1.start();
	}



	/**
	 * 更新学生一对一的状态（新签、在读、停课、结课、毕业、特殊停课）
	 */
	@Scheduled(cron="0 40 05 * * ?")
	@Override
	public void updateStudentOOOStatus() throws SQLException {
		operationCountService.updateStudentOOOStatus();
	}

	/**
	 * 更新一对一学生人数报表数据
	 */
	@Scheduled(cron="0 50 05 * * ?")
	@Override
	public void updateOdsRealStudentOOOCount() throws SQLException {
		operationCountService.updateOdsRealStudentOOOCount(getYesterday());
	}


	/**
	 * 更新学生一对多的状态（新签、在读、停课、结课、毕业、特殊停课）
	 */
	@Scheduled(cron="0 55 05 * * ?")
	@Override
	public void updateStudentOTMStatus() throws SQLException {
		operationCountService.updateStudentOTMStatus();
	}

	/**
	 * 更新一对多学生人数报表数据
	 */
	@Scheduled(cron="0 00 06 * * ?")
	@Override
	public void updateOdsRealStudentOTMCount() throws SQLException {
		operationCountService.updateOdsRealStudentOTMCount(getYesterday());
	}

	/**
	 * 每天回流周期内无跟进资源
	 */
	@Scheduled(cron="0 05 06 * * ?") //"0 05 06 * * ?"
	@Override
	public void resourceCallbackEveryday() throws SQLException{
		resourcePoolService.resourceCallback();

	}

	/**
	 * 客户资源导入从临时表到客户表
	 */
	@Scheduled(cron="0 10 06 * * ?")
	@Override
	public void importToCustomer() throws SQLException{
		customerService.importToCustomer();

	}

	/**
	 * 清理过季小班产品
	 */
	@Scheduled(cron="0 15 06 * * ?")
	@Override
	public void cleanSmallclassProduct() throws SQLException{
		CleanSmallclassHandler cl = new CleanSmallclassHandler();
		Thread t1 = new Thread(cl);
		t1.start();
	}

	/**
	 * 短信发送批处理失败信息
	 */
	@Scheduled(cron="0 55 05 * * ?")
	@Override
	public void sendFailureLogBySms() {
		SchedulerExecuteLog schedulerExecuteLog = new SchedulerExecuteLog();
		schedulerExecuteLog.setStartTime(DateTools.getCurrentDate());
		schedulerExecuteLog.setEndTime(DateTools.getCurrentDate());
		schedulerExecuteLog.setStatus(SchedulerExecuteStatus.FAILURE);
		DataPackage dp = new DataPackage(0, 999);
		dp = schedulerExecuteLogService.getSchedulerExecuteLogList(schedulerExecuteLog, dp);
		List<SchedulerExecuteLogVo> list = (List<SchedulerExecuteLogVo>) dp.getDatas();
		if (list != null && list.size() > 0) {
			String content="";
			for (SchedulerExecuteLogVo log: list) {
				content += log.getSchedulerName() + ",";
			}
			SystemConfig systemConfig = new SystemConfig();
			systemConfig.setTag("smsNumber");
			systemConfig = systemConfigService.getSystemPath(systemConfig);
				if (null != systemConfig && StringUtil.isNotBlank(systemConfig.getValue())) {
					if (systemConfig.getValue().indexOf(",") != -1) {
						String[] phones = systemConfig.getValue().split(",");
						for (String phone : phones) {
							AliyunSmsUtil.sendSms(PropertiesUtils.getStringValue("SIGN_NAME"),"SMS_118615019",phone,"{\"content\":\""+content+"\"}");
						}
					} else {
						AliyunSmsUtil.sendSms(PropertiesUtils.getStringValue("SIGN_NAME"),"SMS_118615019",systemConfig.getValue(),"{\"content\":\""+content+"\"}");
					}
				}
			//推送到消息队列
			MessageQueueUtils.postToMq(PropertiesUtils.getStringValue("mq.batch.key"),content);
		}
	}


	/**
	 * 学生科目状态
	 */
	@Scheduled(cron="0 20 06 * * ?")
	@Override
	public void flushStudentSubjectStatus() throws SQLException{
		operationCountService.flushStudentSubjectStatus(DateTools.getCurrentDate());
	}

	/**
	 * 每个月5号零点开始执行
	 */
	@Scheduled(cron="0 0 00 1 * ?")
	@Override
	public void autoCompleteEcsClass() throws SQLException{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat myFmt1=new SimpleDateFormat("yyyy-MM");

		cal.add(Calendar.MONTH,-1);
		String countYearMonth=myFmt1.format(cal.getTime());
		int year = Integer.valueOf(countYearMonth.split("-")[0]);//获取年份
		int month=Integer.valueOf(countYearMonth.split("-")[1]);//获取月份

		String lastDate=DateTools.getLastDayofMonth(year, month);

		operationCountService.autoCompleteEcsClass(year, month,countYearMonth,lastDate);
		promiseClassService.promiseClassAutoCount(year, month,countYearMonth,lastDate);

	}


	/**
	 * 获取需要统计的日期
	 * 前推一天，获取这个月1号到这天的所有日期
	 * @return
	 */
	private List<String> getDatesUtilYesterday(){
		List<String> dates = new ArrayList<String>();
		Date date = DateTools.addDay(new Date(),-1);
		String yesterday = DateTools.getDateToString(date);
		date.setDate(1);
		while(!yesterday.equals(DateTools.getDateToString(date))){
			dates.add(DateTools.getDateToString(date));
			date.setDate(date.getDate() + 1);
		}
		dates.add(yesterday);
		return dates;
	}

	/**
	 * 删除无用的文件
	 */
	@Scheduled(cron="0 0 06 * * ?")
	@Override
	public void deleteUploadFileFromAliyunOSS() {
		/**
		 * 不需要删除文件
		 */
//		uploadFileService.deleteUnUseFile();
	}

	/**
	 * 一对一月结报表
	 */
	@Scheduled(cron="0 0 01 1 * ?")
	@Override
	public void updateOdsDayMonthlyBalance() {
		Date date = DateTools.addDay(new Date(),-1);
		String yesterday = DateTools.getDateToString(date);
		Date lastDate = DateTools.getFirstDayOfMonth(yesterday);
		String lastMonthDay = DateTools.getDateToString(lastDate);
		try {
			operationCountService.updateOdsDayMonthlyBalance(yesterday, yesterday, lastMonthDay);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 1号跑凭证报表
	 */
	@Scheduled(cron="0 05 0 1 * ?")
	@Override
	public void updateMonthlyEvidence() {
		Date date = DateTools.addDay(new Date(),-1);
		String targeDate = DateTools.getDateToString(date);
		try {
			operationCountService.updateOdsIncomeMonthlyStudentCampus(targeDate, targeDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 2-8号  如果有未终审的校区，继续自动刷新
	 */
	@Scheduled(cron="0 05 0 2-8 * ?")
	@Override
	public void updatePaymentReceiptUntilLastAudit() {
	    
		Date date = DateTools.add(new Date(),Calendar.MONTH,-1);
		try {
			String targeDate = DateTools.dateConversString(date,"yyyy-MM");
			String lastMonthEndDate = DateTools.getDateToString(DateTools.getLastMonthEnd(DateTools.getCurrentDate()));

			//找出上个月还有没开始审核的数据，
//        	List<Map<String,String>> list = odsMonthPaymentRecieptService.findPaymentRecieptMainByMonth(targeDate,EvidenceAuditStatus.NOT_AUDIT.getValue());
//        	for (Map<String, String> map : list) {
			operationCountService.updateOdsPaymentReciept(targeDate);
//			}
			//校区业绩
			operationCountService.updateOdsCampusAchievementFlush(targeDate);
			//上月未审核的营收凭证
			List<Map<String,String>>  list2=odsMonthIncomeCampusService.findOdsMonthIncomeCampusPrintByCountDate(lastMonthEndDate, EvidenceAuditStatus.NOT_AUDIT.getValue());
			for (Map<String, String> map : list2) {
				operationCountService.updateOdsIncomeMonthlyStudentCampusByCampus(lastMonthEndDate, DateTools.getCurrentDate(), map.get("campusId"));
			}
			if((DateTools.dateConversString(new Date(),"yyyy-MM")+"-08").equals(DateTools.getCurrentDate())) { //最后一天跑剩余凭证
			    operationCountService.updateOdsRemainMonthlyStudentCampus(lastMonthEndDate, DateTools.getCurrentDate());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新当前版本
	 * @throws SQLException
	 */
	@Scheduled(cron="0 0 01 * * ?")
	public void updateTeacherVersion() throws SQLException {
		System.out.println("开始"+DateTools.getCurrentDateTime());
		operationCountService.updateTeacherVersion();
		operationCountService.updateAchievementTemplateVesion();
		System.out.println("结束"+DateTools.getCurrentDateTime());
	}



	/**
	 * 1号跑凭证报表
	 */
	@Scheduled(cron="0 30 0 1 * ?")
	@Override
	public void updateSubjectGroup() {
		String currentDate = DateTools.getCurrentDate();
		int currentVersion = Integer.parseInt(currentDate.substring(0, 4) + currentDate.substring(5,7));
		int preVersion = 0;
		int nextVersion = 0;
		try {
			preVersion = Integer.parseInt(DateTools.getLastMonth(currentDate).substring(0, 4)
					+ DateTools.getLastMonth(currentDate).substring(5,7));
			nextVersion = Integer.parseInt(DateTools.getNextMonth(currentDate).substring(0, 4)
					+ DateTools.getNextMonth(currentDate).substring(5,7));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			operationCountService.updateSubjectGroup(preVersion, currentVersion, nextVersion);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载并导入银联支付数据 不对接银联了
	 *//*
	@Scheduled(cron="59 59 11,17,23 * * ?")
	@Override
	public void downLoadAndImportPosData() {
		String currentDate = DateTools.getCurrentDate();
		try {
			String targetDate = DateTools.getDateToString(DateTools.getPre(currentDate));
			operationCountService.downLoadAndImportPosDataReview(targetDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 下载并导入银联支付数据
	 */
	@Scheduled(cron="00 30 11,17,23 * * ?")
	@Override
	public void downLoadAndImportPosDataKuaiQian() {
		String currentDate = DateTools.getCurrentDate();
		try {
			String targetDate = DateTools.getDateToString(DateTools.getPre(currentDate));
			operationCountService.downLoadAndImportPosDataKuaiQianReview(targetDate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static String sycOrg() throws ClientProtocolException, IOException{
		String url = PropertiesUtils.getStringValue("OA_HOST")+"/getNeedSycOrg";
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		setRequestHeader(getrequest);
		HttpResponse getResponse = client.execute(getrequest);
		LOGGER.info("同步组织架构开始");
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = "";
			try {
				str = EntityUtils.toString(getResponse.getEntity());
				LOGGER.info("需要同步的机构：" + str);
				JSONArray result=new JSONArray(str);
				for (int i = 0; i < result.length(); i++) {
					JSONObject valueInfo= result.getJSONObject(i);
					LOGGER.info("同步机构：" + valueInfo.toString());
					SycInfoThread thread= new SycInfoThread(valueInfo);
					thread.run();
				}

				System.out.println(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}





	public static String sycJob() throws ClientProtocolException, IOException{
		String url = PropertiesUtils.getStringValue("OA_HOST")+"/getNeedSycStation";
		HttpClient client = wrapHttpClient();
		HttpGet getrequest = new HttpGet(url);
		setRequestHeader(getrequest);
		HttpResponse getResponse = client.execute(getrequest);
		if (getResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String str = "";
			try {
				str = EntityUtils.toString(getResponse.getEntity());
				JSONArray result=new JSONArray(str);
				for (int i = 0; i < result.length(); i++) {
					JSONObject valueInfo= result.getJSONObject(i);
					SycJobThread thread= new SycJobThread(valueInfo);
					thread.run();
				}

				System.out.println(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private static HttpClient wrapHttpClient()
	{
		HttpClient client = HttpClientUtils.getHttpClient();
		client = WebClientDevWrapper.wrapClient(client);
		return client;
	}

	private static void setRequestHeader(HttpGet getrequest){
		String auth = PropertiesUtils.getStringValue("API_USER")+":" +  PropertiesUtils.getStringValue("API_PWD");
		byte[] encodedAuth = Base64.encode(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		getrequest.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		getrequest.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
	}


	public static void main(String[] args) throws ClientProtocolException, IOException {
		sycJob();
	}
	
	
	/**
	 * 从无效客户手里释放客户资源 目前只是释放  无效的咨询师  外呼主管 外呼专员  网络主管 网络专员
	 * 每天半夜3点执行 24小时制
	 * 校区营运主任咨询师账号注销后，他手上的资源自动释放到校区资源池；TMK主管账号注销，他手上的资源自动释放到TMK资源池；网络则回到网络资源池
	 * 备注:如果 所属的资源池找不到的话 不释放
	 */
	@Scheduled(cron="0 */1 * * * ?") //0/1 * * * * ? 半夜三点  0 */1 * * * ?一分钟一次
	@Override
	public void releaseCustomerByInvalidUser() {
		ResourcePool resourcePool = null;
		List<Customer> customers = null;	
	    int wlzy_size =0;
	    int wlzg_size =0;
	    int whzy_size =0;
	    int whzg_size =0;
	    int zxs_size  =0;
	    int scjl_size =0;
	    int xqyyzr_size = 0;
		Boolean isWLZY =false;
		Boolean isWLZG =false;
		Boolean isWHZY =false;
		Boolean isWHZG =false;
		Boolean isZXS =false;
		Boolean isSCJL =false;
		Boolean isXQYYZR =false;
		String remark ="注销用户并释放资源";
		//JedisUtil.rpoplpush("invalidUserInfo".getBytes(), "temp-invalidUserInfo".getBytes());
		byte[] object = JedisUtil.rpop("invalidUserInfo".getBytes());
		if (object != null) {
			Object value = JedisUtil.ByteToObject(object);
			User user = userService.loadUserById(value.toString());
			ResourcePool networkPool = resourcePoolService.getBelongBranchResourcePool("", OrganizationType.GROUNP.getValue());
			String userId = user.getUserId();
			List<Role> roles =userService.getRoleByUserId(userId);
			if(roles!=null && roles.size()>0){
				for(Role role:roles){	
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.NETWORK_SPEC)){
						wlzy_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.NETWORK_MANAGER)){
						wlzg_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.OUTCALL_SPEC)){
						whzy_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.OUTCALL_MANAGER ||role.getRoleCode()==RoleCode.BRANCH_OUTCALL_MANAGER)){
						whzg_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CONSULTOR ||role.getRoleCode()==RoleCode.CONSULTOR_DIRECTOR )){
						zxs_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.BREND_MERKETING_DIRECTOR)){
						scjl_size ++;
					}
					if(role.getRoleCode()!=null && (role.getRoleCode()==RoleCode.CAMPUS_OPERATION_DIRECTOR)){
						xqyyzr_size ++;
					}
				}
			}
			isWLZY =false;isWLZG =false;isWHZY =false;isWHZG =false;isZXS =false;isSCJL =false;isXQYYZR =false;
			if(wlzy_size>0){
				isWLZY =true;
			}
			if(wlzg_size>0){
				isWLZG =true;
			}
			if(whzy_size>0){
				isWHZY = true;
			}
			if(whzg_size>0){
				isWHZG = true;
			}
			if(zxs_size>0){
				isZXS = true;
			}
			if(scjl_size>0){
				isSCJL = true;
			}
			if(xqyyzr_size>0){
				isXQYYZR = true;
			}
		
			String roleSign = userService.getUserRoleSign(userId);			
			customers = customerService.getCustomersByUserId(userId, roleSign);	
			if(isWLZY||isWLZG){
				//网络主管 或者网络专员 释放客户到网络资源池
				resourcePool = networkPool;
				if(resourcePool!=null && customers!=null){
	                for(Customer customer:customers){
                         releaseCustomer(userId, customer, resourcePool,remark);
	                }					
				}				
			}
			if(isZXS||isXQYYZR){
				//咨询师释放到所属校区资源池
				resourcePool =resourcePoolService.getBelongBranchResourcePool(userId, OrganizationType.CAMPUS.getValue());
				if(resourcePool!=null && customers!=null){
	                for(Customer customer:customers){
                         releaseCustomer(userId, customer, resourcePool,remark);
	                }					
				}				
				
			}
			if(isWHZG||isWHZY){
				//外呼主管或者外呼专员  释放到所属部门的外呼资源池
				resourcePool =resourcePoolService.getBelongBranchResourcePool(userId, OrganizationType.DEPARTMENT.getValue());
				if(resourcePool!=null && customers!=null){
	                for(Customer customer:customers){
                         releaseCustomer(userId, customer, resourcePool,remark);
	                }					
				}							
			}
			if(isSCJL){
				//回到分公司资源池
				resourcePool =resourcePoolService.getBelongBranchResourcePool(userId, OrganizationType.BRENCH.getValue());
				if(resourcePool!=null && customers!=null){
	                for(Customer customer:customers){
                         releaseCustomer(userId, customer, resourcePool,remark);
	                }					
				}
			}
		}	
	}
	
	private void releaseCustomer(String userId,Customer customer,ResourcePool resourcePool,String remark){
    	//customer.setBeforeDeliverTarget(userId);
    	customer.setModifyTime(DateTools.getCurrentDateTime());
    	customer.setModifyUserId("system");
    	customer.setDeliverTarget(resourcePool.getOrganizationId());
		customer.setDeliverType(CustomerDeliverType.CUSTOMER_RESOURCE_POOL);
		customer.setDealStatus(CustomerDealStatus.STAY_FOLLOW);
		customer.setDeliverTime(DateTools.getCurrentDateTime());
		customerService.updateCustomer(customer);	
		//添加分配人变动记录表
		DeliverTargetChangeRecord record = new DeliverTargetChangeRecord();
		record.setCustomerId(customer.getId());
		record.setPreviousTarget(userId);
		record.setCurrentTarget(resourcePool.getOrganizationId());
		record.setRemark(remark);
		record.setCreateUserId("system");
		record.setCreateTime(DateTools.getCurrentDateTime());
		deliverTargetChangeService.saveDeliverTargetChangeRecord(record);	
	}
	
	@Scheduled(cron="0 */1 * * * ?") //0 0 6 * * ? 半夜三点  0 */1 * * * ?一分钟一次 0/1 * * * * ?
	@Override
	public void releaseCustomerByChangeCampus() {
		ResourcePool resourcePool = null;
		List<Customer> customers = null;
		String remark="用户更换校区释放客户资源";
		//JedisUtil.rpoplpush("invalidUserInfo".getBytes(), "temp-invalidUserInfo".getBytes());
		byte[] object = JedisUtil.rpop("changeCampusUserInfo".getBytes());
		if (object != null) {
			Object value = JedisUtil.ByteToObject(object);
			
			String[] strings =value.toString().split("&");
			String userId = strings[0];
			String orgId = strings[1];
			
			String roleSign = userService.getUserRoleSign(userId);			
			customers = customerService.getCustomersByUserId(userId, roleSign);	
			
			resourcePool = resourcePoolService.findResourcePoolById(orgId);
			if(resourcePool!=null && customers!=null){
                for(Customer customer:customers){
                     releaseCustomer(userId, customer, resourcePool,remark);
                }					
			}				
		}
	}

	@Scheduled(cron="0 30 06 * * ?")
	@Override
	public void updateCustomerActive() throws SQLException {
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{CALL proc_update_customerActive()}";
		procedureDao.executeProc(sql);
	}
	
	@Scheduled(cron="0 */10 * * * ?")
	@Override
	public void updateAttentanceInfo() {
		
		List<byte[]> objectList = JedisUtil.lpopList("attentanceInfo".getBytes());
		if(objectList!=null && !objectList.isEmpty()){
			LOGGER.info("attentanceInfo_queue_size:"+objectList.size());
			for(int i=0;i<objectList.size();i++){
				JedisUtil.rpop("attentanceInfo".getBytes());
			}
			final String url = PropertiesUtils.getStringValue("attentanceInfo.url");
			for(byte[] object:objectList){
				Object value = JedisUtil.ByteToObject(object);
				final List<AttentanceInfoVo> list = (List<AttentanceInfoVo>) value;
				if (list != null) {					
					final String postContent = Json.toJson(list);
					if (StringUtils.isNotBlank(url)) {
						cachedThreadPool.execute(new Runnable() {
							@Override
							public void run() {
								try {
									String response = HttpClientUtil.doPostJson(url, postContent);
									com.alibaba.fastjson.JSONObject object = JSON.parseObject(response);
									if (object != null) {
										if (!object.get("code").toString().equals("200")) {
											// 同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
											JedisUtil.lpush("attentanceInfo".getBytes(), JedisUtil.ObjectToByte(list));
										}
									}

								} catch (RuntimeException e) {
									// 异常,同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
									JedisUtil.lpush("attentanceInfo".getBytes(), JedisUtil.ObjectToByte(list));
								}

							}
						});
					}
				}

			}
		}
		
		
		byte[] object = JedisUtil.rpop("attentanceInfo".getBytes());
		if (object != null) {
			
		}

	}

	@Scheduled(cron = "0 */5 * * * ?")
	@Override
	public void updateTextBookBindingData() {
		List<byte[]> list = JedisUtil.lpopList("textBookBindingData".getBytes());
		if(list!=null && !list.isEmpty()){
			LOGGER.info("textBookBindingData_queue_size:"+list.size());
			for(int i=0;i<list.size();i++){
				JedisUtil.rpop("textBookBindingData".getBytes());
			}
			final String url = PropertiesUtils.getStringValue("textBookBindingDataUrl");
			for(byte[] object:list){
				Object value = JedisUtil.ByteToObject(object);
				if (value != null) {
					Map<String, String> params = (Map<String, String>) value;
					
					if (StringUtils.isNotBlank(url)) {
						cachedThreadPool.execute(new Runnable() {
							@Override
							public void run() {
								try {
									String response = HttpClientUtil.doGet(url, params);
									com.alibaba.fastjson.JSONObject object = JSON.parseObject(response);
									if (object != null) {
										if (!object.get("code").toString().equals("200")) {
											// 同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
											JedisUtil.lpush("textBookBindingData".getBytes(),
													JedisUtil.ObjectToByte(params));
										}
									}

								} catch (RuntimeException e) {
									// 异常,同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
									JedisUtil.lpush("textBookBindingData".getBytes(), JedisUtil.ObjectToByte(params));
								}

							}
						});

					}

				}
			}
		}

	}
	

	
	
	/**
	 * 每天更新客户资源的上门标识
	 */
	@Scheduled(cron="0 05 05 * * ?") //"0 05 06 * * ?"
	@Override
	public void updateCustomerVisitEveryday() throws SQLException{
		ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
		String sql = "{CALL proc_update_customerVisitCome()}";
		procedureDao.executeProc(sql);
	}
	
	
	@Scheduled(cron = "0 */3 * * * ?")
	@Override
	public void chargeNotice() {		
		List<byte[]> list = JedisUtil.lpopList("chargeInfo".getBytes());
		if(list!=null && !list.isEmpty()){
			LOGGER.info("chargeInfo_queue_size:"+list.size());
			for(int i=0;i<list.size();i++){
				JedisUtil.rpop("chargeInfo".getBytes());
			}
			final String chargeNoticeUrl = PropertiesUtils.getStringValue("chargeNoticeUrl");
			for(byte[] object:list){
				Object value = JedisUtil.ByteToObject(object);
				if (value != null) {
					Map<String, String> params = (Map<String, String>) value;					
					if (StringUtils.isNotBlank(chargeNoticeUrl)) {
						cachedThreadPool.execute(new Runnable() {
							@Override
							public void run() {
								try {
									String response = HttpClientUtil.doGet(chargeNoticeUrl, params);
									com.alibaba.fastjson.JSONObject object = JSON.parseObject(response);
									if (object != null) {
										if (!object.get("code").toString().equals("200")) {
											// 同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
											JedisUtil.lpush("chargeInfo".getBytes(),
													JedisUtil.ObjectToByte(params));
										}
									}

								} catch (RuntimeException e) {
									// 异常,同步失败把数据丢进队列里面 定时器把数据从队列里面取出来
									JedisUtil.lpush("chargeInfo".getBytes(), JedisUtil.ObjectToByte(params));
								}

							}
						});

					}

				}
			}
		}
	}

	@Scheduled(cron="0 */10 * * * ?")
	@Override
	public void saveLiveFinance() {
		
		List<byte[]> objectList = JedisUtil.lpopList("saveLiveFinance".getBytes());
		if(objectList!=null && !objectList.isEmpty()){
			LOGGER.info("saveLiveFinance_queue_size:"+objectList.size());
			for(int i=0;i<objectList.size();i++){
				JedisUtil.rpop("saveLiveFinance".getBytes());
			}
			for(byte[] object:objectList){
				Object value = JedisUtil.ByteToObject(object);
				Map<String, String> params = (Map<String, String>) value;
				final String url = params.get("callbackUrl");
				if (params.get("postContent") != null) {					
					final String postContent = params.get("postContent");
					if (StringUtils.isNotBlank(url)) {
						cachedThreadPool.execute(new Runnable() {
							@Override
							public void run() {
								try {
									LOGGER.info("zhibo_postContent_requestdata:"+postContent);
									String response = HttpClientUtil.doPostJson2(url, postContent,HttpClientUtil.getLiveDefaultHeaders());
									LOGGER.info("zhibo_pay_responsedata:"+response);
									//使用实时同步syncActualLiveChange
									/*if(response!=null) {
			 							com.alibaba.fastjson.JSONObject content =JSON.parseObject(postContent);
		 								LivePaymentRecordVo vo =JSON.parseObject(response, LivePaymentRecordVo.class);
		 								vo.setPaymentDate(params.get("paymentDate"));
		 								vo.setTotalAmount(new BigDecimal(params.get("amount")));
		 								livePaymentRecordService.saveNewLivePaymentRecordVo(vo,content.get("transactionNum").toString());
			 						}*/

								} catch (RuntimeException e) {
									LOGGER.error("直播付款成功："+e.getMessage());
									// 异常,失败把数据丢进队列里面 定时器把数据从队列里面取出来
									JedisUtil.lpush("saveLiveFinance".getBytes(), JedisUtil.ObjectToByte(params));
								}

							}
						});
					}
				}

			}
		}
	}
	
	@Scheduled(cron="* */1 * * * ?")
	@Override
	public void excuteTongLianPayStatusHandler() throws Exception {
		TongLianPayStatusHandler mt = new TongLianPayStatusHandler();
		Thread t1 = new Thread(mt);
		t1.start();
	}
	@Scheduled(cron="0/1 * * * * ?")
	@Override
	public void excuteFinanceQueueTask() throws Exception {
		QueueFinanceHandler mt = new QueueFinanceHandler();
		Thread t1 = new Thread(mt);
		t1.start();
	}
	
	@Scheduled(cron="0/1 * * * * ?")
	@Override
	public void excuteIncomeQueueTask() throws Exception {
		QueueIncomeHandler mt = new QueueIncomeHandler();
		Thread t1 = new Thread(mt);
		t1.start();
	}
}
