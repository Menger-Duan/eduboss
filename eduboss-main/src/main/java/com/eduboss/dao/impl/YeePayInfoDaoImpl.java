package com.eduboss.dao.impl;

import com.eduboss.dao.YeePayInfoDao;
import com.eduboss.domain.YeePayInfo;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository(value = "YeePayInfoDao")
public class YeePayInfoDaoImpl extends GenericDaoImpl<YeePayInfo, String> implements YeePayInfoDao {

	@Override
	public List<YeePayInfo> getIsPayOkByFundChangeHistory(String fundChangeHistoryId,String [] statuss) {
			List<Order> orderList=new ArrayList<Order>();
			orderList.add(Order.desc("createTime"));
			return  super.findByCriteria(orderList,Expression.in("status", statuss),Expression.eq("fundChargeId.id", fundChangeHistoryId));
	}

}
