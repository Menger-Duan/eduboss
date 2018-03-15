package com.eduboss.service;

import com.eduboss.domain.Resource;
import com.eduboss.domainVo.ResourceVo;
import com.eduboss.dto.DataPackage;

public interface ResourceService {

	/**
	 * 页面说明列表
	 * */
	public DataPackage getGuideLineList(ResourceVo resourceVo, DataPackage dp);
	
	/**
	 * 获取所有页面说明
	 */
	public DataPackage getAllGuideLine();
	
	/**
	 * 修改或保存
	 */
	public void saveOrEditGuideLine(Resource resource);
	
	/**
	 * 更新状态
	 */
	public void updateGuideLineById(String id, String status);
	
	/**
	 * 删除对应的guideLine信息
	 */
	public void deleteGuideLineById(String resourceId);
	
	/**
	 * 通过URL获取页面说明
	 */
	public ResourceVo findResourceVoByURL(String url);
}
