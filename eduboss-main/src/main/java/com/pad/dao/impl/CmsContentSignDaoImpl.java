package com.pad.dao.impl;

import com.eduboss.dao.GenericDAO;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsContentSignDao;
import com.pad.entity.CmsContentSign;
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
public class CmsContentSignDaoImpl extends GenericDaoImpl<CmsContentSign,String> implements CmsContentSignDao{

    @Override
    public List<CmsContentSign> findAllByContentId(Integer id) {
        String hql = " from CmsContentSign where cmsContentId = :cmsContentId";
        Map param = new HashMap();
        param.put("cmsContentId",id);
        return this.findAllByHQL(hql,param);
    }

    @Override
    public List<CmsContentSign> findAllBySignId(Integer signId) {
        String hql = " from CmsContentSign where cmsSignId = :cmsSignId";
        Map param = new HashMap();
        param.put("cmsSignId",signId);
        return this.findAllByHQL(hql,param);
    }
}