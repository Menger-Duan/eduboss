package com.pad.dao.impl;


import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.common.CmsUseType;
import com.pad.dto.CmsMenuVo;
import com.pad.entity.CmsMenu;
import com.pad.dao.CmsMenuDao;
import org.apache.commons.lang.StringUtils;
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
public class CmsMenuDaoImpl extends GenericDaoImpl<CmsMenu,String> implements CmsMenuDao {

    @Override
    public List getCmsMenuList(CmsMenuVo cmsMenu) {
        StringBuilder sql = new StringBuilder();
        Map param = new HashMap();
        sql.append("select cm.* from cms_menu cm where 1=1");

        if(StringUtils.isNotBlank(cmsMenu.getOrgId())){
            sql.append(" and ( cm.all_can_see=0 or exists(select 1 from cms_menu_auth cma where cma.cms_menu_id = cm.id and cma.org_Id =:orgId))");
            param.put("orgId",cmsMenu.getOrgId());
        }

        if(StringUtils.isNotBlank(cmsMenu.getLevel())){
            sql.append(" and level =:level");
            param.put("level",cmsMenu.getLevel());
        }


        if(cmsMenu.getUseType()!=null){
            sql.append(" and  use_type = :useType");
            param.put("useType",cmsMenu.getUseType());
        }

        if(StringUtils.isNotBlank(cmsMenu.getName())){
            sql.append(" and name like :name ");
            param.put("name","%"+cmsMenu.getName()+"%");
        }

        if(cmsMenu.getParentId()!=null){
            sql.append(" and parent_id = :parentId");
            param.put("parentId",cmsMenu.getParentId());
        }

        sql.append(" order by cm.order_num");

        return this.findBySql(sql.toString(),param);
    }


    @Override
    public List getCmsMenuListByParam(CmsMenuVo cmsMenu) {
        StringBuilder sql = new StringBuilder();
        Map param = new HashMap();
        sql.append("select cm.* from cms_menu cm where 1=1");

        if(cmsMenu.getUseType()!=null){
            sql.append(" and  use_type = :useType");
            param.put("useType",cmsMenu.getUseType());
        }

        if(StringUtils.isNotBlank(cmsMenu.getName())){
            sql.append(" and name = :name ");
            param.put("name",cmsMenu.getName());
        }

        if(cmsMenu.getParentId()!=null && cmsMenu.getParentId()>0){
            sql.append(" and parent_id = :parentId");
            param.put("parentId",cmsMenu.getParentId());
        }

        if(StringUtils.isNotBlank(cmsMenu.getLevel())){
            sql.append(" and level = :level");
            param.put("level",cmsMenu.getLevel());
        }

        sql.append(" order by cm.order_num");
        return this.findBySql(sql.toString(),param);
    }

    @Override
    public List<CmsMenu> findAllByLevel(Integer menuLevel, CmsUseType visitor) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from CmsMenu where useType =:useType ");
        Map param = new HashMap();
        param.put("useType",visitor);
        if(menuLevel!=null){
            hql.append(" and level=:level" );
            param.put("level",menuLevel.toString());
        }
        return this.findAllByHQL(hql.toString(),param);
    }

    @Override
    public List<CmsMenu> getCmsMenuByOrgId(String id,Integer level) {
        StringBuilder sql = new StringBuilder();
        Map param = new HashMap();
        sql.append("select cm.* from cms_menu cm where 1=1");

        if(StringUtils.isNotBlank(id)){
            sql.append(" and ( cm.all_can_see=0 or exists(select 1 from cms_menu_auth cma where cma.cms_menu_id = cm.id and cma.org_Id =:orgId))");
            param.put("orgId",id);
        }

        if(level !=null && level>=0){
            sql.append(" and level =:level");
            param.put("level",level);
        }
        sql.append(" order by cm.order_num");

        return this.findBySql(sql.toString(),param);
    }

    @Override
    public List<CmsMenu> getCmsMenuByParentId(Integer id) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from CmsMenu where parentId =:parentId ");
        Map param = new HashMap();
        param.put("parentId",id);
        hql.append(" order by orderNum ");
        return this.findAllByHQL(hql.toString(),param);
    }

    @Override
    public List<Map<Object, Object>> getFistLevelName(Integer menuId) {
        Map param = new HashMap();
        param.put("menuId",menuId);
        StringBuilder sql = new StringBuilder();
        sql.append(" select (case when cm.`level`=1 then cm.id else cmm.id end) id,");
        sql.append(" (case when cm.`level`=1 then cm.name else cmm.name end) name");
        sql.append(" from cms_menu cm ");
        sql.append(" left join cms_menu cmm on cm.parent_id=cmm.id");
        sql.append(" where cm.id=:menuId");
        return this.findMapBySql(sql.toString(),param);
    }
}