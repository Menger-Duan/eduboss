package com.pad.dao;


import com.eduboss.dao.GenericDAO;
import com.pad.common.CmsUseType;
import com.pad.dto.CmsMenuVo;
import com.pad.entity.CmsMenu;

import org.springframework.stereotype.Repository;

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
public interface CmsMenuDao extends GenericDAO<CmsMenu,String> {

    List getCmsMenuList(CmsMenuVo cmsMenu);

    List getCmsMenuListByParam(CmsMenuVo cmsMenu);

    /**
     * 根据级别和使用类型获取菜单列表
     * @param menuLevel
     * @param visitor
     * @return
     */
    List<CmsMenu> findAllByLevel(Integer menuLevel, CmsUseType visitor);

    List<CmsMenu> getCmsMenuByOrgId(String id,Integer level);

    List<CmsMenu> getCmsMenuByParentId(Integer id);

    List<Map<Object, Object>> getFistLevelName(Integer menuId);
}