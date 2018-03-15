package com.eduboss.openapi.dao.impl;

import com.eduboss.dao.impl.GenericDaoImpl;
import com.eduboss.domain.StudentSchool;
import com.eduboss.openapi.dao.StudentSchoolDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/22.
 */
@Repository(value = "StudentSchoolDaoRpc")
public class StudentSchoolDaoImpl extends GenericDaoImpl<StudentSchool, String> implements StudentSchoolDao {
    /**
     * 根据位置信息获取学校列表
     *
     * @param provinceID 省份ID
     * @param cityID     城市ID
     * @return 返回学校列表
     */
    @Override
    public List<StudentSchool> listSchoolByRegion(String provinceID, String cityID) {
        Map<String, Object> params = new HashMap();
        StringBuffer sql=new StringBuffer();
        sql.append(" select * from student_school ");
        if(cityID != null && StringUtils.isNotBlank(cityID)){
            sql.append(" where city_id= :blCampus ");
            params.put("blCampus", cityID);
        }else{
            sql.append(" where 1=2 ");
        }
        List<StudentSchool> list=this.findBySql(sql.toString(),params);
        return list;
    }
}
