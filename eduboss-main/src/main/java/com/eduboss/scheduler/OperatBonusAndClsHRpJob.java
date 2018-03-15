package com.eduboss.scheduler;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.eduboss.service.UserOperationLogService;
import com.eduboss.service.UserService;

public class OperatBonusAndClsHRpJob implements org.quartz.Job{
	
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	
	@Autowired
	private UserService userService;

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		test1(); 
	}
	
	
	//ok
	private void test1(){

		 //http://www.mipang.org/blog/292657.883.htm
		 WebApplicationContext webApplicationContext =ContextLoader.getCurrentWebApplicationContext();  
		 
		 
		 HibernateTemplate hibernateTemplate001= (HibernateTemplate) webApplicationContext.getBean(HibernateTemplate.class);
		 
		 Connection  conn=null;
		 CallableStatement callableStatement =null;
		Session  session = hibernateTemplate001.getSessionFactory().openSession();  
        try {
			//CallableStatement cs = session.connection().prepareCall("  { call pr_add(?,?)}");
       	 
       	 //替换方法 SessionFactoryUtils.getDataSource(hibernateTemplate001.getSessionFactory()).getConnection();
       	 
        	
        	SessionFactory sf=  hibernateTemplate001.getSessionFactory();
        	
        	DataSource  ds=SessionFactoryUtils.getDataSource(sf);
        	
        	conn=session.connection();
        	
        
        	
        	
        	callableStatement=conn.prepareCall("{call proc_getContractCntById(?,?)}");
			//CallableStatement callableStatement=session.connection().prepareCall("{call proc_getContractCntById(?,?)}");

			callableStatement.setString(1, "CON0000000024"); //设置输出参数
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR); //设置第二个参数为输出参数
			
			//callableStatement.executeQuery();
			callableStatement.execute(); //调用存储过程
			String  cnt = callableStatement.getString(2);//获取输出参数
			System.out.println("cnt====="+cnt);

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(callableStatement!=null){
				try {
					callableStatement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(session!=null){
				session.close();
			}
		}
		
		
		
		
	
		
	}

}
