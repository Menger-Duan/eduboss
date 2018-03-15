package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 * 2016-07-25
 *
 */
@Repository
public interface JdbcTemplateDao {

	/*public List<Map<String, Object>> queryForList(String sql);
	
	public <T> T queryForObject(String sql, Class<T> requiredType);
	
	public <T> List<T> queryForList(String sql, Object[] args, Class<T> t);
	
	public <T> DataPackage queryPage(String sql, Class<T> t, DataPackage dp, boolean useCount);
	
	public List<Map<String, Object>> queryPage(String sql, DataPackage dp);
	
	public List<Map<String, Object>> queryPage(String sql, int start, int limit);
	
	public List<Map<String, Object>> queryPage(String sql, Object[] args, int start, int limit);
	
	public Integer findCountSql(String sql);
	
	public BigDecimal sumBySql(String sql);*/
	
}
