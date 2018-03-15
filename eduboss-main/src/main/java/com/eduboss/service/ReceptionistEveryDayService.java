package com.eduboss.service;

import java.util.Map;

import com.eduboss.domainVo.ReceptionistEveryDayVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;

public interface ReceptionistEveryDayService {
	
	DataPackage getReceptionistEveryDays(DataPackage dataPackage,
			ReceptionistEveryDayVo receptionistEveryDayVo, Map<String, Object> params);
	
		/**
		 * 添加每日信息	
		 */	 
		 void saveOrUpdateReceptionist(ReceptionistEveryDayVo reception) throws ApplicationException;		
				
		
		/**
		 * 查找一个信息
		 */
		 ReceptionistEveryDayVo findReceptionById(String id);
		 
		 /**
		  * 修改
		  */
		 void updateReceptionist(ReceptionistEveryDayVo reception);
		 
			
		/***
		 * 查询校区资源
		 */
		 DataPackage shoolResourceList(DataPackage dataPackage,
					ReceptionistEveryDayVo receptionistEveryDayVo, Map<String, Object> params);
}
