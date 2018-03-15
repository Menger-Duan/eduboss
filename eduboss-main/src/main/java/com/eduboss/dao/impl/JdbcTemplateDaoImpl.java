package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 * 2016-07-25
 *
 */
@Repository
public class JdbcTemplateDaoImpl implements JdbcTemplateDao {
	
	/*@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		return jdbcTemplate.queryForList(sql);
	}
	
	@Override
	public <T> T queryForObject(String sql, Class<T> requiredType) {
		return jdbcTemplate.queryForObject(sql, requiredType);
	}
	
	@Override
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> t) {
		sql = " select * from "  + sql.substring(sql.indexOf("from") + 4);
		return jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(t));
	}
	
	@Override
	public <T> DataPackage queryPage(String sql, Class<T> t, DataPackage dp, boolean useCount) {
		int pageNo = dp.getPageNo();
		int pageSize = dp.getPageSize();
		int start = pageNo*pageSize;
//		int limit = pageNo*pageSize + pageSize;
		int limit = pageSize;
		Object[] args = new Object[] {}; 
		List<T> list = null;
		if (useCount) {
			StringBuilder countSql = new StringBuilder();
			int index1 = sql.indexOf("from");
			int index2 = sql.indexOf("FROM");
			int startIndex = index1 <= index2 ? index1 : index2;
			if (startIndex < 0) {
				startIndex = index1 > index2 ? index1 : index2;
			}
			countSql.append("select count(*) ");
			countSql.append(sql.substring(startIndex));
			int resultCount = this.findCountSql(countSql.toString());
			dp.setRowCount(resultCount);
		}
		if (start <= 0 && limit <= 0) { 
			list = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(t));
		}  else {
			if (start <= 1) { 
				sql = getLimitString(sql, false); 
				args = ArrayUtils.add(args, args.length, limit); 
			} else { 
				sql = getLimitString(sql, true); 
				args = ArrayUtils.add(args, args.length, start); 
				args = ArrayUtils.add(args, args.length, limit); 
			} 
			System.out.println("paging sql : \n" + sql); 
			list = jdbcTemplate.query(sql, args, new BeanPropertyRowMapper<T>(t));
		}
		dp.setDatas(list);
		return dp;
	}
	
	@Override
	public List<Map<String, Object>> queryPage(String sql, DataPackage dp) { 
		int pageNo = dp.getPageNo();
		int pageSize = dp.getPageSize();
		return this.queryPage(sql, pageNo*pageSize, pageNo*pageSize + pageSize); 
	}  
	
	@Override
	public List<Map<String, Object>> queryPage(String sql, int start, int limit) { 
		Object[] args = new Object[] {}; 
		return this.queryPage(sql, args, start, limit); 
	} 
	
	@Override
	public List<Map<String, Object>> queryPage(String sql, Object[] args, 
		int start, int limit) { 
		if (start <= 0 && limit <= 0) { 
			return (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, args); 
		} 
		if (start <= 1) { 
			sql = getLimitString(sql, false); 
			args = ArrayUtils.add(args, args.length, limit); 
		} else { 
			sql = getLimitString(sql, true); 
			args = ArrayUtils.add(args, args.length, start); 
			args = ArrayUtils.add(args, args.length, limit); 
		} 

		System.out.println("paging sql : \n" + sql); 
		return (List<Map<String, Object>>) jdbcTemplate.queryForList(sql, args); 
	} 
	
	@Override
	public Integer findCountSql(String sql) {
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	
	@Override
	public BigDecimal sumBySql(String sql) {
		return jdbcTemplate.queryForObject(sql, BigDecimal.class);
	}
	
	private String getLimitString(String sql, boolean hasOffset) { 
		sql = sql.trim(); 
		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 ); 
		pagingSelect.append(sql); 
		if (hasOffset) { 
			pagingSelect.append(" limit ?, ?"); 
		} else { 
			pagingSelect.append(" limit ?"); 
		} 

		return pagingSelect.toString(); 
	} */
	
}
