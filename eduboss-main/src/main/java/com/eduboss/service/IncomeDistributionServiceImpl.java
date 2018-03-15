package com.eduboss.service;

import com.eduboss.common.BonusDistributeType;
import com.eduboss.common.BonusType;
import com.eduboss.common.ProductType;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.IncomeDistributionDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.*;
import com.eduboss.domainVo.IncomeDistributionVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.jedis.Message;
import com.eduboss.utils.BonusQueueUtils;
import com.eduboss.utils.HibernateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/23.
 */
@Service
public class IncomeDistributionServiceImpl implements IncomeDistributionService {

    @Autowired
    private ContractService contractService;

    @Autowired
    private FundsChangeHistoryDao fundsChangeHistoryDao;

    @Autowired
    private IncomeDistributionDao incomeDistributionDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationDao organizationDao;

    @Override
    public DataPackage getContractBonusVoList(DataPackage dp, IncomeDistribution incomeDistribution, Map params) {
        return null;
    }

    @Override
    public List<IncomeDistribution> getContractBonusByStudentReturnId(String studentReturnId) {
        return null;
    }

    @Override
    public void saveContractBonus(String fundChangeHistoryId, List<IncomeDistribution> incomeDistributions, MultipartFile certificateImageFile) {

    }

    /**
     * 退费详情删除图片
     *
     * @param fundsChangeId
     */
    @Override
    public void deleteImage(String fundsChangeId) {

    }

    /**
     * 修改合同后如果每种产品的实付金额少于已分配的业绩,对应的业绩要删除
     *
     * @param contractId
     */
    @Override
    public void afterEditContractDeleteContractBonus(String contractId) {

    }

    /**
     * @param realChargeMoney
     * @param targetConPrd
     */
    @Override
    public void addIncomeDistributionForLive(BigDecimal realChargeMoney, ContractProduct targetConPrd) {
        Contract contract = targetConPrd.getContract();
        String userId = contract.getSignStaff().getUserId();
        String createUserId = contract.getCreateByStaff().getUserId();
        String blcampus = contract.getBlCampusId();
        List<FundsChangeHistory> fundsChangeHistoryList = fundsChangeHistoryDao.getFundsChangeHistoryListByContractId(contract.getId());
        if (fundsChangeHistoryList.size()==1){
            FundsChangeHistory fundsChangeHistory = fundsChangeHistoryList.get(0);
            List<IncomeDistribution> list = incomeDistributionDao.findByFundsChangeHistoryId(fundsChangeHistory.getId());
            List<IncomeDistributionVo> result = new ArrayList<>();
            if (list.size()>0){
                for (IncomeDistribution i :list){
                    BigDecimal amount = i.getAmount();
                    amount = amount.add(realChargeMoney);
                    i.setAmount(amount);
                    IncomeDistributionVo incomeDistributionVo = HibernateUtils.voObjectMapping(i, IncomeDistributionVo.class);
                    incomeDistributionVo.setId(null);
                    result.add(incomeDistributionVo);
                }
                contractService.saveIncomeDistribution(fundsChangeHistory.getId(), result);
                contractService.addToIncomeDistributeStatements(fundsChangeHistory, result);
            }else {
                List<Message> bonusMsg=new ArrayList<>();
                List<IncomeDistributionVo> incomeDistributionVoList = saveIncomArrangeMoney(realChargeMoney, ProductType.LIVE, fundsChangeHistory, userId, blcampus, bonusMsg, createUserId);
                contractService.addToIncomeDistributeStatements(fundsChangeHistory, incomeDistributionVoList);
                BonusQueueUtils.pushToQueue(bonusMsg);
            }
        }else {
            throw new ApplicationException("直播合同收款记录多于一条或者没有收款记录");
        }

    }

    private List<IncomeDistributionVo> saveIncomArrangeMoney(BigDecimal arrangeMoney, ProductType type, FundsChangeHistory fund, String userId, String blcampus, List<Message> msg, String createUserId) {
        User createUser = userService.findUserById(createUserId);
        IncomeDistribution incomeDistribution = new IncomeDistribution(); //个人业绩  为签单人
        incomeDistribution.setFundsChangeHistory(fund);
        incomeDistribution.setAmount(arrangeMoney);
        incomeDistribution.setBonusStaff(userService.findUserById(userId));
        Organization campus=userService.getBelongCampusByUserId(userId);
        incomeDistribution.setBonusStaffCampus(campus);
        incomeDistribution.setBonusType(BonusType.NORMAL);
        incomeDistribution.setProductType(type);
        incomeDistribution.setBaseBonusDistributeType(BonusDistributeType.USER);
        incomeDistribution.setSubBonusDistributeType(BonusDistributeType.USER_USER);
        incomeDistribution.setCreateUser(createUser);
        if (StringUtils.isNotBlank(blcampus)){
            Organization schoolOrg = organizationDao.findById(blcampus);
            if (schoolOrg != null){
                incomeDistribution.setContractCampus(schoolOrg);
            }
        }

        incomeDistributionDao.save(incomeDistribution);
        BonusQueueUtils.addBonusToMessage(incomeDistribution,msg,"1");

        IncomeDistribution campusIncome = new IncomeDistribution();
        campusIncome.setBaseBonusDistributeType(BonusDistributeType.CAMPUS);
        campusIncome.setSubBonusDistributeType(BonusDistributeType.CAMPUS_CAMPUS);
        campusIncome.setFundsChangeHistory(fund);
        campusIncome.setAmount(arrangeMoney);
        if(StringUtils.isNotBlank(blcampus)){
            Organization schoolOrg = organizationDao.findById(blcampus);
            if(schoolOrg != null){
                campusIncome.setBonusOrg(schoolOrg);
                campusIncome.setContractCampus(schoolOrg);
            }
        }
        campusIncome.setProductType(type);
        campusIncome.setBonusType(BonusType.NORMAL);
        campusIncome.setCreateUser(createUser);
        incomeDistributionDao.save(campusIncome);

        List<IncomeDistribution> list = new ArrayList<>();
        list.add(incomeDistribution);
        list.add(campusIncome);


        return HibernateUtils.voListMapping(list, IncomeDistributionVo.class);
    }
}
