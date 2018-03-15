package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.CourseDateTemplate;
import com.eduboss.dto.DataPackage;

public interface CourseDateTemplateService {

	public DataPackage getCourseDateTemplateList(CourseDateTemplate courseDateTemplate, DataPackage dataPackage);

	public void saveOrUpdateCourseDateTemplate(CourseDateTemplate courseDateTemplate);

	public void deleteCourseDateTemplate(CourseDateTemplate courseDateTemplate);

	public List<CourseDateTemplate> getCourseDateTemplateByUserIdAndDate(String userId, String startDate, String endDate);

}
