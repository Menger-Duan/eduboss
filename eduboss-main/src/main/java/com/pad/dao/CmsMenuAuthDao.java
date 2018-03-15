package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.entity.CmsMenu;
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
public interface CmsMenuAuthDao extends GenericDAO<CmsMenuAuth,String> {

    List<CmsMenuAuth> findMenuAuthByMenuId(Integer cmsMenuId);
}