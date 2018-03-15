package com.eduboss.dao.impl;

import com.eduboss.domain.Organization;
import com.eduboss.domain.User;
import com.eduboss.dto.MiniClassModalVo;
import com.eduboss.utils.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SmallClassDao;
import com.eduboss.domain.MiniClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("SmallClassDao")
public class SmallClassDaoImpl extends GenericDaoImpl<MiniClass, String>  implements SmallClassDao {


    @Override
    public List<MiniClass> getSmallClassListOnModal(MiniClassModalVo vo,User user) {

        Map<String,Object> param=new HashMap<>();
        StringBuilder hql = new StringBuilder();

        hql.append("from MiniClass where 1=1");

        if(StringUtils.isNotBlank(vo.getBrenchId())){
            hql.append( " and blCampus.parentId=:parentId");
            param.put("parentId",vo.getBrenchId());
        }
        if(StringUtils.isNotBlank(vo.getBlCampusId())){
            hql.append( " and blCampus.id=:blCampus");
            param.put("blCampus",vo.getBlCampusId());
        }

        if(vo.getBlcampusIds()!=null && vo.getBlcampusIds().length>0){
            int i=1;
            hql.append(" and (1=2 ");
            for(String id:vo.getBlcampusIds()){
                hql.append( " or blCampus.id=:blCampus"+i);
                param.put("blCampus"+i,id);
                i++;
            }
            hql.append(" )");
        }


        if(StringUtils.isNotBlank(vo.getProductVersionId())){
            hql.append( " and product.productVersion.id=:productVersion");
            param.put("productVersion",vo.getProductVersionId());
        }

        if(StringUtils.isNotBlank(vo.getProductQuarterId())){
            hql.append( " and product.productQuarter.id=:productQuarter");
            param.put("productQuarter",vo.getProductQuarterId());
        }

        if(StringUtils.isNotBlank(vo.getPhaseId())){
            hql.append( " and phase.id=:phase");
            param.put("phase",vo.getPhaseId());
        }

        if(StringUtils.isNotBlank(vo.getTeacherId())){
            if("-1".equals(vo.getTeacherId())){
                hql.append(" and teacher.userId is null");
            }else {
                hql.append(" and teacher.userId=:teacher");
                param.put("teacher", vo.getTeacherId());
            }
        }

        if(StringUtils.isNotBlank(vo.getClassroomId())){
            hql.append(" and classroom.id =:classRoomId");
            param.put("classRoomId",vo.getClassroomId());
        }

        if(StringUtils.isNotBlank(vo.getMiniClassId())){
            hql.append(" and miniClassId =:miniClassId");
            param.put("miniClassId",vo.getMiniClassId());
        }
        if(StringUtils.isNotBlank(vo.getName())){
            hql.append(" and name like :miniClassName");
            param.put("miniClassName","%"+vo.getName()+"%");
        }

        if(StringUtils.isNotBlank(vo.getClassTypeId())){
            hql.append(" and product.classType.id =:classTypeId");
            param.put("classTypeId",vo.getClassTypeId());
        }

        if(StringUtils.isNotBlank(vo.getIsModal())){
            hql.append(" and isModal =:isModal");
            param.put("isModal",Integer.valueOf(vo.getIsModal()));
        }

        hql.append(" and ( 1=2 ");
        for(Organization org :user.getOrganization()){
            hql.append(" or blCampus.orgLevel like '"+org.getOrgLevel()+"%'");
        }
        hql.append(" ) ");



        return this.findAllByHQL(hql.toString(),param);
    }

    @Override
    public List<MiniClass> getSmallClassListByTeacher(MiniClassModalVo vo, User user) {
        Map<String,Object> param=new HashMap<>();
        StringBuilder hql = new StringBuilder();

        hql.append("from MiniClass where 1=1");


        if(vo.getTeacherIds()!=null && vo.getTeacherIds().length>0){
            int i=1;
            hql.append(" and ( teacher.userId is null ");
            for(String id:vo.getTeacherIds()){
                hql.append( " or teacher.userId=:teacherId"+i);
                param.put("teacherId"+i,id);
                i++;
            }
            hql.append(" )");
        }

        if(StringUtils.isNotBlank(vo.getProductVersionId())){
            hql.append( " and product.productVersion.id=:productVersion");
            param.put("productVersion",vo.getProductVersionId());
        }

        if(StringUtils.isNotBlank(vo.getProductQuarterId())){
            hql.append( " and product.productQuarter.id=:productQuarter");
            param.put("productQuarter",vo.getProductQuarterId());
        }


        if(StringUtils.isNotBlank(vo.getIsModal())){
            hql.append(" and isModal =:isModal");
            param.put("isModal",Integer.valueOf(vo.getIsModal()));
        }

        return this.findAllByHQL(hql.toString(),param);
    }

    @Override
    public String findProductIdByMiniClassId(String miniClassId) {
        Map param= new HashMap();
        String sql = "select PRODUCE_ID productId from mini_class where mini_class_id = :miniClassId";
        param.put("miniClassId",miniClassId);
        List<Map<Object, Object>> list = findMapBySql(sql,param);

        if(list.size()>0) {
            Map map = list.get(0);
            return map.get("productId").toString();
        }
        return null;
    }
}
