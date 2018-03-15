package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.dto.CmsSignVo;
import com.pad.entity.CmsSign;
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
public interface CmsSignDao extends GenericDAO<CmsSign,String> {

    List<CmsSign> getCmsSignList(CmsSignVo vo);
}