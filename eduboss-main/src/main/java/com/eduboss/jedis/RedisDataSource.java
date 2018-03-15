package com.eduboss.jedis;

import redis.clients.jedis.ShardedJedis;

/** 
 * @author  author :Yao 
 * @date  2016年7月5日 下午2:38:06 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
public interface RedisDataSource {
	 	public abstract ShardedJedis getRedisClient();
	    public void returnResource(ShardedJedis shardedJedis);
	    public void returnResource(ShardedJedis shardedJedis,boolean broken);
}
