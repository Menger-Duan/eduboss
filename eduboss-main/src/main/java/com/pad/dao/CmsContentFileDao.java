package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.entity.CmsContentFile;
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
public interface CmsContentFileDao extends GenericDAO<CmsContentFile,String> {

    List<CmsContentFile> getAllContentFileByContentId(Integer contentId);

}