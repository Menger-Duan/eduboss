/*
 * Copyright (c) 2016 by XuanBang Information Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.eduboss.excel.imported.metadata;

import com.eduboss.dao.StudentDao;
import com.eduboss.excel.InvalidExcelCellDataException;
import com.eduboss.excel.imported.CellParseCtx;
import com.eduboss.excel.imported.anno.CellImportedConverterDefine;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.utils.ApplicationContextUtil;
import com.eduboss.utils.Assert;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author xiangshaoxu 2016年6月8日上午12:47:57
 * @version 1.0.0
 */
public enum ExcelConvertType {

	SQL {
		/**尝试转换sql语句，如果没有记录返回null
		 * @throws InvalidExcelCellDataException */
		@Override
		protected Object doTransfer(CellImportedConverterDefine ann, Object value, Field field) throws InvalidExcelCellDataException {
			String sql = ann.sql();//TODO 增加ctx
			sql = sql.replace("${value}", value.toString());//FIXME if not string, change to prepare statement
			sql = roleQLConfigService.replaceExpression(sql);
			boolean throwIfMoreThanOneReturn = ann.throwIfMoreThanOneReturn();
			
//			RowMapper<Object> extractor = new RowMapper<Object>() {
//
//				@Override
//				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
//
//					return rs.getObject(1);
//				}
//
//			};
			List<Object> list = jdbcTemplate.getCurrentSession().createSQLQuery(sql).list();
			if (list.size() > 1 && throwIfMoreThanOneReturn) {
				String message = String.format("more than one result returned , but not allowed. Filed: %s, Value: %s", field.getName(), value);
				String validateMsg = String.format("\"%s\"数据错误，存在同名记录", ImportedEoUtil.getEoFieldName(field));
				throw new InvalidExcelCellDataException(message, validateMsg);
			}
			if(list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}
		}
	},
	ENUM {
		@Override
		protected Object doTransfer(CellImportedConverterDefine ann, Object value, Field field) throws InvalidExcelCellDataException{
			Class<?> enumClazz = ann.enumClazz();
			try {
				Method method = enumClazz.getMethod("fromLabel", String.class);
				method.setAccessible(true);
				return method.invoke(enumClazz, value);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				Assert.state(false,
						String.format("%s enum define error, can't invoke fromLabel method, field: %s ", ann.getClass().getSimpleName(), field.getName()), e);
				return null; // can't reach here
			}
		}
	};

	StudentDao jdbcTemplate = ApplicationContextUtil.getContext().getBean(StudentDao.class);//FIXME
	RoleQLConfigService roleQLConfigService = ApplicationContextUtil.getContext().getBean(RoleQLConfigService.class);
	
	public Object transfer(CellImportedConverterDefine ann, Object value, Field field, CellParseCtx ctx) throws InvalidExcelCellDataException{
		Object transfered = ctx.getObject(field, value);
		if (transfered == null) {
			transfered = doTransfer(ann, value, field);
			ctx.putObject(field, value, transfered);
		}  
		return transfered;
	}
			
	protected abstract Object doTransfer(CellImportedConverterDefine ann, Object value, Field field) throws InvalidExcelCellDataException;
}
