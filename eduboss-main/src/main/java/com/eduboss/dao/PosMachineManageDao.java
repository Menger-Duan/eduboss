package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PosMachineManage;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;

/**
 * 
 * @author lixuejun
 *
 */
@Repository
public interface PosMachineManageDao extends GenericDAO<PosMachineManage, String>  {

	/**
	 * 查找pos终端列表
	 * @param dp
	 * @param posMachineVo
	 * @param timeVo
	 * @return
	 */
	DataPackage findPagePosMachineManage(DataPackage dp, PosMachineVo posMachineVo, String orgLevel);
	
}
