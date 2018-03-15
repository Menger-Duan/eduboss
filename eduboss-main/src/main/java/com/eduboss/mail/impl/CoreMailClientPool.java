package com.eduboss.mail.impl;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;

/**@author wmy
 *@date 2015年10月8日上午11:34:34
 *@version 1.0 
 *@description
 */
public class CoreMailClientPool {
	
	 private String apiHost;
	 private Integer apiPort;
	 
	 private static CoreMailClientPool instrance = null;
	 
	 private ObjectPool<IClient> clientPool = new GenericObjectPool<IClient>(new BasePoolableObjectFactory<IClient>() {
        @Override
        public IClient makeObject() throws Exception {
            Socket socket = new Socket(apiHost, apiPort);
            IClient client = APIContext.getClient(socket);
            return client;
        }

        @Override
        public void activateObject(IClient client) throws IOException {
            // 在每次从连接池中获取到实例时，校验一下链接是否可用
            client.apiSubmit("cmd=-2");
        }

        @Override
        public void destroyObject(IClient client) throws IOException {
            client.close();
        }
    });

    // 配置连接池参数
    {
        // 配置最多 20 个 Socket 链接
        ((GenericObjectPool<IClient>) clientPool).setMaxActive(20);
        ((GenericObjectPool<IClient>) clientPool).setMaxIdle(20);
        // 当 20 个链接全满时，最多等待 20 秒，之后会抛出 NoSuchElementException
        ((GenericObjectPool<IClient>) clientPool).setMaxWait(20000);
    }
    
    private CoreMailClientPool(String apiHost, Integer apiPort){
    	this.apiHost = apiHost;
    	this.apiPort = apiPort;
    }
    
    public static synchronized CoreMailClientPool getInstrance(String apiHost, Integer apiPort){
    	if(instrance == null){
    		instrance = new CoreMailClientPool(apiHost, apiPort);
    	}
    	return instrance;
    }
    
    public ObjectPool<IClient> getClientPool(){
    	if(instrance != null){
    		return this.clientPool;
    	}
    	return null;
    }
    
}


