package com.eduboss.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;

import com.eduboss.utils.RedisLock;

/**
 * Advice通知类
 * 测试after,before,around,throwing,returning Advice.
 * @author Admin
 *
 */
public class MiniClassInventoryServiceAopHandler {
    
    private final static Logger logger = Logger.getLogger(MiniClassInventoryServiceAopHandler.class);
	
    String miniClassId = "";
    RedisLock lock = null;
    
	/**
	 * @param joinPoint
	 */
	private void doBefore(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		if ("reduceInventory".equals(methodName) || "revertInventory".equals(methodName)) {
		    Object object[] = joinPoint.getArgs(); // 获取被切函数 的参数
		    miniClassId = (String) object[0];
		  /*  lock = new RedisLock(miniClassId, 5);
		    lock.simplelock();*/
		}
	}

	/**
	 * @param joinPoint
	 */
	private void doAfter(JoinPoint joinPoint) {
	    String methodName = joinPoint.getSignature().getName();
        if ("reduceInventory".equals(methodName) || "revertInventory".equals(methodName)) {
            /*if (lock != null) {
                lock.unlock();
            }*/
        }
	}
	
	private  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
	    String methodName = joinPoint.getSignature().getName();
        if ("reduceInventory".equals(methodName) || "revertInventory".equals(methodName)) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
	}

}
