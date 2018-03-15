package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.MoneyReadyToPayDao;
import com.eduboss.domain.MoneyReadyToPay;

@Repository(value = "MoneyReadyToPayDao")
public class MoneyReadyToPayDaoImpl extends GenericDaoImpl<MoneyReadyToPay, String> implements MoneyReadyToPayDao {

	@Override
	public List<MoneyReadyToPay> getIsPayOkByFundChangeHistory(String fundChangeHistoryId,String [] statuss) {
			List<Order> orderList=new ArrayList<Order>();
			orderList.add(Order.desc("createTime"));
			return  super.findByCriteria(orderList,Expression.in("status", statuss),Expression.eq("fundChargeId.id", fundChangeHistoryId));
	}

}
