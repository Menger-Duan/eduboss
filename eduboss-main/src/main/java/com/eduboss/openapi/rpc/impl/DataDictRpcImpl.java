package com.eduboss.openapi.rpc.impl;

import com.eduboss.openapi.service.DataDictService;
import com.eduboss.rpc.DataDictInnerRpc;
import com.eduboss.rpc.dto.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22.
 */
@Component("DataDictInnerRpc")
public class DataDictRpcImpl implements DataDictInnerRpc {


    @Autowired
    private DataDictService dataDictService;

    /**
     * 获取全部省份列表
     *
     * @return 返回省份列表
     */
    @Override
    public List<Label> listAllProvince() {
        return dataDictService.listAllProvince();
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceID 省份ID
     * @return 返回省份下的城市列表
     */
    @Override
    public List<Label> listCityByProvinceID(String provinceID) {
        List<Label> citys = dataDictService.listCityByProvinceID(provinceID);
        return citys;
    }

    /**
     * 根据位置信息获取学校列表
     *
     * @param provinceID 省份ID
     * @param cityID     城市ID
     * @return 返回学校列表
     */
    @Override
    public List<Label> listSchoolByRegion(String provinceID, String cityID) {
        List<Label> studentSchools = dataDictService.listSchoolByRegion(provinceID, cityID);
        return studentSchools;
    }

    /**
     * 获取全部年级信息
     *
     * @return 返回年级列表
     */
    @Override
    public List<Label> listAllGrade() {
        List<Label> allGrade = dataDictService.listAllGrade();
        return allGrade;
    }
}
