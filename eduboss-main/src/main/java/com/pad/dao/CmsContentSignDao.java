package com.pad.dao;

import com.eduboss.dao.GenericDAO;
import com.pad.entity.CmsContentSign;
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
public interface CmsContentSignDao extends GenericDAO<CmsContentSign,String> {

    List<CmsContentSign> findAllByContentId(Integer id);

    List<CmsContentSign> findAllBySignId(Integer signId);
}