package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.PosMachineStatus;
import com.eduboss.dao.PosMachineDao;
import com.eduboss.domain.PosMachine;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.StringUtil;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("PosMachineDao")
public class PosMachineDaoImpl  extends GenericDaoImpl<PosMachine, Integer> implements PosMachineDao {

	/**
	 * 查找pos终端使用日期列表
	 */
	@Override
	public DataPackage findPagePosMachine(DataPackage dp, String posNumber) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from PosMachine  " );
		hql.append(" where 1=1 ") ;
		if (StringUtils.isNotBlank(posNumber)) {
			hql.append(" AND posNumber = :posNumber ");
			params.put("posNumber", posNumber);
		}
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by " + dp.getSidx() + " " + dp.getSord());
		}
		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	@Override
	public int findPosMachineConflictCount(PosMachine posMachine) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql =new StringBuffer();
		sql.append(" select count(distinct ID) from pos_machine pm where 1=1 ");
		sql.append(" and ((:startTime between start_time and end_time  and :startTime2 <> end_time ");
		sql.append("        or :endTime between start_time and end_time  and :endTime2 <> start_time )");
		sql.append(" or  ( start_time BETWEEN :startTime3 and :endTime3 ");
		sql.append(" and start_time <> :endTime4  ");
		sql.append("  and end_time BETWEEN :startTime4 and :endTime5 ");
		sql.append(" and end_time <> :endTime6 )) ");
		params.put("startTime", posMachine.getStartTime());
		params.put("startTime2", posMachine.getStartTime());
		params.put("endTime", posMachine.getEndTime());
		params.put("endTime2", posMachine.getEndTime());
		params.put("startTime3", posMachine.getStartTime());
		params.put("endTime3", posMachine.getEndTime());
		params.put("endTime4", posMachine.getEndTime());
		params.put("startTime4", posMachine.getStartTime());
		params.put("endTime5", posMachine.getEndTime());
		params.put("endTime6", posMachine.getEndTime());
		if (posMachine.getPosNumber() != null) {
			sql.append(" AND POS_NUMBER = :posNumber ");
			params.put("posNumber", posMachine.getPosNumber());
		}
		if (posMachine.getId() > 0) {
			sql.append(" AND id != :id ");
			params.put("id", posMachine.getId());
		}
		int retCount = this.findCountSql(sql.toString(), params);
		
		return retCount;
	}
	
	/**
	 * 禁用，激活终端的使用日期
	 * @param posNumber
	 * @param status
	 */
	@Override
	public void chagePosMachineByPosNumberStatus(String posNumber, PosMachineStatus status) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "update pos_machine set STATUS = :status where 1=1 AND POS_NUMBER = :posNumber ";
		params.put("status", status);
		params.put("posNumber", posNumber);
		super.excuteSql(sql, params);
	}
	
	/**
	 * 根据校区查找pusNumber的下拉框
	 */
	@Override
	public List<PosMachine> findPosMachineListByCampusId(String campusId, String typeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from PosMachine where campus.id = :campusId and status = 'ACTIVATE' and startTime <= CURRENT_TIMESTAMP() and endTime >= CURRENT_TIMESTAMP() ";
		params.put("campusId", campusId);
		if (StringUtil.isNotBlank(typeId)) {
		    hql += " and type.id = :typeId ";
		    params.put("typeId", typeId);
		}
		return super.findAllByHQL(hql, params);
	}
}
