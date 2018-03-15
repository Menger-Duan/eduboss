package com.pad.service.impl;

import com.eduboss.dao.GenericDAO;
import com.eduboss.dao.impl.GenericDaoImpl;
import com.pad.service.IService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/30.
 */
public class ServiceImpl<M extends GenericDaoImpl<T, String>, T> implements IService<T> {
    private static final Logger logger = Logger.getLogger(ServiceImpl.class);
    @Autowired
    private M dao;

    @Override
    public void insert(T entity) {
        this.dao.save(entity);
    }
}
