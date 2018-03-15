package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.WorkFlowConfigDao;
import com.eduboss.domain.WorkFlowConfig;

@Repository
public class WorkFlowConfigDaoImpl extends GenericDaoImpl<WorkFlowConfig, String> implements WorkFlowConfigDao{

	 /**
     * 获取下一个流程节点
     *
     * @param workFlowConfig 当前节点
     * @return 该类型流程的下一个节点
     */
    @Override
    public WorkFlowConfig getNextStep(WorkFlowConfig workFlowConfig) {
    	Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(" from WorkFlowConfig c where c.type = :type ");
        sql.append(" and order > :order ");
        sql.append(" order by order asc limit 1");
        params.put("type", workFlowConfig.getType());
        params.put("order", workFlowConfig.getOrder());
        List<WorkFlowConfig> list = findAllByHQL(sql.toString(), params);
        if(list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * 获取上一个流程节点
     *
     * @param workFlowConfig 当前节点
     * @return 该类型流程的上一个节点
     */
    @Override
    public WorkFlowConfig getPrevStep(WorkFlowConfig workFlowConfig) {
    	Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(" from WorkFlowConfig c where c.type = :type ");
        sql.append(" and order < :order ");
        sql.append(" order by order desc limit 1");
        params.put("type", workFlowConfig.getType());
        params.put("order", workFlowConfig.getOrder());
        List<WorkFlowConfig> list = findAllByHQL(sql.toString(), params);
        if(list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }

    /**
     * 获取第一个流程节点
     *
     * @param type 流程类型
     * @return 该类型流程的第一个节点
     */
    @Override
    public WorkFlowConfig getFirstStep(String type) {
    	Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer sql = new StringBuffer(" from WorkFlowConfig c where c.type = :type");
        sql.append(" order by order asc limit 1");
        params.put("type", type);
        List<WorkFlowConfig> list = findAllByHQL(sql.toString(), params);
        if(list.size() == 0){
            return null;
        }else{
            return list.get(0);
        }
    }
}
