package com.eduboss.dao.impl;

import com.eduboss.dao.RoleQLConfigDao;
import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.dto.RoleQLConfigVo;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuwen on 2014/12/12.
 */
@Repository
public class RoleQLConfigDaoImpl extends GenericDaoImpl<RoleQLConfig,String> implements RoleQLConfigDao {

    @Override
    public List<RoleQLConfig> findRoleQlByCondition(RoleQLConfigSearchVo vo) {
        StringBuilder hql = new StringBuilder();
        Map param = new HashMap();
        hql.append(" from RoleQLConfig where 1 = 1");

        if(StringUtils.isNotBlank(vo.getName())) {
            hql.append(" and name = :name");
            param.put("name",vo.getName());

        }
        if(StringUtils.isNotBlank(vo.getType())) {
            hql.append(" and type = :type");
            param.put("type",vo.getType());

        }
        if(StringUtils.isNotBlank(vo.getResource())) {
            hql.append(" and resource = :resource");
            param.put("resource",vo.getResource());
        }

        return this.findAllByHQL(hql.toString(),param);
    }
}
