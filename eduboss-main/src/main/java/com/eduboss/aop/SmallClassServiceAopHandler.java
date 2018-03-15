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
public class SmallClassServiceAopHandler {
    
    private final static Logger logger = Logger.getLogger(SmallClassServiceAopHandler.class);
	
    String smallClassId = "";
    RedisLock lock = null;
    
	/**
	 * @param joinPoint
	 */
	private void doBefore(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		if ("AddStudentForMiniClasss".equals(methodName)) {
		    Object object[] = joinPoint.getArgs(); // 获取被切函数 的参数
		    smallClassId = (String) object[1];
		   /* lock = new RedisLock(smallClassId, 5);
		    lock.simplelock();*/
		}
	}

	/**
	 * @param joinPoint
	 */
	private void doAfter(JoinPoint joinPoint) {
	    String methodName = joinPoint.getSignature().getName();
        if ("AddStudentForMiniClasss".equals(methodName)) {
          /*  if (lock != null) {
                lock.unlock();
            }*/
        }
	}
	
	private  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
	    String methodName = joinPoint.getSignature().getName();
        if ("AddStudentForMiniClasss".equals(methodName)) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
	}

}
