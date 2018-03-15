package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.LivePaymentRecord;
import com.eduboss.domainVo.LivePaymentRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;

public interface LivePaymentRecordService {
	
    
	LivePaymentRecord saveOrUpdateLivePaymentRecord(LivePaymentRecord livePaymentRecord);

	LivePaymentRecord saveNewLivePaymentRecordVo(LivePaymentRecordVo vo,String transactionNum);
	
	/**
	 * 查询直播流水记录
	 * @author: duanmenrun
	 * @Title: getLivePaymentRecordList 
	 * @Description: TODO 
	 * @param dataPackage 
	 * @param livePaymentRecordVo
	 * @param timeVo
	 * @return
	 */
	DataPackage getLivePaymentRecordList(DataPackage dataPackage, LivePaymentRecordVo livePaymentRecordVo,
			TimeVo timeVo);
	/**
	 * 导出直播流水记录
	 * @author: duanmenrun
	 * @Title: getLivePaymentRecordToExcel 
	 * @Description: TODO 
	 * @param livePaymentRecordVo
	 * @param timeVo 
	 * @return
	 */
	List getLivePaymentRecordToExcel(LivePaymentRecordVo livePaymentRecordVo, TimeVo timeVo);
	
	void saveLivePaymentRecordVo(LivePaymentRecordVo vo); 

	void saveMonthLiveChangeList(List<LivePaymentRecordVo> vos); 
}
