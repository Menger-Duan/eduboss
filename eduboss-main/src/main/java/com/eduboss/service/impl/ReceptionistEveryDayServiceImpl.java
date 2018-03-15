package com.eduboss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.ReceptionistEveryDayDao;
import com.eduboss.domain.ReceptionistEveryDay;
import com.eduboss.domainVo.ReceptionistEveryDayVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.ReceptionistEveryDayService;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;

@Service("ReceptionistEveryDayService")
public class ReceptionistEveryDayServiceImpl implements ReceptionistEveryDayService {
	@Autowired
	private ReceptionistEveryDayDao receptionistEveryDao;

	@Override
	public DataPackage getReceptionistEveryDays(DataPackage dataPackage,
			ReceptionistEveryDayVo receptionistEveryDayVo,Map<String, Object> params) {
		dataPackage=this.receptionistEveryDao.getReceptionistEveryDays(dataPackage, receptionistEveryDayVo, params)	;	
		
		List<ReceptionistEveryDay> rl=(List<ReceptionistEveryDay>)dataPackage.getDatas();
		List<ReceptionistEveryDayVo> rlvo = (List<ReceptionistEveryDayVo>) HibernateUtils.voListMapping(rl, ReceptionistEveryDayVo.class);
		dataPackage.setDatas(rlvo);		
		return dataPackage;
	}

		/**
		 * 添加
		 */
	@Override
	public void saveOrUpdateReceptionist(ReceptionistEveryDayVo reception) throws ApplicationException {
		ReceptionistEveryDay receptionist=HibernateUtils.voObjectMapping(reception, ReceptionistEveryDay.class);
		if (StringUtil.isNotBlank(reception.getId())) {
			ReceptionistEveryDay redInDb = receptionistEveryDao.findById(reception.getId());
			receptionistEveryDao.save(redInDb);
			receptionistEveryDao.merge(receptionist);
		} else {
			ReceptionistEveryDay red = receptionistEveryDao.findByloginDateAndOrganizationId(reception.getLoginDate(), reception.getOrganizationId());
			if (null == red) {
				this.receptionistEveryDao.save(receptionist);
			} else {
				throw new ApplicationException("相同校区同一天不能重复登记。");
			}
		}
		
		
	}

	/**
	 * 查找一个
	 */
	@Override
	public ReceptionistEveryDayVo findReceptionById(String id) {
		if(StringUtil.isNotBlank(id)){
			return HibernateUtils.voObjectMapping(receptionistEveryDao.findById(id), ReceptionistEveryDayVo.class);
		}
		else{
			return new ReceptionistEveryDayVo();
		}
		
	}
	/**
	 * 修改
	 */
	@Override
	public void updateReceptionist(ReceptionistEveryDayVo reception) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 校区资源利用情况
	 */

	@Override
	public DataPackage shoolResourceList(DataPackage dataPackage,
			ReceptionistEveryDayVo receptionistEveryDayVo,
			Map<String, Object> params) {
			return this.receptionistEveryDao.shoolResourceList(dataPackage, receptionistEveryDayVo, params)	;	

//			List<ReceptionistEveryDay> rl=(List<ReceptionistEveryDay>)dataPackage.getDatas();
//			List<ReceptionistEveryDayVo> rlvo = (List<ReceptionistEveryDayVo>) HibernateUtils.voListMapping(rl, ReceptionistEveryDayVo.class);
//			dataPackage.setDatas(rlvo);		
//			return dataPackage;	
			}

}
