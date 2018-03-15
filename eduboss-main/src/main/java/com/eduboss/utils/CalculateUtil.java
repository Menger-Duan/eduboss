package com.eduboss.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

import com.eduboss.common.ProductType;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.exception.ApplicationException;

/**
 * 主要是用于计费的时候的计算， 不会参与写Dao，但是会参与计算例如剩余课程，剩余资金，总价的计算。
 * @author robinzhang
 *
 */
public class CalculateUtil {
	
	/**
	 * 计算全款合同的
	 * @param contract
	 */
	public static void calFullContract(Contract contract) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		// 重新计算的时候需要对 下面值进行计算
		contract.setOneOnOneTotalAmount(BigDecimal.ZERO);
		contract.setMiniClassTotalAmount(BigDecimal.ZERO);
		contract.setOtherTotalAmount(BigDecimal.ZERO);
		
		for(ContractProduct conPro: contract.getContractProducts()) {
//			if(conPro.getProduct().getCategory() == ProductType.ONE_ON_ONE_COURSE && conPro.getProduct().getName().equals(OneOnOneProductCategory.DEFAULT.toString())  )	{
//				calDefaultOneOnOneCourse(conPro);
//				totalAmount =  totalAmount.add(conPro.getPlanAmount());
//			} else if(conPro.getProduct().getCategory() == ProductType.ONE_ON_ONE_COURSE && !conPro.getProduct().getName().equals(OneOnOneProductCategory.DEFAULT.toString())  )	{
//				// 这个是单独计算 不需要加入TotalAmount 里面
//				calOneOnOneCourse(conPro);
//			} 
			if(conPro.getProduct().getCategory() == ProductType.ONE_ON_ONE_COURSE)	{
				calOneOnOneCourse(conPro);
			} else if(conPro.getProduct().getCategory() == ProductType.SMALL_CLASS)	{
				calSmallCourse(conPro);
//				totalAmount =  totalAmount.add(conPro.getPlanAmount());
			} else if(conPro.getProduct().getCategory() == ProductType.OTHERS)	{
				calOther(conPro);
//				totalAmount =  totalAmount.add(conPro.getPlanAmount());
			}
			totalAmount =  totalAmount.add(conPro.getPlanAmount());
		}
		contract.setTotalAmount(totalAmount);
//		contract.setPendingAmount(totalAmount);
		contract.setPendingAmount(totalAmount.subtract(contract.getPaidAmount()));
	}
	
	/**
	 * 计算 Default 的 oneOnOne 收费
	 * @param conPro
	 */
	private static void calDefaultOneOnOneCourse(ContractProduct conPro) {
		if (conPro.getProduct() != null && conPro.getProduct().getPrice() != null) {
			conPro.setPlanAmount(conPro.getProduct().getPrice().multiply(conPro.getQuantity()));
			
			// 一对一项目收费 =  原有的收费 + 产品的收费
			BigDecimal money = conPro.getProduct().getPrice().multiply(conPro.getQuantity());
			conPro.setPlanAmount(money);
			// 设置单价
			conPro.setPrice(conPro.getProduct().getPrice());
			// 默认是没有折扣的
			conPro.setDealDiscount(BigDecimal.ONE);
//			conPro.setType(ContractProductType.ONE_ON_ONE_COURSE_DEFAULT);
			conPro.getContract().setOneOnOneTotalAmount(money.add(conPro.getContract().getOneOnOneTotalAmount()==null ?BigDecimal.ZERO:conPro.getContract().getOneOnOneTotalAmount()));
		}
	}

	/**
	 * 计算其他收费项目
	 * @param conPro
	 */
	private static void calOther(ContractProduct conPro) {
		// 加入单价
		conPro.setPrice(conPro.getPlanAmount());
		conPro.setQuantity(BigDecimal.ONE);
		// 默认是没有折扣的
		conPro.setDealDiscount(BigDecimal.ONE);
//		conPro.setType(ContractProductType.OTHERS);
		// 其他项目收费 =  原有的收费 + 产品的收费
		conPro.getContract().setOtherTotalAmount(conPro.getContract().getOtherTotalAmount().add(conPro.getPlanAmount()));
	}

	/**
	 * 计算小班的收费
	 * @param conPro
	 */
	private static void calSmallCourse(ContractProduct conPro) {
		// 可能要修改见面 把小班的添加上数量和折扣
		conPro.setPrice(conPro.getProduct().getPrice());
		
		// 默认是没有折扣的
		// 小班项目收费 =  原有的收费 + 产品的收费
		BigDecimal money = conPro.getProduct().getPrice().multiply(conPro.getDealDiscount().multiply(conPro.getQuantity()));
//		conPro.setType(ContractProductType.SMALL_CLASS);
		conPro.setPlanAmount(money);
		conPro.getContract().setMiniClassTotalAmount(money.add(conPro.getContract().getMiniClassTotalAmount()));
	}

	/**
	 * 计算一对一的收费   这些值是不需要计算进Total amount 里面的
	 * @param conPro
	 */
	public static void calOneOnOneCourse(ContractProduct conPro) {
		if (conPro.getProduct() != null && conPro.getProduct().getPrice() != null) {
			conPro.setPlanAmount(conPro.getProduct().getPrice().multiply(conPro.getQuantity()));
			
			// 设置单价
			conPro.setPrice(conPro.getProduct().getPrice());
			// 默认是没有折扣的
			conPro.setDealDiscount(BigDecimal.ONE);
			// 一对一项目收费 =  原有的收费 + 产品的收费
			BigDecimal money = conPro.getProduct().getPrice().multiply(conPro.getQuantity());
			conPro.setPlanAmount(money);
			//if(conPro.getPrice().compareTo(BigDecimal.ZERO) == 0) {
				//conPro.setType(ContractProductType.ONE_ON_ONE_COURSE_FREE_HOUR);
			//} else {
				//conPro.setType(ContractProductType.ONE_ON_ONE_COURSE);
			//}
			conPro.getContract().setOneOnOneTotalAmount(money.add(conPro.getContract().getOneOnOneTotalAmount()==null ?BigDecimal.ZERO:conPro.getContract().getOneOnOneTotalAmount()));
		}
	}
	
	public static void calPaidFundContract(Contract contract, Double transactionAmount){
		BigDecimal paidAmount =  contract.getPaidAmount();
		BigDecimal totalAmount =  contract.getTotalAmount();
		int flag = paidAmount.add(BigDecimal.valueOf(transactionAmount)).compareTo(totalAmount);
		// 付款数不能超过他的总钱数
		if(flag > 0 ) {
			throw new ApplicationException("支付金额过高");
		} else {
			// 利用了trigger 已经可以实现了 支付的累计
//			contract.setPaidAmount(paidAmount.add(BigDecimal.valueOf(fundsChangeHistory.getTransactionAmount())));
//			contract.setRemainingAmount(contract.getRemainingAmount().add(BigDecimal.valueOf(fundsChangeHistory.getTransactionAmount())));
//			contract.setPendingAmount(totalAmount.subtract(contract.getPaidAmount()));
//			if(flag == 0) {
//				contract.setPaidStatus(ContractPaidStatus.PAID);
//			} else {
//				contract.setPaidStatus(ContractPaidStatus.PAYING);
//			}
			
			
		}
	}
	
	/**
	 * 计算课时时长(返回的是分钟)
	 * @param courseTime 上课时间（一个区间）
	 * @param courseHours 现在的课时
	 * @return
	 */
	public static int calCourseTimeLong(String courseTime,BigDecimal courseHours){
		String [] times = courseTime.split(" - ");
		String [] d1Time = times[0].split(":");
		BigDecimal d1Hour2Minute = new BigDecimal(d1Time[0]).multiply(new BigDecimal(60));
		BigDecimal d1Minute = new BigDecimal(d1Time[1]);
		String [] d2Time = times[1].split(":");
		BigDecimal d2Hour2Minute = new BigDecimal(d2Time[0]).multiply(new BigDecimal(60));
		BigDecimal d2Minute = new BigDecimal(d2Time[1]);
		BigDecimal timeLong = d2Hour2Minute.add(d2Minute).subtract(d1Hour2Minute.add(d1Minute));
		return timeLong.divide(courseHours,0, RoundingMode.HALF_DOWN).intValue();
	}



	private static String content = "00:00-06:00这段时间不能上课，请修改开始上课时间或者课程时长，再重新排课！";

	public static void calCourseTimeBetweenUnExpectTime(String courseStartTime, BigDecimal courseTimeLong, double courseHours){

		//允许时间开始
		Calendar calendarStart = Calendar.getInstance();
		String ableStartTime = courseStartTime.substring(0, 10);
		Date ableStartDate = DateTools.getDateTime(ableStartTime + " 06:00");
		calendarStart.setTime(ableStartDate);
		long ableStartDateTimeLong = calendarStart.getTimeInMillis();

		Date dateStartTime = DateTools.getDateTime(courseStartTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateStartTime);
		long timeInMillis = calendar.getTimeInMillis();

		/**
		 * 时间早于允许排课的时间
		 */
		if (timeInMillis<ableStartDateTimeLong){
			throw new ApplicationException(content);
		}

		Calendar calendarEnd = Calendar.getInstance();
		Date ableEndDate = DateTools.getDateTime(DateTools.addDateToString(courseStartTime, 1)+" 00:00");
		calendarEnd.setTime(ableEndDate);
		long ableEndDateTimeLong = calendarEnd.getTimeInMillis();

		BigDecimal courseHoursB = BigDecimal.valueOf(courseHours);
		BigDecimal result = courseTimeLong.multiply(courseHoursB);

		double addTime = result.doubleValue();
		long t = (long) addTime*60*1000;
		long courseEndTime = timeInMillis+t;
		/**
		 * 时间晚于允许排课的时间
		 */
		if (ableEndDateTimeLong<courseEndTime){
			throw new ApplicationException(content);
		}


	}

	public static void main(String[] args) {
//		calCourseTimeBetweenUnExpectTime("2017-02-27 06:01", 60, 17.98);
	}

	
}
