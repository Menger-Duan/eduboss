package com.eduboss.service;

import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.IncomeDistribution;
import com.eduboss.dto.DataPackage;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23.
 */
public interface IncomeDistributionService {
    DataPackage getContractBonusVoList(DataPackage dp, IncomeDistribution incomeDistribution, Map params);

    List<IncomeDistribution> getContractBonusByStudentReturnId(String studentReturnId);

    void saveContractBonus(String fundChangeHistoryId, List<IncomeDistribution> incomeDistributions, MultipartFile certificateImageFile);

    /**
     * 退费详情删除图片
     * @param fundsChangeId
     */
    void  deleteImage(String fundsChangeId);


    /**
     * 修改合同后如果每种产品的实付金额少于已分配的业绩,对应的业绩要删除
     * @param contractId
     */
    void afterEditContractDeleteContractBonus(String contractId);

    /**
     *
     * @param realChargeMoney
     * @param targetConPrd
     */
    void addIncomeDistributionForLive(BigDecimal realChargeMoney, ContractProduct targetConPrd);
}
