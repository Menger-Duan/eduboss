package com.pad.dao.impl;


import com.eduboss.dao.GenericDAO;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.dao.CmsContentFileDao;
import com.pad.entity.CmsContentFile;
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
public class CmsContentFileDaoImpl extends GenericDaoImpl<CmsContentFile,String> implements CmsContentFileDao{

    @Override
    public List<CmsContentFile> getAllContentFileByContentId(Integer contentId) {
        String hql =" from CmsContentFile where cmsContentId=:cmsContentId";
        Map map = new HashMap();
        map.put("cmsContentId",contentId);
        return this.findAllByHQL(hql,map);
    }
}