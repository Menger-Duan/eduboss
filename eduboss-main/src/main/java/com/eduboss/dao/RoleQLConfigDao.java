package com.eduboss.dao;

import com.eduboss.domain.RoleQLConfig;
import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.dto.RoleQLConfigVo;

import java.util.List;

/**
 * Created by Administrator on 2014/12/11.
 */
public interface RoleQLConfigDao extends GenericDAO<RoleQLConfig,String>{

    List<RoleQLConfig> findRoleQlByCondition(RoleQLConfigSearchVo vo);
}
