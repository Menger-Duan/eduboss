package com.eduboss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MobileType;
import com.eduboss.dao.AppVersionDao;
import com.eduboss.domain.AppVersion;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.AppFunction;
import com.eduboss.utils.HibernateUtils;

@Service("com.eduboss.service.AppFunction")
public class AppFunctionImpl implements AppFunction {
	
	@Autowired
	private AppVersionDao appVersionDao;

	@Override
	public AppVersion getLastAppVersion(String appId, MobileType mobileType) {
		AppVersion appVerison = null;
		
		DataPackage dp = new DataPackage(0, 1);
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("appId", appId));
		criterionList.add(Restrictions.eq("mobileType", mobileType));
		dp = appVersionDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "version", "desc"), criterionList);
		if (dp.getDatas().size() > 0) {
			appVerison = (AppVersion)((List)dp.getDatas()).get(0);
		} else {
			throw new ApplicationException("找不到应用版本");
		}
		
		return appVerison;
	}

}