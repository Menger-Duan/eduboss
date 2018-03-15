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

public class TestCountTask implements org.quartz.Job {
	
	private static final Logger logger = LoggerFactory.getLogger(TestCountTask.class);
	
	@Autowired
	private HibernateTemplate hibernateTemplate;

	@Autowired
	private UserService userService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Schedule job : ############## " +  this.getClass().getSimpleName()  +  " ##############  ,  start : " + (new Date())) ;
		// callProc();
		logger.info("Schedule job : ############## " +  this.getClass().getSimpleName()  +  " ##############  ,  end : " + (new Date())) ;
		
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
					.prepareCall("{call proc_countOfReceptionistInputRecord()}");

			callableStatement.execute(); // 调用存储过程
			conn.commit();
			callableStatement.close();

		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (session != null) {
				session.close();
			}
		}

	}
}
