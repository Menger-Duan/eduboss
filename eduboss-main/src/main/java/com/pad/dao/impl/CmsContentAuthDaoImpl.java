package com.pad.dao.impl;


import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsContentAuthDao;
import com.pad.dao.CmsMenuAuthDao;
import com.pad.entity.CmsContentAuth;
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
public class CmsContentAuthDaoImpl extends GenericDaoImpl<CmsContentAuth,String> implements CmsContentAuthDao {


    @Override
    public List<CmsContentAuth> findContentAuthByContentId(Integer cmsContentId) {
        Map map = new HashMap();
        String hql = "from CmsContentAuth where cmsContentId = :cmsContentId";
        map.put("cmsContentId",cmsContentId);
        return this.findAllByHQL(hql,map);
    }
}