package com.eduboss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.MoneyWashRecordsDao;
import com.eduboss.domain.MoneyWashRecords;
import com.eduboss.service.MoneyWashRecordsService;

@Service
public class MoneyWashRecordsServiceImpl implements MoneyWashRecordsService {

	@Autowired
	private MoneyWashRecordsDao moneyWashRecordsDao;

	@Override
	public MoneyWashRecords findMoneyWashRecordsByCourseId(String courseId) {
		return moneyWashRecordsDao.findMoneyWashRecordsByCourseId(courseId);
	}

	@Override
	public MoneyWashRecords findMoneyWashRecordsByMiniCourseId(
			String miniCourseId) {
		return moneyWashRecordsDao.findMoneyWashRecordsByMiniCourseId(miniCourseId);
	}

	@Override
	public MoneyWashRecords findMoneyWashRecordsByOtmCourseId(String otmCourseId) {
		return moneyWashRecordsDao.findMoneyWashRecordsByOtmCourseId(otmCourseId);
	}
	
	
	
}
