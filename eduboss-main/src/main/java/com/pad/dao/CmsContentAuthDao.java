package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.entity.CmsContentAuth;
import com.pad.entity.CmsMenuAuth;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Repository
public interface CmsContentAuthDao extends GenericDAO<CmsContentAuth,String> {

    List<CmsContentAuth> findContentAuthByContentId(Integer cmsContentId);
}