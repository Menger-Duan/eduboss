package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PosPayData;
import com.eduboss.domainVo.PosPayDataVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
@Repository
public interface PosPayDataDao extends GenericDAO<PosPayData, Integer> {
	
	/**
	 * 查找银联反馈支付数据列表
	 * @param dp
	 * @param posPayDataVo
	 * @return
	 */
	DataPackage findPagePosPayData(DataPackage dp, PosPayDataVo posPayDataVo);
	
}
