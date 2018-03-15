package com.pad.dao.impl;


import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsSignDao;
import com.pad.dto.CmsSignVo;
import com.pad.entity.CmsSign;
import org.apache.commons.lang.StringUtils;
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
public class CmsSignDaoImpl extends GenericDaoImpl<CmsSign,String> implements CmsSignDao{

    @Override
    public List<CmsSign> getCmsSignList(CmsSignVo vo) {
        Map param = new HashMap<>();
        StringBuilder hql = new StringBuilder();
        hql.append(" from CmsSign where 1=1");
        if(StringUtils.isNotBlank(vo.getType())){
            hql.append(" and type.id = :type ");
            param.put("type",vo.getType());
        }

        if(vo.getUseType()!=null){
            hql.append(" and useType = :useType");
            param.put("useType",vo.getUseType());
        }

        hql.append(" order by type ");
        return this.findAllByHQL(hql.toString(),param);
    }
}