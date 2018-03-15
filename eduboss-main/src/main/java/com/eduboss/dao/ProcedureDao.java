package com.eduboss.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.eduboss.domain.User;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.UserService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.DateTools;

public class ProcedureDao  {
	
private final static Logger log = Logger.getLogger(ProcedureDao.class);
	
	protected HibernateTemplate hibernateTemplate = null;
	
	protected Connection conn = null;
	protected Session session = null;
	protected CallableStatement callableStatement = null;
//	protected int inParamNum = 0;
	
	protected static UserService userService = ApplicationContextUtil.getContext().getBean(UserService.class);
	
	/**
	 * 构造函数
	 * @param hibernateTemplate
	 */
	public ProcedureDao(HibernateTemplate hibernateTemplate) {
		if (null == hibernateTemplate) {
			throw new ApplicationException("入参hibernateTemplate不能为空");
		}
		this.hibernateTemplate = hibernateTemplate;
	}
	
	/**
	 * 执行存储过程
	 * @throws SQLException 
	 */
	public void executeProc(String sql) throws SQLException {
		String userName = "";
		String userId = "";
		User user = userService.getCurrentLoginUser();
		if (user != null) {
			userName=user.getName();
			userId=user.getUserId();
		}
		log.info(userId+"-"+userName+":执行存储过程START--"+sql);
		try {
			createConnection();// 创建连接
			this.callableStatement = createCallableStatement(sql);// 存储过程窗口
			this.callableStatement.execute(); // 调用存储过程
			commit();// 提交
			
		} catch(SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			closeAll();
			log.info(userId+"-"+userName+":执行存储过程END--"+sql);
		}
	}
	
	/**
	 * 执行存储过程
	 */
	public void executeProc(String sql, List<Object> listParam) throws SQLException {
		String userName = "";
		String userId = "";
		User user = userService.getCurrentLoginUser();
		if (user != null) {
			userName=user.getName();
			userId=user.getUserId();
		}
		log.info(userId+"-"+userName+":执行存储过程START--"+sql);
		try {
			createConnection();// 创建连接
			this.callableStatement = createCallableStatement(sql);// 存储过程窗口
			pushInParam(this.callableStatement, listParam);// 设置SQL查询参数
			this.callableStatement.execute(); // 调用存储过程
			commit();// 提交
			
		} catch(SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			closeAll();
			log.info(userId+"-"+userName+":执行存储过程END--"+sql);
		}
	}
	
	/**
	 * 执行存储过程
	 */
	public void executeProcNoLog(String sql, List<Object> listParam) throws SQLException {
		try {
			createConnection();// 创建连接
			this.callableStatement = createCallableStatement(sql);// 存储过程窗口
			pushInParam(this.callableStatement, listParam);// 设置SQL查询参数
			this.callableStatement.execute(); // 调用存储过程
			commit();// 提交
			
		} catch(SQLException e) {
			log.error(e.getMessage(), e);
			throw e;
		} finally {
			closeAll();
		}
	} 
	
	/**
	 * 执行存储过程 - 有返回值
	 */
	public List executeProcHaveOutParam(String sql, List<Object> listParam, List<Integer> listOutParamType) {
		String userName = "";
		String userId = "";
		User user = userService.getCurrentLoginUser();
		if (user != null) {
			userName=user.getName();
			userId=user.getUserId();
		}
		log.info(userId+"-"+userName+":执行存储过程START--"+sql);
		List returnList = null;
		try {
			int inParamNum = listParam.size();
			
			createConnection();// 创建连接
			this.callableStatement = createCallableStatement(sql);// 存储过程窗口
			pushInParam(this.callableStatement, listParam);// 设置SQL查询 - 入参
			pushOutParam(this.callableStatement, listOutParamType, inParamNum);// 设置SQL查询 - 出参
			this.callableStatement.execute(); // 调用存储过程
			commit();// 提交
			returnList = getOutData(this.callableStatement, listOutParamType, inParamNum);
			
		} catch(SQLException e) {
			log.error(e.getMessage(), e);
		} finally {
			closeAll();
			log.info(userId+"-"+userName+":执行存储过程END--"+sql);
		}
		return returnList;
	}
	
	/**
	 * 获取存储过程返回值
	 * @param callableStatement
	 * @param listOutParamType
	 * @param inParamNum
	 * @return
	 */
	protected List getOutData(CallableStatement callableStatement, List<Integer> listOutParamType, int inParamNum) {
		if (null == callableStatement) {
			throw new ApplicationException("获取存储过程输出值的函数入参\"callableStatement\"不能为空！");
		}
		
		List listOutData = new ArrayList<Object>();
		for (int i = 0; i < listOutParamType.size(); i++) {
			int outParamType = listOutParamType.get(i);
			try {
				if (outParamType == java.sql.Types.VARCHAR) {
					listOutData.add(callableStatement.getString(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.INTEGER) {
					listOutData.add(callableStatement.getInt(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.DOUBLE) {
					listOutData.add(callableStatement.getDouble(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.DECIMAL) {
					listOutData.add(callableStatement.getBigDecimal(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.FLOAT) {
					listOutData.add(callableStatement.getFloat(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.CHAR) {
					listOutData.add(callableStatement.getString(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.DATE) {
					listOutData.add(callableStatement.getDate(inParamNum+i+1));
				} else if (outParamType == java.sql.Types.TIMESTAMP) {
					listOutData.add(callableStatement.getTimestamp(inParamNum+i+1));
				} 
				
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
		return listOutData;
	}
	
	/**
	 * 填充SQL查询 - 出参（参数）
	 * @param callableStatement
	 * @param listParam
	 * @param inParamNum		入参个数
	 */
	protected void pushOutParam(CallableStatement callableStatement, List<Integer> listParam, int inParamNum) {
		if (listParam != null && listParam.size() > 0) {
			for (int i = 0; i < listParam.size(); i++) {
				int type = listParam.get(i);
				try {
					callableStatement.registerOutParameter(inParamNum+i+1, type);
				} catch (SQLException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 填充SQL查询 - 入参（参数）
	 * @param listParam
	 */
	protected void pushInParam(CallableStatement callableStatement, List<Object> listParam) {
		if (listParam != null && listParam.size() > 0) {
			for (int i = 0; i < listParam.size(); i++) {
				Object object = listParam.get(i);
				String entityName = object.getClass().getName().toString();
				try {
					if (("java.lang.String").equals(entityName)) {
						callableStatement.setString(i+1, (String) listParam.get(i));
						
					} else if (("java.lang.Integer").equals(entityName)) {
						callableStatement.setInt(i+1, (Integer) listParam.get(i));
						
					} else if (("java.lang.Long").equals(entityName)) {
						callableStatement.setLong(i+1, (Long) listParam.get(i));
						
					} else if (("java.lang.Double").equals(entityName)) {
						callableStatement.setDouble(i+1, (Double) listParam.get(i));
						
					} else if (("java.lang.Float").equals(entityName)) {
						callableStatement.setFloat(i+1, (Float) listParam.get(i));
						
					} else if (("java.lang.Short").equals(entityName)) {
						callableStatement.setShort(i+1, (Short) listParam.get(i));
						
					} else if (("java.util.Date").equals(entityName)) {
						callableStatement.setDate(i+1, (Date) listParam.get(i));
						
					} else if (("java.sql.Timestamp").equals(entityName)) {
						callableStatement.setTimestamp(i+1, (Timestamp) listParam.get(i));
						
					} else {
						callableStatement.setObject(i+1, listParam.get(i));
					}
				} catch(SQLException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
	
	
	
	/**
	 * 提交
	 */
	protected void commit() {
		if (this.conn != null) {
			try {
				this.conn.commit();
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 创建存储过程执行窗口
	 * @param sql
	 * @return
	 */
	CallableStatement createCallableStatement(String sql) throws SQLException {
		if (null == this.conn) {
			createConnection();
		}
		
		CallableStatement callableStatement = null;
		try {
			callableStatement = this.conn.prepareCall(sql);
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
		}
		return callableStatement;
	}
	
	/**
	 * 创建连接
	 * @return
	 * @throws SQLException 
	 */
	protected Connection createConnection() throws SQLException {
		if (null == this.conn) {
			if (null == this.session) {
				createSession();
			}

			this.conn = getSession().connection();
		}
		conn.setAutoCommit(false);
		return conn;
	}
	
	/**
	 * 创建会话
	 * @return
	 */
	protected Session createSession() {
		if (null == this.session) {
			this.session = hibernateTemplate.getSessionFactory().openSession();
		}
		return this.session;
	}
	
	/**
	 * 关闭所有连接、会话、调用存储过程窗口
	 */
	void closeAll() {
		closeCallableStatement();
		closeConnection();
		closeSession();
	}
	
	/**
	 * 关闭会话
	 */
	protected void closeSession() {
		if (this.session != null) {
			this.session.close();
			this.session = null;
		}
	}
	
	/**
	 * 关闭存储过程调用窗口
	 */
	protected void closeCallableStatement() {
		if (this.callableStatement != null) {
			try {
				this.callableStatement.close();
				this.callableStatement = null;
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 关闭连接
	 */
	protected void closeConnection() {
		if (this.conn != null) {
			try {
				this.conn.close();
				this.conn = null;
			} catch (SQLException e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	protected Session getSession() {
		return this.session;
	}
	
	protected String getYesterday() {
		return DateTools.addDateToString( DateTools.getCurrentDate(), -1);
	}
	
	
}
