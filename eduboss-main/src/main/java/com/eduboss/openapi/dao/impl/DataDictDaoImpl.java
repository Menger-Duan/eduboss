package com.eduboss.openapi.dao.impl;

import com.eduboss.dao.impl.GenericDaoImpl;
import com.eduboss.domain.DataDict;
import com.eduboss.openapi.dao.DataDictDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/23.
 */
@Repository(value = "DataDictDaoRpc")
public class DataDictDaoImpl extends GenericDaoImpl<DataDict, String> implements DataDictDao {

    /**
     * 获取全部年级信息
     *
     * @return 返回年级列表
     */
    @Override
    public List<DataDict> listAllGrade() {
        StringBuffer hql = new StringBuffer();
        Map<String, Object> params = new HashMap<>();
        hql.append("from DataDict where category = 'STUDENT_GRADE' ");
        List<DataDict> allGrade = this.findAllByHQL(hql.toString(), params);
        return allGrade;
    }

}
