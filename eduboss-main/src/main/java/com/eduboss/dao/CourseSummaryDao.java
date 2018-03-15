package com.eduboss.dao;

import com.eduboss.domain.CourseSummary;
import com.eduboss.dto.CourseSummarySearchInputVo;
import com.eduboss.dto.DataPackage;

public interface CourseSummaryDao extends GenericDAO<CourseSummary, String> {

	// 大课表
	public DataPackage getCourseSummaryList(
			CourseSummarySearchInputVo courseSummarySearchInputVo,
			DataPackage dp);
	
	public int countCourseOfDummyTeacher(String userId);
	
}
