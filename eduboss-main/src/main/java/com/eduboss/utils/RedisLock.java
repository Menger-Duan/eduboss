package com.eduboss.utils;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eduboss.exception.ApplicationException;

/**
 * Redis分布式锁对象
 */
public class RedisLock {

    private static final Logger logger = LoggerFactory
            .getLogger(RedisLock.class);

    // lock flag stored in redis
    private static final String LOCKED = "TRUE";

    private static final String PREFIX = "spark:lock:";

    // lock expire time(s)
    public static final int EXPIRE = 300;

    // timeout(ms)
    private long timeout = 30000;

    // private Jedis jedis;
    private String key;

    // state flag
    private boolean locked = false;

    private long startTime;

    /**
     * 分布式锁，内部使用Redis进行加锁, 可以设定死锁超时时间， 默认是30s
     * 
     * @param key
     *            锁定的key
     */
    public RedisLock(String key) {
        this.key = PREFIX + key;
    }

    public RedisLock(String key, long timeout) {
        this.key = PREFIX + key;
        this.timeout = timeout;
    }

    /**
     * 加锁，线程一直轮休获取锁，没获取到则阻塞，获取到锁程序往下执行
     * 
     * @param timeout
     *            设定超时时间，单位秒， 如果超时， 抛出异常
     * @throws ApplicationException
     *             锁超时，阻止程序继续往下执行
     */
    private void lock(long timeout) throws ApplicationException {
        this.startTime = System.currentTimeMillis();
        long nano = System.nanoTime();
        timeout *= 1000000;
        final Random r = new Random();
        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (JedisUtil.setnx(key, LOCKED, EXPIRE) > 0) {
                    locked = true;
                    logger.debug("add RedisLock[{}].", key);
                    break;
                }
                Thread.sleep(5, r.nextInt(500));
            }
        } catch (Exception e) {
            logger.error("lock() throws Exception", e);
            unlock();
            throw new ApplicationException("lock() throws Exception");
        }
        // 锁超时， 阻止程序继续往下执行
        if (!locked) {
            logger.info("wait redis[{}] lock timeout.", key);
            throw new ApplicationException("wait redis key lock timeout.");
        }
    }
    
    public boolean simplelock() {
        boolean success = false;
        
        if (JedisUtil.setnx(key, LOCKED, EXPIRE) > 0) {
            locked = true;
            success = true;
        }
        
        return success;
    }

    /**
     * 释放锁，其线程可以开始争用
     */
    public void unlock() {
        try {
            if (locked) {
                JedisUtil.del(key);
                logger.debug("release RedisLock[{}].", key);
            }
            // logger.info("the RedisLock["+this.key+"] has continued {}ms",
            // System.currentTimeMillis()-this.startTime);
        } catch (Exception e) {
            logger.error("unlock() throws Exception", e);
        }
    }

    /**
     * 加锁， 默认30s没有获取到锁， 抛出异常
     * 
     * @throws ApplicationException
     *             锁超时异常
     */
    public void lock() throws ApplicationException {
        lock(timeout);
    }

}