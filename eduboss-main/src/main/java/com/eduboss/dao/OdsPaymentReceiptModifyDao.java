package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.OdsMonthPaymentReciept;
import com.eduboss.domain.OdsPaymentReceiptModify;

public interface OdsPaymentReceiptModifyDao extends GenericDAO<OdsPaymentReceiptModify, String> {
	List<OdsPaymentReceiptModify> findInfoByMainId(String mainId);
}
