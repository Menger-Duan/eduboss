package com.eduboss.service;

import java.util.Date;
import java.util.List;

import com.eduboss.common.MemcachedFactory;

public interface MemcacheManager {
	
	public MemcachedFactory getMemcachedFactory();
	/**
	 * 根据key获得value
	 * @param key
	 * @return
	 */
	public <T> T get(final Object key);

	/**
	 * 获取多个key的缓存，推荐替代多次使用get
	 * @param <T>
	 * @param keys key数组
	 * @param clazz 欲返回的对象类型
	 * @return 数组形式的对象列表
	 */
	public Object[] getMultiArray(Object[] keys);
	
	
	/**
	 * 适用于根据查询语句获得列表
	 * @param key
	 * @param clazz
	 * @return
	 */	
	public <T> List<T> getList(Object key);

	/**
	 * 添加对象进缓存
	 * @param key
	 * @param value
	 * @return
	 */	
	public boolean set(Object key, Object value);
	/**
	 * 添加对象进缓存
	 * @param key
	 * @param value
	 * @param date 过期时间
	 * @return
	 */	
	public boolean set(Object key, Object value, Date date);
	/**
	 * 添加对象进缓存
	 * @param key
	 * @param value
	 * @param expire 过期间隔时间，毫秒
	 * @return
	 */	
	public boolean set(Object key, Object value, Long expire);
		
	/**
	 * 从缓存删除一个值
	 * @param key
	 * @return
	 */	
	public boolean delete(Object key);
	
	/**
	 * 从缓存删除多个值
	 * @param list
	 * @return
	 */	
	public boolean delete(List<String> list);
	/**
	 * 从缓存删除多个值
	 * @param list
	 * @return
	 */	
	public boolean delete(final String[] keys);
	
	/**
	 * 给指定key的缓存加一，不存在则新增，原子操作
	 * @param key
	 * @return
	 */
	public boolean addOrIncr(final Object key);
	/**
	 * 给指定key的缓存减一，不存在则新增，原子操作
	 * @param key
	 * @return
	 */
	public boolean addOrDecr(final Object key);
	
	////////////////////////////////////////////////

	/**
	 * 生成key
	 * @param key
	 * @return
	 */
	public String genKey(Object key);
}
