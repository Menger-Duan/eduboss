package com.eduboss.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eduboss.dao.IntenetPayDao;
import com.eduboss.dao.LiveTransferPayDao;
import com.eduboss.domain.IntenetPay;
import com.eduboss.domain.LiveTransferPay;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.HandleTongLianPayVo;
import com.eduboss.domainVo.IntenetPayVo;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.domainVo.LiveTransferPayVo;
import com.eduboss.dto.MessageQueueDataVo;
import com.eduboss.dto.Response;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.intenetpay.IntenetPayResponseParam;
import com.eduboss.intenetpay.SybPayService;
import com.eduboss.service.ContractService;
import com.eduboss.service.IntenetPayService;
import com.eduboss.service.LivePaymentRecordService;
import com.eduboss.service.LiveTransferPayService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.HttpClientUtil;
import com.eduboss.utils.JedisUtil;
import com.eduboss.utils.ObjectUtil;
import com.eduboss.utils.PropertiesUtils;
import com.google.common.collect.Maps;

@Service("liveTransferPayService")
public class LiveTransferPayServiceImpl implements LiveTransferPayService {

	public static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	 
	@Autowired
	private LiveTransferPayDao liveTransferPayDao;
	 
	@Autowired
	private UserService userService;
	
	@Autowired
	private LivePaymentRecordService livePaymentRecordService;
	
	@Autowired
	private IntenetPayService intenetPayService;
	
	@Autowired
	private IntenetPayDao intenetPayDao;
	
	@Autowired
	private ContractService contractService;
	
	private Logger log = Logger.getLogger(LiveTransferPayServiceImpl.class);
	
	@Override
	public LiveTransferPay findLiveTransferPayByTrxid(String trxid) {
		// TODO Auto-generated method stub
		LiveTransferPay result = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from LiveTransferPay where trxid = :trxid ";
        params.put("trxid", trxid);
        List<LiveTransferPay> list = liveTransferPayDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0); 
        }
        return result;
	}

	@Override
	public LiveTransferPay saveOrUpdateLiveTransferPay(LiveTransferPay liveTransferPay) {
		// TODO Auto-generated method stub
		liveTransferPayDao.save(liveTransferPay);
        return liveTransferPay;
	}

	@Override
	public Map<String, String> sendOrderQRCodeUrl(LiveTransferPayVo liveTransferPayVo) throws ApplicationException{
		// TODO Auto-generated method stub
		Map<String,String> returnMap = sendPayInfo(liveTransferPayVo);
		LiveTransferPay transferPay = HibernateUtils.voObjectMapping(liveTransferPayVo, LiveTransferPay.class);
		transferPay.setAmount(liveTransferPayVo.getAmount().divide(BigDecimal.valueOf(100)));
		transferPay.setCreateTime(DateTools.getCurrentDateTime());
		transferPay.setModifyTime(transferPay.getCreateTime());
		transferPay.setRetcode(returnMap.get("retcode"));
		transferPay.setRetmsg(returnMap.get("retmsg"));
		transferPay.setTrxid(returnMap.get("trxid"));
		
		transferPay.setErrmsg(returnMap.get("errmsg"));
		transferPay.setChnltrxid(returnMap.get("chnltrxid"));
		transferPay.setReqsn(returnMap.get("reqsn"));
		transferPay.setFinishTime(returnMap.get("fintime"));
		transferPay.setPayInfo(returnMap.get("payinfo"));
		transferPay.setTrxstatus(returnMap.get("trxstatus"));
		transferPay.setStatus("0");//未支付
		liveTransferPayDao.save(transferPay);
		
		if("FAIL".equals(returnMap.get("retcode")) || !"0000".equals(returnMap.get("trxstatus"))) {
			throw new ApplicationException(ErrorCode.LIVE_TONGLIAN_PAYINFO);
		}
		HandleTongLianPayVo handleTongLianPayVo = new HandleTongLianPayVo("LIVE",transferPay.getTrxid(),transferPay.getReqsn());
		try {
			JedisUtil.lpush(ObjectUtil.objectToBytes("payMessageKey"),ObjectUtil.objectToBytes(handleTongLianPayVo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("HandleTongLianPayVo  LIVE:" + e.getMessage());
			e.printStackTrace();
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", returnMap.get("payinfo"));
		return map;
	}
	
	/**
	 * 调用通联接口生成订单
	 * @return
	 */
	public Map<String,String> sendPayInfo(LiveTransferPayVo vo) throws ApplicationException{
		Organization o = userService.getBelongBranchByCampusId(vo.getCampusId());
		
		//判断是否有绑定支付帐号
		if(o==null || StringUtils.isBlank(o.getSybAppId()) || StringUtils.isBlank(o.getSybAppKey()) || StringUtils.isBlank(o.getSybCusId())){
			throw new ApplicationException(ErrorCode.LIVE_BRANCH_NO_PAY);
		}
		
		SybPayService service = new SybPayService();
		String reqsn = String.valueOf(System.currentTimeMillis());
		
		if(vo.getActiveTime().compareTo(BigDecimal.ZERO)<=0||vo.getActiveTime().compareTo(BigDecimal.valueOf(60))>0) {//只能在0~60内
			vo.setActiveTime(BigDecimal.valueOf(30));
		}
		
		Map<String, String> map;
		try {
			//通联的金额是以分为单位
			map = service.livePay(vo.getAmount().longValue(),//trxamt金额
					reqsn,//商户交易单号
					vo.getPayType(),//交易方式
					vo.getTitle(),//body订单标题
					"",//remark备注
					String.valueOf(vo.getActiveTime().intValue()),//validtime 失效时间最大60分钟
					vo.getUrl()+PropertiesUtils.getStringValue("liveTransferPay.callbackUrl"),//回调
					"",o.getSybCusId(),o.getSybAppId(),o.getSybAppKey());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException(ErrorCode.LIVE_TONGLIAN_PAY);
		}
		return map;
	}

	@Override
	public Response saveLiveFinanceDetail(IntenetPayResponseParam param) throws ApplicationException{
		// TODO Auto-generated method stub
		Response res=new Response();
		LiveTransferPay transferPay = this.findLiveTransferPayByTrxid(param.getTrxid());
		log.info("直播支付成功回调：trxid="+param.getTrxid() +"###trxstatus="+param.getTrxstatus()+"###pay_time="+param.getPaytime());
		if(transferPay!=null&&!transferPay.getStatus().equals("1")&&!transferPay.getStatus().equals("2")) {
			transferPay.setModifyTime(DateTools.getCurrentDateTime());
			transferPay.setPayTime(param.getPaytime());
			if("0000".equals(param.getTrxstatus())) {//成功
				transferPay.setStatus("1");
				res.setResultMessage("支付成功");
				DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date payDate = new Date();
				try {
					payDate = sdf.parse(param.getPaytime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new ApplicationException(ErrorCode.LIVE_DATEFORMAT_ERROR);
				}
				this.saveLiveFinance(transferPay.getTransactionNum(),transferPay.getAmount(),payDate,transferPay.getCallbackUrl());
			}else {//失败
				res.setResultCode(-1);
				transferPay.setStatus("3");
			}
			liveTransferPayDao.save(transferPay);
		}
		return res;
	}
	
	public void saveLiveFinance(String transactionNum, BigDecimal amount, Date payDate, String callbackUrl) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String 	payTime = sdf.format(payDate);
		String postContent = "{\"amount\":\""+amount.multiply(BigDecimal.valueOf(100)).toString()+"\"," + 
						"	\"transactionNum\":\""+transactionNum+"\"," + 
						"	\"payTime\":\""+payDate.getTime()+"\"" + 
						"}";
		Map<String, String> params = new HashMap<>();
		params.put("postContent", postContent);
		params.put("callbackUrl", callbackUrl);
		params.put("paymentDate", payTime.substring(0, 10));
		params.put("amount", amount.toString());
		cachedThreadPool.execute(new Runnable() {		
 				@Override
 				public void run() {
 					try {
 							log.info("zhibo_postContent_requestdata:"+postContent);
	 						long timeBegin = System.currentTimeMillis();
	 						String response = HttpClientUtil.doPostJson2(callbackUrl, postContent,HttpClientUtil.getLiveDefaultHeaders());
	 						long timeEnd = System.currentTimeMillis();
	 						log.info("saveLiveFinance-costtime:"+(timeEnd-timeBegin)+"ms");
	 						log.info("zhibo_pay_responsedata:"+response);
	 						//使用实时同步syncActualLiveChange
	 						/*if(response!=null) {
	 							JSONObject content =JSON.parseObject(postContent);
 								LivePaymentRecordVo vo =JSON.parseObject(response, LivePaymentRecordVo.class);
 								vo.setPaymentDate(payTime.substring(0, 10));
 								vo.setTotalAmount(amount);
 								livePaymentRecordService.saveNewLivePaymentRecordVo(vo,content.get("transactionNum").toString());
	 						}*/
	  						
 						} catch (RuntimeException e) {
 							e.printStackTrace();
 							//异常,失败把数据丢进队列里面 定时器把数据从队列里面取出来
 							JedisUtil.lpush("saveLiveFinance".getBytes(), JedisUtil.ObjectToByte(params)); 
 						}			
 				}
 			});
	}

	@Override
	public LiveTransferPay findByTransactionNum(String transactionNum) {
		// TODO Auto-generated method stub
		LiveTransferPay result = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from LiveTransferPay where transactionNum = :transactionNum";
        params.put("transactionNum", transactionNum);
        List<LiveTransferPay> list = liveTransferPayDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0); 
        }
        return result;
	}

	@Override
	public LiveTransferPay findLiveTransferPayByReqsn(String reqsn) {
		// TODO Auto-generated method stub
		LiveTransferPay result = null;
        Map<String, Object> params = Maps.newHashMap();
        String hql = " from LiveTransferPay where reqsn = :reqsn ";
        params.put("reqsn", reqsn);
        List<LiveTransferPay> list = liveTransferPayDao.findAllByHQL(hql, params);
        if (list != null && list.size() > 0) {
            result = list.get(0); 
        }
        return result;
	}
	
	@Override
	public Response handlePayStatus(MessageQueueDataVo vo) {
		// TODO Auto-generated method stub
		HandleTongLianPayVo handleTongLianPayVo = JSON.parseObject(vo.getData(), HandleTongLianPayVo.class);
		if(handleTongLianPayVo!=null) {
			if("intenetPay".equals(handleTongLianPayVo.getType())) {
				IntenetPay info = intenetPayDao.findIntenetPayByTrxid(handleTongLianPayVo.getTrxid());
				
				if(info == null) {
					log.info("handlePayStatus 交易申请不存在！");
					return new Response(400,"handlePayStatus 交易申请不存在！");
				}
				
				ContractVo cvo = contractService.getContractById(info.getContractId());
				if(cvo == null) {
					log.info("handlePayStatus 订单"+info.getContractId()+"已被删除,无需继续处理！");
					return new Response(200,"handlePayStatus 订单"+info.getContractId()+"已被删除,无需继续处理！");
				}
				
				Organization o = userService.getBelongBranchByCampusId(cvo.getBlCampusId());
				
				//判断是否有绑定支付帐号
				if(o==null || StringUtils.isBlank(o.getSybAppId()) || StringUtils.isBlank(o.getSybAppKey()) || StringUtils.isBlank(o.getSybCusId())){
					log.info("handlePayStatus 未绑定支付帐号！");
					return new Response(400,"handlePayStatus 未绑定支付帐号！");
				}
				
				IntenetPayVo intenetPayVo = new IntenetPayVo();
				intenetPayVo.setAppId(o.getSybAppId());
				intenetPayVo.setAppKey(o.getSybAppKey());
				intenetPayVo.setCusId(o.getSybCusId());
				intenetPayVo.setTrxid(info.getTrxid());
				intenetPayVo.setReqsn(info.getReqsn());
				
				Map<String,Object> returnMap = intenetPayService.getPayStatusByTrxid(intenetPayVo);
				String trxstatus = (String) returnMap.get("trxstatus");
				
				if("2008".equals(trxstatus)) {
					return new Response(400,"handlePayStatus 交易处理中！返回错误等待再次接收");
					/*try {
						JedisUtil.lpush(ObjectUtil.objectToBytes("payMessageKey"),ObjectUtil.objectToBytes(handleTongLianPayVo));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.info("HandleTongLianPayVo  intenetPay:" + e.getMessage());
						e.printStackTrace();
					}*/
				}else if("0000".equals(info.getTrxstatus())&&
						(info.getStatus() == null || info.getStatus().equals("2"))){
					IntenetPayResponseParam param = new IntenetPayResponseParam();
					param.setTrxstatus(trxstatus);
					param.setTrxid((String) returnMap.get("trxid"));
					param.setPaytime((String) returnMap.get("fintime"));
					intenetPayService.noticePayStatus(param);
				} 
				
			}else if("LIVE".equals(handleTongLianPayVo.getType())) {
				LiveTransferPay liveTransferPay = this.findLiveTransferPayByTrxid(handleTongLianPayVo.getTrxid());
				
				if(liveTransferPay == null) {
					log.info("handlePayStatus 交易申请不存在！");
					return new Response(400,"handlePayStatus 交易申请不存在！");
				}
				
				Organization o = userService.getBelongBranchByCampusId(liveTransferPay.getCampusId());
				
				//判断是否有绑定支付帐号
				if(o==null || StringUtils.isBlank(o.getSybAppId()) || StringUtils.isBlank(o.getSybAppKey()) || StringUtils.isBlank(o.getSybCusId())){
					log.info("handlePayStatus 未绑定支付帐号！");
					return new Response(400,"handlePayStatus 未绑定支付帐号！");
				}
				IntenetPayVo intenetPayVo = new IntenetPayVo();
				intenetPayVo.setAppId(o.getSybAppId());
				intenetPayVo.setAppKey(o.getSybAppKey());
				intenetPayVo.setCusId(o.getSybCusId());
				intenetPayVo.setTrxid(liveTransferPay.getTrxid());
				intenetPayVo.setReqsn(liveTransferPay.getReqsn());
				
				Map<String,Object> returnMap = intenetPayService.getPayStatusByTrxid(intenetPayVo);
				String trxstatus = (String) returnMap.get("trxstatus");
				
				if("0000".equals(trxstatus)) {
					IntenetPayResponseParam param = new IntenetPayResponseParam();
					param.setTrxstatus(trxstatus);
					param.setTrxid((String) returnMap.get("trxid"));
					param.setPaytime((String) returnMap.get("fintime"));
					this.saveLiveFinanceDetail(param);
				}else if("2008".equals(trxstatus)) {
					return new Response(400,"handlePayStatus 交易处理中！返回错误等待再次接收");
					/*try {
						JedisUtil.lpush(ObjectUtil.objectToBytes("payMessageKey"),ObjectUtil.objectToBytes(handleTongLianPayVo));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.info("HandleTongLianPayVo  LIVE:" + e.getMessage());
						e.printStackTrace();
					}*/
				}else {
					liveTransferPay.setTrxstatus(trxstatus);
					liveTransferPay.setErrmsg((String) returnMap.get("errmsg"));
					liveTransferPay.setModifyTime(DateTools.getCurrentDateTime());
					liveTransferPayDao.save(liveTransferPay);
				}
			}
		}
		
		return new Response(200,"handlePayStatus 交易处理成功！");
	}
	
}
