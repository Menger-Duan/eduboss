package com.pad.dao.impl;


import com.eduboss.dao.GenericDAO;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsBindIcoDao;
import com.pad.entity.CmsBindIco;
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
public class CmsBindIcoDaoImpl extends GenericDaoImpl<CmsBindIco,String> implements CmsBindIcoDao {

    @Override
    public List<CmsBindIco> findAllBindIcoByIcoId(Integer id) {
        String hql= " from CmsBindIco where icoId=:icoId";
        Map param = new HashMap();
        param.put("icoId",id);
        return this.findAllByHQL(hql,param);
    }

    @Override
    public List<CmsBindIco> findInfoByMenuId(Integer id) {
        String hql= " from CmsBindIco where menuId=:menuId";
        Map param = new HashMap();
        param.put("menuId",id);
        return this.findAllByHQL(hql,param);
    }

}