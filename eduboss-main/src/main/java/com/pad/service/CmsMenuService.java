package com.pad.service;


import com.eduboss.domain.Organization;
import com.eduboss.dto.Response;
import com.pad.dto.CmsMenuVo;
import com.pad.entity.CmsMenu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Service
public interface CmsMenuService {

    /**
     * 保存菜单
     * @param cmsMenu
     * @return
     */
    Response saveCmsMenu(CmsMenuVo cmsMenu);

    /**
     * 获取菜单列表
     * @param cmsMenu
     * @return
     */
    List<CmsMenuVo> getCmsMenuList(CmsMenuVo cmsMenu);

    List<CmsMenuVo> getCmsMenuListForIpad(CmsMenuVo cmsMenu);

    /**
     * 保存可见范围
     * @param cmsMenu
     * @return
     */
    Response saveCmsMenuAuth(CmsMenuVo cmsMenu);

    Response saveCmsMenuOrder(List<CmsMenuVo> cmsMenu);

    List<CmsMenuVo> getCmsMenuByOrgId(String id,Integer level);

    List<Organization> getAllBrenchAndGroup();

    Response cacelMenuBySelf(Integer id, String branchId);

    Response saveCmsMenuContentNum(Integer id, Integer showContentNum);

    Response deleteCmsMenu(Integer id);

    /**
     * 设置是否显示目录下的图文内容
     * @param cmsMenu
     * @return
     */
    Response setShowMenuContent(CmsMenuVo cmsMenu);

    /**
     * 根据父节点获取菜单
     * @param id
     * @return
     */
    List<CmsMenuVo> findMenuByParentId(Integer id);

    /**
     * 获取一级目录名字ID
     * @param menuId
     * @return
     */
    Map getFistLevelName(Integer menuId);

    /**
     * 获取集团跟所属分公司
     * @return
     */
    Response getBrenchAndGroup();
}
