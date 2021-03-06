package com.eduboss.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** 
 * @author  author :Yao 
 * @date  2016年7月5日 上午11:50:49 
 * @version 1.0 
 * @parameter  
 * @since  
 * @return  
 */
 
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
public class JedisUtil {
    
        private static String redisCode = "utf-8";
	 
	    private static String JEDIS_IP=PropertiesUtils.getStringValue("redis.host");;
	    private static int JEDIS_PORT=PropertiesUtils.getIntValue("redis.port");;
	    private static String JEDIS_PASSWORD=PropertiesUtils.getStringValue("redis.pass");;
	    //private static String JEDIS_SLAVE;
	 
	    private static JedisPool jedisPool;
	    
	    private static Logger logger = Logger.getLogger(JedisUtil.class);
	 
	    static {
	    	GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
	    	
//	    	poolConfig.set
//	        Configuration conf = Configuration.getInstance();
//	        JEDIS_IP = conf.getString("jedis.ip", "127.0.0.1");
//	        JEDIS_PORT = conf.getInt("jedis.port", 6379);
	        JedisPoolConfig config = new JedisPoolConfig();
//	        config.setMaxActive(5000);
	        config.setMaxIdle(256);//20
//	        config.setMaxWait(5000L);
	        config.setTestOnBorrow(true);
	        config.setTestOnReturn(true);
	        config.setTestWhileIdle(true);
	        config.setMinEvictableIdleTimeMillis(60000l);
	        config.setTimeBetweenEvictionRunsMillis(3000l);
	        config.setNumTestsPerEvictionRun(-1);
	        jedisPool = new JedisPool(poolConfig, JEDIS_IP, JEDIS_PORT, 60000,JEDIS_PASSWORD);
	    }

	public static void main(String[] args) throws Exception {
		try{
			byte[] key = ObjectUtil.objectToBytes("grayTablePublish");
			JedisUtil.set(key, ObjectUtil.objectToBytes(null));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	 
	    
	    public static void setex(String key, int seconds, String value) {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.setex(key, seconds, value);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        
	      }
	    
	    public static void set(String key, String value) {
	        set(key, value, 0);
	    }
	    
	    public static void set(String key, String value, int exp) {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.set(key, value);
	            if(exp > 0) {
	                jedis.expire(key, exp);
	            }
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        
	      }
	    
	   
	    
	    public static void del(String key) {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.del(key);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        
	      }
	    /**
	     * 获取数据
	     * @param key
	     * @return
	     */
	    public static String get(String key) {
	 
	        String value = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            value = jedis.get(key);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	 
	        return value;
	    }
	    
	    public static Boolean exists(String key) {
	    	Jedis jedis = jedisPool.getResource();
	    	 boolean returnBoolean=false;
	    	try{
	    		 logger.info("开始判断是否存在"+key+"的jedis");
		         returnBoolean =  jedis.exists(key);
		         logger.info("结束判断是否存在"+key+"的jedis");
		    } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        return returnBoolean;
	      }
	 
	    public static Boolean exists(byte[] key) {
	    	Jedis jedis = jedisPool.getResource();
	    	 boolean returnBoolean=false;
	    	try{
	    		 logger.info("开始判断是否存在"+key+"的jedis");
		         returnBoolean =  jedis.exists(key);
		         logger.info("结束判断是否存在"+key+"的jedis");
		    } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        return returnBoolean;
	      }
	    
	    public static void close(Jedis jedis) {
	        try {
	        	if (jedis != null) {
	        		jedis.close();
	        	}
//	            jedisPool.returnResource(jedis);
	        } catch (Exception e) {
	            if (jedis.isConnected()) {
	                jedis.quit();
	                jedis.disconnect();
	            }
	        }
	    }
	 
	    /**
	     * 获取数据
	     * 
	     * @param key
	     * @return
	     */
	    public static byte[] get(byte[] key) {
	 
	        byte[] value = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            value = jedis.get(key);
	        } catch (Exception e) {
	            //释放redis对象
	        	jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	 
	        return value;
	    }
	 
	    public static void set(byte[] key, byte[] value) {
	 
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.set(key, value);
	        } catch (Exception e) {
	            //释放redis对象
	        	jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	    }
	 
	    public static void set(byte[] key, byte[] value, int time) {
	 
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.set(key, value);
	            jedis.expire(key, time);
	        } catch (Exception e) {
	            //释放redis对象
	        	jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	    }
	 
	    public static void hset(byte[] key, byte[] field, byte[] value) {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.hset(key, field, value);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	    }
	 
	    public static void hset(String key, String field, String value) {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.hset(key, field, value);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	    }
	 
	    /**
	     * 获取数据
	     * 
	     * @param key
	     * @return
	     */
	    public static String hget(String key, String field) {
	 
	        String value = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            value = jedis.hget(key, field);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	 
	        return value;
	    }
	 
	    /**
	     * 获取数据
	     * 
	     * @param key
	     * @return
	     */
	    public static byte[] hget(byte[] key, byte[] field) {
	 
	        byte[] value = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            value = jedis.hget(key, field);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	 
	        return value;
	    }
	 
	    public static void hdel(byte[] key, byte[] field) {
	 
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.hdel(key, field);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	    }
	 
	    /**
	     * 存储REDIS队列 顺序存储
	     */
	    public static void lpush(byte[] key, byte[] value) {
	 
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            jedis.lpush(key, value);
	 
	        } catch (Exception e) {
	 
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	 
	            //返还到连接池
	            close(jedis);
	 
	        }
	    }
	 
	    /**
	     * 存储REDIS队列 反向存储
	     */
	    public static void rpush(byte[] key, byte[] value) {
	 
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            jedis.rpush(key, value);
	 
	        } catch (Exception e) {
	 
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	 
	            //返还到连接池
	            close(jedis);
	 
	        }
	    }
	 
	    /**
	     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
	     */
	    public static void rpoplpush(byte[] key, byte[] destination) {
	 
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            jedis.rpoplpush(key, destination);
	 
	        } catch (Exception e) {
	 
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	 
	            //返还到连接池
	            close(jedis);
	 
	        }
	    }
	 
	    /**
	     * 获取队列数据
	     * @return
	     */
	    public static List<byte[]> lpopList(byte[] key) {
	 
	        List<byte[]> list = null;
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            list = jedis.lrange(key, 0, -1);
	 
	        } catch (Exception e) {
	 
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	 
	            //返还到连接池
	            close(jedis);
	 
	        }
	        return list;
	    }
	 
	    /**
	     * 获取队列数据
	     * @return
	     */
	    public static byte[] rpop(byte[] key) {
	 
	        byte[] bytes = null;
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            bytes = jedis.rpop(key);
	 
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	 
	        }
	        return bytes;
	    }

	public static String rpop(String key) {
		String bytes= null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			bytes = jedis.rpop(key);
		} catch (Exception e) {
			//释放redis对象
			jedis.close();
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
		return bytes;
	}

	public static void lpush(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.lpush(key, value);
		} catch (Exception e) {
			//释放redis对象
			jedis.close();
			e.printStackTrace();
		} finally {
			//返还到连接池
			close(jedis);
		}
	}
	 
	    public static void hmset(Object key, Map<String, String> hash) {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.hmset(key.toString(), hash);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	            //返还到连接池
	            close(jedis);
	 
	        }
	    }
	 
	    public static void hmset(Object key, Map<String, String> hash, int time) {
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            jedis.hmset(key.toString(), hash);
	            jedis.expire(key.toString(), time);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	            //返还到连接池
	            close(jedis);
	 
	        }
	    }
	 
	    public static List<String> hmget(Object key, String... fields) {
	        List<String> result = null;
	        Jedis jedis = null;
	        try {
	 
	            jedis = jedisPool.getResource();
	            result = jedis.hmget(key.toString(), fields);
	 
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	            //返还到连接池
	            close(jedis);
	 
	        }
	        return result;
	    }
	 
	    public static Set<String> hkeys(String key) {
	        Set<String> result = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            result = jedis.hkeys(key);
	 
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	            //返还到连接池
	            close(jedis);
	 
	        }
	        return result;
	    }
	 
	    public static List<byte[]> lrange(byte[] key, int from, int to) {
	        List<byte[]> result = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            result = jedis.lrange(key, from, to);
	 
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	            //返还到连接池
	            close(jedis);
	 
	        }
	        return result;
	    }
	 
	    public static Map<byte[],byte[]> hgetAll(byte[] key) {
	        Map<byte[],byte[]> result = null;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            result = jedis.hgetAll(key);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	 
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        return result;
	    }
	 
	    public static void del(byte[] key) {
	 
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.del(key);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	    }
	 
	    public static long llen(byte[] key) {
	 
	        long len = 0;
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.llen(key);
	        } catch (Exception e) {
	            //释放redis对象
	            jedis.close();
	            e.printStackTrace();
	        } finally {
	            //返还到连接池
	            close(jedis);
	        }
	        return len;
	    }
	    
	    public  static byte[] ObjectToByte(Object obj) {  
	        byte[] bytes = null;  
	        try {  
	            // object to bytearray  
	            ByteArrayOutputStream bo = new ByteArrayOutputStream();  
	            ObjectOutputStream oo = new ObjectOutputStream(bo);  
	            oo.writeObject(obj);  
	      
	            bytes = bo.toByteArray();  
	      
	            bo.close();  
	            oo.close();  
	        } catch (Exception e) {  
	            System.out.println("translation" + e.getMessage());  
	            e.printStackTrace();  
	        }  
	        return bytes;  
	    } 
	    
	    /**
	     * byte转对象
	     * @param bytes
	     * @return
	     */
	    public static  Object ByteToObject(byte[] bytes) {
	        Object obj = null;
	        try {
	            // bytearray to object
	            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
	            ObjectInputStream oi = new ObjectInputStream(bi);

	            obj = oi.readObject();
	            bi.close();
	            oi.close();
	        } catch (Exception e) {
	            System.out.println("translation" + e.getMessage());
	            e.printStackTrace();
	        }
	        return obj;
	    }
	    
	    /**
	     * 如果key不存在添加key value 并且设置存活时间(byte)，当key已经存在时，就不做任何操作
	     * 
	     * @param key
	     * @param value
	     * @param liveTime
	     */
	    public static long setnx(final byte[] key, final byte[] value,
	            final int liveTime) {
	        Jedis jedis = null;
	        jedis = jedisPool.getResource();
	        jedis.expire(key, liveTime); 
	        return jedis.setnx(key, value);
	    }
	    
	    /**
	     * 如果key不存在添加key value 并且设置存活时间，当key已经存在时，就不做任何操作
	     * 
	     * @param key
	     * @param value
	     * @param liveTime
	     *            单位秒
	     */
	    public static long setnx(String key, String value, int liveTime) {
	        return setnx(strToByte(key), strToByte(value), liveTime);
	    }

	    /**
	     * 如果key不存在添加key value，当key已经存在时，就不做任何操作
	     * 
	     * @param key
	     * @param value
	     */
	    public static long setnx(String key, String value) {
	        return setnx(key, value, 0);
	    }
	    
	    /*
	     * 字符串转为byte数组类型(统一编码)
	     * 
	     * @param key
	     * 
	     * @return
	     */
	    public static byte[] strToByte(String key) {
	        try {
	            return key.getBytes(redisCode);
	        } catch (UnsupportedEncodingException e) {
	            return null;
	        }
	    }
	 
}
