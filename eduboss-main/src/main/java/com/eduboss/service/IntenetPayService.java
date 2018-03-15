package com.eduboss.service;

import java.util.Map;

import com.eduboss.domainVo.IntenetPayVo;
import com.eduboss.dto.Response;
import com.eduboss.intenetpay.IntenetPayResponseParam;


public interface IntenetPayService {

	Map<String,String> sendPayDetailInfo(IntenetPayVo intenetPayVo);

	 Map<String,String> commitPayInfo(String trxId, String reqsn);

	Response noticePayStatus(IntenetPayResponseParam param);

	Map<String,String> confirmPaid(String trxId);

	IntenetPayVo findPayInfoByFundId(String fundsId);

	Map<String, String> updatePayStatus(String trxId,String reqsn);

	public Response saveBarCodeInfo(IntenetPayVo intenetPayVo);

	public Map<String,String> sendPayInfoForOtherPlat(IntenetPayVo vo);

	public Map<String,Object> getPayStatusByTrxid(IntenetPayVo vo);
	
}
