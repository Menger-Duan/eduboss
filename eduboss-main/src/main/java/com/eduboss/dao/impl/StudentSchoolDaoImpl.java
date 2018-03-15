package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Expression;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.StudentSchoolDao;
import com.eduboss.domain.StudentSchool;
import com.eduboss.dto.DataPackage;
import com.eduboss.utils.StringUtil;

@Repository
public class StudentSchoolDaoImpl extends GenericDaoImpl<StudentSchool, String> implements StudentSchoolDao {

	@Override
	public String getStudentSchoolIdByName(String schoolName) {
		List<StudentSchool> list= findByCriteria(Expression.eq("name", schoolName));
		if(list!=null && list.size()>0){
			return list.get(0).getId();
		}
		return null;
	}
	
	/**
	 * 根据省份ID获取城市列表
	 */
	@Override
	public List<StudentSchool> listSchoolByRegion(String provinceID,
			String cityID) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from StudentSchool where 1=1 ";
		if (StringUtil.isNotBlank(provinceID)) {
			hql += " and province.id = :provinceID ";
			params.put("provinceID", provinceID);
		}
		if (StringUtil.isNotBlank(cityID)) {
			hql += " and city.id = :cityID ";
			params.put("cityID", cityID);
		}
		return super.findAllByHQL(hql, params);
	}

    @Override
    public DataPackage listPageSchoolByRegionAndName(String provinceId,
            String cityId, String name, DataPackage dp) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from StudentSchool where 1=1 and globalNumber is not null ";
        if (StringUtil.isNotBlank(provinceId)) {
            hql += " and province.id = :provinceId ";
            params.put("provinceId", provinceId);
        }
        if (StringUtil.isNotBlank(cityId)) {
            hql += " and city.id = :cityId ";
            params.put("cityId", cityId);
        }
        if (StringUtil.isNotBlank(name)) {
            hql += " and name like :name ";
            params.put("name", "%" + name + "%");
        }
        return super.findPageByHQL(hql, dp, true, params);
    }

    @Override
    public List<StudentSchool> listSchoolByIds(String[] schoolIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from StudentSchool where 1=1 ";
        hql += " and id in (:schoolIds) ";
        params.put("schoolIds", schoolIds);
        return super.findAllByHQL(hql, params);
    }

    @Override
    public StudentSchool findSchoolByGlobalNumber(String globalNumber) {
        StudentSchool result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        String hql = " from StudentSchool where 1=1 ";
        hql += " and globalNumber = :globalNumber ";
        params.put("globalNumber", globalNumber);
        List<StudentSchool> list = super.findAllByHQL(hql, params);
        if (list != null && !list.isEmpty()) {
            result = list.get(0);
        }
        return result;
    }
	
}
