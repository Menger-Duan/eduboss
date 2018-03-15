package com.eduboss.openapi.service;


import com.eduboss.rpc.dto.Label;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */
public interface DataDictService {

    /**
     * 获取全部年级信息
     *
     * @return 返回年级列表
     */
    List<Label> listAllGrade();


    /**
     * 获取全部省份列表
     *
     * @return 返回省份列表
     */
    List<Label> listAllProvince();

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceID 省份ID
     * @return 返回省份下的城市列表
     */
    List<Label> listCityByProvinceID(String provinceID);


    /**
     * 根据位置信息获取学校列表
     *
     * @param provinceID 省份ID
     * @param cityID     城市ID
     * @return 返回学校列表
     */
    List<Label> listSchoolByRegion(String provinceID, String cityID);


}
