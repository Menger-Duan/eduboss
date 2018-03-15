package com.eduboss.openapi.dao;

import com.eduboss.dao.GenericDAO;
import com.eduboss.domain.DataDict;

import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */
public interface DataDictDao extends GenericDAO<DataDict, String> {
    /**
     * 获取全部年级信息
     *
     * @return 返回年级列表
     */
    List<DataDict> listAllGrade();
}
