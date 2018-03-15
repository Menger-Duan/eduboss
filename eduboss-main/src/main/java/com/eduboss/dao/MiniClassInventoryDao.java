package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.MiniClassInventory;

public interface MiniClassInventoryDao extends GenericDAO<MiniClassInventory, Integer> {

	List<MiniClassInventory> getMiniClassInventoryByMiniClassIds(String miniClassIds);
	
}
