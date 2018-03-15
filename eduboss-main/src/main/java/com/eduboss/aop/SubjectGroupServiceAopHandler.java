package com.eduboss.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import com.eduboss.domainVo.SubjectGroupVo;
import com.eduboss.service.OperationCountService;
import com.eduboss.utils.DateTools;

/**
 * Advice通知类
 * 测试after,before,around,throwing,returning Advice.
 * @author Admin
 *
 */
public class SubjectGroupServiceAopHandler {
	
	@Autowired
	private OperationCountService operationCountService;
	
	 private static Logger logger = Logger.getLogger(SubjectGroupServiceAopHandler.class); 

	/**
	 * @param joinPoint
	 */
	private void doAfter(JoinPoint joinPoint) {
		String methodName = joinPoint.getSignature().getName();
		if ("editSubjectGroup".equals(methodName) || "deleteSubjectGroup".equals(methodName)) {
			Object object[] = joinPoint.getArgs(); // 获取被切函数 的参数
			SubjectGroupVo subjectGroupVo = (SubjectGroupVo) object[0];
			String currentMonth = DateTools.getCurrentDate().substring(0, 4) + DateTools.getCurrentDate().substring(5,7);
			if (subjectGroupVo.getVersion() == Integer.parseInt(currentMonth)) {
				if (subjectGroupVo.getIsUpToNextMonth() == 1) {
					String nextMonth = "";
					try {
						nextMonth = DateTools.getNextMonth(DateTools.getCurrentDate()).substring(0, 4) 
								+  DateTools.getNextMonth(DateTools.getCurrentDate()).substring(5,7);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// 执行存储过程，批量全部更新下月这个校区的科组
					operationCountService.updateToNextVersion(subjectGroupVo.getBlBrenchId(), subjectGroupVo.getBlCampusId(), subjectGroupVo.getVersion(), Integer.parseInt(nextMonth));
				}
			}
			
		}
		
	}
	
	private  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
		String methodName = joinPoint.getSignature().getName();
		Object object[] = joinPoint.getArgs(); // 获取被切函数 的参数
		if ("editSubjectGroup".equals(methodName) || "deleteSubjectGroup".equals(methodName)) {
			SubjectGroupVo subjectGroupVo = (SubjectGroupVo) object[0];
			logger.error(subjectGroupVo.getBlBrenchId() + "-" + subjectGroupVo.getBlCampusId() + "批量全部更新下月这个校区的科组失败");
		}
	}

}
