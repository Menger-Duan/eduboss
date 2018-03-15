package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.MiniClassProductDao;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassProduct;
import com.eduboss.domain.Product;
import com.eduboss.utils.DateTools;
import com.google.common.collect.Maps;

@Repository("MiniClassProductDao")
public class MiniClassProductDaoImpl extends GenericDaoImpl<MiniClassProduct, String> implements MiniClassProductDao {

	@Override
	public void delMiniClassProduct(String miniClassId, String isMainProduct) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassId);
		params.put("isMainProduct", isMainProduct);
		String hql=" delete MiniClassProduct where miniClass.id= :miniClassId and isMainProduct= :isMainProduct ";
		super.excuteHql(hql,params);
	}

	@Override
	public void saveMiniClassProduct(Product product, MiniClass miniClass, String userId, String isMainProduct) {
		MiniClassProduct miniClassProduct = null;
		if ("1".equals(isMainProduct)) {
			List<MiniClassProduct> list = findByCriteria(Expression.eq("miniClass.miniClassId", miniClass.getMiniClassId()),
					Expression.eq("isMainProduct", "1"));
			if(list!=null && list.size()>0) {
				miniClassProduct = list.get(0);
			}
		}
		if(miniClassProduct == null) {
			miniClassProduct =new MiniClassProduct();
			miniClassProduct.setCreateTime(DateTools.getCurrentDateTime());
			miniClassProduct.setCreateUserId(userId);
		}
		miniClassProduct.setProduct(product);
		miniClassProduct.setMiniClass(miniClass);
		miniClassProduct.setIsMainProduct(isMainProduct);
		miniClassProduct.setModifyTime(DateTools.getCurrentDateTime());
		miniClassProduct.setModifyUserId(userId);
		save(miniClassProduct);
	}

	@Override
	public MiniClassProduct findMiniClassProductByMiniClassAndProduct(String miniClassId,
			String productId) {
		List<MiniClassProduct> list = findByCriteria(Expression.eq("miniClass.id", miniClassId),Expression.eq("product.id", productId));
		if(list!=null && list.size()>0){
			for (MiniClassProduct miniClassProduct: list) {
				if ("1".equals(miniClassProduct.getIsMainProduct())) {
					return miniClassProduct;
				}
			}
			return list.get(0);
		}
		return null;
	}

}
