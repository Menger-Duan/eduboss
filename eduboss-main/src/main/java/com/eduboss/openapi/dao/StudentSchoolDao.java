package com.eduboss.openapi.dao;

import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.StudentSchool;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */
public interface StudentSchoolDao  extends GenericDAO<StudentSchool, String> {


    /**
     * 根据位置信息获取学校列表
     *
     * @param provinceID 省份ID
     * @param cityID     城市ID
     * @return 返回学校列表
     */
    List<StudentSchool> listSchoolByRegion(String provinceID, String cityID);

}
