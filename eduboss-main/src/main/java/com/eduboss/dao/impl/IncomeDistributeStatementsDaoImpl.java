package com.eduboss.dao.impl;

import com.eduboss.dao.IncomeDistributeStatementsDao;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.IncomeDistributeStatements;
import com.eduboss.dto.DataPackage;
import com.google.common.collect.Maps;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/26.
 */
@Transactional
@Repository
public class IncomeDistributeStatementsDaoImpl extends GenericDaoImpl<IncomeDistributeStatements, String> implements IncomeDistributeStatementsDao {
    @Override
    public List<IncomeDistributeStatements> getListOfLastIncomeDistributeStatements(FundsChangeHistory fundsChangeHistory) {
        
		Map<String, Object> params = Maps.newHashMap();
		params.put("fundsChangeId", fundsChangeHistory.getId());
		
    	StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.* FROM ");
        sql.append(" (select * from INCOME_DISTRIBUTE_STATEMENTS where 1=1 and funds_change_id = :fundsChangeId ORDER BY create_time DESC ) AS a ");
        sql.append(" group by a.bonus_type, a.base_bonus_type, a.sub_bonus_type, a.bonus_staff_id, a.organizationId, a.product_type  ");
        List list = super.findBySql(sql.toString(),params);
        return list;
    }

    /**
     * 退费的流水记录
     *
     * @param fundsChangeHistory
     * @return
     */
    @Override
    public List<IncomeDistributeStatements> getFeedbackrefundIncome(FundsChangeHistory fundsChangeHistory) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("fundsChangeId", fundsChangeHistory.getId());
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT a.* FROM ");
        sql.append(" (select * from INCOME_DISTRIBUTE_STATEMENTS where 1=1 and bonus_type='FEEDBACK_REFUND' and funds_change_id = :fundsChangeId ORDER BY create_time DESC ) AS a ");
        sql.append(" group by a.bonus_type, a.base_bonus_type, a.sub_bonus_type, a.bonus_staff_id, a.organizationId, a.product_type  ");
        List list = super.findBySql(sql.toString(),params);
        return list;
    }

    @Override
    public DataPackage getListByFundsChangeHistoryId(DataPackage dp, String fundsChangeHistoryId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("fundsChangeId", fundsChangeHistoryId);
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from INCOME_DISTRIBUTE_STATEMENTS where 1=1 and funds_change_id = :fundsChangeId ORDER BY  create_time DESC   "); //bonus_staff_id ASC ,organizationId ASC,
//        List list = super.findBySql(sql.toString());

        dp = super.findPageBySql(sql.toString(), dp,true,params);
       // dp.setRowCount(findCountSql("select count(*) " + sql.substring(sql.indexOf("from") > 0 ? sql.indexOf("from") : sql.indexOf("FROM"))));
        return dp;
    }
}
