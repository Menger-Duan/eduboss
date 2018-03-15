package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.StudentSchool;
import com.eduboss.dto.DataPackage;

public interface StudentSchoolDao extends GenericDAO<StudentSchool, String> {

	public String getStudentSchoolIdByName(String schoolName);
	
	/**
	 * 根据省份ID获取城市列表
	 * 
	 * @param provinceID 省份ID
	 * @return 返回省份下的城市列表
	 */
	List<StudentSchool> listSchoolByRegion(String provinceID, String cityID);
	
	/**
     * 分页查询学校
     * @param provinceId
     * @param cityId
     * @param name
     * @return
     */
    DataPackage listPageSchoolByRegionAndName(String provinceId, 
            String cityId, String name, DataPackage dp);
    
    /**
     * 根据学校id查询学校列表
     * @param schoolIds
     * @return
     */
    List<StudentSchool> listSchoolByIds(String[] schoolIds);
    
    /**
     * 根据全局编号查询学校
     * @param globalNumber
     * @return
     */
    StudentSchool findSchoolByGlobalNumber(String globalNumber);

}
