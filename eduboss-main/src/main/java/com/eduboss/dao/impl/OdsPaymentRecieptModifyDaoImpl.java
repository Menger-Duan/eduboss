package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.OdsPaymentReceiptModifyDao;
import com.eduboss.domain.OdsPaymentReceiptModify;
import com.google.common.collect.Maps;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class OdsPaymentRecieptModifyDaoImpl extends GenericDaoImpl<OdsPaymentReceiptModify, String> implements OdsPaymentReceiptModifyDao {

	@Override
	public List<OdsPaymentReceiptModify> findInfoByMainId(String mainId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("mainId", mainId);
		String hql =" from OdsPaymentReceiptModify where receiptMainId= :mainId ";
		return this.findAllByHQL(hql,params);
	}
}
