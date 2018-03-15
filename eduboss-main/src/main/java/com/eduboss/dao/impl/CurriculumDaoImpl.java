package com.eduboss.dao.impl;


import com.eduboss.dao.CurriculumDao;
import com.eduboss.domain.Curriculum;
import com.eduboss.exception.ApplicationException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CurriculumDaoImpl extends GenericDaoImpl<Curriculum, String> implements CurriculumDao {
    @Override
    public void save(Curriculum curriculum) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("requestId", curriculum.getRequestId());
//        int count = this.findCountSql("SELECT count(*) FROM curriculum WHERE request_id=:requestId", map);
//        if (count>0){
//            throw new ApplicationException("重复的请求");
//        }
        super.save(curriculum);
    }
}
