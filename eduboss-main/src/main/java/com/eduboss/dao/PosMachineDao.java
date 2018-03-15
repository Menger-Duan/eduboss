package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.PosMachineStatus;
import com.eduboss.domain.PosMachine;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
@Repository
public interface PosMachineDao extends GenericDAO<PosMachine, Integer>  {

	/**
	 * 查找pos终端使用日期列表
	 * @param dp
	 * @param posNumber
	 * @return
	 */
	DataPackage findPagePosMachine(DataPackage dp, String posNumber);
	
	/**
	 * 计算pos终端使用日期是否有重复
	 * @param posMachineVo
	 * @return
	 */
	int findPosMachineConflictCount(PosMachine posMachine);
	
	/**
	 * 禁用，激活终端的使用日期
	 * @param posNumber
	 * @param status
	 */
	void chagePosMachineByPosNumberStatus(String posNumber, PosMachineStatus status);
	
	/**
	 * 根据校区查找pusNumber的下拉框
	 * @param campusId
	 * @param typeId
	 * @return
	 */
	List<PosMachine> findPosMachineListByCampusId(String campusId, String typeId);
	
}
