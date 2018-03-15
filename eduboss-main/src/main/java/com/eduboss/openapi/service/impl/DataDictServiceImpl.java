package com.eduboss.openapi.service.impl;

import com.eduboss.domain.DataDict;
import com.eduboss.domain.Region;
import com.eduboss.domain.StudentSchool;
import com.eduboss.openapi.dao.DataDictDao;
import com.eduboss.openapi.dao.RegionDao;
import com.eduboss.openapi.dao.StudentSchoolDao;
import com.eduboss.openapi.service.DataDictService;
import com.eduboss.rpc.dto.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/23.
 */
@Service("DataDictServiceRpc")
public class DataDictServiceImpl implements DataDictService {

    @Autowired
    private DataDictDao dataDictDao;

    @Autowired
    private RegionDao regionDao;

    @Autowired
    private StudentSchoolDao studentSchoolDao;

    /**
     * 获取全部年级信息
     *
     * @return 返回年级列表
     */
    @Override
    public List<Label> listAllGrade() {
        List<Label> result = new ArrayList<>();
        List<DataDict> allGrade = dataDictDao.listAllGrade();
        for (DataDict d : allGrade){
            Label grade = new Label();
            grade.setId(d.getId());
            grade.setText(d.getName());
            result.add(grade);
        }
        return result;
    }


    /**
     * 获取全部省份列表
     *
     * @return 返回省份列表
     */
    @Override
    public List<Label> listAllProvince() {
        List<Label> result = new ArrayList<>();
        List<Region> allProvinces = regionDao.listAllProvince();
        for (Region r : allProvinces){
            Label province = new Label();
            province.setId(r.getId());
            province.setText(r.getName());
            result.add(province);
        }
        return result;
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceID 省份ID
     * @return 返回省份下的城市列表
     */
    @Override
    public List<Label> listCityByProvinceID(String provinceID) {
        List<Label> result = new ArrayList<>();
        List<Region> citys = regionDao.listCityByProvinceID(provinceID);
        for (Region r : citys){
                Label city = new Label();
                city.setId(r.getId());
                city.setText(r.getName());
                result.add(city);
            }
        return result;
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
        List<Label> result = new ArrayList<>();
        List<StudentSchool> studentSchools = studentSchoolDao.listSchoolByRegion(provinceID, cityID);
        for (StudentSchool s : studentSchools){
            Label school = new Label();
            school.setId(s.getId());
            school.setText(s.getName());
            result.add(school);
        }
        return result;
    }

//    private List<Label> voMapping(List<T> list){
//        List<Label> result = new ArrayList<>();
//        for (T t : list){
//            Label label = new Label();
//            label.setId(t.getId());
//            label.setText(t.getName());
//            result.add(label);
//        }
//        return result;
//    }


}
