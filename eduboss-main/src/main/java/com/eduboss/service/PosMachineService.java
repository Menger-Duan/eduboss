package com.eduboss.service;

import java.io.File;
import java.util.Map;

import com.eduboss.common.PosMachineStatus;
import com.eduboss.domain.PosPayData;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;

/**
 * 
 * @author lixuejun
 *
 */
public interface PosMachineService {
	
	/**
	 * 新增终端
	 * @param posMachineVo
	 */
	Response savePosMachineManage(PosMachineVo posMachineVo);

	/**
	 * 删除pos终端使用时间段
	 * @param posMachineVo
	 */
	void deletePosMachine(PosMachineVo posMachineVo);
	
	/**
	 * 新增或修改pos终端使用时间段
	 * @param posMachineVo
	 */
	Response saveOrUpdatePosMachine(PosMachineVo posMachineVo);
	
	/**
	 * 查找pos终端使用日期列表
	 * @param dp
	 * @param posNumber
	 * @return
	 */
	DataPackage findPagePosMachine(DataPackage dp, String posNumber);
	
	/**
	 * 根据id查找pos终端使用日期
	 * @param id
	 * @return
	 */
	PosMachineVo findPosMachineById(Integer id);
	
	/**
	 * 禁用，激活终端的使用日期
	 * @param posNumber
	 * @param status
	 */
	void chagePosMachineByPosNumberStatus(String posNumber, PosMachineStatus status);
	
	/**
	 * 导入银联反馈数据
	 * @param file
     */
	Map<Boolean, String> importPosPayDataFromExcel(File file) throws Exception;
	
	/**
	 * 校验并导入银联数据
	 * @param loadIntoDBMap
	 * @param fileName
	 */
	void importPosPayDataFromList(Map<Integer, PosPayData> loadIntoDBMap, String fileName);
	
	/**
	 * 根据校区查找pusNumber的下拉框
	 * @param campusId
	 * @param typeId
	 * @return
	 */
	SelectOptionResponse getPosNumSelectOptionByCampusId(String campusId, String typeId);
	
}
