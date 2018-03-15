package com.eduboss.dao;

import java.util.List;

import com.eduboss.common.ProductType;
import com.eduboss.domain.ProductCategory;

public interface ProductCategoryDao extends GenericDAO<ProductCategory, String>{
	public List<ProductCategory> getProductCategoryTree();
	public String getProductCategoryLevelString(ProductCategory org);
	
	public List<ProductCategory>  getProductCategoryFirstFloor(String isEcsProductCategory);
	
	/**
	 * 修改子节点的层级
	 * @param oldCatLevel
	 * @param newCatLevel
	 * @return
	 */
	public int updateChildrenCatLevel(String oldCatLevel,String newCatLevel,ProductType productType);
	
	public String getProductCategoryFullName(String catLevel);
}
