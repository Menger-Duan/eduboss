package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.eduboss.exception.ApplicationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MsgNo;
import com.eduboss.common.ValidStatus;
import com.eduboss.dao.SystemMessageManageDao;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SystemMessageManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.SystemMessageManageService;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

@Service
public class SystemMessageManageServiceImpl implements SystemMessageManageService {

	@Autowired
	private SystemMessageManageDao systemMessageManageDao;
	
	@Autowired
	private UserService userService;
	
	private static final Log log = LogFactory.getLog(SystemMessageManageServiceImpl.class);
	
	/**
	 * 获取系统信息列表
	 */
	@Override
	public DataPackage getSystemMessageManageList(SystemMessageManageVo systemMessageManageVo, DataPackage dp) {
		List<Criterion> list =new ArrayList<Criterion>();
		if (null != systemMessageManageVo.getMsgNo()) {
			list.add(Expression.eq("msgNo", systemMessageManageVo.getMsgNo()));
		}
		if (StringUtils.isNotBlank(systemMessageManageVo.getMsgName())) {
			list.add(Expression.like("msgName", "%" + systemMessageManageVo.getMsgName() + "%"));
		}
		if (StringUtils.isNotBlank(systemMessageManageVo.getMsgTypeId())) {
			list.add(Expression.eq("msgType.id", systemMessageManageVo.getMsgTypeId()));
		}
		
		if (null != systemMessageManageVo.getStatus()) {
			list.add(Expression.eq("status", systemMessageManageVo.getStatus()));
		}
		
		dp = systemMessageManageDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "id", "asc"), list);
		List<SystemMessageManage> msgList=(List<SystemMessageManage>) dp.getDatas();
		List<SystemMessageManageVo> sysMsgVoList=HibernateUtils.voListMapping(msgList, SystemMessageManageVo.class);
		dp.setDatas(sysMsgVoList);
		return dp;
	}
	
	/**
	 * 删除系统信息
	 */
	@Override
	public void deleteSystemMsg(SystemMessageManageVo systemMessageManageVo) {
		SystemMessageManage systemMessageManage = HibernateUtils.voObjectMapping(systemMessageManageVo, SystemMessageManage.class);
		systemMessageManageDao.delete(systemMessageManage);
	}
	
	/**
	 * 修改系统信息状态
	 * @param systemMessageManageVo
	 */
	public void updateSystemMsgStatus(SystemMessageManageVo systemMessageManageVo) throws Exception {
		if(StringUtils.isEmpty(systemMessageManageVo.getId()) || null == systemMessageManageVo.getStatus()){
			throw new ApplicationException("操作异常，请联系管理员！");
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("status", systemMessageManageVo.getStatus());
		params.put("id", systemMessageManageVo.getId());
		systemMessageManageDao.excuteHql("update SystemMessageManage set status= :status where id= :id ",params);
	}
	
	/**
	 * 增加或修改系统信息
	 * @param systemMessageManageVo
	 * @return
	 */
	@Override
	public Response saveOrUpdateSystemMsg(SystemMessageManageVo systemMessageManageVo) {
		Response res = checkRepeatSystemMsg(systemMessageManageVo);
		if(res.getResultCode()!=0){
			return res;
		}
		User user = userService.getCurrentLoginUser();
		SystemMessageManage systemMessageManage = HibernateUtils.voObjectMapping(systemMessageManageVo, SystemMessageManage.class);
		systemMessageManage.setModifyTime(DateTools.getCurrentDateTime());
		systemMessageManage.setModifyUserId(user.getUserId());
		if(StringUtils.isEmpty(systemMessageManage.getId())){
			systemMessageManage.setCreateTime(DateTools.getCurrentDateTime());
			systemMessageManage.setCreateUserId(user.getUserId());
			systemMessageManageDao.save(systemMessageManage);
		} else {
			SystemMessageManage oldSysMsg = systemMessageManageDao.findById(systemMessageManage.getId());
			systemMessageManage.setCreateTime(oldSysMsg.getCreateTime());
			systemMessageManage.setCreateUserId(oldSysMsg.getCreateUserId());
			systemMessageManageDao.merge(systemMessageManage);
		}
		
		return res;
	}
	
	/**
	 * 根据id查找系统信息
	 * @param id
	 * @return
	 */
	@Override
	public SystemMessageManageVo findSystemMessageManageById(String id) {
		SystemMessageManage systemMessageManage = systemMessageManageDao.findById(id);
		SystemMessageManageVo systemMessageManageVo =  HibernateUtils.voObjectMapping(systemMessageManage, SystemMessageManageVo.class);
		return systemMessageManageVo;
	}
	
	public SystemMessageManage findSystemMessageManageByMsgNo(MsgNo msgNo) {
		SystemMessageManage sysMsgManage = null;
		List<Criterion> list =new ArrayList<Criterion>();
		list.add(Expression.eq("msgNo", msgNo));
		list.add(Expression.eq("status",ValidStatus.VALID));
		List<SystemMessageManage> sysMsgList= systemMessageManageDao.findAllByCriteria(list);
		if (sysMsgList != null && sysMsgList.size() > 0) {
			sysMsgManage = sysMsgList.get(0);
		} else {
			log.error("系统信息发送：没找到:" + msgNo.getValue() + "编号的系统信息配置");
		}
		return sysMsgManage;
	}
	
	@Override
	public List<Map<String, Object>> findMapBySql(String sql) {
		Map<String, Object> params = Maps.newHashMap();
	   List<Map<Object, Object>> list = systemMessageManageDao.findMapBySql(sql,params);
	   List<Map<String, Object>> result = new ArrayList<>();
	   for(Map<Object, Object> map :list){
		   result.add((Map)map);
	   }
	   return result;
	}

	@Override
	public SystemMessageManage findMsgById(String msgId) {
		SystemMessageManage systemMessageManage = systemMessageManageDao.findById(msgId);
		return systemMessageManage;
	}

	private Response checkRepeatSystemMsg(SystemMessageManageVo systemMessageManageVo) {
		Response res=new Response();
		if (StringUtils.isEmpty(systemMessageManageVo.getId())) {
			List<Criterion> list =new ArrayList<Criterion>();
			list.add(Expression.eq("msgNo", systemMessageManageVo.getMsgNo()));
			list.add(Expression.eq("status",ValidStatus.VALID));
			List<SystemMessageManage> sysMsgList= systemMessageManageDao.findAllByCriteria(list);
			if(sysMsgList!=null && sysMsgList.size()>0){
				res.setResultCode(-1);
				res.setResultMessage("系统信息编号是"+systemMessageManageVo.getMsgNo().getValue()+"已存在");
			}
		}
		return res;
	}
	
}
