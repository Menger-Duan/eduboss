package com.eduboss.openapi.dao;

import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.Region;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */
public interface RegionDao extends GenericDAO<Region, String> {
    /**
     * 获取全部省份列表
     *
     * @return 返回省份列表
     */
    List<Region> listAllProvince();

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceID 省份ID
     * @return 返回省份下的城市列表
     */
    List<Region> listCityByProvinceID(String provinceID);
}
