package com.eduboss.service;

import com.eduboss.domainVo.PosPayDataVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
public interface PosPayDataService {

	/**
	 * 查找银联反馈支付数据列表
	 * @param dp
	 * @param posPayDataVo
	 * @return
	 */
	DataPackage findPagePosPayData(DataPackage dp, PosPayDataVo posPayDataVo);
	
	/**
	 * 人工匹配
	 * @param id
	 * @param remark
	 */
	void artificialMatch(String id, String remark);
	
}
