package com.eduboss.dao.impl;

import com.eduboss.dao.TwoTeacherClassDao;
import com.eduboss.domain.TwoTeacherClass;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/30.
 */
@Repository("TwoTeacherClassDao")
public class TwoTeacherClassDaoImpl extends GenericDaoImpl<TwoTeacherClass, String> implements TwoTeacherClassDao {
    /**
     * 查询是否有重名的双师班级
     *
     * @param teacherClass
     * @return
     */
    @Override
    public boolean checkNameDuplicate(TwoTeacherClass teacherClass,Set<String> blBrenchId) {
        StringBuffer sql = new StringBuffer();
        Map<String, Object> map = new HashMap<>();
        //sql.append(" SELECT count(*) FROM two_teacher_class WHERE `NAME`= :name ");
        sql.append(" select count(*) from two_teacher_class ttc ,two_teacher_brench ttb where ttc.CLASS_ID = ttb.CLASS_ID and ttb.BRENCH_ID in ( :blBrenchId ) ");
        sql.append(" and ttc.`NAME`= :name ");
        map.put("name", teacherClass.getName());
        map.put("blBrenchId", blBrenchId);
        if(teacherClass.getClassId()>0){
            sql.append(" and ttc.CLASS_ID <> :classId ");
            map.put("classId", teacherClass.getClassId());
        }
        int count = super.findCountSql(sql.toString(), map);
        if (count>0){
            return true;
        }else {
            return false;
        }
    }
}
