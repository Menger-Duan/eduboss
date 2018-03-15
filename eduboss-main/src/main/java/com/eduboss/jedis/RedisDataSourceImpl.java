package com.eduboss.jedis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/** 
 * @author  author :Yao 
 * @date  2016年7月5日 下午2:38:06 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
@Repository("redisDataSource")
public class RedisDataSourceImpl implements RedisDataSource {
	 private static final Logger log = Logger.getLogger(RedisDataSourceImpl.class);

	    @Autowired
	    private ShardedJedisPool    shardedJedisPool;
	    
	    private ShardedJedis shardJedis;

	    public ShardedJedis getRedisClient() {
	        try {
	        	if(shardJedis==null)
	             shardJedis = shardedJedisPool.getResource();
	            return shardJedis;
	        } catch (Exception e) {
	            log.error("getRedisClent error", e);
	        }
	        return null;
	    }

	    public void returnResource(ShardedJedis shardedJedis) {
	        shardedJedisPool.returnResource(shardedJedis);
	    }

	    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
	        if (broken) {
	            shardedJedisPool.returnBrokenResource(shardedJedis);
	        } else {
	            shardedJedisPool.returnResource(shardedJedis);
	        }
	    }

		public ShardedJedisPool getShardedJedisPool() {
			return shardedJedisPool;
		}

		public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
			this.shardedJedisPool = shardedJedisPool;
		}
	    
	    
}
