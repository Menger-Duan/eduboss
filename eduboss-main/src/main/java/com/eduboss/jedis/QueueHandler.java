package com.eduboss.jedis;

import org.apache.log4j.Logger;

import com.eduboss.service.FinanceWorkingService;
import com.eduboss.service.IncomeWorkingService;
import com.eduboss.service.StaffBonusDayService;
import com.eduboss.utils.ApplicationContextUtil;

/** 
 * @author  author :Yao 
 * @date  2016年7月5日 下午4:25:17 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class QueueHandler {
	protected static StaffBonusDayService staffBonusDayService =  ApplicationContextUtil.getContext().getBean(StaffBonusDayService.class);
	protected static FinanceWorkingService financeWorkingService =  ApplicationContextUtil.getContext().getBean(FinanceWorkingService.class);
	protected static IncomeWorkingService incomeWorkingService =  ApplicationContextUtil.getContext().getBean(IncomeWorkingService.class);
	
	
	
	Logger log=Logger.getLogger(QueueHandler.class);
	
	
	/** 
	 * 处理序列中的逻辑
	* @param message
	* @author  author :Yao 
	* @date  2016年7月5日 下午4:27:26 
	* @version 1.0 
	* @parameter  
	* @since  
	* @return  
	*/
	public void excuteQueue(Message message){
		log.info(message.getName());
	}
}
