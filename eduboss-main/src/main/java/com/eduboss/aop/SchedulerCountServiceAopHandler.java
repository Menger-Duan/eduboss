package com.eduboss.aop;

import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.eduboss.common.SchedulerExecuteStatus;
import com.eduboss.domain.SchedulerExecuteLog;
import com.eduboss.service.SchedulerExecuteLogService;
import com.eduboss.utils.DateTools;

/**
 * Advice通知类
 * 测试after,before,around,throwing,returning Advice.
 * @author Admin
 *
 */
public class SchedulerCountServiceAopHandler {
	
	@Autowired
	private SchedulerExecuteLogService schedulerExecuteLogService;
	
	// declare domain
	SchedulerExecuteLog log;
	
	/**
	 * @param joinPoint
	 */
	private void doBefore(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		if (!"updateOdsDayIncomingQuantityAnalyzeVery5Second".equals(methodName) 
				&& !"sendFailureLogBySms".equals(methodName) 
				&& !"excuteQueueTask".equals(methodName)
				&& !"excuteStuQueueTask".equals(methodName)) {
			System.out.println(methodName);
			log = new SchedulerExecuteLog();
			log.setSchedulerName(methodName);
			String startTime = DateTools.getCurrentDateTime();
			log.setStartTime(startTime);
			log.setStatus(SchedulerExecuteStatus.FAILURE);
			schedulerExecuteLogService.saveSchedulerExecuteLog(log);
		}
	}

	/**
	 * @param joinPoint
	 */
	private void doAfter(JoinPoint joinPoint) {
		String endTime = DateTools.getCurrentDateTime();
		String methodName = joinPoint.getSignature().getName();
		if (!"updateOdsDayIncomingQuantityAnalyzeVery5Second".equals(methodName) 
				&& !"sendFailureLogBySms".equals(methodName)
				&& !"excuteQueueTask".equals(methodName)
				&& !"excuteStuQueueTask".equals(methodName)) {
			System.out.println(methodName);
			if (log != null && log.getSchedulerName().equals(methodName)) {
				log.setEndTime(endTime);
				log.setStatus(SchedulerExecuteStatus.SUCCESS);
			}
			schedulerExecuteLogService.saveSchedulerExecuteLog(log);
		}
	}
	
	private  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		String methodName = joinPoint.getSignature().getName();
		if (!"updateOdsDayIncomingQuantityAnalyzeVery5Second".equals(methodName)
				&& !"sendFailureLogBySms".equals(methodName)
				&& !"excuteQueueTask".equals(methodName)
				&& !"excuteStuQueueTask".equals(methodName)) {
			System.out.println(methodName);
			if (log != null && log.getSchedulerName().equals(methodName)) {
				log.setStatus(SchedulerExecuteStatus.FAILURE);
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			schedulerExecuteLogService.saveSchedulerExecuteLog(log);
		}
	}

}
