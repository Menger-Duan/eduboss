package com.eduboss.jedis;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;

/** 
 * 
 * @author sniper
 *
 */
public class QueueIncomeHandler extends QueueHandler implements Runnable{
	
	private static byte[] errorRedisKey;
	private static byte[] redisKey;
	private static String STAND_BY="STANDBY_INCOME";

	Logger log=Logger.getLogger(QueueIncomeHandler.class);
	
	{
		try {
			errorRedisKey=ObjectUtil.objectToBytes("incomeQueueError");
			redisKey = ObjectUtil.objectToBytes("incomeQueue");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 处理营收
	 * @author: duanmenrun
	 * @Title: excuteQueue 
	 * @Description: TODO 
	 * @param message
	 */
	public void excuteQueue(IncomeMessage message){
		log.info(message.getCampusId()+"营收处理！");
		if(StringUtils.isNotBlank(message.getCountDate())){
			if(message.getCountDate().length()>10){
				message.setCountDate(message.getCountDate().substring(0,10));
			}
			if (JedisUtil.get(STAND_BY+message.getCampusId())!=null) {//连续的记录休息两秒
				log.info("营收处理连续的记录休息四秒");
				try {
					Thread.sleep(4000, 1000);
				} catch (InterruptedException e) {
					log.error("营收处理休眠失败！");
				}
			}
			JedisUtil.set(STAND_BY+message.getCampusId(),message.getCampusId(),10);

			incomeWorkingService.saveIncomeByQueue(message);
			log.info(message.getCampusId()+"营收处理结束！");
		}
	}
	
	@Override
	public void run() {
		 byte[] bytes =null;
		try{
			bytes = JedisUtil.rpop(redisKey);
			while (bytes!=null) {
				Object object =ObjectUtil.bytesToObject(bytes);
				if(object instanceof IncomeMessage){
					IncomeMessage fmsg = (IncomeMessage) object;
			        excuteQueue(fmsg);
				}else{
					log.info("存入异常的对象！");
				}
		        bytes = JedisUtil.rpop(redisKey);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(bytes!=null){
				log.info("营收存入异常！");
				JedisUtil.lpush(errorRedisKey,bytes);
			}
		}
		
	}
	
	
}
