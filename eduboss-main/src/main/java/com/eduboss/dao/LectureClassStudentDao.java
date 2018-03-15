package com.eduboss.dao;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.LectureClassStudent;




@Repository
public interface LectureClassStudentDao extends GenericDAO<LectureClassStudent, String> {
	/**
	 * 获取已扣费的人数
	 * @param lectureClassId
	 * @return
	 */
	public int findNumByLecutureClassId(String lectureClassId);

	public void deleteInfoByLectureId(String lectureClassId);
}
