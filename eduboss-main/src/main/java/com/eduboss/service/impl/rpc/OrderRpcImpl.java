package com.eduboss.service.impl.rpc;

import java.math.BigDecimal;
import java.util.List;

import org.boss.rpc.base.dto.OrderSurrenderRpcVo;
import org.boss.rpc.eduboss.service.OrderRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.ProductType;
import com.eduboss.domain.ContractProduct;
import com.eduboss.service.ContractService;
import com.eduboss.service.MiniClassInventoryService;

@Service("orderRpcImpl")
public class OrderRpcImpl implements OrderRpc {
    
    @Autowired
    private MiniClassInventoryService miniClassInventoryService;
    
    @Autowired
    private ContractService contractService;

    @Override
    public boolean reduceInventory(String miniClassId) {
        return miniClassInventoryService.reduceInventory(miniClassId, 1, false);
    }
    
    /**
     * 扣库存，特殊流程，允许库存是负数
     */
    @Override
    public boolean reduceInventoryAllowedExcess(String miniClassId) {
        return miniClassInventoryService.reduceInventoryAllowedExcess(miniClassId, 1);
    }

    @Override
    public boolean revertInventory(String miniClassId) {
        return miniClassInventoryService.revertInventory(miniClassId);
    }
    
    @Override
    public boolean revertInventory(String miniClassId, int num) {
        return miniClassInventoryService.revertInventory(miniClassId, num);
    }

    @Override
    public OrderSurrenderRpcVo findOrderSurrenderInfo(String orderNo) {
        OrderSurrenderRpcVo result = null;
        List<ContractProduct> list = contractService.listContractProductByAssocRelatedNo(orderNo);
        if (list != null && !list.isEmpty()) {
            result = new OrderSurrenderRpcVo();
            result.setOrderNo(orderNo);
            BigDecimal remainAmount = BigDecimal.ZERO;
            if (list.size() > 1) {
                boolean isNotCharged = false;
                for (ContractProduct cp : list) {
                    if (cp.getType() == ProductType.SMALL_CLASS && 
                            cp.getPaidAmount().compareTo(cp.getRemainingAmountOfBasicAmount()) == 0) {
                        isNotCharged = true;
                        break;
                    }
                }
                for (ContractProduct cp : list) {
                    if (cp.getType() == ProductType.SMALL_CLASS) {
                        remainAmount = remainAmount.add(cp.getRemainingAmountOfBasicAmount());
                    }
                    if (cp.getType() == ProductType.OTHERS && isNotCharged) {
                        remainAmount = remainAmount.add(cp.getPaidAmount());
                    }
                }
            } else {
                remainAmount = list.get(0).getRemainingAmountOfBasicAmount();
            }
            result.setRemainAmount(remainAmount);
        }
        return result;
    }

}
