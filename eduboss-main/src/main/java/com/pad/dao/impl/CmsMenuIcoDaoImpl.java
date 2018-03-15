package com.pad.dao.impl;


import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsMenuIcoDao;
import com.pad.dto.CmsMenuIcoVo;
import com.pad.entity.CmsMenuIco;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Spark
 * @since 2017-11-30
 */
@Repository
public class CmsMenuIcoDaoImpl extends GenericDaoImpl<CmsMenuIco,String> implements CmsMenuIcoDao {

    @Override
    public List<CmsMenuIco> findIcoByStatusAndLevel(CmsMenuIcoVo vo) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from CmsMenuIco where 1=1 ");
        Map param = new HashMap();
        if(StringUtils.isNotBlank(vo.getStatus())){
            hql.append(" and status=:status");
            param.put("status",vo.getStatus());
        }
        if(vo.getMenuLevel()!=null) {
            hql.append(" and menuLevel=:level");
            param.put("level", vo.getMenuLevel());
        }

        if(vo.getMenuId()!=null && vo.getMenuId()>0){
            hql.append(" and menuId=:menuId");
            param.put("menuId", vo.getMenuId());
        }
        return this.findAllByHQL(hql.toString(),param);
    }

    @Override
    public List<CmsMenuIco> findIcoByMenuId(Integer id) {
        String hql = " from CmsMenuIco where menuId = :menuId";
        Map param = new HashMap();
        param.put("menuId",id);
        return this.findAllByHQL(hql,param);
    }
}