package com.eduboss.jedis;

import com.eduboss.service.StudentService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import org.apache.log4j.Logger;

/** 
 * @author  author :Yao 
 * @date  2017年2月28日 下午4:25:17
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public class QueueStudentStatusHandler implements Runnable{
	protected static StudentService studentService =  ApplicationContextUtil.getContext().getBean(StudentService.class);
	Logger log=Logger.getLogger(QueueStudentStatusHandler.class);
	private byte[] redisKey;
	private byte[] errredisKey;
	{
		try {
			redisKey = ObjectUtil.objectToBytes("studentStatus");
			errredisKey = ObjectUtil.objectToBytes("errstudentStatus");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void excuteQueue(StudentStatusMsg message){
		log.info(message.getStudentId());
		studentService.setStudentRealStatus(message);
	}

	@Override
	public void run() {
		byte[] bytes =null;
		try{
			bytes = JedisUtil.rpop(redisKey);
			while (bytes!=null) {
				Object object = ObjectUtil.bytesToObject(bytes);
				if(object instanceof StudentStatusMsg){
					StudentStatusMsg msg = (StudentStatusMsg) object;
					excuteQueue(msg);
				}else{
					log.info("存入异常的对象！");
				}
				bytes = JedisUtil.rpop(redisKey);
			}
		}catch(Exception e){
			e.printStackTrace();
			if(bytes!=null){
				log.error("存入异常！"+e.getMessage());
				JedisUtil.lpush(errredisKey, bytes);//继续执行
			}

		}
	}
}
