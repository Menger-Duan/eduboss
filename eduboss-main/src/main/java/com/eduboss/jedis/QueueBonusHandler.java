package com.eduboss.jedis;

import com.eduboss.utils.JedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;

import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;

/** 
 * @author  author :Yao 
 * @date  2016年7月5日 下午4:25:17 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class QueueBonusHandler extends QueueHandler implements Runnable{
	
	private byte[] errorRedisKey;
	private byte[] redisKey;
	private static String STAND_BY="STANDBY";

	Logger log=Logger.getLogger(QueueBonusHandler.class);
	
	
	{
		 try {
			errorRedisKey=ObjectUtil.objectToBytes("bonusQueueError");
			redisKey = ObjectUtil.objectToBytes("bonusQueue");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
	public void excuteQueue(Message message, byte[] bytes){
		log.info(message.getUserId()+"业绩处理！");
		if(StringUtils.isNotBlank(message.getUserId()) && StringUtils.isNotBlank(message.getDateTime())){
			if (JedisUtil.get(STAND_BY+message.getUserId())!=null) {//连续的记录休息两秒
				log.info("业绩处理连续的记录休息两秒");
				try {
					Thread.sleep(2000, 1000);
				} catch (InterruptedException e) {
					log.error("业绩处理休眠失败！");
				}
			}
			JedisUtil.set(STAND_BY+message.getUserId(),message.getUserId(),10);
			staffBonusDayService.saveOrUpdateStaffBonus(message);
			log.info(message.getUserId()+"业绩处理结束！");
		}else{
			log.error("业绩处理用户id为空:"+message.getUserId()+" 或者dateTime为空 :"+message.getDateTime());
		}
	}
	
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
	public void excuteQueue(FinanceMessage message){
		log.info(message.getUserId()+"现金流处理！");
		if(StringUtils.isNotBlank(message.getCountDate())){
			financeWorkingService.saveFinanceByQueue(message);
			log.info(message.getUserId()+"现金流处理结束！");
		}
	}
	
	
	public void excuteQueue(IncomeMessage message){
		log.info(message.getCampusId()+"营收处理！");
		if(StringUtils.isNotBlank(message.getCountDate())){
			if(message.getCountDate().length()>10){
				message.setCountDate(message.getCountDate().substring(0,10));
			}
			incomeWorkingService.saveIncomeByQueue(message);
			log.info(message.getCampusId()+"营收处理结束！");
		}
	}
	
	

	@Override
	public void run() {
		 byte[] bytes =null;
		 
		try{
			bytes = JedisUtil.rpop(redisKey);
			if (bytes!=null) {
				Object object =ObjectUtil.bytesToObject(bytes);
				if(object instanceof Message){
					Message msg = (Message) object;
			        excuteQueue(msg,bytes);
				}/*else if(object instanceof FinanceMessage){
					FinanceMessage fmsg = (FinanceMessage) object;
			        excuteQueue(fmsg);
				}else if (object instanceof IncomeMessage){
					IncomeMessage fmsg = (IncomeMessage) object;
			        excuteQueue(fmsg);
				}*/else{
					log.info("存入异常的对象！");
				}
//		        bytes = JedisUtil.rpop(redisKey);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(bytes!=null){
				log.info("业绩存入异常！"+e.getMessage());
				JedisUtil.lpush(errorRedisKey,bytes);
			}
			
		}
		
	}
	
	
}
