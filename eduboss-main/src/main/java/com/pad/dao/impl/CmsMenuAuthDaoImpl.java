package com.pad.dao.impl;


import com.eduboss.dao.GenericDAO;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsMenuAuthDao;
import com.pad.entity.CmsMenu;
import com.pad.entity.CmsMenuAuth;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Repository
public class CmsMenuAuthDaoImpl extends GenericDaoImpl<CmsMenuAuth,String> implements CmsMenuAuthDao{

    @Override
    public List<CmsMenuAuth> findMenuAuthByMenuId(Integer cmsMenuId) {
        Map map = new HashMap();
        String hql = "from CmsMenuAuth where cmsMenuId = :cmsMenuId";
        map.put("cmsMenuId",cmsMenuId);
        return this.findAllByHQL(hql,map);
    }

}