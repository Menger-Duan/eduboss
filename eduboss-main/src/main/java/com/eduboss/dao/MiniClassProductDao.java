package com.eduboss.dao;

import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassProduct;
import com.eduboss.domain.Product;

public interface MiniClassProductDao extends GenericDAO<MiniClassProduct, String>{

	public void delMiniClassProduct(String miniClassId, String isMainProduct);

	public void saveMiniClassProduct(Product product, MiniClass miniClass, String userId, String isMainProduct);
	
	public MiniClassProduct findMiniClassProductByMiniClassAndProduct(String miniClassId,String productId);
	
}
