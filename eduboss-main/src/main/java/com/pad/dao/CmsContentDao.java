package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.common.CmsContentStatus;
import com.pad.common.CmsContentType;
import com.pad.dto.CmsContentVo;
import com.pad.dto.CommonDto;
import com.pad.entity.CmsContent;
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
public interface CmsContentDao extends GenericDAO<CmsContent,String> {

    List<CmsContent> getCmsContentList(CmsContentVo vo,CommonDto dto);

    List<CmsContent> getCmsContentList(CmsContentVo vo);

    List<CmsContent> findFrontContentByMenu(Integer menuId);

    List<CmsContent> getCmsContentByParam(CmsContentVo vo);

    List<CmsContent> getCmsContentByType(CmsContentStatus[] types,Integer menuId);
}