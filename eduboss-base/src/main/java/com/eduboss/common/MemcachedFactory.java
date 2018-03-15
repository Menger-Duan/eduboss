package com.eduboss.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eduboss.memcached.MemcachedClient;
import com.eduboss.memcached.SockIOPool;


public class MemcachedFactory {
	private static final Log log = LogFactory.getLog(MemcachedFactory.class);
	private static Map<String,MemcachedFactory> mfs = new HashMap<String,MemcachedFactory>();
	
	//创建Memcached客户端
	protected static MemcachedClient memcachedClient = null;	
	//MemcachedIP和端口号
	private String[] serverList = new String[]{"localhost:11211"}; 
     //Memcached客户端提供的一个套接字连接池
	private SockIOPool pool = null;
	
	private static final String DEFAULT = "default";
	
	private MemcachedFactory(){}

	public static MemcachedFactory instance(){
		return instance(DEFAULT);
	}

	public static MemcachedFactory instance(String name){
		if(mfs.containsKey(name)){
			return mfs.get(name);
		}
		MemcachedFactory mf = new MemcachedFactory();
		mfs.put(name, mf);
		return mf;
	}
	
	public void start(){		
		memcachedClient = new MemcachedClient(); 
		pool = SockIOPool.getInstance();		
		pool.setServers(this.serverList);		
        pool.setFailover(true);  
        pool.setInitConn(10);  
        pool.setMinConn(5);  
        pool.setMaxConn(250);
        //设置threadPool最大的空闲时间
        pool.setMaxIdle(1000 * 60 * 60 * 6);
        //平衡thread sleep 时间 单位毫秒
        pool.setMaintSleep(30);
        //这是开启一个nagle 算法。此算法避免网络中充塞小封包，提高网络的利用率
        pool.setNagle(true);
        //连接建立后对socket 的读取超时
        pool.setSocketTO(30000);  
        pool.setAliveCheck(true);
        //一致性hash
        //pool.setHashingAlg(3);
        pool.initialize();
		
	}
	
	public void destroy() throws Exception {
		if(pool != null){
			pool.shutDown();
			memcachedClient = null;
		}
	}
	
	public MemcachedClient getClient() {
		if(memcachedClient == null){
			start();
		}
		return memcachedClient;
	}

	 

	public void setServers(String servers) {
		if(servers != null && !servers.isEmpty()){
			this.serverList = servers.split(",");
			pool = null;
		}		
	}	
	
}
