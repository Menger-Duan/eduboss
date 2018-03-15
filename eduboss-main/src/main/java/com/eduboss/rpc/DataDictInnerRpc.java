package com.eduboss.rpc;

import java.util.List;

import com.eduboss.rpc.dto.Label;

/**
 * 字典数据接口
 * 
 * @author sk
 *
 */
public interface DataDictInnerRpc {
	
	/**
	 * 获取全部省份列表
	 * 
	 * @return 返回省份列表
	 */
	List<Label> listAllProvince();
	
	
	/**
	 * 根据省份ID获取城市列表
	 * 
	 * @param provinceID 省份ID
	 * @return 返回省份下的城市列表
	 */
	List<Label> listCityByProvinceID(String provinceID);
	
	
	/**
	 * 根据位置信息获取学校列表
	 * 
	 * @param provinceID 省份ID
	 * @param cityID 城市ID
	 * @return 返回学校列表
	 */
	List<Label> listSchoolByRegion(String provinceID, String cityID);
	
	

	/**
	 * 获取全部年级信息
	 * 
	 * @return 返回年级列表
	 */
	List<Label> listAllGrade();
	
	
	
	
}
