package com.eduboss.service.impl;

import com.eduboss.dao.RegionDao;
import com.eduboss.domain.Region;
import com.eduboss.domainVo.RegionVo;
import com.eduboss.service.RegionService;
import com.eduboss.utils.HibernateUtils;
import com.google.common.collect.Maps;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/22.
 */

@Service
@Transactional
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionDao regionDao;

    /**
     * 获取所有省份
     *
     * @return
     */
    @Override
    public List<RegionVo> getAllProvinces() {
        List<RegionVo> provinces = HibernateUtils.voListMapping(regionDao.findByCriteria(Restrictions.eq("level", 1)), RegionVo.class);
        List<RegionVo> citys = HibernateUtils.voListMapping(regionDao.findByCriteria(Restrictions.eq("level", 2)), RegionVo.class);
        for (RegionVo city : citys) {
            for (RegionVo province : provinces) {
                if(province.getChildren() == null){
                    province.setChildren(new ArrayList<RegionVo>());
                }
                if(city.getId().substring(0,2).equals(province.getId().substring(0,2))){
                    province.getChildren().add(city);
                }
            }
        }
        return provinces;
    }

    /**
     * 通过省份获取城市列表
     *
     * @param provinceId
     * @return
     */
    @Override
    public List<RegionVo> getCitys(String provinceId) {
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("provinceId", provinceId);
        List<Region> list1=regionDao.findAllByHQL("from Region where parent.id= :provinceId ",params);
        return HibernateUtils.voListMapping(list1, RegionVo.class);
    }

    /**
     * 通过城市获取县列表
     *
     * @param cityId
     * @return
     */
    @Override
    public List<RegionVo> getCounties(String cityId) {
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("cityId", cityId);
        return HibernateUtils.voListMapping(regionDao.findAllByHQL("from Region where parent.id= :cityId ",params), RegionVo.class);
    }


    @Override
    public RegionVo getRegionVoById(String regionId) {
        List<Region> list=regionDao.findByCriteria(Restrictions.eq("id", regionId));
        if(list!=null && list.size()>0){
            return HibernateUtils.voObjectMapping(list.get(0),RegionVo.class);
        }
        return null;
    }

    @Override
    public Region getRegionById(String regionId) {
        List<Region> list=regionDao.findByCriteria(Restrictions.eq("id", regionId));
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
