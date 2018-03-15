package com.eduboss.jedis;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.eduboss.domainVo.HandleTongLianPayVo;
import com.eduboss.service.LiveTransferPayService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.MessageQueueUtils;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.PropertiesUtils;

public class TongLianPayStatusHandler implements Runnable {

	protected static LiveTransferPayService liveTransferPayService =  ApplicationContextUtil.getContext().getBean(LiveTransferPayService.class);
	
		private byte[] errorPayMessageKey;
		private byte[] payMessageKey;
		
		Logger log=Logger.getLogger(TongLianPayStatusHandler.class);
		
		private static final long DELAY_SECOND = 60;//单位秒
		
		{
			 try {
				 errorPayMessageKey=ObjectUtil.objectToBytes("errorPayMessageKey");
				 payMessageKey = ObjectUtil.objectToBytes("payMessageKey");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 向消息中心推送消息
		 * @author: duanmenrun
		 * @Title: excuteQueue 
		 * @Description: TODO 
		 * @param handleTongLianPayVo
		 * @throws SQLException
		 */
		public void excuteQueue(HandleTongLianPayVo handleTongLianPayVo) throws SQLException{
			
			String data = JSON.toJSONString(handleTongLianPayVo);
			MessageQueueUtils.postToMq(PropertiesUtils.getStringValue("mq.access.TL_callback_handle"), DELAY_SECOND, data);
		}
		
		

		@Override
		public void run() {
			 byte[] bytes =null;
			 
				try{
					bytes = JedisUtil.rpop(payMessageKey);
					if (bytes!=null) {
						HandleTongLianPayVo handleTongLianPayVo =(HandleTongLianPayVo) ObjectUtil.bytesToObject(bytes);
						excuteQueue(handleTongLianPayVo);
					}
				}catch(Exception e){
					e.printStackTrace();
					if(bytes!=null){
						log.info("存入异常！"+e.getMessage());
						JedisUtil.lpush(errorPayMessageKey,bytes);
					}
					
				}
		}
}

