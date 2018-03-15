package com.eduboss.jedis;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import redis.clients.jedis.ShardedJedis;

import com.eduboss.utils.CommonUtil;
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
public class QueueFinanceHandler extends QueueHandler implements Runnable{
	
	private static byte[] errorRedisKey;
	private static byte[] redisKey;
	private static String STAND_BY="STANDBY_FINANCE";

	Logger log=Logger.getLogger(QueueFinanceHandler.class);
	
	{
		try {
			errorRedisKey=ObjectUtil.objectToBytes(CommonUtil.FINANCE_QUEUE_ERROR);
			redisKey = ObjectUtil.objectToBytes(CommonUtil.FINANCE_QUEUE);
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
	public void excuteQueue(FinanceMessage message){
		log.info(message.getUserId()+"现金流处理！");
		if(StringUtils.isNotBlank(message.getUserId()) && StringUtils.isNotBlank(message.getCountDate())){
			if (JedisUtil.get(STAND_BY+message.getUserId())!=null) {//连续的记录休息两秒
				log.info("现金流处理连续的记录休息两秒");
				try {
					Thread.sleep(2000, 1000);
				} catch (InterruptedException e) {
					log.error("现金流处理休眠失败！");
				}
			}
			JedisUtil.set(STAND_BY+message.getUserId(),message.getUserId(),10);
			financeWorkingService.saveFinanceByQueue(message);
		}else{
			log.error("现金流处理用户id为空:"+message.getUserId()+" 或者dateTime为空 :"+message.getCountDate());
		}
	}
	
	@Override
	public void run() {
		 byte[] bytes =null;
		try{
			bytes = JedisUtil.rpop(redisKey);
			while (bytes!=null) {
				Object object =ObjectUtil.bytesToObject(bytes);
				if(object instanceof FinanceMessage){
					FinanceMessage fmsg = (FinanceMessage) object;
			        excuteQueue(fmsg);
				}else{
					log.info("存入异常的对象！");
				}
		        bytes = JedisUtil.rpop(redisKey);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(bytes!=null){
				log.info("现金流存入异常！");
				JedisUtil.lpush(errorRedisKey,bytes);
			}
		}
		
	}
	
	
}
