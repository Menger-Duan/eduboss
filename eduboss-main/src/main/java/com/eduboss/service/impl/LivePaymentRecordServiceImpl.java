package com.eduboss.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.eduboss.common.ChargePayType;
import com.eduboss.common.ChargeType;
import com.eduboss.common.LiveContactType;
import com.eduboss.common.LiveFinanceType;
import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.controller.DataTransaferController;
import com.eduboss.dao.LiveCourseDetailDao;
import com.eduboss.dao.LivePaymentRecordDao;
import com.eduboss.dao.LiveSyncSignDao;
import com.eduboss.domain.LiveCourseDetail;
import com.eduboss.domain.LivePaymentRecord;
import com.eduboss.domain.LiveSyncSign;
import com.eduboss.domain.LiveTransferPay;
import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.domainVo.LiveCourseResponseVo;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.jedis.FinanceMessage;
import com.eduboss.jedis.IncomeMessage;
import com.eduboss.jedis.Message;
import com.eduboss.service.LivePaymentRecordService;
import com.eduboss.service.LiveTransferPayService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.BonusQueueUtils;
import com.eduboss.utils.CommonUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;

@Service("LivePaymentRecordService")
public class LivePaymentRecordServiceImpl implements LivePaymentRecordService {

	private final static Logger logger = Logger.getLogger(DataTransaferController.class);
	@Autowired
	private LivePaymentRecordDao livePaymentRecordDao;
	 
	@Autowired
	private LiveTransferPayService liveTransferPayService;
	
	@Autowired
	private LiveCourseDetailDao liveCourseDetailDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LiveSyncSignDao liveSyncSignDao;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	@Override
	public LivePaymentRecord saveOrUpdateLivePaymentRecord(LivePaymentRecord livePaymentRecord) {
		// TODO Auto-generated method stub
		livePaymentRecordDao.save(livePaymentRecord);
        return livePaymentRecord;
	}



	@Override
	public LivePaymentRecord saveNewLivePaymentRecordVo(LivePaymentRecordVo vo,String transactionNum) {
		// TODO Auto-generated method stub
		LivePaymentRecord livePaymentRecord = HibernateUtils.voObjectMapping(vo, LivePaymentRecord.class);
		livePaymentRecord.setContactType(LiveContactType.NEW);
		livePaymentRecord.setFinanceType(LiveFinanceType.INCOME);
		livePaymentRecord.setPaidAmount(livePaymentRecord.getTotalAmount());
		livePaymentRecord.setCampusAchievement(livePaymentRecord.getTotalAmount());
		livePaymentRecord.setUserAchievement(livePaymentRecord.getTotalAmount());
		livePaymentRecordDao.save(livePaymentRecord);
		LiveTransferPay pay = liveTransferPayService.findByTransactionNum(transactionNum);
		if(pay!=null) {
			pay.setStatus("2");
			liveTransferPayService.saveOrUpdateLiveTransferPay(pay);
		}
		List<LiveCourseResponseVo> vos  =  vo.getCourseList();
		if(vos!=null&&vos.size()>0) {
			for(LiveCourseResponseVo coursevo : vos) {
				LiveCourseDetail courseDetail = HibernateUtils.voObjectMapping(coursevo, LiveCourseDetail.class);
				courseDetail.setPayRecordId(livePaymentRecord.getId());
				courseDetail.setPaidAmount(courseDetail.getPaidAmount().divide(BigDecimal.valueOf(100)));
				liveCourseDetailDao.save(courseDetail);
			}
		}
		return livePaymentRecord;
	}



	@Override
	public DataPackage getLivePaymentRecordList(DataPackage dataPackage, LivePaymentRecordVo livePaymentRecordVo,
			TimeVo timeVo) {
		// TODO Auto-generated method stub 
		Map<String, Object> params = new HashMap<>();
		long timeStart = System.currentTimeMillis();
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select r.id id,r.payment_date paymentDate,r.contact_type contactType,s.name studentName,s.id studentId ,s.contact studentContact  ");
		sql.append(" ,r.user_name userName,r.user_employeeNo userEmployeeNo,r.user_contact userContact,r.finance_type financeType,r.total_amount totalAmount,r.paid_amount paidAmount  ");
		sql.append(" ,r.campus_achievement campusAchievement,r.user_achievement userAchievement,r.order_num orderNum,o.name as orderCampusName  ");
		sql.append(" ,r.pay_type payType,r.reqsn ");
		sql.append(" from live_payment_record r left join organization o on r.order_campusId = o.id ");
		sql.append(" left join ( select distinct USER_ID,employee_No from user ) u on u.employee_No = r.user_employeeNo ");
		sql.append(" left join student s on r.student_id = s.ID ");
		sql.append(" where 1=1 ");
		/*//个人可见范围
		Organization organization = userService.getBelongCampus();
		if(organization!=null){
			sql.append(" and o.orgLevel LIKE '"+organization.getOrgLevel()+"%' ");
		}*/
		//开始结束日期
		if (StringUtils.isNotBlank(timeVo.getStartDate())) {
			sql.append(" and r.payment_date >= :startDate ");
			params.put("startDate", timeVo.getStartDate());
		}
		if (StringUtils.isNotBlank(timeVo.getEndDate())) {
			sql.append(" and r.payment_date < :endDate ");
			params.put("endDate", DateTools.addDateToString(timeVo.getEndDate(), 1));
		}
		//类型
		if (livePaymentRecordVo.getContactType()!=null) {
			sql.append(" and r.contact_type = :contactType");
			params.put("contactType", livePaymentRecordVo.getContactType().getValue());
		}
		//#733836
		//支付方式
		if(StringUtils.isNotBlank(livePaymentRecordVo.getPayType())) {
			sql.append(" and r.pay_type = :payType");
			params.put("payType", livePaymentRecordVo.getPayType());
		}
		//支付参考号
		if(StringUtils.isNotBlank(livePaymentRecordVo.getReqsn())) {
			sql.append(" and r.reqsn = :reqsn");
			params.put("reqsn", livePaymentRecordVo.getReqsn());
		}
		
		//学生信息
		if (StringUtils.isNotBlank(livePaymentRecordVo.getStudentName())) {
			sql.append(" and r.student_name like :studentName ");
			params.put("studentName", "%"+livePaymentRecordVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(livePaymentRecordVo.getStudentId())) {
			sql.append(" and r.student_id = :studentId ");
			params.put("studentId", livePaymentRecordVo.getStudentId());
		}
		if (StringUtils.isNotBlank(livePaymentRecordVo.getStudentContact())) {
			sql.append(" and r.student_contact like :studentContact ");
			params.put("studentContact", "%"+livePaymentRecordVo.getStudentContact()+"%");
		}
		//下单人信息
		if (StringUtils.isNotBlank(livePaymentRecordVo.getUserName())) {
			sql.append(" and r.user_name like :userName ");
			params.put("userName", "%"+livePaymentRecordVo.getUserName()+"%");
		}
		
		if (StringUtils.isNotBlank(livePaymentRecordVo.getUserEmployeeNo())) {
			sql.append(" and r.user_employeeNo = :userEmployeeNo ");
			params.put("userEmployeeNo", livePaymentRecordVo.getUserEmployeeNo());
		}
		
		if (StringUtils.isNotBlank(livePaymentRecordVo.getUserContact())) {
			sql.append(" and r.user_contact like :userContact ");
			params.put("userContact", "%"+livePaymentRecordVo.getUserContact()+"%");
		}
		//所属校区
		if (StringUtils.isNotBlank(livePaymentRecordVo.getOrderCampusId())) {
			sql.append(" and r.order_campusId = :orderCampusId ");
			params.put("orderCampusId", livePaymentRecordVo.getOrderCampusId());
		}
		
		if (livePaymentRecordVo.getFinanceType()!=null) {
			sql.append(" and r.finance_type = :financeType");
			params.put("financeType", livePaymentRecordVo.getFinanceType().getValue());
		} else {
			if("peiyou".equals(livePaymentRecordVo.getGroup())) {
				sql.append(" and r.finance_type = '" +LiveFinanceType.REVENUE.getValue()+"' ");
			}
		}
		
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("直播合作流水","sql","r.order_campusId"));
		
		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by "+dataPackage.getSidx()+" "+dataPackage.getSord());
		}else {
			sql.append(" order by  r.payment_date desc");
		}
		
		List<Map<Object, Object>> list = livePaymentRecordDao.findMapOfPageBySql(sql.toString(), dataPackage,params);
		dataPackage.setRowCount(livePaymentRecordDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params));

		List<LivePaymentRecordVo> vos = new ArrayList<>();
		LivePaymentRecordVo recordVo = null; 
		
		for(Map<Object, Object> tmaps: list) {
			Map<String, Object> maps =(Map)tmaps;
			recordVo = new LivePaymentRecordVo();
			recordVo.setId(maps.get("id")!=null?maps.get("id").toString():"");
			recordVo.setPaymentDate(maps.get("paymentDate")!=null?maps.get("paymentDate").toString():"");
			
			recordVo.setContactTypeName(LiveContactType.valueOf(maps.get("contactType").toString()).getName());
			recordVo.setFinanceTypeName(LiveFinanceType.valueOf(maps.get("financeType").toString()).getName());
			
			recordVo.setStudentName(maps.get("studentName")!=null?maps.get("studentName").toString():"");
			recordVo.setStudentId(maps.get("studentId")!=null?maps.get("studentId").toString():"");
			recordVo.setStudentContact(maps.get("studentContact")!=null?maps.get("studentContact").toString():"");
			
			recordVo.setUserName(maps.get("userName")!=null?maps.get("userName").toString():"");
			recordVo.setUserEmployeeNo(maps.get("userEmployeeNo")!=null?maps.get("userEmployeeNo").toString():"");
			recordVo.setUserContact(maps.get("userContact")!=null?maps.get("userContact").toString():"");
			
			recordVo.setTotalAmount((BigDecimal) maps.get("totalAmount"));
			recordVo.setPaidAmount((BigDecimal) maps.get("paidAmount"));
			recordVo.setCampusAchievement((BigDecimal) maps.get("campusAchievement"));
			recordVo.setUserAchievement((BigDecimal) maps.get("userAchievement"));
			
			recordVo.setOrderNum(maps.get("orderNum")!=null?maps.get("orderNum").toString():"");
			recordVo.setOrderCampusName(maps.get("orderCampusName")!=null?maps.get("orderCampusName").toString():"");
			
			String payType = maps.get("payType")!=null?maps.get("payType").toString():"";
			if("A01".equals(payType)) {
				recordVo.setPayType("支付宝");
			}else if("W01".equals(payType)){
				recordVo.setPayType("微信");
			}
			recordVo.setReqsn(maps.get("reqsn")!=null?maps.get("reqsn").toString():"");
			
			List<LiveCourseDetail>  courseDetails = liveCourseDetailDao.findListByPayRecordId(recordVo.getId());
			recordVo.setCourseDetails(courseDetails);
			if(courseDetails!=null&&courseDetails.size()>0) {
				recordVo.setCourseName(courseDetails.get(0).getCourseName());
			}
			vos.add(recordVo);
		}
		
		
		dataPackage.setDatas(vos);
		long timeEnd = System.currentTimeMillis();
		System.out.println("getLivePaymentRecordList-costtime:"+(timeEnd-timeStart)+"ms");
		return dataPackage;
	}



	@Override
	public List getLivePaymentRecordToExcel(LivePaymentRecordVo livePaymentRecordVo, TimeVo timeVo) {
		// TODO Auto-generated method stub 
		Map<String, Object> params = new HashMap<>();
		long timeStart = System.currentTimeMillis();
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select r.id id,r.payment_date paymentDate,r.contact_type contactType,s.name studentName,s.id studentId ,s.contact studentContact   ");
		sql.append(" ,r.user_name userName,r.user_employeeNo userEmployeeNo,r.user_contact userContact,r.finance_type financeType,r.total_amount totalAmount,r.paid_amount paidAmount  ");
		sql.append(" ,r.campus_achievement campusAchievement,r.user_achievement userAchievement,r.order_num orderNum,o.name orderCampusName,d.course_name courseName,d.paid_amount coursePaidAmount ");
		sql.append(" ,r.pay_type payType,r.reqsn  ");
		sql.append(" from live_payment_record r left join organization o on r.order_campusId = o.id ");
		sql.append(" left join live_course_detail d on d.pay_record_id = r.id ");
		sql.append(" left join ( select distinct USER_ID,employee_No from user ) u on u.employee_No = r.user_employeeNo ");
		sql.append(" left join student s on r.student_id = s.ID ");
		sql.append(" where 1=1 ");
		/*//个人可见范围
		Organization organization = userService.getBelongCampus();
		if(organization!=null){
			sql.append(" and o.orgLevel LIKE '"+organization.getOrgLevel()+"%' ");
		}*/
		//开始结束日期
		if (StringUtils.isNotBlank(timeVo.getStartDate())) {
			sql.append(" and r.payment_date >= :startDate ");
			params.put("startDate", timeVo.getStartDate());
		}
		if (StringUtils.isNotBlank(timeVo.getEndDate())) {
			sql.append(" and r.payment_date < :endDate ");
			params.put("endDate", DateTools.addDateToString(timeVo.getEndDate(), 1));
		}
		//类型
		if (livePaymentRecordVo.getContactType()!=null) {
			sql.append(" and r.contact_type = :contactType");
			params.put("contactType", livePaymentRecordVo.getContactType().getValue());
		}
		
		//#733836
		//支付方式
		if(StringUtils.isNotBlank(livePaymentRecordVo.getPayType())) {
			sql.append(" and r.pay_type = :payType");
			params.put("payType", livePaymentRecordVo.getPayType());
		}
		//支付参考号
		if(StringUtils.isNotBlank(livePaymentRecordVo.getReqsn())) {
			sql.append(" and r.reqsn = :reqsn");
			params.put("reqsn", livePaymentRecordVo.getReqsn());
		}
		
		//学生信息
		if (StringUtils.isNotBlank(livePaymentRecordVo.getStudentName())) {
			sql.append(" and r.student_name like :studentName ");
			params.put("studentName", "%"+livePaymentRecordVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(livePaymentRecordVo.getStudentId())) {
			sql.append(" and r.student_id = :studentId ");
			params.put("studentId", livePaymentRecordVo.getStudentId());
		}
		if (StringUtils.isNotBlank(livePaymentRecordVo.getStudentContact())) {
			sql.append(" and r.student_contact like :studentContact ");
			params.put("studentContact", "%"+livePaymentRecordVo.getStudentContact()+"%");
		}
		//下单人信息
		if (StringUtils.isNotBlank(livePaymentRecordVo.getUserName())) {
			sql.append(" and r.user_name like :userName ");
			params.put("userName", "%"+livePaymentRecordVo.getUserName()+"%");
		}
		
		if (StringUtils.isNotBlank(livePaymentRecordVo.getUserEmployeeNo())) {
			sql.append(" and r.user_employeeNo = :userEmployeeNo ");
			params.put("userEmployeeNo", livePaymentRecordVo.getUserEmployeeNo());
		}
		
		if (StringUtils.isNotBlank(livePaymentRecordVo.getUserContact())) {
			sql.append(" and r.user_contact like :userContact ");
			params.put("userContact", "%"+livePaymentRecordVo.getUserContact()+"%");
		}
		//所属校区
		if (StringUtils.isNotBlank(livePaymentRecordVo.getOrderCampusId())) {
			sql.append(" and r.order_campusId = :orderCampusId ");
			params.put("orderCampusId", livePaymentRecordVo.getOrderCampusId());
		}
		
		if (livePaymentRecordVo.getFinanceType()!=null) {
			sql.append(" and r.finance_type = :financeType");
			params.put("financeType", livePaymentRecordVo.getFinanceType().getValue());
		} else {
			if("peiyou".equals(livePaymentRecordVo.getGroup())) {
				sql.append(" and r.finance_type = '" +LiveFinanceType.REVENUE.getValue()+"' ");
			}
		}
		
		sql.append(roleQLConfigService.getValueResult("直播合作流水","sql"));
		
		sql.append(" order by  r.payment_date desc,r.id ");
		
		List<Map<Object, Object>> list = livePaymentRecordDao.findMapBySql(sql.toString(), params);
		
		
		if(list!=null&& list.size()>0) {
			for(Map<Object, Object> tmaps: list) {
				tmaps.put("contactTypeName", LiveContactType.valueOf(tmaps.get("contactType").toString()).getName());
				tmaps.put("financeTypeName", LiveFinanceType.valueOf(tmaps.get("financeType").toString()).getName());
				
				String payType = tmaps.get("payType")!=null?tmaps.get("payType").toString():"";
				if("A01".equals(payType)) {
					payType = "支付宝";
				}else if("W01".equals(payType)){
					payType = "微信";
				}
				tmaps.put("payType", payType);
			}
		}
		long timeEnd = System.currentTimeMillis();
		System.out.println("getLivePaymentRecordToExcel-costtime:"+(timeEnd-timeStart)+"ms");
		return list;
	}



	@Override
	public void saveLivePaymentRecordVo(LivePaymentRecordVo vo) {
		// TODO Auto-generated method stub 
		String uniqueId  = vo.getUniqueId();
		boolean flag = true;
		LivePaymentRecord livePaymentRecord = HibernateUtils.voObjectMapping(vo, LivePaymentRecord.class);
		livePaymentRecord.setTotalAmount(livePaymentRecord.getTotalAmount().divide(BigDecimal.valueOf(100)));
		livePaymentRecord.setPaidAmount(livePaymentRecord.getPaidAmount().divide(BigDecimal.valueOf(100)));
		livePaymentRecord.setCampusAchievement(livePaymentRecord.getCampusAchievement().divide(BigDecimal.valueOf(100)));
		livePaymentRecord.setUserAchievement(livePaymentRecord.getUserAchievement().divide(BigDecimal.valueOf(100)));
		livePaymentRecord.setOriginalPrice(livePaymentRecord.getOriginalPrice().divide(BigDecimal.valueOf(100)));
		if(StringUtils.isNotBlank(uniqueId)) {
			LiveSyncSign liveSyncSign = liveSyncSignDao.findById(uniqueId);
			if(liveSyncSign!=null && StringUtils.isNotBlank(liveSyncSign.getRecordId())) {
				liveCourseDetailDao.deleteByRecordId(liveSyncSign.getRecordId());
				LivePaymentRecord dataRecord = livePaymentRecordDao.findById(liveSyncSign.getRecordId());
				if(dataRecord!=null) {
					livePaymentRecord.setId(dataRecord.getId());
					livePaymentRecordDao.merge(livePaymentRecord);
					flag = false;
				}
			}
			if(flag) {
				livePaymentRecordDao.save(livePaymentRecord);
				LiveSyncSign lss = new LiveSyncSign();
				lss.setId(uniqueId);
				lss.setRecordId(livePaymentRecord.getId());
				liveSyncSignDao.save(lss);
				
				//十二月及以后重新跑
				String time = "2017-12-01";
				if(livePaymentRecord.getPaymentDate()!=null && time.compareTo(livePaymentRecord.getPaymentDate())<=0) {
					if(LiveFinanceType.INCOME == livePaymentRecord.getFinanceType()
							||LiveFinanceType.REFUND == livePaymentRecord.getFinanceType()) {
						//直播业绩
						addLiveBonusToMessage(livePaymentRecord);
						if(LiveFinanceType.INCOME == livePaymentRecord.getFinanceType() 
								&& LiveContactType.NEW == livePaymentRecord.getContactType()) {
							//直播现金流
							addLiveFinanceToFinanceMessage(livePaymentRecord);
						}
					}else if(LiveFinanceType.REVENUE == livePaymentRecord.getFinanceType()) {
						//app营收
						addLiveIncomeToIncomeMessage(livePaymentRecord);
					}
				}
			}
			List<LiveCourseResponseVo> vos  =  vo.getCourseList();
			if(vos!=null&&vos.size()>0) {
				for(LiveCourseResponseVo coursevo : vos) {
					LiveCourseDetail courseDetail = HibernateUtils.voObjectMapping(coursevo, LiveCourseDetail.class);
					courseDetail.setPayRecordId(livePaymentRecord.getId());
					courseDetail.setPaidAmount(courseDetail.getPaidAmount().divide(BigDecimal.valueOf(100)));
					courseDetail.setOriginalPrice(courseDetail.getOriginalPrice().divide(BigDecimal.valueOf(100)));
					liveCourseDetailDao.save(courseDetail);
				}
			}
		}
	}



	@Override
	public void saveMonthLiveChangeList(List<LivePaymentRecordVo> vos) {
		// TODO Auto-generated method stub 
		
		for(LivePaymentRecordVo vo :vos) {
			logger.info("直播营收同步参数："+vo.toString());
			this.saveLivePaymentRecordVo(vo);
		}
		
	}

	public void addLiveIncomeToIncomeMessage(LivePaymentRecord livePaymentRecord) {
		logger.info("直播营收addLiveIncomeToIncomeMessage:"+livePaymentRecord.toString());
		IncomeMessage message=new IncomeMessage();
		message.setCampusId(livePaymentRecord.getOrderCampusId());
		message.setAmount(livePaymentRecord.getPaidAmount());
		message.setQuantity(new BigDecimal(1));
		message.setType(ProductType.LIVE);
		message.setChargePayType(ChargePayType.CHARGE);
		message.setChargeType(ChargeType.NORMAL);
		message.setPayType(PayType.REAL);
		message.setCountDate(livePaymentRecord.getPaymentDate());
	    try {
			JedisUtil.lpush(
			        ObjectUtil.objectToBytes("incomeQueue"),
			        ObjectUtil.objectToBytes(message));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("直播营收addLiveIncomeToIncomeMessage:"+livePaymentRecord.getId());
			//e.printStackTrace();
		}
	}
	
	public void addLiveFinanceToFinanceMessage(LivePaymentRecord livePaymentRecord) {
		logger.info("直播现金流addLiveFinanceToFinanceMessage:"+livePaymentRecord.toString());
		FinanceMessage message= new FinanceMessage();
		message.setCountDate(livePaymentRecord.getPaymentDate());
		User u = userService.findUserByEmployeeNo(livePaymentRecord.getUserEmployeeNo());
		if(u!=null) {
			message.setUserId(u.getUserId());
		    message.setUserName(u.getName());
		    message.setCampusId(livePaymentRecord.getOrderCampusId());
		    message.setNewMoney(livePaymentRecord.getPaidAmount());
		    message.setTotalMoney(livePaymentRecord.getPaidAmount());
		    message.setFlag("1");
		    message.setOnline(true);
		    try {
				JedisUtil.lpush(
				        ObjectUtil.objectToBytes(CommonUtil.FINANCE_QUEUE),
				        ObjectUtil.objectToBytes(message));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("直播现金流addLiveFinanceToFinanceMessage:"+livePaymentRecord.getId());
				//e.printStackTrace();
			}
		}else {
			logger.info("直播现金流工号user不存在"+livePaymentRecord.getId());
		}
	    
	}
	
	public void addLiveBonusToMessage(LivePaymentRecord livePaymentRecord) {
		logger.info("直播业绩addLiveBonusToMessage:"+livePaymentRecord.toString());
		Message message=new Message();
		message.setDateTime(livePaymentRecord.getPaymentDate());
		User u = userService.findUserByEmployeeNo(livePaymentRecord.getUserEmployeeNo());
		if(u!=null) {
			message.setUserId(u.getUserId());
		    message.setCampusId(livePaymentRecord.getOrderCampusId());
		    message.setMoney(livePaymentRecord.getUserAchievement());
		    if(LiveFinanceType.INCOME == livePaymentRecord.getFinanceType()) {
		    	message.setFlag("1");
		    }else if(LiveFinanceType.REFUND == livePaymentRecord.getFinanceType()){
		    	message.setFlag("0");
		    }
		    message.setOnline(true);
		    try {
				JedisUtil.lpush(
				        ObjectUtil.objectToBytes("bonusQueue"),
				        ObjectUtil.objectToBytes(message));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("直播业绩加入队列失败addLiveBonusToMessage:"+livePaymentRecord.getId());
				//e.printStackTrace();
			}
		}else {
			logger.info("直播业绩工号user不存在"+livePaymentRecord.getId());
		}
	    
	}



}
