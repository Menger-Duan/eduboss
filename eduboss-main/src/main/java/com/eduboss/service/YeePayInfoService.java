package com.eduboss.service;

import com.eduboss.domain.YeePayInfo;
import com.eduboss.domainVo.YeePayInfoVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface YeePayInfoService {

	public DataPackage getYeePayInfo(YeePayInfo moneyReadyToPay, DataPackage dataPackage);
	
	public String saveYeePayInfo(YeePayInfoVo moneyReadyToPayVo);
	
	public YeePayInfoVo findById(String id);

	public Map<String,String> saveResultNotify(Map<String,String> map, HttpServletRequest request);
	
	public Map<String,String> findByIdAndSignMsg(Map<String,String> map, HttpServletRequest request);

	Response reflushYeePayStatus(String yeePayId);

	Map<String,String> updatePosPaid(String payNo);

	Response saveResultNotify(Map map);

    YeePayInfoVo updateAndFindById(String orderId, String terminalId, String merchantId);
}
