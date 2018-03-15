package com.eduboss.service;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.PosMachineManage;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
public interface PosMachineManageService {

	/**
	 * 新增pos终端
	 * @param posMachineManage
	 */
	void savePosMachineManage(PosMachineManage posMachineManage);
	
	/**
	 * 查找pos终端列表
	 * @param dp
	 * @param posMachineVo
	 * @param timeVo
	 * @return
	 */
	DataPackage findPagePosMachineManage(DataPackage dp, PosMachineVo posMachineVo);
	
	/**
	 * 禁用pos终端
	 * @param posNumber
	 */
	void disablePosMachineManage(String posNumber);
	
	/**
	 * 启用pos终端
	 * @param posNumber
	 */
	void enablePosMachineManage(String posNumber);
	
	/**
	 * 根据posNumber查找pos终端
	 * @param posNumber
	 * @return
	 */
	PosMachineVo findPosMachineManageByPosNumber(String posNumber);
	
	void updatePosTypeByPosNumber(String posNumber, DataDict posType, DataDict Type);
	
}
