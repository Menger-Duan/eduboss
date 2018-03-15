package com.eduboss.domainVo;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.eduboss.common.ChargePayType;
import com.eduboss.utils.DateTools;

public class AccountChargeRecordsVo implements java.io.Serializable {

	// Fields
	private String id;
	private String contractId;
	private String campusId;
	private String blCampusName;
	private BigDecimal amount;
	private BigDecimal quality;
	private String payChannel;
	private String payTime;
	private String subject;
	private String productName;
	private String operateUserName;
	private String studentName;
	private String remark;
	private String miniClassName;
	private String productTypeName;
	private String productTypeValue;
	private String courseTime;//上课时间
	private String courseDate;//上课时间
	private String teacherName;
	private String teacherId;
	private BigDecimal payHour;
	private String grade;
	private String studentId;
	private String contractProductId;
	private String chargeTypeName;
	private String chargeTypeValue;
	
	private String transactionId; 
	//资金来源（REAL：实收金额，PROMOTION：优惠金额）
	private String payTypeName; 
	private String payTypeValue; 
	private String transactionTime;
	
	private String otmClassName;
	
	private BigDecimal courseMinutes; // 扣费课程分钟数
	private String contractIdAccount;//学生电子账户查看扣费记录
	
	private ChargePayType chargePayType; // 扣费，冲销类型
	private String isWashed; // 是否发生过冲销  TRUE: 已冲销， FALSE: 没冲销

	//有2019及以后的目标班产品 （2019年后vip扣费记录不能手动冲销）
	private boolean hasEcsCPAfter2018;
	
	public String getContractIdAccount() {
		return contractIdAccount;
	}
	public void setContractIdAccount(String contractIdAccount) {
		this.contractIdAccount = contractIdAccount;
	}
	public String getId() {
		return id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPayChannel() {
		return payChannel;
	}
	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getOperateUserName() {
		return operateUserName;
	}
	public void setOperateUserName(String operateUserName) {
		this.operateUserName = operateUserName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMiniClassName() {
		return miniClassName;
	}
	public void setMiniClassName(String miniClassName) {
		this.miniClassName = miniClassName;
	}
	public String getProductTypeName() {
		return productTypeName;
	}
	public void setProductTypeName(String productTypeName) {
		this.productTypeName = productTypeName;
	}	
	public String getProductTypeValue() {
		return productTypeValue;
	}
	public void setProductTypeValue(String productTypeValue) {
		this.productTypeValue = productTypeValue;
	}
	public String getCourseTime() {
		return courseTime;
	}
	public void setCourseTime(String courseTime) {
		this.courseTime = courseTime;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public BigDecimal getPayHour() {
		return payHour;
	}
	public void setPayHour(BigDecimal payHour) {
		this.payHour = payHour;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getCourseDate() {
		return courseDate;
	}
	public void setCourseDate(String courseDate) {
		this.courseDate = courseDate;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public BigDecimal getQuality() {
		return quality;
	}
	public void setQuality(BigDecimal quality) {
		this.quality = quality;
	}
	// 获取到周一 和 周日日期的 字符串  用 “ ” 相连
//	public String getWeekStr() {
//		if(StringUtils.isNotEmpty(this.getCourseDate())){
//			StringBuffer sb = new StringBuffer();
//			Calendar cal =Calendar.getInstance();
//			
//			cal.setTime(DateTools.getDate(this.getCourseDate()));
//			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//			sb.append(DateTools.getDateToString(cal.getTime()));
//			cal.add(Calendar.WEEK_OF_YEAR, 1);
//			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//			sb.append(" 至 ").append(DateTools.getDateToString(cal.getTime()));
//			return sb.toString();
//		}else{
//			return "";
//		}
//	}
	
//	public String getWeekStr() {
//		if(StringUtils.isNotEmpty(this.getCourseDate())){
//			StringBuffer sb = new StringBuffer();
//			
//			 //传人参数当前月第一天
//			 Date firstDay=DateTools.getFirstDayOfMonth(this.getCourseDate());
//			//传人参数当前月最后一天
//			 Date lastDay=DateTools.getLastDayOfMonth(this.getCourseDate());
//			 
//			Calendar cal =Calendar.getInstance();
//			
//			cal.setTime(DateTools.getDate(this.getCourseDate()));
//			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//			
//			Date startDate=cal.getTime();			
//			if(startDate.getTime()<firstDay.getTime()){
//				startDate=firstDay;
//			}
//			
//			sb.append(DateTools.getDateToString(startDate));
//			cal.add(Calendar.WEEK_OF_YEAR, 1);
//			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
//			
//			Date endDate=cal.getTime();
//			if(endDate.getTime()>lastDay.getTime()){
//				endDate=lastDay;
//			}
//			
//			sb.append(" 至 ").append(DateTools.getDateToString(endDate));
//			return sb.toString();
//		}else{
//			return "";
//		}
//	}
	
	
	public String getWeekStr() {  
		if(StringUtils.isNotEmpty(this.getCourseDate())){		
			 //传人参数当前月第一天
			 Date firstDay=DateTools.getFirstDayOfMonth(this.getCourseDate());
			//传人参数当前月最后一天
			 Date lastDay=DateTools.getLastDayOfMonth(this.getCourseDate());
			 
			 StringBuffer sb = new StringBuffer();
			 Calendar cal = Calendar.getInstance();  
	         cal.setTime(DateTools.getDate(this.getCourseDate()));  
	         //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
	         int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
	         if(1 == dayWeek) {  
	             cal.add(Calendar.DAY_OF_MONTH, -1);  
	         }		        
	         cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
	         int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
	         cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   		         
	         Date startDate=cal.getTime();
			if(startDate.getTime()<firstDay.getTime()){
				startDate=firstDay;
			}				
			sb.append(DateTools.getDateToString(startDate));	        
	        cal.add(Calendar.DATE, 6);
	        Date endDate=cal.getTime();					 
			if(endDate.getTime()>lastDay.getTime()){
				endDate=lastDay;
			}			
			sb.append(" 至 ").append(DateTools.getDateToString(endDate));
			return sb.toString();				
	        
		}else{
			return "";
		}        
           
	}  
	
	
	public String getMonthStr() {
		if(StringUtils.isNotEmpty(this.getCourseDate())){
			StringBuffer sb = new StringBuffer();
			
			 //传人参数当前月第一天
			 Date firstDay=DateTools.getFirstDayOfMonth(this.getCourseDate());
			//传人参数当前月最后一天
			 Date lastDay=DateTools.getLastDayOfMonth(this.getCourseDate());		
			
			sb.append(DateTools.getDateToString(firstDay));
			sb.append(" 至 ").append(DateTools.getDateToString(lastDay));
			return sb.toString();
		}else{
			return "";
		}
	}
	
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getContractProductId() {
		return contractProductId;
	}
	public void setContractProductId(String contractProductId) {
		this.contractProductId = contractProductId;
	}
	public String getChargeTypeName() {
		return chargeTypeName;
	}
	public void setChargeTypeName(String chargeTypeName) {
		this.chargeTypeName = chargeTypeName;
	}
	public String getChargeTypeValue() {
		return chargeTypeValue;
	}
	public void setChargeTypeValue(String chargeTypeValue) {
		this.chargeTypeValue = chargeTypeValue;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getPayTypeValue() {
		return payTypeValue;
	}
	public void setPayTypeValue(String payTypeValue) {
		this.payTypeValue = payTypeValue;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public String getOtmClassName() {
		return otmClassName;
	}
	public void setOtmClassName(String otmClassName) {
		this.otmClassName = otmClassName;
	}
	public BigDecimal getCourseMinutes() {
		return courseMinutes;
	}
	public void setCourseMinutes(BigDecimal courseMinutes) {
		this.courseMinutes = courseMinutes;
	}
	public ChargePayType getChargePayType() {
		return chargePayType;
	}
	public void setChargePayType(ChargePayType chargePayType) {
		this.chargePayType = chargePayType;
	}
	public String getIsWashed() {
		return isWashed;
	}
	public void setIsWashed(String isWashed) {
		this.isWashed = isWashed;
	}

	public String getBlCampusName() {
		return blCampusName;
	}

	public void setBlCampusName(String blCampusName) {
		this.blCampusName = blCampusName;
	}

	public String getCampusId() {
		return campusId;
	}

	public void setCampusId(String campusId) {
		this.campusId = campusId;
	}

	public boolean isHasEcsCPAfter2018() {
		return hasEcsCPAfter2018;
	}

	public void setHasEcsCPAfter2018(boolean hasEcsCPAfter2018) {
		this.hasEcsCPAfter2018 = hasEcsCPAfter2018;
	}
}
