package com.eduboss.dao;

import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.IncomeDistributeStatements;
import com.eduboss.dto.DataPackage;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */
public interface IncomeDistributeStatementsDao extends GenericDAO<IncomeDistributeStatements, String> {
    List<IncomeDistributeStatements> getListOfLastIncomeDistributeStatements(FundsChangeHistory fundsChangeHistory);

    DataPackage getListByFundsChangeHistoryId(DataPackage dp, String fundsChangeHistoryId);

    /**
     * 退费的流水记录
     * @param fundsChangeHistory
     * @return
     */
    List<IncomeDistributeStatements> getFeedbackrefundIncome(FundsChangeHistory fundsChangeHistory);
}
