package com.eduboss.jedis;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;

import redis.clients.jedis.ShardedJedis;

import com.eduboss.dao.ProcedureDao;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;

public class CleanSmallclassHandler implements Runnable {

	 private RedisDataSource redisDataSource = (RedisDataSource) ApplicationContextUtil.getContext().getBean(RedisDataSource.class);
	 private HibernateTemplate hibernateTemplate = (HibernateTemplate)ApplicationContextUtil.getContext().getBean(HibernateTemplate.class);
	 
		private byte[] errorCleanKey;
		private byte[] cleanKey;
		private ShardedJedis client;
		
		Logger log=Logger.getLogger(CleanSmallclassHandler.class);
		
		
		{
			 try {
				 errorCleanKey=ObjectUtil.objectToBytes("cleanError");
				 cleanKey = ObjectUtil.objectToBytes("clean");
				client = redisDataSource.getRedisClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/** 
		 * @author tangyuping
		 * 执行过季小班批处理操作
		*/
		public void excuteQueue(CleanSmallClass smallclass) throws SQLException{
			log.info(smallclass.getProductVersion() + " "+ smallclass.getProductQuarter()+"处理过季小班产品！");
			if(StringUtils.isNotBlank(smallclass.getProductVersion()) && 
					StringUtils.isNotBlank(smallclass.getProductQuarter())){
				
				log.info("########## proc_clean_out_expired_miniclass ########## " + "begin" );
				
				ProcedureDao procedureDao = new ProcedureDao(this.hibernateTemplate);
				String sql = "{CALL proc_clean_out_expired_miniclass(?,?)}";
				List<Object> listParam = new ArrayList<Object>();
				listParam.add(smallclass.getProductVersion());
				listParam.add(smallclass.getProductQuarter());				
				procedureDao.executeProc(sql, listParam);
				
				
				log.info("########## proc_clean_out_expired_miniclass ########## " + "end" );
			}
		}
		
		

		@Override
		public void run() {
			CleanSmallClass smallclass = new CleanSmallClass();
			List<CleanSmallClass> list = new ArrayList<CleanSmallClass>();
			byte[] bytes =null;
			 
			try{
				bytes = client.rpop(cleanKey);
				while (bytes!=null) {
					int count = 0;
					smallclass = (CleanSmallClass) ObjectUtil.bytesToObject(bytes);					
					for(CleanSmallClass sc : list){
						if(smallclass.getProductQuarter().equals(sc.getProductQuarter()) && 
								smallclass.getProductVersion().equals(sc.getProductVersion())){
							count+=1;
							break;
						}
					}
					if(count > 0 ){
						bytes = client.rpop(cleanKey);
						continue;
						
					}
					list.add(smallclass);
			        excuteQueue(smallclass);
			        bytes = client.rpop(cleanKey);
				}
			}catch(Exception e){
				if(bytes!=null){
					log.info("存入异常！");
					JedisUtil.lpush(errorCleanKey,bytes);
				}
			}
			
		}

}

