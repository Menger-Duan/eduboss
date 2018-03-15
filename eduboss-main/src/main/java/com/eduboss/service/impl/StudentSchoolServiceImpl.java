package com.eduboss.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.StudentSchoolDao;
import com.eduboss.domain.StudentSchool;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.StudentSchoolService;

@Service
public class StudentSchoolServiceImpl implements StudentSchoolService {

	@Autowired
	private StudentSchoolDao studentSchoolDao;
	
	@Override
	public String getStudentSchoolIdByName(String schoolName) {
		return studentSchoolDao.getStudentSchoolIdByName(schoolName);
	}

	/**
	 * 根据省份ID获取城市列表
	 */
	@Override
	public List<StudentSchool> listSchoolByRegion(String provinceID,
			String cityID) {
		return studentSchoolDao.listSchoolByRegion(provinceID, cityID);
	}

    @Override
    public DataPackage listPageSchoolByRegionAndName(String provinceId,
            String cityId, String name, DataPackage dp) {
        return studentSchoolDao.listPageSchoolByRegionAndName(provinceId, cityId, name, dp);
    }
    
    public List<StudentSchool>  listSchoolByIds(String[] schoolIds) {
        return studentSchoolDao.listSchoolByIds(schoolIds);
    }

    @Override
    public StudentSchool findSchoolByGlobalNumber(String globalNumber) {
        return studentSchoolDao.findSchoolByGlobalNumber(globalNumber);
    }

}
