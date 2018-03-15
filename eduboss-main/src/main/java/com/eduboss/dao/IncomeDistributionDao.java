package com.eduboss.dao;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.domain.IncomeDistribution;
import com.eduboss.dto.DataPackage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23.
 */
public interface IncomeDistributionDao extends GenericDAO<IncomeDistribution, String> {
    /**
     *
     * @param fundsChangeHistoryId
     * @return
     */
    List<IncomeDistribution> findByFundsChangeHistoryId(String fundsChangeHistoryId);


    /**
     *
     * @param fundsChangeHistoryId
     */
    void deleteByFundsChangeHistoryId(String fundsChangeHistoryId);


    /**
     * 根据收款ID和业绩分配类型删除业绩分配
     * @param fundsChangeHistoryId
     * @param type
     */
    void deleteByFundsChangeHistoryId(String fundsChangeHistoryId, String type);


    /**
     * 合同业绩列表
     * @param dp
     * @param incomeDistribution
     * @param params
     * @return
     */
    DataPackage getIncomeDistributionList(DataPackage dp, IncomeDistribution incomeDistribution, Map params);


    /**
     * 根据退费Id找到责任信息
     * @param studentReturnId
     * @return
     */
    List<IncomeDistribution> findByStudentReturnId(String studentReturnId);


    /**
     * 根据 退费Id 删除提成信息
     * @param StudentReturnId
     */
    void deleteByStudentReturnId(String StudentReturnId);


    /**
     * 找业绩分配
     * @param contractId
     * @return
     */
    List<IncomeDistribution> findIncomeDistributionByContractId(String contractId);


    List<IncomeDistribution> findIncomeExceptThisFund(String contractId, String fundId);

    List<IncomeDistribution> findIncomeByFundsChangeHistoryIdAndType(String fundsChangeHistoryId, BonusDistributeType bonusDistributeType);


    List<IncomeDistribution> findByContractIdExeptFundId(String contractId,String fundId,String productType);
}
