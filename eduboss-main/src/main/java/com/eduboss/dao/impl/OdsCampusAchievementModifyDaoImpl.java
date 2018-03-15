package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.OdsCampusAchievementModifyDao;
import com.eduboss.domain.OdsCampusAchievementModify;
import com.google.common.collect.Maps;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class OdsCampusAchievementModifyDaoImpl extends GenericDaoImpl<OdsCampusAchievementModify, String> implements OdsCampusAchievementModifyDao {

	@Override
	public List<OdsCampusAchievementModify> findInfoByMainId(String mainId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("mainId", mainId);
		String hql =" from OdsCampusAchievementModify where achievementMainId= :mainId ";
		return this.findAllByHQL(hql,params);
	}
}
