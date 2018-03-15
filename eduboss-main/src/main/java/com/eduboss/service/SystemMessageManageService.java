package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.eduboss.common.MsgNo;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domainVo.SystemMessageManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

@Service
public interface SystemMessageManageService {

	/**
	 * 获取系统信息列表
	 * @param systemMessageManageVo
	 * @param dp
	 * @return
	 */
	public DataPackage getSystemMessageManageList(SystemMessageManageVo systemMessageManageVo, DataPackage dp);
	
	/**
	 * 删除系统信息
	 * @param systemMessageManageVo
	 */
	public void deleteSystemMsg(SystemMessageManageVo systemMessageManageVo);
	
	/**
	 * 修改系统信息状态
	 * @param systemMessageManageVo
	 */
	public void updateSystemMsgStatus(SystemMessageManageVo systemMessageManageVo) throws Exception;
	
	/**
	 * 增加或修改系统信息
	 * @param systemMessageManageVo
	 * @return
	 */
	public Response saveOrUpdateSystemMsg(SystemMessageManageVo systemMessageManageVo);
	
	/**
	 * 根据id查找系统信息
	 * @param id
	 * @return
	 */
	public SystemMessageManageVo findSystemMessageManageById(String id);
	
	/**
	 * 根据msgNo查找系统信息
	 * @param msgNo
	 * @return
	 */
	public SystemMessageManage findSystemMessageManageByMsgNo(MsgNo msgNo);
	
	public List<Map<String, Object>> findMapBySql(String sql);

    SystemMessageManage findMsgById(String msgId);
}
