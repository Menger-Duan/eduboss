package com.eduboss.dao.impl;

import com.eduboss.dao.CourseModalDao;
import com.eduboss.domain.CourseModal;
import com.eduboss.domainVo.CourseModalVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository("CourseModalDao")
public class CourseModalDaoImpl extends GenericDaoImpl<CourseModal, String> implements CourseModalDao {

    @Override
    public List<CourseModal> getCourseModalList(CourseModalVo vo) {
        Map<String,Object> map = new HashMap<>();
        String hql="from CourseModal where 1=1 ";

        if(StringUtils.isNotBlank(vo.getBranchId())){
            hql+=" and branch.id = :branchId";
            map.put("branchId",vo.getBranchId());
        }
        if(StringUtils.isNotBlank(vo.getProductYearId())){
            hql+=" and productYear.id = :productYearId";
            map.put("productYearId",vo.getProductYearId());
        }

        if(StringUtils.isNotBlank(vo.getModalName())){
            hql+=" and modalName = :modalName";
            map.put("modalName",vo.getModalName());
        }
        if(StringUtils.isNotBlank(vo.getProductSeasonId())){
            hql+=" and productSeason.id = :productSeason";
            map.put("productSeason",vo.getProductSeasonId());
        }
        if(vo.getTechNum()>0){
            hql+=" and techNum = :techNum";
            map.put("techNum",vo.getTechNum());
        }

        hql+=" order by isnull(classTime),classTime ";

        return this.findAllByHQL(hql,map);
    }

}
