package com.eduboss.dao.impl;

import com.eduboss.dao.ObjectHashCodeDao;
import com.eduboss.domain.ObjectHashCode;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class ObjectHashCodeDaoImpl extends GenericDaoImpl<ObjectHashCode, String> implements ObjectHashCodeDao {
    @Override
    public ObjectHashCode findByHashCode(int hashCode) {
        String hql = "from ObjectHashCode where hashCode=:hashCode ";
        Map<String, Object> params = new HashMap<>();
        params.put("hashCode", hashCode);
        List<ObjectHashCode> list = this.findAllByHQL(hql, params);
        if (list.size()==1){
            return list.get(0);
        }else {
            return null;
        }
    }
}
