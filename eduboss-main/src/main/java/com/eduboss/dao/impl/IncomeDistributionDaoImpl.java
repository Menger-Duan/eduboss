package com.eduboss.dao.impl;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.ProductType;
import com.eduboss.dao.IncomeDistributionDao;
import com.eduboss.domain.IncomeDistribution;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23.
 */

@Transactional
@Repository
public class IncomeDistributionDaoImpl extends GenericDaoImpl<IncomeDistribution, String> implements IncomeDistributionDao {
    /**
     * @param fundsChangeHistoryId
     * @return
     */
    @Override
    public List<IncomeDistribution> findByFundsChangeHistoryId(String fundsChangeHistoryId) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        criterionList.add(Restrictions.eq("fundsChangeHistory.id",fundsChangeHistoryId));
        List<IncomeDistribution> incomeDistributionList = super.findAllByCriteria(criterionList);
        return incomeDistributionList;
    }

    /**
     * @param fundsChangeHistoryId
     */
    @Override
    public void deleteByFundsChangeHistoryId(String fundsChangeHistoryId) {
        List<IncomeDistribution> incomeDistributions = findByFundsChangeHistoryId(fundsChangeHistoryId);
        for (IncomeDistribution incomeDistribution : incomeDistributions){
            super.delete(incomeDistribution);
        }
    }

    /**
     * 根据收款ID和业绩分配类型删除业绩分配
     *
     * @param fundsChangeHistoryId
     * @param type
     */
    @Override
    public void deleteByFundsChangeHistoryId(String fundsChangeHistoryId, String type) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("fundsChangeHistoryId", fundsChangeHistoryId);
        String hql = "delete from IncomeDistribution where fundsChangeHistory.id = :fundsChangeHistoryId ";
        if (type.equals("bonus")) {
            hql += " and baseBonusDistributeType = 'USER'";
            hql += " and amount > 0 ";
        } else {
            hql += " and baseBonusDistributeType = 'CAMPUS' ";
            hql += " and amount > 0 ";
        }
        super.excuteHql(hql,params);
    }

    /**
     * 合同业绩列表
     *
     * @param dp
     * @param incomeDistribution
     * @param params
     * @return
     */
    @Override
    public DataPackage getIncomeDistributionList(DataPackage dp, IncomeDistribution incomeDistribution, Map params) {
    	
		Map<String, Object> param = Maps.newHashMap();
		
        StringBuffer hqlOrg = new StringBuffer();
        StringBuilder hql = new StringBuilder();
        hql.append(" from IncomeDistribution where 1=1");
        if (incomeDistribution.getBonusStaff()!=null && StringUtil.isNotBlank(incomeDistribution.getBonusStaff().getName())){
            hql.append(" and bonusStaff.name like :bonusStaffName ");
            param.put("bonusStaffName", "%"+incomeDistribution.getBonusStaff().getName()+"%");
        }

        if (incomeDistribution.getBonusOrg()!=null && StringUtil.isNotBlank(incomeDistribution.getBonusOrg().getId())){
            hql.append(" and organization.id= :organizationId ");
            param.put("organizationId",incomeDistribution.getBonusOrg().getId() );
        }
        if(StringUtil.isNotBlank(params.get("startDate").toString())){
            hql.append(" and createTime >= :startDate ");
            param.put("startDate",params.get("startDate").toString()+" 0000 " );
        }
        if(StringUtil.isNotBlank(params.get("endDate").toString())){
            hql.append(" and createTime <= :endDate ");
            param.put("endDate",params.get("endDate").toString()+" 2359 " );
        }

        hql.append(" order by createTime desc, id desc ");
        return super.findPageByHQL(hql.toString(), dp,true,param);
    }

    /**
     * 根据退费Id找到责任信息
     *
     * @param studentReturnId
     * @return
     */
    @Override
    public List<IncomeDistribution> findByStudentReturnId(String studentReturnId) {
        List<Criterion> criterionList = new ArrayList<Criterion>();
        criterionList.add(Restrictions.eq("studentReturnFee.id",studentReturnId));
        List<IncomeDistribution> incomeDistributions = super.findAllByCriteria(criterionList);
        return incomeDistributions;
    }

    /**
     * 根据 退费Id 删除提成信息
     *
     * @param StudentReturnId
     */
    @Override
    public void deleteByStudentReturnId(String StudentReturnId) {
        List<IncomeDistribution> incomeDistributions = findByStudentReturnId(StudentReturnId);
        for (IncomeDistribution incomeDistribution : incomeDistributions){
            super.delete(incomeDistribution);
        }
    }

    /**
     * 找业绩分配
     *
     * @param contractId
     * @return
     */
    @Override
    public List<IncomeDistribution> findIncomeDistributionByContractId(String contractId) {
        StringBuilder str=new StringBuilder();
        Map<String, Object> params = Maps.newHashMap();
        str.append(" from IncomeDistribution where 1=1 and bonusType='NORMAL'");
        if(StringUtils.isNotBlank(contractId)){
            str.append(" and fundsChangeHistory.contract.id= :contractId ");
            params.put("contractId", contractId);
        }
        str.append(" order by createTime desc ");

        return this.findAllByHQL(str.toString(),params);
    }

    @Override
    public List<IncomeDistribution> findIncomeExceptThisFund(String contractId, String fundId) {
        StringBuilder str=new StringBuilder();
        Map<String, Object> params = Maps.newHashMap();
        str.append(" from IncomeDistribution where 1=1 and bonusType='NORMAL'");
        if(StringUtils.isNotBlank(contractId)){
            str.append(" and fundsChangeHistory.contract.id= :contractId");
            params.put("contractId", contractId);
        }
        if (StringUtils.isNotBlank(fundId)){
            str.append(" and fundsChangeHistory.id <> :fundId ");
            params.put("fundId", fundId);
        }
        str.append(" order by createTime desc ");

        return this.findAllByHQL(str.toString(),params);
    }

    @Override
    public List<IncomeDistribution> findIncomeByFundsChangeHistoryIdAndType(String fundsChangeHistoryId, BonusDistributeType bonusDistributeType) {
        StringBuilder str=new StringBuilder();
        Map<String, Object> params = Maps.newHashMap();
        str.append(" from IncomeDistribution where 1=1 and bonusType='NORMAL' and baseBonusDistributeType = :bonusDistributeType ");
        params.put("bonusDistributeType", bonusDistributeType);
        if(StringUtils.isNotBlank(fundsChangeHistoryId)){
            str.append(" and fundsChangeHistory.id= :fundsChangeHistoryId ");
            params.put("fundsChangeHistoryId", fundsChangeHistoryId);
        }
        str.append(" order by createTime desc ");

        return this.findAllByHQL(str.toString(),params);
    }

    @Override
    public List<IncomeDistribution> findByContractIdExeptFundId(String contractId, String fundId, String productType) {
        StringBuilder str=new StringBuilder();
        Map<String, Object> params = Maps.newHashMap();
        str.append(" from IncomeDistribution i where 1=1 and bonusType='NORMAL'");
        if (StringUtils.isNotBlank(fundId)){
            str.append(" and fundsChangeHistory.id<> :fundId ");
            params.put("fundId", fundId);
        }

        if (StringUtils.isNotBlank(contractId)){
            str.append(" and fundsChangeHistory.contract.id = :contractId ");
            params.put("contractId", contractId);
        }

        if (StringUtils.isNotBlank(productType)){
            str.append(" and productType= :productType");
            params.put("productType", ProductType.valueOf(productType));
        }

        str.append(" order by createTime desc ");


        return this.findAllByHQL(str.toString(),params);
    }
}
