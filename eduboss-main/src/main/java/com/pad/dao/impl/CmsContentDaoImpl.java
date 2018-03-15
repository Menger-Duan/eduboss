package com.pad.dao.impl;


import com.eduboss.common.OrganizationType;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.common.CmsContentStatus;
import com.pad.common.CmsContentType;
import com.pad.dto.CmsContentVo;
import com.pad.dto.CommonDto;
import com.pad.entity.CmsContent;
import com.pad.dao.CmsContentDao;
import javafx.beans.binding.StringBinding;
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
public class CmsContentDaoImpl extends GenericDaoImpl<CmsContent,String> implements CmsContentDao {

    @Override
    public List<CmsContent> getCmsContentList(CmsContentVo vo,CommonDto dto) {
        Map param = new HashMap();
        StringBuilder sql = new StringBuilder();
        sql.append("select cc.* from cms_content as cc where (is_del=0 or is_del is null) ");

        if(vo.getMenuId()!=null){
            sql.append(" and cc.menu_id =:menuId");
            param.put("menuId",vo.getMenuId());
        }

        if(vo.getIsFront()!=null){
            sql.append(" and cc.is_front=:isFront");
            param.put("isFront",vo.getIsFront());
        }

        if(vo.getStatus() !=null ){
            sql.append(" and status =:status");
            param.put("status", vo.getStatus());
        }

        if(StringUtils.isNotBlank(vo.getTitle())){
            sql.append(" and cc.title like :title");
            param.put("title","%"+vo.getTitle()+"%");
        }


        if(vo.getOrgType() !=null){
            sql.append(" and cc.org_type=:orgType");
            param.put("orgType", vo.getOrgType());
        }

        if(StringUtils.isNotBlank(dto.getBrenchId())){
            sql.append(" and (cc.all_can_see= 0  or exists(select 1 from cms_content_auth as cca where cca.cms_content_id = cc.id and cca.org_id =:orgId))");
            param.put("orgId",dto.getBrenchId());
        }

        if(StringUtils.isNotBlank(dto.getSignIds())){
            int i = 0;
            for(String signIds:dto.getSignIds().split(",")){
                sql.append(" and exists (select 1 from cms_content_sign ccs where ccs.cms_content_id = cc.id ");
                sql.append(" and ccs.cms_sign_id =:signIds"+i);
                sql.append(" )");
                param.put("signIds"+i,signIds);
                i++;
            }
        }

        if(StringUtils.isNotBlank(dto.getOrderBy())){
            sql.append(" order by  cc."+dto.getOrderBy());
            if(StringUtils.isNotBlank(dto.getOrderType())){
                sql.append(" "+dto.getOrderType());
            }
        }else{
            sql.append(" order by  cc.order_num ");
        }

        if(dto.getPage()!=null && dto.getSize()!=null && dto.getPage()>0 && dto.getSize()>0){
            sql.append(" limit "+(dto.getPage()-1)*dto.getSize()+" ,"+dto.getPage()*dto.getSize());
        }


        return this.findBySql(sql.toString(),param);
    }

    @Override
    public List<CmsContent> getCmsContentList(CmsContentVo vo) {
        String hql = " from CmsContent where 1=1 ";
        Map param = new HashMap();
        if(vo.getMenuId()!=null && vo.getMenuId()>0){
            hql+=" and menuId =:menuId";
            param.put("menuId",vo.getMenuId());
        }

        return this.findAllByHQL(hql,param);
    }

    @Override
    public List<CmsContent> findFrontContentByMenu(Integer menuId) {
        String hql = " from CmsContent where (isDel=0 or isDel is null) and menuId =:menuId and isFront = 0 ";
        Map param = new HashMap();

        param.put("menuId",menuId);
        return this.findAllByHQL(hql,param);
    }

    @Override
    public List<CmsContent> getCmsContentByParam(CmsContentVo vo) {
        StringBuilder sql = new StringBuilder();
        Map param = new HashMap();
        sql.append("select cc.* from cms_content cc where 1=1");


        if(vo.getMenuId()!=null){
            sql.append(" and cc.menu_id =:menuId");
            param.put("menuId",vo.getMenuId());
        }

        if(StringUtils.isNotBlank(vo.getTitle())){
            sql.append(" and cc.title = :title");
            param.put("title",vo.getTitle());
        }

        sql.append(" order by cc.order_num");
        return this.findBySql(sql.toString(),param);
    }

    @Override
    public List<CmsContent> getCmsContentByType(CmsContentStatus[] types,Integer menuId) {
        String hql = "from CmsContent where status in (:types) and menuId = :menuId";
        Map param = new HashMap();
        param.put("types",types);
        param.put("menuId",menuId);
        return this.findAllByHQL(hql,param);
    }

}