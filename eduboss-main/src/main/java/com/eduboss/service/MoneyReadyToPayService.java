package com.eduboss.service;

import java.util.Map;

import com.eduboss.domain.MoneyReadyToPay;
import com.eduboss.domainVo.MoneyReadyToPayVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

public interface MoneyReadyToPayService {

	public DataPackage getMoneyReadyToPay(MoneyReadyToPay moneyReadyToPay,DataPackage dataPackage);
	
	public String saveMoneyReadyToPay(MoneyReadyToPayVo moneyReadyToPayVo);
	
	public MoneyReadyToPayVo findById(String id);

	public Map payResultNotify(String payNo,
			String payResultCode, String payResultMessage, String signMsg,String posCode,
			String payCode,String busNo,String cardNo,String amount,String transactionTime,String encryptedMsg);
	
	public Map payResultNotify( String signMsg,String encryptedMsg);
	
	public Map findByIdAndSignMsg(String signMsg,String encryptedMsg);
	
}
