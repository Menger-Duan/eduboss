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

import com.eduboss.controller.GlobalExceptionHandler;
import com.eduboss.service.UserService;

public class RpUserOperationCount implements org.quartz.Job {

	private static final Logger logger = LoggerFactory.getLogger(RpUserOperationCount.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private UserService userService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Schedule job : ##############  RpUserOperationCount  ##############  ,  start : " + (new Date())) ;
		callProc();
		logger.info("Schedule job : ##############  RpUserOperationCount  ##############  ,  end : " + (new Date())) ;
	}

	private void callProc() {
		WebApplicationContext webApplicationContext = ContextLoader
				.getCurrentWebApplicationContext();
		HibernateTemplate hibernateTemplate001 = (HibernateTemplate) webApplicationContext
				.getBean(HibernateTemplate.class);
		Connection conn = null;
		CallableStatement callableStatement = null;
		Session session = hibernateTemplate001.getSessionFactory()
				.openSession();
		try {

			SessionFactory sf = hibernateTemplate001.getSessionFactory();
			DataSource ds = SessionFactoryUtils.getDataSource(sf);
			conn = session.connection();
			callableStatement = conn
					.prepareCall("{call proc_update_COUNT_USER_OPERATION_AllSubject()}");

			callableStatement.execute(); // 调用存储过程
			conn.commit();
			callableStatement.close();

		} catch (HibernateException e) {
			logger.error(e.getMessage(), e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
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
		
	}
	
	public static void main(String[] args) {
		
	}
}
