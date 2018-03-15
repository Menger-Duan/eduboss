package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.DeliverTargetChangeDao;
import com.eduboss.domain.DeliverTargetChangeRecord;
import com.eduboss.service.DeliverTargetChangeService;
import com.google.common.collect.Maps;

@Service("deliverTargetChangeService")
public class DeliverTargetChangeServiceImpl implements DeliverTargetChangeService{

	@Autowired
	private DeliverTargetChangeDao deliverTargetChangeDao;
	
	@Override
	public String saveDeliverTargetChangeRecord(DeliverTargetChangeRecord record) {
		deliverTargetChangeDao.saveDeliverTargetChangeRecord(record);
		return record.getId();
	}
	
	
	//获取某个customerId的最新的变动记录 用于还原
	@Override
	public DeliverTargetChangeRecord getLastChangeRecord(String customerId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("customerId", customerId);
		String sql ="select * from delivertarget_change_record where customer_id = :customerId order by create_time desc limit 1";
		List<DeliverTargetChangeRecord> list =deliverTargetChangeDao.findBySql(sql,params);
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
		
	}

}
