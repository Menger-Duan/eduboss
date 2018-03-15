package com.eduboss.service.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.boss.rpc.base.dto.OrderDetailRpcVo;
import org.boss.rpc.base.dto.ProductRpcVo;
import org.boss.rpc.mobile.service.BossOrderRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.BusinessType;
import com.eduboss.common.ContractProductStatus;
import com.eduboss.common.ContractType;
import com.eduboss.common.DataDictCategory;
import com.eduboss.common.FundsChangeAuditStatus;
import com.eduboss.common.PayWay;
import com.eduboss.common.ProductType;
import com.eduboss.common.StudentType;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.BusinessAssocMapping;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.FundsChangeHistory;
import com.eduboss.domain.MoneyWashRecords;
import com.eduboss.domain.Student;
import com.eduboss.domain.StudentSchool;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.CustomerVo;
import com.eduboss.domainVo.FundsChangeHistoryVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.BusinessAssocMappingService;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractService;
import com.eduboss.service.CustomerService;
import com.eduboss.service.DataDictService;
import com.eduboss.service.OrderService;
import com.eduboss.service.ProductService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.StudentSchoolService;
import com.eduboss.service.StudentService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;

@Service
public class OrderServiceImpl implements OrderService {
    
    private final static Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Resource(name = "bossOrderRpc")
    private BossOrderRpc bossOrderRpc;
    
    @Autowired
    private ContractService contractService;
    
    @Autowired
    private SmallClassService smallClassService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private StudentSchoolService studentSchoolService;
    
    @Autowired
    private BusinessAssocMappingService businessAssocMappingService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ChargeService chargeService;
    
    @Autowired
    private DataDictService dataDictService;
    
    @Override
    public boolean storageOrder(String orderNo) {
        OrderDetailRpcVo order = bossOrderRpc.findOrderDetailRpcByOrderNo(orderNo);
        if (order == null) {
            throw new ApplicationException("找不到订单:" + orderNo);
        }
        logger.info("OrderDetailRpcVo:" + order.toString());
        MiniClassVo miniClass = smallClassService.findMiniClassById(order.getRelateClassNo());
        List<ProductRpcVo> products = order.getProducts();
        if (products != null && !products.isEmpty()) {
            for (ProductRpcVo product : products) { // 资料费产品被删除，则订单不入库
                if (productService.findProductById(product.getId()) == null) {
                    logger.info("storageOrder error product delete id:" + product.getId());
                    return false;
                }
            }
        }
        if (miniClass != null) {
            ContractVo contractVo = this.buildContractVo(order, miniClass);
            String contractId = "";
            try {
                // 创建合同
                contractId = contractService.saveFullContractInfoNew(contractVo, false);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                throw new ApplicationException(e.getMessage());
            }
            ContractVo contract = contractService.getContractById(contractId);
            Student student = studentService.getStduentById(contract.getStuId());
            student.setRelatedStudentNo(order.getStudentNo());
            studentService.saveOrUpdateStudent(student);
            
            // 合同与订单关联记录
            BusinessAssocMapping contractAssocMapping = new BusinessAssocMapping();
            contractAssocMapping.setBusinessId(contractId);
            contractAssocMapping.setBusinessType(BusinessType.CONTRACT);
            contractAssocMapping.setRelateNo(orderNo);
            businessAssocMappingService.saveBusinessAssocMapping(contractAssocMapping);
            
            FundsChangeHistoryVo fundsVo = new FundsChangeHistoryVo();
            fundsVo.setContractId(contractId);
            fundsVo.setPOSid(order.getTradeNo());
            fundsVo.setTransactionAmount(order.getPayment());
            fundsVo.setAuditStatusValue(FundsChangeAuditStatus.VALIDATE.getValue());
            fundsVo.setChannel(PayWay.WEB_CHART_PAY);
            fundsVo.setPosReceiptDate(DateTools.getCurrentDate());
            
            // 收款
            FundsChangeHistory fundsChangeHistory = contractService.saveFundOfContract(fundsVo);
            
            //收款与订单关联记录
            BusinessAssocMapping fundsAssocMapping = new BusinessAssocMapping();
            fundsAssocMapping.setBusinessId(fundsChangeHistory.getId());
            fundsAssocMapping.setBusinessType(BusinessType.FUNDS);
            fundsAssocMapping.setRelateNo(orderNo);
            businessAssocMappingService.saveBusinessAssocMapping(fundsAssocMapping);
            return true;
        } else {
            logger.info("storageOrder error miniClass delete id:" + order.getRelateClassNo());
        }
        return false;
        
    }
    
    @Override
    public void surrenderOrder(String orderNo) {
        List<ContractProduct> list = contractService.listContractProductByAssocRelatedNo(orderNo);
        if (list != null && !list.isEmpty()) {
            // 判断是不是全额退款
            boolean isAllReturn = false;
            for (ContractProduct cp : list) {
                if (cp.getType() == ProductType.SMALL_CLASS 
                        && cp.getPaidAmount().compareTo(cp.getRemainingAmountOfBasicAmount()) == 0) {
                    isAllReturn = true;
                    break;
                }
            }
            for (ContractProduct cp : list) {
                // 全额退款的其他类型的合同产品若扣费了则冲销后，再退费
                if (isAllReturn && cp.getType() == ProductType.OTHERS) {
                    if (cp.getStatus() == ContractProductStatus.ENDED) {
                        List<AccountChargeRecords> records = chargeService.findAccountRecordsByContractProduct(cp.getId(), "FALSE");
                        if (records != null && !records.isEmpty()) {
                            String transactionId = records.get(0).getTransactionId();
                            MoneyWashRecords moneyWashRecords = new MoneyWashRecords();
                            moneyWashRecords.setTransactionId(transactionId);
                            moneyWashRecords.setDetailReason("在线报读全额退款，冲销关联的其他产品");
                            DataDict cause = dataDictService.findDataDictByNameAndCateGory("在线报读退费", DataDictCategory.WASH_CAUSE.getValue());
                            moneyWashRecords.setCause(cause);
                            chargeService.washCharge(moneyWashRecords);
                        }
                        this.closeContractProcuct(cp);
                    } else if (cp.getStatus() == ContractProductStatus.NORMAL) {
                        this.closeContractProcuct(cp);
                    }
                }
                if (cp.getType() == ProductType.SMALL_CLASS && cp.getStatus() == ContractProductStatus.NORMAL) {
                    this.closeContractProcuct(cp);
                }
            }
        } else {
            logger.error("在线报读退费，找不到关联的合同产品");
        }
    }
    
    // 发起退费
    private void closeContractProcuct(ContractProduct cp) {
        try {
            contractService.closeContractProcuct(cp.getRemainingAmountOfPromotionAmount(), cp.getRemainingAmountOfPromotionAmount(), 
                    cp.getId(), true, cp.getRemainingAmountOfBasicAmount(), BigDecimal.ZERO,
                    "在线报读退费", null, null, "微信退费", null,
                    cp.getContract().getBlCampusId(), "112233", null, null, null);
        } catch (Exception e) {
            logger.info("报读退费出错" + e.getMessage() + cp.getId());
            e.printStackTrace();
        }
    }
    
    private ContractVo buildContractVo(OrderDetailRpcVo order, MiniClassVo miniClass) {
        
        ContractVo contractVo = new ContractVo();
        contractVo.setBlCampusId(miniClass.getBlCampusId());
        contractVo.setContractGradeId(order.getStudentGradeNo());
        contractVo.setContractType(ContractType.NEW_CONTRACT);
        CustomerVo customer = customerService.findCustomerByPhone(order.getCustomerPhone());
        if (customer != null) {
            contractVo.setCusId(customer.getId());
        } else {
            contractVo.setCusName(order.getCustomerName());
            contractVo.setCusPhone(order.getCustomerPhone());
        }
        contractVo.setEcsContract(false);
        contractVo.setGradeId(order.getStudentGradeNo());
//        contractVo.setLive(false);
//        contractVo.setPro(true);
        if (StringUtil.isNotBlank(order.getSchoolNo())) {
            StudentSchool school = studentSchoolService.findSchoolByGlobalNumber(order.getSchoolNo());
            if (school != null) {
                contractVo.setSchoolId(school.getId());
            }
        }
        contractVo.setSchoolOrTemp("school");
        contractVo.setSignByWho("112233");
        contractVo.setSignTime(DateTools.getCurrentDate());
        
        Student student = studentService.findStudentByRelateNo(order.getStudentNo());
        if (student != null) {
            contractVo.setStuId(student.getId());
        } else {
            contractVo.setStuName(order.getStudentName());
            contractVo.setStuType(StudentType.ENROLLED.getValue());
        }
        
        Set<ContractProductVo> contractProductVos = new HashSet<ContractProductVo>();
        ContractProductVo contractProductVo = new ContractProductVo();
        contractProductVo.setFirstSchoolTime(miniClass.getStartDate());
        contractProductVo.setMiniClassId(miniClass.getMiniClassId());
        contractProductVo.setPayment(order.getPrice() * order.getNum());
        contractProductVo.setPrice(order.getPrice());
        contractProductVo.setProductId(miniClass.getProductId());
        contractProductVo.setProductType(ProductType.SMALL_CLASS);
        contractProductVo.setQuantity(miniClass.getTotalClassHours());
        contractProductVo.setHashFlag("1");
        contractProductVos.add(contractProductVo);
        
        // TODO 资料费产品被删除应该怎么处理
        // 资料费产品
        List<ProductRpcVo> products = order.getProducts();
        if (products != null && !products.isEmpty()) {
            for (ProductRpcVo product : products) {
                ContractProductVo otherCp = new ContractProductVo();
                otherCp.setHashFlag("0");
                otherCp.setProductId(product.getId());
                otherCp.setProductType(ProductType.OTHERS);
                otherCp.setQuantity(Double.valueOf(product.getNum()));
                otherCp.setPrice(product.getPrice().doubleValue());
                otherCp.setPayment((product.getPrice().multiply(new BigDecimal(product.getNum()))).doubleValue());
                contractProductVos.add(otherCp);
            }
        }
        
        contractVo.setContractProductVos(contractProductVos);
        return contractVo;
    }

}
