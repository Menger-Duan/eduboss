package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.OdsCampusAchievementModify;

public interface OdsCampusAchievementModifyDao extends GenericDAO<OdsCampusAchievementModify, String> {
	List<OdsCampusAchievementModify> findInfoByMainId(String mainId);
}
