package com.eduboss.dto;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.eduboss.dao.ProcedureDao;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

/**
 * 主键生成策略
 * @author ndd
 * 2014-7-30
 */
public class DispNoGenerator implements IdentifierGenerator, Configurable {
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	private static final Log log = LogFactory.getLog(DispNoGenerator.class);

	private String sql = null;
	
	/**
	 * 数据库表名
	 */
	private String table = null;
	
	/**
	 * id前缀
	 */
	private String idPrefix = null;
	
	/**
	 * 是否查新id前缀
	 */
	private boolean isSelectIdPrefix = false;

	private Connection connection;


	public  String generate(String table, Session session){
		connection = session.connection();
		this.table = table;
		String strGenerateId = "";
		try {
			long t_SeqNo = this.getSeqValue();
			if (StringUtil.isNotEmpty(idPrefix)) {
				strGenerateId = idPrefix.toUpperCase();
			} else {
				strGenerateId = table.toUpperCase().substring(0, 3);
			}
			if ("student".equals(table.toLowerCase()) || "refund_workflow".equals(table.toLowerCase())) {
				strGenerateId += t_SeqNo;
			}
			else {
				strGenerateId += StringUtil.numberPadding0(t_SeqNo, 12);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return strGenerateId;
	}
	
	/**
	 * 获取主键id
	 * id=取表名 前3位+10数字
	 * student表 id=STU+时间（yyMMdd）+4位数字
	 */
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		connection = session.connection();
		// 自己客户化ID
		String strGenerateId = "";
		try {
			long t_SeqNo = this.getSeqValue();
			if (StringUtil.isNotEmpty(idPrefix)) {
				strGenerateId = idPrefix.toUpperCase();
			} else {
				strGenerateId = table.toUpperCase().substring(0, 3);
			}
			if ("student".equals(table.toLowerCase()) || "refund_workflow".equals(table.toLowerCase())) {
				strGenerateId += t_SeqNo;
			} 
			else {
				strGenerateId += StringUtil.numberPadding0(t_SeqNo, 12);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return strGenerateId;
	}

	public void configure(Type type, Properties params, Dialect d) throws MappingException {
		// 取出table参数
		table = params.getProperty("table");

		if (table == null) {
			table = params.getProperty(PersistentIdentifierGenerator.TABLE);
		}

		// //取出column参数
		// String column = params.getProperty("column");
		//
		// if (column == null) {
		// column = params.getProperty(PersistentIdentifierGenerator.PK);
		// }
		//
		// //表的sehcma参数
		// String schema =
		// params.getProperty(PersistentIdentifierGenerator.SCHEMA);
		//
		// returnClass = type.getReturnedClass();
		//
		// //取出step参数
		// String stepvalue = params.getProperty("step");
		//
		// if ((null != stepvalue) && !"".equals(stepvalue.trim())) {
		// try {
		// //step = Integer.parseInt(stepvalue);
		// } catch (Exception e) {
		// log.error(e);
		// }
		// }
		//
		// //构造存储在Map中的索引值的key name
		// key = table + "_$_" + column;

		// 根据参数构造取最大值的SQL
		sql = "select nextval('" + table + "')";
	}

	/**
	 * 取指定SEQUENCE_TABLE的值，不存在插入一条记录
	 * 
	 * @return Sequence最大值
	 * @throws Exception
	 *             if sql error occurs.
	 */
	private long getSeqValue() throws Exception {
		long maxvalue = 0;
		
		CallableStatement  cs = connection.prepareCall("{call proc_update_SEQUENCE_TABLE(?,?,?)}");
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			/*rs = st.executeQuery();
			if (rs.next()) {
				maxvalue = rs.getLong(1);
			}
			String currentDate = DateTools.getCurrentDate("yyMMdd");
			if (maxvalue == 0) {
				maxvalue = 1;
				if ("student".equals(table.toLowerCase())) {
					maxvalue = Long.valueOf(currentDate + "0001");
				}
				st = connection.prepareStatement("insert into sequence_table(seq_name,current_val,increment_val) values('" + table + "'," + maxvalue + ",1)");
				st.execute();
			} else if ("student".equals(table.toLowerCase()) && !currentDate.equals((maxvalue + "").substring(0, 6))) {
				maxvalue = Long.valueOf(currentDate + "0001");
				st = connection.prepareStatement(" update sequence_table set current_val=" + maxvalue + " where seq_name='" + table + "' ");
				st.execute();
			}*/
			
			String currentDate = DateTools.getCurrentDate("yyMMdd");
			log.info("########## getNextValue ########## " + "begin" );
			cs.setString(1, table);
			cs.setString(2, currentDate);
			cs.setLong(3, java.sql.Types.BIGINT);
			cs.execute();
			maxvalue = cs.getLong(3);
			log.info("########## getNextValue ########## " + "end" );
			
			if(!isSelectIdPrefix){
				st = connection.prepareStatement(" select id_prefix from sequence_table where seq_name=  '" + table + "'");
				rs = st.executeQuery();
				if (rs.next()) {
					idPrefix = rs.getString(1);
				}
				isSelectIdPrefix = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();

			if (st != null)
				st.close();
		}
		return maxvalue;
	}

}
