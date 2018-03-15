package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.PosMachineManageDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.PosMachineManage;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
@Repository("PosMachineManageDao")
public class PosMachineManageDaoImpl  extends GenericDaoImpl<PosMachineManage, String> implements PosMachineManageDao {
	
	/**
	 * 查找pos终端列表
	 */
	@Override
	public DataPackage findPagePosMachineManage(DataPackage dp, PosMachineVo posMachineVo, String orgLevel) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT POS_NUMBER, POS_NAME, TYPE, BRENCH_ID, CAMPUS_ID, START_TIME, END_TIME, `STATUS`, CREATE_TIME, CREATE_USER, MODIFY_TIME, MODIFY_USER,POS_TYPE FROM pos_machine  " );
		sql.append(" where 1=1 ") ;
		sql.append(" AND START_TIME <= CURRENT_TIMESTAMP() AND END_TIME >= CURRENT_TIMESTAMP() ");
		
		StringBuffer unionSql = new StringBuffer();
		unionSql.append(" UNION( SELECT POS_NUMBER, POS_NAME, TYPE, NULL, NULL, NULL, NULL, `STATUS`, NULL, NULL, NULL, NULL,POS_TYPE FROM pos_machine_manage where POS_NUMBER NOT IN ( ");
		unionSql.append(" SELECT POS_NUMBER FROM pos_machine WHERE START_TIME <= CURRENT_TIMESTAMP() AND END_TIME >= CURRENT_TIMESTAMP()) ");
		
		boolean isNeedUnion = true;
		
		if (StringUtils.isNotBlank(posMachineVo.getPosNumber())) {
			sql.append(" AND POS_NUMBER LIKE :posNumber ");
			unionSql.append(" AND POS_NUMBER LIKE :posNumber ");
			params.put("posNumber", "%" + posMachineVo.getPosNumber() + "%");
		}
		if (StringUtils.isNotBlank(posMachineVo.getPosName())) {
			sql.append(" AND POS_NAME LIKE :posName ");
			unionSql.append(" AND POS_NAME LIKE :posName ");
			params.put("posName", "%" + posMachineVo.getPosName() + "%");
		}
		if (StringUtils.isNotBlank(posMachineVo.getBrenchId())) {
			isNeedUnion = false;
			sql.append(" AND BRENCH_ID = :brenchId ");
			params.put("brenchId", posMachineVo.getBrenchId());
		}
		if (StringUtils.isNotBlank(posMachineVo.getCampusId())) {
			isNeedUnion = false;
			sql.append(" AND CAMPUS_ID = :campusId ");
			params.put("campusId", posMachineVo.getCampusId());
		}
		if (posMachineVo.getStatus() != null) {
			sql.append(" AND STATUS = :status ");
			unionSql.append(" AND STATUS = :status ");
			params.put("status", posMachineVo.getStatus());
		}
		if (StringUtils.isNotBlank(orgLevel)) {
			sql.append(" AND CAMPUS_ID in (SELECT ID FROM organization where orgLevel like '" + orgLevel + "%')  ");
			unionSql.append(" AND CAMPUS_ID in (SELECT ID FROM organization where orgLevel like '" + orgLevel + "%')  ");
		}

		if (StringUtils.isNotBlank(posMachineVo.getPosTypeId())) {
			sql.append(" AND pos_type='"+posMachineVo.getPosTypeId()+"' ");
			unionSql.append(" AND pos_type='"+posMachineVo.getPosTypeId()+"'  ");
		}
		unionSql.append(" ) "); 
		if (isNeedUnion) {
			sql.append(unionSql);
		}
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql.append(" order by " + dp.getSidx() + ", POS_NUMBER " + dp.getSord());
		} else {
		    sql.append(" order by POS_NUMBER DESC ");
		}
		dp = super.findPageBySql(sql.toString(), dp, false, params);
		dp.setRowCount(super.findCountSql("select count(1) from (" + sql + ") tmp ", params));
		return dp;
	}
	
}
