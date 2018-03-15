package com.eduboss.service;

import com.eduboss.domain.Region;
import com.eduboss.domainVo.RegionVo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public interface RegionService {
    /**
     * 获取所有省份
     * @return
     */
    List<RegionVo> getAllProvinces();

    /**
     * 通过省份获取城市列表
     * @param provinceId
     * @return
     */
    List<RegionVo> getCitys(String provinceId);

    /**
     * 通过城市获取县列表
     * @param cityId
     * @return
     */
    List<RegionVo> getCounties(String cityId);

    RegionVo getRegionVoById(String regionId);

    Region getRegionById(String regionId);
}
