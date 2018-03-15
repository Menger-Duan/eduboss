package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.MiniClassInventoryDao;
import com.eduboss.domain.MiniClassInventory;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MiniClassInventoryService;

@Service("com.eduboss.service.MiniClassInventoryService")
public class MiniClassInventoryServiceImpl implements MiniClassInventoryService {
	
	@Autowired
	private MiniClassInventoryDao miniClassInventoryDao;

	@Override
	public List<MiniClassInventory> findMiniClassInventoryByMiniClassIds(
			String miniClassIds) {
		return miniClassInventoryDao.getMiniClassInventoryByMiniClassIds(miniClassIds);
	}

	@Override
	public void saveMiniClassInventory(MiniClassInventory inventory) {
		miniClassInventoryDao.save(inventory);
	}

    @Override
    public boolean reduceInventory(String miniClassId, int num, boolean isAllowedExcess) {
        MiniClassInventory miniClassInventory = findMiniClassInventoryByMiniClassId(miniClassId);
        if (miniClassInventory == null) {
            return true;
        }
        if (!isAllowedExcess) {
            if (miniClassInventory.getNormalQuantity() - miniClassInventory.getUsedQuantity() >= num) {
                miniClassInventory.setUsedQuantity(miniClassInventory.getUsedQuantity() + num);
                miniClassInventoryDao.merge(miniClassInventory);
                return true;
            }
        } else {
            if (miniClassInventory.getMaxQuantity() - miniClassInventory.getUsedQuantity() >= num) {
                miniClassInventory.setUsedQuantity(miniClassInventory.getUsedQuantity() + num);
                miniClassInventoryDao.merge(miniClassInventory);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 扣库存，特殊流程，允许库存是负数
     */
    @Override
    public boolean reduceInventoryAllowedExcess(String miniClassId, int num) {
        MiniClassInventory miniClassInventory = findMiniClassInventoryByMiniClassId(miniClassId);
        if (miniClassInventory != null) {
            miniClassInventory.setUsedQuantity(miniClassInventory.getUsedQuantity() + num);
            miniClassInventoryDao.merge(miniClassInventory);
        }
        return true;
    }
    
    @Override
    public boolean revertInventory(String miniClassId) {
        return this.revertInventory(miniClassId, 1);
    }

    @Override
    public boolean revertInventory(String miniClassId, int num) {
        MiniClassInventory miniClassInventory = findMiniClassInventoryByMiniClassId(miniClassId);
        if (miniClassInventory != null && miniClassInventory.getUsedQuantity() > 0) {
            int usedQuantity = miniClassInventory.getUsedQuantity() >= num  ? miniClassInventory.getUsedQuantity() - num : 0;
            miniClassInventory.setUsedQuantity(usedQuantity);
            miniClassInventoryDao.merge(miniClassInventory);
            return true;
        }
        if (miniClassInventory == null) {
            return true;
        }
        return false;
    }

    @Override
    public MiniClassInventory findMiniClassInventoryByMiniClassId(
            String miniClassId) {
        MiniClassInventory miniClassInventory = null;
        List<MiniClassInventory> list = this.findMiniClassInventoryByMiniClassIds(miniClassId);
        if (list != null &&  list.size() > 0) {
            miniClassInventory = list.get(0);
        }
        return miniClassInventory;
    }

    /**
     * 更新库存总数
     */
    @Override
    public void updateInventory(String miniClassId, int maxQuantity, int normalQuantity) {
        MiniClassInventory miniClassInventory = this.findMiniClassInventoryByMiniClassId(miniClassId);
        if (miniClassInventory != null 
                && (miniClassInventory.getMaxQuantity() != maxQuantity 
                || miniClassInventory.getNormalQuantity() != normalQuantity)) {
            int usedQuantity = miniClassInventory.getUsedQuantity();
            if (maxQuantity < usedQuantity) {
                throw new ApplicationException("最大报名人数与允许超额人数之和不能小于:" + usedQuantity);
            }
            miniClassInventory.setMaxQuantity(maxQuantity);
            miniClassInventory.setNormalQuantity(normalQuantity);
            miniClassInventoryDao.merge(miniClassInventory);
        }
    }
    
    

}
