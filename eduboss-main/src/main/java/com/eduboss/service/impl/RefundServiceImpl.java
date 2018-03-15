package com.eduboss.service.impl;

import com.eduboss.common.BaseStatus;
import com.eduboss.common.ChargeType;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractStatus;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.dao.AccountChargeRecordsDao;
import com.eduboss.dao.FundsChangeHistoryDao;
import com.eduboss.dao.RefundContractDao;
import com.eduboss.dao.RefundContractWfDao;
import com.eduboss.dao.RefundWfConfigDao;
import com.eduboss.dao.WorkFlowConfigDao;
import com.eduboss.domain.*;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.RefundContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractService;
import com.eduboss.service.RefundService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("com.eduboss.service.RefundService")
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundContractDao refundContractDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private RefundWfConfigDao refundWfConfigDao;

    @Autowired
    private RefundContractWfDao refundContractWfDao;

    @Autowired
    private WorkFlowConfigDao workFlowConfigDao;

    @Autowired
    private AccountChargeRecordsDao accountChargeRecordsDao;
    
    @Autowired
    private ChargeService chargeService;
    
    @Autowired
    private FundsChangeHistoryDao fundsChangeHistoryDao;

    @Override
    public void saveNewRefundContract(RefundContract refundContract) {
        // 新增
        if (refundContract.getId() == null) {
            // 生成第一步流程
            WorkFlowConfig firstStep = workFlowConfigDao.getFirstStep("RefundContract");
            refundContract.setWorkFlowConfig(firstStep);
            refundContract.setStatus(ContractStatus.NORMAL);
            refundContract.setUserByCreateUserId(userService.getCurrentLoginUser());
            refundContract.setCreateTime(DateTools.getCurrentDateTime());
            refundContractDao.save(refundContract);
            refundContractDao.flush();

            BigDecimal refundAmount = BigDecimal.ZERO;
            // 生成 退费产品 和 合同关系
            for (RefundContractProduct pro : refundContract.getRefundContractProducts()) {
                pro.setRefundContract(refundContract);
                refundAmount = refundAmount.add(pro.getRefundAmount());
            }
            refundContract.setRefundAmount(refundAmount);
            refundContractDao.save(refundContract);
            refundContractDao.flush();

            // 更改合同产品的状态
            contractProductConvertToRefund(refundContract);

        }

    }


    /**
     * 更新合同产品状态至退费状态
     *
     * @param refundContract 退费合同
     */
    private void contractProductConvertToRefund(RefundContract refundContract) {
        refundContractDao.getHibernateTemplate().refresh(refundContract);
        for (RefundContractProduct refundContractProduct : refundContract.getRefundContractProducts()) {
            ContractProduct contractProduct = refundContractProduct.getContractProduct();
            if (contractProduct.getType() == ProductType.ONE_ON_ONE_COURSE_NORMAL) {
            } else if (contractProduct.getType() == ProductType.ONE_ON_ONE_COURSE_FREE) {
            } else if (contractProduct.getType() == ProductType.SMALL_CLASS) {
            } else if (contractProduct.getType() == ProductType.OTHERS) {
                if(contractProduct.getStatus() == ContractProductStatus.ENDED){
                    throw new ApplicationException("已扣费的其他产品不允许退费");
                }
            }
            if(refundContractProduct.getQuantity() == null){
                refundContractProduct.setQuantity(refundContractProduct.getContractProduct().getQuantity());
            }
            // 更新合同的 消费金额
            chargeService.updateContractTrigger(refundContractProduct.getQuantity().multiply(contractProduct.getPrice()),contractProduct );

        }
    }

    /**
     * 合同产品从退费状态中恢复
     * 退费不同意导致流程终止时调用
     *
     * @param refundContract 退费合同
     */
    private void contractProductRevertFromRefund(RefundContract refundContract) {
        for (RefundContractProduct refundContractProduct : refundContract.getRefundContractProducts()) {
            ContractProduct contractProduct = refundContractProduct.getContractProduct();
            // 返还合同的 消费金额
            if(refundContractProduct.getQuantity() == null){
                refundContractProduct.setQuantity(refundContractProduct.getContractProduct().getQuantity());
            }
            chargeService.updateContractTrigger(refundContractProduct.getQuantity().multiply(contractProduct.getPrice()).negate(),contractProduct );
        }
    }


    /**
     * 退费处理
     *
     * @param refundContract 需要退费的合同
     */
    private void refund(RefundContract refundContract) {
        // 先还原合同产品再进行扣费
        this.contractProductRevertFromRefund(refundContract);
        refundContractDao.flush();
        // 退费流程，消费退费条目，做账
        for (RefundContractProduct refundContractProduct : refundContract.getRefundContractProducts()) {
            ContractProduct contractProduct = refundContractProduct.getContractProduct();
            // 把需要扣费的 课时 消费掉
            AccountChargeRecords record =  new AccountChargeRecords();
            // 记录的是 课时 * 单价 * 折扣
            record.setAmount(refundContractProduct.getQuantity().multiply(contractProduct.getPrice()).multiply(contractProduct.getDealDiscount()));
            record.setContract(contractProduct.getContract());
            record.setCourse(null);
            record.setOperateUser(userService.getCurrentLoginUser());
            record.setStudent(contractProduct.getContract().getStudent());
            record.setProductType(contractProduct.getType());
            record.setChargeType(ChargeType.REFUND_CONSUME);
            record.setPayTime(DateTools.getCurrentDateTime());

            record.setContractProduct(contractProduct);
            record.setQuality(refundContractProduct.getQuantity());

            accountChargeRecordsDao.save(record);
            accountChargeRecordsDao.flush();

            // 更新合同的 消费金额
            chargeService.updateContractTrigger(refundContractProduct.getQuantity().multiply(contractProduct.getPrice()),contractProduct );

            // 增加退费返还 在消费记录里面
            AccountChargeRecords refundConsumeRecord =  new AccountChargeRecords();
            // 退费的 总价
            refundConsumeRecord.setAmount(refundContractProduct.getRefundAmount().negate());
            refundConsumeRecord.setContract(contractProduct.getContract());
            refundConsumeRecord.setCourse(null);
            refundConsumeRecord.setOperateUser(userService.getCurrentLoginUser());
            refundConsumeRecord.setStudent(contractProduct.getContract().getStudent());
            refundConsumeRecord.setProductType(contractProduct.getType());
            refundConsumeRecord.setChargeType(ChargeType.FEEDBACK_REFUND);
            refundConsumeRecord.setPayTime(DateTools.getCurrentDateTime());

            refundConsumeRecord.setContractProduct(contractProduct);
            refundConsumeRecord.setQuality(BigDecimal.ONE);

            accountChargeRecordsDao.save(refundConsumeRecord);
            accountChargeRecordsDao.flush();

            // 增加退费返还 在收费记录里面
            FundsChangeHistory refundIncomeRecord = new FundsChangeHistory();
            refundIncomeRecord.setRemark(null);
            // 退费的 总价
            refundIncomeRecord.setTransactionAmount(refundContractProduct.getRefundAmount().negate());
            refundIncomeRecord.setTransactionTime(DateTools.getCurrentDateTime());
            refundIncomeRecord.setContract(contractProduct.getContract());
            refundIncomeRecord.setChannel(PayWay.FEEDBACK_REFUND);
//        		fundsChangeHistory.setStudent(contract.getStudent());
            refundIncomeRecord.setChargeBy(userService.getCurrentLoginUser());

            fundsChangeHistoryDao.save(refundIncomeRecord);
        }
    }


    @Override
    public List<ContractVo> getPendingRefundContractsByStuId(String stuId) {
        List<Contract> contracts = contractService.getOrderContracts(new Student(stuId), null);
        List<ContractVo> contractVos = HibernateUtils.voListMapping(contracts, ContractVo.class, "withContractProduct");
        return contractVos;
    }

    @Override
    public void approveRefundContract(RefundContractVo refundContractVo) {
        RefundContract refundContract = refundContractDao.findById(refundContractVo.getId());
        // 权限检查，有归属用户则验证用户ID，否则验证权限代码
        WorkFlowConfig current = refundContract.getWorkFlowConfig();
        if (current.getBelongUser() != null) {
            if (!userService.getCurrentLoginUser().getUserId().equals(current.getBelongUser().getUserId())) {
                throw new ApplicationException(ErrorCode.USER_AUTHROTY_FAIL);
            }
        } else {
            if (!userService.isCurrentUserRoleCode(current.getBelongRole())) {
                throw new ApplicationException(ErrorCode.USER_AUTHROTY_FAIL);
            }
        }

        WorkFlowConfig nextNode = null;
        if (refundContractVo.getApprove() == BaseStatus.TRUE) {
            nextNode = workFlowConfigDao.getNextStep(current);
            // 没有后续节点，退费流程成功完成
            if (nextNode == null) {
                refundContract.setStatus(ContractStatus.RETURNS);
                this.refund(refundContract);
            }
        } else {
            // 退费不通过，结束流程（nextNode = null）
            nextNode = null;
            refundContract.setStatus(ContractStatus.NORMAL);
            this.contractProductRevertFromRefund(refundContract);
        }
        // 完成当前节点并归档
        WorkFlowRefundContract history = new WorkFlowRefundContract();
        history.setRefundContract(refundContract);
        history.setWorkFlowConfig(current);
        history.setApprove(refundContractVo.getApprove());
        history.setRemark(refundContractVo.getRemark());
        history.setUser(userService.getCurrentLoginUser());
        history.setTime(DateTools.getCurrentDateTime());
        history.setNextConfig(nextNode);
        refundContract.getWorkFlowRefundContracts().add(history);
        refundContract.setWorkFlowConfig(nextNode);

    }


    @Override
    public DataPackage getPageRefundContracts(DataPackage dp,
                                              RefundContractVo refundContractVo, TimeVo timeVo) {
        dp = refundContractDao.getPageRefundContracts(dp, refundContractVo, timeVo);
        dp.setDatas(HibernateUtils.voListMapping((List<RefundContract>) dp.getDatas(), RefundContractVo.class));
        return dp;
    }


    @Override
    public RefundContractVo getRedundContractById(String refundContractId) {
        RefundContract refundContract = refundContractDao.findById(refundContractId);
        RefundContractVo refundContractVo = HibernateUtils.voObjectMapping(refundContract, RefundContractVo.class);
        return refundContractVo;
    }

}
