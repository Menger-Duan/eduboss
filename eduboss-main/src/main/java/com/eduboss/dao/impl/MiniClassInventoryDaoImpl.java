package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.MiniClassInventoryDao;
import com.eduboss.domain.MiniClassInventory;
import com.eduboss.utils.StringUtil;

@Repository("MiniClassInventoryDao")
public class MiniClassInventoryDaoImpl extends GenericDaoImpl<MiniClassInventory, Integer> implements MiniClassInventoryDao {

	@Override
	public List<MiniClassInventory> getMiniClassInventoryByMiniClassIds(
			String miniClassIds) {
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = " select * from mini_class_inventory where mini_class_id in (:miniClassId) ";
		if (StringUtil.isNotBlank(miniClassIds)) {
		    params.put("miniClassId", miniClassIds.split(","));
		}
		return super.findBySql(sql, params);
	}

}
