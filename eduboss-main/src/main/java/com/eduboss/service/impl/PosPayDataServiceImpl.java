package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MatchingStatus;
import com.eduboss.dao.PosPayDataDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.PosPayData;
import com.eduboss.domainVo.PosMachineVo;
import com.eduboss.domainVo.PosPayDataVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.OrganizationService;
import com.eduboss.service.PosMachineManageService;
import com.eduboss.service.PosPayDataService;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Service
public class PosPayDataServiceImpl implements PosPayDataService {

    @Autowired
    private OrganizationService organizationService;
    
	@Autowired
	private PosPayDataDao posPayDataDao;
	
	@Autowired
	private PosMachineManageService posMachineManageService;
	
	/**
	 * 查找银联反馈支付数据列表
	 */
	@Override
	public DataPackage findPagePosPayData(DataPackage dp,
			PosPayDataVo posPayDataVo) {
		dp = posPayDataDao.findPagePosPayData(dp, posPayDataVo);
		List<PosPayData> list = (List<PosPayData>) dp.getDatas();
		List<PosPayDataVo> volist = new ArrayList<PosPayDataVo>();
		Organization brench = null;
		for (PosPayData posPayData : list) {
			PosPayDataVo vo = HibernateUtils.voObjectMapping(posPayData, PosPayDataVo.class);
			if (posPayData.getBlCampus() != null && StringUtils.isNotBlank(posPayData.getBlCampus().getParentId())) {
				brench = organizationService.findById(posPayData.getBlCampus().getParentId());
				vo.setBlBrenchName(brench.getName());
			}
			PosMachineVo pmm = posMachineManageService.findPosMachineManageByPosNumber(vo.getPosNumber());
			vo.setPosTypeName(pmm.getPosTypeName());
			volist.add(vo);
		}
		dp.setDatas(volist);
		return dp;
	}
	
	/**
	 * 人工匹配
	 */
	@Override
	public void artificialMatch(String id, String remark) {
		PosPayData posPayData = posPayDataDao.findById(Integer.parseInt(id));
		posPayData.setMatchingStatus(MatchingStatus.ARTIFICIAL_MATCH);
		posPayData.setRemark(remark);
		posPayDataDao.merge(posPayData);
	}

}
