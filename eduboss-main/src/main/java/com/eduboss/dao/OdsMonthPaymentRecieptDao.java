package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.OdsMonthPaymentReciept;
import com.eduboss.domainVo.BasicOperationQueryVo;

public interface OdsMonthPaymentRecieptDao extends GenericDAO<OdsMonthPaymentReciept, String> {
	
	List<OdsMonthPaymentReciept> findInfoByMonth(String yearMonth,String campusId);

	List<Map<Object,Object>> getPersonPaymentFinaceBonus(BasicOperationQueryVo searchVo);
}
