package com.eduboss.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.LectureClassStudentDao;
import com.eduboss.domain.LectureClassStudent;
import com.google.common.collect.Maps;

@Repository("LectureClassStudentDao")
public class LectureClassStudentDaoImpl extends GenericDaoImpl<LectureClassStudent, String> implements LectureClassStudentDao {
	
	private static final Logger log = LoggerFactory.getLogger(LectureClassStudentDaoImpl.class);

	@Override
	public int findNumByLecutureClassId(String lectureClassId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("lectureClassId", lectureClassId);
		String hql="select count(1) from lecture_class_student where lecture_id= :lectureClassId and charge_status='CHARGED'";
		return this.findCountSql(hql,params);
	}

	@Override
	public void deleteInfoByLectureId(String lectureClassId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("lectureClassId", lectureClassId);
		String sql ="delete from lecture_class_student where lecture_id= :lectureClassId ";
		this.excuteSql(sql,params);
	}
	
}
