package com.eduboss.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.PosMachineStatus;
import com.eduboss.dao.PosMachineManageDao;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.PosMachineManage;
import com.eduboss.domainVo.PosMachineManageVo;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.PosMachineManageService;
import com.eduboss.service.PosMachineService;
import com.eduboss.service.UserService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Service
public class PosMachineManageServiceImpl implements PosMachineManageService {

	@Autowired
	private PosMachineService posMachineService;
	
	@Autowired
	private PosMachineManageDao posMachineManageDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService organizationService;
	
	/**
	 * 新增pos终端
	 */
	@Override
	public void savePosMachineManage(PosMachineManage posMachineManage) {
		if (StringUtils.isBlank(posMachineManage.getPosNumber())) {
			throw new ApplicationException("pos终端编号不能为空");
		}
		if (posMachineManageDao.findById(posMachineManage.getPosNumber()) != null) {
			throw new ApplicationException("pos终端编号不能重复");
		}
		posMachineManageDao.save(posMachineManage);
	}
	
	/**
	 * 查找pos终端列表
	 */
	@Override
	public DataPackage findPagePosMachineManage(DataPackage dp, PosMachineVo posMachineVo) {
		Organization org = userService.getCurrentLoginUserOrganization();
		org = StringUtil.isNotBlank(org.getBelong()) ? organizationService.findById(org.getBelong()) : org;
		dp =  posMachineManageDao.findPagePosMachineManage(dp, posMachineVo, org.getOrgLevel());
		List<PosMachineManageVo> voList =  HibernateUtils.voListMapping((List<PosMachineManage>)dp.getDatas(), PosMachineManageVo.class);
		dp.setDatas(voList);
		return dp;
	}
	
	/**
	 * 禁用pos终端
	 */
	@Override
	public void disablePosMachineManage(String posNumber) {
		PosMachineManage posMachineManage = posMachineManageDao.findById(posNumber);
		posMachineService.chagePosMachineByPosNumberStatus(posNumber, PosMachineStatus.DISABLE);
		posMachineManage.setStatus(PosMachineStatus.DISABLE);
		posMachineManageDao.merge(posMachineManage);
	}
	
	/**
	 * 启用pos终端
	 * @param posNumber
	 */
	@Override
	public void enablePosMachineManage(String posNumber) {
		PosMachineManage posMachineManage = posMachineManageDao.findById(posNumber);
		posMachineService.chagePosMachineByPosNumberStatus(posNumber, PosMachineStatus.ACTIVATE);
		posMachineManage.setStatus(PosMachineStatus.ACTIVATE);
		posMachineManageDao.merge(posMachineManage);
	}
	
	/**
	 * 根据posNumber查找pos终端Vo
	 */
	@Override
	public PosMachineVo findPosMachineManageByPosNumber(String posNumber) {
		PosMachineManage posMachineManage = posMachineManageDao.findById(posNumber);
		return HibernateUtils.voObjectMapping(posMachineManage, PosMachineVo.class);
	}
	
	/**
     * 根据posNumber查找pos终端
     */
    @Override
    public void updatePosTypeByPosNumber(String posNumber, DataDict posType, DataDict type) {
        PosMachineManage posMachineManage = posMachineManageDao.findById(posNumber);
        posMachineManage.setPosType(posType);
        posMachineManage.setType(type);
        posMachineManageDao.merge(posMachineManage);
    }
    
    
	
}
