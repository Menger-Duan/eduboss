package com.eduboss.openapi.dao.impl;

import com.eduboss.dao.impl.GenericDaoImpl;
import com.eduboss.domain.Region;
import com.eduboss.openapi.dao.RegionDao;
import com.eduboss.utils.StringUtil;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 */
@Repository(value = "RegionDaoRpc")
public class RegionDaoImpl extends GenericDaoImpl<Region, String> implements RegionDao {
    /**
     * 获取全部省份列表
     *
     * @return 返回省份列表
     */
    @Override
    public List<Region> listAllProvince() {
        List<Region> allProvinces = this.findByCriteria(Restrictions.eq("level", 1));
        return allProvinces;
    }

    /**
     * 根据省份ID获取城市列表
     *
     * @param provinceID 省份ID
     * @return 返回省份下的城市列表
     */
    @Override
    public List<Region> listCityByProvinceID(String provinceID) {
        List<Region> citys = new ArrayList<>();
        if (StringUtil.isNotBlank(provinceID)){
            Map<String, Object> params = new HashMap<>();
            params.put("provinceId", provinceID);
            citys =this.findAllByHQL("from Region where parent.id= :provinceId ",params);
        }
        return citys;
    }
}
