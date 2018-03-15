package com.eduboss.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.eduboss.common.MD5;
import com.eduboss.common.MemcachedFactory;
import com.eduboss.memcached.MemcachedClient;
import com.eduboss.service.MemcacheManager;

/**
 * Memcache命令说明
 * 存储：
 * 1、set是保存数据命令。会覆盖已存在的数据，而新数据将在LRU顶端
 * 2、add只有在该数据不存在时才保存该数据。如果是新加入的item，那么将其直接放在LRU顶端；
 * 	    如果item（key）已经存在则add失败，那么将这个item从LRU链表上摘下再放到LRU顶端。
 * 3、replace 替换已经存在的数据。 这个操作几乎用不到。
 * 4、append 紧接着已经存在的item增加item。这个操作不允许增加原来的item限制，对管理链表很有用。
 * 5、prepend 与append命令类似，这个命令是在已存在的数据前加入新数据。
 * 1、get 读取命令， 更具一个或多个key查找数据，并返回所找到的数据。
 * @author 郭华明
 *
 */
@Component
@SuppressWarnings({"unchecked"})
public class MemcacheManagerImpl implements MemcacheManager{
	protected Log log = LogFactory.getLog(this.getClass());
	
	private MemcachedFactory mf = MemcachedFactory.instance();
	/**
	 * 根据key获得value
	 * @param key
	 * @return
	 */
	@Override
	public <T> T get(final Object key){
		if(key != null){
			final MemcachedClient mcc = mf.getClient();
			return (T)mcc.get(genKey(key));
		}
		return null;
	}

	@Override
	public Object[] getMultiArray(Object[] keys){
		for(int i = 0; i < keys.length; i++){
			keys[i] = genKey(keys[i]);
		}
		final MemcachedClient mcc = mf.getClient();
		Object[] objs = mcc.getMultiArray((String[])keys);
		return objs;
	}

	/**
	 * 适用于根据查询语句获得列表
	 * @param key
	 * @param clazz
	 * @return
	 */
	@Override
	public <T> List<T> getList(final Object key){
		final MemcachedClient mcc = mf.getClient();
		List<T> listId = (List<T>)mcc.get(genKey(key));
		return listId;
	}
	
	/**
	 * 添加对象进缓存
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(final Object key, final Object value){
		final MemcachedClient mcc = mf.getClient();
		return mcc.set(genKey(key), value);
	}
	/**
	 * 添加对象进缓存
	 * @param key
	 * @param value
	 * @param date 过期时间
	 * @return
	 */
	@Override
	public boolean set(final Object key, final Object value, final Date date){
		final MemcachedClient mcc = mf.getClient();
		if(date != null)
			return mcc.set(genKey(key), value, date);
		else
			return mcc.set(genKey(key), value);
	}
	/**
	 * 添加对象进缓存
	 * @param key
	 * @param value
	 * @param delay 过期间隔时间，毫秒
	 * @return
	 */
	@Override
	public boolean set(final Object key, final Object value, final Long expire){
		final MemcachedClient mcc = mf.getClient();
		if(expire == null || expire == 0L)
			return mcc.set(genKey(key), value);
		else
			return mcc.set(genKey(key), value, expireDate(expire));
	}	

	/**
	 * 从缓存删除一个值
	 * @param key
	 * @return
	 */
	@Override
	public boolean delete(final Object key){
		final MemcachedClient mcc = mf.getClient();
		boolean r = mcc.delete(genKey(key));
		if(log.isDebugEnabled())
			if(r)
				log.debug("key为：" + key + "的缓存已删除！");
			else
				log.debug("key为：" + key + "的缓存删除失败！");
		return r;
	}
	
	/**
	 * 从缓存删除多个值
	 * @param list
	 * @return
	 */
	@Override
	public boolean delete(final List<String> list){
		final MemcachedClient mcc = mf.getClient();
		boolean flag = true;
		for(Object key : list){
			flag = mcc.delete(genKey(key));
			if(log.isDebugEnabled() && flag)
				log.debug("key为：" + key + "的缓存已被清除！");
		}
		return flag;
	}
	/**
	 * 从缓存删除多个值
	 * @param list
	 * @return
	 */
	@Override
	public boolean delete(final String[] keys){
		final MemcachedClient mcc = mf.getClient();
		boolean flag = true;
		for(Object key : keys){
			flag = mcc.delete(genKey(key));
			if(log.isDebugEnabled() && flag)
				log.debug("key为：" + key + "的缓存已被清除！");
		}
		return flag;
	}
	
	
	
	@Override
	public boolean addOrIncr(final Object key) {
		boolean flag = true;
		final MemcachedClient mcc = mf.getClient();
		Long n = mcc.addOrIncr(genKey(key));
		if(n == null)
			flag = false;
		return flag;
	}

	@Override
	public boolean addOrDecr(final Object key) {
		boolean flag = true;
		final MemcachedClient mcc = mf.getClient();
		Long n = mcc.addOrDecr(genKey(key));
		if(n == null)
			flag = false;
		return flag;
	}

	/**
	 * 取得计算的过期时间
	 * @param delay
	 * @return
	 */
	public static Date expireDate(Long expire){
		expire = expire == null?0L:expire;
		return new Date(System.currentTimeMillis() + expire);	
	}

	public static String genKeyForIdNoHash(final Object id, final Class<?> clazz){
		return
			new StringBuilder(clazz.getSimpleName())
			.append("_").append(id)
			.toString();
	}
	/**
	 * 通用key生成
	 * @param id
	 * @param flag
	 * @return
	 */
	@Override
	public String genKey(final Object key){
		return MD5.encode(String.valueOf(key));
	}

	@Override
	public MemcachedFactory getMemcachedFactory() {
		return this.mf;
	}

}
