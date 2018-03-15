package com.eduboss.service;

import java.util.Map;

import com.eduboss.domain.LivePaymentRecord;
import com.eduboss.domain.LiveTransferPay;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.domainVo.LiveTransferPayVo;
import com.eduboss.dto.MessageQueueDataVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.intenetpay.IntenetPayResponseParam;

public interface LiveTransferPayService {
	/**
	 * 根据通联流水查询
	 * @author: duanmenrun
	 * @Title: findLiveTransferPayByTrxid
	 * @Description: TODO 
	 * @param trxid
	 * @return
	 */
	LiveTransferPay findLiveTransferPayByTrxid(String trxid);
	
	/**
	 * 根据直播交易号查询
	 * @author: duanmenrun
	 * @Title: findByTransactionNum
	 * @Description: TODO 
	 * @param transactionNum
	 * @return
	 */
	LiveTransferPay findByTransactionNum(String transactionNum);
    
	LiveTransferPay saveOrUpdateLiveTransferPay(LiveTransferPay liveTransferPay);

	/**
	 * 获取支付二维码
	 * @author: duanmenrun
	 * @Title: sendOrderQRCodeUrl 
	 * @Description: TODO 
	 * @param liveTransferPayVo
	 * @return
	 * @throws ApplicationException
	 */
	Map<String, String> sendOrderQRCodeUrl(LiveTransferPayVo liveTransferPayVo) throws ApplicationException;
	/**
	 * 支付结果回调
	 * @author: duanmenrun
	 * @Title: saveLiveFinanceDetail 
	 * @Description: TODO 
	 * @param param
	 * @return
	 */
	Response saveLiveFinanceDetail(IntenetPayResponseParam param);
	/**
	 * 查询通联支付状态，处理订单
	 * @author: duanmenrun
	 * @Title: handlePayStatus 
	 * @Description: TODO 
	 * @param vo
	 */
	Response handlePayStatus(MessageQueueDataVo vo); 
	
	/**
	 * 根据商户交易单号查询
	 * @author: duanmenrun
	 * @Title: findLiveTransferPayByReqsn
	 * @Description: TODO 
	 * @param reqsn
	 * @return
	 */
	LiveTransferPay findLiveTransferPayByReqsn(String reqsn);
}
