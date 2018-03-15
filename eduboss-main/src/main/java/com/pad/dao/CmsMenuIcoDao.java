package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.dto.CmsMenuIcoVo;
import com.pad.entity.CmsBindIco;
import com.pad.entity.CmsMenuIco;
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
public interface CmsMenuIcoDao extends GenericDAO<CmsMenuIco,String> {

    /**
     * 获取图片管理列表
     * @param vo
     * @return
     */
    List<CmsMenuIco> findIcoByStatusAndLevel(CmsMenuIcoVo vo);

    /**
     * 根据目录获取图标信息
     * @param id
     * @return
     */
    List<CmsMenuIco> findIcoByMenuId(Integer id );
}