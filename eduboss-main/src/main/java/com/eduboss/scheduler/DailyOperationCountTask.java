package com.eduboss.scheduler;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

public class DailyOperationCountTask implements org.quartz.Job {
	
	private static final Logger logger = LoggerFactory.getLogger(DailyOperationCountTask.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private UserService userService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Schedule job : ############## " +  this.getClass().getSimpleName()  +  " ##############  ,  start : " + (new Date())) ;
		callProc();
		logger.info("Schedule job : ############## " +  this.getClass().getSimpleName()  +  " ##############  ,  end : " + (new Date())) ;
		
	}

	public void callProc() {
		logger.debug("*** DailyOperationCountTask.callProc start");
		WebApplicationContext webApplicationContext = ContextLoader
				.getCurrentWebApplicationContext();
		HibernateTemplate hibernateTemplate001 = (HibernateTemplate) webApplicationContext
				.getBean(HibernateTemplate.class);
		Connection conn = null;
		CallableStatement dayFinanceCallableStatement = null;
		CallableStatement dayCourseConsumeCallableStatement = null;
		CallableStatement studentRemainingCallableStatement = null;
		CallableStatement studentCountCallableStatement = null;
		Session session = hibernateTemplate001.getSessionFactory()
				.openSession();
		try {

			String yesterdayStr =  DateTools.addDateToString( DateTools.getCurrentDate(), -1); 
			String todayStr =  DateTools.getCurrentDate();
			String targetDay;
			for(int i =0 ; i < 2 ; i++ ) {
				if(i==0) targetDay = todayStr;
				else targetDay = yesterdayStr; 
			
				SessionFactory sf = hibernateTemplate001.getSessionFactory();
				DataSource ds = SessionFactoryUtils.getDataSource(sf);
				conn = session.connection();
				// 以后统一使用proc_update_ODS_DAY_FINANCE_NEW
//				dayFinanceCallableStatement = conn
//						.prepareCall("{call proc_update_ODS_DAY_FINANCE(?)}");
				dayFinanceCallableStatement = conn
						.prepareCall("{call proc_update_ODS_DAY_FINANCE_NEW(?)}");
				dayFinanceCallableStatement.setString(1, targetDay);
				dayFinanceCallableStatement.execute(); // 调用存储过程
				conn.commit();
				dayFinanceCallableStatement.close();
				
				dayCourseConsumeCallableStatement = conn
						.prepareCall("{call proc_update_ODS_DAY_COURSE_CONSUME(?)}");
				dayCourseConsumeCallableStatement.setString(1, targetDay);
				dayCourseConsumeCallableStatement.execute(); // 调用存储过程
				conn.commit();
				dayCourseConsumeCallableStatement.close();
				
//				studentRemainingCallableStatement = conn
//						.prepareCall("{call proc_update_ODS_REAL_STUDENT_REMAINING(?)}");
				studentRemainingCallableStatement = conn
						.prepareCall("{call proc_update_ODS_REAL_STUDENT_REMAINING_NEW(?)}");
				studentRemainingCallableStatement.setString(1, targetDay);
				studentRemainingCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentRemainingCallableStatement.close();
				
				studentCountCallableStatement = conn
						.prepareCall("{call proc_update_ODS_REAL_STUDENT_COUNT(?)}");
				studentCountCallableStatement.setString(1, targetDay);
				studentCountCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentCountCallableStatement.close();
				
				studentCountCallableStatement = conn
						.prepareCall("{call proc_countOfCourseConsumeTeacherView(?)}");
				studentCountCallableStatement.setString(1, targetDay);
				studentCountCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentCountCallableStatement.close();
				
				studentCountCallableStatement = conn
						.prepareCall("{call proc_countOfODS_DAY_MINI_CLASS_SURPLUS_MONEY(?)}");
				studentCountCallableStatement.setString(1, targetDay);
				studentCountCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentCountCallableStatement.close();
				
				studentCountCallableStatement = conn
						.prepareCall("{call proc_update_ODS_DAY_MINI_CLASS_STUDENT_COUNT(?)}");
				studentCountCallableStatement.setString(1, targetDay);
				studentCountCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentCountCallableStatement.close();
				
				studentCountCallableStatement = conn
						.prepareCall("{call proc_update_ODS_DAY_MINI_CLASS_CONSUME(?)}");
				studentCountCallableStatement.setString(1, targetDay);
				studentCountCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentCountCallableStatement.close();
				
				studentCountCallableStatement = conn
						.prepareCall("{call proc_update_ODS_DAY_MINI_CLASS_ATTENDS(?)}");
				studentCountCallableStatement.setString(1, targetDay);
				studentCountCallableStatement.execute(); // 调用存储过程
				conn.commit();
				studentCountCallableStatement.close();
			
			}
			
			
			
			
		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (dayFinanceCallableStatement != null) {
				try {
					dayFinanceCallableStatement.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
			
			if (dayCourseConsumeCallableStatement != null) {
				try {
					dayCourseConsumeCallableStatement.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}

			if (session != null) {
				session.close();
			}
		}
		logger.debug("*** DailyOperationCountTask.callProc end");
	}
}
