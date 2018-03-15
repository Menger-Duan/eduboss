package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.eduboss.common.ProductType;
import com.eduboss.dao.ProductCategoryDao;
import com.eduboss.domain.ProductCategory;

@Repository
public class ProductCategoryDaoImpl extends GenericDaoImpl<ProductCategory, String> implements ProductCategoryDao {
	
	
	public String getProductCategoryLevelString(ProductCategory org) {
		String catLevelString = "0001";
		if (StringUtils.isNotBlank(org.getParentId()) && !"-1".equalsIgnoreCase(org.getParentId())
				&& !EnumUtils.isValidEnum(ProductType.class, org.getParentId())) {
			List<ProductCategory> productCategorys = findByCriteria(Expression.eq("parentId", org.getParentId()), Expression.eq("id", org.getId()));
			if (productCategorys.size() > 0 && StringUtils.isNotBlank(productCategorys.get(0).getCatLevel())) {//本来已存在于父节点中，不用修改层次关系
				catLevelString = productCategorys.get(0).getCatLevel();
			} else {//不存在于父节点中，要修改自身层次关系
				ProductCategory parentProductCategory = findById(org.getParentId());
				if (StringUtils.isNotBlank(parentProductCategory.getCatLevel())) {
					catLevelString = getNextProductCategoryCatLevel(parentProductCategory.getId(), parentProductCategory.getCatLevel());
				}
			}
		}else{
			List<ProductCategory> productCategorys = findByCriteria(Expression.eq("parentId", org.getParentId()), Expression.eq("id", org.getId()));
			if (productCategorys.size() > 0 && StringUtils.isNotBlank(productCategorys.get(0).getCatLevel())) {//本来已存在于父节点中，不用修改层次关系
				catLevelString = productCategorys.get(0).getCatLevel();
			} else {
				ProductCategory productCategory = getProductCategoryFirstFloorMax();
				if (productCategory!=null && StringUtils.isNotBlank(productCategory.getCatLevel())) {
					catLevelString = String.valueOf((Integer.valueOf(productCategory.getCatLevel()) + 1));
					catLevelString = "0000".substring(0, 4 - catLevelString.length()) + catLevelString;//不够4位前面补0
				}
			}
		}
		return catLevelString;
	}
	
	public List<ProductCategory>  getProductCategoryFirstFloor(String isEcsProductCategory) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" from ProductCategory where 1=1 and length(catLevel)=4 ");

		if ("isEcs".equals(isEcsProductCategory)){
			hql.append(" and (productType =  '").append(ProductType.ECS_CLASS).append("' ");
			hql.append("  or productType =  '").append(ProductType.OTHERS).append("' )");
		}else if ("isNotEcs".equals(isEcsProductCategory)){
			hql.append(" and productType <> '").append(ProductType.ECS_CLASS).append("' ");
		}
		//#1736 1738  添加合同屏蔽直播
		//hql.append(" and productType <> '").append(ProductType.LIVE).append("' ");
		
		hql.append(" order by catLevel desc");
		List<ProductCategory> list=findAllByHQL(hql.toString(), params) ;
		return list;
	}

	private ProductCategory getProductCategoryFirstFloorMax() {
		String hql="from ProductCategory  where length(catLevel)=4 ";
		hql+=" order by catLevel desc ";
		List<ProductCategory> list=findAllByHQL(hql, new HashMap<String, Object>());
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	private String getNextProductCategoryCatLevel(String parentId, String parentCatLevel) {

		String nextCatLevel = "";
		List<Order> scoreOrderList = new ArrayList<Order>();
		scoreOrderList.add(Order.desc("catLevel"));
		//查找所有同级节点，拿最大的+1
		List<ProductCategory> productCategorys = findByCriteria(scoreOrderList, Expression.eq("parentId", parentId));
		if (productCategorys.size() > 0) {
			String lastLevel = productCategorys.get(0).getCatLevel();
			if (StringUtils.isNotBlank(lastLevel)) {
				nextCatLevel = String.valueOf((Integer.valueOf(lastLevel.substring(lastLevel.length() - 4, lastLevel.length())) + 1));
				nextCatLevel = "0000".substring(0, 4 - nextCatLevel.length()) + nextCatLevel;//不够4位前面补0
			} else {
				nextCatLevel = "0001";
			}
		} else {
			nextCatLevel = "0001";
		}
		
		if (!"-1".equalsIgnoreCase(parentCatLevel)) {
			nextCatLevel = parentCatLevel + nextCatLevel;
		}
		
		return nextCatLevel;
	
	}
	
	public List<ProductCategory> getProductCategoryTree() {
		String hql="from ProductCategory where id <>'LIVE'";
		hql+=" order by length(catLevel),catOrder,createTime";
		return findAllByHQL(hql, new HashMap<String, Object>());
	}
	
	/**
	 * 修改子节点的层级
	 * @param oldCatLevel
	 * @param newCatLevel
	 * @return
	 */
	public int updateChildrenCatLevel(String oldCatLevel,String newCatLevel,ProductType productType){
		Map<String, Object> params = new HashMap<String, Object>();
		String updateProductType="";
		if(productType!=null) {
			updateProductType+=",productType= :productType ";
			params.put("productType", productType);
		}
		String hql="update ProductCategory set catLevel= concat(:newCatLevel,substring(catLevel,"+(oldCatLevel.length()+1)+")) "+updateProductType+" where catLevel != :newCatLevel2 and catLevel like :oldCatLevel ";
		params.put("newCatLevel", newCatLevel);
		params.put("newCatLevel2", newCatLevel);
		params.put("oldCatLevel", oldCatLevel + "%");
		return excuteHql(hql, params);
	}
	
	public String getProductCategoryFullName(String catLevel){
		Map<String, Object> params = new HashMap<String, Object>();
		String categoryFullName="";
		String sql="select `NAME` from PRODUCT_CATEGORY where :catLevel like CONCAT(CAT_LEVEL,'%') ORDER BY LENGTH(CAT_LEVEL) ";
		params.put("catLevel", catLevel);
		List<Map<Object, Object>> list = findMapBySql(sql, params);
		for(Map<Object, Object> map : list)
			categoryFullName+="-"+map.get("NAME");
		if(categoryFullName.length()>0)		
			return categoryFullName.substring(1);
		else
			return "";
	}
}
